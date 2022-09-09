package com.legent.plat.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dell on 2019/1/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayLoad {
    @JsonProperty("password")
    public String password;

    @JsonProperty("account")
    public String account;
}
