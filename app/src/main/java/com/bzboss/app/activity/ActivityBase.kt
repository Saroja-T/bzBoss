package com.bzboss.app.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bzboss.app.R
import com.bzboss.app.custom.*
import com.bzboss.app.encryption.CryptLib2
import com.bzboss.app.model.UserModel
import com.facebook.drawee.backends.pipeline.Fresco

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson

import java.util.*


@Suppress("DEPRECATION")
abstract class
ActivityBase : AppCompatActivity() {


    var timeZoneSet: String = ""
    protected lateinit var mActivity: Activity

    //  open lateinit var objSharedPref: PrefUtils
    open lateinit var objSharedPref: PrefUtils
    val TAG = javaClass.simpleName
    var auth_token = ""
    var user_id = ""
    private var mProgressDialog: AppCompatDialog? = null

    /*Encryption variables*/
    private var cryptLib: CryptLib2? = null
    private val key = "12345678901234561234567890123456"
    private val ENCRYPTION_IV = "12345678901234561234567890123456"

    var DEVICE_TYPE: String = "Android"

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var globalActivity: Activity
    }

    fun initBaseComponants(activityBase: ActivityBase) {
        mActivity = this@ActivityBase
        globalActivity = this@ActivityBase
        //generateFcmToekn()
        cryptLib = CryptLib2()
        getFirebaseMessagingToken()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this@ActivityBase);
        objSharedPref = PrefUtils(this@ActivityBase)
        initBaseComponants(this)
        initBaseData()
        // objSharedPref.putString("FCM_TOKEN","dhfsdhfhsdfsdj")!!

        try {
            val tz: TimeZone = TimeZone.getDefault()
            Log.e(
                "TAG", "TIME ZONE " + "TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT)
                    .toString() + " Timezon id :: " + tz.id
            )
            System.out.println(
                "TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT)
                    .toString() + " Timezon id :: " + tz.id
            )
            timeZoneSet = tz.id
        } catch (E: Exception) {

        }
    }

    fun appClose() {
        AlertDialog.Builder(this@ActivityBase)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit this app ?") // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, which ->
                    finishAffinity()
                    dialog.dismiss()
                }) // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, which ->
                    // Continue with delete operation
                    dialog.dismiss()
                })

            .show()
    }

    open fun getFirebaseMessagingToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    //Could not get FirebaseMessagingToken
                    return@addOnCompleteListener
                }
                if (null != task.result) {
                    //Got FirebaseMessagingToken
                    val token = task.result
                    Log.e(TAG, "FCM_TOKEN token===> $token")
                    Log.e(TAG, "FCM_TOKEN ===> $token")
                    objSharedPref.putString("FCM_TOKEN", token!!)
                    //   val firebaseMessagingToken: String = Objects.requireNonNull(task.result).toString()
                    //Use firebaseMessagingToken further
                }
            }
    }

    /*encrypt editext value*/
    fun encrypt(value: String): String {
        if (TextUtils.isEmpty(value)) {
            return value.replace('\"', ' ', ignoreCase = false)
        } else {
            val values = cryptLib!!.encrypt(value, key, ENCRYPTION_IV)
            Log.e(
                TAG,
                "original value == $value " +
                        "encrypted message ==> ${
                            values.trimEnd().encode()
                        }"
            )
            return values.trimEnd().encode()
        }
    }

    /*decrypt editext value*/
    fun decrypt(decrypted: String): String {
        val decrypted = cryptLib!!.decrypt(decrypted.decode(), key, ENCRYPTION_IV)
        Log.e(
            TAG,
            " decrypt message in android ===> $decrypted and original value is == $decrypted"
        )
        return decrypted
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setupUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        /* if (view !is EditText) {
             view.setOnTouchListener { v, event ->
                 hideSoftKeyboard(this@ActivityBase)
                 false
             }
         }*/

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        globalActivity = this@ActivityBase
    }

    @SuppressLint("HardwareIds")
    fun getDeviceToken(): String {
        //var deviceToken: String
        val android_id = Settings.Secure.getString(
            this@ActivityBase.contentResolver,
            Settings.Secure.ANDROID_ID
        );
        Log.e("TAG", "Get unique device token==> $android_id")
        return android_id
    }


    private fun initBaseData() {

        //auth_token = "Bearer " + objSharedPref.getString(resources.getString(R.string.key_token))
        // user_id = objSharedPref.getString(resources.getString(R.string.key_user_id))!!
        Log.e("auth_token", "auth_token = $auth_token")
        Log.e("user_id", "user_id = $user_id")

        try {

            Log.e(TAG,"Token is"+getUserModel()!!.data.token)
        } catch (E: Exception) {

        }

    }

    /*protected enum class ACTIONSNACKBAR(val actionMessage: String) {
        DISMISS(globalActivity.getString(R.string.dismiss)),
        FINISH_ACTIVITY(globalActivity.getString(R.string.done)),
        NONE(""),

    }*/
    fun showProgress() {
        if (mProgressDialog == null)
            mProgressDialog = getProgressDialog(this@ActivityBase)
    }

    fun dismissProgress() {
        if (mProgressDialog != null) {
            dismissProgressDialog(mProgressDialog!!)
        }
    }

    protected fun showMessage(message: String) {
        Toast.makeText(this@ActivityBase, message, Toast.LENGTH_LONG).show()
    }

    protected fun showSnackBar(view: View, message: String, action: ACTIONSNACKBAR) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(Color.DKGRAY)


        val tv = snackBar.view.findViewById(R.id.snackbar_text) as AppCompatTextView
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13f);
        tv.setTextColor(Color.WHITE)
        // tv.typeface = Typeface.createFromAsset(assets, getString(R.string.sans_regular))
        tv.maxLines = 25

        val snackBaraction = snackBar.view.findViewById(R.id.snackbar_action) as AppCompatButton
        //    snackBaraction.typeface = Typeface.createFromAsset(view.context.assets, getString(R.string.sans_regular))
        snackBaraction.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13f);


        snackBar.setAction(action.actionMessage) {
            when (action.actionMessage) {
                ACTIONSNACKBAR.DISMISS.actionMessage -> {
                    snackBar.dismiss()
                }

                ACTIONSNACKBAR.FINISH_ACTIVITY.actionMessage -> {
                    finish()
                }
            }
        }
        snackBar.setActionTextColor(Color.WHITE)
        snackBar.show()
    }

    protected fun setCheckBoxPadding(checkBox: AppCompatCheckBox) {
        val scale = resources.displayMetrics.density
        checkBox.setPadding(
            checkBox.paddingLeft + (6.0f * scale + 0.5f).toInt(),
            checkBox.paddingTop,
            checkBox.paddingRight,
            checkBox.paddingBottom
        )
    }


    /*protected fun getInitialNameTag(): String {
        return getUserModel()!!.data.name[0].toUpperCase().toString()
            .plus(getUserModel()!!.data.lastName!![0].toUpperCase().toString())
    }

    protected fun getUserName(): String? {
        return let { getUserModel()!!.data.name.plus(" ").plus(getUserModel()!!.data.lastName) }
    }*/

    /* protected fun getUserEmail(): String {
      //   return getUserModel()!!.data.email
     }*/

    /*   fun getUserModel(): UserModel? {
           val gson = Gson()
           return gson.fromJson(
               objSharedPref.getString(getString(R.string.user_response))!!,
               UserModel::class.java
           )
       }*/
    fun getUserModel(): UserModel? {
        val gson = Gson()
        return gson.fromJson(
            objSharedPref.getString(getString(R.string.user_response))!!, UserModel::class.java
        )
    }

    protected enum class ACTIONSNACKBAR(val actionMessage: String) {
        DISMISS(globalActivity.getString(R.string.dismiss)),
        FINISH_ACTIVITY(globalActivity.getString(R.string.done)),
        NONE(""),

    }

    protected fun goScreen(activityName: Activity) {
        startActivity(Intent(this@ActivityBase, activityName::class.java))
    }


    /**
     * Generate fcm Tocken
     */
    fun generateFcmToekn(): String {
        var device_token: String
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {
                override fun onComplete(task: Task<InstanceIdResult>) {
                    if (!task.isSuccessful) {
                        Log.e(TAG, "Fcm token  not generated.")
                        return
                    }
                    device_token = task.result?.token.toString()
                    Log.e(TAG, "DEVICE TOKEN ===> $device_token")
                    objSharedPref.putString("FCM_TOKEN", device_token)
                }
            })
        return objSharedPref.getString("FCM_TOKEN")!!
    }


    fun printDebugLogs(tagClass: AppCompatActivity, printValue: String) {
        /*  if (BuildConfig.DEBUG)*/
        Log.e(tagClass.localClassName, printValue)
    }
}