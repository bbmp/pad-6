package com.robam.common.services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.events.DownloadCompleteEvent;
import com.legent.events.PageChangedEvent;
import com.legent.plat.Plat;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.services.AbsUpdateService;
import com.legent.services.DownloadService;
import com.legent.ui.UI;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.ViewUtils;
import com.robam.common.R;

@SuppressLint("InflateParams")
public class AppUpdateService extends AbsUpdateService {

    private static AppUpdateService instance = new AppUpdateService();
    private AlertDialog dlg;

    synchronized public static AppUpdateService getInstance() {
        return instance;
    }

    Context cx;
    Resources r;
    String apkName;

    private AppUpdateService() {
        apkName = String.format("%s.apk", Plat.app.getPackageName());
        LogUtils.e("20190115","apkName:" + apkName);
    }

    @Override
    public void checkVersion(final Context cx, final CheckVersionListener listener) {
        this.cx = cx;
        r = cx.getResources();
        checkVersion(new Callback<AppVersionInfo>() {
            @Override
            public void onSuccess(AppVersionInfo verInfo) {
                PreferenceManager.getDefaultSharedPreferences(cx).edit().putString("newVersionDesc",verInfo.desc).commit();
                if (listener == null)
                    return;

                if (verInfo == null) {
                    listener.onWithoutNewest();
                } else {
                    int currentVercode = PackageUtils.getVersionCode(cx);
                    if (verInfo.code > currentVercode) {
                        if (Strings.isNullOrEmpty(verInfo.desc)){
                            listener.onWithNewest(verInfo.url);
                        } else{
                            listener.onWithNewest(verInfo.url, verInfo.desc);
                        }
                    } else {
                        listener.onWithoutNewest();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (listener != null) {
                    listener.onCheckFailure(new Exception(t));
                }
            }
        });
    }

    public void checkVersion(Callback<AppVersionInfo> callback) {
        Plat.commonService.checkAppVersion(callback);
    }


    public Runnable updatePollingTask = new Runnable() {

        @Override
        public void run() {
            AppUpdateService.getInstance().start(cx);
        }
    };



    @Override
    protected void download(String downUrl,String newVersionDes) {
        ToastUtils.showLong(R.string.update_downloading);
        LogUtils.e("20190115","downUrl:" + downUrl);
        LogUtils.e("20190115","newVersionDes:" + newVersionDes);
        String title = r.getString(R.string.update_title);
        String description = r.getString(R.string.update_description);
        try {
            DownloadService.newAppDownloadTask(cx, getClass().getSimpleName(),
                    downUrl,newVersionDes).download(apkName, title, description);

        } catch (Exception e) {
            LogUtils.i("HomeRecipeView","e:"+e.toString());
        }
    }


}
