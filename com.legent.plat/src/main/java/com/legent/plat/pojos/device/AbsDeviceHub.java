package com.legent.plat.pojos.device;

import android.content.Context;
import android.text.TextUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.constant.IDeviceDp;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.DevicePotOnlineSwitchEvent;
import com.legent.plat.events.DeviceStoveOnlineSwitchEvent;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/7/22.
 */
abstract public class AbsDeviceHub extends AbsDevice implements IDeviceHub {

    public long ownerId;
    public String mac;
    protected long groupId;
    protected Map<String, IDevice> map = Maps.newConcurrentMap();
    private String toast2Stove = null;
    private String toast2Pot = null;
    private static final String TAG = "AbsDeviceHub";

    public AbsDeviceHub(DeviceInfo devInfo) {
        super(devInfo);

        ownerId = devInfo.ownerId;
        groupId = devInfo.groupId;
        mac = devInfo.mac;

        if (devInfo.subDevices != null) {
            for (SubDeviceInfo subDevice : devInfo.subDevices) {
                try {
                    IDevice dev = newSubDevice(subDevice);
                    LogUtils.e("20190522", "dev:" + dev);
                    Preconditions.checkNotNull(dev, "newSubDevice() return null.\n subDeviceInfo:" + subDevice.guid);
                    dev.setParent(this);
                    LogUtils.e(TAG, "subDevice.guid:" + subDevice.guid);
                    map.put(subDevice.guid, dev);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        if (map != null) {
            for (IDevice dev : map.values()) {
                dev.init(cx, params);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        childDispose();
    }

    @Override
    public void childDispose() {
        if (map != null) {
            for (IDevice dev : map.values()) {
                dev.dispose();
            }
        }
    }

    public boolean isOwner() {
        return ownerId == getCurrentUserId();
    }

    protected IDevice newSubDevice(SubDeviceInfo subDevice) {
        return Plat.deviceFactory.generate(subDevice);
    }

    @Override
    public long getGroupId() {
        return groupId;
    }


    @Override
    public <T extends IDevice> T getChild(String guid) {
        return (T) map.get(guid);
    }

    @Override
    public <T extends IDevice> T getChild(int index) {
        if (index > 0 && index < map.size()) {
            return (T) Lists.newArrayList(map.values()).get(index);
        }
        return null;
    }

    @Override
    public <T extends IDevice> T getChildByDeviceType(String deviceTypeId) {
        List<T> list = getChildrenByDeviceType(deviceTypeId);
        LogUtils.e("20190522", "getChildByDeviceType list:" + list);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public <T extends IDevice> List<T> getChildrenByDeviceType(String deviceTypeId) {
        List<T> list = Lists.newArrayList();
        for (IDevice dev : map.values()) {
            if (DeviceTypeManager.getInstance().isInDeviceType(dev.getGuid(), deviceTypeId)) {
                list.add((T) dev);
            }
        }
        return list;
    }

    @Override
    public <T extends IDevice> T getChild() {
        return getChildStove();
    }

    public <T extends IDevice> T getChildStove() {
        List<T> list = Lists.newArrayList();
        for (IDevice dev : map.values()) {
            if (IDeviceType.RRQZ.equals(dev.getDc()))
                list.add((T) dev);
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public <T extends IDevice> T getChildPot() {
        List<T> list = Lists.newArrayList();
        for (IDevice dev : map.values()) {
            if (IDeviceType.RPOT.equals(dev.getDc()))
                list.add((T) dev);
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    // by zhoudj 20170106
    public List<IDevice> getChildList() {
        List<IDevice> list = Lists.newArrayList();
        for (IDevice dev : map.values()) {
            list.add(dev);
        }
        return list;

    }


    @Override
    public List<IDevice> getChildren() {
        return Lists.newArrayList(map.values());
    }

    /**
     * @return 默认设备是否发生变化
     */
    private int mapUpdate(Map<String, IDevice> map, List<SubDeviceInfo> children) throws Exception {

        LogUtils.e("20190522", " children:" + children);
        if (children == null || children.size() <= 0)
            return 0;
        childDispose();

        String stoveGuid = null;
        String potGuid = null;
        int filter = 0;

        try {
            for (SubDeviceInfo subDeviceInfo : children) {
                IDevice device = newSubDevice(subDeviceInfo);
                LogUtils.i("AbsOioBus", "device =null:" + (device == null));
                if (null != device && null != device.getGuid()) {
                    String dt = device.getGuid().getDeviceTypeId();
                    device.setDt(dt);

                    LogUtils.e(TAG, "subDeviceInfo ID:" + subDeviceInfo.getID());
                    if (subDeviceInfo.getID().startsWith("R9W70")
                            || subDeviceInfo.getID().startsWith("R9B37")
                            || subDeviceInfo.getID().startsWith("R9B39")
                            || subDeviceInfo.getID().startsWith("HI704")
                            || subDeviceInfo.getID().startsWith("9B515")
                            || subDeviceInfo.getID().startsWith("9B39L")
                            || subDeviceInfo.getID().startsWith("9W851")
                            || subDeviceInfo.getID().startsWith("9B39E")
                    ) {

                        IDevice TmpDev = getChild(subDeviceInfo.guid);
                        if (TmpDev == null) {
                            toast2Stove = new String("灶具添加成功");
                            filter = filter | 2;
                        } else if (TmpDev.isHardIsConnected() != subDeviceInfo.isConnected) {
                            toast2Stove = subDeviceInfo.isConnected ? new String("灶具上线") : new String("灶具离线");
                            filter = filter | 2;
                        }

                        stoveGuid = new String(subDeviceInfo.getID());

                        device.setDc(IDeviceType.RRQZ);
                        if ("9B39L".equals(dt) || "R9B37".equals(dt) || "R9B39".equals(dt) || "9B515".equals(dt) || "9B39E".equals(dt)) {
                            device.setDp(IDeviceDp.RQZ02);
                        } else if ("9W851".equals(dt)) {
                            device.setDp(IDeviceDp.RQZ05);
                        } else {
                            device.setDp(IDeviceDp.RQZ01);
                        }
                    }
                    if (subDeviceInfo.getID().startsWith("R0001")) {

                        IDevice TmpDev = getChild(subDeviceInfo.guid);


                        if (TmpDev == null) {
                            toast2Pot = new String("无人锅添加成功");
                            filter = filter | 1;
                        } else if (TmpDev.isHardIsConnected() != subDeviceInfo.isConnected) {
                            toast2Pot = subDeviceInfo.isConnected ? new String("无人锅上线") : new String("无人锅离线");
                            filter = filter | 1;
                        }

                        potGuid = new String(subDeviceInfo.getID());
                        device.setDc(IDeviceType.RPOT);
                        if ("R0001".equals(dt)) {
                            device.setDp(IDeviceDp.ZNG01);
                        }
                    }
                    device.setParent(this);
                    LogUtils.i("AbsOioBus", "map.get(subDeviceInfo.guid) == null:" + (map.get(subDeviceInfo.guid) == null) + "  map.values().size():" + map.values().size());
                    if (map.get(subDeviceInfo.guid) == null) {  // todo
                        if (Plat.DEBUG)
                            LogUtils.i("AbsOioBus", "添加设备进map ID：" + device.getID() + ":传递进来的hashcode:" + map.hashCode());
                        map.put(subDeviceInfo.guid, device);
                    } else {
//                    map.get(subDeviceInfo.guid).setConnected(subDeviceInfo.isConnected); //map.put(subDeviceInfo.guid, device);
                        map.get(subDeviceInfo.guid).setHardIsConnected(subDeviceInfo.isConnected);
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e("20190512", "e:" + e.toString());
            LogUtils.e("20190512", "e:" + e.getCause());
        }

        Iterator<Map.Entry<String, IDevice>> it = map.entrySet().iterator();
        if (it != null) {
            while (it.hasNext()) {
                try {
                    Map.Entry<String, IDevice> entry = it.next();
                    String guid = entry.getKey();
                    if (Plat.DEBUG)
                        LogUtils.i("mapUpdate", "遍历map map keySet size guid:" + guid);
                    boolean ishas = false;
                    for (SubDeviceInfo subDeviceInfo : children) {
                        if (Plat.DEBUG)
                            LogUtils.i("mapUpdate", "遍历map map keySet size 子 guid:" + subDeviceInfo.getID());
                        if (subDeviceInfo.getID().equals(guid)) {
                            ishas = true;
                            break;
                        }
                    }
                    if (Plat.DEBUG)
                        LogUtils.i("mapUpdate", "遍历map map keySet 删除 前 子设备:" + guid);
                    if (!ishas)
                        it.remove();
                    if (Plat.DEBUG)
                        LogUtils.i("mapUpdate", "遍历map map keySet 删除 后 子设备:" + guid);
                } catch (Exception e) {
                    if (Plat.DEBUG)
                        LogUtils.i("mapUpdate", e == null ? "循环查看错误" : "循环查看错误:" + e.getMessage());
                }
            }
        } else {
            if (Plat.DEBUG) {
                LogUtils.i("mapUpdate", "遍历map map keySet is null");
            }
        }
        if (stoveGuid == null) {
            for (SubDeviceInfo deviceInfo : children) {
                if (deviceInfo.getID().startsWith("R9W70") || deviceInfo.getID().startsWith("R9B37")
                        || deviceInfo.getID().startsWith("R9B39") || deviceInfo.getID().startsWith("HI704")
                        || deviceInfo.getID().startsWith("9B39L") || deviceInfo.getID().startsWith("9B515")
                        || deviceInfo.getID().startsWith("9W851") || deviceInfo.getID().startsWith("9B39E")) {
                    stoveGuid = new String(deviceInfo.getID());
                    break;
                }
            }
        }
        if (potGuid == null) {
            for (SubDeviceInfo deviceInfo : children) {
                if (deviceInfo.getID().startsWith("R0001")) {
                    potGuid = new String(deviceInfo.getID());
                    break;
                }
            }
        }
        String stoveid_ = stoveGuid; // != null ? new String(stoveGuid) : null
        String potId_ = potGuid; // != null ? new String(potGuid) : null

        LogUtils.e(TAG, "Plat.getStoveGuid():" + Plat.getStoveGuid() + " Plat.getPotGuid():" + Plat.getPotGuid());
        if (StringUtils.isNullOrEmpty(Plat.getStoveGuid())) {
            if (stoveid_ == null) {
                //filter = filter & 1;
            } else {
                filter = filter | 2;
                toast2Stove = new String("灶具添加成功");
            }
        } else {
            if (stoveid_ == null) {
                toast2Stove = new String("灶具被移除");
                filter = filter | 2;
            } else if (Plat.getStoveGuid().equals(stoveid_)) {
                //filter = filter & 1;
            } else {
                toast2Stove = new String("灶具被更换");
                filter = filter | 2;
            }
        }
        if (StringUtils.isNullOrEmpty(Plat.getPotGuid())) {
            if (potId_ == null) {
                //filter = filter & 2;
            } else {
                filter = filter | 1;
                toast2Pot = new String("无人锅添加成功");
            }
        } else {
            if (potId_ == null) {
                filter = filter | 1;
                toast2Pot = new String("无人锅被移除");
            } else if (Plat.getPotGuid().equals(potId_)) {
                //filter = filter & 2;
            } else {
                toast2Pot = new String("无人锅被更换");
                filter = filter | 1;
            }
        }

        LogUtils.e("20190522", "stoveid_:" + stoveid_);
        Plat.setStoveGuid(stoveid_);
        Plat.setPotGuid(potId_);
        return filter;
    }

    @Override
    public synchronized void onChildrenChanged(List<SubDeviceInfo> children) {
        try {
            if (IAppType.RKPBD.equals(Plat.appType)) {
                if (Plat.DEBUG) {
                    LogUtils.i("AbsOioBus", "默认设备----前：灶具：" + Plat.getStoveGuid() + " 锅：" + Plat.getPotGuid());
                    for (IDevice dev : map.values()) {
                        if (Plat.DEBUG)
                            LogUtils.i("AbsOioBus", "更换前子设备：" + dev.getID() + " " + dev.isHardIsConnected());
                    }
                }
                int filter = 0;
                if (children != null && children.size() > 0) {
                    try {
                        filter = mapUpdate(map, children);
                    } catch (Exception e) {
                        if (Plat.DEBUG) {
                            LogUtils.i("AbsOioBus", e == null ? "e不为空 mapUpdate设备上线解析异常" : "e为空:" + e.getCause().getMessage());
                        }
                    }

                } else {
                    childDispose();
                    map.clear();
                    if (!StringUtils.isNullOrEmpty(Plat.getStoveGuid())) {
                        Plat.setStoveGuid(null);
                        filter = filter | 2;
                        toast2Stove = new String("灶具被移除");
                    }
                    if (!StringUtils.isNullOrEmpty(Plat.getPotGuid())) {
                        Plat.setPotGuid(null);
                        filter = filter | 1;
                        toast2Pot = new String("无人锅被移除");
                    }
                }

                if (Plat.DEBUG) {
                    LogUtils.i("AbsOioBus", "默认设备----后：灶具：" + Plat.getStoveGuid() + " 锅：" + Plat.getPotGuid() + " map.values().size():" + map.values().size());
                    for (IDevice dev : map.values()) {
                        LogUtils.i("AbsOioBus", "更换后子设备：" + dev.getID() + " " + dev.isHardIsConnected());
                    }
                }
                StringBuilder res_str = new StringBuilder();
                if (Plat.DEBUG) {
                    LogUtils.i("AbsOioBus", "filter:" + filter + "  (filter & 1):" + (filter & 1) + " (filter & 2):" + (filter & 2));
                }
                if ((filter & 1) == 1) {
                    LogUtils.i("AbsOioBus", "原生:map.hashCode():" + map.hashCode());
                    EventUtils.postEvent(new DevicePotOnlineSwitchEvent());
                    if (Plat.DEBUG) {
                        LogUtils.e("AbsOioBus", "无人锅发生变化事件 ");
                    }
                    res_str.append(new String(toast2Pot));
                }

                if ((filter & 2) == 2) {
                    EventUtils.postEvent(new DeviceStoveOnlineSwitchEvent(children));
                    if (Plat.DEBUG) {
                        LogUtils.e("AbsOioBus", "灶具发生变化事件 ");
                    }
                    res_str.append(" " + new String(toast2Stove));
                }

                if (!TextUtils.isEmpty(res_str.toString())) {
                    ToastUtils.showShort(res_str.toString());
                }
                return;
            }
            dealDeviceChange(children);

        } catch (Exception e) {
            if (Plat.DEBUG) {
                LogUtils.i("AbsOioBus", e == null ? "onChildrenChanged设备上线异常" : "onChildrenChanged设备上线异常" + e.getMessage());
            }
        }
    }

    private void dealDeviceChange(List<SubDeviceInfo> children) {

        LogUtils.e("20190522", "dev children:" + children);
        // 先暂置无效标记
        for (IDevice dev : map.values()) {
            dev.setValid(false);
        }

        // 处理变更
        for (SubDeviceInfo devInfo : children) {
            if (map.containsKey(devInfo.guid)) {
                // 如果变更后仍存在，置为有效
                map.get(devInfo.guid).setValid(true);
            } else {
                // 变更后新增的设备
                IDevice dev = newSubDevice(devInfo);
                dev.setParent(this);
                map.put(devInfo.guid, dev);
            }
        }

        // 无效设备更改连接状态
        for (IDevice dev : map.values()) {
            if (!dev.getValid()) {
                dev.setConnected(false);
            }
        }
    }

    @Override
    public void setWifiParam(String wifiSsid, String wifiPwd,
                             VoidCallback callback) {
        if (Plat.dcMqtt != null)
            dcMqtt.setWifiParam(id, wifiSsid, wifiPwd, callback);
        if (Plat.dcSerial != null)
            dcSerial.setWifiParam(id, wifiSsid, wifiPwd, callback);

        dcMqtt.setWifiParam(id, wifiSsid, wifiPwd, callback);
    }

    @Override
    public void setOwnerId(long ownerId, VoidCallback callback) {
        if (Plat.dcMqtt != null)
            dcMqtt.setOwnerId(id, ownerId, callback);
        if (Plat.dcSerial != null)
            dcSerial.setOwnerId(id, ownerId, callback);

        dcMqtt.setOwnerId(id, ownerId, callback);
    }

}
