package com.robam.common;

import com.legent.plat.Plat;
import com.legent.plat.PlatApp;
import com.robam.common.services.NotifyService;

abstract public class RobamApp extends PlatApp {
    protected static final String SERVICE_HOST = "api.myroki.com";
    protected static final int ecsPort = 80;
    protected static final String MQTT_HOST = "mqtt.myroki.com";
    protected static final int acsHost = 1883;

    protected static final String Test_SERVICE_HOST = "develop.api.myroki.com";
    protected static final int Test_ecsPort = 8081;
    protected static final String Test_MQTT_HOST = "develop.mqtt.myroki.com";
    protected static final int Test_acsHost = 1883;
//     protected static final String SERVICE_HOST = "115.29.246.216";  115.29.246.216:8081  1883
    //     static final String SERVICE_HOST = "172.16.14.32";

    abstract public NotifyService getNotifyService();

    @Override
    protected void init() {
        super.init();
        initPlat();
    }

    protected void initPlat() {
        Plat.serverOpt.set(SERVICE_HOST, ecsPort, MQTT_HOST, acsHost);//正式环境
//        Plat.serverOpt.set(Test_SERVICE_HOST, Test_ecsPort, Test_MQTT_HOST, Test_acsHost);//测试环境
    }


}
