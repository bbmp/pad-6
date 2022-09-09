package com.robam.common.services;

import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.util.StoveSendCommandUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by as on 2017-04-07.
 */

public interface IAbsCookTaskInterface {
    boolean isRunning();

    boolean isPause();

    int getStepCount();

    int getStepIndex();

    int getRemainTime();

    void start(Stove.StoveHead stoveHead, Recipe book);

    void start(Stove.StoveHead stoveHead, List<CookStep> steps, Long id, StoveSendCommandUtils mStoveSendCommandUtils);

    void next();

    void stop();
}
