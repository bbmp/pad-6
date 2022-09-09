package com.robam.common.pojos.device.Stove;

import com.legent.plat.pojos.device.SubDeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

public class Stove9B39E extends Stove {

    public Stove9B39E(SubDeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getStoveModel() {
        return IRokiFamily._9B39E;
    }
}
