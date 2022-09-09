package com.robam.rokipad.ui.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.robam.common.Utils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.R;
import com.robam.rokipad.utils.IClickModeListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/3.
 * PS: 灶具智能设定.
 */
public class DeviceStoveSmartSetView extends FrameLayout {


    @InjectView(R.id.tv_fan_stove_name)
    TextView mTvFanStoveName;
    @InjectView(R.id.iv_fan_stove_linked_switch)
    ImageView mIvFanStoveLinkedSwitch;
    @InjectView(R.id.iv_fan_stove_power_switch)
    ImageView mIvFanStovePowerSwitch;
    @InjectView(R.id.iv_delayed_min_switch)
    ImageView mIvDelayedMinSwitch;
    @InjectView(R.id.tv_delayed_min)
    TextView mTvDelayedMin;
    private Context mContext;
    private IClickModeListener mClickModeListener;

    public DeviceStoveSmartSetView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceStoveSmartSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceStoveSmartSetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_device_stove_smart_set, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        AbsFan defaultFan = Utils.getDefaultFan();
        String fanDt = "";
        Stove child = null;
        if (defaultFan != null) {
            fanDt = defaultFan.getDt();
            child = defaultFan.getChild();
        }
        String stoveDt;
        if (child == null) {
            stoveDt = "";
        } else {
            stoveDt = child.getDt();
        }
        if (!stoveDt.isEmpty()) {
            mTvFanStoveName.setText(fanDt + "+" + stoveDt);
        } else {
            mTvFanStoveName.setText(fanDt);
        }
    }

    @OnClick({R.id.iv_fan_stove_linked_switch,
            R.id.iv_fan_stove_power_switch,
            R.id.tv_delayed_min,
            R.id.iv_delayed_min_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fan_stove_linked_switch:
                onCallBackListener("linked_switch");
                break;
            case R.id.iv_fan_stove_power_switch:
                onCallBackListener("power_switch");
                break;
            case R.id.tv_delayed_min:
                onCallBackListener("delayed_min");
                break;
            case R.id.iv_delayed_min_switch:
                onCallBackListener("delayed_min_switch");
                break;
        }
    }

    public void setOnClickModeListener(IClickModeListener listener) {
        mClickModeListener = listener;
    }

    private void onCallBackListener(String content) {
        if (mClickModeListener != null) {
            mClickModeListener.onClickModeListener(content);
        }
    }

    //开启灶具时烟机自动开启
    public void setPowerLinkageSwitch(boolean isPowerLinkage) {
        if (isPowerLinkage) {
            mIvFanStoveLinkedSwitch.setImageResource(R.mipmap.wifi_on);
        } else {
            mIvFanStoveLinkedSwitch.setImageResource(R.mipmap.wifi_off);
        }
    }

    //烟机灶具档位联动
    public void setLevelLinkageSwitch(boolean isLevelLinkage) {
        if (isLevelLinkage) {
            mIvFanStovePowerSwitch.setImageResource(R.mipmap.wifi_on);
        } else {
            mIvFanStovePowerSwitch.setImageResource(R.mipmap.wifi_off);
        }
    }

    //灶具关闭后烟机延时几分钟关机开关状态
    public void setShutdownDelaySwitch(boolean isShutdownLinkage) {
        if (isShutdownLinkage) {
            mIvDelayedMinSwitch.setImageResource(R.mipmap.wifi_on);
            mTvDelayedMin.setBackground(getResources().getDrawable(R.drawable.shape_item_blue_bg));
        } else {
            mIvDelayedMinSwitch.setImageResource(R.mipmap.wifi_off);
            mTvDelayedMin.setBackground(getResources().getDrawable(R.drawable.shape_item_gray_bg));
        }
    }

    //关闭灶具，烟机延时分钟数
    public void setShutdownDelay(short shutdownDelay) {
        mTvDelayedMin.setText(shutdownDelay + "");
    }


}
