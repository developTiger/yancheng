package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.yc.core.shoppingCar.application.IShoppingCarService;
import com.sunesoft.seera.yc.core.uAuth.application.SysResourceService;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.ResourceCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.utils.Helper;
import com.sunesoft.seera.yc.webapp.utils.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zhouz on 2016/5/15.
 */
public class BaseController {

    @Autowired
    UserSession us;
    @Autowired
    SysResourceService menuService;
    @Autowired
    IShoppingCarService iShoppingCarService;

    public ModelAndView view(String viewName, Model model) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        model.addAttribute("view", viewName + ".html");
        model.addAttribute("ui", UI.class);
        model.addAttribute("helper", Helper.class);
        UserSessionDto userInfo = us.getCurrentUser(request);
        model.addAttribute("imghost", Configs.getProperty("imgHost", "http://storeimg.cn-yc.com.cn"));
        int cartCount = 0;
        if (userInfo != null) {
            cartCount = iShoppingCarService.getItemsCount(userInfo.getId());
        }
        String currentUrl = request.getRequestURI().substring(1);
        model.addAttribute("currentUrl", currentUrl);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("cartCount", cartCount);
        ModelAndView mv = new ModelAndView(viewName);
        return mv;
    }

}
