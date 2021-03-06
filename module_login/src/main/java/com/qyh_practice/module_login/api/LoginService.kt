package com.qyh_practice.module_login.api

import com.example.module_common.constants.LoginUrl
import com.example.module_common.entity.AppConfigEntity
import com.example.module_common.entity.ResponseEntity
import com.qyh_practice.module_login.entity.LoginEntity
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {

    /**
     * 用户验证码登录
     *
     * @param phone
     * @param code
     * @param stageToken 微信登录时的暂存token
     * @return ResponseEntity<LoginEntity>
     */
    @FormUrlEncoded
    @POST(LoginUrl.USER_LOGIN)
    suspend fun mobileLogin(
        @Field("mobile") phone: String,
        @Field("code") code: String,
        @Field("stageToken") stageToken: String
    ): ResponseEntity<LoginEntity>


    @FormUrlEncoded
    @POST(LoginUrl.USER_LOGIN)
    fun mobileLogin2(
        @Field("mobile") phone: String,
        @Field("code") code: String,
        @Field("stageToken") stageToken: String
    ): Call<ResponseEntity<LoginEntity>>


    @GET(LoginUrl.GET_APP_CONFIG)
    suspend fun getAppConfig(): ResponseEntity<AppConfigEntity>;


    @GET(LoginUrl.GET_APP_CONFIG)
    fun getAppConfig2(): Call<ResponseEntity<AppConfigEntity>>;


}