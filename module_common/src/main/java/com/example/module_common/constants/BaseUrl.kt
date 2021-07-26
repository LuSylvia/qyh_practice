package com.example.module_common.constants

/**
 * 绑定手机号
 */

open class BaseUrl {
    companion object {
        //TODO:更新构建retrofit的baseurl
        const val BASEURL: String = "http://www.test.com"

        //绑定手机号
        const val MOBILE_BIND = "/account/mobileBind.do"
    }


}