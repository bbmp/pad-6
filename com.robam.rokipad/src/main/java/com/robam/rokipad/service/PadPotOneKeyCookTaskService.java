package com.robam.rokipad.service;

import com.legent.plat.Plat;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.pojos.device.Stove.StoveStatus;

/**
 * Created by Dell on 2018/5/22.
 */

public class PadPotOneKeyCookTaskService extends PadPotCookTaskService {
    static private PadPotOneKeyCookTaskService instance = new PadPotOneKeyCookTaskService();

    static public PadPotOneKeyCookTaskService getInstance() {

        if (instance == null) {
            synchronized (PadPotOneKeyCookTaskService.class) {
                if (instance == null) {
                    instance = new PadPotOneKeyCookTaskService();
                }
            }
        }
        return instance;
    }

    @Override
    protected void onShowCookingView() {
    }

    private PadPotOneKeyCookTaskService() {
        SpeechManager.getInstance().init(Plat.app);
    }


    @Override
    public void stop() {
        setFanLevel(0);
        setStoveStatus(StoveStatus.Off, null);
        stopCountdown();
        this.stove = null;
        this.book = null;
        this.step = null;
        this.stepIndex = -1;
        isRunning = false;
        isPause = false;
    }

    @Override
    public void stopCountdown() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        SpeechManager.getInstance().dispose();
    }


}
