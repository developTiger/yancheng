package com.sunesoft.seera.yc.webapp.utils;

import com.sunesoft.seera.yc.core.order.domain.OrderStatus;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 付款展示
     * @param str
     * @return
     */
    public static String paySelect(OrderStatus str){

        if(str==OrderStatus.waitPay){
            return "待付款";
        }else if(str==OrderStatus.payCheck) {
            return "待确认";
        }else if(str==OrderStatus.payed){
            return "已付款";
        }else if(str==OrderStatus.expired){
            return "已超时";
        }else if(str==OrderStatus.waitComment){
            return "待评价";
        }else if(str==OrderStatus.canceled){
            return "已取消";
        }else if(str==OrderStatus.end){
            return "已完成已评价";
        }else{
            return "";
        }
    }

}
