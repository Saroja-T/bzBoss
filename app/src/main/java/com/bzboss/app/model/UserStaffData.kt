package com.bzboss.app.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep







/*@Keep
data class UserStaffData(
    @SerializedName("status_code")
    val statusCode: Int, // 200
    @SerializedName("message")
    val message: String, // staff data
    @SerializedName("data")
    val `data`: List<Data>
) {
    @Keep
    data class Data(
        @SerializedName("staff_id")
        val staffId: Int, // 1
        @SerializedName("token_id")
        val tokenId: String, // 1234
        @SerializedName("premise_id")
        val premiseId: Int, // 1
        @SerializedName("date_time_recorded")
        val dateTimeRecorded: String, // 2021-04-12 18:00:00
        @SerializedName("first_name")
        val firstName: String, // harshit
        @SerializedName("last_name")
        val lastName: String, // darji
        @SerializedName("title")
        val title: String, // Heelo
        @SerializedName("photo")
        val photo: String, // http://192.168.1.10/bizzboss/public/uploads/stafflog/42ffe723d8ccd56ba5dcd4bd013327f4161978071899.png
        @SerializedName("created_at")
        val createdAt: String, // 2021-04-30 00:00:00
        @SerializedName("updated_at")
        val updatedAt: String // 2021-04-30 00:00:00
    )
}*/


@Keep
data class UserStaffData(
    @SerializedName("status_code")
    val statusCode: Int, // 200
    @SerializedName("message")
    val message: String, // staff data
    @SerializedName("data")
    val `data`: List<Data>
) {
    @Keep
    data class Data(
        @SerializedName("staff_log_id")
        val staffLogId: Int, // 13
        @SerializedName("premise_daily_data_id")
        val premiseDailyDataId: Int, // 17
        @SerializedName("date_time_recorded")
        val dateTimeRecorded: String, // 2021-04-30 11:05:18
        @SerializedName("staff_id")
        val staffId: Int, // 2
        @SerializedName("staff_name")
        val staffName: String, //  STAFF1
        @SerializedName("number_of_appearances")
        val numberOfAppearances: Int, // 32
        @SerializedName("threshold")
        val threshold: String, // 0.54
        @SerializedName("first_appearance_date_time")
        val firstAppearanceDateTime: String, // 2021-04-29 10:32:00
        @SerializedName("first_appearance_image")
        val firstAppearanceImage: String, // 42ffe723d8ccd56ba5dcd4bd013327f41619780718880.png
        @SerializedName("first_appearance_score")
        val firstAppearanceScore: Int, // 0
        @SerializedName("max_score_date_time")
        val maxScoreDateTime: String, // 2021-04-30 10:32:00
        @SerializedName("max_score_image")
        val maxScoreImage: String, // 42ffe723d8ccd56ba5dcd4bd013327f41619780718701.png
        @SerializedName("max_score")
        val maxScore: Int, // 11
        @SerializedName("duration")
        val duration: String, // STAFF
        @SerializedName("created_at")
        val createdAt: String, // 2021-04-30 05:35:18
        @SerializedName("updated_at")
        val updatedAt: String // 2021-04-30 05:35:18
    )
}