package com.robam.rokipad.ui.page;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.StoveAlarmEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.rokipad.R;
import com.robam.rokipad.utils.AlarmDataUtils;
import com.robam.rokipad.utils.IStove;
import com.robam.rokipad.view.StoveConnectView;
import com.robam.rokipad.view.StoveView9B30C;
import com.robam.rokipad.view.StoveView9B37;
import com.robam.rokipad.view.StoveView9B39;
import com.robam.rokipad.view.StoveView9B39E;
import com.robam.rokipad.view.StoveView9B515;
import com.robam.rokipad.view.StoveView9W70;
import com.robam.rokipad.view.StoveView9W851;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2019/1/5.
 */

public class StoveDevicePage extends BasePage {

    private static final String TAG = "StoveDevicePage";

    @InjectView(R.id.stove_container)
    FrameLayout stoveContainer;

    StoveConnectView stoveConnectView;

    IStove stoveView;
    Stove[] stove;

    @NonNull
    public static StoveDevicePage newInstance() {
        return new StoveDevicePage();
    }

    @Subscribe
    public void onEvent(StoveAlarmEvent event) {

        if (null == stove || !Objects.equal(stove[0].getID(), event.stove.getID())) {
            return;
        }
        AlarmDataUtils.onStoveAlarmEvent(event.stove, event.alarm);
    }


    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        stove[0] = (Stove) event.device;
        LogUtils.e("20190524", "stove:" + stove);
        if (null == stove || !Objects.equal(stove[0].getID(), event.device.getID())) {
            return;
        }
        if (event.isConnected) {
            stoveView.isConnected(true);
        } else {
            stoveView.isConnected(false);
        }
    }


    public void statusChangedEvent(Stove pojo) {
        if (stoveView != null) {
            stoveView.setLeftStatus(pojo);
            stoveView.setRightStatus(pojo);
            stoveView.setLockStatus(pojo);
            stoveView.isConnected(pojo.isConnected());

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewroot = inflater.inflate(R.layout.stove_device_page, container, false);
        ButterKnife.inject(this, viewroot);
        stove = Utils.getDefaultStove();
        LogUtils.e("20190524", "onCreateView stove:" + stove[0]);
        initView(stove[0]);
        return viewroot;
    }

    public void initView(Stove stove) {
        if (stove == null) {
            LogUtils.e("20190524", "if stove:");
            stoveConnectView = new StoveConnectView(cx);
            stoveContainer.removeAllViews();
            stoveContainer.addView(stoveConnectView);

        } else {
            LogUtils.e("20190524", "eles_stove:" + stove);
            if (IRokiFamily.R9W70.equals(stove.getDt())) {
                stoveView = new StoveView9W70(cx);
                stoveContainer.removeAllViews();
                stoveContainer.addView((StoveView9W70) stoveView);
            } else if (IRokiFamily.R9B37.equals(stove.getDt())) {
                stoveView = new StoveView9B37(cx);
                stoveContainer.removeAllViews();
                stoveContainer.addView((StoveView9B37) stoveView);
            } else if (IRokiFamily._9B39E.equals(stove.getDt())) {
                stoveView = new StoveView9B39E(cx);
                stoveContainer.removeAllViews();
                stoveContainer.addView((StoveView9B39E) stoveView);
            } else if (IRokiFamily.R9B30C.equals(stove.getDt())) {
                stoveView = new StoveView9B30C(cx);
                stoveContainer.removeAllViews();
                stoveContainer.addView((StoveView9B30C) stoveView);
            } else if (IRokiFamily.R9B39.equals(stove.getDt())) {
                stoveView = new StoveView9B39(cx);
                stoveContainer.removeAllViews();
                stoveContainer.addView((StoveView9B39) stoveView);
            } else if (IRokiFamily._9B515.equals(stove.getDt())) {
                stoveView = new StoveView9B515(cx);
                stoveContainer.removeAllViews();
                stoveContainer.addView((StoveView9B515) stoveView);
            }else if (IRokiFamily._9W851.equals(stove.getDt())){
                stoveView = new StoveView9W851(cx);
                stoveContainer.removeAllViews();
                stoveContainer.addView((StoveView9W851) stoveView);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

    }

}
