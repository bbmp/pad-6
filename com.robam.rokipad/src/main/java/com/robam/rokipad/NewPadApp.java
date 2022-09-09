package com.robam.rokipad;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.multidex.MultiDex;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback2;
import com.legent.VoidCallback2;
import com.legent.plat.Plat;
import com.legent.plat.PlatApp;
import com.legent.plat.constant.IAppType;
import com.legent.plat.io.device.mqtt.MqttChannel;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ResourcesUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.RobamApp;
import com.robam.common.io.device.RokiDeviceFactory;
import com.robam.common.io.device.RokiMsgMarshaller;
import com.robam.common.io.device.RokiMsgSyncDecider;
import com.robam.common.io.device.RokiNoticeReceiver;
import com.robam.common.pojos.dictionary.AppExtendDic;
import com.robam.common.services.NotifyService;
import com.robam.common.services.StoreService;
import com.robam.rokipad.event.HomeIsCrash;
import com.robam.rokipad.io.SerialChannel;
import com.robam.rokipad.service.AppService;
import com.robam.rokipad.service.PadNotifyService;

/**
 * Created by Dell on 2018/12/6.
 */

public class NewPadApp extends RobamApp {

    public static boolean isRecipeRun = false;
    private static FirebaseAnalytics mFireBaseAnalytics;

    @Override
    public NotifyService getNotifyService() {
        return new NotifyService();
    }

    @Override
    protected void initPlat() {
        super.initPlat();
        initApp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static FirebaseAnalytics getFireBaseAnalytics() {
        return mFireBaseAnalytics;
    }

    private void initApp(final Application application) {
        if (application == null) {
            ToastUtils.show(R.string.app_error, Toast.LENGTH_SHORT);
            return;
        }
        if (mFireBaseAnalytics == null) {
            mFireBaseAnalytics = FirebaseAnalytics.getInstance(NewPadApp.this);

        }
//        GoogleAnalytics.getInstance(this).dispatchLocalHits();
        Plat.init(application, IAppType.RKPBD,
                new RokiDeviceFactory(),
                new RokiMsgMarshaller(),
                new RokiMsgSyncDecider(),
                new RokiNoticeReceiver(),
                MqttChannel.getInstance(),
                new SerialChannel(),
                new VoidCallback2() {
                    @Override
                    public void onCompleted() {
                        AppService.getInstance().init(application);
                    }
                },
                new VoidCallback2() {
                    @Override
                    public void onCompleted() {
                        AppService.getInstance().onProbeGuid(new Callback2() {
                            @Override
                            public void onCompleted(Object o) {
                                if (!PlatApp.isCrash) {
                                    EventUtils.postEvent(new HomeIsCrash(!PlatApp.isCrash));
                                }
                            }
                        });
                    }
                });
        AppExtendDic.init(application);
        StoreService.getInstance().init(application);
        //AdvertManager.getInstance().init(application);
        String uiConfig = ResourcesUtils.raw2String(com.robam.common.R.raw.ui);
        UIService.getInstance().loadConfig(uiConfig);
        PadNotifyService.getInstance().init(application);
        LogUtils.delLog();
    }


}
