package com.robam.common.events;

/**
 * Created by Administrator on 2017/8/15.
 */
public class ReturnHomeDevicePageEvent {
    private String homeDeviceIndex;

    public ReturnHomeDevicePageEvent(String homeDeviceIndex) {
        this.homeDeviceIndex = homeDeviceIndex;
    }

    public String getHomeDeviceIndex() {
        return homeDeviceIndex;
    }
}
