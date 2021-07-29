package com.qyh_practice.module_login.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module_common.BaseViewModel
import com.example.module_common.entity.AppConfigEntity
import com.example.module_common.entity.LoadState
import com.example.module_common.retrofit.RetrofitManager
import com.example.module_common.retrofit.launch
import com.qyh_practice.module_login.api.LoginService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class LoginViewModel : BaseViewModel() {

    val loadState = MutableLiveData<LoadState>()

    /**
     * 登录
     * 需要根据返回值来处理具体逻辑
     */
    fun login(phone: String, code:String,stageToken: String) {
        launch({
            loadState.value=LoadState.LOADING
            val loginService: LoginService = RetrofitManager.getService(LoginService::class.java)

            val loginResponse=async { loginService.mobileLogin(phone,code, stageToken).errorMessage }
            //等登录接口的返回值
            val loginErrMsg=loginResponse.await()

            if(!loginErrMsg.equals("")){
                //出错
                Log.d("LoginViewModel-login","登录失败，提示是${loginErrMsg}")
                    loadState.value=LoadState.FAIL
            }else{
                //loadState.value=LoadState.SUCCESS
                val appConfigMsg=async { loginService.getAppConfig().errorMessage }
                Log.d("LoginViewModel-msg","配置异常,提示是${appConfigMsg.await()}")
            }

        },{
            loadState.value=LoadState.FAIL
        }

        )




    }

    /**
     * 获取系统配置信息
     */
    fun getAppConfig():AppConfigEntity?{
        var appConfig:AppConfigEntity?=null
        viewModelScope.launch {
            val loginService=RetrofitManager.getService(LoginService::class.java)
            try {
                val responseBody=loginService.getAppConfig()

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


