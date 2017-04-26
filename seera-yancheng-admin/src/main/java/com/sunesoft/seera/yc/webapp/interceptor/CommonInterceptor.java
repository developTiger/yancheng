package com.sunesoft.seera.yc.webapp.interceptor;


import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.utils.Helper;
import com.sunesoft.seera.yc.webapp.utils.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhouz on 2016/5/13.
 */
public class CommonInterceptor implements HandlerInterceptor {
    private static final String[] IGNORE_URI = {"/wxoauth"};
	/**
	 * 3
	 * 在DispatcherServlet完全处理完请求后被调用  
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion() 
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 2
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
	}

	@Autowired
    UserSession us;
	
	/** 
	 * 1
     * 在业务处理器处理请求之前被调用 
     * 如果返回false 
     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
     *  
     * 如果返回true 
     *    执行下一个拦截器,直到所有的拦截器都执行完毕 
     *    再执行被拦截的Controller 
     *    然后进入拦截器链, 
     *    从最后一个拦截器往回执行所有的postHandle() 
     *    接着再从最后一个拦截器往回执行所有的afterCompletion()
     */ 
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj) throws Exception {
			
		String uid = us.getUserIdFromCookie(request);
		if(uid != null) us.updateUserSession(request,response,uid);
		String xRequestedWith = request.getHeader("X-Requested-With");
		UserSessionDto user = us.getCurrentUser(request);
		if(xRequestedWith != null && xRequestedWith.equals("XMLHttpRequest")){
			if(user == null){
				response.setContentType("application/text");
				try {
                    us.removeUserCookie(request, response);
                    response.getWriter().write("notlogin");
                    /*us.removeUserSession(request, uid.toString());
					response.getWriter().write("notLogin");*/
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		}
		if(uid == null){
            String currentRequestUrl = Helper.getCurrentRequestUrl(request);
            for (String s : IGNORE_URI) {
                if (currentRequestUrl.contains(s)) {
                    return false;
                }
            }
			response.sendRedirect("/login?rurl=" + URI.enURI(currentRequestUrl));
			return false;
		}
//		us.getCurrentUser(request, uid);
		
//		if(obj instanceof HandlerMethod){
//			HandlerMethod method = (HandlerMethod)obj;
//			PermissionCheck permissionCheck = method.getMethod().getAnnotation(PermissionCheck.class);
//			if(permissionCheck != null){
//				//这里表示我需要进行权限校验
//				if(!ssUserFinder.isHasPrivilege(uid, permissionCheck.value())){//系统用户ID 权限模块
//					response.sendRedirect("/wxoauth");
//					return false;
//				}else{
//					return true;
//				}
////				return systemUserFinder.isHasPrivilege(uid, permissionCheck.value());
//			}
//		}
		return true;
	}

}
