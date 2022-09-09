package com.robam.rokipad.ui.page;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
import com.legent.VoidCallback3;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DevicePotOnlineSwitchEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.FormManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ResourcesUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.FanCleanNoticEvent;
import com.robam.common.events.FanLevelEvent;
import com.robam.common.events.FanLightEvent;
import com.robam.common.events.FanPowerEvent;
import com.robam.common.events.FanRegularlyRemindEvent;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.HomeIcUpdateEvent;
import com.robam.common.events.MainActivityFanExitEvent;
import com.robam.common.events.MainActivityRunEvent;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.HotOil;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Pot.PotStatus;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.pojos.dictionary.HotOilData;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.ui.FormKey;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.view.pot.IntelPotPadShowView;
import com.robam.rokipad.ui.view.pot.PotAddDeviceBgView;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.IClickModeListener;
import com.robam.rokipad.utils.IClickStoveSelectListener;
import com.robam.rokipad.view.FanModeShowView;
import com.robam.rokipad.view.StoveSelectView;
import com.robam.rokipad.view.TemperatureShowView;

import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Dell on 2018/12/19.
 */

public class FanDevicePage extends BasePage {

    @InjectView(R.id.recipe)
    ImageView recipe;
    @InjectView(R.id.recipe_all)
    TextView recipeAll;
    @InjectView(R.id.fan_light)
    ImageView fanLight;
    @InjectView(R.id.fan_small)
    ImageView fanSmall;
    @InjectView(R.id.fan_middle)
    ImageView fanMiddle;
    @InjectView(R.id.fan_big)
    ImageView fanBig;
    @InjectView(R.id.fan_touch)
    ImageView fanTouch;
    @InjectView(R.id.iv_pot_back)
    ImageView ivPotBack;
    @InjectView(R.id.fan_view)
    FrameLayout fanView;
    @InjectView(R.id.fan_frame)
    ImageView fanFrame;
    FanModeShowView fanModeShowView;
    StoveSelectView stoveSelectView;
    TemperatureShowView temperatureShowView;
    PotAddDeviceBgView potAddDeviceBgView;
    AbsFan fan;
    Pot[] pot;
    @InjectView(R.id.tv_close_smoke_detector)
    TextView mTvCloseSmokeDetector;
    //
    @InjectView(R.id.pot_temp_view)
    IntelPotPadShowView potTempView;
    @InjectView(R.id.ll_smoke_detector_desc)
    LinearLayout mLlSmokeDetectorDesc;
    @InjectView(R.id.iv_clean_lock)
    ImageView ivCleanLock;
    @InjectView(R.id.rl_smoke_detector)
    RelativeLayout rlSmokeDetector;
    //    @InjectView(R.id.textView2)
//    TextView textView2;
    @InjectView(R.id.tv_pot_status)
    TextView tvPotStatus;
    @InjectView(R.id.pot_intelli_device_curve)
    ImageView potIntelliDeviceCurve;
    @InjectView(R.id.fl_cooking_show)
    FrameLayout flCookingShow;
    @InjectView(R.id.fl_pot_temp)
    RelativeLayout flPotTemp;
    @InjectView(R.id.ll_fan_lay)
    LinearLayout llFanLay;
    @InjectView(R.id.rl_clean_lock_lay)
    RelativeLayout rlCleanLockLay;
    private AnimationDrawable frameAnim;
    private int modeShow;
    private int headId = 0;
    private HotOilData oilData = null;
    private List<HotOil> mHotOils;
    private HotOil mHotOil;
    private IRokiDialog goodOilTempDialog;
    private IRokiDialog alarmOilTempDialog;
    private IRokiDialog needCleanDialog;//油网清洗
    private IRokiDialog regularlyRemindDialog;//计时提醒
    private IRokiDialog shutdownCountdownDialog;
    private IRokiDialog delayShutdownDialog;
    private IRokiDialog regularVentilationDialog;
    VoidCallback3<Integer> callback3;
    boolean isDelayed = true;
    long DELAYED_TIME = 15000;


    @NonNull
    public static FanDevicePage newInstance() {
        return new FanDevicePage();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new MyHandler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    isDelayed = true;
                    break;
            }
        }
    };

    @Subscribe
    public void onEvent(FanPowerEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.fan.getID())) {
            return;
        }
//        refreshLevelIcon(event.fan.level);

        if (!event.power) {
            fanSmall.setImageResource(R.mipmap.fan_small_normal);
            fanMiddle.setImageResource(R.mipmap.fan_middle_normal);
            fanBig.setImageResource(R.mipmap.fan_big_normal);
            fanLight.setImageResource(R.mipmap.fan_light_normal);
            if (goodOilTempDialog != null && goodOilTempDialog.isShow()) {
                goodOilTempDialog.dismiss();
            }
            if (alarmOilTempDialog != null && alarmOilTempDialog.isShow()) {
                alarmOilTempDialog.dismiss();
            }
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
    public void onEvent(FanLightEvent event) {
        LogUtils.e("20201021", "FanLightEvent:" + event.fan.light);
        refreshLightIcon(event.fan.light);
    }

    @Subscribe
    public void onEvent(FanLevelEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.fan.getID())) {
            return;
        }
        refreshLevelIcon(event.level);
        refreshLevelToast(event.level);
    }

//    @Subscribe
//    public void onEvent(StoveCloseEvent event) {
//
//        LogUtils.e("20200727", "StoveCloseEvent:" + fan.level);
//        if (fan.level != FanStatus.LEVEL_EMPTY) {
//            initDialog();
//        }
//    }

    @Subscribe
    public void onEvent(FanCleanNoticEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.fan.getID())) {
            return;
        }
        //event.fan.setFanLevel(AbsFan.PowerLevel_0, null);
        LogUtils.e("20200302", "FanCleanNoticEvent");
        if (event.fan.clean) {
            if (needCleanDialog == null) {
                needCleanDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_07);
            }
            needCleanDialog.setContentText(R.string.dialog_fan_clean_oil);
            needCleanDialog.setCanceledOnTouchOutside(false);
            needCleanDialog.setOkBtn(R.string.dialog_roger_text, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (needCleanDialog != null && needCleanDialog.isShow()) {
                        needCleanDialog.dismiss();
                        needCleanDialog = null;
                        fan.restFanCleanTime(null);
                    }
                }
            });
            needCleanDialog.show();
        } else {
            if (needCleanDialog != null && needCleanDialog.isShow()) {
                needCleanDialog.dismiss();
            }
        }
    }


    @Subscribe
    public void onEvent(FanRegularlyRemindEvent event) {//定时提醒
        if (fan == null || !Objects.equal(fan.getID(), event.fan.getID())) {
            return;
        }
        if (regularlyRemindDialog == null) {
            regularlyRemindDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_07);
        }
        regularlyRemindDialog.setContentText(R.string.dialog_fan_regularly_remind);
        regularlyRemindDialog.setCanceledOnTouchOutside(false);
        regularlyRemindDialog.setOkBtn(R.string.dialog_roger_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regularlyRemindDialog != null && regularlyRemindDialog.isShow()) {
                    regularlyRemindDialog.dismiss();
                    regularlyRemindDialog = null;
                }
            }
        });
        regularlyRemindDialog.show();
    }

    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {

        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID())) {
            return;
        }
        fan = event.pojo;
        refreshLightIcon(fan.light);
        if (fan.fanFeelStatus == 1) {
            //String levelByName = FanAssistUtils.getDeviceLevelByName(cx, fan.level);
            //mTvLevel.setText(levelByName);
            refreshLevelIcon(fan.level);
            smokeDetectorStart();
        } else {
            refreshLevelIcon(fan.level);
            smokeDetectorStop();
        }
        short temperatureReportOne = fan.temperatureReportOne;
        short temperatureReportTwo = fan.temperatureReportTwo;
//        temperatureReportOne = 300;
        LogUtils.e("20200703", "temperatureReportOne:" + temperatureReportOne + " temperatureReportTwo:" + temperatureReportTwo);

//        if (SMOKE_DETECTOR_MAX_TEMP < temperatureReportOne) {
//            temperatureReportOne = SMOKE_DETECTOR_MAX_TEMP;
//        }
//        if (SMOKE_DETECTOR_MAX_TEMP < temperatureReportTwo) {
//            temperatureReportTwo = SMOKE_DETECTOR_MAX_TEMP;
//        }

        if (headId == 1) {
            temperatureShowView.setProgress(temperatureReportOne);
        } else if (headId == 2) {
            temperatureShowView.setProgress(temperatureReportTwo);
        }
        if (headId == 1 || headId == 2) {
            listenerTempDialog(temperatureReportOne, temperatureReportTwo);
        }
        shutdownCountdownClose();
        cleanLockStatus();
        if (fan.level != FanStatus.LEVEL_EMPTY) {
            initDialog();
        }
    }

    @Subscribe
    public void onEvent(MainActivityFanExitEvent exitEvent) {
        int fanViewVisibility = fanView.getVisibility();
        if (fanViewVisibility == View.INVISIBLE || fanViewVisibility == View.GONE) {
            flPotTemp.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEvent(MainActivityRunEvent exitEvent) {

        int fanViewVisibility = fanView.getVisibility();
        if (fanViewVisibility == View.INVISIBLE || fanViewVisibility == View.GONE) {
            flPotTemp.setVisibility(View.VISIBLE);
        } else {
            flPotTemp.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (event.pojo == null) {
            return;
        }
        pot[0] = event.pojo;
        LogUtils.e("20200703", "tempUp:" + pot[0].tempUp + " potStatus:" + pot[0].potStatus);

        if (null != fanModeShowView) {
            fanModeShowView.setPotLinkStatus(pot[0]);
        }
        switch (pot[0].potStatus) {
            case 1:
                tvPotStatus.setText(R.string.device_pot_await);
                break;
            case 2:
                tvPotStatus.setText(R.string.device_pot_alarm_E3);
                break;
            case 3:
                tvPotStatus.setText(R.string.device_pot_alarm_E2);
                break;
            case 4:
                tvPotStatus.setText(R.string.device_pot_alarm_E1);
                break;
        }
        updateCurve();
    }

    @Subscribe
    public void onEvent(DevicePotOnlineSwitchEvent event) {

        pot = Utils.getDefaultPot();
        LogUtils.e("20200731", "pot:" + pot[0]);
        fanModeShowView.setPotLinkStatus(pot[0]);
        fanView.removeAllViews();
        fanView.addView(fanModeShowView);
        if (null == pot[0]) {
            dealStoveEvent("back");
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

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics firebaseAnalytics = NewPadApp.getFireBaseAnalytics();
        firebaseAnalytics.setCurrentScreen(getActivity(), cx.getString(R.string.google_screen_fan_home), null);
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (pot == null || !Objects.equal(pot[0].getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            tvPotStatus.setText(R.string.device_pot_off_line);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fan_device_page, container, false);
        ButterKnife.inject(this, viewRoot);
        init();
        initDevices();
        healthyHotOilData();
        setTempSection();
        initDialog();
        return viewRoot;
    }

    private void initDevices() {
        fan = Utils.getDefaultFan();
        pot = Utils.getDefaultPot();
        if (null != fanModeShowView) {
            fanModeShowView.setPotLinkStatus(pot[0]);
        }
        if (null != pot[0]) {
            initPotTemp();
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
                regularVentilationDialog.setCanceledOnTouchOutside(false);
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

        if (goodOilTempDialog == null) {
            goodOilTempDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
            goodOilTempDialog.setContentImg(R.mipmap.ic_health_hot_oil_show);
            goodOilTempDialog.setContentText(R.string.device_hot_oil_good_text);
            goodOilTempDialog.setCanceledOnTouchOutside(false);
        }
        if (alarmOilTempDialog == null) {

            alarmOilTempDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
            alarmOilTempDialog.setContentImg(R.mipmap.ic_health_hot_oil_alarm);
            alarmOilTempDialog.setContentText(R.string.device_hot_oil_alarm_text);
            alarmOilTempDialog.setCanceledOnTouchOutside(false);
        }

    }

    //清洗锁定部分
    private void cleanLockStatus() {
        if (fan.status == FanStatus.CleanLock) {
            llFanLay.setVisibility(View.GONE);
            rlCleanLockLay.setVisibility(View.VISIBLE);
        } else {
            llFanLay.setVisibility(View.VISIBLE);
            rlCleanLockLay.setVisibility(View.GONE);
        }
    }

    private void setTempSection() {
        if (mHotOils == null || mHotOils.size() == 0) return;
        for (int i = 0; i < mHotOils.size(); i++) {
            int model = mHotOils.get(i).model;
            if (modeShow == model) {
                mHotOil = mHotOils.get(i);
                int health_temp = mHotOil.health_temp_high;
                temperatureShowView.setTotalProgress(health_temp);
                temperatureShowView.setModelByTempInterval(model, mHotOils);
            }
        }
    }

    private void healthyHotOilData() {

        String hotOilData = ResourcesUtils.raw2String(R.raw.healthy_hot_oil);
        try {
            oilData = JsonUtils.json2Pojo(hotOilData, HotOilData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Preconditions.checkNotNull(oilData, "加载 app oilData 失败");
        mHotOils = oilData.hotOils;
    }

    private void updateCurve() {

        if (callback3 == null) {
            callback3 = new VoidCallback3<Integer>() {
                @Override
                public void onCompleted(final Integer integer) {
                }
            };
        }
        if (pot[0].potStatus == PotStatus.lowBatteryStatus
                || pot[0].potStatus == PotStatus.tempSensorStatus) return;
        if (pot[0].tempUp <= 50 && pot[0].tempUp >= 0) {
            potIntelliDeviceCurve.setImageResource(R.mipmap.ic_potcurve_grey);
            potTempView.setModel(IntelPotPadShowView.IntelPotShow_Model.StandBy, null);
            tvPotStatus.setText(R.string.device_pot_await);
        } else if (pot[0].tempUp > 50 && pot[0].tempUp < 240) {
            potIntelliDeviceCurve.setImageResource(R.mipmap.ic_potcurve_blue);
            potTempView.setModel(IntelPotPadShowView.IntelPotShow_Model.ShowTemp, callback3, pot[0].tempUp + "");
            tvPotStatus.setText("");
        } else if (pot[0].tempUp > 240 && pot[0].tempUp <= 250) {
            potIntelliDeviceCurve.setImageResource(R.mipmap.ic_potcurve_yellow);
            potTempView.setModel(IntelPotPadShowView.IntelPotShow_Model.ShowTemp, callback3, pot[0].tempUp + "");
            tvPotStatus.setText(R.string.device_pot_overheating);
        } else if (pot[0].tempUp > 250) {
            potIntelliDeviceCurve.setImageResource(R.mipmap.ic_potcurve_red);
            potTempView.setModel(IntelPotPadShowView.IntelPotShow_Model.ShowTemp, callback3, pot[0].tempUp + "");
            tvPotStatus.setText(R.string.device_pot_temp_overheating);
        } else {

        }

    }

    private void init() {
        Glide.with(getContext()).load(R.mipmap.fan_anim_frame).asGif().into(fanFrame);
        fanModeShowView = new FanModeShowView(cx);
        fanView.addView(fanModeShowView);
        stoveSelectView = new StoveSelectView(cx);
        temperatureShowView = new TemperatureShowView(cx);
        potAddDeviceBgView = new PotAddDeviceBgView(cx);
        flPotTemp.setVisibility(View.GONE);
        fanModeShowView.setOnClickModeListener(new IClickModeListener() {

            @Override
            public void onClickModeListener(int mode) {
                modeShow = mode;
                LogUtils.e("20200703", "mode：" + mode);
                if (mode == 4) {
                    LogUtils.e("20200703", "pot：" + pot);
                    if (null == pot[0]) {
                        LogUtils.e("20200703", "weilian");
                        fanView.removeAllViews();
                        fanView.addView(potAddDeviceBgView);
                        flPotTemp.setVisibility(View.GONE);
                        fanView.setVisibility(View.VISIBLE);
                    }
                } else if (mode == 5) {
                    headId = 0;
                    flPotTemp.setVisibility(View.VISIBLE);
                    fanView.setVisibility(View.GONE);
                } else {
                    fanView.removeAllViews();
                    fanView.addView(stoveSelectView);
                    flPotTemp.setVisibility(View.GONE);
                    fanView.setVisibility(View.VISIBLE);
                    setTempSection();
                }
            }

            @Override
            public void onClickModeListener(String content) {

            }
        });
        stoveSelectView.setOnClickStoveSelectListener(new IClickStoveSelectListener() {
            @Override
            public void clickStoveSelectListener(String tag) {
                dealStoveEvent(tag);
            }
        });
        temperatureShowView.setClickBackListener(new IClickStoveSelectListener() {

            @Override
            public void clickStoveSelectListener(String tag) {
                dealStoveEvent(tag);
            }
        });

        potAddDeviceBgView.setClickBackListener(new IClickStoveSelectListener() {
            @Override
            public void clickStoveSelectListener(String tag) {
                dealStoveEvent(tag);
            }
        });

        ivPotBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealStoveEvent("back");
            }
        });

    }

    private void dealStoveEvent(String tag) {
        LogUtils.e("20200922", "tag:" + tag);
        switch (tag) {
            case "back":
                fanView.removeAllViews();
                fanView.addView(fanModeShowView);
                flPotTemp.setVisibility(View.GONE);
                fanView.setVisibility(View.VISIBLE);
                headId = 0;
                break;
            case "left":
                fanView.removeAllViews();
                fanView.addView(temperatureShowView);
                temperatureShowView.setStoveHeadText("left");
                headId = 1;
                break;
            case "right":
                fanView.removeAllViews();
                fanView.addView(temperatureShowView);
                temperatureShowView.setStoveHeadText("right");
                headId = 2;
                break;
            case "return":
                fanView.removeAllViews();
                fanView.addView(fanModeShowView);
                headId = 0;
                break;
            default:
                break;
        }
    }


    @OnClick({R.id.recipe, R.id.fan_light, R.id.fan_small, R.id.fan_middle,
            R.id.fan_big, R.id.fan_touch, R.id.fan_frame, R.id.fl_cooking_show,
            R.id.iv_clean_lock, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recipe:

                EventUtils.postEvent(new MainActivityFanExitEvent());
                UIService instance = UIService.getInstance();
                if (null != instance) {
                    FormManager top = instance.getTop();
                    if (null != top) {
                        String formKey = top.getFormKey();
                        if (FormKey.MainForm.equals(formKey)) {
                            UIService.getInstance().postPage(PageKey.RecipeAll);
                            EventUtils.postEvent(new HomeIcUpdateEvent("NO_HOME"));
                        }
                    }
                }
                break;
            case R.id.fan_light:
                if (fan.light) {
                    sendLight(false);
                } else {
                    sendLight(true);
                }
                break;
            case R.id.fan_small:
                if (fan.level == FanStatus.LEVEL_SMALL || fan.level == FanStatus.LEVEL_SMALL_Two) {
                    sendLevel(FanStatus.LEVEL_EMPTY);
                } else {
                    sendLevel(FanStatus.LEVEL_SMALL);
                }
                break;
            // 代表小档，下面的相同
            case R.id.fan_middle:
                if (fan.level == FanStatus.LEVEL_MIDDLE) {
                    sendLevel(FanStatus.LEVEL_EMPTY);
                } else {
                    sendLevel(FanStatus.LEVEL_MIDDLE);
                }
                break;
            case R.id.fan_big:
                if (fan.level == FanStatus.LEVEL_BIG) {
                    sendLevel(FanStatus.LEVEL_EMPTY);
                } else {
                    sendLevel(FanStatus.LEVEL_BIG);
                }
                break;
            case R.id.fan_touch:
                smokeDetector((short) 1);
                break;
            case R.id.fan_frame:
                smokeDetector((short) 0);
                break;
            case R.id.fl_cooking_show:
                UIService.getInstance().postPage(PageKey.PotCookingTips);
                break;
            case R.id.tv_cancel:
                if (fan != null) {
                    fan.setFanStatus(FanStatus.On, null);
                }
                break;
            case R.id.iv_clean_lock:
                LogUtils.e("20200302", "iv_clean_lock" + fan);
                final IRokiDialog cleanLockDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_08);
                cleanLockDialog.setContentImg(R.mipmap.ic_clean_lock_dialog);
                cleanLockDialog.setContentText(R.string.dialog_fan_8236s_clean_text);
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
                break;
            default:
                break;
        }
    }

    private void initPotTemp() {

        callback3 = new VoidCallback3<Integer>() {
            @Override
            public void onCompleted(final Integer integer) {

            }
        };
        if (!pot[0].isConnected()) {
            potTempView.initTimer(false);
            return;
        }
        if (pot[0].potStatus == PotStatus.dryAlarmStatus || pot[0].potStatus == PotStatus.lowBatteryStatus
                || pot[0].potStatus == PotStatus.tempSensorStatus) return;
        if (pot[0].tempUp <= 50 && pot[0].tempUp > 0) {
            potTempView.setModel(IntelPotPadShowView.IntelPotShow_Model.StandBy, null);
        } else {
            potTempView.setModel(IntelPotPadShowView.IntelPotShow_Model.ShowTemp, callback3, pot[0].tempUp + "");
        }
    }

    /**
     * 智能烟感
     *
     * @param value 0关 1 开
     */
    private void smokeDetector(final short value) {

        LogUtils.e("20190429", "smokeDetector:" + value);
        FanStatusComposite fanStatusComposite = new FanStatusComposite();
        fanStatusComposite.FanFeelPower = value;
        if (fan == null) return;
        fan.setFanSmartSmoke(fanStatusComposite, (short) 1, new VoidCallback() {
            @Override
            public void onSuccess() {
                fan.fanFeelStatus = value;
                LogUtils.e("20190425", "fanFeelStatus:" + fan.fanFeelStatus);
                if (fan.fanFeelStatus != 1) {
                    smokeDetectorStop();
                } else {
                    smokeDetectorStart();
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void smokeDetectorStop() {
//        stop();
        fanTouch.setVisibility(View.VISIBLE);
//        mTvSmokeDetector.setVisibility(View.VISIBLE);
        fanFrame.setVisibility(View.GONE);
        mLlSmokeDetectorDesc.setVisibility(View.GONE);
    }

    private void smokeDetectorStart() {

//        start();
        fanTouch.setVisibility(View.GONE);
        fanFrame.setVisibility(View.VISIBLE);
        mLlSmokeDetectorDesc.setVisibility(View.VISIBLE);
//        mTvSmokeDetector.setVisibility(View.GONE);
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

    private void refreshLightIcon(boolean light) {
        if (light) {
            fanLight.setImageResource(R.mipmap.fan_light_select);
        } else {
            fanLight.setImageResource(R.mipmap.fan_light_normal);
        }

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
                fanSmall.setImageResource(R.mipmap.fan_small_select);
                fanMiddle.setImageResource(R.mipmap.fan_middle_normal);
                fanBig.setImageResource(R.mipmap.fan_big_normal);
                break;
            case FanStatus.LEVEL_MIDDLE:
                fanMiddle.setImageResource(R.mipmap.fan_middle_select);
                fanSmall.setImageResource(R.mipmap.fan_small_normal);
                fanBig.setImageResource(R.mipmap.fan_big_normal);
                break;
            case FanStatus.LEVEL_BIG:
                fanBig.setImageResource(R.mipmap.fan_big_select);
                fanSmall.setImageResource(R.mipmap.fan_small_normal);
                fanMiddle.setImageResource(R.mipmap.fan_middle_normal);
                break;
            default:
                if (level == FanStatus.LEVEL_EMPTY) {
                    fanSmall.setImageResource(R.mipmap.fan_small_normal);
                    fanMiddle.setImageResource(R.mipmap.fan_middle_normal);
                    fanBig.setImageResource(R.mipmap.fan_big_normal);
//                    stop();
                    fanTouch.setVisibility(View.VISIBLE);
//                    mTvSmokeDetector.setVisibility(View.VISIBLE);
                    fanFrame.setVisibility(View.GONE);
                    mLlSmokeDetectorDesc.setVisibility(View.GONE);
                }
                if (fan.fanFeelStatus == 1) {
                    fanSmall.setImageResource(R.mipmap.fan_small_normal);
                    fanMiddle.setImageResource(R.mipmap.fan_middle_normal);
                    fanBig.setImageResource(R.mipmap.fan_big_normal);
                }
                break;
        }

    }

    //监听弹框
    private void listenerTempDialog(short temperatureReportOne, short temperatureReportTwo) {
        LogUtils.e("20200706", "headId:" + headId + " :temperatureReportOne:" + temperatureReportOne
                + " temperatureReportTwo:" + temperatureReportTwo + " mHotOil.health_temp_low:" + mHotOil.health_temp_low
                + " mHotOil.health_temp_high:" + mHotOil.health_temp_high);

        if (isDelayed) {

            if (goodOilTempDialog != null) {
                if (headId == 1) {
                    if (temperatureReportOne >= mHotOil.health_temp_low && temperatureReportOne <= mHotOil.health_temp_high) {
                        goodOilTempDialog.setOkBtn(R.string.iamknown, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LogUtils.e("20200706", "dismiss");
                                handlerPostDelayed();
                                goodOilTempDialog.dismiss();
                            }
                        });
                        if (!goodOilTempDialog.isShow()) {
                            LogUtils.e("20200706", "headId:" + headId + " :goodOilTempDialog show:");
                            goodOilTempDialog.show();
                        }
                    } else if (temperatureReportOne < mHotOil.health_temp_low || temperatureReportOne > mHotOil.health_temp_high) {
                        if (goodOilTempDialog.isShow()) {
                            goodOilTempDialog.dismiss();
                        }
                    }
                } else if (headId == 2) {
                    if (goodOilTempDialog != null) {
                        if (temperatureReportTwo >= mHotOil.health_temp_low && temperatureReportTwo <= mHotOil.health_temp_high) {
                            goodOilTempDialog.setOkBtn(R.string.iamknown, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    handlerPostDelayed();
                                    goodOilTempDialog.dismiss();
                                }
                            });
                            if (!goodOilTempDialog.isShow()) {
                                LogUtils.e("20200706", "headId:" + headId + " :goodOilTempDialog show:");
                                goodOilTempDialog.show();
                            }

                        } else if (temperatureReportTwo < mHotOil.health_temp_low || temperatureReportTwo > mHotOil.health_temp_high) {
                            if (goodOilTempDialog.isShow()) {
                                goodOilTempDialog.dismiss();
                            }

                        }
                    }

                }
            }


            if (alarmOilTempDialog != null) {

                LogUtils.e("20200706", "headId:" + headId + " :warm_temp:" + mHotOil.warm_temp);
                if (headId == 1) {
                    if (temperatureReportOne > mHotOil.warm_temp) {
                        alarmOilTempDialog.setOkBtn(R.string.iamknown, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlerPostDelayed();
                                alarmOilTempDialog.dismiss();
                            }
                        });
                        if (!alarmOilTempDialog.isShow()) {
                            LogUtils.e("20200706", "headId:" + headId + " :alarmOilTempDialog show:");
                            alarmOilTempDialog.show();
                        }
                    } else if (temperatureReportOne < mHotOil.warm_temp) {
                        if (alarmOilTempDialog.isShow()) {
                            alarmOilTempDialog.dismiss();
                        }


                    }


                } else if (headId == 2) {
                    if (temperatureReportTwo > mHotOil.warm_temp) {
                        alarmOilTempDialog.setOkBtn(R.string.iamknown, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlerPostDelayed();
                                alarmOilTempDialog.dismiss();
                            }
                        });
                        if (!alarmOilTempDialog.isShow()) {
                            LogUtils.e("20200706", "headId:" + headId + " :alarmOilTempDialog show:");
                            alarmOilTempDialog.show();
                        }
                    } else if (temperatureReportTwo < mHotOil.warm_temp) {

                        alarmOilTempDialog.dismiss();

                    }
                }

            }

        }


    }


    /**
     * handler_postDelayed 方法实现
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Message msg = mHandler.obtainMessage();
            msg.what = 10;
            mHandler.sendMessage(msg);
        }

    };

    private void handlerPostDelayed() {
        isDelayed = false;
        mHandler.postDelayed(mRunnable, DELAYED_TIME);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

        if (null != mRunnable) {
            mRunnable = null;
        }
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }

    }


}
