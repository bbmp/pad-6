package com.robam.rokipad.ui.view.cooking;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.robam.common.events.RecipeDcEvent;
import com.robam.common.events.RecipeOrderEvent;
import com.robam.common.events.RecipeStepEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.util.StoveSendCommandUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.service.PadPotOneKeyCookTaskService;
import com.robam.rokipad.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robam.rokipad.NewPadApp.isRecipeRun;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/28.
 * PS: 做菜当前步鄹页面.
 */
public class RecipePotCurrentStepDetailView extends FrameLayout {


    @InjectView(R.id.tv_current_step_desc)
    TextView mTvCurrentStepDesc;
    @InjectView(R.id.tv_cook_finish)
    TextView tvCookFinish;
    private CookStep mCurrentCookStep;//当前展示的步鄹信息
    ArrayList<CookStep> mCookSteps;
    private Recipe mRecipe;
    private Context mContext;
    private AbsFan mFan;
    private Stove mStove;
    private int step;
    private Stove.StoveHead mHeadId;
    private StoveSendCommandUtils mStoveSendCommandUtils;
    private int order;


    @Subscribe
    public void onEvent(RecipeOrderEvent event) {
        order = event.order;
        stepSkip(order);
    }

    public RecipePotCurrentStepDetailView(Context context, ArrayList<CookStep> cookSteps,
                                          int currentIndex, Recipe recipe, AbsFan fan, Stove stove,
                                          Stove.StoveHead headId, StoveSendCommandUtils commandUtils) {
        super(context);
        mContext = context;
        mRecipe = recipe;
        mCookSteps = cookSteps;
        mCurrentCookStep = cookSteps.get(currentIndex);
        mFan = fan;
        mStove = stove;
        mHeadId = headId;
        mStoveSendCommandUtils = commandUtils;
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_pot_current_step_recipe_detail,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        initStep();
        initData();
        initListener();
        if (IRokiFamily.IRokiDevicePlat.RQZ02.equals(mStove.getDp())) {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mStoveSendCommandUtils.setStoveLevel(mCurrentCookStep.getParamByCodeName(mStove.getDp(), "stoveGear"));
            }
        }
        PadPotOneKeyCookTaskService.getInstance().setStep(order);
    }

    //跳转到指定步鄹
    private void stepSkip(final int order) {

        try {
            String cookStepData = PreferenceUtils.getString(("roki" + mRecipe.id), null);
            List<CookStep> cookSteps = JsonUtils.json2List(cookStepData, CookStep.class);
            if (cookSteps == null || cookSteps.size() == 0) return;
            mCurrentCookStep = cookSteps.get(order - 1);
            LogUtils.e("20190610", " mCurrentCookStep:" + mCurrentCookStep.desc);
            mTvCurrentStepDesc.setText(mCurrentCookStep.desc);
            step = order - 1;
//            mStoveSendCommandUtils.setStep(step);
//            mStoveSendCommandUtils.setCookStep(mCurrentCookStep);
//            mStoveSendCommandUtils.onStart();
            isRecipeRun = true;
            initStep();
            PadPotOneKeyCookTaskService.getInstance().setStep(step);
        } catch (Exception e) {
        }
    }


    private void initStep() {
        LogUtils.e("20190416", "dc:::" + mCurrentCookStep.dc);

        if (TextUtils.isEmpty(mCurrentCookStep.dc)) {
            EventUtils.postEvent(new RecipeDcEvent(false));
        } else {
            EventUtils.postEvent(new RecipeDcEvent(true));
        }
    }


    private void initListener() {

        tvCookFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("20190820", "text:" + tvCookFinish.getText().toString());
                if ((mContext.getString(R.string.cook_next_text)).equals(tvCookFinish.getText().toString())) {
                    stepSkip(step + 2);
                    EventUtils.postEvent(new RecipeStepEvent(0));
                    step++;
                    tvCookFinish.setText(R.string.cook_finish_text);

                } else {
                    final IRokiDialog exitCookDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_0);
                    exitCookDialog.setContentText(R.string.cook_exit_text);
                    exitCookDialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
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
                                LogUtils.e("20190820", ":::mCookSteps:::" + mCookSteps.size());
                                if (mCookSteps.size() == 1) {
                                    finishCooking();
                                }
                            }
                        }
                    });
                    exitCookDialog.setCancelBtn(R.string.cancel, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (exitCookDialog.isShow()) {
                                exitCookDialog.dismiss();
                            }
                        }
                    });
                    exitCookDialog.show();
                }
            }
        });

    }

    private void initData() {
        mTvCurrentStepDesc.setText(mCurrentCookStep.desc);
        tvCookFinish.setText(R.string.cook_finish_text);
    }

    public void setNotAuto(int res) {
        tvCookFinish.setText(res);
    }

    //做菜结束时的逻辑
    private void finishCooking() {
        mStoveSendCommandUtils.onFinish();
        final IRokiDialog cookFinishDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_COOKING_FINISH);
        cookFinishDialog.setContentText(mContext.getString(R.string.cook_ok_finish_text) + '"' + mRecipe.getName() + '"');
        cookFinishDialog.setOkBtn(R.string.cook_know_text, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cookFinishDialog.isShow()) {
                    cookFinishDialog.dismiss();
//                    UIService.getInstance().popBack();
//                    UIService.getInstance().popBack();
//                    UIService.getInstance().popBack();
                    isRecipeRun = false;
                }
            }
        });
        cookFinishDialog.show();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isRecipeRun = true;
        EventUtils.regist(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
        removeView(this);
        isRecipeRun = false;
        step = 0;
        mCookSteps = null;
        mCurrentCookStep = null;
    }


}
