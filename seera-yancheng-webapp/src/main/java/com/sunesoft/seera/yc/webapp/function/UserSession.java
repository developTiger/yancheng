package com.sunesoft.seera.yc.webapp.function;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.web.ServletActionContext;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;
import com.sunesoft.seera.yc.core.tourist.application.UserIntegralService;
import com.sunesoft.seera.yc.webapp.utils.Des;
import com.sunesoft.seera.yc.webapp.utils.Helper;
import com.sunesoft.seera.yc.webapp.utils.URI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.ConfigurationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by zhouz on 2016/5/13.
 */
@Component
public class UserSession {

    @Autowired
    private ITouristService iTouristService;
    private static final String[] IGNORE_URI = {"/login"};
    private int sessionTime = 30 * 60;
    private String sessionKey = "empDto_";
    // private static String cookiePath = "localhost";

    // private Long userId;

    public long getCurrentUserId() {
        // if (userId == null)
        Long userId = getCurrentUser().getId();
        return userId;
    }

    public UserSessionDto getCurrentUser() {
        HttpServletRequest request = ServletActionContext.getCurrentRequest();
        HttpServletResponse response = ServletActionContext.getCurrentResponse();
        String uid = getUserIdFromCookie(request);
        if (uid != null) updateUserSession(request, response, uid);
        String xRequestedWith = request.getHeader("X-Requested-With");
        UserSessionDto user = getCurrentUser(request);
        if (xRequestedWith != null && xRequestedWith.equals("XMLHttpRequest")) {
            if (user == null) {
                response.setContentType("application/text");
                try {
                    removeUserCookie(request, response);
                    response.getWriter().write("notlogin");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        if (uid == null) {
            String currentRequestUrl = Helper.getCurrentRequestUrl(request);
            for (String s : IGNORE_URI) {
                if (currentRequestUrl.contains(s)) {
                    return null;
                }
            }
            try {
                response.sendRedirect("/login?rurl=" + URI.enURI(currentRequestUrl));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // return false;
        }


        return user;
    }

    public HttpSession getCurrentSession() {
        return ServletActionContext.getCurrentRequest().getSession();
    }

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

        UniqueResult<TouristSimpleDto> touristDtoUniqueResult = iTouristService.login(userName, password);
        if (touristDtoUniqueResult != null && touristDtoUniqueResult.getIsSuccess()) {
            TouristSimpleDto touristDto = touristDtoUniqueResult.getT();
            if (touristDto != null) {
                UserSessionDto userSessionDto = new UserSessionDto();
                userSessionDto.setId(touristDto.getId());
                userSessionDto.setName(touristDto.getRealName());
                userSessionDto.setLoginName(touristDto.getUserName());
                userSessionDto.setMobile(touristDto.getMobilePhone());
                setSystemUserCookie(touristDto.getId().toString(), response);
                setUserSession(request, userSessionDto);
                return userSessionDto;
            }
        }
        return null;
    }

    public UserSessionDto updateUserSession(HttpServletRequest request, HttpServletResponse response, String sysUserId) {
        if (StringUtils.isNotEmpty(sysUserId)) {
            UniqueResult<TouristSimpleDto> touristDtoUniqueResult = iTouristService.getTouristSimple(Long.parseLong(sysUserId));
            if (touristDtoUniqueResult != null && touristDtoUniqueResult.getIsSuccess()) {
                TouristSimpleDto touristDto = touristDtoUniqueResult.getT();
                if (touristDto != null) {
                    UserSessionDto userSessionDto = new UserSessionDto();
                    userSessionDto.setId(touristDto.getId());
                    userSessionDto.setName(touristDto.getRealName());
                    userSessionDto.setLoginName(touristDto.getUserName());
                    userSessionDto.setMobile(touristDto.getMobilePhone());
                    setSystemUserCookie(userSessionDto.getId().toString(), response);
                    setUserSession(request, userSessionDto);
                    return userSessionDto;
                }
            }
        }
        return null;
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
    public void setUserSession(HttpServletRequest request, UserSessionDto userSessionDto) {
        HttpSession session = request.getSession();
        String key = sessionKey + userSessionDto.getId();
        session.setAttribute(key, userSessionDto);
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
      /*  String key = sessionKey + getUserIdFromCookie(request).toString();
        Cookie cookie = new Cookie("sra_cun", "");
        //cookie.setDomain(cookiePath);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        request.getSession().removeAttribute(key); //删除某个session
        return true;*/

        /**
         * 我的修改
         */
        String userId=getUserIdFromCookie(request);
        if(userId==null){
            return false;
        }
        String key = sessionKey + userId.toString();
        Cookie cookie = new Cookie("sra_cun", "");
        //cookie.setDomain(cookiePath);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        request.getSession().removeAttribute(key); //删除某个session
        return true;
    }

    /**
     * 重新加载用户积分[尚未使用，可放置在指定位置触发，定期刷新用户分]
     * @param openId
     * @throws IOException
     * @throws ConfigurationException
     */
    private void reloadTouristIneggrals(String openId) throws IOException, ConfigurationException {
       CommonResult result= UserIntegralService.getUserIntegrals(openId);
        if(!result.getIsSuccess())
        {
            UserSessionDto cu = getCurrentUser();
            iTouristService.setIntegrals(cu.getId(),Integer.parseInt(result.getMsg()));
        }

    }

}
