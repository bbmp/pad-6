package com.legent.plat.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.legent.Callback;
import com.legent.VoidCallback2;
import com.legent.plat.Plat;
import com.legent.plat.PlatApp;
import com.legent.plat.constant.PrefsKey;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.PreferenceUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by as on 2017-08-02.
 */

public class PatchUtils2 {
    class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                atTimeAndFix();
            } catch (Exception e) {
            }
        }
    }

    public static void setTimeInRepeat(AlarmManager am, Context cx) {
        if (isStartFix)
            return;
        isStartFix = true;
        Calendar mCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mCalendar.set(Calendar.HOUR_OF_DAY, 10);
        mCalendar.set(Calendar.MINUTE, 30);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        long selectTime = mCalendar.getTimeInMillis();
        if (System.currentTimeMillis() > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Intent intent = new Intent(cx, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.class.getName());
        PendingIntent pi = PendingIntent.getBroadcast(cx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
    }


    private static boolean isStartFix;

    public static void atTimeAndFix() throws Exception {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
        int min = c.get(Calendar.MINUTE); // 获取当前分钟
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (Plat.DEBUG)
            LogUtils.out("分钟：" + min);
        if (min == 30 && hour == 10) {
            Random random = new Random();
            int num = random.nextInt(40);
            if (Plat.DEBUG)
                LogUtils.out("秒后开始 检测：：" + (num * 3 + 75) * 1000);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        PatchUtils2.checkPatchAndDown();
                    } catch (Exception e) {
                        if (e != null)
                            LogUtils.out("发生错误：：" + e.getMessage());
                        LogUtils.out("发生错误——————");
                    }
                }
            }, num * 1000);
        }
    }

    public static void checkPatchAndDown() throws Exception {
        CloudHelper.checkAppVersion("FXPAD", new Callback<AppVersionInfo>() {
            @Override
            public void onSuccess(final AppVersionInfo appVersionInfo) {
                try {
                    if (appVersionInfo == null)
                        return;
                    LogUtils.out(appVersionInfo.toString());
//                    {"url":"http://roki-test.oss-cn-qingdao.aliyuncs.com/ota/app/FXPAD_65456_20190909.apk","desc":"undefined","name":"测试","code":20190909}
                    final String[] params = getUrlParam(appVersionInfo.url);
                    LogUtils.out(Arrays.toString(params));
                    if (StringUtils.isNullOrEmpty(params[0]) ||
                            StringUtils.isNullOrEmpty(params[1]) ||
                            StringUtils.isNullOrEmpty(appVersionInfo.name))
                        return;
                    if (Plat.DEBUG)
                        LogUtils.out("名称：" + appVersionInfo.name);
                    String patchCode = params[0];
                    String targetCode = params[1];
                    String patchname = appVersionInfo.name;
                    if ("test".equalsIgnoreCase(patchname))
                        return;
                    int currentCode = PackageUtils.getVersionCode(Plat.app);
                    if (Plat.DEBUG)
                        LogUtils.out(currentCode + " " + targetCode);
                    if (Integer.parseInt(targetCode) != currentCode)
                        return;
                    if (Plat.DEBUG)
                        LogUtils.out("" + currentCode + "");
                    String sTargetCode = PreferenceUtils.getString(PrefsKey.AndFix_Target_Code, "");
                    if (!StringUtils.isNullOrEmpty(sTargetCode)) {
                        String sPatchCode = PreferenceUtils.getString(PrefsKey.AndFix_Patch_Code, "");
                        if (!StringUtils.isNullOrEmpty(sPatchCode)) {
                            String sPatchName = PreferenceUtils.getString(PrefsKey.AndFix_Name, "");
                            if (!StringUtils.isNullOrEmpty(sPatchName)) {
                                if (targetCode.equals(sTargetCode) &&
                                        patchCode.equals(sPatchCode) &&
                                        patchname.equals(sPatchName)) {
                                    return;
                                }
                            }
                        }
                    }
                    if (Plat.DEBUG)
                        LogUtils.out("开始下载：" + appVersionInfo.url);
                    setDownpath(appVersionInfo.url, new VoidCallback2() {
                        @Override
                        public void onCompleted() {
                            PreferenceUtils.setString(PrefsKey.AndFix_Name, appVersionInfo.name);
                            PreferenceUtils.setString(PrefsKey.AndFix_Target_Code, params[1]);
                            PreferenceUtils.setString(PrefsKey.AndFix_Patch_Code, params[0]);
                            if (Plat.DEBUG)
                                LogUtils.out("下载完成");
                        }
                    });
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null)
                    LogUtils.out("t+" + t.toString());
                else
                    LogUtils.out("t 失败");
            }
        });
    }

    public static String[] getUrlParam(String url) throws Exception {
        //String string = "http://roki-test.oss-cn-qingdao.aliyuncs.com/ota/app/FXPAD_65456_20190909.apk";
        String[] strings1 = url.split("/");
        String strings2 = strings1[strings1.length - 1];
        String[] strings3 = strings2.split("\\.");
        String[] strings4 = strings3[0].split("_");
        return new String[]{strings4[strings4.length - 1], strings4[strings4.length - 2]};
    }

    public static void setDownpath(String url, final VoidCallback2 callback) throws Exception {
        final String[] downpath = new String[1];
        if (StringUtils.isNullOrEmpty(url))
            return;
        String[] args = url.split("/");
        final DownUtil downUtil = new DownUtil(url, args[args.length - 1], 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    downpath[0] = downUtil.download();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                double per = downUtil.getCompleteRate();
                if (Plat.DEBUG)
                    LogUtils.i("patchpro", per + "");
                if (per >= 1) {
                    if (!StringUtils.isNullOrEmpty(downpath[0]))
                        PlatApp.getInstance().addPath(downpath[0]);
                    if (Plat.DEBUG)
                        LogUtils.i("patchpro", "加载完成！");
                    File patch = new File(downpath[0]);
                    if (patch.exists())
                        patch.delete();
                    callback.onCompleted();
                }
            }
        }, 1000);
    }
}
