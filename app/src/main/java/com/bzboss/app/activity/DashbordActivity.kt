package com.bzboss.app.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bzboss.app.R
import com.bzboss.app.adapter.HomeListAdapter
import com.bzboss.app.custom.MyCustomTextView
import com.bzboss.app.custom.isOnline
import com.bzboss.app.model.HomeDataModel
import com.bzboss.app.model.HomeListModel
import com.bzboss.app.model.UserAccessData
import com.bzboss.app.model.UserModel
import com.bzboss.app.restapi.ApiInitialize
import com.bzboss.app.restapi.ApiRequest
import com.bzboss.interfaceD.HomeClick
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iqamahtimes.app.custom.*
import com.iqamahtimes.app.restapi.ApiResponseInterface
import com.iqamahtimes.app.restapi.ApiResponseManager
import kotlinx.android.synthetic.main.activity_dashbord.*
import kotlinx.android.synthetic.main.my_custom_toolbar.*


class DashbordActivity : ActivityBase(), ApiResponseInterface,HomeClick {
    private var homeListData: ArrayList<HomeListModel>? = null
    private var dialog: Dialog? = null
    private var sideMenuSelectKey: String = "Home"
    private var userResponse: String = ""
    private var checkDialog: Boolean = false
    private var homeDataGet:HomeDataModel?=null
    private var userAccessData:UserAccessData?=null


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashbord)
        homeListData = ArrayList()
        // setHomeData()
        userResponse = intent.getStringExtra("user_response")!!
        Log.e("user_response",userResponse)
        Log.e("user_response",getUserModel()?.data?.userId.toString())


        logout.visibility = View.VISIBLE
        logout.setImageResource(R.drawable.refresh_page_option)

        val rotate = RotateAnimation(
            0f,
            180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 3000
        rotate.interpolator = LinearInterpolator()
        logout.setOnClickListener {
            if(!isUserHavingAccess){
                getuserAccess()
            }else{
                homeDataGet()
            }
           // homeDataGet()
           // logout.startAnimation(rotate);
            //logout.rotation = logout.rotation + 90;
        }
        click_drawer_menu.setOnClickListener {
            setDrawerDialog()
        }
        //homeDataGet()

        if(!isUserHavingAccess){
            getuserAccess()
        }else{
            homeDataGet()
        }



        /*pullToRefresh.setOnRefreshListener {
            homeDataGet()// your code
            pullToRefresh.isRefreshing = false
        }*/
       /* refereshView.setOnClickListener {
            homeDataGet()
        }*/
    }

    @SuppressLint("NewApi")
    private fun getuserAccess() {
        Log.e("eee",getUserModel()?.data?.userId.toString())
        if (isOnline(this@DashbordActivity)) {
            ApiRequest(
                    this,
                    ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userAccess(
                           // "Bearer " + getUserModel()!!.data.token,
                            encrypt(getUserModel()?.data?.userId.toString())
                    ),
                    USER_ACCESSS, false, this
            )
        } else {
            showSnackBar(
                    ll_home_dashbord,
                    getString(R.string.no_internet),
                    ACTIONSNACKBAR.DISMISS
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
     //   homeDataGet()
        sideMenuSelectKey = "home"
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun logout() {
        val builder =
            MaterialAlertDialogBuilder(this@DashbordActivity)

        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")

        builder.setPositiveButton("Yes") { _, _ ->
            objSharedPref.putBoolean(getString(R.string.is_login), false)
            val getStartedIntent = Intent(this@DashbordActivity, LoginSignHomeActivity::class.java)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            startActivity(getStartedIntent)
            finishAffinity()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.show()

    }
   

    override fun onBackPressed() {
        appClose()
    }

    fun setHomeData() {
        homeListData!!.clear()




        homeListData!!.add(HomeListModel("Watch place 1", "Surat, Gujarat", "OPEN", R.color.red))
        homeListData!!.add(
            HomeListModel(
                "Watch place 2",
                "Ahmedabad, Gujarat",
                "OPEN",
                R.color.red
            )
        )
        homeListData!!.add(
            HomeListModel(
                "Watch place 3",
                "Vadodara, Gujarat",
                "CLOSED",
                R.drawable.ic_slider_four
            )
        )
        homeListData!!.add(HomeListModel("Watch place 1", "Surat, Gujarat", "OPEN", R.color.red))
        homeListData!!.add(
            HomeListModel(
                "Watch place 2",
                "Ahmedabad, Gujarat",
                "CLOSED",
                R.drawable.test_ofer
            )
        )


    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun homeDataGet() {
        if (isOnline(this@DashbordActivity)) {
            ApiRequest(
                this,
                ApiInitialize.initialize(ApiInitialize.LOCAL_URL).homeDataGet(
                    "Bearer " + getUserModel()!!.data.token
                ),
                HOME_DATA_GET, true, this
            )
        } else {
            showSnackBar(
                ll_home_dashbord,
                getString(R.string.no_internet),
                ACTIONSNACKBAR.DISMISS
            )
        }
    }

    private fun setHomeOfferData(data: ArrayList<HomeDataModel.Data>) {
        if (data.size > 0) {
            Log.d(TAG, "setHomeOfferData: "+"calling")

            rv_offer_data.setHasFixedSize(true)
            val layoutManager =
                LinearLayoutManager(this@DashbordActivity, LinearLayoutManager.VERTICAL, false)
            rv_offer_data.layoutManager = layoutManager
            val treatment_reason =
                HomeListAdapter(
                    this@DashbordActivity,
                    data, this
                )
            // Create adapter object
            rv_offer_data.adapter = treatment_reason
            rv_offer_data.visibility = View.VISIBLE
          //  refereshView.visibility=View.VISIBLE
            tv_home_no_data.visibility=View.GONE
            /*rv_offer_data.addOnItemTouchListener(
                RecyclerTouchListener(
                    this@DashbordActivity,
                    rv_offer_data,
                    object : ClickListener {
                        override fun onClick(view: View, position: Int) {

                            if (data[position].data_availabel == "1") {
                                startActivity(
                                    Intent(
                                        this@DashbordActivity,
                                        WhatchFaceDashBordActivity::class.java
                                    )
                                        .putExtra(
                                            "open_or_close",
                                            data[position].getpremisecurrentstatus.status
                                        )
                                        .putExtra("premiseId", data[position].premiseId.toString())
                                        .putExtra("mane_toolbar", "gone")
                                )
                            } else {
                                Toast("No data available", true, this@DashbordActivity)
                            }


                        }

                        override fun onLongClick(view: View, position: Int) {}
                    })
            )*/
            treatment_reason.notifyDataSetChanged()

        } else {
         //   refereshView.visibility=View.GONE
            tv_home_no_data.visibility = View.VISIBLE
            rv_offer_data.visibility = View.GONE
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun setDrawerDialog() {
        dialog = Dialog(this@DashbordActivity, R.style.MyDialogTheme)
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
            dialog!!.dismiss()
            logout()
        }

        tv_side_menu_home.setOnClickListener {
            tv_side_menu_home.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.location_text_color
                )
            )
            tv_side_menu_privacy_polivy.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_term_condition.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_contacts.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_abouts.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
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
                    this@DashbordActivity,
                    R.color.location_text_color
                )
            )
            tv_side_menu_term_condition.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_abouts.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_contacts.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_home.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
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
                    this@DashbordActivity,
                    R.color.location_text_color
                )
            )
            tv_side_menu_home.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_privacy_polivy.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_contacts.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_abouts.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            sideMenuSelectKey = "term_condition"
            checkDialog = false
            objSharedPref.putBoolean(CHECK_DIALOG_OPEN_CLOSE, checkDialog)
            dialog!!.dismiss()
        }
        tv_side_menu_contacts.setOnClickListener {
            startActivity(Intent(this@DashbordActivity, ContactsUsActiivty::class.java))

            tv_side_menu_contacts.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.location_text_color
                )
            )
            tv_side_menu_home.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_abouts.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_term_condition.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_privacy_polivy.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
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
                    this@DashbordActivity,
                    R.color.location_text_color
                )
            )
            tv_side_menu_home.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_privacy_polivy.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_term_condition.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
                    R.color.white
                )
            )
            tv_side_menu_contacts.setTextColor(
                ContextCompat.getColor(
                    this@DashbordActivity,
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

    override fun homePostionClick(postion: Int) {
      /*  startActivity(
            Intent(
                this@DashbordActivity,
                PrimiseDetailActivty::class.java
            )
                .putExtra(
                    "status",
                    homeDataGet!!.data[postion].getpremisecurrentstatus.status
                )
                .putExtra("premiseId", homeDataGet!!.data[postion].premiseId.toString())
                .putExtra("name", homeDataGet!!.data[postion].name)
                .putExtra("city", homeDataGet!!.data[postion].city)
                .putExtra("state", homeDataGet!!.data[postion].state)
                .putExtra("Iimage", homeDataGet!!.data[postion].getpremisecurrentstatus.imageFilename)
                .putExtra("lastUpdated", homeDataGet!!.data[postion].getpremisecurrentstatus.lastUpdated)
                .putExtra("mane_toolbar", "gone")
        )
*/
        startActivity(
            Intent(
                this@DashbordActivity,
                WhatchFaceDashBordActivity::class.java
            )
                .putExtra(
                    "open_or_close",
                    homeDataGet!!.data[postion].getpremisecurrentstatus.status
                )
                .putExtra("premiseId", homeDataGet!!.data[postion].premiseId.toString())
                .putExtra("mane_toolbar", "gone")
        )
    }

    @SuppressLint("NewApi")
    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            HOME_DATA_GET -> {
                homeDataGet = apiResponseManager.response as HomeDataModel
                if (homeDataGet!!.statusCode == SUCCESS_CODE) {
                    if (homeDataGet!!.data.isNotEmpty()) {
                        rv_offer_data.visibility = View.VISIBLE
                        tv_home_no_data.visibility = View.GONE
                        setHomeOfferData(homeDataGet!!.data as ArrayList<HomeDataModel.Data>)


                        /* if (homeDataGet.data.size == 1) {
                            if (homeDataGet.data[0].data_availabel == "1") {
                                startActivity(
                                    Intent(
                                        this@DashbordActivity,
                                        WhatchFaceDashBordActivity::class.java
                                    )
                                        .putExtra(
                                            "open_or_close",
                                            homeDataGet.data[0].getpremisecurrentstatus.status
                                        )
                                        .putExtra(
                                            "premiseId",
                                            homeDataGet.data[0].premiseId.toString()
                                        ).putExtra("mane_toolbar","visible")
                                )
                            } else {
                                Toast("No data avilable", true, this@DashbordActivity)
                            }
                        }*/



                    } else {
                        rv_offer_data.visibility = View.GONE
                        tv_home_no_data.visibility = View.VISIBLE
                    }
                }
            }
            USER_ACCESSS->{
                userAccessData = apiResponseManager.response as UserAccessData

                Log.e("fffff",userAccessData.toString())
                if(userAccessData!!.data.useraccess){
                    isUserHavingAccess = true
                    homeDataGet()
                }else{
                    isUserHavingAccess = false
                    rv_offer_data.visibility = View.GONE
                    tv_home_no_data.visibility = View.VISIBLE
                }

            }
        }
    }
}