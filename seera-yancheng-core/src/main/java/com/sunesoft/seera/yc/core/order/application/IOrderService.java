package com.sunesoft.seera.yc.core.order.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.yc.core.order.application.criteria.CreateOrder;
import com.sunesoft.seera.yc.core.order.application.criteria.OrderCriteria;
import com.sunesoft.seera.yc.core.order.application.criteria.OrderProductItemCriteria;
import com.sunesoft.seera.yc.core.order.application.dtos.*;
import com.sunesoft.seera.yc.core.order.domain.OrderInfo;
import com.sunesoft.seera.yc.core.order.domain.OrderProductStatus;

import java.util.Date;
import java.util.List;

/**
 * 商品服务接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IOrderService {

    /**
     * 下订单
     *
     * @return 下单是否成功
     * <p>成功下单后20分钟未支付，订单作废，自动取消</p>
     * @ CreateOrder order
     */
    UniqueResult<OrderInfo> createOrder(CreateOrder order) throws Exception;

    /**
     * 订单付款
     *
     * @param orderId 订单标识
     * @return 更新成功
     * <p>确保在订单支付成功后更新订单状态为已支付</p>
     */
    public CommonResult payOrder(Long orderId);

    /**
     * 订单付款
     *
     * @param orderNum 订单号.
     * @return 更新成功
     * <p>确保在订单支付成功后更新订单状态为已支付</p>
     */
    CommonResult payOrder(String orderNum);


    /**
     * 订单成功支付通知
     *
     * @param orderNum   订单号
     * @return 已支付成功后续事宜处理结果
     */
    public CommonResult orderPaySuccess(String orderNum,String payType) throws Exception;

    /**
     * 取消订单
     *
     * @param orderId 订单标识
     * @return 取消成功
     * <p>取消订单，更新订单状态，同时返还商品库存数量</p>
     */
    public CommonResult cancelOrder(Long orderId);

//    /**
//     * 删除订单记录
//     * @param orderIds
//     * @return
//     */
//    public CommonResult deleteOrder(List<Long> orderIds);

    /**
     * 订单商品项使用
     *
     * @param id  订单商品项标识
     * @return 是否成功
     * <p>适用于游客订单商品二维码扫描核销处理</p>
     */
    public CommonResult productItemTake(Long id);

    /**
     * 订单商品项使用
     *
     * @param takeNum  订单商品项提取码
     * @return 是否成功
     * <p>适用于游客订单商品二维码扫描核销处理</p>
     */
    public CommonResult productItemTake(String takeNum);

    /**
     * 获取指定订单详情
     *
     * @param orderId 订单标识
     * @return 订单实体
     */
    public OrderDto getOrder(Long orderId);

    /**
     * 获取指定订单详情
     *
     * @param orderNum 订单编号
     * @return 订单实体
     */
    public OrderDto getOrder(String orderNum);

//    /**
//     * 获取指定订单商品项
//     *
//     * @param orderNum 订单编号
//     * @return 订单商品项实体
//     */
//    public List<OrderProductItemDto>  getOrderItems(String orderNum);

    /**
     * 获取指定订单商品项
     *
     * @param takeNum 订单商品项取货码
     * @return 订单商品项实体
     */
    public OrderProductItemDto getOrderItem(String takeNum);

    /**
     * @param criteria 查询条件
     * @return 订单分页集合
     */
    public PagedResult<OrderDto> getOrders(OrderCriteria criteria);

    /**
     *
     * @param criteria
     * @return 订单商品项集合
     */
    public PagedResult<OrderProductItemDto> getOrderProductItems(OrderProductItemCriteria criteria);

    public PagedResult<OrderProductItemDowLoadDto> getOrderProductDownloadItems(OrderProductItemCriteria criteria);




    public List<OrderDownLoadDto> getOrdersDownload(OrderCriteria criteria)  ;

    /**
     * 单个订单商品退单处理
     *
     * @param orderProductIde 订单商品标识
     * @return 改签成功与否
     */
    public CommonResult returnApprove(Long orderProductIde);

    /**
     * 改签
     * *@param orderProductId
     *
     * @param tourMealDate          改签入园日期sy
     * @param hotelScheduleMealDate 改签入住日期，非酒店订单可忽略
     * @return
     */
    public CommonResult meal(Long orderProductId, Date tourMealDate, Date hotelScheduleMealDate);

    /**
     * 改签审核
     * <p>改签成功后计算订单入园或入住时间已改签后的时间为准</p>
     * *@param orderProductId
     *
     * @param approve
     * @return
     */
    public CommonResult mealCheck(String orderNum,Long orderProductId, Boolean approve);

    //TODO Order数据 统计

    //endregion

    /**
     * 订单商品查询
     *
     * @param orderProductStatus
     * @return
     */
    public List<OrderProductDto> findOrderProduct(OrderProductStatus orderProductStatus);

    /**
     * 订单商品查询
     *
     * @param orderProductId
     * @return
     */
    public UniqueResult<OrderProductDto> getOrderProduct(Long orderProductId);

    /**
     *
     * 商品下架 取消包含该商品的订单
     * @return
     */
    public void  setOrderStatusByProductStatus(Long productId);

    PagedResult getMealOrders(OrderCriteria orderCriteria);

    PagedResult getMealOrdersProduct(OrderCriteria orderCriteria);


    /**
     * 商品检品成功
     * @param zybOrderCode 检票商品Id
     * @return
     */
    CommonResult ticketEnterConfirm(String zybOrderCode);


    /**
     * 取消订单
     * @param zybOrderCode
     * @return
     */
    CommonResult ticketZybMeal(String zybOrderCode);


    /**
     * 退票
     * @param orderNum
     * @param productId
     * @return
     */
    CommonResult ticketChargeBack(String orderNum,List<Long> productId);


    /**
     * 重新生成二维码
     * @param orderNum
     * @param productId
     * @return
     */
    public CommonResult reGetZybImg(String orderNum, Long productId);


    /**
     * 重新创建门票
     * @param orderNum
     * @param productId
     * @return
     */
    public CommonResult reCreateTicketByProduct(String orderNum,Long productId);
//    /**
//     * 获取单个订单商品
//     * @param orderProductId
//     * @return
//     */
//    public OrderProductDto getOrderProductDto(Long orderProductId);

    public  CommonResult gjpCreateOrder(String code,String orderNo,Integer count);

    /**
     * 领取夜公园门票
     * @param createOrder
     * @return
     */
    public CommonResult LightNightPark(CreateOrder createOrder,Long typeId);




}
