package com.legent.plat.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.RemoteException;

import com.cvte.mid.systemguardian.ICvteSystemGuardian;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.device.IDeviceHub;
import com.legent.plat.utils.PatchUtils;
import com.legent.services.ScreenPowerService;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.AlarmUtils;

import java.util.List;

/**
 * Created by sylar on 15/7/25.
 */
public class DevicePollingReceiver extends BroadcastReceiver {
    private static ICvteSystemGuardian mCuardService;

    public static void setIsFeedDog(boolean isFeedDog) {
        DevicePollingReceiver.isFeedDog = isFeedDog;
    }


    private static boolean isFeedDog = true;

    //当前需要轮询的设备
    private static String currentPollingDevice;

    public static void setCurrentPollingDevice(String currentPollingDevice) {
        DevicePollingReceiver.currentPollingDevice = currentPollingDevice;

    }

    public static void setCuardService(ICvteSystemGuardian iCuardService) {
        mCuardService = iCuardService;
    }

    static public Intent getIntent(Context cx) {
        Intent intent = new Intent(cx, DevicePollingReceiver.class);
        intent.setAction(DevicePollingReceiver.class.getName());
        return intent;
    }

    @Override
    public void onReceive(Context cx, Intent i) {

        try {
            onPolling();
        } catch (Exception e) {
            LogUtils.logFIleWithTime("轮询出错:" + e.getMessage());
        }

        if (Build.VERSION.SDK_INT >= 19) {
            /** API 19 后的 AlarmManager 不再提供精准闹钟
             *
             * From API level 19, all repeating alarms are inexact—that is, if our application targets KitKat or above, our repeat alarms will be inexact even if we use setRepeating.
             If we really need exact repeat alarms, we can use setExact instead, and schedule the next alarm while handling the current one.
             *
             * **/
            AlarmUtils.startPollingWithBroadcast(cx,
                    DevicePollingReceiver.getIntent(cx),
                    Plat.deviceService.getPollingPeriod(),
                    Plat.deviceService.getPollingTaskId());
        }

    }


    /**
     * 轮训任务
     */
    protected Runnable AppPollingTask = new Runnable() {

        @Override
        public void run() {
            onPolling();
        }
    };
    static Integer[] mPollingNums = new Integer[]{-1};

    void onPolling() {
//        if (Plat.LOG_FILE_ENABLE) {
//            LogUtils.logFIleWithTime("开始轮询");
//        }
        //熄屏状态下禁止轮询
        if (Plat.appType.equals(IAppType.RKDRD)) {
            if (!ScreenPowerService.getInstance().isScreenOn())
                return;
        } else if (Plat.appType.equals(IAppType.RKPBD)) {
            try {
                if (false) {
                    PatchUtils.atTimeAndFix(mPollingNums);
                    mPollingNums[0]++;
                }
            } catch (Exception e) {
            }
            if (mCuardService != null) {
                try {
                    if (isFeedDog) {
                        mCuardService.feedAppWatcher();
                        if (Plat.DEBUG)
                            LogUtils.i("20170308", "feed dog");
                    }
                } catch (RemoteException e) {
                }
            }

        }
        List<IDevice> devices = null;
        if (Plat.appType.equals(IAppType.RKPBD)) {
            devices = Plat.deviceService.queryPollingAll();
        } else {
            devices = Plat.deviceService.queryAll();
        }
        //devices = Plat.deviceService.queryAll();
        LogUtils.i("devices_polling","devices.size():"+devices.size());
        if (devices != null && devices.size() > 0) {
            for (IDevice dev : devices) {
                try {
                    if (dev == null)
                        continue;

                    onPolling(dev);

                    if (IDeviceType.RYYJ.equals(dev.getDc()) && dev instanceof IDeviceHub) {
                        IDeviceHub hub = (IDeviceHub) dev;
                        if (IAppType.RKDRD.equals(Plat.appType)) {
                            List<IDevice> children = hub.getChildren();
                            if (children != null && children.size() > 0) {
                                for (IDevice device : children) {
                                    if (Plat.DEBUG)
                                        LogUtils.i("stove_polling", device.getID() + "   device.isHardIsConnected():" + device.isHardIsConnected());
                                    if (device.isHardIsConnected())
                                        onPolling(device);
                                }
                            }
                        } else {
                            if (!StringUtils.isNullOrEmpty(Plat.getStoveGuid())) {
                                IDevice device = hub.getChild(Plat.getStoveGuid());
                                onPolling(device);
                            }
                            if (!StringUtils.isNullOrEmpty(Plat.getPotGuid())) {
                                IDevice device = hub.getChild(Plat.getPotGuid());
                                onPolling(device);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    void onPolling(IDevice device) {
        LogUtils.i("devices_polling","device.getDc():"+device.getDc());
        if (device.getDc().equals(IDeviceType.RRQZ) || device.getDc().equals(IDeviceType.RPOT)) {
            device.onPolling();
            device.onCheckConnection();
            return;
        }
        device.onPolling();
        device.onCheckConnection();
    }

}
