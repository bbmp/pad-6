package com.legent.utils.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.legent.R;
import com.legent.utils.LogUtils;

public class ToastUtils {

    static Context cx;

    public static void init(Context cx) {
        ToastUtils.cx = cx;
    }

    public static void showThrowable(Throwable t) {
        if (t != null)
            showShort(t.getMessage());
    }

    public static void showException(Exception e) {
        if (e != null)
            showShort(e.getMessage());
    }

    public static void showShort(int resId) {
        show(resId);
    }

    public static void showShort(String msg) {
        show(msg);
    }

    public static void showLong(int resId) {
        show(resId);
    }

    public static void showLong(String msg) {
        show(msg);
    }

    public static void show(int resId, int duration) {
        String msg = cx.getString(resId);
        show(msg);
    }

    /**
     * 显示toast 这是个view
     */
    public static void show(String msg) {
//        if (toast == null) {
//            toast = new Toast(cx);
//        }
//        View view = LayoutInflater.from(cx).inflate(R.layout.toast_custom, null);
//        TextView tv = (TextView) view.findViewById(R.id.toast_custom_tv);
//        tv.setText(TextUtils.isEmpty(msg) ? "" : msg);
//        toast.setView(view);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 45, -10);
//        toast.show();
        if (toast == null) {
            toast = new Toast(cx);
        }
        LinearLayout.LayoutParams params = null;
        LayoutInflater inflater = (LayoutInflater) cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LogUtils.e("20200324", "MSG:" + msg.length());
        if (12 == msg.trim().length()) {
            params = new LinearLayout.LayoutParams(510,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (13 == msg.length()) {
            params = new LinearLayout.LayoutParams(520,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (14 == msg.length()) {
            params = new LinearLayout.LayoutParams(550,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (15 == msg.length()) {
            params = new LinearLayout.LayoutParams(570,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (16 == msg.length()) {
            params = new LinearLayout.LayoutParams(590,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (17 == msg.length()) {
            params = new LinearLayout.LayoutParams(600,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (18 == msg.length()) {
            params = new LinearLayout.LayoutParams(640,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (19 == msg.length()) {
            params = new LinearLayout.LayoutParams(660,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (20 == msg.length()) {
            params = new LinearLayout.LayoutParams(680,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (21 == msg.length()) {
            params = new LinearLayout.LayoutParams(700,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (22 == msg.length()) {
            params = new LinearLayout.LayoutParams(720,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (23 <= msg.length()) {
            params = new LinearLayout.LayoutParams(820,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }

        View view = inflater.inflate(R.layout.toast_custom, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_custom_tv);
        tv.setLayoutParams(params);
        tv.setText(msg);
        toast.setGravity(Gravity.CENTER, 45, (int) -10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();

    }

    public static void show(int resId) {
        String msg = cx.getString(resId);
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (toast == null) {
            toast = new Toast(cx);
        }
        LinearLayout.LayoutParams params = null;
        LayoutInflater inflater = (LayoutInflater) cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LogUtils.e("20200324", "MSG:" + msg.length());
        if (12 == msg.trim().length()) {
            params = new LinearLayout.LayoutParams(510,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (13 == msg.length()) {
            params = new LinearLayout.LayoutParams(520,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (14 == msg.length()) {
            params = new LinearLayout.LayoutParams(550,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (15 == msg.length()) {
            params = new LinearLayout.LayoutParams(570,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (16 == msg.length()) {
            params = new LinearLayout.LayoutParams(590,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (17 == msg.length()) {
            params = new LinearLayout.LayoutParams(600,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (18 == msg.length()) {
            params = new LinearLayout.LayoutParams(640,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (19 == msg.length()) {
            params = new LinearLayout.LayoutParams(660,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (20 == msg.length()) {
            params = new LinearLayout.LayoutParams(680,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (21 == msg.length()) {
            params = new LinearLayout.LayoutParams(700,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (22 == msg.length()) {
            params = new LinearLayout.LayoutParams(720,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (23 <= msg.length()) {
            params = new LinearLayout.LayoutParams(820,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }

        View view = inflater.inflate(R.layout.toast_custom, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_custom_tv);
        tv.setLayoutParams(params);
        tv.setText(msg);
        toast.setGravity(Gravity.CENTER, 45, (int) -10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public static void show(final String msg, final int duration) {

        if (Strings.isNullOrEmpty(msg))
            return;

        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {

            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(cx, msg, duration);
                } else {
                    toast.setText(msg);
                    toast.setDuration(duration);
                }
                toast.setGravity(Gravity.CENTER, 40, 0);
                toast.show();
            }
        });
        h = null;
    }

    private static Toast toast;
}
