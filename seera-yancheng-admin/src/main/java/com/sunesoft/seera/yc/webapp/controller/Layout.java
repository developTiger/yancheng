package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.uAuth.application.SysResourceService;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.ResourceCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;
import com.sunesoft.seera.yc.webapp.function.ResouceFactory;
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
public class Layout {
    public static String layout = "layout/~layout";
    public static String testLayout = "test/layout/layout";
    public static String activeLayout = "layout/~activeLayout";
    public static String formLayout = "layout/~formLayout";

    @Autowired
    UserSession us;
    @Autowired
    SysResourceService menuService;

    @Autowired
    ResouceFactory resouceFactory;
    public ModelAndView view(String layout, String viewName, Model model) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        model.addAttribute("view", viewName + ".html");
        model.addAttribute("ui", UI.class);
        model.addAttribute("helper", Helper.class);
        UserSessionDto userInfo = us.getCurrentUser(request);
        model.addAttribute("imghost", Configs.getProperty("imgHost", "http://127.0.0.1:8033/"));
        List<ResourceDto> resourceDtos;

        if(userInfo.getLoginName().equals("1")||userInfo.getLoginName().equals("admin"))
            resourceDtos = menuService.getResourceList(new ResourceCriteria());
        else
            resourceDtos = resouceFactory.getResourceById(userInfo.getId());
        model.addAttribute("menu", resourceDtos);
        String currentUrl = request.getRequestURI().substring(1);
        model.addAttribute("currentUrl", currentUrl);
        if(!StringUtils.isNullOrWhiteSpace(currentUrl)&&resourceDtos!=null) {
            Long  currentID=0L;
            for (ResourceDto dto : resourceDtos) {

                Boolean hasFoundParent = false;
                if (dto.getChild() != null && dto.getChild().size() > 0) {
                    for (ResourceDto cc : dto.getChild()) {
                        if (cc.getUrl()!=null&&cc.getUrl().equals(currentUrl)) {
                            currentID = dto.getId();
                            hasFoundParent = true;
                            break;
                        }

                    }
                }
                if(hasFoundParent) {
                    model.addAttribute("parentMenu", currentID);
                    break;
                }
            }
        }
        model.addAttribute("userInfo", userInfo);
        ModelAndView mv = new ModelAndView(layout);
        return mv;
    }

    public ModelAndView view(String viewName, Model model) {
        model.addAttribute("helper", Helper.class);
        model.addAttribute("imghost", Configs.getProperty("imgHost","http://storeimg.cn-yc.com.cn"));
        ModelAndView mv = new ModelAndView(viewName);
        return mv;
    }


}
