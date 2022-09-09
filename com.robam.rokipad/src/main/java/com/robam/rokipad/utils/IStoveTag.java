package com.robam.rokipad.utils;

/**
 * Created by yinwei on 2019/2/22.
 */
//定义stove的tag用于回调处理
public interface IStoveTag {
    String add = "add";
    String decrease = "decrease";
    String time = "time";
    String time_cancel = "time_cancel";
    String power = "power";
    String head_work_time="head_work_time";
    String STOVE_MIN_LEVEL ="Stove_MinLevel";
    String STOVE_MAX_LEVEL="Stove_MaxLevel";
}
