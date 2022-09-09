package com.robam.rokipad.utils;

import android.content.Context;

import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.rokipad.R;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/5/24.
 * PS: Not easy to write code, please indicate.
 */
public class FanAssistUtils {

    public static String getDeviceLevelByName(Context context, int level) {

        switch (level) {
            case FanStatus.LEVEL_EMPTY:
                return context.getString(R.string.cook_fan_close_level_text);
            case FanStatus.LEVEL_SMALL:
            case FanStatus.LEVEL_SMALL_Two:
                return context.getString(R.string.device_fan_small_text);
            case FanStatus.LEVEL_MIDDLE:
                return context.getString(R.string.device_fan_middle_text);
            case FanStatus.LEVEL_BIG:
                return context.getString(R.string.device_fan_big_text);
        }
        return "";
    }

    public static String getRecipeLevelByName(Context context, int level) {

        switch (level) {
            case FanStatus.LEVEL_EMPTY:
                return context.getString(R.string.cook_fan_close_level_text);
            case FanStatus.LEVEL_SMALL:
            case FanStatus.LEVEL_SMALL_Two:
                return context.getString(R.string.cook_fan_weak_file_text);
            case FanStatus.LEVEL_MIDDLE:
                return context.getString(R.string.cook_fan_season_text);
            case FanStatus.LEVEL_BIG:
                return context.getString(R.string.cook_fan_stir_fry_text);
        }
        return "";
    }
}
