package com.robam.rokipad.utils;

/**
 * Created by Dell on 2019/2/27.
 */

public interface CallBackCommand {

    void success();

    void fail(Throwable t);
}
