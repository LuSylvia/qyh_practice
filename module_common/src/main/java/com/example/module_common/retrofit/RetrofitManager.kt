package com.example.module_common.retrofit

import android.util.Log
import com.example.module_common.constants.BaseUrl
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {


    private val retrofit: Retrofit

    init {
        val gson = Gson().newBuilder()
            .setLenient()
            .serializeNulls()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl.BASEURL)
            .client(initOkhttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }

    //具体服务实例化
    fun <T> getService(service: Class<T>): T {
        return retrofit.create(service)
    }

    /**
     * 设置okhttpclient，包括超时设置，日志拦截器
     */
    private fun initOkhttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(initLogInterceptor())
            .build()

        return okHttpClient
    }

    /**
     * 设置拦截器，打印日志
     */
    private fun initLogInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String?) {
                //message非空时，打印信息
                message?.let {
                    Log.i("Retrofit", message)
                }
            }
        })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }


}