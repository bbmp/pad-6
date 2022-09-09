package com.robam.rokipad.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.legent.utils.TimeUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.dialog.BaseDialog;



/**
 * 类型0 Dialog
 * 中部两个滑轮，底部两个按钮，居中弹出
 */
public class DialogType_04 extends BaseDialog {

    private TextView mTvOk;
    private TextView mCancelTv;
    private TextView mContentText;
    private TextView mTvTimeText;

    public DialogType_04(Context context) {
        super(context);
        mDialog.setPosition(Gravity.CENTER, 0, 0);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_type_04, null);
        mTvOk = (TextView) rootView.findViewById(R.id.tv_ok);
        mCancelTv = (TextView) rootView.findViewById(R.id.tv_cancel);
        mContentText = (TextView) rootView.findViewById(R.id.tv_content_text);
        mTvTimeText = (TextView) rootView.findViewById(R.id.tv_time_text);
        createDialog(rootView);
    }


    @Override
    public void setContentText(int contentStrId) {
        super.setContentText(contentStrId);
        mContentText.setText(contentStrId);
    }

    @Override
    public void setCountDown(final int min) {
        super.setCountDown(min);
        String time = TimeUtils.sec2clock(min);
        mTvTimeText.setText(String.valueOf(time));
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mTvOk.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        mCancelTv.setText(textId);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void bindAllListeners() {
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });
        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick(v);
            }
        });
    }

}
