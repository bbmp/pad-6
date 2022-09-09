package com.robam.common.pojos.device.Stove;

import com.legent.plat.pojos.device.SubDeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

public class Stove9W851 extends Stove {
    public Stove9W851(SubDeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getStoveModel() {
        return IRokiFamily._9W851;
    }
}
