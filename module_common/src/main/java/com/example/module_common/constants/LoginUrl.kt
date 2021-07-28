package com.example.module_common.constants

class LoginUrl : BaseUrl() {
    companion object {
        /**
         * 登录
         * 需要手机号，验证码，微信token
         */
        const val USER_LOGIN: String = "/account/mobileLogin.do"

        /**
         * 登录
         * 只需要手机号，验证码
         */
        const val USER_LOGIN_WITHOUTTOKEN: String = "/account/mobileLoginExistAccount.do"


        /**
         * 获取验证码
         */
        const val GT_SMS_REQUEST: String = "account/getSmsCode.do"

        /**
         * 发送手机验证码
         */
        const val SEND_PHONE_IDENTITY: String = "api/business/smscode/sendMobileCode.do"

        /**
         * 临时授权码登录
         */
        const val LOGIN_BY_STAGE_TOKEN: String = "account/loginByStageToken.do"
    }
}