package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dell on 2019/1/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultBean {
    @JsonProperty("id")
    public long id;


    @JsonProperty("name")
    public String name;

    /**
     * 小图
     */
    @JsonProperty("imgSmall")
    public String imgSmall;

    /**
     * 中图
     */
    @JsonProperty("imgMedium")
    public String imgMedium;



    @JsonProperty("collectCount")
    public int collectCount;

    @JsonProperty("providerImage")
    public String providerImage;

    @JsonProperty("type")
    public String type;
}
