package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.QRCodeGenerator;
import com.sunesoft.seera.fr.web.ServletActionContext;
import com.sunesoft.seera.yc.core.coupon.application.ICouponService;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.order.application.criteria.CreateOrder;
import com.sunesoft.seera.yc.core.order.application.criteria.CreateOrderProduct;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderDto;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductDto;
import com.sunesoft.seera.yc.core.order.domain.OrderInfo;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.shoppingCar.application.IShoppingCarService;
import com.sunesoft.seera.yc.core.shoppingCar.application.dto.ShoppingItemDto;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.FetcherDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.model.OrderConfirm;
import com.sunesoft.seera.yc.webapp.model.OrderProductModel;
import com.sunesoft.seera.yc.webapp.pingplus.ChargeUtil;
import com.sunesoft.seera.yc.webapp.utils.IP;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.sunesoft.seera.yc.webapp.utils.IP.getClientIpAddr;


/**
 * Created by jade on 16/8/18.
 */
@Controller
public class OrderController extends BaseController {
    private static String specialConpon = Configs.getProperty("couponId");
    private static String specialticket = Configs.getProperty("nightPark");
    @Autowired
    private IShoppingCarService shoppingCarService;

    @Autowired
    private UserSession userSession;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    ITouristService touristService;

    @Autowired
    ICouponService couponService;


    /**
     * 购物车
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/order/shoppingCart.html")
    public ModelAndView shoppingCart_view(Model model) {
        List<ShoppingItemDto> items = shoppingCarService.getTouristShoppingItems(userSession.getCurrentUserId());
        if (items != null)
            model.addAttribute("shoppingItems", items);
        else
            model.addAttribute("itemsError", "购物车里暂没商品，赶紧去添加");
        return view("/order/shoppingCart", model);
    }


    //"?fetcherId=" + fetcherId + "&couponI=" + couponId;
    @RequestMapping(value = "/order/receiverlist")
    public ModelAndView receiverlist(Model model, Long fetcherId, Long couponId, HttpServletRequest request) {

        if (fetcherId != null) {
            model.addAttribute("fetcherId", fetcherId);
        }
        if (couponId != null) {
            model.addAttribute("couponId", couponId);
        }
        //获取所有取件人
        List<FetcherDto> fetcherDtoList = touristService.getAllFetchers(userSession.getCurrentUserId());
        model.addAttribute("fetcherDtoList", fetcherDtoList);
        return view("/order/receiverList", model);
    }


    @RequestMapping(value = "/order/couponlist")
    public ModelAndView couponlist(Model model, Long fetcherId, Long couponId, HttpServletRequest request) {
        if (fetcherId != null) {
            model.addAttribute("fetcherId", fetcherId);
        }
        if (couponId != null) {
            model.addAttribute("couponId", couponId);
        }
        List<CouponDto> couponDtos = touristService.getAllCoupons(userSession.getCurrentUserId(), CouponStatus.Valid);
        model.addAttribute("couponDtos", couponDtos);
        model.addAttribute("count", couponDtos == null ? 0 : couponDtos.size());
        return view("order/coupon", model);
    }

    /**
     * 加入购物车
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/order/shoppingCart", method = RequestMethod.POST)
    public CommonResult shoppingCart(HttpServletRequest request) {
        String pid = request.getParameter("id");
        String count = request.getParameter("num");
        String tourScheduleDate = request.getParameter("tourScheduleDate");
        String hotelScheduleDate = request.getParameter("hotelScheduleDate");

        Long id = Long.valueOf(pid);

        ProductSimpleDto productSimpleDto = iProductService.getSimple(id);
        if (productSimpleDto == null)
            return new CommonResult(false, "商品不存在!");

        ShoppingItemDto dto = new ShoppingItemDto();
        dto.setCount(Integer.valueOf(count));
        dto.setTouristId(userSession.getCurrentUserId());
        dto.setProductId(id);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isNotEmpty(hotelScheduleDate)) {
                dto.setHotelScheduleDate(simpleDateFormat.parse(hotelScheduleDate));
            }
            if (StringUtils.isNotEmpty(tourScheduleDate)) {
                dto.setTourScheduleDate(simpleDateFormat.parse(tourScheduleDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return shoppingCarService.addItem(dto);
    }

    /**
     * 单个移除购物车商品项数量
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/order/shoppingCart/del/{id}", method = RequestMethod.POST)
    public CommonResult singleRemoveCount(@PathVariable Long id) throws Exception {
        if (id == null) return new CommonResult(false, "输入的ids有误");
        return shoppingCarService.removeItem(id);
    }

    /**
     * 订单确认
     *
     * @param orderConfirm
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/order/ajax_confirm", method = RequestMethod.POST)
    public CommonResult order_confirm(OrderConfirm orderConfirm, Model model) {
        if (orderConfirm == null || orderConfirm.getOrderProducts() == null)
            return new CommonResult(false, "提交信息出错!");
        Map<Long, Integer> map = new HashMap<>();

        //设置购物车数量
        for (OrderProductModel orderProduct : orderConfirm.getOrderProducts()) {

            shoppingCarService.SetProductCountAndScheduleDate(orderProduct.getProductId(), orderProduct.getCount(), DateHelper.parse(orderProduct.getHotelScheduleDate(), "yyyy-MM-dd"), DateHelper.parse(orderProduct.getTourScheduleDate(), "yyyy-MM-dd"));
            map.put(orderProduct.getProductId(), orderProduct.getCount());
        }
        //将购物车商品信息加入到session
        userSession.getCurrentSession().setAttribute("shoppingItemIdWithCounts", map);
        return new CommonResult(true, "操作成功");

    }

    /**
     * 订单确认
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/order/createOrder")
    public String order_confirm_view(Long fetcherId, Long couponId, Model model, HttpServletRequest request) throws Exception {
        Map<Long, Integer> map = (Map<Long, Integer>) userSession.getCurrentSession().getAttribute("shoppingItemIdWithCounts");
        List<ShoppingItemDto> items = new ArrayList<>();
        if (map == null || map.isEmpty())
            items = (List<ShoppingItemDto>) userSession.getCurrentSession().getAttribute("shoppingItemDtos");
        else
            items = shoppingCarService.get(new ArrayList<>(map.keySet()));

        List<CreateOrderProduct> createOrderProductLsit = new ArrayList<>();
        for (ShoppingItemDto itemDto : items) {
            CreateOrderProduct product = new CreateOrderProduct();
            product.setProductId(itemDto.productId);
            product.setCount(itemDto.getCount());
            product.setHotelScheduleDate(itemDto.getHotelScheduleDate());
            product.setTourScheduleDate(itemDto.getTourScheduleDate());
            createOrderProductLsit.add(product);

        }
        CreateOrder createOrder = new CreateOrder();
        if (fetcherId != null) {
            createOrder.setCouponId(couponId);
            createOrder.setFetcherDtoId(fetcherId);
        }
        createOrder.setOrderProducts(createOrderProductLsit);
        createOrder.setTouristId(userSession.getCurrentUserId());
        UniqueResult commonResult = orderService.createOrder(createOrder);

        if (commonResult.getIsSuccess()) {
            return "redirect:/order/payInfo?sn=" + ((OrderInfo) commonResult.getT()).getNum();
        } else
            return "redirect:/errorinfo?msg=" + commonResult.getMsg();
    }

    @RequestMapping(value = "/order/confirm")
    public ModelAndView orderConfirm(Model model, Long fetcherId, Long couponId, HttpServletResponse response) {
        Long userId = userSession.getCurrentUserId();
        //获取所有取件人
        //获取所有取件人
        List<FetcherDto> fetcherDtoList = touristService.getAllFetchers(userId);
        model.addAttribute("fetcherDtoList", fetcherDtoList);

        Map<Long, Integer> shoppingItemIdWithCounts = (Map<Long, Integer>) userSession.getCurrentSession().getAttribute("shoppingItemIdWithCounts");

        UniqueResult<TouristDto> touristDtoUniqueResult = touristService.getTourist(userId);
        if (touristDtoUniqueResult.getIsSuccess()) {
            //显示默认取票人
            List<FetcherDto> list = touristDtoUniqueResult.getT().getFetcherDtos();
            if (list != null && list.size() > 0) {
                if (fetcherId == null) {
                    model.addAttribute("defaultFetcher", touristDtoUniqueResult.getT().getDefaultFetcher());
                } else {
                    for (FetcherDto dto : list) {
                        if (dto.getId().equals(fetcherId))
                            model.addAttribute("defaultFetcher", dto);
                    }

                }
                //显示非默认取票人
                list.remove(touristDtoUniqueResult.getT().getDefaultFetcher());
                model.addAttribute("fetcherDtos", list);

            }
            //显示优惠券
            List<CouponDto> couponDtoList = touristDtoUniqueResult.getT().getBindCouponDtos();
            List<CouponDto> couponDtos = new ArrayList<>();
            if (couponDtoList != null) {
                couponDtoList.stream().forEach(i -> {
                    if (i.getCouponStatus() == CouponStatus.bind&&DateHelper.addDay(DateHelper.parse(i.getGqDate(),"yyyy-MM-dd"),1).after(new Date())) couponDtos.add(i);
                });
            }
            model.addAttribute("couponSize", couponDtos == null ? 0 : couponDtos.size());
            model.addAttribute("couponDtos", couponDtos);

            List<ShoppingItemDto> shoppingItemDtos;
            if (shoppingItemIdWithCounts != null && !shoppingItemIdWithCounts.isEmpty()) {
                shoppingItemDtos = shoppingCarService.get(new ArrayList<>(shoppingItemIdWithCounts.keySet()));
            } else {
                shoppingItemDtos = (List<ShoppingItemDto>) userSession.getCurrentSession().getAttribute("shoppingItemDtos");
            }
            if (shoppingItemDtos == null || shoppingItemDtos.size() == 0) {
                try {
                    response.sendRedirect("/order/shoppingCart.html");
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //return view("/order/shoppingCart", model);
            }
            model.addAttribute("shoppingItemDtos", shoppingItemDtos);
        }


        return view("order/confirm", model);
    }

    @RequestMapping(value = "/order/buyNow", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult reservation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String productId = request.getParameter("pid");
        if (StringUtils.isEmpty(productId) || !StringUtils.isNumeric(productId))
            return new CommonResult(false, "商品Id不能为空!");
        String num = request.getParameter("num");
        if (StringUtils.isEmpty(num) || !StringUtils.isNumeric(num)) return new CommonResult(false, "商品数量不能为空!");

        String tourScheduleDate = request.getParameter("tsd");
        String hotelScheduleDate = request.getParameter("hsd");

        long pid = Long.parseLong(productId);
        ProductSimpleDto productSimpleDto = iProductService.getSimple(pid);
        if (productSimpleDto != null) {
            ShoppingItemDto shoppingItemDto = new ShoppingItemDto();
            shoppingItemDto.setProductName(productSimpleDto.getName());
            shoppingItemDto.setPrice(productSimpleDto.getDiscountPrice());
            shoppingItemDto.setSpecDescription(productSimpleDto.getSpecDescription());
            shoppingItemDto.setStock(productSimpleDto.getStock());
            shoppingItemDto.setTouristId(userSession.getCurrentUserId());
            shoppingItemDto.setTouristName(userSession.getCurrentUser().getName());
            shoppingItemDto.setProductId(Long.parseLong(productId));
            shoppingItemDto.setCount(Integer.parseInt(num));
            shoppingItemDto.setRejectAreas(productSimpleDto.getRejectAreas());
            shoppingItemDto.setRejectAreasNames(productSimpleDto.getRejectAreasNames());
            //TODO
            if (StringUtils.isNotEmpty(tourScheduleDate)) {
                shoppingItemDto.setTourScheduleDate(DateHelper.parse(tourScheduleDate, "yyyy-MM-dd"));
            }
            if (StringUtils.isNotEmpty(hotelScheduleDate)) {
                shoppingItemDto.setHotelScheduleDate(DateHelper.parse(hotelScheduleDate, "yyyy-MM-dd"));
            }
            List<ShoppingItemDto> shoppingItemDtos = new ArrayList<>();
            shoppingItemDtos.add(shoppingItemDto);
            userSession.getCurrentSession().setAttribute("shoppingItemDtos", shoppingItemDtos);
        }
        return new CommonResult(true, "添加成功!");
    }

    /**
     * 支付
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/order/payInfo")
    public ModelAndView order_pay_view(Model model, HttpServletRequest request) {
        String ordernum = request.getParameter("sn");
        OrderDto orderDto = orderService.getOrder(ordernum);

        if (!com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(ordernum)) {
            if (orderDto != null) {
                List<OrderProductDto> orderProductDtos = orderDto.getProductDtos();
                List<ShoppingItemDto> itemDtos = new ArrayList<>();
                orderProductDtos.stream().forEach(i -> {
                    ShoppingItemDto dto = new ShoppingItemDto();
                    dto.setProductId(i.getId());
                    dto.setProductName(i.getProductDto().getName());
                    dto.setCount(i.getCount());
                    dto.setPrice(i.getProductDto().getDiscountPrice());

                    itemDtos.add(dto);
                });
                BigDecimal originPrice = BigDecimal.valueOf(0);
                for (OrderProductDto p : orderDto.getProductDtos()) {
                    originPrice = originPrice.add(p.getProductDto().getDiscountPrice().multiply(BigDecimal.valueOf(p.getCount())));
                }
                model.addAttribute("originPrice", originPrice);
                model.addAttribute("orderDto", orderDto);
                model.addAttribute("shoppingItemDtos", itemDtos);
                userSession.getCurrentSession().setAttribute("orderNum", orderDto.getNum());
                userSession.getCurrentSession().removeAttribute("shoppingItemIdWithCounts");
                userSession.getCurrentSession().removeAttribute("shoppingItems");
                return view("order/pay", model);
            }
        }
        return view("error", model);
    }

    /**
     * 支付成功
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/order/pay/success")
    public ModelAndView order_pay_success_view(Model model, HttpServletRequest request, HttpServletResponse response) {

        String ordernum = request.getParameter("order_no");
        OrderDto dto = orderService.getOrder(ordernum);
        model.addAttribute("amount", dto.getOrderPrice());
        model.addAttribute("orderNum", ordernum);
        return view("/order/paySuccess", model);

    }

    /**
     * 支付失败
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/order/pay/error")
    public ModelAndView order_pay_error_view(Model model, HttpServletRequest request) {
        String ordernum = request.getParameter("order_no");
        OrderDto dto = orderService.getOrder(ordernum);
        model.addAttribute("amount", dto.getOrderPrice());
        model.addAttribute("orderNum", ordernum);
        return view("/order/payError", model);
    }

    @RequestMapping("alipay_order_confirm")
    public void alipayConfirm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = request.getParameter("result");
        if ("success".equals(result)) {
            String tradeNo = request.getParameter("out_trade_no");
            CommonResult commonResult = orderService.payOrder(tradeNo);

            if (commonResult.getIsSuccess()) {
                response.sendRedirect("/order/pay/success?order_no=" + tradeNo);
            }
        } else {
            response.sendRedirect("/order/payError");
        }
    }


    @RequestMapping("alipay_wx_confirm")
    public String alipay_wx_confirm(HttpServletRequest request, HttpServletResponse response) throws IOException {

        return "/alipay_wx_success";

    }

    @RequestMapping("wx_order_confirm")
    public void wxConfirm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/orderSuccess");
    }

    @RequestMapping("upacp_order_confirm")
    public void UpacpConfirm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String respMsg = request.getParameter("respMsg");
        if ("success".equals(respMsg)) {
            String orderId = request.getParameter("orderId");
            CommonResult commonResult = orderService.payOrder(orderId);
            if (commonResult.getIsSuccess()) {
                response.sendRedirect("/order/pay/success?order_no=" + orderId);
            }
        } else {
            response.sendRedirect("/order/payError");
        }
    }

    @RequestMapping(value = "orderSuccess")
    public ModelAndView orderSuccess(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ordernum = request.getParameter("order_no");
        OrderDto dto = orderService.getOrder(ordernum);
        //orderService.orderPaySuccess(ordernum);
        model.addAttribute("amount", dto.getOrderPrice());
        model.addAttribute("orderNum", ordernum);
        return view("/order/paySuccess", model);
    }

    @RequestMapping(value = "/order/addReceiverView")
    public ModelAndView recAddView(HttpServletRequest request, HttpServletResponse response, Model model) {

        String fetcherId = request.getParameter("fetcherId");

        String couponId = request.getParameter("couponId");

        model.addAttribute("fetcherId", fetcherId);
        model.addAttribute("couponId", couponId);

        return view("/order/addReceiver", model);

    }

    @RequestMapping(value = "/order/orderDetail")

    public ModelAndView orderDetail(HttpServletRequest request, HttpServletResponse response, Model model) {
        String ordernum = request.getParameter("order_no");
        OrderDto dto = orderService.getOrder(ordernum);

        model.addAttribute("orderDto", dto);

        return view("/order/orderDetail", model);


    }

    /**
     * 收件人
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/addReceiver", method = RequestMethod.POST)
    public void receiver_Add(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String cardNo = request.getParameter("cardNo");
        String isDefault = request.getParameter("isDefault");
        String mobilePhone = request.getParameter("mobilePhone");
        String realName = request.getParameter("realName");
        String orderNo = request.getParameter("orderNo");

        String fetcherId = request.getParameter("fetcherId");

        String couponId = request.getParameter("couponId");

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
        CommonResult result = touristService.create(touristId, fetcherDto);


        String strCouponId = "";
        if (couponId != null)
            strCouponId = couponId.toString();
        response.sendRedirect("/order/confirm?fetcherId=" + result.getId() + "&couponId=" + strCouponId);

    }


    @RequestMapping(value = "/ali_wx_pay.htm")
    public ModelAndView wxAlipay(HttpServletRequest request, HttpServletResponse response, Model model) {


        return view("/order/alipay", model);

    }

    @ResponseBody
    @RequestMapping(value = "/uc/ajax_check_conpon", method = RequestMethod.GET)
    public CommonResult ajax_check_conpon(HttpServletRequest request, Model model) {
        Map<Long, Integer> map = (Map<Long, Integer>) userSession.getCurrentSession().getAttribute("shoppingItemIdWithCounts");
        List<ShoppingItemDto> items = new ArrayList<>();
        if (map == null || map.isEmpty())
            items = (List<ShoppingItemDto>) userSession.getCurrentSession().getAttribute("shoppingItemDtos");
        else
            items = shoppingCarService.get(new ArrayList<>(map.keySet()));
        String cid = request.getParameter("id");
        String peoductIds = request.getParameter("peoductIds");
        String[] sp = specialConpon.split(",");
        String[] spPids = specialticket.split(",");

        CouponDto couponDto = couponService.getById(Long.parseLong(cid));
        for (String s : sp) {
            if (s.equals(couponDto.getCouponTypeId().toString())) {
                Boolean flag = false;
                for (ShoppingItemDto p : items) {
                    for (String spid : spPids) {
                        if (p.getProductId().toString().equals(spid))
                            return ResultFactory.commonSuccess();
                    }
                }
                if (!flag)
                    return ResultFactory.commonError("该优惠券只能用于夜公园门票");
            }

        }

        BigDecimal countMoney = BigDecimal.valueOf(0) ;
        for(ShoppingItemDto p : items){
            countMoney=countMoney.add(p.getPrice().multiply(BigDecimal.valueOf(p.getCount())));
        }
        if (countMoney.compareTo(couponDto.getUseCondition())>=0) {

            return ResultFactory.commonSuccess();
        } else
            return ResultFactory.commonError("订单金额不满足优惠券使用要求");

    }
}
