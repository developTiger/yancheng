package com.sunesoft.seera.yc.webapp.controller.order;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.QRCodeGenerator;
import com.sunesoft.seera.fr.utils.StringUtils;
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
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import com.sunesoft.seera.yc.core.product.domain.IProductItemRepository;
import com.sunesoft.seera.yc.core.product.domain.IProductRepository;
import com.sunesoft.seera.yc.core.shoppingCar.application.IShoppingCarService;
import com.sunesoft.seera.yc.core.shoppingCar.application.dto.ShoppingItemDto;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.FetcherDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.pingplus.ChargeUtil;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.model.OrderConfirm;
import com.sunesoft.seera.yc.webapp.model.OrderProductModel;
import com.sunesoft.seera.yc.webapp.utils.IP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by zhouz on 2016/6/29.
 */
@Controller
public class OrderController extends Layout {

    @Autowired
    IShoppingCarService shoppingCarService;

    @Autowired
    IProductRepository iProductRepository;
    @Autowired
    IProductService  iProductService;

    @Autowired
    IProductItemRepository iProductItemRepository;

    @Autowired
    ITouristService iTouristService;

    @Autowired
    ICouponService iConponService;

    @Autowired
    UserSession userSession;

    @Autowired
    IOrderService orderService;


    @RequestMapping(value = "createOrder")
    @Transactional
    public String createOrder(OrderConfirm orderConfirm, Model model, HttpServletRequest request) throws Exception {
        // TODO 需要填充页面数据.
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
        if (orderConfirm != null) {
            createOrder.setCouponId(orderConfirm.getCouponId());
            createOrder.setFetcherDtoId(orderConfirm.getFetcherId());
        }
        createOrder.setOrderProducts(createOrderProductLsit);
        createOrder.setTouristId(userSession.getCurrentUserId());
        UniqueResult commonResult = orderService.createOrder(createOrder);

        if (null != map)
            shoppingCarService.removeItems(new ArrayList<>(map.keySet()));
        if (commonResult.getIsSuccess()) {
            return "redirect:/orderPayInfo?sn=" + ((OrderInfo) commonResult.getT()).getNum();
        } else
            return "error";
    }

    @RequestMapping(value = "orderPayInfo")
    public ModelAndView orderPayInfo(Model model, HttpServletRequest request) {
        String ordernum = request.getParameter("sn");
        OrderDto orderDto = orderService.getOrder(ordernum);
        String orderName = "";
        for (OrderProductDto orderProductDto : orderDto.getProductDtos()) {
            if (orderName.equals("")) {
                orderName = orderProductDto.getProductDto().getName();
            } else {
                orderName += "+" + orderProductDto.getProductDto().getName();
            }
        }

        if (orderName.length() > 25) {
            orderName = orderName.substring(0, 25) + "…";
        }
        if (!com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(ordernum)) {
            if (orderDto != null) {
                List<OrderProductDto> orderProductDtos = orderDto.getProductDtos();
//                List<ShoppingItemDto> itemDtos = new ArrayList<>();
//                orderProductDtos.stream().forEach(i -> {
//                    ShoppingItemDto dto = new ShoppingItemDto();
//                    dto.setProductId(i.getId());
//                    dto.setProductName(i.getProductDto().getName());
//                    dto.setCount(i.getCount());
//                    dto.setPrice(i.getProductDto().getPrice());
//
//                    itemDtos.add(dto);
//                });
                //String alipayQr = ChargeUtil.createQr(orderDto.getOrderPrice(), orderDto.getNum(), IP.getClientIpAddr(ServletActionContext.getCurrentRequest()), "alipay_qr");
                String wxPubQr = ChargeUtil.createQr(orderDto.getOrderPrice(), orderName, orderDto.getNum(), IP.getClientIpAddr(ServletActionContext.getCurrentRequest()), "wx_pub_qr");

                //String alipayQrCode = QRCodeGenerator.generalQRCode(alipayQr, 200, 200);
                String wxPubQrCode = QRCodeGenerator.generalQRCode(wxPubQr, 300, 300);

                //  model.addAttribute("alipayQrCode", alipayQrCode);
                model.addAttribute("wxPubQrCode", wxPubQrCode);
                model.addAttribute("orderDto", orderDto);
//                model.addAttribute("orderProductDtos", orderProductDtos);
                userSession.getCurrentSession().setAttribute("orderNum", orderDto.getNum());
                userSession.getCurrentSession().removeAttribute("shoppingItemIdWithCounts");
                userSession.getCurrentSession().removeAttribute("shoppingItems");
                return view(activeLayout, "order/order", model);
            }
        }
        return view("error", model);
    }


    @RequestMapping(value = "orderConfirm")
    public ModelAndView orderConfirm(Model model) throws Exception {
        //获取所有取件人
        List<FetcherDto> fetcherDtoList = iTouristService.getAllFetchers(userSession.getCurrentUserId());
        model.addAttribute("fetcherDtoList", fetcherDtoList);

        Map<Long, Integer> shoppingItemIdWithCounts = (Map<Long, Integer>) userSession.getCurrentSession().getAttribute("shoppingItemIdWithCounts");

        UniqueResult<TouristDto> touristDtoUniqueResult = iTouristService.getTourist(userSession.getCurrentUserId());
        if (touristDtoUniqueResult.getIsSuccess()) {
            //显示默认取票人
            List<FetcherDto> list = touristDtoUniqueResult.getT().getFetcherDtos();
            if (list != null && list.size() > 0) {
                model.addAttribute("defaultFetcher", touristDtoUniqueResult.getT().getDefaultFetcher());
                //显示非默认取票人
                list.remove(touristDtoUniqueResult.getT().getDefaultFetcher());
                model.addAttribute("fetcherDtos", list);

            }
            //显示优惠券
            List<CouponDto> couponDtoList = touristDtoUniqueResult.getT().getBindCouponDtos();
            List<CouponDto> couponDtos = new ArrayList<>();
            if (couponDtoList != null) {
                couponDtoList.stream().forEach(i -> {
                    if (i.getCouponStatus().equals(CouponStatus.bind)) couponDtos.add(i);
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
            model.addAttribute("shoppingItemDtos", shoppingItemDtos);

            List<ProductDto> rejectAreasProducts =new ArrayList<>();
            for(ShoppingItemDto item : shoppingItemDtos ){
                if(iProductService.get(item.getProductId())!=null){
                    String  areas=iProductService.get(item.getProductId()).getRejectAreas();
                    if(!"".equals(areas)||null!=areas){
                        rejectAreasProducts.add(iProductService.get(item.getProductId()));
                    }
                }
            }
            model.addAttribute("rejectAreasProducts", rejectAreasProducts);
        }


        return view(activeLayout, "order/orderConfirm", model);
    }


    @RequestMapping(value = "repay")
    public String rePay(Model model, HttpServletRequest request) throws Exception {
        String num = request.getParameter("sn");
        return "redirect:/orderPayInfo?sn=" + num;
    }

    @ResponseBody
    @RequestMapping(value = "ajax_order_confirm", method = RequestMethod.POST)
    public CommonResult orderConfirmPost(OrderConfirm orderConfirm) {
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

    @RequestMapping("alipay_order_confirm")
    public void alipayConfirm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = request.getParameter("trade_status");
        if ("TRADE_SUCCESS".equals(result)) {
            String tradeNo = request.getParameter("out_trade_no");
//  CommonResult commonResult = orderService.payOrder(tradeNo);
            CommonResult commonResult = orderService.orderPaySuccess(tradeNo,"5");
            if (commonResult.getIsSuccess()) {
                response.sendRedirect("/orderSuccess?order_no=" + tradeNo);
            }
        }
    }

    @RequestMapping("upacp_order_confirm")
    public void UpacpConfirm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String respMsg = request.getParameter("respMsg");
        if ("success".equals(respMsg)) {
            String orderId = request.getParameter("orderId");
            CommonResult commonResult = orderService.payOrder(orderId);
            if (commonResult.getIsSuccess()) {
                response.sendRedirect("/orderSuccess?order_no=" + orderId);
            }
        }
    }

    /**
     * 获取某个购物车商品项详情
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "shoppingItem/{pid}")
    public ModelAndView shoppingItem(@PathVariable Long pid, Model model) {
        if (pid == null) {
            model.addAttribute("error", "输入的ids有误");
            //待在购物车页面
            return view(activeLayout, "order/shoppingCar", model);
        }
        model.addAttribute("bean", shoppingCarService.get(pid));
        //跳转到购物车商品项详情页
        return view(activeLayout, "order/shoppingItem", model);
    }

    @RequestMapping(value = "orderSuccess")
    public ModelAndView orderSuccess(Model model, HttpServletRequest request) {
        String ordernum = request.getParameter("order_no");
        OrderDto dto = orderService.getOrder(ordernum);
        model.addAttribute("amount", dto.getOrderPrice());
        model.addAttribute("orderNum", ordernum);
        return view(activeLayout, "order/orderSuccess", model);
    }

    @RequestMapping(value = "orderPaySuccess")
    public ModelAndView orderPaySuccess(Model model) {

        return view(activeLayout, "order/orderPay", model);
    }

    @RequestMapping(value = "orderPay/{orderId}")
    public ModelAndView orderPay(@PathVariable Long orderId, Model model, HttpServletRequest
            request, HttpServletResponse response) {
        String acount = request.getParameter("acount");
        String password = request.getParameter("password");
        //        CommonResult c=orderService.payOrder(orderId);
        if (false) {
            model.addAttribute("success", "支付成功");
            return view(activeLayout, "order/orderSuccess", model);
        }
        model.addAttribute("error", "支付失败");
        return view(activeLayout, "order/orderPay", model);
    }


    @RequestMapping(value = "order/{type}")
    public ModelAndView orderPay(@PathVariable String type, Model model) {
        //静态测试
        if (type.equalsIgnoreCase("cup"))
            //分别跳转到在线支付
            return view(activeLayout, "order/orderPay", model);
        if (type.equalsIgnoreCase("af"))
            //分别跳转到支付宝支付
            return view(activeLayout, "order/orderPay", model);
        //扫码支付不懂了
        return view(activeLayout, "order/order", model);
    }

    /**
     * 添加修改收件人
     *
     * @param request
     * @param response
     * @param model
     * @param dto
     * @return
     */
    @RequestMapping(value = "order/addReceiver")
    @ResponseBody
    public CommonResult addReceiver(HttpServletRequest request, HttpServletResponse response, Model
            model, FetcherDto dto) {
        if (dto.getId() == null) {
            return iTouristService.create(userSession.getCurrentUserId(), dto);
        } else {
            return iTouristService.update(userSession.getCurrentUserId(), dto);
        }
    }

    /**
     * 删除收件人
     *
     * @param request
     * @param response
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "order/deleteReceiver")
    @ResponseBody
    public CommonResult deleteReceiver(HttpServletRequest request, HttpServletResponse response, Model
            model, Long[] id) {
        List<Long> list = Arrays.asList(id);
        return iTouristService.remove(userSession.getCurrentUserId(), list);
    }

    /**
     * 绑定优惠券
     *
     * @param request
     * @param response
     * @param model
     * @param num
     * @return
     */
    @RequestMapping(value = "order/checkCoupon")
    @ResponseBody
    public CommonResult checkCoupon(HttpServletRequest request, HttpServletResponse response, Model model, String
            num) {
        return iTouristService.bindCoupon(userSession.getCurrentUserId(), num);
    }

    /**
     * 获取单条优惠券信息
     *
     * @param request
     * @param num
     * @return
     */
    @RequestMapping(value = "order/getCoupon")
    @ResponseBody
    public CouponDto getCoupon(HttpServletRequest request, String num) {
        CouponDto couponDto = iTouristService.getByNum(userSession.getCurrentUserId(), num);
        return couponDto;
    }


    @RequestMapping(value = "order/mealRedo")
    @ResponseBody
    public CommonResult mealRedo(HttpServletRequest request, HttpServletResponse response) {

        String zybCode = request.getParameter("zybCode");

        CommonResult result = orderService.ticketZybMeal(zybCode);


        return result;
    }


    @RequestMapping(value = "order/getImg")
    @ResponseBody
    public CommonResult getImg(HttpServletRequest request, HttpServletResponse response) {
        String orderNum = request.getParameter("orderNum");
        String productId = request.getParameter("productId");
        CommonResult result = orderService.reGetZybImg(orderNum, Long.parseLong(productId));
        return result;
    }


    @RequestMapping(value = "order/productReturn")
    @ResponseBody
    public CommonResult productReturn(HttpServletRequest request, HttpServletResponse response) {

        String orderNum = request.getParameter("orderNum");
        String productIds = request.getParameter("productIds");

        List<Long> idList = new ArrayList<>();
        String[] ids = productIds.split(",");
        for (String id : ids) {
            idList.add(Long.parseLong(id));
        }
        CommonResult result = orderService.ticketChargeBack(orderNum, idList);
        return result;
    }

    @RequestMapping(value = "order/cancelOrder")
    @ResponseBody
    public CommonResult cancelOrder(HttpServletRequest request, HttpServletResponse response) {

        String orderId = request.getParameter("orderId");
        if (!StringUtils.isNullOrWhiteSpace(orderId)) {
            CommonResult result = orderService.cancelOrder(Long.parseLong(orderId));
            return result;
        }
        return ResultFactory.commonError("订单异常取消失败");
    }


}
