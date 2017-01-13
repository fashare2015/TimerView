package com.fashare.timer_view;

/**
 * Created by jinliangshan on 17/1/11.
 * 时间信息 —— 时、分、秒
 */
public class TimeInfo {
    public static final int MILLIS_PER_SEC = 1000;
    public static final int SEC_PER_MIN = 60;
    public static final int MIN_PER_HOUR = 60;

    private long mHour, mMinute, mSecond;

    private TimeInfo() {}

    public static TimeInfo make(long curTime){
        long totalMillis = curTime;
        if(totalMillis <= 0)
            return new TimeInfo(0, 0, 0);

        long totalSecond = totalMillis/MILLIS_PER_SEC;
        long totalMinute = totalSecond/SEC_PER_MIN;
        long totalHour = totalMinute/MIN_PER_HOUR;

        return new TimeInfo(
                totalHour,
                totalMinute % MIN_PER_HOUR,
                totalSecond % SEC_PER_MIN
        );
    }

    public TimeInfo(long hour, long minute, long second) {
        mHour = hour;
        mMinute = minute;
        mSecond = second;
    }

    public long getHour() {
        return mHour;
    }

    public long getMinute() {
        return mMinute;
    }

    public long getSecond() {
        return mSecond;
    }

    public String getFormatedHour() {
        return format(mHour);
    }

    public String getFormatedMinute() {
        return format(mMinute);
    }

    public String getFormatedSecond() {
        return format(mSecond);
    }

    /**
     * 格式化, 不足两位的补全, 如 "3" -> "03"
     * @param time
     * @return
     */
    public static String format(long time){
        String result = time + "";
        if(result.length() == 1)
            result = "0" + result;
        return result;
    }

    @Override
    public String toString() {
        return String.format("TimeInfo[mHour = %s, mMinute = %s, mSecond = %s]", mHour, mMinute, mSecond);
    }
}
