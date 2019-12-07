package com.hackathon.project1.data

import com.hackathon.project1.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIGenerator {

    var API_BASE_URL = "http://192.168.43.183:8080/"

    private val httpClient = prepareClient()


    private fun prepareClient(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY //if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC

        val builder = OkHttpClient.Builder()
        builder.addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .followSslRedirects(false)
            .followRedirects(false)
        return builder
    }

    private fun prepareRetrofit(serverURL: String): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(serverURL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    private fun <S> generateAPIs(apiClass: Class<S>, builder: Retrofit.Builder): S {

        builder.client(httpClient.build())

        val retrofit = builder.build()
        return retrofit.create(apiClass)
    }



    fun getClient(): APIs {
        return generateAPIs(APIs::class.java, prepareRetrofit(API_BASE_URL))
    }
}