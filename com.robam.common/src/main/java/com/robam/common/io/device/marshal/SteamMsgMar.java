package com.robam.common.io.device.marshal;

import com.google.common.collect.Lists;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by as on 2017-08-09.
 */

public class SteamMsgMar {

    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;

        LogUtils.i("steam_st_Rec","marshaller:"+msg.getDeviceGuid().getGuid().toString());

        if (Utils.getDefaultSteam() != null && IRokiFamily.RS209.equals(Utils.getDefaultSteam().getGuid().getDeviceTypeId())) {
            switch (key) {
                case MsgKeys.setSteamTime_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamTemp_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamMode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamMode);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamProMode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.GetSteamOvenStatus_Req:
                    break;
                case MsgKeys.setSteamStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamStatus);
                    buf.put(b);
                    break;
            }
        }else if (IRokiFamily.RS228.equals(msg.getDeviceGuid().getGuid().substring(0,5))){
            switch (key) {
                case MsgKeys.setSteamTime_Req://设置蒸汽炉工作时间
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamTemp_Req://设置蒸汽炉工作温度
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamMode_Req://蒸汽炉烹饪模式
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SetMeum);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    int ovenRecipeId = msg.optInt(MsgParams.SteamRecipeId);
                    b = (byte) (ovenRecipeId & 0xFF);
                    buf.put(b);
                    b = (byte) ((ovenRecipeId >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeStep);
                    buf.put(b);
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
                    }
                    break;
                case MsgKeys.setSteamProMode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeId);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeStep);
                    buf.put(b);
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
                    }
                    break;
                case MsgKeys.GetSteamOvenStatus_Req:
                    str = msg.optString(MsgParams.TerminalType);
                    buf.put(str.getBytes());
                    break;
                case MsgKeys.setSteamStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamStatus);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OrderTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteamOven_Recipe_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.SteamRecipeKey) == 1) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.SteamRecipeUniqueKey) == 2) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueValue);
                            buf.put(b);
                        }

                        if (msg.optInt(MsgParams.SteamRecipeConcreteKey) == 3) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeConcreteKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeConcreteLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamTemp);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamTime);
                            buf.put(b);
                        }
                    }
                    break;
                case MsgKeys.SetSteamLightReq:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamLight);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteamWaterTankPOPReq:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    break;
                default:
                    break;
            }
        } else{// if(Utils.getDefaultSteam() != null && IRokiFamily.RS226.equals(Utils.getDefaultSteam().getGuid().getDeviceTypeId())){//蒸箱226
            switch (key) {
                case MsgKeys.setSteamTime_Req://设置蒸汽炉工作时间
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamTemp_Req://设置蒸汽炉工作温度
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamMode_Req://蒸汽炉烹饪模式
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SetMeum);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    int ovenRecipeId = msg.optInt(MsgParams.SteamRecipeId);
                    b = (byte) (ovenRecipeId & 0xFF);
                    buf.put(b);
                    b = (byte) ((ovenRecipeId >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeStep);
                    buf.put(b);
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
                    }
                    break;
                case MsgKeys.setSteamProMode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeId);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeStep);
                    buf.put(b);
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
                    }
                    break;
                case MsgKeys.GetSteamOvenStatus_Req:
                    break;
                case MsgKeys.setSteamStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamStatus);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OrderTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteamOven_Recipe_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.SteamRecipeKey) == 1) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.SteamRecipeUniqueKey) == 2) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueValue);
                            buf.put(b);
                        }

                        if (msg.optInt(MsgParams.SteamRecipeConcreteKey) == 3) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeConcreteKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeConcreteLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamTemp);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamTime);
                            buf.put(b);
                        }
                    }
                    break;
                case MsgKeys.SetSteamLightReq:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamLight);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteamWaterTankPOPReq:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    break;
            }
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        LogUtils.i("steam_st_Rec","unmarshaller:"+msg.getDeviceGuid().getGuid().toString());
        int offset = 0;
        if (Utils.getDefaultSteam() != null && IRokiFamily.RS209.equals(Utils.getDefaultSteam().getGuid().getDeviceTypeId())) {
            switch (key) {
                case MsgKeys.setSteamTime_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamTemp_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamProMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetSteamOvenStatus_Rep:
                    msg.putOpt(MsgParams.SteamLock,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamAlarm,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamMode,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTime, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.SteamDoorState,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTempSet,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTimeSet,
                            MsgUtils.getShort(payload[offset++]));


                    break;
                case MsgKeys.setSteamStatus_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOvenAlarm_Noti:
                    msg.putOpt(MsgParams.AlarmId,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOven_Noti:
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset++, 10));
                    break;
            }
        }else if (IRokiFamily.RS228.equals((msg.getDeviceGuid().getGuid()).substring(0,5))){
            LogUtils.i("steam_st_Rec","key:"+key+" getGuid(): "+msg.getDeviceGuid().getGuid());
            switch (key) {
                case MsgKeys.setSteamTime_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamTemp_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamMode_Rep:
                    List<String> list = Lists.newArrayList();
                    for (int i = 0; i < payload.length; i++) {
                        String hex = Integer.toHexString(payload[i] & 0xff);
                        if (hex.length() == 1) {
                            hex = "0" + hex;
                        }
                        list.add(hex);
                    }
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamProMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetSteamOvenStatus_Rep:
                    msg.putOpt(MsgParams.SteamLock,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamAlarm,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamMode,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.SteamDoorState,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTempSet,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTimeSet,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_hour,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_min,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamLight,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamRecipeId,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.SteamRecipeStep,
                            MsgUtils.getShort(payload[offset++]));

                    short argument = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber,
                            argument);
                    LogUtils.i("steam_st_Rec","argument:"+argument);
                    //取可变参数值
                    while (argument > 0) {
                        short argument_key = MsgUtils.getShort(payload[offset++]);
                        switch (argument_key) {
                            case 1:
                                msg.putOpt(MsgParams.SteamOvenWaterBoxKey,
                                        argument_key);
                                msg.putOpt(MsgParams.SteamOvenWaterBoxLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SteamOvenWaterBoxValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            case 2:
                                msg.putOpt(MsgParams.SteamOvenCurrentStageKey,
                                        argument_key);
                                msg.putOpt(MsgParams.SteamOvenCurrentStageLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SteamOvenCurrentStageValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            default:
                                break;
                        }
                        argument--;
                    }
                    break;
                case MsgKeys.setSteamStatus_Rep:
                    List<String> list1 = Lists.newArrayList();
                    for (int i = 0; i < payload.length; i++) {
                        String hex = Integer.toHexString(payload[i] & 0xff);
                        if (hex.length() == 1) {
                            hex = "0" + hex;
                        }
                        list1.add(hex);
                    }
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOvenAlarm_Noti:
//                        List<String> list1 = Lists.newArrayList();
//                        for (int i = 0; i < payload.length; i++) {
//                            String hex = Integer.toHexString(payload[i] & 0xff);
//                            if (hex.length() == 1) {
//                                hex = "0" + hex;
//                            }
//                            list1.add(hex);
//                        }
//                        LogUtils.i("steam_st", list1.toString());
                    msg.putOpt(MsgParams.AlarmId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.ArgumentNumber,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOven_Noti:
                       /* List<String> list1 = Lists.newArrayList();
                        for (int i = 0; i < payload.length; i++) {
                            String hex = Integer.toHexString(payload[i] & 0xff);
                            if (hex.length() == 1) {
                                hex = "0" + hex;
                            }
                            list1.add(hex);
                        }
                        LogUtils.i("8888888", list1.toString());*/
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset++, 10));
                    msg.putOpt(MsgParams.ArgumentNumber,
                            MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.GetSteamRecipeRep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                default:
                    break;

            }
        }else{// if(Utils.getDefaultSteam() != null && IRokiFamily.RS226.equals(Utils.getDefaultSteam().getGuid().getDeviceTypeId())){//226
            LogUtils.i("steam_st_Rec","key:"+key+"       226getGuid(): "+msg.getDeviceGuid().getGuid());
            switch (key) {
                case MsgKeys.setSteamTime_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamTemp_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamProMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetSteamOvenStatus_Rep:
                    msg.putOpt(MsgParams.SteamLock,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamAlarm,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamMode,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.SteamDoorState,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTempSet,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTimeSet,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_hour,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_min,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamLight,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamRecipeId,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.SteamRecipeStep,
                            MsgUtils.getShort(payload[offset++]));

                    short argument = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber,
                            argument);
                    //取可变参数值
                    while (argument > 0) {
                        short argument_key = MsgUtils.getShort(payload[offset++]);
                        switch (argument_key) {
                            case 1:
                                msg.putOpt(MsgParams.SteamOvenWaterBoxKey,
                                        argument_key);
                                msg.putOpt(MsgParams.SteamOvenWaterBoxLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SteamOvenWaterBoxValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            default:
                                break;
                        }
                        argument--;
                    }
                    break;
                case MsgKeys.setSteamStatus_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOvenAlarm_Noti:
//                        List<String> list1 = Lists.newArrayList();
//                        for (int i = 0; i < payload.length; i++) {
//                            String hex = Integer.toHexString(payload[i] & 0xff);
//                            if (hex.length() == 1) {
//                                hex = "0" + hex;
//                            }
//                            list1.add(hex);
//                        }
//                        LogUtils.i("steam_st", list1.toString());
                    msg.putOpt(MsgParams.AlarmId,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOven_Noti:
//                        List<String> list = Lists.newArrayList();
//                        for (int i = 0; i < payload.length; i++) {
//                            String hex = Integer.toHexString(payload[i] & 0xff);
//                            if (hex.length() == 1) {
//                                hex = "0" + hex;
//                            }
//                            list.add(hex);
//                        }
//                        LogUtils.i("steam_st", list.toString());
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset++, 10));
                    break;

                case MsgKeys.GetSteamRecipeRep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;

            }
        }
    }
}
