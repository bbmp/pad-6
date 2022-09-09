package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 */

public class SteamOvenOneDescalingEvent {

    public AbsSteameOvenOne steameOvenOne;

    public short descaling;

    public SteamOvenOneDescalingEvent(AbsSteameOvenOne steameOvenOne, short descaling) {
        this.steameOvenOne = steameOvenOne;
        this.descaling = descaling;
    }
}
