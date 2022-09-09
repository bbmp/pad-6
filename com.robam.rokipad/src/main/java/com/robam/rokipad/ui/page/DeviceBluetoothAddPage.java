package com.robam.rokipad.ui.page;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.Pd;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.recycler.RecyclerAdapter;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/30.
 * PS: 添加设备蓝牙页面.
 */
public class DeviceBluetoothAddPage extends BasePage {

    RecyclerAdapter<Pd> mAdapterDeviceList;
    String deviceCategory;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            deviceCategory = bundle.getString(PageArgumentKey.deviceCategory);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        fireBaseAnalytics.setCurrentScreen((Activity) cx, cx.getString(R.string.google_screen_bluetooth_add), null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = null;
        if (IDeviceType.RRQZ.equals(deviceCategory) || IDeviceType.RDCZ.equals(deviceCategory)) {
            view = inflater.inflate(R.layout.page_bluetooth_add_stove, container, false);
        } else {
            //TODO
        }
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.ll_stove_9w70,
            R.id.ll_stove_9b37,
            R.id.ll_stove_9b39,
            R.id.ll_stove_9b30c,
            R.id.ll_stove_9b515,
            R.id.ll_stove_9w851,
            R.id.ll_stove_9b39e
    })
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_stove_9w70:
                bundle.putString(PageArgumentKey.deviceType, IRokiFamily.R9W70);
                break;
            case R.id.ll_stove_9b37:
                bundle.putString(PageArgumentKey.deviceType, IRokiFamily.R9B37);
                break;
            case R.id.ll_stove_9b39:
                bundle.putString(PageArgumentKey.deviceType, IRokiFamily.R9B39);
                break;
            case R.id.ll_stove_9b30c:
                bundle.putString(PageArgumentKey.deviceType, IRokiFamily.R9B30C);
                break;
            case R.id.ll_stove_9b515:
                bundle.putString(PageArgumentKey.deviceType, IRokiFamily._9B515);
                break;
            case R.id.ll_stove_9w851:
                bundle.putString(PageArgumentKey.deviceType, IRokiFamily._9W851);
                break;
            case R.id.ll_stove_9b39e:
                bundle.putString(PageArgumentKey.deviceType, IRokiFamily._9B39E);
                break;
            default:
                break;
        }
        UIService.getInstance().postPage(PageKey.DeviceStoveAddPreview, bundle);
    }
}
