package com.legent.plat.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.pojos.AbsPojo;

import java.util.List;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/31.
 * PS: 添加设备列表.
 */
public class DeviceList extends AbsPojo{

    @JsonProperty("vendor")
    public String vendor;

    @JsonProperty("dc")
    public String dc;

    @JsonProperty("pds")
    public List<Pd> pds;
}
