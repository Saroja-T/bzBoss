package com.bzboss.app.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bzboss.app.R
import com.bzboss.app.custom.MyCustomTextView
import com.bzboss.app.model.HomeDataModel
import com.bzboss.app.model.HomeListModel
import com.bzboss.interfaceD.HomeClick


class HomeListAdapter(
    private val context: Context,
    val commingOfferList: ArrayList<HomeDataModel.Data>,
    var homeClick: HomeClick
) : RecyclerView.Adapter<HomeListAdapter.InnerViewHolder>() {

    private val TAG = "HomeListAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        return InnerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_home_list, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return commingOfferList.size
    }


    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.bind(position, commingOfferList)
        val model: HomeDataModel.Data = commingOfferList[position]


        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Log.d(TAG, "onBindViewHolder: "+model.getpremisecurrentstatus.imageFilename)

        Glide.with(context)
            .load(model.getpremisecurrentstatus.imageFilename)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(circularProgressDrawable)
            .into(holder.im_upcomming_offer)

        holder.tv_offer_name.text = model.name

        if (model.country.isNotEmpty()) {
            holder.tv_offer_city.text = model.city + ", " + model.country
        } else {
            holder.tv_offer_city.text = model.city
        }


        holder.tv_open_close.text = model.getpremisecurrentstatus.status

        if (model.getpremisecurrentstatus.status == "Open") {
            holder.tv_open_close.setBackgroundResource(R.drawable.home_list_second_list_second)
        } else if (model.getpremisecurrentstatus.status == "Closed") {
            holder.tv_open_close.setBackgroundResource(R.drawable.home_list_second_list)
        } else {
            holder.tv_open_close.visibility = View.INVISIBLE
        }
        holder.ll_home_view.setOnClickListener {
            homeClick.homePostionClick(position)
        }

    }

    class InnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val im_upcomming_offer = view.findViewById<ImageView>(R.id.im_offer_image)
        val tv_offer_name = view.findViewById<MyCustomTextView>(R.id.tv_offer_name)

        val tv_offer_city = view.findViewById<MyCustomTextView>(R.id.tv_offer_city)
        val tv_open_close = view.findViewById<MyCustomTextView>(R.id.tv_open_close)
        val ll_home_view = view.findViewById<LinearLayout>(R.id.ll_home_view)

        fun bind(
            position: Int,
            treatmentName: ArrayList<HomeDataModel.Data>
        ) {

        }
    }
}
