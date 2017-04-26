package com.sunesoft.seera.yc.webapp.controller.uc;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.coupon.application.ICouponService;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.order.application.criteria.OrderCriteria;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderDto;
import com.sunesoft.seera.yc.core.order.domain.OrderStatus;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.FeedBackDto;
import com.sunesoft.seera.yc.core.shoppingCar.application.IShoppingCarService;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.FetcherDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;
import com.sunesoft.seera.yc.core.tourist.domain.TouristGender;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bing on 16/8/15.
 */
@Controller
public class UserCenterController extends Layout {

    @Autowired
    UserSession us;
    @Autowired
    IOrderService orderService;
    @Autowired
    ICouponService couponService;
    @Autowired
    ITouristService touristService;
    @Autowired
    IShoppingCarService shoppingCarService;
    @Autowired
    IProductService productService;

    @RequestMapping(value = "error")
    public ModelAndView fd( Model model) {
        return view(activeLayout, "error/error", model);
    }

    @RequestMapping(value = "order_evaluation/{pid}")
    public ModelAndView order_evaluation(@PathVariable String pid, Model model) {

          /*  ProductSimpleDto productDto = productService.getSimple(pid);
            if (productDto == null) {
                model.addAttribute("productDto", "该商品已经停止销售");
            } else {
                model.addAttribute("productSimpleDto", productDto);
            }*/

        OrderDto orderDto=orderService.getOrder(pid);
        model.addAttribute("orderNum",pid);
        model.addAttribute("productDtos",orderDto.getProductDtos());
        return view(activeLayout, "uc/order_evaluation", model);
    }

    @RequestMapping(value = "ajax_product_evaluation")
    @ResponseBody
    public CommonResult ajax_evaluation(HttpServletRequest request,FeedBack feedBack) {
        Long touristId = us.getCurrentUserId();
        List<FeedBackDto> dto = feedBack.getFeedBackDtos();
        String orderNum = request.getParameter("orderNum");
        for(int i=0;i<dto.size();i++){
            dto.get(i).setContent(dto.get(i).getCommentProduct());
            dto.get(i).setTouristId(touristId);
        }
        return productService.addProductFeedBack(dto,orderNum);

    }


    /**
     * 不知道需要做什么的页面
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "x")
    public ModelAndView order_eva(Model model, HttpServletRequest request, HttpServletResponse response) {
        return view(activeLayout, "uc/oderEva", model);
    }

    @RequestMapping(value = "edit_user")
    public ModelAndView editUser(Model model, HttpServletRequest request) throws Exception {
        Long touristId = us.getCurrentUserId();
        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (uniqueResult == null || uniqueResult.getT() == null) {
            model.addAttribute("userError", "该用户不存在");
        } else {
            model.addAttribute("user", uniqueResult.getT());
        }
        String editOrshowFlag = request.getParameter("EditOrShow");
        if (StringUtils.isNullOrWhiteSpace(editOrshowFlag)) {
            model.addAttribute("edit_select", false);
        } else {
            model.addAttribute("edit_select", true);
        }
        return view(activeLayout, "uc/editUser", model);
    }


    @RequestMapping(value = "ajax_update_user", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_update_user(Model model, HttpServletRequest request) throws Exception {
        Long touristId = us.getCurrentUserId();
        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (uniqueResult == null) {
            return new CommonResult(false, "用户不存在");
        } else {
            model.addAttribute("user", uniqueResult.getT());
            TouristDto dto = uniqueResult.getT();
            dto.setMobilePhone(request.getParameter("mobilePhone"));
            dto.setRealName(request.getParameter("realName"));
            dto.setUserName(request.getParameter("userName"));
            dto.setWxName(request.getParameter("wxName"));
            dto.setEmail(request.getParameter("email"));
//            dto.setAge(Integer.parseInt(request.getParameter("age")));
//            dto.setIdCardNo(request.getParameter("idCardNo"));
            //设置性别
            switch (request.getParameter("gender").toLowerCase()) {
                case "male":
                    dto.setGender(TouristGender.Male);
                    break;
                case "female":
                    dto.setGender(TouristGender.Female);
                    break;
                default:
                    dto.setGender(TouristGender.Unknown);
                    break;
            }
            return touristService.update(dto);
        }
    }

    //订单列表
    @RequestMapping(value = "orderlist")
    public ModelAndView orderlist(Model model, HttpServletRequest request, HttpServletResponse response) {
        Long touristId = us.getCurrentUserId();
        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (uniqueResult == null || uniqueResult.getT() == null) {
            model.addAttribute("userError", "该用户不存在");
        } else {
            model.addAttribute("user", uniqueResult.getT());
        }
        OrderCriteria criteria = new OrderCriteria();
        criteria.setTouristId(String.valueOf(touristId));

        String searchOrder = request.getParameter("tourId");
        if (!StringUtils.isNullOrWhiteSpace(searchOrder)) {
            criteria.setProductId(Long.parseLong(searchOrder));
        }
        String fromOrderTime = request.getParameter("fromOrderTime");
        if (!StringUtils.isNullOrWhiteSpace(fromOrderTime)) {
            criteria.setFromOrderTime(DateHelper.parse(fromOrderTime));
        }
        String endOrderTime = request.getParameter("endOrderTime");
        if (!StringUtils.isNullOrWhiteSpace(endOrderTime)) {
            criteria.setFromOrderTime(DateHelper.parse(endOrderTime));
        }

        String paging = request.getParameter("paging");
        if (!StringUtils.isNullOrWhiteSpace(paging)) {
            criteria.setPageNumber(Integer.parseInt(paging));
        }

        PagedResult<OrderDto> pg = orderService.getOrders(criteria);
        //最后将放置pg
        model.addAttribute("pg", pg);
        model.addAttribute("orders", pg.getItems());
        return view(activeLayout, "uc/orderList", model);
    }


    //订单列表 订单收索 num
    @RequestMapping(value = "orderNum")
    public ModelAndView orderNum(Model model, HttpServletRequest request) {
        Long touristId = us.getCurrentUserId();
        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (uniqueResult == null || uniqueResult.getT() == null) {
            model.addAttribute("userError", "该用户不存在");
        } else {
            model.addAttribute("user", uniqueResult.getT());
        }
        OrderCriteria criteria = new OrderCriteria();
        criteria.setTouristId(String.valueOf(touristId));

        String searchOrder = request.getParameter("orderNum");
        if (!StringUtils.isNullOrWhiteSpace(searchOrder)) {
            criteria.setNum(searchOrder);
        }
        PagedResult<OrderDto> pg = orderService.getOrders(criteria);
        //最后将放置pg
        model.addAttribute("pg", pg);
        return view(activeLayout, "uc/orderList", model);
    }

    //订单列表 条件查询
    @RequestMapping(value = "order_type_list")
    public ModelAndView orderType(Model model, HttpServletRequest request) {
        Long touristId = us.getCurrentUserId();
        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (uniqueResult == null || uniqueResult.getT() == null) {
            model.addAttribute("userError", "该用户不存在");
        } else {
            model.addAttribute("user", uniqueResult.getT());
        }
        OrderCriteria criteria = new OrderCriteria();
        criteria.setTouristId(String.valueOf(touristId));
        OrderStatus orderStatus = null;
        String searStatus = request.getParameter("status");
        switch (searStatus) {
            case "all":
                orderStatus = null;
                break;
            case "waitpay":
                orderStatus = OrderStatus.waitPay;
                break;
            case "waitComment":
                orderStatus = OrderStatus.waitComment;
                break;
            case "end":
                orderStatus = OrderStatus.end;
                break;
            case "cancel":
                orderStatus = OrderStatus.canceled;
                break;
        }
        criteria.setStatus(orderStatus);
        PagedResult<OrderDto> pg = orderService.getOrders(criteria);
        model.addAttribute("pg", pg);
        return view(activeLayout, "uc/orderList", model);
    }

    //去支付
    @RequestMapping(value = "goto_pay/{pid}")
    public ModelAndView gotoPay(@PathVariable Long pid, Model model) {
        Long touristId = us.getCurrentUserId();
        if (touristId == null)
            return view(activeLayout, "login/login", model);
        if (pid == null) return view(activeLayout, "uc/orderList", model);
        OrderDto dto = orderService.getOrder(pid);
        us.getCurrentSession().setAttribute("order", dto);
        return view(activeLayout, "支付页面", model);
    }


    @RequestMapping(value = "ajax_order_details", method = RequestMethod.POST)
    public CommonResult order_details(OrderCriteria criteria, Model model) {
        Long touristId = us.getCurrentUserId();
        if (touristId == null) {
            return new CommonResult(false, "未登录");
        }
        criteria.setTouristId(String.valueOf(touristId));
        PagedResult<OrderDto> pg = orderService.getOrders(criteria);
        if (pg == null) {
            return new CommonResult(false, "暂无符合查询条件的订单");
        } else {
            //最终放pg
            model.addAttribute("orders", pg.getItems());
            return new CommonResult(true);
        }
    }

    //单个订单细节查询
    @RequestMapping(value = "order_detail/{pid}")
    public ModelAndView order_detail(@PathVariable String pid, Model model, HttpServletRequest request) {
        Long touristId = us.getCurrentUserId();
        if (pid == null) {
            model.addAttribute("error", "请选择订单");
            return view(activeLayout, "uc/order_details", model);
        }
        OrderDto dto = orderService.getOrder(pid);
        model.addAttribute("order", dto);
        model.addAttribute("productDtos", dto.getProductDtos());
        return view(activeLayout, "uc/order_details", model);
    }

    @RequestMapping(value = "ticketHolderInfo")
    public ModelAndView ticketHolderInfo(Model model) throws Exception {
        Long touristId = us.getCurrentUserId();
        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (uniqueResult == null || uniqueResult.getT() == null) {
            model.addAttribute("userError", "该用户不存在");
        } else {
            model.addAttribute("user", uniqueResult.getT());
        }
        List<FetcherDto> fetchers = touristService.getAllFetchers(touristId);
        model.addAttribute("fetchers", fetchers);
        model.addAttribute("count", fetchers == null ? 0 : fetchers.size());
        return view(activeLayout, "uc/ticketHolderInfo", model);
    }

    @RequestMapping(value = "updatePassword")
    public ModelAndView updatePassword(Model model, HttpServletRequest request) throws Exception {
        Long touristId = us.getCurrentUserId();
        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (uniqueResult == null || uniqueResult.getT() == null) {
            model.addAttribute("userError", "该用户不存在");
        } else {
            model.addAttribute("user", uniqueResult.getT());
        }
        return view(activeLayout, "uc/updatePassword", model);
    }


    @ResponseBody
    @RequestMapping(value = "ajax_edit_password")
    public CommonResult ajax_edit_password(Model model, HttpServletRequest request) throws Exception {
        Long touristId = us.getCurrentUserId();
        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (uniqueResult == null || uniqueResult.getT() == null) {
            model.addAttribute("userError", "该用户不存在");
        } else {
            model.addAttribute("user", uniqueResult.getT());
        }
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        String loginName = uniqueResult.getT().getUserName();
        if (StringUtils.isNullOrWhiteSpace(newPassword) || StringUtils.isNullOrWhiteSpace(confirmPassword))
            return ResultFactory.commonError("新密码与认密码不能为空");

        if (!newPassword.equals(confirmPassword))
            return ResultFactory.commonError("新密码与确认密码不一致");

        if (!StringUtils.isNullOrWhiteSpace(oldPassword)) {
            UniqueResult<TouristSimpleDto> ur = touristService.login(loginName, oldPassword);
            if (ur.getIsSuccess())
                return touristService.restPassword(touristId, newPassword);
             else
                return ResultFactory.commonError(ur.getMsg());
        } else
            return ResultFactory.commonError("请输入原始密码");
    }


    @ResponseBody
    @RequestMapping(value = "ajax_getUpdateHolderInfo/{fetchersId}")
    public FetcherDto ajax_getUpdateHolderInfo(@PathVariable(value = "fetchersId") String fetchersId) throws Exception {
        Long touristId = us.getCurrentUserId();
        FetcherDto fetcherDto = touristService.getByFetcherId(touristId, Long.parseLong(fetchersId));
        return fetcherDto;
    }

    /*
     *获取更新的取票人信息
     */
    @ResponseBody
    @RequestMapping(value = "ajax_delete_ticket_holder/{fetchersId}")
    public CommonResult ajax_delete_ticket_holder(@PathVariable(value = "fetchersId") String fetchersId) throws Exception {
        Long touristId = us.getCurrentUserId();
        List<Long> ids = new ArrayList<>();
        ids.add(Long.parseLong(fetchersId));
        return touristService.remove(touristId, ids);
    }


    @RequestMapping(value = "setDefautFetcher/{pid}")
    @ResponseBody
    public CommonResult setDefautFetcher(@PathVariable(value = "pid") Long pid) {
        Long touristId = us.getCurrentUserId();
        if (pid <= 0)
            return new CommonResult(false, "请选择取票人");
        return touristService.setDefault(touristId, pid);
    }

    /*
     * 新增or更新取票人
     */
    @ResponseBody
    @RequestMapping(value = "ajax_ticket_holderInfo", method = RequestMethod.POST)
    public CommonResult ajax_ticket_holderInfo(FetcherDto dto) throws Exception {
        Long touristId = us.getCurrentUserId();
        if (dto.getId() == null) {
            return touristService.create(touristId, dto);
        } else {
            return touristService.updateNoSame(touristId, dto);
        }
    }

    @RequestMapping(value = "CouponList")
    public ModelAndView ticket(Model model) throws Exception {
        Long touristId = us.getCurrentUserId();
        if (touristId == null) {
            model.addAttribute("error", "请登录");
            return view(activeLayout, "/login/login", model);
        }

        UniqueResult<TouristDto> uniqueResult = touristService.getTourist(touristId);
        if (uniqueResult == null || uniqueResult.getT() == null) {
            model.addAttribute("userError", "该用户不存在");
        } else {
            model.addAttribute("user", uniqueResult.getT());
        }
        List<CouponDto> couponDtos = touristService.getAllCoupons(touristId);
        model.addAttribute("couponDtos", couponDtos);
        model.addAttribute("count", couponDtos == null ? 0 : couponDtos.size());
        return view(activeLayout, "uc/coupon", model);
    }

    @RequestMapping(value = "select_CouponList")
    public ModelAndView selectTicket(Model model, HttpServletRequest request) throws Exception {
        Long touristId = us.getCurrentUserId();
        if (touristId == null) {
            model.addAttribute("error", "请登录");
            return view(activeLayout, "/login/login", model);
        }
        String status = request.getParameter("couponStatus");
        CouponStatus couponStatus = null;
        switch (status) {
            case "used":
                couponStatus = CouponStatus.Used;
                break;
            case "expired":
                couponStatus = CouponStatus.Expired;
                break;
            case "noUsed":
                couponStatus = CouponStatus.bind;
                break;
            default:
                couponStatus = null;
                break;

        }
        List<CouponDto> couponDtos = touristService.getAllCoupons(touristId, couponStatus);
        model.addAttribute("couponDtos", couponDtos);
        model.addAttribute("count", couponDtos == null ? 0 : couponDtos.size());
        return view(activeLayout, "uc/coupon", model);
    }


    @RequestMapping(value = "add_ticket", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addTicket(HttpServletRequest request, HttpServletResponse response) {
        Long touristId = us.getCurrentUserId();
        if (touristId == null) return new CommonResult(false, "您未登录");
        String addTicket = request.getParameter("add_ticket");
        return touristService.bindCoupon(touristId, addTicket);
    }

    @RequestMapping(value = "ajax_delete_one_ticket",method=RequestMethod.POST)
    public CommonResult deleteOneTicket(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //游客
        Long touristId = us.getCurrentUserId();
        if (touristId == null) {
            response.sendRedirect("/login/login");
            return null;
        }
        //选择的优惠券
        String deltId = request.getParameter("deleteId");
        if (StringUtils.isNullOrWhiteSpace(deltId)) {
            return new CommonResult(false, "请选中优惠券");
        } else {
            return touristService.removeCoupon(touristId, Long.parseLong(deltId));
        }
    }

    @RequestMapping(value = "ajax_update_date", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_update_date(HttpServletRequest request, HttpServletResponse response) {

        String tourDate = request.getParameter("tourDate");
        String hotelDate = request.getParameter("hotelDate");
        String orderProductId = request.getParameter("orderProductId");
        return orderService.meal(Long.parseLong(orderProductId), DateHelper.parse(tourDate, "yyyy-MM-dd"), DateHelper.parse(hotelDate, "yyyy-MM-dd"));

    }


}
