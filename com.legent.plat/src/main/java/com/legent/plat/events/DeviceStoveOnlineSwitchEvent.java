package com.legent.plat.events;

import com.legent.plat.pojos.device.SubDeviceInfo;

import java.util.List;

/**
 * Created by as on 2017-06-21. 灶具设备上线通知
 */

public class DeviceStoveOnlineSwitchEvent {
    public List<SubDeviceInfo> children;

    public DeviceStoveOnlineSwitchEvent(List<SubDeviceInfo> children) {
        this.children = children;
    }
}
