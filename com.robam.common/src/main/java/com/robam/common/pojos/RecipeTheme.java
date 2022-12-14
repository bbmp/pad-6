package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

/**
 * Created by as on 2016/8/2.
 */

public class RecipeTheme extends AbsStorePojo<Long> implements Serializable {
    protected final static String COLUMN_ID = "generatedId";
    @DatabaseField(generatedId = true)
    public Long generatedId;

    @DatabaseField()
    @JsonProperty("id")
    public Long id;

    @DatabaseField
    @JsonProperty("name")
    public String name;

    @DatabaseField
    @JsonProperty("subName")
    public String subName;

    @DatabaseField
    @JsonProperty("type")
    public String type;

    @DatabaseField
    @JsonProperty("imageUrl")
    public String imageUrl;

    @DatabaseField
    @JsonProperty("activity")
    public String activity;

    @DatabaseField
    @JsonProperty("insertTimeDate")
    public String insertTimeDate;

    @DatabaseField
    @JsonProperty("relateCookbookId")
    public String relateCookbookId;

    @DatabaseField
    @JsonProperty("attribute1")
    public String attribute1;

    @DatabaseField
    @JsonProperty("attribute2")
    public String attribute2;

    @DatabaseField
    @JsonProperty("sortNo")
    public int sortNo;

    @DatabaseField
    @JsonProperty("description")
    public String description;

    @DatabaseField
    @JsonProperty("isShow")
    public int isShow;

    @DatabaseField
    @JsonProperty("topFlag")
    public int topFlag;


    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "RecipeTheme{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subname='" + subName + '\'' +
                ", type='" + type + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", activity='" + activity + '\'' +
                ", insertTime='" + insertTimeDate + '\'' +
                ", relateCookbookId='" + relateCookbookId + '\'' +
                ", attribute1='" + attribute1 + '\'' +
                ", attribute2='" + attribute2 + '\'' +
                ", sortNo=" + sortNo +
                ", description='" + description + '\'' +
                '}';
    }
}
