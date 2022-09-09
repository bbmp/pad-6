package com.robam.rokipad.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robam.rokipad.R;
import com.robam.rokipad.listener.OnItemSelectedListenerFront;
import com.robam.rokipad.listener.OnItemSelectedListenerRear;
import com.robam.rokipad.ui.dialog.BaseDialog;
import com.robam.rokipad.ui.view.wheelview.LoopView;

import java.util.List;


/**
 * 类型0 Dialog
 * 中部两个滑轮，底部两个按钮，居中弹出
 */
public class DialogType_02 extends BaseDialog {

    private TextView mCancelTv;
    private TextView mTvAffirm;
    protected LoopView mLoopViewCenterFront;
    protected LoopView mLoopViewCenterRear;

    public DialogType_02(Context context) {
        super(context);
        mDialog.setPosition(Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_type_02, null);
        mCancelTv = (TextView) rootView.findViewById(R.id.tv_cancel);
        mTvAffirm = (TextView) rootView.findViewById(R.id.tv_affirm);
        mLoopViewCenterFront = (LoopView) rootView.findViewById(R.id.wheel_front);
        mLoopViewCenterFront.setDividerColor(0xff666666);
        mLoopViewCenterFront.setCenterTextColor(0xffffffff);
        mLoopViewCenterRear = (LoopView) rootView.findViewById(R.id.wheel_later);
        mLoopViewCenterRear.setDividerColor(0xff666666);
        mLoopViewCenterRear.setCenterTextColor(0xffffffff);
        createDialog(rootView);
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        mCancelTv.setText(textId);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mTvAffirm.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }


    @Override
    public void setWheelViewData(List<String> listFront, List<String> listLater, boolean isLoop,
                                 int frontIndex, int laterIndex, OnItemSelectedListenerFront
                                         onItemSelectedListenerFront, OnItemSelectedListenerRear
                                         onItemSelectedListenerLater) {

        if (!isLoop) {
            mLoopViewCenterFront.setNotLoop();
            mLoopViewCenterRear.setNotLoop();
        }
        mLoopViewCenterFront.setItems(listFront);
        mLoopViewCenterFront.setInitPosition(frontIndex);
        mLoopViewCenterFront.setCurrentPosition(frontIndex);
        setmOnItemSelectedListenerFront(onItemSelectedListenerFront);
        mLoopViewCenterRear.setItems(listLater);
        mLoopViewCenterRear.setInitPosition(laterIndex);
        mLoopViewCenterRear.setCurrentPosition(laterIndex);
        setmOnItemSelectedListenerRear(onItemSelectedListenerLater);
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
        mLoopViewCenterFront.setListenerFrone(new OnItemSelectedListenerFront() {

            @Override
            public void onItemSelectedFront(String contentFront) {
                onTouchWheelSelectedFront(contentFront);
            }
        });
        mLoopViewCenterRear.setListenerRear(new OnItemSelectedListenerRear() {
            @Override
            public void onItemSelectedRear(String contentRear) {
                onTouchWheelSelectedRear(contentRear);
            }
        });
    }

}
