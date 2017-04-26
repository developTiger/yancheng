package com.sunesoft.seera.yc.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by xiazl on 2016/9/3.
 */
@Controller
public class MyController extends Layout {

    @RequestMapping(value = "xzl")
    public ModelAndView xzl(Model model){
        return view("index/cmgd",model);
    }
}
