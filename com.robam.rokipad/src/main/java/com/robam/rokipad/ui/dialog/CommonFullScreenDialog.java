package com.robam.rokipad.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.rokipad.R;

/**
 * Created by as on 2017-10-23.
 */

public class CommonFullScreenDialog extends AbsDialog {

//    public CommonFullScreenDialog(Context context) {
//
//        super(context, R.style.Theme_Dialog_HorziFullScreen);
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = windowManager.getDefaultDisplay();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        display.getMetrics(displayMetrics);
//        Window window = this.getWindow();
//        int widthPixels = 1024;
//        int heightPixels = (int) (displayMetrics.heightPixels * 0.78);
//        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        layoutParams.width = widthPixels;
//        layoutParams.height = heightPixels;
//        window.setAttributes(layoutParams);
//    }

    public CommonFullScreenDialog(Context context, int widthPixels, int heightPixels) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = widthPixels;
        layoutParams.height = heightPixels;
        layoutParams.gravity = Gravity.RIGHT;
        window.setAttributes(layoutParams);
    }

    @Override
    protected int getViewResId() {
        return 0;
    }

    @Override
    protected void initView(View view) {
    }
}
