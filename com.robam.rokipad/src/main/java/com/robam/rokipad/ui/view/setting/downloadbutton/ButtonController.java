package com.robam.rokipad.ui.view.setting.downloadbutton;

/**
 * Created by tanfujun on 10/26/16.
 */

public interface ButtonController {
    int getPressedColor(int color);

    int getLighterColor(int color);

    int getDarkerColor(int color);

    boolean enablePress();

    boolean enableGradient();


}

