package com.qyh_practice.module_login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.module_common.constants.RouterManager
import com.example.module_common.eventbus.EventBusMessage
import com.example.module_common.retrofit.RetrofitManager
import com.example.module_common.utils.AccountManager
import com.qyh_practice.module_login.api.LoginService
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus

@Route(path = RouterManager.ACTIVITY_SPLASH)
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        jumpNextActivity()
    }

    /**
     * 跳转下一个界面
     * 根据系统是否有登录过，决定跳转MainActivity或者LoginActivity
     */
    fun jumpNextActivity() {
        //判断用户是否登录过
        val hasLogined: Boolean = AccountManager.getInstance().hasLogined()
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            if (hasLogined) {
                //调用获取系统配置信息的方法
                //成功后，将配置信息存储到本地
                val appConfigResponse = async {
                    RetrofitManager.getService(LoginService::class.java).getAppConfig()
                }.await()
                Log.d("Sylvia-Success", "自动登录成功！")

                //获取配置信息成功
                if (!appConfigResponse.isError && appConfigResponse.data != null) {
                    AccountManager.getInstance().setAppConfigEntity(appConfigResponse.data)
                    EventBus.getDefault().postSticky(EventBusMessage(appConfigResponse.data.workcity))
                    jumpMainActivity()
                    return@launch
                }
            }
            Log.d("Sylvia-FAIL", "自动登录失败，根本没本地数据！")
            delay(1000)
            jumpLoginActivity()
        }
    }




    private fun jumpLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        //ARouter.getInstance().build(RouterManager.ACTIVITY_LOGIN).navigation()
        this.finish()
    }




    private fun jumpMainActivity() {
        //EVENTBUS测试,传用户ID
        val eventBusMessage:EventBusMessage= EventBusMessage(AccountManager.getInstance().loadUserId())
        EventBus.getDefault().postSticky(eventBusMessage)
        Log.d("Syliva-test","测试消息是${eventBusMessage.getUserID()}")

        //ARouter的跳转速度实在是太慢了，如果直接finish会导致先返回手机桌面，再进入登录界面
        //目前逻辑是跳转成功后，直接finish掉SplashActivity
        ARouter.getInstance().build(RouterManager.ACTIVITY_MAIN).navigation()
        //this.finish()
    }
}