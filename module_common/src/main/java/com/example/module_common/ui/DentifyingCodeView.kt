package com.example.module_common.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.example.module_common.R
import java.util.*

class DentifyingCodeView @JvmOverloads constructor(
    context: Context,
    private var attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {
    //验证码位数
    var mFigures = 0

    //验证码之间的间距
    private var mCodeMargin = 0

    //选中框的颜色
    private var mSelectColor = 0

    //普通框的颜色
    private var mNormalColor = 0

    //边框直角的曲度
    private var mBorderRadius = 0f

    //边框的厚度
    private var mBorderWidth = 0f

    //光标宽度
    private var mCursorWidth = 0f

    //光标颜色
    private var mCursorColor = 0

    //光标闪烁的时间
    private var mCursorDuration = 0L

    var onVerifyCodeChangedListener: OnVerifyCodeChangedListener? = null
    var mCurrentPosition = 0// 当前验证码的位置
    var mEachRectLength = 0// 矩形边长
    val mNormalPaint = Paint()
    val mSelectPaint = Paint()
    val mCursorPaint = Paint()


    // 控制光标闪烁
    var isCursorShowing = false
    var mCursorTimerTask: TimerTask? = null
    var mCursorTimer: Timer? = null

    init {
        initAttr()
        initPaint()
        initCursorTimer()
        isFocusableInTouchMode = true
        initTextChangedListener()
    }

    private fun initAttr() {
        val typedArray = context.obtainStyledAttributes(attrs!!, R.styleable.DentifyingCodeView)
        mFigures = typedArray.getInteger(R.styleable.DentifyingCodeView_figures, 4)
        mCodeMargin = typedArray.getDimension(R.styleable.DentifyingCodeView_codeMargin, 0f).toInt()
        mSelectColor =
            typedArray.getColor(R.styleable.DentifyingCodeView_selectBorderColor, currentTextColor)
        mNormalColor =
            typedArray.getColor(
                R.styleable.DentifyingCodeView_normalBorderColor,
                resources.getColor(android.R.color.darker_gray)
            )
        mBorderRadius = typedArray.getDimension(R.styleable.DentifyingCodeView_borderRadius, 6f)
        mBorderWidth = typedArray.getDimension(R.styleable.DentifyingCodeView_borderWidth, 1f)
        mCursorWidth = typedArray.getDimension(R.styleable.DentifyingCodeView_cursorWidth, 1f)
        mCursorColor =
            typedArray.getColor(
                R.styleable.DentifyingCodeView_cursorColor,
                resources.getColor(android.R.color.darker_gray)
            )
        mCursorDuration =
            typedArray.getInteger(
                R.styleable.DentifyingCodeView_cursorDuration,
                DEFAULT_CURSOR_DURATION
            )
                .toLong()

        typedArray.recycle()

        // force LTR because of bug: https://github.com/JustKiddingBaby/VercodeEditText/issues/4
        layoutDirection = LAYOUT_DIRECTION_LTR
    }

    private fun initPaint() {
        mNormalPaint.isAntiAlias = true
        mNormalPaint.color = mNormalColor
        mNormalPaint.style = Paint.Style.STROKE// 空心
        mNormalPaint.strokeWidth = mBorderWidth

        mSelectPaint.isAntiAlias = true
        mSelectPaint.color = mSelectColor
        mSelectPaint.style = Paint.Style.STROKE// 空心
        mSelectPaint.strokeWidth = mBorderWidth

        mCursorPaint.isAntiAlias = true
        mCursorPaint.color = mCursorColor
        mCursorPaint.style = Paint.Style.FILL_AND_STROKE
        mCursorPaint.strokeWidth = mCursorWidth
    }

    private fun initCursorTimer() {
        mCursorTimerTask = object : TimerTask() {
            override fun run() {
                // 通过光标间歇性显示实现闪烁效果
                isCursorShowing = !isCursorShowing
                postInvalidate()
            }
        }
        mCursorTimer = Timer()
    }

    private fun initTextChangedListener() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mCurrentPosition = text!!.length
                postInvalidate()
                if (text!!.length == mFigures) {
                    onVerifyCodeChangedListener?.onInputCompleted(text!!)
                } else if (text!!.length > mFigures) {
                    text!!.delete(mFigures, text!!.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                mCurrentPosition = text!!.length
                postInvalidate()
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mCurrentPosition = text!!.length
                postInvalidate()
                onVerifyCodeChangedListener?.onVerCodeChanged(text!!, start, before, count)
            }

        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthResult: Int
        var heightResult = 0

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        //适配match_parent
        widthResult = if (widthMode == MeasureSpec.EXACTLY) {
            widthSize
        } else {
            getScreenWidth(context)
        }

        //计算每个矩形输入框的宽度
        mEachRectLength = (widthResult - (mFigures - 1) * mCodeMargin) / mFigures


        heightResult = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            mEachRectLength
        }
        setMeasuredDimension(widthResult, heightResult)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        mCurrentPosition = text!!.length
        val width = mEachRectLength - paddingLeft - paddingRight
        val height = measuredHeight - paddingTop - paddingBottom
        for (i in 0 until mFigures) {
            canvas.save()
            val start = width * i + mCodeMargin * i + mBorderWidth
            var end = start + width - mBorderWidth
            if (i == mFigures - 1) {
                end -= mBorderWidth
            }
            // 画矩形选框
            val rect = RectF(start, mBorderWidth * 4, end, height.toFloat() - mBorderWidth)
            if (i == mCurrentPosition) {//选中的下一个状态
                canvas.drawRoundRect(rect, mBorderRadius, mBorderRadius, mSelectPaint)
            } else {
                canvas.drawRoundRect(rect, mBorderRadius, mBorderRadius, mNormalPaint)
            }
            canvas.restore()
        }

        // 绘制文字
        val value = text.toString()
        for (i in 0 until value.length) {
            canvas.save()
            val start = width * i + mCodeMargin * i
            val x = start + width / 2f// x
            paint.textAlign = Paint.Align.CENTER
            paint.color = currentTextColor
            val fontMetrics = paint.fontMetrics
            val baseline = (height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top
            canvas.drawText(value[i].toString(), x, baseline, paint)
            canvas.restore()
        }

        // 绘制光标
        if (!isCursorShowing && isCursorVisible && mCurrentPosition < mFigures && hasFocus()) {
            canvas.save()
            val startX = (width + mCodeMargin) * mCurrentPosition + width / 2f
            val startY = height / 4f
            val endX = startX
            val endY = height - height / 4f
            canvas.drawLine(startX, startY, endX, endY, mCursorPaint)
            canvas.restore()
        }

    }


    private fun getScreenWidth(context: Context?): Int {
        val metrics = DisplayMetrics()
        val windowManager = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            if (action == MotionEvent.ACTION_DOWN) {
                requestFocus()
                setSelection(text!!.length)
                showKeyBoard(context)
                return false
            }
        }

        return super.onTouchEvent(event)
    }

    private fun showKeyBoard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 启动定时任务，定时刷新实现光标闪烁
        mCursorTimer?.scheduleAtFixedRate(mCursorTimerTask, 0, mCursorDuration)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mCursorTimer?.cancel()
        mCursorTimer = null
    }

    companion object {
        const val DEFAULT_CURSOR_DURATION = 400//光标闪烁的默认间隔时间
    }

    /**
     * 验证码变化时候的监听事件
     */
    interface OnVerifyCodeChangedListener {
        /**
         * 当验证码变化的时候
         */
        fun onVerCodeChanged(s: CharSequence, start: Int, before: Int, count: Int)

        /**
         * 输入完毕后的回调
         */
        fun onInputCompleted(s: CharSequence)
    }


}