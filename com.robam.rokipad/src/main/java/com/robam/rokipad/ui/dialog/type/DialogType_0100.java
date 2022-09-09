package com.robam.rokipad.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.legent.utils.LogUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.listener.OnItemSelectedListenerCenter;
import com.robam.rokipad.ui.dialog.BaseDialog;
import com.robam.rokipad.ui.view.wheelview.LoopView2;

import java.util.List;


/**
 * 类型0 Dialog
 * 中部一个滑轮，底部两个按钮，居中弹出
 */
public class DialogType_0100 extends BaseDialog {

    private TextView mCancelTv;
    private TextView mTvAffirm;
    private TextView mTvWheelUnitName;
    protected LoopView2 mLoopViewCenter;

    public DialogType_0100(Context context) {
        super(context);
        mDialog.setPosition(Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_type_0100, null);
        mCancelTv = (TextView) rootView.findViewById(R.id.tv_cancel);
        mTvAffirm = (TextView) rootView.findViewById(R.id.tv_affirm);
        mTvWheelUnitName = (TextView) rootView.findViewById(R.id.tv_wheel_unit_name);
        mLoopViewCenter = (LoopView2) rootView.findViewById(R.id.wheel_view);
        mLoopViewCenter.setDividerColor(0xff666666);
        mLoopViewCenter.setCenterTextColor(0xffffffff);
        createDialog(rootView);
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        mCancelTv.setText(textId);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void setUnitName(int textId) {
        mTvWheelUnitName.setText(textId);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mTvAffirm.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }


    @Override
    public void setWheelViewData(List<String> listCenter,
                                 boolean isLoop, int centerIndex,
                                 OnItemSelectedListenerCenter onItemSelectedListenerCenter) {
        if (!isLoop) {
            mLoopViewCenter.setNotLoop();
        }
        mLoopViewCenter.setItems(listCenter);
        mLoopViewCenter.setInitPosition(centerIndex);
        mLoopViewCenter.setCurrentPosition(centerIndex);
        setmOnItemSelectedListenerCenter(onItemSelectedListenerCenter);
        LogUtils.i("20170904", "listCenter:" + listCenter);
        LogUtils.i("20170904", "onItemSelectedListenerCenter:" + onItemSelectedListenerCenter);
    }

    @Override
    public void bindAllListeners() {
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });
        mTvAffirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick(v);
            }
        });
        mLoopViewCenter.setListenerCenter(new OnItemSelectedListenerCenter() {

            @Override
            public void onItemSelectedCenter(String contentCenter) {
                onTouchWheelSelectedCenter(contentCenter);
            }

        });
    }

}
