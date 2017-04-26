package com.sunesoft.seera.yc.webapi;

import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.fr.utils.MD5;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.spi.http.HttpContext;
import java.util.Properties;

/**
 * Created by zhaowy on 2016/7/25.
 */

public class RequestValidator {

    public static Boolean isValid(String access_token,String target)
    {
        String privateKey = Configs.getProperty("touristGetPrivateKey");
        String md5Sign = MD5.GetMD5Code(target + privateKey).toLowerCase();
        if(access_token.equals(md5Sign)) return true;
        return false;
    }

}
