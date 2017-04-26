package com.sunesoft.seera.yc.pingplus;

import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import com.sunesoft.seera.fr.loggers.Logger;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderDto;
import com.sunesoft.seera.yc.core.order.domain.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by zhaowy on 2016/8/5.
 */

@Controller
public class SeeraWebHooks {

    @Autowired
    IOrderService orderService;

    @Autowired
    Logger logger;

    @RequestMapping("order_qr_confirm")
    public void seeraQrConfirm(HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("order webHook begin!");
      //  String signature = request.getHeader("X-Pingplusplus-Signature");

           /*System.out.println("ping++　webhooks");*/
        request.setCharacterEncoding("UTF8");
        //获取头部所有信息
        Enumeration headerNames = request.getHeaderNames();
        String signature=null;
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            if("x-pingplusplus-signature".equals(key)){
                signature=value;
            }
        }
        logger.info(signature);

       /*System.out.println("signature"+signature);*/
        // 获得 http body 内容
        StringBuffer eventJson=new StringBuffer();
        BufferedReader reader= null;
        try {
              reader = request.getReader();
            //StringBuffer buffer = new StringBuffer();
            String string;
            while ((string = reader.readLine()) != null) {
                eventJson.append(string);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reader.close();

        logger.info(eventJson.toString());
//        JSONObject event=JSON.parseObject(eventJson.toString());
//        boolean verifyRS=false;
        Event event = Webhooks.eventParse(eventJson.toString());//(Event) JsonHelper.toObject(eventJson.toString(), Event.class);

      //  logger.info(JsonHelper.toJson(event));
        boolean result = ChargeUtil.verifyData(eventJson.toString(), signature);
        //Event event = (Event) JsonHelper.toObject(webhooksRawPostData, Event.class);

        if (result) {
            if ("charge.succeeded".equals(event.getType())) {
             //   Charge charge=  Charge.retrieve(event.getObject());
                Charge charge =(Charge)event.getData().getObject();
                String orderNum = charge.getOrderNo();
                String channel = charge.getChannel();
                String payType = null;
                int amountFen = charge.getAmount();
                Double amountYuan = amountFen * 1.0 / 100;//ping++扣款,精确到分，而数据库精确到元
                Double weiXinInput = null;
                Double aliPayInput = null;
                Double bankCardInput = null;

                if ("wx_pub".equals(channel)||"wx_wap".equals(channel)||"wx_pub_qr".equals(channel)||"wx".equals(channel)) {
                    payType = "4";//支付类型(1:储值卡，2:现金,3:银行卡,4:微信,5:支付宝,6:优惠券，7：打白条;8:多方式付款;9:微信个人，10：支付宝（个人）)
                    weiXinInput = amountYuan;
                } else if ("alipay_wap".equals(channel)||"alipay_in_weixin".equals(channel)||"alipay_qr".equals(channel)||"alipay_pc_direct".equals(channel)) {
                    payType = "5";
                    aliPayInput = amountYuan;
                } else if ("upacp".equals(channel) || "upacp_wap".equals(channel) || "upacp_pc".equals(channel)) {
                    payType = "3";
                    bankCardInput = amountYuan;
                }
                //TODO 实际支付数据与支付数据校对

                OrderDto order = orderService.getOrder(orderNum);
                if (order != null && (order.getStatus().equals(OrderStatus.payCheck)||order.getStatus().equals(OrderStatus.waitPay))) {
                    if (orderService.orderPaySuccess(orderNum,payType).getIsSuccess()) {
                     /*  System.out.println("订单结算成功");*/
                        response.setStatus(200);
                        //return "订单结算成功";
                    } else {
                      /* System.out.println("订单结算失败");*/
                        //return "订单结算失败";
                        response.setStatus(500);
                    }
                } else {
                  /* System.out.println("该订单不存在");*/
                    //return "该订单不存在";
                    response.setStatus(500);
                }
            }
            //TODO 此次可扩展为多种事件监听
        } else {
           /*System.out.println("签名验证失败");*/
            //return "签名验证失败";
            response.setStatus(500);
        }
        logger.info("order webHook end!");
    }
}
