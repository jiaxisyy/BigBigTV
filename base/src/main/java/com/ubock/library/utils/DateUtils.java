package com.ubock.library.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by chenGD on 2017/3/29.
 */
public class DateUtils {

    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String HHMMSS = "HH:mm:ss";
    public static final String hhMMSS = "hh:mm:ss";
    public static final String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认时区为北京时区
     */
    private final static TimeZone defaultTimeZone = TimeZone.getTimeZone("GMT+8:00");

    /**
     * 将date转换成string
     */
    public static String dateToStringFormat(Date date, String params) {
        SimpleDateFormat format = new SimpleDateFormat(params, Locale.ENGLISH);
        format.setTimeZone(defaultTimeZone);
        return format.format(date);
    }

    /**
     * 字符串获取对应的date
     *
     * @param dateString
     * @return
     */
    public static Date stringToDateFormat(String dateString, String format) {
        try {
            SimpleDateFormat localDate = new SimpleDateFormat(format);
            localDate.setTimeZone(defaultTimeZone);
            return localDate.parse(dateString);
        } catch (Exception localException) {
            //Log.e("localException", "-->"+localException.getMessage());
        }
        return null;
    }

    /**
     * 字符串日期时间中取出日期
     *
     * @param dateString
     * @return
     */
    public static String stringDateFormat(String dateString, String format) {
        try {
            SimpleDateFormat localDate = new SimpleDateFormat(format);
            localDate.setTimeZone(defaultTimeZone);
            Date date = stringToDateFormat(dateString, format);
            return localDate.format(date);
        } catch (Exception localException) {
            Log.e("localException", "-->" + localException.getMessage());
        }
        return null;
    }

    /**
     * 字符串日期时间中取出不带日期的时间，如HH:mm
     *
     * @param dateString
     * @return
     */
    public static String stringTimeFormat(String dateString, String format) {
        try {
            SimpleDateFormat localDate = new SimpleDateFormat(format);
            localDate.setTimeZone(defaultTimeZone);
            Date date = stringToDateFormat(dateString, YYYYMMDD_HHMMSS);
            return localDate.format(date);
        } catch (Exception localException) {
            Log.e("localException", "-->" + localException.getMessage());
        }
        return null;
    }

    public static String timeStampToString(Long timeStamp, String format) {
        try {
            SimpleDateFormat localDate = new SimpleDateFormat(format);
            localDate.setTimeZone(defaultTimeZone);
            String date = localDate.format(timeStamp);
            return date;
        } catch (Exception localException) {
            Log.e("localException", localException.getMessage());
        }
        return null;
    }

    public static Date LongStampToDateFormat(Long timeStamp, String format) {
        try {
            SimpleDateFormat localDate = new SimpleDateFormat(format);
            localDate.setTimeZone(defaultTimeZone);
            String date = localDate.format(timeStamp);
            return localDate.parse(date);
        } catch (Exception localException) {
            Log.e("localException", localException.getMessage());
        }
        return null;
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(YYYYMMDD_HHMMSS);
        sDateFormat.setTimeZone(defaultTimeZone);
        return sDateFormat.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(YYYYMMDD);
        sDateFormat.setTimeZone(defaultTimeZone);
        return sDateFormat.format(new Date());
    }

    /**
     * @param fromDate
     * @return UTC时间转换成时间戳
     */
    public static String timeToUnix(String fromDate) {
        String res = fromDate;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = simpleDateFormat.parse(fromDate);
            long ts = date.getTime();
            res = String.valueOf(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * @param fromDate
     * @return UTC时间转换成时间戳
     */
    public static long timeToUnixLong(String fromDate) {
        String res = fromDate;
        long ts = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = simpleDateFormat.parse(fromDate);
            ts = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ts;
    }


    /**
     * @param fromDate
     * @return 时间戳格式化成本地北京时间 yyyy-MM-dd HH:mm:ss
     */
    public static String unixToLocalTime(String fromDate) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(defaultTimeZone);
        long lt = new Long(fromDate);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * @param fromDate
     * @return 时间戳格式化成本地北京时间 yyyy-MM-dd HH:mm:ss
     */
    public static String unixToLocalTime(String fromDate, String partner) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(partner);
        simpleDateFormat.setTimeZone(defaultTimeZone);
        long lt = new Long(fromDate);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * @param fromDate
     * @return 时间戳格式化成本地北京时间 yyyy-MM-dd HH:mm:ss
     */
    public static String unixToUtcTime(String fromDate, String partner) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(partner);
        long lt = new Long(fromDate);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * @param fromDate
     * @return 时间戳格式化成本地北京时间 yyyy-MM-dd HH:mm:ss
     */
    public static String unixToTime(String fromDate, String partner) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(partner);
        long lt = new Long(fromDate);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     *
     * @return UTC yyyy-MM-dd HH:mm:ss 转换成北京 yyyy-MM-dd HH:mm:ss
     */
    public static String utcToLocalTime(String utcTime) {
        if(!TextUtils.isEmpty(utcTime)){
            String utcUnix = timeToUnix(utcTime);
            return unixToLocalTime(utcUnix);
        }
        return null;
    }

    /**
     *
     * @return UTC yyyy-MM-dd HH:mm:ss 转换成北京 yyyy-MM-dd HH:mm:ss
     */
    public static String utcToLocalTime(String utcTime, String partner) {
        if(!TextUtils.isEmpty(utcTime)){
            String utcUnix = timeToUnix(utcTime);
            return unixToLocalTime(utcUnix,partner);
        }
        return null;
    }

    /**
     *
     * @return UTC yyyy-MM-dd HH:mm:ss 转换成北京 yyyy-MM-dd HH:mm:ss
     */
    public static String utcFormat(String utcTime, String partner) {
        if(!TextUtils.isEmpty(utcTime)){
            String utcUnix = timeToUnix(utcTime);
            return unixToUtcTime(utcUnix,partner);
        }
        return null;
    }


    /**
     * 当地时间 ---> UTC时间
     *  localTime  yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String LocalToUtc(String localTime){
        if(!TextUtils.isEmpty(localTime)){
            long utcUnix = localToUnix(localTime);
            //utcUnix = utcUnix + 8*60*60;
            return unixToUTcTime(utcUnix);
        }
        return null;

    }

    public static long localToUnix(String fromDate) {
        long ts = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            simpleDateFormat.setTimeZone(defaultTimeZone);
            Date date = simpleDateFormat.parse(fromDate);
            ts = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ts;
    }

    public static long localToUtcUnix(String fromDate, String partner) {
        long ts = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(partner);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = simpleDateFormat.parse(fromDate);
            ts = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ts;
    }

    /**
     * @param fromDate
     * @return 时间戳格式化成本地utc时间 yyyy-MM-dd HH:mm:ss
     */
    public static String unixToUTcTime(long fromDate) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(fromDate);
        res = simpleDateFormat.format(date);
        return res;
    }
    /**
     * @param fromDate
     * @return 时间戳格式化成本地utc时间时长 HH:mm
     */
    public static String unixToUTcTimeDuration(long fromDate) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(fromDate);
        res = simpleDateFormat.format(date);
        return res;
    }


        /**设置每个阶段时间*/
        private static final int seconds_of_1minute = 60;

        private static final int seconds_of_30minutes = 30 * 60;

        private static final int seconds_of_1hour = 60 * 60;

        private static final int seconds_of_1day = 24 * 60 * 60;

        private static final int seconds_of_3days = seconds_of_1day * 3;

        private static final int seconds_of_30days = seconds_of_1day * 30;

        private static final int seconds_of_6months = seconds_of_30days * 6;

        private static final int seconds_of_1year = seconds_of_30days * 12;

        /**
         * 格式化时间
         * @param mTime
         * @return
         */
        public static String getTimeRange(String mTime) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                /**获取当前时间*/
                Date curDate = new Date(System.currentTimeMillis());
                String dataStrNew= sdf.format(curDate);
                Date startTime=null;
                /**将时间转化成Date*/
                curDate=sdf.parse(dataStrNew);
                startTime = sdf.parse(mTime);
                /**除以1000是为了转换成秒*/
                long   between=(curDate.getTime()- startTime.getTime())/1000;
                int   elapsedTime= (int) (between);
                if (elapsedTime < seconds_of_1minute * 5) {

                    return "刚刚";
                }

                if (elapsedTime < seconds_of_1hour) {

                    return  elapsedTime / seconds_of_1minute + "分钟前";
                }
                if (elapsedTime < seconds_of_1day) {

                    return elapsedTime / seconds_of_1hour + "小时前";
                }
                if (elapsedTime < seconds_of_3days) {

                    return elapsedTime / seconds_of_1day + "天前";
                }
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                if(mTime.substring(0,4).equals(year + "")){
                    return new SimpleDateFormat("MM-dd HH:mm").format(startTime);
                }else{
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startTime);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";
        }

}
