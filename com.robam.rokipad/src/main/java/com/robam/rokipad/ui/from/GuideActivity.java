package com.robam.rokipad.ui.from;

import android.app.Activity;
import android.view.WindowManager;

import com.legent.plat.constant.PrefsKey;
import com.legent.ui.AbsActivity;
import com.legent.utils.api.PreferenceUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.FormKey;

/**
 * Created by Dell on 2018/12/6.
 */

public class GuideActivity extends AbsActivity {

//    @Subscribe
////    public void onEvent(HomeIsCrash crash) {
////        if (crash.isCarch) {
////            nextForm();
////        }
////    }

    @Override
    protected void setContentView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.guid_activity);
    }

    static public void onGuideOver(Activity atv, boolean isGuided) {
        PreferenceUtils.setBool(PrefsKey.Guided, isGuided);
        MainActivity.start(atv);
    }

    @Override
    protected String createFormKey() {
        return FormKey.GuideForm;
    }

    private void nextForm() {
        boolean guided = PreferenceUtils.getBool(PrefsKey.Guided, false);
        if (guided) {
            startMainForm();
        }
    }

    private void startMainForm() {
        MainActivity.start(this);
    }

}
