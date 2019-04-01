package com.jyx.mylibrary.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.jyx.mylibrary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author jyx
 * @ctime 2017/12/8:10:05
 * @explain
 */

public class TimeTransformUtils {

    public static final String TAG = "TimeTransformUtils";

    public static final String YMD = "yyyyMMdd";
    public static final String YMD_YEAR = "yyyy";
    public static final String YMD_BREAK = "yyyy-MM-dd";
    public static final String YMDHMS = "yyyyMMddHHmmss";
    public static final String YMDHMS_BREAK = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHMS_BREAK_HALF = "yyyy-MM-dd HH:mm";

    /**
     * 计算时间差
     */
    public static final int CAL_MINUTES = 1000 * 60;
    public static final int CAL_HOURS = 1000 * 60 * 60;
    public static final int CAL_DAYS = 1000 * 60 * 60 * 24;

    /**
     * 获取当前时间格式化后的值
     *
     * @param pattern
     * @return
     */
    public static String getNowDateText(String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    /**
     * 获取日期格式化后的值
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getDateText(Date date, String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 获取格式化日期
     *
     * @param timeStr
     * @param pattern
     * @return
     */
    public static String getDateText(Object timeStr, String pattern) {
        if (StringUtils.isObjectEmpty(timeStr)) {
            return "";
        }
        Log.e(TAG, "time：" + timeStr.toString());
        String data13Time = date13Time(timeStr.toString());
        Log.e(TAG, "date13:" + data13Time);
        if (StringUtils.isObjectEmpty(data13Time)) {
            return "";
        }

        Date date = new Date(Long.valueOf(data13Time));
        return getDateText(date, pattern);
    }

    /**
     * 字符串时间转换成Date格式
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date getDate(String date, String pattern) throws ParseException {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 获取时间戳
     *
     * @param date
     * @return
     */
    public static Long getTime(Date date) {
        return date.getTime();
    }

    /**
     * 计算时间差
     *
     * @param startDate
     * @param endDate
     * @param calType   计算类型,按分钟、小时、天数计算
     * @return
     */
    public static int calDiffs(Date startDate, Date endDate, int calType) {
        Long start = TimeTransformUtils.getTime(startDate);
        Long end = TimeTransformUtils.getTime(endDate);
        int diff = (int) ((end - start) / calType);
        return diff;
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param startDate
     * @return
     */
    public static String timeDiffText(Date startDate) {
        startDate = date13Time(startDate);

        if (StringUtils.isObjectEmpty(startDate)) {
            return "";
        }

        int calDiffs = TimeTransformUtils.calDiffs(startDate, Calendar.getInstance().getTime(), TimeTransformUtils.CAL_MINUTES);
        if (calDiffs == 0) {
            return "刚刚";
        }
        if (calDiffs < 60) {
            return calDiffs + "分钟前";
        }
        calDiffs = TimeTransformUtils.calDiffs(startDate, Calendar.getInstance().getTime(), TimeTransformUtils.CAL_HOURS);
        if (calDiffs < 24) {
            return calDiffs + "小时前";
        }
        if (calDiffs < 48) {
            return "昨天";
        }
        return TimeTransformUtils.getDateText(startDate, TimeTransformUtils.YMD_BREAK);
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @return
     */
    public static String timeDiffText(String startDate) {
        if (StringUtils.isObjectEmpty(startDate)) {
            return "";
        }
        startDate = date13Time(startDate);

        if (StringUtils.isObjectEmpty(startDate)) {
            return "";
        }

        Date date = new Date(Long.valueOf(startDate));
        int calDiffs = TimeTransformUtils.calDiffs(date, Calendar.getInstance().getTime(), TimeTransformUtils.CAL_MINUTES);
        if (calDiffs == 0) {
            return "刚刚";
        }
        if (calDiffs < 60) {
            return calDiffs + "分钟前";
        }
        calDiffs = TimeTransformUtils.calDiffs(date, Calendar.getInstance().getTime(), TimeTransformUtils.CAL_HOURS);
        if (calDiffs < 24) {
            return calDiffs + "小时前";
        }
        if (calDiffs / 24 < 8) {
            return calDiffs / 24 + "天前";
        }
        return TimeTransformUtils.getDateText(startDate, TimeTransformUtils.YMD_BREAK);
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @return
     */
    public static String timeDiffText(String pattern, String startDate) {
        if (StringUtils.isObjectEmpty(startDate)) {
            return "";
        }
        startDate = date13Time(startDate);

        if (StringUtils.isObjectEmpty(startDate)) {
            return "";
        }

        Date date = new Date(Long.valueOf(startDate));
        int calDiffs = TimeTransformUtils.calDiffs(date, Calendar.getInstance().getTime(), TimeTransformUtils.CAL_MINUTES);
        if (calDiffs == 0) {
            return "刚刚";
        }
        if (calDiffs < 60) {
            return calDiffs + "分钟前";
        }
        calDiffs = TimeTransformUtils.calDiffs(date, Calendar.getInstance().getTime(), TimeTransformUtils.CAL_HOURS);
        if (calDiffs < 24) {
            return calDiffs + "小时前";
        }
        if (calDiffs / 24 < 8) {
            return calDiffs / 24 + "天前";
        }
        return TimeTransformUtils.getDateText(startDate, StringUtils.isObjectEmpty(pattern) ? TimeTransformUtils.YMD_BREAK : pattern);
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param timeStr
     * @return
     */
    public static String timeDiffText(Object timeStr) {
        timeStr = date13Time(timeStr.toString());
        Date startDate = new Date(Long.valueOf(timeStr.toString()));
        if (StringUtils.isObjectEmpty(startDate)) {
            return "";
        }

        int calDiffs = TimeTransformUtils.calDiffs(startDate, Calendar.getInstance().getTime(), TimeTransformUtils.CAL_MINUTES);
        if (calDiffs == 0) {
            return "刚刚";
        }
        if (calDiffs < 60) {
            return calDiffs + "分钟前";
        }
        calDiffs = TimeTransformUtils.calDiffs(startDate, Calendar.getInstance().getTime(), TimeTransformUtils.CAL_HOURS);
        if (calDiffs < 24) {
            return calDiffs + "小时前";
        }
        if (calDiffs < 48) {
            return "昨天";
        }
        return TimeTransformUtils.getDateText(startDate, TimeTransformUtils.YMD_BREAK);
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String timeDiffText(Date startDate, Date endDate) {
        int calDiffs = TimeTransformUtils.calDiffs(startDate, endDate, TimeTransformUtils.CAL_MINUTES);
        if (calDiffs == 0) {
            return "刚刚";
        }
        if (calDiffs < 60) {
            return calDiffs + "分钟前";
        }
        calDiffs = TimeTransformUtils.calDiffs(startDate, endDate, TimeTransformUtils.CAL_HOURS);
        if (calDiffs < 24) {
            return calDiffs + "小时前";
        }
        if (calDiffs < 48) {
            return "昨天";
        }
        return TimeTransformUtils.getDateText(startDate, TimeTransformUtils.YMD_BREAK);
    }

    /**
     * 计算时间差值以某种约定形式显示（类同微信朋友圈的时间节点）
     *
     * @param startDate
     * @return
     */
    public static SpannableStringBuilder timeDiffTextNews(Context context, Date startDate) {

        String sTime = "";//时间值-今天，昨天
        int sDay = 0;//具体天
        int sMonth = 0;//具体月

        //时间点
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        //系统当前时间
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        //昨天
        Calendar yCalender = Calendar.getInstance();
        yCalender.set(Calendar.DATE, yCalender.get(Calendar.DATE) - 1);

        //如果是同一年，进行天的比较
        if (calendar.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = pre.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);//与当前系统时间是否是同一天
            if (diffDay == 0) {
                sTime = "今天";
            } else if (diffDay == 1) {
                sTime = "昨天";
            } else {
                sDay = getNowDay(startDate);//时间点的当前月的几号
                sMonth = getNowMonth(startDate);//时间点的当前月
            }
        } else {
            if (calendar.get(Calendar.YEAR) == (yCalender.get(Calendar.YEAR))) {//是否是上一年的最后一天
                int diffDay = yCalender.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);//昨天是否是上一年的最后一天
                if (diffDay == 0) {
                    sTime = "昨天";
                } else {
                    sDay = getNowDay(startDate);//时间点的当前月的几号
                    sMonth = getNowMonth(startDate);//时间点的当前月
                }
            } else {
                sDay = getNowDay(startDate);//时间点的当前月的几号
                sMonth = getNowMonth(startDate);//时间点的当前月
            }
        }

        if (sDay == 0) {//如果时间点的天为0 ，显示sTime
            SpannableStringBuilder stringBuilder = SpannableStringUtils.getBuilder(sTime)
                    .setBold()
                    .setForegroundColor(context.getResources().getColor(R.color.black))
                    .setProportion(2)
                    .create(context);
            return stringBuilder;
        } else {//如果不为0 ，说明有具体的天和月
            SpannableStringBuilder stringBuilder = SpannableStringUtils.getBuilder(sDay + "")
                    .setBold()//天，加粗
                    .setForegroundColor(context.getResources().getColor(R.color.black))
                    .setProportion(2)//天，字体放大2倍
                    .append(context, sMonth + "月")//追加月份
                    .setForegroundColor(context.getResources().getColor(R.color.black))
                    .setProportion(1)//月份，字体比例1倍
                    .setBold()
                    .create(context);
            return stringBuilder;
        }
    }

    /**
     * 计算时间差值以某种约定形式显示是否是同一天
     *
     * @param startDate
     * @return
     */
    public static Boolean timeSame(Date startDate, Date endDate) {

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        int startYear = startCalendar.get(Calendar.YEAR);
        int startDay = startCalendar.get(Calendar.DAY_OF_YEAR);
        //系统当前时间
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        int endYear = endCalendar.get(Calendar.YEAR);
        int endDay = endCalendar.get(Calendar.DAY_OF_YEAR);

        //与当前系统时间比较是否是同一天
        if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                startCalendar.get(Calendar.DAY_OF_YEAR) == endCalendar.get(Calendar.DAY_OF_YEAR)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 返回当前月份
     *
     * @return
     */
    public static int getNowMonth(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前月号
     *
     * @return
     */
    public static int getNowDay(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getNowYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 显示某种约定后的时间值,类似微信朋友圈发布说说显示的时间那种
     *
     * @param date
     * @return
     */
    public static String showTimeText(Date date) {
        return TimeTransformUtils.timeDiffText(date, new Date());
    }

    /**
     * 数据13补全
     *
     * @param date
     */
    public static Date date13Time(Date date) {
        int i = 0;
        if (!StringUtils.isObjectEmpty(date)) {
            if ((date.getTime() + "").length() < 13) {
                if (i > 3) {
                    return date;
                }
                i++;
                date.setTime(date.getTime() * 10);
                return date13Time(date);
            } else {
                return date;
            }
        } else {
            return date;
        }
    }

    public static String date13Time(String timeStr) {
        int i = 0;
        if (!StringUtils.isObjectEmpty(timeStr)) {
            if (timeStr.length() < 13) {
                if (i > 3) {
                    return timeStr;
                }
                i++;
                timeStr = timeStr + "0";
                return date13Time(timeStr);
            } else {
                return timeStr;
            }
        }
        return timeStr;
    }
}
