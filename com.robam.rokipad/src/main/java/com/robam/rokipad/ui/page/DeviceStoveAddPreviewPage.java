package com.robam.rokipad.ui.page;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.plat.events.DeviceStoveOnlineSwitchEvent;
import com.legent.plat.events.DeviceStoveRunEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.DeviceAddBluetoothSubsetFailureEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.view.setting.downloadbutton.AnimDownloadProgressButton;
import com.robam.rokipad.utils.ToolUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/2/1.
 * PS: 灶具添加设备预览页.
 */
public class DeviceStoveAddPreviewPage extends BasePage {

    @InjectView(R.id.iv_preview)
    ImageView mIvPreview;
    @InjectView(R.id.anim_btn)
    AnimDownloadProgressButton mAnimBtn;
    private String deviceType;
    private int progress;
    private Timer mTimer;
    private TimerTask mTimerTask;
    boolean isProgress = true;

    MyHandler mHandler = new MyHandler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    changedButtonProgress(progress = (int) msg.obj);
                    break;
            }
        }
    };

    private void changedButtonProgress(int progress) {
        if (mAnimBtn == null) return;
        if (isProgress) {
            mAnimBtn.setProgress(progress);
            mAnimBtn.setState(AnimDownloadProgressButton.DOWNLOADING);
            mAnimBtn.setClickable(false);
            mAnimBtn.setProgressText("", mAnimBtn.getProgress());
        }
        if (progress == 100) {
            ToastUtils.showShort("连接失败");
            mAnimBtn.setState(AnimDownloadProgressButton.NORMAL);
            mAnimBtn.setCurrentText("确定");
            mAnimBtn.setClickable(true);
            isProgress = false;
        }
    }


    @Subscribe
    public void onEvent(DeviceStoveOnlineSwitchEvent event) {
        if (event.children != null) {
            EventUtils.postEvent(new DeviceStoveRunEvent(true));
            UIService.getInstance().returnHome();
        }
    }


//    @Subscribe
//    public void onEvent(DeviceAddBluetoothSubsetSuccessEvent event) {
//        ToastUtils.showShort(R.string.setting_model_device_add_success_text);
//        progress = 100;
//        mAnimBtn.setState(AnimDownloadProgressButton.DOWNLOADING);
//        mAnimBtn.setProgressText("", progress);
//        mAnimBtn.setState(AnimDownloadProgressButton.NORMAL);
//        mAnimBtn.setCurrentText("确定");
//        if (mTimer != null) {
//            mTimer.cancel();
//            mTimer = null;
//        }
//        if (mTimerTask != null) {
//            mTimerTask.cancel();
//            mTimerTask = null;
//        }
//        UIService.getInstance().popBack();
//        UIService.getInstance().popBack();
//        UIService.getInstance().popBack();
//        EventUtils.postEvent(new DeviceStoveAddSuccessEvent());
//
//    }

    @Subscribe
    public void onEvent(DeviceAddBluetoothSubsetFailureEvent event) {

        LogUtils.e("20200227", "DeviceAddBluetoothSubsetFailureEvent:");
//        progress = 100;
//        mAnimBtn.setState(AnimDownloadProgressButton.DOWNLOADING);
//        mAnimBtn.setProgressText("", progress);
//        mAnimBtn.setState(AnimDownloadProgressButton.NORMAL);
//        mAnimBtn.setCurrentText("确定");
//        if (mTimer != null) {
//            mTimer.cancel();
//            mTimer = null;
//        }
//        if (mTimerTask != null) {
//            mTimerTask.cancel();
//            mTimerTask = null;
//        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            deviceType = (String) bundle.getSerializable(PageArgumentKey.deviceType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        fireBaseAnalytics.setCurrentScreen((Activity) cx, deviceType + ":" + cx.getString(R.string.google_screen_bluetooth_add), null);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_add_stove_preview, container, false);
        ButterKnife.inject(this, view);
        initData();
        initListener();
        return view;
    }

    /**
     * 初始化监听，DownloadButton的状态监听
     */
    private void initListener() {
        mAnimBtn.setClickable(true);
        mAnimBtn.setCurrentText(cx.getString(R.string.ok_btn));
        mAnimBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mAnimBtn.setProgressBtnBackgroundColor(Color.parseColor("#0000ff"));
                } else {
                    mAnimBtn.setProgressBtnBackgroundColor(Color.parseColor("#00ff00"));
                }
            }
        });
        mAnimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProgress = true;
                ToolUtils.logEvent(deviceType, cx.getString(R.string.google_screen_stove_add),
                        cx.getString(R.string.google_screen_name));
                AbsFan fan = Utils.getDefaultFan();
                fan.addPotDevice();
                progress = 0;
                showButton();
            }
        });
    }

    private void showButton() {
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = progress += 1;
                    mHandler.sendMessage(msg);
                }
            };
        }
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(mTimerTask, 10, 500);
        }

    }

    /**
     * 初始化数据
     */
    private void initData() {
        switch (deviceType) {
            case IRokiFamily.R9W70:
            case IRokiFamily.HI704:
                Glide.with(this).load(R.mipmap.ic_stove_9w70_gif)
                        .asGif().transformFrame(new CenterCrop(cx))
                        .into(mIvPreview);
                break;
            case IRokiFamily.R9B39:
                Glide.with(this).load(R.mipmap.ic_stove_9b39_gif)
                        .asGif().transformFrame(new CenterCrop(cx))
                        .into(mIvPreview);
                break;
            case IRokiFamily.R9B37:
                Glide.with(this).load(R.mipmap.ic_stove_9b37_git)
                        .asGif().transformFrame(new CenterCrop(cx))
                        .into(mIvPreview);
                break;
            case IRokiFamily.R9B30C:
                Glide.with(this).load(R.mipmap.ic_stove_9b30c_gif)
                        .asGif().transformFrame(new CenterCrop(cx))
                        .into(mIvPreview);
                break;
            case IRokiFamily._9B515:
                Glide.with(this).load(R.mipmap.ic_stove_9b515_gif)
//                        .asGif()
                        .into(mIvPreview);
                break;
            case IRokiFamily._9W851:
                Glide.with(this).load(R.mipmap.ic_stove_9w70_gif)
                        .asGif().transformFrame(new CenterCrop(cx))
                        .into(mIvPreview);
                break;
            case IRokiFamily.R9B39E:
                Glide.with(this).load(R.mipmap.ic_stove_9b39e_gif)
                        .asGif().transformFrame(new CenterCrop(cx))
                        .into(mIvPreview);
                break;
            default:
                Glide.with(this).load(R.mipmap.ic_stove_9b39e_gif)
                        .asGif().transformFrame(new CenterCrop(cx))
                        .into(mIvPreview);
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        ButterKnife.reset(this);
    }
}
