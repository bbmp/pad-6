package com.robam.common.pojos.device.fan;

public interface FanStatus {
    /**
     * 关机
     */
    short Off = 0;

    /**
     * 开机
     */
    short On = 1;

    /**
     * 延时关机
     */
    short DelayShutdown = 2;

    /**
     * 清洗锁定
     */
    short CleanLock = 4;

    /**
     * 拆除挡风板
     */
    short RemoveWindshield = 5;


    short LEVEL_SMALL = 1;

    short LEVEL_SMALL_Two = 2;

    short LEVEL_MIDDLE = 3;

    short LEVEL_BIG = 6;
    short LEVEL_BIG_5 = 5;
    short LEVEL_BIG_4 = 4;

    short LEVEL_EMPTY = 0;


}