package com.qyh_practice.module_recommend.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.qyh_practice.module_recommend.R

class RecommendHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

     init {
        val view: View =LayoutInflater.from(context).inflate(R.layout.item_recommend_header,this,false)
        //TODO:绑定3个textview控件，并设置跳转事件
        val tv_video=view.findViewById<TextView>(R.id.tv_video)
        val tv_zhuanshu=view.findViewById<TextView>(R.id.tv_zhuanshu)
        val tv_group_chat=view.findViewById<TextView>(R.id.tv_group_chat)

        tv_video.setOnClickListener { v->{

        } }

        tv_zhuanshu.setOnClickListener { v->{

        } }

        tv_group_chat.setOnClickListener { v->{

        } }


        addView(view)
    }
}