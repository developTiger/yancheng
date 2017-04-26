package com.sunesoft.seera.yc.webapp.function;

import com.sunesoft.seera.yc.core.manager.application.IManagerService;
import com.sunesoft.seera.yc.core.manager.application.dtos.InnerManagerDto;
import com.sunesoft.seera.yc.core.manager.application.dtos.ManagerSessionDto;
import com.sunesoft.seera.yc.core.uAuth.application.SysUserService;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;
import com.sunesoft.seera.yc.webapp.utils.Des;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by zhouz on 2016/5/13.
 */
@Component
public class UserSession {

    @Autowired
    //SysUserService sysUserService;
    IManagerService iManagerService;
    private int sessionTime = 30 * 60;
    private String sessionKey = "empDto_";
    private static String cookiePath = "localhost";

    /**
     * 获取当前登录用户信息
     *
     * @param request
     * @return
     */
    public UserSessionDto getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (getUserIdFromCookie(request) == null) {
            return null;
        }
        UserSessionDto auth = session.getAttribute(sessionKey + getUserIdFromCookie(request).toString()) == null ? null : (UserSessionDto) session.getAttribute(sessionKey + getUserIdFromCookie(request).toString());
        return auth;
    }

    /**
     * 设置登录信息
     *
     * @param request
     * @param response
     * @param userName
     * @param password
     * @return
     */
    public UserSessionDto login(HttpServletRequest request, HttpServletResponse response, String userName, String password) {
        ManagerSessionDto auth = iManagerService.login(userName, password);
        if (auth == null) {
            return null;
        }
        UserSessionDto userSessionDto  = new UserSessionDto();
        userSessionDto.setId(auth.getId());
        userSessionDto.setName(auth.getRealName());
        userSessionDto.setLoginName(auth.getUserName());
        userSessionDto.setMobile(auth.getPhone());
        setSystemUserCookie(auth.getId().toString(), response);
        setUserSession(request, userSessionDto);
        return userSessionDto;
    }

    public UserSessionDto updateUserSession(HttpServletRequest request, HttpServletResponse response, String sysUserId) {
        InnerManagerDto auth  = iManagerService.getById(Long.parseLong(sysUserId));
        if (auth == null) {
            return null;
        }
        UserSessionDto userSessionDto  = new UserSessionDto();
        userSessionDto.setId(auth.getId());
        userSessionDto.setName(auth.getRealName());
        userSessionDto.setLoginName(auth.getUserName());
        userSessionDto.setMobile(auth.getPhone());
        setSystemUserCookie(auth.getId().toString(), response);
        setUserSession(request, userSessionDto);
        return userSessionDto;
    }

    //
    public void setSystemUserCookie(String userId, HttpServletResponse response) {
        Cookie cookie = null;
        try {
            cookie = new Cookie("sra_cun", Des.encode(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //cookie.setDomain(cookiePath);
        cookie.setPath("/");
        cookie.setMaxAge(sessionTime);
        response.addCookie(cookie);
    }

    //
//    /**
//     * 把用户ID保存到cookie中，周期为30分钟
//     *
//     * @param response
//     * @param userId
//     */
    public void setUserCookie(HttpServletRequest request, HttpServletResponse response, String userId) {

//        UserServiceDAO userService = new UserServiceDAO();
//        SimpleUserInfoDTO simpleUserInfoDTO = userService.getSimpleUser(userId);
//        try {
//            BtUserSessionDao.Login(simpleUserInfoDTO, response);
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }

    //
//
//    /**
//     * 设置用户缓存信息
//     * @param request
//     * @param userInfo
//     */
    public void setUserSession(HttpServletRequest request, UserSessionDto empSessionDto) {
        HttpSession session = request.getSession();
        String key = sessionKey + empSessionDto.getId();
        session.setAttribute(key, empSessionDto);
        session.setMaxInactiveInterval(sessionTime);
    }

    public String getSystemUserIdCookie(HttpServletRequest request) {
        String userId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie c = cookies[i];
                if ("sra_cun".equals(c.getName())) {
                    try {
                        userId = Des.decode(c.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return userId;

    }

    //
//    /**
//     * 从cookie中获取到用户id
//     *
//     * @param request
//     * @return
//     */
    public String getUserIdFromCookie(HttpServletRequest request) {
        String userId = getSystemUserIdCookie(request);
        if (userId != null && !userId.equals("")) {
            return userId;
        }

        return null;
    }

    //
//    /**
//     * 删除用户cookie
//     *
//     * @param request
//     * @param response
//     * @param
//     */
    public Boolean removeUserCookie(HttpServletRequest request, HttpServletResponse response) {
        String key = sessionKey + getUserIdFromCookie(request).toString();
        Cookie cookie = new Cookie("sra_cun", null);

        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
        request.getSession().removeAttribute(key); //删除某个session
        return true;
    }

}
