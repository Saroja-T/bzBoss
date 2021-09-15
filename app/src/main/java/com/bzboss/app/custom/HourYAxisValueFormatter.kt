package com.bzboss.app.custom

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*


class HourYAxisValueFormatter : ValueFormatter() {
    companion object {
        private val TAG = HourYAxisValueFormatter::class.java.simpleName
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val date = Date(value.toLong())
        val dateFormatExpression = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(date)

        // return Date(new Float(value).longValue()).toString();
       /* return when {
            value == 0.toFloat() -> {
                "${getHourString(value)}"
                //value.toInt().toString()
            }
            else -> {
                SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(date)
            }
        }*/
    }
    private fun getHourString(value: Float): String {
        val hours: Int = value.toInt() / 60 //since both are ints, you get an int
        val minutes: Int = value.toInt() % 60
        return String.format("%d:%02d", hours, minutes)
    }
}