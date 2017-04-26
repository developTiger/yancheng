package com.sunesoft.seera.yc.webapp.utils;

import com.sunesoft.seera.yc.core.order.domain.OrderProductStatus;
import com.sunesoft.seera.yc.core.order.domain.OrderStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductItemType;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
     * 商品状态显示
     * @param orderStatus
     * @return
     */
    public static String productStatus(OrderProductStatus orderStatus){

        switch (orderStatus){
            case normal:
                return "可改签";
            case mealCheck:
                return "改签审核";
            case mealReject:
                return "拒绝改签";
            case mealed:
                return "已改签审核通过";
            case returned:
                return "已退单";
            case expire:
                return "过期未消费";
            case end:
                return "已完成";
        }
        return "";
    }

    /**
     * 付款展示
     * @param str
     * @return
     */
    public static String paySelect(OrderStatus str){

        if(str==OrderStatus.waitPay){
            return "待付款";
        }else if(str==OrderStatus.payed){
            return "已付款";
        }else if(str==OrderStatus.expired){
            return "已超时";
        }else if(str==OrderStatus.waitComment){
            return "待评价";
        }else if(str==OrderStatus.canceled){
            return "已取消";
        }else if(str==OrderStatus.end){
            return "已完成";
        }else{
            return "";
        }
    }
    /**
     * 订单 操作展示
     * @param str
     * @return
     */
    public static String operateShow(OrderStatus str){

        if(str==OrderStatus.waitPay){
            return "去付款";
        }else if(str==OrderStatus.payed){
            return "去评价";
        }else if(str==OrderStatus.expired){
            return "重新下单";
        }else if(str==OrderStatus.canceled){
            return "重新下单";
        }else if(str==OrderStatus.end){
            return "再次下单";
        }else{
            return "";
        }
    }

    /**
     * 订单 操作链接
     * @param str
     * @return
     */
    public static String operateHref(OrderStatus str,Long fetcherId,Long orderId,String ordernum,String productNum,Boolean selectEvaluationOrMeal){

        if(str==OrderStatus.waitPay){
            return "href='/repay?sn="+ordernum+"'";
        }else if(str==OrderStatus.payCheck){
            return "";
        }else if(str==OrderStatus.payed){
                return "href='/order_evaluation/" + ordernum + "'";
        }else if(str==OrderStatus.expired){
            return "href='/toItem?productNum="+productNum+"'";
        }else if(str==OrderStatus.canceled){
            return "href='/toItem?productNum="+productNum+"'";
        }else if(str==OrderStatus.end){
            return "href='/toItem?productNum="+productNum+"'";
        }else{
            return "";
        }
    }

    /**
     * 四则运算，加减乘除
     * @param target
     * @return
     */
    public static BigDecimal fourArithmetic(BigDecimal target,BigDecimal source,String type){

        BigDecimal result=null;
        switch (type){
            case "+":
                result=target.add(source);
                break;
            case "-":
                result=target.subtract(source);
                break;
            case "*":
                result=target.multiply(source);
                break;
            case "/":
                result=target.divide(source);
                break;
        }
        return result;
    }

    public static String productTypeShow(ProductItemType type){

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


    /**
     * 改签状态
     * @param type
     * @return
     */
    public static String mealStatus(OrderProductStatus type){

        switch (type){
            case normal:
                return "可改签";
            case mealCheck:
                return "改签审核中";
            case mealReject:
                return "拒绝改签";
            case mealed:
                return "改签通过";
            case returned:
                return "已退单";
            case expire:
                return "过期未消费";
            case end:
                return "已完成";
        }
        return "";
    }
    /**
     * mealCheck
     */
    public static Boolean checkMeal(Boolean canMeal,OrderStatus status){
        if(canMeal!=null) {
            if (canMeal) {
                if (OrderStatus.payed.equals(status)) {
                    return true;
                }
            }
        }
        return false;
    }
}
