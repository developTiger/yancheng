package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.domain.TouristStatus;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import com.sunesoft.seera.yc.webapp.wxoauth.CodeResponse;
import com.sunesoft.seera.yc.webapp.wxoauth.QrCode;
import com.sunesoft.seera.yc.webapp.wxoauth.UserInfoResponse;
import com.sunesoft.seera.yc.webapp.wxoauth.WxOAuthClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhouz on 2016/6/29.
 */
@Controller
public class LoginController extends Layout {

    @Autowired
    private ITouristService iTouristService;

    @Autowired
    private UserSession userSession;

    @Autowired
    private WxOAuthClient wxOAuthClient;

    private static Map<String, Long> tourists = new ConcurrentHashMap<>();

    @RequestMapping(value = "/login")
    public ModelAndView login(Model model) {
        QrCode qrCode = wxOAuthClient.wxQr();
        userSession.getCurrentSession().setAttribute("qrCode", qrCode);
        model.addAttribute("qrCode", qrCode.getCode());
        return view("login/login", model);
    }

    @RequestMapping("/login_wx_oauth")
    public ModelAndView loginWxOAuth(HttpServletRequest request) throws IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if (StringUtils.isNotEmpty(code)) {
            CodeResponse codeResponse = wxOAuthClient.authorizationCode(code);
            if (codeResponse != null && StringUtils.isEmpty(codeResponse.getErrCode())) {
                UserInfoResponse userInfoResponse = wxOAuthClient.getUserInfo(codeResponse.getAccessToken(), codeResponse.getOpenId());
                if (userInfoResponse != null && StringUtils.isEmpty(userInfoResponse.getErrCode())) {
                    CommonResult commonResult = iTouristService.register(userInfoResponse.getOpenid(), "");
                    if (commonResult.getIsSuccess()) {
                        TouristDto touristDto = new TouristDto();
                        touristDto.setId(commonResult.getId());
                        touristDto.setUserName(userInfoResponse.getOpenid());
                        touristDto.setRealName(userInfoResponse.getNickname());
                        touristDto.setStatus(TouristStatus.Normal);
                        iTouristService.update(touristDto);
                        if (!tourists.containsKey(state)) {
                            tourists.put(state, touristDto.getId());
                        }
                    }
                }
            }
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/get_qr", method = RequestMethod.POST)
    public String getQr() {
        QrCode qrCode = wxOAuthClient.wxQr();
        userSession.getCurrentSession().setAttribute("qrCode", qrCode);
        return qrCode.getCode();
    }

    @RequestMapping("verify_qr")
    public void verifyQr(HttpServletRequest request, HttpServletResponse responses) throws InterruptedException {
        QrCode qrCode = (QrCode) userSession.getCurrentSession().getAttribute("qrCode");
        // TODO: 16/8/25 需要测试服务是否会被释放!
        do {
            if (qrCode != null) {
                if (tourists.containsKey(qrCode.getUuid())) {
                    Long touristId = tourists.get(qrCode.getUuid());
                    UniqueResult<TouristDto> touristDtoUniqueResult = iTouristService.getTourist(touristId);
                    if (touristDtoUniqueResult != null && touristDtoUniqueResult.getIsSuccess()) {
                        userSession.login(request, responses, touristDtoUniqueResult.getT().getUserName(), "");
                        tourists.remove(qrCode.getUuid());
                        return;
                    }
                }
            }
        } while (qrCode == null || !tourists.containsKey(qrCode.getUuid()));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login_post(Model model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        String redirectUrl = httpServletRequest.getParameter("rurl");
        if (StringUtils.isEmpty(username))
            model.addAttribute("username_error", "用户名不能为空!");
        if (StringUtils.isEmpty(password))
            model.addAttribute("password_error", "密码不能为空!");

        UserSessionDto userSessionDto = userSession.login(httpServletRequest, httpServletResponse, username, password);
        if (userSessionDto != null) {
            if (StringUtils.isEmpty(redirectUrl))
                httpServletResponse.sendRedirect("/");
            else httpServletResponse.sendRedirect(redirectUrl);
            return null;
        } else {
            model.addAttribute("error", "用户名或密码错误!");
        }
        return view("login/login", model);
    }

    @RequestMapping(value = "/reg")
    public ModelAndView reg(Model model) throws IOException {
        return view("login/register", model);
    }

    @RequestMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        us.removeUserCookie(request,response);
        response.sendRedirect("/login");
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public ModelAndView reg_post(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String surepassword = request.getParameter("surepassword");
        String code = request.getParameter("code");

        if (StringUtils.isEmpty(username))
            model.addAttribute("error", "用户名不能为空!");
        if (StringUtils.isEmpty(password))
            model.addAttribute("error", "密码不能为空!");
        if (StringUtils.isEmpty(code))
            model.addAttribute("error", "验证码不能为空!");
        if (!password.equals(surepassword))
            model.addAttribute("error", "密码输入不一致!");

        CommonResult commonResult = iTouristService.register(username, password);
        if (commonResult.getIsSuccess()) {
            response.sendRedirect("/login");
            return null;
        }
        model.addAttribute("error", commonResult.getMsg());
        return view("login/register", model);
    }

    @ResponseBody
    @RequestMapping(value = "ajax_check_userName/{userName}")
    public CommonResult ajax_check_userName(@PathVariable(value = "userName") String userName) throws Exception {
        userName = new String(userName.getBytes("ISO-8859-1"), "UTF-8");
        CommonResult commonResult = iTouristService.check(userName);
        return commonResult;
    }


    @RequestMapping(value = "logonInexWeChat")
    public ModelAndView logonInexWeChat(Model model, HttpServletRequest request, HttpServletResponse response) {
        return view("login/loginInexWeChat", model);
    }


    @RequestMapping(value = "tourist")
    public ModelAndView touristDto(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getParameter("token");
        if (!com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(token)) {
            model.addAttribute("bean", iTouristService.getTourist(token).getT());
            response.sendRedirect("");
            return null;
        } else {
            model.addAttribute("error", "该游客不存在");
            return view("停留在原来页面", model);
        }
    }

    @RequestMapping(value = "Ajax_sc_updatePassword")
    public CommonResult updatePassword(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserSessionDto dto = userSession.getCurrentUser(request);
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String superPassword = request.getParameter("superPassword");
        if (!superPassword.equals(newPassword)) return new CommonResult(false, "新密码与确认密码输入不一致");
        if (iTouristService.login(dto.getLoginName(), oldPassword).getIsSuccess()) {
            CommonResult c = iTouristService.restPassword(dto.getId(), newPassword);
            if (c.getIsSuccess()) {
                response.sendRedirect("login/login");
                return c;
            } else {
                return new CommonResult(false, c.getMsg());
            }
        }
        return new CommonResult(false, "原始密码错误");
    }

    @RequestMapping(value = "Ajax_sc_setstatus")
    public CommonResult setstatus(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String touristId = request.getParameter("touristId");
        String status = request.getParameter("status");
        TouristStatus touristStatus = null;
        if (status.equalsIgnoreCase("Forbidden")) {
            touristStatus = TouristStatus.Forbidden;
        } else {
            touristStatus = TouristStatus.Normal;
        }

        if (com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(touristId)) {
            return new CommonResult(false, "请选择游客");
        } else {
            return iTouristService.setStatus(Long.parseLong(touristId), touristStatus);
        }
    }

    @RequestMapping(value = "touristSimpleTable")
    public void findSimpleTourists(HttpServletRequest request, HttpServletResponse response) {
        TouristCriteria criteria = new TouristCriteria();
        String status = request.getParameter("status");
        String endRegisterDate = request.getParameter("endRegisterDate");
        String fromRegisterDate = request.getParameter("fromRegisterDate");
        String taken = request.getParameter("taken");
        if (status.equals(0))
            //全部
            criteria.setStatus(null);
        else if (status.equals(1))
            //禁止的状态
            criteria.setStatus(TouristStatus.Forbidden);
        else
            //正常状态
            criteria.setStatus(TouristStatus.Normal);
        criteria.setEndRegisterDate(DateHelper.parse(endRegisterDate));
        criteria.setFromRegisterDate(DateHelper.parse(fromRegisterDate));
        criteria.setToken(taken);
        String json = JsonHelper.toJson(iTouristService.findSimpleTourists(criteria));
        System.out.println(json);
        AjaxResponse.write(response, json);
    }

    @RequestMapping(value = "touristTable")
    public void findTourists(HttpServletRequest request, HttpServletResponse response) {
        TouristCriteria criteria = new TouristCriteria();
        String status = request.getParameter("status");
        String endRegisterDate = request.getParameter("endRegisterDate");
        String fromRegisterDate = request.getParameter("fromRegisterDate");
        String taken = request.getParameter("taken");
        if (status.equals(0))
            //全部
            criteria.setStatus(null);
        else if (status.equals(1))
            //禁止的状态
            criteria.setStatus(TouristStatus.Forbidden);
        else
            //正常状态
            criteria.setStatus(TouristStatus.Normal);
        criteria.setEndRegisterDate(DateHelper.parse(endRegisterDate));
        criteria.setFromRegisterDate(DateHelper.parse(fromRegisterDate));
        criteria.setToken(taken);
        String json = JsonHelper.toJson(iTouristService.findTourists(criteria));
        System.out.println(json);
        AjaxResponse.write(response, json);
    }

}
