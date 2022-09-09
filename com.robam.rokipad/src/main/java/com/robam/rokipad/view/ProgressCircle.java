package com.robam.rokipad.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.robam.rokipad.R;


/**
 * Created by Dell on 2019/1/17.
 */

public class ProgressCircle extends View {
    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 画圆环的画笔背景色
    private Paint mRingPaintBg;
    // 画字体的画笔
    private Paint mTextPaint;
    // 画字体的画笔2
    private Paint mTextPaint2;

    // 圆形颜色
    private int mCircleColor;
    // 圆环颜色
    private int mRingColor;
    // 圆环背景颜色
    private int mRingBgColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 字的长度
    private float mTxtWidth;
    // 字的高度
    private float mTxtHeight;

    // 字的长度
    private float mTxtWidth2;
    // 字的高度
    private float mTxtHeight2;
    // 总进度
    private int mTotalProgress = 100;
    // 当前进度
    private int mProgress;


    public ProgressCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
    }

    //属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.progrssview, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.progrssview_radiusT, 70);
        mStrokeWidth = typeArray.getDimension(R.styleable.progrssview_strWidth, 10);
        mCircleColor = typeArray.getColor(R.styleable.progrssview_circleColor, Color.parseColor("#00000000"));
        mRingColor = typeArray.getColor(R.styleable.progrssview_ringColor, Color.parseColor("#3468d3"));
        mRingBgColor = typeArray.getColor(R.styleable.progrssview_ringBgColor, Color.parseColor("#2e3037"));
        mRingRadius = mRadius + mStrokeWidth / 2;
    }

    //初始化画笔
    private void initVariable() {
        //内圆
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        //外圆弧背景
        mRingPaintBg = new Paint();
        mRingPaintBg.setAntiAlias(true);
        mRingPaintBg.setColor(mRingBgColor);
        mRingPaintBg.setStyle(Paint.Style.STROKE);
        mRingPaintBg.setStrokeWidth(mStrokeWidth);


        //外圆弧
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        mRingPaint.setStrokeCap(Paint.Cap.ROUND);//设置线冒样式，有圆 有方

        //中间字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.parseColor("#ffffff"));
        mTextPaint.setTextSize(44);

        mTextPaint2 = new Paint();
        mTextPaint2.setAntiAlias(true);
        mTextPaint2.setStyle(Paint.Style.FILL);
        mTextPaint2.setColor(Color.parseColor("#666666"));
        mTextPaint2.setTextSize(18);


        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
        Paint.FontMetrics fm1 = mTextPaint2.getFontMetrics();
        mTxtHeight2 = (int) Math.ceil(fm1.descent - fm1.ascent);
    }

    //画图
    @Override
    protected void onDraw(Canvas canvas) {
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;


        //内圆
        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);

        //外圆弧背景
        RectF oval1 = new RectF();
        oval1.left = (mXCenter - mRingRadius);
        oval1.top = (mYCenter - mRingRadius);
        oval1.right = mRingRadius * 2 + (mXCenter - mRingRadius);
        oval1.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
        canvas.drawArc(oval1, 0, 360, false, mRingPaintBg); //圆弧所在的椭圆对象、圆弧的起始角度、圆弧的角度、是否显示半径连线

        //这块写死了
        String txt1 = getContext().getString(R.string.current_tempute);
        mTxtWidth2 = mTextPaint2.measureText(txt1, 0, txt1.length());
        canvas.drawText(txt1, mXCenter - mTxtWidth2 / 2, mYCenter + (mTxtHeight2 + mTxtHeight) / 4 + 5, mTextPaint2);

        //外圆弧
        if (mProgress > 0) {
            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter - mRingRadius);
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            canvas.drawArc(oval, -90, ((float) mProgress / mTotalProgress) * 360, false, mRingPaint); //
            //字体
            String txt = mProgress + "";
            mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
            canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4 - 10, mTextPaint);
        } else {
            String t = "-";
            mTxtWidth = mTextPaint.measureText(t, 0, t.length());
            canvas.drawText(t, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4 - 10, mTextPaint);
        }
    }

    //设置进度
    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();//重绘
    }

    //设置总进度
    public void setTotalProgress(int totalProgress) {
        mTotalProgress = totalProgress;
    }


}
