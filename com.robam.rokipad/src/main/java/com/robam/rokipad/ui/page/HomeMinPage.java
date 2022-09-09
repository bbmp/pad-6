package com.robam.rokipad.ui.page;


import android.annotation.SuppressLint;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.constant.IPlatRokiFamily;
import com.legent.plat.pojos.device.IDevice;
import com.legent.services.TaskService;
import com.legent.ui.FormManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiState;
import com.legent.utils.api.WifiUtils;
import com.robam.common.Utils;
import com.robam.common.events.FanLevelEvent;
import com.robam.common.events.FanLightEvent;
import com.robam.common.events.FanPowerEvent;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.MainActivityRunEvent;
import com.robam.common.events.StoveCloseEvent;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.utils.DialogUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by lixin 2018/12/6.
 */
public class HomeMinPage extends BasePage {


    @InjectView(R.id.tv_time)
    TextClock tvTime;
    @InjectView(R.id.iv_wifi_state)
    ImageView ivWifiState;
    @InjectView(R.id.fan_light)
    ImageView fanLight;
    @InjectView(R.id.fan_small)
    ImageView fanSmall;
    @InjectView(R.id.fan_middle)
    ImageView fanMiddle;
    @InjectView(R.id.fan_big)
    ImageView fanBig;
    @InjectView(R.id.tv_clean_lock)
    TextView tvCleanLock;
    @InjectView(R.id.tv_back)
    TextView tvBack;
    AbsFan fan;
    Animation animationBig;
    Animation animationMiddle;
    Animation animationSmall;
    private IRokiDialog delayShutdownDialog;
    private IRokiDialog regularVentilationDialog;
    private IRokiDialog shutdownCountdownDialog;
    WifiUtils.WifiReceiver wifiReceiver;



    @Subscribe
    public void onEvent(FanLightEvent event) {
        refreshLightIcon(event.fan.light);

    }

    @Subscribe
    public void onEvent(StoveCloseEvent event) {

        if (fan.level != FanStatus.LEVEL_EMPTY) {
            initDialog();
        }
    }

    @Subscribe
    public void onEvent(FanPowerEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.fan.getID())) {
            return;
        }
        LogUtils.e("20200306", "event.power:" + event.power);
        refreshLevelIcon(event.fan.level);
        if (!event.power) {
            if (shutdownCountdownDialog != null && shutdownCountdownDialog.isShow()) {
                shutdownCountdownDialog.dismiss();
            }
            if (regularVentilationDialog != null && regularVentilationDialog.isShow()) {
                regularVentilationDialog.dismiss();
            }

            if (delayShutdownDialog != null && delayShutdownDialog.isShow()) {
                delayShutdownDialog.dismiss();
            }
        }
    }

    @Subscribe
    public void onEvent(FanLevelEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.fan.getID())) {
            return;
        }
        refreshLevelIcon(event.level);
        refreshLevelToast(event.level);
    }


    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID())) {
            return;
        }
        fan = event.pojo;
        LogUtils.e("20200226", "fan:" + fan.level + " state:" + fan.status);
        if (FanStatus.LEVEL_EMPTY == fan.level) {//无档位和第一次初始进入

            refreshLevelIcon(fan.level);
        }
        refreshLightIcon(fan.light);
        refreshLevelIcon(fan.level);
        shutdownCountdownClose();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewroot = inflater.inflate(R.layout.home_min_page, container, false);
        ButterKnife.inject(this, viewroot);
        return viewroot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wifiReceiver = new WifiUtils.WifiReceiver(cx);
        wifiReceiver.startScaning();

        initView();
        initListener();
    }

    private void initListener() {

        wifiReceiver.setWifiStateListener(new WifiState() {
            @Override
            public void onScanResult(List<ScanResult> list) {
                if (list == null || list.size() == 0) {
                    ivWifiState.setImageResource(R.mipmap.ic_wifi_cloce);
                    return;
                }
                if (list != null && list.size() > 0) {
                    ScanResult sr = WifiUtils.getCurrentScanResult(cx);
                    if (sr == null) {
                        if (null != ivWifiState) {
                            ivWifiState.setImageResource(R.mipmap.ic_wifi_cloce);
                        }
                        return;
                    }
                    String ssidCurrent = sr.SSID;
                    for (int i = 0; i < list.size(); i++) {
                        String ssid = list.get(i).SSID;
                        if (ssidCurrent.equals(ssid)) {
                            wifiState(list.get(i));
                        }
                    }
                }

            }

            @Override
            public void onNetWorkStateChanged(NetworkInfo.DetailedState state) {
                LogUtils.e("20200821", "onNetWorkStateChanged:" + state);
                if (state == NetworkInfo.DetailedState.CONNECTED) {
                    TaskService.getInstance().postUiTask(new Runnable() {
                        @Override
                        public void run() {
                            ScanResult sr = WifiUtils.getCurrentScanResult(cx);
                            wifiState(sr);
                        }
                    }, 500);
                } else {
                }
            }

            @Override
            public void onWiFiStateChanged(int wifiState) {


                boolean wifiConnected = WifiUtils.isWifiConnected(getContext());
                if (!wifiConnected)return;

                switch (wifiState) {
                    case 1:
                        ivWifiState.setImageResource(R.mipmap.wifi_img_one);
                        break;
                    case 2:
                        ivWifiState.setImageResource(R.mipmap.wifi_img_two);
                        break;
                    case 3:
                        ivWifiState.setImageResource(R.mipmap.wifi_img_three);
                        break;
                    default:
                        ivWifiState.setImageResource(R.mipmap.wifi_img_no);
                        break;
                }
            }

            @Override
            public void onWifiPasswordFault() {
            }
        });
        try {
            if (null != tvBack) {
                tvBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventUtils.postEvent(new MainActivityRunEvent());
                        UIService uiService = UIService.getInstance();
                        if (null != uiService) {
                            FormManager top = uiService.getTop();
                            if (null != top) {
                                uiService.popBack();
                            } else {
                                getActivity().finish();
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void wifiState(ScanResult sr) {
        if (sr == null) {
            if (null != ivWifiState) {
                ivWifiState.setImageResource(R.mipmap.ic_wifi_cloce);
            }
            return;
        }
        switch (WifiManager.calculateSignalLevel(sr.level, 4)) {
            case 1:
                if (null != ivWifiState) {
                    ivWifiState.setImageResource(R.mipmap.wifi_img_one);
                }
                break;
            case 2:
                if (null != ivWifiState) {
                    ivWifiState.setImageResource(R.mipmap.wifi_img_two);
                }
                break;
            case 3:
                if (null != ivWifiState) {
                    ivWifiState.setImageResource(R.mipmap.wifi_img_three);
                }

                break;
            default:
                if (null != ivWifiState) {
                    ivWifiState.setImageResource(R.mipmap.wifi_img_no);
                }
                break;
        }
    }


    @Override
    public void setRootBg() {
        setRootBgRes(R.mipmap.ic_center_bg);
    }

    private void initView() {
        fan = Utils.getDefaultFan();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        wifiReceiver.stopScanning();
        wifiReceiver = null;
        getActivity().finish();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.fan_light)
    public void onFanLightClicked() {
        if (fan != null) {
            if (fan.light) {
                sendLight(false);
            } else {
                sendLight(true);
            }
        }
    }

    @OnClick(R.id.fan_small)
    public void onFanSmallClicked() {

        if (fan != null) {
            if (fan.level == FanStatus.LEVEL_SMALL || fan.level == FanStatus.LEVEL_SMALL_Two) {
                sendLevel(FanStatus.LEVEL_EMPTY);
            } else {
                sendLevel(FanStatus.LEVEL_SMALL);
            }
        }
    }

    @OnClick(R.id.fan_middle)
    public void onFanMiddleClicked() {

        if (fan != null) {
            if (fan.level == FanStatus.LEVEL_MIDDLE) {
                sendLevel(FanStatus.LEVEL_EMPTY);
            } else {
                sendLevel(FanStatus.LEVEL_MIDDLE);
            }
        }
    }

    @OnClick(R.id.fan_big)
    public void onFanBigClicked() {

        if (fan != null) {
            if (fan.level == FanStatus.LEVEL_BIG || fan.level == FanStatus.LEVEL_BIG_4
                    || fan.level == FanStatus.LEVEL_BIG_5) {
                sendLevel(FanStatus.LEVEL_EMPTY);
            } else {
                sendLevel(FanStatus.LEVEL_BIG);

            }
        }
    }

    @OnClick(R.id.tv_clean_lock)
    public void onTvCleanLockClicked() {

        //TODO 清洗锁定弹框逻辑
        LogUtils.e("20200302", "iv_clean_lock" + fan);
        final IRokiDialog cleanLockDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_08);
        cleanLockDialog.setContentImg(R.mipmap.ic_clean_lock_dialog);
        if (null !=fan){
            String dt = fan.getDt();
            if (IPlatRokiFamily._8236S.equals(dt)){
                cleanLockDialog.setContentText(R.string.dialog_fan_8236s_clean_text);
            }else {
                cleanLockDialog.setContentText(R.string.dialog_fan_5916s_clean_text);
            }
        }

        cleanLockDialog.setCanceledOnTouchOutside(false);
        cleanLockDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cleanLockDialog != null) {
                    cleanLockDialog.dismiss();

                }

                if (fan != null) {
                    fan.setFanStatus(FanStatus.CleanLock, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            LogUtils.e("20200302", "清洗锁定弹框逻辑onSuccess");
                            getActivity().finish();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.e("20200302", "清洗锁定弹框逻辑onFailure");
                        }
                    });
                }
            }
        });

        cleanLockDialog.setCancelBtn(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cleanLockDialog != null) {
                    cleanLockDialog.dismiss();
                }
            }
        });

        cleanLockDialog.show();

    }


    private void refreshLightIcon(boolean light) {
        if (light) {
            fanLight.setImageResource(R.mipmap.fan_min_light_select);
        } else {
            fanLight.setImageResource(R.mipmap.fan_min_light_normal);
        }

    }

    /**
     * @param light 灯光
     */
    private void sendLight(final boolean light) {

        fan.setFanLight(light, new VoidCallback() {
            @Override
            public void onSuccess() {
                refreshLightIcon(light);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 下发档位
     *
     * @param level 烟机档位
     */
    private void sendLevel(short level) {
        LogUtils.e("sendLevel", "level:" + level);
        fan.setFanLevel(level, new VoidCallback() {
            @Override
            public void onSuccess() {
                refreshLevelIcon(fan.level);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 刷新档位图标
     *
     * @param level 烟机档位
     */
    private void refreshLevelIcon(short level) {
        switch (level) {
            case FanStatus.LEVEL_SMALL:
            case FanStatus.LEVEL_SMALL_Two:

//                stopMiddleAnimation();
//                stopBigAnimation();
                fanSmall.setImageResource(R.mipmap.fan_min_small_select);
                fanMiddle.setImageResource(R.mipmap.fan_min_middle_normal);
                fanBig.setImageResource(R.mipmap.fan_min_big_normal);
//                startSmallAnimation();
                break;
            case FanStatus.LEVEL_MIDDLE:

//                stopSmallAnimation();
//                stopBigAnimation();
                fanMiddle.setImageResource(R.mipmap.fan_fan_middle_select);
                fanSmall.setImageResource(R.mipmap.fan_min_small_normal);
                fanBig.setImageResource(R.mipmap.fan_min_big_normal);
//                startMiddleAnimation();

                break;
            case FanStatus.LEVEL_BIG:
            case FanStatus.LEVEL_BIG_5:
            case FanStatus.LEVEL_BIG_4:

//                stopMiddleAnimation();
//                stopSmallAnimation();
                fanBig.setImageResource(R.mipmap.fan_min_big_select);
                fanSmall.setImageResource(R.mipmap.fan_min_small_normal);
                fanMiddle.setImageResource(R.mipmap.fan_min_middle_normal);
//                startBigAnimation();

                break;
            case FanStatus.LEVEL_EMPTY:
//                stopSmallAnimation();
//                stopMiddleAnimation();
//                stopBigAnimation();
                fanSmall.setImageResource(R.mipmap.fan_min_small_normal);
                fanMiddle.setImageResource(R.mipmap.fan_min_middle_normal);
                fanBig.setImageResource(R.mipmap.fan_min_big_normal);

                break;
            default:

                break;
        }
    }


    private void refreshLevelToast(short level) {
        switch (level) {

            case FanStatus.LEVEL_SMALL:
            case FanStatus.LEVEL_SMALL_Two:
                ToastUtils.show(R.string.dialog_small_level_text);
                break;
            case FanStatus.LEVEL_MIDDLE:

                ToastUtils.show(R.string.dialog_middle_level_text);
                break;
            case FanStatus.LEVEL_BIG:
            case FanStatus.LEVEL_BIG_4:
            case FanStatus.LEVEL_BIG_5:
                ToastUtils.show(R.string.dialog_big_level_text);
                break;
            case FanStatus.LEVEL_EMPTY:
                ToastUtils.show(R.string.dialog_close_level_text);
                break;
            default:

                break;

        }
    }

    /**
     * 开启爆炒动画
     */
    void startBigAnimation() {

        if (animationBig == null) {
            animationBig = AnimationUtils.loadAnimation(getContext(), R.anim.device_big_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            animationBig.setInterpolator(lin);
            fanBig.startAnimation(animationBig);
        }
    }

    /**
     * 开启中档动画
     */
    void startMiddleAnimation() {

        if (animationMiddle == null) {
            animationMiddle = AnimationUtils.loadAnimation(getContext(), R.anim.device_middle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            animationMiddle.setInterpolator(lin);
            fanMiddle.startAnimation(animationMiddle);
        }
    }

    /**
     * 开启小档动画
     */
    void startSmallAnimation() {

        if (animationSmall == null) {
            animationSmall = AnimationUtils.loadAnimation(getContext(), R.anim.device_small_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            animationSmall.setInterpolator(lin);
            fanSmall.startAnimation(animationSmall);
        }
    }


    /**
     * 关闭爆炒动画
     */
    void stopBigAnimation() {

        if (animationBig != null) {
            animationBig.cancel();
            fanBig.clearAnimation();
            animationBig = null;
        }
    }

    /**
     * 关闭中档动画
     */
    void stopMiddleAnimation() {
        if (animationMiddle != null) {
            animationMiddle.cancel();
            fanMiddle.clearAnimation();
            animationMiddle = null;
        }
    }

    /**
     * 关闭小档动画
     */
    void stopSmallAnimation() {

        if (animationSmall != null) {
            animationSmall.cancel();
            fanSmall.clearAnimation();
            animationSmall = null;
        }
    }

    private void shutdownCountdownClose() {
        LogUtils.e("20200318", "status:" + fan.status);
        if (FanStatus.DelayShutdown == fan.status && fan.presTurnOffRemainingTime > 0) {
            if (delayShutdownDialog == null) {
                delayShutdownDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
            }
            delayShutdownDialog.setContentText(R.string.dialog_fan_delay_shutdown_content);
            delayShutdownDialog.setCountDown(fan.presTurnOffRemainingTime);
            delayShutdownDialog.setOkBtn(R.string.dialog_off_device_text, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fan.setFanStatus(FanStatus.Off, null);
                }
            });
            delayShutdownDialog.setCancelBtn(R.string.dialog_cancel_off_device_text, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fan.setFanStatus(FanStatus.On, null);
                }
            });

            if (delayShutdownDialog != null) {
                if (!delayShutdownDialog.isShow()) {
                    delayShutdownDialog.show();
                }
            }
        } else {
            if (0 == fan.presTurnOffRemainingTime || -1 == fan.presTurnOffRemainingTime) {
                if (delayShutdownDialog != null && delayShutdownDialog.isShow()) {
                    delayShutdownDialog.dismiss();
                }
            }
        }
    }


    private void initDialog() {
        if (fan != null) {
            if (FanStatus.DelayShutdown == fan.status && fan.fanStoveLinkageVentilationRemainingTime != -1 &&
                    fan.fanStoveLinkageVentilationRemainingTime != 0) {

                IDevice stove = fan.getChildStove();
                if (stove instanceof Stove) {
                    if (((Stove) stove).leftHead.status == StoveStatus.Off && ((Stove) stove).rightHead.status == StoveStatus.StandyBy
                            || ((Stove) stove).leftHead.status == StoveStatus.StandyBy && ((Stove) stove).rightHead.status == StoveStatus.Off
                            || ((Stove) stove).leftHead.status == StoveStatus.Off && ((Stove) stove).rightHead.status == StoveStatus.Off) {
                        if (shutdownCountdownDialog == null) {
                            shutdownCountdownDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
                        }

                        shutdownCountdownDialog.setContentText(R.string.dialog_fan_stove_link_text);
                        shutdownCountdownDialog.setCountDown(fan.fanStoveLinkageVentilationRemainingTime);
                        shutdownCountdownDialog.setOkBtn(R.string.dialog_off_device_text, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shutdownCountdownDialog.dismiss();
                                shutdownCountdownDialog = null;
                                fan.setFanStatus(FanStatus.Off, null);
                            }
                        });
                        shutdownCountdownDialog.setCancelBtn(R.string.dialog_cancel_off_device_text, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shutdownCountdownDialog.dismiss();
                                shutdownCountdownDialog = null;
                                fan.setFanStatus(FanStatus.On, null);

                            }
                        });
                        if (shutdownCountdownDialog != null) {
                            if (!shutdownCountdownDialog.isShow()) {
                                shutdownCountdownDialog.show();
                            }
                        }
                    }
                }
            } else {
                if (shutdownCountdownDialog != null && shutdownCountdownDialog.isShow()) {
                    shutdownCountdownDialog.dismiss();
                    shutdownCountdownDialog = null;
                }
            }

            if (FanStatus.On == fan.status && fan.regularVentilationRemainingTime != -1 && fan.regularVentilationRemainingTime != 0) {
                if (regularVentilationDialog == null) {
                    regularVentilationDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_06);
                }
                regularVentilationDialog.setContentText(R.string.dialog_fan_regular_ventilation_text);
                regularVentilationDialog.setCountDown(fan.regularVentilationRemainingTime);
                regularVentilationDialog.setOkBtn(R.string.dialog_off_promptly_device_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        regularVentilationDialog.dismiss();
                        fan.setFanStatus(FanStatus.Off, null);
                    }
                });

                if (regularVentilationDialog != null) {
                    if (!regularVentilationDialog.isShow()) {
                        regularVentilationDialog.show();
                    }
                }
            } else {
                if (regularVentilationDialog != null && regularVentilationDialog.isShow()) {
                    regularVentilationDialog.dismiss();
                    regularVentilationDialog = null;
                }
            }
        }
    }

}
