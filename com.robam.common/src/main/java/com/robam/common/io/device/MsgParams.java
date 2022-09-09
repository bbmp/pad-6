package com.robam.common.io.device;

import com.robam.common.pojos.device.Sterilizer.SteriStatus;

import java.util.Set;

/**
 *
 */
public interface MsgParams {
    /**
     * 回应码 1B 0-成功，1-失败
     */
    String RC = "RC";
    String Key = "KEY";
    String Length = "LENGTH";
    String Argument = "Argument";

    /**
     * • 控制端类型[1Byte]，参考编码表
     */
    String TerminalType = "TerminalType";

    /**
     * 设备类型编码
     */
    String equipmentCoding = "equipmentCoding";

    /**
     * 用户编码[10Byte]
     */
    String UserId = "UserId";

    /**
     * • 是否菜谱烧菜[1Byte]，0不是，1是
     */
    String IsCook = "IsCook";

    /**
     * 报警码[1Byte]
     */
    String AlarmId = "AlarmId";

    /**
     * 事件码[1Byte]
     */
    String EventId = "EventId";

    /**
     * 事件参数 1Byte
     */
    String EventParam = "EventParam";

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    /**
     * 童锁状态[1Byte]，{0：解锁，1上锁}
     */
    String StoveLock = "StoveLock";

    /**
     * 炉头ID[1Byte]（0左，1右）
     */
    String IhId = "IhId";

    /**
     * • 炉头Num[1Byte], 炉头的数量
     */
    String IhNum = "IhNum";

    /**
     * 炉头信息列表，数量=IhNum
     */
    String StoveHeadList = "StoveHeadList";

    /**
     * 炉头工作状态[1Byte]（0关，1待机，2工作中）
     */
    String IhStatus = "IhStatus";

    /**
     * 炉头功率等级[1Byte]（0-9档）
     */
    String IhLevel = "IhLevel";

    /**
     * 炉头定时关机时间[2BYTE]（0-6000，单位：秒）
     */
    String IhTime = "IhTime";

    /**
     * 炉头连续工作时间[2BYTE]（0-6000，单位：秒）
     */
    String WorkTime = "Worktime";
    /**
     * 菜谱ID [2BYTE]
     */
    String RecipeID = "RecipeID";
    /**
     * 菜谱步骤[1BYTE]
     */
    String RecipeStep = "RecipeStep";


    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    /**
     * 烟机电磁灶开关联动 联动参数:灶具开，烟机开（0关，1开） 1B
     */
    String IsPowerLinkage = "IsPowerLinkage";

    /**
     * 烟机档位联动开关 联动参数:烟机档位是否联动（0关，1开） 1B
     */
    String IsLevelLinkage = "IsLevelLinkage";

    /**
     * 电磁灶关机后烟机延时关机 联动参数:电磁灶关机后，烟机是否延时关机（0关，1开） 1B
     */
    String IsShutdownLinkage = "IsShutdownLinkage";

    /**
     * 电磁灶关机后烟机延时关机时间 联动参数:电磁灶关机后烟机延时关机时间（延时时间，单位分钟，1~5分钟） 1B
     */
    String ShutdownDelay = "ShutdownDelay";

    /**
     * 灶具传过来的2个温度参数
     */
    String Pot_Temp = "Pot_Temp";


    String Pot_keybood = "Pot_keybood";


    /**
     * 油烟机清洗提示开关 [1Byte], 0不提示，1提示
     */
    String IsNoticClean = "IsNoticClean";

    /**
     * 是否开启定时通风[1BYTE]
     */
    String IsTimingVentilation = "IsTimingVentilation";

    /**
     * 定时通风间隔时间[1BYTE],单位天
     */
    String TimingVentilationPeriod = "TimingVentilationPeriod";


    /**
     * 是否开启每周通风[1BYTE]
     */
    String IsWeeklyVentilation = "IsWeeklyVentilation";

    /**
     * 每周通风的时间--周几
     */
    String WeeklyVentilationDate_Week = "WeeklyVentilationDate_Week";

    /**
     * 每周通风的时间--小时
     */
    String WeeklyVentilationDate_Hour = "WeeklyVentilationDate_Hour";

    /**
     * 每周通风的时间--分钟
     */
    String WeeklyVentilationDate_Minute = "WeeklyVentilationDate_Minute";

    String R8230S_Switch = "R8230S_Switch";
    String R8230S_Time = "R8230S_Time";
    String dryBurningPromptSwitch = "dryBurningPromptSwitch";
    String dryBurningSwitch = "dryBurningSwitch";


    /**
     * 功率等级[1Byte]（0、1、2、3、6档）
     */
    String FanLevel = "FanLevel";

    /**
     * 工作状态[1Byte]（0关机，1开机）
     */
    String FanStatus = "FanStatus";

    /**
     * 灯开关［1Byte］（0关，1开）
     */
    String FanLight = "FanLight";

    /**
     * 是否需要清洗［1Byte］（0不需要，1需要）
     */
    String NeedClean = "NeedClean";

    /**
     * 油烟机定时工作 定时时间，[1Byte]（单位：分钟）
     */
    String FanTime = "FanTime";

    /**
     * 油烟机wifi状态 -1未查询到 0断网 1链接路由器 2链接服务器，[1Byte]
     */
    String FanWIfi = "FanWifi";

    /**
     * 防回烟
     */
    String BackSmoke = "BackSmoke";
    //止回阀
    String CheckFan = "CheckFan";
    //等待时间
    String WaitTime = "WaitTime";
    //空气质量检测
    String GasCheck = "GasCheck";
    //是否需要倒油杯
    String IsNeedCupOil = "IsNeedCupOil";
    //智能烟感状态
    String FanFeelStatus = "FanFeelStatus";
    //红外温度上报
    String TemperatureReportOne = "TemperatureReportOne";
    String TemperatureReportTwo = "TemperatureReportTwo";
    //干烧报警
    String BraiseAlarm = "BraiseAlarm";
    //定时通风剩余时间
    String RegularVentilationRemainingTime = "RegularVentilationRemainingTime";
    //烟灶联动通风剩余时间
    String FanStoveLinkageVentilationRemainingTime = "FanStoveLinkageVentilationRemainingTime";
    //定时提醒剩余时间
    String PeriodicallyRemindTheRemainingTime = "PeriodicallyRemindTheRemainingTime";

    //关机（按关机键）剩余时间
    String PresTurnOffRemainingTime = "PresTurnOffRemainingTime";
    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------
    /**
     * 消毒柜工作状态[1Byte]（0关机，1开机）
     */
    String SteriStatus = "SteriStatus";

    /**
     * 设置消毒柜工作时间
     */
    String SteriTime = "SteriTime";

    /**
     * 设置暖碟温度
     */
    String SetSteriTem = "SetSteriTem";

    /**
     * ORDER_TIME[1Byte] {0:预约取消，1，2，3，4…24预约时间}
     */
    String SteriReserveTime = "SteriReserveTime";
    /**
     * DRYING_TIME[1Byte] {0取消烘干，>1 为烘干时间}
     */
    String SteriDryingTime = "SteriDryingTime";
    /**
     * CLEAN_TIME[1Byte] {0取消保洁，60，保洁时间}
     */
    String SteriCleanTime = "SteriCleanTime";
    /**
     * DISINFECT_TIME[1Byte] {0取消消毒,150消毒时间}
     */
    String SteriDisinfectTime = "SteriDisinfectTime";
    /**
     * ON_OFF [1Byte] {0取消童锁，1，开童锁}
     */
    String SteriLock = "SteriLock";

    /**
     * ON_OFF [1Byte] {0取消童锁，1，开童锁}
     */
    String OnOff = "ON_OFF";
    /**
     * WORK_TIME_LEFT{0:无/剩余时间到，>剩余时间} 分钟字节
     */
    String SteriWorkLeftTimeL = "SteriWorkLeftTimeL";
    /**
     * WORK_TIME_LEFT{0:无/剩余时间到，>剩余时间} 小时字节
     */
    String SteriWorkLeftTimeH = "SteriWorkLeftTimeH";
    /**
     * DOORLOCK{0：门锁关，1 门锁关}
     */
    String SteriDoorLock = "SteriDoorLock";
    /**
     * ALARM {0xff:无报警，
     * 0x00:门控报警，
     * 0x01: 紫外线灯管不工作或上层传感器不良
     * 0x02: 温度传感器不良}
     */
    String SteriAlarmStatus = "SteriAlarmStatus";
    /**
     * 是否开启定时消毒[1BYTE]
     */
    String SteriSwitchDisinfect = "SteriSwitchDisinfect";
    /**
     * 定时消毒间隔时间[1BYTE],单位天
     */
    String SteriInternalDisinfect = "SteriInternalDisinfect";
    /**
     * 是否开启每周消毒[1BYTE]
     */
    String SteriSwitchWeekDisinfect = "SteriSwitchWeekDisinfect";
    /**
     * 每周消毒的时时间[1BYTE]
     */
    String SteriWeekInternalDisinfect = "SteriWeekInternalDisinfect";
    /**
     * 消毒柜峰谷电时间[1BYTE]
     */
    String SteriPVDisinfectTime = "SteriPVDisinfectTime";

    String warmDishTempValue = "warmDishTempValue";
    /**
     * 消毒柜参数：
     * TEM[1Byte]  温度值
     * HUM [1Byte]  湿度值
     * GERM [1Byte] 细菌值
     * 臭氧[1Byte]
     */
    String SteriParaTem = "SteriParaTem";
    String SteriParaHum = "SteriParaHum";
    String SteriParaGerm = "SteriParaGerm";
    String SteriParaOzone = "SteriParaOzone";

    // ---------------------------------------------------------------------------------------------
    // 蒸汽炉json参数
    // ---------------------------------------------------------------------------------------------

    /**
     * 童锁状态
     */
    String SteamLock = "SteamLock";
    /**
     * 蒸汽炉工作状态
     */
    String SteamStatus = "SteamStatus";
    String OrderTime = "OrderTime";
    /**
     * 蒸汽炉工作模式
     */
    String SteamMode = "SteamMode";
    /**
     * 蒸汽炉警告
     */
    String SteamAlarm = "SteamAlarm";
    /**
     * 蒸汽炉门阀状态
     */
    String SteamDoorState = "SteamDoorState";
    /**
     * 蒸汽炉工作当前温度和剩余时间
     */
    String SteamTemp = "SteamTemp";
    String SteamTime = "SteamTime";
    /**
     * 蒸汽炉设定时间
     */
    String SteamTempSet = "SteamTempSet";
    String SteamTimeSet = "SteamTimeSet";
    String PreFlag = "preFlag";
    //设置烹饪模式
    String SetMeum = "SetMeum";

    /**
     * 蒸汽炉菜谱id 菜谱步骤
     */
    String SteamRecipeId = "SteamRecipeId";
    String SteamRecipeStep = "SteamRecipeStep";
    String SteamRecipeTotalStep = "SteamRecipeStep";

    String SteamRecipeKey = "SteamRecipeKey";
    String SteamRecipeLength = "SteamRecipeLength";
    String SteamRecipeValue = "SteamRecipeValue";

    String SteamRecipeUniqueKey = "SteamRecipeUniqueKey";
    String SteamRecipeUniqueLength = "SteamRecipeUniqueLength";
    String SteamRecipeUniqueValue = "SteamRecipeUniqueValue";

    String SteamRecipeConcreteKey = "SteamRecipeConcreteKey";
    String SteamRecipeConcreteLength = "SteamRecipeConcreteLength";
    String SteamOvenWaterBoxKey = "SteamOvenWaterBoxKey";
    String SteamOvenWaterBoxLength = "SteamOvenWaterBoxLength";
    String SteamOvenWaterBoxValue = "SteamOvenWaterBoxValue";
    String SteamOvenCurrentStageKey = "SteamOvenCurrentStageKey";
    String SteamOvenCurrentStageLength = "SteamOvenCurrentStageLength";
    String SteamOvenCurrentStageValue = "SteamOvenCurrentStageValue";

    /**
     * 新增蒸箱设备275
     */
    String setSteamModeSendKey = "setSteamModeSendKey";
    String setSteamModeSendLength = "setSteamModeSendLength";
    String setSteamModeSendValue = "setSteamModeSendValue";

    String setSteamTemptureSendKey = "setSteamTemptureSendKey";
    String setSteamTemptureSendLength = "setSteamTemptureSendLength";
    String setSteamTemptureSendValue = "setSteamTemptureSendValue";

    String setSteamTimeSendKey = "setSteamTimeSendKey";
    String setSteamTimeSendLength = "setSteamTimeSendLength";
    String setSteamTimeSendValue = "setSteamTimeSendValue";

    // ------------------------------------------------------------------------
    // 微波炉json参数
    // ------------------------------------------------------------------------
    String MicroWaveStatus = "MicroWaveStatus";
    String MicroWaveMode = "MicroWaveMode";
    String MicroWaveWeight = "MicroWaveWeight";
    String MicroWaveLight = "MicroWaveLight";
    String MicroWavePower = "MicroWavePower";
    String MicroWaveTime = "MicroWaveTime";
    String MicroWaveDoorState = "MicroWaveDoorState";
    String MicroWaveStepState = "MicroWaveStepState";
    String MicroWaveSettime = "MicroWaveSettime";
    String MicroWaveError = "MicroWaveError";
    String MicroWaveRestartNow = "MicroWaveRestartNow";
    String MicroWaveStage = "MicroWaveStage";
    String MicroWaveLinkdMode1 = "MicroWaveLinkdMode1";
    String MicroWaveLinkdMode2 = "MicroWaveLinkdMode2";
    String MicroWaveLinkdMode3 = "MicroWaveLinkdMode3";
    String MicroWaveLinkTime1 = "MicroWaveLinkTime1";
    String MicroWaveLinkTime2 = "MicroWaveLinkTime2";
    String MicroWaveLinkTime3 = "MicroWaveLinkTime3";
    String MicroWaveLinkPower1 = "MicroWaveLinkPower1";
    String MicroWaveLinkPower2 = "MicroWaveLinkPower2";
    String MicroWaveLinkPower3 = "MicroWaveLinkPower3";
    String MicroWaveRecipe = "MicroWaveRecipe";//微波炉菜谱ID
    String MicroRecipeTotalStep = "MicroRecipeTotalStep";
    String MicroRecipeStep = "MicroRecipeStep";//菜谱步骤
    // String ArgumentNumber = "ArgumentNumber";//参数个数
    // ---------------------------------------------------------------------------------------------
    // 烤箱Json参数
    // ---------------------------------------------------------------------------------------------

    /**
     * 烤箱工作状态
     */
    String OvenStatus = "OvenStatus";
    /**
     * 烤箱工作模式
     */
    String OvenMode = "OvenMode";
    /**
     * 烤箱警告
     */
    String OvenAlarm = "OvenAlarm";
    /**
     * 烤箱运行状态
     */
    String OvenRunP = "OvenRunP";
    /**
     * 烤箱烤叉旋转
     */
    String OvenRevolve = "OvenRevolve";
    /**
     * 烤箱灯光
     */
    String OvenLight = "OvenLight";
    /**
     * 烤箱设置温度和设置时间
     */
    String OvenSetTime = "OvenSetTime";
    String OvenSetTemp = "OvenSetTemp";
    /**
     * 烤箱工作当前温度和剩余时间
     */
    String OvenTemp = "OvenTemp";
    String OvenTime = "OvenTime";
    String OvenPreFlag = "OvenPreflag";
    String OvenTempBelow = "OvenTempBelow";
    String OvenTempUp = "OvenTempUp";


    //新增指令by 周定钧
    /**
     * 剩余时间leftTime
     * 烤箱自动模式
     * 菜谱ID[1Byte]
     * 菜谱总步骤数[1Byte]
     * 菜谱步骤[1Byte]
     * 参数个数[1Byte]
     * 设置下温度[1Byte]
     */
    String leftTime = "LeftTime";
    String ovenAutoMode = "ovenAutoMode";
    String ovenLight = "ovenLight";
    String OvenRecipeId = "OvenRecipeId";//菜谱ID
    String OvenRecipeTotalStep = "OvenRecipeTotalStep";
    String OvenRecipeStep = "OvenRecipeStep";//菜谱步骤
    String ArgumentNumber = "ArgumentNumber";//参数个数
    String SetTempDown = "SetTempDown";
    String SetTempDownKey = "SetTempDownKey";
    String SetTempDownLength = "SetTempDownLength";
    String SetTempDownValue = "SetTempDownValue";
    //    String CurrentTempDown = "CurrentTempDown";
    String CurrentTempDownKey = "CurrentTempDownKey";
    String CurrentTempDownLength = "CurrentTempDownLength";
    String CurrentTempDownValue = "CurrentTempDownValue";
    String OrderTime_key = "OrderTime_key";
    String OrderTime_value_min = "OrderTime_value_min";
    String OrderTime_value_hour = "OrderTime_value_hour";
    String OrderTime_length = "OrderTime_length";
    String SteamLight = "SteamLight";
    String HWindTIME = "HWindTIME";//爆炒时间
    String SteameOvenOneWaterbox = "SteameOvenOneWaterbox";
    String CurrentStatusKey = "CurrentStatusKey";
    String CurrentStatusLength = "CurrentStatusLength";
    String CurrentStatusValue = "CurrentStatusValue";
    //新增多段总数
    String TotalKey = "TotalKey";
    String TotalLength = "TotalLength";
    String TotalValue = "TotalValue";

    /**
     * 烤箱新增多段模式字段
     */
    String OvenStagekey = "OvenStagekey";
    String OvenStageLength = "OvenStageLength";
    String OvenStageValue = "OvenStageValue";

    String OvenStep1Modekey = "OvenStep1Modekey";
    String OvenStep1ModeLength = "OvenStep1ModeLength";
    String OvenStep1ModeValue = "OvenStep1ModeValue";

    String OvenStep1SetTempkey = "OvenStep1SetTempkey";
    String OvenStep1SetTempLength = "OvenStep1SetTempLength";
    String OvenStep1SetTempValue = "OvenStep1SetTempValue";

    String OvenStep1SetTimekey = "OvenStep1SetTimekey";
    String OvenStep1SetTimeLength = "OvenStep1SetTimeLength";
    String OvenStep1SetTimeValue = "OvenStep1SetTimeValue";

    String OvenStep2Modekey = "OvenStep2Modekey";
    String OvenStep2ModeLength = "OvenStep2ModeLength";
    String OvenStep2ModeValue = "OvenStep2ModeValue";

    String OvenStep2SetTempkey = "OvenStep2SetTempkey";
    String OvenStep2SetTempLength = "OvenStep2SetTempLength";
    String OvenStep2SetTempValue = "OvenStep2SetTempValue";

    String OvenStep2SetTimekey = "OvenStep2SetTimekey";
    String OvenStep2SetTimeLength = "OvenStep2SetTimeLength";
    String OvenStep2SetTimeValue = "OvenStep2SetTimeValue";

    String OvenStep3Modekey = "OvenStep3Modekey";
    String OvenStep3ModeLength = "OvenStep3ModeLength";
    String OvenStep3ModeValue = "OvenStep3ModeValue";

    String OvenStep3SetTempkey = "OvenStep3SetTempkey";
    String OvenStep3SetTempLength = "OvenStep3SetTempLength";
    String OvenStep3SetTempValue = "OvenStep3SetTempValue";

    String OvenStep3SetTimekey = "OvenStep3SetTimekey";
    String OvenStep3SetTimeLength = "OvenStep3SetTimeLength";
    String OvenStep3SetTimeValue = "OvenStep3SetTimeValue";

    /**
     * 净水器工作状态
     */
    String WaterPurifiyStatus = "WaterPurifiyStatus";
    /**
     * 净水器工作制水水量
     */
    String WaterPurifiyModel = "WaterPurifiyModel";
    /**
     * 净水器工作状态
     */
    String WaterPurifierStatus = "WaterPurifierStatus";
    /**
     * 净水器警报
     */
    String WaterPurifierAlarm = "WaterPurifierAlarm";
    /**
     * 净水器已经工作时间
     */
    String WaterWorkTime = "WaterWorkTime";
    /**
     * 已经净化的水
     */
    String WaterCleand = "WaterCleand";
    /**
     * 净水器滤芯状态_PP
     */
    String WaterFilterStatus_pp = "WaterFilterStatus_pp";
    /**
     * 净水器滤芯状态_CTO
     */
    String WaterFilterStatus_cto = "WaterFilterStatus_cto";
    /**
     * 净水器滤芯状态_RO1
     */
    String WaterFilterStatus_ro1 = "WaterFilterStatus_ro1";
    /**
     * 净水器滤芯状态_RO2
     */
    String WaterFilterStatus_ro2 = "WaterFilterStatus_ro2";
    /**
     * 净水器滤芯剩余的时间pp
     */
    String WaterFilter_time_pp = "WaterFilter_time_pp";
    String WaterFilter_time_cto = "WaterFilter_time_cto";
    String WaterFilter_time_ro1 = "WaterFilter_time_ro1";
    String WaterFilter_time_ro2 = "WaterFilter_time_ro2";

    /**
     * 净水器每日饮水量
     */
    String WaterEveryDay = "WaterEveryDay";
    /**
     * 净水器净水前水质
     */
    String WaterQualityBefore = "WaterQualityBefore";
    /**
     * 净水器净水后水质
     */
    String WaterQualityAfter = "WaterQualityAfter";
    /**
     * 净水器开关
     */
    String WaterPurifierSwitch = "WaterPurifierSwitch";
    /**
     * 净水器制水
     */
    String WaterPurifierClean = "WaterPurifierClean";
    /**
     * 净水器冲洗
     */
    String WaterPurifierWash = "WaterPurifierWash";
    /**
     * 净水器滤芯到期时间
     */
    String WaterPurifierFiliter = "WaterPurifierFiliter";
    /**
     * 净水器当日饮水量上报
     */
    String WaterPurifierDayReport = "WaterPurifierDayReport";
    /**
     * 净水器设置制水升量
     */
    String WaterPurifierKettelCount = "WaterPurifierKettelCount";

    /**
     * 设置连续制水的key
     */
    String SetWaterPurifierSystemKey = " SetWaterPurifierSystemKey";
    String SetWaterPurifierSystemLength = "SetWaterPurifierSystemLength";
    String SetSetWaterPurifierSystemValue = "SetSetWaterPurifierSystemValue";
    String WaterCurrentQuilityKey = "WaterCurrentQuilityKey";
    String WaterCurrentQuilityLength = "WaterCurrentQuilityKey";
    String WaterCurrentQuilityValue = "WaterCurrentQuilityValue";
    /**
     * 设置省电模式
     */
    String setWaterPurifierPowerSavingKey = "setWaterPurifierPowerSavingKey";
    String SetWaterPurifierPowerSavingLength = "SetWaterPurifierPowerSavingLength";
    String SetWaterPurifierPowerSavingValue = "SetWaterPurifierPowerSavingValue";

    /**
     * 变频爆炒时间
     */
    String R8230SFryTime = "R8230SFryTime";
    /**
     * 变频爆炒开关
     */
    String R8230SFrySwitch = "R8230SFrySwitch";

    //油杯提示功能开关
    String FanCupOilSwitch = "FanCupOilSwitch";

    //智能烟感开关
    String FanReducePower = "FanReducePower";

    //3D手势识别开关
    String gestureRecognitionSwitch = "gestureRecognitionSwitch";
    //过温保护开关
    String overTemperatureProtectionSwitch = "overTemperatureProtectionSwitch";
    //过温保护设置温度
    String overTemperatureProtectionSetTemp = "overTemperatureProtectionSetTemp";

    //烟机电磁灶开关联动
    String FanStovePower = "FanStovePower";
    //烟机档位联动开关
    String FanPowerLink = "FanPowerLink";
    //电磁灶关机后烟机延时关机开关
    String StoveShutDelay = "StoveShutDelay";
    //电磁灶关机后烟机延时关机时间
    String StoveShutDelayTime = "StoveShutDelayTime";
    //油烟机清洗提示开关
    String FanCleanPower = "FanCleanPower";
    //定时通风
    String TimeAirPower = "TimeAirPower";
    String TimeAirPowerDay = "TimeAirPowerDay";
    //通风的时间
    String AirTimePower = "AirTimePower";
    String AirTimeWeek = "AirTimeWeek";
    String AirTimeHour = "AirTimeHour";
    String AirTimeMinute = "AirTimeMinute";
    //爆炒时间
    String FryStrongTimePower = "FryStrongTimePower";
    String FryStrongTime = "FryStrongTime";
    //倒油杯提示开关
    String CupOilPower = "CupOilPower";
    //定时提醒设置开关
    String TimeReminderSetSwitch = "TimeReminderSetSwitch";
    //定时提醒设置时间
    String TimeReminderSetTime = "TimeReminderSetTime";
    //智能烟感开关
    String FanFeelPower = "FanFeelPower";
    //防干烧提示开关
    String ProtectTipDryPower = "ProtectTipDryPower";
    //防干烧开关
    String ProtectDryPower = "ProtectDryPower";


    String DeviceId = "Guid";

    String RcValue = "RcValue";

    /**
     * 一体机状态
     */
    String SteameOvenStatus = "SteameOvenStatus";


    /**
     * 一体机操作状态
     */
    String SteameOvenPowerOnStatus = "SteameOvenPowerOnStatus";

    /**
     * 一体机工作状态
     */
    String SteameOvenWorknStatus = "SteameOvenWorknStatus";


    /**
     * 一体机预约时间
     */
    String SteameOvenOrderTime_min = "SteameOvenOrderTime_min";
    String SteameOvenOrderTime_hour = "SteameOvenOrderTime_hour";


    /**
     * 一体机工作模式
     */
    String SteameOvenMode = "SteameOvenMode";


    /**
     * 一体机温度和时间
     */
    String SteameOvenTemp = "SteameOvenTemp";
    String SteameOvenTime = "SteameOvenTime";
    String OvenSteamTemp = "OvenSteamTemp";
    String OvenSteamTime = "OvenSteamTime";
    String OvenSteamMode = "OvenSteamMode";
    String OvenSteamUp = "OvenSteamUp";
    String OvenSteamBelow = "OvenSteamBelow";

    /**
     * 一体机设置温度
     */
    String SteameOvenSetTemp = "SteameOvenSetTemp";
    String SteameOvenSetTemp_Length = "SteameOvenSetTemp_Length";
    String SteameOvenSetTemp_Value = "SteameOvenSetTemp_Value";

    /**
     * 一体机设置时间
     */
    String SteameOvenSetTime = "SteameOvenSetTime";
    String SteameOvenSetTime_Length = "SteameOvenSetTime_Length";
    String SteameOvenSetTime_Value = "SteameOvenSetTime_Value";

    /**
     * 一体机设置PreFlag
     */
    String SteameOvenPreFlag = "SteameOvenPreFlag";

    /**
     * 一体机设置ModelType
     */
    String SteameOvenModelType = "SteameOvenModelType";

    /**
     * 一体机菜谱标识
     */
    String SteameOvenRecipeFlag = "SteameOvenRecipeFlag";

    /**
     * 一体机设置菜谱ID
     */
    String SteameOvenRecipeId = "SteameOvenRecipeId";
    String SteameOvenRecipeValue = "SteameOvenRecipeValue";
    String SteameOvenRecipeLength = "SteameOvenRecipeLength";

    /**
     * 一体机设置菜谱总步骤数
     */
    String SteameOvenRecipeTotalsteps = "SteameOvenRecipeTotalsteps";
    String SteameOvenRecipeTotalstepsLength = "SteameOvenRecipeTotalstepsLength";
    String SteameOvenRecipeTotalstepsValue = "SteameOvenRecipeTotalstepsValue";

    /**
     * 一体机设置菜谱步骤
     */
    String SteameOvenRecipesteps = "SteameOvenRecipesteps";
    String SteameOvenRecipestepsLength = "SteameOvenRecipeTotalstepsLength";
    String SteameOvenRecipestepsValue = "SteameOvenRecipeTotalstepsValue";

    /**
     * 一体机设置下温度
     */
    String SteameOvenSetDownTemp = "SteameOvenSetDownTemp";
    String SteameOvenSetDownTemp_Lenght = "SteameOvenSetDownTemp_Lenght";
    String SteameOvenSetDownTemp_Vaue = "SteameOvenSetDownTemp_Vaue";

    /**
     * 一体机下温度
     */
    String SteameOvenDownTemp = "SteameOvenDownTemp";
    String SteameOvenDownTemp_Lenght = "SteameOvenDownTemp_Lenght";
    String SteameOvenDownTemp_Vaue = "SteameOvenDownTemp_Vaue";
    /**
     * 一体机蒸汽
     */
    String SteameOvenSteam = "SteameOvenSteam";
    String SteameOvenSteam_Length = "SteameOvenSteamt_Length";
    String SteameOvenSteam_Value = "SteameOvenSteam_Value";

    /**
     * 一体机自动模式
     */
    String SteameOvenCpMode = "SteameOvenPcMode";
    String SteameOvenCpMode_Length = "SteameOvenCpMode_Length";
    String SteameOvenCpMode_Value = "SteameOvenCpMode_Value";

    /**
     * 一体机照明灯
     */
    String SteameOvenLight = "SteameOvenLight";
    String SteameOvenLight_Length = "SteameOvenLight_Length";
    String SteameOvenLight_Value = "SteameOvenLight_Value";

    /**
     * 一体机多段烹饪
     */
    String steameOvenTotalNumberOfSegments_Key = "steameOvenTotalNumberOfSegments_Key";//总段数
    String steameOvenTotalNumberOfSegments_Length = "steameOvenTotalNumberOfSegments_Length";
    String steameOvenTotalNumberOfSegments_Value = "steameOvenTotalNumberOfSegments_Value";

    String SteameOvenSectionOfTheStep_Key = "SteameOvenSectionOfTheStep_Key";//段步骤
    String SteameOvenSectionOfTheStep_Length = "SteameOvenSectionOfTheStep_Length";
    String SteameOvenSectionOfTheStep_Value = "SteameOvenSectionOfTheStep_Value";


    /**
     * 一体机预设标志
     */
    String SteameOvenPreset = "SteameOvenPreset";

    /**
     * 一体机考叉旋转
     */
    String SteameOvenRevolve = "SteameOvenRevolve";
    String SteameOvenRevolve_Length = "SteameOvenRevolve_Length";
    String SteameOvenRevolve_Value = "SteameOvenRevolve_Value";

    /**
     * 水箱更改
     */
    String SteameOvenWaterChanges = "SteameOvenWaterChanges";
    String SteameOvenWaterChanges_Length = "SteameOvenWaterChanges_Length";
    String SteameOvenWaterChanges_Value = "SteameOvenWaterChanges_Value";

    /**
     * 一体机工作完成参数
     */
    String SteameOvenWorkComplete = "SteameOvenWorkComplete";
    String SteameOvenWorkComplete_Length = "SteameOvenWorkComplete_Length";
    String SteameOvenWorkComplete_Value = "SteameOvenWorkComplete_Value";

    /**
     * 一体机开关事件参数
     */
    String setSteameOvenSwitchControl = "setSteameOvenSwitchControl";
    String setSteameOvenSwitchControl_Length = "setSteameOvenSwitchControl_Length";
    String setSteameOvenSwitchControl_Value = "setSteameOvenSwitchControl_Value";


    //一体机状态查询回应参数
    String SteameOvenAlarm = "SteameOvenAlarm";//故障
    String SteameOvenLeftTime = "SteameOvenLeftTime";
    String SteameOvenWaterStatus = "SteameOvenWaterStatus";
    String SteameOvenCpStep = "SteameOvenCpStep";//自动模式介
    String SteameOvenCpStep_Lenght = "SteameOvenCpStep_Lenght";
    String SteameOvenCpStep_Value = "SteameOvenCpStep_Value";

    //一体机工作事件上报
    String setSteameOvenBasicMode_Key = "setSteameOvenBasicMode_Key";//设置基本模式
    String setSteameOvenBasicMode_Length = "setSteameOvenBasicMode_Length";
    String setSteameOvenBasicMode_value = "setSteameOvenBasicMode_value";

    //温控锅
    String isPotHyperthermiaAlarm = "isPotHyperthermiaAlarm";
    String isPotLinkWith = "isPotLinkWith";
    String Pot_Alarm_Value = "Pot_Alarm_Value";
    String Pot_Status = "Pot_Status";

    //干烧预警烟锅联动开关
    String potBurningWarnSwitch = "potBurningWarnSwitch";
    //锅状态
    String Pot_status = "Pot_status";
}