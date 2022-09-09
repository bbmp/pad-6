package com.robam.rokipad.ui.view.cooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robam.rokipad.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/28.
 * PS: 备菜阶段页面.
 */
public class RecipePrePareDetailView extends FrameLayout implements View.OnClickListener {

    @InjectView(R.id.tv_desc_txt)
    TextView mTvDescTxt;
    @InjectView(R.id.ll_start_cook)
    RelativeLayout mLlStartCook;
    @InjectView(R.id.tv_start_cook)
    TextView tvStartCook;
    private String mDesc;
    private IStartCookingListener mStartCookingListener;

    public RecipePrePareDetailView(Context context, String desc) {
        super(context);
        this.mDesc = desc;
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_prepare_recipe_detail,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        mLlStartCook.setOnClickListener(this);
        initData();
    }

    private void initData() {
        mTvDescTxt.setText(mDesc);
    }

    public void setPreStepDese(String desc) {
        mTvDescTxt.setText(desc);
    }

    public void setStartCookingListener(IStartCookingListener startCookingListener) {
        mStartCookingListener = startCookingListener;
    }


    public interface IStartCookingListener {

        void start();
    }

    @Override
    public void onClick(View v) {
        if (mStartCookingListener != null) {
            mStartCookingListener.start();
        }
    }
}
