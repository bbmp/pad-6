package com.dome.alibaba_picdetalg;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

/**
 * Created by as on 2017-10-25.
 */

public class DecBit {


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

    public static byte[] getBytesFromBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if ((quality < 0 || quality > 100) && format != Bitmap.CompressFormat.PNG)
            quality = 50;
        bitmap.compress(format, quality, outputStream);
        return outputStream.toByteArray();
    }

    // -----------------------------------------------ViewPager----------------------------------------------

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


    static public byte[] decAl_bitmap(View v, int width, int height) {
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

    static public class FixedSpeedScroller extends Scroller {
        private int mDuration = 150;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int X, int Y, int dx, int dy,
                                int duration) {
            super.startScroll(X, Y, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int X, int startY, int dx, int dy) {
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
