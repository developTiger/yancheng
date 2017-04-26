package com.sunesoft.seera.yc.pwb;

import com.sunesoft.seera.yc.pwb.order.Order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * 下单返回对象.
 * <p>
 * Created by bing on 16/7/27.
 */
@XmlRootElement(name = "PWBResponse")
public class OrderResponse extends PwbResponse {

    /**
     * 订单信息.
     */
    private List<Order> orders = new ArrayList<Order>();

    /**
     * 退单情况查询编号
     */
    private String retreatBatchNo;

    public OrderResponse() {
        super(OrderResponse.class);
    }

    public OrderResponse(Class<?> objectClass) {
        super(objectClass);
    }

    /**
     * 获取订单信息.
     *
     * @return 订单信息.
     */
    @XmlElementWrapper(name = "orderResponse")
    @XmlElement(name = "order")
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * 设置订单信息.
     *
     * @param orders 订单信息.
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        if (this.orders == null)
            this.orders = new ArrayList<>();
        if (this.orders.contains(order))
            return;
        orders.add(order);
    }

    /**
     * 获取订单响应信息.
     *
     * @return 响应信息.
     */
    @XmlElement(name = "retreatBatchNo")
    public String getRetreatBatchNo() {
        return retreatBatchNo;
    }

    /**
     * 响应信息.
     *
     * @param retreatBatchNo 响应信息.
     */
    public void setRetreatBatchNo(String retreatBatchNo) {
        this.retreatBatchNo = retreatBatchNo;
    }
}
