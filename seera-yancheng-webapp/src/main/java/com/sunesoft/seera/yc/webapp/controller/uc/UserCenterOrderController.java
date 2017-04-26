package com.sunesoft.seera.yc.webapp.controller.uc;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.order.application.criteria.OrderCriteria;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderDto;
import com.sunesoft.seera.yc.core.order.domain.OrderStatus;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zwork on 2016/8/27.
 */
@Controller
public class UserCenterOrderController extends Layout {

    @Autowired
    UserSession us;
    @Autowired
    IOrderService orderService;
    @Autowired
    ITouristService touristService;

    //订单列表
    @RequestMapping(value = "orderlist/{type}")
    public ModelAndView orderlist(Model model,HttpServletRequest request,@PathVariable("type") String type) {
        if (us == null|| null ==us.getCurrentUser()) {
            model.addAttribute("error", "请登录");
            return view(activeLayout, "/login/login", model);
        }
        Long touristId = us.getCurrentUserId();
        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (touristId == null) {
            model.addAttribute("error", "请登录");
            return view(activeLayout, "/login/login", model);
        }else {
            model.addAttribute("user", uniqueResult.getT());
        }
        OrderCriteria criteria = new OrderCriteria();
        if(type.equals("prepay")){
            criteria.setStatus(OrderStatus.waitPay);
            model.addAttribute("type",type);
        }else if(type.equals("complete")){
            model.addAttribute("type",type);
            criteria.setStatus(OrderStatus.end);
        }else if(type.equals("cancel")){
            model.addAttribute("type",type);
            criteria.setStatus(OrderStatus.canceled);
        }else{
            model.addAttribute("type","all") ;
        }
        if(!StringUtils.isNullOrWhiteSpace(request.getParameter("num"))){
            criteria.setNum(request.getParameter("num"));
            model.addAttribute("num",request.getParameter("num"));
        }
        if(!StringUtils.isNullOrWhiteSpace(request.getParameter("pageNumber"))){
            criteria.setPageNumber(Integer.parseInt(request.getParameter("pageNumber")));
        }
        criteria.setTouristId(String.valueOf(touristId));

        PagedResult<OrderDto> pg = orderService.getOrders(criteria);
        //最后将放置pg
        model.addAttribute("pg", pg);
        model.addAttribute("orders", pg.getItems());
        return view(activeLayout, "uc/orderList", model);
    }

    /**
     * 用户中心订单详情页
     * @param model
     * @param orderId
     * @return
     */
    @RequestMapping("orderdetail/{orderId}")
    public ModelAndView orderdetail(Model model,@PathVariable String orderId) {
        Long touristId = us.getCurrentUserId();
        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (touristId == null) {
            model.addAttribute("error", "请登录");
            return view(activeLayout, "/login/login", model);
        }else {
            model.addAttribute("user", uniqueResult.getT());
        }
        OrderDto orderDto=orderService.getOrder(orderId);
        model.addAttribute("order",orderDto);
        return view(activeLayout, "uc/orderDetail", model);
    }

    /**
     * 用户中心订单详情页
     * @param
     * @return
     */
    @RequestMapping(value="ajax_update_password", method= RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_update_password(HttpServletRequest request, HttpServletResponse response) {

        String oldPassword=request.getParameter("oldPassword");
        String newPassword=request.getParameter("newPassword");
        Long touristId = us.getCurrentUserId();
        CommonResult commonResult=touristService.updatePassWord(touristId,oldPassword,newPassword);
        if(commonResult.getIsSuccess()){
            us.removeUserCookie(request,response);
        }
        return commonResult;
    }
}
