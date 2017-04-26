package com.sunesoft.seera.yc.webapp.controller.portal;

import com.sunesoft.seera.yc.webapp.controller.Layout;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by admin on 2016/8/1.
 */
@Controller
public class BeforeStageController extends Layout {

    @RequestMapping(value = "sra_b_search")
    public ModelAndView search(Model model){
        return  view("beforeStage/search",model);
    }

}
