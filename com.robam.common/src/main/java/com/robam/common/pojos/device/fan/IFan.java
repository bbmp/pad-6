package com.robam.common.pojos.device.fan;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.IPauseable;
import com.robam.common.pojos.device.SmartParams;

/**
 * Created by zhaiyuanyi on 15/10/19.
 */
public interface IFan extends IPauseable {

    /**
     * model of fan
     */
    String getFanModel();

    /**
     * 读取油烟机状态
     *
     * @param callback
     */
    void getFanStatus(VoidCallback callback);

    /**
     * 设置油烟机工作状态
     *
     * @param status   0-关机，1-开机
     * @param callback
     */
    void setFanStatus(short status, VoidCallback callback);

    /**
     * 设置油烟机档位
     *
     * @param level    0、1、2、3、6档
     * @param callback
     */
    void setFanLevel(short level, VoidCallback callback);

    /**
     * 设置油烟机灯的状态
     *
     * @param light
     * @param callback
     */
    void setFanLight(boolean light, VoidCallback callback);

    /**
     * 设置油烟机整体状态
     *
     * @param level
     * @param light
     * @param callback
     */
    void setFanAllParams(short level, boolean light, VoidCallback callback);

    /**
     * 重置烟机清洗计时
     *
     * @param callback
     */
    void restFanCleanTime(VoidCallback callback);

    /**
     * 重启油烟机网络板
     *
     * @param callback
     */
    void RestFanNetBoard(VoidCallback callback);

    /**
     * 设置油烟机定时工作
     *
     * @param level
     * @param time     单位：分钟
     * @param callback
     */
    void setFanTimeWork(short level, short time, VoidCallback callback);


    /**
     * 读取智能互动模式设定
     *
     * @param callback
     */
    void getSmartConfig(Callback<SmartParams> callback);

    /**
     * 设置智能互动模式
     *
     * @param callback
     */
    void setSmartConfig(SmartParams smartParams, VoidCallback callback);

    /**
     * 烟机定时提醒
     * @param onOff
     * @param time
     */
    void setTimingRemind(short onOff,short time,VoidCallback callback);

    /**
     * 设置恢复出厂设置
     */
    void setFactoryDataReset(FanStatusComposite fanStatusComposite,short argumentNumber, VoidCallback callback);

}
