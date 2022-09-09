package com.robam.rokipad.service;

import com.legent.plat.Plat;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.services.RuleCookTaskService;

/**
 * Created by Dell on 2018/5/22.
 */

public class PadPotCookTaskService extends RuleCookTaskService {



    static private PadPotCookTaskService instance = new PadPotCookTaskService();

    static public PadPotCookTaskService getInstance() {
        if (instance == null) {
            synchronized (PadPotCookTaskService.class) {
                if (instance == null) {
                    instance = new PadPotCookTaskService();
                }
            }
        }
        return instance;
    }

    @Override
    protected void onStart() {

    }

    protected PadPotCookTaskService() {
        SpeechManager.getInstance().init(Plat.app);
    }


    @Override
    public void dispose() {
        super.dispose();
        SpeechManager.getInstance().dispose();
    }
}
