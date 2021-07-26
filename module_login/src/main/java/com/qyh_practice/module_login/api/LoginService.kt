package com.qyh_practice.module_login.api

import com.example.module_common.constants.LoginUrl
import com.example.module_common.entity.ResponseEntity
import com.qyh_practice.module_login.entity.LoginEntity
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {

    /**
     * 用户验证码登录
     *
     * @param phone
     * @param code
     * @param stageToken 微信登录时的暂存token
     * @return
     */
    @FormUrlEncoded
    @POST(LoginUrl.USER_LOGIN)
    suspend fun identityLogin(
        @Field("mobile") phone: String,
        @Field("code") code: String,
        @Field("stageToken") stageToken: String
    ): ResponseEntity<LoginEntity>


}