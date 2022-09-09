package com.robam.common.io.device;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.device.IDeviceFactory;
import com.legent.plat.pojos.device.SubDeviceInfo;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.Stove9B30C;
import com.robam.common.pojos.device.Stove.Stove9B37;
import com.robam.common.pojos.device.Stove.Stove9B39;
import com.robam.common.pojos.device.Stove.Stove9B39E;
import com.robam.common.pojos.device.Stove.Stove9B515;
import com.robam.common.pojos.device.Stove.Stove9W851;
import com.robam.common.pojos.device.Stove.StoveHI704;
import com.robam.common.pojos.device.fan.Fan5916S;
import com.robam.common.pojos.device.fan.Fan8236S;

/**
 * Created by sylar on 15/8/10.  edited by zhaiyuanyi 15/10/16
 */
public class RokiDeviceFactory implements IDeviceFactory {

    @Override
    public IDevice generate(SubDeviceInfo deviceInfo) {
        return generateModel(deviceInfo);
    }

    public static IDevice generateModel(SubDeviceInfo deviceInfo) {//产生设备model：／8700/9700/8229...
        Preconditions.checkNotNull(deviceInfo, "deviceInfo is null!");
        Preconditions.checkState(!Strings.isNullOrEmpty(deviceInfo.guid), "guid is null or empty");

        String guid = deviceInfo.guid;
        LogUtils.e("Welcome", "guid:" + guid + "  deviceInfo instanceof DeviceInfo:" + (deviceInfo instanceof DeviceInfo));
        if (deviceInfo instanceof DeviceInfo) {
            // is HubDevice
            DeviceInfo devInfo = (DeviceInfo) deviceInfo;
            Log.e("20181220", "guid:" + guid);
            if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._8236S)) {
                return new Fan8236S(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._5916S)) {
                return new Fan5916S(devInfo);
            } else {
                return null;
            }

        } else {
            // is subDevice/chileDevice
            if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9W70)) {
                return new Stove(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9B39)) {
                return new Stove9B39(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._9B39E)) {
                return new Stove9B39E(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._9B515)) {
                return new Stove9B515(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._9W851)) {
                return new Stove9W851(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9B30C)) {
                return new Stove9B30C(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9B37)) {
                return new Stove9B37(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R0001)) {
                return new Pot(deviceInfo);
            } else {
                //TODO add new devices here ....
                return null;
            }
        }
    }


}
