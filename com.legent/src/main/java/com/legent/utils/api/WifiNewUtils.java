package com.legent.utils.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.legent.utils.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import static android.net.wifi.WifiManager.EXTRA_SUPPLICANT_ERROR;

/**
 * Created by Dell on 2018/12/29.
 */

public class WifiNewUtils {

    public WifiManager wifiManager;
    WifiReceiver wifiReceiver;
    Context cx;

    public WifiNewUtils(Context cx){
        this.cx = cx;
        wifiManager = (WifiManager) cx.getSystemService(Context.WIFI_SERVICE);
        IntentFilter wifiFilter = new IntentFilter();
        wifiFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        wifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerWifiReceiver(wifiFilter);
    }

    public void registerWifiReceiver(IntentFilter intentFilter) {
        if (null == wifiReceiver) {
            wifiReceiver = new WifiReceiver();
        }
        cx.registerReceiver(wifiReceiver, intentFilter);
    }


    public boolean startScan() {
        return wifiManager.startScan();
    }

    public void closeWifi(Context cx) {
        wifiManager.setWifiEnabled(false);
    }

    public interface WifiState{
        void onScanResult(List<ScanResult> list);
        void onNetWorkStateChanged(NetworkInfo.DetailedState state);
        void onWiFiStateChanged(int wifiState);
        void onWifiPasswordFault();
    }

    public WifiState wifiStateListener;

    public void setWifiStateListener(WifiState wifiStateListener) {
        this.wifiStateListener = wifiStateListener;
    }



    public String getCurrentSsid(Context cx) {
        String ssid = wifiManager.getConnectionInfo().getSSID();
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        return ssid;
    }

    class WifiReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            Log.e("20181229", "onReceive: intent action" + intent.getAction());
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                if (null != wifiStateListener) {
                    List<ScanResult> srList = getScanResult(wifiManager.getScanResults());
                    wifiStateListener.onScanResult(srList);
                }
            } else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {//wifi连接网络状态变化
                NetworkInfo.DetailedState state = ((NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).getDetailedState();
                if (null != wifiStateListener) {
                    wifiStateListener.onNetWorkStateChanged(state);
                }
            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//wifi状态变化
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                if (null != wifiStateListener) {
                    wifiStateListener.onWiFiStateChanged(wifiState);
                }
            } else if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                int error = intent.getIntExtra(EXTRA_SUPPLICANT_ERROR, 0);
                if (null != wifiStateListener) {
                    if (error == WifiManager.ERROR_AUTHENTICATING) {
                       //这里可以获取到监听连接wifi密码错误的时候进行回调
                        wifiStateListener.onWifiPasswordFault();
                    }
                }
            }
        }

        public List<ScanResult> getScanResult(List<ScanResult> sr) {
            List<ScanResult>   scanResults = Lists.newArrayList();
             List<ScanResult> scanResults_sort = Lists.newArrayList();
            List<ScanResult> scanResults_out = Lists.newArrayList();
            scanResults.clear();
            scanResults = filterScanResult(sr);
            String currentSsid = WifiUtils.getCurrentSsid(cx);

            for (ScanResult scanResult : scanResults) {
                if ("NVRAM WARNING: Err = 0x10".equals(scanResult.SSID))
                    continue;
                if (!StringUtils.isNullOrEmpty(scanResult.SSID)) {
                    if (!Objects.equal(scanResult.SSID, currentSsid)){
                        scanResults_sort.add(scanResult);
                    } else{
                        // scanResults_out.add(scanResult);
                    }
                }
                //  Log.e("WifiName","scanResult.toString():"+scanResult.toString());
            }
            Collections.sort(scanResults_sort, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult lhs, ScanResult rhs) {
                    if (WifiManager.calculateSignalLevel(lhs.level, 4) > WifiManager.calculateSignalLevel(rhs.level, 4)) {
                        return -1;
                    }
                    if (WifiManager.calculateSignalLevel(lhs.level, 4) == WifiManager.calculateSignalLevel(rhs.level, 4)) {
                        return 0;
                    }
                    return 1;
                }
            });
            scanResults_out.addAll(scanResults_sort);
            return scanResults_out;
           /* if (isRepeat) {
                TaskService.getInstance().postUiTask(new Runnable() {

                    @Override
                    public void run() {

                    }
                }, 3000);

            } else {
                stopScanning();
            }*/

        }


        public  List<ScanResult> filterScanResult(final List<ScanResult> list) {
            LinkedHashMap<String, ScanResult> linkedMap = new LinkedHashMap<String, ScanResult>(list.size());
            for (ScanResult rst : list) {
                if (linkedMap.containsKey(rst.SSID)) {
                    if (rst.level > linkedMap.get(rst.SSID).level) {
                        linkedMap.put(rst.SSID, rst);
                    }
                    continue;
                }
                linkedMap.put(rst.SSID, rst);
            }
            list.clear();
            list.addAll(linkedMap.values());
            return list;
        }



    }

}
