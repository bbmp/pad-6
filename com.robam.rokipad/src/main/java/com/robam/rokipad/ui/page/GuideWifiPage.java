package com.robam.rokipad.ui.page;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
import com.legent.events.ConnectionModeChangedEvent;
import com.legent.plat.Plat;
import com.legent.plat.constant.PrefsKey;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiUtils;
import com.robam.common.Utils;
import com.robam.common.events.WifiChangeEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.util.ButtonUtils;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.from.MainActivity;
import com.robam.rokipad.ui.view.WifiListShowView;

/**
 * Created by Dell on 2018/12/8.
 */

public class GuideWifiPage extends BasePage {


    RelativeLayout guidWifiTitlebar;
    FrameLayout wifiContainer;
    WifiListShowView wifiListShowView;
    TextView guidWifiSkip;
    @SuppressLint("HandlerLeak")
    MyHandler myHandler = new MyHandler();
    boolean isSkip = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guidwifi_page, container, false);
        guidWifiTitlebar = (RelativeLayout) view.findViewById(R.id.guid_wifi_titlebar);
        wifiContainer = (FrameLayout) view.findViewById(R.id.wifi_container);
        guidWifiSkip = (TextView) view.findViewById(R.id.guid_wifi_skip);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isSkip = true;
            }
        }, 4000);

        if (wifiListShowView == null) {
            wifiListShowView = new WifiListShowView(cx);
            wifiContainer.removeAllViews();
            wifiContainer.addView(wifiListShowView);
            initView();
            initListener();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics firebaseAnalytics = NewPadApp.getFireBaseAnalytics();
        if (firebaseAnalytics != null) {
            firebaseAnalytics.setCurrentScreen(getActivity(), cx.getString(R.string.google_screen_fan_home), null);
        }
    }

    private void initView() {
        LogUtils.e("20200324", "Guide initView");
        wifiListShowView.setCallback(new WifiListShowView.OnWifiSetCallback() {
            @Override
            public void onCompleted(final String ssid, final String pwd) {
                AbsFan fan = Utils.getDefaultFan();
                if (fan == null)
                    return;
                Plat.dcSerial.setWifiParam(fan.getID(), ssid, pwd, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        ToastUtils.show("烟机网络设置成功了");
                        EventUtils.postEvent(new WifiChangeEvent(ssid, pwd));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });
            }
        });
    }


    @Subscribe
    public void onEvent(ConnectionModeChangedEvent event) {
        refresh();
    }

    private void initListener() {


        guidWifiSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (ButtonUtils.isWaitLock(guidWifiSkip, 3000)) {
                        ToastUtils.showShort("努力加载配置中请稍后...");
                        return;
                    }

                    boolean isConnected = WifiUtils.isWifiConnected(cx);
                    boolean bool = PreferenceUtils.getBool(PrefsKey.Guided, false);
                    if (isSkip) {
                        if (isConnected && !bool) {
                            UIService.getInstance().postPage(PageKey.GuideLogin);
                        } else if (bool && isConnected) {
                            MainActivity.start(getActivity());
                        } else {
                            if (!isConnected) {
                                MainActivity.start(getActivity());
                            }
                        }
                    } else {
                        ToastUtils.showShort("努力加载配置中请稍后...");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showShort("努力加载配置中请稍后...");
                }
            }
        });

    }

    private void refresh() {
        boolean isConnected = WifiUtils.isWifiConnected(cx);
        if (isConnected) {
            guidWifiSkip.setText("下一步");

        } else {
            guidWifiSkip.setText("跳过");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isSkip = false;
    }

}
