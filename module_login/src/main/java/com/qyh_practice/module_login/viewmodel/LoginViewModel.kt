package com.qyh_practice.module_login.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_common.BaseViewModel
import com.example.module_common.entity.AppConfigEntity
import com.example.module_common.entity.LoadState
import com.example.module_common.retrofit.RetrofitManager
import com.example.module_common.retrofit.launch
import com.qyh_practice.module_login.api.LoginService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class LoginViewModel : BaseViewModel() {

    val loadState = MutableLiveData<LoadState>()

    private val mService by lazy { RetrofitManager.getService(LoginService::class.java) }

    /**
     * 登录
     * 需要根据返回值来处理具体逻辑
     */
    fun login(phone: String, code: String, stageToken: String) {
        launch({
            loadState.value = LoadState.LOADING

            val loginResponse = (async {
                mService.mobileLogin(phone, code, stageToken)
            }).await()
            //等登录接口的返回值
            if (loginResponse.isError) {
                //mobileLogin出错
                Log.d("LoginViewModel", "登录失败，提示是${loginResponse.errorMessage}")
                loadState.value = LoadState.FAIL
            } else {
                //mobileLogin成功
                Log.d("LoginViewModel", "登录成功，userId是${loginResponse.data.userId}")
                //AccountManager.getInstance().saveAccount(phone,)

                //获取系统配置信息
                getAppConfig()
            }

        }, {
            loadState.value = LoadState.FAIL
        }

        )

    }

    /**
     * 获取系统配置信息
     */
    fun getAppConfig(): AppConfigEntity? {
        var appConfig: AppConfigEntity? = null
        viewModelScope.launch {
            try {
                val responseBody = mService.getAppConfig()

                if (responseBody.isError) {
                    //请求失败，打印错误信息
                    Log.d("Sylvia_Exception", "errorMessage是"+responseBody.errorMessage)
                } else {
                    //请求成功，返回配置信息
                    appConfig = responseBody.data
                    loadState.value = LoadState.SUCCESS
                    Log.d("Sylvia-Success","成功了，回复是"+responseBody.data.avatar)

                }

            } catch (e: Exception) {
                Log.d("Sylvia_Exception", "loginviewmodel,异常是" + e.message)
            }
        }
        return appConfig
    }


}


