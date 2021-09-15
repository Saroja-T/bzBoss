package com.bzboss.app.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserKnownVisitorsData(
        @SerializedName("status_code") val status_code : Int,
        @SerializedName("message") val message : String,
        @SerializedName("data") val data : List<Data>
) {
    @Keep
    data class Data (

            @SerializedName("known_visitors_log_id") val known_visitors_log_id : Int,
            @SerializedName("premise_daily_data_id") val premise_daily_data_id : Int,
            @SerializedName("known_visitors_id") val known_visitors_id : Int,
            @SerializedName("known_visitors_name") val known_visitors_name : String,
            @SerializedName("date_time_recorded") val date_time_recorded : String,
            @SerializedName("appearance_date_time") val appearance_date_time : String,
            @SerializedName("appearance_image") val appearance_image : String,
            @SerializedName("duration") val duration : String,
            @SerializedName("created_at") val created_at : String,
            @SerializedName("updated_at") val updated_at : String
    )
}