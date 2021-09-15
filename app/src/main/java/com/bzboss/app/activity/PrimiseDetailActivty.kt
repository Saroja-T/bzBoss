package com.bzboss.app.activity

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bzboss.app.R
import com.bzboss.app.custom.ImagePopup
import com.bzboss.app.custom.changeTimeFormat
import com.bzboss.app.custom.showImageDialog
import kotlinx.android.synthetic.main.activity_primise_detail_activty.*
import kotlinx.android.synthetic.main.my_custom_toolbar.*


class PrimiseDetailActivty : AppCompatActivity() {

    var image : String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primise_detail_activty)
        setToolbar()
        getPremiseDataSet()
        image = intent.getStringExtra("Iimage").toString();
        Log.e("imge",image);

        val imagePopup = ImagePopup(this@PrimiseDetailActivty)
        imagePopup.windowHeight = 800 // Optional

        imagePopup.windowWidth = 800 // Optional

        imagePopup.backgroundColor = Color.BLACK // Optional

        imagePopup.isFullScreen = true // Optional

        imagePopup.isHideCloseIcon = false // Optional

        imagePopup.isImageOnClickClose = true // Optional


        val imageView = im_home_premise

        imageView.setOnClickListener {
            /** Initiate Popup view
             * Initiate Popup view*/
            imagePopup.initiatePopupWithGlide(image);
            imagePopup.viewPopup()
        }
        /*im_home_premise.setOnClickListener(View.OnClickListener {
            showImageDialog(this@PrimiseDetailActivty,image)
        })*/

    }



  /*  fun showDialog() {
        val dialog = Dialog(this@PrimiseDetailActivty)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.image_zoom_layout)

        val yesBtn = dialog.findViewById(R.id.yesBtn) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
         dialog.show()
         val window: Window? = dialog.getWindow()
         window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    }*/
    fun setToolbar()
    {
        click_drawer_menu.visibility=View.INVISIBLE
        logout.visibility=View.INVISIBLE
        toolbar_name.text="Last Known Status"
    }

    fun getPremiseDataSet() {
        tv_watch_place_name.text = intent.getStringExtra("name").toString()

        if (intent.getStringExtra("state").toString().isNotEmpty()) {
            tv_watch_place_city.text = intent.getStringExtra("city").toString() + ", " +intent.getStringExtra(
                    "state"
            ).toString()
        } else {
            tv_watch_place_city.text = intent.getStringExtra("city").toString()
        }
        /*   Glide.with(this@WhatchFaceDashBordActivity)
               .load(dataModel.photo)
               .placeholder(R.drawable.home_btn_get_started)
               .into(im_home_premise)*/

        val circularProgressDrawable = CircularProgressDrawable(this@PrimiseDetailActivty)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(this@PrimiseDetailActivty)
            .load(intent.getStringExtra("Iimage").toString())
            .placeholder(circularProgressDrawable)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(im_home_premise)



        tv_open_close.text = intent.getStringExtra("status").toString()


        if (intent.getStringExtra("status").toString() == "Open") {
            tv_open_close.visibility = View.VISIBLE
            tv_open_close.setBackgroundResource(R.drawable.home_list_second_list_second)
        } else if (intent.getStringExtra("status").toString() == "Closed") {
            tv_open_close.visibility = View.VISIBLE
            tv_open_close.setBackgroundResource(R.drawable.home_list_second_list)
        } else {
            tv_open_close.visibility = View.INVISIBLE
        }

        /*if (status == "Open" || status == "OPEN") {
            tv_open_close.setBackgroundColor()
        } else {

        }*/

        val timeStr : String
        timeStr = changeTimeFormat(intent.getStringExtra("lastUpdated").toString());
        tv_watch_time.text = timeStr
    }



}



