package com.robam.common.pojos.device.fan;

import android.os.PowerManager;
import android.util.Log;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgCallback;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.services.ScreenPowerService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.R;
import com.robam.common.Utils;
import com.robam.common.events.DeviceAddBluetoothSubsetFailureEvent;
import com.robam.common.events.Fan3DGestureEvent;
import com.robam.common.events.FanCleanLockEvent;
import com.robam.common.events.FanCleanNoticEvent;
import com.robam.common.events.FanLevelEvent;
import com.robam.common.events.FanLightEvent;
import com.robam.common.events.FanOilCupCleanEvent;
import com.robam.common.events.FanPlateRemoveEvent;
import com.robam.common.events.FanPowerEvent;
import com.robam.common.events.FanRegularlyRemindEvent;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.FanTimingCompletedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.SmartParams;

import org.json.JSONException;

abstract public class AbsFan extends AbsDeviceHub implements IFan {

    static final public short PowerLevel_0 = 0;
    static final public short PowerLevel_1 = 1;
    static final public short PowerLevel_2 = 2;
    static final public short PowerLevel_3 = 3;
    static final public short PowerLevel_6 = 6;

    static final public short Event_Power = 10;
    static final public short Event_TimingCompleted = 11;
    static final public short Event_Level = 12;
    static final public short Event_Light = 13;
    static final public short Event_CleanNotic = 14;
    static final public short Event_CleanLock = 15;
    static final public short Event_PlateRemove = 16;
    static final public short Event_OilCup = 20;
    static final public short Event_TimingRemind = 22;//定时提醒结束事件
    static final public short Event_3D_gesture = 23;//3D手势事件
    protected short terminalType = TerminalType.getType();
    public SmartParams smartParams = new SmartParams();
    public short status;
    public short prestatus;
    public short level;
    public short prelevel;
    public short timeLevel;
    public short timeWork;
    public boolean light;
    public boolean clean;
    public short wifi = -1;
    public short argument;
    public short backsmoke = 255;//回烟
    public short checkFan = 0;//止回阀
    public short waitTime = 0;//等待时间
    public short gasCheck = 0;//空气质量检测
    public short isNeedCupOil = 0;//是否需要倒油杯
    public short fanFeelStatus = 0;//智能烟感状态
    PowerManager.WakeLock wakeLock;
    public short temperatureReportOne;
    public short temperatureReportTwo;
    public short braiseAlarm;
    public short regularVentilationRemainingTime;
    public short fanStoveLinkageVentilationRemainingTime;
    public short periodicallyRemindTheRemainingTime;
    public short presTurnOffRemainingTime;
    public short leftStoveBraiseAlarm;
    public short rightStoveBraiseAlarm;

    public AbsFan(DeviceInfo devInfo) {
        super(devInfo);
    }

    public abstract boolean isSupportPot();
    // -------------------------------------------------------------------------------
    // IDevice
    // -------------------------------------------------------------------------------


    @Override
    public void onPolling() {
        try {
            if (Plat.DEBUG)
                Log.i("fan_polling", "烟机 onPolling");
            Msg reqMsg = newReqMsg(MsgKeys.GetFanStatus_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
            reqMsg.setIsFan(true);
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged() {
        if (Plat.LOG_FILE_ENABLE) {
            LogUtils.logFIleWithTime(String.format("Fan onStatusChanged. isConnected:%s level:%s", isConnected(), level));
        }
        if (Plat.DEBUG)
            Log.e("fan_polling_rep", "leftStoveBraiseAlarm:" + leftStoveBraiseAlarm + " rightStoveBraiseAlarm:"
                    + rightStoveBraiseAlarm + " status:" + status + " prestatus" + prestatus + " level:" + level
                    + " prelevel:" + prelevel + " timeLevel:" + timeLevel + " timeWork" + timeWork
                    + " light:" + light + " clean " + clean + " wifi " + wifi + " argument "
                    + argument + " gasCheck " + gasCheck + " isNeedCupOil:" + isNeedCupOil + " fanFeelStatus: " + fanFeelStatus
                    + " temperatureReportOne:" + temperatureReportOne + " temperatureReportTwo:" + temperatureReportTwo
                    + " braiseAlarm:" + braiseAlarm);
        postEvent(new FanStatusChangedEvent(AbsFan.this));
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);

        try {
            int key = msg.getID();
            switch (key) {
                case MsgKeys.FanEvent_Noti:
                    short eventId = (short) msg.optInt(MsgParams.EventId);
                    short eventParam = (short) msg.optInt(MsgParams.EventParam);
                    LogUtils.e("20190628", "eventId:" + eventId);
                    LogUtils.e("20190628", "eventParam:" + eventParam);
                    switch (eventId) {
                        case Event_Power:
                            if (this.getID() != null && this.getID().equals(Utils.getDefaultFan().getID())) {
                                postEvent(new FanPowerEvent(this, 1 == eventParam));
                            }
                            break;
                        case Event_Light:
                            if (this.getID() != null && this.getID().equals(Utils.getDefaultFan().getID())) {
                                postEvent(new FanLightEvent(this, 1 == eventParam));
                            }
                            break;
                        case Event_Level:
                            if (this.getID() != null && this.getID().equals(Utils.getDefaultFan().getID())) {
                                postEvent(new FanLevelEvent(this, eventParam));
                            }
                            break;
                        case Event_TimingCompleted:
                            postEvent(new FanTimingCompletedEvent(this, eventParam));
                            break;
                        case Event_CleanNotic://清洗提醒
                            postEvent(new FanCleanNoticEvent(this));
                            LogUtils.e("20200302", "清洗提醒");
                            break;
                        case Event_CleanLock://清洗锁定
                            LogUtils.e("20200302", "清洗锁定");
                            postEvent(new FanCleanLockEvent(this, eventParam));
                            break;
                        case Event_PlateRemove://挡风板拆除
                            postEvent(new FanPlateRemoveEvent(this, eventParam));
                            break;
                        case Event_OilCup:   //油杯提醒
                            postEvent(new FanOilCupCleanEvent(this));
                            break;
                        case Event_TimingRemind:  //定时提醒
                            postEvent(new FanRegularlyRemindEvent(this));
                            break;
                        case Event_3D_gesture://3D手势事件
                            postEvent(new Fan3DGestureEvent(this, eventParam));
                            break;
                    }

                    break;
                case MsgKeys.GetFanStatus_Rep:
                    AbsFan.this.prestatus = AbsFan.this.status;
                    AbsFan.this.status = (short) msg.optInt(MsgParams.FanStatus);
                    AbsFan.this.prelevel = AbsFan.this.level;
                    AbsFan.this.level = (short) msg.optInt(MsgParams.FanLevel);
                    AbsFan.this.light = msg.optBoolean(MsgParams.FanLight);
                    AbsFan.this.clean = msg.optBoolean(MsgParams.NeedClean);
                    AbsFan.this.timeWork = (short) msg.optInt(MsgParams.FanTime);
                    AbsFan.this.wifi = (short) msg.optInt(MsgParams.FanWIfi, -1);
                    AbsFan.this.argument = (short) msg.optInt(MsgParams.ArgumentNumber);
                    AbsFan.this.checkFan = (short) msg.optInt(MsgParams.CheckFan);
                    AbsFan.this.waitTime = (short) msg.optInt(MsgParams.WaitTime);
                    AbsFan.this.gasCheck = (short) msg.optInt(MsgParams.GasCheck);
                    AbsFan.this.isNeedCupOil = (short) msg.optInt(MsgParams.IsNeedCupOil);
                    AbsFan.this.fanFeelStatus = (short) msg.optInt(MsgParams.FanFeelStatus);
                    AbsFan.this.temperatureReportOne = (short) msg.optInt(MsgParams.TemperatureReportOne);
                    AbsFan.this.temperatureReportTwo = (short) msg.optInt(MsgParams.TemperatureReportTwo);
                    AbsFan.this.braiseAlarm = (short) msg.optInt(MsgParams.BraiseAlarm);
                    byte[] args = new byte[8];
                    for (short i = 7; i > -1; i--) {
                        args[7 - i] = ((braiseAlarm & (1 << i)) == 0) ? (byte) 0 : 1;
                        leftStoveBraiseAlarm = args[7];
                        rightStoveBraiseAlarm = args[6];
                    }
                    AbsFan.this.regularVentilationRemainingTime = (short) msg.optInt(MsgParams.RegularVentilationRemainingTime);
                    AbsFan.this.fanStoveLinkageVentilationRemainingTime = (short) msg.optInt(MsgParams.FanStoveLinkageVentilationRemainingTime);
                    AbsFan.this.periodicallyRemindTheRemainingTime = (short) msg.optInt(MsgParams.PeriodicallyRemindTheRemainingTime);
                    AbsFan.this.presTurnOffRemainingTime = (short) msg.optInt(MsgParams.PresTurnOffRemainingTime);

                    LogUtils.i("20190508", "braiseAlarm:" + braiseAlarm);
                    LogUtils.i("20190508", "leftStoveBraiseAlarm:" + leftStoveBraiseAlarm);
                    LogUtils.i("20190508", "rightStoveBraiseAlarm:" + rightStoveBraiseAlarm);

                    LogUtils.e("20190724", "regularVentilationRemainingTime:" + regularVentilationRemainingTime);
                    LogUtils.e("20190724", "fanStoveLinkageVentilationRemainingTime:" + fanStoveLinkageVentilationRemainingTime);
                    LogUtils.e("20190724", "periodicallyRemindTheRemainingTime:" + periodicallyRemindTheRemainingTime);
                    LogUtils.e("20200318", "presTurnOffRemainingTime:" + presTurnOffRemainingTime);


                    //add by wusi for keeping screen light when fan is working.
                    if ((short) msg.optInt(MsgParams.FanLevel) == 0 && null != wakeLock && wakeLock.isHeld()) {
                        wakeLock.release();
                    } else {
                        wakeLock = ScreenPowerService.getInstance().getWakeLock();
                        wakeLock.setReferenceCounted(false);
                        wakeLock.acquire();
                    }
                    onStatusChanged();

                    break;

                default:
                    break;
            }
        } catch (
                Exception e
        ) {
            e.printStackTrace();
        }


    }


    // -------------------------------------------------------------------------------
    // IFan
    // -------------------------------------------------------------------------------
    short savedLevel;

    @Override
    public void pause() {
        if (level > PowerLevel_0) {
            savedLevel = level;
            setFanLevel(PowerLevel_1, null);
        }
    }

    @Override
    public void restore() {
        setFanLevel(savedLevel, null);
        savedLevel = PowerLevel_0;
    }


    @Override
    public void getFanStatus(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetFanStatus_Req);
            msg.put(MsgParams.TerminalType, terminalType);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.prestatus = AbsFan.this.status;
                    AbsFan.this.status = (short) resMsg.optInt(MsgParams.FanStatus);
                    AbsFan.this.prelevel = AbsFan.this.level;
                    AbsFan.this.level = (short) resMsg.optInt(MsgParams.FanLevel);
                    AbsFan.this.light = resMsg.optBoolean(MsgParams.FanLight);
                    AbsFan.this.clean = resMsg.optBoolean(MsgParams.NeedClean);
                    AbsFan.this.timeWork = (short) resMsg.optInt(MsgParams.FanTime);   //增加预约时间 by zhaiyuanyi
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getFanModel() {
        return null;
    }

    @Override
    public void setFanStatus(final short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatus_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanStatus, status);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.prestatus = AbsFan.this.status;
                    AbsFan.this.status = status;

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearOilCupAramTime(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanCleanOirCupTime_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.prestatus = AbsFan.this.status;
                    AbsFan.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置烟机档位
     *
     * @param level    0、1、2、3、6档
     * @param callback
     */
    @Override
    public void setFanLevel(final short level, VoidCallback callback) {
        try {
            if (Plat.DEBUG)
                LogUtils.e("fan_setlevel", "level:" + level);
            Msg msg = newReqMsg(MsgKeys.SetFanLevel_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanLevel, level);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.prelevel = AbsFan.this.level;
                    AbsFan.this.level = level;
                    if (Plat.DEBUG)
                        LogUtils.i("20170328", "resMsg:" + resMsg.toString());
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setFanLight(final boolean light, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanLight_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanLight, light);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.light = light;

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setFanAllParams(final short level, final boolean light,
                                VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanAllParams_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanLevel, level);
            msg.putOpt(MsgParams.FanLight, light);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.prelevel = AbsFan.this.level;
                    AbsFan.this.level = level;
                    AbsFan.this.light = light;

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void restFanCleanTime(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.RestFanCleanTime_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void RestFanNetBoard(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.RestFanNetBoard_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setFanTimeWork(final short level, final short time,
                               VoidCallback callback) {
        try {
            if (Plat.DEBUG)
                LogUtils.i("20170328", "level:" + level + "time:" + time);
            Msg msg = newReqMsg(MsgKeys.SetFanTimeWork_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);             // 控制端类型区分 by zhaiyuanyi
//            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanLevel, level);
            msg.putOpt(MsgParams.FanTime, time);
            msg.setIsFan(true);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.timeLevel = level;
                    AbsFan.this.timeWork = time;
                    if (Plat.DEBUG)
                        LogUtils.i("20170328", "resmsg:" + resMsg.toString());
                    onStatusChanged();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void setTimingRemind(short onOff, short time, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanTimingRemind_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.TimeReminderSetSwitch, onOff);
            msg.putOpt(MsgParams.TimeReminderSetTime, time);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190812", "resMsg：" + resMsg.toString());
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSmartConfig(final Callback<SmartParams> callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSmartConfig_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallback(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    smartParams.IsPowerLinkage = resMsg
                            .optBoolean(MsgParams.IsPowerLinkage);
                    smartParams.IsLevelLinkage = resMsg
                            .optBoolean(MsgParams.IsLevelLinkage);
                    smartParams.IsShutdownLinkage = resMsg
                            .optBoolean(MsgParams.IsShutdownLinkage);
                    smartParams.ShutdownDelay = (short) resMsg
                            .optInt(MsgParams.ShutdownDelay);
                    smartParams.IsNoticClean = resMsg
                            .optBoolean(MsgParams.IsNoticClean);

                    //
                    smartParams.IsTimingVentilation = resMsg
                            .optBoolean(MsgParams.IsTimingVentilation);
                    smartParams.TimingVentilationPeriod = (short) resMsg
                            .optInt(MsgParams.TimingVentilationPeriod);
                    //
                    smartParams.IsWeeklyVentilation = resMsg
                            .optBoolean(MsgParams.IsWeeklyVentilation);
                    smartParams.WeeklyVentilationDate_Week = (short) resMsg
                            .optInt(MsgParams.WeeklyVentilationDate_Week);
                    smartParams.WeeklyVentilationDate_Hour = (short) resMsg
                            .optInt(MsgParams.WeeklyVentilationDate_Hour);
                    smartParams.WeeklyVentilationDate_Minute = (short) resMsg
                            .optInt(MsgParams.WeeklyVentilationDate_Minute);
                    smartParams.R8230S_Switch = (short) resMsg.optInt(MsgParams.R8230SFrySwitch);
                    smartParams.R8230S_Time = (short) resMsg.optInt(MsgParams.R8230SFryTime);
                    smartParams.FanCupOilSwitch = (short) resMsg.optInt(MsgParams.FanCupOilSwitch);
                    smartParams.FanReducePower = (short) resMsg.optInt(MsgParams.FanReducePower);
                    smartParams.gestureRecognitionSwitch = (short) resMsg.optInt(MsgParams.gestureRecognitionSwitch);
                    if (smartParams.TimingVentilationPeriod == 255)
                        smartParams.TimingVentilationPeriod = 3;
                    if (smartParams.WeeklyVentilationDate_Week == 255)
                        smartParams.WeeklyVentilationDate_Week = 1;
                    if (smartParams.WeeklyVentilationDate_Hour == 255)
                        smartParams.WeeklyVentilationDate_Hour = 12;
                    if (smartParams.WeeklyVentilationDate_Minute == 255)
                        smartParams.WeeklyVentilationDate_Minute = 30;

                    Helper.onSuccess(callback, smartParams);

                }
            });
        } catch (Exception e) {
            LogUtils.e("20190628", "e:" + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void setSmartConfig(final SmartParams smartParams, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSmartConfig_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            // msg.putOpt(MsgParams.UserId, getSrcUser());                   //增加 userid
            msg.putOpt(MsgParams.IsPowerLinkage, smartParams.IsPowerLinkage);
            msg.putOpt(MsgParams.IsLevelLinkage, smartParams.IsLevelLinkage);
            msg.putOpt(MsgParams.IsShutdownLinkage, smartParams.IsShutdownLinkage);
            msg.putOpt(MsgParams.ShutdownDelay, smartParams.ShutdownDelay);
            msg.putOpt(MsgParams.IsNoticClean, smartParams.IsNoticClean);
            msg.putOpt(MsgParams.IsTimingVentilation, smartParams.IsTimingVentilation);
            msg.putOpt(MsgParams.TimingVentilationPeriod, smartParams.TimingVentilationPeriod);
            msg.putOpt(MsgParams.IsWeeklyVentilation, smartParams.IsWeeklyVentilation);
            msg.putOpt(MsgParams.WeeklyVentilationDate_Week, smartParams.WeeklyVentilationDate_Week);
            msg.putOpt(MsgParams.WeeklyVentilationDate_Hour, smartParams.WeeklyVentilationDate_Hour);
            msg.putOpt(MsgParams.WeeklyVentilationDate_Minute, smartParams.WeeklyVentilationDate_Minute);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.smartParams = smartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addPotDevice() {
        try {
            if (Plat.DEBUG)
                LogUtils.i("20190807", "无人锅添加 53指令");
            Msg reqMsg = newReqMsg(MsgKeys.FanAddPot_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);
            reqMsg.setIsFan(true);
            sendMsg(reqMsg, new RCMsgCallbackWithVoid(new VoidCallback() {
                @Override
                public void onSuccess() {
                    if (Plat.DEBUG)
                        LogUtils.e("20190807", "无人锅添加 54指令 成功");
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.e("20190807", "失败:" + t.toString());
                    EventUtils.postEvent(new DeviceAddBluetoothSubsetFailureEvent());

                }
            }));
        } catch (Exception e) {
        }
    }

    public void delPotDevice(String deviceId, boolean isDefaultFan, MsgCallback msgCallback) {
        try {
            Msg reqMsg = newReqMsg(MsgKeys.FanDelPot_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);
            reqMsg.putOpt(MsgParams.DeviceId, deviceId);
            reqMsg.setIsFan(isDefaultFan);
            sendMsg(reqMsg, msgCallback);
        } catch (Exception e) {
        }
    }
    // -------------------------------------------------------------------------------
    // protected
    // -------------------------------------------------------------------------------

    /**
     * 设置智能烟感状态
     */
    public void setFanSmartSmoke(final FanStatusComposite fanStatusComposite, short argumentNumber,
                                 VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 10);
                msg.putOpt(MsgParams.Length, 1);
                msg.putOpt(MsgParams.FanFeelPower, fanStatusComposite.FanFeelPower);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190424", "afterSuccess:" + resMsg.toString());

                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20190424", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置定时通风天数
     */
    public void setFanTimingVentilationTime(FanStatusComposite fanStatusComposite
            , short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 6);
                msg.putOpt(MsgParams.Length, 2);
                msg.putOpt(MsgParams.TimeAirPower, fanStatusComposite.IsTimingVentilation);
                msg.putOpt(MsgParams.TimeAirPowerDay, fanStatusComposite.TimingVentilationPeriod);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置烟机油网清洗提示开关
     */
    public void setFanOilCleanHintSwitch(FanStatusComposite fanStatusComposite
            , short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 5);
                msg.putOpt(MsgParams.Length, 1);
                msg.putOpt(MsgParams.FanCleanPower, fanStatusComposite.IsNoticClean);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置开启灶具烟机自动开启开关
     */
    public void setPowerLinkageSwitch(FanStatusComposite fanStatusComposite, short argumentNumber,
                                      VoidCallback callback) {

        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 1);
                msg.putOpt(MsgParams.Length, 1);
                msg.putOpt(MsgParams.FanStovePower, fanStatusComposite.IsPowerLinkage);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 设置灶具烟机档位联动开关
     */
    public void setLevelLinkageSwitch(FanStatusComposite fanStatusComposite, short argumentNumber,
                                      VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 2);
                msg.putOpt(MsgParams.Length, 1);
                msg.putOpt(MsgParams.FanPowerLink, fanStatusComposite.IsLevelLinkage);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 灶具关闭后烟机延时时间
     */
    public void setShutdownLinkageTime(FanStatusComposite fanStatusComposite, short argumentNumber,
                                       VoidCallback callback) {

        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 4);
                msg.putOpt(MsgParams.Length, 1);
                msg.putOpt(MsgParams.StoveShutDelayTime, fanStatusComposite.ShutdownDelay);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 灶具关闭后烟机延时开关
     */
    public void setShutdownLinkageSwitch(FanStatusComposite fanStatusComposite, short argumentNumber,
                                         VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 3);
                msg.putOpt(MsgParams.Length, 1);
                msg.putOpt(MsgParams.StoveShutDelay, fanStatusComposite.IsShutdownLinkage);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //设置礼拜几通风
    public void setFanWeekDay(FanStatusComposite fanStatusComposite, short argumentNumber, VoidCallback callback) {

        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 7);
                msg.putOpt(MsgParams.Length, 4);
                msg.putOpt(MsgParams.AirTimePower, fanStatusComposite.IsWeeklyVentilation);
                msg.putOpt(MsgParams.AirTimeWeek, fanStatusComposite.WeeklyVentilationDate_Week);
                msg.putOpt(MsgParams.AirTimeHour, fanStatusComposite.WeeklyVentilationDate_Hour);
                msg.putOpt(MsgParams.AirTimeMinute, fanStatusComposite.WeeklyVentilationDate_Minute);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setVariableTime(FanStatusComposite fanStatusComposite,
                                short argumentNumber, VoidCallback callback) {
        try {
            LogUtils.e("20190925", "R8230S_Switch:" + fanStatusComposite.R8230S_Switch);
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 8);
                msg.putOpt(MsgParams.Length, 2);
                msg.putOpt(MsgParams.R8230SFrySwitch, fanStatusComposite.R8230S_Switch);
                msg.putOpt(MsgParams.R8230SFryTime, fanStatusComposite.R8230S_Time);
            }
            msg.setIsFan(true);
            LogUtils.e("20190925", "msg:" + msg.toString());
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190925", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20190925", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setGestureRecognitionSwitch(FanStatusComposite fanStatusComposite,
                                            short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 13);
                msg.putOpt(MsgParams.Length, 1);
                msg.putOpt(MsgParams.gestureRecognitionSwitch, fanStatusComposite.gestureRecognitionSwitch);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20190925", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20190925", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setFactoryDataReset(final FanStatusComposite fanStatusComposite, short argumentNumber, VoidCallback callback) {

        try {

            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            setGestureRecognitionSwitch(fanStatusComposite, (short) 1, null);
            setVariableTime(fanStatusComposite, (short) 1, null);
            setFanWeekDay(fanStatusComposite, (short) 1, null);
            setShutdownLinkageSwitch(fanStatusComposite, (short) 1, null);
            setShutdownLinkageTime(fanStatusComposite, (short) 1, null);
            setLevelLinkageSwitch(fanStatusComposite, (short) 1, null);
            setPowerLinkageSwitch(fanStatusComposite, (short) 1, null);
            setFanOilCleanHintSwitch(fanStatusComposite, (short) 1, null);
            setFanTimingVentilationTime(fanStatusComposite, (short) 1, null);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.e("20200224", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.e("20200224", "Throwable:" + t.toString());
                }
            });
        } catch (Exception e) {

        }
    }


    @Override
    protected void initStatus() {
        super.initStatus();

        status = FanStatus.Off;
        level = 0;
        timeLevel = 0;
        timeWork = 0;
        light = false;
        clean = false;
        wifi = -1;
    }


}
