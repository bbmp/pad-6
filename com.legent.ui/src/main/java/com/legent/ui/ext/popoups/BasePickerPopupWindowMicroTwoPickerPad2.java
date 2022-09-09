package com.legent.ui.ext.popoups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.R;

/**
 * Created by linxiaobin on 2016/1/18.
 */
public class BasePickerPopupWindowMicroTwoPickerPad2 extends AbsPopupWindow implements View.OnClickListener {

    public interface PickListener {

        void onCancel();

        void onConfirm();
    }

    FrameLayout divMain;
    ImageView ivCookModelName;
    TextView txtConfirm, txtCancel,txtProMode,txtDesc;
    View customView;
    protected PickListener listener;

    public BasePickerPopupWindowMicroTwoPickerPad2(Context cx, View customView,int cookModuleId) {
        super(cx);

        View view = LayoutInflater.from(cx).inflate(R.layout.abs_view_micro_picker_popoup_pad2, null);
        divMain = (FrameLayout) view.findViewById(R.id.divMain);
        txtConfirm = (TextView) view.findViewById(R.id.txtConfirm);
        txtProMode = (TextView) view.findViewById(R.id.txtProMode);
        txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        txtDesc = (TextView) view.findViewById(R.id.txtDesc);
        ivCookModelName = (ImageView) view.findViewById(R.id.iv_cook_mode_icon);
        txtConfirm.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        ivCookModelName.setImageResource(cookModuleId);

        this.setContentView(view);
        this.setWidth((int)cx.getResources().getDimension(R.dimen.common_popWindown_width));
        this.setHeight((int)cx.getResources().getDimension(R.dimen.common_popWindown_height));
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
            if (listener != null) {
                listener.onCancel();
            }
        }
    }

    public void setPickListener(PickListener listener) {
        this.listener = listener;
    }

    public void setTxtDesc(String desc) {
        txtDesc.setText(desc);
    }
    public void setTxtProMode(String title) {
        txtProMode.setText(title);
    }


    public void setTxtConfirm(String tvConfirm) {
        txtConfirm.setText(tvConfirm);
    }

    public void setTxtCancel(String tvCancel) {
        txtCancel.setText(tvCancel);
    }
}

