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
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.robam.common.Utils;
import com.robam.common.events.MainActivityExitEvent;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.rokipad.R;
import com.robam.rokipad.service.ConvenientCookService;
import com.robam.rokipad.utils.IClickStoveSelectListener;
import com.robam.rokipad.utils.ILongClickStoveSelectListener;
import com.robam.rokipad.utils.IStoveTag;
import com.robam.rokipad.utils.SpannableStringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by Dell on 2019/2/12.
 */

public class Stove9W851ChildrenView extends FrameLayout {
    private static final String TAG = "Stove9W851ChildrenView";
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
    private Stove[] stove;
    @InjectView(R.id.work_time)
    TextView workTime;
    private short stoveHeadihId;
    private ConvenientCookService convenientCookService = ConvenientCookService.getInstance();


    public Stove9W851ChildrenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public Stove9W851ChildrenView(Context context) {
        super(context);
        initView(context, null);
    }

    public Stove9W851ChildrenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.cx = context;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.stove_9w851_view, this, true);
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
    public ILongClickStoveSelectListener longClickStoveSelectListener;

    public void setLongClickStoveSelectListener(ILongClickStoveSelectListener longClickStoveSelectListener) {
        this.longClickStoveSelectListener = longClickStoveSelectListener;
    }


    public void setOnClickStoveSelectListener(IClickStoveSelectListener clickStoveSelectListener) {
        this.clickStoveSelectListener = clickStoveSelectListener;
    }

    @OnClick({R.id.stove_timer, R.id.decrease, R.id.add, R.id.ll_stove_close, R.id.time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.stove_timer:
                clickCallBack(IStoveTag.time);
                break;
            case R.id.time:
                clickCallBack(IStoveTag.time_cancel);
                break;
            case R.id.decrease:
                clickCallBack(IStoveTag.decrease);
                break;
            case R.id.add:
                clickCallBack(IStoveTag.add);
                break;
            case R.id.ll_stove_close:
                clickCallBack(IStoveTag.power);
                break;
        }
    }

    @OnLongClick({R.id.add, R.id.decrease})
    public boolean onViewLongClicked(View view) {
        switch (view.getId()) {
            case R.id.decrease:
                LogUtils.e(TAG, "long click decrease min level");
                longClickCallBack(IStoveTag.STOVE_MIN_LEVEL);
                break;
            case R.id.add:
                LogUtils.e(TAG, "long click add max level");
                longClickCallBack(IStoveTag.STOVE_MAX_LEVEL);
                break;
        }
        return true;
    }

    protected void setHeadName(int name) {
        headName.setText(name);
    }

    protected void switchPowerStatus(boolean powerFlag) {
        stopAnimation();
//        LogUtils.e("20200723", "stopAnimation 1");
        animImg.setVisibility(GONE);
        if (powerFlag) {
            stoveH.setImageResource(R.mipmap.stove_head_blue);
            stoveTimer.setImageResource(R.mipmap.stove_timer_white);
            decrease.setImageResource(R.mipmap.stove_fire_decrease_white);
            add.setImageResource(R.mipmap.stove_fire_add_white);
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.stove_power_on);
            fireShow.setText("——");
            workTime.setVisibility(GONE);
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
            workTime.setVisibility(GONE);
            powerStatus.setTextColor(Color.parseColor("#ffffff"));
        }
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
//            LogUtils.e("20200723", "stopAnimation 2");
            stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
            decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            add.setImageResource(R.mipmap.stove_fire_add_gray);

        } else {
            stoveH.setImageResource(R.mipmap.stove_head_blue);
            animImg.setVisibility(VISIBLE);
            startAnimation();
//            LogUtils.e("20200723", "startAnimation 3");
            stoveTimer.setImageResource(R.mipmap.stove_timer_white);
            if (1 == level) {
                decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            } else {
                decrease.setImageResource(R.mipmap.stove_fire_decrease_white);
            }
            if (10 == level) {
                add.setImageResource(R.mipmap.stove_fire_add_gray);
            } else {
                add.setImageResource(R.mipmap.stove_fire_add_white);
            }
        }
        fireShow.setText(SpannableStringUtils.createSpannableString(level));
        fireShow.setTextColor(Color.parseColor("#FFFFFF"));
        stoveClose.setImageResource(R.mipmap.stove_power_blue);
        powerStatus.setText(R.string.stove_power_on);
        powerStatus.setTextColor(Color.parseColor("#3468d3"));
        if (convenientCookService.isConvenientCook()) {
            stoveHeadihId = convenientCookService.getStoveHeadihId();
            if (stoveHeadihId == Stove.StoveHead.RIGHT_ID) {
                workTime.setText(convenientCookService.getCookRecipeName());
                time.setVisibility(VISIBLE);
                time.setTextColor(Color.parseColor("#3468d3"));
                stoveTimer.setVisibility(INVISIBLE);
                time.setText(TimeUtils.sec2clock(convenientCookService.getCountDownTime()));
            } else {
                workTime.setText(TimeUtils.sec2clock(stove[0].rightHead.worktime));
            }
        } else {
            workTime.setText(TimeUtils.sec2clock(stove[0].rightHead.worktime));
        }
        workTime.setVisibility(VISIBLE);
    }


    public void setLeftWorkStatus(short level) {
        if (stove[0].leftHead.time != 0) {
            LogUtils.e(TAG, "leftHeadTime:" + stove[0].leftHead.time);
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
//            LogUtils.e("20200723", "stopAnimation 3");
            stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
            decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            add.setImageResource(R.mipmap.stove_fire_add_gray);

        } else {
            stoveH.setImageResource(R.mipmap.stove_head_blue);
            animImg.setVisibility(VISIBLE);
            startAnimation();
//            LogUtils.e("20200723", "startAnimation 2");
            stoveTimer.setImageResource(R.mipmap.stove_timer_white);

            if (1 == level) {
                decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            } else {
                decrease.setImageResource(R.mipmap.stove_fire_decrease_white);
            }
            if (10 == level) {
                add.setImageResource(R.mipmap.stove_fire_add_gray);
            } else {
                add.setImageResource(R.mipmap.stove_fire_add_white);
            }

        }
        fireShow.setText(SpannableStringUtils.createSpannableString(level));
        fireShow.setTextColor(Color.parseColor("#FFFFFF"));
        stoveClose.setImageResource(R.mipmap.stove_power_blue);
        powerStatus.setText(R.string.stove_power_on);
        powerStatus.setTextColor(Color.parseColor("#3468d3"));
        if (convenientCookService.isConvenientCook()) {
            stoveHeadihId = convenientCookService.getStoveHeadihId();
            if (stoveHeadihId == Stove.StoveHead.LEFT_ID) {
                workTime.setText(convenientCookService.getCookRecipeName());
                time.setVisibility(VISIBLE);
                time.setTextColor(Color.parseColor("#3468d3"));
                stoveTimer.setVisibility(INVISIBLE);
                time.setText(TimeUtils.sec2clock(convenientCookService.getCountDownTime()));
            } else {
                workTime.setText(TimeUtils.sec2clock(stove[0].leftHead.worktime));
            }
        } else {
            workTime.setText(TimeUtils.sec2clock(stove[0].leftHead.worktime));
        }
        workTime.setVisibility(VISIBLE);
    }


    protected void disConnected() {
        stopAnimation();
//        LogUtils.e("20200723", "stopAnimation 4");
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
        workTime.setVisibility(GONE);
        powerStatus.setTextColor(Color.parseColor("#666666"));
    }

    private void clickCallBack(String tag) {
        if (clickStoveSelectListener != null) {
            clickStoveSelectListener.clickStoveSelectListener(tag);
        }
    }

    private void longClickCallBack(String tag) {
        if (longClickStoveSelectListener != null) {
            longClickStoveSelectListener.longClickStoveSelectListener(tag);
        }
    }

    Animation rotate;

    void startAnimation() {
        if (rotate == null) {
            rotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            rotate.setInterpolator(lin);
            animImg.startAnimation(rotate);
            LogUtils.e("20200723", "startAnimation:");
        }
    }

    void stopAnimation() {

        if (rotate != null) {
            LogUtils.e("20200723", "stopAnimation:" + rotate);
            rotate.cancel();
            rotate = null;
            animImg.clearAnimation();
        }
    }

    public void setOffStatus() {
        stopAnimation();
//        LogUtils.e("20200723", "stopAnimation 5");
        animImg.setVisibility(GONE);
        stoveTimer.setVisibility(VISIBLE);
        time.setVisibility(INVISIBLE);
        stoveH.setImageResource(R.mipmap.stove_head_gray);
        stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
        decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
        add.setImageResource(R.mipmap.stove_fire_add_gray);
        stoveClose.setImageResource(R.mipmap.stove_power_white);
        fireShow.setText("——");
        fireShow.setTextColor(Color.parseColor("#666666"));
        powerStatus.setText(R.string.stove_close);
        workTime.setVisibility(GONE);
        powerStatus.setTextColor(Color.parseColor("#ffffff"));
    }

    public void setStandyByStatus() {
        if (stove[0].isLock) {
            stopAnimation();
//            LogUtils.e("20200723", "stopAnimation 5");
            time.setVisibility(GONE);
            stoveTimer.setVisibility(VISIBLE);
            fireShow.setText("——");
            fireShow.setTextColor(Color.parseColor("#808080"));
            animImg.setVisibility(GONE);
            stoveH.setImageResource(R.mipmap.stove_head_gray);
            stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
            decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            add.setImageResource(R.mipmap.stove_fire_add_gray);
        } else {
            stopAnimation();
            time.setVisibility(GONE);
            stoveTimer.setVisibility(VISIBLE);
            fireShow.setText("——");
            fireShow.setTextColor(Color.parseColor("#FFFFFF"));
            animImg.setVisibility(GONE);
            stoveH.setImageResource(R.mipmap.stove_head_blue);
            stoveTimer.setImageResource(R.mipmap.stove_timer_white);
            decrease.setImageResource(R.mipmap.stove_fire_decrease_white);
            add.setImageResource(R.mipmap.stove_fire_add_white);
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.stove_power_on);
            powerStatus.setTextColor(Color.parseColor("#3468d3"));
        }
    }

    //实时获取最新的灶
    public void setDevice(Stove stove) {
        this.stove[0] = stove;
    }

}
