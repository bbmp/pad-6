package com.robam.common.pojos.device.fan;


import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.pojos.device.IRokiFamily;


/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/4/24.
 * PS: Not easy to write code, please indicate.
 */
public class Fan8236S extends AbsFan {


    public Fan8236S(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public boolean isSupportPot() {
        return false;
    }

    @Override
    public String getFanModel() {
        return IRokiFamily._8236S;
    }



}
