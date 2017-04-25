package com.eexikn.uxcebc.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Created by scene on 2017/3/14.
 */

public class DateUtils {
    /**
     * 判断日期是否相同
     */
    public static boolean isDifferentDay(long newTimeMillis, long oldTimeMillis) {
        Date newDate = null;
        Date oldDate = null;
        try {
            newDate = new Date(newTimeMillis);
            oldDate = new Date(oldTimeMillis);
            //获取现在的年月日
            Calendar newCal = Calendar.getInstance();
            newCal.setTime(newDate);
            int newYear = newCal.get(Calendar.YEAR);
            int newMonth = newCal.get(Calendar.MONTH);
            int newDay = newCal.get(Calendar.DAY_OF_MONTH);
            //获取之前的年月日
            Calendar oldCal = Calendar.getInstance();
            oldCal.setTime(oldDate);
            int oldYear = oldCal.get(Calendar.YEAR);
            int oldMonth = oldCal.get(Calendar.MONTH);
            int oldDay = oldCal.get(Calendar.DAY_OF_MONTH);
            if (oldYear != newYear || oldDay != newDay || oldMonth != newMonth) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
