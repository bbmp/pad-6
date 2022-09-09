package com.robam.rokipad.service.cooking;

import com.robam.common.Utils;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.services.StoveCookTaskService;

import java.util.Calendar;

import static com.robam.common.Utils.getDefaultFan;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/4/11.
 * PS: 灶具（手动，自动）烹饪模式封装.
 */
public class AutoCookingStoveTaskService extends StoveCookTaskService {

    public static AutoCookingStoveTaskService mInstance;
//    public static final int HM_COOK = 1;//手动模式
//    public static final int AUTO_COOK = 2;//自动模式


    public static AutoCookingStoveTaskService getInstance() {

        if (mInstance == null) {
            synchronized (AutoCookingStoveTaskService.class) {
                if (mInstance == null) {
                    mInstance = new AutoCookingStoveTaskService();
                }
            }
        }
        return mInstance;
    }


    @Override
    public void start(Stove.StoveHead stoveHead, Recipe book) {
        this.book = book;
        this.stoveHead = stoveHead;
        this.startTime = Calendar.getInstance().getTimeInMillis();
        if (stoveHead != null) {
            stove = stoveHead.parent;
            fan = (AbsFan) stove.getParent();
        } else {
            fan = getDefaultFan();
            stove = Utils.getDefaultStove()[0];
        }
        steps = book.getJs_cookSteps();
        stepIndex = -1;
        isRunning = true;
        next();
        onShowCookingView();
        onStart();
    }
}
