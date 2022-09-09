package com.robam.rokipad.ui.page;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.VoidCallback3;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DevicePotOnlineSwitchEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.FormManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.Fan3DGestureEvent;
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
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Pot.PotStatus;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.ui.FormKey;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.view.pot.IntelPotPadShowView;
import com.robam.rokipad.ui.view.pot.PotAddDeviceView;
import com.robam.rokipad.ui.view.pot.PotImgPosterView;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.ToolUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 烟机5616S主页面
 */
public class Fan5916SDevicePage extends BasePage {

    AbsFan fan;
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
    @InjectView(R.id.iv_3d_gesture_switch)
    ImageView iv3dGestureSwitch;
    PotImgPosterView potImgPosterView;
    PotAddDeviceView potAddDeviceView;
    @InjectView(R.id.fl_card_poster)
    FrameLayout flCardPoster;
    @InjectView(R.id.ll_card_add)
    LinearLayout llCardAdd;
    @InjectView(R.id.fl_main_container)
    FrameLayout flMainContainer;
    AnimatorSet mRightOutSet;
    AnimatorSet mLeftInSet;
    @InjectView(R.id.pot_temp_view)
    IntelPotPadShowView potTempView;
    @InjectView(R.id.fl_pot_temp)
    RelativeLayout flPotTemp;
    @InjectView(R.id.iv_light_anim)
    ImageView ivLightAnim;
    @InjectView(R.id.ll_wave)
    LinearLayout llWave;
    @InjectView(R.id.fl_3d_explain)
    FrameLayout fl3dExplain;
    @InjectView(R.id.fl_light_anim)
    FrameLayout flLightAnim;
    @InjectView(R.id.tv_pot_status)
    TextView tvPotStatus;
    @InjectView(R.id.pot_intelli_device_curve)
    ImageView potIntelliDeviceCurve;
    @InjectView(R.id.fl_cooking_show)
    FrameLayout flCookingShow;
    @InjectView(R.id.iv_clean_lock)
    ImageView ivCleanLock;
    @InjectView(R.id.ll_hover_startup)
    LinearLayout llHoverStartup;
    @InjectView(R.id.ll_left_right_wave)
    LinearLayout llLeftRightWave;
    @InjectView(R.id.ll_3d_gesture_close)
    LinearLayout ll3dGestureClose;
    @InjectView(R.id.iv_content)
    ImageView ivContent;
    @InjectView(R.id.tv_content_text)
    TextView tvContentText;
    @InjectView(R.id.tv_cancel)
    TextView tvCancel;
    //    @InjectView(R.id.tv_close)
//    TextView tvClose;
    @InjectView(R.id.tv_3d_wave_close)
    TextView tv3dWaveClose;
    @InjectView(R.id.ll_fan_lay)
    LinearLayout llFanLay;
    @InjectView(R.id.rl_clean_lock_lay)
    RelativeLayout rlCleanLockLay;
    @InjectView(R.id.tv_3d_wave)
    TextView tv3dWave;
    @InjectView(R.id.fl_3d_explain_not_wave)
    FrameLayout fl3dExplainNotWave;
    private boolean mIsShowBack;
    private Pot[] pot;
    Animation animationBig;
    Animation animationMiddle;
    Animation animationSmall;
    private Timer timer;
    private TimerTask timerTask;
    private int isinit = 0;
    private IRokiDialog shutdownCountdownDialog;
    private IRokiDialog delayShutdownDialog;
    private IRokiDialog regularVentilationDialog;
    private IRokiDialog anim3DDialog;
    VoidCallback3<Integer> callback3;
    private IRokiDialog needCleanDialog;//油网清洗
    private IRokiDialog regularlyRemindDialog;//计时提醒
    private short gesture;
    boolean isSmallAnim = false;
    boolean isSmallMiddle = false;
    boolean isSmallBig = false;


    @NonNull
    public static Fan5916SDevicePage newInstance() {
        return new Fan5916SDevicePage();
    }

    @SuppressLint("HandlerLeak")
    MyHandler handler = new MyHandler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (fan != null) {
                        fan.addPotDevice();
                    }
                    break;
                case 1:
                    onShowTemStatus(msg.arg1);
                    break;
            }
        }
    };

    private void onShowTemStatus(int arg1) {
    }


    @Subscribe
    public void onEvent(MainActivityFanExitEvent exitEvent) {
        flPotTemp.setVisibility(View.GONE);
    }

    @Subscribe
    public void onEvent(MainActivityRunEvent exitEvent) {
        Pot[] defaultPot = Utils.getDefaultPot();
        if (null != defaultPot[0]) {
            flPotTemp.setVisibility(View.VISIBLE);
        }
    }


    @Subscribe
    public void onEvent(FanPowerEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.fan.getID())) {
            return;
        }
        LogUtils.e("20200306", "event.power:" + event.power);
//        refreshLevelIcon(event.fan.level);
        if (!event.power) {
            initHomeUiListener("init");
            fanSmall.setImageResource(R.mipmap.fan_small_normal);
            fanMiddle.setImageResource(R.mipmap.fan_middle_normal);
            fanBig.setImageResource(R.mipmap.fan_big_normal);
            fanLight.setImageResource(R.mipmap.fan_light_normal);
            if (anim3DDialog != null && anim3DDialog.isShow()) {
                anim3DDialog.dismiss();
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
    public void onEvent(FanLevelEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.fan.getID())) {
            return;
        }
//        initHomeUiListener("level");
        LogUtils.e("20200306", "FanLevelEvent:" + event.fan.level);
        refreshLevelIcon(event.level);
        refreshLevelToast(event.level);
    }

    @Subscribe
    public void onEvent(FanLightEvent event) {
        refreshLightIcon(event.fan.light);
    }

    @Subscribe
    public void onEvent(Fan3DGestureEvent event) {
        gesture = event.gesture;
        LogUtils.e("20200226", "gesture:" + gesture);
        initHomeUiListener("3d");
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
    public void onEvent(DevicePotOnlineSwitchEvent event) {
        Pot[] pot = Utils.getDefaultPot();
        initView(pot[0]);
    }

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (event.pojo == null) {
            return;
        }
        pot[0] = event.pojo;
        LogUtils.e("20190831", "PotStatusChangedEvent:" + pot[0].tempUp);

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
    public void onEvent(FanStatusChangedEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID())) {
            return;
        }
        fan = event.pojo;
        LogUtils.e("20200226", "fan:" + fan.level + " state:" + fan.status);
        if (FanStatus.LEVEL_EMPTY == fan.level && isinit < 3) {//无档位和第一次初始进入
            isinit++;
            refreshLevelIcon(fan.level);
        }
        initHomeUiListener("init");
        refreshLightIcon(fan.light);
        refreshLevelIcon(fan.level);
        shutdownCountdownClose();
        cleanLockStatus();
        if (fan.level != FanStatus.LEVEL_EMPTY) {
            initDialog();
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
        View viewRoot = inflater.inflate(R.layout.fan_5916s_device_page, container, false);
        ButterKnife.inject(this, viewRoot);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initDevices();
        initView(pot[0]);
        initHomeUiListener("init");
        initDialog();
        setAnimators(); // 设置动画
        setCameraDistance(); // 设置镜头距离
        initListener();
    }

    private void initHomeUiListener(String flag) {

        if (fan != null) {
            if (null == llWave) return;
            if (flag.equals("init") || flag.equals("level") || flag.equals("Resume")) {
                fan.getSmartConfig(new Callback<SmartParams>() {
                    @Override
                    public void onSuccess(SmartParams smartParams) {
                        LogUtils.e("20200226", "111smartParams:" + smartParams.toString());
                        short gestureRecognitionSwitch = smartParams.gestureRecognitionSwitch;
                        if (0 == gestureRecognitionSwitch && fan.level == FanStatus.LEVEL_EMPTY) {
                            llWave.setVisibility(View.VISIBLE);
                            flLightAnim.setVisibility(View.GONE);
                            llHoverStartup.setVisibility(View.GONE);
                            llLeftRightWave.setVisibility(View.GONE);
                            ll3dGestureClose.setVisibility(View.VISIBLE);
                            iv3dGestureSwitch.setImageResource(R.mipmap.icon_home_gesture_close);

                        } else if (0 == gestureRecognitionSwitch && fan.level != FanStatus.LEVEL_EMPTY) {
                            llWave.setVisibility(View.GONE);
                            flLightAnim.setVisibility(View.VISIBLE);
                            llHoverStartup.setVisibility(View.GONE);
                            llLeftRightWave.setVisibility(View.GONE);
                            ll3dGestureClose.setVisibility(View.GONE);
                            fl3dExplain.setVisibility(View.GONE);
                            fl3dExplainNotWave.setVisibility(View.VISIBLE);

                        } else if (1 == gestureRecognitionSwitch && fan.level == FanStatus.LEVEL_EMPTY) {

                            llWave.setVisibility(View.VISIBLE);
                            flLightAnim.setVisibility(View.GONE);
                            llHoverStartup.setVisibility(View.VISIBLE);
                            llLeftRightWave.setVisibility(View.VISIBLE);
                            ll3dGestureClose.setVisibility(View.GONE);
                            iv3dGestureSwitch.setImageResource(R.mipmap.icon_home_gesture);

                        } else if (1 == gestureRecognitionSwitch && fan.level != FanStatus.LEVEL_EMPTY) {
                            llWave.setVisibility(View.GONE);
                            flLightAnim.setVisibility(View.VISIBLE);
                            llHoverStartup.setVisibility(View.VISIBLE);
                            llLeftRightWave.setVisibility(View.VISIBLE);
                            ll3dGestureClose.setVisibility(View.GONE);
                            fl3dExplain.setVisibility(View.VISIBLE);
                            fl3dExplainNotWave.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.e("20200226", " 111 t:" + t.toString());
                    }
                });
            } else {
                if (0 == gesture && fan.level == FanStatus.LEVEL_EMPTY) {
                    llWave.setVisibility(View.VISIBLE);
                    flLightAnim.setVisibility(View.GONE);
                    llHoverStartup.setVisibility(View.GONE);
                    llLeftRightWave.setVisibility(View.GONE);
                    ll3dGestureClose.setVisibility(View.VISIBLE);
                    iv3dGestureSwitch.setImageResource(R.mipmap.icon_home_gesture_close);


                } else if (0 == gesture && fan.level != FanStatus.LEVEL_EMPTY) {
                    llWave.setVisibility(View.GONE);
                    flLightAnim.setVisibility(View.VISIBLE);
                    llHoverStartup.setVisibility(View.GONE);
                    llLeftRightWave.setVisibility(View.GONE);
                    ll3dGestureClose.setVisibility(View.GONE);
                    fl3dExplain.setVisibility(View.GONE);
                    fl3dExplainNotWave.setVisibility(View.VISIBLE);

                } else if (1 == gesture && fan.level == FanStatus.LEVEL_EMPTY) {
                    llWave.setVisibility(View.VISIBLE);
                    flLightAnim.setVisibility(View.GONE);
                    llHoverStartup.setVisibility(View.VISIBLE);
                    llLeftRightWave.setVisibility(View.VISIBLE);
                    ll3dGestureClose.setVisibility(View.GONE);
                    iv3dGestureSwitch.setImageResource(R.mipmap.icon_home_gesture);

                } else if (1 == gesture && fan.level != FanStatus.LEVEL_EMPTY) {
                    llWave.setVisibility(View.GONE);
                    flLightAnim.setVisibility(View.VISIBLE);
                    llHoverStartup.setVisibility(View.VISIBLE);
                    llLeftRightWave.setVisibility(View.VISIBLE);
                    ll3dGestureClose.setVisibility(View.GONE);
                    fl3dExplain.setVisibility(View.VISIBLE);
                    fl3dExplainNotWave.setVisibility(View.GONE);

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

    private void initDevices() {
        fan = Utils.getDefaultFan();
        pot = Utils.getDefaultPot();
    }

    private void initView(Pot pot) {
        LogUtils.e("20200723", "initView");
        potImgPosterView = new PotImgPosterView(cx);
        potAddDeviceView = new PotAddDeviceView(cx);

        LogUtils.e("20190816", "pot:" + pot);
        if (null == pot) {
            flCardPoster.setVisibility(View.VISIBLE);
            llCardAdd.setVisibility(View.VISIBLE);
            flPotTemp.setVisibility(View.GONE);
        } else {
//            if (pot.isConnected()){
            closeAddDeviceTask();
            flCardPoster.setVisibility(View.GONE);
            llCardAdd.setVisibility(View.GONE);
            flPotTemp.setVisibility(View.VISIBLE);
            initPotTemp();

//            }else {
//                fan.delPotDevice(pot.getGuid().getGuid(),true,null);
//                flCardPoster.setVisibility(View.VISIBLE);
//                llCardAdd.setVisibility(View.VISIBLE);
//                flPotTemp.setVisibility(View.INVISIBLE);
//            }

        }

    }

    private void initPotTemp() {
        LogUtils.e("20200723", "initPotTemp");
        callback3 = new VoidCallback3<Integer>() {
            @Override
            public void onCompleted(final Integer integer) {
//                Message msg = handler.obtainMessage();
//                msg.what = 1;
//                msg.arg1 = integer;
//                handler.sendMessage(msg);
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

    private void updateCurve() {
        if (pot[0].potStatus == PotStatus.dryAlarmStatus || pot[0].potStatus == PotStatus.lowBatteryStatus
                || pot[0].potStatus == PotStatus.tempSensorStatus) return;

        if (pot[0].tempUp <= 50 && pot[0].tempUp >= 0) {
            potIntelliDeviceCurve.setImageResource(R.mipmap.ic_potcurve_grey);
            potTempView.setModel(IntelPotPadShowView.IntelPotShow_Model.StandBy, null);
            tvPotStatus.setText(R.string.device_pot_await);
        } else if (pot[0].tempUp > 50 && pot[0].tempUp < 240) {
            potIntelliDeviceCurve.setImageResource(R.mipmap.ic_potcurve_blue);
            potTempView.setModel(IntelPotPadShowView.IntelPotShow_Model.ShowTemp, callback3, pot[0].tempUp + "");
            tvPotStatus.setText("");
        } else if (pot[0].tempUp > 240 && pot[0].tempUp < 250) {
            potIntelliDeviceCurve.setImageResource(R.mipmap.ic_potcurve_yellow);
            potTempView.setModel(IntelPotPadShowView.IntelPotShow_Model.ShowTemp, callback3, pot[0].tempUp + "");
            tvPotStatus.setText(R.string.device_pot_overheating);
        } else if (pot[0].tempUp > 250) {
            potIntelliDeviceCurve.setImageResource(R.mipmap.ic_potcurve_red);
            potTempView.setModel(IntelPotPadShowView.IntelPotShow_Model.ShowTemp, callback3, pot[0].tempUp + "");
            tvPotStatus.setText(R.string.device_pot_temp_overheating);
        }

    }

    // 设置动画
    @SuppressLint("ResourceType")
    private void setAnimators() {
        try {
            mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(cx, R.anim.anim_out);
            mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(cx, R.anim.anim_in);

            // 设置点击事件
            mRightOutSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    flMainContainer.setClickable(false);
                }
            });
            mLeftInSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    flMainContainer.setClickable(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 改变视角距离, 贴近屏幕
    private void setCameraDistance() {
        int distance = 1600;
        float scale = getResources().getDisplayMetrics().density * distance;
        flCardPoster.setCameraDistance(scale);
        llCardAdd.setCameraDistance(scale);
    }

    private void initListener() {

        flMainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 正面朝上
                if (!mIsShowBack) {
                    mRightOutSet.setTarget(flCardPoster);
                    mLeftInSet.setTarget(llCardAdd);
                    mRightOutSet.start();
                    mLeftInSet.start();
                    mIsShowBack = true;
                    initAddDeviceTask();
                } else { // 背面朝上
                    mRightOutSet.setTarget(llCardAdd);
                    mLeftInSet.setTarget(flCardPoster);
                    mRightOutSet.start();
                    mLeftInSet.start();
                    mIsShowBack = false;
                    closeAddDeviceTask();
                }
            }
        });
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

    @OnClick({R.id.recipe, R.id.fan_light, R.id.fan_small, R.id.fan_middle,
            R.id.fan_big, R.id.fl_3d_explain, R.id.fl_cooking_show, R.id.iv_clean_lock,
            R.id.tv_cancel, R.id.tv_smart_set, R.id.tv_3d_wave_close})
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
                if (fan != null) {
                    if (fan.light) {
                        sendLight(false);
                    } else {
                        sendLight(true);
                    }
                }
                break;
            case R.id.fan_small:
                LogUtils.e("sendLevel", "fan:" + fan);
                if (fan != null) {
                    if (fan.level == FanStatus.LEVEL_SMALL || fan.level == FanStatus.LEVEL_SMALL_Two) {
                        sendLevel(FanStatus.LEVEL_EMPTY);
                    } else {
                        sendLevel(FanStatus.LEVEL_SMALL);

                    }
                }
                break;
            // 代表小档，下面的相同
            case R.id.fan_middle:
                if (fan != null) {
                    if (fan.level == FanStatus.LEVEL_MIDDLE) {
                        sendLevel(FanStatus.LEVEL_EMPTY);
                    } else {
                        sendLevel(FanStatus.LEVEL_MIDDLE);

                    }
                }
                break;
            case R.id.fan_big:
                if (fan != null) {
                    if (fan.level == FanStatus.LEVEL_BIG || fan.level == FanStatus.LEVEL_BIG_4
                            || fan.level == FanStatus.LEVEL_BIG_5) {
                        sendLevel(FanStatus.LEVEL_EMPTY);

                    } else {
                        sendLevel(FanStatus.LEVEL_BIG);
                    }
                }
                break;
            case R.id.fl_3d_explain:
                //启动3d动画弹框
                anim3DDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_05);
                anim3DDialog.setOkBtn(R.string.dialog_fan_close_anim, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (anim3DDialog != null && anim3DDialog.isShow()) {
                            anim3DDialog.dismiss();
                        }
                    }
                });
                anim3DDialog.show();
                break;
            case R.id.fl_cooking_show:
                UIService.getInstance().postPage(PageKey.PotCookingTips);
                break;

            case R.id.iv_clean_lock:
                LogUtils.e("20200302", "iv_clean_lock" + fan);
                final IRokiDialog cleanLockDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_08);
                cleanLockDialog.setContentImg(R.mipmap.ic_clean_lock_dialog);
                cleanLockDialog.setContentText(R.string.dialog_fan_5916s_clean_text);
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

            case R.id.tv_cancel:
                if (fan != null) {
                    fan.setFanStatus(FanStatus.On, null);
                }
                break;
//            case R.id.tv_close:
//                if (fan != null) {
//                    fan.setFanStatus(FanStatus.Off, null);
//                }

//                break;
            case R.id.tv_smart_set:
            case R.id.tv_3d_wave_close:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.IsNotNetwork, "smart_set");
                UIService.getInstance().postPage(PageKey.HomeSet, bd);

                break;
            default:
                break;
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
                ToolUtils.logEvent(fan.getDt(), cx.getString(R.string.google_screen_fan_level_small),
                        cx.getString(R.string.google_screen_name));
                llWave.setVisibility(View.INVISIBLE);
                flLightAnim.setVisibility(View.VISIBLE);
//                stopMiddleAnimation();
//                stopBigAnimation();
                fanSmall.setImageResource(R.mipmap.fan_small_select);
                fanMiddle.setImageResource(R.mipmap.fan_middle_normal);
                fanBig.setImageResource(R.mipmap.fan_big_normal);
                if (!isSmallAnim) {
                    loadingGearsAnimSmall();
                }
//                startSmallAnimation();
                break;
            case FanStatus.LEVEL_MIDDLE:
                ToolUtils.logEvent(fan.getDt(), cx.getString(R.string.google_screen_fan_level_middle),
                        cx.getString(R.string.google_screen_name));
                llWave.setVisibility(View.INVISIBLE);
                flLightAnim.setVisibility(View.VISIBLE);
//                stopSmallAnimation();
//                stopBigAnimation();
                fanMiddle.setImageResource(R.mipmap.fan_middle_select);
                fanSmall.setImageResource(R.mipmap.fan_small_normal);
                fanBig.setImageResource(R.mipmap.fan_big_normal);
                if (!isSmallMiddle) {
                    loadingGearsAnimMiddle();
                }
//                startMiddleAnimation();

                break;
            case FanStatus.LEVEL_BIG:
            case FanStatus.LEVEL_BIG_5:
            case FanStatus.LEVEL_BIG_4:
                ToolUtils.logEvent(fan.getDt(), cx.getString(R.string.google_screen_fan_level_big),
                        cx.getString(R.string.google_screen_name));
                llWave.setVisibility(View.INVISIBLE);
                flLightAnim.setVisibility(View.VISIBLE);
//                stopMiddleAnimation();
//                stopSmallAnimation();
                fanBig.setImageResource(R.mipmap.fan_big_select);
                fanSmall.setImageResource(R.mipmap.fan_small_normal);
                fanMiddle.setImageResource(R.mipmap.fan_middle_normal);
                if (!isSmallBig) {
                    loadingGearsAnimBig();
                }
//                startBigAnimation();

                break;
            case FanStatus.LEVEL_EMPTY:
                llWave.setVisibility(View.VISIBLE);
                flLightAnim.setVisibility(View.INVISIBLE);
                fanSmall.setImageResource(R.mipmap.fan_small_normal);
                fanMiddle.setImageResource(R.mipmap.fan_middle_normal);
                fanBig.setImageResource(R.mipmap.fan_big_normal);
//                stopSmallAnimation();
//                stopMiddleAnimation();
//                stopBigAnimation();
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

    private void loadingGearsAnimSmall() {
        isSmallAnim = true;
        isSmallBig = false;
        isSmallMiddle = false;
        Glide.with(getActivity())
                .load(R.mipmap.fan_git_small)
                .asGif()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivLightAnim);
    }

    private void loadingGearsAnimMiddle() {
        isSmallAnim = false;
        isSmallBig = false;
        isSmallMiddle = true;
        Glide.with(getActivity())
                .load(R.mipmap.fan_git_middle)
                .asGif()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivLightAnim);

    }

    private void loadingGearsAnimBig() {
        isSmallAnim = false;
        isSmallBig = true;
        isSmallMiddle = false;
        Glide.with(getActivity())
                .load(R.mipmap.fan_git_big)
                .asGif()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivLightAnim);
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


    @Override
    public void onPause() {
        super.onPause();
        isinit = 0;
        potTempView.initTimer(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        potTempView.initTimer(false);
        ButterKnife.reset(this);
    }

}
