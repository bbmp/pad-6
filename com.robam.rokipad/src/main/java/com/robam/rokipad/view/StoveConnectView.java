package com.robam.rokipad.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.plat.constant.IDeviceType;
import com.legent.ui.UIService;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2019/2/21.
 */

public class StoveConnectView extends FrameLayout {
    Context cx;
    View viewRoot;
    @InjectView(R.id.stove_con)
    ImageView mStoveCon;
    @InjectView(R.id.iv_add_stove)
    ImageView mIvAddStove;

    public StoveConnectView(Context context) {
        super(context);
        initView(context, null);
    }

    public StoveConnectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public StoveConnectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.cx = context;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.stove_connect_view, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        fireBaseAnalytics.setCurrentScreen((Activity)cx,cx.getString(R.string.google_screen_stove_add_not),null);
    }

    @OnClick(R.id.iv_add_stove)
    public void onViewClicked() {
//        ToastUtils.showShort("敬请期待");
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RRQZ);
        UIService.getInstance().postPage(PageKey.DeviceBluetoothAdd, bd);
    }
}
