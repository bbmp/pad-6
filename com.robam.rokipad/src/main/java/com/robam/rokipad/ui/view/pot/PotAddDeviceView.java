package com.robam.rokipad.ui.view.pot;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.legent.utils.LogUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.listener.OnItemClickListener;

import butterknife.ButterKnife;

/**
 * Created by 14807 on 2019/8/6.
 * PS:温控锅配网View
 */
public class PotAddDeviceView extends FrameLayout {

    Context cx;
    View viewRoot;
    private OnItemClickListener onItemClickListener;


    public PotAddDeviceView(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public PotAddDeviceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public PotAddDeviceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context cx, Object o) {
        this.cx = cx;
        viewRoot = LayoutInflater.from(cx).inflate(R.layout.pot_add_device_view, this, true);
        if (!viewRoot.isInEditMode()) {
            ButterKnife.inject(this, viewRoot);
        }
        initListener();
    }

    private void initListener() {
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtils.e("20190807", "onAttachedToWindow:");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.e("20190807", "onDetachedFromWindow:");
    }
}
