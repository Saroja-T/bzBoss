package com.bzboss.app.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.bzboss.app.R
import com.bzboss.app.custom.isOnline
import com.bzboss.app.restapi.ApiInitialize
import com.bzboss.app.restapi.ApiRequest
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.iqamahtimes.app.custom.*
import com.iqamahtimes.app.restapi.ApiResponseInterface
import com.iqamahtimes.app.restapi.ApiResponseManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

class RegisterActivity : ActivityBase(), AdapterView.OnItemSelectedListener, ApiResponseInterface {
    private var accessLevelList: ArrayList<String>? = null
    private var accesLevelName: String = ""
    private var mAuth: FirebaseAuth? = null
    private var verifyCode: String = ""
    private var verificationId: String = ""

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        accessLevelList = ArrayList()
        accessLevelList!!.add("Manager")
        accessLevelList!!.add("Staff")
        accessLevelList!!.add("Other")

        setSpinner(sp_access_level, accessLevelList!!)
        btn_register.setOnClickListener {
            registerApi()
        }

        tv_term_and_condition.setOnClickListener {
            val httpIntent = Intent(Intent.ACTION_VIEW)
            httpIntent.data = Uri.parse(TERM_AND_CONDITION)
            startActivity(httpIntent)
        }
        tv_second.setOnClickListener {
            val httpIntent = Intent(Intent.ACTION_VIEW)
            httpIntent.data = Uri.parse(TERM_AND_CONDITION)
            startActivity(httpIntent)
        }
    }

    private fun setSpinner(spinner: Spinner, list: ArrayList<String>) {

        spinner.onItemSelectedListener = this
        val adapter = object : ArrayAdapter<String>(
            this@RegisterActivity,
            R.layout.item_detail__second_row,
            list
        ) {
            /* override fun isEnabled(position: Int): Boolean {
                 return position != 0
             }*/


            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup?
            ): View {
                val view = super.getDropDownView(position, convertView, parent!!)
                val tv = view as TextView
                /* if (position == 0) {
                     tv.setTextColor(Color.GRAY)
                 } else {
                     tv.setTextColor(Color.GRAY)
                 }*/
                return view
            }
        }
        adapter.setDropDownViewResource(R.layout.item_detail__second_row)
        spinner.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun registerApi() {
        Log.d(TAG, "onCreate: "+objSharedPref.getString("FCM_TOKEN")!!, )

        if (isOnline(this@RegisterActivity)) {
            ApiRequest(
                this,
                ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userRegisterFunction(
                    encrypt(ed_mobile_number.text.toString()),
                    encrypt(tv_login_email.text.toString()),
                    encrypt(ed_last_name.text.toString()),
                    encrypt(accesLevelName),
                    encrypt(DEVICE_TYPE),
                    objSharedPref.getString("FCM_TOKEN")!!,
                    encrypt(timeZoneSet),
                    "check"
                ),
                REGISTER, true, this
            )
        } else {
            showSnackBar(
                ll_register_parent_view,
                getString(R.string.no_internet),
                ACTIONSNACKBAR.DISMISS
            )
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            sp_access_level -> {
                accesLevelName = accessLevelList!![position]
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
      //  TODO("Not yet implemented")
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

                        /* val getStartedIntent =
                             Intent(this@RegisterActivity, VerifyOtpActivity::class.java)
                         overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                         startActivity(getStartedIntent)
                         finish()*/
                        val phone = "+91" + ed_mobile_number.text.toString()
                        sendVerificationCode(phone)
                        //showProgress()
                    }
                    ERROR_CODE -> {
                        val data = response.getJSONObject("data")
                        showSnackBar(
                            ll_register_parent,
                            data.optString("all"),
                            ACTIONSNACKBAR.DISMISS
                        )
                    }
                }
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
                dismissProgress()
                // customDismissDialog(this@LoginActivity, getProgressDialog(this@LoginActivity))
                //when we recieve the OTP it contains a unique id wich we are storing in our string which we have already created.
                verificationId = s

                val getStartedIntent = Intent(this@RegisterActivity, VerifyOtpActivity::class.java)
                getStartedIntent.putExtra("verificationId", verificationId)
                getStartedIntent.putExtra("phone_number", ed_mobile_number!!.text.toString())
                getStartedIntent.putExtra("otp_type","Register")
                getStartedIntent.putExtra("first_name", tv_login_email!!.text.toString())
                getStartedIntent.putExtra("last_name",ed_last_name.text.toString())
                getStartedIntent.putExtra("accesLevelName",accesLevelName)
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
                Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
}