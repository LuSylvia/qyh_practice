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


    /**
     * 登录
     * 需要根据返回值来处理具体逻辑
     */
    fun login(phone: String, code:String,stageToken: String) {
        launch({
            loadState.value=LoadState.LOADING
            val loginService: LoginService = RetrofitManager.getService(LoginService::class.java)

            val loginResponse = async { loginService.mobileLogin(phone, code, stageToken) }.await()

            if (loginResponse.isError) {
                //出错
                LogUtil.d("LoginViewModel-login", "登录失败，提示是${loginResponse.errorMessage}")
                loadState.value = LoadState.FAIL
            } else {

                RetrofitManager.setToken(loginResponse.data.temporaryToken)
                val appConfigMsg = async { loginService.getAppConfig().errorMessage }.await()
                LogUtil.d("LoginViewModel-msg", "配置异常,提示是：${appConfigMsg}")
                LogUtil.d("LoginViewModel-msg", "Token是：${loginResponse.data.temporaryToken}")

                loadState.value = LoadState.SUCCESS

            }

        },{
            loadState.value = LoadState.FAIL
        }

        )

    }




}


