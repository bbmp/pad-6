package com.robam.common.io.device;


public interface MsgKeys {

    // -------------------------------------------------------------------------------
    // 通知类
    // -------------------------------------------------------------------------------

    /**
     * 电磁灶报警上报
     */
    short StoveAlarm_Noti = 138;

    /**
     * 电磁灶事件上报
     */
    short StoveEvent_Noti = 139;

    /**
     * 灶具温度事件上报     by zhaiyuanyi 20151029
     */

    short StoveTemp_Noti = 140;

    /**
     * 油烟机事件上报
     */
    short FanEvent_Noti = 148;
    /**
     * 消毒柜报警上报      by zhaiyuanyi 20151120
     */
    short SteriAlarm_Noti = 146;
    /**
     * 消毒柜事件上报      by zhaiyuanyi 20151218
     */
    short SteriEvent_Noti = 152;

    // -------------------------------------------------------------------------------
    // 应答类
    // -------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------电磁灶
    /**
     * 获取电磁灶状态（请求）
     */
    short GetStoveStatus_Req = 128;

    /**
     * 获取电磁灶状态（应答）
     */
    short GetStoveStatus_Rep = 129;

    /**
     * 设置电磁灶状态（请求）
     */
    short SetStoveStatus_Req = 130;

    /**
     * 设置电磁灶状态（应答）
     */
    short SetStoveStatus_Rep = 131;
    /**
     * 设置电磁灶档位（请求）
     */
    short SetStoveLevel_Req = 132;

    /**
     * 设置电磁灶档位（应答）
     */
    short SetStoveLevel_Rep = 133;
    /**
     * 设置电磁灶定时关机（请求）
     */
    short SetStoveShutdown_Req = 134;

    /**
     * 设置电磁灶定时关机（应答）
     */
    short SetStoveShutdown_Rep = 135;

    /**
     * 设置电磁灶童锁（请求）
     */
    short SetStoveLock_Req = 136;

    /**
     * 设置电磁灶童锁（应答）
     */
    short SetStoveLock_Rep = 137;

    // -------------------------------------------------------------------------------油烟机

    /**
     * 读取智能互动模式设定（请求）
     */
    short GetSmartConfig_Req = 128;

    /**
     * 读取智能互动模式设定（应答）
     */
    short GetSmartConfig_Rep = 129;

    /**
     * 读取油烟机状态（请求）
     */
    short GetFanStatus_Req = 130;

    /**
     * 读取油烟机状态（应答）
     */
    short GetFanStatus_Rep = 131;

    /**
     * 设置油烟机工作状态（请求）
     */
    short SetFanStatus_Req = 132;

    /**
     * 设置油烟机工作状态（应答）
     */
    short SetFanStatus_Rep = 133;

    /**
     * 设置油烟机档位（请求）
     */
    short SetFanLevel_Req = 134;

    /**
     * 设置油烟机档位（应答）
     */
    short SetFanLevel_Rep = 135;

    /**
     * 设置油烟机灯（请求）
     */
    short SetFanLight_Req = 136;

    /**
     * 设置油烟机灯（应答）
     */
    short SetFanLight_Rep = 137;

    /**
     * 设置油烟机整体状态（请求）
     */
    short SetFanAllParams_Req = 138;

    /**
     * 设置油烟机整体状态（应答）
     */
    short SetFanAllParams_Rep = 139;

    /**
     * 重置烟机清洗计时（请求）
     */
    short RestFanCleanTime_Req = 140;

    /**
     * 重置烟机清洗计时（应答）
     */
    short RestFanCleanTime_Rep = 141;

    /**
     * 重启油烟机网络板（请求）
     */
    short RestFanNetBoard_Req = 142;

    /**
     * 重启油烟机网络板（应答）
     */
    short RestFanNetBoard_Rep = 143;

    /**
     * 设置油烟机定时工作（请求）
     */
    short SetFanTimeWork_Req = 144;

    /**
     * 设置油烟机定时工作（应答）
     */
    short SetFanTimeWork_Rep = 145;

    /**
     * 设置智能互动模式（请求）
     */
    short SetSmartConfig_Req = 146;

    /**
     * 设置智能互动模式（应答）
     */
    short SetSmartConfig_Rep = 147;

    /**
     * 设置油烟机重置油杯定时工作（请求）
     */
    short SetFanCleanOirCupTime_Req = 166;

    //2019年新增指令
    short SetFanStatusCompose_Rep = 162;


    short SetFanStatusCompose_Req = 163;

    short FanStatusComposeCheck_Rep = 149;

    short FanStatusComposeCheck_Req = 150;

    //设置定时提醒
    short SetFanTimingRemind_Rep = 168;

    short SetFanTimingRemind_Req = 169;

    /**
     * 设置油烟机重置油杯定时工作（应答）
     */
    short SetFanCleanOirCupTime_Rep = 167;

    // ------------------------------------------------------------------------------消毒柜  by zhaiyuanyi 20151120
    /**
     * 设置消毒柜开关（请求）
     */
    short SetSteriPowerOnOff_Req = 128;
    /**
     * 设置消毒柜开关（应答）
     */
    short SetSteriPowerOnOff_Rep = 129;
    /**
     * 设置消毒柜预约时间（请求）
     */
    short SetSteriReserveTime_Req = 130;
    /**
     * 设置消毒柜预约时间（应答）
     */
    short SetSteriReserveTime_Rep = 131;
    /**
     * 设置消毒柜烘干（请求）
     */
    short SetSteriDrying_Req = 132;
    /**
     * 设置消毒柜烘干（应答）
     */
    short SetSteriDrying_Rep = 133;
    /**
     * 设置消毒柜保洁（请求）
     */
    short SetSteriClean_Req = 134;
    /**
     * 设置消毒柜保洁（应答）
     */
    short SetSteriClean_Rep = 135;
    /**
     * 设置消毒柜消毒（请求）
     */
    short SetSteriDisinfect_Req = 136;
    /**
     * 设置消毒柜消毒（应答）
     */
    short SetSteriDisinfect_Rep = 137;
    /**
     * 设置消毒柜童锁（829请求）
     */
    short SetSteriLock_Req = 138;//消毒柜829童锁
    /**
     * 设置消毒柜童锁（829应答）
     */
    short SetSteriLock_Rep = 139;

    /**
     * 设置消毒柜童锁（826请求）
     */
    short SetSteri826Lock_Req = 153;//消毒柜826童锁
    /**
     * 设置消毒柜童锁（826应答）
     */
    short SetSteri826Lock_Rep = 154;

    /**
     * 查询消毒柜温度／湿度／细菌数（请求）
     */
    short GetSteriParam_Req = 142;
    /**
     * 查询消毒柜温度／湿度／细菌数（应答）
     */
    short GetSteriParam_Rep = 143;
    /**
     * 消毒柜状态查询（请求）
     */
    short GetSteriStatus_Req = 144;
    /**
     * 消毒柜状态查询（应答）
     */
    short GetSteriStatus_Rep = 145;
    /**
     * 读取消毒柜峰谷定时设置(请求)
     */
    short GetSteriPVConfig_Req = 147;
    /**
     * 读取消毒柜峰谷定时设置(应答)
     */
    short GetSteriPVConfig_Rep = 148;
    /**
     * 设置消毒柜峰谷定时开启（请求）
     */
    short SetSteriPVConfig_Req = 149;
    /**
     * 设置消毒柜峰谷定时开启（应答）
     */
    short SetSteriPVConfig_Rep = 150;

    // ------------------------------------------------------------------------------蒸汽炉  by Rosicky 20151214

    /**
     * 设置蒸汽炉工作时间
     */
    short setSteamTime_Req = 129;
    /**
     * 设置蒸汽炉工作时间（应答）
     */
    short setSteamTime_Rep = 130;
    /**
     * 设置蒸汽炉工作温度
     */
    short setSteamTemp_Req = 131;
    /**
     * 蒸汽炉工作温度（应答）
     */
    short setSteamTemp_Rep = 132;
    /**
     * 设置蒸汽炉工作烹饪模式
     */
    short setSteamMode_Req = 133;
    /**
     * 设置蒸汽炉烹饪模式（应答）
     */
    short setSteamMode_Rep = 134;
    /**
     * 蒸汽炉专业模式设置
     */
    short setSteamProMode_Req = 141;
    /**
     * 蒸汽炉专业模式设置（应答）
     */
    short setSteamProMode_Rep = 142;
    /**
     * 蒸汽炉状态查询（请求）
     */
    short GetSteamOvenStatus_Req = 143;
    /**
     * 蒸汽炉状态查询（应答）
     */
    short GetSteamOvenStatus_Rep = 144;
    /**
     * 设置蒸汽炉状态
     */
    short setSteamStatus_Req = 145;
    /**
     * 设置蒸汽炉状态（应答）
     */
    short setSteamStatus_Rep = 146;
    /**
     * 蒸汽炉报警事件上报
     */
    short SteamOvenAlarm_Noti = 149;
    /**
     * 蒸汽炉事件上报
     */
    short SteamOven_Noti = 150;

    /**
     * 菜谱设置
     */
    short SetSteamRecipeReq = 158;
    /**
     * 菜谱设置
     */
    short GetSteamRecipeRep = 159;

    /**
     * 蒸汽炉灯设置
     */
    short SetSteamLightReq = 162;

    /**
     * 蒸汽炉灯回应
     */
    short GetSteamLightRep = 163;

    /**
     * 设置水箱弹出
     */
    short SetSteamWaterTankPOPReq = 164;
    /**
     * 水箱弹出回应
     */
    short GetSteamWaterTankPOPRep = 165;


    // ------------------------------------------------------------------------------微波炉  by Rosicky 20151217

    /**
     * 设置微波炉开关（请求）
     */
    short setMicroWaveStatus_Req = 128;
    /**
     * 设置微波炉开关（应答）
     */
    short setMicroWaveStates_Rep = 129;
    /**
     * 设置去味模式
     */
    short setMicroWaveClean_Req=132;

    /**
     * 去味模式回复
     */
    short setMicroWaveClean_Rep=133;
    /**
     * 设置微波炉品类和加热解冻（请求）
     */
    short setMicroWaveKindsAndHeatCold_Req = 134;
    /**
     * 设置微波炉品类和加热解冻（应答）
     */
    short setMicroWaveKindsAndHeatCold_Rep = 135;
    /**
     * 设置微波炉联动料理请求
     */
    short setMicroWaveLinkedCook_Req = 136;
    /**
     * 设置微波炉联动料理请求(应答)
     */
    short setMicroWaveLinkedCook_Rep = 137;
    /**
     * 设置微波炉专业模式加热（请求）
     */
    short setMicroWaveProModeHeat_Req = 140;
    /**
     * 设置微波炉妆业模式加热（应答）
     */
    short setMicroWaveProModeHeat_Rep = 141;
    /**
     * 设置微波炉照明灯开关（请求）
     */
    short setMicroWaveLight_Req = 142;
    /**
     * 设置微波炉照明灯开关（应答）
     */
    short setMicroWaveLight_Rep = 143;
    /**
     * 查询微波炉状态（请求）
     */
    short getMicroWaveStatus_Req = 144;
    /**
     * 查询微波炉状态（应答）
     */
    short getMicroWaveStatus_Rep = 145;
    /**
     * 查询微波炉报警（应答）
     */
    short getMicroWaveAlarm_Rep = 149;
    /**
     * 微波炉事件上报
     */
    short MicroWave_Noti = 150;

    /**
     * 菜谱设置
     */
    short MicroWave_Recipe_Req=158;
    /**
     * 菜谱回应
     */
    short MicroWave_Recipe_Rep=159;


    // ------------------------------------------------------------------------------电烤箱  by Linxiaobin 20151214

    /**
     * 设置状态控制
     */
    short setOvenStatusControl_Req = 128;
    /**
     * 设置状态控制回应（应答）
     */
    short setOvenStatusControl_Rep = 129;
    /**
     * 设置快热
     */
    short setOvenQuickHeat_Req = 130;
    /**
     * 设置快热回应（应答）
     */
    short setOvenQuickHeat_Rep = 131;
    /**
     * 设置风焙烤
     */
    short setOvenAirBaking_Req = 132;
    /**
     * 设置风焙烤回应（应答）
     */
    short setOvenAirBaking_Rep = 133;
    /**
     * 设置焙烤
     */
    short setOvenToast_Req = 134;
    /**
     * 设置焙烤回应（应答）
     */
    short setOvenToast_Rep = 135;
    /**
     * 设置底加热（请求）
     */
    short setOvenBottomHeat_Req = 136;
    /**
     * 设置底加热回应（应答）
     */
    short setOvenBottomHeat_Rep = 137;
    /**
     * 设置解冻
     */
    short setOvenUnfreeze_Req = 138;
    /**
     * p[
     * 设置解冻回应（应答）
     */
    short setOvenUnfreeze_Rep = 139;
    /**
     * 设置风扇烤
     */
    short setOvenAirBarbecue_Req = 140;
    /**
     * 设置风扇烤回应（应答）
     */
    short setOvenAirBarbecue_Rep = 141;
    /**
     * 设置烧烤
     */
    short setOvenBarbecue_Req = 142;
    /**
     * 设置烧烤回应（应答）
     */
    short setOvenBarbecue_Rep = 143;
    /**
     * 设置强烧烤
     */
    short setOvenStrongBarbecue_Req = 144;

    /**
     * 设置Exp模式
     */
    short setOvenExpModel_Req = 9;
    /**

    /**
     * 设置风扇烤回应（应答）
     */
    short setOvenStrongBarbecue_Rep = 145;
    /**
     * 设置 烤叉旋转，灯光控制
     */
    short setOvenSpitRotateLightControl_Req = 148;
    /**
     * 设置 烤叉旋转，灯光控制回应（应答）
     */
    short setOvenSpitRotateLightControl_Rep = 149;
    /**
     * 烤箱状态查询（请求）
     */
    short getOvenStatus_Req = 150;
    /**
     * 烤箱状态查询应答（应答）
     */
    short getOvenStatus_Rep = 151;
    /**
     * 烤箱报警事件上报
     */
    short OvenAlarm_Noti = 152;
    /**
     * 烤箱工作事件上报
     */
    short Oven_Noti = 153;
    /**
     * 设置烤箱运行模式
     */
    short SetOven_RunMode_Req = 154;
    /**
     * 设置烤箱运行模式回复
     */
    short GetOven_RunMode_Rep = 155;

    /**
     * 菜谱设置
     */
    short SetSteamOven_Recipe_Req = 158;

    /**
     * 菜谱设置回应
     */
    short GetOven_Recipe_Rep = 159;

    /**
     * 设置烤箱自动模式
     */
    short Set_Oven_Auto_Mode_Req = 160;

    /**
     * 设置烤箱自动模式
     */
    short Get_Oven_Auto_Mode_Rep = 161;

    /**
     * 设置烤箱灯
     */
    short Set_Oven_Light_Req = 162;

    /**
     * 烤箱灯回复
     */
    short Get_Oven_Light_Rep = 163;

    /*
    * 设置烤箱多段烹饪
    */

    short Set_Oven_More_Cook = 164;

    /*
    * 设置烤箱多段烹饪回复
    */

    short Get_Oven_More_Cook = 165;
    // ------------------------------------------------------------------------------净水器  by rentao 20151214
    /**
     * 设置净水器工作模式(请求)
     */
    short setWaterPurifiy_Req = 128;
    /**
     * 净水器状态查询（请求）
     */
    short GetWaterPurifiyStatus_Req = 132;
    /**
     * 净水器状态查询（返回）
     */
    short GetWaterPurifiyStatus_Rep = 133;
    /**
     * 净水器报警上报
     */
    short GetWaterPurifiyAlarm_Rep = 134;

    //新增净水器智能设定指令
    /**
     * 净水器智能设定请求
     *  净水器智能设定回应
     */
    short SetWaterPurifiySmart_Req=136;//请求
    short getWaterPurifiySmart_Rep=137;//回应

    short getWaterPurifierStatusSmart_Req=138;//请求
    short getWaterPurifierStatusSmart_Rep=139;//应答

    /**
     * 净水器滤芯寿命（返回）
     */
    short GetWaterPurifiyFiliter_Rep = 135;

    /**
     * 开始制水(请求)
     */
    short SetWaterPurifiyWorking_Req = 128;
    /**
     * 开始制水(返回)
     */
    short SetWaterPurifiyWorking_Rep = 129;

    /**
     * 获取温控锅温度(请求)
     */
    short GetPotTemp_Req = 128;
    /**
     * 获取温控锅温度(返回)
     */
    short SetPotTemp_Rep = 129;

    /**
     * 温控锅主动上报
     */
    short ActiveTemp_Rep = 142;

    /**
     * 温控锅报警上报
     */
    short PotAlarm_Report = 143;

    /**
     * 温控锅按键键值上报
     */
    short PotKey_Report = 141;

    /**
     * 144干烧预警及烟锅联动上报
     */
    short SetPotCom_Req = 144;

    /**
     * 干烧预警及烟锅联动上报回复
     */
    short GetPotCom_Rep = 145;

    /**
     * 干烧预警及烟锅联动上报开关设置
     */
    short SetPotSwitch_Req = 146;

    /**
     * 干烧预警及烟锅联动上报开关设置回复
     */
    short GetPotSwitch_Rep = 147;

    /**
     * 温控锅添加设备指令
     */
    short FanAddPot_Req = 53;

    /**
     * 温控锅添加设备指令 返回
     */
    short FanAddPot_Rep = 54;

    /**
     * 温控锅删除设备指令
     */
    short FanDelPot_Req = 38;

    /**
     * 温控锅删除设备指令 返回
     */
    short FanDelPot_Rep = 39;
    /**
     * 温控锅报警上报
     */
    short PotAlarm_Noti = 143;
    /**
     * 温控锅配置设置
     */
    short SetPotConfig_Req = 144;
    /**
     * 温控锅配置设置回复
     */
    short SetPotConfig_Rep = 145;
    /**
     * 温控锅配置查询
     */
    short GetPotConfig_Req = 146;

    /**
     * 温控锅配置查询 返回
     */
    short GetPotConfig_Rep = 147;

    /*-------------------------------烤蒸一体机------------------------*/
    /**
     * 设置状态控制
     */
    short setSteameOvenStatusControl_Req = 128;
    /**
     * 设置状态控制回应（应答）
     */
    short setSteameOvenStatusControl_Rep = 129;

    /**
     * 一体机状态查询
     */
    short getSteameOvenStatus_Req = 150;

    /**
     * 一体机状态查询(应答)
     */
    short getSteameOvenStatus_Rep = 151;

    /**
     * 报警事件上报
     */
    short SteameOvenAlarm_Noti = 152;
    /**
     * 工作事件上报
     */
    short SteameOven_Noti = 153;
    /**
     * 设置一体机基本模式
     */
    short setSteameOvenBasicMode_Req = 154;

    /**
     * 设置一体机基本模式(应答)
     */
    short setSteameOvenBasicMode_Rep = 155;

    /**
     * 菜谱设置
     */
    short setTheRecipe_Req = 158;

    /**
     * 菜谱设置(应答)
     */
    short setTheRecipe_Rep = 159;

    /**
     * 设置一体机自动模式
     */
    short setSteameOvenAutomaticMode_Req = 160;

    /**
     * 设置一体机自动模式(应答)
     */
    short setSteameOvenAutomaticMode_Rep = 161;

    /**
     * 设置一体机照明灯
     */
    short setSteameOvenFloodlight_Req = 162;

    /**
     * 设置一体机照明灯(应答)
     */
    short setSteameOvenFloodlight_Rep = 163;

    /**
     * 设置一体机加蒸汽
     */
    short setSteameOvensteam_Req = 164;
    /**
     * 设置一体机加蒸汽(应答)
     */
    short setSteameOvensteam_Rep = 165;
    /**
     * 设置一体机多段烹饪
     */
    short setSteameOvenMultistageCooking_Req =166;

    /**
     * 设置一体机多段烹饪(应答)
     */
    short setSteameOvenMultistageCooking_Rep =167;

    /**
     * 设置一体机水箱
     */
    short setSteameOvenWater_Req =168;

    /**
     * 设置一体机水箱(应答)
     */
    short setSteameOvenWater_Rep =169;
}
