package com.bzboss.app.adapter

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bzboss.app.R
import com.bzboss.app.custom.*
import com.bzboss.app.model.UserKnownVisitorsData
import com.bzboss.app.restapi.ApiInitialize
import kotlinx.android.synthetic.main.activity_disply_graph.*
import kotlinx.android.synthetic.main.activity_staff_details.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class KnownVisiorsListAdapter(
        private val context: Context,
        val commingOfferList: ArrayList<UserKnownVisitorsData.Data>,
        var knownVisitorsClick: KnownVisitorsClick,
        var from: String,
) : RecyclerView.Adapter<KnownVisiorsListAdapter.InnerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        return InnerViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_view_staff_list, parent, false
                )
        )
    }

    /*override fun ActivityCompat.OnRequestPermissionsResultCallback(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        Log.e("click", "onRequestPermissionsResult")
        when (requestCode) {
            REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("click", "PERMISSION_GRANTED")
                phoneIntent()
            } else {
                Log.e("click", "NOT PERMISSION_GRANTED")
                Toast.makeText(mContext, "Call Permission Not Granted!", Toast.LENGTH_SHORT).show()
            }
        }
    }*/
    override fun getItemCount(): Int {
        return commingOfferList.size
    }


    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.bind(position, commingOfferList)

        holder.apply {

            val xAxisLabelList = ArrayList<String>()
            val model: UserKnownVisitorsData.Data = commingOfferList[position]
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
                .load(model.appearance_image)
                .placeholder(R.drawable.home_btn_get_started)
                .into(im_staff_image)

            Glide.with(context)
                .load(model.appearance_image)
                .placeholder(R.drawable.home_btn_get_started)
                .into(imStaffImage)

            tv_staff_name.text = model.known_visitors_name
            tvStaffName.text = model.known_visitors_name

            val timeStr : String
            timeStr = convertDate(model.appearance_date_time, "yyyy-MM-dd HH:mm:ss", "hh:mm a");

            tv_time.text = timeStr
           // tvTime.text = timeStr


        }
        holder.ll_staff_list.setOnClickListener {
            knownVisitorsClick.knownPositionVisitorClick(position)
        }
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
                treatmentName: ArrayList<UserKnownVisitorsData.Data>
        ) {
        }
    }
}
