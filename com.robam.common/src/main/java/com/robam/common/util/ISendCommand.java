package com.robam.common.util;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/4/24.
 * PS: Not easy to write code, please indicate.
 */
public interface ISendCommand {

    void onStart();

    void onPause();

    void onPreSend();

    void onFinish();

    void onRestart();
}
