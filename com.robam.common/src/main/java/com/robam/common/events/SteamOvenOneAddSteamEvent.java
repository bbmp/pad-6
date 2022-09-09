package com.robam.common.events;

import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

/**
 * Created by Administrator on 2017/7/10.
 * 一体机加蒸汽事件
 */

public class SteamOvenOneAddSteamEvent {

    public AbsSteameOvenOne steameOvenOne;

    public short addSteam;

    public SteamOvenOneAddSteamEvent(AbsSteameOvenOne steameOvenOne, short addSteam) {
        this.steameOvenOne = steameOvenOne;
        this.addSteam = addSteam;
    }
}
