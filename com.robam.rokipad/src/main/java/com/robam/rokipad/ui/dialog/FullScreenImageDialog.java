package com.robam.rokipad.ui.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.qrcode.QrUtils;
import com.robam.rokipad.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by WZTCM on 2016/1/11.
 */
public class FullScreenImageDialog extends AbsDialog {


    @InjectView(R.id.img)
    ImageView img;

    public FullScreenImageDialog(Context context, String sn) {
        super(context, R.style.Theme_Dialog_FullScreen);
        Bitmap bitmap = QrUtils.create2DCode(sn);
        img.setImageBitmap(bitmap);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_full_screen_img;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }


    static public FullScreenImageDialog show(Context cx, String sn, String name) {
        FullScreenImageDialog dlg = new FullScreenImageDialog(cx, sn);
        dlg.setTitle(name);
        dlg.show();
        return dlg;
    }

    @OnClick(R.id.layout)
    public void onClickLayout() {
        dismiss();
    }

}
