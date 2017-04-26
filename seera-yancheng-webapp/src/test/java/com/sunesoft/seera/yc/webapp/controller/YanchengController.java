package com.sunesoft.seera.yc.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhouz on 2016/6/29.
 */
@Controller
public class YanchengController extends Layout {

    @RequestMapping(value = "Product")
    public ModelAndView deleteRoleForm(Model model, HttpServletRequest request, HttpServletResponse response) {

        return view("uAuth/_addRoleForm", model);
    }

    @RequestMapping(value = "productIndex")
    public ModelAndView deleteRoleFormd(Model model, HttpServletRequest request, HttpServletResponse response) {

        return view(layout, "yanchengProduct/productIndex", model);
    }

    @RequestMapping(value = "productIndex2")
    public ModelAndView deleteRoleFormddd(Model model, HttpServletRequest request, HttpServletResponse response) {

        return view("yanchengProduct/productIndex2", model);
    }
}
