package com.robam.common.pojos.device.Pot;

/**
 * Created by robam on 2018/8/23.
 */

public class PotSmartParams {
    /**
     * 无人锅干烧提示(0关，1开)[1Byte]
     */
    public boolean isPotHyperthermiaAlarm = true;

    /**
     * 无人锅智能联动开关（0关，1开）[1Byte]
     */
    public boolean isPotLinkWith = true;
}
