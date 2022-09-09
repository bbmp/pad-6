package com.robam.common.services;

import android.util.Log;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.services.AbsService;
import com.legent.utils.LogUtils;
import com.robam.common.RobamApp;
import com.robam.common.Utils;
import com.robam.common.events.FanCleanLockEvent;
import com.robam.common.events.FanCleanNoticEvent;
import com.robam.common.events.FanLevelEvent;
import com.robam.common.events.FanLightEvent;
import com.robam.common.events.FanPlateRemoveEvent;
import com.robam.common.events.FanPowerEvent;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.FanTimingCompletedEvent;
import com.robam.common.events.StoveAlarmEvent;
import com.robam.common.events.StoveLevelEvent;
import com.robam.common.events.StovePowerEvent;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.dictionary.StoveAlarm;

public class NotifyService extends AbsService {

    static final String TAG = "roki";

    static public NotifyService getInstance() {
        RobamApp app = (RobamApp) Plat.app;
        return app.getNotifyService();
    }

    //-----------------------------------------------------------------------------------

    @Subscribe
    public void onEvent(StovePowerEvent event) {
        Log.d(TAG, "电磁灶电源事件：" + event.power);
        if (event.power == true)
            onStovePowerEvent(event.stove, event.power);
    }

    @Subscribe
    public void onEvent(final StoveLevelEvent event) {
        Log.d(TAG, "电磁灶档位事件：" + event.level);
        final AbsFan defaultFan = Utils.getDefaultFan();
        if (null != defaultFan) {
            final String dt = defaultFan.getDt();
            if (IRokiFamily._8236S.equals(dt)) {
                defaultFan.getSmartConfig(new Callback<SmartParams>() {
                    @Override
                    public void onSuccess(SmartParams smartParams) {
                        boolean isPowerLinkage = smartParams.IsPowerLinkage;
                        LogUtils.e("20201023", "isPowerLinkage:" + isPowerLinkage);
                        if (IRokiFamily._8236S.equals(dt)) {
                            if (event.level > 0 && defaultFan.fanFeelStatus == 0 && isPowerLinkage) {
                                onStoveLevelEvent(event.stove, event.level);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            } else {
                if (event.level > 0) {
                    onStoveLevelEvent(event.stove, event.level);
                }
            }

        }

    }

    @Subscribe
    public void onEvent(StoveAlarmEvent event) {
        LogUtils.e(TAG, String.format("电磁灶报警事件: 报警码【%s】 报警描述【%S】", event.alarm.getID(), event.alarm.getName()));
        onStoveAlarmEvent(event.stove, event.alarm);
    }

    //-----------------------------------------------------------------------------------

    @Subscribe
    public void onEvent(FanPowerEvent event) {
        if (Plat.DEBUG)
            Log.d(TAG, "油烟机电源事件：" + event.power);
        if (Plat.DEBUG)
            Log.i("erhtrh5ry4r33wrw", "FanPowerEvent");
        LogUtils.e("20190628", "FanPowerEvent:" + event.fan.getDt());
        onFanPowerEvent(event.fan, event.power);
    }

    @Subscribe
    public void onEvent(FanLightEvent event) {
        Log.d(TAG, "油烟机灯光事件：" + event.power);
        onFanLightEvent(event.fan, event.power);
    }

    @Subscribe
    public void onEvent(FanLevelEvent event) {
        Log.d(TAG, "油烟机档位事件：" + event.level);
        onFanLevelEvent(event.fan, event.level);
    }

    @Subscribe
    public void onEvent(FanTimingCompletedEvent event) {
        Log.d(TAG, "油烟机定时完成事件");
        onFanTimingCompletedEvent(event.fan);
    }

    @Subscribe
    public void onEvent(FanCleanNoticEvent event) {
        Log.d(TAG, "油烟机清洗提醒事件");
        onFanNeedClean(event.fan);
    }


    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
//        if (Plat.DEBUG)
//            Log.e("fan_polling_rep", "event.pojo.clean:" + (event.pojo.clean));
        if (event.pojo.clean) {
            Log.d(TAG, "油烟机清洗提醒事件");
            onFanNeedClean(event.pojo);
        }
    }

    @Subscribe
    public void onEvent(FanCleanLockEvent event) {
        Log.d(TAG, "油烟机清洗锁定");
        onFanCleanLockEvent(event.fan, event.flag_CleanLock);
    }

    @Subscribe
    public void onEvent(FanPlateRemoveEvent event) {
        Log.d(TAG, "油烟机挡风板移除");
        onFanPlateRemoveEvent(event.fan, event.flag_PlateRemove);
    }

    //-----------------------------------------------------------------------------------

    protected void onStoveAlarmEvent(Stove stove, StoveAlarm alarm) {

    }

    protected void onStovePowerEvent(Stove stove, boolean power) {

    }

    protected void onStoveLevelEvent(Stove stove, short level) {
    }


    protected void onFanPowerEvent(AbsFan fan, boolean power) {

        LogUtils.e("20190903", "fan:" + 20190903);

    }

    protected void onFanLightEvent(AbsFan fan, boolean power) {
    }

    protected void onFanLevelEvent(AbsFan fan, short level) {

    }

    protected void onFanNeedClean(AbsFan fan) {
    }

    protected void onFanOilCupNeedClean(AbsFan fan) {
    }

    protected void onFanTimingCompletedEvent(AbsFan fan) {
    }

    protected void onFanCleanLockEvent(AbsFan fan, short flag) {
    }

    protected void onFanPlateRemoveEvent(AbsFan fan, short flag) {

    }

}
