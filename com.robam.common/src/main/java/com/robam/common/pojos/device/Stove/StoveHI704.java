package com.robam.common.pojos.device.Stove;

import com.legent.plat.pojos.device.SubDeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by zhaiyuanyi on 15/10/16.
 */
public class StoveHI704 extends Stove{
    public StoveHI704(SubDeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getStoveModel() {
        return IRokiFamily.HI704;
    }
}
