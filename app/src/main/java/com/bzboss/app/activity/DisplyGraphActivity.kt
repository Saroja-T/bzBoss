    package com.bzboss.app.activity

    import android.Manifest
    import android.annotation.SuppressLint
    import android.app.DatePickerDialog
    import android.app.Dialog
    import android.app.ProgressDialog
    import android.content.ActivityNotFoundException
    import android.content.Context
    import android.content.DialogInterface
    import android.content.Intent
    import android.content.pm.PackageManager
    import android.content.pm.ResolveInfo
    import android.graphics.Color
    import android.graphics.DashPathEffect
    import android.net.Uri
    import android.os.AsyncTask
    import android.os.Build
    import android.os.Bundle
    import android.util.Log
    import android.view.View
    import android.view.ViewGroup
    import android.view.Window
    import android.widget.*
    import androidx.annotation.RequiresApi
    import androidx.core.app.ActivityCompat
    import androidx.core.content.ContextCompat
    import androidx.core.content.FileProvider
    import androidx.recyclerview.widget.GridLayoutManager
    import androidx.swiperefreshlayout.widget.CircularProgressDrawable
    import androidx.ui.material.AlertDialog
    import com.bumptech.glide.Glide
    import com.bzboss.app.R
    import com.bzboss.app.adapter.*
    import com.bzboss.app.custom.*
    import com.bzboss.app.model.HomeGraphData
    import com.bzboss.app.model.UserKnownVisitorsData
    import com.bzboss.app.model.UserStaffData
    import com.bzboss.app.restapi.ApiInitialize
    import com.bzboss.app.restapi.ApiRequest
    import com.bzboss.interfaceD.StaffClick
    import com.github.mikephil.charting.charts.LineChart
    import com.github.mikephil.charting.components.MarkerView
    import com.github.mikephil.charting.components.XAxis
    import com.github.mikephil.charting.data.Entry
    import com.github.mikephil.charting.data.LineData
    import com.github.mikephil.charting.data.LineDataSet
    import com.github.mikephil.charting.formatter.IFillFormatter
    import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
    import com.github.mikephil.charting.highlight.Highlight
    import com.github.mikephil.charting.interfaces.datasets.IDataSet
    import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
    import com.github.mikephil.charting.listener.OnChartValueSelectedListener
    import com.github.mikephil.charting.utils.MPPointF
    import com.iqamahtimes.app.custom.*
    import com.iqamahtimes.app.restapi.ApiResponseInterface
    import com.iqamahtimes.app.restapi.ApiResponseManager
    import kotlinx.android.synthetic.main.activity_disply_graph.*
    import kotlinx.android.synthetic.main.activity_disply_graph.llSelectedStaff
    import kotlinx.android.synthetic.main.activity_disply_graph.tv_open_close
    import kotlinx.android.synthetic.main.activity_disply_graph.tv_watch_place_city
    import kotlinx.android.synthetic.main.activity_disply_graph.tv_watch_place_name
    import kotlinx.android.synthetic.main.activity_disply_graph.tv_watch_time
    import kotlinx.android.synthetic.main.activity_primise_detail_activty.*
    import kotlinx.android.synthetic.main.activity_whatch_face_dash_bord.*
    import java.io.File
    import java.text.SimpleDateFormat
    import java.util.*
    import androidx.appcompat.app.AlertDialog



    class DisplyGraphActivity : ActivityBase(), ApiResponseInterface, StaffClick, KnownVisitorsClick {
        var REQUEST_WRITE_PERMISSION = 0
        private var values = ArrayList<Entry>()
        private var userStaffData:UserStaffData?=null
        private var userKnownVisitorsData:UserKnownVisitorsData?=null
        private var premiseID : String = ""
        private var startDate : String = ""
        private var endDate : String = ""
        private var status : String = ""
        private var lastUpdate : String = ""
        private var homeType : String = ""
        private var minValue : String = ""
        private var maxValue : String = ""
        private var premiseImage : String = ""
        private var typeName : String = ""
        private var imageType : String = ""
        private var selecetdMonth : String = ""
        var data1 : String = ""
        var isSwitchChecked : Boolean = true
        var view: View? = null

        @RequiresApi(Build.VERSION_CODES.R)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_disply_graph)

            /* Glide.with(this@DisplyGraphActivity)
                 .load("http://192.168.1.10/bizzboss/public/uploads/premise/4205fe62be8572d31978b22e0eeca4e2161959983919.png")
                 .into(im_offer_image)*/

            // GetIntentData
            endDate = intent.getStringExtra("end_date")!!
            startDate = intent.getStringExtra("start_date")!!
            premiseID = intent.getStringExtra("premiseId")!!
            status = intent.getStringExtra("status").toString()!!
            lastUpdate = intent.getStringExtra("last_update").toString()!!
            homeType = intent.getStringExtra("home_type")!!
            premiseImage = intent.getStringExtra("premise_image").toString()!!

            if(homeType== "openAt" || homeType== "first_customer" || homeType== "closeAt"){
                typeName = intent.getStringExtra("type_name").toString()!!
                imageType = intent.getStringExtra("image_type")!!
                Log.e("image_type123", imageType);
            }

            if(homeType== "staff" || homeType== "customer" || homeType== "known_visitors"){
                maxValue = intent.getStringExtra("max_value")!!
                minValue = intent.getStringExtra("min_vale")!!
            }


               /* startDate = "2021-07-01"
                endDate = "2021-07-07"
                premiseID = "4"
                status = "Closed"
                lastUpdate = "22:42 PM - Jul 07, 2021"
                homeType = "openAt"
                maxValue = "2"
                minValue = "2"
                imageType = "http://903113e41fad.ngrok.io/uploads/premisedailydata/0313bafd8a07adfe9d02aee9243d906b1625628648475.png"
                typeName = "00:34:43"
                imageType = " http://903113e41fad.ngrok.io/uploads/premisedailydata/0313bafd8a07adfe9d02aee9243d906b162562864867.png"
                typeName = "09:18:00"
                premiseImage = "http://bb6993d0e9a3.ngrok.io/uploads/premisecurrentstatus/0313bafd8a07adfe9d02aee9243d906b1625628648358.png"*/

            userPremiseGraphData()

            val cal: Calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("EEEE, MMM dd,yyyy")
            selecetdMonth = convertDate(sdf.format(cal.time), "EEEE, MMM dd,yyyy", "yyyy-MM-dd")
            tvMonthYearSelect.text = convertDate(sdf.format(cal.time), "EEEE, MMM dd,yyyy", "MMMM , yyyy")
            tvMonthYearSelect.setOnClickListener {
                MonthYearPickerDialog().apply {
                    setListener { view, year, month, dayOfMonth ->
                        var monthVal = "";
                        if(month<=9){
                            monthVal ="0"+month
                        }else{
                            monthVal = month.toString()
                        }
                        Log.d(TAG, "onCreate: " + "Set date: 01/$monthVal/$dayOfMonth")
                        selecetdMonth = "$dayOfMonth-$monthVal-01"
                        callmethod()
                    }

                    show(supportFragmentManager, "MonthYearPickerDialog")

                }
            }

            ibDownLoad.setOnClickListener {
                if(selecetdMonth!= ""){
                    if (ContextCompat.checkSelfPermission(this@DisplyGraphActivity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        download()
                    } else {
                        requestStoragePermission();
                    }
//                    requestPermission()
                }else{
                    Toast("Please select month and year", true, this@DisplyGraphActivity)

                }
            }

            tv_selected_date.text = convertDate(
                    endDate!!,
                    "yyyy-MM-dd",
                    "EEEE, MMMM dd, yyyy"
            )
            tvOnSelectedDate.text = convertDate(
                    endDate!!,
                    "yyyy-MM-dd",
                    "EEEE, MMMM dd, yyyy"
            )
            scSelected.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                isSwitchChecked = isChecked
                if (isChecked) {
                    llOnSelectedStaff.visibility = View.VISIBLE
                } else {
                    llOnSelectedStaff.visibility = View.GONE
                }
                //Log.e("isSwitchChecked", isSwitchChecked.toString())
            })

            //Log.d(TAG, "onCreate: "+"iscalled")
            val imagePopup = ImagePopup(this@DisplyGraphActivity)
            imagePopup.windowHeight = 800 // Optional

            imagePopup.windowWidth = 800 // Optional

            imagePopup.backgroundColor = Color.BLACK // Optional

            imagePopup.isFullScreen = true // Optional

            imagePopup.isHideCloseIcon = false // Optional

            imagePopup.isImageOnClickClose = true // Optional


            val imageView = im_view_detail

            imageView.setOnClickListener {
                /** Initiate Popup view
                * Initiate Popup view*/
                imagePopup.initiatePopupWithGlide(imageType);
                imagePopup.viewPopup()
            }
           /* im_view_detail.setOnClickListener{
                   showImageDialog(this@DisplyGraphActivity, imageType)


                // showImageDialog1(this@DisplyGraphActivity, imageType, im_view_detail)
            }*/

            tvDatePicker.setOnClickListener {
                createDialogWithoutDateField()?.show()
            }
           /* im_view_detail.setOnClickListener{
                 // showImageDialog(this@DisplyGraphActivity, imageType)


               // showImageDialog1(this@DisplyGraphActivity, imageType, im_view_detail)
           }*/

        }

        private fun callmethod() {
            tvMonthYearSelect.text = convertDate(selecetdMonth, "yyyy-MM-dd", "MMMM , yyyy")
        }

        override fun onRequestPermissionsResult(
                requestCode: Int,
                permissions: Array<out String>,
                grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
            if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               download()
            }else  {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_PERMISSION
            )
        } else {
            download()
        }
    }
        private fun requestStoragePermission() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                AlertDialog.Builder(this@DisplyGraphActivity)
                        .setTitle("Permission required")
                        .setMessage("This permission is needed because of this and that")
                        .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which -> ActivityCompat.requestPermissions(this@DisplyGraphActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_PERMISSION) })
                        .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                        .create().show()

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_PERMISSION)
            }
        }
        private fun createDialogWithoutDateField(): DatePickerDialog? {
            val dpd = DatePickerDialog(this, null, 2014, 1, 0)
            try {
                val datePickerDialogFields = dpd.javaClass.declaredFields
                for (datePickerDialogField in datePickerDialogFields) {
                    if (datePickerDialogField.name == "mDatePicker") {
                        datePickerDialogField.isAccessible = false
                        val datePicker = datePickerDialogField[dpd] as DatePicker
                        val datePickerFields = datePickerDialogField.type.declaredFields
                        for (datePickerField in datePickerFields) {
                            Log.i("test", datePickerField.name)
                            if ("mDaySpinner" == datePickerField.name) {
                                datePickerField.isAccessible = false
                                val dayPicker = datePickerField[datePicker]
                                (dayPicker as View).visibility = View.GONE
                            }
                        }
                    }
                }
            }catch (ex: Exception) {
            }
            return dpd
        }
        fun download() {
            Log.d("Download complete", "----------")
            var file_path : String =""
            var uri: Uri
            class DownloadFile : AsyncTask<String?, Void?, Void?>(){

                var uploading: ProgressDialog? = null

                override fun onPreExecute() {
                    super.onPreExecute()
                    uploading =
                            ProgressDialog.show(
                                    this@DisplyGraphActivity,
                                    "Please Wait",
                                    "PDF Downloading...",
                                    false,
                                    false
                            )
                }
                override fun doInBackground(vararg strings: String?): Void? {
                    Log.d(TAG, "doInBackground: ")
                    val fileUrl: String = strings.get(0)!! // -> http://maven.apache.org/maven-1.x/maven.pdf
                    val fileName: String = strings.get(1)!! // -> maven.pdf

                    val localStorage = getExternalFilesDir(null)
                    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US)
                    val now = Date()
                    var tempFilename :String = homeType+"_"+selecetdMonth+"_"
                    val storagePath = localStorage!!.absolutePath
                    val rootPath = "$storagePath/$tempFilename"
                    val file = File(rootPath + formatter.format(now).toString() + ".pdf")
                    try {
                        val permissionCheck = ContextCompat.checkSelfPermission(
                                this@DisplyGraphActivity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            if (!file.createNewFile()) {
                                Log.i("Test", "This file is already exist: " + file.absolutePath)
                            }
                            file_path = file.absolutePath


                            uri = if (Build.VERSION.SDK_INT < 24) {
                                Uri.fromFile(file)
                            } else {
                                Uri.parse(file.path) // My work-around for new SDKs, worked for me in Android 10 using Solid Explorer Text Editor as the external editor.
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    FileDownloader.downloadFile(fileUrl, file)
                    return null
                }
                override fun onPostExecute(aVoid: Void?) {
                    super.onPostExecute(aVoid)
                    Log.d(TAG, "onPostExecute: ")
                    uploading?.dismiss()
                    if(file_path!=""){
                        Log.d(TAG, "file path: ")
                        val d = Dialog(this@DisplyGraphActivity)
                        d.setTitle("File Download")
                        d.setContentView(R.layout.downloaded_dialog_layout)
                        val btnView: Button = d.findViewById<View>(R.id.btnView) as Button
                        val btnShare: Button = d.findViewById<View>(R.id.btnShare) as Button
                        val tvTitle: TextView = d.findViewById<View>(R.id.tvTitle) as TextView
                        val tvClose: TextView = d.findViewById<View>(R.id.tvClose) as TextView


                        btnView.setOnClickListener {


                            Log.d(TAG, "onPostExecute: " + file_path);
//                            Log.d(TAG, "onPostExecute: " + localStorage);

                            val file: File = File(
                                    "$file_path"
                            )
                            Log.d(TAG, "onPostExecute: " + file.toString())

                            val photoURI = FileProvider.getUriForFile(this@DisplyGraphActivity,
                                    this@DisplyGraphActivity.getApplicationContext().getPackageName()
                                            .toString() + ".provider",
                                    file
                            )


                            val target = Intent(Intent.ACTION_VIEW)
                            target.setDataAndType(
                                    photoURI,
                                    "application/pdf"
                            )
                            target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                            val intent = Intent.createChooser(target, "Open File")
                            try {
                                startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                // Instruct the user to install a PDF reader here, or something
                            }
                            d.dismiss()
                        }
                        btnShare.setOnClickListener {
                            val intentShareFile = Intent(Intent.ACTION_SEND)
                            val fileWithinMyDir: File = File(file_path)
                            val photoURI = FileProvider.getUriForFile(this@DisplyGraphActivity,
                                    this@DisplyGraphActivity.getApplicationContext().getPackageName()
                                            .toString() + ".provider",
                                    fileWithinMyDir
                            )

                            intentShareFile.type = "application/pdf"
                            intentShareFile.putExtra(
                                    Intent.EXTRA_STREAM,
                                    photoURI
                            )
                            intentShareFile.putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Sharing File..."
                            )
                            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...")
                            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                            val chooser = Intent.createChooser(intentShareFile, "Share File")

                            val resInfoList: List<ResolveInfo> = getPackageManager().queryIntentActivities(
                                    chooser,
                                    PackageManager.MATCH_DEFAULT_ONLY
                            )

                            for (resolveInfo in resInfoList) {
                                val packageName = resolveInfo.activityInfo.packageName
                                grantUriPermission(
                                        packageName,
                                        photoURI,
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                                )
                            }


                            startActivity(chooser)
                            //  d.dismiss()
                        }

                        tvClose.setOnClickListener {
                            d.dismiss()
                        }

                        d.show()

                        val window: Window? = d.getWindow()
                        window?.setLayout(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )


                    }
                }
            }
            Log.d(TAG, "download: homeType__" + homeType)
            var premise_id = premiseID
            var date = selecetdMonth
            var type = ""
            if(homeType == "first_customer"){
                type = "First_customer"
            }else if(homeType == "openAt"){
                type = "Opened_at"
            }else if(homeType == "customer"){
                type = "Ind-cust"
            }else if(homeType == "staff"){
                type = "Total-staff"
            }else if(homeType == "closeAt"){
                type = "Closed_at"
            }else if(homeType == "known_visitors"){
                type = "Total-known"
            }
            var downLoadURL = ApiInitialize.LOCAL_URL+"user/pdfgraph?premise_id=$premise_id&date=$date&type=$type"
            Log.d(TAG, "download: " + downLoadURL)
            DownloadFile().execute(
                    downLoadURL,
                    "maven.pdf"
            )
            /*DownloadFile().execute(
                "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
                "maven.pdf"
            )*/
            Log.d("Download complete", "----------")
        }
        @RequiresApi(Build.VERSION_CODES.R)
        private fun userPremiseGraphData() {
            if (isOnline(this@DisplyGraphActivity)) {
                Log.e(TAG, "ID premiseId=>")
                ApiRequest(
                        this,
                        ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userPremiseGraphData(
                                "Bearer " + getUserModel()!!.data.token,
                                encrypt(startDate!!),
                                encrypt(endDate!!),
                                encrypt(premiseID!!)
                        ),
                        HOME_GRAPH_DATA, true, this
                )
            } else {
                showSnackBar(
                        ll_graph_view,
                        getString(R.string.no_internet),
                        ACTIONSNACKBAR.DISMISS
                )
            }
        }



        @RequiresApi(Build.VERSION_CODES.R)
        fun userStaffData(b: Boolean) {
            if (isOnline(this@DisplyGraphActivity)) {
                ApiRequest(
                        this,
                        ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userStaffData(
                                "Bearer " + getUserModel()!!.data.token,
                                encrypt(premiseID!!),
                                encrypt(startDate!!),
                                encrypt(endDate!!),
                        ),
                        USER_STAFF_DATA, b, this
                )
            } else {
                showSnackBar(
                        ll_graph_view,
                        getString(R.string.no_internet),
                        ACTIONSNACKBAR.DISMISS
                )
            }
        }

        @RequiresApi(Build.VERSION_CODES.R)
        override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
            Log.e("apiResponseManager12", apiResponseManager.response.toString())
            when (apiResponseManager.type) {
                HOME_GRAPH_DATA -> {
                    var homeGraphData = apiResponseManager.response as HomeGraphData
                    if (homeGraphData.statusCode == SUCCESS_CODE) {
                        getPremiseDataSet(
                                homeGraphData.data.premisedata!!,
                                status.toString(),
                                lastUpdate.toString()
                        )
                        Log.e("homeGraphData.dat", homeGraphData.data.toString());
                        setTypeWiseSetData(homeGraphData.data)
                    }
                }
                USER_STAFF_DATA -> {

                    userStaffData = apiResponseManager.response as UserStaffData
                    //val staffDataGet = apiResponseManager.response as UserStaffData
                    Log.e("staffGraphData.dat", userStaffData!!.data.toString());
                    if (userStaffData!!.statusCode == SUCCESS_CODE) {
                        setHomeOfferData(userStaffData!!.data as ArrayList<UserStaffData.Data>)
                    }
                }
                USER_KNOWN_DETAILS_DATA -> {

                    userKnownVisitorsData = apiResponseManager.response as UserKnownVisitorsData
                    //val staffDataGet = apiResponseManager.response as UserStaffData
                    Log.e("staffGraphData.dat", userKnownVisitorsData!!.data.toString());
                    if (userKnownVisitorsData!!.status_code == SUCCESS_CODE) {
                        setKnownVisitorsData(userKnownVisitorsData!!.data as ArrayList<UserKnownVisitorsData.Data>)
                    }
                }
            }
        }

        private fun setKnownVisitorsData(data: ArrayList<UserKnownVisitorsData.Data>) {
            if(isSwitchChecked){
                llOnSelectedStaff.visibility = View.VISIBLE
            }else{
                llOnSelectedStaff.visibility = View.GONE
            }
            if (data.size > 0) {
                val localStorage = getExternalFilesDir(null)

                rv_set_staff_images.setHasFixedSize(true)
                rv_set_staff_images.layoutManager = GridLayoutManager(this@DisplyGraphActivity, 2)
                val treatment_reason =
                        KnownVisiorsListAdapter(
                                this@DisplyGraphActivity,
                                data,
                                this,
                                "known_visitors_graph"

                        )  // Create adapter object
                rv_set_staff_images.adapter = treatment_reason
                rv_set_staff_images.visibility = View.VISIBLE
                tv_no_staff_available.visibility = View.GONE

            } else {
                tv_no_staff_available.visibility = View.VISIBLE
                tv_no_staff_available.text = "No known visitors data available"
                rv_set_staff_images.visibility = View.GONE

            }
        }

        fun tempGraphDataSet(type: String, data: HomeGraphData.Data) {
            values = ArrayList<Entry>()
            values.clear()
            val xAxisLabelList = ArrayList<String>()

            //entries.add(Entry(data.getDate().getTime().toFloat(), data.getValueY().toFloat()))
            /* values.add(
                 Entry(
                     2f, 0F
                 )
             )
             values.add(
                 Entry(
                     1f, 0f
                 )
             )*/

            if (type == "customer") {

                for (i in data.premisedailydata.indices) {

                    Log.e(
                            TAG,
                            "avarage is=> " + data.premisedailydata[i].date + "---" + data.premisedailydata[i].numberOfCustomersMax + "--" + data.premisedailydata[i].numberOfCustomersMin
                    )

                    if (data.premisedailydata[i].numberOfCustomersMax != 0 && data.premisedailydata[i].numberOfCustomersMin != 0) {
                        val avagareIs = (data.premisedailydata[i].numberOfCustomersMax + data.premisedailydata[i].numberOfCustomersMin) / 2
                        Log.e(TAG, "avarage is=> " + avagareIs.toFloat())
                        values.add(
                                Entry(
                                        i.toFloat(),
                                        avagareIs.toFloat()
                                )
                        )

                    } else {
                        values.add(
                                Entry(
                                        i.toFloat(),
                                        0f
                                )
                        )
                    }

                    xAxisLabelList.add(
                            convertDate(data.premisedailydata[i].date, "yyyy-MM-dd", "dd/MM")
                    )

                }
                setLineChartDataWithoutDate(
                        10f,
                        xAxisLabelList,
                        lc_my_goal_activity_log_chart,
                        data,
                        homeType
                )

            } else if (type == "staff") {

                for (i in data.premisedailydata.indices) {

                    if (data.premisedailydata[i].numberOfStaffMax != 0 && data.premisedailydata[i].numberOfStaffMin != 0) {
                        val avagareIs =
                                (data.premisedailydata[i].numberOfStaffMax + data.premisedailydata[i].numberOfStaffMin) / 2
                        Log.e(TAG, "avarage is=> " + avagareIs.toFloat())
                        values.add(
                                Entry(
                                        i.toFloat(),
                                        avagareIs.toFloat()
                                )
                        )

                    } else {
                        values.add(
                                Entry(
                                        i.toFloat(),
                                        0f
                                )
                        )
                    }

                    xAxisLabelList.add(
                            convertDate(data.premisedailydata[i].date, "yyyy-MM-dd", "dd/MM")
                    )

                }
                setLineChartDataWithoutDate(
                        10f,
                        xAxisLabelList,
                        lc_my_goal_activity_log_chart,
                        data,
                        homeType
                )

            } else if (type == "openAt") {
                for (i in data.premisedailydata.indices) {

                    if (data.premisedailydata[i].date.isNotEmpty() && data.premisedailydata[i].openedAt.isNotEmpty()) {
                        values.add(
                                Entry(
                                        i.toFloat(),
                                        conVertDateTimeToTimeStarp(/*data.premisedailydata[i].date + " " +*/
                                                data.premisedailydata[i].openedAt
                                        ).toFloat()
                                )
                        )

                        xAxisLabelList.add(
                                convertDate(data.premisedailydata[i].date, "yyyy-MM-dd", "dd/MM")
                        )

                    }
                }
                setLineChartData(10f, xAxisLabelList, lc_my_goal_activity_log_chart, data, homeType)
                //setLineChartDataWithoutDate(10f, xAxisLabelList, lc_my_goal_activity_log_chart)
            } else if (type == "first_customer") {

                for (i in data.premisedailydata.indices) {

                    if (data.premisedailydata[i].date.isNotEmpty() && data.premisedailydata[i].firstCustomerTime.isNotEmpty()) {
                        values.add(
                                Entry(
                                        i.toFloat(),
                                        conVertDateTimeToTimeStarp(data.premisedailydata[i].firstCustomerTime).toFloat()
                                )
                        )

                        xAxisLabelList.add(
                                convertDate(data.premisedailydata[i].date, "yyyy-MM-dd", "dd/MM")
                        )

                    }

                }
                setLineChartData(10f, xAxisLabelList, lc_my_goal_activity_log_chart, data, homeType)
            }else if (type == "closeAt") {

                for (i in data.premisedailydata.indices) {
                    Log.e(
                            "dfdfdfdfd",
                            data.premisedailydata[i].date + "--" + data.premisedailydata[i].closedAt
                    )
                    if (data.premisedailydata[i].date.isNotEmpty() && data.premisedailydata[i].closedAt.isNotEmpty()) {
                        values.add(
                                Entry(
                                        i.toFloat(),
                                        conVertDateTimeToTimeStarp(/*data.premisedailydata[i].date + " " +*/
                                                data.premisedailydata[i].closedAt
                                        ).toFloat()
                                )
                        )

                        xAxisLabelList.add(
                                convertDate(data.premisedailydata[i].date, "yyyy-MM-dd", "dd/MM")
                        )

                    }
                }
                setLineChartData(10f, xAxisLabelList, lc_my_goal_activity_log_chart, data, homeType)
                //setLineChartDataWithoutDate(10f, xAxisLabelList, lc_my_goal_activity_log_chart)
            }else if (type == "known_visitors") {

                for (i in data.premisedailydata.indices) {

                    if (data.premisedailydata[i].numberOfKnownVisitorsMax != 0 && data.premisedailydata[i].numberOfKnownVisitorsMin != 0) {
                        val avagareIs =
                                (data.premisedailydata[i].numberOfKnownVisitorsMax + data.premisedailydata[i].numberOfKnownVisitorsMin) / 2
                        Log.e(TAG, "avarage is=> " + avagareIs.toFloat())
                        values.add(
                                Entry(
                                        i.toFloat(),
                                        avagareIs.toFloat()
                                )
                        )

                    } else {
                        values.add(
                                Entry(
                                        i.toFloat(),
                                        0f
                                )
                        )
                    }

                    xAxisLabelList.add(
                            convertDate(data.premisedailydata[i].date, "yyyy-MM-dd", "dd/MM")
                    )

                }
                setLineChartDataWithoutDate(
                        10f,
                        xAxisLabelList,
                        lc_my_goal_activity_log_chart,
                        data,
                        homeType
                )

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

        @SuppressLint("UseSparseArrays")
        fun setLineChartData(
                lineValue: Float, weekdays: ArrayList<String>, row_line_chart: LineChart,
                homeGraphData: HomeGraphData.Data, homeType: String
        ) {
            row_line_chart.clear()
            val zeroNumberList = ArrayList<Entry>()
            val otherNumberList = ArrayList<Entry>()

            zeroNumberList.clear()
            otherNumberList.clear()

            for (i in values.indices) {
                /*if (this.values[i].y == 0f) {
                    zeroNumberList.add(Entry(i.toFloat(), this.values[i].y))
                }*/
                zeroNumberList.add(Entry(i.toFloat(), this.values[i].y))
            }

            for (i in this.values.indices) {
                /* if (this.values[i].y != 0f) {
                     otherNumberList.add(Entry(i.toFloat(), this.values[i].y))
                 }*/
                otherNumberList.add(Entry(i.toFloat(), this.values[i].y))
            }
            Log.e("count123", zeroNumberList.size.toString())
            Log.e("count123", otherNumberList.size.toString())


            val set1 = LineDataSet(zeroNumberList, "")
            val set2 = LineDataSet(otherNumberList, "")
            set1.setDrawIcons(false)
            set2.setDrawIcons(false)
            row_line_chart.setBackgroundColor(
                    ContextCompat.getColor(
                            this@DisplyGraphActivity,
                            R.color.white
                    )
            )


            row_line_chart.description.isEnabled = false
            row_line_chart.setTouchEnabled(true)


            val xAxis = row_line_chart.xAxis
            xAxis.enableGridDashedLine(10f, 0f, 0f)
            xAxis.gridColor = ContextCompat.getColor(this@DisplyGraphActivity, R.color.black)
            xAxis.gridLineWidth = 1f
            xAxis.setCenterAxisLabels(false)

            xAxis.granularity = 1f
            row_line_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            row_line_chart.xAxis.setDrawGridLines(false)



            row_line_chart.xAxis.axisLineWidth = 1f
            //xAxis.labelCount = weekdays.size
            xAxis.valueFormatter = IndexAxisValueFormatter(weekdays)
            // xAxis.valueFormatter = IndexAxisValueFormatter(weekdays)
            xAxis.setDrawAxisLine(true)
            xAxis.setDrawLabels(true)
            // xAxis.labelRotationAngle = -40f
            xAxis.mLabelWidth = 1


    //        row_line_chart.xAxis.setDrawLabels(true)
    //        xAxis.setDrawAxisLine(true)
    //        xAxis.setDrawLabels(true)


            val yAxis = row_line_chart.axisLeft
            row_line_chart.axisLeft.isEnabled = true
            row_line_chart.axisRight.isEnabled = false

            // row_line_chart.axisLeft.axisMinimum = 0f


            row_line_chart.axisLeft.setDrawGridLines(true)
            yAxis.setDrawLabels(true)
            yAxis.setDrawAxisLine(false)
            yAxis.granularity = 1f
            row_line_chart.animateXY(500, 500)
            row_line_chart.legend.isEnabled = false
            //l.form = Legend.LegendForm.LINE
            yAxis.valueFormatter = HourYAxisValueFormatter()


            // black lines and points
            set1.color = ContextCompat.getColor(this@DisplyGraphActivity, R.color.green)
            set2.color = ContextCompat.getColor(this@DisplyGraphActivity, R.color.green)
            set1.setCircleColor(ContextCompat.getColor(this@DisplyGraphActivity, R.color.green))
            set2.setCircleColor(ContextCompat.getColor(this@DisplyGraphActivity, R.color.green))
            set1.lineWidth = 2.5f
            set2.lineWidth = 2.5f
            set1.circleRadius = 6.5f
            set2.circleRadius = 6.5f
            set1.circleHoleRadius = 4f
            set2.circleHoleRadius = 4f
            set1.setDrawValues(false)
            set2.setDrawValues(false)

            // draw points as solid circles
            set1.setDrawCircleHole(true)


            // customize legend entry
            set1.formLineWidth = 1f
            set2.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set2.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            set2.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f
            set2.valueTextSize = 9f

            // draw selection line as dashed
            /* set1.enableDashedHighlightLine(10f, 5f, 0f)
             set2.enableDashedHighlightLine(10f, 5f, 0f)*/

            // set the filled area

            set2.setDrawFilled(false)
            set1.fillFormatter =
                    IFillFormatter { _, _ -> lc_my_goal_activity_log_chart!!.axisLeft.axisMinimum }
            set2.fillFormatter =
                    IFillFormatter { _, _ -> lc_my_goal_activity_log_chart!!.axisLeft.axisMinimum }
            // set color of filled area
            /*val drawable = ContextCompat.getColor(this@DisplyGraphActivity, R.color.background)
            set1.fillColor = drawable
            set2.fillColor = drawable*/

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1)
            dataSets.add(set2)
            set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            set2.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

            // create a data object with the data sets
            val data = LineData(dataSets)
            row_line_chart.data = data


            val mv = CustomMarkerView(this@DisplyGraphActivity, R.layout.tooltip)

            row_line_chart.setMarkerView(mv)

            row_line_chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                @RequiresApi(Build.VERSION_CODES.R)
                override fun onValueSelected(e: Entry, h: Highlight?) {
                    row_line_chart.getXAxis().getValueFormatter().getFormattedValue(
                            e.x,
                            row_line_chart.getXAxis()
                    )
                    toolTipTime = ""
                    toolTipDate = ""

                    for (i in homeGraphData.premisedailydata.indices) {
                        if (homeType == "first_customer") {
                            if (homeGraphData.premisedailydata[i].date.isNotEmpty() &&
                                    homeGraphData.premisedailydata[i].firstCustomerTime.isNotEmpty()
                            ) {
                                Log.e(
                                        "value123",
                                        conVertDateTimeToTimeStarp(homeGraphData.premisedailydata[i].firstCustomerTime).toFloat()
                                                .toString()
                                )
                                Log.e("value123", e.y.toString())
                                if (conVertDateTimeToTimeStarp(homeGraphData.premisedailydata[i].firstCustomerTime).toFloat() == e.y) {
                                    toolTipTime = convertDate(
                                            homeGraphData.premisedailydata[i].firstCustomerTime,
                                            "HH:mm:ss",
                                            "hh:mm a"
                                    )
                                    toolTipDate =
                                            dateConvert(homeGraphData.premisedailydata[i].date);
                                }
                                if (row_line_chart.getXAxis().getValueFormatter().getFormattedValue(
                                                e.x,
                                                row_line_chart.getXAxis()
                                        ) == convertDate(
                                                homeGraphData.premisedailydata[i].date,
                                                "yyyy-MM-dd",
                                                "dd/MM"
                                        )
                                ) {

                                }
                            }
                        } else if (homeType == "openAt") {
                            if (homeGraphData.premisedailydata[i].date.isNotEmpty() && homeGraphData.premisedailydata[i].openedAt.isNotEmpty()) {
                                if (homeGraphData.premisedailydata[i].date.isNotEmpty() && homeGraphData.premisedailydata[i].openedAt.isNotEmpty()) {
                                    Log.e("cvbcmbvc", "dfjdfdhfhkd")
                                    if (conVertDateTimeToTimeStarp(homeGraphData.premisedailydata[i].openedAt).toFloat() == e.y) {
                                        toolTipTime = convertDate(
                                                homeGraphData.premisedailydata[i].openedAt,
                                                "HH:mm:ss",
                                                "hh:mm a"
                                        )
                                        toolTipDate =
                                                dateConvert(homeGraphData.premisedailydata[i].date);
                                    }
                                    if (row_line_chart.getXAxis().getValueFormatter()
                                                    .getFormattedValue(
                                                            e.x,
                                                            row_line_chart.getXAxis()
                                                    ) == convertDate(
                                                    homeGraphData.premisedailydata[i].date,
                                                    "yyyy-MM-dd",
                                                    "dd/MM"
                                            )
                                    ) {

                                        /* convertDate(
                                            homeGraphData.premisedailydata[i].date,
                                            "yyyy-MM-dd",
                                            "MMM dd, yyyy"
                                        )*/
                                    }
                                }
                            }

                        } else if (homeType == "closeAt") {
                            if (homeGraphData.premisedailydata[i].date.isNotEmpty() && homeGraphData.premisedailydata[i].closedAt.isNotEmpty()) {
                                if (homeGraphData.premisedailydata[i].date.isNotEmpty() && homeGraphData.premisedailydata[i].closedAt.isNotEmpty()) {
                                    Log.e("cvbcmbvc", "dfjdfdhfhkd")
                                    if (conVertDateTimeToTimeStarp(homeGraphData.premisedailydata[i].closedAt).toFloat() == e.y) {
                                        toolTipTime = convertDate(
                                                homeGraphData.premisedailydata[i].closedAt,
                                                "HH:mm:ss",
                                                "hh:mm a"
                                        )
                                        toolTipDate =
                                                dateConvert(homeGraphData.premisedailydata[i].date);
                                    }
                                    if (row_line_chart.getXAxis().getValueFormatter()
                                                    .getFormattedValue(
                                                            e.x,
                                                            row_line_chart.getXAxis()
                                                    ) == convertDate(
                                                    homeGraphData.premisedailydata[i].date,
                                                    "yyyy-MM-dd",
                                                    "dd/MM"
                                            )
                                    ) {

                                        /* convertDate(
                                             homeGraphData.premisedailydata[i].date,
                                             "yyyy-MM-dd",
                                             "MMM dd, yyyy"
                                         )*/
                                    }
                                }
                            }

                        }


                    }


                    val highlight = arrayOfNulls<Highlight>(
                            row_line_chart.getData().getDataSets().size
                    )
                    for (j in 0 until row_line_chart.getData().getDataSets().size) {
                        val iDataSet: IDataSet<*> = row_line_chart.getData().getDataSets().get(j)
                        for (i in (iDataSet as LineDataSet).values.indices) {
                            if ((iDataSet as LineDataSet).values[i].x == e.x) {
                                highlight[j] = Highlight(e.x, e.y, j)
                                //toolTipDate = row_line_chart.getXAxis().getValueFormatter().getFormattedValue(e.x, row_line_chart.getXAxis())
                            }
                        }
                    }
                    Log.e("graphhhhhh", e.x.toString())
                    Log.e("graphhhhhh", e.y.toString())
                    row_line_chart.highlightValues(highlight)
                    row_line_chart.marker

                    Log.e(
                            "VAL SELECTED", "Value: " + e.data + ", index: " + h!!.x.toInt()
                            + ", DataSet index: "
                    );
                    val postion = h.x.toString()
                    Log.e("dfdfjd", "Postion IS that" + postion)

                }

                override fun onNothingSelected() {}
            })

            /*row_line_chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onNothingSelected() {

                }

                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    Log.e(
                        "VAL SELECTED", "Value: " + e + ", index: " + h!!.x.toInt()
                                + ", DataSet index: "
                    );
                    val postion = h.x.toInt()
                    Log.e(TAG, "Postion IS that" + postion)


                    Log.e("TAG", "data id" + data_line_set!!.data[postion].data)



                    if (e!!.y != 0.0f) {
                        *//* val mv = YourLineChartMarkerView(
                             this@MyGoalActivity,
                             R.layout.custom_linechart_marker_view,
                             data_line_set!!.data[postion].data,
                             data_line_set!!.data[postion].date
                         )*//*

                        showDialog(
                            data_line_set!!.data[postion].data,
                            data_line_set!!.data[postion].date
                        )

                        //  lc_my_goal_activity_log_chart.marker = mv
                    } else {
                        lc_my_goal_activity_log_chart.highlightValue(null)
                    }
                }

            })*/


        }


        private fun dateConvert(date: String): String {
            var date = date
            var spf = SimpleDateFormat("yyyy-MM-dd")
            val newDate = spf.parse(date)
            spf = SimpleDateFormat("MMM dd, yyyy")
            date = spf.format(newDate)
            return date.toString()
        }

        fun setLineChartDataWithoutDate(
                lineValue: Float,
                weekdays: ArrayList<String>,
                row_line_chart: LineChart,
                homeGraphData: HomeGraphData.Data,
                homeType: String
        ) {

            row_line_chart.clear()
            val zeroNumberList = ArrayList<Entry>()
            val otherNumberList = ArrayList<Entry>()


            zeroNumberList.clear()
            otherNumberList.clear()


            for (i in values.indices) {
                /*if (this.values[i].y == 0f) {
                    zeroNumberList.add(Entry(i.toFloat(), this.values[i].y))
                }*/
                    Log.e("zeroNumberList", this.values[i].y.toString())
                zeroNumberList.add(Entry(i.toFloat(), this.values[i].y))
            }

            for (i in this.values.indices) {
                /* if (this.values[i].y != 0f) {
                     otherNumberList.add(Entry(i.toFloat(), this.values[i].y))
                 }*/
                Log.e("otherNumberList", this.values[i].y.toString())
                otherNumberList.add(Entry(i.toFloat(), this.values[i].y))
            }


            val set1 = LineDataSet(zeroNumberList, "")
            val set2 = LineDataSet(otherNumberList, "")
            set1.setDrawIcons(false)
            set2.setDrawIcons(false)
            row_line_chart.setBackgroundColor(
                    ContextCompat.getColor(
                            this@DisplyGraphActivity,
                            R.color.white
                    )
            )


            row_line_chart.description.isEnabled = false
            row_line_chart.setTouchEnabled(true)


            val xAxis = row_line_chart.xAxis
            xAxis.enableGridDashedLine(10f, 0f, 0f)
            xAxis.gridColor = ContextCompat.getColor(this@DisplyGraphActivity, R.color.black)
            xAxis.gridLineWidth = 1f
            xAxis.setCenterAxisLabels(false)
            xAxis.granularity = 1f
            row_line_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            row_line_chart.xAxis.setDrawGridLines(false)
            row_line_chart.xAxis.axisLineWidth = 1f
            //xAxis.labelCount = weekdays.size
            xAxis.valueFormatter = IndexAxisValueFormatter(weekdays)
            // xAxis.valueFormatter = IndexAxisValueFormatter(weekdays)
            xAxis.setDrawAxisLine(true)
            xAxis.setDrawLabels(true)
            // xAxis.labelRotationAngle = -40f
            xAxis.mLabelWidth = 5


    //        row_line_chart.xAxis.setDrawLabels(true)
    //        xAxis.setDrawAxisLine(true)
    //        xAxis.setDrawLabels(true)


            val yAxis = row_line_chart.axisLeft
            row_line_chart.axisLeft.isEnabled = true
            row_line_chart.axisRight.isEnabled = false

            // row_line_chart.axisLeft.axisMinimum = 0f


            row_line_chart.axisLeft.setDrawGridLines(true)
            yAxis.granularity = 1F
            yAxis.setDrawLabels(true)
            yAxis.setDrawAxisLine(false)
            row_line_chart.animateXY(500, 500)
            row_line_chart.legend.isEnabled = false
            //l.form = Legend.LegendForm.LINE
            //yAxis.valueFormatter = HourYAxisValueFormatter()
              //yAxis.valueFormatter = IndexAxisValueFormatter(yAxisLabelList)


            // black lines and points
            // black lines and points
            set1.color = ContextCompat.getColor(this@DisplyGraphActivity, R.color.green)
            set2.color = ContextCompat.getColor(this@DisplyGraphActivity, R.color.green)
            set1.setCircleColor(ContextCompat.getColor(this@DisplyGraphActivity, R.color.green))
            set2.setCircleColor(ContextCompat.getColor(this@DisplyGraphActivity, R.color.green))
            set1.lineWidth = 2.5f
            set2.lineWidth = 2.5f
            set1.circleRadius = 6.5f
            set2.circleRadius = 6.5f
            set1.circleHoleRadius = 4f
            set2.circleHoleRadius = 4f
            set1.setDrawValues(false)
            set2.setDrawValues(false)

            // draw points as solid circles
            set1.setDrawCircleHole(true)
            set2.setDrawCircleHole(true)

            // customize legend entry
            set1.formLineWidth = 1f
            set2.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set2.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            set2.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f
            set2.valueTextSize = 9f

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set2.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(false)
            set2.setDrawFilled(false)
            set1.fillFormatter =
                    IFillFormatter { _, _ -> lc_my_goal_activity_log_chart!!.axisLeft.axisMinimum }
            set2.fillFormatter =
                    IFillFormatter { _, _ -> lc_my_goal_activity_log_chart!!.axisLeft.axisMinimum }
            // set color of filled area
            val drawable = ContextCompat.getColor(this@DisplyGraphActivity, R.color.background)
            set1.fillColor = drawable
            set2.fillColor = drawable

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1)
            dataSets.add(set2)
            set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            set2.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            // create a data object with the data sets
            val data = LineData(dataSets)

            row_line_chart.data = data
           /* row_line_chart.setMarker(object : MarkerView(this@DisplyGraphActivity, R.layout.tooltip) {
                // tooltip is layout you want to set as markerview, for background use a drawble

                override fun refreshContent(e: Entry, highlight: Highlight) {
                    super.refreshContent(e, highlight)
                }
                override fun getOffset(): MPPointF {
                    return MPPointF((-(width / 2)).toFloat(), (-height).toFloat()) // place the midpoint of marker over the bar
                }
            })*/


            val mv = CustomMarkerView(this@DisplyGraphActivity, R.layout.tooltip)

            row_line_chart.setMarkerView(mv)

            row_line_chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                @RequiresApi(Build.VERSION_CODES.R)
                override fun onValueSelected(e: Entry, h: Highlight?) {

                    toolTipTime = ""
                    toolTipDate = ""
                    row_line_chart.getXAxis().getValueFormatter().getFormattedValue(
                            e.x,
                            row_line_chart.getXAxis()
                    )
                    Log.e(
                            "graphhhhhh",
                            row_line_chart.getXAxis().getValueFormatter().getFormattedValue(
                                    e.x,
                                    row_line_chart.getXAxis()
                            ).toString()
                    )

                    for (i in homeGraphData.premisedailydata.indices) {

                        if (homeType == "staff") {
                            if (homeGraphData.premisedailydata[i].numberOfStaffMax != 0 && homeGraphData.premisedailydata[i].numberOfStaffMin != 0) {
                                val avagareIs =
                                        (homeGraphData.premisedailydata[i].numberOfStaffMax + homeGraphData.premisedailydata[i].numberOfStaffMin) / 2
                                if (avagareIs.toFloat() == e.y) {
                                    toolTipTime = avagareIs.toString()
                                    if (row_line_chart.getXAxis().getValueFormatter()
                                                    .getFormattedValue(
                                                            e.x,
                                                            row_line_chart.getXAxis()
                                                    ) == convertDate(
                                                    homeGraphData.premisedailydata[i].date,
                                                    "yyyy-MM-dd",
                                                    "dd/MM"
                                            )
                                    ) {
                                        toolTipDate =
                                                dateConvert(homeGraphData.premisedailydata[i].date)
                                    }

                                    Log.e("ggaggagga", toolTipTime + "--" + toolTipDate)
                                }

                            }

                        } else if (homeType == "customer") {
                            val avagareIs =
                                    (homeGraphData.premisedailydata[i].numberOfCustomersMax + homeGraphData.premisedailydata[i].numberOfCustomersMin) / 2
                            if (avagareIs.toFloat() == e.y) {
                                toolTipTime = avagareIs.toString()

                                if (row_line_chart.getXAxis().getValueFormatter().getFormattedValue(
                                                e.x,
                                                row_line_chart.getXAxis()
                                        ) == convertDate(
                                                homeGraphData.premisedailydata[i].date,
                                                "yyyy-MM-dd",
                                                "dd/MM"
                                        )
                                ) {
                                    toolTipDate =
                                            dateConvert(homeGraphData.premisedailydata[i].date)
                                }

                                Log.e("ggaggagga", toolTipTime + "--" + toolTipDate)
                            }

                        } else if (homeType == "known_visitors") {
                            if (homeGraphData.premisedailydata[i].numberOfKnownVisitorsMax != 0 && homeGraphData.premisedailydata[i].numberOfKnownVisitorsMin != 0) {
                                val avagareIs =
                                        (homeGraphData.premisedailydata[i].numberOfKnownVisitorsMax + homeGraphData.premisedailydata[i].numberOfKnownVisitorsMin) / 2
                                if (avagareIs.toFloat() == e.y) {
                                    toolTipTime = avagareIs.toString()
                                    if (row_line_chart.getXAxis().getValueFormatter()
                                                    .getFormattedValue(
                                                            e.x,
                                                            row_line_chart.getXAxis()
                                                    ) == convertDate(
                                                    homeGraphData.premisedailydata[i].date,
                                                    "yyyy-MM-dd",
                                                    "dd/MM"
                                            )
                                    ) {
                                        toolTipDate =
                                                dateConvert(homeGraphData.premisedailydata[i].date)
                                    }

                                    Log.e("ggaggagga", toolTipTime + "--" + toolTipDate)
                                }

                            }

                        }

                    }


                    if (toolTipTime.isEmpty()) {
                        toolTipTime = "0"
                    }
                    if (toolTipDate.isEmpty()) {
                        toolTipDate = ""
                    }


                    if (homeType == "staff") {


                        updateStaffDetails(

                                row_line_chart.getXAxis().getValueFormatter().getFormattedValue(
                                        e.x,
                                        row_line_chart.getXAxis()
                                ).toString(), homeGraphData
                        );
                    }
                    if (homeType == "known_visitors") {

                        updateKnownVisitorsDetails(
                                row_line_chart.getXAxis().getValueFormatter().getFormattedValue(
                                        e.x,
                                        row_line_chart.getXAxis()
                                ).toString(), homeGraphData
                        );
                    }


                    val highlight = arrayOfNulls<Highlight>(
                            row_line_chart.getData().getDataSets().size
                    )
                    for (j in 0 until row_line_chart.getData().getDataSets().size) {
                        val iDataSet: IDataSet<*> = row_line_chart.getData().getDataSets().get(j)
                        for (i in (iDataSet as LineDataSet).values.indices) {
                            if ((iDataSet as LineDataSet).values[i].x == e.x) {
                                highlight[j] = Highlight(e.x, e.y, j)
                                if (toolTipDate == "") {
                                    for (i in homeGraphData.premisedailydata.indices) {
                                        if (row_line_chart.getXAxis().getValueFormatter()
                                                        .getFormattedValue(
                                                                e.x,
                                                                row_line_chart.getXAxis()
                                                        ) == convertDate(
                                                        homeGraphData.premisedailydata[i].date,
                                                        "yyyy-MM-dd",
                                                        "dd/MM"
                                                )
                                        ) {
                                            toolTipDate =
                                                    dateConvert(homeGraphData.premisedailydata[i].date)
                                        }

                                    }
                                }
                            }
                        }
                    }

                    row_line_chart.highlightValues(highlight)
                    row_line_chart.marker

                    Log.e(
                            "VAL SELECTED", "Value: " + e.data + ", index: " + h!!.x.toInt()
                            + ", DataSet index: "
                    );
                    val postion = h.x.toString()
                    Log.e("dfdfjd", "Postion IS that" + postion)

                }

                override fun onNothingSelected() {}
            })
            //row_line_chart.setOnChartGestureListener(MyChartGestureListener())
            /*row_line_chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onNothingSelected() {

                }

                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    Log.e(
                        "VAL SELECTED", "Value: " + e + ", index: " + h!!.x.toInt()
                                + ", DataSet index: "
                    );
                    val postion = h.x.toInt()
                    Log.e(TAG, "Postion IS that" + postion)


                    Log.e("TAG", "data id" + data_line_set!!.data[postion].data)



                    if (e!!.y != 0.0f) {
                        *//* val mv = YourLineChartMarkerView(
                             this@MyGoalActivity,
                             R.layout.custom_linechart_marker_view,
                             data_line_set!!.data[postion].data,
                             data_line_set!!.data[postion].date
                         )*//*

                        showDialog(
                            data_line_set!!.data[postion].data,
                            data_line_set!!.data[postion].date
                        )

                        //  lc_my_goal_activity_log_chart.marker = mv
                    } else {
                        lc_my_goal_activity_log_chart.highlightValue(null)
                    }
                }

            })*/


        }

        @RequiresApi(Build.VERSION_CODES.R)
        private fun updateStaffDetails(selectedDate: String, data: HomeGraphData.Data) {
            var tempSeletedDate : String = ""
            for (i in data.premisedailydata.indices) {
                if(selectedDate==convertDate(data.premisedailydata[i].date, "yyyy-MM-dd", "dd/MM")){
                    startDate = data.premisedailydata[i].date
                    endDate = data.premisedailydata[i].date
                }
            }
            tvOnSelectedDate.text = convertDate(
                    endDate!!,
                    "yyyy-MM-dd",
                    "EEEE, MMMM dd, yyyy"
            )
            userStaffData(isSwitchChecked)
        }
        @RequiresApi(Build.VERSION_CODES.R)
        private fun updateKnownVisitorsDetails(selectedDate: String, data: HomeGraphData.Data) {
            var tempSeletedDate : String = ""
            for (i in data.premisedailydata.indices) {
                if(selectedDate==convertDate(data.premisedailydata[i].date, "yyyy-MM-dd", "dd/MM")){
                    startDate = data.premisedailydata[i].date
                    endDate = data.premisedailydata[i].date
                }
            }
            tvOnSelectedDate.text = convertDate(
                    endDate!!,
                    "yyyy-MM-dd",
                    "EEEE, MMMM dd, yyyy"
            )
            userKnownVisitorsData(isSwitchChecked)
        }

        fun getPremiseDataSet(
                dataModel: HomeGraphData.Data.Premisedata,
                status: String,
                lastUpdated: String
        ) {

            dataModel.apply {
                tv_watch_place_name.text=name
                if (dataModel.state.isNotEmpty()) {
                    tv_watch_place_city.text = "$city, $state"
                } else {
                    tv_watch_place_city.text = city
                }
            }


            /* tv_watch_place_name.text = dataModel.name

             if (dataModel.state.isNotEmpty()) {
                 tv_watch_place_city.text = dataModel.city + ", " + dataModel.state
             } else {
                 tv_watch_place_city.text = dataModel.city
             }*/
            /*   Glide.with(this@WhatchFaceDashBordActivity)
                   .load(dataModel.photo)
                   .placeholder(R.drawable.home_btn_get_started)
                   .into(im_home_premise)*/

            val circularProgressDrawable = CircularProgressDrawable(this@DisplyGraphActivity)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(this@DisplyGraphActivity)
                    .load(premiseImage)
                    .placeholder(circularProgressDrawable)
                    .into(im_graph_palce)

            tv_open_close.text = status


            if (status == "Open") {
                tv_open_close.visibility = View.VISIBLE
                tv_open_close.setBackgroundResource(R.drawable.home_list_second_list_second)
            } else if (status == "Closed") {
                tv_open_close.visibility = View.VISIBLE
                tv_open_close.setBackgroundResource(R.drawable.home_list_second_list)
            } else {
                tv_open_close.visibility = View.GONE
            }

            /*if (status == "Open" || status == "OPEN") {
                tv_open_close.setBackgroundColor()
            } else {

            }*/

            val timeStr : String
            timeStr = changeTimeFormat(lastUpdated);
            tv_watch_time.text = timeStr
        }

        @RequiresApi(Build.VERSION_CODES.R)
        fun setTypeWiseSetData(data: HomeGraphData.Data) {
            if (homeType == "openAt") {
                if(imageType!=""){
                    im_view_detail.visibility = View.VISIBLE
                    im_view_detail.setImageURI(imageType)
                }
            tv_graph_name.text = "opened at " + convertDate(
                    typeName, "HH:mm:ss", "hh:mm a"
            )
                tempGraphDataSet("openAt", data)
            } else if (homeType== "first_customer") {
                im_view_detail.visibility = View.VISIBLE
                im_view_detail.setImageURI(imageType)
                tv_graph_name.text = "First customer " + convertDate(
                        typeName, "HH:mm:ss", "hh:mm a"
                )
                tempGraphDataSet("first_customer", data)
            } else if (homeType == "customer") {
                im_view_detail.visibility = View.GONE
                /*set multiple image set*/
                //im_view_detail.setImageURI(imageType)
                tv_graph_name.text =
                        "Customer " + maxValue
                tempGraphDataSet("customer", data)

            } else if (homeType == "staff") {
                im_view_detail.visibility = View.GONE
                tv_graph_name.visibility = View.GONE
                llSelectedStaff.visibility = View.VISIBLE
               /* tv_graph_name.text =
                        "Staff " + maxValue*/
                tempGraphDataSet("staff", data)
                userStaffData(true)
            }else if (homeType == "closeAt") {
                if(imageType!=""){
                    im_view_detail.visibility = View.VISIBLE
                    im_view_detail.setImageURI(imageType)
                }
                tv_graph_name.text = "closed at " + convertDate(
                        typeName, "HH:mm:ss", "hh:mm a"
                )
                tempGraphDataSet("closeAt", data)
            }else if (homeType == "known_visitors") {
                im_view_detail.visibility = View.GONE
                tv_graph_name.visibility = View.GONE
                llSelectedStaff.visibility = View.VISIBLE
                tvStaffSelectedLabel.text = "Known visitors"
                /* tv_graph_name.text =
                         "Staff " + maxValue*/
                tempGraphDataSet("known_visitors", data)
                userKnownVisitorsData(true)
            }

        }

        @RequiresApi(Build.VERSION_CODES.R)
        private fun userKnownVisitorsData(isSwitchChecked: Boolean) {
            Log.e("ssss123", premiseID)
            Log.e("ssss123", startDate)
            Log.e("ssss123", endDate)
            if (isOnline(this@DisplyGraphActivity)) {
                ApiRequest(
                        this,
                        ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userKnownVisitorsData(
                                "Bearer " + getUserModel()!!.data.token,
                                encrypt(premiseID!!),
                                encrypt(startDate!!),
                                encrypt(endDate!!),
                        ),
                        USER_KNOWN_DETAILS_DATA, isSwitchChecked, this
                )
            } else {
                showSnackBar(
                        ll_graph_view,
                        getString(R.string.no_internet),
                        ACTIONSNACKBAR.DISMISS
                )
            }
        }

        private fun setHomeOfferData(data: ArrayList<UserStaffData.Data>) {
            if(isSwitchChecked){
                llOnSelectedStaff.visibility = View.VISIBLE
            }else{
                llOnSelectedStaff.visibility = View.GONE
            }
            if (data.size > 0) {
                rv_set_staff_images.setHasFixedSize(true)
                rv_set_staff_images.layoutManager = GridLayoutManager(this@DisplyGraphActivity, 2)
                val treatment_reason =
                        StaffistAdapter(
                                this@DisplyGraphActivity,
                                data,
                                this,
                                "staff_graph"
                        )  // Create adapter object
                rv_set_staff_images.adapter = treatment_reason
                rv_set_staff_images.visibility = View.VISIBLE
                tv_no_staff_available.visibility = View.GONE

            } else {
                tv_no_staff_available.visibility = View.VISIBLE
                rv_set_staff_images.visibility = View.GONE

            }


        }



        override fun staffPostionClick(postion: Int) {
            //super.staffPostionClick(postion)
            Log.d(TAG, "staffPostionClick: "+postion.toString());
            startActivity(
                    Intent(
                            this@DisplyGraphActivity,
                            StaffDetailsActivity::class.java
                    )
                            .putExtra(
                                    "open_or_close",
                                    userStaffData!!.data[postion].staffId.toString()
                            )
                            .putExtra("staffId", userStaffData!!.data[postion].staffId.toString())
                            .putExtra("start_date", startDate)
                            .putExtra("end_date", endDate)
                            .putExtra("premiseId", premiseID)
                            .putExtra("menu_toolbar", "gone")
                            .putExtra("from", "staff")
                            .putExtra("position", postion)

            )
        }

        override fun knownPositionVisitorClick(postion: Int) {
            //super.staffPostionClick(postion)
            startActivity(
                    Intent(
                            this@DisplyGraphActivity,
                            StaffDetailsActivity::class.java
                    )
                            .putExtra(
                                    "open_or_close",
                                    userKnownVisitorsData!!.data[postion].known_visitors_id.toString()
                            )
                            .putExtra(
                                    "staffId",
                                    userKnownVisitorsData!!.data[postion].known_visitors_id.toString()
                            )
                            .putExtra("start_date", startDate)
                            .putExtra("end_date", endDate)
                            .putExtra("premiseId", premiseID)
                            .putExtra("menu_toolbar", "gone")
                            .putExtra("from", "known_visitors")
                            .putExtra("position", postion)

            )
        }

    }

    class CustomMarkerView(context: Context?, layoutResource: Int) : MarkerView(
            context,
            layoutResource
    ) {
        private val tvContent: TextView
        private val tvContent1: TextView

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        override fun refreshContent(e: Entry, highlight: Highlight) {
            super.refreshContent(e, highlight)
            tvContent.text = toolTipTime // set the entry-value as the display text
            tvContent1.text = toolTipDate // set the entry-value as the display text
        }


        fun getYOffset(ypos: Float): Int {
            // this will cause the marker-view to be above the selected value
            return -height
        }
        override fun getOffset(): MPPointF {
            return MPPointF((-(width / 2)).toFloat(), (-height).toFloat()) // place the midpoint of marker over the bar
        }

        init {
            // this markerview only displays a textview
            tvContent = findViewById<View>(R.id.data1) as TextView
            tvContent1 = findViewById<View>(R.id.data2) as TextView
        }
    }


