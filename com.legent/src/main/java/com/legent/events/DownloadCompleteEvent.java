package com.legent.events;

import android.net.Uri;

/**
 * Created by Administrator on 2018/4/11.
 */
public class DownloadCompleteEvent {
    private Uri uri;
    private boolean isDownComplete;
    private String  newVersionDes;//新版本的描述

    public DownloadCompleteEvent(Uri uri, boolean isDownComplete,String  newVersionDes) {
        this.uri = uri;
        this.isDownComplete = isDownComplete;
        this.newVersionDes = newVersionDes;
    }

    public Uri getUri() {
        return uri;
    }

    public boolean isDownComplete() {
        return isDownComplete;
    }

    public String getNewVersionDes() {
        return newVersionDes;
    }
}
