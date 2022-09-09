package com.robam.common.events;

import com.robam.common.pojos.device.fan.AbsFan;

/**
 * Created by Administrator on 2018/2/2.
 */
public class FanTimingRemingEvent {
    public AbsFan fan;

    public FanTimingRemingEvent(AbsFan fan) {
        this.fan = fan;
    }

}
