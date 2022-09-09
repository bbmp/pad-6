package com.robam.common.pojos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by r on 2016/12/29.
 */

public class DrinkingItem  implements Serializable {
    @JsonProperty("hasPrevious")
    public Boolean hasPrevious;

    @JsonProperty("hasNext")
    public Boolean hasNext;

    @JsonProperty("data")
    public ArrayList<DataInfo> dataInfo= Lists.newArrayList();

    @Override
     public String toString() {
      return "DrinkingItem{" +
            "hasPrevious=" + hasPrevious +
            ", hasNext=" + hasNext +
            ", dataInfo=" + dataInfo +
            '}';
  }
}
