package com.bzboss.app.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bzboss.app.R
import com.bzboss.app.custom.PrefUtils
import com.bzboss.app.custom.getProgressDialog
import com.google.android.material.snackbar.Snackbar
import java.util.*


abstract class BaseFragment : Fragment() {

    val TAG = javaClass.simpleName
    protected lateinit var objSharedPref: PrefUtils

    private var mProgressDialog: AppCompatDialog? = null


    var timeZoneSet: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        objSharedPref = PrefUtils(requireActivity() as FragmentActivity)
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Log.e(TAG, "onCreateView triggered...")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("HardwareIds")
    fun getDeviceToken(): String {
        //var deviceToken: String
        val android_id = Settings.Secure.getString(
            requireActivity().contentResolver,
            Settings.Secure.ANDROID_ID
        );
        Log.e("TAG", "Get unique device token==> $android_id")
        return android_id
    }
    open fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Log.e(TAG, "onActivityCreated triggered...")

    }



    override fun onStart() {
        super.onStart()

        // Log.e(TAG, "onStart triggered...")
    }

    override fun onResume() {
        super.onResume()

        // Log.e(TAG, "onResume triggered...")
    }

    override fun onPause() {
        super.onPause()

        // Log.e(TAG, "onPause triggered...")
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Log.e(TAG, "onDestroyView triggered...")
    }

    override fun onDestroy() {
        super.onDestroy()

        // Log.e(TAG, "onDestroy triggered...")
    }

    override fun onDetach() {
        super.onDetach()

        // Log.e(TAG, "onDetach triggered...")
    }

    fun showProgress() {
        if (mProgressDialog == null)
            mProgressDialog = getProgressDialog(requireActivity())
    }

    /*fun dismissProgress() {
        if (mProgressDialog != null) {
            dismissProgressDialog(mProgressDialog!!)
        }
    }*/

    protected fun showSnackBar(view: View, message: String, action: ACTIONSNACKBAR) {

        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(Color.DKGRAY)
        val tv = snackBar.view.findViewById(R.id.snackbar_text) as AppCompatTextView
        tv.maxLines = 5
        tv.setTextColor(Color.WHITE)

        val snackBaraction = snackBar.view.findViewById(R.id.snackbar_action) as AppCompatButton

        snackBar.setAction(action.actionMessage) {
            when (action.actionMessage) {
                ACTIONSNACKBAR.DISMISS.actionMessage -> {
                    snackBar.dismiss()
                }

                ACTIONSNACKBAR.FINISH_ACTIVITY.actionMessage -> {
                    requireActivity().finish()
                }
            }
        }
        snackBar.setActionTextColor(Color.WHITE)
        snackBar.show()
    }

    protected enum class ACTIONSNACKBAR(val actionMessage: String) {
        DISMISS("Dismiss"), FINISH_ACTIVITY("Done"), NONE(""),
    }

    protected fun showMessage(message: String) {
        Toast.makeText(activity as Context, message, Toast.LENGTH_LONG).show()
    }

   /* fun logout() {
        objSharedPref.logout()
        objSharedPref.putBoolean(resources.getString(R.string.key_is_login), false)
        startActivity(
            Intent(requireActivity(), CheckEmailAccountActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        requireActivity().finishAffinity()
    }*/


    /* fun getUserModel(): LoginResponse? {
         return Gson().fromJson(
             objSharedPref.getString(getString(R.string.user_response))!!,
             LoginResponse::class.java
         )
     }*/


}
