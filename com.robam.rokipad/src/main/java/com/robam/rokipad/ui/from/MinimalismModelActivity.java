package com.robam.rokipad.ui.from;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.legent.ui.UIService;
import com.legent.ui.ext.BaseActivity;
import com.legent.utils.LogUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.FormKey;
import com.robam.rokipad.ui.PageKey;

/**
 * Created by 14807 on 2020/3/25.
 * PS:
 */

public class MinimalismModelActivity extends BaseActivity {


    @Override
    protected void setContentView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.login_activity);
    }


    @Override
    protected String createFormKey() {
        return FormKey.MinimalismForm;
    }

    public static void start(Activity atv) {
        atv.startActivity(new Intent(atv, MinimalismModelActivity.class));
    }

    @Override
    protected void initOnCreate() {
        LogUtils.e("20200401", "initOnCreate:");
        UIService.getInstance().postPage(PageKey.HomeMin,new Bundle());
    }


}
