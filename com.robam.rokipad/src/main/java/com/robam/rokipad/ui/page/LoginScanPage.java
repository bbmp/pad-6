package com.robam.rokipad.ui.page;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceLoadCompletedEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.PayLoad;
import com.legent.plat.pojos.User;
import com.legent.services.TaskService;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.qrcode.QrUtils;
import com.robam.common.events.LoginCompletedEvent;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.from.GuideActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Dell on 2019/1/3.
 */

public class LoginScanPage extends BasePage {
    View view;
    @InjectView(R.id.main_back)
    ImageView mainBack;
    @InjectView(R.id.back)
    RelativeLayout back;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.scan_show)
    ImageView scanShow;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics firebaseAnalytics = NewPadApp.getFireBaseAnalytics();
        firebaseAnalytics.setCurrentScreen(getActivity(),
                cx.getString(R.string.google_screen_scan_login), null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_scan_show, container, false);
        ButterKnife.inject(this, view);
        initQrCode();
        return view;
    }


    String uuid = null;

    private void initQrCode() {
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    getQrCode();
                }
            };
        }
        timer.schedule(timerTask, 0, 55000);
    }

    @Override
    public void setRootBg() {
        setRootBgRes(R.mipmap.ic_center_bg);
    }

    @OnClick(R.id.back)
    public void back() {
        UIService.getInstance().popBack();
    }

    /**
     * 获取二维码接口
     */
    private void getQrCode() {
        CloudHelper.getCode(new Callback<Reponses.GetCode>() {
            @Override
            public void onSuccess(Reponses.GetCode getCode) {
                if (getCode != null) {
                    Log.e("20190628", "Code:" + getCode.payload);
                    uuid = getCode.payload;
                    final Bitmap imgBit = QrUtils.create2DCode("UUID-LOGIN" + uuid);
                    if (scanShow != null) {
                        scanShow.setImageBitmap(imgBit);
                    }
                    getLoginStatus(uuid);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 登录接口
     *
     * @param key uuid唯一的
     */
    private void getLoginStatus(String key) {
        try {
            CloudHelper.getLoginStatus(key, new retrofit.Callback<Reponses.GetLoginStatus>() {
                @Override
                public void success(Reponses.GetLoginStatus getLoginStauts, Response response) {
                    if (null == getLoginStauts) return;
                    PayLoad payLoad = getLoginStauts.payLoad;
                    if (null != payLoad) {
                        String account = payLoad.account;
                        String password = payLoad.password;
                        toLogin(account, password);
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toLogin(String account, String pwdMd5) {
        Plat.accountService.login(account, pwdMd5, new Callback<User>() {

            @Override
            public void onSuccess(User user) {
                ProgressDialogHelper.setRunning(cx, false);
                onLoginCompleted(user);

            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show("网络较差，请检查网络状态");
            }
        });
    }

    private void onLoginCompleted(final User user) {
        stopTimer();
        ToastUtils.showShort(R.string.loginSuccess);
        GuideActivity.onGuideOver(activity, true);
//        EventUtils.postEvent(new LoginCompletedEvent());
//        EventUtils.postEvent(new DeviceLoadCompletedEvent());
//        TaskService.getInstance().postUiTask(new Runnable() {
//            @Override
//            public void run() {
//                EventUtils.postEvent(new UserLoginEvent(user));
//            }
//        }, 1000);
    }

    private void stopTimer() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask == null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTimer();
    }
}
