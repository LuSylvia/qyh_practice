package com.example.module_common.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.module_common.BaseApplication
import com.example.module_common.R

object NotificationUtil {
    private val manager: NotificationManager by lazy {
        BaseApplication.getContext()
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    /**
     * 负责创建详细的通知
     */
    fun createNotification(
        context: Context, channelID: String,
        contentTitle: String, contentText: String,
        smallIcon: Int, largeIcon: Int
    ): Notification {


        val notification = NotificationCompat.Builder(context, channelID)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)//点击该通知时，通知会自动取消
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(BitmapFactory.decodeResource(context.resources, R.drawable.test1))
            )
            .build()
        return notification
    }

    /**
     * TODO:负责创建通知(可扩展版)
     * @param context 上下文
     * @param contentText 通知内容
     * @param style 自定义风格
     */
    fun sendNotification(
        context: Context,
        contentText: String,
        style: Notification.Style?
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "qyh_practice",
                "Qyh_Practice",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "qyh_practice")
            .setContentTitle("仿趣约会")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.icon_common_net_error)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(BitmapFactory.decodeResource(context.resources, R.drawable.test1))
            )

            //.setStyle()
            .setAutoCancel(true)
            .build()
        manager.notify(10086, notification)


    }


    /**
     * 测试用函数
     */
    fun createSimpleNotification(context: Context, channelID: String): Notification {
        return createNotification(
            context, channelID, "This is content title", "This is content text",
            R.drawable.ic_launcher_foreground, 0
        );
    }


}