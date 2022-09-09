package com.robam.rokipad.ui.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class PropertiesUtil {

    private Context mContext;
    private Class systemProperties;
    private Method setProp, getProp;
    private AssetManager assetManager;
    private Properties mProperties;
    private static PropertiesUtil propertiesUtil;

    public PropertiesUtil(Context context) {
        mContext = context;
        assetManager = mContext.getAssets();
        mProperties = new Properties();
        try {
            systemProperties = Class.forName("android.os.SystemProperties");
            setProp = systemProperties.getDeclaredMethod("set", new Class[]{String.class, String.class});
            getProp = systemProperties.getDeclaredMethod("get", new Class[]{String.class, String.class});
            setProp.setAccessible(true);
            getProp.setAccessible(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static PropertiesUtil getInstance(Context context) {
        if (propertiesUtil == null) {
            synchronized (PropertiesUtil.class) {
                if (propertiesUtil == null) {
                    propertiesUtil = new PropertiesUtil(context);
                }
            }
        }
        return propertiesUtil;
    }

    public void setSystemProp(String name, String val) {
        try {
            setProp.invoke(systemProperties, name, val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getSystemProp(String name, String def) {
        String result = "";
        try {
            result = (String) getProp.invoke(systemProperties, name, def);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String readJsonFile(Context context, int rawId) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(rawId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String info = "";
            while ((info = bufferedReader.readLine()) != null) {
                stringBuilder.append(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return stringBuilder.toString();
    }
}