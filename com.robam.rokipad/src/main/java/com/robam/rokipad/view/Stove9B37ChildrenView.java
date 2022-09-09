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
 * Created by Dell on 2019/5/30.
 */

public class Stove9B37ChildrenView extends FrameLayout {
    Context cx;
    View viewRoot;
    @InjectView(R.id.stove_timer)
    ImageView stoveTimer;
    @InjectView(R.id.time)
    TextView time;
    @InjectView(R.id.head_name)
    TextView headName;
    @InjectView(R.id.iv_fire_show)
    ImageView ivFireShow;
    @InjectView(R.id.stove_h)
    ImageView stoveH;
    @InjectView(R.id.anim_img)
    ImageView animImg;
    private Stove[] stove;


    public Stove9B37ChildrenView(Context context) {
        super(context);
        initView(context, null);
    }


    public Stove9B37ChildrenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public Stove9B37ChildrenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.cx = context;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.stove_9b37_view, this, true);
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


    @OnClick({R.id.stove_timer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.stove_timer:
                clickCallBack("time");
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
        stoveH.setImageResource(R.mipmap.stove_head_blue);
        animImg.setVisibility(VISIBLE);
        startAnimation();
        stoveTimer.setImageResource(R.mipmap.stove_timer_white);
        ivFireShow.setImageResource(R.mipmap.ic_fire_on);

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

        stoveH.setImageResource(R.mipmap.stove_head_blue);
        animImg.setVisibility(VISIBLE);
        startAnimation();
        stoveTimer.setImageResource(R.mipmap.stove_timer_white);
        ivFireShow.setImageResource(R.mipmap.ic_fire_on);

    }


    protected void disConnected() {
        stopAnimation();
        animImg.setVisibility(GONE);
        stoveTimer.setVisibility(VISIBLE);
        time.setVisibility(INVISIBLE);
        stoveH.setImageResource(R.mipmap.stove_head_gray);
        stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
        ivFireShow.setImageResource(R.mipmap.ic_fire_off);
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


    public void setOffStatus() {
        stopAnimation();
        animImg.setVisibility(GONE);
        stoveTimer.setVisibility(VISIBLE);
        time.setVisibility(INVISIBLE);
        stoveH.setImageResource(R.mipmap.stove_head_gray);
        stoveTimer.setImageResource(R.mipmap.stove_timer_gray);
        ivFireShow.setImageResource(R.mipmap.ic_fire_off);
    }

    public void setDevice(Stove stove) {
        this.stove[0] = stove;
    }
}
