package com.robam.common.pojos.device.Stove;

import com.google.common.base.Preconditions;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.SubDeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.events.StoveAlarmEvent;
import com.robam.common.events.StoveLevelEvent;
import com.robam.common.events.StovePowerEvent;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.services.StoveAlarmManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Stove extends AbsDevice implements IStove {
    /**
     * 用于温控锅变量
     **/
    long currenmimle;
    Timer tempPlotConnectTimer;
    public float tempUpNum = -1;
    //温控锅 离线状态标志位
    public boolean TempPlotIsConnect;

    /********************/
    static final public short PowerLevel_0 = 0;
    static final public short PowerLevel_1 = 1;
    static final public short PowerLevel_2 = 2;
    static final public short PowerLevel_3 = 3;
    static final public short PowerLevel_4 = 4;
    static final public short PowerLevel_5 = 5;
    static final public short PowerLevel_6 = 6;
    static final public short PowerLevel_7 = 7;
    static final public short PowerLevel_8 = 8;
    static final public short PowerLevel_9 = 9;
    static final public short PowerLevel_10 = 10;

    static final public short Event_Power = 11;
    static final public short Event_Level = 12;

    public StoveHead leftHead, rightHead;
    public boolean isLock = false;
    protected short terminalType = TerminalType.getType();



    public Stove(SubDeviceInfo devInfo) {
        super(devInfo);
        leftHead = new StoveHead(StoveHead.LEFT_ID);
        leftHead.parent = this;

        rightHead = new StoveHead(StoveHead.RIGHT_ID);
        rightHead.parent = this;
    }

    public StoveHead getHeadById(int headId) {
        return headId == StoveHead.LEFT_ID ? leftHead : rightHead;
    }

    // -------------------------------------------------------------------------------
    // IDevice
    // -------------------------------------------------------------------------------

    @Override
    public void onPolling() {
        try {
            if (Plat.DEBUG)
                LogUtils.i("stove_polling", "灶具stove onPolling" + this.getID());
            Msg reqMsg = newReqMsg(MsgKeys.GetStoveStatus_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);
            reqMsg.setIsFan(true);
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onReceivedMsg(Msg msg) {
        int key = msg.getID();
        if (MsgKeys.StoveTemp_Noti != key) {
            super.onReceivedMsg(msg);
        }

        switch (key) {
            case MsgKeys.StoveAlarm_Noti:
                if (this.getID() != null && this.getID().equals(Utils.getDefaultStove()[0].getID())) {
                    short ihId = (short) msg.optInt(MsgParams.IhId);
                    short alarmId = (short) msg.optInt(MsgParams.AlarmId);
                    postEvent(new StoveAlarmEvent(this, getHeadById(ihId), alarmId));
                }
                break;
            case MsgKeys.StoveEvent_Noti:
                short eventId = (short) msg.optInt(MsgParams.EventId);
                short eventParam = (short) msg.optInt(MsgParams.EventParam);
                switch (eventId) {
                    //Event_Power 11 Event_Level 12
                    case Event_Power:
                        if (this.getID() != null && this.getID().equals(Utils.getDefaultStove()[0].getID())) {
                            postEvent(new StovePowerEvent(this, 2 == eventParam));
                        }
                        break;
                    case Event_Level:
                        if (this.getID() != null && this.getID().equals(Utils.getDefaultStove()[0].getID())) {
                            postEvent(new StoveLevelEvent(this, eventParam));
                        }
                        break;
                }
                break;
            //温控锅温度事件上报处理
            case MsgKeys.StoveTemp_Noti:
                //Test_Rent
                long cu = System.currentTimeMillis();
                if (currenmimle != 0) {
                    if (cu - currenmimle < 500)
                        return;
                }
                currenmimle = cu;
                if (tempPlotConnectTimer != null) {
                    tempPlotConnectTimer.cancel();
                    tempPlotConnectTimer.purge();
                    tempPlotConnectTimer = null;
                }
                TempPlotIsConnect = true;
                tempPlotConnectTimer = new Timer();
                tempPlotConnectTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // ToastUtils.show(new String("温控锅因为未知原因离线"), Toast.LENGTH_SHORT);
                        TempPlotIsConnect = false;
                        tempUpNum = -1;
                    }
                }, 4000);
                float temp1 = (float) msg.optDouble(MsgParams.Pot_Temp);
                try {
                    // tempUpNum = Math.round(temp1 * 100);
                    tempUpNum = temp1;
                } catch (Exception e) {
                    tempUpNum = temp1;
                }
                if (Plat.DEBUG)
                    LogUtils.e("stove_temp", "temp1  " + temp1 + " stv " + this.getDc() + "  " + this.getDp() + "  " + this.getDt());
//                stoveTempEvent.temp = temp1;
//                stoveTempEvent.stove = this;
//                EventUtils.postEvent(stoveTempEvent);
                break;
            case MsgKeys.GetStoveStatus_Rep:
                Stove.this.isLock = msg.optBoolean(MsgParams.StoveLock);
                if (Plat.DEBUG)
                    LogUtils.e("stove_polling", " isLock:" + isLock + " leftHead.level:" + Stove.this.leftHead.level + " rightHead.level:" + Stove.this.rightHead.level
                            + " leftHead.status:" + Stove.this.leftHead.status + " rightHead.status:" + Stove.this.rightHead.status);
                List<StoveHead> heads = (List<StoveHead>) msg.opt(MsgParams.StoveHeadList);

                if (heads != null) {
                    onGetHeads(heads);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    @Override
    public void pause() {
        leftHead.pause();
        rightHead.pause();
    }

    @Override
    public void restore() {
        leftHead.restore();
        rightHead.restore();
    }

    @Override
    public String getStoveModel() {
        return IRokiFamily.R9W70;
    }

    @Override
    public void getStoveStatus(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetStoveStatus_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {

                    Stove.this.isLock = resMsg.optBoolean(MsgParams.StoveLock);
                    List<StoveHead> heads = (List<StoveHead>) resMsg.opt(MsgParams.StoveHeadList);
                    if (heads != null) {
                        onGetHeads(heads);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setStoveStatus(final boolean isCook, final short ihId,
                               final short ihStatus, final VoidCallback callback) {
        checkHeadId(ihId);

        try {
            Msg msg = newReqMsg(MsgKeys.SetStoveStatus_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.IsCook, isCook);
            msg.putOpt(MsgParams.IhId, ihId);
            msg.putOpt(MsgParams.IhStatus, ihStatus);
            msg.setIsFan(true);
            if (Plat.DEBUG)
                LogUtils.i("stove_status", "设置状态：ihStatus:" + ihStatus + " UserId:" + getSrcUser() + " IhId:" + ihId + " ");
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
//                    callback.onSuccess();
                    if (Plat.DEBUG)
                        LogUtils.i("stove_status", "状态设置成功 " + ihStatus);
                    short id = (short) resMsg.optInt(MsgParams.IhId);
                    StoveHead head = getHeadById(id);
                    head.status = ihStatus;
                    onStatusChanged();
                }

                @Override
                public void onFailure(Throwable t) {
//                    ToastUtils.showLong("t:"+t.toString());
                    setStoveStatus(isCook, ihId, ihStatus, callback);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setStoveLevel(final boolean isCook, final short ihId, final short ihLevel,
                              final VoidCallback callback) {
        checkHeadId(ihId);


        //若灶具已经关机,先开机，再设置档位
        StoveHead stoveHead = getHeadById(ihId);
        if (stoveHead.status == StoveStatus.Off) {
            setStoveStatus(isCook, ihId, StoveStatus.StandyBy, new VoidCallback() {
                @Override
                public void onSuccess() {
                    if (Plat.DEBUG) {
                        LogUtils.i("stove_sl", "灶具下发状态：" + "isCook:" + isCook + " ihId:" + ihId
                                + " ihLevel:" + ihLevel);
                    }
                    StoveHead head = getHeadById(ihId);
                    head.status = StoveStatus.StandyBy;
                    setStoveLevel(isCook, ihId, ihLevel, callback);
                }

                @Override
                public void onFailure(Throwable t) {
                    if (Plat.DEBUG) {
                        LogUtils.i("stove_sl", "下发状态error:" + t.getMessage());
                    }
                    Helper.onFailure(callback, t);
                }
            });
            return;
        }

        try {
            Msg msg = newReqMsg(MsgKeys.SetStoveLevel_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.IsCook, isCook);
            msg.putOpt(MsgParams.IhId, ihId);
            msg.putOpt(MsgParams.IhLevel, ihLevel);
            msg.setIsFan(true);
            if (Plat.DEBUG)
                LogUtils.i("stove_sl", "灶具下发档位:" + msg.toString());
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    if (Plat.DEBUG)
                        LogUtils.i("20180414", "成功  " + ihLevel);
                    short id = (short) resMsg.optInt(MsgParams.IhId);
                    StoveHead head = getHeadById(id);
                    head.level = ihLevel;
                    onStatusChanged();
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20180414", "失败:22222  " + t.toString());
                    super.onFailure(t);
                    if (t.toString().contains("SyncTimeoutException")) {
                        LogUtils.i("20180414", "失败:  " + t.toString());
                    }
                }
            });
        } catch (Exception e) {
            if (Plat.DEBUG)
                LogUtils.i("stove_sl", "失败  " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setStoveShutdown(short ihId, final short ihTime,
                                 VoidCallback callback) {
        checkHeadId(ihId);

        try {
            Msg msg = newReqMsg(MsgKeys.SetStoveShutdown_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.IhId, ihId);
            msg.putOpt(MsgParams.IhTime, ihTime);
            msg.setIsFan(true);
            if (Plat.DEBUG)
                LogUtils.e("stove_setshutdown", "shutdown_ihTime:" + ihTime + " ");
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    if (Plat.DEBUG)
                        LogUtils.e("stove_setshutdown", "shutdown_ihTime_成功");
                    short id = (short) resMsg.optInt(MsgParams.IhId);
                    StoveHead head = getHeadById(id);
                    head.time = ihTime;
                    head.updateStatus(head);

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setStoveLock(final boolean isLock, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetStoveLock_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.StoveLock, isLock);
            msg.setIsFan(true);
            if (Plat.DEBUG)
                LogUtils.i("stove_setlock", "lock:" + isLock + " ");
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    if (Plat.DEBUG)
                        LogUtils.i("stove_setlock", "lock 成功");
                    Stove.this.isLock = isLock;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // -------------------------------------------------------------------------------
    // protected
    // -------------------------------------------------------------------------------

    @Override
    protected void initStatus() {
        super.initStatus();
        isLock = false;
        if (leftHead != null) {
            leftHead.initStatus();
        }
        if (rightHead != null) {
            rightHead.initStatus();
        }
    }

    @Override
    public void onStatusChanged() {
        postEvent(new StoveStatusChangedEvent(this));
    }

    // -------------------------------------------------------------------------------
    // private
    // -------------------------------------------------------------------------------

    private void onGetHeads(List<StoveHead> heads) {
        try {
            if (heads == null)
                return;

            if (heads.size() >= 1) {
                if (Plat.DEBUG)
                    LogUtils.i("stove_polling", "leftHead:" + heads.get(0) + "  " + this.getID());
                leftHead.updateStatus(heads.get(0));
            }

            if (heads.size() >= 2) {
                if (Plat.DEBUG)
                    LogUtils.i("stove_polling", "rightHead:" + heads.get(1));
                rightHead.updateStatus(heads.get(1));
            }

            onStatusChanged();
        } catch (Exception e) {
        }
    }

    private void checkHeadId(short headId) {
        Preconditions.checkArgument(headId == StoveHead.LEFT_ID
                || headId == StoveHead.RIGHT_ID);
    }

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    static public class StoveHead {

        final static public short LEFT_ID = 0;
        final static public short RIGHT_ID = 1;

        public Stove parent;
        public short ihId;

        public short getStatus() {
            return status;
        }

        public short status;
        public short level;
        public short time;
        public short alarmId;
        public short worktime;//炉头工作时间

        private short savedLevel;


        public StoveHead(short id) {
            ihId = id;
            initStatus();
        }

        public void updateStatus(StoveHead head) {
            this.status = head.status;
            this.level = head.level;
            this.time = head.time;
            this.alarmId = head.alarmId;
            this.worktime=head.worktime;
        }

        public boolean isLeft() {
            return ihId == LEFT_ID;
        }

        public boolean isRight() {
            return ihId == RIGHT_ID;
        }

        void initStatus() {
            status = StoveStatus.Off;
            level = 0;
            time = 0;
            alarmId = StoveAlarmManager.getInstance().getDefault().getID();
            worktime=0;
        }

        public void pause(VoidCallback callback) {
            savedLevel = (status == StoveStatus.StandyBy ? PowerLevel_0 : level);
            if (IRokiFamily.R9B39.equals(parent.getStoveModel()) || IRokiFamily.R9B37.equals(parent.getStoveModel()) || IRokiFamily._9B39E.equals(parent.getStoveModel()))
                parent.setStoveLevel(true, ihId, PowerLevel_1, callback);
            else
                parent.setStoveLevel(true, ihId, PowerLevel_0, callback);
        }

        public void pause() {
            pause(null);
        }

        public void restore(VoidCallback callback) {
            parent.setStoveLevel(true, ihId, savedLevel, callback);
            savedLevel = PowerLevel_0;
        }

        public void restore() {
            restore(null);
        }

        @Override
        public String toString() {
            return String.format(
                    "{id=%s,status=%s,level=%s,time=%s,alarmId=%s,worktime=%s}", ihId,
                    status, level, time, alarmId, worktime);
        }
    }

}
