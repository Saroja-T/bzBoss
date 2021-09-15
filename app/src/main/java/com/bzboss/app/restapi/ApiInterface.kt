package com.bzboss.app.restapi

import com.bzboss.app.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    // this function is used for Login user in application
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("user/login")
    fun userLoginFunction(
        @Field("phone_number") first_name: String,
        @Field("device_type") device_type: String,
        @Field("device_token") device_token: String,
        @Field("time_zone") time_zone: String,
    ): Call<ResponseBody>


    // this function is used for register user in application
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("user/register")
    fun userRegisterFunction(
        @Field("phone_number") phone_number: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("access_level") access_level: String,
        @Field("device_type") device_type: String,
        @Field("device_token") device_token: String,
        @Field("time_zone") time_zone: String,
        @Field("type") type: String,
    ): Call<ResponseBody>


    @Headers("Accept:application/json")
    @GET("user/home/data")
    fun homeDataGet(
        @Header("Authorization") inToken: String,
    ): Call<HomeDataModel>


    /*Home data get*/
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("user/premise")
    fun userPremiseData(
        @Header("Authorization") inToken: String,
        @Field("date") date: String,
        @Field("id") id: String,
        @Field("user_id") user_id: String,
    ): Call<DashbordPrimisiDataModel>


    /*Home data get*/
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("user/premise/graph")
    fun userPremiseGraphData(
        @Header("Authorization") inToken: String,
        @Field("startdate") startdate: String,
        @Field("enddate") enddate: String,
        @Field("id") id: String,
    ): Call<HomeGraphData>


    /*Home data get*/
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("contact/us")
    fun contactUs(
        @Header("Authorization") inToken: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("category") category: String,
        @Field("message") message: String,
    ): Call<ResponseBody>



    /*Home data get*/
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("user/staff")
    fun userStaffData(
        @Header("Authorization") inToken: String,
        @Field("id") id: String,
        @Field("startdate") startdate: String,
        @Field("enddate") enddate: String,
    ): Call<UserStaffData>

    /*Home data get*/
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("user/staff/details")
    fun userStaffDetailsData(
        @Header("Authorization") inToken: String,
        @Field("staff_id") id: String,
        @Field("premise_id") premiseId: String,
        @Field("startdate") startdate: String,
        @Field("enddate") enddate: String,
        ): Call<StaffDetailsGraphData>


    /*Home data get*/
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("user/userconfig")
    fun userConfigData(
        @Header("Authorization") inToken: String,
        @Field("user_id") id: String,
        @Field("premise_id") premiseId: String,
        @Field("date") date: String,
        @Field("toggle") toggle: String,
        @Field("targetstaff") targetStaff: String,
        @Field("targetcust") targetCustomer: String,
        @Field("targetknown") targetKnown: String,
    ): Call<UserConfigData>

    /*Home data get*/
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("user/knownvisitors")
    fun userKnownVisitorsData(
            @Header("Authorization") inToken: String,
            @Field("premise_id") id: String,
            @Field("startdate") startdate: String,
            @Field("enddate") enddate: String,
    ): Call<UserKnownVisitorsData>

    /*Home data get*/
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("user/knownvisitors/details")
    fun userKnownVisitorsDetailsData(
            @Header("Authorization") inToken: String,
            @Field("known_visitors_id") id: String,
            @Field("premise_id") premiseId: String,
            @Field("startdate") startdate: String,
            @Field("enddate") enddate: String,
): Call<KnownVisitorsDetailsGraphData>
    /*Home data get*/
    @FormUrlEncoded
    @Headers("Accept:application/json")
    @POST("user/useraccess")
    fun userAccess(
           // @Header("Authorization") inToken: String,
            @Field("user_id") id: String,
    ): Call<UserAccessData>


}