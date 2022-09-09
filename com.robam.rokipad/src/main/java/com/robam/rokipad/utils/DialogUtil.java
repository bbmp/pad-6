package com.robam.rokipad.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.legent.ui.UIService;
import com.legent.utils.LogUtils;


/**
 * 对话框类型整理
 */
public class DialogUtil {

    /**
     * 中部文本内容，底部两个按钮 居中弹出
     */
    public static final int DIALOG_TYPE_0 = 0;
    /**
     * 中部一个滑轮，左右上角两个按钮，底部弹出，横行满屏
     */
    public static final int DIALOG_TYPE_01 = 1;
    /**
     * 中部一个滑轮，左右上角两个按钮，底部弹出，横行满屏汉字
     */
    public static final int DIALOG_TYPE_0100 = 100;
    /**
     * 中部两个滑轮，左右上角两个按钮，底部弹出，横行满屏
     */
    public static final int DIALOG_TYPE_02 = 2;

    /**
     * 上边图片，中部文本，底部一个按钮，居中弹出
     */
    public static final int DIALOG_TYPE_03 = 3;
    /**
     * 上边居中图片，中部文本，底部两个按钮，居中弹出
     */
    public static final int DIALOG_TYPE_04 = 4;
    /**
     * 3D手势动画
     */
    public static final int DIALOG_TYPE_05 = 5;


    public static final int DIALOG_TYPE_06 = 6;

    /**
     * 中部文本，底部一个按钮，居中弹出
     */
    public static final int DIALOG_TYPE_07 = 7;

    /**
     * 上边图片，中部文本，底部两个按钮，居中弹出
     */
    public static final int DIALOG_TYPE_08 = 8;

    /**
     * 上边图片，中部文本，底部两个按钮，居中弹出
     */
    public static final int DIALOG_TYPE_09 = 9;
    /**
     * 添加设备售后Dialog
     */
    public static final int DIALOG_TYPE_10 = 10;

    public static final int DIALOG_TYPE_11 = 11;


    public static final int DIALOG_ADD_DEVICE_SELECT = 12;
    /**
     * 烹饪完成dialog
     */
    public static final int DIALOG_COOKING_FINISH = 13;


    public static final int LENGTH_SHORT = 2;
    public static final int LENGTH_CENTER = 4;
    public static final int LENGTH_LONG = 6;

    /**
     * 获取内容应该显示的宽度(px)
     */
    public static int getContentWidth(Context context) {
        if (context == null || context.getResources() == null || context.getResources().getDisplayMetrics() == null) {
            return 480;
        }
        DisplayMetrics dm = new DisplayMetrics();
        UIService.getInstance().getMain().getActivity().
                getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int width = dm.widthPixels;  // 屏幕宽
//        int width = context.getResources().getDisplayMetrics().widthPixels;

        LogUtils.i("20170814", "width:" + width);

//        if (width > 720) {      // 屏幕宽度大于720px则取屏幕宽度的95%作为内容的宽度
//            width = width * 95 / 100;
//        }
        return width;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        if (context == null || context.getResources() == null || context.getResources().getDisplayMetrics() == null) {
            return 480;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        if (context == null || context.getResources() == null || context.getResources().getDisplayMetrics() == null) {
            return 800;
        }
        DisplayMetrics dm = new DisplayMetrics();
        UIService.getInstance().getMain().getActivity().
                getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int height = dm.heightPixels;  // 屏幕高
//        return context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }


    /**
     * dip 转 px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
