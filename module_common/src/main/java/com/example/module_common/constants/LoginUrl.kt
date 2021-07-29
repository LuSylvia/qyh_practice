package com.example.module_common.constants

class LoginUrl : BaseUrl() {
    companion object {
        /**
         * 登录
         * 需要手机号，验证码，微信token
         */
        const val USER_LOGIN: String = "/account/mobileLogin.do"

        /**
         * 获取APP配置
         * 未登陆过时，需要调用上面的USER_LOGIN接口后，才能调用本接口
         * 已登陆过时，直接在闪屏页调用该接口，获取配置后，进入MainActivity
         */
        const val GET_APP_CONFIG:String="/system/appConfig.do"

        /**
         * 获取验证码
         */
        const val GT_SMS_REQUEST: String = "account/getSmsCode.do"

        /**
         * 发送手机验证码
         */
        const val SEND_PHONE_IDENTITY: String = "api/business/smscode/sendMobileCode.do"

    }
}