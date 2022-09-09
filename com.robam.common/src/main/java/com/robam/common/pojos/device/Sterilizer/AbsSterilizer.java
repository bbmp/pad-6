package com.robam.common.pojos.device.Sterilizer;

import android.util.Log;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.SteriAlarmEvent;
import com.robam.common.events.SteriStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;

/**
 * Created by zhaiyuanyi on 15/11/19.
 */
abstract public class AbsSterilizer extends AbsDeviceHub implements ISterilizer {

    public short status;
    public short oldstatus;
    public boolean isChildLock;
    public boolean isDoorLock;
    public short AlarmStautus;
    public short SteriReserveTime;
    public short SteriDrying;
    public short SteriCleanTime;
    public short SteriDisinfectTime, work_left_time_l, work_left_time_h;
    public short temp, hum, germ, ozone;
    public short warmDishTemp;
    public SteriSmartParams steriSmartParams = new SteriSmartParams();

    protected short terminalType = TerminalType.getType();

    public AbsSterilizer(DeviceInfo devInfo) {
        super(devInfo);
    }


    // -------------------------------------------------------------------------------
    // IDevice
    // -------------------------------------------------------------------------------

    private static int times = 0;

    @Override
    public void onPolling() {
        if (Plat.DEBUG)
            LogUtils.i("sterilizer_polling", "消毒柜 sterilizer onPolling" + this.getID());
        times++;
        if (times % 6 != 0) {
            try {
                Msg reqMsg = newReqMsg(MsgKeys.GetSteriStatus_Req);
                reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
                sendMsg(reqMsg, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                times = 0;
                Msg reqMsg = newReqMsg(MsgKeys.GetSteriParam_Req);
                reqMsg.put(MsgParams.TerminalType, terminalType);   // 控制端类型区分
                sendMsg(reqMsg, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStatusChanged() {
        if (Plat.LOG_FILE_ENABLE) {
            LogUtils.logFIleWithTime(String.format("Sterilizer onStatusChanged. isConnected:%s;work_left_time_l:%s,", isConnected(), work_left_time_l));
        }
        postEvent(new SteriStatusChangedEvent(AbsSterilizer.this));
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        try {
            int key = msg.getID();
            switch (key) {
                case MsgKeys.SteriAlarm_Noti:
                    short alarmId = (short) msg.optInt(MsgParams.AlarmId);
                    postEvent(new SteriAlarmEvent(this, alarmId));
                    break;
                case MsgKeys.GetSteriStatus_Rep:
                    oldstatus = status;
                    AbsSterilizer.this.status = (short) msg.optInt(MsgParams.SteriStatus);
                    AbsSterilizer.this.isChildLock = msg.optBoolean(MsgParams.SteriLock);
                    AbsSterilizer.this.work_left_time_l = (short) msg.optInt(MsgParams.SteriWorkLeftTimeL);
                    AbsSterilizer.this.work_left_time_h = (short) msg.optInt(MsgParams.SteriWorkLeftTimeH);
                    AbsSterilizer.this.AlarmStautus = (short) msg.optInt(MsgParams.SteriAlarmStatus);
                    onStatusChanged();
                    break;
                case MsgKeys.GetSteriParam_Rep:
                    AbsSterilizer.this.temp = (short) msg.optInt(MsgParams.SteriParaTem);
                    AbsSterilizer.this.hum = (short) msg.optInt(MsgParams.SteriParaHum);
                    AbsSterilizer.this.germ = (short) msg.optInt(MsgParams.SteriParaGerm);
                    AbsSterilizer.this.ozone = (short) msg.optInt(MsgParams.SteriParaOzone);
                    onStatusChanged();
                    break;
                case MsgKeys.GetSteriPVConfig_Rep:

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------------
    // ISterilizer
    // -------------------------------------------------------------------------------
    @Override
    public void pause() {

    }

    @Override
    public void restore() {

    }

    @Override
    public String getSterilizerModel() {
        return null;
    }

    @Override
    public void getSteriStatus(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSteriStatus_Req);
            msg.put(MsgParams.TerminalType, terminalType);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSterilizer.this.status = (short) resMsg.optInt(MsgParams.SteriStatus);
                    AbsSterilizer.this.isChildLock = resMsg.optBoolean(MsgParams.SteriLock);
                    AbsSterilizer.this.work_left_time_l = (short) resMsg.optInt(MsgParams.SteriWorkLeftTimeL);
                    AbsSterilizer.this.work_left_time_h = (short) resMsg.optInt(MsgParams.SteriWorkLeftTimeH);
                    AbsSterilizer.this.AlarmStautus = (short) resMsg.optInt(MsgParams.SteriAlarmStatus);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSteriPower(final short status, final VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPowerOnOff_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriStatus, status);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSterilizer.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSteriNewStatus(final short status,final short setTime,
                                  short warmDishTempValue, final VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPowerOnOff_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriStatus, status);
            msg.putOpt(MsgParams.SteriTime, setTime);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.Key,1);
            msg.putOpt(MsgParams.Length,1);
            msg.putOpt(MsgParams.warmDishTempValue, warmDishTempValue);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    if (Plat.DEBUG)
                        LogUtils.i("SterilizerStatus", "SterilizerStatus:" + resMsg.toString()+" SteriDisinfectTime:"+SteriDisinfectTime+" status:"+status);
                    AbsSterilizer.this.SteriDisinfectTime = setTime;
                    AbsSterilizer.this.status = status;
                    onStatusChanged();
                }
                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                }
            });
        } catch (Exception e) {
        }
    }


    @Override
    public void setSteriNewStatus(final short status,final short SteriDisinfectTime, final VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPowerOnOff_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriStatus, status);
            msg.putOpt(MsgParams.SteriTime, SteriDisinfectTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    if (Plat.DEBUG)
                        LogUtils.i("SterilizerStatus", "SterilizerStatus:" + resMsg.toString()+" SteriDisinfectTime:"+SteriDisinfectTime+" status:"+status);
                    AbsSterilizer.this.SteriDisinfectTime = SteriDisinfectTime;
                    AbsSterilizer.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
        }
    }

//    @Override  原始 829
//    public void setSteriNewStatus(final short status,final short steriTime,final short steriTemperature, final VoidCallback callback) {
//        try {
//            Msg msg = newReqMsg(MsgKeys.SetSteriPowerOnOff_Req);
//            msg.putOpt(MsgParams.TerminalType, terminalType);
//            msg.putOpt(MsgParams.UserId, getSrcUser());
//            msg.putOpt(MsgParams.SteriStatus, status);
//            msg.putOpt(MsgParams.SteriDisinfectTime, steriTime);
//            msg.putOpt(MsgParams.SetSteriTem, steriTemperature);
//
//            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
//                protected void afterSuccess(Msg resMsg) {
//                    if (Plat.DEBUG)
//                        LogUtils.i("SterilizerStatus", "SterilizerStatus:" + resMsg.toString()+" SteriDisinfectTime:"+SteriDisinfectTime+" status:"+status);
//                    AbsSterilizer.this.SteriDisinfectTime = steriTime;
//                    AbsSterilizer.this.warmDishTemp = steriTemperature;
//                    AbsSterilizer.this.status = status;
//                    onStatusChanged();
//                }
//            });
//        } catch (Exception e) {
//        }
//    }




    @Override
    public void setSteriReserveTime(final short SteriReserveTime, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriReserveTime_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriReserveTime, SteriReserveTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSterilizer.this.SteriReserveTime = SteriReserveTime;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setSteriDrying(final short SteriDrying, VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriDrying_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriDryingTime, SteriDrying);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSterilizer.this.SteriDrying = SteriDrying;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setSteriClean(final short SteriCleanTime, VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriClean_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriCleanTime, SteriCleanTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSterilizer.this.SteriCleanTime = SteriCleanTime;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setSteriDisinfect(final short SteriDisinfectTime, VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriDisinfect_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriDisinfectTime, SteriDisinfectTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSterilizer.this.SteriDisinfectTime = SteriDisinfectTime;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setSteriLock(final boolean isChildLock, VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriLock_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriLock, isChildLock);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSterilizer.this.isChildLock = isChildLock;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void querySteriParm(VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSteriParam_Req);
            msg.put(MsgParams.TerminalType, terminalType);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSterilizer.this.temp = (short) resMsg.optInt(MsgParams.SteriParaTem);
                    AbsSterilizer.this.hum = (short) resMsg.optInt(MsgParams.SteriParaHum);
                    AbsSterilizer.this.germ = (short) resMsg.optInt(MsgParams.SteriParaGerm);
                    AbsSterilizer.this.ozone = (short) resMsg.optInt(MsgParams.SteriParaOzone);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getSteriPVConfig(final Callback<SteriSmartParams> callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSteriPVConfig_Req);
            msg.put(MsgParams.TerminalType, terminalType);

            sendMsg(msg, new RCMsgCallback(callback) {
                protected void afterSuccess(Msg resMsg) {
                    steriSmartParams.IsInternalDays = resMsg.optInt(MsgParams.SteriSwitchDisinfect) == 1 ? true : false;
                    steriSmartParams.InternalDays = (short) resMsg.optInt(MsgParams.SteriInternalDisinfect);
                    steriSmartParams.IsWeekSteri = (boolean) resMsg.optBoolean(MsgParams.SteriSwitchWeekDisinfect);
                    steriSmartParams.WeeklySteri_week = (short) resMsg.optInt(MsgParams.SteriWeekInternalDisinfect);
                    steriSmartParams.PVCTime = (short) resMsg.optInt(MsgParams.SteriPVDisinfectTime);
                    //onStatusChanged();
                    if (steriSmartParams.InternalDays == 255)
                        steriSmartParams.InternalDays = 3;
                    if (steriSmartParams.WeeklySteri_week == 255)
                        steriSmartParams.WeeklySteri_week = 3;
                    if (steriSmartParams.PVCTime == 255)
                        steriSmartParams.PVCTime = 20;

                    Helper.onSuccess(callback, steriSmartParams);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setSteriPVConfig(final SteriSmartParams steriSmartParams, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPVConfig_Req);
            msg.put(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriSwitchDisinfect, steriSmartParams.IsInternalDays);
            msg.putOpt(MsgParams.SteriInternalDisinfect, steriSmartParams.InternalDays);
            msg.putOpt(MsgParams.SteriSwitchWeekDisinfect, steriSmartParams.IsWeekSteri);
            msg.putOpt(MsgParams.SteriWeekInternalDisinfect, steriSmartParams.WeeklySteri_week);
            msg.putOpt(MsgParams.SteriPVDisinfectTime, steriSmartParams.PVCTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSterilizer.this.steriSmartParams = steriSmartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
