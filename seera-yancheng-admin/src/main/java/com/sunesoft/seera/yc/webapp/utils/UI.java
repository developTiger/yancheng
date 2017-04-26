package com.sunesoft.seera.yc.webapp.utils;



import java.util.Date;

/**
 * Created by zhouz on 2016/5/13.
 */
public class
        UI {

    private static String doMain = "/";
    private static String staticDoMain = "/";

    /**
     * 加载静态文件服务器js
     * @param url
     * @return
     */
    public static String loadJs(String url) {
        try {
            System.out.print(url);
            if (url != null && url.length() > 0) {
                if (!url.substring(0, 1).equals("/")) {
                    url = "/" + url;
                }
            }
            if (doMain != null && !"".equals(doMain) && !"/".equals(doMain) && url.indexOf("text.js") != -1) {
                return "";
            }
            String rUrl = doMain + "js" + url.trim() + "?" + Helper.formatDateToString(new Date(), "yyyyMMdd");
            if (url.indexOf("require.js") != -1 || url.indexOf("text.js") != -1) {
                rUrl = staticDoMain + "js" + url.trim() + "?" + Helper.formatDateToString(new Date(), "yyyyMMdd");
            }
            StringBuilder s = new StringBuilder();
            s.append("<script src='");
            s.append(rUrl);
            s.append("' ></script>");
            return s.toString();
        }catch (Exception ex )
        {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 加载静态文件css
     * @param url
     * @return
     */
    public static String loadCss(String url) {
        if(url != null && url.length() > 0){
            if(!url.substring(0, 1).equals("/")){
                url = "/" + url;
            }
        }
        StringBuilder s = new StringBuilder();
        s.append("<link href='");
        s.append(doMain + "css" + url.trim() + "?" + Helper.formatDateToString(new Date(), "yyyyMMdd"));
        s.append("' rel='stylesheet' type='text/css'/>");
        return s.toString();
    }
}
