package com.sunesoft.seera.yc.core.order.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.loggers.Logger;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.*;
import com.sunesoft.seera.yc.Client;
import com.sunesoft.seera.yc.core.coupon.domain.Coupon;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;
import com.sunesoft.seera.yc.core.coupon.domain.ICouponRepository;
import com.sunesoft.seera.yc.core.lottery.domain.Subject;
import com.sunesoft.seera.yc.core.lottery.domain.SubjectRepository;
import com.sunesoft.seera.yc.core.order.application.criteria.CreateOrder;
import com.sunesoft.seera.yc.core.order.application.criteria.OrderCriteria;
import com.sunesoft.seera.yc.core.order.application.criteria.OrderProductItemCriteria;
import com.sunesoft.seera.yc.core.order.application.dtos.*;
import com.sunesoft.seera.yc.core.order.application.factory.OrderFactory;
import com.sunesoft.seera.yc.core.order.application.factory.OrderItemFactory;
import com.sunesoft.seera.yc.core.order.application.factory.OrderProductFactory;
import com.sunesoft.seera.yc.core.order.application.factory.ProductHistoryFactory;
import com.sunesoft.seera.yc.core.order.domain.*;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.domain.*;
import com.sunesoft.seera.yc.core.tourist.domain.Fetcher;
import com.sunesoft.seera.yc.core.tourist.domain.ITouristRepository;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;
import com.sunesoft.seera.yc.jaxb.JaxbBase;
import com.sunesoft.seera.yc.pwb.*;
import com.sunesoft.seera.yc.pwb.order.Order;
import com.sunesoft.seera.yc.pwb.order.TicketOrder;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.time.DateUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.ConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by zhaowy on 2016/7/11.
 */
@Service("iOrderService")
public class OrderServiceImpl extends GenericHibernateFinder implements IOrderService {

    @Autowired
    IOrderRepository orderRepository;

    @Autowired
    IOrderItemRepository orderItemRepository;

    @Autowired
    IProductRepository productRepository;

    @Autowired
    IProductItemRepository productItemRepository;

    @Autowired
    IProductHistoryRepository productHistoryRepository;

    @Autowired
    IOrderProductRepository orderProductRepository;

    @Autowired
    ITouristRepository touristRepository;

    @Autowired
    ICouponRepository couponRepository;

    @Autowired
    SubjectRepository repository;


    @Autowired
    Logger logger;
    @Autowired
    IProductService iProductService;

    //region Order public method

    /**
     * 下订单
     *
     * @return 下单是否成功
     * <p>成功下单后20分钟未支付，订单作废，自动取消</p>
     * @ CreateOrder order
     * <p>
     * 下订单时必要信息
     * param1：下单人标识
     * param2: 商品标识对应数量
     * param3：使用优惠券
     * param4: 取件人信息标识
     * </p>
     */
    @Override
    public UniqueResult<OrderInfo> createOrder(CreateOrder order) {
        //...................edit by rock 2016年8月15日15:31:36.......................................................
        //数据校验
        CommonResult c = orderCheck(order);
        if (!c.getIsSuccess())
            //数据检查不通过，就是返回信息提示
            return new UniqueResult<OrderInfo>(c.getMsg());
        OrderInfo orderInfo = new OrderInfo();

        //获取游客信息
        Tourist tourist = new Tourist();
        tourist.setId(order.getTouristId());
        orderInfo.setTourist(tourist);
        Criteria criterion = getSession().createCriteria(Fetcher.class);
        criterion.add(Restrictions.eq("id", order.getFetcherDtoId()));
        List<Fetcher> fetchers = criterion.list();
        if (fetchers != null && fetchers.size() > 0) {
            //获取取票人信息
//        Fetcher fetcher = new Fetcher();
//        fetcher.setId(order.getFetcherDtoId());
            orderInfo.setFetcher(fetchers.get(0));
        }

        //获取商品信息
        try {
            List<OrderProduct> orderProducts = new ArrayList<>();
            order.getOrderProducts().stream().forEach(i ->
            {
                OrderProduct orderProduct = new OrderProduct();
                Product product = productRepository.get(i.getProductId());
                product.reduceProductStock(i.getCount());
                productRepository.save(product);
                List<ProductItem> productItems = product.getProductItemList();
                Integer productCount = i.getCount();

                Date hotelScheduleDate = i.getHotelScheduleDate();
                Date tourScheduleDate = i.getTourScheduleDate();
                if (null != hotelScheduleDate)
                    orderProduct.setHotelScheduleDate(hotelScheduleDate);
                if (null != tourScheduleDate)
                    orderProduct.setTourScheduleDate(tourScheduleDate);
                orderProduct.setCanMeal(product.getCanMeal());
                orderProduct.setCanReturn(product.getCanReturn());
                orderProduct.setCount(productCount);
                //判断最新的商品版本
                ProductHistory latestHistory = loadLatestProductHistory(product);
                orderProduct.setProduct(latestHistory);

                List<OrderProductItem> orderItems = new ArrayList<>();
                productItems.stream().forEach(j -> {
                    OrderProductItem item = new OrderProductItem();
                    item.setInnerManageId(j.getInnerManageId());
                    item.setName(j.getName());
                    item.setNum(j.getNum());
                    item.setProduct(orderProduct);
                    item.setCount(product.getProductItemCountById(j.getId()));
                    item.setOriginalId(j.getId());
                    item.setPrice(j.getPrice());
                    item.setType(j.getProductItemType());
                    item.setScheduleDate(j.getProductItemType().equals(ProductItemType.Hotel)
                            ? i.getHotelScheduleDate() : i.getTourScheduleDate());
                    orderItems.add(item);
                });
                orderProduct.setItems(orderItems);
//                orderProductRepository.save(orderProduct);

                //change to OrderProduct Set
                orderProducts.add(orderProduct);
            });
            orderInfo.setProduct(orderProducts);
            final BigDecimal[] totalPrice = {BigDecimal.ZERO};
            orderProducts.stream().map(k -> k.getTotalPrice()).forEach(l -> totalPrice[0] = totalPrice[0].add(l));

            orderInfo.setOrderPrice(totalPrice[0]);
            if (order.getCouponId() != null) {
                UniqueResult<Coupon> result = useCoupon(orderInfo, order.getCouponId());
                if (orderInfo.getOrderPrice().compareTo(result.getT().getUseCondition()) == -1) {
                    return ResultFactory.ErrorUniqueRollback(c.getMsg());
                }
                if (result.getIsSuccess()) {
                    Coupon coupon = result.getT();
                    coupon.setCouponStatus(CouponStatus.Used);
                    orderInfo.setCoupon(coupon);

                    orderInfo.setOrderPrice(orderInfo.getOrderPrice().subtract(coupon.getQuota()));
                } else {
                    return ResultFactory.ErrorUniqueRollback("当前价格不允许使用改优惠券");
                }
            }

            orderInfo.setRemark(order.getRemark());

            orderRepository.save(orderInfo);
//            orderRepository.save(orderInfo);
            orderInfo.setNum("B" + DateHelper.getYear(new Date()) + orderInfo.getId().toString());
            orderRepository.save(orderInfo);
            return new UniqueResult<OrderInfo>(orderInfo);
        } catch (Exception ex) {
            return ResultFactory.ErrorUniqueRollback(ex.getMessage());
        }
    }

    /**
     * 订单商品项使用
     *
     * @param orderProductItemId 订单商品项标识
     * @return 是否成功
     * <p>适用于游客订单商品二维码扫描核销处理</p>
     */
    @Override
    public CommonResult productItemTake(Long orderProductItemId) {
        try {
            OrderProductItem item = orderItemRepository.get(orderProductItemId);
            if (null == item) return ResultFactory.commonError("订单商品项不存在");
            //商品领取Check
            if (item.getHaveTaked())
                return ResultFactory.commonError("此商品已领取！");
            //有效性check
            Date today = new Date();
            if (item.getType().equals(ProductItemType.Hotel))
                if (item.getProduct().getHotelScheduleMealDate() != null
                        && item.getProduct().getStatus().equals(OrderProductStatus.mealed)) {
                    if (!DateUtils.isSameDay(item.getProduct().getHotelScheduleMealDate(), today))
                        return ResultFactory.commonError("未到预定的入住日期或已过期！");
                } else {
                    if (!DateUtils.isSameDay(item.getProduct().getHotelScheduleDate(), today))
                        return ResultFactory.commonError("未到预定的入住日期或已过期！");
                }
            else {
                if (item.getProduct().getTourScheduleMealDate() != null
                        && item.getProduct().getStatus().equals(OrderProductStatus.mealed)) {
                    if (!DateUtils.isSameDay(item.getProduct().getTourScheduleMealDate(), today))
                        return ResultFactory.commonError("未到预定的领取日期或已过期！");
                } else {
                    if (!DateUtils.isSameDay(item.getProduct().getTourScheduleDate(), today))
                        return ResultFactory.commonError("未到预定的领取日期或已过期！");
                }
            }

            item.setHaveTaked(true);
            item.setTakeDate(today);
            return ResultFactory.commonSuccess(orderItemRepository.save(item));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 订单商品项使用
     *
     * @param takeNum 订单商品项提取码
     * @return 是否成功
     * <p>适用于游客订单商品二维码扫描核销处理</p>
     */
    @Override
    public CommonResult productItemTake(String takeNum) {
        try {
            String[] nums = takeNum.split("C");
            OrderInfo intfo = orderRepository.get(nums[0]);
            for (OrderProduct product : intfo.getProducts()) {
                for (OrderProductItem item : product.getItems()) {
                    if (item.getId().toString().equals(nums[1])) {
                        //  OrderProductItem item = orderItemRepository.get(takeNum);
                        if (null == item) return ResultFactory.commonError("订单商品项不存在");
                        if (item.getItemStatus() == ItemStatus.checked) {
                            //领取check

                            return ResultFactory.commonError("此商品已领取！");
                        }
                        if (item.getHaveTaked())
                            return ResultFactory.commonError("此商品已领取！");


                        //有效性check
                        Date today = new Date();
                        if (item.getType().equals(ProductItemType.Hotel))
                            if (item.getProduct().getHotelScheduleMealDate() != null
                                    && item.getProduct().getStatus().equals(OrderProductStatus.mealed)) {
                                if (!DateUtils.isSameDay(item.getProduct().getHotelScheduleMealDate(), today))
                                    return ResultFactory.commonError("未到预定的入住日期或已过期！");
                            } else {
                                if (!DateUtils.isSameDay(item.getProduct().getHotelScheduleDate(), today))
                                    return ResultFactory.commonError("未到预定的入住日期或已过期！");
                            }
                        else {
                            if (item.getProduct().getTourScheduleMealDate() != null
                                    && item.getProduct().getStatus().equals(OrderProductStatus.mealed)) {
                                if (!DateUtils.isSameDay(item.getProduct().getTourScheduleMealDate(), today))
                                    return ResultFactory.commonError("未到预定的领取日期或已过期！");
                            } else {
                                if (!DateUtils.isSameDay(item.getProduct().getTourScheduleDate(), today))
                                    return ResultFactory.commonError("未到预定的领取日期或已过期！");
                            }
                        }

                        item.setHaveTaked(true);
                        item.setTakeDate(today);
                        item.setItemStatus(ItemStatus.checked);
                        intfo.synacOrderOperatorStatus();
                        return ResultFactory.commonSuccess(orderRepository.save(intfo));
                    }
                }
            }
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
        return ResultFactory.commonError("领取码错误！领取失败！");
    }


    /**
     * 订单付款
     *
     * @param orderId 订单标识
     * @return 更新成功
     * <p>确保在订单支付成功后更新订单状态为已支付</p>
     */
    @Override
    public CommonResult payOrder(Long orderId) {
        OrderInfo info = orderRepository.get(orderId);
        if (info != null && info.getStatus() == OrderStatus.waitPay) {
            //智游宝下单
            // if (zhiyoubao(info).getIsSuccess()) {
            info.setStatus(OrderStatus.payCheck);
            return ResultFactory.commonSuccess(orderRepository.save(info));
            //}
        }
        return ResultFactory.commonError("订单异常！");
    }


    /**
     * 智游宝下单逻辑
     *
     * @param info
     * @return
     */
    private CommonResult zhiyoubao(OrderInfo info) {

        final StringBuilder errorInfo = new StringBuilder("");
        logger.info("zhiyoubao:" + info.getNum() + ",begin");
        info.getProducts().stream().forEach(p ->
        {
            Boolean isOk = false;
            for (int i = 0; i < 3; i++) {
                isOk = createTicketOrder(info.getNum(), info.getFetcher(), p);
                if (isOk)
                    break;
                else if (i == 0) errorInfo.append("{智游宝下单错误:" + "订单号：" + info.getNum() + ",订单商品id" + p.getId() + "},");
            }

        });
        if (StringUtils.isNullOrWhiteSpace(errorInfo.toString())) {
            info.setOrderOperatorStatus(OrderOperatorStatus.Success);
            logger.info("zhiyoubao:" + info.getNum() + ",end");
            return ResultFactory.commonSuccess();

        } else {
            info.setOrderOperatorStatus(OrderOperatorStatus.ZybError);
            info.setOperatorRemark(errorInfo.toString());
            logger.error(errorInfo.toString());
            logger.info("zhiyoubao:" + info.getNum() + ",end");
            return ResultFactory.commonError(errorInfo.toString());
        }
    }

    /**
     * 智游宝订单商品下单
     *
     * @param orderNum
     * @param fetcher
     * @param product
     * @return
     */
    private Boolean createTicketOrder(String orderNum, Fetcher fetcher, OrderProduct product) {
        Client client = new PwbClientImpl();

        StringBuilder stringBuilder = new StringBuilder("");
        product.getItems().stream().filter(x -> x.getType() == ProductItemType.Ticket).forEach(e -> {
            if (e.ticketCanCreate()) {
                e.setTakeNum(orderNum + "C" + e.getId());
                OrderRequest orderRequest = new OrderRequest();
                OrderResponse orderResponse;
                Order order = new Order();
                order.setCertificateNo(fetcher.getIdCardNo());
                order.setLinkName(fetcher.getRealName());
                order.setLinkMobile(fetcher.getMobilePhone());
                order.setOrderCode(e.getTakeNum());
                order.setOrderPrice(e.getPrice().multiply(BigDecimal.valueOf(e.getCount() * product.getCount())).toString());
                order.setPayMethod("vm");
                TicketOrder ticketOrder = new TicketOrder();
                ticketOrder.setOrderCode(e.getTakeNum());
                ticketOrder.setPrice(e.getPrice().toString());
                ticketOrder.setQuantity(e.getCount() * product.getCount());
                ticketOrder.setTotalPrice(e.getPrice().multiply(BigDecimal.valueOf(ticketOrder.getQuantity())).toString());
                ticketOrder.setOccDate(JaxbBase.SIMPLE_DATE_FORMAT.format(e.getScheduleDate()));
                ticketOrder.setGoodsCode(e.getNum());
                ticketOrder.setGoodsName(e.getName());
                order.setTicketOrder(ticketOrder);
                orderRequest.setOrder(order);
                try {
                    orderResponse = client.execute(orderRequest);
                    if (orderResponse.getCode() == 0) {
                        e.updateItemStatus(ItemStatus.created, "");
                    } else {
                        String msg = "智游宝下单返回失败 orderNum:" + orderNum
                                + ",orderProductId:" + product.getId() + ",error_code:" + orderResponse.getCode()
                                + ",description:" + orderResponse.getDescription();
                        logger.info(msg);
                        e.updateItemStatus(ItemStatus.createError, "__error;" + orderResponse.getDescription());
//                        e.setItemStatus(ItemStatus.createError);
//                        e.setItemStatusRemark( "__error;" + orderResponse.getDescription());
                        stringBuilder.append(product.getProduct().getName() + "__error;" + orderResponse.getDescription());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    stringBuilder.append(product.getProduct().getName() + "__error;" + e1.getMessage());
                }
            }
        });
        if (!StringUtils.isNullOrWhiteSpace(stringBuilder.toString())) {
            product.setOrderOperatorStatus(OrderOperatorStatus.ZybError);
            product.setOperatorRemark(stringBuilder.toString());
        } else {
            product.setOrderOperatorStatus(OrderOperatorStatus.Success);
            product.setOperatorRemark("");
        }


        return StringUtils.isNullOrWhiteSpace(stringBuilder.toString());
    }


    /**
     * 智游宝订单商品项二维码获取
     *
     * @param orderNum
     * @return
     */
    private String getTicketQrCode(String orderNum) {

        logger.info("zhiyoubao 二维码获取:" + orderNum + ",begin");
        Client client = new PwbClientImpl();
        ImgRequest imgRequest = new ImgRequest();
        Order order = new Order();
        order.setOrderCode(orderNum);//orderInfo.getNum()+ item.getId()
        imgRequest.setOrder(order);
        ImgResponse imgResponse = null;
        try {
            imgResponse = client.execute(imgRequest);
            if (imgResponse.getCode() != 0) {
                logger.error("二维码获取返回失败：orderNum" + orderNum
                        + ",code:" + imgResponse.getCode() + ",description:" + imgResponse.getDescription());

                return "500";
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "二维码获取异常 orderNum:" + orderNum;
            logger.error(msg, e);
            logger.info("zhiyoubao 二维码获取:" + orderNum + ",end");
            return "500";
        }
        logger.info("zhiyoubao 二维码获取:" + orderNum + ",end");
        return imgResponse.getImg();
    }

    /**
     * 订单付款
     *
     * @param orderNum 订单号.
     * @return 更新成功
     * <p>确保在订单支付成功后更新订单状态为已支付</p>
     */
    public CommonResult payOrder(String orderNum) {
        OrderInfo info = orderRepository.get(orderNum);
//
        if (info != null && info.getStatus() == OrderStatus.waitPay) {
//            //想获取第三方支付成功的返回值，否则支付失败
//            //TODO 第三方支付返回成功
//

            //智游宝下单
            //if (zhiyoubao(info).getIsSuccess()) {
            info.setStatus(OrderStatus.payCheck);
            return ResultFactory.commonSuccess(orderRepository.save(info));
//            }
        }
        if (info != null && info.getStatus() == OrderStatus.payed)
            return ResultFactory.commonSuccess(info.getId());
        return ResultFactory.commonError("订单异常！");

    }

    /**
     * 订单成功支付通知
     *
     * @param orderNum 订单编号
     * @return 已支付成功后续事宜处理结果
     * <p>后续事宜包括订单状态修改。取票二维码生成
     * 生成取票二维码响应地址，至少应包含：orderNum orderProductItemId
     * </p>
     */
    @Override
    public CommonResult orderPaySuccess(String orderNum, String payType) throws Exception {
        OrderInfo info = orderRepository.get(orderNum);
        if (null == info) throw new NullArgumentException("orderNum");
        //TODO 订单状态在接到hook后直接改为payed
        info.setStatus(OrderStatus.payed);

        if ("1".equals(payType)) {
            info.setPayTypes("储值卡");
        } else if ("2".equals(payType)) {
            info.setPayTypes("现金");
        } else if ("3".equals(payType)) {
            info.setPayTypes("银行卡");
        } else if ("4".equals(payType)) {
            info.setPayTypes("微信");
        } else if ("5".equals(payType)) {
            info.setPayTypes("支付宝");
        } else if ("6".equals(payType)) {
            info.setPayTypes("优惠券");
        } else if ("7".equals(payType)) {
            info.setPayTypes("打白条");
        } else if ("8".equals(payType)) {
            info.setPayTypes("多方式付款");
        } else if ("9".equals(payType)) {
            info.setPayTypes("微信个人");
        } else if ("10".equals(payType)) {
            info.setPayTypes("支付宝（个人）");
        } else if ("0".equals(payType)) {
            info.setPayTypes("活动订单");
        } else {
            info.setPayTypes("其他");
        }

        try {
            //TODO 智游宝下单
            if (!zhiyoubao(info).getIsSuccess())
                info.setOrderOperatorStatus(OrderOperatorStatus.ZybError);
            else
                info.setOrderOperatorStatus(OrderOperatorStatus.Success);
//=================华丽分割线==============================================
            //TODO  设置取货二维码，更新订单状态
            info.getProducts().stream().forEach(p ->
            {
                //
                StringBuilder builder = new StringBuilder("");
                p.getItems().stream().forEach(item ->
                {

                    item.setTakeNum(orderNum + "C" + item.getId());//订单商品项唯一编码，做订单明细编号、取货编码
                    String qrCode = "";
                    Boolean isGjpOk = true;
                    if (item.getType().equals(ProductItemType.Ticket)) {

                        if (item.getItemStatus() == ItemStatus.created || item.getItemStatus() == ItemStatus.reCreated) {
                            String code = getTicketQrCode(item.getTakeNum());
                            // 重试三次，硬编码了
                            for (int i = 0; i < 2; i++) {
                                if (!code.equals("500")) {
                                    qrCode = code;
                                    break;
                                } else code = getTicketQrCode(item.getTakeNum());
                            }
                        }
                    } else {
                        // 生成每类商品项的取货二维码
                        //actionUrl模板 "http://admin3.cn-yc.com/itemTake?takeNum=@";
                        try {
//
//                            String actionUrl = Configs.getProperty("itemTakeUrl");
//                            actionUrl = actionUrl.replace("@", item.getTakeNum());
//                            if (StringUtils.isNullOrWhiteSpace(actionUrl))
//                                throw new ConfigurationException("itemTakeUrl");

//                            actionUrl = URLEncoder.encode(actionUrl, "utf-8");
                            qrCode = QRCodeGenerator.encode(item.getTakeNum(), 300, 100, null);
//                            if(!gjpCreateOrder(item.getNum(),orderNum,p.getCount()*item.getCount()).getIsSuccess()){
//                                isGjpOk=false;
//                            }

                        } catch (Exception e) {
                            item.updateItemStatus(ItemStatus.imgError, item.getName() + "：生成二维码错误;");
                            builder.append(item.getName() + "：生成二维码错误;");
                            e.printStackTrace();
                        }
                    }
                    if (!StringUtils.isNullOrWhiteSpace(qrCode)) {
                        qrCode = "data:image/jpg;base64," + qrCode;
                        item.setQrCode(qrCode);
//                        if(isGjpOk) {
                        item.updateItemStatus(ItemStatus.allSuccess, "");
//                        }else{
//                            item.updateItemStatus(ItemStatus.gjpError,"");
//                        }

                    } else {
                        if (item.getType().equals(ProductItemType.Ticket)) {
                            if (item.getItemStatus() == ItemStatus.created || item.getItemStatus() == ItemStatus.reCreated) {
                                item.updateItemStatus(ItemStatus.imgError, item.getName() + "：生成二维码错误;");
                                builder.append(item.getName() + "：生成二维码错误;");
                            }
                        } else {
                            item.updateItemStatus(ItemStatus.imgError, item.getName() + "：生成二维码错误;");
                            builder.append(item.getName() + "：生成二维码错误;");
                        }

                    }
                    //orderItemRepository.save(item);
                });

            });
        } catch (Exception ex) {
            logger.error("zhiyoubao:运行结束，但部分单据异常：", ex);
            //  info.setOrderOperatorStatus(OrderOperatorStatus.PayError);
        }


        info.synacOrderOperatorStatus();

        info.setStatus(OrderStatus.payed);
        Long id = orderRepository.save(info);
        if (info.getOrderOperatorStatus() == OrderOperatorStatus.Success) {
            return ResultFactory.commonSuccess(id);
        } else {
            return ResultFactory.commonError("门票创建异常，请点击查看详情！");
        }
    }


    public CommonResult gjpCreateOrder(String code, String orderNo, Integer count) {

        String connectGjp = Configs.getProperty("connectGjp");
        if (connectGjp.equals("yes")) {
            String actionUrl = Configs.getProperty("gjpUrl");
            String privateKey = Configs.getProperty("gjpPrivateKey");

            String stridp = "zs001" + "abc123" + "5678";

            String sign = MD5.GetMD5Code(stridp);
            String targetURL = actionUrl + "?userid=zs001&pwd=5678&sign=" + sign.toUpperCase() + "&code=" + code + "&qty=" + count + "&billcode=" + orderNo;
            try {
                URL restServiceURL = new URL(targetURL);
                HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.setRequestProperty("Accept", "application/json");
                if (httpConnection.getResponseCode() != 200) {
                    throw new RuntimeException("HTTP GET Request Failed with Error code : "
                            + httpConnection.getResponseCode());
                }
                BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                        (httpConnection.getInputStream())));
                String output = responseBuffer.readLine();
                System.out.println("Output from Server:  \n" + output);
                httpConnection.disconnect();
                if (output.equals("1"))
                    return ResultFactory.commonSuccess();
                else {
                    return ResultFactory.commonError("error");
                }
//
//                while ((output = responseBuffer.readLine()) != null) {
//                    System.out.println(output);
//                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResultFactory.commonError(e.getMessage());

            }

        }
        return ResultFactory.commonSuccess();
    }

    /**
     * 领取夜公园门票
     *
     * @param createOrder
     * @return
     */
    @Override
    public CommonResult LightNightPark(CreateOrder createOrder, Long typeId) {
        Tourist tourist = touristRepository.get(createOrder.getTouristId());

        ///todo 加判断
        createOrder.setFetcherDtoId(90L);
        UniqueResult<OrderInfo> orderInfo = this.createOrder(createOrder);
        if (orderInfo.getIsSuccess()) {
            try {
                CommonResult commonResult = this.orderPaySuccess(orderInfo.getT().getNum(), "0");
                Subject subject = repository.get(typeId);
                subject.setReceive(false);
                repository.save(subject);
                return commonResult;
            } catch (Exception e) {
                e.printStackTrace();
                this.cancelOrder(orderInfo.getT().getId());
                return new CommonResult(false, "门票领取失败");
            }
        } else {
            return ResultFactory.ErrorCommonRollbackLight("门票领取失败");
        }

    }

    /**
     * 取消订单
     *
     * @param orderId 订单标识
     * @return 取消成功
     * <p>取消订单，更新订单状态，同时返还商品库存数量</p>
     */
    @Override
    public CommonResult cancelOrder(Long orderId) {
        //订单生成为付款是可调用，设置订单状态为取消
        OrderInfo info = orderRepository.get(orderId);
        if (info == null || info.getStatus() != OrderStatus.waitPay) {
            return ResultFactory.commonError("该订单不存在，或者订单已经完成");
        }
        info.setStatus(OrderStatus.canceled);
        Coupon coupon = info.getCoupon();
        if (null != coupon)
            returnCoupon(coupon.getId(), info.getTourist().getId(), null);
        orderRepository.save(info);
        List<OrderProduct> list = info.getProducts();
        if (list == null || list.size() < 1) {
            return ResultFactory.commonError("该订单暂时没有商品");
        }
        list.stream().forEach(i -> {
            Product product = productRepository.get(i.getProduct().getOriginalId());
            product.increaseProductStock(i.getCount());
            productRepository.save(product);
        });


        return ResultFactory.commonSuccess();
    }

    /**
     * 获取指定订单详情
     *
     * @param orderId 订单标识
     * @return 订单实体
     */
    @Override
    public OrderDto getOrder(Long orderId) {

        OrderInfo order = orderRepository.get(orderId);
        if (order == null) return null;
        OrderDto  dto=OrderFactory.convert(order);
        dto.getProductDtos().stream().forEach(i->{
            if(!StringUtils.isNullOrWhiteSpace(i.getProductDto().getRejectAreas()))
                i.getProductDto().setRejectAreasNames(iProductService.editRejectAreasNames(i.getProductDto().getRejectAreas()));
        });
        return dto;
    }

//    /**
//     * 删除订单记录
//     *
//     * @param orderIds
//     * @return
//     */
//    public CommonResult deleteOrder(List<Long> orderIds) {
//        Criteria criteria = getSession().createCriteria(OrderInfo.class);
//        criteria.add(Restrictions.eq("isActive", true));
//        criteria.add(Restrictions.in("ids", orderIds));
//        List<OrderInfo> list = criteria.list();
//        if (list == null) return new CommonResult(false, "所选的订单都不存在");
//        list.stream().forEach(i -> {
//            i.setIsActive(false);
//            orderRepository.save(i);
//        });
//        return new CommonResult(true);
//    }

    /**
     * 获取指定订单详情
     *
     * @param orderNum 订单编码
     * @return 订单实体
     */
    @Override
    public OrderDto getOrder(String orderNum) {

        OrderInfo order = orderRepository.get(orderNum);
        if (order == null) return null;
        return OrderFactory.convert(order);
    }

//    /**
//     * 获取指定订单商品项
//     *
//     * @param orderNum 订单编号
//     * @return 订单商品项实体
//     */
//    @Override
//    public List<OrderProductItemDto> getOrderItems(String orderNum) {
//        OrderInfo order = orderRepository.get(orderNum);
//        return OrderItemFactory.convert(order.getProductItems());
//    }

    /**
     * 获取指定订单商品项
     *
     * @param takeNum 订单商品项取货码
     * @return 订单商品项实体
     */
    @Override
    public OrderProductItemDto getOrderItem(String takeNum) {
        OrderProductItem item = orderItemRepository.get(takeNum);
        if (item != null)
            return OrderItemFactory.convert(item);
        else
            return null;
    }

    /**
     * @param criteria 查询条件
     * @return 订单分页集合
     */
    @Override
    public PagedResult<OrderDto> getOrders(OrderCriteria criteria) {
        int pageSize=criteria.getPageSize();
        criteria.setPageSize(Integer.MAX_VALUE);

        Criteria criterion = getSession().createCriteria(OrderInfo.class);
        criterion.add(Restrictions.eq("isActive", true));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getNum())) {
            criterion.add(Restrictions.like("num", "%" + criteria.getNum() + "%"));
        }
        criterion.createAlias("tourist", "tourist");
        if (!StringUtils.isNullOrWhiteSpace(criteria.getTouristRealName())) {
            criterion.add(Restrictions.like("tourist.realName", "%" + criteria.getTouristRealName() + "%"));
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getTouristUserName())) {
            criterion.add(Restrictions.like("tourist.userName", "%" + criteria.getTouristUserName() + "%"));
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getTouristWxName())) {
            criterion.add(Restrictions.like("tourist.wxName", "%" + criteria.getTouristWxName() + "%"));
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getTouristId())) {
            criterion.add(Restrictions.eq("tourist.id", Long.parseLong(criteria.getTouristId())));
        }


        //TODO 需要级联productHistory的时候加上 criterion.createAlias("products", "products");
        if (!StringUtils.isNullOrWhiteSpace(criteria.getProductName()) || !StringUtils.isNullOrWhiteSpace(criteria.getRejectArea())) {
            criterion.createAlias("products", "products");
            criterion.createAlias("products.product", "product");
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getProductName())) {
            criterion.add(Restrictions.like("product.name", "%" + criteria.getProductName() + "%"));
        }

        if (!StringUtils.isNullOrWhiteSpace(criteria.getRejectArea())) {//不限定限定区域的情况是为null
            if ("-1".equals(criteria.getRejectArea())) {
                //这种情况是:
                // 不限定区域的商品
//                criterion.add(Restrictions.or(Restrictions.isNull("product.rejectAreas"), Restrictions.eq("product.rejectAreas", "")));
                criterion.add(Restrictions.isNull("product.rejectAreas"));
            } else if ("0".equals(criteria.getRejectArea())) {
                //限定区域的情况
                criterion.add(Restrictions.and(Restrictions.isNotNull("product.rejectAreas"), Restrictions.ne("product.rejectAreas", "")));
            } else {
                //限定某个限定区域的情况
                criterion.add(Restrictions.like("product.rejectAreas", "%" + criteria.getRejectArea() + "%"));
            }
        }


        if (criteria.getStatus() != null) {
            criterion.add(Restrictions.eq("status", criteria.getStatus()));
        }
        if (criteria.getFromOrderTime() != null) {
            criterion.add(Restrictions.ge("orderTime", criteria.getFromOrderTime()));
        }
        if (criteria.getEndOrderTime() != null) {
            criterion.add(Restrictions.le("orderTime", criteria.getEndOrderTime()));
        }

//        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criterion.addOrder(
                criteria.isAscOrDesc() ?
                        org.hibernate.criterion.Order.asc(criteria.getOrderByProperty() == null ? "orderTime" : criteria.getOrderByProperty())
                        : org.hibernate.criterion.Order.desc(criteria.getOrderByProperty() == null ? "orderTime" : criteria.getOrderByProperty()));
//        criterion.setFirstResult(
//                (criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());

        List<OrderDto> dtos = OrderFactory.convert(criterion.list());
        List<OrderDto> list=new ArrayList<>();
        int total=dtos==null?0:dtos.size();
        if(dtos!=null&&dtos.size()>0) {
           int min=criteria.getPageNumber() * pageSize > total?total:criteria.getPageNumber() * pageSize;
            for(int i=(criteria.getPageNumber()-1)*pageSize;i<min;i++){
                list.add(dtos.get(i));
            }
        }
//该三级表查询的时候，分页有问题
        return new PagedResult<>(list, criteria.getPageNumber(), pageSize, total);
    }

    /**
     * @param criteria 查询条件
     * @return 订单商品集合
     */
    @Override
    public PagedResult<OrderProductItemDto> getOrderProductItems(OrderProductItemCriteria criteria) {
        String hql1 = "select o.num as orderNum,o.orderTime as scheduleDate,i.name as name,i.num as num,i.type as type,o.id as orderId" +
                ",i.price as price,i.count as count,i.ItemStatus as ItemStatus,i.ItemStatusRemark as ItemStatusRemark,i.takeDate as takeDate  from OrderInfo o join o.products p join p.items i";
        String countHql = "select  count(i)  from OrderInfo o join o.tourist t join o.products p join p.items i";
        String whereCondition = " where o.isActive=1";
        String orders = " order by i.scheduleDate desc";
        whereCondition += " and o.status='2'";
        if (criteria.getProduct_status() != null) {
            if ("1".equals(criteria.getProduct_status())) {
                whereCondition += " and i.ItemStatus in (7)";
            } else if ("2".equals(criteria.getProduct_status())) {
                whereCondition += " and i.ItemStatus in (1,4,6,8,9)";
            } else if ("3".equals(criteria.getProduct_status())) {
                whereCondition += " and i.ItemStatus in (2)";
            } else if ("4".equals(criteria.getProduct_status())) {
                whereCondition += " and i.ItemStatus in (3,5,10)";
            } else {

            }
        }

        if (!StringUtils.isNullOrWhiteSpace(criteria.getProductItemName())) {
            whereCondition += " and i.name='" + criteria.getProductItemName() + "'";
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getOrderNum())) {
            whereCondition += " and o.num='" + criteria.getOrderNum() + "'";
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getTouristUserName())) {
            whereCondition += " and t.userName='" + criteria.getTouristUserName() + "'";
        }

        if (criteria.getFromOrderTime() != null) {

            Date FromTime = new java.sql.Date(criteria.getFromOrderTime().getTime());
            whereCondition += " and o.orderTime>='" + FromTime + "'";
        }
        if (criteria.getEndOrderTime() != null) {
            Date EndTime = new java.sql.Date(criteria.getEndOrderTime().getTime());
            whereCondition += " and o.orderTime<='" + EndTime + "'";
        }

        Query countQuery = this.getSession().createQuery(countHql + whereCondition);
        Integer totalCount = ((Long) countQuery.uniqueResult()).intValue();

        Query query = this.getSession().createQuery(hql1 + whereCondition + orders);
        query.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize());
        query.setMaxResults(criteria.getPageSize());
        query.setResultTransformer(Transformers.aliasToBean(OrderProductItemDto.class));
        //addScalar(query, OrderMealProductDto.class);
        List<OrderProductItemDto> list = query.list();

//        List<OrderProductItemDto> temp = OrderItemFactory.convert(list);
        PagedResult<OrderProductItemDto> permissionGroupDtoPagedResult = new PagedResult<OrderProductItemDto>(list, criteria.getPageNumber(), criteria.getPageSize(), totalCount);
        return permissionGroupDtoPagedResult;
    }

    @Override
    public PagedResult<OrderProductItemDowLoadDto> getOrderProductDownloadItems(OrderProductItemCriteria criteria) {

        PagedResult<OrderProductItemDto> pagedResult = this.getOrderProductItems(criteria);
        List<OrderProductItemDowLoadDto> list = new ArrayList<>();
        pagedResult.getItems().stream().forEach(i -> {
            OrderProductItemDowLoadDto dowLoadDto = new OrderProductItemDowLoadDto();
            dowLoadDto.setOrderNum(i.getOrderNum());
            dowLoadDto.setScheduleDate(i.getScheduleDate());
            dowLoadDto.setName(i.getName());
            dowLoadDto.setNum(i.getNum());
            if (i.getType() != null) {
                switch (i.getType()) {
                    case Ticket:
                        dowLoadDto.setType("门票");
                        break;
                    case Catering:
                        dowLoadDto.setType("餐饮");
                        break;
                    case Souvenirs:
                        dowLoadDto.setType("纪念品");
                        break;
                    case Hotel:
                        dowLoadDto.setType("酒店");
                        break;
                    case Other:
                        dowLoadDto.setType("其他");
                        break;
                    default:
                        dowLoadDto.setItemStatus("");
                        break;
                }
            } else {
                dowLoadDto.setType("");
            }
            dowLoadDto.setPrice(i.getPrice());
            dowLoadDto.setCount(i.getCount());
            if (i.getItemStatus() != null) {
                switch (i.getItemStatus()) {
                    case checked:
                        dowLoadDto.setItemStatus("已使用");
                        break;
                    case cancelFailed:
                    case created:
                    case reCreated:
                    case imgError:
                    case allSuccess:
                        dowLoadDto.setItemStatus("未使用");
                        break;
                    case canceled:
                        dowLoadDto.setItemStatus("已取消");
                        break;
                    case createError:
                    case reCreateError:
                    case gjpError:
                        dowLoadDto.setItemStatus("付款未出票");
                        break;
                    default:
                        dowLoadDto.setItemStatus("");
                        break;
                }
            } else {
                dowLoadDto.setItemStatus("");
            }
            dowLoadDto.setTakeDate(i.getTakeDate());
            list.add(dowLoadDto);
        });

        PagedResult<OrderProductItemDowLoadDto> permissionGroupDtoPagedResult = new PagedResult<OrderProductItemDowLoadDto>(list, criteria.getPageNumber(), criteria.getPageSize(), pagedResult.getTotalItemsCount());
        return permissionGroupDtoPagedResult;
    }


    @Override
    public List<OrderDownLoadDto> getOrdersDownload(OrderCriteria criteria) {
        Criteria criterion = getSession().createCriteria(OrderInfo.class);
        if (!StringUtils.isNullOrWhiteSpace(criteria.getNum())) {
            criterion.add(Restrictions.like("num", "%" + criteria.getNum() + "%"));
        }
        criterion.createAlias("tourist", "tourist");
        if (!StringUtils.isNullOrWhiteSpace(criteria.getTouristRealName())) {
            criterion.add(Restrictions.like("tourist.realName", "%" + criteria.getTouristRealName() + "%"));
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getTouristUserName())) {
            criterion.add(Restrictions.like("tourist.userName", "%" + criteria.getTouristUserName() + "%"));
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getTouristWxName())) {
            criterion.add(Restrictions.like("tourist.wxName", "%" + criteria.getTouristWxName() + "%"));
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getTouristId())) {
            criterion.add(Restrictions.eq("tourist.id", Long.parseLong(criteria.getTouristId())));
        }

        criterion.createAlias("products", "products");
        if (criteria.getProductId() != null && criteria.getProductId() > 0) {
            // criterion.createAlias("products", "products");
            criterion.add(Restrictions.eq("products.id", criteria.getProductId()));
        }

        criterion.createAlias("products.product", "product");
        if (!StringUtils.isNullOrWhiteSpace(criteria.getProductName())) {
            criterion.add(Restrictions.like("product.name", criteria.getProductName()));
        }

        if (criteria.getStatus() != null) {
            criterion.add(Restrictions.eq("status", criteria.getStatus()));
        }
        if (criteria.getFromOrderTime() != null) {
            criterion.add(Restrictions.ge("orderTime", criteria.getFromOrderTime()));
        }
        if (criteria.getEndOrderTime() != null) {
            criterion.add(Restrictions.le("orderTime", criteria.getEndOrderTime()));
        }

        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criterion.addOrder(
                criteria.isAscOrDesc() ?
                        org.hibernate.criterion.Order.asc(criteria.getOrderByProperty() == null ? "orderTime" : criteria.getOrderByProperty())
                        : org.hibernate.criterion.Order.desc(criteria.getOrderByProperty() == null ? "orderTime" : criteria.getOrderByProperty()));

        List<OrderInfo> infos = criterion.list();

        List<OrderDownLoadDto> downLoadDtos = new ArrayList<>();


        for (OrderInfo orderInfo : infos) {
            OrderDownLoadDto dto = new OrderDownLoadDto();
            dto.setNum(orderInfo.getNum());
            dto.setStatus(getStatusInfo(orderInfo.getStatus()));
            dto.setUserName(orderInfo.getTourist().getWxName());
            if (orderInfo.getCoupon() != null)
                dto.setCouponInfo("满" + orderInfo.getCoupon().getUseCondition() + "减" + orderInfo.getCoupon().getQuota());
            dto.setFetcher(orderInfo.getFetcher().getRealName());
            dto.setPhone(orderInfo.getFetcher().getMobilePhone());

            dto.setOrderTime(orderInfo.getOrderTime());
            dto.setOrderPrice(orderInfo.getOrderPrice());
            String products = "";
            for (OrderProduct p : orderInfo.getProducts()) {
                products += p.getProduct().getName() + " *" + p.getCount() + "张;";
            }

            dto.setProductNames(products);
            dto.setPayTypes(orderInfo.getPayTypes());
            downLoadDtos.add(dto);

        }


        return downLoadDtos;
    }


    private String getStatusInfo(OrderStatus status) {
        Integer i = status.ordinal();
        String strStatus = "";
        switch (i) {
            case 0:
                strStatus = "待付款";
                break;
            case 1:
                strStatus = "付款待确认";
                break;
            case 2:
                strStatus = "已付款";
                break;
            case 3:
                strStatus = "已超时";
                break;
            case 4:
                strStatus = "待评价";
                break;
            case 5:
                strStatus = "已取消";
                break;
            case 6:
                strStatus = "已完成已评价";
                break;
            default:
                strStatus = "异常";
                break;

        }
        return strStatus;
    }

    /**
     * 单个订单商品退单处理
     *
     * @param orderProductId
     * @return 退票处理
     */
    @Override
    public CommonResult returnApprove(Long orderProductId) {
        OrderProduct orderProduct = orderProductRepository.get(orderProductId);
        if (orderProduct == null || !orderProduct.getIsActive()) return ResultFactory.commonError("该订单商品不存在");
        if (orderProduct.getCanReturn() && orderProduct.getStatus().equals(OrderProductStatus.normal)) {
            if (orderProduct.returnApprove(true)) {
                Product product = productRepository.get(orderProduct.getProduct().getOriginalId());
                //增加该商品的库存
                if (product.increaseProductStock(orderProduct.getCount())) {
                    //持久化
                    productRepository.save(product);
                    return ResultFactory.commonSuccess(orderProductRepository.save(orderProduct));
                }
            }
        }
        return ResultFactory.commonError("该订单商品属于不可退商品");
    }

    /**
     * 改签申请
     * *@param orderProductId        订单商品标识
     *
     * @param tourMealDate          改签入园日期
     * @param hotelScheduleMealDate 改签入住日期，非酒店订单可忽略
     * @return
     */
    @Override
    public CommonResult meal(Long orderProductId, Date tourMealDate, Date hotelScheduleMealDate) {
        OrderProduct orderProduct = orderProductRepository.get(orderProductId);
        if (orderProduct == null || !orderProduct.getIsActive()) return ResultFactory.commonError("该订单商品不存在");
        if (orderProduct.getCanMeal()) {
            boolean b = orderProduct.meal(tourMealDate, hotelScheduleMealDate);
            if (b) return ResultFactory.commonSuccess(orderProductRepository.save(orderProduct));
        }
        return ResultFactory.commonError("该订单商品属于不可改签商品");
    }

    /**
     * 改签审核
     *
     * @param orderProductId 订单商品标识
     * @param approve        是否同意改签
     * @return
     */
    //todo no over
    @Override
    public CommonResult mealCheck(String orderNum, Long orderProductId, Boolean approve) {
        OrderProduct orderProduct = orderProductRepository.get(orderProductId);
        if (orderProduct == null || !orderProduct.getIsActive()) return ResultFactory.commonError("该订单商品不存在");
        if (orderProduct.getStatus().equals(OrderProductStatus.mealCheck)) {
            if (approve) {
                Boolean hasError = false;
                for (OrderProductItem item : orderProduct.getItems()) {
                    if (item.getType() == ProductItemType.Ticket) {
                        CommonResult result = ticketZybMeal(item.getTakeNum());
                        if (!result.getIsSuccess()) {
                            hasError = true;
                        } else {
                            item.setScheduleDate(orderProduct.getTourScheduleMealDate());
                        }
                    }
                    if (item.getType() == ProductItemType.Hotel) {
                        item.setScheduleDate(orderProduct.getHotelScheduleDate());
                    }
                }
                if (hasError) {
                    return ResultFactory.commonError("改签失败，请点击查看详细信息，稍后重试！");
                }
                return ResultFactory.commonSuccess(orderProductRepository.save(orderProduct));
            } else {
                orderProduct.setStatus(OrderProductStatus.mealReject);
                orderProductRepository.save(orderProduct);
                return ResultFactory.commonError("该订单商品未通过审核");
            }
        }
        return ResultFactory.commonError("该订单商品未申请改签");
    }

    /**
     * 订单商品查询
     *
     * @param orderProductStatus
     * @return
     */
    @Override
    public List<OrderProductDto> findOrderProduct(OrderProductStatus orderProductStatus) {
        List<OrderProduct> list = orderProductRepository.findOrderProduct(orderProductStatus);
        if (list == null || list.size() == 0) return null;
        else return OrderProductFactory.convert(list);
    }

    /**
     * 订单商品查询
     *
     * @param orderProductId
     * @return
     */
    public UniqueResult<OrderProductDto> getOrderProduct(Long orderProductId) {
        OrderProduct product = orderProductRepository.get(orderProductId);
        return null != product ? new UniqueResult<>(OrderProductFactory.convert(product))
                : new UniqueResult<>("s");
    }

//    /**
//     * 用于对单个订单商品的操作
//     * 获取单个订单商品
//     * @param orderProductId
//     * @return
//     */
//    public OrderProductDto getOrderProductDto(Long orderProductId){
//        OrderProduct orderProduct=orderProductRepository.get(orderProductId);
//        return orderProduct==null?null:OrderProductFactory.convert(orderProduct);
//    }

    //endregion

    //region inner private method

    /**
     * 根据product标识加载最新ProductHistory
     *
     * @param product 商品 ，对应ProductHistory中的OriginalId
     * @return <p>如果存在最新ProductHistory则直接获取返回，否则新增ProductHistory并获取返回</p>
     */
    private ProductHistory loadLatestProductHistory(Product product) {
        ProductHistory history = productHistoryRepository.getLatestByOriginalId(product.getId());
        if (history != null && history.getCreateDateTime().after(product.getLastUpdateTime()))
            return history;

        ProductHistory latest = ProductHistoryFactory.convert(product);
        productHistoryRepository.save(latest);
        return latest;
    }


    /**
     * 订单信息有效性校验
     *
     * @param order <p>if order is invalid then throw exception to interrupt</p>
     */
    private CommonResult orderCheck(CreateOrder order) {
        //TODO order validCheck
        //游客的检查
        //touristRepository.get(order.getTouristId()).getStatus().equals(TouristStatus.Forbidden);游客状态是否检查
        if (null == order.getTouristId() || !touristRepository.exists(order.getTouristId()))
            return ResultFactory.commonError("指定游客标识" + order.getTouristId() + "不存在");
        //商品的检查
        Stream<Long> productIds = order.getOrderProducts().stream().map(x -> x.getProductId()).distinct();


        if (productIds.anyMatch(x -> {
            Integer count = order.getOrderProducts().stream().filter(j -> j.getProductId().equals(x)).mapToInt(i -> i.getCount()).sum();
            Product product = productRepository.get(x);
            return product == null || product.getStock() < count;

        }))
            return ResultFactory.commonError("指定商品标识不存在或库存不足");
        //取件人的检查
        if (order.getCheckFetcher())
            if (null == order.getFetcherDtoId() || touristRepository.get(order.getTouristId()).getFetchers().stream().noneMatch(i -> order.getFetcherDtoId().equals(i.getId())))
                return ResultFactory.commonError("指定取件人标识" + order.getFetcherDtoId() + "不存在");
        //优惠券的检查，优惠券可以没有
        if (null != order.getCouponId()) {
            Coupon coupon = couponRepository.get(order.getCouponId());
            Date ex = DateHelper.addDay(coupon.getExpireDate(), 1);
            if (!ex.after(new Date()))
                return ResultFactory.commonError("指定优惠券已过期");
            if (null == coupon || !coupon.getIsActive())
                return ResultFactory.commonError("指定优惠券标识" + order.getCouponId() + "不存在");
            else if (!coupon.getCouponStatus().equals(CouponStatus.bind))
                return ResultFactory.commonError("指定优惠券标识" + order.getCouponId() + "处于不可用状态");

        }
        return ResultFactory.commonSuccess();
//        if (null != order.getCouponId() && couponRepository.exists(order.getCouponId()))
//            throw new Exception("指定优惠券标识" + order.getCouponId() + "不存在");
    }


    /**
     * 使用优惠券
     *
     * @param orderInfo
     * @return
     */
    private UniqueResult<Coupon> useCoupon(OrderInfo orderInfo, Long couponId) {
//        Tourist tourist = touristRepository.get(orderInfo.getTourist().getId());
        Coupon coupon = couponRepository.get(couponId);

        BigDecimal totalPrice = orderInfo.getOrderPrice();
        //是否享受优惠券的优惠，判断是否满足条件
        if (coupon != null && totalPrice.compareTo(coupon.getUseCondition()) >= 0)
            return new UniqueResult<Coupon>(coupon);
        else {
            return new UniqueResult<Coupon>("优惠券不可用");
        }
    }


    /**
     * 退回使用优惠券
     *
     * @param couponId   优惠券标识
     * @param touristId  游客标识
     * @param expireDate 延长优惠券有效日期
     * @return
     */
    private Boolean returnCoupon(Long couponId, Long touristId, Date expireDate) {
        Tourist tourist = touristRepository.get(touristId);
        Coupon c = couponRepository.get(couponId);
        if (tourist != null && c != null) {
            //检查该优惠券是否为这位消费者者的，并且处于可使用状态
            if (c.getCouponStatus().equals(CouponStatus.Used)) {
                tourist.getBindCoupons().add(c);
                if (expireDate != null && expireDate.after(new Date())) c.setExpireDate(expireDate);
                c.setCouponStatus(CouponStatus.bind);
                touristRepository.save(tourist);
                couponRepository.save(c);
                return true;
            }
        }
        return false;
    }


    @Override
    public void setOrderStatusByProductStatus(Long productId) {

        String hql = "update orderinfo set status=5 where id in(select order_id from orderproduct where product_id in(select id from producthistory where originalId=" + productId + "))";
        Query query = this.getSession().createSQLQuery(hql);
        query.executeUpdate();
    }

    @Override
    public PagedResult getMealOrders(OrderCriteria criteria) {
        Criteria criterion = getSession().createCriteria(OrderInfo.class);

        criterion.createAlias("products", "products");
        criterion.add(Restrictions.or(Restrictions.eq("products.status", OrderProductStatus.mealCheck),
                Restrictions.eq("products.status", OrderProductStatus.mealed), Restrictions.eq("products.status", OrderProductStatus.mealReject), Restrictions.eq("products.status", OrderProductStatus.mealed)));


        if (criteria.getFromOrderTime() != null) {
            criterion.add(Restrictions.ge("orderTime", criteria.getFromOrderTime()));
        }
        if (criteria.getEndOrderTime() != null) {
            criterion.add(Restrictions.le("orderTime", criteria.getEndOrderTime()));
        }

        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criterion.addOrder(
                criteria.isAscOrDesc() ?
                        org.hibernate.criterion.Order.asc(criteria.getOrderByProperty() == null ? "orderTime" : criteria.getOrderByProperty())
                        : org.hibernate.criterion.Order.desc(criteria.getOrderByProperty() == null ? "orderTime" : criteria.getOrderByProperty()));
        criterion.setFirstResult(
                (criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());

        List<OrderDto> dtos = OrderFactory.convert(criterion.list());
        return new PagedResult<>(dtos, criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }

    @Override
    public PagedResult getMealOrdersProduct(OrderCriteria orderCriteria) {

        String hql1 = "select o.orderTime as timeOrder, o.num as num, ph.name as name,t.userName as username,op.status as orderStatus,op.id as orderProductId from OrderInfo o join o.tourist t join o.products op join op.product ph ";
        String countHql = "select  count(*)  from OrderInfo o join o.tourist t join o.products op join op.product ph  ";
        String whereCondition = " where o.isActive=1 ";
        String orders = " order by o.num desc";

        if (!StringUtils.isNullOrWhiteSpace(orderCriteria.getProductName())) {
            whereCondition += " and ph.name='" + orderCriteria.getProductName() + "'";
        }
        if (!StringUtils.isNullOrWhiteSpace(orderCriteria.getNum())) {
            whereCondition += " and o.num='" + orderCriteria.getNum() + "'";
        }
        if (!StringUtils.isNullOrWhiteSpace(orderCriteria.getTouristUserName())) {
            whereCondition += " and t.userName='" + orderCriteria.getTouristUserName() + "'";
        }

        if (orderCriteria.getFromOrderTime() != null) {

            Date FromTime = new java.sql.Date(orderCriteria.getFromOrderTime().getTime());
            whereCondition += " and o.orderTime>='" + FromTime + "'";
        } else {
            Date FromTime = new java.sql.Date(new Date().getTime());
            whereCondition += " and (op.tourScheduleDate>='" + FromTime + "' or op.hotelScheduleDate>='" + FromTime + "')";
        }
        if (orderCriteria.getEndOrderTime() != null) {
            Date EndTime = new java.sql.Date(orderCriteria.getEndOrderTime().getTime());
            whereCondition += " and o.orderTime<='" + EndTime + "'";
        }
        if (orderCriteria.getProductStatus() != null) {
            whereCondition += " and op.status=" + orderCriteria.getProductStatus().ordinal();
        } else {
            whereCondition += " and op.status>=1 and op.status<=3";
        }
        Query countQuery = this.getSession().createQuery(countHql + whereCondition);
        Integer totalCount = ((Long) countQuery.uniqueResult()).intValue();
        Query query = this.getSession().createQuery(hql1 + whereCondition + orders);
        query.setFirstResult((orderCriteria.getPageNumber() - 1) * orderCriteria.getPageSize());
        query.setMaxResults(orderCriteria.getPageSize());
        query.setResultTransformer(Transformers.aliasToBean(OrderMealProductDto.class));
        //addScalar(query, OrderMealProductDto.class);
        List<OrderMealProductDto> list = query.list();
        PagedResult<OrderMealProductDto> permissionGroupDtoPagedResult = new PagedResult<OrderMealProductDto>(list, orderCriteria.getPageNumber(), orderCriteria.getPageSize(), totalCount);
        return permissionGroupDtoPagedResult;
    }

    @Override
    public CommonResult ticketEnterConfirm(String zybOrderCode) {
        String[] ids = zybOrderCode.split("C");
        String orderNum = ids[0];
        Long itemId = Long.parseLong(ids[1]);
        OrderInfo orderInfo = orderRepository.get(orderNum);
        for (OrderProduct product : orderInfo.getProducts()) {
            Boolean allChecked = true;
            for (OrderProductItem item : product.getItems()) {
                if (item.getId().equals(itemId)) {
                    item.updateItemStatus(ItemStatus.checked, "");
                }
                if (!item.isComplete()) {
                    allChecked = false;
                }
            }
            if (allChecked)
                product.setStatus(OrderProductStatus.end);
        }
        //  orderInfo.synacOrderOperatorStatus();

        orderRepository.save(orderInfo);

        return ResultFactory.commonSuccess();
    }

    public CommonResult reGetZybImg(String orderNum, Long productId) {
        OrderInfo info = orderRepository.get(orderNum);

        StringBuilder hasFalse = new StringBuilder();
        info.getProducts().stream().forEach(p ->
        {
            p.getItems().stream().forEach(item ->
            {
                if (!this.getZybImage(orderNum, item).getIsSuccess())
                    hasFalse.append(item.getName() + ";");
            });
        });
        info.synacOrderOperatorStatus();
        info.setStatus(OrderStatus.payed);
        orderRepository.save(info);

        if (!StringUtils.isNullOrWhiteSpace(hasFalse.toString())) {
            return ResultFactory.commonError(hasFalse.toString() + "生成二维码异常");
        }
        return ResultFactory.commonSuccess();
    }


    public CommonResult getZybImage(String orderNum, OrderProductItem item) {
        String itemUninCode = item.getTakeNum();//订单商品项唯一编码，做订单明细编号、取货编码
        String qrCode = "";
        if (StringUtils.isNullOrWhiteSpace(itemUninCode))
            itemUninCode = orderNum + "C" + item.getId();

        if (item.getType().equals(ProductItemType.Ticket)) {
            if (item.getItemStatus() == ItemStatus.created || item.getItemStatus() == ItemStatus.reCreated || item.getItemStatus() == ItemStatus.imgError) {
                String code = getTicketQrCode(itemUninCode);
                // 重试三次，硬编码了
                for (int i = 0; i < 2; i++) {
                    if (!code.equals("500")) {
                        qrCode = code;
                        break;
                    } else code = getTicketQrCode(itemUninCode);
                }
            }
        } else {
            // 生成每类商品项的取货二维码
            //actionUrl模板 "http://admin3.cn-yc.com/itemTake?takeNum=@";
            try {
                String actionUrl = Configs.getProperty("itemTakeUrl");
                actionUrl = actionUrl.replace("@", itemUninCode);
                if (StringUtils.isNullOrWhiteSpace(actionUrl))
                    throw new ConfigurationException("itemTakeUrl");

                actionUrl = URLEncoder.encode(actionUrl, "utf-8");
                qrCode = QRCodeGenerator.generalQRCode(actionUrl, 200, 200);
            } catch (ConfigurationException | UnsupportedEncodingException e) {
                e.printStackTrace();

                return ResultFactory.commonError("二维码生成失败");
            }
        }
        if (!StringUtils.isNullOrWhiteSpace(qrCode)) {
            qrCode = "data:image/jpg;base64," + qrCode;
            item.setTakeNum(itemUninCode);

            item.setQrCode(qrCode);
            item.updateItemStatus(ItemStatus.allSuccess, "");
        } else {
            if (item.getType().equals(ProductItemType.Ticket)) {
                if (item.getItemStatus() == ItemStatus.created || item.getItemStatus() == ItemStatus.reCreated || item.getItemStatus() == ItemStatus.imgError) {
                    item.updateItemStatus(ItemStatus.imgError, item.getName() + ":二维码生成失败");
                    return ResultFactory.commonError("二维码生成失败");
                }
            } else {
                item.updateItemStatus(ItemStatus.imgError, item.getName() + ":二维码生成失败");
                return ResultFactory.commonError("二维码生成失败");
            }
        }


        return ResultFactory.commonSuccess();
    }


    public CommonResult reCreateTicketByProduct(String orderNum, Long productId) {
        OrderInfo info = orderRepository.get(orderNum);
        if (null == info) throw new NullArgumentException("orderNum");
        Boolean allSuccess = true;
        if (info.getStatus() == OrderStatus.payed) {
            for (OrderProduct p : info.getProducts()) {
                if (productId.equals(p.getId())) {
                    if (p.getOrderOperatorStatus() == OrderOperatorStatus.ZybError)

                        for (OrderProductItem item : p.getItems()) {
                            if (item.getItemStatus() != ItemStatus.checked)
                                if (item.getType() == ProductItemType.Ticket && (item.getItemStatus() == ItemStatus.createError || item.getItemStatus() == ItemStatus.reCreateError)) {
                                    if (ticketZybCreate(orderNum, p, info.getFetcher(), item).getIsSuccess()) {
                                        getZybImage(orderNum, item);
                                    } else
                                        allSuccess = false;
                                }
                            if (item.getType() != ProductItemType.Ticket && item.getItemStatus() != ItemStatus.allSuccess)
                                getZybImage(orderNum, item);
                        }
                    else {
                        return ResultFactory.commonError("商品状态异常，创建失败！");
                    }
                }
            }
            info.synacOrderOperatorStatus();
        }
        info.setStatus(OrderStatus.payed);
        orderRepository.save(info);
        if (allSuccess)
            return ResultFactory.commonSuccess();
        else
            return ResultFactory.commonError("智游宝异常，请点击查看详细信息！");
    }


    /**
     * 创建智游宝门票
     *
     * @param orderNum
     * @param product
     * @param fetcher
     * @param item
     * @return
     */
    public CommonResult ticketZybCreate(String orderNum, OrderProduct product, Fetcher fetcher, OrderProductItem item) {
        Client client = new PwbClientImpl();

        String reTakeNum = "";
        Date scDate;
        if (product.getStatus() == OrderProductStatus.mealCheck) {
            scDate = product.getTourScheduleMealDate();
        } else {
            scDate = product.getTourScheduleDate();
        }
        if (!StringUtils.isNullOrWhiteSpace(item.getTakeNum()))
            reTakeNum = item.getTakeNum() + "_re";
        else {
            reTakeNum = orderNum + "C" + item.getId();
        }
        StringBuilder stringBuilder = new StringBuilder("");
        OrderRequest orderRequest = new OrderRequest();
        OrderResponse orderResponse;
        Order order = new Order();
        order.setCertificateNo(fetcher.getIdCardNo());
        order.setLinkName(fetcher.getRealName());
        order.setLinkMobile(fetcher.getMobilePhone());
        order.setOrderCode(reTakeNum);
        order.setOrderPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getCount() * product.getCount())).toString());
        order.setPayMethod("vm");
        TicketOrder ticketOrder = new TicketOrder();
        ticketOrder.setOrderCode(reTakeNum);
        ticketOrder.setPrice(item.getPrice().toString());
        ticketOrder.setQuantity(item.getCount() * product.getCount());
        ticketOrder.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(ticketOrder.getQuantity())).toString());
        ticketOrder.setOccDate(JaxbBase.SIMPLE_DATE_FORMAT.format(scDate));
        ticketOrder.setGoodsCode(item.getNum());
        ticketOrder.setGoodsName(item.getName());
        order.setTicketOrder(ticketOrder);
        orderRequest.setOrder(order);
        try {
            orderResponse = client.execute(orderRequest);
            if (orderResponse.getCode() == 0) {
                if (product.getStatus() == OrderProductStatus.mealCheck) {
                    item.setItemStatus(ItemStatus.reCreated);
                    item.setItemStatusRemark("");
                    item.setScheduleDate(scDate);
                    item.setTakeNum(reTakeNum);
                } else {
                    item.setItemStatus(ItemStatus.created);
                    item.setItemStatusRemark("");
                    item.setScheduleDate(scDate);
                    item.setTakeNum(reTakeNum);
                }

//                product.setOrderOperatorStatus(OrderOperatorStatus.Success);
//                product.setOperatorRemark("");


            } else {
                String msg = "智游宝下单返回失败 orderNum:" + orderNum
                        + ",orderProductId:" + product.getId() + ",error_code:" + orderResponse.getCode()
                        + ",description:" + orderResponse.getDescription();
                logger.info(msg);
                if (product.getStatus() == OrderProductStatus.mealCheck) {
                    item.updateItemStatus(ItemStatus.reCreateError, "智游宝下单失败:" + orderResponse.getDescription());

                } else
                    item.updateItemStatus(ItemStatus.createError, "智游宝下单失败:" + orderResponse.getDescription());

                product.setOrderOperatorStatus(OrderOperatorStatus.ZybError);
                product.setOperatorRemark("智游宝下单失败:" + orderResponse.getDescription());
                //stringBuilder.append("智游宝下单失败:"+ orderResponse.getDescription());
                return new CommonResult(false, "智游宝下单失败:" + orderResponse.getDescription());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            stringBuilder.append(orderNum + "error;");

            return new CommonResult(false, "智游宝下单失败:" + e1.getMessage());
        }

        return ResultFactory.commonSuccess();
    }

    @Override
    public CommonResult ticketZybMeal(String zybOrderCode) {
        String[] ids = zybOrderCode.split("C");
        String orderNum = ids[0];
        Long itemId = Long.parseLong(ids[1]);
        OrderInfo orderInfo = orderRepository.get(orderNum);
        for (OrderProduct product : orderInfo.getProducts()) {
            for (OrderProductItem item : product.getItems()) {
                if (item.getId().equals(itemId)) {
                    if (product.getCanMeal() && product.getStatus() == OrderProductStatus.mealCheck) {
                        CommonResult cancelResult;
                        if (item.ticketCanCancel()) {
                            cancelResult = this.zybTicketCancel(zybOrderCode);
                        } else {
                            if (item.getItemStatus() == ItemStatus.canceled || item.getItemStatus() == ItemStatus.reCreateError)
                                cancelResult = ResultFactory.commonSuccess();
                            else {
                                return ResultFactory.commonError("门票状态异常无法改签");
                            }
                        }

                        if (cancelResult.getIsSuccess()) {

                            item.setQrCode(null);

                            item.setItemStatus(ItemStatus.canceled);

                            CommonResult result = ticketZybCreate(orderNum, product, orderInfo.getFetcher(), item);
                            if (result.getIsSuccess()) {
                                product.setStatus(OrderProductStatus.mealed);
                                if (getZybImage(orderNum, item).getIsSuccess()) {
                                    product.setOrderOperatorStatus(OrderOperatorStatus.Success);

                                    item.updateItemStatus(ItemStatus.allSuccess, "");
                                    item.setItemStatusRemark("");
                                } else {
                                    product.setOrderOperatorStatus(OrderOperatorStatus.ZybImgError);
                                    item.updateItemStatus(ItemStatus.imgError, "票已生成，二维码获取失败,点击查看");
                                }

                            } else {
                                orderRepository.save(orderInfo);
                                return result;
                            }
                        } else {
                            //  item.setItemStatus(ItemStatus.cancelFailed);
                            return ResultFactory.commonError("门票取消失败");
                        }

                    } else {
                        return ResultFactory.commonError("门票状态异常无法改签");
                    }
                }
            }
        }

        orderInfo.synacOrderOperatorStatus();
        orderInfo.setStatus(OrderStatus.payed);
        orderRepository.save(orderInfo);
        return ResultFactory.commonSuccess();
    }

    @Override
    public CommonResult ticketChargeBack(String orderNum, List<Long> productId) {
        OrderInfo info = orderRepository.get(orderNum);
        if (null == info) throw new NullArgumentException("orderNum");

        if (info.getStatus() == OrderStatus.payed) {
            for (OrderProduct p : info.getProducts()) {
                if (productId.contains(p.getId())) {
                    if (p.getCanReturn())
                        for (OrderProductItem item : p.getItems()) {
                            if (item.getItemStatus() == ItemStatus.checked)
                                return ResultFactory.commonError("有商品已消费无法退单！");
                        }
                    else {
                        return ResultFactory.commonError("该商品不可退！");
                    }
                }
            }
            info.getProducts().stream().forEach(p ->
            {
                try {
                    if (productId.contains(p.getId())) {
                        StringBuilder builder = new StringBuilder();
                        p.getItems().stream().forEach(item ->
                        {
                            if (item.getType() == ProductItemType.Ticket) {
                                if (item.ticketCanCancel()) {
                                    if (zybTicketCancel(item.getTakeNum()).getIsSuccess()) {
                                        item.updateItemStatus(ItemStatus.canceled, "");
                                        item.setQrCode("");

                                    } else {

                                        item.updateItemStatus(ItemStatus.cancelFailed, item.getName() + ":取消失败;");

                                        builder.append(orderNum + "_" + item.getName() + "取消失败;");
                                    }
                                } else if (item.getItemStatus() == ItemStatus.createError || item.getItemStatus() == ItemStatus.reCreateError) {
                                    item.updateItemStatus(ItemStatus.canceled, "");
                                    item.setQrCode("");
                                } else {
                                    item.updateItemStatus(ItemStatus.cancelFailed, item.getName() + ":当前状态无法退票;");
                                    builder.append(item.getName() + ":当前状态无法退票;");

                                }
                            } else {
                                if (item.getItemStatus() != ItemStatus.canceled)
                                    item.updateItemStatus(ItemStatus.canceled, "");
                                item.setQrCode("");
                            }
                        });
                        if (!StringUtils.isNullOrWhiteSpace(builder.toString())) {
                            p.setOrderOperatorStatus(OrderOperatorStatus.zybCancelError);
                            p.setOperatorRemark(builder.toString());
                        } else {
                            p.setStatus(OrderProductStatus.returned);
                            Product product = productRepository.get(p.getProduct().getOriginalId());
                            product.increaseProductStock(p.getCount());
                            productRepository.save(product);
                        }
                    }
                } catch (Exception ex) {
                    p.setOrderOperatorStatus(OrderOperatorStatus.zybCancelError);
                    p.setOperatorRemark(ex.getMessage());
                }

            });
        } else {

            return ResultFactory.commonError("订单当前状态不允许退单");
        }

        info.synacOrderOperatorStatus();
        orderRepository.save(info);
        if (info.getOrderOperatorStatus() != OrderOperatorStatus.Success)
            return ResultFactory.commonError("取消失败,点击查看详情;");

        else
            return ResultFactory.commonSuccess();
    }


    private CommonResult zybTicketCancel(String zybCode) {
        Client client = new PwbClientImpl();
        CancelOrderRequest cancelRequest = new CancelOrderRequest();
        Order order = new Order();
        order.setOrderCode(zybCode);
        cancelRequest.setOrder(order);
        OrderResponse orderResponse;
        try {
            orderResponse = client.execute(cancelRequest);
            if (orderResponse.getCode() == 0) {
                return ResultFactory.commonSuccess();

            } else {
                String msg = "智游宝下单返回失败 orderNum:" + zybCode + ",error_code:" + orderResponse.getCode()
                        + ",description:" + orderResponse.getDescription();
                logger.info(msg);

                return new CommonResult(false, "智游宝下单失败:" + orderResponse.getDescription());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.info(e1.getMessage());
            return new CommonResult(false, "智游宝下单失败:" + e1.getMessage());
        }
    }

}
