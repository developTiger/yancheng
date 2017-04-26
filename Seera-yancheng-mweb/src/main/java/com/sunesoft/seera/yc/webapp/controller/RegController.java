package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jade on 16/8/18.
 */
@Controller
public class RegController extends BaseController {


    @Autowired
    private ITouristService iTouristService;

    @Autowired
    private UserSession userSession;
    /**
     * 注册.html
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/reg.html")
    public ModelAndView reg_view(Model model){
        return view("reg/index",model);
    }

    /**
     * 注册
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/reg" ,method = RequestMethod.POST)
    public ModelAndView reg(Model model, HttpServletRequest request, HttpServletResponse response)throws IOException {

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
        return view("",model);
    }
}
