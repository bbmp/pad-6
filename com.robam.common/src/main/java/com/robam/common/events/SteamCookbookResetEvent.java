package com.robam.common.events;

import com.robam.common.pojos.device.Steamoven.AbsSteamoven;

/**
 * Created by Rosicky on 15/12/15.
 */
public class SteamCookbookResetEvent {
    public AbsSteamoven steamoven;
    public short cookbook;

    public SteamCookbookResetEvent(AbsSteamoven steamoven, short cookbook) {
        this.steamoven = steamoven;
        this.cookbook = cookbook;
    }
}
