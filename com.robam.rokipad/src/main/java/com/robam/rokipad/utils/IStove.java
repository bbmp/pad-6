package com.robam.rokipad.utils;

import com.robam.common.pojos.device.Stove.Stove;

/**
 * Created by Dell on 2019/2/21.
 */

public interface IStove {
    void setLeftStatus(Stove stove);

    void setRightStatus(Stove stove);

    //void setDeviceStatus(Stove stove);
    void isConnected(boolean isCon);

    void setLockStatus(Stove pojo);



}
