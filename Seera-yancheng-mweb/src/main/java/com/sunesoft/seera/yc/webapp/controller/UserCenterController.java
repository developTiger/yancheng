package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.UniqueResult;

import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.order.application.criteria.OrderCriteria;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderDto;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductDto;
import com.sunesoft.seera.yc.core.order.domain.OrderStatus;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.FeedBackDto;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.FetcherDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.domain.TouristGender;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.model.FeedBack;
import org.apache.commons.lang.StringUtils;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jade on 16/8/18.
 */
@Controller
public class UserCenterController extends BaseController {

    @Autowired
    ITouristService iTouristService;

    @Autowired
    UserSession userSession;

    @Autowired
    ITouristService touristService;

    @Autowired
    IOrderService iOrderService;

    @Autowired
    IProductService productService;

    /**
     * 用户中心
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/uc.html")
    public ModelAndView uc_view(Model model) {
        return view("/uc/index", model);
    }

    /**
     * 基本信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/uc/userInfo")
    public ModelAndView uc_baseinfo_view(Model model) {

        UniqueResult<TouristDto> dto = iTouristService.getTourist(us.getCurrentUserId());
        if (dto.getIsSuccess()) {
            model.addAttribute("touristDto", dto.getT());
        }
        return view("/uc/baseInfo", model);
    }

    /**
     * 基本信息-修改
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/uc/updateUserInfo", method = RequestMethod.POST)
    public String uc_baseinfo(Model model, HttpServletRequest request) {
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String mail = request.getParameter("mail");
        String mobilePhone = request.getParameter("mobilePhone");

        String password = request.getParameter("password");
        if(!com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(password)){
            touristService.restPassword(us.getCurrentUserId(),password);
        }

        UniqueResult result = touristService.getTourist(us.getCurrentUserId());
        if (result.getIsSuccess()) {
            TouristDto touristDto = (TouristDto) result.getT();

            touristDto.setUserName(userName);
            touristDto.setGender(TouristGender.valueOf(gender));
            touristDto.setEmail(mail);
            touristDto.setMobilePhone(mobilePhone);

            iTouristService.update(touristDto);
        }
        return "redirect:/uc.html";
    }

    /**
     * 订单列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/uc/order/list/{type}.html")
    public ModelAndView order_list_view(@PathVariable String type, Model model,HttpServletRequest request) {
        if(us.getCurrentUser()==null){
            return new ModelAndView("redirect:"+us.authur(request.getRequestURI()));
        }
        OrderCriteria criteria = new OrderCriteria();
        if (!StringUtils.equals("all", type)) {
            criteria.setStatus(OrderStatus.valueOf(type));
        }
        criteria.setTouristId(userSession.getCurrentUser().getId().toString());
        PagedResult<OrderDto> pr = iOrderService.getOrders(criteria);
        model.addAttribute("orderDtos",pr.getItems());
        model.addAttribute("type",type);
        return view("/uc/orderList", model);
    }

    /**
     * 订单详情
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/uc/order/{id}.html")
    public ModelAndView order_detail_view(@PathVariable long id, Model model) {

        OrderDto orderDto = iOrderService.getOrder(id);
        model.addAttribute("orderDto", orderDto);
        return view("/uc/orderDetail", model);
    }

    /**
     * 评价
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/uc/order/evaluate.html")
    public ModelAndView order_evaluate_view(Model model) {
        return view("/uc/orderEvaluate", model);
    }


    /**
     * 取票人列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/uc/receiverlist.html")
    public ModelAndView order_receiverlist_view(Model model) {

        //获取所有取件人
        List<FetcherDto> fetcherDtoList = iTouristService.getAllFetchers(userSession.getCurrentUserId());
        model.addAttribute("fetcherDtoList", fetcherDtoList);
        return view("/uc/receiverList", model);
    }

    /**
     * 取票人修改
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/uc/receiver.html")
    public ModelAndView order_receiver_view(HttpServletRequest request, Model model) {

        Long touristId = us.getCurrentUserId();
        String fetcherId = request.getParameter("id");
        if (StringUtils.isNotEmpty(fetcherId)) {
            FetcherDto fetcherDto = touristService.getByFetcherId(touristId, Long.valueOf(fetcherId));
            model.addAttribute("fetcherDto", fetcherDto);
        }

        return view("/uc/receiverForm", model);
    }

    /**
     * 收件人
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/uc/receiver", method = RequestMethod.POST)
    public ModelAndView receiver_edit(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = request.getParameter("id");
        String cardNo = request.getParameter("cardNo");
        String isDefault = request.getParameter("isDefault");
        String mobilePhone = request.getParameter("mobilePhone");
        String realName = request.getParameter("realName");
        Long touristId = us.getCurrentUserId();

        FetcherDto fetcherDto = new FetcherDto();
        fetcherDto.setIdCardNo(cardNo);
        if (StringUtils.equals(isDefault, "on")) {
            fetcherDto.setIsDefault(true);
        } else {
            fetcherDto.setIsDefault(false);
        }
        fetcherDto.setMobilePhone(mobilePhone);
        fetcherDto.setRealName(realName);
        if (StringUtils.isNotEmpty(id)) {
            fetcherDto.setId(Long.valueOf(id));
            touristService.update(touristId, fetcherDto);
        } else {
            touristService.create(touristId, fetcherDto);
        }

        response.sendRedirect("/uc/receiverlist.html");
        return null;
    }

    /**
     * 删除
     *
     * @param request
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uc/receiver/del", method = RequestMethod.POST)
    public CommonResult order_receiver_del(HttpServletRequest request, Model model) {

        Long touristId = us.getCurrentUserId();
        String fetcherId = request.getParameter("id");

        List<Long> fetcherIds = new ArrayList<>();
        fetcherIds.add(Long.valueOf(fetcherId));

        return touristService.remove(touristId, fetcherIds);
    }

    /**
     * 优惠券
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/uc/myCoupon")
    public ModelAndView coupon_view(HttpServletRequest request,Model model) {
        if(us.getCurrentUser()==null){
            return new ModelAndView("redirect:"+us.authur(request.getRequestURI()));
        }

        String strStatus =request.getParameter("couponStatus");
        if(com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(strStatus))
            strStatus="bind";
        CouponStatus status = CouponStatus.valueOf(strStatus);

        List<CouponDto> couponDtos = iTouristService.getCouponsByStatus(userSession.getCurrentUserId(), status);
        model.addAttribute("couponDtos", couponDtos);
        model.addAttribute("type",strStatus);
        return view("/uc/coupon", model);
    }
    @ResponseBody
    @RequestMapping(value = "/uc/ajax_confirm_conpon", method = RequestMethod.POST)
    public UniqueResult ajax_confirm_conpon(HttpServletRequest request,Model model) {
        String cno =request.getParameter("uconponNum");

        CommonResult result= iTouristService.bindCoupon(userSession.getCurrentUserId(), cno);

        if(result.getIsSuccess()){

            CouponDto couponDto = iTouristService.getByNum(userSession.getCurrentUserId(), cno);
            return new UniqueResult(couponDto);
        }
        return new UniqueResult(result.getMsg());
    }







    @RequestMapping(value = "/uc/changeDate")
    public ModelAndView changeUseDate(Model model,HttpServletRequest request){
        String productid = request.getParameter("productid");
        String orderNum = request.getParameter("orderNum");

        UniqueResult<OrderProductDto> dto = iOrderService.getOrderProduct(Long.parseLong(productid));
        model.addAttribute("bean",dto.getT());
        model.addAttribute("orderNum",orderNum);

        return view("/uc/changeUseDate",model);
    }

    @RequestMapping(value = "/uc/comment")
    public ModelAndView comment(Model model,HttpServletRequest request){
        String ordernum = request.getParameter("order_no");
        OrderDto dto = iOrderService.getOrder(ordernum);
        model.addAttribute("orderDto",dto);
        return view("/uc/comment",model);
    }





    @RequestMapping(value = "ajax_product_evaluation")
    @ResponseBody
    public CommonResult ajax_evaluation(HttpServletRequest request,FeedBack feedBack) {
        Long touristId = us.getCurrentUserId();
        List<FeedBackDto> dto = feedBack.getFeedBackDtos();
        String orderNum = request.getParameter("order_no");
        for(int i=0;i<dto.size();i++){
           dto.get(i).setContent(dto.get(i).getCommentProduct());
            dto.get(i).setTouristId(touristId);
        }
        return productService.addProductFeedBack(dto,orderNum);

    }

    @RequestMapping(value = "ajax_update_date", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_update_date(HttpServletRequest request, HttpServletResponse response) {

        String tourDateStr = request.getParameter("tourDate");
        String hotelDateStr = request.getParameter("hotelDate");
        Date tourDate=null;
        Date hotelDate=null;
        if(!com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(tourDateStr)){
            tourDate= DateHelper.parse(tourDateStr, "yyyy-MM-dd");
        }

        if(!com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(hotelDateStr)){
            hotelDate= DateHelper.parse(hotelDateStr, "yyyy-MM-dd");
        }
        String orderProductId = request.getParameter("orderProductId");
        return iOrderService.meal(Long.parseLong(orderProductId), tourDate , hotelDate);

    }


}







