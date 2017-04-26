package com.sunesoft.seera.yc.webapi.zhiyoubao;

import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.fr.utils.MD5;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zwork on 2016/10/27.
 */
@Controller
public class zybCallback {

    private final String privateKey = Configs.getProperty("seera.yancheng.pwb.sign");




    @Autowired
    IOrderService orderService;


    @CrossOrigin
    @ResponseBody
    @RequestMapping("zyb_call_back")
    public void zybCallback(HttpServletRequest request,HttpServletResponse response){

        String result = "";
        String orderNo = request.getParameter("order_no");
        String sign = MD5.GetMD5Code("order_no="+orderNo+privateKey);
        String status = request.getParameter("status");

        if(status.equals("success")){
            //执行检票完成逻辑


            if(orderService.ticketEnterConfirm(orderNo).getIsSuccess()){

                result = "success";

            }else
                result="failed";

        }
        if(status.equals("cancel")){
            //执行取消订单逻辑
            result = "success";
        }

        AjaxResponse.write(response, result);
        return;
    }


}
