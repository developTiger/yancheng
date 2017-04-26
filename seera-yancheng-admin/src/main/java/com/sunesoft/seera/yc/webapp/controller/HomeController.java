package com.sunesoft.seera.yc.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhouz on 2016/5/26.
 */
@Controller
public class HomeController extends Layout {

    @RequestMapping(value = "sra_index")
    public ModelAndView index(Model model) {

        return view(layout, "index", model);
    }


}
