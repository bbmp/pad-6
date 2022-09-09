package com.robam.rokipad.ui.page.setting;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.WifiChangeEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.view.WifiListShowView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/21.
 * PS: WI-FI页面.
 */
public class WifiPage extends BasePage {

    @InjectView(R.id.wifi_container)
    FrameLayout wifiContainer;
    WifiListShowView wifiListShowView;

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        fireBaseAnalytics.setCurrentScreen(getActivity(),cx.getString(R.string.google_screen_wifi),null);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    private void initView() {
        wifiListShowView = new WifiListShowView(cx);
        wifiContainer.addView(wifiListShowView);
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
//                        ToastUtils.showThrowable(t);
                    }
                });
            }
        });
    }

    @Override
    public void setRootBg() {
        setRootBgRes(R.mipmap.ic_center_bg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
