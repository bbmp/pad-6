package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/8.
 * 一体机控制事件Event
 */

public class SteamOvenOneSwitchControlResetEvent {
    public AbsSteameOvenOne steameOven;
    public boolean switchControlStatus;

    public SteamOvenOneSwitchControlResetEvent(AbsSteameOvenOne steameOven, boolean switchControlStatus) {
        this.steameOven = steameOven;
        this.switchControlStatus = switchControlStatus;
    }
}
