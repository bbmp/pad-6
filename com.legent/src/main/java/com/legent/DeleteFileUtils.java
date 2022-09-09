package com.legent;

import android.os.Environment;

import com.legent.utils.LogUtils;

import java.io.File;

/**
 * Created by Administrator on 2018/6/7.
 */
public class DeleteFileUtils {
    /**
     * Created by 75213 on 2017/11/1.
     */

    public static File getPathFile(String path){
        String apkName = path.substring(path.lastIndexOf("/"));
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkName);
        LogUtils.i("FilePath","outputFile.getAbsolutePath():"+outputFile.getAbsolutePath() + " apkName:"+ apkName);
        return outputFile;
    }

    public static void rmoveFile(String path){
        File file = getPathFile(path);
        file.delete();
    }
}
