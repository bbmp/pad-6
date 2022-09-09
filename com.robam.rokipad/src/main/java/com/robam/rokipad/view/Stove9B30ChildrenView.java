package com.robam.rokipad.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.utils.EventUtils;
import com.legent.utils.TimeUtils;
import com.robam.common.Utils;
import com.robam.common.events.MainActivityExitEvent;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.rokipad.R;
import com.robam.rokipad.utils.IClickStoveSelectListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2019/2/12.
 */

public class Stove9B30ChildrenView extends FrameLayout {
    Context cx;
    View viewRoot;
    @InjectView(R.id.stove_h)
    ImageView stoveH;
    @InjectView(R.id.stove_timer)
    ImageView stoveTimer;
    @InjectView(R.id.time)
    TextView time;
    @InjectView(R.id.head_name)
    TextView headName;
    @InjectView(R.id.fire_show)
    TextView fireShow;
    @InjectView(R.id.decrease)
    ImageView decrease;
    @InjectView(R.id.add)
    ImageView add;
    @InjectView(R.id.stove_close)
    ImageView stoveClose;
    @InjectView(R.id.power_status)
    TextView powerStatus;
    @InjectView(R.id.anim_img)
    ImageView animImg;
    @InjectView(R.id.ll_stove_close)
    LinearLayout llStoveClose;
    private Stove[] stove;

    public Stove9B30ChildrenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public Stove9B30ChildrenView(Context context) {
        super(context);
        initView(context, null);
    }

    public Stove9B30ChildrenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.cx = context;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.stove_9b30_view, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);
        stove = Utils.getDefaultStove();
    }

    @Subscribe
    public void onEvent(MainActivityExitEvent exitEvent) {
//        LogUtils.e("20200723", "MainActivityExitEvent:");
        stopAnimation();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
    }

    public IClickStoveSelectListener clickStoveSelectListener;

    public void setOnClickStoveSelectListener(IClickStoveSelectListener clickStoveSelectListener) {
        this.clickStoveSelectListener = clickStoveSelectListener;
    }


    @OnClick({R.id.stove_timer, R.id.decrease, R.id.add, R.id.ll_stove_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.stove_timer:
                clickCallBack("time");
                break;
            case R.id.decrease:
                clickCallBack("decrease");
                break;
            case R.id.add:
                clickCallBack("add");
                break;
            case R.id.ll_stove_close:
                clickCallBack("power");
                break;
        }
    }

    protected void setHeadName(int name) {
        headName.setText(name);
    }


    public void setRightWorkStatus(short level) {
        if (stove[0].rightHead.time != 0) {
            time.setText(TimeUtils.sec2clock(stove[0].rightHead.time));
            time.setVisibility(VISIBLE);
            if (stove[0].isLock) {
                time.setTextColor(Color.parseColor("#666666"));
            } else {
                time.setTextColor(Color.parseColor("#3468d3"));
            }
            stoveTimer.setVisibility(INVISIBLE);
        } else {
            time.setVisibility(GONE);
            stoveTimer.setVisibility(VISIBLE);
        }
        if (stove[0].isLock) {
            stoveH.setImageResource(R.mipmap.stove_head_gray);
            animImg.setVisibility(GONE);
            stopAnimation();
            stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
            decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            add.setImageResource(R.mipmap.stove_fire_add_gray);
            fireShow.setText(String.valueOf("P" + level));
            fireShow.setTextColor(Color.parseColor("#666666"));
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.device_stove_auxiliary_heat);
            powerStatus.setTextColor(Color.parseColor("#3468d3"));
            stoveClose.setVisibility(VISIBLE);
            powerStatus.setVisibility(VISIBLE);

        } else {
            stoveH.setImageResource(R.mipmap.stove_head_blue);
            animImg.setVisibility(VISIBLE);
            startAnimation();
            stoveTimer.setImageResource(R.mipmap.stove_timer_white);
            if (1 == level) {
                decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            } else {
                decrease.setImageResource(R.mipmap.stove_fire_decrease_white);
            }
            if (5 == level) {
                add.setImageResource(R.mipmap.stove_fire_add_gray);
            } else {
                add.setImageResource(R.mipmap.stove_fire_add_white);
            }
            fireShow.setText(String.valueOf("P" + level));
            fireShow.setTextColor(Color.parseColor("#ffffff"));
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.device_stove_auxiliary_heat);
            powerStatus.setTextColor(Color.parseColor("#3468d3"));
            stoveClose.setVisibility(VISIBLE);
            powerStatus.setVisibility(VISIBLE);
        }
    }

    public void setLeftWorkStatus(short level) {
        if (stove[0].leftHead.time != 0) {
            time.setText(TimeUtils.sec2clock(stove[0].leftHead.time));
            time.setVisibility(VISIBLE);
            if (stove[0].isLock) {
                time.setTextColor(Color.parseColor("#666666"));
            } else {
                time.setTextColor(Color.parseColor("#3468d3"));
            }
            stoveTimer.setVisibility(INVISIBLE);
        } else {
            time.setVisibility(GONE);
            stoveTimer.setVisibility(VISIBLE);
        }

        if (stove[0].isLock) {
            stoveH.setImageResource(R.mipmap.stove_head_gray);
            animImg.setVisibility(GONE);
            stopAnimation();
            stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
            decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            add.setImageResource(R.mipmap.stove_fire_add_gray);
            fireShow.setText(String.valueOf("P" + level));
            fireShow.setTextColor(Color.parseColor("#666666"));
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.device_stove_auxiliary_heat);
            powerStatus.setTextColor(Color.parseColor("#3468d3"));
            stoveClose.setVisibility(VISIBLE);
            powerStatus.setVisibility(VISIBLE);

        } else {
            stoveH.setImageResource(R.mipmap.stove_head_blue);
            animImg.setVisibility(VISIBLE);
            startAnimation();
            stoveTimer.setImageResource(R.mipmap.stove_timer_white);
            if (1 == level) {
                decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            } else {
                decrease.setImageResource(R.mipmap.stove_fire_decrease_white);
            }
            if (5 == level) {
                add.setImageResource(R.mipmap.stove_fire_add_gray);
            } else {
                add.setImageResource(R.mipmap.stove_fire_add_white);
            }
            fireShow.setText(String.valueOf("P" + level));
            fireShow.setTextColor(Color.parseColor("#ffffff"));
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.device_stove_auxiliary_heat);
            powerStatus.setTextColor(Color.parseColor("#3468d3"));
            stoveClose.setVisibility(VISIBLE);
            powerStatus.setVisibility(VISIBLE);
        }
    }


    protected void disConnected() {
        stopAnimation();
        animImg.setVisibility(GONE);
        stoveTimer.setVisibility(VISIBLE);
        time.setVisibility(INVISIBLE);
        stoveH.setImageResource(R.mipmap.stove_head_gray);
        stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
        decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
        add.setImageResource(R.mipmap.stove_fire_add_gray);
        stoveClose.setImageResource(R.mipmap.stove_power_gray);
        fireShow.setText("——");
        fireShow.setTextColor(Color.parseColor("#666666"));
        powerStatus.setText(R.string.stove_close);
        powerStatus.setTextColor(Color.parseColor("#666666"));
        stoveClose.setVisibility(GONE);
        powerStatus.setVisibility(GONE);
    }

    private void clickCallBack(String tag) {
        if (clickStoveSelectListener != null) {
            clickStoveSelectListener.clickStoveSelectListener(tag);
        }
    }


    Animation rotate;

    void startAnimation() {
        if (rotate == null) {
            rotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            rotate.setInterpolator(lin);
            animImg.startAnimation(rotate);
        }
    }


    void stopAnimation() {
        if (rotate != null) {
            rotate.cancel();
            rotate = null;
            animImg.clearAnimation();
        }
    }


    protected void switchPowerStatus(boolean powerFlag) {
        stopAnimation();
        animImg.setVisibility(GONE);
        if (powerFlag) {
            stoveH.setImageResource(R.mipmap.stove_head_blue);
            stoveTimer.setImageResource(R.mipmap.stove_timer_white);
            decrease.setImageResource(R.mipmap.stove_fire_decrease_white);
            add.setImageResource(R.mipmap.stove_fire_add_white);
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.stove_power_on);
            fireShow.setText("——");
            fireShow.setTextColor(Color.parseColor("#ffffff"));
            powerStatus.setTextColor(Color.parseColor("#3468d3"));
        } else {
            stoveH.setImageResource(R.mipmap.stove_head_gray);
            stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
            decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            add.setImageResource(R.mipmap.stove_fire_add_gray);
            stoveClose.setImageResource(R.mipmap.stove_power_white);
            fireShow.setText("——");
            fireShow.setTextColor(Color.parseColor("#666666"));
            powerStatus.setText(R.string.stove_close);
            powerStatus.setTextColor(Color.parseColor("#ffffff"));
            stoveClose.setVisibility(GONE);
            powerStatus.setVisibility(GONE);
        }
    }

    public void setDevice(Stove stove) {
        this.stove[0] = stove;
    }
}
