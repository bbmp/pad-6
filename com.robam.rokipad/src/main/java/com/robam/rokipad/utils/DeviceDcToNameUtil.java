package com.robam.rokipad.utils;

import com.legent.plat.constant.IDeviceType;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/3.
 * PS:根据设备的DC转换为设备名称.
 */
public class DeviceDcToNameUtil {

    public static String getDcToName(String dc){
        switch (dc){
            case IDeviceType.RRQZ:
            case IDeviceType.RDCZ:
                return IDeviceType.RDZJ_ZN;
            case IDeviceType.RPOT:
                return IDeviceType.RPOT_ZN;
            case IDeviceType.RJSQ:
                return IDeviceType.RJSQ_ZN;
            case IDeviceType.RXDG:
                return IDeviceType.RXDG_ZN;
            case IDeviceType.RDKX:
                return IDeviceType.RDKX_ZN;
            case IDeviceType.RZQL:
                return IDeviceType.RZQL_ZN;
            case IDeviceType.RWBL:
                return IDeviceType.RWBL_ZN;
            case IDeviceType.RYYJ:
                return IDeviceType.RYYJ_ZN;
            case IDeviceType.RZKY:
                return IDeviceType.RZKY_ZN;
        }
        return null;
    }
}
