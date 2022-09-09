package com.robam.common.events;

import com.robam.common.pojos.device.fan.AbsFan;

/**
 * Created by 14807 on 2019/9/27.
 * PS:3D手势开关事件
 */

public class Fan3DGestureEvent {

    public AbsFan fan;
    public short gesture;

    public Fan3DGestureEvent(AbsFan fan, short gesture) {
        this.fan = fan;
        this.gesture = gesture;
    }
}
