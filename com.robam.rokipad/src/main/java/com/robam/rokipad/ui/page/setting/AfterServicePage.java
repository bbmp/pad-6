package com.robam.rokipad.ui.page.setting;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.ui.ext.BasePage;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/21.
 * PS: 售后服务页面.
 */
public class AfterServicePage extends BasePage {


    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        fireBaseAnalytics.setCurrentScreen(getActivity(), cx.getString(R.string.google_screen_after_service), null);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_after_service, container, false);
        return view;
    }
}
