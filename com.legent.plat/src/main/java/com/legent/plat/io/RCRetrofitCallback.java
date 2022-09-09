package com.legent.plat.io;

import android.util.Log;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.plat.exceptions.ExceptionHelper;
import com.legent.plat.pojos.RCReponse;
import com.legent.services.RestfulService;
import com.legent.utils.LogUtils;

import java.io.IOException;
import java.net.ConnectException;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RCRetrofitCallback<T extends RCReponse> implements
        retrofit.Callback<T> {

    Callback<?> callback;

    public RCRetrofitCallback(Callback<?> callback) {
        this.callback = callback;
    }

    @Override
    public void success(T result, Response response) {
        if (result == null) {
            Helper.onFailure(callback, ExceptionHelper.newRestfulNullException());
            return;
        }
        boolean isSuccess = result.isSuccess();
        if (!isSuccess) {
            Helper.onFailure(callback, ExceptionHelper.newRCException(result.rc));
        } else {
            afterSuccess(result);
        }
    }

    @Override
    public void failure(RetrofitError e) {
        RestfulService.printError(e);

        if (e.getCause() instanceof ConnectException) {
            Helper.onFailure(callback, ExceptionHelper.newConnectException());
        }else if(e.getCause() instanceof IOException) {
            Helper.onFailure(callback,
                    ExceptionHelper.newDeviceIOException(e.getMessage()));
        } else  {
            Helper.onFailure(callback,
                    ExceptionHelper.newRestfulException(e.getMessage()));
        }
    }

    /**
     * RC码正确时的处理
     */
    protected void afterSuccess(T result) {
    }
}
