package com.legent.plat.services;


import com.google.common.base.Objects;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.pojos.AbsPojoManagerWithMap;
import com.legent.utils.LogUtils;

public class DeviceTypeManager extends AbsPojoManagerWithMap<DeviceType, String> {

//    final static public String DT_App = "App";
//    final static public String DT_Cloud = "Cloud";

    static private DeviceTypeManager instance = new DeviceTypeManager();

    synchronized static public DeviceTypeManager getInstance() {
        return instance;
    }

    private DeviceTypeManager() {
    }

    public boolean isInDeviceType(String guid, String deviceTypeId) {
        return Objects.equal(DeviceGuid.newGuid(guid).getDeviceTypeId(), deviceTypeId);
    }

    public boolean isInDeviceType(String guid, DeviceType dt) {
        return Objects.equal(DeviceGuid.newGuid(guid).getDeviceTypeId(), dt.getID());
    }

    public boolean isInDeviceType(DeviceGuid guid, String deviceTypeId) {
        return Objects.equal(guid.getDeviceTypeId(), deviceTypeId);
    }

    public boolean isInDeviceType(DeviceGuid guid, DeviceType dt) {
        return Objects.equal(guid.getDeviceTypeId(), dt.getID());
    }

    public DeviceType getDeviceType(String guid) {
        LogUtils.i("ChangeNum","guid:"+guid);
        String id = DeviceGuid.newGuid(guid).getDeviceTypeId();
        LogUtils.i("ChangeNum","id:"+id);
        return queryById(id);
    }

    public DeviceType getDeviceType(DeviceGuid guid) {
        String id = guid.getDeviceTypeId();
        if (Plat.DEBUG){
            LogUtils.i("20170327","id:"+id);
            LogUtils.i("20170327","queryById(id):"+queryById(id));
        }
        return queryById(id);
    }
}
