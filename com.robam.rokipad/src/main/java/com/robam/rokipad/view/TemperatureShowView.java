package com.robam.rokipad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.robam.common.pojos.HotOil;
import com.robam.rokipad.R;
import com.robam.rokipad.utils.IClickStoveSelectListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2019/1/18.
 */

public class TemperatureShowView extends FrameLayout {

    Context cx;
    View viewRoot;
    @InjectView(R.id.mode)
    TextView mode;
    @InjectView(R.id.which_stove_header)
    TextView whichStoveHeader;
    @InjectView(R.id.temperature)
    TextView temperature;
    @InjectView(R.id.progress)
    ProgressCircle progress;
    @InjectView(R.id.img_back)
    ImageView imgBack;

    public TemperatureShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TemperatureShowView(Context context) {
        super(context);
        initView(context, null);
    }

    public TemperatureShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context cx, AttributeSet attrs) {
        this.cx = cx;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.fan_temputure_show, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);
    }

    private IClickStoveSelectListener clickStoveSelectListener;

    public void setClickBackListener(IClickStoveSelectListener clickStoveSelectListener) {
        this.clickStoveSelectListener = clickStoveSelectListener;
    }

    @OnClick(R.id.img_back)
    public void onClickBack() {
        if (clickStoveSelectListener != null) {
            clickStoveSelectListener.clickStoveSelectListener("return");
        }
    }

    public void setProgress(int pro) {
        progress.setProgress(pro);
    }

    public void setTotalProgress(int totalProgress) {
        progress.setTotalProgress(totalProgress);
    }

    /**
     * 设置模式名称和温度区间
     *
     * @param modeId  模式Id
     * @param hotOils 存储所有模式的集合
     */
    public void setModelByTempInterval(int modeId, List<HotOil> hotOils) {
        if (modeId == 100 || hotOils == null) return;
        String name = hotOils.get(modeId).name;
        String tempInterval = hotOils.get(modeId).temp_interval;
        mode.setText(name);
        temperature.setText(tempInterval);
    }

    /**
     * 设置左右炉头
     *
     * @param direction left左炉头  right右炉头
     */
    public void setStoveHeadText(String direction) {
        if ("left".equals(direction)) {
            whichStoveHeader.setText(R.string.device_head_left_text);
        } else {
            whichStoveHeader.setText(R.string.device_head_right_text);
        }
    }
}
