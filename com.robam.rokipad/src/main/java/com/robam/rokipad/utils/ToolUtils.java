package com.robam.rokipad.utils;


import android.os.Bundle;

import com.robam.rokipad.NewPadApp;


public class ToolUtils {

    public static void logEvent(String type, String action, String eventName) {
        try {
            if (type != null) {
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                bundle.putString("action", type + ":" + action);
                NewPadApp.getFireBaseAnalytics().logEvent(eventName, bundle);
            } else {
                new RuntimeException("type is null exception");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
