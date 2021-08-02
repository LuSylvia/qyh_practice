package com.example.module_common.broadcast


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.example.module_common.ActivityManager

class ForceOfflineReceiver : BaseBroadcastReceiver() {
    //Exported表示是否允许该Receiver接受本程序以外的广播
    //Enabled表示是否启用该Receiver
    //要在APP未启动时监听系统广播的话，请去AndroidManifest.xml里申请权限
    //并在receiver里添加intent-filter,然后添加对应的action
    //请不要在此方法里添加任何的耗时操作
    override fun onReceive(context: Context, intent: Intent) {
        AlertDialog.Builder(context).apply {
            setTitle("warning")
            setMessage("You are forced to be offline.Please try to login again.")
            setCancelable(false)//对话框不可取消
            setPositiveButton("OK") { _, _ ->
                ActivityManager.getInstance().removeAllActivity()//销毁所有Activity
                //val intent=Intent(context, SplashActivity::class.java)
                //context.startActivity(intent)
            }
            show()
        }

    }


}

