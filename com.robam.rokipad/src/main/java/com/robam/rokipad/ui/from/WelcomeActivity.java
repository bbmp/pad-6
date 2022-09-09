package com.robam.rokipad.ui.from;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback2;
import com.legent.VoidCallback2;
import com.legent.plat.Plat;
import com.legent.plat.PlatApp;
import com.legent.plat.constant.IAppType;
import com.legent.plat.constant.PrefsKey;
import com.legent.plat.io.device.mqtt.MqttChannel;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ResourcesUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.device.RokiDeviceFactory;
import com.robam.common.io.device.RokiMsgMarshaller;
import com.robam.common.io.device.RokiMsgSyncDecider;
import com.robam.common.io.device.RokiNoticeReceiver;
import com.robam.common.pojos.dictionary.AppExtendDic;
import com.robam.common.services.StoreService;
import com.robam.rokipad.R;
import com.robam.rokipad.event.HomeIsCrash;
import com.robam.rokipad.io.SerialChannel;
import com.robam.rokipad.service.AppService;

import static com.legent.ContextIniter.cx;


/**
 * Created by Dell on 2018/12/6.
 */

public class WelcomeActivity extends Activity {


    @Subscribe
    public void onEvent(HomeIsCrash homeIsCrash) {
        Log.e("20181220", "homeIsCrash:" + homeIsCrash);
        if (!homeIsCrash.isCarch) {
            nextForm();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initApp(getApplication());
    }

    private void initApp(final Application application) {
        if (application == null) {
            ToastUtils.show(R.string.app_error, Toast.LENGTH_SHORT);
            return;
        }
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
                                    nextForm();
                                    LogUtils.e("20190326", "isCrash:" + !PlatApp.isCrash);
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
        // NotifyService.getInstance().init(application);

    }

    private void nextForm() {
        if (!PlatApp.isCrash) {
            boolean guided = PreferenceUtils.getBool(PrefsKey.Guided, false);
            if (!guided) {
                // 未激活帐号
                startGuideForm();
            } else {
                startMainForm();
            }

        }
    }

    private void startGuideForm() {
        startActivity(new Intent(cx, GuideActivity.class));
        this.finish();
    }

    private void startMainForm() {
        MainActivity.start(this);
    }

}
