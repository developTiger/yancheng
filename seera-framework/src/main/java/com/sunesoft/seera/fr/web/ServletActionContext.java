package com.sunesoft.seera.fr.web;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by bing on 16/8/18.
 */
public class ServletActionContext {

    private static ServletRequestAttributes getServletRequestAttributes() {
        try {
            return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 获取当前线程的用户会话对象.
     *
     * @return 返回用户会话对象.
     */
    public static HttpSession getCurrentSession() {
        ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getRequest().getSession();
        }
        return null;
    }

    /**
     * 获取当前线程的用户cookie对象集.
     *
     * @return 返回用户cookie对象集.
     */
    public static Cookie[] getCurrentCookies() {
        ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
        if (servletRequestAttributes != null)
            return servletRequestAttributes.getRequest().getCookies();
        return null;
    }

    /**
     * 获取当前线程的用户请求对象.
     *
     * @return 返回用户请求对象.
     */
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
        if (servletRequestAttributes != null)
            return servletRequestAttributes.getRequest();
        return null;
    }

    /**
     * 获取当前线程的用户返回对象.
     *
     * @return 返回用户返回对象.
     */
    public static HttpServletResponse getCurrentResponse() {
        ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
        if (servletRequestAttributes != null)
            return servletRequestAttributes.getResponse();
        return null;
    }
}
