package com.robam.rokipad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.utils.IStove;

import butterknife.ButterKnife;


//此类主要进行业务逻辑的编写（因为每个灶具的业务逻辑都不相同，因此进行分开处理，方便理解）
public class StoveView9B515 extends FrameLayout implements IStove {
    Context cx;
    View viewRoot;


    public StoveView9B515(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public StoveView9B515(Context context) {
        super(context);
        initView(context, null);
    }


    public StoveView9B515(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.cx = context;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.stove_view_9b515, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);
        init();
    }


    private void init() {
        FirebaseAnalytics firebaseAnalytics = NewPadApp.getFireBaseAnalytics();


    }

    @Override
    public void setLeftStatus(Stove stove) {


    }

    @Override
    public void setRightStatus(Stove stove) {

    }

    @Override
    public void setLockStatus(Stove pojo) {

    }

    //灶具定时的指令
    private void setStoveTime(final short headId, final String data) {


    }

    //网络连接状况
    @Override
    public void isConnected(boolean isCon) {

    }


}
