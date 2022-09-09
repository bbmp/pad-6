package com.robam.common.events;

import com.robam.common.pojos.device.fan.AbsFan;

/**
 * Created by Administrator on 2018/2/2.
 */
public class FanRegularlyRemindEvent {
    public AbsFan fan;

    public FanRegularlyRemindEvent(AbsFan fan) {
        this.fan = fan;
    }
}
