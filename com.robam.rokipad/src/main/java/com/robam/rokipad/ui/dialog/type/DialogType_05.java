package com.robam.rokipad.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.dialog.BaseDialog;


/**
 * 类型0 Dialog
 * 中部两个滑轮，底部两个按钮，居中弹出
 */
public class DialogType_05 extends BaseDialog {

    private TextView mTvOk;
    private ImageView mIvAnim;

    public DialogType_05(Context context) {
        super(context);
        mDialog.setPosition(Gravity.CENTER, 0, 0);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_type_05, null);
        mTvOk = (TextView) rootView.findViewById(R.id.tv_ok);
        mIvAnim = (ImageView) rootView.findViewById(R.id.iv_anim);
        createDialog(rootView);
        initAnim();
    }

    private void initAnim() {
        Glide.with(mContext)
                .load(R.mipmap.ic_gesture_anim)
                .asGif()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mIvAnim);
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
