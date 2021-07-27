package com.example.module_common.constants

/**
 * 存储最为基础的URL
 */

open class BaseUrl {
    companion object {
        //测试用接口
        const val BASEURL: String = "https://api.zhenai.com"

        //绑定手机号
        const val MOBILE_BIND = "/account/mobileBind.do"
    }


}