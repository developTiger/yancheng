package com.sunesoft.seera.fr.utils;

import com.sun.javafx.binding.StringFormatter;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by zhaowy on 2016/7/18.
 */
public class NumGenerator {
    private static SecureRandom random = new SecureRandom();

    /**
     * 生成默认8位字符串序列号
     * @return
     */
    public static String generate() {

        return generate(8).toUpperCase();
    }

    /** 生成随机数序列号
     * @param length 随机数长度
     * @return
     */
    public static String generate(int length) {
        String str = new BigInteger(130, random).toString(32);
        return str.substring(0, length).toUpperCase();
    }

    /**
     * 生成包含随机数+时间戳的唯一序列号
     * @param date 日期
     * @remark 生成的时间戳为自2016-01-01 00:00:00起的时间戳,如需转化为真实时间戳可加上 1451577600000
     * @return 时间戳+随机数
     */
    public static String generate(Date date,int randomLength)
    {
        Date orginalDate = DateHelper.parse("2016-01-01 00:00:00");
        long time =  date.getTime() - orginalDate.getTime();
        String randomStr = "";
        for(Integer i =0; i<randomLength;i++){
            randomStr += String.valueOf((int) (Math.random() * (10)));
        }

        return time+randomStr;
    }

    /**
     * 生成时间戳的唯一序列号
     * @param startWith 序列号前缀
     * @remark 生成的时间戳为自2016-01-01 00:00:00起的时间戳,如需转化为真实时间戳可加上 1451577600000
     * @return 时间戳+随机数
     */
    public static String generate(String startWith)
    {
        Date date = new Date();
        Date orginalDate = DateHelper.parse("2016-01-01 00:00:00");
        long time =  date.getTime() - orginalDate.getTime();
        return startWith+String.valueOf(time);
    }

    public static void main(String args[]) {
        System.out.println(generate());
        System.out.println(generate(12));
        System.out.println(generate(new Date(),8));
    }

}
