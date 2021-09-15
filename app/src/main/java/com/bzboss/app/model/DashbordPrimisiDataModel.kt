package com.bzboss.app.model


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep
data class DashbordPrimisiDataModel(
    @SerializedName("status_code")
    val statusCode: Int, // 200
    @SerializedName("message")
    val message: String, // get premise  data
    @SerializedName("data")
    val `data`: Data,
) {
    @Keep
    data class Data(
        @SerializedName("premise_daily_data_id")
        val premiseDailyDataId: Int, // 1
        @SerializedName("premise_id")
        val premiseId: Int, // 1
        @SerializedName("date_time_recorded")
        val dateTimeRecorded: String, // 2021-04-28 08:50:39
        @SerializedName("date")
        val date: String, // 2021-04-28
        @SerializedName("opened_at")
        val openedAt: String, // 10:03:22
        @SerializedName("opened_at_image")
        val openedAtImage: String, // http://192.168.1.10/bizzboss/public/uploads/premisedailydata/4205fe62be8572d31978b22e0eeca4e21619599839590.png
        @SerializedName("first_customer_time")
        val firstCustomerTime: String, // 10:13:11
        @SerializedName("first_customer_image")
        val firstCustomerImage: String, // http://192.168.1.10/bizzboss/public/uploads/premisedailydata/4205fe62be8572d31978b22e0eeca4e21619599839261.png
        @SerializedName("number_of_customers_min")
        val numberOfCustomersMin: Int, // 100
        @SerializedName("number_of_customers_max")
        val numberOfCustomersMax: Int, // 200
        @SerializedName("number_of_known_customers_min")
        val numberOfKnownCustomersMin: Int, // 100
        @SerializedName("number_of_known_customers_max")
        val numberOfKnownCustomersMax: Int, // 200
        @SerializedName("number_of_vip_customers_min")
        val numberOfVipCustomersMin: Int, // 100
        @SerializedName("number_of_vip_customers_max")
        val numberOfVipCustomersMax: Int, // 200
        @SerializedName("number_of_staff_min")
        val numberOfStaffMin: Int, // 10
        @SerializedName("number_of_staff_max")
        val numberOfStaffMax: Int, // 50
        @SerializedName("number_of_known_visitors_min")
        val numberOfKnownVisitorsMin: Int, // 10
        @SerializedName("number_of_known_visitors_max")
        val numberOfKnownVisitorsMax: Int, // 50
        @SerializedName("created_at")
        val createdAt: String, // 2021-04-28 08:50:39
        @SerializedName("updated_at")
        val updatedAt: String, // 2021-04-28 08:50:39
        @SerializedName("getcurrentstatus")
        val getcurrentstatus: Getcurrentstatus,
        @SerializedName("number_of_customersnumber")
        val numberOfCustomersnumber: Int, // 100
        @SerializedName("number_of_customers")
        val numberOfCustomers: String, // up
        @SerializedName("userconfigdata")
        val `userconfigdata`: Userconfigdata,
        @SerializedName("userpremise")
        val `userpremise`: UserPremiseData,
        @SerializedName("premisedata")
        val premisedata: Premisedata,
        @SerializedName("closed_at")
        val closedAt: String,
        @SerializedName("closed_at_image")
        val closedAtImage: String,
        @SerializedName("known_visitors")
        val knownVisitors: String,
    ) {
        @Keep
        data class Getcurrentstatus(
            @SerializedName("premise_current_status_id")
            val premiseCurrentStatusId: Int, // 8
            @SerializedName("premise_id")
            val premiseId: Int, // 1
            @SerializedName("date_time_recorded")
            val dateTimeRecorded: String, // 2021-04-29 00:00:00
            @SerializedName("status")
            val status: String, // Open
            @SerializedName("last_updated")
            val lastUpdated: String, // 2021-04-29 08:50:39
            @SerializedName("image_filename")
            val imageFilename: String, // http://192.168.1.10/bizzboss/public/uploads/premisecurrentstatus/4205fe62be8572d31978b22e0eeca4e2161959983919.png
            @SerializedName("created_at")
            val createdAt: String, // 2021-04-28 03:20:39
            @SerializedName("updated_at")
            val updatedAt: String // 2021-04-28 03:20:39
        )
        @Keep
        data class UserPremiseData(
            @SerializedName("user_premise_link_id") val user_premise_link_id : Int,
            @SerializedName("user_id") val user_id : Int,
            @SerializedName("premise_id") val premise_id : Int,
            @SerializedName("opened_at") val opened_at : Boolean,
            @SerializedName("first_customer") val first_customer : Boolean,
            @SerializedName("customer") val customer : Boolean,
            @SerializedName("staff") val staff : Boolean,
            @SerializedName("closed_at") val closed_at : Boolean,
            @SerializedName("visitors") val visitors : Boolean,
            @SerializedName("date_time_recorded") val date_time_recorded : String,
            @SerializedName("active") val active : Boolean,
            @SerializedName("created_at") val created_at : String,
            @SerializedName("updated_at") val updated_at : String
        )

        @Keep
        data class Userconfigdata(
            @SerializedName("userconfigration_id") val userconfigration_id : Int,
            @SerializedName("user_premise_link_id") val user_premise_link_id : Int,
            @SerializedName("date") val date : String,
            @SerializedName("toggle") val toggle : String,
            @SerializedName("targetstaff") val targetstaff : Int,
            @SerializedName("targetcust") val targetcust : Int,
            @SerializedName("targetknown") val targetknown : Int,
            @SerializedName("created_at") val created_at : String,
            @SerializedName("updated_at") val updated_at : String
        )

        @Keep
        data class Premisedata(
            @SerializedName("premise_id")
            val premiseId: Int, // 1
            @SerializedName("token_id")
            val tokenId: String, // 45bcc085-7403-45ff-a1c4-8a3170c07067
            @SerializedName("date_time_recorded")
            val dateTimeRecorded: String, // 2021-04-01 00:00:00
            @SerializedName("title")
            val title: String, // Showroom
            @SerializedName("name")
            val name: String, // ad
            @SerializedName("sub_name")
            val subName: String, // asd
            @SerializedName("address")
            val address: String, // asd
            @SerializedName("country")
            val country: String, // asd
            @SerializedName("state")
            val state: String, // das
            @SerializedName("city")
            val city: String, // sadasd
            @SerializedName("photo")
            val photo: String, // http://192.168.1.10/bizzboss/public/uploads/premise/4205fe62be8572d31978b22e0eeca4e21619599839427.png
            @SerializedName("reserved")
            val reserved: String, // asdasd
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("updated_at")
            val updatedAt: String
        )

    }
}