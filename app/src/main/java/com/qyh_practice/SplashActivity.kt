package com.qyh_practice

import android.content.Intent
import android.os.Bundle
import com.example.module_common.adapter.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)


        //TODO:实现自动登录功能


        /*
        * 实现跳转至主界面
        * */
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            delay(1000)
            jumpToMainPage()
        }

        //initData()

    }


    private fun jumpToMainPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }


}