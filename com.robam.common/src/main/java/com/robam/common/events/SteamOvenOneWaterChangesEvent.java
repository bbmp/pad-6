package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 * 一体机水箱更改事件
 */

public class SteamOvenOneWaterChangesEvent {

    public AbsSteameOvenOne steameOvenOne;

    public short waterChanges;

    public SteamOvenOneWaterChangesEvent(AbsSteameOvenOne steameOvenOne, short waterChanges) {
        this.steameOvenOne = steameOvenOne;
        this.waterChanges = waterChanges;
    }
}
