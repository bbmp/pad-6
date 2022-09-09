package com.robam.common.pojos.device.Sterilizer;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.robam.common.pojos.device.IPauseable;

/**
 * Created by zhaiyuanyi on 15/11/19.
 */
public interface ISterilizer extends IPauseable {
    public String getSterilizerModel();
    /**
     * 读取消毒柜工作状态
     *
     * @param callback
     */
//    void getSteriPowerStatus(VoidCallback callback);
//
//    /**
//     * 设置消毒柜工作状态
//     *
//     * @param status   0-关机，1-开机
//     * @param callback
//     */
    void setSteriPower(short status, VoidCallback callback);

    void setSteriNewStatus(short status,short steriDisinfectTime, VoidCallback callback);

    void setSteriNewStatus(final short status,final short setTime,short warmDishTemp, final VoidCallback callback);


    /**
     * 设置消毒柜预约时间
     * ORDER_TIME[1Byte] {0:预约取消，1，2，3，4…24预约时间
     */
    void setSteriReserveTime(short SteriReserveTime, VoidCallback callback);
    /**
     * 设置消毒柜烘干
     * DRYING_TIME[1Byte] {0取消烘干，>1 为烘干时间}
     */
    void setSteriDrying(short SteriDrying,VoidCallback voidCallback);
    /**
     * 设置消毒柜保洁
     * CLEAN_TIME[1Byte] {0取消保洁，60，保洁时间}
     */
    void setSteriClean(short SteriCleanTime, VoidCallback voidCallback);

    /**
     * 设置消毒柜消毒
     * DISINFECT_TIME[1Byte] {0取消消毒,150消毒时间}
     */
    void setSteriDisinfect(short SteriDisinfectTime, VoidCallback voidCallback);

    /**
     * 设置消毒柜童锁
     * ON_OFF [1Byte] {0取消童锁，1，开童锁}
     */
    void  setSteriLock(boolean isChildLock,VoidCallback voidCallback);

    /**
     * 查询温度，湿度，细菌数信息
     * TEM[1Byte]  温度值
     HUM [1Byte]  湿度值
     GERM [1Byte] 细菌值
     臭氧[1Byte]
     */
    void querySteriParm(VoidCallback voidCallback);
    /**
     *消毒柜状态查询
     */
    void getSteriStatus(VoidCallback callback);

    /**
     * 读取消毒柜峰谷定时设置
     *
     */
    void getSteriPVConfig(Callback<SteriSmartParams> callback);

    /**
     * 设置消毒柜峰谷定时命令开启
     * 是否开启定时消毒[1BYTE] SteriSwitchDisinfect
     *定时消毒间隔时间[1BYTE], SteriInternalDisinfect  单位天
     *是否开启每周消毒[1BYTE]  SteriSwitchWeekDisinfect
     *每周消毒的时时间[1BYTE]  SteriWeekInternalDisinfect
     *消毒柜峰谷电时间[1BYTE]  SteriPVDisinfectTime
     *单位：小时
     */
    void setSteriPVConfig(SteriSmartParams smartParams, VoidCallback callback);

}
