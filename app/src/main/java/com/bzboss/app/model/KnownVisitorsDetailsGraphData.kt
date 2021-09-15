package com.bzboss.app.model


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep
data class KnownVisitorsDetailsGraphData(
    @SerializedName("status_code")
    val statusCode: Int, // 200
    @SerializedName("message")
    val message: String, // get premise  data
    @SerializedName("data")
    val `data`: Data
) {
    @Keep
    data class Data(
            @SerializedName("knownVisitorsData") val knownVisitorsData : List<KnownVisitorsData>
    ) {
        @Keep
        data class KnownVisitorsData(
                @SerializedName("known_visitors_id") val known_visitors_id : Int,
                @SerializedName("token_id") val token_id : String,
                @SerializedName("premise_id") val premise_id : Int,
                @SerializedName("date_time_recorded") val date_time_recorded : String,
                @SerializedName("first_name") val first_name : String,
                @SerializedName("last_name") val last_name : String,
                @SerializedName("photo") val photo : String,
                @SerializedName("created_at") val created_at : String,
                @SerializedName("updated_at") val updated_at : String,
                @SerializedName("knownvisitorsdata") val knownvisitorsdata : List<Knownvisitorsdata>
        ){
            @Keep
            data class Knownvisitorsdata(
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
            )// 200
        }
    }
}