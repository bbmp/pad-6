package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

public class PreSubStep extends AbsStorePojo<Long> implements Serializable {
    public static final String FOREIGN_COLUMNNAME_ID = "PreStep_ID";
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    @JsonProperty("no")
    public long index;

    @DatabaseField
    @JsonProperty("desc")
    public String desc;

    @DatabaseField
    @JsonProperty("imgUrl")
    public String imageUrl;

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = PreSubStep.FOREIGN_COLUMNNAME_ID)
    protected PreStep preStep;

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return String.valueOf(index);
    }


    public PreStep getParent() {
        return preStep;
    }

    @Override
    public void save2db() {
        super.save2db();
    }

}
