package com.robam.rokipad.ui.page.cooking;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.RecipeDcEvent;
import com.robam.common.events.RecipeExitEvent;
import com.robam.common.events.RecipeOrderEvent;
import com.robam.common.events.RecipeStepEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.util.StoveSendCommandUtils;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.listener.OnRecyclerViewItemClickListener;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.view.cooking.RecipeCurrentStepDetailView;
import com.robam.rokipad.ui.view.cooking.RecipeWaitStepView;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.FanAssistUtils;
import com.robam.rokipad.utils.RoundTransformation;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robam.rokipad.NewPadApp.isRecipeRun;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/29.
 * PS: 灶具自动烹饪开始页面
 */
public class AutoCookRecipeStoveStartPage extends BasePage {


    @InjectView(R.id.iv_cur_step_image)
    ImageView mIvCurStepImage;
    @InjectView(R.id.tv_stove_fire)
    TextView mTvStoveFire;
    @InjectView(R.id.tv_fan_volume)
    TextView mTvFanVolume;
    @InjectView(R.id.fl_cur_step_recipe_container)
    FrameLayout mFlCurStepRecipeContainer;
    @InjectView(R.id.fl_surplus_recipe_step_container)
    FrameLayout mFlSurplusRecipeStepContainer;
    @InjectView(R.id.ll_device_dc)
    LinearLayout llDeviceDc;
    private Recipe mRecipe;
    private ArrayList<CookStep> mCookSteps;
    private String mDeviceGuid;
    private short mHeadId;
    public int step = 0;
    private RecipeCurrentStepDetailView mCurrentStepDetailView;
    private RecipeWaitStepView mRecipeWaitStepView;
    private AbsFan mFan;
    private Stove mStove;
    private Stove.StoveHead mStoveHeadId;
    private StoveSendCommandUtils mStoveSendCommandUtils;


    @Subscribe
    public void onEvent(RecipeExitEvent event) {
        exitRecipeDialog();
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {

        if (null == mStove || !com.google.common.base.Objects.equal(mStove.getID(), event.device.getID())) {
            return;
        }
        if (!event.isConnected) {
            ToastUtils.showShort(R.string.device_off_line);
        }
    }

    @Subscribe
    public void onEvent(RecipeDcEvent event) {//做菜时无需设备的事件 false 无设备  true 有设备
        if (event.isDc) {
            llDeviceDc.setVisibility(View.VISIBLE);
        } else {
            llDeviceDc.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRecipe = (Recipe) bundle.getSerializable(PageArgumentKey.Recipe);
            mCookSteps = (ArrayList<CookStep>) bundle.getSerializable(PageArgumentKey.CookSteps);
            mDeviceGuid = bundle.getString(PageArgumentKey.DeviceGuid);
            mHeadId = (short) bundle.getInt(PageArgumentKey.HeadId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        fireBaseAnalytics.setCurrentScreen(getActivity(), mRecipe.name + cx.getString(R.string.google_screen_recipe_cook), null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_auto_recipe_stove_start, container, false);
        ButterKnife.inject(this, view);
        mFan = Utils.getDefaultFan();
        mStove = mFan.getChildStove();
        LogUtils.e("20190416", "mStove:" + mStove);
        if (mHeadId == Stove.StoveHead.LEFT_ID) {
            mStoveHeadId = mStove.leftHead;
        } else {
            mStoveHeadId = mStove.rightHead;
        }
        initData();
        return view;
    }

    private void initData() {
        if (mCookSteps == null || mCookSteps.size() == 0) return;
        mIvCurStepImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(cx).load(mCookSteps.get(0).imageUrl)
                .placeholder(R.mipmap.img_default_recipe)
                .transform(new RoundTransformation(cx, 10))
                .into(mIvCurStepImage);
        mStoveSendCommandUtils = new StoveSendCommandUtils(mFan, mStove, mStoveHeadId, mCookSteps, step);
        mCurrentStepDetailView = new RecipeCurrentStepDetailView(cx, mCookSteps, step, mRecipe, mFan
                , mStove, mStoveHeadId, mStoveSendCommandUtils);
        mFlCurStepRecipeContainer.addView(mCurrentStepDetailView);
        mRecipeWaitStepView = new RecipeWaitStepView(cx, mCookSteps, mCookSteps.size());
        mFlSurplusRecipeStepContainer.addView(mRecipeWaitStepView);
        listener();
    }

    private void listener() {

        mStoveSendCommandUtils.setListenerLevel(new StoveSendCommandUtils.ListenerLevel() {
            @Override
            public void stoveLevel(int level) {
                if (mTvStoveFire == null) return;
                mTvStoveFire.setText("P" + level);
            }

            @Override
            public void fanLevel(int level) {
                String levelByName = FanAssistUtils.getRecipeLevelByName(cx, level);
                if (mTvFanVolume == null) return;
                mTvFanVolume.setText(levelByName);
            }

            @Override
            public void setStepImgUrl(String imgUrl) {
                Glide.with(cx).load(imgUrl)
                        .placeholder(R.mipmap.img_default_recipe)
                        .transform(new RoundTransformation(cx, 10))
                        .into(mIvCurStepImage);
            }
        });
        mRecipeWaitStepView.setItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(final View view) {
                final IRokiDialog switcherStepDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_0);
                switcherStepDialog.setContentText(R.string.cook_switcher_step_text);
                switcherStepDialog.setOkBtn(R.string.cook_switcher_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switcherStepDialog.dismiss();
                        int step = (int) view.getTag();
                        int order = (int) view.getTag(R.id.tag_recycler_holder);
                        EventUtils.postEvent(new RecipeStepEvent(step));
                        EventUtils.postEvent(new RecipeOrderEvent(order));
                    }
                });
                switcherStepDialog.setCancelBtn(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switcherStepDialog.dismiss();
                    }
                });
                switcherStepDialog.show();

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                exitRecipeDialog();
            }
        }
        return true;

    }

    private void exitRecipeDialog() {
        final IRokiDialog exitCookDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_0);
        exitCookDialog.setContentText(R.string.cook_exit_text);
        exitCookDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exitCookDialog.isShow()) {
                    exitCookDialog.dismiss();
                    // TODO: 2019/4/12 退出烹饪时下发命令
                    UIService.getInstance().popBack();
                    UIService.getInstance().popBack();
                    UIService.getInstance().popBack();
                    isRecipeRun = false;
                    mStoveSendCommandUtils.onFinish();
                }
            }
        });
        exitCookDialog.setCancelBtn(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exitCookDialog.isShow()) {
                    exitCookDialog.dismiss();
                }
            }
        });
        exitCookDialog.show();
    }

    @Override
    public void setRootBg() {
        setRootBgRes(R.mipmap.ic_center_bg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
