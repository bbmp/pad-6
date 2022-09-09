package com.robam.rokipad.service;

import android.content.Context;
import android.os.CountDownTimer;

import com.legent.VoidCallback;
import com.legent.plat.constant.IDeviceDp;
import com.legent.services.AbsService;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.paramCode;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.rokipad.ui.page.StoveCommand;
import com.robam.rokipad.utils.CallBackCommand;
import com.robam.rokipad.utils.IStoveSendCommand;

import java.util.LinkedList;
import java.util.List;


public class ConvenientCookService extends AbsService {

    private static final String TAG = "ConvenientCookService";
    public static ConvenientCookService mInstance;
    private Recipe recipe;
    private List<CookStep> cookSteps;
    private short stoveLevel;
    private short needTime;
    private Stove[] stoveList;
    private short stoveHeadihId;
    private Context context;
    private IStoveSendCommand stoveCom = null;
    private String platCode = IDeviceDp.RQZ05;
    private CountDownTimer countDownTimer;
    private long cookTotalTime = 0;
    private LinkedList<CookStep> cookStepLinkedList;
    private boolean isConvenientCook = false;
    private long downTime = 0;//倒计时时间


    public long getCountDownTime() {
        return downTime;
    }

    public short getStoveHeadihId() {
        return stoveHeadihId;
    }

    public static ConvenientCookService getInstance() {

        if (mInstance == null) {
            synchronized (ConvenientCookService.class) {
                if (mInstance == null) {
                    mInstance = new ConvenientCookService();
                }
            }
        }
        return mInstance;
    }

    public void start(Context context, final Stove.StoveHead stoveHead, Recipe recipe) {
        this.context = context;
        this.stoveCom = new StoveCommand(context);
        this.recipe = recipe;
        this.cookSteps = recipe.js_cookSteps;
        this.stoveList = Utils.getDefaultStove();
        this.stoveHeadihId = stoveHead.ihId;
        this.platCode = stoveList[0].getDp();
        this.cookStepLinkedList = new LinkedList<>();
        for (CookStep cookStep : cookSteps) {
            needTime = (short) cookStep.getParamByCodeName(platCode, paramCode.NEED_TIME);
            cookTotalTime = cookTotalTime + needTime;
            cookStepLinkedList.add(cookStep);
        }
        LogUtils.e(TAG, "cookTotalTime:" + cookTotalTime);
        countDownTimer = new CountDownTimer(cookTotalTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                downTime = millisUntilFinished / 1000;
                if (!cookStepLinkedList.isEmpty()) {
                    CookStep cookStep = cookStepLinkedList.getFirst();
                    isConvenientCook = true;
                    long stepTime = cookStep.getParamByCodeName(platCode, paramCode.NEED_TIME) * 1000;
                    if (cookTotalTime * 1000 - millisUntilFinished - stepTime > 0) {
                        cookStepLinkedList.removeFirst();
                        if (!cookStepLinkedList.isEmpty()) {
                            cookStep = cookStepLinkedList.getFirst();
                            stoveLevel = (short) cookStep.getParamByCodeName(platCode, paramCode.STOVE_LEVEL);
                            setStoveLevel(stoveLevel);
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                stoveCom.setPower(stoveHeadihId, new CallBackCommand() {
                    @Override
                    public void success() {
                        LogUtils.e(TAG, "onFinish power ");
                    }

                    @Override
                    public void fail(Throwable t) {
                        LogUtils.e(TAG, "fail " + t.toString());
                    }
                });
                cookTotalTime = 0;
                isConvenientCook = false;
            }
        };
        cookStepCountDown(countDownTimer);
    }

    public boolean isConvenientCook() {
        return isConvenientCook;
    }

    private void setStoveLevel(short stoveLevel) {
        stoveList[0].setStoveLevel(false, stoveHeadihId, stoveLevel, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.e(TAG, "onSuccess stoveLevel:");
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.e(TAG, "onFailure stoveLevel:" + t.toString());
            }
        });
    }


    private void cookStepCountDown(CountDownTimer countDownTimer) {
        stoveLevel = (short) cookStepLinkedList.getFirst().getParamByCodeName(platCode, paramCode.STOVE_LEVEL);
        setStoveLevel(stoveLevel);
        countDownTimer.start();
    }

    public String getCookRecipeName() {
        return recipe.name;
    }

    public void stop() {
        if (countDownTimer != null) {
            LogUtils.e(TAG, "stop countDownTimer");
            countDownTimer.cancel();
            stoveCom.setPower(stoveHeadihId, new CallBackCommand() {
                @Override
                public void success() {
                    LogUtils.e(TAG, "onFinish power ");
                }

                @Override
                public void fail(Throwable t) {
                    LogUtils.e(TAG, "fail " + t.toString());
                }
            });
            isConvenientCook = false;
            cookTotalTime = 0;
        }
    }
}
