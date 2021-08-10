package com.qyh_practice.module_login.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.module_common.BaseViewModel
import com.example.module_common.entity.LoadState
import com.example.module_common.retrofit.RetrofitManager
import com.example.module_common.retrofit.launch
import com.example.module_common.utils.LogUtil
import com.qyh_practice.module_login.api.LoginService
import kotlinx.coroutines.async


class LoginViewModel : BaseViewModel() {

    val loadState = MutableLiveData<LoadState>()

    private val mService by lazy { RetrofitManager.getService(LoginService::class.java) }

    /**
     * 登录
     * 需要根据返回值来处理具体逻辑
     */
    fun login(phone: String, code:String,stageToken: String) {
        launch({
            loadState.value=LoadState.LOADING

            val loginResponse=(async {
                mService.mobileLogin(phone,code, stageToken)
            }).await()
            //等登录接口的返回值
            if(loginResponse.isError){
                //mobileLogin出错
                Log.d("LoginViewModel","登录失败，提示是${loginResponse.errorMessage}")
                loadState.value=LoadState.FAIL
            }else{
                //mobileLogin成功
                Log.d("LoginViewModel","登录成功，userId是${loginResponse.data.userId}")

//                RetrofitManager.setToken(loginResponse.await().data.temporaryToken)
                getAppConfig()
            }

        },{
            loadState.value = LoadState.FAIL
        }

        )

    }

    /**
     * 获取系统配置信息
     */
    fun getAppConfig():AppConfigEntity?{
        var appConfig:AppConfigEntity?=null
        viewModelScope.launch {
            try {
                val responseBody=mService.getAppConfig()

                 if (responseBody.isError) {
                     //请求失败，打印错误信息
                    Log.d("LoginViewModel_config", responseBody.errorMessage)
                } else {
                    //请求成功，返回配置信息
                    appConfig = responseBody.data
                }

            }catch (e:Exception){
                Log.d("LoginViewModel_config","异常是"+e.message)
            }
        }
        return appConfig
    }


    /**
     * 获取验证码
     * 该函数不需要处理返回值
     */
    fun getSmsCode(phone: String, type: Int) {
        viewModelScope.launch {
            val loginService = RetrofitManager.getService(LoginService::class.java)
            loginService.getSmsCode(phone, type)
        }
    }

}


