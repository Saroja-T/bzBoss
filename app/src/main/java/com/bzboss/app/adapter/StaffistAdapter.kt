package com.bzboss.app.adapter

import android.content.Context
import android.graphics.DashPathEffect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bzboss.app.R
import com.bzboss.app.custom.HourYAxisValueFormatter
import com.bzboss.app.custom.MyCustomTextView
import com.bzboss.app.custom.convertDate
import com.bzboss.app.model.UserStaffData
import com.bzboss.interfaceD.StaffClick
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_staff_details.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StaffistAdapter(
    private val context: Context,
    val commingOfferList: ArrayList<UserStaffData.Data>,
    var StaffClick: StaffClick,
    var from: String
) : RecyclerView.Adapter<StaffistAdapter.InnerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        return InnerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_staff_list, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return commingOfferList.size
    }


    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.bind(position, commingOfferList)

        holder.apply {

            val xAxisLabelList = ArrayList<String>()
            val model: UserStaffData.Data = commingOfferList[position]
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            if(from == "staff_details"){
                llStaffDetails.visibility = View.VISIBLE
                ll_staff_list.visibility = View.GONE
            }else if(from == "staff_graph"){
                ll_staff_list.visibility = View.VISIBLE
                llStaffDetails.visibility = View.GONE
            }

            Glide.with(context)
                .load(model.firstAppearanceImage)
                .placeholder(R.drawable.home_btn_get_started)
                .into(im_staff_image)

            Glide.with(context)
                .load(model.firstAppearanceImage)
                .placeholder(R.drawable.home_btn_get_started)
                .into(imStaffImage)

            tv_staff_name.text = model.staffName
            tvStaffName.text = model.staffName

            val timeStr : String
            timeStr = convertDate(model.firstAppearanceDateTime,"yyyy-MM-dd HH:mm:ss","hh:mm a");

            tv_time.text = timeStr
            //tvTime.text = timeStr

          /*  for (i in data.premisedailydata.indices) {
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

            }*/
          //  setLineChartData(10f, xAxisLabelList, lc_my_goal_activity_log_chart)


        }
        holder.ll_staff_list.setOnClickListener {
            StaffClick.staffPostionClick(position)
        }
    }

    fun conVertDateTimeToTimeStarp(date: String): String {
        val sdf = SimpleDateFormat("HH:mm:ss")
        val date: Date = sdf.parse(date)
        System.out.println("Given Time in milliseconds : " + date.time)
        return date.time.toString() + "f"
    }

    private fun setLineChartData(fl: Float, weekdays: ArrayList<String>, row_line_chart: LineChart?) {

        row_line_chart?.clear()
        val zeroNumberList = ArrayList<Entry>()
        val otherNumberList = ArrayList<Entry>()
        zeroNumberList.clear()
        otherNumberList.clear()

       /* for (i in values.indices) {
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
        }*/


        val set1 = LineDataSet(zeroNumberList, "")
        val set2 = LineDataSet(otherNumberList, "")
        set1.setDrawIcons(false)
        set2.setDrawIcons(false)
        row_line_chart?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )


        row_line_chart?.description?.isEnabled = false
        row_line_chart?.setTouchEnabled(true)


        val xAxis = row_line_chart?.xAxis
        xAxis?.enableGridDashedLine(10f, 0f, 0f)
        xAxis?.gridColor = ContextCompat.getColor(context, R.color.black)
        xAxis?.gridLineWidth = 1f
        xAxis?.setCenterAxisLabels(false)

        xAxis?.granularity = 1f
        row_line_chart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        row_line_chart?.xAxis?.setDrawGridLines(false)



        row_line_chart?.xAxis?.axisLineWidth = 1f
        //xAxis.labelCount = weekdays.size
        xAxis?.valueFormatter = IndexAxisValueFormatter(weekdays)
        // xAxis.valueFormatter = IndexAxisValueFormatter(weekdays)
        xAxis?.setDrawAxisLine(true)
        xAxis?.setDrawLabels(true)
        // xAxis.labelRotationAngle = -40f
        xAxis?.mLabelWidth = 1


//        row_line_chart.xAxis.setDrawLabels(true)
//        xAxis.setDrawAxisLine(true)
//        xAxis.setDrawLabels(true)


        val yAxis = row_line_chart?.axisLeft
        row_line_chart?.axisLeft?.isEnabled = true
        row_line_chart?.axisRight?.isEnabled = false

        // row_line_chart.axisLeft.axisMinimum = 0f


        row_line_chart?.axisLeft?.setDrawGridLines(true)
        yAxis?.setDrawLabels(true)
        yAxis?.setDrawAxisLine(false)
        yAxis?.granularity = 1f
        row_line_chart?.animateXY(500, 500)
        row_line_chart?.legend?.isEnabled = false
        //l.form = Legend.LegendForm.LINE
        yAxis?.valueFormatter = HourYAxisValueFormatter()


        // black lines and points
        set1.color = ContextCompat.getColor(context, R.color.green)
        set2.color = ContextCompat.getColor(context, R.color.green)
        set1.setCircleColor(ContextCompat.getColor(context, R.color.green))
        set2.setCircleColor(ContextCompat.getColor(context, R.color.green))
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
            IFillFormatter { _, _ -> row_line_chart!!.axisLeft.axisMinimum }
        set2.fillFormatter =
            IFillFormatter { _, _ -> row_line_chart!!.axisLeft.axisMinimum }


        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        dataSets.add(set2)
        set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set2.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

        // create a data object with the data sets
        val data = LineData(dataSets)
        row_line_chart?.data = data


    }

    class InnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val im_staff_image = view.findViewById<ImageView>(R.id.im_staff_image)
        val tv_staff_name = view.findViewById<MyCustomTextView>(R.id.tv_staff_name)
        val tv_time = view.findViewById<MyCustomTextView>(R.id.tv_time)
        val ll_staff_list = view.findViewById<LinearLayout>(R.id.ll_staff_list)

        val llStaffDetails = view.findViewById<LinearLayout>(R.id.llStaffDetails)
        val imStaffImage = view.findViewById<ImageView>(R.id.imStaffImage)
        val tvStaffName = view.findViewById<MyCustomTextView>(R.id.tvStaffName)
//        val tvTime = view.findViewById<MyCustomTextView>(R.id.tvTime)
        val lc_my_goal_activity_log_chart = view.findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lc_my_goal_activity_log_chart)
        fun bind(
            position: Int,
            treatmentName: ArrayList<UserStaffData.Data>
        ) {
        }
    }
}
