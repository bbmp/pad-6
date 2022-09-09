package com.robam.rokipad.ui.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/1.
 */
public class CacheUtils {
    private static Map<String, Object> cacheMap = new HashMap<String, Object>();
    private static Map<String, List> cacheList = new HashMap<String, List>();

    public static void putObject(String key, Object obj) {
        cacheMap.put(key, obj);
    }

    public static Object getObject(String key) {
        return cacheMap.get(key);
    }

    public static void putList(String key, List list) {
        cacheList.put(key, list);
    }

    public static List getList(String key) {
        return cacheList.get(key);
    }

    public static void putValue(String key, int value) {
        cacheMap.put(key, value);
    }

    public static int getValue(String key) {
        if (null != cacheMap.get(key)) {
            return (Integer) cacheMap.get(key);
        }
        return 1;
    }


    public static void putStringValue(String key, String value) {
        cacheMap.put(key, value);
    }

    public static String getStringValue(String key) {
        if (null != cacheMap.get(key)) {
            return (String) cacheMap.get(key);
        }
        return null;
    }

}
