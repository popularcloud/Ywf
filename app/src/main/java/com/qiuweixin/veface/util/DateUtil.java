package com.qiuweixin.veface.util;


import android.util.Log;

import com.qiuweixin.veface.R;
import com.qiuweixin.veface.base.App;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public interface Unit {
        int MILLI_SECOND = 1;
        int SECOND = 1000;
    }

    public static String DATE_TEMPLATE = App.self.getString(R.string.date_format);
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_TEMPLATE, Locale.CHINA);

    public static String parseTimeStamp(Long timeStamp, int unit) {
        if (timeStamp == null) {
            return "";
        }

        return parseTimeMillis(timeStamp * unit);
    }

    public static String parseTimeMillis(Long timeMillis) {
        if (timeMillis == null) {
            return "";
        }

        return DATE_FORMAT.format(new Date(timeMillis));
    }

    /**
     *
     * @param beginTimeMillis
     * @param endingTimeMillis
     * @return 当前时间是否在指定的时间范围内
     */
    public static boolean expireIn(Long beginTimeMillis, Long endingTimeMillis) {
        long currentTime = System.currentTimeMillis();

        Log.d(DateUtil.class.getName(), "开始时间：" + beginTimeMillis);
        Log.d(DateUtil.class.getName(), "结束时间：" + endingTimeMillis);
        Log.d(DateUtil.class.getName(), "当前时间：" + currentTime);

        return (beginTimeMillis == null || beginTimeMillis <= currentTime)
                && (endingTimeMillis == null || endingTimeMillis <= 0 || endingTimeMillis >= currentTime);
    }

    public static String formatData(Long times){
        if(times == null){
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new java.sql.Date(times * 1000L));
    }

    public static String formatDataYMDHM(Long times){
        if(times == null){
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(new java.sql.Date(times * 1000L));
    }

    public static String formatDataHM(Long times){
        if(times == null){
            return "";
        }
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(new java.sql.Date(times * 1000L));
    }

    public static String formaddOrMinusDay(int addOrMinusDay){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_YEAR,addOrMinusDay);
        Date date = calendar.getTime();

        return sdf.format(date);
    }

    public static String formaddOrMinusMonth(int addOrMinusDay){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        calendar.add(Calendar.DAY_OF_YEAR,addOrMinusDay);
        Date date = calendar.getTime();

        return sdf.format(date);
    }

    /**
     * 将字符串转换为时间戳
     * @param time yyyy-mm格式
     * @return
     */
    public static Long getTime(String time) {
        Long re_time = 0L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = sdf.parse(time);
            re_time = d.getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }


    /** 日期格式 */
    private final static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /** 时间格式 */
    private final static ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 友好的方式显示时间
     */
    public static String friendlyFormat(long myTime){
        Date date = new Date(myTime * 1000L);
        if(date == null)
            return ":)";
        Calendar now = Calendar.getInstance();
        String time = new SimpleDateFormat("HH:mm").format(date);

        // 第一种情况，日期在同一天
        String curDate = dateFormat.get().format(now.getTime());
        String paramDate = dateFormat.get().format(date);
        if(curDate.equals(paramDate)){
            int hour = (int) ((now.getTimeInMillis() - date.getTime()) / 3600000);
           /* if(hour > 0)
                return time;*/
            int minute = (int) ((now.getTimeInMillis() - date.getTime()) / 60000);
            if (minute < 2)
                return "刚刚";
            if (minute < 60)
                return minute + "分钟前";

            return hour + "小时前";
        }

        // 第二种情况，不在同一天
        int days = (int) ((getBegin(getDate()).getTime() - getBegin(date).getTime()) / 86400000 );
        if(days <= 7)
            return days + "天前";
        return dateToStrMD(date);
    }

    /**
     * 返回日期的0点:2012-07-07 20:20:20 --> 2012-07-07 00:00:00
     */
    public static Date getBegin(Date date){
        return strToTime(dateToStr(date)+" 00:00:00");
    }

    /**
     * 日期转换为字符串:yyyy-MM-dd
     */
    public static String dateToStr(Date date){
        if(date != null)
            return dateFormat.get().format(date);
        return null;
    }

    /**
     * 日期转换为字符串:MM-dd
     */
    public static String dateToStrMD(Date date){
        if(date != null)
            return new SimpleDateFormat("MM-dd").format(date);
        return null;
    }

    /**
     * 时间转换为字符串:yyyy-MM-dd HH:mm:ss
     */
    public static String timeToStr(Date date){
        if(date != null)
            return timeFormat.get().format(date);
        return null;
    }

    /**
     * 获取当前时间:Date
     */
    public static Date getDate(){
        return new Date();
    }

    /**
     * 字符串转换为时间:yyyy-MM-dd HH:mm:ss
     */
    public static Date strToTime(String str){
        Date date = null;
        try {
            date = timeFormat.get().parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
