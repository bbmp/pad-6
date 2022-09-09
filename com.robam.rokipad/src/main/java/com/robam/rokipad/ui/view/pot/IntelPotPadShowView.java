package com.robam.rokipad.ui.view.pot;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.legent.VoidCallback3;
import com.legent.plat.Plat;
import com.legent.utils.LogUtils;
import com.robam.rokipad.R;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by as on 2017-03-30.
 */

public class IntelPotPadShowView extends ViewGroup {

    interface PotColors {
        int Color_Default = 0xff303641;
        int Color_Yellow = 0XFFF9E75C;
        int Color_Light1 = 0XFF4B83F5;
        int Color_Light2 = 0XFFFF5B1A;
        int Color_Light3 = 0XFFD52345;
    }

    public enum IntelPotShow_Model {
        StandBy, ShowTemp
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = (short) w;
        mHeight = (short) h;
        initDrawOrTool();
    }

    short mWidth;
    short mHeight;
    Context mContext;
    Resources resources;
    float[] mTemPoints = new float[]{50f, 240f, 250f, 350f};
    //float[] mTemPoints = new float[]{400f, 400f, 400f, 400f};
    float gap_deg = 3;
    float mTemp_Value;

    public IntelPotPadShowView(Context context) {
        super(context);
        init(context);
    }

    public IntelPotPadShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IntelPotPadShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    Timer timer = new Timer();
    Paint mPaint;
    RectF mOutRectF;
    IntelPotShow_Model mModel = IntelPotShow_Model.StandBy;
    float mSweepAngle;

    public void init(Context context) {
        mContext = context;
        resources = context.getResources();
        setWillNotDraw(false);
        initPaint();
    }

    void initPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.reset();
        mPaint.setAntiAlias(true);
    }

    void initPaint2() {
        if (mPaint2 == null) {
            mPaint2 = new Paint();
        }
        mPaint2.reset();
        mPaint2.setAntiAlias(true);
    }

    void initDrawOrTool() {
        mOutRectF = new RectF(-mWidth / 2 + 3, -mHeight / 2 + 3, mWidth / 2 - 3, mHeight / 2 - 3);
    }

    private float mTemp_Value_cal = 0;
    int callback_status = -1;//当前温度范围 1表示第一段 2表示第二段 3表示第三段

    public synchronized void setModel(IntelPotShow_Model model, final VoidCallback3 callback, String... arg) {

        this.mModel = model;
        if (mModel == IntelPotShow_Model.StandBy) {
            if (timer != null) {
                initTimer(false);
            }
            callback_status = -1;
            postInvalidate();
        } else if (mModel == IntelPotShow_Model.ShowTemp) {
            if (arg == null || arg.length == 0) {
                return;
            }
            final float temp_value = Float.parseFloat(arg[0]);
            float temp = mTemp_Value;
            LogUtils.e("20200703", "temp_value:" + temp_value);
            if ((int) temp != (int) temp_value) {

                onTextDrawSt((int) temp_value, callback);
            }
            float offset = mTemp_Value - temp_value;
            mTemp_Value = temp_value;
            if (offset > 0) {
                if (timer == null)
                    initTimer(true);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.i("147", mTemp_Value_cal + " ---  " + mTemp_Value_cal);
                        float offset = mTemp_Value_cal - temp_value;
                        mTemp_Value_cal = mTemp_Value_cal - offset / 20;
                        if (mTemp_Value_cal >= temp_value) {
                            drawSlideCalculation(mTemp_Value_cal, callback);
                        } else {
                            initTimer(false);
                        }
                    }
                }, 0, 30);
                return;
            } else if (offset < 0) {
                if (timer == null)
                    initTimer(true);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.i("147", mTemp_Value_cal + "  " + mTemp_Value_cal);
                        float offset = temp_value - mTemp_Value_cal;
                        mTemp_Value_cal = mTemp_Value_cal + offset / 20;
                        if (mTemp_Value_cal <= temp_value) {
                            drawSlideCalculation(mTemp_Value_cal, callback);
                        } else
                            initTimer(false);
                    }
                }, 0, 30);
                return;
            }
            drawSlideCalculation(mTemp_Value, callback);
        }
    }


    private void drawSlideCalculation(float temp_value, VoidCallback3 callback3) {
        if (temp_value <= mTemPoints[0]) {
            mSweepAngle = .0f;
            postInvalidate();
            if (callback_status != 1) {
                callback_status = 1;
                if (callback3 != null)
                    callback3.onCompleted(1);
            }
            return;
        } else if (temp_value > mTemPoints[0] && temp_value <= mTemPoints[1]) {
            float float1 = (temp_value - mTemPoints[0]) / (mTemPoints[1] - mTemPoints[0]);
            mSweepAngle = float1 * (180f + 5f);
            postInvalidate();
            if (callback_status != 1) {
                callback_status = 1;
                if (callback3 != null)
                    callback3.onCompleted(1);
            }
            return;
        } else if (temp_value > mTemPoints[1] && temp_value <= mTemPoints[2]) {
            float float1 = (temp_value - mTemPoints[1]) / (mTemPoints[2] - mTemPoints[1]);
            mSweepAngle = float1 * 50f + gap_deg + (180f + 5f);
            postInvalidate();
            if (callback_status != 2) {
                callback_status = 2;
                if (callback3 != null)
                    callback3.onCompleted(2);
            }
            return;
        } else if (temp_value > mTemPoints[2] && temp_value <= mTemPoints[3]) {
            float float1 = (temp_value - mTemPoints[2]) / (mTemPoints[3] - mTemPoints[2]);
            float fl = 50f + gap_deg * 2 + (180f + 5f);
            mSweepAngle = (300 - fl) * float1 + fl;
            postInvalidate();
            if (callback_status != 3) {
                callback_status = 3;
                if (callback3 != null)
                    callback3.onCompleted(3);
            }
            return;
        } else if (temp_value > mTemPoints[3]) {
            mSweepAngle = 300;
            postInvalidate();
            if (callback_status != 3) {
                callback_status = 3;
                if (callback3 != null)
                    callback3.onCompleted(3);
            }
        }
    }

    private void onTextDrawSt(int temp, VoidCallback3 callback3) {
        if (temp < 85 && temp >= 50) {
            callback3.onCompleted(-101);
        } else if (temp < 120 && temp >= 85) {
            callback3.onCompleted(-102);
        } else if (temp < 180 && temp >= 120) {
            callback3.onCompleted(-103);
        } else if (temp < 220 && temp >= 180) {
            callback3.onCompleted(-104);
        } else if (temp < 250 && temp >= 220) {
            callback3.onCompleted(-105);
        } else if (temp >= 250) {
            callback3.onCompleted(-106);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.rotate(-240);
        switch (mModel) {
            case StandBy:
                drawOutSideCircle(canvas, 6);
                drawInSideCircle(canvas);
                drawInner(canvas);
                break;
            case ShowTemp:
                drawOutSideCircle(canvas, 6);
                drawInSideCircle(canvas);
                drawInner(canvas);
                break;
            default:
                break;
        }
        //endInit(canvas);
    }

    void drawOutSideCircle(Canvas canvas, int width) {
        initPaint();
        mPaint.setColor(PotColors.Color_Default);
        if (mModel == IntelPotShow_Model.ShowTemp) {
            mPaint.setColor(PotColors.Color_Yellow);
        }
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(width);
        float ra1 = mWidth / 2 - width / 2;
        float ra2 = mHeight / 2 - width / 2;
        mOutRectF = new RectF(-ra1, -ra2, ra1, ra2);

        Path path = new Path();
        path.addArc(mOutRectF, 5, 180);
        if (mModel == IntelPotShow_Model.ShowTemp && mSweepAngle > 0 && mSweepAngle <= 185f)
            mPaint.setColor(PotColors.Color_Light1);
        else
            mPaint.setColor(PotColors.Color_Default);
        canvas.drawPath(path, mPaint);//绘制外圆 第一部分
        float radius = mWidth / 2 - width / 2;
        float pointx = (float) Math.cos(5 * Math.PI / 180) * radius;
        float pointy = (float) Math.sin(5 * Math.PI / 180) * radius;
        canvas.translate(pointx, pointy + 2);
        RectF rectF = new RectF(-width / 2, -4, width / 2, 2);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        if (mModel == IntelPotShow_Model.ShowTemp && mSweepAngle > 0 && mSweepAngle <= 185f)
            mPaint.setColor(PotColors.Color_Light1);
        else
            mPaint.setColor(PotColors.Color_Default);
        canvas.drawArc(rectF, -180, 180, true, mPaint);//绘制外圆第一部分第1角
        canvas.translate(-pointx, -pointy - 2);

        pointx = (float) Math.cos(185 * Math.PI / 180) * radius;
        pointy = (float) Math.sin(185 * Math.PI / 180) * radius;
        canvas.translate(pointx, pointy + 2);
        rectF = new RectF(-width / 2, -4, width / 2, 2);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        if (mModel == IntelPotShow_Model.ShowTemp && mSweepAngle > 0 && mSweepAngle <= 185f)
            mPaint.setColor(PotColors.Color_Light1);
        else
            mPaint.setColor(PotColors.Color_Default);
        canvas.drawArc(rectF, -180, 180, true, mPaint);//绘制外圆第一部分第2角
        canvas.translate(-pointx, -pointy - 2);
        path.reset();
        initPaint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(width);
        mPaint.setColor(PotColors.Color_Yellow);
        path.addArc(mOutRectF, 188, 50);
        if (mModel == IntelPotShow_Model.ShowTemp && mSweepAngle > (gap_deg + (180f + 5f)) && mSweepAngle <= (50f + gap_deg + (180f + 5f)))
            mPaint.setColor(PotColors.Color_Light2);
        else
            mPaint.setColor(PotColors.Color_Default);
        canvas.drawPath(path, mPaint);//绘制外圆第二部分
        float radius2 = mWidth / 2 - width / 2;
        float pointx2 = (float) Math.cos(188 * Math.PI / 180) * radius2;
        float pointy2 = (float) Math.sin(188 * Math.PI / 180) * radius2;
        canvas.translate(pointx2, pointy2);
        canvas.rotate(gap_deg + 7);
        RectF rectF2 = new RectF(-width / 2, -4, width / 2, 3);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF2, -180, -180, true, mPaint);//绘制外圆第二部分 第1角
        canvas.rotate(-gap_deg - 7);
        canvas.translate(-pointx2, -pointy2);


        pointx2 = (float) Math.cos(235 * Math.PI / 180) * radius2;
        pointy2 = (float) Math.sin(235 * Math.PI / 180) * radius2;
        canvas.translate(pointx2, pointy2);
        canvas.rotate(gap_deg + 55);
        rectF2 = new RectF(-width / 2, -9, width / 2, -5);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF2, -180, 180, true, mPaint);//绘制外圆第二部分 第2角
        canvas.rotate(-gap_deg - 55);
        canvas.translate(-pointx2, -pointy2);

        path.reset();
        initPaint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(width);
        mPaint.setColor(PotColors.Color_Yellow);
        path.addArc(mOutRectF, 241, 295 - (50f + gap_deg * 2 + 180f + 5f));
        float f = 50f + gap_deg * 2 + (180f + 5f);
        if (mModel == IntelPotShow_Model.ShowTemp && mSweepAngle > f && mSweepAngle <= 300)
            mPaint.setColor(PotColors.Color_Light3);
        else
            mPaint.setColor(PotColors.Color_Default);
        canvas.drawPath(path, mPaint);//绘制外圆第三部分
        float radius1 = mWidth / 2 - width / 2;
        float pointx1 = (float) Math.cos(235 * Math.PI / 180) * radius1;
        float pointy1 = (float) Math.sin(235 * Math.PI / 180) * radius1;
        canvas.translate(pointx1, pointy1);
        canvas.rotate(gap_deg + 55);
        RectF rectF1 = new RectF(-width / 2, -22, width / 2, -16);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF1, -180, -180, true, mPaint);//绘制外圆第三部分 第1角
        canvas.rotate(-gap_deg - 55);
        canvas.translate(-pointx1, -pointy1);


        pointx1 = (float) Math.cos(295 * Math.PI / 180) * radius1;
        pointy1 = (float) Math.sin(295 * Math.PI / 180) * radius1;
        canvas.translate(pointx1, pointy1);
        canvas.rotate(20);
        rectF1 = new RectF(-width / 2 - 4, -3, width / 2, 3);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);

        //canvas.drawLine(0, 0, 0, 50, mPaint);
        //.drawLine(0, 0, 50, 0, mPaint);
        canvas.drawArc(rectF1, -270, -180, true, mPaint);//绘制外圆第三部分 第2角
        canvas.rotate(-20);
        canvas.translate(-pointx1, -pointy1);

        mPaint.reset();
        initPaint();
        mPaint.setColor(PotColors.Color_Default);
        mPaint.setTextSize(18);
        mPaint.setStyle(Paint.Style.FILL);
        path.reset();
        path.addArc(mOutRectF, 5, 290);
        canvas.drawTextOnPath(resources.getString(R.string.healthy_cooking), path, 140, 25, mPaint);
        canvas.drawTextOnPath(resources.getString(R.string.over_heated), path, 410, 25, mPaint);
        canvas.drawTextOnPath(resources.getString(R.string.dry_cleaning), path, 510, 25, mPaint);

    }

    Paint mPaint2 = new Paint();
    Path path2 = new Path();
    Path path = new Path();
    Path path1 = new Path();

    void drawInSideCircle(Canvas canvas) {
        initPaint();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(PotColors.Color_Default);
        mPaint.setStrokeWidth(18);
        mOutRectF = new RectF(-80, -80, 80, 80);
        path.reset();
        path.addArc(mOutRectF, 0, 300);
        canvas.drawPath(path, mPaint);//绘制内圆
        path.reset();
        if (mModel == IntelPotShow_Model.ShowTemp) {
//            int[] colors = new int[]{Color.WHITE, Color.YELLOW, Color.RED};
//            int ran_color = getCurrentColor(mSweepAngle / 300, colors);
            Paint paint = new Paint();
            if (mTemp_Value < mTemPoints[1]) {
                mPaint.setColor(PotColors.Color_Light1);
                paint.setColor(PotColors.Color_Light1);
                mPaint.setShadowLayer(5, 0, 0, PotColors.Color_Light1);
            } else if (mTemp_Value > mTemPoints[1] && mTemp_Value < mTemPoints[2]) {
                mPaint.setColor(PotColors.Color_Light2);
                paint.setColor(PotColors.Color_Light2);
                mPaint.setShadowLayer(5, 0, 0, PotColors.Color_Light2);
            } else {
                mPaint.setColor(PotColors.Color_Light3);
                paint.setColor(PotColors.Color_Light3);
                mPaint.setShadowLayer(5, 0, 0, PotColors.Color_Light3);
            }

            Path path3 = new Path();
            path3.addArc(mOutRectF, 0, mSweepAngle);
//            mPaint.setShader(new SweepGradient(0, 0, colors, null));
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);
            canvas.drawPath(path3, mPaint);//绘制内圆弧度颜色变化范围
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(5);
            mPaint.setShader(null);
//            mPaint.setColor(PotColors.Color_Default);
            canvas.drawPoint((float) (Math.cos(mSweepAngle * Math.PI / 180) * (80 + 14)), (float) (Math.sin(mSweepAngle * Math.PI / 180) * (80 + 14)), mPaint);


            paint.setAntiAlias(true);
            canvas.translate(80, 3);
            path2.reset();
            RectF rect = new RectF(-9f, -6f, 9f, 3f);
            path2.arcTo(rect, 182, 184, false);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.FILL);
//
            canvas.drawPath(path2, paint);//绘制内圆第1角
            canvas.translate(-80, -3);

            float pointx = (float) Math.cos(300 * Math.PI / 180) * 80;
            float pointy = (float) Math.sin(300 * Math.PI / 180) * 80;
            canvas.translate(pointx, pointy);
            canvas.rotate(120);
            path2.reset();
            RectF rect2 = new RectF(-9f, -4f, 9f, 8f);

            path2.arcTo(rect2, 182, 184, false);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.FILL);
            if (mSweepAngle >= 299) {
                paint.setColor(PotColors.Color_Light1);
            } else
                paint.setColor(PotColors.Color_Default);
            canvas.drawPath(path2, paint);//绘制内圆第2角
            canvas.rotate(-120);
            canvas.translate(-pointx, -pointy);
            initPaint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(2);
            float x = (float) (Math.cos(mSweepAngle * Math.PI / 180) * (80 + 9));
            float y = (float) (Math.sin(mSweepAngle * Math.PI / 180) * (80 + 9));
            if (mTemp_Value < mTemPoints[1]) {
                mPaint.setColor(PotColors.Color_Light1);
                mPaint.setShadowLayer(4, 0, 0, PotColors.Color_Light1);
                mPaint.setShader(new LinearGradient(0, 0, x, y, 0x00000000, PotColors.Color_Light1, Shader.TileMode.MIRROR));
            } else if (mTemp_Value > mTemPoints[1] && mTemp_Value < mTemPoints[2]) {
                mPaint.setColor(PotColors.Color_Light2);
                mPaint.setShadowLayer(4, 0, 0, PotColors.Color_Light2);
                mPaint.setShader(new LinearGradient(0, 0, x, y, 0x00000000, PotColors.Color_Light2, Shader.TileMode.MIRROR));
            } else {
                mPaint.setColor(PotColors.Color_Light3);
                mPaint.setShadowLayer(4, 0, 0, PotColors.Color_Light3);
                mPaint.setShader(new LinearGradient(0, 0, x, y, 0x00000000, PotColors.Color_Light3, Shader.TileMode.MIRROR));
            }
            canvas.drawLine(0, 0, x, y, mPaint);//绘制指针
        } else {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.translate(80, 3);
            path2.reset();
            RectF rect = new RectF(-9f, -6f, 9f, 3f);
            path2.arcTo(rect, 182, 184, false);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(PotColors.Color_Default);
            canvas.drawPath(path2, paint);//绘制内圆第1角
            canvas.translate(-80, -3);


            float pointx = (float) Math.cos(300 * Math.PI / 180) * 80;
            float pointy = (float) Math.sin(300 * Math.PI / 180) * 80;
            canvas.translate(pointx, pointy);
            canvas.rotate(120);
            path2.reset();
            RectF rect2 = new RectF(-9f, -4f, 9f, 8f);

            path2.arcTo(rect2, 182, 184, false);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(PotColors.Color_Default);
            canvas.drawPath(path2, paint);//绘制内圆第2角
            canvas.rotate(-120);
            canvas.translate(-pointx, -pointy);
        }
    }

    DecimalFormat decimalFormat = new DecimalFormat(".00");

    void drawInner(Canvas canvas) {
        initPaint();
        canvas.rotate(240);
        if (mModel == IntelPotShow_Model.ShowTemp) {
            mPaint.setStrokeWidth(5);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(46);
            mPaint.setColor(Color.WHITE);
            mPaint.setTextAlign(Paint.Align.CENTER);
            int tempva = (int) mTemp_Value;
            canvas.drawText(String.valueOf(tempva), 0, 10, mPaint);
        } else {
            mPaint.reset();
            mPaint.setStrokeWidth(3);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.WHITE);
            canvas.drawLine(-20, 0, 20, 0, mPaint);
        }
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(24);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("℃", 30, -15, mPaint);
        canvas.rotate(-240);
    }

    private void endInit(Canvas canvas) {
        initPaint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(PotColors.Color_Default);
        canvas.rotate(240);
        mPaint.setStrokeWidth(1);
        canvas.drawRect(-mWidth / 2, -mHeight / 2, mWidth / 2, mHeight / 2, mPaint);
    }

    public boolean isMainThread() throws Exception {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    public void initTimer(Boolean init) {
        if (init) {
            if (timer == null)
                timer = new Timer();
        } else {
            if (timer != null) {
                timer.cancel();
                timer.purge();
            }
            timer = null;
        }
    }

    public static int getCurrentColor(float percent, int[] colors) {
        float[][] f = new float[colors.length][3];
        for (int i = 0; i < colors.length; i++) {
            f[i][0] = (colors[i] & 0xff0000) >> 16;
            f[i][1] = (colors[i] & 0x00ff00) >> 8;
            f[i][2] = (colors[i] & 0x0000ff);
        }
        float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < f.length; j++) {
                if (f.length == 1 || percent == j / (f.length - 1f)) {
                    result = f[j];
                } else {
                    if (percent > j / (f.length - 1f) && percent < (j + 1f) / (f.length - 1)) {
                        result[i] = f[j][i] - (f[j][i] - f[j + 1][i]) * (percent - j / (f.length - 1f)) * (f.length - 1f);
                    }
                }
            }
        }
        return Color.rgb((int) result[0], (int) result[1], (int) result[2]);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        initTimer(false);
    }
}
