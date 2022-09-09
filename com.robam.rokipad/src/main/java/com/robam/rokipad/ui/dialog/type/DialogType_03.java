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
public class DialogType_03 extends BaseDialog {

    private TextView mTvOk;
    private TextView mContentText;
    private ImageView mIvContent;


    public DialogType_03(Context context) {
        super(context);
        mDialog.setPosition(Gravity.CENTER, 0, 0);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_type_03, null);
        mTvOk = (TextView) rootView.findViewById(R.id.tv_ok);
        mContentText = (TextView) rootView.findViewById(R.id.tv_content_text);
        mIvContent = (ImageView) rootView.findViewById(R.id.iv_content);
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
    public void setContentText(CharSequence csq) {
        super.setContentText(csq);
        mContentText.setText(csq);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mTvOk.setText(textId);
        setOnOkClickListener(okOnClickListener);
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
