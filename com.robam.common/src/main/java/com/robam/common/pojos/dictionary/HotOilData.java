package com.robam.common.pojos.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robam.common.pojos.HotOil;

import java.util.List;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/4/22.
 * PS: Not easy to write code, please indicate.
 */
public class HotOilData {

    public HotOilData() {

    }

    @JsonProperty("hotoils")
    public List<HotOil> hotOils;

}
