package com.legent;

import android.content.Context;

import com.legent.services.*;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ResourcesUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;

public class ContextIniter {

    public static Context cx;

    public static void init(Context cx,String appFlag) {
        if (ContextIniter.cx == null) {
            ContextIniter.cx = cx;

            ImageUtils.init(cx,appFlag);
            ToastUtils.init(cx);
            ResourcesUtils.init(cx);
            PreferenceUtils.init(cx);

            CrashLogService.getInstance().init(cx);
            TaskService.getInstance().init(cx);
            RestfulService.getInstance().init(cx);
            ScreenPowerService.getInstance().init(cx);
            ConnectivtyService.getInstance().init(cx);
        }
    }
}
