package com.robam.rokipad.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GuideHomePage extends BasePage {


    AbsFan fan = null;
    @InjectView(R.id.btn_next)
    TextView btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_guide_home, container,
                false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.btn_next)
    public void onClick() {
        UIService.getInstance().postPage(PageKey.GuideWifi);
    }


}
