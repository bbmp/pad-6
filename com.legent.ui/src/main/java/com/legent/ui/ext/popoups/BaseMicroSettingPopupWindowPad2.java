package com.legent.ui.ext.popoups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.R;

/**
 * Created by Rosicky on 16/2/27.
 */
public class BaseMicroSettingPopupWindowPad2 extends AbsPopupWindow implements View.OnClickListener {

    public interface PickListener {

        void onCancel();

        void onConfirm();
    }


    ImageView ivCookIcon;
    TextView  tvCookName;
    TextView txtConfirm;
    protected PickListener listener;


    public BaseMicroSettingPopupWindowPad2(Context cx, String setTitle, String setConfirm, String setCancel, int ivCookIconId, String cookDesc) {
        super(cx);
        View view = LayoutInflater.from(cx).inflate(R.layout.view_micro_setting_popoup_pad2, null);
        ivCookIcon = (ImageView) view.findViewById(R.id.iv_cook_icon);

        tvCookName = (TextView) view.findViewById(R.id.tv_cook_name);

        txtConfirm = (TextView) view.findViewById(R.id.tv_confirm);

        txtConfirm.setText(setConfirm);
        ivCookIcon.setImageResource(ivCookIconId);
        tvCookName.setText(cookDesc);

        ivCookIcon.setVisibility(View.VISIBLE);
        txtConfirm.setOnClickListener(this);

        this.setContentView(view);
        this.setWidth((int)cx.getResources().getDimension(R.dimen.common_popWindown_width));
        this.setHeight((int)cx.getResources().getDimension(R.dimen.common_microwava_popWindown_height));
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setAnimationStyle(R.style.bottom_window_style);

    }



    public BaseMicroSettingPopupWindowPad2(Context cx, String setTitle, String setConfirm, String setCancel) {
        super(cx);
        View view = LayoutInflater.from(cx).inflate(R.layout.view_micro_setting_popoup_pad2, null);
        ivCookIcon = (ImageView) view.findViewById(R.id.iv_cook_icon);

        tvCookName = (TextView) view.findViewById(R.id.tv_cook_name);

        txtConfirm = (TextView) view.findViewById(R.id.tv_confirm);

        txtConfirm.setText(setConfirm);

        ivCookIcon.setVisibility(View.VISIBLE);
        txtConfirm.setOnClickListener(this);

        this.setContentView(view);
        this.setWidth((int)cx.getResources().getDimension(R.dimen.common_popWindown_width));
        this.setHeight((int)cx.getResources().getDimension(R.dimen.common_microwava_popWindown_height));
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setAnimationStyle(R.style.bottom_window_style);
    }

    @Override
    public void onClick(View view) {
        if (view == txtConfirm) {
            dismiss();
            if (listener != null) {
                listener.onConfirm();
            }
        } else  {
            dismiss();
        }
    }

    public void setPickListener(PickListener listener) {
        this.listener = listener;
    }

    public void setTitle(String title) {
        tvCookName.setText(title);
    }

    public void setTxtConfirm(String title) {
        txtConfirm.setText(title);
    }



}
