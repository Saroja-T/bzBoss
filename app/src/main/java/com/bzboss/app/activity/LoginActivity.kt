package com.bzboss.app.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialog
import com.bzboss.app.R
import com.bzboss.app.custom.customDismissDialog
import com.bzboss.app.custom.getProgressDialog
import com.bzboss.app.custom.isOnline
import com.bzboss.app.restapi.ApiInitialize
import com.bzboss.app.restapi.ApiRequest
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.iqamahtimes.app.custom.ERROR_CODE
import com.iqamahtimes.app.custom.LOGIN
import com.iqamahtimes.app.custom.SUCCESS_CODE
import com.iqamahtimes.app.custom.TERM_AND_CONDITION
import com.iqamahtimes.app.restapi.ApiResponseInterface
import com.iqamahtimes.app.restapi.ApiResponseManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class LoginActivity : ActivityBase(), ApiResponseInterface {
    private var mAuth: FirebaseAuth? = null
    private var verifyCode: String = ""
    private var verificationId: String = ""
    private var mProgressDialog: AppCompatDialog? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        btn_sign_in.setOnClickListener {

            loginApi()

            /*if (TextUtils.isEmpty(ed_phone_number!!.text.toString())) {
                //when mobile number text field is empty displaying a toast message.
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter a valid phone number.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //if the text field is not empty we are calling our send OTP method for gettig OTP from Firebase.
                val phone = "+91" + ed_phone_number!!.text.toString()
                sendVerificationCode(phone)
                showProgress()
            }*/

            //  sendVerificationCode(ed_phone_number.text.toString())

        }

        txtSignUp.setOnClickListener {
            val httpIntent = Intent(Intent.ACTION_VIEW)
            httpIntent.data = Uri.parse(TERM_AND_CONDITION)
            startActivity(httpIntent)
        }
    }

    private fun sendVerificationCode(number: String) {
        //this method is used for getting OTP on user phone number.

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        /*PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,  //first parameter is user's mobile number
            60,  //second parameter is time limit for OTP verification which is 60 seconds in our case.
            TimeUnit.SECONDS,  // third parameter is for initializing units for time period which is in seconds in our case.
            TaskExecutors.MAIN_THREAD,  //this task will be excuted on Main thread.
            mCallBack //we are calling callback method when we recieve OTP for auto verification of user.
        )*/
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun loginApi() {
        if (isOnline(this@LoginActivity)) {
            ApiRequest(
                this,
                ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userLoginFunction(
                    encrypt(ed_phone_number.text.toString()),
                    encrypt(DEVICE_TYPE),
                    objSharedPref.getString("FCM_TOKEN").toString(),
                    encrypt(timeZoneSet)
                ),
                LOGIN, true, this
            )
        } else {
            showSnackBar(
                ll_parent_view,
                getString(R.string.no_internet),
                ACTIONSNACKBAR.DISMISS
            )
        }
    }

    private val   //initializing our callbacks for on verification callback method.
            mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            //below method is used when OTP is sent from Firebase
            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                dismissProgress()
                // customDismissDialog(this@LoginActivity, getProgressDialog(this@LoginActivity))
                //when we recieve the OTP it contains a unique id wich we are storing in our string which we have already created.
                verificationId = s

                val getStartedIntent = Intent(this@LoginActivity, VerifyOtpActivity::class.java)
                getStartedIntent.putExtra("verificationId", verificationId)
                getStartedIntent.putExtra("phone_number", ed_phone_number!!.text.toString())
                getStartedIntent.putExtra("otp_type","Login")
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                startActivity(getStartedIntent)
                finish()
            }

            //this method is called when user recieve OTP from Firebase.
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                //below line is used for getting OTP code which is sent in phone auth credentials.
                val code = phoneAuthCredential.smsCode
                //checking if the code is null or not.
                if (code != null) {
                    //if the code is not null then we are setting that code to our OTP edittext field.
                    //   edtOTP!!.setText(code)
                    //after setting this code to OTP edittext field we are calling our verifycode method.
                    //  verifyCode(code)
                    verifyCode = code
                }
            }

            //thid method is called when firebase doesnot sends our OTP code due to any error or issue.
            override fun onVerificationFailed(e: FirebaseException) {
                //displaying error message with firebase exception.
                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            /* LOGIN -> {
             }*/
            LOGIN -> {
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                Log.e(TAG, "response LOGIN:-$responseValue")
                val response = JSONObject(responseValue)
                when (response.optInt("status_code")) {
                    SUCCESS_CODE -> {
                        objSharedPref.putString(getString(R.string.user_response), responseValue)

                        val phone = "+91" + ed_phone_number!!.text.toString()
                        sendVerificationCode(phone)
                        showProgress()

                    }
                    ERROR_CODE -> {
                        val data = response.getJSONObject("data")
                        showSnackBar(
                            ll_parent_view,
                            data.optString("all"),
                            ACTIONSNACKBAR.DISMISS
                        )
                    }
                }
            }
        }
    }
}