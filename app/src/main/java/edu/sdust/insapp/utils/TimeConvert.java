package edu.sdust.insapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/11/3.
 */

public class TimeConvert {
    private static int RATE = 86400000;
    private static int JET_LAG = 28800000;
    public static int getDaysByMill(long time){
        int result = (int)((time+JET_LAG)/RATE);
        return result;
    }

    public static int getDaysByMill(Date date){
        return getDaysByMill(date.getTime());
    }

    public static Date getDateByDays(int days){
        return new Date((long)days*RATE);
    }


    public static Date toMidNight(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTime();
    }

    public static Date toTomorrowMidNight(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.DAY_OF_YEAR,1);
        System.out.println(calendar.getTime());
        return calendar.getTime();
    }

    public static void main(String[] args) {
        toMidNight(new Date());
    }
    //字符串强制转换成日期
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转换成字符串
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    public static String parseHour(String datdString) {

        datdString = datdString.replace("GMT","").replaceAll("\\(.*\\)", "");
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
        Date dateTrans = null;
        try {
            dateTrans = format.parse(datdString);
            return new SimpleDateFormat("HH:mm:ss").format(dateTrans);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datdString;
    }

}
