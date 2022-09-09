package com.robam.rokipad.ui.view.pot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.legent.ui.AbsPage;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.R;
import com.robam.rokipad.listener.OnItemClickListener;
import com.robam.rokipad.utils.IClickStoveSelectListener;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2019/8/6.
 * PS:温控锅配网添加自带背景View
 */
public class PotAddDeviceBgView extends FrameLayout {

    Context cx;
    View viewRoot;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.ll_card_add)
    LinearLayout llCardAdd;
    private OnItemClickListener onItemClickListener;
    private IClickStoveSelectListener clickStoveSelectListener;
    private Timer timer;
    private TimerTask timerTask;
    private AbsFan fan;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (fan != null) {
                        fan.addPotDevice();
                    }
                    break;
            }
        }
    };

    public PotAddDeviceBgView(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public PotAddDeviceBgView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public PotAddDeviceBgView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context cx, Object o) {
        this.cx = cx;
        viewRoot = LayoutInflater.from(cx).inflate(R.layout.pot_add_device_bg_view, this, true);
        if (!viewRoot.isInEditMode()) {
            ButterKnife.inject(this, viewRoot);
        }
        fan = Utils.getDefaultFan();
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
        initAddDeviceTask();
        LogUtils.e("20190807", "onAttachedToWindow:");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        closeAddDeviceTask();
        LogUtils.e("20190807", "onDetachedFromWindow:");
    }

    private void closeAddDeviceTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private void initAddDeviceTask() {
        LogUtils.e("20190807", "initAddDeviceTask:");
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };
        }
        timer.schedule(timerTask, 0, 2000);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {

        if (null != clickStoveSelectListener) {
            clickStoveSelectListener.clickStoveSelectListener("back");
        }
    }

    public void setClickBackListener(IClickStoveSelectListener clickStoveSelectListener) {
        this.clickStoveSelectListener = clickStoveSelectListener;
    }

}
