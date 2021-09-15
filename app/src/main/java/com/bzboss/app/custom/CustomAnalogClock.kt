package com.bzboss.app.custom

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.iqamahtimes.app.custom.*
import kotlinx.android.synthetic.main.activity_whatch_face_dash_bord.view.*
import java.util.*


class CustomAnalogClock : View {
    private var padding = 0
    private var fontSize = 0
    private val numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var paint: Paint? = null
    var isInit = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    fun initClock() {
        Log.e("calling","initMethod")
        var height = 0
        var width = 0
        height = getHeight()
        width = getWidth()
        padding = numeralSpacing + 50
        fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 10f,
            resources.displayMetrics
        ).toInt()
        val min = Math.min(height, width)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        paint = Paint()
       // isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }

        //canvas.drawColor(Color.BLACK)
        canvas.drawColor(Color.parseColor("#4CC0A6"))
        drawCircle(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawHands(canvas)
    }

    private fun drawHand(canvas: Canvas, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius =
            if (isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation
        canvas.drawLine(
            (width / 2).toFloat(), (height / 2).toFloat(),
            (width / 2 + Math.cos(angle) * handRadius).toFloat(),
            (height / 2 + Math.sin(angle) * handRadius).toFloat(),
            paint!!
        )
    }

    fun drawHands(canvas: Canvas) {
        val c = Calendar.getInstance()
       // var hour = c[Calendar.HOUR_OF_DAY].toFloat()
        var hour = 8.0


        Log.e("hoursssss",hoursOpenedAt.toString())
        Log.e("hoursssss", minsOpenedAt.toString())
        Log.e("hoursssss", secsOpenedAt.toString())

       // hour = if (hour > 12) hour - 12 else hour
       // hours = if (hours > 12) hours - 12 else hours

        if(hoursOpenedAt!=0.0){
            drawHand(canvas, ((hoursOpenedAt + c[Calendar.MINUTE] / 60) * 5f).toDouble(), true)
            drawHand(canvas, minsOpenedAt, false)
        }

      //  drawHand(canvas, secsOpenedAt, false )


       /* drawHand(canvas, ((hour + c[Calendar.MINUTE] / 60) * 5f).toDouble(), true)
        drawHand(canvas, c[Calendar.MINUTE].toDouble(), false)*/
       // drawHand(canvas, c[Calendar.SECOND].toDouble(), false )
       /* else{
            drawHand(canvas, ((hour + c[Calendar.MINUTE] / 60) * 5f).toDouble(), true)
            drawHand(canvas, c[Calendar.MINUTE].toDouble(), false)
            drawHand(canvas, c[Calendar.SECOND].toDouble(), false )
        }*/

    }

    private fun drawNumeral(canvas: Canvas) {
        paint!!.textSize = fontSize.toFloat()
        for (number in numbers) {
            val tmp = number.toString()
            paint!!.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + Math.cos(angle) * radius - rect.width() / 2).toInt()
            val y = (height / 2 + Math.sin(angle) * radius + rect.height() / 2).toInt()
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint!!)
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint!!.style = Paint.Style.FILL
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 12f, paint!!)
    }

    private fun drawCircle(canvas: Canvas) {
        paint!!.reset()
        paint!!.color = resources.getColor(R.color.white)
        //paint!!.color = Color.parseColor("#4CC0A6")
        paint!!.strokeWidth = 5f
        paint!!.style = Paint.Style.STROKE
        paint!!.isAntiAlias = true
        canvas.drawCircle(
            (width / 2).toFloat(), (height / 2).toFloat(), (radius + padding - 10).toFloat(),
            paint!!
        )
    }
}
