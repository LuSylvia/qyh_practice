package com.example.module_common.device

import java.lang.Boolean

object BuildConfig {
    val DEBUG = Boolean.parseBoolean("true")
    const val LIBRARY_PACKAGE_NAME = "com.nearby.android.common"

    @Deprecated("APPLICATION_ID is misleading in libraries. For the library package name use LIBRARY_PACKAGE_NAME")
    val APPLICATION_ID = "com.nearby.android.common"
    const val BUILD_TYPE = "debug"
    const val FLAVOR = ""
    const val VERSION_CODE = 2021072711
    const val VERSION_NAME = "2.15.000"

    // Fields from default config.
    const val BUILD_NUMBER = "1"
    const val DISABLE_ZA_DATA_MONITOR = false
    const val IS_CHECK_CERTIFICATE = false
    const val IS_DEBUG = true
    const val IS_HTTPS = false
    val PHOTO_SERVER_ADDRESS_LIST = arrayOf(
        "photo.zastatic.com",
        "images.zastatic.com",
        "quyuehui-1251661065.image.myqcloud.com"
    )
    const val SERVER_ADDRESS = "api.zhenaihn.com"
    const val UMENG_APP_KEY = "5cb1c00b570df3f1f50012ab"
}