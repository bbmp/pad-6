package com.legent.plat.services;

import android.content.Context;
import android.os.Build;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.events.ScreenPowerChangedEvent;
import com.legent.plat.Plat;
import com.legent.plat.R;
import com.legent.plat.constant.IAppType;
import com.legent.plat.events.DeviceAddedEvent;
import com.legent.plat.events.DeviceCollectionChangedEvent;
import com.legent.plat.events.DeviceConnectedNoticEvent;
import com.legent.plat.events.DeviceDeletedEvent;
import com.legent.plat.events.DeviceSelectedEvent;
import com.legent.plat.events.DeviceUpdatedEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.events.ViewChangedEvent;
import com.legent.plat.pojos.CurrentDeviceInfo;
import com.legent.plat.pojos.device.DeviceGroupInfo;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.device.IDeviceHub;
import com.legent.services.CrashLogService;
import com.legent.services.ScreenPowerService;
import com.legent.services.TaskService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.AlarmUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DeviceService extends AbsDeviceCloudService {

    private static DeviceService instance = new DeviceService();

    synchronized public static DeviceService getInstance() {
        return instance;
    }

    private DeviceService() {

    }

    protected long userId;
    protected long pollingPeriodInBack = 1000 * 10;
    protected long pollingPeriodInFront = 1000 * 2;
    protected long pollingPeriod = pollingPeriodInFront;
    protected IDevice defPojo;
    protected Map<Long, DeviceGroupInfo> mapGroups = Maps.newConcurrentMap(); // ?????? ???????????????Map,  liyuebiao
    protected Map<String, IDevice> mapDevice = Maps.newConcurrentMap();  // ?????? ???????????????Map, liyuebiao
    protected Map<Long, List<IDevice>> mapTree = Maps.newConcurrentMap(); // ?????? ???????????????Map, liyuebiao
    protected ScheduledFuture<?> future;

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);
        startPolling(false);
    }

    /**
     * ????????? App ??????????????????????????????????????????????????????  liyuebiao
     */
    private void startPollingSchedule(long pollingPeriod) {
        future = TaskService.getInstance().scheduleAtFixedRate(
                new DevicePollingReceiver().AppPollingTask,
                1000 * 2, pollingPeriod, TimeUnit.MILLISECONDS);
    }

    /**
     * ????????? App ??????????????????????????????????????????????????????  liyuebiao
     */
    private void stopPollingSchedule() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        stopPollingSchedule();
        stopPolling();
    }

    // -------------------------------------------------------------------------------
    // onEvent
    // -------------------------------------------------------------------------------

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        userId = event.pojo.id;
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        userId = 0;
    }

    @Subscribe
    public void onEvent(ScreenPowerChangedEvent event) {
        boolean isInBack = event.powerStatus == ScreenPowerService.OFF;
        LogUtils.logFIleWithTime("\n\n");
        LogUtils.logFIleWithTime("---------------------------------------------");
        LogUtils.logFIleWithTime(String.format("App ???????????????:%s", isInBack ? "??????" : "??????"));
        LogUtils.logFIleWithTime("---------------------------------------------\n\n");
        startPolling(isInBack);
    }

    @Subscribe
    public void onEvent(DeviceConnectedNoticEvent event) {
        if (Plat.DEBUG)
            LogUtils.e("20190522", "dev guid:" + event.deviceInfo.guid);
        LogUtils.e("20190522", "dev deviceInfo:" + event.deviceInfo);
        try {//?????????????????????????????????????????? ??????pad??????????????????????????????
//            DeviceInfo deviceInfo = event.deviceInfo;
//            String guid = deviceInfo.guid;
//            if (!guid.equals(Plat.getFanGuid().getGuid())) {
//                CrashLogService.exceptionlog(new String("???????????????????????????guid??????????????????old:")
//                        + Plat.getFanGuid().getGuid() + " new:" + guid);
//                return;
//            }

        } catch (Exception e) {
            LogUtils.e("20190522", "Exception :" + e.getMessage());
            CrashLogService.exceptionlog(e != null ? e.getMessage() : new String("32?????????????????????"));
            return;
        }

        IDevice dev = queryById(event.deviceInfo.guid);
        LogUtils.e("20190522", "dev dev:" + dev);
        if (dev == null) {
            if (IAppType.RKPBD.equals(Plat.appType)) {
                Plat.GlobalOnSwitching = false;
            }
            return;
        }
        if (dev instanceof IDeviceHub) {
            IDeviceHub hub = (IDeviceHub) dev;
            hub.onChildrenChanged(event.deviceInfo.subDevices);
        }

    }

    // -------------------------------------------------------------------------------
    // device cache manager start
    // -------------------------------------------------------------------------------

    /**
     * ????????????(????????????)
     */
    public synchronized <T extends IDevice> T lookupChild(String guid) {
        if (guid == null || guid.equals(""))
            return null;

        for (IDevice dev : mapDevice.values()) {
//            LogUtils.i("devices_polling","dev.getID():"+dev.getID() +" guid:"+ guid);
            if (Objects.equal(dev.getID(), guid)) {
                return (T) dev;
            } else if (dev instanceof IDeviceHub) {
                IDeviceHub hub = (IDeviceHub) dev;
                IDevice child = hub.getChild(guid);
                if (Plat.DEBUG)
                    for (IDevice device : hub.getChildren()) {
                        LogUtils.i("20170326", "-->" + device.getID());
                    }
                if (child != null) {
                    return (T) child;
                }
            }
        }
        return null;
    }

    public List<DeviceGroupInfo> getGroups() {
        return Lists.newArrayList(mapGroups.values());
    }

    public List<IDevice> getDevicesByGroup(long groupId) {
        if (!mapTree.containsKey(groupId)) {
            List<IDevice> list = Lists.newArrayList();
            mapTree.put(groupId, list);
        }

        return mapTree.get(groupId);
    }

    public void batchAddGroup(List<DeviceGroupInfo> deviceGroupInfoList) {
        if (deviceGroupInfoList == null || deviceGroupInfoList.size() == 0)
            return;

        for (DeviceGroupInfo deviceGroupInfo : deviceGroupInfoList) {
            mapGroups.put(deviceGroupInfo.getID(), deviceGroupInfo);
        }
    }

    public synchronized void batchAdd(List<IDevice> list) {
        if (list == null || list.size() == 0)
            return;
        for (IDevice dev : list) {
            add(dev);
        }
        mappingGroup();
    }

    protected synchronized void mappingGroup() {
        mapTree.clear();
        List<IDevice> devices = queryAll();

        if (devices == null)
            return;

        for (IDevice device : devices) {
            if (device instanceof IDeviceHub) {
                IDeviceHub hub = (IDeviceHub) device;
                List<IDevice> list = getDevicesByGroup(hub.getGroupId());
                if (!list.contains(device)) {
                    list.add(device);
                }
            }
        }
    }

    public void addWithBind(final String guid, String name, boolean isOwner,
                            final VoidCallback callback) {

        bindDevice(userId, guid, name, isOwner, new VoidCallback() {
            @Override
            public void onSuccess() {
                getDeviceById(userId, guid, new Callback<DeviceInfo>() {

                    @Override
                    public void onSuccess(DeviceInfo deviceInfo) {

                        if (deviceInfo == null || Strings.isNullOrEmpty(deviceInfo.guid)) {
                            Helper.onFailure(callback, new Throwable("deviceInfo'guid is invalid"));
                            return;
                        }

                        if (Plat.deviceFactory != null) {
                            IDevice device = Plat.deviceFactory.generate(deviceInfo);
                            add(device);
                        }

                        Helper.onSuccess(callback);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Helper.onFailure(callback, t);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }

    public void deleteWithUnbind(final long userId, final String guid, final VoidCallback callback) {

        unbindDevice(userId, guid, new VoidCallback() {

            @Override
            public void onSuccess() {
                IDevice device = queryById(guid);
                if (device != null) {
                    delete(device);
                }
                callback.onSuccess();
                Helper.onSuccess(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                Helper.onFailure(callback, t);
            }
        });
    }
    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    public boolean isEmpty() {
        return count() == 0;
    }

    public synchronized long count() {
        return mapDevice.size();
    }

    public synchronized boolean containsId(String id) {
        return mapDevice.containsKey(id);
    }

    public <T extends IDevice> boolean containsPojo(T t) {

        return containsId(t.getID());
    }

    public <T extends IDevice> T queryById(String id) {
        return (T) mapDevice.get(id);
    }

    public synchronized <T extends IDevice> T queryByIndex(int index) {
        List<IDevice> list = queryAll();
        if (list.size() > index)
            return (T) list.get(index);
        else
            return null;
    }

    public <T extends IDevice> List<T> queryDevices() {
        List<T> list = Lists.newArrayList();
        List<IDevice> all = queryAll();
        for (IDevice device : all) {
            list.add((T) device);
        }
        return list;
    }

    public static boolean isAppInit = true;

    public List<IDevice> queryAll() {
        if (isAppInit)
            return new ArrayList<IDevice>();
        return Lists.newArrayList(mapDevice.values());
    }

    public List<IDevice> queryPollingAll() {
        if (isAppInit) {
            return new ArrayList<IDevice>();
        }

        Set<IDevice> set = new HashSet<IDevice>();
        set.add(lookupChild(CurrentDeviceInfo.getCurrentFan()));
        set.add(lookupChild(CurrentDeviceInfo.getCurrentSter()));
        set.add(lookupChild(CurrentDeviceInfo.getCurrentOven()));
        set.add(lookupChild(CurrentDeviceInfo.getCurrentSteam()));
        set.add(lookupChild(CurrentDeviceInfo.getCurrentmic()));
        set.add(lookupChild(CurrentDeviceInfo.getCurrentwater()));
        set.add(lookupChild(CurrentDeviceInfo.getCurrentSteamOven()));
        List<IDevice> lists = Lists.newArrayList();
        lists.addAll(set);
        return lists;
    }

    public synchronized <T extends IDevice> boolean add(T pojo) {
        if (pojo == null || containsPojo(pojo))
            return false;
        mapDevice.put(pojo.getID(), pojo);
        pojo.init(cx);
        //?????????????????????????????????????????????
        onPojoAdded(pojo);
        return true;
    }

    public synchronized <T extends IDevice> void addDefaultFan(T pojo) {
        if (pojo == null)
            return;
        if (containsPojo(pojo)) {
            delete(pojo);
        }
        mapDevice.put(pojo.getID(), pojo);
        pojo.init(cx);
        //?????????????????????????????????????????????
        onPojoAdded(pojo);
    }

    public synchronized <T extends IDevice> boolean delete(T pojo) {
        if (!containsPojo(pojo))
            return false;

        mapDevice.remove(pojo.getID());
        pojo.dispose();

        onPojoDeleted(pojo);
        return true;
    }

    public synchronized <T extends IDevice> boolean update(T pojo) {
        if (!containsPojo(pojo))
            return false;

        mapDevice.put(pojo.getID(), pojo);
        onPojoUpdated(pojo);
        return true;
    }


    public synchronized void clear() {
        for (IDevice dev : mapDevice.values()) {
            dev.dispose();
        }
        mapDevice.clear();
        clearGroupAndTree();
    }

    public synchronized void clearButDefaultDevice(IDevice device) {
        if (device == null)
            clear();
        for (IDevice dev : mapDevice.values()) {
            if (!device.getID().equals(dev.getID())) {
                dev.dispose();
                mapDevice.remove(dev.getID());
            }
        }
        clearGroupAndTree();
    }

    // ????????????????????????????????????
    public void clearExclude(IDevice iDevice) {
        Iterator<Map.Entry<String, IDevice>> it = mapDevice.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, IDevice> entry = it.next();
            if (!iDevice.getID().equals(entry.getKey())) { // ????????????????????????
                IDevice dev = entry.getValue();
                dev.dispose();
                it.remove();
            }
        }
        clearGroupAndTree();
    }

    private void clearGroupAndTree() {
        mapGroups.clear();
        mapTree.clear();
        onCollectionChanged();
    }


    public <T extends IDevice> T getDefault() {
        return (T) defPojo;
    }

    public synchronized <T extends IDevice> void setDefault(T t) {
        defPojo = t;
        onPojoSelected(defPojo);
    }

    protected synchronized <T extends IDevice> void onPojoAdded(T pojo) {
        LogUtils.e("20190522","onPojoAdded pojo:" + pojo.getGuid());
        onCollectionChanged();
        postEvent(new DeviceAddedEvent(pojo));
    }

    protected synchronized <T extends IDevice> void onPojoDeleted(T pojo) {
        onCollectionChanged();
        postEvent(new DeviceDeletedEvent(pojo));
    }

    protected <T extends IDevice> void onPojoUpdated(T pojo) {
        postEvent(new DeviceUpdatedEvent(pojo));
    }

    protected synchronized <T extends IDevice> void onPojoSelected(T pojo) {
        postEvent(new DeviceSelectedEvent(pojo));
    }

    //TODO
    protected synchronized void onCollectionChanged() {
        if (defPojo == null || !containsId(defPojo.getID())) {

            setInternalDefault();
        }
        postEvent(new DeviceCollectionChangedEvent(queryAll()));
    }

    protected synchronized void setInternalDefault() {
        if (IAppType.RKPBD.equals(Plat.appType))
            return;
        if (count() > 0) {
            setDefault(queryByIndex(0));  // ???????????????????????????????????????
        } else {
            setDefault(null);
        }
    }

    // -------------------------------------------------------------------------------
    // device cache manager end
    // -------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------
    // ???????????? start
    // -------------------------------------------------------------------------------

    public long getPollingPeriod() {
        return pollingPeriod;
    }

    public int getPollingTaskId() {
        return R.id.device_polling_task_id;
    }

    public int getUpdatePollingTaskId() {
        return R.id.apk_polling_task_id;
    }

    public void setPolltingPeriod(long periodInFront, long periodInBack) {
        this.pollingPeriodInFront = periodInFront < 1000 ? 1000 : periodInFront;
        this.pollingPeriodInBack = periodInBack < 1000 ? 1000 : periodInBack;
    }

    private void startPolling(boolean isInBack) {
        pollingPeriod = getPollingInterval(isInBack);
        //???????????????APP???????????????Andorid ?????? 23 ??????????????????startPollingSchedule ???????????? liyuebiao
        if ("RKDRD".equals(Plat.appType) && Build.VERSION.SDK_INT >= 23) {
            if (isInBack) {
                stopPollingSchedule();
                AlarmUtils.startPollingWithBroadcast(cx,
                        DevicePollingReceiver.getIntent(cx),
                        pollingPeriod,
                        getPollingTaskId());
            } else {
                stopPolling();
                startPollingSchedule(pollingPeriod);
            }
        } else {

            AlarmUtils.startPollingWithBroadcast(cx,
                    DevicePollingReceiver.getIntent(cx),
                    pollingPeriod,
                    getPollingTaskId());
        }
    }

    private void stopPolling() {
        AlarmUtils.stopPollingWithBroadcast(cx,
                DevicePollingReceiver.getIntent(cx),
                getPollingTaskId());
    }

    protected long getPollingInterval(boolean isInBack) {
        return isInBack ? pollingPeriodInBack : pollingPeriodInFront;
    }


    // -------------------------------------------------------------------------------
    // ???????????? end
    // -------------------------------------------------------------------------------

    @Subscribe
    public void onEvent(ViewChangedEvent event) {
        String absDevice = event.getCurrentDevice();
        if (Plat.DEBUG)
            LogUtils.i("SteamHS906", "absDevice:" + absDevice);

        DevicePollingReceiver.setCurrentPollingDevice(absDevice);
    }

}
