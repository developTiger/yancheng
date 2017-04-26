package com.sunesoft.seera.yc.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GlobalExeptionController {

    @RequestMapping("test")
    public void test() throws Exception {
        throw new Exception("出错了！");
    }
}
