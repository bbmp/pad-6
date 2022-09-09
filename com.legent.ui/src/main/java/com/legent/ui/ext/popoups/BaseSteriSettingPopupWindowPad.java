package com.legent.ui.ext.popoups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legent.ui.R;

/**
 * Created by Rosicky on 16/2/27.
 */
public class BaseSteriSettingPopupWindowPad extends AbsPopupWindow implements View.OnClickListener {

    public interface PickListener {

        void onCancel();

        void onConfirm();
    }

    FrameLayout divMain;
    TextView txtConfirm, txtCancel,txtSetting,tvTitleName1;
    View customView;
    protected PickListener listener;

    public BaseSteriSettingPopupWindowPad(Context cx,String title,View customView) {
        super(cx);

        View view = LayoutInflater.from(cx).inflate(R.layout.view_steri_setting_popoup_pad, null);
        divMain = (FrameLayout) view.findViewById(R.id.divMain);
        txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        txtConfirm = (TextView) view.findViewById(R.id.txtConfirm);
        txtSetting = (TextView) view.findViewById(R.id.txtSetting);
        tvTitleName1 = (TextView) view.findViewById(R.id.tvTitleName1);
        txtConfirm.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        txtSetting.setText(title);

        this.setContentView(view);
        this.setWidth((int)cx.getResources().getDimension(R.dimen.common_popWindown_width));
        this.setHeight((int)cx.getResources().getDimension(R.dimen.common_stove_popWindown_height));
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setAnimationStyle(R.style.bottom_window_style);

        this.customView = customView;
        divMain.addView(customView);
    }


    public BaseSteriSettingPopupWindowPad(Context cx,String title,String tvTitleName,View customView) {
        super(cx);

        View view = LayoutInflater.from(cx).inflate(R.layout.view_steri_setting_popoup_pad, null);
        divMain = (FrameLayout) view.findViewById(R.id.divMain);
        txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        txtConfirm = (TextView) view.findViewById(R.id.txtConfirm);
        txtSetting = (TextView) view.findViewById(R.id.txtSetting);
        tvTitleName1 = (TextView) view.findViewById(R.id.tvTitleName1);
        txtConfirm.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        txtSetting.setText(title);
        tvTitleName1.setText(tvTitleName);

        this.setContentView(view);
        this.setWidth((int)cx.getResources().getDimension(R.dimen.common_popWindown_width));
        this.setHeight((int)cx.getResources().getDimension(R.dimen.common_stove_popWindown_height));
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setAnimationStyle(R.style.bottom_window_style);

        this.customView = customView;
        divMain.addView(customView);
    }

    @Override
    public void onClick(View view) {
        if (view == txtConfirm) {
            dismiss();
            if (listener != null) {
                listener.onConfirm();
            }
        } else if (view == txtCancel) {
            dismiss();
        }
    }

    public void setPickListener(PickListener listener) {
        this.listener = listener;
    }
    public void setTitle(String title) {
        txtSetting.setText(title);
    }
}
