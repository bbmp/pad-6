package com.robam.common.pojos.device.steameovenone;


import android.util.Log;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.SteamOvenOneAddSteamEvent;
import com.robam.common.events.SteamOvenOneAlarmEvent;
import com.robam.common.events.SteamOvenOneAutomaticModelEvent;
import com.robam.common.events.SteamOvenOneDescalingEvent;
import com.robam.common.events.SteamOvenOneLightResetEvent;
import com.robam.common.events.SteamOvenOneOvenRunModeResetEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.events.SteamOvenOneSwitchControlResetEvent;
import com.robam.common.events.SteamOvenOneWaterChangesEvent;
import com.robam.common.events.SteamOvenOneWorkFinishEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;

import java.util.Arrays;

import static com.robam.common.io.device.MsgParams.ArgumentNumber;
import static com.robam.common.io.device.MsgParams.SteameOvenOrderTime_hour;
import static com.robam.common.io.device.MsgParams.UserId;


/**
 * Created by zhoudingjun on 2017/6/13.
 * 烤蒸一体机
 */

public class AbsSteameOvenOne extends AbsDeviceHub implements ISteamOvenOne {


    static final public short Event_SteameOven_Switch_Control_Reset = 10;       //开关控制事件
    static final public short Event_SteameOven_run_model_Control_Reset = 11;    //运行模式调整事件
    static final public short Event_SteameOven_Light_Reset = 13;                //灯光调整事件
    static final public short Event_SteameOven_work_finish_Reset = 14;          //工作完成事件
    static final public short Event_SteameOven_automatic_model_Reset = 15;      //自动模式调整事件
    static final public short Event_SteameOven_water_changes_Reset = 16;        //水箱更改事件
    static final public short Event_SteameOven_descaling_Reset = 17;            //除垢提醒事件
    static final public short Event_SteameOven_add_steam_Reset = 18;            //加蒸汽事件


    static final public short Event_SteameOven_No_Alarm = 0;//温度非正常故障
    static final public short Event_SteameOven_Up_Sensor_Alarm = 1;//温度上传感器故障
    static final public short Event_SteameOven_Down_Sensor_Alarm = 2;//温度下传感器故障
    static final public short Event_SteameOven_Cooling_Fan_Alarm = 3;//散热风机故障
    static final public short Event_SteameOven_No_Water_Alarm = 4;//缺水故障
    static final public short Event_SteameOven_Gating_Toast_Alarm = 5;//门控提示
    static final public short Event_SteameOven_Did_Not_Identify_Water_Alarm = 6;//未识别水箱

    static final public short Event_SteameOven_No_SpitRotate = 0;//烤叉非旋转
    static final public short Event_SteameOven_SpitRotate = 1;//烤叉旋转


    static final public short Event_SteameOven_Light_On = 1;//烤叉旋转
    static final public short Event_SteameOven_Light_Off = 0;//烤叉旋转


    public short powerStatus;
    public short worknStatus;
    public short powerOnStatus;
    public short alarm = 0;
    public short temp; // 当前温度
    public short time; // 当前剩余时
    public short light;//灯光控制
    public short leftTime;//烤叉旋转
    public short workModel;//工作模式
    public short setTemp;
    public short setTime;
    public short ordertime_min;
    public short ordertime_hour;
    public short WaterStatus;
    public short autoMode;//自动模式
    public short CpStep;//自动模式介
    public short recipeId;//菜谱ID
    public short recipeStep;//菜谱步骤
    public short argument;//参数个数
    public short SectionOfTheStep;//多段烹饪步
    public short steam;//蒸汽
    public short setTempDownValue;//设置下温度
    public short currentTempDownValue;//实时下温度
    public short setTempUpValue;//设置上温度
    public short currentTempUpValue;//实时温度
    protected short terminalType = TerminalType.getType();
    public short multiSumStep;//多段总段数
    public short modelType;//模式类型

    public AbsSteameOvenOne(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onPolling() {
        try {
            if (Plat.DEBUG)
                LogUtils.i("steamovenone_st", "ID:" + getID() + " onPolling");
            Msg reqMsg = newReqMsg(MsgKeys.getSteameOvenStatus_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged() {
        if (Plat.DEBUG)
            LogUtils.i("ooooooooo", " powerStatus:" + powerStatus + " powerOnStatus:" + powerOnStatus + " worknStatus:" + worknStatus
                    + " workModel:" + workModel
                    + " ordertime_hour:" + ordertime_hour + " ordertime_min:" + ordertime_min
                    + " alarm:" + alarm + " setTime:" + setTime + " leftTime:" + leftTime + " setTemp:" + setTemp + " temp:"
                    + temp + " setTempDownValue:" + setTempDownValue + " currentTempDownValue:" + currentTempDownValue + " light:" + light + " WaterStatus:" + WaterStatus
                    + " modelType:" + modelType + " SectionOfTheStep:" + SectionOfTheStep + " multiSumStep:" + multiSumStep);
        postEvent(new SteamOvenOneStatusChangedEvent(AbsSteameOvenOne.this));
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        int key = msg.getID();
        switch (key) {
            case MsgKeys.SteameOven_Noti:
                short eventId = (short) msg.optInt(MsgParams.EventId);
                short eventParam = (short) msg.optInt(MsgParams.EventParam);

                switch (eventId) {
                    case Event_SteameOven_Switch_Control_Reset:
                        postEvent(new SteamOvenOneSwitchControlResetEvent(this, 1 == eventParam));
                        break;
                    case Event_SteameOven_run_model_Control_Reset:
                        postEvent(new SteamOvenOneOvenRunModeResetEvent(this, eventParam));
                        break;
                    case Event_SteameOven_Light_Reset:
                        postEvent(new SteamOvenOneLightResetEvent(this, eventParam));
                        break;
                    case Event_SteameOven_work_finish_Reset:
                        postEvent(new SteamOvenOneWorkFinishEvent(this, eventParam));

                        break;
                    case Event_SteameOven_automatic_model_Reset:
                        postEvent(new SteamOvenOneAutomaticModelEvent(this, eventParam));

                        break;
                    case Event_SteameOven_water_changes_Reset:
                        postEvent(new SteamOvenOneWaterChangesEvent(this, eventParam));
                        break;
                    case Event_SteameOven_descaling_Reset:
                        postEvent(new SteamOvenOneDescalingEvent(this, eventParam));

                        break;
                    case Event_SteameOven_add_steam_Reset:
                        postEvent(new SteamOvenOneAddSteamEvent(this, eventParam));
                        break;
                    default:
                        break;
                }
                break;

            case MsgKeys.SteameOvenAlarm_Noti:
                short alarmId = (short) msg.optInt(MsgParams.SteameOvenAlarm);
                Log.i("20171211", "alarmId:" + alarmId);
                if (alarmId == 0)
                    return;
                short[] args = new short[8];
                for (short i = 7; i > -1; i--) {
                    args[7 - i] = ((alarmId & (1 << i)) == 0) ? (short) 0 : 1;
                }
                Log.i("20171211", "args:" + Arrays.toString(args));
                postEvent(new SteamOvenOneAlarmEvent(this, args));
                // 传感器上 2  传感器下 4  //风机故障 8
                break;
            case MsgKeys.getSteameOvenStatus_Rep:
                this.powerStatus = (short) msg.optInt(MsgParams.SteameOvenStatus);
                this.alarm = (short) msg.optInt(MsgParams.SteameOvenAlarm);
                this.powerOnStatus = (short) msg.optInt(MsgParams.SteameOvenPowerOnStatus);
                this.worknStatus = (short) msg.optInt(MsgParams.SteameOvenWorknStatus);
                this.temp = (short) msg.optInt(MsgParams.SteameOvenTemp);
                this.light = (short) msg.optInt(MsgParams.SteameOvenLight);
                this.workModel = (short) msg.optInt(MsgParams.SteameOvenMode);
                this.leftTime = (short) msg.optInt(MsgParams.SteameOvenLeftTime);
                this.ordertime_min = (short) msg.optInt(MsgParams.SteameOvenOrderTime_min);
                this.ordertime_hour = (short) msg.optInt(MsgParams.SteameOvenOrderTime_hour);
                this.WaterStatus = (short) msg.optInt(MsgParams.SteameOvenWaterStatus);
                this.setTemp = (short) msg.optInt(MsgParams.SteameOvenSetTemp);
                this.setTime = (short) msg.optInt(MsgParams.SteameOvenSetTime);
                this.autoMode = (short) msg.optInt(MsgParams.SteameOvenCpMode);
                this.CpStep = (short) msg.optInt(MsgParams.SteameOvenCpStep);
                this.recipeId = (short) msg.optInt(MsgParams.SteameOvenRecipeId);
                this.recipeStep = (short) msg.optInt(MsgParams.SteameOvenRecipesteps);
                this.setTempDownValue = (short) msg.optInt(MsgParams.SteameOvenSetDownTemp);
                this.currentTempDownValue = (short) msg.optInt(MsgParams.SteameOvenDownTemp);
                this.steam = (short) msg.optInt(MsgParams.SteameOvenSteam);
                this.SectionOfTheStep = (short) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Key);
                this.multiSumStep = (short) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key);
                this.modelType = (short) msg.optInt(MsgParams.SteameOvenModelType);
                this.argument = (short) msg.optInt(ArgumentNumber);

                onStatusChanged();

                LogUtils.i("20171113", "SteamOvenOne:" + this.getClass().hashCode() + " worknStatus:" + worknStatus + " powerOnStatus:" + powerOnStatus + " leftTime:" + leftTime + " alarm:" + alarm +
                        " workModel:" + workModel + " setTemp:" + setTemp + " temp:" + temp + " setTime:" + setTime + " time:" + time + " ordertime_hour:" + ordertime_hour + " ordertime_min:" + ordertime_min
                        + " autoMode:" + autoMode + " powerStatus:" + powerStatus + " light:" + light + " modelType:" + modelType
                        + "SectionOfTheStep:" + SectionOfTheStep + " multiSumStep:" + multiSumStep);
                break;
        }

    }


    /**
     * 灯控
     *
     * @param light
     * @param callback
     */
    public void setLightControl(final short light, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenFloodlight_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenLight, light);
            msg.putOpt(ArgumentNumber, 0);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteameOvenOne.this.light = light;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 一体机状态控制
     *
     * @param status
     * @param callback
     */
    public void setSteamOvenOneStatusControl(final short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenStatusControl_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenPowerOnStatus, status);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteameOvenOne.this.powerOnStatus = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 关机
     */
    public void setSteameOvenStatus_Off(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatus.Off, SteamOvenOnePowerOnStatus.NoStatus, callback);
    }

    /**
     * 开机
     */
    public void setSteameOvenStatus_on(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.OperatingState, callback);
    }


    /**
     * 暂停
     */
    public void setSteameOvenStatus_pause(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.Pause, callback);
    }

    /**
     * 恢复运行
     */
    public void setSteameOvenStatus_rerun(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.WorkingStatus, callback);
    }

    /**
     * 一体机状态
     *
     * @param callback
     */
    public void setSteameOvenStatus(final short powerStatus,
                                    final short powerOnStatus, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenStatusControl_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenStatus, powerStatus);
            msg.putOpt(MsgParams.SteameOvenPowerOnStatus, powerOnStatus);
            msg.putOpt(SteameOvenOrderTime_hour, 255);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                   // AbsSteameOvenOne.this.powerStatus = powerStatus;
                   // AbsSteameOvenOne.this.powerOnStatus = powerOnStatus;
                   // onStatusChanged();
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20180414","t:"+t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSteameOvenOneRunMode(short mode, final short setTime,
                                        final short setTempUp, short preflag, VoidCallback callback) {
        setSteameOvenOneRunMode(mode, setTime, setTempUp, preflag, (short) 0, (short) 255, (short) 255, callback);
    }


    //154
    public void setSteameOvenOneRunMode(final short mode, final short setTime,
                                        final short setTempUp, short preflag, final short setTempDown,
                                        short orderTime_min, short orderTime_hour, VoidCallback callback) {
        try {
            Log.i("20171026", "mode:" + mode + " setTime:" + setTime + " setTempUp:" + setTempUp + " preflag:" + preflag
                    + " setTempDown:" + setTempDown + " orderTime_min:" + orderTime_min + " orderTime_hour:" + orderTime_hour);
            Msg msg = newReqMsg(MsgKeys.setSteameOvenBasicMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenMode, mode);
            msg.putOpt(MsgParams.SteameOvenSetTemp, setTempUp);
            msg.putOpt(MsgParams.SteameOvenSetTime, setTime);
            msg.putOpt(MsgParams.SteameOvenPreFlag, preflag);

            msg.putOpt(MsgParams.SteameOvenRecipeId, 0);
            msg.putOpt(MsgParams.SteameOvenRecipesteps, 0);
            msg.putOpt(MsgParams.SteameOvenSetDownTemp, setTempDown);
            msg.putOpt(MsgParams.OrderTime_value_min, orderTime_min);
            msg.putOpt(MsgParams.OrderTime_value_hour, orderTime_hour);
            msg.putOpt(ArgumentNumber, 0);
            final long curtime = System.currentTimeMillis();
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20180404","success");
//                    Log.i("20171023", "setRunMode:" + (System.currentTimeMillis() - curtime));
                    AbsSteameOvenOne.this.workModel = mode;
                    AbsSteameOvenOne.this.setTemp = setTempUp;
                    AbsSteameOvenOne.this.setTime = setTime;
                    AbsSteameOvenOne.this.setTempDownValue = setTempDown;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 菜谱设置
     *
     * @param ArgumentNumber
     * @param recipeId
     * @param OvenRecipeTotalStep
     * @param recipeStep
     * @param ovenMode
     * @param setTemp
     * @param setTime
     * @param callback
     */
    public void setSteameOvenOneRecipe(short ArgumentNumber, short recipeId,
                                       short OvenRecipeTotalStep, short recipeStep, short ovenMode, final short setTemp,
                                       final short setTime, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setTheRecipe_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());

            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            msg.putOpt(MsgParams.SteameOvenRecipeId, recipeId);
            msg.putOpt(MsgParams.SteameOvenRecipeTotalsteps, OvenRecipeTotalStep);
            msg.putOpt(MsgParams.SteameOvenRecipesteps, recipeStep);
            msg.putOpt(MsgParams.SteameOvenMode, ovenMode);
            msg.putOpt(MsgParams.SteameOvenTemp, setTemp);
            msg.putOpt(MsgParams.SteameOvenTime, setTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteameOvenOne.this.temp = setTemp;
                    AbsSteameOvenOne.this.time = (short) (setTime * 60);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置自动运行模式
     */
    public void setSteameOvenOneAutoRunMode(short autoMode, short setTime, VoidCallback
            callback) {
        setSteameOvenOneAutoRunMode(autoMode, setTime, (short) 0, (short) 0, (short) 0, callback);
    }

    public void setSteameOvenOneAutoRunMode(short autoMode, final short setTime,
                                            short ArgumentNumber,
                                            short min, short hour, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenAutomaticMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());

            msg.putOpt(MsgParams.SteameOvenCpMode, autoMode);
            msg.putOpt(MsgParams.SteameOvenSetTime, setTime);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                msg.putOpt(MsgParams.OrderTime_key, 1);
                msg.putOpt(MsgParams.OrderTime_length, 2);
                msg.putOpt(MsgParams.OrderTime_value_min, min);
                msg.putOpt(MsgParams.OrderTime_value_hour, hour);
            }
            LogUtils.i("20161021", " autoModel:" + autoMode + " setTime:" + setTime + " ArgumentNumber:" + ArgumentNumber
                    + " min:" + min + " hour:" + hour);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteameOvenOne.this.setTime = setTime;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //水箱控制
    public void setSteameOvenOneWaterPop(final short waterStatus, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenWater_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenWaterStatus, waterStatus);
            msg.putOpt(ArgumentNumber, 0);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteameOvenOne.this.WaterStatus = waterStatus;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSteamOvenOneMode() {
        return null;
    }

    @Override
    public void pause() {

    }

    @Override
    public void restore() {

    }
}