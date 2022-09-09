package com.robam.common.events;

import com.robam.common.pojos.device.fan.AbsFan;

/**
 * Created by Administrator on 2016/4/8.
 */
public class FanCleanLockEvent {
    public AbsFan fan;
    public  short flag_CleanLock;
    public FanCleanLockEvent(AbsFan fan,short flag_CleanLock) {
        this.fan = fan;
        this.flag_CleanLock = flag_CleanLock;
    }
}
