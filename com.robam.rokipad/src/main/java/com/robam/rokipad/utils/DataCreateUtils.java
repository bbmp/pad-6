package com.robam.rokipad.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2019/2/26.
 */

public class DataCreateUtils {
    //灶具时间控制
    public static List<String> createStoveTimer(){
        List<String> timeList = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            timeList.add(i + "");
        }

        return timeList;
    }

    //灶具9B37时间控制
    public static List<String> create9b37StoveTimer() {
        List<String> timeList = new ArrayList<>();
        for (int i = 1; i <= 90; i++) {
            timeList.add(i + "");
        }

        return timeList;
    }

    //灶具9B37时间控制
    public static List<String> create9w851StoveTimer() {
        List<String> timeList = new ArrayList<>();
        for (int i = 1; i <= 180; i++) {
            timeList.add(i + "");
        }

        return timeList;
    }
}
