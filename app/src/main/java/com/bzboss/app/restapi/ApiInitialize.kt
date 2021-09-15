package com.bzboss.app.restapi

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiInitialize {

   // val LOCAL_URL ="http://192.168.1.10/bizzboss/public/api/"
    val LOCAL_URL ="https://theadeptz.com/bzBoss/public/api/"
    //val LOCAL_URL ="https://demo.emeetify.com:4500/api/"

   // val LOCAL_URL ="https://655b-106-195-45-145.ngrok.io/api/"
    private var retrofit: Retrofit? = null
    private lateinit var apiInIt: ApiInterface

    @Synchronized
    fun getApiInIt(): ApiInterface {
        return apiInIt
    }

    fun initialize(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(LOCAL_URL)
                .client(requestHeader)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }

    @JvmStatic
    fun initializes(): ApiInterface {

        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(LOCAL_URL)
            .client(requestHeader)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        apiInIt = retrofit!!.create(ApiInterface::class.java)
        return apiInIt
    }

    fun initialize(baseUrl: String): ApiInterface {

        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(requestHeader)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        apiInIt = retrofit!!.create(ApiInterface::class.java)
        return apiInIt
    }

    private val requestHeader: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .cache(null)
                .build()
        }
}
