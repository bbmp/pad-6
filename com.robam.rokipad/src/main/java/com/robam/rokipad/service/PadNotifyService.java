package com.robam.rokipad.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.services.ScreenPowerService;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.FanOilCupCleanEvent;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.pojos.dictionary.StoveAlarm;
import com.robam.common.services.NotifyService;
import com.robam.common.services.StoveAlarmManager;
import com.robam.common.util.ButtonUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.ui.utils.PropertiesUtil;
import com.robam.rokipad.utils.DialogUtil;

/**
 * Created by sylar on 15/6/21.
 */
public class PadNotifyService extends NotifyService {

    private static final String TAG = "PadNotifyService";


    static PadNotifyService instance = new PadNotifyService();
    private PropertiesUtil propertiesUtil;

    synchronized public static PadNotifyService getInstance() {

        Log.d(" 20190628", "PadNotifyService getInstance()");
        return instance;
    }

    private PadNotifyService() {
    }

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_SHUTDOWN);
        cx.registerReceiver(shutdownBroaddcastReceiver, mIntentFilter);
        propertiesUtil = PropertiesUtil.getInstance(cx);
    }

    @Override
    public void dispose() {
        super.dispose();
        cx.unregisterReceiver(shutdownBroaddcastReceiver);
    }

    @Override
    public void onEvent(FanStatusChangedEvent event) {
        if (event.pojo.level > 0) {
            wakeup();
            LogUtils.e(TAG, "FanStatusChangedEvent wakeup");
        }
        super.onEvent(event);
    }

    @Override
    protected void onFanNeedClean(AbsFan fan) {
        Activity atv = UIService.getInstance().getTop().getActivity();
        if (atv == null) {
            ToastUtils.showShort("油烟机需要清洗了！");
            return;
        }
    }


    @Subscribe
    public void onEvent(FanOilCupCleanEvent event) {
        Activity atv = UIService.getInstance().getTop().getActivity();
        if (atv == null) {
            ToastUtils.showShort("油烟机油杯需要清洗了！");
            return;
        }

    }


    @Override
    protected void onStovePowerEvent(Stove stove, boolean power) {
        if (power) {
            wakeup();
        } else {
        }
    }

    @Override
    protected void onStoveLevelEvent(Stove stove, short level) {
        if (level > 0)
            wakeup();
    }


    Object object = new Object();

    @Override
    protected void onFanPowerEvent(AbsFan fan, boolean power) {
        AbsFan defaultFan = Utils.getDefaultFan();
        if (defaultFan != null) {
            if (fan.getGuid().equals(defaultFan.getGuid())) {
                if (power) {
                    wakeup();
                }
            }
        }

    }

    @Override
    protected void onFanLevelEvent(AbsFan fan, short level) {
        AbsFan defaultFan = Utils.getDefaultFan();
        if (defaultFan != null) {
            if (fan.getGuid().equals(defaultFan.getGuid())) {
                if (level > 0) {
                    wakeup();
                }
            }
        }

    }

    @Override
    protected void onFanLightEvent(AbsFan fan, boolean power) {
        super.onFanLightEvent(fan, power);
        if (power) {
            wakeup();
        }
    }


    void wakeup() {
        if (ScreenPowerService.getInstance().isScreenOn()) return;
        callOemToolbox(true);

    }


    void callOemToolbox(boolean isOn) {   // comment by zhaiyuanyi :套用CVTE唤醒app
        if (ButtonUtils.isWaitLock(object, 6000))
            return;
        String action;
        //这里是为了做兼容不同rom版本. 老rom 发.SYSTEM_SLEEP 新rom 发SCREEN_OFF 广播.
        String systemProp = propertiesUtil.getSystemProp("sys.cvte.toolbox.version", "0");
        int toolboxVersion = Integer.parseInt(systemProp);
        if (toolboxVersion >= 2) {
            if (isOn) {
                action = "com.cvte.androidsystemtoolbox.action.SCREEN_ON";
            } else {
                action = "com.cvte.androidsystemtoolbox.action.SCREEN_OFF";
            }
        } else {
            action = "com.cvte.androidsystemtoolbox.action.SYSTEM_SLEEP";
        }
        Intent intent = new Intent(action);
        cx.sendBroadcast(intent);
    }

    @Override
    protected void onStoveAlarmEvent(Stove stove, StoveAlarm alarm) {
        LogUtils.e(TAG, "stove alarm:" + alarm.getName() + " alarmID:" + alarm.getID());
        short id = alarm.getID();
        if (id == StoveAlarmManager.Key_None || id < 0)
            return;

    }

    //TODO ------------------------------ 关机广播

    BroadcastReceiver shutdownBroaddcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            closeDevice();
        }

        private void closeDevice() {
            try {
                AbsFan fan = Utils.getDefaultFan();
                if (fan != null) {
                    fan.setFanStatus(FanStatus.Off, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //TODO ------------------------------ 关机广播
}
