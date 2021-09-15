package com.bzboss.app.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bzboss.app.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit


class SplashActivity : ActivityBase() {
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private val screenTimeOut = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        redirectDashBord()
    }

    fun redirectDashBord() {

        mRunnable = Runnable {
            run {
                if (objSharedPref.getBoolean(getString(R.string.is_login))) {
                    val getStartedIntent =
                        Intent(this@SplashActivity, DashbordActivity::class.java)
                    getStartedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    getStartedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    getStartedIntent.putExtra("user_response",objSharedPref.getString(getString(R.string.user_response)))
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    startActivity(getStartedIntent)
                    finish()
                } else {
                    val getStartedIntent =
                        Intent(this@SplashActivity, LoginSignHomeActivity::class.java)
                    getStartedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    getStartedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    startActivity(getStartedIntent)
                    finish()
                }

            }
        }
        mHandler = Handler(Looper.getMainLooper())
        mHandler.postDelayed(mRunnable, screenTimeOut)
    }

}