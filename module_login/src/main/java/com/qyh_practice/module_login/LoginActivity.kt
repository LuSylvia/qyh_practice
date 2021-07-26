package com.qyh_practice.module_login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.module_common.entity.LoadState
import com.qyh_practice.module_login.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        viewModel.loadState.observe(this, {
            when (it) {
                LoadState.FAIL -> {
                    //登录失败，打印错误信息
                    Toast.makeText(this, "网络异常！", Toast.LENGTH_SHORT).show()
                }
                LoadState.SUCCESS -> {
                    //TODO:登录成功，跳转到推荐页面

                }
                else -> {
                    //啥都不做
                }

            }
        })


    }
}