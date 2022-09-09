package com.robam.rokipad.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robam.rokipad.R;
import com.robam.rokipad.ui.dialog.BaseDialog;


/**
 * 类型0 Dialog
 * 中部两个滑轮，底部两个按钮，居中弹出
 */
public class DialogType_09 extends BaseDialog {

    private TextView mTvOk;
    private TextView mTvCancel;
    private TextView mContentText;
    private ImageView mIvContent;


    public DialogType_09(Context context) {
        super(context);
        mDialog.setPosition(Gravity.CENTER, 0, 0);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_type_09, null);
        mTvOk = (TextView) rootView.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) rootView.findViewById(R.id.tv_exit);
        createDialog(rootView);
    }


    @Override
    public void setContentImg(int res) {
        super.setContentImg(res);
        mIvContent.setImageResource(res);
    }

    @Override
    public void setContentText(int contentStrId) {
        super.setContentText(contentStrId);
        mContentText.setText(contentStrId);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mTvOk.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        mTvCancel.setText(textId);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void bindAllListeners() {
        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick(v);
            }
        });

        if (mTvCancel != null) {
            mTvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCancelClick(v);
                }
            });
        }

    }

}
