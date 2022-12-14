package com.legent.utils;

import android.os.Environment;
import android.util.Log;

import com.google.common.base.Strings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by sylar on 15/6/23.
 */
public class LogUtils {
    private static boolean debug = true;

    final static SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    final static SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
    public static final String DEFAULT_PATH = "log";
    public static final String DEFAULT_File = "log";
    public static final String DEFAULT_SUFFIX = ".txt";

    public static void logFIleWithTime(String content) {
//        logFIleWithTime(getPath(), content);
    }

    public static void logFIleWithTime(String path, String content) {
        logFIleWithTime(path, content, true);
    }

    public static void logFIleWithTime(String path, String content, boolean isAppend) {

        String time = SDF_TIME.format(Calendar.getInstance().getTime());
        content = String.format("%s:\t %s\n", time, content);
        logFile(path, content, isAppend);
    }

    public static void logFile(String path, String content, boolean isAppend) {
        if (Strings.isNullOrEmpty(content)) return;
        FileUtils.writeFile(path, content, isAppend);
    }


    static String getPath() {
        String path = Environment.getExternalStorageDirectory()
                .getPath()
                .concat(File.separator)
                .concat(DEFAULT_PATH)
                .concat(File.separator)
                .concat(DEFAULT_File)
                .concat(String.format("_%s", SDF_DATE.format(Calendar.getInstance().getTime())))
                .concat(DEFAULT_SUFFIX);


        return path;
    }

    public static void i(String key, String value) {
        if (!debug) return;
        if (!StringUtils.isNullOrEmpty(value))
            Log.i(key, value);
    }


    public static void e(String key, String value) {
        if (!debug) return;
        if (!StringUtils.isNullOrEmpty(value))
            Log.e(key, value);
    }

    public static void out(String value) {
        if (!debug) return;
        if (!StringUtils.isNullOrEmpty(value))
            Log.i("roki_rent", value);
    }

    public static void var(String value) {
        if (!debug) return;
        if (!StringUtils.isNullOrEmpty(value))
            Log.i("var:", value);
    }

    static String getPathDir() {
        String path = Environment.getExternalStorageDirectory()
                .getPath()
                .concat(File.separator)
                .concat(DEFAULT_PATH);


        return path;
    }
    public static void delLog(){
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(getPathDir());
                if (!file.exists()) {
                } else {
                    if (file.isFile()) {
                        // ????????????????????????????????????
                        deleteFile(getPathDir());
                    } else {
                        // ????????????????????????????????????
                        deleteDirectory(getPathDir());
                    }
                }
            }
        });

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                File file = new File(getPathDir());
//                if (!file.exists()) {
//                } else {
//                    if (file.isFile()) {
//                        // ????????????????????????????????????
//                        deleteFile(getPathDir());
//                    } else {
//                        // ????????????????????????????????????
//                        deleteDirectory(getPathDir());
//                    }
//                }
//            }
//        };

    }
    /**
     * ??????????????????
     * @param   filePath    ???????????????????????????
     * @return ????????????????????????true???????????????false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * ???????????????????????????????????????
     * @param   filePath ??????????????????????????????
     * @return  ????????????????????????true???????????????false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //??????filePath?????????????????????????????????????????????????????????
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //???????????????????????????????????????(???????????????)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //???????????????
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //???????????????
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //?????????????????????
        return dirFile.delete();
    }
}
