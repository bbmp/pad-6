
package com.dome.alibaba_picdetalg.rule;

import java.io.Serializable;


public class TimeRuleConfig implements Serializable{
	private long baseTime;//seconds
	private long addTimePerStep;
	public TimeRuleConfig(){
		baseTime=60*30;
		addTimePerStep=3;
	}
	public long getAddTimePerStep() {
		return addTimePerStep;
	}
	public void setAddTimePerStep(long addTimePerStep) {
		this.addTimePerStep = addTimePerStep;
	}
	public long getBaseTime() {
		return baseTime;
	}
	public void setBaseTime(long baseTime) {
		this.baseTime = baseTime;
	}
}
