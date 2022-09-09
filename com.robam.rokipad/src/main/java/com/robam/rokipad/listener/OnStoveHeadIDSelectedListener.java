package com.robam.rokipad.listener;

import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.device.Stove.Stove;

import java.util.List;

public interface OnStoveHeadIDSelectedListener {
    void onStoveHeadIDSelected(short ihId, List<CookStep> cookStepList);
}
