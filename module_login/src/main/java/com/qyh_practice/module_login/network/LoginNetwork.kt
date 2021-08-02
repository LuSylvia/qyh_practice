package com.qyh_practice.module_login.network

import com.example.module_common.retrofit.RetrofitManager
import com.qyh_practice.module_login.api.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object LoginNetwork {
    private val loginService = RetrofitManager.getService<LoginService>()

    suspend fun login(phone: String, code: String, stageToken: String) =
        loginService.mobileLogin2(phone, code, stageToken).await()

    suspend fun getAppconfig() = loginService.getAppConfig2().await()


    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })


        }
    }

}