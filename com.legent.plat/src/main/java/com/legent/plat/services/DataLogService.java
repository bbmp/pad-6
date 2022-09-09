package com.legent.plat.services;

import android.graphics.Bitmap;
import android.os.Environment;

import com.legent.ILogService;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.SubDeviceInfo;
import com.legent.services.AbsService;
import com.legent.utils.FileUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.ViewUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by as on 2017-10-10.
 */

public class DataLogService extends AbsService implements ILogService {
    private static DataLogService dataLogService;

    private DataLogService() {
    }

    static final String PATH = Environment.getExternalStorageDirectory()
            .getPath().concat(File.separator).concat("_datalog")
            .concat(File.separator).concat(String.valueOf(PackageUtils.getVersionCode(Plat.app)))
            .concat(File.separator);

    public static DataLogService getInstance() {
        if (dataLogService == null)
            synchronized (DataLogService.class) {
                if (dataLogService == null)
                    dataLogService = new DataLogService();
            }
        return dataLogService;
    }

    static final String Fan32EventFile = PATH + "Event32.txt";

    /**
     * 32指令上报 事件存储
     */
    public synchronized void write32UpEvenData(List<SubDeviceInfo> subDeviceInfos) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\r\n");
        stringBuffer.append(TimeUtils.getTime(TimeUtils.getCurrentTimeInLong(), TimeUtils.SDF_DEFAULT_));

        if (subDeviceInfos != null && subDeviceInfos.size() > 0) {
            for (SubDeviceInfo subDeviceInfo : subDeviceInfos) {
                if (subDeviceInfo != null)
                    stringBuffer.append("__" + subDeviceInfo.guid + "__" + subDeviceInfo.isConnected);
            }
        } else {
            stringBuffer.append("__null");
        }

        File file = new File(Fan32EventFile);
        if (file.exists()) {
            long size = FileUtils.getFileSize(file.getAbsolutePath());
            if (size > 40 * 3000)
                FileUtils.deleteFile(Fan32EventFile);
        }
        byte[] bytes = null;
        try {
            bytes = stringBuffer.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            FileUtils.writeFile(file, new ByteArrayInputStream(bytes), true);
        }
    }

    static public final String PATH_IMG = Environment.getExternalStorageDirectory()
            .getPath().concat(File.separator).concat("_imgt")
            .concat(File.separator);

    public  synchronized void writeImgBitmap(Bitmap bitmap, String fileNmae) {
        if (bitmap == null || StringUtils.isNullOrEmpty(fileNmae))
            return;
        final byte[] bytes = ViewUtils.getBytesFromBitmap(bitmap, Bitmap.CompressFormat.PNG, 100);
        final String path = PATH_IMG + fileNmae + ".png";
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.writeFile(FileUtils.createFile(path), new ByteArrayInputStream(bytes));
            }
        }).start();
    }

    @Override
    public void delate() {
        //删除所有文件
    }
}
