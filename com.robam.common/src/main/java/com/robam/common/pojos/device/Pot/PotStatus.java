package com.robam.common.pojos.device.Pot;

/**
 * Created by 14807 on 2019/9/2.
 * PS:
 */

public interface PotStatus {

    /**
     * 待机
     */
    short wait = 0;

    /**
     * 开机
     */
    short On = 1;


    /**
     * 干烧预警
     */
    short dryAlarmStatus = 2;

    /**
     * 低电量提醒
     */
    short lowBatteryStatus  = 3;

    /**
     * 温度传感器故障
     */
    short tempSensorStatus = 4;

}
