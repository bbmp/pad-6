package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;

import java.nio.ByteBuffer;

/**
 * Created by as on 2017-08-09.
 */

public class OvenMsgMar {

    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        if (Utils.getDefaultOven() != null && IRokiFamily.RR039.equals(Utils.getDefaultOven().getGuid().getDeviceTypeId())) {
            switch (key) {
                case MsgKeys.setOvenStatusControl_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.OvenStatus);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenQuickHeat_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.OvenTemp);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.OvenTime);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenAirBaking_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenToast_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenBottomHeat_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenUnfreeze_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenAirBarbecue_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenBarbecue_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenStrongBarbecue_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenSpitRotateLightControl_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenRevolve);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.OvenLight);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.getOvenStatus_Req:
                    break;
                default:
                    break;
            }
        } else if (Utils.getDefaultOven() != null && (IRokiFamily.RR026.equals(Utils.getDefaultOven().getGuid().getDeviceTypeId())
                ||IRokiFamily.RR016.equals(Utils.getDefaultOven().getGuid().getDeviceTypeId()))
                ||IRokiFamily.HK906.equals(Utils.getDefaultOven().getGuid().getDeviceTypeId())){//026烤箱
            switch (key) {
                case MsgKeys.setOvenStatusControl_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenStatus);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenSpitRotateLightControl_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenRevolve);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenLight);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.getOvenStatus_Req:
                    break;

                //新增烤箱指令
                case MsgKeys.SetOven_RunMode_Req:

                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenMode);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);

                    int ovenRecipeId = msg.optInt(MsgParams.OvenRecipeId);
                    b = (byte) (ovenRecipeId & 0xFF);
                    buf.put(b);
                    b = (byte) ((ovenRecipeId >> 8) & 0xFF);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.OvenRecipeStep);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.SetTempDownKey) == 1) {
                            b = (byte) msg.optInt(MsgParams.SetTempDownKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SetTempDownLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SetTempDownValue);
                            buf.put(b);
                        }

                        if (msg.optInt(MsgParams.OrderTime_key) == 2) {
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

                case MsgKeys.SetSteamOven_Recipe_Req:
                    //疑问
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenRecipeId);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenRecipeTotalStep);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenRecipeStep);
                    buf.put(b);

                    break;

                case MsgKeys.Set_Oven_Auto_Mode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.ovenAutoMode);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.OvenSetTime);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);

                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {//可变长参数判断
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
                case MsgKeys.Set_Oven_Light_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenLight);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                default:
                    break;
            }
        } else if (IRokiFamily.RR028.equals(Utils.getDefaultOven().getGuid().getDeviceTypeId())) {
            switch (key) {
                case MsgKeys.setOvenStatusControl_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenStatus);
                    buf.put(b);
                    break;
                case MsgKeys.setOvenSpitRotateLightControl_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenRevolve);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.getOvenStatus_Req:
                    break;

                //新增烤箱指令
                case MsgKeys.SetOven_RunMode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenMode);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);

                    int ovenRecipeId = msg.optInt(MsgParams.OvenRecipeId);
                    b = (byte) (ovenRecipeId & 0xFF);
                    buf.put(b);
                    b = (byte) ((ovenRecipeId >> 8) & 0xFF);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.OvenRecipeStep);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.SetTempDownKey) == 1) {
                            b = (byte) msg.optInt(MsgParams.SetTempDownKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SetTempDownLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SetTempDownValue);
                            buf.put(b);
                        }

                        if (msg.optInt(MsgParams.OrderTime_key) == 2) {
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

                case MsgKeys.SetSteamOven_Recipe_Req:
                    //疑问
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenRecipeId);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenRecipeTotalStep);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenRecipeStep);
                    buf.put(b);

                    break;

                case MsgKeys.Set_Oven_Auto_Mode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.ovenAutoMode);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.OvenSetTime);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);

                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {//可变长参数判断
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
                case MsgKeys.Set_Oven_Light_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenLight);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.Set_Oven_More_Cook://164
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                    buf.put(b);
                    int ovenRecipe028Id = msg.optInt(MsgParams.OvenRecipeId);
                    b = (byte) (ovenRecipe028Id & 0xFF);
                    buf.put(b);
                    b = (byte) ((ovenRecipe028Id >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OvenRecipeStep);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {//可变长参数判断
                        if (msg.optInt(MsgParams.OvenStagekey) == 1) {
                            b = (byte) msg.optInt(MsgParams.OvenStagekey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStageLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStageValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.OvenStep1Modekey) == 2) {
                            b = (byte) msg.optInt(MsgParams.OvenStep1Modekey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep1ModeLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep1ModeValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.OvenStep1SetTempkey) == 3) {
                            b = (byte) msg.optInt(MsgParams.OvenStep1SetTempkey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep1SetTempLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep1SetTempValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.OvenStep1SetTimekey) == 4) {
                            b = (byte) msg.optInt(MsgParams.OvenStep1SetTimekey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep1SetTimeLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep1SetTimeValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.OvenStep2Modekey) == 5) {
                            b = (byte) msg.optInt(MsgParams.OvenStep2Modekey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep2ModeLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep2ModeValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.OvenStep2SetTempkey) == 6) {
                            b = (byte) msg.optInt(MsgParams.OvenStep2SetTempkey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep2SetTempLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep2SetTempValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.OvenStep2SetTimekey) == 7) {
                            b = (byte) msg.optInt(MsgParams.OvenStep2SetTimekey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep2SetTimeLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep2SetTimeValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.OvenStep3Modekey) == 8) {
                            b = (byte) msg.optInt(MsgParams.OvenStep3Modekey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep3ModeLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep3ModeValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.OvenStep3SetTempkey) == 9) {
                            b = (byte) msg.optInt(MsgParams.OvenStep3SetTempkey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep3SetTempLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep3SetTempValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.OvenStep2SetTimekey) == 10) {
                            b = (byte) msg.optInt(MsgParams.OvenStep3SetTimekey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep3SetTimeLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OvenStep3SetTimeValue);
                            buf.put(b);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        if (Utils.getDefaultOven() != null && IRokiFamily.RR039.equals(Utils.getDefaultOven().getGuid().getDeviceTypeId())) {
            switch (key) {
                case MsgKeys.setOvenStatusControl_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setOvenQuickHeat_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setOvenAirBaking_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setOvenAirBarbecue_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setOvenToast_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setOvenBottomHeat_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setOvenUnfreeze_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setOvenStrongBarbecue_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setOvenBarbecue_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setOvenSpitRotateLightControl_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.getOvenStatus_Rep:
                    msg.putOpt(MsgParams.OvenStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenAlarm,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenRunP,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenRevolve,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.OvenLight,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenSetTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenSetTime,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.OvenAlarm_Noti:
                    msg.putOpt(MsgParams.AlarmId,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.Oven_Noti:
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset++, 10));
                    break;
            }

        } else if (IRokiFamily.RR026.equals(Utils.getDefaultOven().getGuid().getDeviceTypeId())
                || IRokiFamily.RR016.equals(Utils.getDefaultOven().getGuid().getDeviceTypeId())
                || IRokiFamily.HK906.equals(Utils.getDefaultOven().getGuid().getDeviceTypeId())) {//026
            switch (key) {
                case MsgKeys.setOvenStatusControl_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.setOvenSpitRotateLightControl_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.getOvenStatus_Rep:
                    msg.putOpt(MsgParams.OvenStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenAlarm,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenRunP,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenRevolve,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.OvenLight,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenSetTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenSetTime,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_min,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_hour,
                            MsgUtils.getShort(payload[offset++]));
                    //026
                    msg.putOpt(MsgParams.ovenAutoMode,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenRecipeId,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.OvenRecipeStep,
                            MsgUtils.getShort(payload[offset++]));
                    short argument = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber,
                            argument);
                    //取可变参数值
                    while (argument > 0) {
                        short argument_key = MsgUtils.getShort(payload[offset++]);
                        switch (argument_key) {
                            case 1:
                                msg.putOpt(MsgParams.SetTempDownKey,
                                        argument_key);
                                msg.putOpt(MsgParams.SetTempDownLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SetTempDownValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            case 2:
                                msg.putOpt(MsgParams.CurrentTempDownKey,
                                        argument_key);
                                msg.putOpt(MsgParams.CurrentTempDownLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.CurrentTempDownValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            default:
                                break;
                        }
                        argument--;
                    }
                    break;
                case MsgKeys.OvenAlarm_Noti:
                    msg.putOpt(MsgParams.AlarmId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.ArgumentNumber,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.Oven_Noti:
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset++, 10));
                    msg.putOpt(MsgParams.ArgumentNumber,
                            MsgUtils.getShort(payload[offset++]));
                    if (msg.optInt(MsgParams.SetTempDownKey) == 1) {
                        msg.putOpt(MsgParams.SetTempDownKey,
                                MsgUtils.getShort(payload[offset++]));
                        msg.putOpt(MsgParams.SetTempDownLength,
                                MsgUtils.getShort(payload[offset++]));
                        msg.putOpt(MsgParams.SetTempDownValue,
                                MsgUtils.getShort(payload[offset++]));

                    }
                    break;

                //新增烤箱指令
                case MsgKeys.GetOven_RunMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetOven_Recipe_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.Get_Oven_Auto_Mode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.Get_Oven_Light_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                default:
                    break;

            }
        } else if (IRokiFamily.RR028.equals(Utils.getDefaultOven().getGuid().getDeviceTypeId())) {
            switch (key) {
                case MsgKeys.setOvenStatusControl_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.setOvenSpitRotateLightControl_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.getOvenStatus_Rep:
//                        List<String> list = Lists.newArrayList();
//                        for (int i = 0; i < payload.length; i++) {
//                            String hex = Integer.toHexString(payload[i] & 0xff);
//                            if (hex.length() == 1) {
//                                hex = "0" + hex;
//                            }
//                            list.add(hex);
//                        }
                    msg.putOpt(MsgParams.OvenStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenAlarm,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenRunP,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenRevolve,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.OvenLight,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenSetTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenSetTime,
                            MsgUtils.getShort(payload[offset++]));

                    msg.putOpt(MsgParams.OrderTime_value_min,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_hour,
                            MsgUtils.getShort(payload[offset++]));
                    //026
                    msg.putOpt(MsgParams.ovenAutoMode,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OvenRecipeId,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.OvenRecipeStep,
                            MsgUtils.getShort(payload[offset++]));
                    short argument = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber,
                            argument);
                    //取可变参数值
                    while (argument > 0) {
                        short argument_key = MsgUtils.getShort(payload[offset++]);
                        switch (argument_key) {
                            case 1:
                                msg.putOpt(MsgParams.SetTempDownKey,
                                        argument_key);
                                msg.putOpt(MsgParams.SetTempDownLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SetTempDownValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            case 2:
                                msg.putOpt(MsgParams.CurrentTempDownKey,
                                        argument_key);
                                msg.putOpt(MsgParams.CurrentTempDownLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.CurrentTempDownValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            case 3:
                                msg.putOpt(MsgParams.CurrentStatusKey,
                                        argument_key);
                                msg.putOpt(MsgParams.CurrentStatusLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.CurrentStatusValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            case 4:
                                msg.putOpt(MsgParams.TotalKey,
                                        argument_key);
                                msg.putOpt(MsgParams.TotalLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.TotalValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            default:
                                break;
                        }
                        argument--;
                    }
                    break;
                case MsgKeys.OvenAlarm_Noti:
                    msg.putOpt(MsgParams.AlarmId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.ArgumentNumber,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.Oven_Noti:
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset++, 10));
                    msg.putOpt(MsgParams.ArgumentNumber,
                            MsgUtils.getShort(payload[offset++]));
                    if (msg.optInt(MsgParams.SetTempDownKey) == 1) {
                        msg.putOpt(MsgParams.SetTempDownKey,
                                MsgUtils.getShort(payload[offset++]));
                        msg.putOpt(MsgParams.SetTempDownLength,
                                MsgUtils.getShort(payload[offset++]));
                        msg.putOpt(MsgParams.SetTempDownValue,
                                MsgUtils.getShort(payload[offset++]));

                    }
                    break;

                //新增烤箱指令
                case MsgKeys.GetOven_RunMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetOven_Recipe_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.Get_Oven_Auto_Mode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.Get_Oven_Light_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.Get_Oven_More_Cook:
                    msg.putOpt(MsgParams.ArgumentNumber,
                            MsgUtils.getShort(payload[offset++]));
                    break;

            }
        }
    }
}
