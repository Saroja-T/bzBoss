package com.bzboss.app.activity

import android.annotation.SuppressLint
import android.graphics.DashPathEffect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bzboss.app.R
import com.bzboss.app.adapter.KnownVisitorsDetailstAdapter
import com.bzboss.app.adapter.StaffDetailstAdapter
import com.bzboss.app.adapter.knownVisitorsPostionClick
import com.bzboss.app.custom.HourYAxisValueFormatter
import com.bzboss.app.custom.convertDate
import com.bzboss.app.custom.isOnline
import com.bzboss.app.model.KnownVisitorsDetailsGraphData
import com.bzboss.app.model.StaffDetailsGraphData
import com.bzboss.app.restapi.ApiInitialize
import com.bzboss.app.restapi.ApiRequest
import com.bzboss.interfaceD.StaffClick
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.iqamahtimes.app.custom.HOME_GRAPH_DATA
import com.iqamahtimes.app.custom.SUCCESS_CODE
import com.iqamahtimes.app.custom.USER_KNOWN_DETAILS_DATA
import com.iqamahtimes.app.custom.USER_STAFF_DATA
import com.iqamahtimes.app.restapi.ApiResponseInterface
import com.iqamahtimes.app.restapi.ApiResponseManager
import kotlinx.android.synthetic.main.activity_disply_graph.*
import kotlinx.android.synthetic.main.activity_staff_details.*
import kotlinx.android.synthetic.main.activity_staff_details.lc_my_goal_activity_log_chart
import kotlinx.android.synthetic.main.activity_staff_details.ll_graph_view
import kotlinx.android.synthetic.main.activity_staff_details.tv_no_staff_available
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StaffDetailsActivity : ActivityBase(), ApiResponseInterface, StaffClick, knownVisitorsPostionClick {
    private var values = ArrayList<Entry>()
    private var userStaffData: StaffDetailsGraphData?=null
    private var knownVisitorsDetailsGraphData: KnownVisitorsDetailsGraphData?=null
    private var staffId : String = ""
    private var startDate : String = ""
    private var endDate : String = ""
    private var open_or_close : String = ""
    private var menu_toolbar: String = ""
    private var premiseId : String = ""
    private var from : String = ""
    var staffItemCount=0
    var position: Int = 0
    lateinit var layoutManager : LinearLayoutManager
//    var staffObj = StaffDetailsActivity()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_details)

        layoutManager = LinearLayoutManager(this@StaffDetailsActivity, RecyclerView.HORIZONTAL, false)
        // GetIntentData
        endDate = intent.getStringExtra("end_date")!!
        startDate = intent.getStringExtra("start_date")!!
        staffId = intent.getStringExtra("staffId")!!
        open_or_close = intent.getStringExtra("open_or_close")!!
        menu_toolbar = intent.getStringExtra("menu_toolbar")!!
        premiseId = intent.getStringExtra("premiseId")!!
        from = intent.getStringExtra("from")!!
        position = intent.getIntExtra("position",0)!!





        /* startDate = "2021-07-02"
         endDate = "2021-07-08"
         staffId = "4"
         premiseId = "4"
         open_or_close = "4"
         menu_toolbar = "gone"
         from = "staff"*/

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val myDate: Date = dateFormat.parse(endDate)
        val calendar = Calendar.getInstance()
        calendar.time = myDate
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        val newDate : Date = calendar.time
        Log.e("--startdate", startDate)
        Log.e("--endDate", dateFormat.format(newDate).toString())


        startDate = dateFormat.format(newDate).toString()



        Log.e("endDate", endDate)
        Log.e("startDate", startDate)
        Log.e("staffId", staffId)
        Log.e("open_or_close", open_or_close)
        Log.e("premiseId", premiseId)
        Log.e("menu_toolbar", menu_toolbar)

        tvForward.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(position == staffItemCount-1){
                    position = staffItemCount - 1
                }else{
                    position = position + 1
                }
                setPositionToRv(position,staffItemCount,"forward")

                //   rvStaff.layoutManager.findFirstVisibleItemPosition();
              /*  if(staffItemCount-1 == position){
                    Log.e("forwardposition_if",position.toString())
                    Log.e("staffItemCount",staffItemCount.toString())
                    position = staffItemCount -1
                    tvForward.visibility = View.GONE
                    tvBack.visibility = View.VISIBLE
                    rvStaff.getLayoutManager()?.scrollToPosition(position)
                }else{
                    Log.e("forwardposition_else",position.toString())
                    Log.e("staffItemCount",staffItemCount.toString())
                    tvBack.visibility = View.VISIBLE
                    rvStaff.getLayoutManager()?.scrollToPosition(position)
                }*/




            }
        })
        tvBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if(position!= 0){
                    position = position - 1
                }else{
                    position = 0;
                }
                setPositionToRv(position,staffItemCount,"forward")
                /*if(position == 0){
                    Log.e("backdposition",position.toString())
                    Log.e("staffItemCount",staffItemCount.toString())
                    position = 0
                    tvBack.visibility = View.GONE
                    if(staffItemCount>1){
                        tvForward.visibility = View.VISIBLE
                    }
                    rvStaff.getLayoutManager()?.scrollToPosition(position)
                }else{
                    Log.e("backdposition",position.toString())


                    if(staffItemCount>1){
                        tvForward.visibility = View.VISIBLE
                    }
                    rvStaff.getLayoutManager()?.scrollToPosition(position)
                }*/
            }
        })

        rvStaff.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                   position = layoutManager.findFirstVisibleItemPosition()
                    setPositionToRv(position,staffItemCount,"swipe")
                    Log.e("position", position.toString())
                }
            }
        })



        if(from=="staff")
        {
            userStaffData()
        }else if(from=="known_visitors")
        {
            userKnownVisitorsData()
        }

    }

    private fun setPositionToRv(position1: Int, staffItemCount: Int, s: String) {
        Log.d(TAG, "setPositionToRv: "+position1+"itmcount-"+staffItemCount)
            var position = position1
        Log.d(TAG, "setPositionToRv: "+position+"itmcount-"+staffItemCount)

        if(position == 0){
            Log.d(TAG, "setPositionToRv: "+"if")

            tvBack.visibility = View.INVISIBLE
                if(staffItemCount>1){
                    tvForward.visibility = View.VISIBLE
                }else{
                    tvForward.visibility = View.INVISIBLE
                }
            }else if(position == staffItemCount-1){
                Log.d(TAG, "setPositionToRv: "+"e,seif")
                tvForward.visibility = View.GONE
                tvBack.visibility = View.VISIBLE
            }else{
            Log.d(TAG, "setPositionToRv: "+"else")

            tvForward.visibility = View.VISIBLE
                tvBack.visibility = View.VISIBLE
            }

            rvStaff.getLayoutManager()?.scrollToPosition(position)


    }

    fun setDatat() {
        values = ArrayList<Entry>()
        values.clear()
        val xAxisLabelList = ArrayList<String>()


        for(i in userStaffData!!.data.staffDetailsData.indices){
            Log.e("stafffffffDatatatatat", userStaffData!!.data.staffDetailsData[i].toString())
            if(userStaffData!!.data.staffDetailsData[i].staff_id.toString()=="4"){
                for (j in userStaffData!!.data.staffDetailsData[i].staffdata.indices){

                    if (userStaffData!!.data.staffDetailsData[i].staffdata[j].first_appearance_date_time.isNotEmpty()) {
                        Log.e("stafffffffDatatatatat", conVertDateTimeToTimeStarp(convertDate(userStaffData!!.data.staffDetailsData[i].staffdata[j].first_appearance_date_time, "yyyy-MM-dd HH:mm:ss", "HH:mm:ss")).toFloat().toString())
                        Log.e("stafffffffDatatatatat", conVertDateTimeToTimeStarp("12:42:00").toFloat().toString())
                        Log.e("stafffffffDatatatatat", convertDate(userStaffData!!.data.staffDetailsData[i].staffdata[j].first_appearance_date_time, "yyyy-MM-dd HH:mm:ss", "HH:mm:ss"))
                        values.add(
                                Entry(
                                        j.toFloat(),
                                        //2.592E7f
                                        conVertDateTimeToTimeStarp(/*data.premisedailydata[i].date + " " +*/
                                                "12:42:00"
                                        ).toFloat()

                                        //conVertDateTimeToTimeStarp(convertDate(userStaffData!!.data.staffDetailsData[i].staffdata[j].first_appearance_date_time, "yyyy-MM-dd HH:mm:ss", "HH:mm:ss")).toFloat()
                                )
                        )
                        xAxisLabelList.add(
                                convertDate(userStaffData!!.data.staffDetailsData[i].staffdata[j].first_appearance_date_time, "yyyy-MM-dd HH:mm:ss", "dd/MM")

                        )
                    }
                }
                setLineChartData(10f, xAxisLabelList, lc_my_goal_activity_log_chart)
            }
        }


     /*   val datavals = ArrayList<Entry>()
        datavals.add(Entry(0F, 20F))
        datavals.add(Entry(1F, 30F))
        datavals.add(Entry(2F, 25F))
        datavals.add(Entry(3F, 35F))
        datavals.add(Entry(4F, 40F))

        *//* val datavals1 = ArrayList<Entry>()
         datavals.add(Entry(0F, 5F))
         datavals.add(Entry(1F, 10F))
         datavals.add(Entry(2F, 15F))
         datavals.add(Entry(3F, 45F))
         datavals.add(Entry(4F, 50F))*//*


        val set1 = LineDataSet(datavals, "")
        // val set2 = LineDataSet(datavals1, "")
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        //   dataSets.add(set2)

        // create a data object with the data sets
        val data = LineData(dataSets)
        lc_my_goal_activity_log_chart.data = data
        lc_my_goal_activity_log_chart.invalidate()*/
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun userPremiseGraphData() {
        if (isOnline(this@StaffDetailsActivity)) {
            Log.e(TAG, "ID premiseId=>")
            ApiRequest(
                    this,
                    ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userPremiseGraphData(
                            "Bearer " + getUserModel()!!.data.token,
                            encrypt(startDate!!),
                            encrypt(endDate!!),
                            encrypt(staffId!!)
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
    private fun userStaffData() {

        Log.e("startDate", startDate)
        Log.e("startDate", endDate)
        Log.e("startDate", staffId)
        if (isOnline(this@StaffDetailsActivity)) {
            ApiRequest(
                    this,
                    ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userStaffDetailsData(
                            "Bearer " + getUserModel()!!.data.token,
                            encrypt(staffId!!),
                            encrypt(premiseId!!),
                            encrypt(startDate!!),
                            encrypt(endDate!!),

                            ),
                    USER_STAFF_DATA, true, this
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
    private fun userKnownVisitorsData() {

        Log.e("startDate", startDate)
        Log.e("startDate", endDate)
        Log.e("startDate", staffId)
        if (isOnline(this@StaffDetailsActivity)) {
            ApiRequest(
                    this,
                    ApiInitialize.initialize(ApiInitialize.LOCAL_URL).userKnownVisitorsDetailsData(
                            "Bearer " + getUserModel()!!.data.token,
                            encrypt(staffId!!),
                            encrypt(premiseId!!),
                            encrypt(startDate!!),
                            encrypt(endDate!!),

                            ),
                    USER_KNOWN_DETAILS_DATA, true, this
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

        when (apiResponseManager.type) {
            USER_STAFF_DATA -> {
                userStaffData = apiResponseManager.response as StaffDetailsGraphData
                Log.e("staffGraphData.dat", userStaffData.toString());
                //val staffDataGet = apiResponseManager.response as UserStaffData
                Log.e("staffGraphData.dat", userStaffData!!.data.staffDetailsData.toString());
                if (userStaffData!!.statusCode == SUCCESS_CODE) {
                    dataSetToRV(userStaffData!!.data);
                    //setDatat()
                    // setHomeOfferData(userStaffData!!.data.staffDetailsData as ArrayList<StaffDetailsGraphData.Data.StaffDetailsData>)
                }
            }

            USER_KNOWN_DETAILS_DATA -> {
                knownVisitorsDetailsGraphData = apiResponseManager.response as KnownVisitorsDetailsGraphData
                Log.e("staffGraphData.dat", knownVisitorsDetailsGraphData.toString());
                //val staffDataGet = apiResponseManager.response as UserStaffData
                Log.e("staffGraphData.dat", knownVisitorsDetailsGraphData!!.data.knownVisitorsData.toString());
                if (knownVisitorsDetailsGraphData!!.statusCode == SUCCESS_CODE) {
                    dataSetToRV1(knownVisitorsDetailsGraphData!!.data);
                    //setDatat()
                    // setHomeOfferData(userStaffData!!.data.staffDetailsData as ArrayList<StaffDetailsGraphData.Data.StaffDetailsData>)
                }
            }
        }
    }

    private fun dataSetToRV(data: StaffDetailsGraphData.Data) {
        staffItemCount = data.staffDetailsData.size
        setHomeOfferData(data.staffDetailsData as ArrayList<StaffDetailsGraphData.Data.StaffDetailsData>)

       /* if(staffItemCount == 1){
            tvBack.visibility = View.GONE
            tvForward.visibility = View.GONE
        }else if(staffItemCount > 1){
            tvBack.visibility = View.GONE
            tvForward.visibility = View.VISIBLE
        }*/
    }
    private fun dataSetToRV1(data: KnownVisitorsDetailsGraphData.Data) {
        setHomeOfferData1(data.knownVisitorsData as ArrayList<KnownVisitorsDetailsGraphData.Data.KnownVisitorsData>)
        staffItemCount = data.knownVisitorsData.size
        Log.e("data.knownVisitorssize",data.knownVisitorsData.size.toString())
        if(staffItemCount == 1){
            tvBack.visibility = View.GONE
            tvForward.visibility = View.GONE
        }else if(staffItemCount > 1){
            tvBack.visibility = View.GONE
            tvForward.visibility = View.VISIBLE
        }
    }

    /*fun tempGraphDataSet(type: String, data: HomeGraphData.Data) {
        values = ArrayList<Entry>()
        values.clear()
        val xAxisLabelList = ArrayList<String>()

    if (type == "first_customer") {
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

            setLineChartData(10f, xAxisLabelList, lc_my_goal_activity_log_chart, values)
        }


    }*/


    fun conVertDateTimeToTimeStarp(date: String): String {
        val sdf = SimpleDateFormat("HH:mm:ss")
        val date: Date = sdf.parse(date)
        System.out.println("Given Time in milliseconds : " + date.time)
        return date.time.toString() + "f"
    }

    @SuppressLint("UseSparseArrays")
    fun setLineChartData(lineValue: Float, weekdays: ArrayList<String>, row_line_chart: LineChart) {
        row_line_chart.clear()
        val zeroNumberList = ArrayList<Entry>()
        val otherNumberList = ArrayList<Entry>()

        zeroNumberList.clear()
        otherNumberList.clear()

        for (i in this.values.indices) {
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


        val set1 = LineDataSet(zeroNumberList, "")
        val set2 = LineDataSet(otherNumberList, "")
        set1.setDrawIcons(false)
        set2.setDrawIcons(false)
        row_line_chart.setBackgroundColor(
                ContextCompat.getColor(
                        this@StaffDetailsActivity,
                        R.color.white
                )
        )


        row_line_chart.description.isEnabled = false
        row_line_chart.setTouchEnabled(true)


        val xAxis = row_line_chart.xAxis
        xAxis.enableGridDashedLine(10f, 0f, 0f)
        xAxis.gridColor = ContextCompat.getColor(this@StaffDetailsActivity, R.color.black)
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
        set1.color = ContextCompat.getColor(this@StaffDetailsActivity, R.color.green)
        set2.color = ContextCompat.getColor(this@StaffDetailsActivity, R.color.green)
        set1.setCircleColor(ContextCompat.getColor(this@StaffDetailsActivity, R.color.green))
        set2.setCircleColor(ContextCompat.getColor(this@StaffDetailsActivity, R.color.green))
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
/*
    fun setLineChartDataWithoutDate(
            lineValue: Float,
            weekdays: ArrayList<String>,
            row_line_chart: LineChart
    ) {
        row_line_chart.clear()
        val zeroNumberList = ArrayList<Entry>()
        val otherNumberList = ArrayList<Entry>()

        zeroNumberList.clear()
        otherNumberList.clear()

        for (i in values.indices) {
            *//*if (this.values[i].y == 0f) {
                zeroNumberList.add(Entry(i.toFloat(), this.values[i].y))
            }*//*
            zeroNumberList.add(Entry(i.toFloat(), this.values[i].y))
        }

        for (i in this.values.indices) {
            *//* if (this.values[i].y != 0f) {
                 otherNumberList.add(Entry(i.toFloat(), this.values[i].y))
             }*//*
            otherNumberList.add(Entry(i.toFloat(), this.values[i].y))
        }


        val set1 = LineDataSet(zeroNumberList, "")
        val set2 = LineDataSet(otherNumberList, "")
        set1.setDrawIcons(false)
        set2.setDrawIcons(false)
        row_line_chart.setBackgroundColor(
                ContextCompat.getColor(
                        this@StaffDetailsActivity,
                        R.color.white
                )
        )


        row_line_chart.description.isEnabled = false
        row_line_chart.setTouchEnabled(true)


        val xAxis = row_line_chart.xAxis
        xAxis.enableGridDashedLine(10f, 0f, 0f)
        xAxis.gridColor = ContextCompat.getColor(this@StaffDetailsActivity, R.color.black)
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
        //  yAxis.valueFormatter = HourYAxisValueFormatter()
        yAxis.valueFormatter = IndexAxisValueFormatter(weekdays)


        // black lines and points
        // black lines and points
        set1.color = ContextCompat.getColor(this@StaffDetailsActivity, R.color.green)
        set2.color = ContextCompat.getColor(this@StaffDetailsActivity, R.color.green)
        set1.setCircleColor(ContextCompat.getColor(this@StaffDetailsActivity, R.color.green))
        set2.setCircleColor(ContextCompat.getColor(this@StaffDetailsActivity, R.color.green))
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
        val drawable = ContextCompat.getColor(this@StaffDetailsActivity, R.color.background)
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



       *//* row_line_chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
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
                     val mv = YourLineChartMarkerView(
                         this@MyGoalActivity,
                         R.layout.custom_linechart_marker_view,
                         data_line_set!!.data[postion].data,
                         data_line_set!!.data[postion].date
                     )

                    showDialog(
                        data_line_set!!.data[postion].data,
                        data_line_set!!.data[postion].date
                    )

                    //  lc_my_goal_activity_log_chart.marker = mv
                } else {
                    row_line_chart.highlightValue(null)
                }
            }

        })*//*


    }*/

    /*fun getPremiseDataSet(
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


        *//* tv_watch_place_name.text = dataModel.name

         if (dataModel.state.isNotEmpty()) {
             tv_watch_place_city.text = dataModel.city + ", " + dataModel.state
         } else {
             tv_watch_place_city.text = dataModel.city
         }*//*
        *//*   Glide.with(this@WhatchFaceDashBordActivity)
               .load(dataModel.photo)
               .placeholder(R.drawable.home_btn_get_started)
               .into(im_home_premise)*//*

        val circularProgressDrawable = CircularProgressDrawable(this@StaffDetailsActivity)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(this@StaffDetailsActivity)
            .load(premiseImage.toString())
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

        *//*if (status == "Open" || status == "OPEN") {
            tv_open_close.setBackgroundColor()
        } else {

        }*//*

        val timeStr : String
        timeStr = changeTimeFormat(lastUpdated);
        tv_watch_time.text = timeStr
    }*/


    private fun setHomeOfferData(data: ArrayList<StaffDetailsGraphData.Data.StaffDetailsData>) {
        if (data.size > 0) {
            rvStaff.setHasFixedSize(true)

            rvStaff.layoutManager = layoutManager
            val treatment_reason =
                StaffDetailstAdapter(
                        this@StaffDetailsActivity,
                        data,
                        this,
                        "staff_details",
                        supportFragmentManager,
                        premiseId

                )  // Create adapter object
            rvStaff.adapter = treatment_reason
            rvStaff.visibility = View.VISIBLE
            tv_no_staff_available.visibility = View.GONE
            if(position!=0){
                setPositionToRv(position,staffItemCount,"forward")
            }
        } else {
            tv_no_staff_available.visibility = View.VISIBLE
            rvStaff.visibility = View.GONE

        }

        rvStaff.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            var scrollDy = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                scrollDy += dy
                Log.e("scrollDy", scrollDy.toString())
            }
        })


    }

    private fun setHomeOfferData1(data: ArrayList<KnownVisitorsDetailsGraphData.Data.KnownVisitorsData>) {
        if (data.size > 0) {
            rvStaff.setHasFixedSize(true)

            rvStaff.layoutManager =
                    LinearLayoutManager(this@StaffDetailsActivity, RecyclerView.HORIZONTAL, false)
            val treatment_reason =
                    KnownVisitorsDetailstAdapter(
                            this@StaffDetailsActivity,
                            data,
                            this,
                            "staff_details",
                            supportFragmentManager,
                            premiseId

                    )  // Create adapter object
            rvStaff.adapter = treatment_reason
            rvStaff.visibility = View.VISIBLE
            tv_no_staff_available.visibility = View.GONE

        } else {
            tv_no_staff_available.visibility = View.VISIBLE
            rvStaff.visibility = View.GONE

        }



        rvStaff.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            var scrollDy = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                Log.e("newState", newState.toString())
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                scrollDy += dy
                Log.e("scrollDy", scrollDy.toString())
            }
        })



    }




}
