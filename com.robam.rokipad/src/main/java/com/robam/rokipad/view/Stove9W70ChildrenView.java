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
import com.robam.rokipad.utils.IClickStoveSelectListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2019/2/12.
 */

public class Stove9W70ChildrenView extends FrameLayout {
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


    public Stove9W70ChildrenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public Stove9W70ChildrenView(Context context) {
        super(context);
        initView(context, null);
    }

    public Stove9W70ChildrenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.cx = context;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.stove_9w70_view, this, true);
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
            fireShow.setText(String.valueOf("P" + level));
            fireShow.setTextColor(Color.parseColor("#666666"));
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.stove_power_on);
            powerStatus.setTextColor(Color.parseColor("#3468d3"));

        } else {
            stoveH.setImageResource(R.mipmap.stove_head_blue);
            animImg.setVisibility(VISIBLE);
            startAnimation();
//            LogUtils.e("20200723", "startAnimation 3");
            stoveTimer.setImageResource(R.mipmap.stove_timer_white);
            if (1 == level){
                decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            }else {
                decrease.setImageResource(R.mipmap.stove_fire_decrease_white);
            }
            if (9 == level){
                add.setImageResource(R.mipmap.stove_fire_add_gray);
            }else {
                add.setImageResource(R.mipmap.stove_fire_add_white);
            }
            fireShow.setText(String.valueOf("P" + level));
            fireShow.setTextColor(Color.parseColor("#ffffff"));
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.stove_power_on);
            powerStatus.setTextColor(Color.parseColor("#3468d3"));
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
//            LogUtils.e("20200723", "stopAnimation 3");
            stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
            decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            add.setImageResource(R.mipmap.stove_fire_add_gray);
            fireShow.setText(String.valueOf("P" + level));
            fireShow.setTextColor(Color.parseColor("#666666"));
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.stove_power_on);
            powerStatus.setTextColor(Color.parseColor("#3468d3"));

        } else {
            stoveH.setImageResource(R.mipmap.stove_head_blue);
            animImg.setVisibility(VISIBLE);
            startAnimation();
//            LogUtils.e("20200723", "startAnimation 2");
            stoveTimer.setImageResource(R.mipmap.stove_timer_white);

            if (1 == level){
                decrease.setImageResource(R.mipmap.stove_fire_decrease_gray);
            }else {
                decrease.setImageResource(R.mipmap.stove_fire_decrease_white);
            }
            if (9 == level){
                add.setImageResource(R.mipmap.stove_fire_add_gray);
            }else {
                add.setImageResource(R.mipmap.stove_fire_add_white);
            }

            fireShow.setText(String.valueOf("P" + level));
            fireShow.setTextColor(Color.parseColor("#ffffff"));
            stoveClose.setImageResource(R.mipmap.stove_power_blue);
            powerStatus.setText(R.string.stove_power_on);
            powerStatus.setTextColor(Color.parseColor("#3468d3"));
        }
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
        powerStatus.setTextColor(Color.parseColor("#666666"));
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
//            LogUtils.e("20200723", "stopAnimation 6");
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

//    public void onPause() {
//        stopAnimation();
//        LogUtils.e("20200723", "Stove9W70ChildrenView onPause");
//
//    }

//    public void onResume() {
//        startAnimation();
//        LogUtils.e("20200723", "Stove9W70ChildrenView onResume");
//    }


//    public boolean getIsTop() {
//
//        String currentPageKey = UIService.getInstance().getTop().getCurrentPageKey();
//        LogUtils.e("20200723", "currentPageKey:" + currentPageKey);
//        if ("GuideLogin".equals(currentPageKey) || "HomeSet".equals(currentPageKey)) {
//            LogUtils.e("20200723", "return false;:" + currentPageKey);
//            return false;
//        } else if ("Home".equals(currentPageKey)) {
//            LogUtils.e("20200723", "return true;:" + currentPageKey);
//            return true;
//        }
//        LogUtils.e("20200723", "return false:::;:" + currentPageKey);
//        return false;
//    }


}
