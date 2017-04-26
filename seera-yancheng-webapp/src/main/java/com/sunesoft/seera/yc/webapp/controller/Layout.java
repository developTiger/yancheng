package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.yc.core.article.application.IArticleService;
import com.sunesoft.seera.yc.core.article.domain.ArticleType;
import com.sunesoft.seera.yc.core.shoppingCar.application.IShoppingCarService;
import com.sunesoft.seera.yc.core.uAuth.application.SysResourceService;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.ResourceCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.model.HelpCenterFactory;
import com.sunesoft.seera.yc.webapp.model.PictureAllFactory;
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
    public static String findpassword="layout/~findpasswordlayout";


    @Autowired
    PictureAllFactory pictureAllFactory;
    @Autowired
    UserSession us;
    @Autowired
    SysResourceService menuService;
    @Autowired
    IShoppingCarService iShoppingCarService;
    @Autowired
    IArticleService iArticleService;
    @Autowired
    HelpCenterFactory hcFactory;

    public ModelAndView view(String layout, String viewName, Model model) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        model.addAttribute("view", viewName + ".html");
        model.addAttribute("ui", UI.class);
        model.addAttribute("helper", Helper.class);
        UserSessionDto userInfo = us.getCurrentUser(request);
        model.addAttribute("imghost", Configs.getProperty("imgHost","http://storeimg.cn-yc.com.cn"));

        model.addAttribute("HelpCenter",hcFactory.getArticlesByType(ArticleType.HelpCenter));
        model.addAttribute("ServicePromise",hcFactory.getArticlesByType(ArticleType.ServicePromise));
        model.addAttribute("HelpCenter",hcFactory.getArticlesByType(ArticleType.HelpCenter));
        model.addAttribute("friendlyLink",hcFactory.getArticlesByType(ArticleType.friendlyLink));

        if(!layout.equals(activeLayout)) {
            List<ResourceDto> resourceDtos = menuService.getResourceList(new ResourceCriteria());
            model.addAttribute("menu", resourceDtos);
        }
        int cartCount=0;
        if(userInfo!=null){
            cartCount=iShoppingCarService.getItemsCount(userInfo.getId());
        }

        model.addAttribute("logo",pictureAllFactory.getImage("logo",1));
        String currentUrl = request.getRequestURI().substring(1);
        model.addAttribute("currentUrl", currentUrl);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("cartCount",cartCount);
        ModelAndView mv = new ModelAndView(layout);
        return mv;
    }

    public ModelAndView view(String viewName, Model model) {
        model.addAttribute("helper", Helper.class);
        model.addAttribute("imghost", Configs.getProperty("imgHost","http://127.0.0.1:8033/"));
        ModelAndView mv = new ModelAndView(viewName);
        return mv;
    }


}
