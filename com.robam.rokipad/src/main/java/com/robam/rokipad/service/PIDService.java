package com.robam.rokipad.service;

import android.os.SystemClock;

import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.utils.LogUtils;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.rokipad.service.pidcontroltemperature.MiniPID;

public class PIDService {
    private static final String TAG = "PIDService";

    private Stove stove;//灶具
    private Pot pot;//无人锅
    private long cookTime;//烹饪时长
    private long startCookTime;//开始烹饪时间

    private static final PIDService ourInstance = new PIDService();
    private MiniPID miniPID = new MiniPID(0.25f, 0.01f, 0.04f);

    private float targetTemperature = 100;//设置目标值

    private float actualTemperature = 0;//实际值
    private float outputTemperature = 0;//

    public static PIDService getInstance() {
        return ourInstance;
    }

    private PIDService() {
    }

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (event.pojo == null) {
            return;
        }
        pot = event.pojo;
        LogUtils.e(TAG, "PotStatusChangedEvent:" + pot.tempUp);
        work(pot.tempUp);

    }

    public void start(Stove stove, Pot pot, float SetTemperature, float lastTime) {
        this.stove = stove;
        this.pot = pot;
        miniPID.setOutputLimits(10);
        miniPID.setSetpointRange(40);
        miniPID.setSetpoint(0);
        miniPID.setSetpoint(targetTemperature);
        startCookTime = SystemClock.currentThreadTimeMillis();

    }

    public void work(float pot_temperature) {
        if (pot_temperature == 60)
            targetTemperature = 80;

        //if(i==75)target=(100);
        //if(i>50 && i%4==0)target=target+(Math.random()-.5)*50;

        outputTemperature = miniPID.getOutput(actualTemperature, targetTemperature);
        actualTemperature = actualTemperature + outputTemperature;

        //System.out.println("==========================");
        //System.out.printf("Current: %3.2f , Actual: %3.2f, Error: %3.2f\n",actual, output, (target-actual));
        LogUtils.e(TAG, "target:" + targetTemperature + " actual：" + actualTemperature + " target-actual:" + (targetTemperature - actualTemperature));
        //if(i>80 && i%5==0)actual+=(Math.random()-.5)*20;
    }

    public void end() {
        stove.setStoveShutdown((short) 0, (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

}
