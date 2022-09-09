package com.robam.common.recipe.step;

import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;

/**
 * Created by Dell on 2018/4/2.
 */

public class DeviceStatusCheck {
    public static DeviceStatusCheck getInstance() {
        return new DeviceStatusCheck();
    }


    public boolean getStatus(String dc, String headId) {
        if ("RDKX".equals(dc)) {
            AbsOven oven = Utils.getDefaultOven();
            if (oven != null) {
                if (oven.status == OvenStatus.PreHeat || oven.status == OvenStatus.Working ||
                        oven.status == OvenStatus.Pause
                        ) {
                    return true;
                }
            }
        } else if ("RZQL".equals(dc)) {
            AbsSteamoven steamoven = Utils.getDefaultSteam();
            if (steamoven != null) {
                LogUtils.i("20180419", "status:;" + steamoven.status);
                if (steamoven.status == SteamStatus.PreHeat || steamoven.status == SteamStatus.Working ||
                        steamoven.status == SteamStatus.Pause) {
                    return true;
                }
            }
        } else if ("RWBL".equals(dc)) {
            AbsMicroWave microWave = Utils.getDefaultMicrowave();
            if (microWave != null) {

                if (microWave.state == MicroWaveStatus.Run || microWave.state == MicroWaveStatus.Pause) {
                    return true;
                }
            }
        } else if ("RZKY".equals(dc)) {
            AbsSteameOvenOne absSteameOvenOne = Utils.getAllSteamOvenOne().get(0);
            if (absSteameOvenOne != null) {
                if (absSteameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus ||
                        absSteameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
                    return true;
                }
            }
        } else if ("RRQZ".equals(dc)) {
            Stove[] stove = Utils.getDefaultStove();

            if (stove != null) {
                if ("0".equals(headId)) {
                    if (stove[0].leftHead.status == 2) {
                        return true;
                    }
                } else {
                    LogUtils.i("20180413", "stove__status::" + stove[0].rightHead.status);
                    if (stove[0].rightHead.status == 2) {
                        return true;
                    }
                }
            }


        }
        return false;
    }

    public boolean getIsAlarm(String dc){
        if ("RZQL".equals(dc)) {
            AbsSteamoven steamoven = Utils.getDefaultSteam();
            if (steamoven != null) {
                LogUtils.i("20180419", "status:;" + steamoven.status);
                if (steamoven.doorState==0) {
                    return true;
                }
            }
        } else if ("RWBL".equals(dc)) {
            AbsMicroWave microWave = Utils.getDefaultMicrowave();
            if (microWave != null) {

                if (microWave.doorState==1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean getDeviceConnect(String dc) {
        LogUtils.i("20180403", "dc::" + dc);
        if ("RDKX".equals(dc)) {
            AbsOven oven = Utils.getDefaultOven();
            if (oven != null) {
                if (!oven.isConnected()) {
                    return true;
                }
            }
        } else if ("RZQL".equals(dc)) {
            AbsSteamoven steamoven = Utils.getDefaultSteam();
            if (steamoven != null) {
                if (!steamoven.isConnected()) {
                    return true;
                }
            }
        } else if ("RWBL".equals(dc)) {
            AbsMicroWave microWave = Utils.getDefaultMicrowave();
            if (microWave != null) {
                if (!microWave.isConnected()) {
                    return true;
                }

            }
        } else if ("RZKY".equals(dc)) {
            AbsSteameOvenOne absSteameOvenOne = Utils.getAllSteamOvenOne().get(0);
            if (absSteameOvenOne != null) {
                if (!absSteameOvenOne.isConnected()) {
                    return true;
                }
            }
        } else if ("RRQZ".equals(dc)) {
            Stove[] stove = Utils.getDefaultStove();
            if (stove != null) {
                if (!stove[0].isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean getIsLock(String dc) {
        if ("RRQZ".equals(dc)) {
            Stove[] stove = Utils.getDefaultStove();
            if (stove[0].isLock) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }



}




