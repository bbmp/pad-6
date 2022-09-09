package com.robam.rokipad.ui.page.setting;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback;
import com.legent.events.DownloadCompleteEvent;
import com.legent.events.DownloadProgressEvent;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.services.AppUpdateService;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.view.setting.downloadbutton.AnimDownloadProgressButton;
import com.robam.rokipad.utils.ToolUtils;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/21.
 * PS:关于ROKI页面.
 */
public class AboutRokiPage extends BasePage {


    @InjectView(R.id.tv_current_version)
    TextView mTvCurrentVersion;                             //当前版本号
    @InjectView(R.id.tv_new_version)
    TextView mTvNewVersion;                                 //最新版本号
    AnimDownloadProgressButton mAnimDownloadProgressButton; //下载按钮
    AppVersionInfo mVersionInfo;
    @InjectView(R.id.ll_term_sheet)
    LinearLayout mLlTermSheet;
    static boolean isDown = false;
    private static Uri mUri;
    @InjectView(R.id.iv_logo)
    ImageView ivLogo;
    @InjectView(R.id.anim_btn)
    AnimDownloadProgressButton animBtn;
    @InjectView(R.id.tv_copy_name)
    TextView tvCopyName;
    @InjectView(R.id.ll_version)
    LinearLayout llVersion;
    private boolean isExit;
    private int logoClickCount;

    private String currentVersion;
    private int newVersion;
    private int localVersion;


    @Subscribe
    public void onEvent(DownloadProgressEvent event) {

        int progress = event.progress;
        LogUtils.e("20190402", "getProgress:" + mAnimDownloadProgressButton.getProgress());
        if (mAnimDownloadProgressButton.getProgress() == 100) return;//解决重复性100
        mAnimDownloadProgressButton.setProgress(progress);
        mAnimDownloadProgressButton.setState(AnimDownloadProgressButton.DOWNLOADING);
        mAnimDownloadProgressButton.setProgressText("", mAnimDownloadProgressButton.getProgress());
        if (progress == 100) {
            mAnimDownloadProgressButton.setState(AnimDownloadProgressButton.NORMAL);
            if (localVersion > newVersion || localVersion == newVersion) {
                mAnimDownloadProgressButton.setCurrentText("已是最新版本");
                mAnimDownloadProgressButton.setClickable(false);
            } else {
                mAnimDownloadProgressButton.setCurrentText("升级");
                mAnimDownloadProgressButton.setClickable(true);
            }
        }
    }

    @Subscribe
    public void onEvent(DownloadCompleteEvent event) {
        mUri = event.getUri();
        PackageUtils.installApk(cx, event.getUri());
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        fireBaseAnalytics.setCurrentScreen(getActivity(), cx.getString(R.string.google_screen_about_roki), null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_roki, container, false);
        mAnimDownloadProgressButton = (AnimDownloadProgressButton) view.findViewById(R.id.anim_btn);
        ButterKnife.inject(this, view);
        mTvCurrentVersion.setText(PackageUtils.getVersionName(cx));
        int year = Calendar.getInstance().get(Calendar.YEAR);
        tvCopyName.setText(cx.getString(R.string.setting_model_copy_name_text) + year +
                cx.getString(R.string.setting_model_copy_name_after_text));
        checkVer();
        initListener();
        return view;
    }

    private void checkVer() {
        AppUpdateService.getInstance().checkVersion(new Callback<AppVersionInfo>() {
            @Override
            public void onSuccess(AppVersionInfo info) {
                try {
                    LogUtils.e("20200331", "info:" + info);
                    mAnimDownloadProgressButton.setCurrentText(cx.getString(R.string.setting_new_version));
                    mAnimDownloadProgressButton.setClickable(false);
                    if (info == null) return;
                    mVersionInfo = info;
                    newVersion = info.code;
                    if (null != mTvCurrentVersion) {
                        CharSequence text = mTvCurrentVersion.getText();
                        if (!TextUtils.isEmpty(text)) {
                            currentVersion = text.toString();
                        }
                    }
                    PackageInfo packageInfo = cx.getApplicationContext()
                            .getPackageManager()
                            .getPackageInfo(cx.getPackageName(), 0);
                    localVersion = packageInfo.versionCode;
                    mTvNewVersion.setText(info != null ? info.name : currentVersion);
                    LogUtils.e("20200331", "newVersion:" + newVersion);
                    LogUtils.e("20200331", "localVersion:" + localVersion);
                    PreferenceUtils.setInt("newVersion", newVersion);
                    PreferenceUtils.setInt("localVersion", localVersion);
                    PreferenceUtils.setString("info.name", info.name);
                    if (localVersion > newVersion || localVersion == newVersion) {
                        llVersion.setVisibility(View.INVISIBLE);
                        mAnimDownloadProgressButton.setCurrentText(cx.getString(R.string.setting_new_version));
                        mAnimDownloadProgressButton.setClickable(false);
                    } else {
                        llVersion.setVisibility(View.VISIBLE);
                        mAnimDownloadProgressButton.setCurrentText(cx.getString(R.string.setting_updata_text));
                        mAnimDownloadProgressButton.setClickable(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                int newVersion = PreferenceUtils.getInt("newVersion", AboutRokiPage.this.newVersion);
                int localVersion = PreferenceUtils.getInt("localVersion", AboutRokiPage.this.localVersion);
                String name = PreferenceUtils.getString("info.name", null);
                LogUtils.e("20190110", "fa  newVersion:" + newVersion);
                LogUtils.e("20190110", "fa  localVersion:" + localVersion);
                if (localVersion > newVersion || localVersion == newVersion) {
                    llVersion.setVisibility(View.INVISIBLE);
                    mAnimDownloadProgressButton.setCurrentText(cx.getString(R.string.setting_new_version));
                    mAnimDownloadProgressButton.setClickable(false);
                } else {
                    llVersion.setVisibility(View.VISIBLE);
                    mAnimDownloadProgressButton.setCurrentText(cx.getString(R.string.setting_updata_text));
                    mAnimDownloadProgressButton.setClickable(true);
                }
                mTvNewVersion.setText(name);
            }
        });
    }

    private void initListener() {

        mAnimDownloadProgressButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mAnimDownloadProgressButton.setProgressBtnBackgroundColor(Color.parseColor("#0000ff"));
                } else {
                    mAnimDownloadProgressButton.setProgressBtnBackgroundColor(Color.parseColor("#00ff00"));
                }
            }
        });
        mAnimDownloadProgressButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ToolUtils.logEvent(Utils.getDefaultFan().getDt(), cx.getString(R.string.google_screen_down),
                        cx.getString(R.string.google_screen_name));
                if (mVersionInfo == null) {
                    checkVer();
                } else {
                    if (mAnimDownloadProgressButton.getState() == AnimDownloadProgressButton.DOWNLOADING) {
                        ToastUtils.showShort("下载中不可重复点击");
                        return;
                    }
                    if (isDown) {
                        PackageUtils.installApk(cx, mUri);
                        ToastUtils.showLong("系统检测到您已下载最新版本，直接安装吧");
                        return;
                    }
                    AppUpdateService.getInstance().start(cx);
                    mAnimDownloadProgressButton.setState(AnimDownloadProgressButton.DOWNLOADING);
                    isDown = true;

                }
            }
        });
    }

    private void onToDestop() {
        ToolUtils.logEvent(Utils.getDefaultFan().getDt(), cx.getString(R.string.google_screen_skip_after),
                cx.getString(R.string.google_screen_name));
        Intent mIntent = new Intent();
        ComponentName comp = new ComponentName("com.android.launcher3",
                "com.android.launcher3.Launcher");
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.VIEW");
        getContext().startActivity(mIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_logo)
    public void onIvLogoClicked() {
        if (!isExit) {
            isExit = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            logoClickCount++;
            if (logoClickCount >= 5) {
                onToDestop();
            }
        }
    }

    /**
     * 跳转到使用条款页面
     */
    @OnClick(R.id.ll_term_sheet)
    public void onLlTermSheetClicked() {
        ToolUtils.logEvent(Utils.getDefaultFan().getDt(), cx.getString(R.string.google_screen_skip_after)
                , cx.getString(R.string.google_screen_name));
        UIService.getInstance().postPage(PageKey.TermSheet);
    }


}
