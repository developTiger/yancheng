package com.sunesoft.seera.yc.webapp.controller.paramat;


import com.sunesoft.seera.yc.webapp.controller.Layout;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by user on 2016/6/1.
 */
@Controller
public class paramettypeController extends Layout {

    @RequestMapping(value = "sra_paramettype")
    public ModelAndView manage(Model modal, HttpServletRequest request, HttpServletResponse response) {
        return view(layout, "parameter/paramettype", modal);

    }
}
