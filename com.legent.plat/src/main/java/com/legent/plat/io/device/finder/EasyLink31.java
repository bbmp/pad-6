package com.legent.plat.io.device.finder;

import android.content.Context;
import android.util.Log;

import com.legent.plat.Plat;
import com.legent.utils.api.WifiUtils;
import com.mxchip.ftc_service.FTC_Listener;
import com.mxchip.ftc_service.FTC_Service;

import java.net.Socket;

/**
 * Created by sylar on 15/7/9.
 */
public class EasyLink31 implements IEasyLink {


    static final String TAG = "ftc";
    private static EasyLink31 instance = new EasyLink31();

    synchronized public static EasyLink31 getInstance() {
        return instance;
    }


    Context cx;
    FTC_Service ftcService = FTC_Service.getInstence();

    private EasyLink31() {
        cx = Plat.app;
    }


    @Override
    public void start(String ssid, String wifiPwd, String userInfo) {
        WifiUtils.allowMulticast(cx);

        int ipAddr = WifiUtils.getCurrentWifiInfo(cx).getIpAddress();
        ftcService.transmitSettings(ssid, wifiPwd, userInfo, ipAddr, ftcListener);
    }

    @Override
    public void stop() {
        WifiUtils.disableMulticast();
        ftcService.stopTransmitting();
    }

    FTC_Listener ftcListener = new FTC_Listener() {
        @Override
        public void onFTCfinished(Socket s, String jsonString) {
            Log.d(TAG, jsonString);
        }

        @Override
        public void isSmallMTU(int MTU) {
            Log.d(TAG, String.format("MTU is %s", MTU));
        }
    };

}
