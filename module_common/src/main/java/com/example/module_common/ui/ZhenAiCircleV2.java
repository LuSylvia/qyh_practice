package com.example.module_common.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.module_common.R;

public class ZhenAiCircleV2 extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    //动画总时间，包括雷达扫描、扩散圆
    private final long totalTime = 3600L;
    //小圆半径
    private final float smallCircleRadius;
    //小圆颜色
    private final int smallCircleColor;
    //扩散圆颜色
    private final int diffusionCircleColor;
    //雷达画笔的颜色
    private final int radarColor = getResources().getColor(R.color.purple_200);
    private SurfaceHolder surfaceHolder;
    //与surfaceHolder绑定的canvas
    private Canvas mCanvas;
    //绘制线程
    private Thread drawThread;
    //线程的控制开关
    //用volatile修饰，保证多线程安全
    private volatile boolean isRunning;
    //大圆半径
    private float bigCircleRadius;
    //扩散圆半径
    private float diffusionCircleRadius;
    //透明度
    private int mAlpha = 255;
    //圆心x坐标
    private float centerX = 0F;
    //圆心Y坐标
    private float centerY = 0F;
    //雷达旋转角度
    private float offsetAngles = 0F;
    //实现雷达扫描效果的渐变
    private SweepGradient sweepGradient;
    //扩散圆扩散起始时间
    private long startTime = 0L;
    //扩散圆扩散到当前位置时的系统时间
    private long currentTime = 0L;

    //3只画笔，均设置抗锯齿效果
    private Paint smallCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint diffusionCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint radarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public ZhenAiCircleV2(Context context) {
        this(context, null);
    }

    public ZhenAiCircleV2(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @SuppressLint("ResourceAsColor")
    public ZhenAiCircleV2(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
        //获得自定义属性并赋值
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.ZhenAiCircleV2);
        smallCircleRadius = ta.getDimension(R.styleable.ZhenAiCircleV2_smallCircleRadiusV2, 20F);
        smallCircleColor = ta.getColor(R.styleable.ZhenAiCircleV2_smallCircleColorV2, R.color.black);
        diffusionCircleColor = ta.getColor(R.styleable.ZhenAiCircleV2_diffusionCircleColorV2, R.color.purple_200);
        initPaint();
        ta.recycle();
    }

    void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        //设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常亮
        this.setKeepScreenOn(true);
    }

    void initPaint() {
        //画笔属性设置
        //设置颜色平滑过渡，让控件颜色看起来更柔和
        smallCirclePaint.setDither(true);
        smallCirclePaint.setColor(smallCircleColor);
        smallCirclePaint.setStyle(Paint.Style.FILL);

        diffusionCirclePaint.setDither(true);
        diffusionCirclePaint.setColor(diffusionCircleColor);
        diffusionCirclePaint.setStyle(Paint.Style.FILL);

        radarPaint.setDither(true);
        radarPaint.setColor(radarColor);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = (w * 1.0f / 2);
        centerY = (h * 1.0f / 2);
        //大圆的半径=min{控件宽度，控件高度}
        bigCircleRadius = Math.min(centerX, centerY);
        Log.d("Sylvia-measure-V2", "小圆半径=" + smallCircleRadius + ",大圆半径=" + bigCircleRadius);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
    }


    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (!isRunning || drawThread == null || drawThread.getState() == Thread.State.TERMINATED) {
            isRunning = true;
            drawThread = new Thread(this);
            drawThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        isRunning = false;
        drawThread = null;
        //holder.removeCallback(this);
    }


    @Override
    public void run() {
        while (isRunning) {
            draw();
        }
    }

    //画小圆
    private void drawSmallCircle(Canvas mCanvas) {
        mCanvas.drawCircle(centerX, centerY, smallCircleRadius, smallCirclePaint);
    }

    //画扩散圆
    private void drawDiffusionCircle(Canvas mCanvas, float progress) {
        //计算扩散圆半径
        diffusionCircleRadius = smallCircleRadius + (bigCircleRadius - smallCircleRadius) * progress;
        //计算扩散圆透明度
        mAlpha = (int) (255 - 255 * progress);
        //设置透明度
        diffusionCirclePaint.setAlpha(mAlpha);
        mCanvas.drawCircle(centerX, centerY, diffusionCircleRadius, diffusionCirclePaint);
    }

    //画雷达
    private void drawRadar(Canvas mCanvas, float progress) {
        if (sweepGradient == null) {
            //渐变初始化，用来实现雷达扫描效果
            sweepGradient = new SweepGradient(centerX, centerY, Color.TRANSPARENT, radarColor);
            radarPaint.setShader(sweepGradient);
        }
        //计算指针偏移角度
        offsetAngles = 360 * progress;
        //雷达扫描的实现
        mCanvas.save();
        mCanvas.rotate(offsetAngles, centerX, centerY);
        mCanvas.drawCircle(centerX, centerY, bigCircleRadius, radarPaint);
        mCanvas.restore();
    }

    //清除画布内容
    private void clearCanvas(Canvas mCanvas) {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    //计算进度值
    float countProgress() {
        if (currentTime - startTime > totalTime) {
            //超过一次动画最大时间，该初始化了
            currentTime = 0L;
            startTime = 0L;
        }
        if (startTime == 0L) {
            startTime = System.currentTimeMillis();
        }
        currentTime = System.currentTimeMillis();
        return (currentTime - startTime) * 1.0f / totalTime;
    }

    //核心方法
    private void draw() {
        try {
            mCanvas = surfaceHolder.lockCanvas();
            if (mCanvas != null) {
                clearCanvas(mCanvas);
                //白色背景
                mCanvas.drawColor(Color.WHITE);
                //计算进度值，统一使用
                float progress = countProgress();
                //开始绘图，需要绘制： 雷达扫描 扩散圆 小圆
                //顺序不能变！否则会出现覆盖现象，导致小圆被雷达扫描挡住
                drawRadar(mCanvas, progress);
                drawDiffusionCircle(mCanvas, progress);
                drawSmallCircle(mCanvas);

            } else {
                Log.d("Sylvia-draw-error", "nsnmn,canvas是空的！");
                //throw new NullPointerException("你的canvas是空的！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                surfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }


}
