package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by 14807 on 2018/4/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class History {

    @JsonProperty("score")
    public int score;

    @JsonProperty("value")
    public String value;
}
