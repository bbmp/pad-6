package com.robam.common.events;

import com.legent.events.AbsEvent;
import com.legent.plat.pojos.device.AbsDevice;

/**
 * Created by WZTCM on 2016/1/11.
 */
public class PopWindwonDismissEvent extends AbsEvent<AbsDevice> {
    public PopWindwonDismissEvent(AbsDevice pojo) {
        super(pojo);
    }
}
