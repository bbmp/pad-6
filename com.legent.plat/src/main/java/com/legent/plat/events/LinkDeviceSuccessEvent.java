package com.legent.plat.events;

import com.legent.plat.pojos.device.AbsDeviceHub;

/**
 * Created by Administrator on 2017/6/8.
 */
public class LinkDeviceSuccessEvent {
    private AbsDeviceHub absDeviceHub;
    private int msgKeys = 0;
    private int setTime = 0;
    private int setTemp = 0;
    private int setTempUp = 0;
    private int setTempDown = 0;
    private String mode;
    private int cookStepIndex;

    public LinkDeviceSuccessEvent(AbsDeviceHub absDeviceHub, int msgKeys, String mode,int setTime, int setTemp,int cookStepIndex) {
        this.absDeviceHub = absDeviceHub;
        this.msgKeys = msgKeys;
        this.mode = mode;
        this.setTime = setTime;
        this.setTemp = setTemp;
        this.cookStepIndex = cookStepIndex;
    }

    public LinkDeviceSuccessEvent(AbsDeviceHub absDeviceHub, int msgKeys, String mode, int setTime, int setTempUp, int setTempDown, int cookStepIndex) {
        this.absDeviceHub = absDeviceHub;
        this.msgKeys = msgKeys;
        this.mode = mode;
        this.setTime = setTime;
        this.cookStepIndex = cookStepIndex;
        this.setTempDown = setTempDown;
        this.setTempUp = setTempUp;
    }

    public AbsDeviceHub getAbsDeviceHub() {
        return absDeviceHub;
    }

    public void setAbsDeviceHub(AbsDeviceHub absDeviceHub) {
        this.absDeviceHub = absDeviceHub;
    }

    public int getMsgKeys() {
        return msgKeys;
    }

    public int getSetTime() {
        return setTime;
    }

    public int getSetTemp() {
        return setTemp;
    }

    public String getMode() {
        return mode;
    }

    public int getCookStepIndex() {
        return cookStepIndex;
    }

    public int getSetTempDown() {
        return setTempDown;
    }
    public int getSetTempUp() {
        return setTempUp;
    }
}
