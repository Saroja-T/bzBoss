package com.bzboss.app.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bzboss.app.R
import com.bzboss.app.SliderPagerAdapter
import com.bzboss.app.model.CustomSlider
import kotlinx.android.synthetic.main.activity_login_sign_home.*
import java.util.*
import kotlin.collections.ArrayList


class LoginSignHomeActivity : ActivityBase() {
    var sliderImageList: ArrayList<CustomSlider>? = null
    var sliderPagerAdapter: SliderPagerAdapter? = null
    var dots: Array<TextView?>? = null

    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed

    val PERIOD_MS: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_home)
        sliderImageList = ArrayList()
        setSliderImageAndName()
        btnClickManage()
    }

    fun setSliderImageAndName() {
        sliderImageList!!.clear()
        sliderImageList!!.add(
            CustomSlider(
                R.drawable.ic_slider_one,
                "Track number of customer visits"
            )
        )
        sliderImageList!!.add(
            CustomSlider(
                R.drawable.ic_slider_two,
                "manage staff attendance"
            )
        )
        sliderImageList!!.add(
            CustomSlider(
                R.drawable.ic_slider_three,
                "track business open & close time"
            )
        )
        sliderImageList!!.add(
            CustomSlider(
                R.drawable.ic_slider_four,
                "know the demography of your customers"
            )
        )




        sliderPagerAdapter = SliderPagerAdapter(this@LoginSignHomeActivity, sliderImageList!!)
        vp_slider.adapter = sliderPagerAdapter
        vp_slider.startAutoScroll();
        vp_slider.interval = 2000;
        vp_slider.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        //vp_slider.setd
        dots_indicator.setViewPager(vp_slider);


      /*  *//*After setting the adapter use the timer *//*

        *//*After setting the adapter use the timer *//*
        val handler = Handler(Looper.getMainLooper())
        val Update = Runnable {
            if (currentPage === 5 - 1) {
                currentPage = 0
            }
            vp_slider.setCurrentItem(currentPage++, true)
        }

        timer = Timer() // This will create a new Thread

        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)*/
    }

    fun btnClickManage() {
        btn_sign_in.setOnClickListener {
            val getStartedIntent = Intent(this@LoginSignHomeActivity, LoginActivity::class.java)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            startActivity(getStartedIntent)

        }
        btn_register.setOnClickListener {
            val getStartedIntent = Intent(this@LoginSignHomeActivity, RegisterActivity::class.java)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            startActivity(getStartedIntent)


        }
    }

    override fun onBackPressed() {

        appClose()
    }
    /*private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls<TextView>(sliderImageList!!.size)
        ll_dots.removeAllViews()
        for (i in dots!!.indices) {
            dots!![i] = TextView(this@LoginSignHomeActivity)
            dots!![i]!!.text = HtmlCompat.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_COMPACT)
            dots!![i]!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35f);

            dots!![i]!!.setBackgroundResource(R.drawable.slider_not_seleted)
            ll_dots.addView(dots!![i])
        }
        if (dots!!.isNotEmpty()) dots!!.get(currentPage)!!
            .setBackgroundResource(R.drawable.slider_back)
    }*/
}