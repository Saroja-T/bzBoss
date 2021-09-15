package com.bzboss.app.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.app.adprogressbarlib.AdCircleProgress
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bzboss.app.R
import com.bzboss.app.custom.*
import com.bzboss.app.model.DashbordPrimisiDataModel
import com.bzboss.app.model.UserConfigData
import com.bzboss.app.restapi.ApiInitialize
import com.bzboss.app.restapi.ApiRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iqamahtimes.app.custom.*
import com.iqamahtimes.app.restapi.ApiResponseInterface
import com.iqamahtimes.app.restapi.ApiResponseManager
import io.ghyeok.stickyswitch.widget.StickySwitch
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_whatch_face_dash_bord.*
import kotlinx.android.synthetic.main.activity_whatch_face_dash_bord.view.*
import kotlinx.android.synthetic.main.my_custom_toolbar.*
/*import kotlinx.android.synthetic.main.graphical_closed_at.*
import kotlinx.android.synthetic.main.graphical_customer.*
import kotlinx.android.synthetic.main.graphical_first_customer.*
import kotlinx.android.synthetic.main.graphical_known_visitors.*
import kotlinx.android.synthetic.main.graphical_opened_at.*
import kotlinx.android.synthetic.main.graphical_staff.*
import kotlinx.android.synthetic.main.my_custom_toolbar.*
import kotlinx.android.synthetic.main.numerical_closed_at.*
import kotlinx.android.synthetic.main.numerical_customer.*
import kotlinx.android.synthetic.main.numerical_first_customer.*
import kotlinx.android.synthetic.main.numerical_known_visitors.*
import kotlinx.android.synthetic.main.numerical_open_at.*
import kotlinx.android.synthetic.main.numerical_staff.**/
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class WhatchFaceDashBordActivity : ActivityBase(), ApiResponseInterface,View.OnClickListener,
    NumberPicker.OnValueChangeListener {
    var datePickerDialog:DatePickerDialog? = null
    private var starDate: String = ""
    private var endDate: String = ""
    private var homePremisedata: DashbordPrimisiDataModel? = null
    private var userconfigdataModel: UserConfigData? = null
    private var dialog: Dialog? = null
    private var sideMenuSelectKey: String = "Home"
    private var toggleSetting: String = "graphic"
    private var targetCustomerCount: String = ""
    private var targetKnownCustomersCount: String = ""
    private var maxStaffCount: String = ""
    private var seletedDate: String = ""
    private var userID: String = ""
    private var premiseID: String = ""
    var mYear = 0
    var mMonth = 0
    var mDay = 0
    private var checkDialog: Boolean = false
    var isDataChanged = ""
    val arrayList: ArrayList<String> = ArrayList<String>()
    var tempSize = 0;
    var tempNumericalSize = 0;
    var isLayoutCreated = false
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whatch_face_dash_bord)

        userID = getUserModel()?.data?.userId.toString()
        premiseID = intent.getStringExtra("premiseId").toString()

        //addLayout()

        sticky_switch.onSelectedChangeListener = object : StickySwitch.OnSelectedChangeListener {
            override fun onSelectedChange(direction: StickySwitch.Direction, text: String) {
                Log.d(TAG, "Now Selected : " + direction.name + ", Current Text : " + text)
                isDataChanged = "toggle"
                if(direction.name=="LEFT"){
                    toggleSetting = "numeric"
                    ll_numerical.visibility = View.VISIBLE
                    ll_graphical.visibility = View.GONE
                    userConfigAPI("numerical")
                }else if(direction.name=="RIGHT"){
                    toggleSetting = "graphic"
                    ll_numerical.visibility = View.GONE
                    ll_graphical.visibility = View.VISIBLE
                    userConfigAPI("graphic")
                }
            }
        }

        tabViewClick()
        menu_item.visibility = View.INVISIBLE
        boxClick()
        //toolbarVisibleOrgone()


        Log.e("ghhgh", userID)
        //configurationSettings()

        /*val section1 = DonutSection(
                name = "section_1",
                color = Color.parseColor("#FB1D32"),
                amount = 1f
        )

        val section2 = DonutSection(
                name = "section_2",
                color = Color.parseColor("#FFB98E"),
                amount = 1f
        )*/

        /*donut_view.cap = 5f
        donut_view.submitData(listOf(section1, section2))*/

        tv_referesh_data.setOnClickListener {
            homePremiseDataGet(endDate)
        }

        //rb_graphical.setOnClickListener(this)
        //rb_numerical.setOnClickListener(this)

    }

    private fun addLayout(rowIndex: Int) {
        Log.d(TAG, "addLayout: ....")
        val a = LinearLayout(this)
        a.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(20, 20, 20, 0)
        a.layoutParams = layoutParams

        for(i in 0..1){
            Log.d(TAG, "addLayout: For ...." + i)
            if(tempSize <= arrayList.size-1){
                if(arrayList.get(tempSize)=="opened_at"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.graphical_opened_at,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }else if(arrayList.get(tempSize)=="first_customer"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.graphical_first_customer,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }else if(arrayList.get(tempSize)=="customer"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.graphical_customer,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                } else if(arrayList.get(tempSize)=="staff"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.graphical_staff,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }else if(arrayList.get(tempSize)=="closed_at"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.graphical_closed_at,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }else if(arrayList.get(tempSize)=="known_visitors"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.graphical_known_visitors,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }
            }
            tempSize++
        }
        ll_graphical.addView(a)
    }

    private fun configurationSettings1() {
        Log.e("CongurationSet", "CallingSetiing")
        Log.e("CongurationSet12", toggleSetting + "--hhhh")
        if(toggleSetting == "numeric"){
            sticky_switch.setDirection(StickySwitch.Direction.LEFT, false, false);
            ll_numerical.visibility = View.VISIBLE
            ll_graphical.visibility = View.GONE
            toggleSetting = "numeric"
        }else if(toggleSetting == "graphic"){
            sticky_switch.setDirection(StickySwitch.Direction.RIGHT, false, false);
            ll_graphical.visibility = View.VISIBLE
            ll_numerical.visibility = View.GONE
            toggleSetting = "graphic"
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        //homePremiseDataGet(endDate)
        sideMenuSelectKey = "home"

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun toolbarVisibleOrgone() {
        var goneVisible = intent.getStringExtra("mane_toolbar")
        if (goneVisible == "visible") {
            menu_item.visibility = View.VISIBLE
            logout.setOnClickListener {
                logout()
            }
            click_drawer_menu.setOnClickListener {
                setDrawerDialog()
            }
        } else {
            menu_item.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun setDrawerDialog() {
        dialog = Dialog(this@WhatchFaceDashBordActivity, R.style.MyDialogTheme)
        dialog!!.setContentView(R.layout.sidemenu_drawer_dialog)
/*        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)*/
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        val close = dialog!!.findViewById<ImageView>(R.id.ic_close_side_menu)

        val tv_side_menu_privacy_polivy =
            dialog!!.findViewById<MyCustomTextView>(R.id.tv_side_menu_privacy_polivy)
        val tv_side_menu_term_condition =
            dialog!!.findViewById<MyCustomTextView>(R.id.tv_side_menu_term_condition)
        val tv_side_menu_abouts =
            dialog!!.findViewById<MyCustomTextView>(R.id.tv_side_menu_abouts)
        val tv_side_menu_contacts =
            dialog!!.findViewById<MyCustomTextView>(R.id.tv_side_menu_contacts)

        val tv_side_menu_home =
            dialog!!.findViewById<MyCustomTextView>(R.id.tv_side_menu_home)
        val tv_side_menu_logout = dialog!!.findViewById<MyCustomTextView>(R.id.tv_side_menu_logout)
        tv_side_menu_logout.setOnClickListener {
            logout()
        }
        tv_side_menu_home.setOnClickListener {
            tv_side_menu_home.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.location_text_color
                    )
            )
            tv_side_menu_privacy_polivy.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_term_condition.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_contacts.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_abouts.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )

            sideMenuSelectKey = "home"
            checkDialog = false
            objSharedPref.putBoolean(CHECK_DIALOG_OPEN_CLOSE, checkDialog)
            dialog!!.dismiss()
        }
        tv_side_menu_privacy_polivy.setOnClickListener {

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(PRIVACY_POLIVY)
            startActivity(i)
            tv_side_menu_privacy_polivy.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.location_text_color
                    )
            )
            tv_side_menu_term_condition.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_abouts.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_contacts.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_home.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            sideMenuSelectKey = "privacy_policy"

            checkDialog = false
            objSharedPref.putBoolean(CHECK_DIALOG_OPEN_CLOSE, checkDialog)
            dialog!!.dismiss()
        }
        tv_side_menu_term_condition.setOnClickListener {


            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(TERM_AND_CONDITION)
            startActivity(i)

            tv_side_menu_term_condition.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.location_text_color
                    )
            )
            tv_side_menu_home.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_privacy_polivy.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_contacts.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_abouts.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            sideMenuSelectKey = "term_condition"
            checkDialog = false
            objSharedPref.putBoolean(CHECK_DIALOG_OPEN_CLOSE, checkDialog)
            dialog!!.dismiss()
        }
        tv_side_menu_contacts.setOnClickListener {
            startActivity(Intent(this@WhatchFaceDashBordActivity, ContactsUsActiivty::class.java))

            tv_side_menu_contacts.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.location_text_color
                    )
            )
            tv_side_menu_home.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_abouts.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_term_condition.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_privacy_polivy.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            sideMenuSelectKey = "contacts"
            checkDialog = false
            objSharedPref.putBoolean(CHECK_DIALOG_OPEN_CLOSE, checkDialog)
            dialog!!.dismiss()
        }

        /*privacy p[olicy*/
        tv_side_menu_abouts.setOnClickListener {
            //startActivity(Intent(requireActivity(), ContactsUsActiivty::class.java))

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(ABOUTS_US)
            startActivity(i)
            tv_side_menu_abouts.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.location_text_color
                    )
            )
            tv_side_menu_home.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_privacy_polivy.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_term_condition.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_side_menu_contacts.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            sideMenuSelectKey = "abouts"
            checkDialog = false
            objSharedPref.putBoolean(CHECK_DIALOG_OPEN_CLOSE, checkDialog)
            dialog!!.dismiss()
        }

        if (sideMenuSelectKey == "home") {
            tv_side_menu_home.performClick()
        } else if (sideMenuSelectKey == "privacy_policy") {
            tv_side_menu_privacy_polivy.performClick()
        } else if (sideMenuSelectKey == "term_condition") {
            tv_side_menu_term_condition.performClick()
        } else if (sideMenuSelectKey == "contacts") {
            tv_side_menu_contacts.performClick()
        } else if (sideMenuSelectKey == "abouts") {
            tv_side_menu_abouts.performClick()
        }


        //tv_side_menu_home.performClick()
        close.setOnClickListener {
            checkDialog = false
            objSharedPref.putBoolean(CHECK_DIALOG_OPEN_CLOSE, checkDialog)
            dialog!!.dismiss()
        }
        checkDialog = true
        objSharedPref.putBoolean(CHECK_DIALOG_OPEN_CLOSE, checkDialog)
        dialog!!.show()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun logout() {
        val builder =
            MaterialAlertDialogBuilder(this@WhatchFaceDashBordActivity)

        builder.setTitle("Logout")
        builder.setMessage("Are you sure want to logout?")

        builder.setPositiveButton("Yes") { _, _ ->
            objSharedPref.putBoolean(getString(R.string.is_login), false)
            val getStartedIntent =
                Intent(this@WhatchFaceDashBordActivity, LoginSignHomeActivity::class.java)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            startActivity(getStartedIntent)
            finishAffinity()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.show()

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun boxClick() {

        //TODO: Numerical OnClick
        if(tv_open_at!=null){
           tv_open_at.setOnClickListener {
            openAtOnClick()
            }
        }
        if(ll_first_custome!=null){
            ll_first_custome.setOnClickListener {
                firstCustomerOnClick()
            }
        }

        if(ll_custome_up_down!=null){
            ll_custome_up_down.setOnClickListener {
                customerOnClick()
            }
        }

        if(ll_staff!=null){
            ll_staff.setOnClickListener {
                staffOnClick()
            }
        }

        if(tv_closed_at!=null){
            tv_closed_at.setOnClickListener {
                // openAtOnClick()
                closedAtOnClick()
            }
        }
        if(ll_known_visitors!=null){
            ll_known_visitors.setOnClickListener {
                knownVistorrsOnClick()
            }
        }

        //TODO: Graphical OnClick
        if(ll_graphical_open_at!=null){
            ll_graphical_open_at.setOnClickListener {
                openAtOnClick()
            }
        }
        if(ll_graphical_first_customer!=null){
            ll_graphical_first_customer.setOnClickListener {
                firstCustomerOnClick()
            }
        }
        if(ll_graphical_customers!=null){
            ll_graphical_customers.setOnClickListener {
                customerOnClick()
            }
        }

        if(ll_graphical_staff!=null){
            ll_graphical_staff.setOnClickListener {
                staffOnClick()
            }
        }
        if(ll_graphical_open_at!=null){
            ll_graphical_open_at.setOnClickListener {
                openAtOnClick()
            }
        }
        if(ll_graphical_closed_at!=null){
            ll_graphical_closed_at.setOnClickListener {
                closedAtOnClick()
            }
        }
        if(ll_graphical_known_visitors!=null){
            ll_graphical_known_visitors.setOnClickListener {
                knownVistorrsOnClick()
            }
        }

        if(tvTargetCustomerCount!=null){
            tvTargetCustomerCount.setOnClickListener {
                showNumberSelect("Select Customer Max Count")
            }
        }

        if(tvTargetStaffCount!=null){
            tvTargetStaffCount.setOnClickListener {
                showStaffNumberSelect("Select Staff Target")
            }
        }
        if(tvTargetKnownVisitors!=null){
            tvTargetKnownVisitors.setOnClickListener {
                showKnownVisitorsNumberSelect("Select Known Visitors Max Count")
            }
        }



    }

    private fun closedAtOnClick() {
        Log.e("start_date", starDate)
        Log.e("endDate", endDate)
        Log.e("premiseId", premiseID)
        Log.e("status", homePremisedata!!.data.getcurrentstatus.status)
        Log.e("last_update", homePremisedata!!.data.getcurrentstatus.lastUpdated.toString())
        Log.e("home_type", "first_customer")
        Log.e("max_value", homePremisedata!!.data.numberOfStaffMax.toString())
        Log.e("min_vale", homePremisedata!!.data.numberOfStaffMin.toString())
        Log.e("premise_image", homePremisedata!!.data.getcurrentstatus.imageFilename)
        Log.e("image_type", homePremisedata!!.data.openedAtImage)
        Log.e("type_name", homePremisedata!!.data.closedAt)
        Log.e("type_name", tv_closedat_time.text.toString())
        if (tv_closedat_time.text.toString().trim() == "-") {
            Log.e("type_nameeee", tv_closedat_time.text.toString())
            Toast("No data available", true, this@WhatchFaceDashBordActivity)

        } else {
            startActivity(
                    Intent(
                            this@WhatchFaceDashBordActivity,
                            DisplyGraphActivity::class.java
                    ).putExtra("start_date", starDate)
                            .putExtra("end_date", endDate)
                            .putExtra("premiseId", premiseID)
                            .putExtra("status", homePremisedata!!.data.getcurrentstatus.status)
                            .putExtra(
                                    "last_update",
                                    homePremisedata!!.data.getcurrentstatus.lastUpdated
                            ).putExtra("home_type", "closeAt")
                            .putExtra(
                                    "image_type",
                                    homePremisedata!!.data.closedAtImage
                            ).putExtra("type_name", homePremisedata!!.data.closedAt)

                            .putExtra(
                                    "premise_image",
                                    homePremisedata!!.data.getcurrentstatus.imageFilename
                            )

            )
        }
    }

    private fun knownVistorrsOnClick() {
        Log.e("start_date", starDate)
        Log.e("endDate", endDate)
        Log.e("premiseId", premiseID)
        Log.e("status", homePremisedata!!.data.getcurrentstatus.status)
        Log.e("last_update", homePremisedata!!.data.getcurrentstatus.lastUpdated.toString())
        Log.e("home_type", "known")
        Log.e("max_value", homePremisedata!!.data.numberOfStaffMax.toString())
        Log.e("min_vale", homePremisedata!!.data.numberOfStaffMin.toString())
        Log.e("premise_image", homePremisedata!!.data.getcurrentstatus.imageFilename)

        if (tv_known_visitors_count.text.toString() == "-") {
            Toast("No data available", true, this@WhatchFaceDashBordActivity)
        } else {
            startActivity(
                    Intent(
                            this@WhatchFaceDashBordActivity,
                            DisplyGraphActivity::class.java
                    ).putExtra("start_date", starDate)
                            .putExtra("end_date", endDate)
                            .putExtra("premiseId", premiseID)
                            .putExtra("status", homePremisedata!!.data.getcurrentstatus.status)
                            .putExtra(
                                    "last_update",
                                    homePremisedata!!.data.getcurrentstatus.lastUpdated
                            ).putExtra("home_type", "known_visitors")
                            .putExtra(
                                    "max_value",
                                    homePremisedata!!.data.numberOfStaffMax.toString()
                            ).putExtra(
                                    "min_vale",
                                    homePremisedata!!.data.numberOfStaffMin.toString()
                            )
                            .putExtra(
                                    "premise_image",
                                    homePremisedata!!.data.getcurrentstatus.imageFilename
                            )
            )
        }
    }

    private fun staffOnClick() {
        Log.e("start_date", starDate)
        Log.e("endDate", endDate)
        Log.e("premiseId", premiseID)
        Log.e("status", homePremisedata!!.data.getcurrentstatus.status)
        Log.e("last_update", homePremisedata!!.data.getcurrentstatus.lastUpdated.toString())
        Log.e("home_type", "staff")
        Log.e("max_value", homePremisedata!!.data.numberOfStaffMax.toString())
        Log.e("min_vale", homePremisedata!!.data.numberOfStaffMin.toString())
        Log.e("premise_image", homePremisedata!!.data.getcurrentstatus.imageFilename)

        if (tv_staff_count.text.toString() == "-") {
            Toast("No data available", true, this@WhatchFaceDashBordActivity)
        } else {
            startActivity(
                    Intent(
                            this@WhatchFaceDashBordActivity,
                            DisplyGraphActivity::class.java
                    ).putExtra("start_date", starDate)
                            .putExtra("end_date", endDate)
                            .putExtra("premiseId", premiseID)
                            .putExtra("status", homePremisedata!!.data.getcurrentstatus.status)
                            .putExtra(
                                    "last_update",
                                    homePremisedata!!.data.getcurrentstatus.lastUpdated
                            ).putExtra("home_type", "staff")
                            .putExtra(
                                    "max_value",
                                    homePremisedata!!.data.numberOfStaffMax.toString()
                            ).putExtra(
                                    "min_vale",
                                    homePremisedata!!.data.numberOfStaffMin.toString()
                            )
                            .putExtra(
                                    "premise_image",
                                    homePremisedata!!.data.getcurrentstatus.imageFilename
                            )
            )
        }
    }

    private fun customerOnClick() {

        Log.e("start_date", starDate)
        Log.e("endDate", endDate)
        Log.e("premiseId", premiseID)
        Log.e("status", homePremisedata!!.data.getcurrentstatus.status)
        Log.e("last_update", homePremisedata!!.data.getcurrentstatus.lastUpdated.toString())
        Log.e("home_type", "customer")
        Log.e("max_value", homePremisedata!!.data.numberOfStaffMax.toString())
        Log.e("min_vale", homePremisedata!!.data.numberOfStaffMin.toString())
        Log.e("premise_image", homePremisedata!!.data.getcurrentstatus.imageFilename)
        if (tv_customer_min_max.text.toString() == "-") {
            Toast("No data available", true, this@WhatchFaceDashBordActivity)
        } else {
            startActivity(
                    Intent(
                            this@WhatchFaceDashBordActivity,
                            DisplyGraphActivity::class.java
                    ).putExtra("start_date", starDate)
                            .putExtra("end_date", endDate)
                            .putExtra("premiseId", premiseID)
                            .putExtra("status", homePremisedata!!.data.getcurrentstatus.status)
                            .putExtra(
                                    "last_update",
                                    homePremisedata!!.data.getcurrentstatus.lastUpdated
                            ).putExtra("home_type", "customer")
                            .putExtra(
                                    "max_value",
                                    homePremisedata!!.data.numberOfCustomersMax.toString()
                            ).putExtra(
                                    "min_vale",
                                    homePremisedata!!.data.numberOfCustomersMin.toString()
                            )
                            .putExtra(
                                    "premise_image",
                                    homePremisedata!!.data.getcurrentstatus.imageFilename
                            )
            )
        }
    }

    private fun firstCustomerOnClick() {
        Log.e("start_date", starDate)
        Log.e("endDate", endDate)
        Log.e("premiseId", premiseID)
        Log.e("status", homePremisedata!!.data.getcurrentstatus.status)
        Log.e("last_update", homePremisedata!!.data.getcurrentstatus.lastUpdated.toString())
        Log.e("home_type", "first_customer")
        Log.e("max_value", homePremisedata!!.data.numberOfStaffMax.toString())
        Log.e("min_vale", homePremisedata!!.data.numberOfStaffMin.toString())
        Log.e("premise_image", homePremisedata!!.data.getcurrentstatus.imageFilename)
        Log.e("image_type", homePremisedata!!.data.firstCustomerImage)
        Log.e("type_name", homePremisedata!!.data.firstCustomerTime)

        if (tv_first_customer_time.text.toString() == "-") {
            Toast("No data available", true, this@WhatchFaceDashBordActivity)
        } else {
            startActivity(
                    Intent(
                            this@WhatchFaceDashBordActivity,
                            DisplyGraphActivity::class.java
                    ).putExtra("start_date", starDate)
                            .putExtra("end_date", endDate)
                            .putExtra("premiseId", premiseID)
                            .putExtra("status", homePremisedata!!.data.getcurrentstatus.status)
                            .putExtra(
                                    "last_update",
                                    homePremisedata!!.data.getcurrentstatus.lastUpdated
                            ).putExtra("home_type", "first_customer")
                            .putExtra(
                                    "image_type",
                                    homePremisedata!!.data.firstCustomerImage
                            ).putExtra("type_name", homePremisedata!!.data.firstCustomerTime)
                            .putExtra(
                                    "premise_image",
                                    homePremisedata!!.data.getcurrentstatus.imageFilename
                            )

            )
        }
    }

    private fun openAtOnClick() {

        Log.e("start_date", starDate)
        Log.e("endDate", endDate)
        Log.e("premiseId", premiseID)
        Log.e("status", homePremisedata!!.data.getcurrentstatus.status)
        Log.e("last_update", homePremisedata!!.data.getcurrentstatus.lastUpdated.toString())
        Log.e("home_type", "first_customer")
        Log.e("max_value", homePremisedata!!.data.numberOfStaffMax.toString())
        Log.e("min_vale", homePremisedata!!.data.numberOfStaffMin.toString())
        Log.e("premise_image", homePremisedata!!.data.getcurrentstatus.imageFilename)
        Log.e("image_type", homePremisedata!!.data.openedAtImage)
        Log.e("type_name", homePremisedata!!.data.openedAt)
        if (tv_openat_time.text.toString() == "-") {
            Toast("No data available", true, this@WhatchFaceDashBordActivity)
        } else {
            startActivity(
                    Intent(
                            this@WhatchFaceDashBordActivity,
                            DisplyGraphActivity::class.java
                    ).putExtra("start_date", starDate)
                            .putExtra("end_date", endDate)
                            .putExtra("premiseId", premiseID)
                            .putExtra("status", homePremisedata!!.data.getcurrentstatus.status)
                            .putExtra(
                                    "last_update",
                                    homePremisedata!!.data.getcurrentstatus.lastUpdated
                            ).putExtra("home_type", "openAt")
                            .putExtra(
                                    "image_type",
                                    homePremisedata!!.data.openedAtImage
                            ).putExtra("type_name", homePremisedata!!.data.openedAt)

                            .putExtra(
                                    "premise_image",
                                    homePremisedata!!.data.getcurrentstatus.imageFilename
                            )

            )
        }
    }

    fun getdata(dataModel: DashbordPrimisiDataModel.Data) {
        //addLayout()
        if (dataModel.openedAt.isNotEmpty() && dataModel.openedAt!="00:00:00") {
            if(tv_openat_time!=null && tv_set_am_pm!=null && tvOpenAt!=null){
                setTimeToTv(dataModel.openedAt, tv_openat_time, tv_set_am_pm, tvOpenAt, "openedAt")
            }
        } else {
            if(tv_openat_time!=null && tv_set_am_pm!=null && tvOpenAt!=null){
                setEmptyToTv(tv_openat_time, tv_set_am_pm, tvOpenAt, "openedAt");
            }
        }


        if (dataModel.numberOfCustomersMax == 0 || dataModel.numberOfCustomersMin == 0) {
            val count : String
            count = "0";

            if(tv_customer_min_max!=null && tvCustomerCount!=null && tvTargetCustomerCount!=null && pgb_customers!=null){
                setEmptyToPgTV(
                        tv_customer_min_max,
                        tvCustomerCount,
                        tvTargetCustomerCount,
                        pgb_customers,
                        count
                )
                tvTargetCustomerCount?.text = targetCustomerCount
            }

        } else {
            val average : Int
            average = ((dataModel.numberOfCustomersMin+dataModel.numberOfCustomersMax)/2)
            if(tv_customer_min_max!=null && tvCustomerCount!=null && tvTargetCustomerCount!=null && pgb_customers!=null){
                tv_customer_min_max?.text = average.toString()
                tvCustomerCount?.text = average.toString()
                tvTargetCustomerCount?.text = targetCustomerCount
                var percentage = 0
                if(homePremisedata?.data?.userconfigdata?.targetcust!! <=average){
                    percentage =  100
                }else{
                    percentage =  ((100* average) /targetCustomerCount.toInt())
                }
                pgb_customers?.progress = percentage.toFloat()
            }

            /* setPgToTv(

                     tv_customer_min_max,
                     tvCustomerCount,
                     tvTargetCustomerCount,
                     pgb_customers,
                     count,
                     dataModel.numberOfCustomersMax,
                     dataModel.numberOfCustomersMin
             )*/
        }


        if (dataModel.numberOfCustomersnumber == 0) {
            if(tv_customer_value!=null){
                tv_customer_value.visibility = View.GONE
                tv_customer_value.text = "-"
            }
        } else {
            if(tv_customer_value!=null){
                tv_customer_value.visibility = View.GONE
                tv_customer_value.text = dataModel.numberOfCustomersnumber.toString()
            }
        }

        if (dataModel.numberOfStaffMax == 0 && dataModel.numberOfStaffMin == 0) {
            val count : String
            count = "0";
            if(tvTargetStaffCount!=null && tvStaffsCount!=null && tv_staff_count!=null && pgb_staffs!=null){
                setEmptyToPgTV(tv_staff_count, tvStaffsCount, tvTargetStaffCount, pgb_staffs, count)
                tvTargetStaffCount?.text = maxStaffCount
            }

        } else {
            val average : Int
            average = ((dataModel.numberOfStaffMin+dataModel.numberOfStaffMax)/2)
            if(tvTargetStaffCount!=null && tvStaffsCount!=null && tv_staff_count!=null && pgb_staffs!=null){
                tv_staff_count?.text = average.toString()
                tvStaffsCount?.text = average.toString()
                tvTargetStaffCount?.text = maxStaffCount
                var percentage = 0
                if(homePremisedata?.data?.userconfigdata?.targetstaff!! <= average){
                    percentage =  100
                }else{
                    percentage =  ((100* average)
                            /maxStaffCount.toInt())
                }
                pgb_staffs?.progress = percentage.toFloat()
            }

        }


        if (dataModel.firstCustomerTime.isNotEmpty() && dataModel.firstCustomerTime!="00:00:00") {

            if(tv_first_customer_time!=null && tv_first_customer_time_am_pm!=null && tvFirstCustomer!=null){
                setTimeToTv(
                        dataModel.firstCustomerTime,
                        tv_first_customer_time,
                        tv_first_customer_time_am_pm,
                        tvFirstCustomer,
                        "firstCustomer"
                )
            }

        } else {
            if(tv_first_customer_time!=null && tv_first_customer_time_am_pm!=null && tvFirstCustomer!=null){
                setEmptyToTv(
                        tv_first_customer_time,
                        tv_first_customer_time_am_pm,
                        tvFirstCustomer,
                        "firstCustomer"
                )
            }

        }

        if(dataModel.closedAt!=null && dataModel.closedAt.isNotEmpty()  && dataModel.closedAt!="00:00:00") {

            if(tv_closedat_time!=null && tv_set_am_pm_for_closed!=null && tvClosedAt!=null){
                setTimeToTv(
                        dataModel.closedAt,
                        tv_closedat_time,
                        tv_set_am_pm_for_closed,
                        tvClosedAt,
                        "closedAt"
                )
            }
        } else {
            if(tv_closedat_time!=null && tv_set_am_pm_for_closed!=null && tvClosedAt!=null){
                setEmptyToTv(tv_closedat_time, tv_set_am_pm_for_closed, tvClosedAt, "closedAt");
            }
        }

        if (dataModel.numberOfKnownVisitorsMin == 0 && dataModel.numberOfKnownVisitorsMax == 0) {
            val count : String
            count = "0"
            if(tv_known_visitors_count!=null && tvKnownVisitorsCount!=null && tvTargetKnownVisitors!=null && pgb_knownVisitors!=null ){
                setEmptyToPgTV(
                        tv_known_visitors_count,
                        tvKnownVisitorsCount,
                        tvTargetKnownVisitors,
                        pgb_knownVisitors,
                        count
                )
                tvTargetKnownVisitors?.text = targetKnownCustomersCount
            }
        } else {
            val average : Int
            average = ((dataModel.numberOfKnownVisitorsMin+dataModel.numberOfKnownVisitorsMax)/2)
            if(tv_known_visitors_count!=null && tvKnownVisitorsCount!=null && tvTargetKnownVisitors!=null && pgb_knownVisitors!=null ) {
                tv_known_visitors_count?.text = average.toString()
                tvKnownVisitorsCount?.text = average.toString()
                tvTargetKnownVisitors?.text = targetKnownCustomersCount
                var percentage = 0

                if(homePremisedata?.data?.userconfigdata?.targetknown!! <=average){
                    percentage =  100
                }else{
                    percentage =  ((100* average)
                            /targetKnownCustomersCount.toInt())
                }
                pgb_knownVisitors?.progress = percentage.toFloat()
            }

        }


        ll_graphical.visibility = View.GONE
        ll_numerical.visibility = View.GONE
        configurationSettings1()
    }


    private fun setEmptyToPgTV(
            tvMinMax: MyCustomTextView?,
            tvCount: TextView?,
            tvTargetCount: TextView?,
            adCircleProgress: AdCircleProgress?,
            count: String
    ) {
        tvMinMax?.text = "-"
        tvCount?.text = count
        adCircleProgress?.progress = 0F
        tvTargetCount?.text = "0"
    }

    private fun setEmptyToTv(
            tvOpenatTime: MyCustomTextView?,
            tvSetAmPm: MyCustomTextView?,
            tvOpenAt: TextView?,
            tempFrom: String
    ) {

        tvOpenatTime?.text = "-"
        tvSetAmPm?.visibility = View.GONE
        tvOpenAt?.text = "-"
        if(tempFrom=="openedAt"){
            hoursOpenedAt = 0.0
            minsOpenedAt = 0.0
            secsOpenedAt = 0.0
            var customAnalogClock : CustomAnalogClock? = null
            customAnalogClock?.initClock()

        }else if(tempFrom=="firstCustomer"){
            hoursFirstCustomer = 0.0
            minsFirstCustomer = 0.0
            secsFirstCustomer = 0.0
            var customAnalogClockFirstCustomer : CustomAnalogClockFirstCustomer? = null
            customAnalogClockFirstCustomer?.initClockFC()
        }else if(tempFrom=="closedAt"){
            hoursClosedAt = 0.0
            minsClosedAt = 0.0
            secsClosedAt = 0.0
            var customAnalogClockClosedAt : CustomAnalogClockClosedAt? = null
            customAnalogClockClosedAt?.initClockCAT()
        }
    }

    private fun setTimeToTv(
            timeStr: String,
            tvNumericalTime: MyCustomTextView?,
            tvSetAmPm: MyCustomTextView?,
            tvGraphical: TextView?,
            tempFrom: String
    ) {
        var convertedTime = convertDate(timeStr, "HH:mm:ss", "hh:mm")
        tvNumericalTime?.text = convertedTime
        tvSetAmPm?.text = convertDate(timeStr, "HH:mm:ss", "a")
        tvGraphical?.text = convertedTime + " " +convertDate(timeStr, "HH:mm:ss", "a")
        tvSetAmPm?.visibility = View.VISIBLE

        if(tempFrom=="openedAt"){
            var value:Double = 0.0
            if(timeStr.substring(3, 5).toDouble()<=15.0){
                value = 0.15
            }else if(timeStr.substring(3, 5).toDouble()<=30.0){
                value = 0.30
            }else if(timeStr.substring(3, 5).toDouble()<45.0){
                value = 0.45
            }else if(timeStr.substring(3, 5).toDouble()>=45.0){
                value = 0.75
            }
            hoursOpenedAt = convertedTime.substring(0, convertedTime.indexOf(":")).toDouble()+value
            minsOpenedAt = timeStr.substring(3, 5).toDouble()
            secsOpenedAt = timeStr.substring(6, timeStr.length).toDouble()
            var customAnalogClock : CustomAnalogClock? = null
            customAnalogClock?.initClock()
        }else if(tempFrom=="firstCustomer"){
            var value:Double = 0.0
            if(timeStr.substring(3, 5).toDouble()<=15.0){
                value = 0.15
            }else if(timeStr.substring(3, 5).toDouble()<=30.0){
                value = 0.30
            }else if(timeStr.substring(3, 5).toDouble()<45.0){
                value = 0.45
            }else if(timeStr.substring(3, 5).toDouble()>=45.0){
                value = 0.75
            }
            hoursFirstCustomer = convertedTime.substring(0, convertedTime.indexOf(":")).toDouble()+value
            minsFirstCustomer = timeStr.substring(3, 5).toDouble()
            secsFirstCustomer = timeStr.substring(6, timeStr.length).toDouble()

        }else if(tempFrom=="closedAt"){
            var value:Double = 0.0
            if(timeStr.substring(3, 5).toDouble()<=15.0){
                value = 0.15
            }else if(timeStr.substring(3, 5).toDouble()<=30.0){
                value = 0.30
            }else if(timeStr.substring(3, 5).toDouble()<45.0){
                value = 0.45
            }else if(timeStr.substring(3, 5).toDouble()>=45.0){
                value = 0.75
            }
            Log.d(TAG, "setTimeToTv: " + timeStr.substring(3, 5).toDouble().toString())
            Log.d(TAG, "setTimeToTv: " + value.toString())

            hoursClosedAt = convertedTime.substring(0, convertedTime.indexOf(":")).toDouble()+value
            Log.d(TAG, "setTimeToTv: " + hoursClosedAt.toString())
            // hoursClosedAt = 10.75
            minsClosedAt = timeStr.substring(3, 5).toDouble()
            secsClosedAt = timeStr.substring(6, timeStr.length).toDouble()
        }
    }
    fun conVertDateTimeToTimeStarp(date: String): String {
        var tempDate:String = ""
        if(date!=""){
            val sdf = SimpleDateFormat("HH:mm:ss")
            val date: Date = sdf.parse(date)
            System.out.println("Given Time in milliseconds : " + date.time)
            tempDate = date.time.toString() + "f"
        }

        return tempDate
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun setDateInTextView(type: String, date: String, end: String) {

        if (type == "today") {
            val cal: Calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("EEEE, MMM dd,yyyy")
            today_date_disply.text =
                convertDate(sdf.format(cal.time), "EEEE, MMM dd,yyyy", "EEEE, MMMM dd, yyyy")



            endDate = convertDate(sdf.format(cal.time), "EEEE, MMM dd,yyyy", "yyyy-MM-dd")
            cal.add(Calendar.DAY_OF_MONTH, -6)
            starDate = convertDate(sdf.format(cal.time), "EEEE, MMM dd,yyyy", "yyyy-MM-dd")

            Log.e(TAG, "Start date is-> $starDate end data is-> $endDate")
            homePremiseDataGet(endDate)

        } else if (type == "yesterday") {
            val cal: Calendar = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_MONTH, -1)
            val sdf = SimpleDateFormat("EEEE, MMM dd,yyyy")
            today_date_disply.text =
                convertDate(sdf.format(cal.time), "EEEE, MMM dd,yyyy", "EEEE, MMMM dd, yyyy")


            endDate = convertDate(sdf.format(cal.time), "EEEE, MMM dd,yyyy", "yyyy-MM-dd")
            cal.add(Calendar.DAY_OF_MONTH, -6)
            starDate = convertDate(sdf.format(cal.time), "EEEE, MMM dd,yyyy", "yyyy-MM-dd")

            Log.e(TAG, "Start date is-> $starDate end data is-> $endDate")
            homePremiseDataGet(endDate)
        } else if (type == "cal") {

            today_date_disply.text = convertDate(date, "EEEE, MMM dd,yyyy", "EEEE, MMMM dd, yyyy")

            endDate = convertDate(date, "EEEE, MMM dd,yyyy", "yyyy-MM-dd")
            starDate = convertDate(end, "EEEE, MMM dd,yyyy", "yyyy-MM-dd")

            Log.e(TAG, "Start date isss-> $starDate end data is-> $endDate")
            seletedDate = endDate
            isDataChanged = "calendar"
            if(isDataChanged == "calendar"){
                homePremiseDataGet(endDate)
            }
            userConfigAPI("cal")
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun homePremiseDataGet(date: String) {
        Log.e("date", date)
        Log.e("premiseId", premiseID)
        Log.e("userID", userID)
        Log.e("userModel", getUserModel()!!.data.token)

        if (isOnline(this@WhatchFaceDashBordActivity)) {
            Log.e(TAG, "ID premiseId=>")
            ApiRequest(
                    this,
                    ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userPremiseData(
                            "Bearer " + getUserModel()!!.data.token,
                            encrypt(date),
                            encrypt(premiseID),
                            encrypt(userID)
//                            encrypt("2021-07-10"),
//                            encrypt("4"),
//                            encrypt("13")
                    ),
                    HOME_PREMISE_DATA, true, this
            )
        } else {
            showSnackBar(
                    ll_home_premise,
                    getString(R.string.no_internet),
                    ACTIONSNACKBAR.DISMISS
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun tabViewClick() {
        tv_today_click.setOnClickListener {
            setDateInTextView("today", "", "")
            tv_today_click.setBackgroundResource(R.drawable.first_watch_back)
            tv_calender_click.setBackgroundResource(R.drawable.third_watch_back)
            tv_yesterday_click.setBackgroundColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.d_seleted_back_color
                    )
            )
            /*tv_lastweek_click.setBackgroundColor(
                ContextCompat.getColor(
                    this@WhatchFaceDashBordActivity,
                    R.color.d_seleted_back_color
                )
            )*/

            tv_today_click.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_yesterday_click.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.black
                    )
            )
            /* tv_lastweek_click.setTextColor(
                 ContextCompat.getColor(
                     this@WhatchFaceDashBordActivity,
                     R.color.black
                 )
             )*/

            tv_calender_click.setColorFilter(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.black
                    ), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        tv_yesterday_click.setOnClickListener {
            setDateInTextView("yesterday", "", "")

            tv_yesterday_click.setBackgroundResource(R.drawable.first_watch_back)
            tv_today_click.setBackgroundResource(R.drawable.fsecond_watch_back)
            tv_calender_click.setBackgroundResource(R.drawable.third_watch_back)


            /*  tv_lastweek_click.setBackgroundColor(
                  ContextCompat.getColor(
                      this@WhatchFaceDashBordActivity,
                      R.color.d_seleted_back_color
                  )
              )*/

            tv_yesterday_click.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    )
            )
            tv_today_click.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.black
                    )
            )
            /*tv_lastweek_click.setTextColor(
                ContextCompat.getColor(
                    this@WhatchFaceDashBordActivity,
                    R.color.black
                )
            )*/

            tv_calender_click.setColorFilter(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.black
                    ), android.graphics.PorterDuff.Mode.SRC_IN
            );

        }
        /* tv_lastweek_click.setOnClickListener {

             tv_lastweek_click.setBackgroundResource(R.drawable.first_watch_back)
             tv_today_click.setBackgroundResource(R.drawable.fsecond_watch_back)
             tv_calender_click.setBackgroundResource(R.drawable.third_watch_back)
             tv_yesterday_click.setBackgroundColor(
                 ContextCompat.getColor(
                     this@WhatchFaceDashBordActivity,
                     R.color.d_seleted_back_color
                 )
             )

             tv_lastweek_click.setTextColor(
                 ContextCompat.getColor(
                     this@WhatchFaceDashBordActivity,
                     R.color.white
                 )
             )
             tv_today_click.setTextColor(
                 ContextCompat.getColor(
                     this@WhatchFaceDashBordActivity,
                     R.color.black
                 )
             )
             tv_yesterday_click.setTextColor(
                 ContextCompat.getColor(
                     this@WhatchFaceDashBordActivity,
                     R.color.black
                 )
             )
             tv_calender_click.setColorFilter(
                 ContextCompat.getColor(
                     this@WhatchFaceDashBordActivity,
                     R.color.black
                 ), android.graphics.PorterDuff.Mode.SRC_IN
             );
         }*/
        tv_calender_click.setOnClickListener {


            val c = Calendar.getInstance()


            Log.e("ccccc123", seletedDate)

            if(seletedDate!=""){
                val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val cal = Calendar.getInstance()
                cal.time = df.parse(seletedDate)
                mYear = cal.get(Calendar.YEAR)
                Log.e("ccccc-y", mYear.toString())
                mMonth = cal.get(Calendar.MONTH)
                Log.e("ccccc-m", mMonth.toString())
                mDay = cal.get(Calendar.DAY_OF_MONTH)
                Log.e("ccccc-d", mDay.toString())

            }

            if(mYear==0 || mMonth==0 ||mDay==0){
                mYear = c.get(Calendar.YEAR)
                Log.e("ccccc-y", mYear.toString())
                mMonth = c.get(Calendar.MONTH)
                Log.e("ccccc-m", mMonth.toString())
                mDay = c.get(Calendar.DAY_OF_MONTH)
                Log.e("ccccc-d", mDay.toString())
            }


            datePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // etPatientRegisterBirthDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                        val calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
                        //setDateInTextView("yesterday", calendar.time)
                        // tv_acute_data_question.text = simpleDateFormat!!.format(calendar.time)
                        val cal = GregorianCalendar(year, monthOfYear, dayOfMonth)
                        cal.add(Calendar.DAY_OF_MONTH, -6)
                        val sdf = SimpleDateFormat("EEEE, MMM dd,yyyy")

                        setDateInTextView("cal", sdf.format(calendar.time), sdf.format(cal.time))
                    }, mYear, mMonth, mDay
            )
            datePickerDialog?.show()

            datePickerDialog!!.getDatePicker().setMaxDate(System.currentTimeMillis());

            tv_calender_click.setColorFilter(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
            );

            tv_calender_click.setBackgroundResource(R.drawable.first_watch_back)
            tv_today_click.setBackgroundResource(R.drawable.fsecond_watch_back)
            tv_yesterday_click.setBackgroundColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.d_seleted_back_color
                    )
            )
            /*  tv_lastweek_click.setBackgroundColor(
                  ContextCompat.getColor(
                      this@WhatchFaceDashBordActivity,
                      R.color.d_seleted_back_color
                  )
              )*/
            tv_yesterday_click.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.black
                    )
            )
            tv_today_click.setTextColor(
                    ContextCompat.getColor(
                            this@WhatchFaceDashBordActivity,
                            R.color.black
                    )
            )
            /*tv_lastweek_click.setTextColor(
                ContextCompat.getColor(
                    this@WhatchFaceDashBordActivity,
                    R.color.black
                )
            )*/
        }
        tv_today_click.performClick()

    }

    fun getPremiseDataSet(
            dataModel: DashbordPrimisiDataModel.Data.Premisedata,
            status: String,
            lastUpdated: String,
            Iimage: String,
            userconfigdata: DashbordPrimisiDataModel.Data.Userconfigdata
    ) {

        if(homePremisedata!!.data.userpremise.opened_at){
            tv_open_at.visibility  = View.VISIBLE
            ll_graphical_open_at.visibility = View.VISIBLE
        }else{
            tv_open_at.visibility  = View.GONE
            ll_graphical_open_at.visibility = View.GONE
        }
        if(homePremisedata!!.data.userpremise.first_customer){
            ll_first_custome.visibility  = View.VISIBLE
            ll_graphical_first_customer.visibility = View.VISIBLE
        }else{
            ll_first_custome.visibility  = View.GONE
            ll_graphical_first_customer.visibility = View.GONE
        }
        if(homePremisedata!!.data.userpremise.customer){
            ll_custome_up_down.visibility  = View.VISIBLE
            ll_graphical_customers.visibility = View.VISIBLE
        }else{
            ll_custome_up_down.visibility  = View.GONE
            ll_graphical_customers.visibility = View.GONE
        }
        if(homePremisedata!!.data.userpremise.staff){
            ll_staff.visibility  = View.VISIBLE
            ll_graphical_staff.visibility = View.VISIBLE
        }else{
            ll_staff.visibility  = View.GONE
            ll_graphical_staff.visibility = View.GONE
        }

        if(homePremisedata!!.data.userpremise.closed_at){
            tv_closed_at.visibility  = View.VISIBLE
            ll_graphical_closed_at.visibility = View.VISIBLE
        }else{
            tv_closed_at.visibility  = View.GONE
            ll_graphical_closed_at.visibility = View.GONE
        }
        if(homePremisedata!!.data.userpremise.visitors){
            ll_known_visitors.visibility  = View.VISIBLE
            ll_graphical_known_visitors.visibility = View.VISIBLE
        }else{
            ll_known_visitors.visibility  = View.GONE
            ll_graphical_known_visitors.visibility = View.GONE
        }
        if(userconfigdata!=null){
            ll_graphical.visibility = View.GONE
            ll_numerical.visibility = View.GONE
            toggleSetting = userconfigdata.toggle
            seletedDate = userconfigdata.date
            targetCustomerCount = userconfigdata.targetcust.toString()
            maxStaffCount = userconfigdata.targetstaff.toString()
            targetKnownCustomersCount = userconfigdata.targetknown.toString()
            configurationSettings1()
            if(tvTargetCustomerCount!=null){
                tvTargetCustomerCount.text = targetCustomerCount
            }
            if(tvTargetStaffCount!=null){
                tvTargetStaffCount.text = maxStaffCount
            }
            if(tvTargetKnownVisitors!=null){
                tvTargetKnownVisitors.text = targetKnownCustomersCount
            }
            Log.e("toggle_count", toggleSetting);
            Log.e("seletedDate", seletedDate);
            Log.e("targetCustomerCount", targetCustomerCount);
            Log.e("maxStaffCount", maxStaffCount)
            /*  tvTargetCustomerCount.setOnClickListener(this)
       tvTargetStaffCount.setOnClickListener(this)
       tvTargetKnownVisitors.setOnClickListener(this)*/
        }


        // if(toggleSetting=="numeric")

        tv_watch_place_name.text = dataModel.name

        if (dataModel.state.isNotEmpty()) {
            tv_watch_place_city.text = dataModel.city + ", " + dataModel.state
        } else {
            tv_watch_place_city.text = dataModel.city
        }
        /*   Glide.with(this@WhatchFaceDashBordActivity)
               .load(dataModel.photo)
               .placeholder(R.drawable.home_btn_get_started)
               .into(im_home_premise)*/

        val circularProgressDrawable = CircularProgressDrawable(this@WhatchFaceDashBordActivity)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(this@WhatchFaceDashBordActivity)
            .load(Iimage)
            .placeholder(circularProgressDrawable)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(im_home_premise)

        tv_open_close.text = status

        if (status == "Open") {
            tv_open_close.visibility = View.VISIBLE
            tv_open_close.setBackgroundResource(R.drawable.home_list_second_list_second)
        } else if (status == "Closed") {
            tv_open_close.visibility = View.VISIBLE
            tv_open_close.setBackgroundResource(R.drawable.home_list_second_list)
        } else {
            tv_open_close.visibility = View.INVISIBLE
        }

        Log.e("lastUpdated123", lastUpdated)
        val timeStr : String
        timeStr = changeTimeFormat(lastUpdated);
        tv_watch_time.text = timeStr.toString()

        ll_first_view.setOnClickListener {

            startActivity(
                    Intent(
                            this@WhatchFaceDashBordActivity,
                            PrimiseDetailActivty::class.java
                    ).putExtra("name", dataModel.name)
                            .putExtra("city", dataModel.city)
                            .putExtra("state", dataModel.state)
                            .putExtra("Iimage", Iimage)
                            .putExtra(
                                    "status", status
                            ).putExtra("lastUpdated", lastUpdated)

            )
        }
        // ll_graphical.removeAllViews()
        getdata(homePremisedata!!.data)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            HOME_PREMISE_DATA -> {
                Log.e("reponsseeee", apiResponseManager.response.toString())
                homePremisedata = apiResponseManager.response as DashbordPrimisiDataModel
                if (homePremisedata!!.statusCode == SUCCESS_CODE) {
                    if (homePremisedata!!.data != null) {
                            getPremiseDataSet(
                                    homePremisedata!!.data.premisedata,
                                    homePremisedata!!.data.getcurrentstatus.status,
                                    homePremisedata!!.data.getcurrentstatus.lastUpdated,
                                    homePremisedata!!.data.getcurrentstatus.imageFilename,
                                    homePremisedata!!.data?.userconfigdata
                            )

                    }

                }
            }
            USER_CONFIG_DATA -> {
                Log.e("reponsseeee123", apiResponseManager.response.toString())
                userconfigdataModel = apiResponseManager.response as UserConfigData
                if (userconfigdataModel!!.status_code == SUCCESS_CODE) {
                    /*if(isDataChanged == "calendar"){
                        homePremiseDataGet(endDate)
                    }*/
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun createDynamicLayouts() {
        ll_graphical.removeAllViews()
        ll_numerical.removeAllViews()
        Log.d(TAG, "createDynamicLayouts: ....")
        tempSize = 0;
        Log.d(TAG, "createDynamicLayouts: opened_at-" + homePremisedata!!.data.userpremise.opened_at)
        Log.d(TAG, "createDynamicLayouts: fs-" + homePremisedata!!.data.userpremise.first_customer)
        Log.d(TAG, "createDynamicLayouts: cus-" + homePremisedata!!.data.userpremise.customer)
        Log.d(TAG, "createDynamicLayouts: sta-" + homePremisedata!!.data.userpremise.staff)
        Log.d(TAG, "createDynamicLayouts: clos-" + homePremisedata!!.data.userpremise.closed_at)
        Log.d(TAG, "createDynamicLayouts: vis-" + homePremisedata!!.data.userpremise.visitors)
        if(homePremisedata!!.data.userpremise.opened_at){
            arrayList.add("opened_at")
        }
        if(homePremisedata!!.data.userpremise.first_customer){
            arrayList.add("first_customer")
        }
        if(homePremisedata!!.data.userpremise.customer){
            arrayList.add("customer")
        }
        if(homePremisedata!!.data.userpremise.staff){
            arrayList.add("staff")
        }
        if(homePremisedata!!.data.userpremise.closed_at){
            arrayList.add("closed_at")
        }
        if(homePremisedata!!.data.userpremise.visitors){
            arrayList.add("known_visitors")
        }
        var rowLength = 0
        var arraySize = arrayList.size
        var columnLength = 0
        if(((arrayList.size)%2)==0){
            rowLength = (arrayList.size)/2
            columnLength = 2
        }else{
            rowLength =  ((arrayList.size)/2) + ((arrayList.size)%2)
            columnLength = 1
        }
        var x = arraySize%2
        Log.d(TAG, "createDynamicLayouts: " + arrayList.size + "--" + x)
        for(i in 0..rowLength-1){
            addLayout(i)
        }
        for(i in 0..rowLength-1){
            addNumericalLayout(i)
        }


        getPremiseDataSet(
                homePremisedata!!.data.premisedata,
                homePremisedata!!.data.getcurrentstatus.status,
                homePremisedata!!.data.getcurrentstatus.lastUpdated,
                homePremisedata!!.data.getcurrentstatus.imageFilename,
                homePremisedata!!.data?.userconfigdata
        )
        isLayoutCreated = true
        boxClick()
    }

    private fun addNumericalLayout(i: Int) {
        val a = LinearLayout(this)
        a.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(20, 20, 20, 0)
        a.layoutParams = layoutParams

        for(i in 0..1){
            Log.d(TAG, "addLayout: For ...." + i)
            if(tempNumericalSize <= arrayList.size-1){
                if(arrayList.get(tempNumericalSize)=="opened_at"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempNumericalSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.numerical_open_at,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }else if(arrayList.get(tempNumericalSize)=="first_customer"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempNumericalSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.numerical_first_customer,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }else if(arrayList.get(tempNumericalSize)=="customer"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempNumericalSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.numerical_customer,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }else if(arrayList.get(tempNumericalSize)=="staff"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempNumericalSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.numerical_staff,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }else if(arrayList.get(tempNumericalSize)=="closed_at"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempNumericalSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.numerical_closed_at,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }else if(arrayList.get(tempNumericalSize)=="known_visitors"){
                    Log.d(TAG, "addLayout: if ...." + arrayList.get(tempNumericalSize))
                    val inflater: LayoutInflater = LayoutInflater.from(this@WhatchFaceDashBordActivity)
                    val inflatedLayout: View = inflater.inflate(
                            R.layout.numerical_known_visitors,
                            ll_graphical,
                            false
                    )
                    a.addView(inflatedLayout)
                }
            }
            tempNumericalSize++
        }
        ll_numerical.addView(a)

    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onClick(v: View?) {
        when(v?.id){
            /* R.id.rb_numerical -> {
                 toggleSetting = "numeric"
                 Log.e("toggleSetting", "numericall")
                 userConfigAPI("numerical")
                 rb_numerical.setTextColor(Color.WHITE)
                 rb_graphical.setTextColor(Color.BLACK)
                 sticky_switch.setDirection(StickySwitch.Direction.RIGHT, false, false);
                 ll_numerical.visibility = View.VISIBLE
                 ll_graphical.visibility = View.GONE
             }
             R.id.rb_graphical -> {
                 toggleSetting = "graphic"
                 sticky_switch.setDirection(StickySwitch.Direction.RIGHT, false, false);
                 userConfigAPI("graphic")
                 ll_graphical.visibility = View.VISIBLE
                 ll_numerical.visibility = View.GONE

             }*/

            R.id.tvTargetCustomerCount -> {
                showNumberSelect("Select Customer Max Count")
            }

            R.id.tvTargetStaffCount -> {
                showStaffNumberSelect("Select Staff Target")
            }
            R.id.tvTargetKnownVisitors -> {
                showKnownVisitorsNumberSelect("Select Known Visitors Target")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showKnownVisitorsNumberSelect(title: String) {
        val d = Dialog(this@WhatchFaceDashBordActivity)
        d.setTitle("NumberPicker")
        d.setContentView(R.layout.dialog)
        val b1: Button = d.findViewById<View>(R.id.button1) as Button
        val b2: Button = d.findViewById<View>(R.id.button2) as Button
        val np = d.findViewById<View>(R.id.numberPicker1) as NumberPicker
        val tvClose: TextView = d.findViewById<View>(R.id.tvClose) as TextView
        val tvTitle: TextView = d.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.text = title
//        tvTitle.text = title.toUpperCase()
        tvClose.setOnClickListener {
            d.dismiss()
        }

//        np.maxValue = 100
//        np.minValue = 0
//        np.wrapSelectorWheel = false


        //var nums = arrayOf("20","50")

        val nums = arrayOfNulls<String>(2)

        /* for (i in 0 until 49) {
             if(nums.size <= i){
                 Log.e("nums-print",i.toString() +"--"+(i+1).toString())
                 nums[i]=(i+1).toString()
             }
         }*/



        np.maxValue = 50
        np.minValue = 1
        np.wrapSelectorWheel = false
        //  np.displayedValues = nums
        np.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        np.setOnValueChangedListener(this)
        np.setOnValueChangedListener(this)
        np.value = targetKnownCustomersCount.toInt()

        b1.setOnClickListener {
            tvTargetKnownVisitors.text = np.value.toString()
            targetKnownCustomersCount =  np.value.toString()
            isDataChanged = "known_visitors"
            var percentage = 0
            var avg : Int = 0
            avg = ((homePremisedata!!.data.numberOfKnownVisitorsMin+homePremisedata!!.data.numberOfKnownVisitorsMax)/2)
            if(targetKnownCustomersCount.toInt()<= avg){
                percentage =  100
            }else{
                percentage =  ((100* avg)
                        /targetKnownCustomersCount.toInt())
            }
            pgb_knownVisitors?.progress = percentage.toFloat()
            userConfigAPI("known_visitors")
            d.dismiss()
        }

        b2.setOnClickListener {
            d.dismiss()
        }

        d.show()
        val window: Window? = d.getWindow()
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun userConfigAPI(s: String) {

        var isProgressDialog = true
        if(s=="cal"){
            isProgressDialog = false
        }

        Log.e("hfhdhfjd", userID)
        Log.e("hfhdhfjd", premiseID)
        Log.e("hfhdhfjd", seletedDate)
        Log.e("hfhdhfjd", toggleSetting)
        Log.e("hfhdhfjd", maxStaffCount)
        Log.e("hfhdhfjd", targetCustomerCount)

        if (isOnline(this@WhatchFaceDashBordActivity)) {
            ApiRequest(
                    this,
                    ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userConfigData(
                            "Bearer " + getUserModel()!!.data.token,
                            encrypt(userID),
                            encrypt(premiseID),
                            encrypt(seletedDate),
                            encrypt(toggleSetting),
                            //encrypt("graphic"),
                            encrypt(maxStaffCount),
                            encrypt(targetCustomerCount),
                            encrypt(targetKnownCustomersCount)
                    ),
                    USER_CONFIG_DATA, isProgressDialog, this

            )

        } else {
            showSnackBar(
                    ll_home_premise,
                    getString(R.string.no_internet),
                    ACTIONSNACKBAR.DISMISS
            )
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showStaffNumberSelect(title: String) {
        val d = Dialog(this@WhatchFaceDashBordActivity)
        d.setTitle("NumberPicker")
        d.setContentView(R.layout.dialog)
        val b1: Button = d.findViewById<View>(R.id.button1) as Button
        val b2: Button = d.findViewById<View>(R.id.button2) as Button
        val np = d.findViewById<View>(R.id.numberPicker1) as NumberPicker
        val tvClose: TextView = d.findViewById<View>(R.id.tvClose) as TextView
        val tvTitle: TextView = d.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.text = title
//        tvTitle.text = title.toUpperCase()
        tvClose.setOnClickListener {
            d.dismiss()
        }
//        np.maxValue = 100
//        np.minValue = 0
//        np.wrapSelectorWheel = false


        //var nums = arrayOf("20","50")

        val nums = arrayOfNulls<String>(2)

        /* for (i in 0 until 49) {
             if(nums.size <= i){
                 Log.e("nums-print",i.toString() +"--"+(i+1).toString())
                 nums[i]=(i+1).toString()
             }
         }*/



        np.maxValue = 50
        np.minValue = 1
        np.wrapSelectorWheel = false
        //  np.displayedValues = nums
        np.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        np.setOnValueChangedListener(this)
        np.setOnValueChangedListener(this)
        np.value = maxStaffCount.toInt()

        b1.setOnClickListener {
            tvTargetStaffCount.text = np.value.toString()
            maxStaffCount =  np.value.toString()
            isDataChanged = "staff"
            var percentage = 0
            var avg : Int = 0
            avg = ((homePremisedata!!.data.numberOfStaffMin+homePremisedata!!.data.numberOfStaffMax)/2)
            if(np.value<=avg){
                percentage =  100
            }else{
                percentage =  ((100* avg) /np.value)
            }
            pgb_staffs?.progress = percentage.toFloat()
            Log.e("percentage", percentage.toString());
            userConfigAPI("staff")
            d.dismiss()
        }

        b2.setOnClickListener {
            d.dismiss()
        }

        d.show()
        val window: Window? = d.getWindow()
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showNumberSelect(title: String) {
        Log.e("targetCustomerCount", targetCustomerCount + "---")
        val d = Dialog(this@WhatchFaceDashBordActivity)
        d.setTitle("NumberPicker")
        d.setContentView(R.layout.dialog)
        val b1: Button = d.findViewById<View>(R.id.button1) as Button
        val b2: Button = d.findViewById<View>(R.id.button2) as Button
        val tvClose: TextView = d.findViewById<View>(R.id.tvClose) as TextView
        val tvTitle: TextView = d.findViewById<View>(R.id.tvTitle) as TextView
        val np = d.findViewById<View>(R.id.numberPicker1) as NumberPicker

//        np.maxValue = 100
//        np.minValue = 0
//        np.wrapSelectorWheel = false

        //var nums = arrayOf("20","50")

        tvTitle.text = title
//        tvTitle.text = title.toUpperCase()

        val nums = arrayOfNulls<String>(21)
        nums[0] = "20"
        var j = 1
        for (i in 1 until 1001) {
            if(i%50==0){
                nums[j] = (j*50).toString()
                j++
            }

        }

        np.maxValue = nums.size - 1
        np.minValue = 0
        np.wrapSelectorWheel = false
        np.displayedValues = nums
        np.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        np.setOnValueChangedListener(this)
        np.setOnValueChangedListener(this)
        j = 1
        for (i in 1 until 1001) {
            if(i%50==0){
                if((j*50).toString() == targetCustomerCount){
                    np.setValue(j)
                    break
                }
                j++
            }
        }
        b1.setOnClickListener {
            Log.e("nums[np.value]", nums[np.value].toString())
            tvTargetCustomerCount.text = nums[np.value].toString()
            targetCustomerCount = nums[np.value].toString()
            isDataChanged = "customer"
            var percentage = 0
            var avg :Int = 0
            avg = ((homePremisedata!!.data.numberOfCustomersMin+homePremisedata!!.data.numberOfCustomersMax)/2)
            if(targetCustomerCount.toInt()<=avg){
                percentage =  100
            }else{
                percentage =  ((100* avg) /targetCustomerCount.toInt())
            }
            pgb_customers?.progress = percentage.toFloat()
            userConfigAPI("customer")

            d.dismiss()
        }
        b2.setOnClickListener {
            d.dismiss()
        }

        tvClose.setOnClickListener {
            d.dismiss()
        }

        d.show()

        val window: Window? = d.getWindow()
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        Log.i("value is", "" + newVal);
    }

}

