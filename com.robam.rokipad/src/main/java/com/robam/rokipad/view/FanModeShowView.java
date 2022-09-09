package com.robam.rokipad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robam.common.pojos.HotOil;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.rokipad.R;
import com.robam.rokipad.utils.IClickModeListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2019/1/18.
 */

public class FanModeShowView extends FrameLayout {
    Pot pot;
    Context cx;
    View viewRoot;
    @InjectView(R.id.guo)
    ImageView guo;
    @InjectView(R.id.guo_click)
    RelativeLayout guoClick;
    @InjectView(R.id.rou)
    ImageView rou;
    @InjectView(R.id.rou_click)
    RelativeLayout rouClick;
    @InjectView(R.id.fuliao)
    ImageView fuliao;
    @InjectView(R.id.fuliao_click)
    RelativeLayout fuliaoClick;
    @InjectView(R.id.jian)
    ImageView jian;
    @InjectView(R.id.jian_click)
    RelativeLayout jianClick;
    @InjectView(R.id.tv_pot_link_status)
    TextView tvPotLinkStatus;
    @InjectView(R.id.rl_pot_link_status)
    RelativeLayout rlPotLinkStatus;
    private List<HotOil> mData;
    public IClickModeListener clickModelListener;

    public FanModeShowView(Context context) {
        super(context);
        initView(context, null);
    }

    public FanModeShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FanModeShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context cx, Object o) {
        this.cx = cx;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.fan_tempture_oil, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);

    }


    public void setOnClickModeListener(IClickModeListener clickModelistener) {
        this.clickModelListener = clickModelistener;
    }

    @OnClick({R.id.guo_click, R.id.rou_click, R.id.fuliao_click, R.id.jian_click,
            R.id.rl_pot_link_status})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.guo_click://编号定为0
                callBackListener(0);
                break;
            case R.id.rou_click://编号定为1
                callBackListener(1);
                break;
            case R.id.fuliao_click://编号定为2
                callBackListener(2);
                break;
            case R.id.jian_click://编号定为3
                callBackListener(3);
                break;
            case R.id.rl_pot_link_status://编号定为4

                if (null == pot) {
                    callBackListener(4);
                } else {
                    callBackListener(5);
                }
                break;
            default:
                break;
        }
    }

    private void callBackListener(int num) {
        if (clickModelListener != null) {
            clickModelListener.onClickModeListener(num);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setPotLinkStatus(Pot pot) {
        this.pot = pot;
        if (null != pot) {
            tvPotLinkStatus.setText(R.string.pot_link_ok);
        } else {
            tvPotLinkStatus.setText(R.string.pot_link_not);
        }
    }
}
