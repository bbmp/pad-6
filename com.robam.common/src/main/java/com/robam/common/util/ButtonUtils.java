package com.robam.common.util;


import com.legent.VoidCallback2;
import com.legent.plat.Plat;
import com.legent.utils.LogUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by as on 2017-02-22.
 */

public class ButtonUtils {
    static private boolean ButtonLock;

    public static boolean isButtonLock() {
        return isButtonLock(1000);
    }

    public static boolean isButtonLock(int millisecond) {
        if (!ButtonLock) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ButtonLock = false;
                }
            }, millisecond);
            ButtonLock = true;
            return false;
        }
        return ButtonLock;
    }

    static private Set hashSet = Collections.synchronizedSet(new HashSet<Object>());
    static private Map treemap = Collections.synchronizedMap(new HashMap());

    public static boolean isWaitLock(Object object) {
        return isWaitLock(object, 1000);
    }

    public static synchronized boolean isWaitLock(final Object object, int millisecond) {
        if (Plat.DEBUG) {
            LogUtils.i("20170814", "size:" + hashSet.size());
            LogUtils.i("20170814", "" + object);
        }
        if (!hashSet.contains(object)) {
            if (Plat.DEBUG)
                LogUtils.i("20170814", "null:" + object);
            hashSet.add(object);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (ButtonUtils.class) {
                        try {
                            hashSet.remove(object);
                        } catch (Exception e) {
                            hashSet.clear();
                        } finally {
                            if (Plat.DEBUG)
                                LogUtils.i("20170814", "remove remove remove" + hashSet.contains(object));
                            hashSet.remove(object);
                            if (Plat.DEBUG)
                                LogUtils.i("20170814", "remove remove remove" + hashSet.contains(object));
                        }
                    }
                }
            }, millisecond);
            return false;
        }
        if (Plat.DEBUG)
            LogUtils.i("20170814", "not null:" + hashSet.contains(object));
        return true;
    }

    public static synchronized void isWaitLockEndCallBack(final Object object, int millisecond, VoidCallback2 callback3) {
        if (Plat.DEBUG)
            LogUtils.i("20170814", "" + object);
        try {
            if (treemap.get(object) == null) {
                if (Plat.DEBUG)
                    LogUtils.i("20170814", "NULL");
                treemap.put(object, callback3);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (ButtonUtils.class) {
                            try {
                                VoidCallback2 cal = (VoidCallback2) treemap.get(object);
                                cal.onCompleted();
                            } catch (Exception e) {
                                if (Plat.DEBUG)
                                    LogUtils.i("20170814", "error " + e.getMessage());
                                treemap.clear();
                            } finally {
                                treemap.remove(object);
                                if (Plat.DEBUG)
                                    LogUtils.i("20170814", "remove remove remove ");
                            }
                        }
                    }
                }, millisecond);
            } else {
                if (Plat.DEBUG)
                    LogUtils.i("20170814", "NOT NULL" + treemap.get(object));
                treemap.put(object, callback3);
            }
        } catch (Exception e) {
            if (Plat.DEBUG)
                LogUtils.i("20170814", "Exception " + e.getMessage());
        }

    }
}
