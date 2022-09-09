package com.robam.rokipad.utils;

import android.net.wifi.ScanResult;

/**
 * Created by Dell on 2018/12/25.
 */

public interface OnItemSelectedListener {
    void onItemSelect(ScanResult sr,int position);
}
