package com.sunesoft.seera.yc.webapp.controller.order;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductItemDto;
import com.sunesoft.seera.yc.core.product.application.IProductItemService;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhaowy on 2016/9/8.
 * 订单商品项领取控制器
 */
@Controller
public class OrderItemTakeAction extends Layout {

    @Autowired
    IOrderService orderService;
    @Autowired
    UserSession us;
    @Autowired
    IProductItemService itemService;

    @RequestMapping(value = "sra_t_productTake")
    public ModelAndView index(Model model) {
        return view(layout, "order/productTake", model);
    }

    /**
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "ajax_orderItem_query", method = RequestMethod.POST)
    public void orderItemQuery(HttpServletRequest request,HttpServletResponse response) throws Exception {
        //TODO 安全认证
        String takeNum = request.getParameter("takeNum");
//        String orderno = param.split("-")[0];
//        String takeNum = param.split("-")[1];
        //TODO 当前扫描员能否派件指定二维码的商品项
        UniqueResult result;
        Long currentManagerId = us.getCurrentUser(request).getId();
        OrderProductItemDto item = orderService.getOrderItem(takeNum);
        if(item==null)
            result= new UniqueResult<>("商品消费编码不存在！");
        else {
            //排除不在当前用户权限下的扫码数据
            if (item.getInnerManageId().equals(currentManagerId))
                result = new UniqueResult<>(item);
            else result = new UniqueResult<>("此商品不能在此处领取！");
        }
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);
    }

    /**
     * @param takeNum
     * @throws Exception
     */
    @RequestMapping(value = "ajax_itemTake", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult itemTake(String takeNum) throws Exception {
        //TODO 安全认证
        if(StringUtils.isNullOrWhiteSpace(takeNum)) return ResultFactory.commonError("取货码不正确");
        return orderService.productItemTake(takeNum);
    }
}
