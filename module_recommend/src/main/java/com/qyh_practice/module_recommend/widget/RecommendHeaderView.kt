package com.qyh_practice.module_recommend.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.qyh_practice.module_recommend.R

class RecommendHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    fun init(context: Context?) {
        val view: View =LayoutInflater.from(context).inflate(R.layout.item_recommend_header,this,false)
        //TODO:绑定3个textview控件，并设置跳转事件

        addView(view)
    }
}