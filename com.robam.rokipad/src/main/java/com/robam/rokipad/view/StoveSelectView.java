package com.robam.rokipad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robam.rokipad.R;
import com.robam.rokipad.utils.IClickStoveSelectListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2019/1/18.
 */

public class StoveSelectView extends FrameLayout {
    Context cx;
    View viewRoot;
    @InjectView(R.id.txt_select)
    TextView txtSelect;
    @InjectView(R.id.left)
    ImageView left;
    @InjectView(R.id.left_click)
    RelativeLayout leftClick;
    @InjectView(R.id.right)
    ImageView right;
    @InjectView(R.id.right_click)
    RelativeLayout rightClick;

    public StoveSelectView(Context context) {
        super(context);
        initView(context, null);
    }

    public StoveSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public StoveSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context cx, AttributeSet attrs) {
        this.cx = cx;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.fan_stove_select, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);
    }


    public IClickStoveSelectListener clickStoveSelectListener;

    public void setOnClickStoveSelectListener(IClickStoveSelectListener clickStoveSelectListener) {
        this.clickStoveSelectListener = clickStoveSelectListener;
    }

    @OnClick({R.id.fl_back_oil, R.id.left_click, R.id.right_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_back_oil:
                clickCallBack("back");
                break;
            case R.id.left_click:
                clickCallBack("left");
                break;
            case R.id.right_click:
                clickCallBack("right");
                break;
        }
    }

    private void clickCallBack(String tag) {
        if (clickStoveSelectListener != null) {
            clickStoveSelectListener.clickStoveSelectListener(tag);
        }
    }


}
