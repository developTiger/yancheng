package com.sunesoft.seera.yc.webapp.interceptor;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OverallExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex) {
        ex.printStackTrace();
        return new ModelAndView("/error");
    }

//    private String exceptionAttribute = "exception";
//    private Properties exceptionMappings;
//
////    @Override
////    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
////                                         Object o, Exception ex) {
////        //handler为当前处理器适配器执行的对象
////        String message = null;
////        //判断是否为系统自定义异常。
////        if(ex instanceof CustomException) {
////            message = ((CustomException) ex).getMessage();
////        }else {
////            message = "系统出错啦，稍后再试试！";
////        }
////
////        ModelAndView modelAndView = new ModelAndView();
////        //跳转到相应的处理页面
////        modelAndView.addObject(this.exceptionAttribute, ex);
////        modelAndView.setViewName("error/error");
////        return modelAndView;
////    }
//
//
//    public void setExceptionMappings(Properties exceptionMappings) {
//        this.exceptionMappings = exceptionMappings;
//    }
//
//    public Properties getExceptionMappings() {
//        return exceptionMappings;
//    }


}
