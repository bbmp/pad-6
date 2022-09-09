package com.robam.rokipad.io;

import android.content.Context;

import com.legent.io.buses.IBus;
import com.legent.io.channels.AbsChannel;
import com.legent.io.protocols.IProtocol;
import com.legent.plat.services.CommonService;
import com.legent.utils.serialport.SerialBus;

public class SerialChannel extends AbsChannel {

    @Override
    protected IBus createBus() {
        return new SerialBus();
    }

    @Override
    protected IProtocol createProtocol() {
        return new SerialProtocol();
    }

    @Override
    public void init(Context cx, Object... params) {

        SerialBus.SerialParams busParams = new SerialBus.SerialParams(
                "/dev/ttyMT3", 115200);
        bus.init(cx, busParams);
        serial = true;
        CommonService.getInstance().setCommonCrashListener(new CommonService.OnCommonCrashListener() {
            @Override
            public void onCrashed() {
                dispose();
            }
        });
    }
}
