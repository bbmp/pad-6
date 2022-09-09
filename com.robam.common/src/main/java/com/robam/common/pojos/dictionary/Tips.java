package com.robam.common.pojos.dictionary;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by 14807 on 2019/8/16.
 * PS:无人锅烹饪小贴士
 */
public class Tips {

    @JsonProperty("tempeRange")
    private String tempeRange;

    @JsonProperty("statue")
    private String statue;

    @JsonProperty("tips")
    private String tips;

    public String getTempeRange() {
        return tempeRange;
    }

    public String getStatue() {
        return statue;
    }

    public String getTips() {
        return tips;
    }
}
