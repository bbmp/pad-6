package com.robam.common.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;

import java.util.ArrayList;

public class StoveSendCommandUtils implements ISendCommand {
    private static final String TAG = "StoveSendCommandUtils";

    protected AbsFan fan;
    protected Stove stove;
    protected Stove.StoveHead stoveHead;
    private ArrayList<CookStep> cookSteps;
    public int step;
    private CookStep cookStep;
    private ListenerLevel mListenerLevel;

    public StoveSendCommandUtils(AbsFan fan, Stove stove,
                                 Stove.StoveHead stoveHead,
                                 ArrayList<CookStep> cookSteps,
                                 int step) {
        this.fan = fan;
        this.stove = stove;
        this.stoveHead = stoveHead;
        this.step = step;
        this.cookSteps = cookSteps;

    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setCookStep(CookStep cookStep) {
        this.cookStep = cookStep;
    }

    @Override
    public void onStart() {

        String ste = null;
        if (null != stove) {
            ste = stove.getDp();
        }
        final int fanlevel = cookStep.getParamByCodeName(ste, "fanGear");
        final int stovelevel = cookStep.getParamByCodeName(ste, "stoveGear");
        LogUtils.e(TAG, "fanlevel:" + fanlevel + " stovelevel:" + stovelevel);
        setFanLevel(fanlevel);
        String dc = cookStep.dc;

        if (mListenerLevel != null) {
            mListenerLevel.setStepImgUrl(cookStep.imageUrl);
        }

        if (null != stove)
            LogUtils.e("20190416", "dc:" + dc);
        if (TextUtils.isEmpty(dc)) {
            setStoveStatus(StoveStatus.Off, null);
            setFanLevel(0);
        }

        if (IRokiFamily.IRokiDevicePlat.RQZ02.equals(ste)) {
            short status = stoveHead.status;
            if (StoveStatus.Off == status) {
                setStoveStatus(StoveStatus.Working, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (mListenerLevel != null) {
                            mListenerLevel.stoveLevel(stovelevel);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            } else {
                setStoveLevel(stovelevel);
            }
        } else {
            setStoveLevel(stovelevel);
        }


    }

    @Override
    public void onPause() {

    }

    @Override
    public void onPreSend() {

    }

    @Override
    public void onFinish() {
        setFanLevel(0);
        setStoveStatus(StoveStatus.Off, null);
    }

    @Override
    public void onRestart() {

    }

    public void setFanLevel(final int level) {
        if (fan != null) {
            if (!fan.isConnected()) {
                ToastUtils.show("烟机已离线");
                return;
            }
            fan.setFanLevel((short) level, new VoidCallback() {
                @Override
                public void onSuccess() {
                    if (mListenerLevel != null) {
                        mListenerLevel.fanLevel(level);
                    }
                    LogUtils.i("isLastStep", "下发成功 fanlevel:" + level);
                    failureNum = 0;
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("isLastStep", "下发失败 :" + t.toString());
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

    protected short failureNum;

    public void setStoveLevel(final int level) {
        LogUtils.e("20190416", "setStoveLevel:" + level);
        if (stove != null && !stove.isConnected())
            ToastUtils.show("灶具已离线");
        if (stove != null && stoveHead != null) {
            stove.setStoveLevel(true, stoveHead.ihId, (short) level, new VoidCallback() {
                @Override
                public void onSuccess() {
                    failureNum = 0;
                    if (mListenerLevel != null) {
                        LogUtils.e("20190416", "onSuccess stove_level:" + level);
                        mListenerLevel.stoveLevel(level);
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    if (failureNum > 3) {
                        failureNum = 0;
                        return;
                    }
                    failureNum++;
                    setStoveLevel(level);
                }
            });
        }
    }

    protected void setStoveStatus(final int status, final VoidCallback callback) {
        LogUtils.e("20190416", "status::" + status);
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

    public interface ListenerLevel {

        void stoveLevel(int level);

        void fanLevel(int level);

        void setStepImgUrl(String imgUrl);

    }

    public void setListenerLevel(ListenerLevel listenerLevel) {
        mListenerLevel = listenerLevel;
    }

}
