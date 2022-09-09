package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 */

public class SteamOvenOneWorkFinishEvent {

    public AbsSteameOvenOne steameOvenOne;

    public short finish;

    public SteamOvenOneWorkFinishEvent(AbsSteameOvenOne steameOvenOne, short finish) {
        this.steameOvenOne = steameOvenOne;
        this.finish = finish;
    }
}
