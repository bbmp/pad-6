package com.legent.utils;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat SDF_DEFAULT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat SDF_DATE = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat SDF_TIME = new SimpleDateFormat(
            "HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat SDF_DEFAULT_ = new SimpleDateFormat(
            "MM-dd-HH-mm-ss", Locale.getDefault());

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #SDF_DEFAULT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, SDF_DEFAULT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #SDF_DEFAULT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * ???????????????????????? date
     *
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String????????????date??????
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime(); // date????????????long??????
            return currentTime;
        }
    }

    /**
     * ???????????????????????????
     */
    public static boolean isToday(long timeInMillis) {
        return DateUtils.isToday(timeInMillis);
    }

    /**
     * ???????????????????????????
     */
    public static boolean isYestoday(long timeInMillis) {

        long mills = timeInMillis + 1000 * 60 * 60 * 24;
        return DateUtils.isToday(mills);

//		Calendar today = Calendar.getInstance(); // ??????
//		// Calendar.HOUR??????12????????????????????? Calendar.HOUR_OF_DAY??????24?????????????????????
//		today.set(Calendar.HOUR_OF_DAY, 0);
//		today.set(Calendar.MINUTE, 0);
//		today.set(Calendar.SECOND, 0);
//
//		Calendar yesterday = (Calendar) today.clone();// ??????
//		yesterday.set(Calendar.DAY_OF_MONTH,
//				today.get(Calendar.DAY_OF_MONTH) - 1);
//
//		Calendar current = Calendar.getInstance();
//		current.setTimeInMillis(timeInMillis);
//
//		return current.before(today) && current.after(yesterday);
    }

    /**
     * ???????????????????????????
     */
    public static boolean isBeforeYestoday(long timeInMillis) {

        long mills = timeInMillis + 1000 * 60 * 60 * 24 * 2;
        return DateUtils.isToday(mills);

//		Calendar yesterday = Calendar.getInstance(); // ??????
//		yesterday.set(Calendar.DAY_OF_MONTH,
//				yesterday.get(Calendar.DAY_OF_MONTH) - 1);
//		yesterday.set(Calendar.HOUR_OF_DAY, 0);
//		yesterday.set(Calendar.MINUTE, 0);
//		yesterday.set(Calendar.SECOND, 0);
//
//		Calendar beforeday = (Calendar) yesterday.clone(); // ??????
//		beforeday.set(Calendar.DAY_OF_MONTH,
//				yesterday.get(Calendar.DAY_OF_MONTH) - 1);
//
//		Calendar current = Calendar.getInstance();
//		current.setTimeInMillis(timeInMillis);
//
//		return current.before(yesterday) && current.after(beforeday);
    }

    static public Calendar getZeroTime(Calendar c) {

        Calendar res = (Calendar) c.clone();
        res.set(Calendar.HOUR_OF_DAY, 0);
        res.set(Calendar.MINUTE, 0);
        res.set(Calendar.SECOND, 0);
        return res;
    }

    /**
     * @param sec ????????????
     * @return ?????? ???00:00?????????
     */
    public static String sec2clock(long sec) {
        if (sec <= 0) {
            return "00:00";
        }
        StringBuilder sb = new StringBuilder();
        long min = sec / 60;
        long second = sec % 60;
        if (min < 10) {
            sb.append("0");
        }
        sb.append(min);
        sb.append(":");
        if (second < 10) {
            sb.append("0");
        }
        sb.append(second);
        return sb.toString();
    }

    /**
     * @param seconds ?????????
     * @return ??????????????????00?????????
     */
    public static String secToString(short seconds) {
        short min = (short) (seconds / 60);
        short sec = (short) (seconds % 60);
        if (sec < 10)
            return min + ":0" + sec;
        else
            return min + ":" + sec;
    }

    /**
     * @param seconds ?????????
     * @return ??????????????????00???00?????????
     */
    public static String secToHourMinSec(short seconds) {
        short hour = (short) (seconds / 3600);
        short min = (short) ((seconds % 3600) / 60);
        short sec = (short) (seconds % 60);
        if (min < 10) {
            if (sec < 10)
                return hour + ":0" + min + ":0" + sec;
            else
                return hour + ":0" + min + ":" + sec;
        } else {
            if (sec < 10)
                return hour + ":" + min + ":0" + sec;
            else
                return hour + ":" + min + ":" + sec;
        }
    }


    static long old = getCurrentTimeInLong();

    /**
     * ????????????????????????
     */
    public static void aa(String s) {
        long newt = getCurrentTimeInLong();
        LogUtils.i("welcomactivity", s + "-->" + newt + " :- " + (newt - old) + "");
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String hourTime;
        String minuteTime;
        if (hour <= 9 && hour >= 0) {
            hourTime = "0" + hour;
            if (minute <= 9 && minute >= 0) {
                minuteTime = "0" + minute;
                return hourTime + ":" + minuteTime;
            } else {
                return hourTime + ":" + minute;
            }
        }else {
            if (minute <= 9 && minute >= 0) {
                minuteTime = "0" + minute;
                return hour + ":" + minuteTime;
            }else {
                return hour + ":" + minute;
            }
        }
    }
}
