package com.legent.plat.services;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.Callback2;
import com.legent.plat.Plat;
import com.legent.plat.events.AppGuidGettedEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.services.CrashLogService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ApiUtils;
import com.legent.utils.api.AppUtils;
import com.legent.utils.api.PreferenceUtils;

public class CommonService extends AbsCommonCloudService {

    final static String APP_GUID = "AppGuid";
    final static int LOG_CRASH = 0;
    private static CommonService instance = new CommonService();

    synchronized public static CommonService getInstance() {
        return instance;
    }

    private CommonService() {

    }

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);
        CrashLogService.getInstance().setOnCrashedListener(crashedListener);
    }


    // -------------------------------------------------------------------------------
    // onEvent
    // -------------------------------------------------------------------------------

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        bindAppGuidAndUser(Plat.appGuid, event.pojo.id, null);
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        unbindAppGuidAndUser(Plat.appGuid, event.pojo.id, null);
    }


    // -------------------------------------------------------------------------------
    // public
    // -------------------------------------------------------------------------------

    public void getAppGuid(Context context,final Callback2<String> callback) {

        String guid = getAppId();
        Log.e("20190712","guid:" + guid);
        if (!Strings.isNullOrEmpty(guid)) {
            callback.onCompleted(guid);
            return;
        }

        String token = ApiUtils.getClientId(Plat.app);
        Log.e("20190712","token:" + token);
        String versionName ="2.7.2-test";
        try {
             versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        getAppGuid(Plat.appType, token,versionName, new Callback<String>() {

            @Override
            public void onSuccess(String guid) {
                setAppId(guid);
                callback.onCompleted(guid);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onCompleted(DeviceGuid.ZeroGuid);
            }
        });
    }

    public void checkAppVersion(Callback<AppVersionInfo> callback) {
        super.checkAppVersion(Plat.appType, callback);
    }

    // -------------------------------------------------------------------------------
    // override
    // -------------------------------------------------------------------------------

    @Override
    protected void onConnected(boolean isWifi) {
        //当联网时，若appId无效，重新获取
        if (!Plat.isValidAppGuid()) {
            getAppGuid(cx,new Callback2<String>() {
                @Override
                public void onCompleted(String appGuid) {
                    Plat.appGuid = appGuid;
                    //若获取的appId有效, 发布事件
                    if (Plat.isValidAppGuid()) {
                        postEvent(new AppGuidGettedEvent(Plat.appGuid));
                        if (Plat.accountService.isLogon()) {
                            bindAppGuidAndUser(Plat.appGuid, Plat.accountService.getCurrentUserId(), null);
                        }
                    }
                }
            });
        }
    }


    // -------------------------------------------------------------------------------
    // other
    // -------------------------------------------------------------------------------

    private CrashLogService.OnCrashedListener crashedListener = new CrashLogService.OnCrashedListener() {

        @Override
        public void onCrashed(String log) {
            if (!AppUtils.isDebug(cx)) {
                reportLog(Plat.appGuid, LOG_CRASH, log, null);
            }
            if (commonCrashListener != null)
                commonCrashListener.onCrashed();
        }
    };

    public void setCommonCrashListener(OnCommonCrashListener commonCrashListener) {
        this.commonCrashListener = commonCrashListener;
    }

    private OnCommonCrashListener commonCrashListener;

    public interface OnCommonCrashListener {
        void onCrashed();
    }

    private void setAppId(String appId) {
        if (Strings.isNullOrEmpty(appId))
            PreferenceUtils.remove(APP_GUID);
        else
            PreferenceUtils.setString(APP_GUID, appId);
    }

    private String getAppId() {
        return PreferenceUtils.getString(APP_GUID, null);
    }


}
