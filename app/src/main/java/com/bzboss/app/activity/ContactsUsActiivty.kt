package com.bzboss.app.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.bzboss.app.R
import com.bzboss.app.custom.Toast
import com.bzboss.app.custom.Validator
import com.bzboss.app.custom.isNetWork
import com.bzboss.app.restapi.ApiInitialize
import com.bzboss.app.restapi.ApiRequest
import com.iqamahtimes.app.custom.ERROR_CODE
import com.iqamahtimes.app.custom.STORE_CONTACTS_DATA
import com.iqamahtimes.app.custom.SUCCESS_CODE

import com.iqamahtimes.app.restapi.ApiResponseInterface
import com.iqamahtimes.app.restapi.ApiResponseManager
import kotlinx.android.synthetic.main.activity_contacts_us_actiivty.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.ResponseBody
import org.json.JSONObject


class ContactsUsActiivty : ActivityBase(), AdapterView.OnItemSelectedListener,
    ApiResponseInterface {
    private var contactsUsDetailList: ArrayList<String>? = null
    private var contactsUsDetailName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_us_actiivty)
        contactsUsDetailList = ArrayList()
        contactsUsDetailList!!.add("Business enquiry")
        contactsUsDetailList!!.add("Support request")
        contactsUsDetailList!!.add("Terms & Conditions / Privacy policy")

        setSpinner(sp_enquiry_contacts, contactsUsDetailList!!)


        iv_back_our_contact.setOnClickListener {
            finish()
        }


        val name = "<font color=#153E5C><b>Name</b></font> <font color=#6E6E6E> (required)</font>"
        name_contact.text = HtmlCompat.fromHtml(name, HtmlCompat.FROM_HTML_MODE_COMPACT)

        val email = "<font color=#153E5C><b>Email</b></font> <font color=#6E6E6E> (required)</font>"
        email_contact.text = HtmlCompat.fromHtml(email, HtmlCompat.FROM_HTML_MODE_COMPACT)

        val phone =
            "<font color=#153E5C><b>Phone number</b></font> <font color=#6E6E6E> (required)</font>"
        phone_contact.text = HtmlCompat.fromHtml(phone, HtmlCompat.FROM_HTML_MODE_COMPACT)


        val enquiry =
            "<font color=#153E5C><b>Category</b></font> <font color=#6E6E6E> (required)</font>"
        enquiry_contact.text = HtmlCompat.fromHtml(enquiry, HtmlCompat.FROM_HTML_MODE_COMPACT)


        val enterDetail =
            "<font color=#153E5C><b>Message</b></font> <font color=#6E6E6E> (required)</font>"
        enterDetail_contact.text =
            HtmlCompat.fromHtml(enterDetail, HtmlCompat.FROM_HTML_MODE_COMPACT)



        ed_name_contacts
        ed_email_contacts
        ed_enterDetail_contacts


        btn_send.setOnClickListener {
            storeContactsData()
           /* if (checkValidation()) {
                // storeContactsData()
                // Toast("Done Sucess", true, this@ContactsUsActiivty)
            }*/

        }
    }

    fun storeContactsData() {
        if (isNetWork(this@ContactsUsActiivty)) {
            ApiRequest(
                this@ContactsUsActiivty,
                ApiInitialize.initialize(ApiInitialize.LOCAL_URL).contactUs(
                    "Bearer " + getUserModel()!!.data.token,
                    encrypt(ed_name_contacts.text.toString()),
                    encrypt(ed_phone_contacts.text.toString()),
                    encrypt(ed_email_contacts.text.toString()),
                    encrypt(contactsUsDetailName),
                    encrypt(ed_enterDetail_contacts.text.toString())
                ),
                STORE_CONTACTS_DATA, true, this
            )
        } else {
            Toast(
                resources.getString(R.string.internet_not_available),
                true, this@ContactsUsActiivty
            )
        }
    }

    fun checkValidation(): Boolean {
        if (TextUtils.isEmpty(ed_name_contacts.text.toString())) {
            showSnackBar(nn_scroll, "Enter name", ACTIONSNACKBAR.DISMISS)
            return false
        } else if (TextUtils.isEmpty(ed_email_contacts.text.toString())) {
            showSnackBar(nn_scroll, "Enter email", ACTIONSNACKBAR.DISMISS)
            return false
        } else if (!Validator().validateEmail(ed_email_contacts.text.toString())
        ) {
            showSnackBar(nn_scroll, "Enter proper  email", ACTIONSNACKBAR.DISMISS)
            return false
        } else if (TextUtils.isEmpty(ed_enterDetail_contacts.text.toString())) {
            showSnackBar(nn_scroll, "Enter detail", ACTIONSNACKBAR.DISMISS)
            return false
        }
        return true

    }

    private fun setSpinner(spinner: Spinner, list: java.util.ArrayList<String>) {

        spinner.onItemSelectedListener = this
        val adapter = object : ArrayAdapter<String>(
            this@ContactsUsActiivty,
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            sp_enquiry_contacts -> {
                contactsUsDetailName = contactsUsDetailList!![position]
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            STORE_CONTACTS_DATA -> {
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
                        finish()
                    }
                    ERROR_CODE -> {
                        val data = response.getJSONObject("data")
                        showSnackBar(
                            ll_contact,
                            data.optString("all"),
                            ACTIONSNACKBAR.DISMISS
                        )
                    }
                }
            }
        }
    }
}