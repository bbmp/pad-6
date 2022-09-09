package com.legent.plat.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

import java.io.Serializable;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/31.
 * PS: 添加设备时要展示的详细信息.
 */
public class Pd extends AbsPojo implements Serializable{

    @JsonProperty("dt")
    public String dt;

    @JsonProperty("displayType")
    public String displayType;

    @JsonProperty("dp")
    public String dp;

    @JsonProperty("iconUrl")//产品缩略图
    public String iconUrl;

    @JsonProperty("netImgUrl")//联网图片地址
    public String netImgUrl;

    @JsonProperty("netTips")//联网说明文字
    public String netTips;

}
