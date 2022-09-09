package com.legent.plat.utils;

import android.os.Environment;

import com.legent.plat.Plat;
import com.legent.utils.FileUtils;
import com.legent.utils.LogUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author as rent
 */
public class DownUtil {
    private String url;
    private String filename;
    private int threadNum;
    private DownloadThread[] threads;
    private int fileSize;
    final static String PATH = Environment.getExternalStorageDirectory()
            .getPath().concat(File.separator).concat("_default")
            .concat(File.separator);

    public DownUtil(String url, String filename, int threadNum) {
        this(url, PATH, filename, threadNum);
    }

    public DownUtil(String url, String dirname, String filename, int threadNum) {
        this.url = url;
        this.threadNum = threadNum;
        threads = new DownloadThread[threadNum];
        this.filename = dirname + filename;
        FileUtils.makeDirs(this.filename);
        if (Plat.DEBUG)
            LogUtils.i("patchpro", "DownUtil " + filename);
    }

    public String download() throws Exception {
        if (threadNum < 0 || threadNum > 10)   //线程数量限制
            return "";
        URL url = new URL(this.url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        fileSize = conn.getContentLength();
        //LogUtils.out("文件大小：" + fileSize);
        conn.disconnect();
        int currentPartSize = fileSize / threadNum;
        //LogUtils.out("线程数量：" + threadNum);
        //LogUtils.out("每部分大小：" + currentPartSize);
        if (currentPartSize <= 1)
            return "";
        int restPartSize = fileSize % threadNum;
        //LogUtils.out("平均后剩余：" + restPartSize);
        RandomAccessFile file = new RandomAccessFile(filename, "rw");
        file.setLength(fileSize);
        file.close();
        for (int i = 0; i < threadNum; i++) {
            int startPos = i * currentPartSize;
            RandomAccessFile currentPart = new RandomAccessFile(filename,
                    "rw");
            currentPart.seek(startPos);
            if (i == (threadNum - 1))
                currentPartSize += restPartSize;
            threads[i] = new DownloadThread(startPos, currentPartSize,
                    currentPart);
            threads[i].start();
        }
        return this.filename;
    }

    public double getCompleteRate() {
        int sumSize = 0;
        for (int i = 0; i < threadNum; i++) {
            sumSize += threads[i].length;
        }
        return sumSize * 1.0 / fileSize;
    }

    private class DownloadThread extends Thread {
        private int startPos;
        private int currentPartSize;
        private RandomAccessFile currentPart;
        public int length;

        public DownloadThread(int startPos, int currentPartSize,
                              RandomAccessFile currentPart) {
            this.startPos = startPos;
            this.currentPartSize = currentPartSize;
            this.currentPart = currentPart;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(DownUtil.this.url);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                InputStream inStream = conn.getInputStream();
                // 跳过startPos个字节，表明该线程只下载自己负责哪部分文件。（手机端pad端公用）
                inStream.skip(this.startPos);
                byte[] buffer = new byte[1024];
                int hasRead = 0;
                while (length < currentPartSize
                        && (hasRead = inStream.read(buffer)) != -1) {
                    if (length + hasRead <= currentPartSize) {
                        currentPart.write(buffer, 0, hasRead);
                        length += hasRead;
                    } else {
                        currentPart.write(buffer, 0, currentPartSize - length);
                        length = currentPartSize;
                    }
                }
                currentPart.close();
                inStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
