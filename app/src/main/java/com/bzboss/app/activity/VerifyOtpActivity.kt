package com.bzboss.app.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bzboss.app.R
import com.bzboss.app.custom.isOnline
import com.bzboss.app.restapi.ApiInitialize
import com.bzboss.app.restapi.ApiRequest
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.iqamahtimes.app.custom.ERROR_CODE
import com.iqamahtimes.app.custom.REGISTER
import com.iqamahtimes.app.custom.SUCCESS_CODE
import com.iqamahtimes.app.restapi.ApiResponseInterface
import com.iqamahtimes.app.restapi.ApiResponseManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_verify_otp.*
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class VerifyOtpActivity : ActivityBase(), ApiResponseInterface {
    private var mAuth: FirebaseAuth? = null
    var resendOtpFirstValue: String = ""
    var resendOtpSecondValue: String = ""
    var resendOtpTIme: String = ""
    var phone_number: String = ""
    private var verifyCode: String = ""
    private var verificationId: String = ""

    private var register_type: String = ""
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)
        mAuth = FirebaseAuth.getInstance()


        verificationId = intent.getStringExtra("verificationId").toString()
        phone_number = intent.getStringExtra("phone_number").toString()
        register_type = intent.getStringExtra("otp_type").toString()

        btn_otp_verify.setOnClickListener {
            /*  val getStartedIntent = Intent(this@VerifyOtpActivity, DashbordActivity::class.java)
              overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
              startActivity(getStartedIntent)
              finishAffinity()*/
            if (TextUtils.isEmpty(tv_sms_code_verify!!.text.toString())) {
                //if the OTP text field is empty display a message to user to enter OTP
                Toast.makeText(this@VerifyOtpActivity, "Please enter OTP", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //if OTP field is not empty calling method to verify the OTP.
                verifyCode(tv_sms_code_verify!!.text.toString())
            }
        }

        set_resend.setOnClickListener {
            if (TextUtils.isEmpty(phone_number)) {
                //when mobile number text field is empty displaying a toast message.
                Toast.makeText(
                    this@VerifyOtpActivity,
                    "Please enter a valid phone number.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //if the text field is not empty we are calling our send OTP method for gettig OTP from Firebase.
                val phone = "+91" + phone_number
                //val phone = "+91$phone_number"
                sendVerificationCode(phone)
                tv_timer_change.visibility = View.VISIBLE
                set_resend.visibility = View.INVISIBLE
                showProgress()

            }

        }


        val spannable = SpannableString("Passcode will expire in 60 sec.")
        spannable.setSpan(
            ContextCompat.getColor(this@VerifyOtpActivity, R.color.app_color),
            24, 26,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        resendOtpFirstValue = getColoredSpanned("Passcode will expire in ", "#818382")!!

        resendOtpSecondValue = getColoredSpanned(" sec.", "#818382")!!

        // var fg="<font color=" + R.color.black + ">" + "Passcode will expire in" + "</font> "+"<font color=" + R.color.background + ">" + "60" + "</font> "+"<font color=" + R.color.black + ">" + "sec" + "</font> "

        setPhoneCountDownTime()
    }

    private fun setPhoneCountDownTime() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resendOtpTIme = getColoredLong(millisUntilFinished / 1000, "#00A2E7").toString()
                //set_timer.text = "You can Resend in " + millisUntilFinished / 1000 + " seconds"
                //here you can have your logic to set text to edittext
                tv_timer_change.visibility = View.VISIBLE
                // set_resend.visibility=View.GONE

                tv_timer_change.text =
                    HtmlCompat.fromHtml(
                        resendOtpFirstValue + resendOtpTIme + resendOtpSecondValue,
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
            }

            override fun onFinish() {
                // set_timer.setText("Resend code")
                resendOtpTIme = getColoredSpanned("00", "#00A2E7")!!
                // tv_timer_change.visibility= View.GONE

                tv_timer_change.visibility = View.INVISIBLE
                set_resend.visibility = View.VISIBLE
                tv_timer_change.text =
                    HtmlCompat.fromHtml(
                        resendOtpFirstValue + resendOtpTIme + resendOtpSecondValue,
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun verifyCode(code: String) {

        //below line is used for getting getting credentials from our verification id and code.
        val credential = PhoneAuthProvider.getCredential(verificationId, code)

        Log.e("TAG", "Verify cation id is$verificationId")
        //after getting credential we are calling sign in method.
        signInWithCredential(credential)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun registerApi() {
        if (isOnline(this@VerifyOtpActivity)) {
            ApiRequest(
                this,
                ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userRegisterFunction(
                    encrypt(phone_number),
                    encrypt(intent.getStringExtra("first_name").toString()),
                    encrypt(intent.getStringExtra("last_name").toString()),
                    encrypt(intent.getStringExtra("accesLevelName").toString()),
                    encrypt(DEVICE_TYPE),
                    objSharedPref.getString("FCM_TOKEN")!!,
                    encrypt(timeZoneSet),
                    "register"
                ),
                REGISTER, true, this
            )
        } else {
            showSnackBar(
                ll_otp_view_parent,
                getString(R.string.no_internet),
                ACTIONSNACKBAR.DISMISS
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun signInWithCredential(credential: PhoneAuthCredential) {
        //inside this method we are checking if the code entered is correct or not.
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (register_type == "Register") {
                        registerApi()
                    } else {
                        objSharedPref.putBoolean(getString(R.string.is_login), true)
                        val getStartedIntent = Intent(this@VerifyOtpActivity, DashbordActivity::class.java)
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        getStartedIntent.putExtra("user_response",objSharedPref.getString(getString(R.string.user_response)))
                        startActivity(getStartedIntent)
                        finishAffinity()
                    }
                    //sdf


                    /*  //if the code is correct and the task is succesful we are sending our user to new activity.
                      val i = Intent(this@VerifyOtpActivity, SplashActivity::class.java)
                      startActivity(i)
                      finish()*/
                } else {
                    //if the code is not correct then we are displaying an error message to the user.
                    Toast.makeText(
                        this@VerifyOtpActivity,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
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

    private val   //initializing our callbacks for on verification callback method.
            mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            //below method is used when OTP is sent from Firebase
            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                // customDismissDialog(this@LoginActivity, getProgressDialog(this@LoginActivity))
                //when we recieve the OTP it contains a unique id wich we are storing in our string which we have already created.
                verificationId = s
                dismissProgress()
                setPhoneCountDownTime()
            }

            //this method is called when user recieve OTP from Firebase.
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                //below line is used for getting OTP code which is sent in phone auth credentials.
                val code = phoneAuthCredential.smsCode
                //checking if the code is null or not.
                if (code != null) {
                    verifyCode = code
                }
            }

            //thid method is called when firebase doesnot sends our OTP code due to any error or issue.
            override fun onVerificationFailed(e: FirebaseException) {
                //displaying error message with firebase exception.
                Toast.makeText(this@VerifyOtpActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    private fun getColoredLong(text: Long, color: String): String? {
        return "<font color=$color>$text</font>"
    }

    private fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            REGISTER -> {
                val model = apiResponseManager.response as ResponseBody

                val responseValue = model.string()
                Log.e(TAG, "response LOGIN:-$responseValue")
                val response = JSONObject(responseValue)
                when (response.optInt("status_code")) {
                    SUCCESS_CODE -> {
                        objSharedPref.putString(getString(R.string.user_response), responseValue)
                        objSharedPref.putBoolean(getString(R.string.is_login), true)
                        val getStartedIntent = Intent(this@VerifyOtpActivity, DashbordActivity::class.java)
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                        getStartedIntent.putExtra("user_response",objSharedPref.getString(getString(R.string.user_response)))
                        startActivity(getStartedIntent)
                        finishAffinity()

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