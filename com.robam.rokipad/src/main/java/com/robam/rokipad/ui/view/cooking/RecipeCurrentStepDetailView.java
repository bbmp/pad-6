package com.robam.rokipad.ui.view.cooking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.plat.constant.IPlatRokiFamily;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.RecipeDcEvent;
import com.robam.common.events.RecipeNeedTimeEvent;
import com.robam.common.events.RecipeOrderEvent;
import com.robam.common.events.RecipeStepEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.util.StoveSendCommandUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robam.rokipad.NewPadApp.isRecipeRun;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/28.
 * PS: 做菜当前步鄹页面.
 */
public class RecipeCurrentStepDetailView extends FrameLayout {

    private static final String TAG = "RecipeCurrentStepDetail";

    @InjectView(R.id.tv_current_step)
    TextView mTvCurrentStep;
    @InjectView(R.id.tv_sum_step)
    TextView mTvSumStep;
    @InjectView(R.id.tv_step_time)
    TextView mTvStepTime;
    @InjectView(R.id.tv_current_step_desc)
    TextView mTvCurrentStepDesc;
    @InjectView(R.id.rl_button_left)
    TextView mRlButtonLeft;
    @InjectView(R.id.rl_button_right)
    TextView mRlButtonRight;
    @InjectView(R.id.seek_bar)
    SeekBar mSeekBar;
    @InjectView(R.id.tv_model_switcher)
    TextView mTvModelSwitcher;
    //    private IStartCookingListener mStartCookingListener;
    private CookStep mCurrentCookStep;//当前展示的步鄹信息
    ArrayList<CookStep> mCookSteps;
    private Recipe mRecipe;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mNeedTime;//步鄹时间
    private int mCurrentTime;
    private int mTime = 0;
    private final int mTotalStep;//总步鄹
    private Context mContext;
    private boolean isModelSwitch = false;
    private AbsFan mFan;
    private Stove mStove;
    private int step;
    private Stove.StoveHead mHeadId;
    private StoveSendCommandUtils mStoveSendCommandUtils;
    private int COOK_MODEL = 2;//1 手动模式 2自动
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    updateProgress();
                    break;
            }
        }
    };


    private void updateProgress() {
        mNeedTime--;
        mTime++;
        EventUtils.postEvent(new RecipeNeedTimeEvent(mNeedTime));
        if (mTime == mCurrentTime) {
            if (COOK_MODEL == 2) {
                next();
                EventUtils.postEvent(new RecipeStepEvent(0));
            }
        } else {
            mSeekBar.setProgress(mTime);
            String time = TimeUtils.sec2clock(mNeedTime);
            if (mNeedTime > 0) {
                mTvStepTime.setText("剩余" + time);
            } else {
                mTvStepTime.setText("100%");
            }
        }
    }

    @Subscribe
    public void onEvent(RecipeOrderEvent event) {
        int order = event.order;
        stepSkip(order);
    }

    public RecipeCurrentStepDetailView(Context context, ArrayList<CookStep> cookSteps,
                                       int currentIndex, Recipe recipe, AbsFan fan, Stove stove,
                                       Stove.StoveHead headId, StoveSendCommandUtils commandUtils) {
        super(context);
        mContext = context;
        mRecipe = recipe;
        mCookSteps = cookSteps;
        mTotalStep = mCookSteps.size();
        mCurrentCookStep = cookSteps.get(currentIndex);
        mFan = fan;
        mStove = stove;
        mHeadId = headId;
        mStoveSendCommandUtils = commandUtils;
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_current_step_recipe_detail,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        initStep();
        mStoveSendCommandUtils.setStep(step);
        mStoveSendCommandUtils.setCookStep(mCurrentCookStep);
        mStoveSendCommandUtils.onStart();
        initListener();
        initData();
        startCookTask();
        if (IRokiFamily.IRokiDevicePlat.RQZ02.equals(mStove.getDp())) {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LogUtils.e(TAG, "stove dp:" + mStove.getDp());
                mStoveSendCommandUtils.setStoveLevel(mCurrentCookStep.getParamByCodeName(mStove.getDp(), "stoveGear"));
            }
        }

    }

    //跳转到指定步鄹
    private void stepSkip(final int order) {

        mTime = 0;
        try {
            String cookStepData = PreferenceUtils.getString(("roki" + mRecipe.id), null);
            List<CookStep> cookSteps = JsonUtils.json2List(cookStepData, CookStep.class);
            if (cookSteps == null || cookSteps.size() == 0) return;
            mCurrentCookStep = cookSteps.get(order - 1);
            mTvCurrentStep.setText(order + "");
            mTvSumStep.setText(mTotalStep + "");
            mTvCurrentStepDesc.setText(mCurrentCookStep.desc);
            mNeedTime = mCurrentCookStep.needTime;
            mCurrentTime = mNeedTime;
            String time = TimeUtils.sec2clock(mNeedTime);
            mTvStepTime.setText("剩余" + time);
            mSeekBar.setMax(mNeedTime);
            step = order - 1;
            COOK_MODEL = 2;
            mStoveSendCommandUtils.setStep(step);
            mStoveSendCommandUtils.setCookStep(mCurrentCookStep);
            mStoveSendCommandUtils.onStart();
            isRecipeRun = true;
            initStep();
        } catch (Exception e) {
        }
    }

    //下一步
    public void next() {
        step++;
        mStoveSendCommandUtils.setStep(step);
        COOK_MODEL = 2;
        mRlButtonRight.setText(R.string.cook_stop_text);
        mTvModelSwitcher.setText(R.string.cook_current_step_delayed_text);
        if (null == mTimer || mTimerTask == null) {
            startCookTask();
        }
        if (mCookSteps != null && mCookSteps.size() > 0) {

            if (mCookSteps.size() == 1) {
                finishCooking();
                return;
            } else {
                mCurrentCookStep = mCookSteps.get(1);
                mStoveSendCommandUtils.setCookStep(mCurrentCookStep);
                mStoveSendCommandUtils.onStart();
                isRecipeRun = true;
                initStep();
            }
            initData();
        }

    }

    private void initStep() {
        LogUtils.e("20190416", "dc:::" + mCurrentCookStep.dc);

        if (TextUtils.isEmpty(mCurrentCookStep.dc)) {
            COOK_MODEL = 1;
            isModelSwitch = true;
            mTvModelSwitcher.setText("");
            mRlButtonRight.setText(R.string.cook_next_text);
            EventUtils.postEvent(new RecipeDcEvent(false));
        } else {
            COOK_MODEL = 2;
            isModelSwitch = false;
            mTvModelSwitcher.setText(R.string.cook_current_step_delayed_text);
            mRlButtonRight.setText(R.string.cook_stop_text);
            EventUtils.postEvent(new RecipeDcEvent(true));
        }
    }

    private void startCookTask() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            };
        }
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    private void stopCookTask() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

    }

    private void initListener() {
        mRlButtonLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });
        mRlButtonRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("20190411", "mNeedTime:" + mNeedTime);
                LogUtils.e("20190412", "text:" + mRlButtonRight.getText().toString());
                if (mRlButtonRight.getText().toString().equals(mContext.getString(R.string.cook_stop_text))) {
                    if (!mStove.isConnected()) {
                        ToastUtils.showShort(R.string.device_off_line);
                        return;
                    }
                    mStoveSendCommandUtils.setFanLevel(FanStatus.LEVEL_SMALL);
                    if (IRokiFamily.IRokiDevicePlat.RQZ01.equals(mStove.getDp())) {
                        mStoveSendCommandUtils.setStoveLevel((short) 0);
                    } else {
                        mStoveSendCommandUtils.setStoveLevel((short) 1);
                    }
                    stopCookTask();
                    mRlButtonRight.setText(R.string.cook_continue_text);
                } else if (mRlButtonRight.getText().toString().equals(mContext.getString(R.string.cook_continue_text))) {
                    if (!mStove.isConnected()) {
                        ToastUtils.showShort(R.string.device_off_line);
                        return;
                    }
                    String dp = mStove.getDp();
                    LogUtils.e(TAG, "mStove dp：" + dp + " StoveModel:" + mStove.getStoveModel());
                    int fanlevel = mCurrentCookStep.getParamByCodeName(dp, "fanGear");
                    int stovelevel = mCurrentCookStep.getParamByCodeName(dp, "stoveGear");
                    mStoveSendCommandUtils.setFanLevel(fanlevel);
                    mStoveSendCommandUtils.setStoveLevel(stovelevel);
                    startCookTask();
                    mRlButtonRight.setText(R.string.cook_stop_text);

                } else {
                    nextAndDelayedClick();
                }
            }
        });
        mTvModelSwitcher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COOK_MODEL == 1 && isModelSwitch) {
                    nextAndDelayedClick();
                } else if (COOK_MODEL == 2) {
                    COOK_MODEL = 1;
                    isModelSwitch = true;
                    mTvModelSwitcher.setText("");
                    mRlButtonRight.setText(R.string.cook_next_text);
                }
            }
        });
    }

    private void nextAndDelayedClick() {
        if (!mStove.isConnected()) {
            ToastUtils.showShort(R.string.device_off_line);
            return;
        }
        if (mNeedTime < 0) {
            mNeedTime = 0;
        }
        if (COOK_MODEL == 1 && mNeedTime == 0) {
            next();
            EventUtils.postEvent(new RecipeStepEvent(0));
        } else if (COOK_MODEL == 1 && mNeedTime > 0) {
            final IRokiDialog switcherStepDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_0);
            switcherStepDialog.setContentText(R.string.cook_switcher_step_text);
            switcherStepDialog.setOkBtn(R.string.cook_switcher_text, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switcherStepDialog.dismiss();
                    next();
                    EventUtils.postEvent(new RecipeStepEvent(0));
                }
            });
            switcherStepDialog.setCancelBtn(R.string.cancel, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switcherStepDialog.dismiss();
                }
            });
            switcherStepDialog.show();
        }
    }

    private void initData() {
        mTime = 0;
        mTvCurrentStep.setText(step + 1 + "");
        mTvSumStep.setText(mTotalStep + "");
        mTvCurrentStepDesc.setText(mCurrentCookStep.desc);
        mNeedTime = mCurrentCookStep.needTime;
        mCurrentTime = mNeedTime;
        String time = TimeUtils.sec2clock(mNeedTime);
        mTvStepTime.setText("剩余" + time);
        mSeekBar.setMax(mNeedTime);

    }

    //做菜结束时的逻辑
    private void finishCooking() {
        mStoveSendCommandUtils.onFinish();
        stopCookTask();
        final IRokiDialog cookFinishDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_COOKING_FINISH);
        cookFinishDialog.setContentText(mContext.getString(R.string.cook_ok_finish_text) + '"' + mRecipe.getName() + '"');
        cookFinishDialog.setOkBtn(R.string.cook_know_text, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cookFinishDialog.isShow()) {
                    cookFinishDialog.dismiss();
                    UIService.getInstance().popBack();
                    UIService.getInstance().popBack();
                    UIService.getInstance().popBack();
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
        COOK_MODEL = 2;
        step = 0;
        mCookSteps = null;
        mCurrentCookStep = null;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

}
