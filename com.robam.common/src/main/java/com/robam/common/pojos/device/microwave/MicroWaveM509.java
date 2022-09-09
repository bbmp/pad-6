package com.robam.common.pojos.device.microwave;

import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by WZTCM on 2015/12/17.
 */
public class MicroWaveM509 extends AbsMicroWave implements iMicroWaveM509 {
    public MicroWaveM509(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getMicroWaveMode() {
        return IRokiFamily.RM509;
    }
}
