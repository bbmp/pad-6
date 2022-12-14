package com.legent.utils.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.legent.Callback2;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

public class ViewUtils {
    static public class Size {
        public int width, height;
    }

    private ViewUtils() {
        throw new AssertionError();
    }

    static int anInt = 0;

    /**
     * 设置view显示
     */
    public static void changeVisibilty(View view, int isVisibilty) {
        if (view.getVisibility() != isVisibilty)
            view.setVisibility(isVisibilty);
    }

    /**
     * 设置文本
     */
    public static void changeText(Context cx, TextView view, int str) {
        if (view != null && str != 0 && (view.getText() == null ||
                !view.getText().equals(cx.getResources().getString(str)))) {
            view.setText(str);
        }
    }

    public static void changeText(TextView view, String str) {
        if (view != null && (view.getText() == null ||
                !view.getText().equals(str))) {
            view.setText(str == null ? "" : str);
        }
    }

    public static void changeTextColor(Context cx, TextView view, int color) {
        int color1 = cx.getResources().getColor(color);
        changeTextColor(view, color1);
    }

    public static void changeTextColor(TextView view, int color) {
        if (view != null && color != 0) {
            if (color != view.getCurrentTextColor()) {
                view.setTextColor(color);
            }
        }
    }

    /**
     * 改变imgsrc
     */
    public static void changeImgsrc(ImageView view, int imgId) {
        if (view != null && imgId != 0) {
            if (view.getTag() == null || (view.getTag() instanceof Integer &&
                    (Integer) view.getTag() != imgId)) {
                view.setImageResource(imgId);
                view.setTag(imgId);
            }
        }

    }

    /**
     * 改变背景src
     */
    public static void changeBackSrc(Context cx, View view, int backID) {
        if (view != null && backID != 0) {
            Drawable drawable = view.getBackground();
            if (drawable != null && drawable.getCurrent().getConstantState() !=
                    cx.getResources().getDrawable(backID).getConstantState())
                view.setBackgroundResource(backID);
        }
    }

    public static void changeBackMap(Context cx, View view, int backID) {
        if (cx != null && view != null && backID != 0) {
            if (view.getTag() == null || (view.getTag() instanceof Integer &&
                    (Integer) view.getTag() != backID)) {
                view.setBackgroundResource(backID);
                view.setTag(backID);
            }
        }
    }

    public static void changeBackColor(Context cx, View view, int color) {
        if (cx != null) {
            changeBackColor(view, cx.getResources().getColor(color));
        }
    }

    public static void changeBackColor(View view, int color) {
        if (view != null && color != 0) {
            if (view.getTag() == null || (view.getTag() instanceof Integer &&
                    (Integer) view.getTag() != color)) {
                view.setBackgroundColor(color);
                view.setTag(color);
            }
        }
    }


    /**
     * @return 获取view 截图 速度较慢 图片质量高
     */
    public static byte[] getViewCapture(Context ctx, View view) {
        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();
        byte[] bytes = getBytesFromBitmap(bitmap, Bitmap.CompressFormat.PNG, 100);
        view.setDrawingCacheEnabled(false);
        return bytes;
    }

    /**
     * @param v 截图 速度较快 图片质量稍差
     * @return
     */
    public static byte[] getViewCapture(View v, int width, int height) {
        int w = width;
        int h = height;
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
        /** 如果不设置canvas画布为白色，则生成透明 */
        c.drawColor(Color.WHITE);
        v.layout(0, 0, w, h);
        v.draw(c);
        return getBytesFromBitmap(bmp, Bitmap.CompressFormat.PNG, 100);
    }

    /**
     * 输入位图 输出对应字节流
     * @return
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if ((quality < 0 || quality > 100) && format != Bitmap.CompressFormat.PNG)
            quality = 50;
        bitmap.compress(format, quality, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * view 绘制完毕后 获取长宽 @rent
     */
    static public void getViewSize(final View view, final Callback2<Size> callback) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                Size size = new Size();
                size.width = view.getWidth();
                size.height = view.getHeight();

                if (callback != null) {
                    callback.onCompleted(size);
                }
            }
        });
    }

    /**
     * 设置透明view
     */
    static public void setTransparentBackground(View view) {

        if (view != null) {
            int color = view.getContext().getResources()
                    .getColor(android.R.color.transparent);
            view.setBackgroundColor(color);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    // -----------------------------------------------Dialog----------------------------------------------

    /**
     * @param dialog
     * @param closable true - 可以关闭 false - 不能关闭
     */
    public static void setDialogShowField(DialogInterface dialog,
                                          boolean closable) {
        try {
            Field field = dialog.getClass().getSuperclass()
                    .getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, closable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFullScreen(Context cx, Dialog dialog) {
        setFullScreen(cx, dialog, Gravity.FILL);
    }

    public static void setFullScreen(Context cx, Dialog dialog, int gravity) {

        Activity atv = (Activity) cx;
        Window window = dialog.getWindow();

        // 此处可以设置dialog显示的位置
        window.setGravity(gravity);

        //设置宽高
        WindowManager.LayoutParams lp = window.getAttributes();
        Display display = atv.getWindowManager().getDefaultDisplay();
        lp.width = display.getWidth();
        lp.height = display.getHeight();

        window.setAttributes(lp);
    }

    public static void setHalfScreen(Context cx, Dialog dialog, int gravity) {

        Activity atv = (Activity) cx;
        Window window = dialog.getWindow();

        // 此处可以设置dialog显示的位置
        window.setGravity(gravity);

        //设置宽高
        WindowManager.LayoutParams lp = window.getAttributes();
        Display display = atv.getWindowManager().getDefaultDisplay();
        lp.width = 2 * display.getWidth() / 3;
        lp.height = 2 * display.getHeight() / 3;

        window.setAttributes(lp);
    }

    public static void setBottmScreen(Context cx, Dialog dialog) {

        Window window = dialog.getWindow();

        // 此处可以设置dialog显示的位置
        window.setGravity(Gravity.BOTTOM);
        //设置宽高
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
    }


    // -----------------------------------------------ViewPager----------------------------------------------

    static public void setScroolDuration(ViewPager pager, int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(
                    pager.getContext(), new AccelerateInterpolator());
            field.set(pager, scroller);
            scroller.setmDuration(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void scrollTop(final ScrollView sv) {

        sv.postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_UP);
            }
        }, 50);
    }


    static public void scrollBottom(final ScrollView sv) {
        sv.postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 50);
    }

    static public class FixedSpeedScroller extends Scroller {
        private int mDuration = 150;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,
                                int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }
}
