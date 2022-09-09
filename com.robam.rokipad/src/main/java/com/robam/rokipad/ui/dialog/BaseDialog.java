package com.robam.rokipad.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.robam.rokipad.R;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.listener.OnItemSelectedListenerCenter;
import com.robam.rokipad.listener.OnItemSelectedListenerFront;
import com.robam.rokipad.listener.OnItemSelectedListenerRear;

import java.util.List;

/**
 * 这个类会实现IRokiDialog的所有方法，这样任何一种类型的对话框调用都不会出错
 * 不支持的部分调用相应的方法时无效，不会导致崩溃
 */
public abstract class BaseDialog implements IRokiDialog {


    protected CoreDialog mDialog = null;
    protected Context mContext;
    protected TextView mTitleTv;
    protected TextView mTitleAralmCodeTv;
    protected View.OnClickListener mCancelOnClickListener;
    protected View.OnClickListener mOkOnClickListener;
    protected OnItemSelectedListenerFront mOnItemSelectedListenerFront;
    protected OnItemSelectedListenerCenter mOnItemSelectedListenerCenter;
    protected OnItemSelectedListenerRear mOnItemSelectedListenerRear;
    public abstract void initDialog();

    public abstract void bindAllListeners();

    public BaseDialog(Context context) {
        mContext = context;
        initDialog();
        bindAllListeners();
    }

    protected void createDialog(View rootView) {
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.dialog, rootView, true);
            mDialog.setPosition(Gravity.CENTER, 0, 0);
        }
    }

    public void onCancelClick(View v) {
        dismiss();
        if (mCancelOnClickListener != null) mCancelOnClickListener.onClick(v);
    }

    public void onOkClick(View v) {
        if (mOkOnClickListener != null) mOkOnClickListener.onClick(v);
    }


    @Override
    public void setTitleText(int titleStrId) {
        if (mTitleTv != null) mTitleTv.setText(titleStrId);
    }

    @Override
    public void setUnitName(int textId) {

    }

    @Override
    public void setTitleAralmCodeText(int titleAralmCodeStrId) {
        if (mTitleAralmCodeTv != null) mTitleAralmCodeTv.setText(titleAralmCodeStrId);
    }

    @Override
    public void setCountDown(int min) {

    }

    @Override
    public void setContentImg(int res) {

    }

    @Override
    public void setContentText(int contentStrId) {
    }

    @Override
    public void setContentText(CharSequence contentText) {

    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
    }


    @Override
    public void setCanBtnTextColor(int color) {

    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
    }

    @Override
    public void setData(String[] deviceCategory) {

    }

    public void setOnCancelClickListener(View.OnClickListener cancelClickListener) {
        mCancelOnClickListener = cancelClickListener;
    }

    public void setOnOkClickListener(View.OnClickListener okClickListener) {
        mOkOnClickListener = okClickListener;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        if (mDialog != null) mDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    @Override
    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        if (mDialog != null) mDialog.setOnDismissListener(listener);
    }

    @Override
    public void setOnShowListener(DialogInterface.OnShowListener listener) {
        if (mDialog != null) mDialog.setOnShowListener(listener);
    }

    @Override
    public void setCancelable(boolean b) {
        if (mDialog != null) mDialog.setCancelable(b);
    }

    @Override
    public void setInitTaskData(int randomNumber, int readyTime, long timingTime) {
    }

    @Override
    public void setToastShowTime(int time) {

    }
    public void onTouchWheelSelectedFront(String content) {
        if (mOnItemSelectedListenerFront != null)
            mOnItemSelectedListenerFront.onItemSelectedFront(content);

    }

    public void onTouchWheelSelectedCenter(String content) {
        if (mOnItemSelectedListenerCenter != null)
            mOnItemSelectedListenerCenter.onItemSelectedCenter(content);

    }

    public void onTouchWheelSelectedRear(String content) {
        if (mOnItemSelectedListenerRear != null)
            mOnItemSelectedListenerRear.onItemSelectedRear(content);

    }

    public void setmOnItemSelectedListenerFront(OnItemSelectedListenerFront mOnItemSelectedListenerFront) {
        this.mOnItemSelectedListenerFront = mOnItemSelectedListenerFront;
    }

    public void setmOnItemSelectedListenerCenter(OnItemSelectedListenerCenter mOnItemSelectedListenerCenter) {
        this.mOnItemSelectedListenerCenter = mOnItemSelectedListenerCenter;
    }

    public void setmOnItemSelectedListenerRear(OnItemSelectedListenerRear mOnItemSelectedListenerRear) {
        this.mOnItemSelectedListenerRear = mOnItemSelectedListenerRear;
    }

    @Override
    public void setWheelViewData(List<String> listCenter, boolean isLoop, int centerIndex,
                                 OnItemSelectedListenerCenter onItemSelectedListenerCenter) {

    }

    @Override
    public void setWheelViewData(List<String> listFront, List<String> listLater, boolean isLoop, int frontIndex,
                                 int laterIndex, OnItemSelectedListenerFront onItemSelectedListenerFront,
                                 OnItemSelectedListenerRear onItemSelectedListenerLater) {

    }

    @Override
    public boolean isShow() {
        if (mDialog == null)
            return false;
        return mDialog.isShowing();
    }

    @Override
    public void show() {
        if (mDialog != null) {
            /*WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            display.getMetrics(dm);

            Window win = mDialog.getWindow();
            WindowManager.LayoutParams wl = win.getAttributes();
           // wl.width = (int) (dm.widthPixels*0.6);
            wl.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 1.0);
            wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(wl);*/

            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                if (!activity.isFinishing()) {
                    mDialog.show();
                }
            } else {
                mDialog.show();
            }
        }
    }

    @Override
    public void dismiss() {
        if (mDialog != null) mDialog.dismiss();
    }


    @Override
    public CoreDialog getCoreDialog() {
        return null;
    }

}