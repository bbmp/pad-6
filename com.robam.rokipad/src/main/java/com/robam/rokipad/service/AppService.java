package com.robam.rokipad.service;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceLoadCompletedEvent;
import com.legent.plat.events.DeviceSelectedEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.pojos.device.DeviceGroupInfo;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceService;
import com.legent.services.AbsService;
import com.legent.services.CrashLogService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.device.RokiDeviceFactory;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.R;

import java.util.List;

/**
 * Created by sylar on 15/7/24.
 */
public class AppService extends AbsService {
    private static final String TAG = "App_Service";
    //  注释
    private static AppService instance = new AppService();

    synchronized public static AppService getInstance() {
        return instance;
    }

    long userId;

    public IDevice getDefaultDev() {
        return defaultDev;
    }

    private IDevice defaultDev;
    private Callback2 callback_startUI;

    private AppService() {
        EventUtils.regist(this);
    }

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        if (Plat.accountService.isLogon()) {
            onLogin();
            Plat.deviceService.setPolltingPeriod(2000, 2000);
        }

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {

        userId = event.pojo.id;
        onLogin();
        onBindOwner(defaultDev);
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        userId = 0;
        Plat.deviceService.clear();
        Plat.deviceService.add(defaultDev);
        Plat.deviceService.setDefault(defaultDev);
        EventUtils.postEvent(new DeviceLoadCompletedEvent());
    }


    @Subscribe
    public void onEvent(DeviceSelectedEvent event) {
        if (defaultDev == null)
            return;
        IDevice device = DeviceService.getInstance().getDefault();
        if (defaultDev.getID() != device.getID()) {
            Plat.deviceService.add(defaultDev);
            Plat.deviceService.setDefault(defaultDev);
            return;
        }
    }

    // -------------------------------------------------------------------------------
    // private
    // -------------------------------------------------------------------------------

    private int get40CodeReply = 0;

    public void onProbeGuid(final Callback2 callback2) {
        this.callback_startUI = callback2;
        LogUtils.e("20190807", "ZeroGuid:" + DeviceGuid.ZeroGuid);
        Plat.dcSerial.getDevice(DeviceGuid.ZeroGuid, new Callback<DeviceInfo>() {

                    @Override
                    public void onSuccess(DeviceInfo devInfo) {
                        if (devInfo == null) {
                            Log.e(TAG, "获取烟机信息SUC 但结果null");
                            onProbeGuid(callback_startUI);
                            return;
                        }
                        if (Plat.DEBUG)
                            Log.e(TAG, "获取烟机信息SUC:");

                        DeviceInfo deviceInfo_db = DaoHelper.getById(DeviceInfo.class, devInfo.getID());
                        if (deviceInfo_db == null)
                            devInfo.save2db();
                        else {
                            DaoHelper.delete(deviceInfo_db);
                            devInfo.save2db();
                        }

                        IDevice dev1 = RokiDeviceFactory.generateModel(devInfo);
                        if (dev1 == null) {
                            devInfo = new DeviceInfo();
                            devInfo.ownerId = 4272829977L;
                            devInfo.groupId = 0L;
                            devInfo.mac = "00000000000";
                            devInfo.guid = "R8230000000000000";
                            devInfo.bid = "00000000000";
                            devInfo.ver = 0;
                            dev1 = RokiDeviceFactory.generateModel(devInfo);
                        }

                        try {//手动设置串口设备 dc dp dt
                            dev1.setDc(DeviceType.RYYJ);
                            dev1.setDt(dev1.getDeviceType().getID());
                            if (dev1 instanceof AbsFan) {
                                String deviceTypeId = dev1.getGuid().getDeviceTypeId();
                                if (deviceTypeId.equals(IRokiFamily._8236S)) {
                                    dev1.setDisplayType(IRokiFamily._8236S);
                                } else if (deviceTypeId.equals(IRokiFamily._5916S)) {
                                    dev1.setDisplayType(IRokiFamily._5916S);

                                }
                            }
                            List<IDevice> lists = ((AbsFan) dev1).getChildList();
                            if (lists != null && lists.size() > 0) {
                                for (IDevice device : lists) {
                                    if (device instanceof Stove) {
                                        Stove stove = (Stove) device;
                                        String deviceType = stove.getGuid().getDeviceTypeId();
                                        stove.setDc(DeviceType.RRQZ);
                                        stove.setDt(stove.getGuid().getDeviceTypeId());
                                        if (IRokiFamily.R9B37.equals(deviceType)
                                                || IRokiFamily.R9B39.equals(deviceType)
                                                || IRokiFamily._9B39E.equals(deviceType)
                                                || IRokiFamily._9B515.equals(deviceType)
                                                || IRokiFamily.R9B30C.equals(deviceType)) {
                                            stove.setDp(IRokiFamily.IRokiDevicePlat.RQZ02);

                                            if (IRokiFamily.R9B37.equals(deviceType)) {
                                                stove.setDisplayType(IRokiFamily.R9B37);
                                            } else if (IRokiFamily.R9B39.equals(deviceType)) {
                                                stove.setDisplayType(IRokiFamily.R9B39);
                                            } else if (IRokiFamily._9B39E.equals(deviceType)) {
                                                stove.setDisplayType(IRokiFamily._9B39E);
                                            } else if (IRokiFamily.R9B30C.equals(deviceType)) {
                                                stove.setDisplayType(IRokiFamily.R9B30C);
                                            } else if (IRokiFamily._9B515.equals(deviceType)) {
                                                stove.setDisplayType(IRokiFamily._9B515);
                                            }
                                        } else if (IRokiFamily.R9W70.equals(deviceType)) {
                                            stove.setDp(IRokiFamily.IRokiDevicePlat.RQZ01);
                                            stove.setDisplayType(IRokiFamily.R9W70);
                                        }else if (IRokiFamily._9W851.equals(deviceType)){
                                            stove.setDp(IRokiFamily.IRokiDevicePlat.RQZ05);
                                            stove.setDisplayType(IRokiFamily._9W851);
                                        }
                                        if (stove.isHardIsConnected() && StringUtils.isNullOrEmpty(Plat.getStoveGuid())) {
                                            Plat.setStoveGuid(new String(stove.getID()));
                                        }
                                    } else if (device instanceof Pot) {
                                        Pot pot = (Pot) device;
                                        String deviceType = pot.getGuid().getDeviceTypeId();
                                        pot.setDc(DeviceType.RPOT);
                                        pot.setDt(deviceType);
                                        if (pot.isHardIsConnected() && StringUtils.isNullOrEmpty(Plat.getPotGuid())) {
                                            Plat.setPotGuid(new String(pot.getID()));
                                        }
                                    }
                                }

                                if (StringUtils.isNullOrEmpty(Plat.getStoveGuid())) {
                                    if (lists != null && lists.size() > 0) {
                                        for (IDevice device : lists) {
                                            if (device instanceof Stove) {
                                                String stoveID = new String(device.getID());
                                                Plat.setStoveGuid(stoveID);
                                            }
                                        }
                                    }
                                }
                                if (StringUtils.isNullOrEmpty(Plat.getPotGuid())) {
                                    if (lists != null && lists.size() > 0) {
                                        for (IDevice device : lists) {
                                            if (device instanceof Pot) {
                                                String stoveID = new String(device.getID());
                                                Plat.setPotGuid(stoveID);
                                            }
                                        }
                                    }
                                }

                            }
                        } catch (Exception e) {
                        }
                        IDevice already_Device = DeviceService.getInstance().queryById(dev1.getID());
                        Log.e("20181220", "already_Device:" + already_Device);
                        if (already_Device != null) {
                            DeviceService.getInstance().delete(already_Device);
                            Plat.deviceService.setDefault(null);
                        }
                        Plat.deviceService.addDefaultFan(dev1);
                        Plat.deviceService.setDefault(dev1);
                        defaultDev = dev1;
                        DeviceService.isAppInit = false;
                        Plat.setFanGuid(defaultDev.getGuid());
                        if (callback_startUI != null) {
                            callback_startUI.onCompleted(null);
                            callback_startUI = null;
                        }
                        Plat.dcSerial.setFanChangeCallBack(new Callback2() {
                            @Override
                            public void onCompleted(Object o) {
                                onProbeGuid2();
                            }
                        });
                        onBindOwner(dev1);
                    }

                    @Override
                    public void onFailure(final Throwable t) {
                        LogUtils.e("20200319", "t:" + t.toString());
                        get40CodeReply++;
                        if (get40CodeReply >= 10) {
                            CrashLogService.getInstance().handleException(t);
                            ToastUtils.showLong(cx.getString(R.string.stopOnStart));
                        } else {
                            if (Plat.DEBUG)
                                LogUtils.e("20200319", "获取烟机信息FAIL:" + t.getMessage());
                            t.printStackTrace();
                        }
                        onProbeGuid(callback_startUI);

                    }
                }

        );
    }

    private void onBindOwner(IDevice iDevice) {
        if (!Plat.accountService.isLogon())
            return;
        final AbsFan fan = (AbsFan) iDevice;
        if (fan == null)
            return;
        if (Plat.DEBUG)
            LogUtils.i(TAG, "wifi 密码配置开始执行 " + "烟机：" + fan.getID() + " userId:" + userId);
        Plat.dcSerial.setOwnerId(fan.getID(), userId, new VoidCallback() {
            @Override
            public void onSuccess() {
                if (Plat.DEBUG)
                    LogUtils.i(TAG, "wifi 密码配置成功");
            }

            @Override
            public void onFailure(Throwable t) {
                if (Plat.DEBUG)
                    LogUtils.i(TAG, "wifi 密码配置失败-->" + t.getMessage());
            }
        });
        if (Plat.DEBUG)
            LogUtils.i(TAG, "绑定程序开始执行 用户：" + userId + " 烟机：" + fan.getID() + " 烟机名称;" + fan.getName());
        Plat.deviceService.bindDevice(userId, fan.getID(), fan.getName(), true, new VoidCallback() {
            @Override
            public void onSuccess() {
                if (Plat.DEBUG)
                    LogUtils.i(TAG, "绑定程序执行返回-successful");
            }

            @Override
            public void onFailure(Throwable t) {
                if (Plat.DEBUG)
                    LogUtils.i(TAG, "绑定程序执行返回-failure" + t.getMessage());
            }
        });

    }

    private void onLogin() {
        Plat.deviceService.clearButDefaultDevice(defaultDev);
        onLoadGroup();
        onLoadDevice();
    }

    private void onLoadGroup() {

        Plat.deviceService.getDeviceGroups(userId, new Callback<List<DeviceGroupInfo>>() {

            @Override
            public void onSuccess(List<DeviceGroupInfo> groups) {
                if (groups != null) {
                    Plat.deviceService.batchAddGroup(groups);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void onLoadDevice() {
        Plat.deviceService.getDevices(userId, new Callback<List<DeviceInfo>>() {
            @Override
            public void onSuccess(List<DeviceInfo> result) {
                if (result != null) {
                    List<IDevice> devices = Lists.newArrayList();
                    for (DeviceInfo deviceInfo : result) {
                        if (defaultDev != null && defaultDev.getID().equals(deviceInfo.getID()))
                            continue;
                        IDevice dev = RokiDeviceFactory.generateModel(deviceInfo);
                        if (dev != null)
                            devices.add(dev);
                    }
                    Plat.deviceService.batchAdd(devices);
                }
                EventUtils.postEvent(new DeviceLoadCompletedEvent());
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void onProbeGuid2() {
        Plat.dcSerial.getDevice(DeviceGuid.ZeroGuid, new Callback<DeviceInfo>() {

            @Override
            public void onSuccess(DeviceInfo devInfo) {
                if (devInfo == null) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        onProbeGuid2();
                    }

                    return;
                }
                ToastUtils.show(new String("灶具变更成功"));
                DeviceInfo deviceInfo_db = DaoHelper.getById(DeviceInfo.class, devInfo.getID());
                if (deviceInfo_db == null)
                    devInfo.save2db();
                else {
                    DaoHelper.delete(deviceInfo_db);
                    devInfo.save2db();
                }

                IDevice dev1 = RokiDeviceFactory.generateModel(devInfo);
                if (dev1 == null)
                    return;
                if (dev1.getDc() == null)
                    dev1.setDc(DeviceType.RYYJ);
                IDevice already_Device = DeviceService.getInstance().queryById(dev1.getID());
                if (already_Device != null) {
                    DeviceService.getInstance().delete(already_Device);
                    Plat.deviceService.setDefault(null);
                }
                Plat.deviceService.addDefaultFan(dev1);
                Plat.deviceService.setDefault(dev1);
                defaultDev = dev1;
            }

            @Override
            public void onFailure(Throwable t) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    onProbeGuid2();
                }
            }
        });
    }
}
