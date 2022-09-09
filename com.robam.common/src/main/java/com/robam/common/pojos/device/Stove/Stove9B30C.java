package com.robam.common.pojos.device.Stove;

import com.legent.plat.pojos.device.SubDeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by zhaiyuanyi on 15/10/16.
 */
public class Stove9B30C extends Stove{
    public Stove9B30C(SubDeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getStoveModel() {
        return IRokiFamily.R9B30C;
    }
}
