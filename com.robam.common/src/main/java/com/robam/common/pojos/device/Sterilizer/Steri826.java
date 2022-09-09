package com.robam.common.pojos.device.Sterilizer;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.SteriAlarmEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by zhaiyuanyi on 15/11/19.
 */
public class Steri826 extends AbsSterilizer {
    public short doorLock;
    public short isChildLock;

    public Steri826(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getSterilizerModel() {
        return IRokiFamily.RR826;
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
                    Steri826.this.status = (short) msg.optInt(MsgParams.SteriStatus);
//                    Steri826.this.isChildLock = msg.optBoolean(MsgParams.SteriLock);
                    Steri826.this.isChildLock = (short) msg.optInt(MsgParams.SteriLock);
                    Steri826.this.work_left_time_l = (short) msg.optInt(MsgParams.SteriWorkLeftTimeL);
                    Steri826.this.work_left_time_h = (short) msg.optInt(MsgParams.SteriWorkLeftTimeH);
                    Steri826.this.doorLock = (short) msg.optInt(MsgParams.SteriDoorLock);
                    Steri826.this.AlarmStautus = (short) msg.optInt(MsgParams.SteriAlarmStatus);
                    Steri826.this.temp = (short) msg.optInt(MsgParams.SteriParaTem);
                    Steri826.this.hum = (short) msg.optInt(MsgParams.SteriParaHum);
                    Steri826.this.germ = (short) msg.optInt(MsgParams.SteriParaGerm);
                    Steri826.this.ozone = (short) msg.optInt(MsgParams.SteriParaOzone);

                    LogUtils.i("R826_4"," status:"+ status+ " doorLock:"+doorLock +" isChildLock:"+isChildLock+" AlarmStautus:"+AlarmStautus
                            + " work_left_time_l:"+ work_left_time_l +" temp:"+ temp+" hum:"+ hum+" germ:"+germ+" ozone:"+ozone);
                    onStatusChanged();
                    break;
                case MsgKeys.GetSteriParam_Rep:
                    Steri826.this.temp = (short) msg.optInt(MsgParams.SteriParaTem);
                    Steri826.this.hum = (short) msg.optInt(MsgParams.SteriParaHum);
                    Steri826.this.germ = (short) msg.optInt(MsgParams.SteriParaGerm);
                    Steri826.this.ozone = (short) msg.optInt(MsgParams.SteriParaOzone);
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

    //设置铜锁相关
    public void setSteriLock(final short status, final VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteri826Lock_Req);
//            msg.putOpt(MsgParams.TerminalType, terminalType);
//            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriLock, status);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri826.this.isChildLock = isChildLock;
//                    Steri826.this.isChildLock =  status  == 0;
//                    LogUtils.i("R826_5"," Steri826.this.isChildLock:"+ Steri826.this.isChildLock);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
