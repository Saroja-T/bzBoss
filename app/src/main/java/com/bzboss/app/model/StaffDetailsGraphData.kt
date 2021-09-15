package com.bzboss.app.model


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep
data class StaffDetailsGraphData(
    @SerializedName("status_code")
    val statusCode: Int, // 200
    @SerializedName("message")
    val message: String, // get premise  data
    @SerializedName("data")
    val `data`: Data
) {
    @Keep
    data class Data(
        @SerializedName("staffDetailsData")
        val staffDetailsData : List<StaffDetailsData>
    ) {
        @Keep
        data class StaffDetailsData(
                @SerializedName("staff_id") val staff_id : Int,
                @SerializedName("token_id") val token_id : String,
                @SerializedName("premise_id") val premise_id : Int,
                @SerializedName("date_time_recorded") val date_time_recorded : String,
                @SerializedName("first_name") val first_name : String,
                @SerializedName("last_name") val last_name : String,
                @SerializedName("title") val title : String,
                @SerializedName("photo") val photo : String,
                @SerializedName("created_at") val created_at : String,
                @SerializedName("updated_at") val updated_at : String,
                @SerializedName("staffdata") val staffdata : List<Staffdata>
        ){
            @Keep
            data class Staffdata(
                    @SerializedName("staff_log_id") val staff_log_id : Int,
                    @SerializedName("premise_daily_data_id") val premise_daily_data_id : Int,
                    @SerializedName("date_time_recorded") val date_time_recorded : String,
                    @SerializedName("staff_id") val staff_id : Int,
                    @SerializedName("staff_name") val staff_name : String,
                    @SerializedName("number_of_appearances") val number_of_appearances : Int,
                    @SerializedName("threshold") val threshold : Int,
                    @SerializedName("first_appearance_date_time") val first_appearance_date_time : String,
                    @SerializedName("first_appearance_image") val first_appearance_image : String,
                    @SerializedName("first_appearance_score") val first_appearance_score : Int,
                    @SerializedName("max_score_date_time") val max_score_date_time : String,
                    @SerializedName("max_score_image") val max_score_image : String,
                    @SerializedName("max_score") val max_score : Int,
                    @SerializedName("duration") val duration : Int,
                    @SerializedName("created_at") val created_at : String,
                    @SerializedName("updated_at") val updated_at : String
            )// 200
        }
    }
}