package com.qyh_practice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /*
        * 实现跳转至主界面
        * */
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            delay(3000)
            jumpToMainPage()
        }

        //initData()

    }


    private fun jumpToMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }


}