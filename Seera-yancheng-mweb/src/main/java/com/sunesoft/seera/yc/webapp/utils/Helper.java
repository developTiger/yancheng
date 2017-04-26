package com.sunesoft.seera.yc.webapp.utils;

import com.sunesoft.seera.yc.core.product.domain.ProductType;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhouz on 2016/5/13.
 */
public class Helper {

    /**
     * 日期转String格式化
     * @param date
     * @return
     */
    public static String formatDateToString(Date date, String formatType){
        if(date == null) return null;
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        return format.format(date).toString();
    }

    /**
     * 日期格式化
     * @param date
     * @return
     */
    public static String formateDate(String date){
        return date.substring(0,10);
    }


    /**
     * 日期格式化
     * 将String 转 Date 用于查询
     * @param cDate
     * @return
     */
    public static Date formateStringToDate(String cDate){
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(cDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期格式化
     * 将String 转 Date 用于查询
     * @param cDate
     * @param type
     * @return
     */
    public static Date formateStringToDate(String cDate,String type){
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        try {
            date = sdf.parse(cDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 日期格式化 过滤到 时分秒 yyyy-MM-dd-mm-ss
     * @param date
     * @return
     */
    public static String formateMinDateToString(Date date){
        if(date == null) return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date).toString();
    }

    /**
     * 获取当前请求地址
     * @param request
     * @return
     */
    public static String getCurrentRequestUrl(HttpServletRequest request){
        String currentRequestUrl = request.getScheme() + "://"; //请求协议 http 或 https
        currentRequestUrl += request.getHeader("host");  // 请求服务器
        currentRequestUrl += request.getRequestURI();     // 工程名
        if(request.getQueryString() != null) //判断请求参数是否为空
            currentRequestUrl += "?" + request.getQueryString();   // 参数
        return currentRequestUrl;
    }

    public  static  Boolean compareDateToCurrent(Date date){
        //String date = formatDateToString(new Date(), "yyyy-MM-dd");
        return  new Date().after(date);
    }

    /**
     * 转义
     * @param str
     * @return
     */
    public static String escape(String str){
        str = str.replace("&#40;","(");
        str = str.replace("&#41;",")");
        return str;
    }

    public static String productTypeShow(ProductType type){

        if(type==null){
            return "";
        }
        switch (type){
            case Ticket:
                return "门票";
            case Catering:
                return "餐饮";
            case Souvenirs:
                return "纪念品";
            case Hotel:
                return "酒店";
            case Other:
                return "其他";
        }
        return "";
    }


    public static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    public static String string = "abcdefghijklmnopqrstuvwxyz";

    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
