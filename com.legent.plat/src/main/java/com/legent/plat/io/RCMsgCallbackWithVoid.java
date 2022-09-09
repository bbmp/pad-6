package com.legent.plat.io;

import android.util.Log;

import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.exceptions.ExceptionHelper;
import com.legent.plat.exceptions.RCException;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgCallback;
import com.legent.plat.io.device.msg.MsgParams;
import com.legent.plat.services.ResultCodeManager;

public class RCMsgCallbackWithVoid implements MsgCallback {

    VoidCallback callback;

    public RCMsgCallbackWithVoid(VoidCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onSuccess(Msg resMsg) {
        try {
            boolean isRcMsg = !resMsg.isNull(MsgParams.RC);
            if (isRcMsg) {
                int rc = resMsg.getInt(MsgParams.RC);
                boolean isSuccess = ResultCodeManager.isSuccessRC(rc);

                if (!isSuccess) {
                    RCException e = (RCException) ExceptionHelper.newRCException(rc);
                    e.setTag(resMsg);
                    Helper.onFailure(callback, e);
                } else {
                    afterSuccess(resMsg);
                    Helper.onSuccess(callback);
                }
            } else {
                afterSuccess(resMsg);
                Helper.onSuccess(callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(Throwable t) {
        Helper.onFailure(callback, ExceptionHelper.newDeviceIOException(t.getMessage()));
    }

    protected void afterSuccess(Msg resMsg) {
    }
}
