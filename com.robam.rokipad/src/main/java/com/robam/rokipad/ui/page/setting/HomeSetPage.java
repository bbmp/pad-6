package com.robam.rokipad.ui.page.setting;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiUtils;
import com.robam.common.events.HomeIcUpdateEvent;
import com.robam.common.events.HomeSetEvent;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.view.BanSlidingViewPage;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/24.
 * PS: 设置模块主页面.
 */
public class HomeSetPage extends BasePage {

    @InjectView(R.id.vp_setting)
    BanSlidingViewPage mPager;
    @InjectView(R.id.iv_personal_center_line)
    ImageView mIvPersonalCenterLine;
    @InjectView(R.id.iv_personal_center_icon)
    ImageView mIvPersonalCenterIcon;
    @InjectView(R.id.tv_personal_center_name)
    TextView mTvPersonalCenterName;
    @InjectView(R.id.fl_personal_center)
    FrameLayout mFlPersonalCenter;
    @InjectView(R.id.iv_smart_line)
    ImageView mIvSmartLine;
    @InjectView(R.id.iv_smart_icon)
    ImageView mIvSmartIcon;
    @InjectView(R.id.tv_smart_name)
    TextView mTvSmartName;
    @InjectView(R.id.fl_smart)
    FrameLayout mFlSmart;
    @InjectView(R.id.iv_after_service_line)
    ImageView mIvAfterServiceLine;
    @InjectView(R.id.iv_after_service_icon)
    ImageView mIvAfterServiceIcon;
    @InjectView(R.id.tv_after_service_name)
    TextView mTvAfterServiceName;
    @InjectView(R.id.fl_after_service)
    FrameLayout mFlAfterService;
    @InjectView(R.id.iv_wifi_line)
    ImageView mIvWifiLine;
    @InjectView(R.id.iv_wifi_icon)
    ImageView mIvWifiIcon;
    @InjectView(R.id.tv_wifi_name)
    TextView mTvWifiName;
    @InjectView(R.id.fl_wifi)
    FrameLayout mFlWifi;
    @InjectView(R.id.iv_about_roki_line)
    ImageView mIvAboutRokiLine;
    @InjectView(R.id.iv_about_roki_icon)
    ImageView mIvAboutRokiIcon;
    @InjectView(R.id.tv_about_roki_name)
    TextView mTvAboutRokiName;
    @InjectView(R.id.fl_about_roki)
    FrameLayout mFlAboutRoki;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private final int PAGE_PERSONAL_CENTER_INDEX = 0;//个人中心
    private final int PAGE_SMART_SET_INDEX = 1;//智能设定
    private final int PAGE_AFTER_SERVICE_INDEX = 2;//售后服务
    private final int PAGE_WIFI_INDEX = 3;//WI-FI
    private final int PAGE_ABOUT_ROKI_INDEX = 4;//关于ROKI
    private PersonalCenterPage personalCenterPage;
    private SmartSetPage smartSetPage;
    private AfterServicePage afterServicePage;
    private WifiPage wifiPage;
    private AboutRokiPage aboutRokiPage;
    private String isNotNetwork;


    @Subscribe
    public void onEvent(HomeSetEvent event) {

        if (mPager != null) {
            mPager.setCurrentItem(PAGE_PERSONAL_CENTER_INDEX);
            statePersonalCenterSelect();
            //其他
            stateSmartSet();
            stateAfterService();
            stateWifi();
            stateAboutRoki();
            checkWork();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null) {
            isNotNetwork = arguments.getString(PageArgumentKey.IsNotNetwork);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventUtils.postEvent(new HomeIcUpdateEvent("NO_HOME"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.inject(this, view);
        initData();
        checkWork();
        return view;
    }


    private void initData() {
        if (personalCenterPage == null) {
            personalCenterPage = new PersonalCenterPage();
        }
        if (smartSetPage == null) {
            smartSetPage = new SmartSetPage();
        }

        if (afterServicePage == null) {
            afterServicePage = new AfterServicePage();
        }

        if (wifiPage == null) {
            wifiPage = new WifiPage();
        }

        if (aboutRokiPage == null) {
            aboutRokiPage = new AboutRokiPage();
        }
        mFragmentList.clear();

        mFragmentList.add(personalCenterPage);
        mFragmentList.add(smartSetPage);
        mFragmentList.add(afterServicePage);
        mFragmentList.add(wifiPage);
        mFragmentList.add(aboutRokiPage);
        SettingPageAdapter settingPageAdapter = new SettingPageAdapter(getContext(),
                getActivity().getSupportFragmentManager(), mFragmentList);
        mPager.setOffscreenPageLimit(mFragmentList.size());
        mPager.setAdapter(settingPageAdapter);
        if ("isNotNetwork".equals(isNotNetwork) && !TextUtils.isEmpty(isNotNetwork)) {
            mPager.setCurrentItem(PAGE_WIFI_INDEX);
            stateWifiSelect();
            //其他
            statePersonalCenter();
            stateSmartSet();
            stateAfterService();
            stateAboutRoki();
        } else if ("smart_set".equals(isNotNetwork) && !TextUtils.isEmpty(isNotNetwork)) {
            mPager.setCurrentItem(PAGE_SMART_SET_INDEX);
            stateSmartSetSelect();
            //其他
            statePersonalCenter();
            stateAfterService();
            stateWifi();
            stateAboutRoki();
        } else {
            mPager.setCurrentItem(PAGE_PERSONAL_CENTER_INDEX);
            statePersonalCenterSelect();
            //其他
            stateSmartSet();
            stateAfterService();
            stateWifi();
            stateAboutRoki();

        }

    }

    @OnClick(R.id.fl_personal_center)
    public void onMFlPersonalCenterClicked() {
        mPager.setCurrentItem(PAGE_PERSONAL_CENTER_INDEX);
        statePersonalCenterSelect();
        //其他
        stateSmartSet();
        stateAfterService();
        stateWifi();
        stateAboutRoki();
        checkWork();
    }

    private void checkWork() {
        boolean isConnected = WifiUtils.isWifiConnected(cx);
        if (!isConnected) {
            ToastUtils.showShort(R.string.setting_check_network);
        } else {
            if (personalCenterPage != null) {
                personalCenterPage.upDate();
            }
        }
    }

    @OnClick(R.id.fl_smart)
    public void onMFlSmartClicked() {
        mPager.setCurrentItem(PAGE_SMART_SET_INDEX);
        stateSmartSetSelect();
        //其他
        statePersonalCenter();
        stateAfterService();
        stateWifi();
        stateAboutRoki();
    }

    @OnClick(R.id.fl_after_service)
    public void onMFlAfterServiceClicked() {
        mPager.setCurrentItem(PAGE_AFTER_SERVICE_INDEX);
        stateAfterServiceSelect();
        //其他
        statePersonalCenter();
        stateSmartSet();
        stateWifi();
        stateAboutRoki();
    }

    @OnClick(R.id.fl_wifi)
    public void onMFlWifiClicked() {
        mPager.setCurrentItem(PAGE_WIFI_INDEX);
        stateWifiSelect();
        //其他
        statePersonalCenter();
        stateSmartSet();
        stateAfterService();
        stateAboutRoki();
    }

    @OnClick(R.id.fl_about_roki)
    public void onMFlAboutRokiClicked() {
        mPager.setCurrentItem(PAGE_ABOUT_ROKI_INDEX);
        stateAboutRokiSelect();
        //其他
        statePersonalCenter();
        stateSmartSet();
        stateAfterService();
        stateWifi();
    }

    //个人中心未选中状态
    private void statePersonalCenter() {
        mIvPersonalCenterLine.setVisibility(View.GONE);
        mIvPersonalCenterIcon.setImageResource(R.mipmap.ic_menu_setting_personal_center);
        mTvPersonalCenterName.setTextColor(getResources().getColor(R.color.menu_setting_model_text));
    }

    //个人中心选中状态
    private void statePersonalCenterSelect() {
        mIvPersonalCenterLine.setVisibility(View.VISIBLE);
        mIvPersonalCenterIcon.setImageResource(R.mipmap.ic_menu_setting_personal_center_select);
        mTvPersonalCenterName.setTextColor(getResources().getColor(R.color.menu_setting_model_select_text));
    }

    //智能设定未选中状态
    private void stateSmartSet() {
        mIvSmartLine.setVisibility(View.GONE);
        mIvSmartIcon.setImageResource(R.mipmap.ic_menu_setting_smart_set);
        mTvSmartName.setTextColor(getResources().getColor(R.color.menu_setting_model_text));
    }

    //智能设定选中状态
    private void stateSmartSetSelect() {
        mIvSmartLine.setVisibility(View.VISIBLE);
        mIvSmartIcon.setImageResource(R.mipmap.ic_menu_setting_smart_set_select);
        mTvSmartName.setTextColor(getResources().getColor(R.color.menu_setting_model_select_text));
    }

    //售后服务未选中状态
    private void stateAfterService() {
        mIvAfterServiceLine.setVisibility(View.GONE);
        mIvAfterServiceIcon.setImageResource(R.mipmap.ic_menu_setting_after_service);
        mTvAfterServiceName.setTextColor(getResources().getColor(R.color.menu_setting_model_text));
    }

    //售后服务选中状态
    private void stateAfterServiceSelect() {
        mIvAfterServiceLine.setVisibility(View.VISIBLE);
        mIvAfterServiceIcon.setImageResource(R.mipmap.ic_menu_setting_after_service_select);
        mTvAfterServiceName.setTextColor(getResources().getColor(R.color.menu_setting_model_select_text));
    }

    //WI-FI未选中状态
    private void stateWifi() {
        mIvWifiLine.setVisibility(View.GONE);
        mIvWifiIcon.setImageResource(R.mipmap.ic_menu_setting_wifi);
        mTvWifiName.setTextColor(getResources().getColor(R.color.menu_setting_model_text));
    }

    //WI-FI选中状态
    private void stateWifiSelect() {
        mIvWifiLine.setVisibility(View.VISIBLE);
        mIvWifiIcon.setImageResource(R.mipmap.ic_menu_setting_wifi_select);
        mTvWifiName.setTextColor(getResources().getColor(R.color.menu_setting_model_select_text));
    }

    //关于ROKI未选中状态
    private void stateAboutRoki() {
        mIvAboutRokiLine.setVisibility(View.GONE);
        mIvAboutRokiIcon.setImageResource(R.mipmap.ic_menu_setting_about_roki);
        mTvAboutRokiName.setTextColor(getResources().getColor(R.color.menu_setting_model_text));
    }

    //关于ROKI未选中状态
    private void stateAboutRokiSelect() {
        mIvAboutRokiLine.setVisibility(View.VISIBLE);
        mIvAboutRokiIcon.setImageResource(R.mipmap.ic_menu_setting_about_roki_select);
        mTvAboutRokiName.setTextColor(getResources().getColor(R.color.menu_setting_model_select_text));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    class SettingPageAdapter extends FragmentPagerAdapter {
        List<Fragment> lists = new ArrayList<>();
        Context context;
        FragmentManager fm;

        public SettingPageAdapter(Context context, FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fm = fm;
            this.context = context;
            this.lists = list;
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("20181226", "position:" + position);
            return lists.get(position);
        }

        @Override
        public int getCount() {
            return lists != null ? lists.size() : 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FragmentTransaction ft = fm.beginTransaction();
            for (int i = 0; i < getCount(); i++) {//通过遍历清除所有缓存
                final long itemId = getItemId(i);
                // 得到缓存fragment的名字
                String name = makeFragmentName(container.getId(), itemId);

                //通过fragment名字找到该对象
                Fragment fragment = fm.findFragmentByTag(name);
                if (fragment != null) {
                    //移除之前的fragment
                    ft.remove(fragment);
                }
            }
            //重新添加新的fragment:最后记得commit
            ft.add(container.getId(), getItem(position)).attach(getItem(position)).commit();
            return getItem(position);
        }

        private String makeFragmentName(int viewId, long id) {
            return "android:switcher:" + viewId + ":" + id;
        }
    }

}
