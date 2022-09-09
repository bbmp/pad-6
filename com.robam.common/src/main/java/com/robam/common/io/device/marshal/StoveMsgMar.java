package com.robam.common.io.device.marshal;

import com.google.common.collect.Lists;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.Stove.Stove;

import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by as on 2017-08-09.
 */

public class StoveMsgMar {
    private static final String TAG = "StoveMsgMar";

    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        // 电磁灶
        switch (key) {

            case MsgKeys.GetStoveStatus_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                break;
            case MsgKeys.SetStoveStatus_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                bool = msg.optBoolean(MsgParams.IsCook);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.IhId);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.IhStatus);
                buf.put(b);

                break;
            case MsgKeys.SetStoveLevel_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                bool = msg.optBoolean(MsgParams.IsCook);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.IhId);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.IhLevel);
                LogUtils.e(TAG, "MsgParams.IhLevel:" + msg.optInt(MsgParams.IhLevel) + " levle byte:" + b);
                buf.put(b);
                break;
            case MsgKeys.SetStoveShutdown_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                //
                b = (byte) msg.optInt(MsgParams.IhId);
                buf.put(b);

                //
                s = (short) msg.optInt(MsgParams.IhTime);
                buf.putShort(s);
                break;
            case MsgKeys.SetStoveLock_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                //
                bool = msg.optBoolean(MsgParams.StoveLock);
                buf.put(bool ? (byte) 1 : (byte) 0);

                break;

            default:
                break;
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        LogUtils.e(TAG,"unmarshaller key:"+key+" payload:"+StringUtils.bytes2Hex(payload));
        int offset = 0;
        // 电磁灶
        switch (key) {
            case MsgKeys.GetStoveStatus_Rep:
                LogUtils.e(TAG, "unmarshaller:" + StringUtils.bytes2Hex(payload) + "payload length:" + payload.length);
                int num = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.IhNum, num);
                msg.putOpt(MsgParams.StoveLock,
                        MsgUtils.getShort(payload[offset++]) == 1);

                List<Stove.StoveHead> list = Lists.newArrayList();
                for (short i = 0; i < num; i++) {
                    Stove.StoveHead head = new Stove.StoveHead(i);
                    head.status = MsgUtils.getShort(payload[offset++]);
                    head.level = MsgUtils.getShort(payload[offset++]);
                    head.time = MsgUtils.getShort(payload, offset);
                    offset += 2;
                    head.alarmId = MsgUtils.getShort(payload[offset++]);
                    LogUtils.e(TAG, " head.time :" + head.time + " status:" + head.status + " level:" + head.level + " worktime:" + head.worktime);
                    list.add(head);
                }
                LogUtils.e(TAG, "payload length:" + payload.length + " offset：" + offset);
                if (offset < payload.length) {
                    int messageNum = MsgUtils.getShort(payload[offset++]);
                    if (messageNum > 0) {
                        for (int i = 0; i < messageNum; i++) {
                            char keyChar = (char) MsgUtils.getShort(payload[offset++]);
//                            LogUtils.e(TAG, "keyChar:" + keyChar);
                            switch (keyChar) {
                                case 'A':
                                    int keyALen = MsgUtils.getShort(payload[offset++]);
                                    short recipeAID = MsgUtils.getShort(payload, offset);
                                    offset += 2;
                                    short recipeAStep = MsgUtils.getShort(payload[offset++]);
//                                    LogUtils.e(TAG, "keyALen:" + keyALen + " recipeAID:" + recipeAID + " recipeAStep:" + recipeAStep);
                                    break;
                                case 'B':
                                    int keyBLen = MsgUtils.getShort(payload[offset++]);
                                    int recipeBID = MsgUtils.getShort(payload, offset);
                                    offset += 2;
                                    int recipeBStep = MsgUtils.getShort(payload[offset++]);
//                                    LogUtils.e(TAG, "keyBLen:" + keyBLen + " recipeBID:" + recipeBID + " recipeBStep:" + recipeBStep);
                                    break;
                                case 'C':
                                    int keyCLen = MsgUtils.getShort(payload[offset++]);
                                    short leftWorkTime = MsgUtils.getShort(payload, offset);
                                    offset += 2;
//                                    LogUtils.e(TAG, "keyCLen:" + keyCLen + " leftWorkTime:" + leftWorkTime);
                                    if (!list.isEmpty()) {
                                        list.get(0).worktime = leftWorkTime;
                                    }
                                    break;
                                case 'D':
                                    int keyDLen = MsgUtils.getShort(payload[offset++]);
                                    short rightWorkTime = MsgUtils.getShort(payload, offset);
//                                    LogUtils.e(TAG, "keyDLen:" + keyDLen + " rightWorkTime:" + rightWorkTime);
                                    offset += 2;
                                    list.get(1).worktime = rightWorkTime;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }

                msg.putOpt(MsgParams.StoveHeadList, list);
                break;
            case MsgKeys.SetStoveStatus_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));

                break;
            case MsgKeys.SetStoveLevel_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));

                break;
            case MsgKeys.SetStoveShutdown_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));

                break;
            case MsgKeys.SetStoveLock_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;

            // -------------------------------------------------------------------------------
            // 通知类
            // -------------------------------------------------------------------------------

            case MsgKeys.StoveAlarm_Noti:
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.AlarmId,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.StoveEvent_Noti:
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.EventId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.EventParam,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.StoveTemp_Noti:        //灶具温度参数上报
                msg.putOpt(MsgParams.Pot_Temp,
                        MsgUtils.getFloat(payload, offset));
                break;


            default:
                break;
        }
    }
}
