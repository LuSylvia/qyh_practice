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

    suspend fun login(phone: String, code: String, stageToken: String) {
        viewModelScope.launch {
            loadState.value = LoadState.LOADING
            val loginService: LoginService = RetrofitManager.getService(LoginService::class.java)
            val responseBody = loginService.identityLogin(phone, code, stageToken)
            if (responseBody.isError) {
                //请求失败，网络异常
                loadState.value = LoadState.FAIL


            } else {
                loadState.value = LoadState.SUCCESS
            }

        }

    }

}