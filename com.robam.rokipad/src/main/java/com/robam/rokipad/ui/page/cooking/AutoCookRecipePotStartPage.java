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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.events.PlotRecipeNextEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.events.RecipeExitEvent;
import com.robam.common.events.RecipeOrderEvent;
import com.robam.common.events.RecipeStepEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.services.RuleCookTaskService;
import com.robam.common.util.StoveSendCommandUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.listener.OnRecyclerViewItemClickListener;
import com.robam.rokipad.service.PadPotOneKeyCookTaskService;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.view.cooking.RecipeCurrentStepDetailView;
import com.robam.rokipad.ui.view.cooking.RecipePotCurrentStepDetailView;
import com.robam.rokipad.ui.view.cooking.RecipePotWaitStepView;
import com.robam.rokipad.ui.view.cooking.RecipeWaitStepView;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.FanAssistUtils;
import com.robam.rokipad.utils.RoundTransformation;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import regulation.dto.StatusResult;

import static com.robam.rokipad.NewPadApp.isRecipeRun;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/29.
 * PS: 温控锅自动烹饪开始页面
 */
public class AutoCookRecipePotStartPage extends BasePage {


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
    @InjectView(R.id.tv_pot_temp)
    TextView tvPotTemp;
    private Recipe mRecipe;
    private ArrayList<CookStep> mCookSteps;
    private String mDeviceGuid;
    private short mHeadId;
    public int step = 0;
    private RecipePotCurrentStepDetailView mPotCurrentStepDetailView;
    private RecipePotWaitStepView mRecipePotWaitStepView;
    private AbsFan mFan;
    private Stove mStove;
    private Pot[] mPot;
    private Stove.StoveHead mStoveHeadId;
    private StoveSendCommandUtils mStoveSendCommandUtils;
    private PadPotOneKeyCookTaskService padPotOneKeyCookTaskService;
    private int mStepIndex;

    @Subscribe
    public void onEvent(RecipeExitEvent event) {
        exitRecipeDialog();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_auto_recipe_pot_start, container, false);
        ButterKnife.inject(this, view);
        padPotOneKeyCookTaskService = padPotOneKeyCookTaskService.getInstance();
        mFan = Utils.getDefaultFan();
        mStove = mFan.getChildStove();
        mPot = Utils.getDefaultPot();
        LogUtils.e("20190416", "mStove:" + mStove);
        if (mHeadId == 0) {
            mStoveHeadId = mStove.leftHead;
        } else {
            mStoveHeadId = mStove.rightHead;
        }
        startCook_Fan(mHeadId);
        initData();
        return view;
    }

    private void startCook_Fan(int headId) {
        mStoveSendCommandUtils = new StoveSendCommandUtils(mFan, mStove, mStoveHeadId, mCookSteps, step);
//        if (padPotOneKeyCookTaskService.isRunning()) {
//            ToastUtils.showShort("正在烧菜中");
//            return;
//        }

        Stove.StoveHead head = null;
        if (mStove != null) {
            head = mStove.getHeadById(headId);
            if (!mStove.isConnected()) {
                ToastUtils.showShort(R.string.stove_invalid_error);
                return;
            }

        }
        padPotOneKeyCookTaskService.start(head, mCookSteps, mRecipe.id, mStoveSendCommandUtils);
        mStepIndex = 0;

    }

    private void initData() {
        if (mCookSteps == null || mCookSteps.size() == 0) return;
        mIvCurStepImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(cx).load(mCookSteps.get(0).imageUrl)
                .placeholder(R.mipmap.img_default_recipe)
                .transform(new RoundTransformation(cx, 10))
                .into(mIvCurStepImage);
        mPotCurrentStepDetailView = new RecipePotCurrentStepDetailView(cx, mCookSteps, step, mRecipe, mFan
                , mStove, mStoveHeadId, mStoveSendCommandUtils);
        mFlCurStepRecipeContainer.addView(mPotCurrentStepDetailView);
        mRecipePotWaitStepView = new RecipePotWaitStepView(cx, mCookSteps, mCookSteps.size());
        mFlSurplusRecipeStepContainer.addView(mRecipePotWaitStepView);
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
        tvPotTemp.setText((String.valueOf((int) mPot[0].tempUp) + "℃"));
        mRecipePotWaitStepView.setItemClickListener(new OnRecyclerViewItemClickListener() {
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
                        mStepIndex = order;
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

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        LogUtils.e("20180507", "PotStatusChangedEvent");
        if (event.pojo == null || padPotOneKeyCookTaskService == null || !padPotOneKeyCookTaskService.isRunning())
            return;
        try {
            if (!Objects.equal(mPot[0].getID(), event.pojo.getID())) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        tvPotTemp.setText((String.valueOf((int) mPot[0].tempUp) + "℃"));
        /****开始做烹饪传包****/
        LogUtils.e("20180507", "tempUp::;" + mPot[0].tempUp + "   mstep::" + mStepIndex);
        padPotOneKeyCookTaskService.RecipeTempUpEvent(mPot[0].tempUp, new RuleCookTaskService.AbsCookTaskServiceCallBack() {

            @Override
            public void onCompleted(PlotRecipeNextEvent event) {
                if (event == null) return;

                try {
                    if (!"auto".equals(event.getType())) {//手动
                        if (mCookSteps.size() == 1) {
                            mPotCurrentStepDetailView.setNotAuto(R.string.cook_finish_text);
                        } else {
                            mPotCurrentStepDetailView.setNotAuto(R.string.cook_next_text);
                        }
                    } else {//自动
                        StatusResult statusResult = event.getStatusResult();
                        mPotCurrentStepDetailView.setNotAuto(R.string.cook_finish_text);
//                        LogUtils.e("20190610", "ProcessingPercent" + statusResult.getProcessingPercent());
//                        if (statusResult.getPercentEnable()) {
//                            runStep(mPot.tempUp, statusResult.getProcessingPercent());
//                        } else {
//                            runStep(mPot.tempUp, statusResult.getProcessingPercent());
//                        }
                        if (statusResult.getFinished()) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                            }
                            mStepIndex++;
                            padPotOneKeyCookTaskService.next();
                            EventUtils.postEvent(new RecipeOrderEvent(mStepIndex + 1));
                            EventUtils.postEvent(new RecipeStepEvent(0));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    padPotOneKeyCookTaskService.stop();
                    padPotOneKeyCookTaskService.setRunning(false);
                    UIService.getInstance().popBack();
                    UIService.getInstance().popBack();
                    UIService.getInstance().popBack();
                    isRecipeRun = false;

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
        padPotOneKeyCookTaskService.setRunning(false);
        padPotOneKeyCookTaskService = null;
    }
}
