package com.robam.rokipad.ui.page.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.common.collect.Lists;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.j256.ormlite.stmt.query.In;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.listener.OnItemSelectedListenerCenter;
import com.robam.rokipad.listener.OnItemSelectedListenerFront;
import com.robam.rokipad.listener.OnItemSelectedListenerRear;
import com.robam.rokipad.ui.view.setting.DeviceFanSmartSetView;
import com.robam.rokipad.ui.view.setting.DeviceStoveSmartSetView;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.IClickModeListener;
import com.robam.rokipad.utils.RemoveManOrsymbolUtil;
import com.robam.rokipad.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/21.
 * PS: 智能设定页面.
 */
public class SmartSetPage extends BasePage {
    private static final String TAG = "SmartSetPage";

    @InjectView(R.id.lay_fan)
    FrameLayout mLayFan;
    @InjectView(R.id.lay_stove)
    FrameLayout mLayStove;
    private DeviceFanSmartSetView mDeviceFanSmartSetView;
    private DeviceStoveSmartSetView mDeviceStoveSmartSetView;
    private IRokiDialog mDialogWheel;
    private AbsFan mFan;
    private boolean mIsTimingVentilation;
    private boolean mIsNoticClean;
    private boolean mIsPowerLinkage;
    private boolean mIsLevelLinkage;
    private boolean mIsShutdownLinkage;
    private short mDay;
    private FanStatusComposite mFanStatusComposite = new FanStatusComposite();
    private short mShutdownDelay;
    private IRokiDialog mWeekDayDialog;
    private IRokiDialog mVariableTimeDialog;
    private boolean mIsWeeklyVentilation;
    private IRokiDialog mHourMinDialog;
    private short mHour;
    private short mMin;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new MyHandler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    setDayData((String) msg.obj);
                    break;
                case 1:
                    setMinData((String) msg.obj);
                    break;
                case 2:
                    setWeekData((String) msg.obj);
                    break;
                case 3:
                    setHourData((String) msg.obj);
                    break;
                case 4:
                    setHourMinData((String) msg.obj);
                    break;

                case 5:
                    setVariableTime((String) msg.obj);
                    break;

                default:

                    break;
            }
        }
    };


    private void setHourData(String data) {
        mHour = Short.parseShort(data);
    }

    private void setHourMinData(String data) {
        mMin = Short.parseShort(data);

    }

    private void setWeekData(String data) {
        final short weekCharacterByNumber = getWeekCharacterByNumber(data);
        mWeekDayDialog.setOkBtn(R.string.ok_btn2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeekDayDialog.dismiss();
                mFanStatusComposite.WeeklyVentilationDate_Week = weekCharacterByNumber;
                mFan.setFanWeekDay(mFanStatusComposite, (short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        redSmartConfig();
                        mDeviceFanSmartSetView.setFanWeekDay(weekCharacterByNumber, mIsWeeklyVentilation);
                        ToastUtils.showShort(R.string.setSuccess);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
    }

    private void setMinData(String data) {

        final String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
        mDialogWheel.setOkBtn(R.string.ok_btn2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogWheel.dismiss();
//                FanStatusComposite fanStatusComposite = new FanStatusComposite();
                mFanStatusComposite.ShutdownDelay = Short.parseShort(removeString);
                ToolUtils.logEvent(Utils.getDefaultFan().getDt(),
                        cx.getString(R.string.google_screen_fan_stove_delay_switch_time)
                                + mFanStatusComposite.ShutdownDelay,
                        cx.getString(R.string.google_screen_name));
                mFan.setShutdownLinkageTime(mFanStatusComposite, (short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        mDeviceStoveSmartSetView.setShutdownDelay(Short.parseShort(removeString));
                        ToastUtils.showShort(R.string.setSuccess);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            }
        });
    }

    private void setDayData(final String data) {

        final String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
        mDialogWheel.setOkBtn(R.string.ok_btn2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogWheel.dismiss();

                mFanStatusComposite.TimingVentilationPeriod = Short.parseShort(removeString);
                ToolUtils.logEvent(Utils.getDefaultFan().getDt(),
                        cx.getString(R.string.google_screen_aeration_switch_day) + mFanStatusComposite.TimingVentilationPeriod, cx.getString(R.string.google_screen_name));
                mFan.setFanTimingVentilationTime(mFanStatusComposite, (short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        mDeviceFanSmartSetView.setDayData(removeString);
                        ToastUtils.showShort(R.string.setSuccess);

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            }
        });

    }

    /**
     * 设置延时爆炒时间
     *
     * @param data
     */
    private void setVariableTime(final String data) {

        mVariableTimeDialog.setOkBtn(R.string.ok_btn2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVariableTimeDialog.dismiss();
                mFanStatusComposite.R8230S_Time = Short.parseShort(data);
                mFan.setVariableTime(mFanStatusComposite, (short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        redSmartConfig();
//                        mDeviceFanSmartSetView.setVariableTime(removeString);
                        ToastUtils.showShort(R.string.setSuccess);

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        fireBaseAnalytics.setCurrentScreen(getActivity(), cx.getString(R.string.google_screen_smart_set), null);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_set, container, false);
        ButterKnife.inject(this, view);
        mFan = Utils.getDefaultFan();
        initView();
        initListener();
        return view;
    }

    private void initView() {

        if (mDeviceFanSmartSetView == null) {
            mDeviceFanSmartSetView = new DeviceFanSmartSetView(cx);
            mLayFan.removeAllViews();
            mLayFan.addView(mDeviceFanSmartSetView);
        }
        if (mDeviceStoveSmartSetView == null) {
            mDeviceStoveSmartSetView = new DeviceStoveSmartSetView(cx);
            mLayStove.removeAllViews();
            mLayStove.addView(mDeviceStoveSmartSetView);
        }
        if (mFan != null) {
            mDeviceFanSmartSetView.setFanDeviceDt(mFan.getDt());
            redSmartConfig();
        }

    }

    private void redSmartConfig() {

        mFan.getSmartConfig(new Callback<SmartParams>() {
            @Override
            public void onSuccess(SmartParams smartParams) {

                LogUtils.e("20200224", "smartParams:" + smartParams);

                mIsTimingVentilation = smartParams.IsTimingVentilation;
                                    mIsNoticClean = smartParams.IsNoticClean;
                                    mDay = smartParams.TimingVentilationPeriod;
                                    mIsPowerLinkage = smartParams.IsPowerLinkage;
                                    mIsLevelLinkage = smartParams.IsLevelLinkage;
                                    mIsShutdownLinkage = smartParams.IsShutdownLinkage;
                                    mFanStatusComposite.ShutdownDelay = mShutdownDelay = smartParams.ShutdownDelay;
                                    mFanStatusComposite.TimingVentilationPeriod = smartParams.TimingVentilationPeriod;
                                    mIsWeeklyVentilation = smartParams.IsWeeklyVentilation;
                                    mFanStatusComposite.WeeklyVentilationDate_Week = smartParams.WeeklyVentilationDate_Week;
                                    mFanStatusComposite.WeeklyVentilationDate_Hour = smartParams.WeeklyVentilationDate_Hour;
                                    mFanStatusComposite.WeeklyVentilationDate_Minute = smartParams.WeeklyVentilationDate_Minute;
                                    mFanStatusComposite.R8230S_Switch = smartParams.R8230S_Switch;
                                    mFanStatusComposite.R8230S_Time = smartParams.R8230S_Time;
                                    mFanStatusComposite.gestureRecognitionSwitch = smartParams.gestureRecognitionSwitch;
                                    if (mFanStatusComposite.gestureRecognitionSwitch == 0) {
                                        mDeviceFanSmartSetView.setGestureRecognitionSwitch(false);
                                    } else {
                                        mDeviceFanSmartSetView.setGestureRecognitionSwitch(true);
                                    }


                                    if (mFanStatusComposite.R8230S_Switch == 0) {
                                        mDeviceFanSmartSetView.setVariableTimeSwitch(false);
                                        mDeviceFanSmartSetView.setVariableTime(String.valueOf(mFanStatusComposite.R8230S_Time));
                                    } else {
                                        mDeviceFanSmartSetView.setVariableTimeSwitch(true);
                                        mDeviceFanSmartSetView.setVariableTime(String.valueOf(mFanStatusComposite.R8230S_Time));
                                    }

                                    if (mIsTimingVentilation) {
                                        mDeviceFanSmartSetView.setTimingVentilationSwitch(mIsTimingVentilation);
                                        mDeviceFanSmartSetView.setTimingVentilationDay(mDay, mIsTimingVentilation);
                                    } else {
                                        mDeviceFanSmartSetView.setTimingVentilationSwitch(mIsTimingVentilation);
                                        mDeviceFanSmartSetView.setTimingVentilationDay(mDay, mIsTimingVentilation);
                                    }
                                    if (mFanStatusComposite.R8230S_Switch == 1) {
                                        mDeviceFanSmartSetView.setNoticCleanSwitch(true);
                                    } else {
                                        mDeviceFanSmartSetView.setNoticCleanSwitch(false);
                                    }

                                    if (mIsPowerLinkage) {
                                        mDeviceStoveSmartSetView.setPowerLinkageSwitch(mIsPowerLinkage);
                                        mFanStatusComposite.IsPowerLinkage = 1;
                                    } else {
                                        mDeviceStoveSmartSetView.setPowerLinkageSwitch(mIsPowerLinkage);
                                        mFanStatusComposite.IsPowerLinkage = 0;
                                    }

                                    if (mIsLevelLinkage) {
                                        mDeviceStoveSmartSetView.setLevelLinkageSwitch(mIsLevelLinkage);
                                        mFanStatusComposite.IsLevelLinkage = 1;
                                    } else {
                                        mDeviceStoveSmartSetView.setLevelLinkageSwitch(mIsLevelLinkage);
                                        mFanStatusComposite.IsLevelLinkage = 0;
                                    }

                                    if (mIsShutdownLinkage) {
                                        mDeviceStoveSmartSetView.setShutdownDelaySwitch(mIsShutdownLinkage);
                                        mFanStatusComposite.IsShutdownLinkage = 1;
                                    } else {
                                        mDeviceStoveSmartSetView.setShutdownDelaySwitch(mIsShutdownLinkage);
                                        mFanStatusComposite.IsShutdownLinkage = 0;
                                    }

                                    if (mIsWeeklyVentilation) {
                                        mDeviceFanSmartSetView.setWeekVentilationSwitch(mIsWeeklyVentilation);
                                        mFanStatusComposite.IsWeeklyVentilation = 1;
                                    } else {
                                        mDeviceFanSmartSetView.setWeekVentilationSwitch(mIsWeeklyVentilation);
                                        mFanStatusComposite.IsWeeklyVentilation = 0;
                                    }

                                    if (mIsNoticClean){
                                        mDeviceFanSmartSetView.setOilSwitch(mIsNoticClean);
                                        mFanStatusComposite.IsNoticClean = 1;
                                    } else {
                                        mDeviceFanSmartSetView.setOilSwitch(mIsNoticClean);
                                        mFanStatusComposite.IsNoticClean = 0;
                                    }

                                    mDeviceFanSmartSetView.setWeekDay(mFanStatusComposite.WeeklyVentilationDate_Week, mFanStatusComposite.WeeklyVentilationDate_Hour
                                            , mFanStatusComposite.WeeklyVentilationDate_Minute);
                                    mDeviceStoveSmartSetView.setShutdownDelay(mShutdownDelay);
                                }


                                @Override
                                public void onFailure(Throwable t) {

                                }
                            }

        );
    }

    private void initListener() {
        mDeviceFanSmartSetView.setOnClickModeListener(new IClickModeListener() {
            @Override
            public void onClickModeListener(int mode) {

            }

            @Override
            public void onClickModeListener(String content) {

                if (content.equals("fl_aeration_day")) {
                    if (!mIsTimingVentilation) {
                        ToastUtils.showShort(R.string.setting_model_delayed_min_open_text);
                        return;
                    }
                    if (mDialogWheel != null) mDialogWheel = null;
                    mDialogWheel = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
                    mDialogWheel.setUnitName(R.string.setting_model_day_text);
                    mDialogWheel.setWheelViewData(getDayData(), false, 2, new OnItemSelectedListenerCenter() {
                        @Override
                        public void onItemSelectedCenter(String contentCenter) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = 0;
                            msg.obj = contentCenter;
                            mHandler.sendMessage(msg);
                        }
                    });
                    mDialogWheel.show();
                } else if (content.equals("aeration_switch")) {//通风换气开关
                    if (mIsTimingVentilation) {
//                        final FanStatusComposite fanStatusComposite = new FanStatusComposite();
                        mFanStatusComposite.IsTimingVentilation = 0;
                        mFan.setFanTimingVentilationTime(mFanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                redSmartConfig();
                                ToastUtils.showShort(R.string.setSuccess);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });
                    } else {
//                        FanStatusComposite fanStatusComposite = new FanStatusComposite();
                        mFanStatusComposite.IsTimingVentilation = 1;
                        mFan.setFanTimingVentilationTime(mFanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                redSmartConfig();
                                ToastUtils.showShort(R.string.setSuccess);
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    }


                } else if (content.equals("clean_switch")) {//油网清洗

                    if (mIsNoticClean) {
//                        FanStatusComposite fanStatusComposite = new FanStatusComposite();
                        mFanStatusComposite.IsNoticClean = 0;
                        mFan.setFanOilCleanHintSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                redSmartConfig();
                                ToastUtils.showShort(R.string.setSuccess);
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    } else {
//                        FanStatusComposite fanStatusComposite = new FanStatusComposite();
                        mFanStatusComposite.IsNoticClean = 1;
                        mFan.setFanOilCleanHintSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                redSmartConfig();
                                ToastUtils.showShort(R.string.setSuccess);
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });

                    }

                } else if (content.equals("week_day")) {

                    if (!mIsWeeklyVentilation) {
                        ToastUtils.showShort(R.string.setting_model_delayed_min_open_text);
                        return;
                    }
                    mWeekDayDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_0100);
                    mWeekDayDialog.setUnitName(R.string.setting_model_null_text);
                    mWeekDayDialog.setWheelViewData(getWeekDayData(), false, 0, new OnItemSelectedListenerCenter() {
                        @Override
                        public void onItemSelectedCenter(String contentCenter) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = 2;
                            msg.obj = contentCenter;
                            mHandler.sendMessage(msg);
                        }
                    });
                    mWeekDayDialog.show();

                } else if (content.equals("tv_time_day")) {
                    if (!mIsWeeklyVentilation) {
                        ToastUtils.showShort(R.string.setting_model_delayed_min_open_text);
                        return;
                    }
                    mHourMinDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
                    mHourMinDialog.setWheelViewData(getHourData(), getMinData(), false, 12, 30, new OnItemSelectedListenerFront() {
                        @Override
                        public void onItemSelectedFront(String contentFront) {

                            Message msg = mHandler.obtainMessage();
                            msg.what = 3;
                            msg.obj = contentFront;
                            mHandler.sendMessage(msg);

                        }
                    }, new OnItemSelectedListenerRear() {
                        @Override
                        public void onItemSelectedRear(String contentRear) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = 4;
                            msg.obj = contentRear;
                            mHandler.sendMessage(msg);
                        }
                    });

                    mHourMinDialog.show();

                    mHourMinDialog.setOkBtn(R.string.ok_btn2, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mHourMinDialog == null) return;
                            mHourMinDialog.dismiss();
                            mFanStatusComposite.WeeklyVentilationDate_Hour = mHour;
                            mFanStatusComposite.WeeklyVentilationDate_Minute = mMin;
                            mFan.setFanWeekDay(mFanStatusComposite, (short) 1, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    redSmartConfig();
                                    ToastUtils.showShort(R.string.setSuccess);
                                }

                                @Override
                                public void onFailure(Throwable t) {

                                }
                            });

                        }
                    });


                } else if (content.equals("breath_time_switch")) {
                    if (mIsWeeklyVentilation) {
                        mFanStatusComposite.IsWeeklyVentilation = 0;
                        mFan.setFanWeekDay(mFanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                redSmartConfig();
                                ToastUtils.showShort(R.string.setSuccess);
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    } else {
                        mFanStatusComposite.IsWeeklyVentilation = 1;
                        mFan.setFanWeekDay(mFanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                redSmartConfig();
                                ToastUtils.showShort(R.string.setSuccess);
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    }

                } else if (content.equals("factory_reset")) {
                    final IRokiDialog dialogResetFactory = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_0);
                    dialogResetFactory.setContentText(R.string.setting_model_ok_factory_reset_text);
                    dialogResetFactory.setCanceledOnTouchOutside(true);
                    dialogResetFactory.show();
                    dialogResetFactory.setOkBtn(R.string.cancel,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogResetFactory.dismiss();
                                }
                            });

                    dialogResetFactory.setCancelBtn(R.string.setting_model_factory_reset_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogResetFactory.dismiss();
                            final FanStatusComposite fanStatusComposite = new FanStatusComposite();
                            mFan.setFactoryDataReset(fanStatusComposite, (short) 1, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    redSmartConfig();
                                    ToastUtils.showShort(R.string.setSuccess);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    ToastUtils.showShort("设置失败");
                                }
                            });
                        }
                    });

                } else if ("fl_variable_time".equals(content)) {//变频爆炒延时时间

                    if (mFanStatusComposite.R8230S_Switch == 0) {
                        ToastUtils.showShort(R.string.setting_model_delayed_min_open_text);
                        return;
                    }
                    mVariableTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
                    mVariableTimeDialog.setUnitName(R.string.setting_model_min_text);
                    mVariableTimeDialog.setWheelViewData(getVariableTime(), false, 2, new OnItemSelectedListenerCenter() {
                        @Override
                        public void onItemSelectedCenter(String contentCenter) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = 5;
                            msg.obj = contentCenter;
                            mHandler.sendMessage(msg);
                        }
                    });
                    mVariableTimeDialog.show();
                } else if ("iv_3d_gesture_switch".equals(content)) {//3d手势开关

                    if (mFanStatusComposite.gestureRecognitionSwitch == 0) {
                        mFanStatusComposite.gestureRecognitionSwitch = 1;
                        mFan.setGestureRecognitionSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                redSmartConfig();
                                ToastUtils.showShort(R.string.setSuccess);
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    } else {
                        mFanStatusComposite.gestureRecognitionSwitch = 0;
                        mFan.setGestureRecognitionSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                redSmartConfig();
                                ToastUtils.showShort(R.string.setSuccess);
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });

                    }

                } else if ("variable_time_switch".equals(content)) {
                    LogUtils.e("20200908", "variable_time_switch：" + mFanStatusComposite.R8230S_Switch);
                    if (mFanStatusComposite.R8230S_Switch == 0) {
                        mFanStatusComposite.R8230S_Switch = 1;
                        mFan.setVariableTime(mFanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                redSmartConfig();
                                ToastUtils.showShort(R.string.setSuccess);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.e("20200908", "T:" + t.toString());
                            }
                        });
                    } else {
                        mFanStatusComposite.R8230S_Switch = 0;
                        mFan.setVariableTime(mFanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                redSmartConfig();
                                ToastUtils.showShort(R.string.setSuccess);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.e("20200908", ":T:" + t.toString());
                            }
                        });
                    }
                }
            }
        });

        mDeviceStoveSmartSetView.setOnClickModeListener(new

                                                                IClickModeListener() {
                                                                    @Override
                                                                    public void onClickModeListener(int mode) {

                                                                    }

                                                                    @Override
                                                                    public void onClickModeListener(String content) {

                                                                        LogUtils.e("20190425", "content:" + content + " mIsPowerLinkage:" + mIsPowerLinkage);
                                                                        if (content.equals("linked_switch")) {
                                                                            if (mIsPowerLinkage) {
//                        FanStatusComposite fanStatusComposite = new FanStatusComposite();
                                                                                mFanStatusComposite.IsPowerLinkage = 0;
                                                                                mFan.setPowerLinkageSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                                                                                    @Override
                                                                                    public void onSuccess() {
                                                                                        redSmartConfig();
                                                                                        ToastUtils.showShort(R.string.setSuccess);
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Throwable t) {

                                                                                    }
                                                                                });

                                                                            } else {
//                        FanStatusComposite fanStatusComposite = new FanStatusComposite();
                                                                                mFanStatusComposite.IsPowerLinkage = 1;
                                                                                mFan.setPowerLinkageSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                                                                                    @Override
                                                                                    public void onSuccess() {
                                                                                        redSmartConfig();
                                                                                        ToastUtils.showShort(R.string.setSuccess);
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Throwable t) {

                                                                                    }
                                                                                });
                                                                            }

                                                                        } else if (content.equals("power_switch")) {

                                                                            if (mIsLevelLinkage) {
//                        FanStatusComposite fanStatusComposite = new FanStatusComposite();
                                                                                mFanStatusComposite.IsLevelLinkage = 0;
                                                                                mFan.setLevelLinkageSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                                                                                    @Override
                                                                                    public void onSuccess() {
                                                                                        redSmartConfig();
                                                                                        ToastUtils.showShort(R.string.setSuccess);
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Throwable t) {

                                                                                    }
                                                                                });

                                                                            } else {
//                        FanStatusComposite fanStatusComposite = new FanStatusComposite();
                                                                                mFanStatusComposite.IsLevelLinkage = 1;
                                                                                mFan.setLevelLinkageSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                                                                                    @Override
                                                                                    public void onSuccess() {
                                                                                        redSmartConfig();
                                                                                        ToastUtils.showShort(R.string.setSuccess);
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Throwable t) {

                                                                                    }
                                                                                });
                                                                            }

                                                                        } else if (content.equals("delayed_min")) {

                                                                            if (!mIsShutdownLinkage) {
                                                                                ToastUtils.showShort(R.string.setting_model_delayed_min_open_text);
                                                                                return;
                                                                            }
                                                                            if (mDialogWheel != null)
                                                                                mDialogWheel = null;
                                                                            mDialogWheel = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
                                                                            mDialogWheel.setUnitName(R.string.setting_model_min_text);
                                                                            mDialogWheel.setWheelViewData(getMineData(), false, 0, new OnItemSelectedListenerCenter() {
                                                                                @Override
                                                                                public void onItemSelectedCenter(String contentCenter) {
                                                                                    Message msg = mHandler.obtainMessage();
                                                                                    msg.what = 1;
                                                                                    msg.obj = contentCenter;
                                                                                    mHandler.sendMessage(msg);
                                                                                }
                                                                            });
                                                                            mDialogWheel.show();

                                                                        } else if (content.equals("delayed_min_switch")) {

                                                                            if (mIsShutdownLinkage) {
//                        FanStatusComposite fanStatusComposite = new FanStatusComposite();
                                                                                mFanStatusComposite.IsShutdownLinkage = 0;
                                                                                mFan.setShutdownLinkageSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                                                                                    @Override
                                                                                    public void onSuccess() {
                                                                                        redSmartConfig();
                                                                                        ToastUtils.showShort(R.string.setSuccess);
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Throwable t) {

                                                                                    }
                                                                                });
                                                                            } else {
//                        FanStatusComposite fanStatusComposite = new FanStatusComposite();
                                                                                mFanStatusComposite.IsShutdownLinkage = 1;
                                                                                mFan.setShutdownLinkageSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                                                                                    @Override
                                                                                    public void onSuccess() {
                                                                                        redSmartConfig();
                                                                                        ToastUtils.showShort(R.string.setSuccess);
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Throwable t) {

                                                                                    }
                                                                                });
                                                                            }

                                                                        }
                                                                    }

                                                                });
    }


    private List<String> getVariableTime() {


        List<String> list = Lists.newArrayList();
        for (int i = 1; i <= 9; i++) {
            list.add(i + "");
        }
        return list;


    }

    private List<String> getWeekDayData() {

        List<String> mWeekData = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            switch (i) {
                case 1:
                    mWeekData.add(cx.getString(R.string.setting_model_week_one));
                    break;
                case 2:
                    mWeekData.add(cx.getString(R.string.setting_model_week_two));
                    break;
                case 3:
                    mWeekData.add(cx.getString(R.string.setting_model_week_three));
                    break;
                case 4:
                    mWeekData.add(cx.getString(R.string.setting_model_week_four));
                    break;
                case 5:
                    mWeekData.add(cx.getString(R.string.setting_model_week_five));
                    break;
                case 6:
                    mWeekData.add(cx.getString(R.string.setting_model_week_six));
                    break;
                case 7:
                    mWeekData.add(cx.getString(R.string.setting_model_week_seven));
                    break;
            }
        }
        return mWeekData;
    }

    private List<String> getDayData() {
        List<String> list = Lists.newArrayList();
        for (int i = 1; i <= 9; i++) {
            list.add(i + "");
        }
        return list;
    }

    private List<String> getMineData() {
        List<String> list = Lists.newArrayList();
        for (int i = 1; i <= 5; i++) {
            list.add(i + "");
        }
        return list;
    }

    private List<String> getHourData() {

        List<String> list = Lists.newArrayList();
        for (int i = 0; i <= 23; i++) {
            list.add(i + "");
        }
        return list;

    }

    private List<String> getMinData() {

        List<String> list = Lists.newArrayList();
        for (int i = 0; i <= 59; i++) {
            list.add(i + "");
        }
        return list;

    }

    //将获取到的文字转化为数字 例:周一 -->  1

    private short getWeekCharacterByNumber(String data) {

        if (data.equals(cx.getString(R.string.setting_model_week_one))) {
            return 1;
        } else if (data.equals(cx.getString(R.string.setting_model_week_two))) {
            return 2;
        } else if (data.equals(cx.getString(R.string.setting_model_week_three))) {
            return 3;
        } else if (data.equals(cx.getString(R.string.setting_model_week_four))) {
            return 4;
        } else if (data.equals(cx.getString(R.string.setting_model_week_five))) {
            return 5;
        } else if (data.equals(cx.getString(R.string.setting_model_week_six))) {
            return 6;
        } else if (data.equals(cx.getString(R.string.setting_model_week_seven))) {
            return 7;
        }
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
