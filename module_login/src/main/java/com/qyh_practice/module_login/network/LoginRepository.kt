package com.qyh_practice.module_login.network

import androidx.lifecycle.liveData
import com.example.module_common.entity.ResponseEntity
import com.example.module_common.utils.LogUtil
import com.qyh_practice.module_login.entity.LoginEntity
import kotlinx.coroutines.Dispatchers

object LoginRepository {
    private const val TAG = "LoginRepository"
    fun login(phone: String, code: String, stageToken: String) = liveData(Dispatchers.IO) {
        val result = try {
            val loginResponse = LoginNetwork.login(phone, code, stageToken)
            if (!loginResponse.isError) {
                getAppconfig()
                Result.success("Success")
            } else {
                //登录失败，打印日志
                LogUtil.d(
                    TAG,
                    "response error code is ${loginResponse.errorCode},error message is ${loginResponse.errorMessage}"
                )
                Result.failure(RuntimeException("response error code is ${loginResponse.errorCode},error message is ${loginResponse.errorMessage}"))
            }

        } catch (e: Exception) {
            Result.failure<List<ResponseEntity<LoginEntity>>>(e)
        }

        emit(result)
    }

    fun getAppconfig() = liveData(Dispatchers.IO) {
        val result = try {
            val getAppconfigResponse = LoginNetwork.getAppconfig()
            if (!getAppconfigResponse.isError) {
                //获取配置成功
                Result.success(getAppconfigResponse.data)
            } else {
                Result.failure(RuntimeException("response error code is ${getAppconfigResponse.errorCode},error message is ${getAppconfigResponse.errorMessage}"))
            }
        } catch (e: Exception) {
            LogUtil.e(TAG, e.message.toString())
        }
        emit(result)
    }

}