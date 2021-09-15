package com.bzboss.app.restapi

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatDialog
import com.bzboss.app.R
import com.bzboss.app.activity.ActivityBase
import com.bzboss.app.activity.LoginActivity
import com.bzboss.app.custom.PrefUtils
import com.bzboss.app.custom.Toast
import com.bzboss.app.custom.Utils.dismissDialog
import com.bzboss.app.custom.getProgressDialog
import com.iqamahtimes.app.restapi.ApiResponseInterface
import com.iqamahtimes.app.restapi.ApiResponseManager
import com.iqamahtimes.app.restapi.ErrorUtils


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.ParseException
import java.util.concurrent.TimeoutException

@SuppressLint("ParcelCreator")
class ApiRequest<T>(
    private val activity: Activity,
    objectType: T,
    private val TYPE: Int,
    private val isShowProgressDialog: Boolean,
    private val apiResponseInterface: ApiResponseInterface
) : Callback<T>, Parcelable {

    private val TAG = javaClass.simpleName
    private var mProgressDialog: AppCompatDialog? = null
    private var retryCount = 0
    private var call: Call<T>? = null

    protected lateinit var objSharedPref: PrefUtils

    init {

        showProgress()
        call = objectType as Call<T>
        call!!.enqueue(this)
    }

    private fun showProgress() {
        if (isShowProgressDialog) {
            if (mProgressDialog == null)
                mProgressDialog = getProgressDialog(ActivityBase.globalActivity)
        }
    }

    private fun dismissProgress() {
        if (isShowProgressDialog) {
            dismissDialog(activity, mProgressDialog!!)
        }
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        dismissProgress()
        Log.e("xchxbcxbcxmnccccccc", "RESPONSE = " + "xchxbcxbcxmnccccccc")
        Log.e("Api Request", "REQUEST_URL = " + call.request().url().toString())
        Log.e("onResponse", "RESPONSE_CODE = " + response.code().toString())
        Log.e("REQUEST", "RESPONSE = " + response.body())

        if (response.isSuccessful) {
            Log.e("REQUEST", "RESPONSE = " + response.body())
            apiResponseInterface.getApiResponse(ApiResponseManager(response.body(), TYPE))
        } else {
            //Toast(response.message(), true, activity)

            val error = ErrorUtils.parseError(response)
            Toast(error.message(), true, activity)
            /*Log.e("error.status()==>", error.status().toString())
            Log.e("error.message()==>", error.message())
            Log.e("status_code==>", error.status_code.toString())
*/
            Log.e("error.status()==>", error.status().toString())
            if (error.status() == 401 || error.status() == 405) {

                // PrefUtils(activity).logout()
                /*val language = PrefUtils(activity).getString(
                    AppController.getContext()!!.getString(
                        R.string.key_lang_test
                    )
                )
                PrefUtils(activity).putBoolean(
                    AppController.getContext()!!.getString(R.string.key_is_profile_updated), false
                )
                PrefUtils(activity).putBoolean(
                    AppController.getContext()!!.getString(R.string.key_intro_visited), true
                )
                PrefUtils(activity).putString(
                    AppController.getContext()!!.getString(R.string.key_lang_test), language!!
                )
                PrefUtils(activity).putBoolean(
                    AppController.getContext()!!.getString(R.string.key_is_login), false
                )*/

                PrefUtils(activity).putBoolean(
                    activity.getString(R.string.is_login), false
                )
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                activity.startActivity(intent)
                activity.finishAffinity()
            }


            /*try {
                val error = ErrorUtils.parseError(response)

                Toast(error.message(), true, activity)

                if (error.status() == 401) {
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    activity.startActivity(intent)
                    activity.finishAffinity()
                }

            } catch (e: Exception) {
             //   printDebugLogs("REQUEST", "RESPONSE EXCEPTION ${e.printStackTrace()}")
            }*/
        }
    }

    override fun onFailure(call: Call<T>, error: Throwable) {

        // printDebugLogs("onFailure", "REQUEST_URL = " + call.request().url().toString())
        // printDebugLogs("onFailure", "ERROR" + error.printStackTrace())

        if (retryCount++ < TOTAL_RETRIES) {
            //    printDebugLogs("REQUEST", "RETRYING $retryCount OUT OF $TOTAL_RETRIES)")
            retry()
            return
        }

        dismissProgress()
        when (error) {
            is NetworkErrorException -> Toast(
                activity.resources.getString(R.string.toast_time_out),
                true,
                activity
            )
            is TimeoutException -> Toast(
                activity.resources.getString(R.string.toast_time_out),
                false,
                activity
            )
            is SocketTimeoutException -> Toast(
                activity.resources.getString(R.string.toast_try_after_sometimes),
                false,
                activity
            )
            is ParseException -> Toast(
                activity.resources.getString(R.string.toast_something_wrong),
                false,
                activity
            )
        }
    }

    private fun retry() {
        call!!.clone().enqueue(this)
    }

    companion object {
        private val TAG = "ApiRequest"
        private val TOTAL_RETRIES = 5
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(TYPE)
        parcel.writeByte(if (isShowProgressDialog) 1 else 0)
        parcel.writeInt(retryCount)
    }

    override fun describeContents(): Int {
        return 0
    }
}