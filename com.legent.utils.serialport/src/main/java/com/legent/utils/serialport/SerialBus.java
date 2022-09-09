package com.legent.utils.serialport;

import android.content.Context;

import com.google.common.base.Preconditions;
import com.legent.VoidCallback;
import com.legent.io.buses.AbsOioBus;
import com.legent.io.msgs.IMsg;
import com.legent.utils.LogUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialBus extends AbsOioBus {

    protected SerialParams busParams;
    protected SerialPort sp;
    protected InputStream in;
    protected OutputStream out;

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        Preconditions.checkArgument(params.length >= 1);
        busParams = (SerialParams) params[0];
        LogUtils.e("20200116","filePath:" + busParams.filePath);
        LogUtils.e("20200116","baudrate:" + busParams.baudrate);
        Preconditions.checkNotNull(busParams, "SerialParams parmas is null");
    }

    @Override
    protected void onOpen(VoidCallback callback) {

        try {
            if (sp != null)
                return;
            File file = new File(busParams.filePath);
            Preconditions.checkState(file.exists(), "serialport file invalid");
            sp = new SerialPort(file, busParams.baudrate, 0);
            in = sp.getInputStream();
            out = sp.getOutputStream();
            onCallSuccess(callback);
        } catch (Exception e) {
            onCallFailure(callback, e.getCause());
        } finally {
        }

    }

    @Override
    protected void onClose(VoidCallback callback) {
        try {
            if (sp != null)
                sp.close();
            if (in != null)
                in.close();
            if (out != null)
                out.close();

            onCallSuccess(callback);
        } catch (Exception e) {
            onCallFailure(callback, e.getCause());
        } finally {
            sp = null;
            in = null;
            out = null;
        }
    }

    @Override
    public void send(IMsg msg, VoidCallback callback) {

        try {
            if (!msg.toString().contains("130")) {
                LogUtils.e("AddStove", "SerialBus send:" + " " + msg);
            }
            byte[] data = msg.getBytes();
            LogUtils.e("AddStove","data:" + data.length);
            if (out != null){
                out.write(data);
                out.flush();
            }
            onCallSuccess(callback);
            if (!msg.toString().contains("130")) {
                LogUtils.e("AddStove", "SerialBus send: succes");
            }
        } catch (Exception e) {
            LogUtils.e("AddStove", "SerialBus send:" + e.toString());
            onCallFailure(callback, e.getCause());
        } finally {
        }
    }

    @Override
    protected int read(byte[] buffer) throws Exception {
        int count = in.read(buffer);
        LogUtils.e("AddStove", "read: count");
        return count;
    }



    @Override
    protected int getBufferSize() {
        return 256;
    }

    // -------------------------------------------------------------------------------
    // SerialParams
    // -------------------------------------------------------------------------------

    /**
     * 串口参数
     *
     * @author sylar
     */
    public static class SerialParams {
        public String filePath;
        public int baudrate;

        public SerialParams() {
            this("/dev/ttyMT1", 115200);
        }

        public SerialParams(String filePath, int baud) {
            this.filePath = filePath;
            this.baudrate = baud;
        }
    }
}
