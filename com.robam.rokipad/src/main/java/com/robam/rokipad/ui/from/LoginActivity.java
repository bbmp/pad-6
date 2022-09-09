package com.robam.rokipad.ui.from;

import android.app.Activity;
import android.content.Intent;
import android.view.WindowManager;

import com.legent.ui.UIService;
import com.legent.ui.ext.BaseActivity;
import com.legent.utils.LogUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.FormKey;
import com.robam.rokipad.ui.PageKey;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/11.
 * PS: 登陆的Activity.
 */
public class LoginActivity extends BaseActivity {


    @Override
    protected void setContentView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.login_activity);
    }

    @Override
    protected String createFormKey() {
        return FormKey.LoginForm;
    }

    public static void start(Activity atv) {
        atv.startActivity(new Intent(atv, LoginActivity.class));
    }
    @Override
    protected void initOnCreate() {
        UIService.getInstance().postPage(PageKey.GuideLogin);
    }

    @Override
    protected void onDestroy() {
        LogUtils.e("20200717","LoginActivity onDestroy");
        super.onDestroy();
    }
}
