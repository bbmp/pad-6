package com.robam.common;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

import java.util.List;

public class Utils {

    static public boolean hasRokiDevice() {
        return getDefaultFan() != null;
    }

    static public AbsFan getDefaultFan() {
        IDevice device = Plat.deviceService.getDefault();
        return (AbsFan) device;
    }


    static public AbsWaterPurifier getDefaultWaterPurifier() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
                if (device instanceof AbsWaterPurifier && device.isConnected()) {
                    return (AbsWaterPurifier) device;
                }
            }

            for (AbsDevice device : list) {
                if (device instanceof AbsWaterPurifier) {
                    return (AbsWaterPurifier) device;
                }
            }

            return null;
        }
    }

    static public AbsSteamoven getSteam(String dt) {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsSteamoven && device.isConnected() && dt.equals(device.getDt())) {
                    return (AbsSteamoven) device;
                }
            }

            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsSteamoven && dt.equals(device.getDt())) {
                    return (AbsSteamoven) device;
                }
            }

            return null;
        }
    }

    static public AbsSteamoven getDefaultSteam() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsSteamoven && device.isConnected()) {
                    return (AbsSteamoven) device;
                }
            }

            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsSteamoven) {
                    return (AbsSteamoven) device;
                }
            }

            return null;
        }
    }

    static public AbsSteameOvenOne getDefaultSteameOven() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
                if (device instanceof AbsSteameOvenOne && device.isConnected()) {
                    return (AbsSteameOvenOne) device;
                }
            }

            for (AbsDevice device : list) {
                if (device instanceof AbsSteameOvenOne) {
                    return (AbsSteameOvenOne) device;
                }
            }
            return null;
        }
    }

    final public static Stove[] stoves = new Stove[1];

    static public Stove[] getDefaultStove() {
        AbsFan fan = getDefaultFan();
        if (IAppType.RKPBD.equals(Plat.appType)) {
            LogUtils.e("20190522","Plat.getStoveGuid:" + Plat.getStoveGuid());
            if (!StringUtils.isNullOrEmpty(Plat.getStoveGuid())) {
                stoves[0] = fan.getChild(Plat.getStoveGuid());
                LogUtils.e("20190522","stoves[0]:" + stoves[0]);
                return stoves;
            } else {
                stoves[0] = null;
                return stoves;
            }
        } else {
            stoves[0] = null;
            if (fan != null) {
                //stove = fan.getChildByDeviceType(IRokiFamily.R9W70);
                for (IDevice device : fan.getChildList()) {
                    if (device != null && device instanceof Stove) {
                        if (stoves[0] == null)
                            stoves[0] = (Stove) device;
                        if (device.isHardIsConnected()) {
                            stoves[0] = (Stove) device;
                            return stoves;
                        }
                    }
                }
            }
            return stoves;
        }
    }

    final public static Pot[] pots = new Pot[1];

    static public Pot[] getDefaultPot() {
        AbsFan fan = getDefaultFan();
        if (IAppType.RKPBD.equals(Plat.appType)) {
            if (!StringUtils.isNullOrEmpty(Plat.getPotGuid())) {
                pots[0] = fan.getChild(Plat.getPotGuid());
                return pots;
            } else {
                pots[0] = null;
                return pots;
            }
        } else {
            pots[0] = null;
            if (fan != null) {
                for (IDevice device : fan.getChildList()) {
                    if (device != null && device instanceof Pot) {
                        if (pots[0] == null)
                            pots[0] = (Pot) device;
                        if (device.isHardIsConnected()) {
                            pots[0] = (Pot) device;
                            return pots;
                        }
                    }
                }
            }
            return pots;
        }
    }

    static public AbsOven getDefaultOven() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsOven && device.isConnected()) {
                    return (AbsOven) device;
                }
            }

            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsOven) {
                    return (AbsOven) device;
                }
            }

            return null;
        }
    }

    static public AbsMicroWave getDefaultMicrowave() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
                if (device instanceof AbsMicroWave) {
                    return (AbsMicroWave) device;
                }
            }
            return null;
        }
    }


    static public AbsSterilizer getDefaultSterilizer() {
        List<IDevice> list = Plat.deviceService.queryAll();
        for (IDevice device : list) {
            if (device instanceof AbsSterilizer && device.isConnected())
                return (AbsSterilizer) device;
        }

        for (IDevice device : list) {
            if (device instanceof AbsSterilizer)
                return (AbsSterilizer) device;
        }

        return null;
    }

    static public boolean isMobApp() {
        boolean isMob = Objects.equal(Plat.app.getPackageName(),
                "com.robam.roki");
        return isMob;
    }


    static public boolean isStove(String guid) {//判断是否为灶具 by zhaiyuanyi
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9W70)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9B39)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._9B39E)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._9B515)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._9W851)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9B30C)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9B37)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.HI704);
    }

    static public boolean isFan(String guid) {//判断是否为烟机 by zhaiyuanyi
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._8236S)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._5916S);

    }

    static public boolean isSterilizer(String guid) {//判断是否为消毒柜 by zhaiyuanyi
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR829)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR826);
    }

    static public boolean isSteam(String guid) {//判断是否为蒸汽炉 by Rosicky
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RS209)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RS226)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.HS906)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RS228);
    }

    static public boolean isMicroWave(String guid) {//判断是否为微波炉 by Rosicky
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RM509)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RM526);
    }

    static public boolean isOven(String guid) {//判断是否为电烤箱 by Linxiaobin
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR039)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR026)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR016)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR028)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.HK906);
    }

    static public boolean isWaterPurifier(String guid) {//判断是否为RR372净水器
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RJ312)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RJ321)
                || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RJ320);
    }

    static public boolean isPot(String guid) {//判断是否为温控锅R0001
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R0001);
    }

    static public boolean isSteamOvenMsg(String guid) {//判断是否为蒸考一体机C906
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RC906);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * <p>
     * （DisplayMetrics类中属性scaledDensity）
     *
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * <p>
     * （DisplayMetrics类中属性scaledDensity）
     *
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    static public List<Stove> getAllStoves() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        List<Stove> stoves = Lists.newArrayList();
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            for (IDevice device : list) {
                if (device instanceof Stove)
                    stoves.add((Stove) device);
            }
        }
        return stoves;
    }

    static public List<AbsWaterPurifier> getAllWaterPurifier() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        List<AbsWaterPurifier> waterpurifierList = Lists.newArrayList();
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            for (IDevice device : list) {
                if (device instanceof AbsWaterPurifier)
                    waterpurifierList.add((AbsWaterPurifier) device);
            }
        }
        return waterpurifierList;
    }

    static public List<AbsOven> getAllOven() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        List<AbsOven> ovens = Lists.newArrayList();
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            for (IDevice device : list) {
                if (device instanceof AbsOven)
                    ovens.add((AbsOven) device);
            }
        }
        return ovens;
    }

    static public List<AbsSteamoven> getAllSteams() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        List<AbsSteamoven> steams = Lists.newArrayList();
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            for (IDevice device : list) {
                if (device instanceof AbsSteamoven)
                    steams.add((AbsSteamoven) device);
            }
        }
        return steams;
    }

    static public List<AbsMicroWave> getAllMicro() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        List<AbsMicroWave> waves = Lists.newArrayList();
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            for (IDevice device : list) {
                if (device instanceof AbsMicroWave)
                    waves.add((AbsMicroWave) device);
            }
        }
        return waves;
    }

    static public List<AbsSteameOvenOne> getAllSteamOvenOne() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        List<AbsSteameOvenOne> steamovens = Lists.newArrayList();
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            for (IDevice device : list) {
                if (device instanceof AbsSteameOvenOne)
                    steamovens.add((AbsSteameOvenOne) device);
            }
        }
        return steamovens;
    }


    static public List<AbsFan> getAllFan() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        List<AbsFan> fanList = Lists.newArrayList();
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            for (IDevice device : list) {
                if (device instanceof AbsFan)
                    fanList.add((AbsFan) device);
            }
        }
        return fanList;
    }
}
