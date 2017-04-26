package com.sunesoft.seera.yc.core.parameter.application.factory;

import com.sunesoft.seera.fr.utils.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by xiazl on 2016/6/16.
 */
public class DtoFactory {
    /**
     * convert from dto
     *
     * @param source
     * @param target
     * @return
     */
    public static <S, T> T convert(S source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 根据时间求时间段
     *
     * @param time
     * @return
     */
    public static int timeSlot(Date time) {
        if(time==null)return 0;
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
//        if (date.before(time)) {
//            throw new IllegalArgumentException("this time is before now,It's unbelievable!");
//        }
        int yearNow;
        yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(time);
        int yearOld = cal.get(Calendar.YEAR);
        int monthOld = cal.get(Calendar.MONTH) + 1;
        int dayOld = cal.get(Calendar.DAY_OF_MONTH);

        int result = yearNow - yearOld;
        if (monthNow <= monthOld) {
            if (monthNow == monthOld) {
                if (dayNow < dayOld) {
                    result--;
                }
            } else {
                result--;
            }
        }
        return result;
    }



}
