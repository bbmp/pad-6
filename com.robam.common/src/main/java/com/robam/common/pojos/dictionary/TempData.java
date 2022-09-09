package com.robam.common.pojos.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by 14807 on 2019/8/16.
 * PS:
 */

public class TempData {

    @JsonProperty("onLin")
    private List<Tips> tipsList;

    public List<Tips> getTipsList() {
        return tipsList;
    }
}
