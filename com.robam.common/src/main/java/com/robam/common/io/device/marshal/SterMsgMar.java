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

public class SterMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;

        if(null != Utils.getDefaultSterilizer() && IRokiFamily.RR826 .equals(Utils.getDefaultSterilizer().getGuid().getDeviceTypeId())){

            switch (key) {
                case MsgKeys.SetSteriPowerOnOff_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    //
                    b = (byte) msg.optInt(MsgParams.SteriStatus);
                    buf.put(b);
                    short setTime = (short) msg.optInt(MsgParams.SteriTime);
                    s = (short) (setTime & 0xFF);
                    buf.putShort(s);
//                    s = (short) ((setTime >> 8) & 0xFF);
//                    buf.putShort(s);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
//                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
//                        if (msg.optInt(MsgParams.Key) == 1) {
//                            b = (byte) msg.optInt(MsgParams.Key);
//                            buf.put(b);
//                            b = (byte) msg.optInt(MsgParams.Length);
//                            buf.put(b);
//                            b = (byte) msg.optInt(MsgParams.warmDishTempValue);
//                            buf.put(b);
//                        }
//                    }
                    break;


                case MsgKeys.GetSteriStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    break;


                case MsgKeys.GetSteriPVConfig_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    break;


                case MsgKeys.SetSteriPVConfig_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    bool = msg.optBoolean(MsgParams.SteriSwitchDisinfect);
                    buf.put(bool ? (byte) 1 : (byte) 0);

                    b = (byte) msg.optInt(MsgParams.SteriInternalDisinfect);
                    buf.put(b);

                    bool = msg.optBoolean(MsgParams.SteriSwitchWeekDisinfect);
                    buf.put(bool ? (byte) 1 : (byte) 0);

                    b = (byte) msg.optInt(MsgParams.SteriWeekInternalDisinfect);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteriPVDisinfectTime);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;

                case MsgKeys.SetSteri826Lock_Req:
//                    str = msg.optString(MsgParams.UserId);
//                    buf.put(str.getBytes());
//                    b = (byte) msg.optInt(MsgParams.SteriLock);

                    b = (byte) msg.optInt(MsgParams.SteriLock);
                    LogUtils.i("R826_5","b:"+b);
                    buf.put(b);
                    break;

//                case MsgKeys.SetSteriLock_Req:
//                    str = msg.optString(MsgParams.UserId);
//                    buf.put(str.getBytes());
//                    b = (byte) msg.optInt(MsgParams.SteriLock);
//                    buf.put(b);
//                    break;

            }


        }else if(null != Utils.getDefaultSterilizer() && IRokiFamily.RR829 .equals(Utils.getDefaultSterilizer().getGuid().getDeviceTypeId())){
            switch (key) {
                case MsgKeys.SetSteriPowerOnOff_Req:
//					b = (byte)msg.optInt(MsgParams.TerminalType);
//					buf.put(MsgUtils.toByte(b));
                    //
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    //
                    b = (byte) msg.optInt(MsgParams.SteriStatus);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteriReserveTime_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteriReserveTime);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteriDrying_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteriDryingTime);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteriClean_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteriCleanTime);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteriDisinfect_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteriDisinfectTime);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteriLock_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteriLock);
                    buf.put(b);
                    break;
                case MsgKeys.GetSteriParam_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    break;
                case MsgKeys.GetSteriStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    break;
                case MsgKeys.GetSteriPVConfig_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    break;
                case MsgKeys.SetSteriPVConfig_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    bool = msg.optBoolean(MsgParams.SteriSwitchDisinfect);
                    buf.put(bool ? (byte) 1 : (byte) 0);

                    b = (byte) msg.optInt(MsgParams.SteriInternalDisinfect);
                    buf.put(b);

                    bool = msg.optBoolean(MsgParams.SteriSwitchWeekDisinfect);
                    buf.put(bool ? (byte) 1 : (byte) 0);

                    b = (byte) msg.optInt(MsgParams.SteriWeekInternalDisinfect);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteriPVDisinfectTime);
                    buf.put(b);
                    break;
                default:
                    break;
            }
        }

    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;

        if(null != Utils.getDefaultSterilizer() && IRokiFamily.RR826 .equals(Utils.getDefaultSterilizer().getGuid().getDeviceTypeId())){
            switch (key) {

                case MsgKeys.SetSteriPowerOnOff_Rep:
                    msg.putOpt(MsgParams.RC, MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.GetSteriStatus_Rep:
                    msg.putOpt(MsgParams.SteriStatus, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriLock, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriWorkLeftTimeL, MsgUtils.getInt(payload, offset++));
                    offset++;
                    offset++;
                    offset++;
                    msg.putOpt(MsgParams.SteriDoorLock, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriAlarmStatus, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriParaTem, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriParaHum, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriParaGerm, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriParaOzone, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.ArgumentNumber, MsgUtils.getShort(payload[offset++]));

                    break;

                case MsgKeys.GetSteriPVConfig_Rep:
                    msg.putOpt(MsgParams.SteriSwitchDisinfect, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriInternalDisinfect, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriSwitchWeekDisinfect, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriWeekInternalDisinfect, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriPVDisinfectTime, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.ArgumentNumber, MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.SetSteriPVConfig_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;

                // 通知类
                case MsgKeys.SteriAlarm_Noti:
                    msg.putOpt(MsgParams.AlarmId, MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.SteriEvent_Noti:
                    msg.putOpt(MsgParams.EventId, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.ArgumentNumber, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId, MsgUtils.getString(payload, offset++, 10));
                    break;

//                case MsgKeys.SetSteriLock_Rep:
//                    msg.putOpt(MsgParams.RC,
//                            MsgUtils.getShort(payload[offset++]));
//                    break;

                case MsgKeys.SetSteri826Lock_Rep:
                    msg.putOpt(MsgParams.RC, MsgUtils.getShort(payload[offset++]));
                    break;
            }

        }else{
            switch (key) {
                case MsgKeys.SetSteriPowerOnOff_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SetSteriReserveTime_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SetSteriDrying_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SetSteriClean_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SetSteriDisinfect_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SetSteriLock_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetSteriParam_Rep:
                    msg.putOpt(MsgParams.SteriParaTem,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriParaHum,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriParaGerm,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriParaOzone,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetSteriStatus_Rep:
                    msg.putOpt(MsgParams.SteriStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriLock,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriWorkLeftTimeL,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriWorkLeftTimeH,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriAlarmStatus,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetSteriPVConfig_Rep:
                    msg.putOpt(MsgParams.SteriSwitchDisinfect,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriInternalDisinfect,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriSwitchWeekDisinfect,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriWeekInternalDisinfect,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteriPVDisinfectTime,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SetSteriPVConfig_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                // -------------------------------------------------------------------------------
                // 通知类
                // -------------------------------------------------------------------------------
                case MsgKeys.SteriAlarm_Noti:
                    msg.putOpt(MsgParams.AlarmId, MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.SteriEvent_Noti:
                    msg.putOpt(MsgParams.EventId, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam, MsgUtils.getShort(payload[offset++]));
                    break;
            }
        }

    }
}
