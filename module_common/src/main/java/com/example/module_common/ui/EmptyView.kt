package com.example.module_common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.module_common.R

class EmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val iv_empty: ImageView
    private val tv_empty: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true)
        iv_empty = findViewById(R.id.iv_empty)
        tv_empty = findViewById(R.id.tv_empty)
    }
}