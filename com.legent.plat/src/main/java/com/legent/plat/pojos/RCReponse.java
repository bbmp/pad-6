package com.legent.plat.pojos;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.plat.services.ResultCodeManager;
import com.legent.utils.LogUtils;

/**
 * Created by sylar on 15/7/23.
 */
public class RCReponse extends AbsPostResponse {


    @JsonProperty("rc")
    public int rc;

    public boolean isSuccess() {
        return ResultCodeManager.isSuccessRC(rc);
    }


}
