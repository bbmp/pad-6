package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.nio.ByteBuffer;

/**
 * Created by as on 2017-08-09.
 */

public class SteamOvenMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        switch (key) {
            case MsgKeys.setSteameOvenStatusControl_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteameOvenStatus);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenPowerOnStatus);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenOrderTime_hour);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;
            case MsgKeys.getSteameOvenStatus_Req:

                break;
            case MsgKeys.setSteameOvenBasicMode_Req:

                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteameOvenMode);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenSetTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenSetTime);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenPreFlag);
                buf.put(b);
                int recipeId = msg.optInt(MsgParams.SteameOvenRecipeId);
                buf.put((byte) (recipeId & 0Xff));
                buf.put((byte) ((recipeId >> 8) & 0Xff));
                b = (byte) msg.optInt(MsgParams.SteameOvenRecipesteps);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenSetDownTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);

                break;
            case MsgKeys.setTheRecipe_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteameOvenPreFlag);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                    if (msg.optInt(MsgParams.SteameOvenRecipeId) == 1) {
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeId);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.SteameOvenRecipeTotalsteps) == 2) {
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeTotalsteps);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeTotalstepsLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeTotalstepsValue);
                        buf.put(b);
                    }

                    if (msg.optInt(MsgParams.SteameOvenRecipesteps) == 3) {
                        b = (byte) msg.optInt(MsgParams.SteameOvenMode);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenTemp);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenTime);
                        buf.put(b);
                    }


                }
                break;
            case MsgKeys.setSteameOvenFloodlight_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteameOvenLight);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;

            case MsgKeys.setSteameOvenWater_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteameOvenWaterStatus);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;
            case MsgKeys.setSteameOvensteam_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;
            case MsgKeys.setSteameOvenMultistageCooking_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                    if (msg.optInt(MsgParams.OrderTime_key) == 1) {
                        b = (byte) msg.optInt(MsgParams.OrderTime_key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OrderTime_length);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
                        buf.put(b);
                    }

                    if (msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key) == 2) {
                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Length);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Value);
                        buf.put(b);
                    }

                    if (msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Key) == 3) {
                        b = (byte) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenMode);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenTemp);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenTime);
                        buf.put(b);
                    }

                }

                break;
            default:
                break;
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        switch (key) {
            case MsgKeys.setSteameOvenStatusControl_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.getSteameOvenStatus_Rep://一体机状态查询回应

                msg.putOpt(MsgParams.SteameOvenStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenPowerOnStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenWorknStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenAlarm,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenMode,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenTemp,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenLeftTime,
                        MsgUtils.getShort(payload, offset++));
                offset++;
                msg.putOpt(MsgParams.SteameOvenLight,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenWaterStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenSetTemp,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenSetTime,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenOrderTime_min,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenOrderTime_hour,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenRecipeId,
                        MsgUtils.getShort(payload[offset++]));
                offset++;
                msg.putOpt(MsgParams.SteameOvenRecipesteps,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenSetDownTemp,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenDownTemp,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenSteam,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Key,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Key,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenPreFlag,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenModelType,
                        MsgUtils.getShort(payload[offset++]));
                short argument = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, argument);
                break;
            case MsgKeys.SteameOvenAlarm_Noti:
                msg.putOpt(MsgParams.SteameOvenAlarm,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.ArgumentNumber,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SteameOven_Noti://工作事件上报

                msg.optInt(MsgParams.EventId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.UserId,
                        MsgUtils.getString(payload, offset, 10));
                msg.putOpt(MsgParams.EventParam,
                        MsgUtils.getShort(payload[offset++]));
                short arg = MsgUtils.getShort(payload[offset++]);

                msg.putOpt(MsgParams.ArgumentNumber, arg);
                short arg_key = MsgUtils.getShort(payload[offset++]);
                while (arg > 0) {
                    switch (arg_key) {
                        case 1:
                            msg.putOpt(MsgParams.setSteameOvenBasicMode_Key,
                                    arg_key);
                            msg.putOpt(MsgParams.setSteameOvenBasicMode_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.setSteameOvenBasicMode_value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 2:
                            msg.putOpt(MsgParams.SteameOvenSetTemp,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenSetTemp_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            offset++;
                            msg.putOpt(MsgParams.SteameOvenSetTemp_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            offset++;
                            break;
                        case 3:
                            msg.putOpt(MsgParams.SteameOvenSetTime,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenSetTime_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            offset++;
                            msg.putOpt(MsgParams.SteameOvenSetTime_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            offset++;
                            break;
                        case 4:
                            msg.putOpt(MsgParams.SteameOvenSetDownTemp,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenSetDownTemp_Lenght,
                                    MsgUtils.getShort(payload[offset++]));
                            offset++;
                            msg.putOpt(MsgParams.SteameOvenSetDownTemp_Vaue,
                                    MsgUtils.getShort(payload[offset++]));
                            offset++;
                            break;
                        case 5:
                            msg.putOpt(MsgParams.SteameOvenCpMode,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenCpMode_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenCpMode_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 6:
                            msg.putOpt(MsgParams.SteameOvenRevolve,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenRevolve_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenRevolve_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 7:
                            msg.putOpt(MsgParams.SteameOvenWaterChanges,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenWaterChanges_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenWaterChanges_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 8:
                            msg.putOpt(MsgParams.SteameOvenLight,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenLight_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenLight_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 9:
                            msg.putOpt(MsgParams.SteameOvenWorkComplete,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenWorkComplete_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenWorkComplete_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 10:
                            msg.putOpt(MsgParams.SteameOvenSteam,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenSteam_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenSteam_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 11:
                            msg.putOpt(MsgParams.setSteameOvenSwitchControl,
                                    arg_key);
                            msg.putOpt(MsgParams.setSteameOvenSwitchControl_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.setSteameOvenSwitchControl_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                    }
                    arg--;
                }

                break;
            case MsgKeys.setSteameOvenBasicMode_Rep://一体机基本模式回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setTheRecipe_Rep://一体机菜谱设置回
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvenAutomaticMode_Rep://一体机自动模式回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvenFloodlight_Rep://一体机照明灯回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvenWater_Rep://水箱回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvensteam_Rep://一体机加蒸汽回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvenMultistageCooking_Rep://一体机多段烹饪回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            default:

                break;
        }
    }
}
