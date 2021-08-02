package com.example.module_common.retrofit

import android.util.Log
import com.example.module_common.constants.BaseUrl
import com.example.module_common.device.DeviceInfoManager
import com.google.gson.Gson
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {

    private var loginToken: String? = null
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

    public fun setToken(token: String) {
        this.loginToken = token
    }

    //具体服务实例化
    fun <T> getService(service: Class<T>): T {
        return retrofit.create(service)
    }

    inline fun <reified T> getService(): T = getService(T::class.java)

    /**
     * 设置okhttpclient，包括超时设置，日志拦截器
     */
    private fun initOkhttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(initLogInterceptor())
            .addInterceptor(initRequestIntercepetor())
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                    if (!cookies.isNullOrEmpty()) {
                        mCookieMap[getDomain(url)] = cookies
                    }
                }

                override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                    return mCookieMap[getDomain(url)] ?: arrayListOf()
                }
            })
            .build()

        return okHttpClient
    }

    private var mCookieMap = HashMap<String, MutableList<Cookie>>()

    private fun getDomain(url: HttpUrl):String {
        return url.topPrivateDomain() ?: url.host() ?: ""
    }


    private fun initRequestIntercepetor(): Interceptor {
        return Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            //更新此处的value为DeviceInfoManager.getUAInfo(String key)
            //1.先从本地读取缓存
//            val request = if (loginToken == null || loginToken.equals("")) {
//                requestBuilder.build()
//            } else {
//                requestBuilder.addHeader("ua", DeviceInfoManager.getInstance().getUAInfo(loginToken))
//                    .build()
//            }

            val request = requestBuilder
                .addHeader("Accept-Charset", "UTF-8,*;q=0.5")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Connection", "Keep-Alive")
                .addHeader("ua", DeviceInfoManager.getInstance().getUAInfo(loginToken ?: ""))
                .build()

            chain.proceed(request)
        }
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