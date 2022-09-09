package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;

import java.nio.ByteBuffer;

import static com.robam.common.io.device.MsgParams.StoveShutDelay;

/**
 * Created by as on 2017-08-09.
 */

public class FanMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        switch (key) {
            case MsgKeys.GetSmartConfig_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                break;

            case MsgKeys.GetFanStatus_Req:

                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                break;
            case MsgKeys.SetFanStatus_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                b = (byte) msg.optInt(MsgParams.FanStatus);
                buf.put(b);

                break;
            case MsgKeys.SetFanLevel_Req:


                LogUtils.e("20190430", "SetFanLevel_Req:" + MsgKeys.SetFanLevel_Req);

                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                b = (byte) msg.optInt(MsgParams.FanLevel);
                buf.put(b);
                break;
            case MsgKeys.SetFanLight_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));

                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                bool = msg.optBoolean(MsgParams.FanLight);
                buf.put(bool ? (byte) 1 : (byte) 0);
                break;
            case MsgKeys.SetFanAllParams_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                b = (byte) msg.optInt(MsgParams.FanLevel);
                buf.put(b);
                //
                bool = msg.optBoolean(MsgParams.FanLight);
                buf.put(bool ? (byte) 1 : (byte) 0);
                break;
            case MsgKeys.RestFanCleanTime_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                    /*str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());*/
                break;
            case MsgKeys.RestFanNetBoard_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                break;
            case MsgKeys.SetFanTimeWork_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //增加 userid
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());

                b = (byte) msg.optInt(MsgParams.FanLevel);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.FanTime);
                buf.put(b);
                break;
            case MsgKeys.SetSmartConfig_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                bool = msg.optBoolean(MsgParams.IsPowerLinkage);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                bool = msg.optBoolean(MsgParams.IsLevelLinkage);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                bool = msg.optBoolean(MsgParams.IsShutdownLinkage);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.ShutdownDelay);
                buf.put(b);
                //
                bool = msg.optBoolean(MsgParams.IsNoticClean);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                bool = msg.optBoolean(MsgParams.IsTimingVentilation);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.TimingVentilationPeriod);
                buf.put(b);
                //
                bool = msg.optBoolean(MsgParams.IsWeeklyVentilation);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.WeeklyVentilationDate_Week);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.WeeklyVentilationDate_Hour);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.WeeklyVentilationDate_Minute);
                buf.put(b);
                //
                if (IRokiFamily._8230S.equals(msg.getDeviceGuid().getDeviceTypeId())
                        || IRokiFamily._8231S.equals(msg.getDeviceGuid().getDeviceTypeId())
                        || IRokiFamily._5910S.equals(msg.getDeviceGuid().getDeviceTypeId())) {
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    //
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.Key) == 1) {//变频爆炒
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.R8230SFrySwitch);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.R8230SFryTime);
                            buf.put(b);
                        }

                        if (msg.optInt(MsgParams.Key) == 2) {
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.CupOilPower);
                            buf.put(b);
                        }
                    }
                }
//                    byte[] data = new byte[buf.position()];
//                    System.arraycopy(buf.array(), 0, data, 0, data.length);
//                    LogUtils.i("20162016", Arrays.toString(data) + " 设置参数");
                break;
            case MsgKeys.FanAddPot_Req:
//                b = (byte) msg.optInt(MsgParams.TerminalType);
//                buf.put(b);
                break;
            case MsgKeys.FanDelPot_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                String optString = msg.optString(MsgParams.DeviceId);
                buf.put(optString.getBytes());

                break;

            case MsgKeys.SetFanCleanOirCupTime_Req:

                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));

                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());

                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);

                break;
            case MsgKeys.SetFanStatusCompose_Rep:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                int arg = msg.optInt(MsgParams.ArgumentNumber);
                int keyV = msg.optInt(MsgParams.Key);
                LogUtils.e("20200224", "keyV:" + keyV);
                if (arg > 0) {
                    switch (keyV) {
                        case 1:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.FanStovePower);
                            buf.put(b);
                            break;
                        case 2:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.FanPowerLink);
                            buf.put(b);
                            break;
                        case 3:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(StoveShutDelay);
                            buf.put(b);
                            break;
                        case 4:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.StoveShutDelayTime);
                            buf.put(b);
                            break;
                        case 5:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.FanCleanPower);
                            buf.put(b);
                            break;
                        case 6:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.TimeAirPower);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.TimeAirPowerDay);
                            buf.put(b);
                            break;
                        case 7:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.AirTimePower);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.AirTimeWeek);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.AirTimeHour);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.AirTimeMinute);
                            buf.put(b);
                            break;
                        case 8:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.R8230SFrySwitch);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.R8230SFryTime);
                            buf.put(b);
                            break;
                        case 9:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.CupOilPower);
                            buf.put(b);
                            break;
                        case 10:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.FanFeelPower);
                            buf.put(b);
                            break;
                        case 11:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.ProtectTipDryPower);
                            buf.put(b);
                            break;
                        case 12:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.ProtectDryPower);
                            buf.put(b);
                            break;
                        case 13:
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.gestureRecognitionSwitch);
                            buf.put(b);
                            break;
                    }
                }
                break;
            case MsgKeys.FanStatusComposeCheck_Rep:

                break;
            case MsgKeys.SetFanTimingRemind_Rep:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                b = (byte) msg.optInt(MsgParams.TimeReminderSetSwitch);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.TimeReminderSetTime);
                buf.put(b);
                break;
            default:
                break;
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        // 油烟机

        switch (key) {            // 8700/9700为通用烟机
            case MsgKeys.GetFanStatus_Rep:
                msg.putOpt(MsgParams.FanStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.FanLevel,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.FanLight,
                        MsgUtils.getShort(payload[offset++]) == 1);
                msg.putOpt(MsgParams.NeedClean,
                        MsgUtils.getShort(payload[offset++]) == 1);

                /***增加烟机wifi状态字段，因为不明确那些烟机型号支持此字段，同意try{}catch{}**/
                msg.putOpt(MsgParams.FanWIfi,
                        MsgUtils.getShort(payload[offset++]));
                short argument = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber,
                        argument);
                //取可变参数值
                while (argument > 0) {
                    short argument_key = MsgUtils.getShort(payload[offset++]);
                    short argument_length = MsgUtils.getShort(payload[offset++]);
                    LogUtils.e("20190724", "argument_key:" + argument_key + " argument_length:" + argument_length);
                    switch (argument_key) {
                        case 1:
                            msg.putOpt(MsgParams.CheckFan,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 2:
                            msg.putOpt(MsgParams.WaitTime,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 3:
                            msg.putOpt(MsgParams.GasCheck,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 4:
                            msg.putOpt(MsgParams.IsNeedCupOil,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 5:
                            short fanFellStatus = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.FanFeelStatus, fanFellStatus);

                            break;
                        case 6:
                            msg.putOpt(MsgParams.TemperatureReportOne,
                                    MsgUtils.getInt(payload, offset++));
                            offset++;
                            offset++;
                            offset++;

                            msg.putOpt(MsgParams.TemperatureReportTwo,
                                    MsgUtils.getInt(payload, offset++));
                            offset++;
                            offset++;
                            offset++;

                            break;
                        case 7://待写，取每一个bit的值进行判断是否是报警
                            msg.putOpt(MsgParams.BraiseAlarm,
                                    MsgUtils.getShort(payload[offset++]));
                            break;

                        case 8:
                            msg.putOpt(MsgParams.RegularVentilationRemainingTime,
                                    MsgUtils.getShort(payload, offset++));
                            offset++;
                            break;

                        case 9:
                            msg.putOpt(MsgParams.FanStoveLinkageVentilationRemainingTime,
                                    MsgUtils.getShort(payload, offset++));
                            offset++;
                            break;
                        case 10:
                            msg.putOpt(MsgParams.PeriodicallyRemindTheRemainingTime,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 11:
                            msg.putOpt(MsgParams.PresTurnOffRemainingTime,
                                    MsgUtils.getShort(payload, offset++));
                            offset++;
                            break;


                        default:
                            break;
                    }
                    argument--;
                }
                break;
            case MsgKeys.SetFanStatus_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SetFanLevel_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SetFanLight_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SetFanAllParams_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.RestFanCleanTime_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.RestFanNetBoard_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SetFanTimeWork_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.GetSmartConfig_Rep:
                msg.putOpt(MsgParams.IsPowerLinkage,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IsLevelLinkage,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IsShutdownLinkage,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.ShutdownDelay,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IsNoticClean,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IsTimingVentilation,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.TimingVentilationPeriod,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IsWeeklyVentilation,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.WeeklyVentilationDate_Week,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.WeeklyVentilationDate_Hour,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.WeeklyVentilationDate_Minute,
                        MsgUtils.getShort(payload[offset++]));

                //参数个数
                short argument1 = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, argument1);
                //取可变参数值
                while (argument1 > 0) {
                    short argument_key = MsgUtils.getShort(payload[offset++]);
                    switch (argument_key) {
                        case 1:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.R8230SFrySwitch,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.R8230SFryTime,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 2:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.FanCupOilSwitch,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 3:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.FanReducePower,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 4:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.gestureRecognitionSwitch,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 5:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.overTemperatureProtectionSwitch,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 6:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.overTemperatureProtectionSetTemp,
                                    MsgUtils.getShort(payload,offset++));
                            break;
                        default:
                            break;
                    }
                    argument1--;
                }

                break;
            case MsgKeys.SetSmartConfig_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;


            case MsgKeys.SetFanCleanOirCupTime_Rep://重置烟机倒油杯计回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;


            // -------------------------------------------------------------------------------
            // 通知类
            // -------------------------------------------------------------------------------

            case MsgKeys.FanEvent_Noti:
//                msg.putOpt(MsgParams.TerminalType,
//                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.EventId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.EventParam,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.UserId,
                        MsgUtils.getString(payload, offset, 10));
                offset += 10;
                msg.putOpt(MsgParams.ArgumentNumber,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.FanAddPot_Rep:
                short rc = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.RC, rc);
                break;
            case MsgKeys.FanDelPot_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SetFanStatusCompose_Req:
                short num = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, num);
                if (num > 0) {
                    short argument_key = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.Key,
                            argument_key);
                    msg.putOpt(MsgParams.RC, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.RcValue,
                            MsgUtils.getShort(payload[offset++]));
                }
                break;
            case MsgKeys.SetFanTimingRemind_Req:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            default:
                break;
        }
    }
}
