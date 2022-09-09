package com.robam.rokipad.ui.page;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.from.GuideActivity;
import com.robam.rokipad.ui.from.LoginActivity;
import com.robam.rokipad.ui.from.MainActivity;
import com.robam.rokipad.utils.ToolUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.legent.plat.io.cloud.CloudHelper.getVerifyCode;

/**
 * Created by Dell on 2018/12/17.
 */

public class GuideLoginPage extends BasePage {
    View view;
    @InjectView(R.id.main_back)
    ImageView mainBack;
    @InjectView(R.id.back)
    RelativeLayout back;
    @InjectView(R.id.rl_login_title)
    RelativeLayout rl_login_title;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.skip)
    TextView skip;
    @InjectView(R.id.edtPhone_login)
    EditText edtPhoneLogin;
    @InjectView(R.id.login_code)
    EditText loginCode;
    @InjectView(R.id.send_code)
    TextView sendCode;
    @InjectView(R.id.connect)
    TextView connect;
    @InjectView(R.id.login)
    LinearLayout login;
    @InjectView(R.id.warning)
    TextView warning;
    String tag;
    String phone, code;
    CountDownTimer timer;
    @InjectView(R.id.mobile)
    LinearLayout mMobile;
    @InjectView(R.id.ll_pwd_login)
    LinearLayout llPwdLogin;
    @InjectView(R.id.iv_scan)
    ImageView mIvScan;
    @InjectView(R.id.tv_scan)
    TextView mTvScan;
    @InjectView(R.id.edtPwd_login)
    EditText edtPwdLogin;
    @InjectView(R.id.eye_btn)
    ImageView eyeBtn;
    @InjectView(R.id.ll_phone_login)
    LinearLayout llPhoneLogin;
    @InjectView(R.id.tv_login_status)
    TextView tvLoginStatus;
    private boolean btnChecked = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = inflater.inflate(R.layout.frame_guide_login, container, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    private void initView() {
        FirebaseAnalytics firebaseAnalytics = NewPadApp.getFireBaseAnalytics();
        AbsFan defaultFan = Utils.getDefaultFan();
        if (defaultFan != null) {
            String dt = defaultFan.getDt();
            if (dt != null) {
                firebaseAnalytics.setCurrentScreen(getActivity(), dt + ":"
                        + cx.getString(R.string.google_screen_login_home), null);
            }
        }
        edtPhoneLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                warning.setVisibility(View.INVISIBLE);
                sendCode.setTextColor(Color.parseColor("#666666"));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendCode.setTextColor(Color.parseColor("#3468d3"));
            }
        });
        loginCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int length = s.length();
                if (0 < length) {
                    login.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_blue));
                    login.setClickable(true);
                } else {
                    if (0 == length) {
                        login.setClickable(true);
                    } else {
                        login.setClickable(false);
                    }
                    login.setBackgroundDrawable(getResources().getDrawable(R.drawable.wifi_pad_bg));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPwdLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int length = s.length();
                LogUtils.e("20200908", "length:" + length);
                if (0 < length) {
                    login.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_blue));
                    login.setClickable(true);
                } else {
                    if (0 == length) {
                        login.setClickable(true);
                    } else {
                        login.setClickable(false);
                    }
                    login.setBackgroundDrawable(getResources().getDrawable(R.drawable.wifi_pad_bg));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        try {
            if (null != mIvScan) {
                mIvScan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIService uiService = UIService.getInstance();
                        LogUtils.e("20200420", "mIvScan:uiService:" + uiService);
                        if (null != uiService) {
                            uiService.postPage(PageKey.LoginScan);
                        }
                    }
                });
            }
            if (null != mTvScan) {
                mTvScan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIService uiService = UIService.getInstance();
                        LogUtils.e("20200420", "mTvScan:uiService:" + uiService);
                        if (null != uiService) {
                            uiService.postPage(PageKey.LoginScan);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setRootBg() {
        setRootBgRes(R.mipmap.ic_center_bg);
    }

    @OnClick(R.id.send_code)
    public void sendCode() {
        try {
            String dt = Utils.getDefaultFan().getDt();
            if (null != dt) {
                ToolUtils.logEvent(dt, cx.getString(R.string.google_screen_send_auth_code), cx.getString(R.string.google_screen_name));
                phone = edtPhoneLogin.getText().toString();
//                Preconditions.checkState(!Strings.isNullOrEmpty(phone), "手机不能为空");
            }
            if (!isMobile(phone)) {
                warning.setVisibility(View.VISIBLE);
            } else {
                getCode(phone);
            }
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    @OnClick(R.id.skip)
    public void toMain() {
        if (activity instanceof LoginActivity) {
            activity.finish();
        }
        if (activity instanceof GuideActivity) {
            MainActivity.start(activity);
        }
    }

    String uuid = null;

    void getCode(final String phone) {
        this.phone = phone;

        ProgressDialogHelper.setRunning(cx, true);
        getVerifyCode(phone, new Callback<String>() {

            @Override
            public void onSuccess(String s) {
                code = s;
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showShort(getCodeDesc() + "已发送，请及时查收");
                startCountdown();
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });

    }


    @OnClick(R.id.login)
    public void onClickLogin() {
        try {
            if ("密码登录".equals(tvLoginStatus.getText().toString())) {
                String phone = edtPhoneLogin.getText().toString();
                if (!isMobile(phone)) {
                    warning.setVisibility(View.VISIBLE);
                    return;
                } else {
                    warning.setVisibility(View.GONE);
                }
                Preconditions.checkState(!Strings.isNullOrEmpty(phone), "手机号不能为空");
                String code = loginCode.getText().toString();
                Preconditions.checkState(Objects.equal(this.code, code), getCodeDesc() + "不匹配");
                onConfirm(phone, code);
            } else {
                String phone = edtPhoneLogin.getText().toString();
                String pwd = edtPwdLogin.getText().toString().replace(" ", "");

                if (!isMobile(phone)) {
                    warning.setVisibility(View.VISIBLE);
                    return;
                } else {
                    warning.setVisibility(View.GONE);
                }
                Preconditions.checkState(!Strings.isNullOrEmpty(phone), "手机号不能为空");
                Preconditions.checkState(!Strings.isNullOrEmpty(pwd), "密码不能为空");
                final String pwdMd5 = User.encryptPassword(pwd);
                pwdLogin(phone, pwdMd5);
            }


        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    private void pwdLogin(String phone, String pwd) {
        Plat.accountService.login(phone, pwd, new Callback<User>() {
            @Override
            public void onSuccess(User user) {
                ProgressDialogHelper.setRunning(cx, false);
                onLoginCompleted(user);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }

    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        String s2 = "^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$";// 验证手机号
        if (StringUtils.isNotBlank(str)) {
            p = Pattern.compile(s2);
            m = p.matcher(str);
            b = m.matches();
        }
        return b;
    }


    private void onConfirm(String phone, String code) {
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.expressLogin(phone, code, new Callback<User>() {
            @Override
            public void onSuccess(User user) {
                ProgressDialogHelper.setRunning(cx, false);
                onLoginCompleted(user);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }

    void onLoginCompleted(User user) {
        ToastUtils.showShort("用户登录成功");
        if (activity instanceof MainActivity)
            UIService.getInstance().returnHome();
        else
            MainActivity.start(activity);
    }

    void startCountdown() {
        stopCountdown();
        sendCode.setEnabled(false);
        loginCode.requestFocus();
        timer = new CountDownTimer(1000 * 60, 1000) {
            @Override
            public void onTick(final long millisUntilFinished) {

                if (sendCode != null) {
                    sendCode.post(new Runnable() {
                        @Override
                        public void run() {
                            if (GuideLoginPage.this.isAdded() && !GuideLoginPage.this.isDetached()) {
                                sendCode.setText(String.format(getCodeDesc() + "(%s)", millisUntilFinished / 1000));
                            }
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                sendCode.setEnabled(true);
                sendCode.setText("重新获取");
            }
        };
        timer.start();
    }

    void stopCountdown() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroyView() {
        LogUtils.e("20200723", "GuidLoginPage onDestroyView");
        stopCountdown();
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    String getCodeDesc() {
        return "验证码";
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        if (activity instanceof LoginActivity) {
            MainActivity.start(activity);
        } else {
            UIService.getInstance().popBack();
        }
    }

    @OnClick(R.id.eye_btn)
    public void onEyeBtnClicked() {
        if (TextUtils.isEmpty(edtPwdLogin.getText())) return;
        if (btnChecked) {
            edtPwdLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eyeBtn.setImageResource(R.mipmap.eye_close);
        } else {
            edtPwdLogin.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            eyeBtn.setImageResource(R.mipmap.eye_open);
        }
        btnChecked = !btnChecked;
    }

    @OnClick(R.id.tv_login_status)
    public void onTvLoginStatusClicked() {
        String tvLoginText = tvLoginStatus.getText().toString();
        if ("手机登录".equals(tvLoginText)) {
            tvLoginStatus.setText("密码登录");
            llPwdLogin.setVisibility(View.GONE);
            llPhoneLogin.setVisibility(View.VISIBLE);
        } else {
            tvLoginStatus.setText("手机登录");
            llPwdLogin.setVisibility(View.VISIBLE);
            llPhoneLogin.setVisibility(View.GONE);
        }
    }
}
