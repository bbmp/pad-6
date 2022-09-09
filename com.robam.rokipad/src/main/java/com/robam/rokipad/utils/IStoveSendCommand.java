package com.robam.rokipad.utils;

import com.robam.common.pojos.device.Stove.Stove;

/**
 * Created by Dell on 2019/2/25.
 */

public interface IStoveSendCommand {
    void add(int headId, CallBackCommand callback);

    void decrease(int headId, CallBackCommand callback);

    void setTime(short headId, short time,CallBackCommand callback);

    void setPower(int stoveHeadIndex, CallBackCommand callback);

    void setLockStatus(boolean status, CallBackCommand callback);

    void setDevice(Stove stove);
}
