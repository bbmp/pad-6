package com.legent.plat.pojos.device;

import android.content.Context;

import com.google.common.base.Strings;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DeviceStatusChangedEvent;
import com.legent.plat.io.device.DeviceCommander;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgCallback;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.pojos.AbsKeyPojo;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;


abstract public class AbsDevice extends AbsKeyPojo<String> implements IDevice {

    protected final static int MAX_ERROR_COUNT = 4;

    protected String id, bid, name;
    // 周定钧
    // dc 代表 设备种类 油烟机:RYYJ 电磁灶：RDCZ 燃气灶：RRQZ;
    // dp 代表 菜谱平台参数：RQZ01 RQZ02
    // dt 代表 型号   烟机8230 ：R8230  烤箱029 ：R029
    protected String dc, dp, dt, displayType;


    protected int ver;
    protected boolean isConnected;
    protected boolean valid;
    protected int errorCountOnCheck;
    protected IDevice parent;
    //    protected DeviceCommander dc = Plat.commander;
    protected DeviceCommander dcMqtt = Plat.dcMqtt;
    protected DeviceCommander dcSerial = Plat.dcSerial;
    protected boolean hardIsConnected;//代表 硬件上报的连接状态  具体看协议41返回指令

    @Override
    public boolean isHardIsConnected() {
        return hardIsConnected;
    }

    @Override
    public void setHardIsConnected(boolean hardIsConnected) {
        this.hardIsConnected = hardIsConnected;
    }

    public AbsDevice(SubDeviceInfo devInfo) {
        this.valid = true;
        this.id = devInfo.guid;
        this.bid = devInfo.bid;
        this.name = devInfo.name;
        this.ver = (short) devInfo.ver;
        //周定钧
        this.dc = devInfo.dc;
        this.dp = devInfo.dp;
        this.dt = devInfo.dt;
        this.displayType = devInfo.displayType;
        this.hardIsConnected = devInfo.isConnected;
        if (Strings.isNullOrEmpty(this.name)) {
            if (DeviceTypeManager.getInstance().getDeviceType(id) != null) {
                this.name = DeviceTypeManager.getInstance().getDeviceType(id).getName();
            }
        }

        initStatus();
    }

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);
//        dc.initIO(id);
        if (dcMqtt != null) {
            dcMqtt.initIO(id);
        }
        if (dcSerial != null) {
            dcSerial.initIO(id);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
//        dc.disposeIO(id);
        if (dcMqtt != null) {
            dcMqtt.disposeIO(id);
        }
        if (dcSerial != null) {
            dcSerial.disposeIO(id);
        }
    }

    // -------------------------------------------------------------------------------
    // IDevice
    // -------------------------------------------------------------------------------

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DeviceGuid getGuid() {
        return DeviceGuid.newGuid(id);
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceTypeManager.getInstance().getDeviceType(id);
    }

    @Override
    public String getBid() {
        return bid;
    }

    @Override
    public int getVersion() {
        return ver;
    }

    @Override
    public String getDc() {
        return dc;
    }


    @Override
    public void setDc(String dc) {
        this.dc = dc;
    }

    @Override
    public void setDt(String dt) {
        this.dt = dt;
    }

    @Override
    public void setDp(String dp) {
        this.dp = dp;
    }

    @Override
    public String getDt() {
        return dt;
    }

    @Override
    public String getDp() {
        return dp;
    }

    @Override
    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    @Override
    public synchronized boolean isConnected() {
        return isConnected;
    }


    @Override
    public synchronized void setConnected(boolean connected) {
        errorCountOnCheck = connected ? 0 : errorCountOnCheck;
        if (isConnected == connected)
            return;
        LogUtils.i("devices_polling", "isConnected:" + isConnected + " connected:" + connected);
        isConnected = connected;
        if (!isConnected) {
            initStatus();
        }
        postEvent(new DeviceConnectionChangedEvent(this, isConnected));
        if (Plat.LOG_FILE_ENABLE) {
            LogUtils.logFIleWithTime(String.format("post DeviceConnectionChangedEvent:%s", isConnected));
        }
    }


    @Override
    public boolean getValid() {
        return valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public IDevice getParent() {
        return parent;
    }

    @Override
    public void setParent(IDevice parent) {
        this.parent = parent;
    }

    @Override
    public void onPolling() {
    }

    @Override
    public void onCheckConnection() {
        LogUtils.i("devices_polling", "isConnected:" + isConnected);
        if (isConnected) {
            errorCountOnCheck++;
            if (errorCountOnCheck >= MAX_ERROR_COUNT) {
                setConnected(false);
            }
        } else {
            postEvent(new DeviceConnectionChangedEvent(this, isConnected));
        }
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        setConnected(true);
    }

    @Override
    public void onStatusChanged() {
        postEvent(new DeviceStatusChangedEvent(this));
    }

    // -------------------------------------------------------------------------------
    // protected
    // -------------------------------------------------------------------------------

    /**
     * 设备状态参数重置
     */
    protected void initStatus() {

    }


    final protected Msg newReqMsg(short msgKey) {
        Msg msg = Msg.newRequestMsg(msgKey, id);
        return msg;
    }

    final protected void sendMsg(Msg reqMsg, MsgCallback callback) {

        //dc.asyncSend(id, reqMsg, callback);
        if (Plat.appType.equals(IAppType.RKDRD)) {
            if (Plat.DEBUG)
                Plat.dcMqtt.asyncSend(this.id, reqMsg, callback);
        } else if (Plat.appType.equals(IAppType.RKPBD)) {
            if (reqMsg.getIsFan()) { // 如果是烟机端，则走串口
                Plat.dcSerial.asyncSend(this.id, reqMsg, callback);
            } else {
                Plat.dcMqtt.asyncSend(this.id, reqMsg, callback);
            }
        }
    }

    final protected void sendMsgBySerial(Msg reqMsg, MsgCallback callback) {
        Plat.dcSerial.asyncSend(id, reqMsg, callback);
    }

    final protected void sendMsgByMqtt(Msg reqMsg, MsgCallback callback) {
        Plat.dcMqtt.asyncSend(id, reqMsg, callback);
    }

    final protected String getSrcUser() {
        long id = getCurrentUserId();

        String userId = String.valueOf(id);
        userId = Strings.padEnd(userId, 10, '\0');
        return userId;
    }

    final protected long getCurrentUserId() {
        return Plat.accountService.getCurrentUserId();
    }

    final protected void postEvent(Object event) {
        EventUtils.postEvent(event);
    }

    // -------------------------------------------------------------------------------
    // private
    // -------------------------------------------------------------------------------

}
