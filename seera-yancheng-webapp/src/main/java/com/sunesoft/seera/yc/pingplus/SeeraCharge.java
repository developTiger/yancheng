/**
 * Ping++ Server SDK
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可根据自己网站需求按照技术文档编写, 并非一定要使用该代码。
 * 接入支付流程参考开发者中心：https://www.pingxx.com/docs/server/charge ，文档可筛选后端语言和接入渠道。
 * 该代码仅供学习和研究 Ping++ SDK 使用，仅供参考。
 */
package com.sunesoft.seera.yc.pingplus;

import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderDto;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductDto;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.sunesoft.seera.yc.webapp.utils.IP.getClientIpAddr;

/**
 * Charge 对象相关示例
 * <p>
 * 该实例程序演示了如何从 Ping++ 服务器获得 charge ，查询 charge。
 * <p>
 * 开发者需要填写 apiKey 和 appId ，
 * <p>
 * apiKey 有 TestKey 和 LiveKey 两种。
 * <p>
 * TestKey 模式下不会产生真实的交易。
 */
@Controller
public class SeeraCharge {

    @Autowired
    private IOrderService service;

    @RequestMapping(value = "/order/pay", method = RequestMethod.POST)
    public void createCharge(HttpServletRequest request,HttpServletResponse response) {
        String orderNum = request.getParameter("orderNum");
        String channel = request.getParameter("channel");
        OrderDto dto = service.getOrder(orderNum);
        String orderName = "";
        for(OrderProductDto orderProductDto : dto.getProductDtos()){
            if(orderName.equals("")){
                orderName = orderProductDto.getProductDto().getName();
            }
            else {
                orderName+="+"+orderProductDto.getProductDto().getName();
            }
        }
        if(orderName.length()>25){
            orderName = orderName.substring(0,25)+"…";
        }

        switch (channel) {

            case "alipay"://支付宝手机支付
                break;
            case "alipay_wap"://支付宝手机网页支付
//                break;
            case "alipay_qr"://支付宝扫码支付
            case "wx_pub_qr"://微信公众号扫码支付
            case "alipay_pc_direct"://支付宝 PC 网页支付
            case "upacp_pc"://银联 PC 网页支付
            case "cp_b2b"://银联企业网银支付
                AjaxResponse.write(response, "text", ChargeUtil.createCharge(dto.getOrderPrice(), orderName, orderNum, getClientIpAddr(request), channel).toString() );
                return;
            case "wx"://微信支付
                break;
            case "wx_pub"://微信公众号支付
                AjaxResponse.write(response, "text", ChargeUtil.createChargeWithOpenid("微信 openId", dto.getOrderPrice(), orderNum, getClientIpAddr(request)).toString());
            default:
                break;
        }

    }
}
