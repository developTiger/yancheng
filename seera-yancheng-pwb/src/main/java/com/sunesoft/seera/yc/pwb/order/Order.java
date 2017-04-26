package com.sunesoft.seera.yc.pwb.order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单信息.
 * <p>
 * Created by bing on 16/7/27.
 */
public final class Order implements Serializable {

    /**
     * 身份证号.
     */
    private String certificateNo;

    /**
     * 联系人.
     */
    private String linkName;

    /**
     * 联系电话.
     */
    private String linkMobile;

    /**
     * 订单编码（或别的），要求唯一.
     * <br/>
     * 我回调你们通知检票完了的标识.
     */
    private String orderCode;

    /**
     * 订单总价格.
     */
    private String orderPrice;

    /**
     * 团号.
     */
    private String groupNo;

    /**
     * 支付方式值spot现场支付vm备佣金 ，zyb智游宝支付.
     */
    private String payMethod;

    /**
     * 辅助码.
     */
    private String assistCheckNo;

    /**
     * 支付状态.
     */
    private String payStatus;

    private String src;

    /**
     * 子订单信息.
     */
    private List<TicketOrder> ticketOrders;

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkMobile() {
        return linkMobile;
    }

    public void setLinkMobile(String linkMobile) {
        this.linkMobile = linkMobile;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    @XmlElementWrapper(name = "ticketOrders")
    @XmlElement(name = "ticketOrder")
    List<TicketOrder> getTicketOrders() {
        return ticketOrders;
    }

    void setTicketOrders(List<TicketOrder> ticketOrders) {
        this.ticketOrders = ticketOrders;
    }

    public void setTicketOrder(TicketOrder ticketOrder) {
        if (ticketOrder == null)
            return;
        if (ticketOrders == null)
            this.ticketOrders = new ArrayList<>();
        if (ticketOrders.contains(ticketOrder))
            return;
        this.ticketOrders.add(ticketOrder);
    }
}
