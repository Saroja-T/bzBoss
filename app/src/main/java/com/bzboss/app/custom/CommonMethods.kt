package com.bzboss.app.custom;


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.DialogInterface
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialog
import com.bumptech.glide.Glide
import com.bzboss.app.R
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_whatch_face_dash_bord.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun generateCircleTag(context: Context, bool: Boolean): GradientDrawable {
//    val rnd = Random()
//    val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

    val brown = Color.parseColor("#651612")
    val green = Color.parseColor("#226512")

    val shape = GradientDrawable()
    shape.shape = GradientDrawable.OVAL
    if (bool) {
        shape.setColor(brown)
    } else {
        shape.setColor(green)
    }
    shape.setStroke(3, null)
    shape.setSize(
        context.resources.getDimension(R.dimen._40sdp).toInt(),
        context.resources.getDimension(R.dimen._40sdp).toInt()
    )
    return shape
}
@SuppressLint("SimpleDateFormat")
fun convertDate(date: String, input_formate: String, output_formate: String): String {
    try {
        val input_formate = SimpleDateFormat(input_formate)
        val date1 = input_formate.parse(date)
        val output_formate = SimpleDateFormat(output_formate)
        Log.e("convertDate", "done = " + output_formate.format(date1))
        return output_formate.format(date1)
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("convertDate", "error = " + e.message)
    }
    return ""
}
fun getProgressDialog(context: Context): AppCompatDialog {
    val myCustomProgressDialog = MyCustomProgressDialog(context)
    myCustomProgressDialog.setCanceledOnTouchOutside(false)
    myCustomProgressDialog.show()
   /* mProgressDialog=myCustomProgressDialog*/
    return myCustomProgressDialog
}
fun String.decode(): String {
    return Base64.decode(this, Base64.NO_WRAP).toString(charset("UTF-8"))
}
@RequiresApi(Build.VERSION_CODES.R)
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false


}
fun String.encode(): String {
    return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.NO_WRAP)
}
fun dismissProgressDialog(pd: AppCompatDialog?) {
    try {
        if (pd != null && pd.isShowing) {
            pd.dismiss()
        }
    } catch (e: java.lang.Exception) {
    }
}
fun customDismissDialog(context: Context?, pd: AppCompatDialog?) {
    try {
        if (pd != null && pd.isShowing) {
            pd.dismiss()
        }
    } catch (e: java.lang.Exception) {
    }
}
fun isNetWork(context: Context): Boolean {
    return isNetworkAvailable(context)
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null
}

//fun getProgressDialogs(): AppCompatDialog {
//    val dialog = AppCompatDialog(AppController.getContext())
//    dialog.setContentView(DialogCompat.requireViewById(dialog, R.layout.progress_dialog))
//    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    dialog.setCancelable(false)
//    dialog.show()
//    return dialog
//}
fun compareDate(objCalendar: Calendar): Boolean {
    val tempObjCal = Calendar.getInstance()
    tempObjCal.set(Calendar.MONTH, objCalendar.get(Calendar.MONTH))
    tempObjCal.set(Calendar.YEAR, objCalendar.get(Calendar.YEAR))
    val objDate = tempObjCal.time

    val tempObjCal_1 = Calendar.getInstance()
    val objDate1 = tempObjCal_1.time

    return objDate < objDate1
}

private var toast: android.widget.Toast? = null

fun changeDateFormat(inputFormat: String, outputFormat: String, inputDate: String): String {
    var parsed: Date? = null
    var outputDate = ""

    val df_input = SimpleDateFormat(inputFormat, Locale.getDefault())
    val df_output = SimpleDateFormat(outputFormat, Locale.getDefault())

    try {
        parsed = df_input.parse(inputDate)
        outputDate = df_output.format(parsed)

    } catch (e: ParseException) {
        Log.e("TAG", "ParseException - dateFormat")
    }
    return outputDate
}

@SuppressLint("ShowToast")
fun Toast(msg: Any?, isShort: Boolean = true, app: Context) {

Log.e("ndf", "dhf")
    msg.let {
        if (toast == null) {
            toast = android.widget.Toast.makeText(
                app,
                msg.toString(),
                android.widget.Toast.LENGTH_SHORT
            )
        } else {
            toast!!.setText(msg.toString())

        }
        toast!!.duration =
            if (isShort) android.widget.Toast.LENGTH_SHORT else android.widget.Toast.LENGTH_LONG
        toast!!.show()
    }
}

private val HIGHT_WIGHT =
    "^\\d+(\\.\\d+)?\$"

public fun validateHightWight(email: String): Boolean {
    val pattern = Pattern.compile(HIGHT_WIGHT)
    val matcher = pattern.matcher(email)
    return matcher.matches()
}





enum class TAG(var myTag: String) {
    EDUCATION_FRAGMENT("EDUCATION_FRAGMENT"), EDUCATION_SUB_ADAPTER("EDUCATION_SUB_ADAPTER"), MANAGE_ASTHMA_ACTIVITY(
        "MANAGE_ASTHMA_ACTIVITY"
    ),
    MY_INFORMATION_ACTIVITY("MY_INFORMATION_ACTIVITY"), MY_MEDICATION_DETAIL_ACTIVITY("MY_MEDICATION_DETAIL_ACTIVITY")
}


fun changeTimeFormat(lastUpdated: String): String {

    Log.e("dateee12", lastUpdated)
    var timeStr : String = ""
    val tk = StringTokenizer(lastUpdated)
    val time = tk.nextToken()
    val now = tk.nextToken()
    val date = lastUpdated.substring(lastUpdated.indexOf('-'), lastUpdated.length)
    Log.e("dateee12", date.toString())
    val sdf = SimpleDateFormat("H:mm")
    val sdfs = SimpleDateFormat("hh:mm a")
    val dt: Date
    try {
        dt = sdf.parse(time)
        Log.e("lastUpdated123", sdfs.format(dt).toString())
        timeStr = sdfs.format(dt).toString() +" "+date
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return timeStr
}
fun showImageDialog(context: Context, image: String) {
    Log.e("imge", image);

    var currentAnimator: Animator? = null

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    var shortAnimationDuration: Int = 0
    val dialog = Dialog(context)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.image_zoom_layout)

    val container: LinearLayout = dialog.findViewById(R.id.container)
    val tvClose: FloatingActionButton = dialog.findViewById(R.id.floatingActionButton)

    val imageView : SubsamplingScaleImageView = dialog.findViewById(R.id.img)
    //imageView.setImage(ImageSource.uri(image));

    tvClose.setOnClickListener {
        dialog.dismiss()
    }
    dialog.setCanceledOnTouchOutside(true)
    dialog.show()

    val window: Window? = dialog.getWindow()
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    window?.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
    )

    class RetrieveFeedTask :
        AsyncTask<String?, Void?, Void?>() {
        var myBitmap: Bitmap? = null
        private var exception: java.lang.Exception? = null


        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            imageView.setImage(ImageSource.bitmap(myBitmap!!))
        }

        override fun doInBackground(vararg params: String?): Void? {
            try {
                val url =
                    URL(image)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                myBitmap = BitmapFactory.decodeStream(input)
            } catch (e: java.lang.Exception) {
                exception = e
                return null
            }
            return null
        }


    }
    RetrieveFeedTask().execute()
}


/*fun showImageDialog(context: Context, image: String) {
    Log.e("imge", image);

    var currentAnimator: Animator? = null

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
     var shortAnimationDuration: Int = 0
    val dialog = Dialog(context)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.image_zoom_layout)

    val yesBtn = dialog.findViewById(R.id.yesBtn) as TextView
//    val imageSingle = dialog.findViewById(R.id.imageSingle) as ImageView
   val thumb1View: TouchImageVIew = dialog.findViewById(R.id.thumb_button_1)
    //val expandedImageView: ImageView = dialog.findViewById(R.id.expanded_image)
    val container: FrameLayout = dialog.findViewById(R.id.container)
    shortAnimationDuration = context.resources.getInteger(android.R.integer.config_shortAnimTime)


    Glide.with(context)
            .load(image)
            .into(thumb1View)
    thumb1View.setMaxZoom(4f)


    *//*Glide.with(context)
            .load(image)
            .into(expandedImageView)*//*



    *//*thumb1View.setOnClickListener({
        zoomImageFromThumb(
            thumb1View,
            R.drawable.vector,
            currentAnimator,
            shortAnimationDuration,
            expandedImageView,
            container
        )
    })*//*

    dialog.setCanceledOnTouchOutside(true)


    // Retrieve and cache the system's default "short" animation time.




  yesBtn.setOnClickListener {
      dialog.dismiss()
  }



  dialog.show()


    val window: Window? = dialog.getWindow()
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    window?.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
    )


    try {
        val newUrl =
            URL("https://demo.emeetify.com:4500/uploads/premisecurrentstatus/065b297b5eac56c21dba02ba52ede7d3162765885212.png")
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    }



}*/

fun showImageDialog1(context: Context, image: String, im_view_detail: SimpleDraweeView) {


    val imagePopup = ImagePopup(context)
    imagePopup.windowHeight = 800 // Optional

    imagePopup.windowWidth = 800 // Optional

    imagePopup.backgroundColor = Color.BLACK // Optional

    imagePopup.isFullScreen = true // Optional

    imagePopup.isHideCloseIcon = false // Optional

    imagePopup.isImageOnClickClose = true // Optional


    val imageView = im_view_detail

    imageView.setOnClickListener {
        /** Initiate Popup view  */
        /** Initiate Popup view  */
        imagePopup.initiatePopupWithGlide(image);
        imagePopup.viewPopup()
    }

}

fun showImageDialog2(context: Context, image: String) {

  /*  val tempImageView: ImageView = imageView


    val imageDialog: AlertDialog.Builder = AlertDialog.Builder(context)

    val layout: View = this.getSystemService(LAYOUT_INFLATER_SERVICE).inflate(R.layout.custom_fullimage_dialog,
            findViewById(R.id.layout_root) as ViewGroup?)
    val image = layout.findViewById<View>(R.id.fullimage) as ImageView
    image.setImageDrawable(tempImageView.drawable)
    imageDialog.setView(layout)
    imageDialog.setPositiveButton(resources.getString(R.string.ok_button), DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })


    imageDialog.create()
    imageDialog.show()*/


    //Log.e("imge", image);

    var currentAnimator: Animator? = null

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    var shortAnimationDuration: Int = 0
    val dialog = Dialog(context)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.image_zoom_layout)
    val thumb1View: ImageView = dialog.findViewById(R.id.img)
    val floatingActionButton = dialog.findViewById(R.id.floatingActionButton) as FloatingActionButton

    val container: LinearLayout = dialog.findViewById(R.id.container)


    Glide.with(context)
        .load(image)
        .into(thumb1View)
    floatingActionButton.setOnClickListener {
        dialog.dismiss()
    }
    container.setOnClickListener {
        dialog.dismiss()
    }

    dialog.setCanceledOnTouchOutside(true)
    dialog.show()

//    val window: Window? = dialog.getWindow()
////    window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//    window?.setFlags(
//        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//    )

}

/*fun showImageDialog(context: Context, image: String) {
    Log.e("imge", image);

    var currentAnimator: Animator? = null

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    var shortAnimationDuration: Int = 0
    val dialog = Dialog(context)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.image_zoom_layout)

//    val yesBtn = dialog.findViewById(R.id.yesBtn) as TextView
    val floatingActionButton = dialog.findViewById(R.id.floatingActionButton) as FloatingActionButton
    val thumb1View: ImageView = dialog.findViewById(R.id.img)
    val container: ConstraintLayout = dialog.findViewById(R.id.container)
    //val expandedImageView: ImageView = dialog.findViewById(R.id.expanded_image)


    Glide.with(context)
        .load(image)
        .into(thumb1View)


    *//*Glide.with(context)
            .load(image)
            .into(expandedImageView)*//*



    *//*thumb1View.setOnClickListener({
        zoomImageFromThumb(
            thumb1View,
            R.drawable.vector,
            currentAnimator,
            shortAnimationDuration,
            expandedImageView,
            container
        )
    })*//*

    dialog.setCanceledOnTouchOutside(true)


    // Retrieve and cache the system's default "short" animation time.



    floatingActionButton.setOnClickListener {
        dialog.dismiss()
    }



    dialog.show()


    val window: Window? = dialog.getWindow()
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    window?.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
    )



}*/

fun zoomImageFromThumb(
    thumbView: ImageView,
    imageResId: Int,
    currentAnimator1: Animator?,
    shortAnimationDuration: Int,
    expandedImageView: ImageView,
    container: FrameLayout
) {
    // If there's an animation in progress, cancel it
    // immediately and proceed with this one.
    var currentAnimator = currentAnimator1
    currentAnimator?.cancel()

    // Load the high-resolution "zoomed-in" image.

    //expandedImageView.setImageResource(imageResId)



    // Calculate the starting and ending bounds for the zoomed-in image.
    // This step involves lots of math. Yay, math.
    val startBoundsInt = Rect()
    val finalBoundsInt = Rect()
    val globalOffset = Point()

    // The start bounds are the global visible rectangle of the thumbnail,
    // and the final bounds are the global visible rectangle of the container
    // view. Also set the container view's offset as the origin for the
    // bounds, since that's the origin for the positioning animation
    // properties (X, Y).
    thumbView.getGlobalVisibleRect(startBoundsInt)
    container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
    startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
    finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

    val startBounds = RectF(startBoundsInt)
    val finalBounds = RectF(finalBoundsInt)

    // Adjust the start bounds to be the same aspect ratio as the final
    // bounds using the "center crop" technique. This prevents undesirable
    // stretching during the animation. Also calculate the start scaling
    // factor (the end scaling factor is always 1.0).
    val startScale: Float
    if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
        // Extend start bounds horizontally
        startScale = startBounds.height() / finalBounds.height()
        val startWidth: Float = startScale * finalBounds.width()
        val deltaWidth: Float = (startWidth - startBounds.width()) / 2
        startBounds.left -= deltaWidth.toInt()
        startBounds.right += deltaWidth.toInt()
    } else {
        // Extend start bounds vertically
        startScale = startBounds.width() / finalBounds.width()
        val startHeight: Float = startScale * finalBounds.height()
        val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
        startBounds.top -= deltaHeight.toInt()
        startBounds.bottom += deltaHeight.toInt()
    }

    // Hide the thumbnail and show the zoomed-in view. When the animation
    // begins, it will position the zoomed-in view in the place of the
    // thumbnail.
    thumbView.alpha = 0f
    expandedImageView.visibility = View.VISIBLE

    // Set the pivot point for SCALE_X and SCALE_Y transformations
    // to the top-left corner of the zoomed-in view (the default
    // is the center of the view).
    expandedImageView.pivotX = 0f
    expandedImageView.pivotY = 0f

    // Construct and run the parallel animation of the four translation and
    // scale properties (X, Y, SCALE_X, and SCALE_Y).
    currentAnimator = AnimatorSet().apply {
        play(
            ObjectAnimator.ofFloat(
                expandedImageView,
                View.X,
                startBounds.left,
                finalBounds.left
            )
        ).apply {
            with(
                ObjectAnimator.ofFloat(
                    expandedImageView,
                    View.Y,
                    startBounds.top,
                    finalBounds.top
                )
            )
            with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
            with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f))
        }
        duration = shortAnimationDuration.toLong()
        interpolator = DecelerateInterpolator()
        addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                currentAnimator = null
            }

            override fun onAnimationCancel(animation: Animator) {
                currentAnimator = null
            }
        })
        start()
    }

    // Upon clicking the zoomed-in image, it should zoom back down
    // to the original bounds and show the thumbnail instead of
    // the expanded image.
    expandedImageView.setOnClickListener {
        currentAnimator?.cancel()

        // Animate the four positioning/sizing properties in parallel,
        // back to their original values.
        currentAnimator = AnimatorSet().apply {
            play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left)).apply {
                with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    thumbView.alpha = 1f
                    expandedImageView.visibility = View.GONE
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    thumbView.alpha = 1f
                    expandedImageView.visibility = View.GONE
                    currentAnimator = null
                }
            })
            start()
        }
    }
}
