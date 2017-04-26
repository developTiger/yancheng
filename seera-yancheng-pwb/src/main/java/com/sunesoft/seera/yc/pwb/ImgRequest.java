package com.sunesoft.seera.yc.pwb;

import com.sunesoft.seera.yc.pwb.order.Order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bing on 16/7/27.
 */
@XmlRootElement(name = "PWBRequest")
public final class ImgRequest extends PwbRequest<ImgResponse> {

    /**
     * 订单信息.
     */
    protected List<Order> orders = new ArrayList<>();

    public ImgRequest() {
        super(ImgRequest.class);
        this.transactionName = "SEND_CODE_IMG_REQ";
    }

    /**
     * 获取订单信息.
     *
     * @return 订单信息.
     */
    @XmlElementWrapper(name = "orderRequest")
    @XmlElement(name = "order")
    List<Order> getOrders() {
        return orders;
    }

    /**
     * 设置订单信息.
     *
     * @param orders 订单信息.
     */
    void setOrder(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * 设置订单信息.
     *
     * @param order 订单信息.
     */
    public void setOrder(Order order) {
        if (order == null)
            return;
        if (this.orders == null)
            this.orders = new ArrayList<>();
        if (this.orders.contains(order))
            return;
        this.orders.add(order);
    }
}
