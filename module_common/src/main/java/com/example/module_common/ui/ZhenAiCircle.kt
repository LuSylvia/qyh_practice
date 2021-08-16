package com.example.module_common.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange

import com.example.module_common.R

//实现一个简单的雷达图
class ZhenAiCircle @JvmOverloads constructor(
    context: Context,
    private var attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attributeSet, defStyleAttr) {
    //大圆半径
    private var bigCircleRadius: Float = 0f

    //小圆半径
    private var smallCircleRadius: Float = 0f

    //小圆的颜色
    private var smallCircleColor: Int = 0

    //扩散圆半径
    private var diffusionCircleRadius: Float = 0f

    //扩散圆颜色
    private var diffusionCircleColor: Int = 0

    //雷达画笔的颜色
    private val radarColor: Int = resources.getColor(R.color.purple_200)

    //是否开启雷达扫描
    private var startScan: Boolean = true

    //是否开启扩散
    private var startDiffuse: Boolean = true

    //透明度
    private var alpha: Int = 255

    //圆心X坐标
    private var centerX = 0f

    //圆心Y坐标
    private var centerY = 0f

    //进度值
    private var progress = 0f

    //旋转角度
    private var offsetAngles = 0f

    //梯度渐变，用于实现雷达扫描效果
    private val radarShader: SweepGradient by lazy {
        SweepGradient(
            centerX,
            centerY,
            Color.TRANSPARENT,
            diffusionCircleColor
        )
    }

    //创建3只画笔，设置抗锯齿效果
    private val smallCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val diffuseCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val radarPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        initAttr()
        initPaint()
    }

    @SuppressLint("ResourceAsColor")
    private fun initAttr() {
        val typedArray = context.obtainStyledAttributes(attributeSet!!, R.styleable.ZhenAiCircle)

        smallCircleRadius = typedArray.getDimension(R.styleable.ZhenAiCircle_smallCircleRadius, 20f)

        smallCircleColor =
            typedArray.getColor(R.styleable.ZhenAiCircle_smallCircleColor, R.color.black)

        diffusionCircleColor =
            typedArray.getColor(R.styleable.ZhenAiCircle_diffusionCircleColor, R.color.purple_200)

        startScan = typedArray.getBoolean(R.styleable.ZhenAiCircle_startScan, true)

        startDiffuse = typedArray.getBoolean(R.styleable.ZhenAiCircle_startDiffuse, true)

        typedArray.recycle()

    }


    private fun initPaint() {
        //设置颜色平滑过渡，让控件颜色看起来更柔和
        smallCirclePaint.isDither = true
        smallCirclePaint.color = smallCircleColor
        smallCirclePaint.style = Paint.Style.FILL

        diffuseCirclePaint.isDither = true
        diffuseCirclePaint.color = diffusionCircleColor
        diffuseCirclePaint.style = Paint.Style.FILL

        radarPaint.isDither = true
        radarPaint.color = radarColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = (w / 2).toFloat()
        centerY = (h / 2).toFloat()
        //大圆的半径=min{控件宽度，控件高度}
        bigCircleRadius = centerX.coerceAtMost(centerY)
    }

    /**
     * @param start    开始值
     * @param end      结束值
     * @param progress 根据进度值计算相应需要的变化值
     * @return
     */
    private fun getValueByProgress(
        start: Float,
        end: Float,
        @FloatRange(from = 0.0, to = 1.0) progress: Float
    ): Float {
        return start + (end - start) * progress
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        diffuseCirclePaint.alpha = alpha
        //画扩散圆
        progress += 0.005f
        alpha = getValueByProgress(255f, 0f, progress).toInt()
        diffusionCircleRadius = getValueByProgress(smallCircleRadius, bigCircleRadius, progress)
        if (progress >= 1f) {
            //超出控件范围后，重新设置透明度和半径
            progress = 0f
        }
        canvas?.drawCircle(centerX, centerY, diffusionCircleRadius, diffuseCirclePaint)
        //实现雷达扫描
        radarPaint.shader = radarShader
        if (startScan) {
            canvas?.run {
                save()
                rotate(offsetAngles, centerX, centerY)
                if (offsetAngles < 360) {
                    offsetAngles++
                } else {
                    offsetAngles = 0f
                }
                drawCircle(centerX, centerY, bigCircleRadius, radarPaint)
                restore()
            }
        }
        //画小圆
        canvas?.drawCircle(centerX, centerY, smallCircleRadius, smallCirclePaint)
        if (startDiffuse) {
            postInvalidate()
        }
    }


}