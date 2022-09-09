package com.legent.plat.pojos;

import android.util.Log;

import com.legent.plat.Plat;
import com.legent.utils.LogUtils;

/**
 * Created by as on 2017-08-24.
 * 当前正在页面显示的 设备 烟灶 烤蒸微净  pad端使用
 */

public class CurrentDeviceInfo {
    static String defaultOven;
    static String defaultSteam;
    static String defaultster;
    static String defaultmic;
    static String defaultwater;
    static String defaultSteamOven;

    public static void setCurrentOven(String defaultOven) {
        CurrentDeviceInfo.defaultOven = defaultOven;
    }

    public static void setCurrentSteam(String defaultSteam) {
        CurrentDeviceInfo.defaultSteam = defaultSteam;
    }

    public static void setCurrentster(String defaultster) {
        CurrentDeviceInfo.defaultster = defaultster;
    }

    public static void setCurrentmic(String defaultmic) {
        CurrentDeviceInfo.defaultmic = defaultmic;
    }

    public static void setCurrentwater(String defaultwater) {
        CurrentDeviceInfo.defaultwater = defaultwater;
    }

    public static void setDefaultSteamOven(String defaultSteamOven) {
        CurrentDeviceInfo.defaultSteamOven = defaultSteamOven;
    }

    public static String getCurrentFan() {
        return Plat.getFanGuid().getID();
    }

    public static String getCurrentStove() {
        return Plat.getStoveGuid();
    }

    public static String getCurrentPot() {
        return Plat.getPotGuid();
    }

    public static String getCurrentOven() {
        return defaultOven;
    }

    public static String getCurrentSteam() {
        return defaultSteam;
    }

    public static String getCurrentSter() {
        return defaultster;
    }

    public static String getCurrentmic() {
        return defaultmic;
    }

    public static String getCurrentwater() {
        return defaultwater;
    }

    public static String getCurrentSteamOven() {
        return defaultSteamOven;
    }
}
