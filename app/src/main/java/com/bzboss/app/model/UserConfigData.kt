package com.bzboss.app.model


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep
data class UserConfigData(
    @SerializedName("status_code") val status_code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : Data
) {
    @Keep
    data class Data (
        @SerializedName("userconfigration_id") val userconfigration_id : Int,
        @SerializedName("user_premise_link_id") val user_premise_link_id : Int,
        @SerializedName("date") val date : String,
        @SerializedName("toggle") val toggle : String,
        @SerializedName("targetstaff") val targetstaff : Int,
        @SerializedName("targetcust") val targetcust : Int,
        @SerializedName("created_at") val created_at : String,
        @SerializedName("updated_at") val updated_at : String
    )
}