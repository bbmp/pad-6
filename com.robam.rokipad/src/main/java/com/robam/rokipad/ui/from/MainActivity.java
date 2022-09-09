package com.robam.rokipad.ui.from;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.pojos.User;
import com.legent.services.TaskService;
import com.legent.ui.FormManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.BaseActivity;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiState;
import com.legent.utils.api.WifiUtils;
import com.robam.common.Utils;
import com.robam.common.events.HomeIcUpdateEvent;
import com.robam.common.events.HomeSetEvent;
import com.robam.common.events.MainActivityExitEvent;
import com.robam.common.events.MainActivityFanExitEvent;
import com.robam.common.events.MainActivityRunEvent;
import com.robam.common.events.RecipeExitEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.FormKey;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.page.HomePage;
import com.robam.rokipad.utils.AlarmDataUtils;
import com.robam.rokipad.utils.GlideCircleTransform;

import java.util.List;

import static com.legent.ContextIniter.cx;
import static com.robam.rokipad.NewPadApp.isRecipeRun;

/**
 * Created by Dell on 2018/12/6.
 */

public class MainActivity extends BaseActivity {

    RelativeLayout mIvBack;
    RelativeLayout mIvHome;
    ImageView mIvHomeImg;
    RelativeLayout mRlPortrait;
    RelativeLayout mIvSetting;
    ImageView ivPerson;
    ImageView mIvWifiShow;
    TextView loginTip;
    TextClock mTvCurrentTime;
    WifiUtils.WifiReceiver wifiReceiver;

    static public void start(Activity atv) {
        atv.startActivity(new Intent(atv, MainActivity.class));
        atv.finish();
    }

    @Override
    protected String createFormKey() {
        return FormKey.MainForm;
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        initView();
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        initView();
    }

    @Subscribe
    public void onEvent(HomeIcUpdateEvent event) {
        if (null != mIvWifiShow) {
            if ("HOME".equals(event.sing)) {
                mIvHomeImg.setImageResource(R.mipmap.ic_home_smiling_face);
//                mIvHome.setClickable(true);
                mIvHome.setTag("HOME");
            } else {
                mIvHomeImg.setImageResource(R.mipmap.ic_home);
                mIvHome.setTag("NO_HOME");
//                mIvHome.setClickable(false);
            }
        }
    }

    @Override
    protected void initOnCreate() {
        super.initOnCreate();
        EventUtils.regist(this);
        AlarmDataUtils.init(this);
        wifiReceiver = new WifiUtils.WifiReceiver(cx);
        wifiReceiver.startScaning();
        mIvBack = (RelativeLayout) findViewById(R.id.iv_back);
        mIvHome = (RelativeLayout) findViewById(R.id.iv_home);
        mIvHomeImg = (ImageView) findViewById(R.id.iv_home_img);
        mRlPortrait = (RelativeLayout) findViewById(R.id.rl_portrait);
        mIvSetting = (RelativeLayout) findViewById(R.id.iv_setting);
        ivPerson = (ImageView) findViewById(R.id.iv_person);
        mIvWifiShow = (ImageView) findViewById(R.id.wifi_show);
        loginTip = (TextView) findViewById(R.id.login_tip);
        mTvCurrentTime = (TextClock) findViewById(R.id.tv_current_time);
//        mIvHome.setClickable(true);
        mIvHomeImg.setImageResource(R.mipmap.ic_home_smiling_face);
        mIvHome.setTag("HOME");
        initView();
        initListener();
    }


    private void initListener() {
        //请登录
        mRlPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果在做菜抛出事件弹框提醒是否退出
                if (isRecipeRun) {
                    EventUtils.postEvent(new RecipeExitEvent());
                } else {
                    EventUtils.postEvent(new MainActivityExitEvent());
                    EventUtils.postEvent(new MainActivityFanExitEvent());
                    String formKey = null;
                    UIService instance = UIService.getInstance();
                    if (null != instance) {
                        FormManager top = instance.getTop();
                        if (null != top) {
                            formKey = top.getCurrentPageKey();
                        }
                    }
                    if ("HomeSet".equals(formKey)) {
                        EventUtils.postEvent(new HomeSetEvent());
                    } else {
                        UIService instance2 = UIService.getInstance();
                        if (null != instance2) {
                            FormManager top = instance2.getTop();
                            if (null != top) {
                                String formKey2 = top.getFormKey();
                                if (FormKey.MainForm.equals(formKey2)) {
                                    UIService.getInstance().postPage(PageKey.HomeSet);
                                }
                            }
                        }
                    }

                }
            }
        });

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean home = false;
                UIService instance = UIService.getInstance();
                if (null != instance) {
                    FormManager top = instance.getTop();
                    if (null != top) {
                        home = top.isHome();
                    }
                }
                if (home) {
                    ToastUtils.show(R.string.setting_top_text);
                } else {
                    //如果在做菜抛出事件弹框提醒是否退出
                    if (isRecipeRun) {
                        EventUtils.postEvent(new RecipeExitEvent());
                    } else {
                        EventUtils.postEvent(new MainActivityRunEvent());
                        String currentPageKey = UIService.getInstance().getTop().getCurrentPageKey();
                        if (PageKey.HomeSet.equals(currentPageKey)) {
                            @SuppressLint("RestrictedApi")
                            List<Fragment> fragments = getSupportFragmentManager().getFragments();
                            for (Fragment fragment : fragments) {
                                if (fragment instanceof HomePage) {
                                    ((HomePage) fragment).setInitPage();
                                }
                            }
                        }
                        UIService.getInstance().popBack();
                    }
                }
            }
        });
        mIvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean home = false;
                    //如果在做菜抛出事件弹框提醒是否退出
                    UIService uiService = UIService.getInstance();
                    if (null != uiService) {
                        FormManager top = uiService.getTop();
                        if (null != top) {
                            home = top.isHome();
                        }
                    }
                    String tag = (String) mIvHome.getTag();
                    if (isRecipeRun) {
                        EventUtils.postEvent(new RecipeExitEvent());
                    } else if (home && "HOME".equals(tag)) {
                        AbsFan fan = Utils.getDefaultFan();
                        if (null != fan) {
                            short status = fan.status;
                            if (FanStatus.CleanLock == status) {
                                ToastUtils.show(R.string.setting_clean_lock_not_in_text);
                                return;
                            }
                        }
                        MinimalismModelActivity.start(MainActivity.this);
                    } else {
                        String currentPageKey = UIService.getInstance().getTop().getCurrentPageKey();
                        EventUtils.postEvent(new MainActivityRunEvent());
                        if (PageKey.HomeSet.equals(currentPageKey)) {
                            @SuppressLint("RestrictedApi")
                            List<Fragment> fragments = getSupportFragmentManager().getFragments();
                            for (Fragment fragment : fragments) {
                                if (fragment instanceof HomePage) {
                                    ((HomePage) fragment).setInitPage();
                                }
                            }
                        }
                        UIService instance = UIService.getInstance();
                        if (null != instance) {
                            instance.returnHome();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mIvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果在做菜抛出事件弹框提醒是否退出
                if (isRecipeRun) {
                    EventUtils.postEvent(new RecipeExitEvent());
                } else {
                    EventUtils.postEvent(new MainActivityExitEvent());
                    EventUtils.postEvent(new MainActivityFanExitEvent());
                    UIService instance = UIService.getInstance();
                    if (null != instance) {
                        FormManager top = instance.getTop();
                        if (null != top) {
                            String formKey = top.getFormKey();
                            if (FormKey.MainForm.equals(formKey)) {
                                UIService.getInstance().postPage(PageKey.HomeSet);
                            }
                        }
                    }
                }
            }
        });

        wifiReceiver.setWifiStateListener(new WifiState() {
            @Override
            public void onScanResult(List<ScanResult> list) {
                if (list == null || list.size() == 0) {
                    mIvWifiShow.setImageResource(R.mipmap.ic_wifi_cloce);
                    return;
                }
                if (list != null && list.size() > 0) {
                    ScanResult sr = WifiUtils.getCurrentScanResult(cx);
                    if (sr == null) {
                        mIvWifiShow.setImageResource(R.mipmap.ic_wifi_cloce);
                        return;
                    }
                    String ssidCurrent = sr.SSID;
                    for (int i = 0; i < list.size(); i++) {
                        String ssid = list.get(i).SSID;
                        if (ssidCurrent.equals(ssid)) {
                            wifiState(list.get(i));
                        }
                    }
                }
            }

            @Override
            public void onNetWorkStateChanged(NetworkInfo.DetailedState state) {
                if (state == NetworkInfo.DetailedState.CONNECTED) {
                    TaskService.getInstance().postUiTask(new Runnable() {
                        @Override
                        public void run() {
                            ScanResult sr = WifiUtils.getCurrentScanResult(cx);
                            wifiState(sr);
                        }
                    }, 500);
                }
            }

            @Override
            public void onWiFiStateChanged(int wifiState) {
                boolean wifiConnected = WifiUtils.isWifiConnected(getApplicationContext());
                if (!wifiConnected) return;
                switch (wifiState) {
                    case 1:
                        mIvWifiShow.setImageResource(R.mipmap.wifi_img_one);
                        break;
                    case 2:
                        mIvWifiShow.setImageResource(R.mipmap.wifi_img_two);
                        break;
                    case 3:
                        mIvWifiShow.setImageResource(R.mipmap.wifi_img_three);
                        break;
                    default:
                        mIvWifiShow.setImageResource(R.mipmap.wifi_img_no);
                        break;
                }
            }

            @Override
            public void onWifiPasswordFault() {
            }
        });

    }


    private void wifiState(ScanResult sr) {
        if (sr == null) {
            mIvWifiShow.setImageResource(R.mipmap.ic_wifi_cloce);
            return;
        }
        switch (WifiManager.calculateSignalLevel(sr.level, 4)) {
            case 1:
                mIvWifiShow.setImageResource(R.mipmap.wifi_img_one);
                break;
            case 2:
                mIvWifiShow.setImageResource(R.mipmap.wifi_img_two);
                break;
            case 3:
                mIvWifiShow.setImageResource(R.mipmap.wifi_img_three);
                break;
            default:
                mIvWifiShow.setImageResource(R.mipmap.wifi_img_no);
                break;
        }
    }

    private void initView() {

//        checkWifiState();
        User user = Plat.accountService.getCurrentUser();
        if (user != null) {
            loginTip.setVisibility(View.INVISIBLE);
            Glide.with(cx).load(user.figureUrl).transform(new GlideCircleTransform(cx))
                    .error(R.mipmap.ic_not_login).into(ivPerson);
            Log.e("20190103", "user:::" + user.getAccount() + " " + user.password);
        } else {
            loginTip.setVisibility(View.VISIBLE);
            ivPerson.setImageResource(R.mipmap.ic_not_login);
        }
    }

    public void checkWifiState() {
//        if (WifiUtils.isWifiConnected(this)) {
//            WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
//            int wifi = mWifiInfo.getRssi();//获取wifi信号强度
//            if (wifi > -50 && wifi < 0) {//最强
//                Log.e(TAG, "最强");
//            } else if (wifi > -70 && wifi < -50) {//较强
//                Log.e(TAG, "较强");
//            } else if (wifi > -80 && wifi < -70) {//较弱
//                Log.e(TAG, "较弱");
//            } else if (wifi > -100 && wifi < -80) {//微弱
//                Log.e(TAG, "微弱");
//            }
//        } else {
//            //无连接
//            Log.e(TAG, "无wifi连接");
//        }
//    }


    }

}
