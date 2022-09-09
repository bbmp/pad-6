package com.legent.plat;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;
import com.google.common.collect.Lists;
import com.legent.IDispose;
import com.legent.LogTags;
import com.legent.plat.constant.PrefsKey;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.PreferenceUtils;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

abstract public class PlatApp extends Application implements IDispose {

    protected final static String TAG = LogTags.TAG_APP;
    private static PlatApp instance;
    public static boolean isCrash = false;//APP是否不停崩溃标志位

    public static PlatApp getInstance() {
        return instance;
    }

    protected List<Activity> activities = Lists.newArrayList();


    // -------------------------------------------------------------------------------
    // App Start
    // -------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            PackageInfo pack = PackageUtils.getPackInfo(this);
            if ("com.robam.rokipad".equals(pack.packageName)) {
                PreferenceUtils.init(this);
                if (!checkCrash()) {
                    isCrash = true;
                    return;
                }
            }
        } catch (Exception e) {
        }
        instance = this;
        init();
    }

    @Override
    public void dispose() {
        clearActivityList();
    }

    public void exit() {
        dispose();
        System.exit(0);
    }

    protected void init() {

    }

    // -------------------------------------------------------------------------------
    // Activity 管理
    // -------------------------------------------------------------------------------

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    protected void clearActivityList() {
        if (activities.size() > 0) {
            for (Activity atv : activities) {
                atv.finish();
            }
        }
        activities.clear();
    }

    private PatchManager patchManager;

    private void initPatchManager() throws Exception {
        int currentCode = PackageUtils.getAppVersionCode(this);
        //获取最后一次更新的补丁版本号
        String beforeCode = PreferenceUtils.getString(PrefsKey.AndFix_Target_Code, "");
        patchManager = new PatchManager(this);
        patchManager.init(currentCode + "");//current version
        if (!StringUtils.isNullOrEmpty(beforeCode) && Integer.parseInt(beforeCode) != currentCode) {
            patchManager.removeAllPatch();
        }
        patchManager.loadPatch();
        //patchManager.removeAllPatch();
        //addPath();
    }

    /**
     * @param path 补丁文件路径
     */
    public void addPath(String path) {
        //寻找自己目录下的补丁文件，具体项目根据网络请求做相应的处理
        try {
            patchManager.addPatch(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 防止奔溃措施
     */
    private boolean checkCrash() throws Exception {
        final String date = TimeUtils.getCurrentTimeInString(TimeUtils.SDF_DATE);
        if (StringUtils.isNullOrEmpty(date))
            return true;
        int crashNUm = PreferenceUtils.getInt(PrefsKey.Application_CrashNum, 0);
        int beforePolling = PreferenceUtils.getInt(PrefsKey.Application_PollingWrite, 0);
        Log.i("20171012", "crashNUm:" + crashNUm + " beforePolling:" + beforePolling);
        if (crashNUm > 10) {
            PreferenceUtils.remove(PrefsKey.Application_CrashNum);
            PreferenceUtils.remove(PrefsKey.Application_PollingWrite);
            return false;
        } else if (beforePolling > 10) {
            PreferenceUtils.remove(PrefsKey.Application_CrashNum);
            PreferenceUtils.remove(PrefsKey.Application_PollingWrite);
            return true;
        } else if (beforePolling <= 10) {
            PreferenceUtils.setInt(PrefsKey.Application_CrashNum, ++crashNUm);
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.i("20171012", "write before：" + wNode);
                    PreferenceUtils.setInt(PrefsKey.Application_PollingWrite, ++wNode);
                    Log.i("20171012", "write after：" + wNode);
                    if (wNode > 10 && timer != null) {
                        timer.cancel();
                        timer.purge();
                        timer = null;
                    }
                } catch (Exception e) {
                    if (e != null)
                        Log.i("20171012", e.getMessage());
                }
            }
        }, 0, 1000);
        return true;
    }
    //此变量 pad使用
    Timer timer;
    int wNode;
}
