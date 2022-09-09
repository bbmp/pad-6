package com.robam.rokipad.ui.dialog.type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robam.rokipad.R;
import com.robam.rokipad.ui.dialog.BaseDialog;


public class DialogCookingFinish extends BaseDialog {


    private TextView mContentText;
    private TextView mTvOk;

    public DialogCookingFinish(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_1, null);
        mContentText = (TextView) rootView.findViewById(R.id.tv_content_text);
        mTvOk = (TextView) rootView.findViewById(R.id.tv_ok);
        createDialog(rootView);
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
        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick(v);
            }
        });
    }
}
