package com.bzboss.app

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bzboss.app.custom.MyCustomButton
import com.bzboss.app.custom.MyCustomTextView
import com.bzboss.app.model.CustomSlider
import java.util.*

class SliderPagerAdapter(var activity: Activity, var image_arraylist: ArrayList<CustomSlider>) :
    PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater =
            activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.layout_slider, container, false)
        val im_slider = view.findViewById<View>(R.id.im_slider) as ImageView
        val tv_slider_name = view.findViewById<View>(R.id.tv_slider_name) as MyCustomTextView

        Glide.with(activity)
            .load(image_arraylist[position].imageType)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(im_slider)
        tv_slider_name.text=image_arraylist[position].image_name
        /* Picasso.with(activity.getApplicationContext())
                .load(image_arraylist.get(position))
                .placeholder(R.mipmap.ic_launcher) // optional
                .error(R.mipmap.ic_launcher)         // optional
                .into(im_slider);


*/container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return image_arraylist.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}