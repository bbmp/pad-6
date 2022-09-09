package com.robam.rokipad.utils;

import com.robam.rokipad.listener.VersionUpdateImpl;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/15.
 * PS: 版本更新.
 */
public class VersionUpdate {
    /**
     * 请求服务器，检查版本是否可以更新
     *
     * @param versionUpdate
     */
    public static void checkVersion(final VersionUpdateImpl versionUpdate) {
        //从网络请求获取到的APK下载路径
        versionUpdate.bindService("http://www.zhaoshangdai.com/file/android.apk");
    }
}
