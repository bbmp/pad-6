package com.robam.rokipad.ui.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.robam.rokipad.R;

import butterknife.ButterKnife;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/3.
 * PS: 消毒柜智能设定.
 */
public class DeviceSterilizeSmartSetView extends FrameLayout {


    private Context mContext;

    public DeviceSterilizeSmartSetView(Context context) {
        super(context);
        init(context,null);
    }

    public DeviceSterilizeSmartSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DeviceSterilizeSmartSetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_device_sterilize_smart_set, this, true);
        if (!view.isInEditMode()){
            ButterKnife.inject(this, view);
        }
    }
}
