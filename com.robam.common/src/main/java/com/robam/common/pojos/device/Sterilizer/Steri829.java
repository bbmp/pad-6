package com.robam.common.pojos.device.Sterilizer;

import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;

/**
 * Created by zhaiyuanyi on 15/11/19.
 */
public class Steri829 extends AbsSterilizer implements ISteri829 {
    public Steri829(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getSterilizerModel() {
        return IRokiFamily.RR829;
    }

}
