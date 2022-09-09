package com.robam.rokipad.ui.page;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DeviceStoveOnlineSwitchEvent;
import com.legent.plat.events.DeviceStoveRunEvent;
import com.legent.plat.pojos.device.SubDeviceInfo;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.DeviceDeleteBluetoothSubsetSuccessEvent;
import com.robam.common.events.DeviceStoveAddSuccessEvent;
import com.robam.common.events.HomeIcUpdateEvent;
import com.robam.common.events.MainActivityExitEvent;
import com.robam.common.events.MainActivityRunEvent;
import com.robam.common.events.StoveAlarmEvent;
import com.robam.common.events.StoveCloseEvent;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.ui.view.CustomScrollViewPager;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lixin 2018/12/6.
 */
public class HomePage extends BasePage {

    private static final String TAG = "HomePage";

    @InjectView(R.id.pager)
    CustomScrollViewPager pager;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    private List<Fragment> list = new ArrayList<Fragment>();
    private Fan5916SDevicePage mFan5916SDevicePage;
    private FanDevicePage mFanDevicePage;
    private StoveDevicePage mStoveDevicePage;
    private SteriizeDevicePage mSteriizeDevicePage;
    private OvenDevicePage mOvenDevicePage;
    private SteamDevicePage mSteamDevicePage;
    private MicrowaveDevicePage mMicrowaveDevicePage;
    private WaterDevicePage mWaterDevicePage;
    private MyPageAdapter mMyPageAdapter;
    private IRokiDialog stoveKnowAlarmDialog;
    private IRokiDialog stoveSaleAlarmDialog;
    boolean isRuning = true;

    private final int[] icon0 = {R.mipmap.ic_yanji_img_gray, R.mipmap.ic_zaoju_gray,
            R.mipmap.ic_xiaodugui_gray, R.mipmap.ic_kaoxiang_gray, R.mipmap.ic_zhengxiang_gray,
            R.mipmap.ic_wbl_gray, R.mipmap.ic_zhengxiang_one_gray};

    private final int[] icon1 = {R.mipmap.ic_yanji_blue, R.mipmap.ic_zaoju_blue,
            R.mipmap.ic_xiaodugui_blue, R.mipmap.ic_kaoxiang_blue, R.mipmap.ic_zhengxiang_blue,
            R.mipmap.ic_wbl_blue, R.mipmap.ic_zhengxiang_one_blue};

    private final int[] icon2 = {R.mipmap.ic_yanji_img_gray, R.mipmap.ic_zaoju_black,
            R.mipmap.ic_xiaodugui_black, R.mipmap.ic_kaoxiang_black, R.mipmap.ic_zhengxiang_black,
            R.mipmap.ic_wbl_black, R.mipmap.ic_zhengxiang_black};

    private String[] titles = {"油烟机 ", "灶具 ", "消毒柜 ", "电烤箱 ", "电蒸箱 ", "微波炉 ", "净水机 "};


    @Subscribe
    public void onEvent(DeviceStoveAddSuccessEvent event) {
        if (pager != null) {
            pager.setCurrentItem(0);
        }
    }

    @Subscribe
    public void onEvent(DeviceDeleteBluetoothSubsetSuccessEvent event) {

        if (mStoveDevicePage != null) {
            mStoveDevicePage.initView(null);
        }
    }

    @Subscribe
    public void onEvent(StoveAlarmEvent event) {
        if (event.stove.getStoveModel().equals(IRokiFamily._9W851)) {
            LogUtils.e(TAG, "setContentText:" + event.alarm.getName() + " alarmId:" + event.alarm.getID());

            if (event.alarm.getID() == 1) {
                ToastUtils.show(event.alarm.getName());
            } else if (event.alarm.getID() == 3 || event.alarm.getID() == 4 || event.alarm.getID() == 5 || event.alarm.getID() == 7 || event.alarm.getID() == 10) {
                if (stoveKnowAlarmDialog == null) {
                    stoveKnowAlarmDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_11);
                }
                LogUtils.e(TAG, " 灶具知道了提示框");
                stoveKnowAlarmDialog.setContentText(event.alarm.getName());
                stoveKnowAlarmDialog.setCanceledOnTouchOutside(false);
                stoveKnowAlarmDialog.setTitleText(R.string.stove_alarm);
                stoveKnowAlarmDialog.show();
                stoveKnowAlarmDialog.setOkBtn(R.string.know, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.e(TAG, "dismiss");
                        stoveKnowAlarmDialog.dismiss();
                    }
                });
            } else if (event.alarm.getID() == 0 || event.alarm.getID() == 2 || event.alarm.getID() == 6 || event.alarm.getID() == 8 || event.alarm.getID() == 9 || event.alarm.getID() == 13) {
                if (stoveSaleAlarmDialog == null) {
                    stoveSaleAlarmDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
                }
                LogUtils.e(TAG, " 灶具售后提示框");
                stoveKnowAlarmDialog.setTitleText(R.string.stove_alarm);
                stoveSaleAlarmDialog.setContentText(event.alarm.getName());
                stoveSaleAlarmDialog.setCanceledOnTouchOutside(false);
                stoveSaleAlarmDialog.show();
                stoveSaleAlarmDialog.setOkBtn(R.string.forsale_phone_hz, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stoveSaleAlarmDialog.dismiss();
                    }
                });
            }

        }
    }

    @Subscribe
    public void onEvent(DeviceStoveOnlineSwitchEvent event) {

        List<SubDeviceInfo> children = event.children;
        for (SubDeviceInfo sub : children) {
            LogUtils.e("20190522", "sub:" + sub.guid);
        }

        if (children.size() == 0 || children == null) {
            if (mStoveDevicePage != null) {
                mStoveDevicePage.initView(null);
            }
        } else {
            Stove[] stove = Utils.getDefaultStove();
            LogUtils.e("20190522", "stove:" + stove[0]);
            if (mStoveDevicePage != null) {
                mStoveDevicePage.initView(stove[0]);
            }
        }
    }

    public void onEvent(DeviceConnectionChangedEvent event) {
        if (event != null && event.device != null) {
            if (tabs != null) {
                tabs.setDeviceConnection(event.device);
            }
        }
    }

    @Subscribe
    public void onEvent(MainActivityExitEvent exitEvent) {
        isRuning = false;
    }

    @Subscribe
    public void onEvent(DeviceStoveRunEvent event) {
        isRuning = event.isRun;
    }

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        if (Utils.getDefaultStove()[0] == null || !Objects.equal(Utils.getDefaultStove()[0].getID(),
                event.pojo.getID())) {
            return;
        }
        EventUtils.postEvent(new StoveCloseEvent());
        if (mStoveDevicePage != null) {
            if (event.pojo.isConnected() && isRuning) {
                mStoveDevicePage.statusChangedEvent(event.pojo);
            }
        }
    }

//    @Subscribe
//    public void onEvent(StoveAlarmEvent event) {
//        LogUtils.e(TAG,"StoveAlarmEvent event:"+event.alarm.getName());
//        Preconditions.checkNotNull(event.alarm.getName(),"alarm is unknow");
//        ToastUtils.show(event.alarm.getName());
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewroot = inflater.inflate(R.layout.home_page, container, false);
        ButterKnife.inject(this, viewroot);
        initView();
        return viewroot;
    }


    @Override
    public void setRootBg() {
        setRootBgRes(R.mipmap.ic_center_bg);
    }

    private void initView() {
        AbsFan defaultFan = Utils.getDefaultFan();
        String dt = null;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        LogUtils.e("20200323", "widthPixels:" + widthPixels);
        LogUtils.e("20200323", "heightPixels:" + heightPixels);
        if (defaultFan != null) {
            dt = defaultFan.getDt();
        }
        if (IRokiFamily._8236S.equals(dt)) {
            mFanDevicePage = FanDevicePage.newInstance();
            list.add(mFanDevicePage);
        } else {
            mFan5916SDevicePage = Fan5916SDevicePage.newInstance();
            list.add(mFan5916SDevicePage);

        }
        mStoveDevicePage = StoveDevicePage.newInstance();
        mSteriizeDevicePage = SteriizeDevicePage.newInstance();
        mOvenDevicePage = OvenDevicePage.newInstance();
        mSteamDevicePage = SteamDevicePage.newInstance();
        mMicrowaveDevicePage = MicrowaveDevicePage.newInstance();
        mWaterDevicePage = WaterDevicePage.newInstance();

        list.add(mStoveDevicePage);
        list.add(mSteriizeDevicePage);
        list.add(mOvenDevicePage);
        list.add(mSteamDevicePage);
        list.add(mMicrowaveDevicePage);
        list.add(mWaterDevicePage);
        // list.add(new SteamOvenOneDevicePage());
        mMyPageAdapter = new MyPageAdapter(cx, getActivity().getSupportFragmentManager(), list);
        pager.setAdapter(mMyPageAdapter);
        tabs.setIconAndText(PagerSlidingTabStrip.TABICONTEXT, 1);

        // 设置Tab底部选中的指示器 Indicator的颜色
        tabs.setIndicatorColor(0xff3468d3);
        //设置Tab标题文字的颜色
        tabs.setTextColor(Color.WHITE);
        // 设置Tab标题文字的大小
        tabs.setTextSize(26);
        //设置Tab底部分割线的颜色
        tabs.setUnderlineColor(Color.TRANSPARENT);
        // 设置点击某个Tab时的背景色,设置为0时取消背景色
        tabs.setTabBackground(0);
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);

        //!!!设置未选择的图标!!!
        tabs.setNormalIconRes(icon0);
        //!!!设置已选择的图标!!!
        tabs.setLightIconRes(icon1);
        //!!!设置离线图标
        tabs.setBlackIconRes(icon2);
        //!!!设置文本!!!
        tabs.setTabTexts(titles);

        //!!!设置选中的Tab文字的颜色!!!
        tabs.setSelectedTextColor(0xff3468d3);
        //去除tab间的分割线
        tabs.setDividerColor(Color.GRAY);
        //底部横线与字体宽度一致
        tabs.setIndicatorinFollower(true);
        //与ViewPager关联，这样指示器就可以和ViewPager联动
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.e("20200724", "position:" + position);
                if (position == 1) {
                    isRuning = true;
                } else {
                    isRuning = false;
                    EventUtils.postEvent(new MainActivityExitEvent());
                }
                if (0 == position) {
                    EventUtils.postEvent(new HomeIcUpdateEvent("HOME"));
                    EventUtils.postEvent(new MainActivityRunEvent());
                } else {
                    EventUtils.postEvent(new HomeIcUpdateEvent("NO_HOME"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setInitPage() {
        if (null != pager && null != tabs) {
            tabs.invalidate();
            pager.setCurrentItem(0);
            EventUtils.postEvent(new HomeIcUpdateEvent("HOME"));
        }
    }

    class MyPageAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.ViewTabProvider {
        List<Fragment> lists = new ArrayList<>();
        FragmentManager fm;
        Context context;

        public MyPageAdapter(Context context, FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.context = context;
            this.lists = list;
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return lists.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            fm.beginTransaction().show(fragment).commitAllowingStateLoss();
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Fragment fragment = lists.get(position);
            fm.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }

        @Override
        public View getTabView(int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_tab_menu, null);
            return view;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
