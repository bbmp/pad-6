package com.robam.rokipad.ui.dialog.type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robam.rokipad.R;
import com.robam.rokipad.ui.dialog.BaseDialog;


/**
 * 类型0 Dialog
 * 中部文本内容，底部两个按钮，居中弹出
 */
public class DialogType_0 extends BaseDialog {


    private TextView mContentText;
    private TextView mCancelTv;
    private TextView mTvOk;

    public DialogType_0(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_0, null);
        mContentText = (TextView) rootView.findViewById(R.id.tv_content_text);
        mCancelTv = (TextView) rootView.findViewById(R.id.tv_cancel);
        mTvOk = (TextView) rootView.findViewById(R.id.tv_ok);
        createDialog(rootView);
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        mCancelTv.setText(textId);
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mTvOk.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }

    public void setContentText(int contentStrId) {
        mContentText.setText(contentStrId);
    }

    @Override
    public void setContentText(CharSequence contentText) {
        if (mContentText != null) {
            mContentText.setText(contentText);
        }
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
