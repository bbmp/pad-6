package com.robam.rokipad.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;

public class RokiService extends Service {

    private static final String TAG = "RokiService";

    protected final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        RokiService getService() {
            return RokiService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PowerServiceImpl();
    }

    public class PowerServiceImpl extends IPowerService.Stub {

        final static String NAME = "cxrobam power";

        @Override
        public String getName() throws RemoteException {
            return NAME;
        }

        @Override
        public int onShortPressToOn() throws RemoteException {
            LogUtils.e(TAG, "onShortPressToOn!");
            openDevice();
            return 0;
        }

        @Override
        public int onShortPressToOff() throws RemoteException {
            closeDevice();
            return 0;
        }

        @Override
        public int onLongPress() throws RemoteException {
            closeDevice();
            return 0;
        }

        @Override
        public int isRunning() throws RemoteException {

            int res = 0;
            try {
                AbsFan fan =  Utils.getDefaultFan();
                if (fan != null) {
                    if (fan.level > AbsFan.PowerLevel_0 || fan.light) {
                        res = 1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }


        private void openDevice() {
            try {
                AbsFan fan = Utils.getDefaultFan();
                if (fan != null) {
                    fan.setFanStatus(FanStatus.On, null);
                    LogUtils.e(TAG, "openDevice FanStatus.On");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void closeDevice() {
            try {
                AbsFan fan =  Utils.getDefaultFan();
                if (fan != null) {
                    fan.setFanStatus(FanStatus.Off, null);
                    fan.setFanLight(false, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
