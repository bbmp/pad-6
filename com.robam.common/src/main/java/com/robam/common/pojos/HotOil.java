package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/4/22.
 * PS: 热油属性.
 */
public class HotOil {

    @JsonProperty("model")
    public int model;
    @JsonProperty("name")
    public String name;
    @JsonProperty("temp_interval")
    public String temp_interval;
    @JsonProperty("health_temp_low")
    public int health_temp_low;
    @JsonProperty("health_temp_high")
    public int health_temp_high;
    @JsonProperty("warm_temp")
    public int warm_temp;

}
