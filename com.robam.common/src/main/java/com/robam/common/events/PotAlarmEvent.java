package com.robam.common.events;

import com.robam.common.pojos.device.Pot.Pot;

/**
 * Created by robam on 2018/8/31.
 */

public class PotAlarmEvent {

    public int alarmId;

    public PotAlarmEvent(Pot pot, int alarmId) {
        this.alarmId = alarmId;
    }
}
