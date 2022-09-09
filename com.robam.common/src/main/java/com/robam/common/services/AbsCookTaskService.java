package com.robam.common.services;

import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.services.AbsService;
import com.legent.services.TaskService;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.R;
import com.robam.common.Utils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.Stove.StoveHead;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.util.StoveSendCommandUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.robam.common.Utils.getDefaultStove;


abstract public class AbsCookTaskService extends AbsService implements IAbsCookTaskInterface {
    private static final String TAG = "AbsCookTaskService";
    protected AbsFan fan;
    protected Stove stove;
    protected StoveHead stoveHead;
    protected Recipe book;
    protected List<CookStep> steps;
    protected long startTime, endTime;

    protected CookStep step;
    protected boolean isRunning, isPause;
    protected int stepIndex, remainTime;
    protected ScheduledFuture<?> future;
    protected Long recipeId;
    protected StoveSendCommandUtils stoveUtils;

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public boolean isPause() {
        return isPause;
    }

    @Override
    public int getStepCount() {
        return steps != null ? steps.size() : 0;
    }

    @Override
    public int getStepIndex() {
        return stepIndex;
    }

    @Override
    public int getRemainTime() {
        return remainTime;
    }


    /**
     * 启动烧菜
     */
    @Override
    public void start(StoveHead stoveHead, List<CookStep> steps, Long id, StoveSendCommandUtils stoveUtils) {
        if (isRunning)
            return;
        this.stoveHead = stoveHead;
        this.startTime = Calendar.getInstance().getTimeInMillis();
        this.steps = steps;
        this.recipeId = id;
        if (stoveHead != null) {
            stove = stoveHead.parent;
            fan = (AbsFan) stove.getParent();
        } else {
            fan = Utils.getDefaultFan();
        }
        stepIndex = -1;
        isRunning = true;
        this.stoveUtils = stoveUtils;
        next();
        onShowCookingView();
//          onStart();
    }

    /**
     * 启动烧菜
     *
     * @param book
     */
    @Override
    public void start(StoveHead stoveHead, Recipe book) {
        if (isRunning) {
            ToastUtils.showShort(R.string.no_repate_cooking);
            return;
        }

        this.book = book;
        this.stoveHead = stoveHead;
        this.startTime = Calendar.getInstance().getTimeInMillis();

        if (stoveHead != null) {
            stove = stoveHead.parent;
            fan = (AbsFan) stove.getParent();
        } else {
            fan = Utils.getDefaultFan();
        }

        steps = book.getJs_cookSteps();
        stepIndex = -1;
        isRunning = true;
        next();
        onShowCookingView();
//        onStart();
    }

    public void setStep(int pos) {
        LogUtils.e(TAG, "pos:" + pos);
        this.stepIndex = pos;
        stepIndex -= 1;
        next();

    }

    abstract protected void onStart();

    /**
     * 执行下一步工序
     *
     * @return
     */
    @Override
    public void next() {
        //是否最后一个步骤
        boolean isLastStep = stepIndex + 1 == steps.size();
        if (isLastStep) {
            //是否仍在倒计时
            boolean isCountdown = remainTime > 0;
            if (isCountdown) {
                // 仍在倒计时
//                onAskAtEnd();
            } else {
                // 倒计时完成
                stepIndex++;
                stop();
            }
        } else {
            // 中间步骤
            stepIndex++;
            String cookStepData = PreferenceUtils.getString(("roki" + recipeId), null);
            LogUtils.e(TAG,"cookStepData:"+cookStepData);
            steps = JsonUtils.json2List(cookStepData, CookStep.class);
            step = steps.get(stepIndex);
            LogUtils.e(TAG, "step:" + step + " stepIndex:" + stepIndex);
            setCommand(step);
            onNext();
        }
    }


    abstract protected void onNext();

    /**
     * 烧菜完成
     */
    @Override
    public void stop() {

        this.endTime = Calendar.getInstance().getTimeInMillis();
        setFanLevel(0);
        setStoveStatus(StoveStatus.Off, null);

        stopCountdown();
        onStoped();

        this.stove = null;
        this.book = null;
        this.stepIndex = -1;
        isRunning = false;
        isPause = false;

    }

    protected void onStoped() {
        if (fan != null && book != null) {
            boolean isLastStep = stepIndex + 1 >= steps.size();
            boolean isNotCountdown = remainTime <= 0;
            boolean isBroken = !(isLastStep && isNotCountdown);

            CookbookManager.getInstance().addCookingLog(fan.getID(), book, startTime, endTime, isBroken, new VoidCallback() {

                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    /**
     * 下发控制指令并计时
     */
    protected void setCommand(CookStep step) {
        setStepParams(step);
        LogUtils.e("20190610", "dp:" + stove.getDp());
        if (null != getDefaultStove()) {
            if (stove != null) {
                startCountdown(step.getParamByCodeName(stove.getDp(), "needTime"));
            }
        }
    }

    /*************************
     * 计时控制 开和关
     *************************/

    abstract protected void startCountdown(final int needTime);

    protected void stopCountdown() {
        if (!isRunning)
            return;

        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }
    // -----------------------------------------------------------------------------------------------------


    /*************************
     * 设备控制
     *********************************/
    private void setStepParams(final CookStep step) {

        TaskService.getInstance().postUiTask(new Runnable() {

            @Override
            public void run() {
                try {
                    stoveUtils.setCookStep(step);
                    stoveUtils.setStep(stepIndex);
                    stoveUtils.onStart();
                } catch (Exception e) {
                    if (Plat.DEBUG)
                        LogUtils.i("20170407", "AbsCookTaskService postUiTask" + e.getMessage());
                }

            }
        }, 500);

    }

    public void stopStep(int stepIndex) {
        setStoveLevel(0);
    }

    /**
     * 失败次数
     */
    protected short failureNum;

    /**
     * 设置烟机档位
     *
     * @param level
     */
    protected void setFanLevel(final int level) {

        if (fan != null) {
            fan.setFanLevel((short) level, new VoidCallback() {
                @Override
                public void onSuccess() {
                    failureNum = 0;
                }

                @Override
                public void onFailure(Throwable t) {
                    if (failureNum > 3) {
                        failureNum = 0;
                        return;
                    }
                    failureNum++;
                    setFanLevel(level);
                }
            });
        }
    }

    /**
     * 设置灶具状态
     *
     * @param status
     */
    protected void setStoveStatus(final int status, final VoidCallback callback) {
        if (stove != null && stoveHead != null) {
            stove.setStoveStatus(true, stoveHead.ihId, (short) status, new VoidCallback() {
                @Override
                public void onSuccess() {
                    failureNum = 0;
                    if (callback != null)
                        callback.onSuccess();
                }

                @Override
                public void onFailure(Throwable t) {
                    if (failureNum > 5) {
                        failureNum = 0;
                        if (callback != null)
                            callback.onFailure(t);
                        return;
                    }
                    failureNum++;
                    setStoveStatus(status, callback);
                }
            });
        }
    }

    protected void setStoveLevel(final int level) {

        if (stove != null && stoveHead != null) {
            if (level == 0 && IRokiFamily.R9B39.equals(stove.getDeviceType().getID())) {
                short status = StoveStatus.Off;
                setStoveStatus(status, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i("startCooking", "setStoveStatus success...");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("startCooking", "setStoveStatus fail...");
                    }
                });
                return;
            }

            if (stoveHead.status == StoveStatus.Off) {
                short status = StoveStatus.StandyBy;
                if (IRokiFamily.R9B39.equals(stove.getDeviceType().getID()) || IRokiFamily._9B39E.equals(stove.getDeviceType().getID())) {
                    status = StoveStatus.Working;
                }
                setStoveStatus(status, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (IRokiFamily.R9B39.equals(stove.getDeviceType().getID())) {
                            try {
                                Thread.currentThread().sleep(5600);
                            } catch (Exception e) {
                                e.printStackTrace();
                                setStoveLevel2(level);
                            }
                        }
                        setStoveLevel2(level);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            } else {
                setStoveLevel2(level);
            }
        }
    }

    /**
     * 下发灶具档位
     *
     * @param level
     */
    private void setStoveLevel2(final int level) {
        if (stove == null || stoveHead == null)
            return;
        if (false && !stove.getStoveModel().equals(IRokiFamily.R9W70)) {                                                       //燃气灶与电磁灶档位关系对应 by zhaiyuanyi 20151128
            if (level == 0 || level == 1) {
                // level = 1;
            } else if (level == 2 || level == 3) {
                //  level = 2;
            } else if (level == 4 || level == 5) {
                // level = 3;
            } else if (level == 6) {
                // level = 4;
            } else if (level == 7 || level == 8 || level == 9) {
                // level = 5;
            }
        }
        if (!stove.isConnected()) {
            ToastUtils.show("灶具已离线");
        }

        TaskService.getInstance().scheduleTaskAfterDelay(new Runnable() {
            @Override
            public void run() {

                stove.setStoveLevel(true, stoveHead.ihId, (short) level, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        failureNum = 0;
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (failureNum > 5) {
                            failureNum = 0;
                            return;
                        }
                        try {
                            Thread.sleep(4000);
                            failureNum++;
                            setStoveLevel2(level);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }, 1, TimeUnit.SECONDS);


    }

    protected void onShowCookingView() {
        // TODO
    }


//    protected void onAskAtEnd() {
//        Context cx = UIService.getInstance().getTop().getActivity();
//        String message = "已到最后一步,是否退出？";
//        DialogHelper.newDialog_OkCancel(cx, null, message,
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dlg, int witch) {
//                        if (witch == DialogInterface.BUTTON_POSITIVE) {
//                            stop();
//                        }
//                    }
//                }).show();
//    }

}
