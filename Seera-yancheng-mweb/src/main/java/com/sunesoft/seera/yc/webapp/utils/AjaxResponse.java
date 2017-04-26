package com.sunesoft.seera.yc.webapp.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AjaxResponse {

    /**
     * ajax请求返回结果
     * @param response
     * @param type ajax返回类型 如：json、text
     * @param jsonStr ajax返回结果
     */
    public static void write(HttpServletResponse response,String type,String jsonStr){

        if(type != null && type.equals("text")){
            response.setContentType("application/text");
        }else{
            response.setContentType("application/json");
        }

        try {
            response.getWriter().write(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ajax请求返回结果
     * @param response
     * @param jsonStr ajax返回结果
     */
    public static void write(HttpServletResponse response, String jsonStr) {

        response.setContentType("application/json");
        try {
            response.getWriter().write(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
