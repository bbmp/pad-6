package com.robam.common.events;
import com.robam.common.pojos.device.fan.AbsFan;

public class FanCleanNoticEvent {
    public AbsFan fan;

    public FanCleanNoticEvent(AbsFan fan) {
        this.fan = fan;
    }
}
