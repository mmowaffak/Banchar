package com.hackathon.project1.data

import com.hackathon.project1.data.model.*
import retrofit2.Call
import retrofit2.http.*


interface APIs {

    @POST("api/auth/signup")
    fun signup(@Body authData: AuthData): Call<UserData>

    @POST("api/auth/signin")
    fun login(@Body authData: AuthData): Call<UserData>

    @POST("rest/initiateRequest")
    fun request(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<Int>

    @POST("rest/isRequestAccepted")
    fun requestStatus(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<Boolean>

    @POST("rest/whoIsMyWinch")
    fun whoIsMyWinch(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<DriverData>

    @POST("rest/whereIsMyWinch")
    fun whereIsMyWinch(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<DriverData>

    @POST("rest/cancelRequest")
    fun cancelRequest(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<Boolean>

    @POST("rest/getHistoricalRequest")
    fun getHistory(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<List<History>>

    @POST("rest/ratePayWinch")
    fun ratePayWinch(@Body payRateData: PayRateData, @Header("Authorization") token: String?): Call<Any>

    @POST("rest/isMyTripComplete")
    fun isMyTripComplete(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<Boolean>


    @POST("rest/getUserInfo")
    fun getUserInfo(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<DriverData>

    @POST("rest/anyRequestsAvailableToService")
    fun anyRequestsAvailableToService(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<OrderData>

    @POST("rest/acceptRequest")
    fun acceptRequest(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<Boolean>

    @POST("rest/declineRequest")
    fun declineRequest(@Body requestData: RequestData, @Header("Authorization") token: String?): Call<Boolean>

//    @GET("users/{id}")
//    fun getUser(@Path("id") id: Int): Call<Any>

}