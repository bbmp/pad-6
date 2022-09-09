package com.robam.common.pojos.device.microwave;

import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by Administrator on 2017/4/1.
 */

public class MicroWaveM526 extends AbsMicroWave implements iMicroWaveM509{

    public MicroWaveM526(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getMicroWaveMode() {
        return IRokiFamily.RM526;
    }
}
