package com.example.module_common.entity

open class BaseRepository {

    //数据脱壳与错误预处理
    open fun <T> preprocessData(responseBody: ResponseEntity<T>): T {
        return if (!responseBody.isError) responseBody.data else throw Throwable(responseBody.errorMessage)
    }

    open suspend fun getData() {}


}
