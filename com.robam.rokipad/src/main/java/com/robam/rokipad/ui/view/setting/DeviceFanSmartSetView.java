package com.robam.rokipad.ui.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.rokipad.R;
import com.robam.rokipad.utils.IClickModeListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/3.
 * PS: 油烟机智能设定.
 */
public class DeviceFanSmartSetView extends FrameLayout {


    @InjectView(R.id.tv_fan_factory_data_reset)
    TextView mTvFanFactoryDataReset;
    @InjectView(R.id.tv_aeration_day)
    TextView mTvAerationDay;
    @InjectView(R.id.tv_variable_time)
    TextView mTvVariableTime;
    @InjectView(R.id.fl_variable_time)
    FrameLayout flVariableTime;
    @InjectView(R.id.iv_fan_aeration_switch)
    ImageView mIvFanAerationSwitch;
    @InjectView(R.id.iv_variable_time_switch)
    ImageView mIvVariableTimeSwitch;
    @InjectView(R.id.tv_week_day)
    TextView mTvWeekDay;
    @InjectView(R.id.tv_time_day)
    TextView mTvTimeDay;
    @InjectView(R.id.iv_fan_breath_time_switch)
    ImageView mIvFanBreathTimeSwitch;
    @InjectView(R.id.iv_fan_oil_switch)
    ImageView mIvFanOilSwitch;
    @InjectView(R.id.iv_3d_gesture_switch)
    ImageView iv3dGestureSwitch;
    @InjectView(R.id.tv_fan_dt)
    TextView tvFanDt;
    @InjectView(R.id.rl_3d_gesture)
    RelativeLayout rl_3d_gesture;
    private Context mContext;
    private IClickModeListener mClickModeListener;
    private String mData;
    private String mVariableData;
    private boolean variableTimeSwitch;
    private boolean gestureRecognitionSwitch;
    private String fanDeviceDt;


    public DeviceFanSmartSetView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceFanSmartSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceFanSmartSetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_device_fan_smart_set, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }

    }

    @OnClick({R.id.tv_fan_factory_data_reset,
            R.id.fl_aeration_day,
            R.id.iv_fan_aeration_switch,
            R.id.fl_variable_time,
            R.id.tv_time_day,
            R.id.iv_variable_time_switch,
            R.id.tv_week_day,
            R.id.iv_fan_breath_time_switch,
            R.id.iv_3d_gesture_switch,
            R.id.iv_fan_oil_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fan_factory_data_reset:
                onCallBackListener("factory_reset");
                break;
            case R.id.fl_aeration_day:
                onCallBackListener("fl_aeration_day");
                break;
            case R.id.iv_fan_aeration_switch:
                onCallBackListener("aeration_switch");
                break;
            case R.id.iv_variable_time_switch:
                onCallBackListener("variable_time_switch");
                break;
            case R.id.tv_week_day:
                onCallBackListener("week_day");
                break;
            case R.id.tv_time_day:
                onCallBackListener("tv_time_day");
                break;
            case R.id.iv_fan_breath_time_switch:
                onCallBackListener("breath_time_switch");
                break;
            case R.id.fl_variable_time:
                onCallBackListener("fl_variable_time");
                break;
            case R.id.iv_3d_gesture_switch:
                onCallBackListener("iv_3d_gesture_switch");
                break;
            case R.id.iv_fan_oil_switch:
                onCallBackListener("clean_switch");
                break;
            default:

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

    public void setDayData(String data) {
        mData = data;
        mTvAerationDay.setText(mData);
    }

    public void setVariableTime(String data) {
        mVariableData = data;
        mTvVariableTime.setText(mVariableData);
    }

    public void setGestureRecognitionSwitch(boolean bool) {
        if (bool) {
            iv3dGestureSwitch.setImageResource(R.mipmap.wifi_on);
        } else {
            iv3dGestureSwitch.setImageResource(R.mipmap.wifi_off);
        }
    }


    //定时通风提示开关
    public void setTimingVentilationSwitch(boolean bool) {
        if (bool) {
            mIvFanAerationSwitch.setImageResource(R.mipmap.wifi_on);
        } else {
            mIvFanAerationSwitch.setImageResource(R.mipmap.wifi_off);
        }

    }

    //提示开关变频爆炒
    public void setNoticCleanSwitch(boolean bool) {
        if (bool) {
            mIvVariableTimeSwitch.setImageResource(R.mipmap.wifi_on);
        } else {
            mIvVariableTimeSwitch.setImageResource(R.mipmap.wifi_off);
        }
    }

    //定时通风天数
    public void setTimingVentilationDay(short day, boolean bool) {

        if (bool) {
            mTvAerationDay.setText(day + "");
            mTvAerationDay.setBackground(getResources().getDrawable(R.drawable.shape_item_blue_bg));
        } else {
            mTvAerationDay.setText(day + "");
            mTvAerationDay.setBackground(getResources().getDrawable(R.drawable.shape_item_gray_bg));
        }
    }

    //变频开关
    public void setVariableTimeSwitch(boolean bool) {
        if (bool) {
            mTvVariableTime.setBackground(getResources().getDrawable(R.drawable.shape_item_blue_bg));
        } else {
            mTvVariableTime.setBackground(getResources().getDrawable(R.drawable.shape_item_gray_bg));
        }
    }

    //设置通风换气周几
    public void setFanWeekDay(short weekCharacterByNumber, boolean isWeeklyVentilation) {
        String numberByCharacter = getNumberByCharacter(weekCharacterByNumber);
        if (isWeeklyVentilation) {
            mTvWeekDay.setText(numberByCharacter);
            mTvWeekDay.setBackground(getResources().getDrawable(R.drawable.shape_item_blue_bg));
        } else {
            mTvWeekDay.setText(numberByCharacter);
            mTvWeekDay.setBackground(getResources().getDrawable(R.drawable.shape_item_gray_bg));
        }
    }

    public void setWeekVentilationSwitch(boolean isWeeklyVentilation) {
        LogUtils.e("20190505", "isWeeklyVentilation + " + isWeeklyVentilation);
        if (isWeeklyVentilation) {
            mIvFanBreathTimeSwitch.setImageResource(R.mipmap.wifi_on);
            mTvWeekDay.setBackground(getResources().getDrawable(R.drawable.shape_item_blue_bg));
            mTvTimeDay.setBackground(getResources().getDrawable(R.drawable.shape_item_blue_bg));
        } else {
            mIvFanBreathTimeSwitch.setImageResource(R.mipmap.wifi_off);
            mTvWeekDay.setBackground(getResources().getDrawable(R.drawable.shape_item_gray_bg));
            mTvTimeDay.setBackground(getResources().getDrawable(R.drawable.shape_item_gray_bg));
        }
    }
    public void setOilSwitch(boolean isOilSwitch) {
        LogUtils.e("20190505", "setOilSwitch + " + isOilSwitch);
        if (isOilSwitch) {
            mIvFanOilSwitch.setImageResource(R.mipmap.wifi_on);
        } else {
            mIvFanOilSwitch.setImageResource(R.mipmap.wifi_off);
        }
    }

    private String getNumberByCharacter(short weekCharacterByNumber) {

        if (weekCharacterByNumber == 1) {
            return mContext.getString(R.string.setting_model_week_one);
        } else if (weekCharacterByNumber == 2) {
            return mContext.getString(R.string.setting_model_week_two);
        } else if (weekCharacterByNumber == 3) {
            return mContext.getString(R.string.setting_model_week_three);
        } else if (weekCharacterByNumber == 4) {
            return mContext.getString(R.string.setting_model_week_four);
        } else if (weekCharacterByNumber == 5) {
            return mContext.getString(R.string.setting_model_week_five);
        } else if (weekCharacterByNumber == 6) {
            return mContext.getString(R.string.setting_model_week_six);
        } else if (weekCharacterByNumber == 7) {
            return mContext.getString(R.string.setting_model_week_seven);
        }
        return "";
    }

    public void setWeekDay(short weeklyVentilationDate_week, short weeklyVentilationDate_hour,
                           short weeklyVentilationDate_minute) {

        String numberByCharacter = getNumberByCharacter(weeklyVentilationDate_week);
        mTvWeekDay.setText(numberByCharacter);
        String hour;
        String minute;
        if (weeklyVentilationDate_hour <= 9) {
            hour = "0" + weeklyVentilationDate_hour;
        } else {
            hour = "" + weeklyVentilationDate_hour;
        }
        if (weeklyVentilationDate_minute <= 9) {
            minute = "0" + weeklyVentilationDate_minute;
        } else {
            minute = "" + weeklyVentilationDate_minute;
        }
        mTvTimeDay.setText(hour + ":" + minute);
    }


    public void setFanDeviceDt(String fanDeviceDt) {
        if (tvFanDt != null) {
            tvFanDt.setText(fanDeviceDt);
            if (IRokiFamily._8236S.equals(fanDeviceDt)){
                rl_3d_gesture.setVisibility(GONE);
            }else {
                rl_3d_gesture.setVisibility(VISIBLE);
            }
        }
    }
}
