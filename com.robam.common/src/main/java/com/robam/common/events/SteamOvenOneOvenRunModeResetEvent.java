package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/8.
 *
 */

public class SteamOvenOneOvenRunModeResetEvent {

    public AbsSteameOvenOne steameOvenOne;
    public short runMode;

    public SteamOvenOneOvenRunModeResetEvent(AbsSteameOvenOne steameOvenOne, short runMode) {
        this.steameOvenOne = steameOvenOne;
        this.runMode = runMode;
    }
}
