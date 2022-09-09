package com.legent.utils.api;

import android.net.NetworkInfo;
import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by Dell on 2018/12/29.
 */

public interface WifiState {

    void onScanResult(List<ScanResult> list);

    void onNetWorkStateChanged(NetworkInfo.DetailedState state);

    void onWiFiStateChanged(int wifiState);

    void onWifiPasswordFault();
}
