package com.robam.common.services;

import com.legent.VoidCallback;
import com.legent.services.TaskService;
import com.legent.utils.EventUtils;
import com.robam.common.events.CookCountdownEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;

import java.util.concurrent.TimeUnit;

/**
 * Created by as on 2017-04-07.
 */

public abstract class StoveCookTaskService extends AbsCookTaskService implements StoveCookTaskInterface {
    private static final String TAG = "StoveCookTaskService";




    /**
     * 启动烧菜
     *
     * @param
     */
    @Override
    public void onStart() {


    }

    /**
     * 执行下一步工序
     *
     * @return
     */
    @Override
    public void onNext() {

    }

    /**
     * 暂停
     */
    @Override
    public void pause(VoidCallback callback) {
        if (isPause)
            return;
        if (isRunning) {
            stopCountdown();
        }

        if (fan != null) {
            fan.pause();
        }
        if (stoveHead != null) {
            stoveHead.pause(callback);
        }
        onPause();
    }

    @Override
    public void pause() {
        pause(null);
    }

    @Override
    public void onPause() {
        isPause = true;
    }

    /**
     * 从暂停恢复
     */
    @Override
    public void restore(VoidCallback callback) {

        if (!isPause)
            return;

        if (isRunning) {
            startCountdown(remainTime);
        }

        if (fan != null) {
            fan.restore();
        }
        if (stoveHead != null) {
            stoveHead.restore(callback);
        }

        onRestore();
    }

    /**
     * 从暂停恢复
     */
    @Override
    public void restore() {
        restore(null);
    }

    @Override
    public void onRestore() {
        isPause = false;
    }

    /**
     * 执行上一步工序
     *
     * @return
     */
    @Override
    public void back() {
        if (stepIndex == 0)
            return;

        stepIndex--;

        CookStep step = steps.get(stepIndex);
        setCommand(step);
    }

    @Override
    public void onBack() {
    }

    protected void startCountdown(final int needTime) {
        if (!isRunning)
            return;
        stopCountdown();
        remainTime = needTime;
        future = TaskService.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                remainTime--;
                onRunning(remainTime);

                if (remainTime <= 0) {
                    stopCountdown();
                }
            }
        }, 500, 1000, TimeUnit.MILLISECONDS);
    }

    protected void onRunning(final int count) {
        EventUtils.postEvent(new CookCountdownEvent(stepIndex, count));
        if (count <= 0) {
            if (stove == null || stoveHead == null)
                return;
            if (stoveHead.status == StoveStatus.StandyBy
                    || stoveHead.status == StoveStatus.Off
                    || stoveHead.level == Stove.PowerLevel_0) {
                return;
            }
            setStoveLevel(Stove.PowerLevel_1);
        }
        isPause = false;
    }

}
