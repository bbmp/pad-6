package com.robam.common.io.device;

import com.legent.plat.io.device.AbsPlatProtocol;
import com.legent.plat.io.device.IAppMsgMarshaller;
import com.legent.plat.io.device.msg.Msg;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.io.device.marshal.FanMsgMar;
import com.robam.common.io.device.marshal.PotMsgMar;
import com.robam.common.io.device.marshal.StoveMsgMar;
import com.robam.common.pojos.device.Pot.Pot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RokiMsgMarshaller implements IAppMsgMarshaller {

    private static final String TAG = "RokiMsgMarshaller";

    static public final int BufferSize = AbsPlatProtocol.BufferSize;
    static public final ByteOrder BYTE_ORDER = AbsPlatProtocol.BYTE_ORDER;

    @Override
    public byte[] marshal(Msg msg) throws Exception {

        int key = msg.getID();
        ByteBuffer buf = ByteBuffer.allocate(BufferSize).order(BYTE_ORDER);
        LogUtils.e(TAG,"isPotMsg:" + isPotMsg(msg));
        LogUtils.e(TAG,"key:" + key);
        if (isStoveMsg(msg)) {
            StoveMsgMar.marshaller(key, msg, buf);
        } else if (isFanMsg(msg)) {
            FanMsgMar.marshaller(key, msg, buf);
        } else if (isPotMsg(msg)) {
            PotMsgMar.marshaller(key, msg, buf);
        }

        byte[] data = new byte[buf.position()];
        System.arraycopy(buf.array(), 0, data, 0, data.length);
        buf.clear();
        return data;
    }

    @Override
    public void unmarshal(Msg msg, byte[] payload) throws Exception {
        int key = msg.getID();
        if (isStoveMsg(msg)) {
            StoveMsgMar.unmarshaller(key, msg, payload);
        } else if (isFanMsg(msg)) {
            FanMsgMar.unmarshaller(key, msg, payload);
        } else if (isPotMsg(msg)) {
            PotMsgMar.unmarshaller(key, msg, payload);
        }
    }

    private boolean isStoveMsg(Msg msg) {
        return Utils.isStove(msg.getDeviceGuid().getGuid());
    }

    private boolean isFanMsg(Msg msg) {  //判断是否为油烟机 by zhaiyuanyi 20151120
        return Utils.isFan(msg.getDeviceGuid().getGuid());
    }

    private boolean isSterilizer(Msg msg) {//判断是否为消毒柜 by zhaiyuanyi 20151120
        return Utils.isSterilizer(msg.getDeviceGuid().getGuid());
    }

    private boolean isSteamMsg(Msg msg) {
        return Utils.isSteam(msg.getDeviceGuid().getGuid());
    }

    private boolean isMicroWaveMsg(Msg msg) {
        return Utils.isMicroWave(msg.getDeviceGuid().getGuid());
    }


    private boolean isOvenMsg(Msg msg) {  //判断是否为油烟机 by linxiaobin 20151220
        return Utils.isOven(msg.getDeviceGuid().getGuid());
    }

    //判断是否为净水器
    private boolean isWaterPurifier(Msg msg) {
        return Utils.isWaterPurifier(msg.getDeviceGuid().getGuid());
    }

    private boolean isPotMsg(Msg msg) {
        return Utils.isPot(msg.getDeviceGuid().getGuid());
    }

    private boolean isSteamOvenMsg(Msg msg) {
        return Utils.isSteamOvenMsg(msg.getDeviceGuid().getGuid());
    }

}
