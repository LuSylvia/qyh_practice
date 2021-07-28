package com.qyh_practice.module_login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module_common.entity.LoadState
import com.example.module_common.retrofit.RetrofitManager
import com.qyh_practice.module_login.api.LoginService
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    //val
    val loadState = MutableLiveData<LoadState>()

    /**
     * 登录
     * 需要根据返回值来处理具体逻辑
     */
    fun login(phone: String, code: String): String? {
        var retMsg: String? = null
        viewModelScope.launch {
            loadState.value = LoadState.LOADING
            val loginService: LoginService = RetrofitManager.getService(LoginService::class.java)
            val responseBody = loginService.mobileLoginExistAccount(phone, code)
            if (responseBody.isError) {
                //请求失败，网络异常
                loadState.value = LoadState.FAIL
                retMsg = responseBody.errorMessage


            } else {
                loadState.value = LoadState.SUCCESS
            }

        }
        return retMsg

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