package com.example.module_common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

//广播接收器基类
//定义广播接收器的通用行为
abstract class BaseBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {};
}