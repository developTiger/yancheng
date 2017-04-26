package com.sunesoft.seera.yc.pwb;

import com.sunesoft.seera.yc.Client;
import com.sunesoft.seera.yc.jaxb.JaxbBase;
import com.sunesoft.seera.yc.pwb.order.Order;
import com.sunesoft.seera.yc.pwb.order.TicketOrder;
import org.junit.Test;

import java.util.Date;

/**
 * Created by bing on 16/8/4.
 */
public class PwbClientImplTest {

    @Test
    public void testOrder() throws Exception {
        Client client = new PwbClientImpl();
        OrderRequest orderRequest = new OrderRequest();

        Order order = new Order();
        order.setCertificateNo("330182198804273139");
        order.setLinkName("111");
        order.setLinkMobile("13625814109");
        order.setOrderCode("t20153111331196");
        order.setOrderPrice("200.00");
        order.setPayMethod("vm");

        TicketOrder ticketOrder = new TicketOrder();
        ticketOrder.setOrderCode("t20153111331196");
        ticketOrder.setPrice("200.00");
        ticketOrder.setQuantity(1);
        ticketOrder.setTotalPrice("200.00");
        ticketOrder.setOccDate(JaxbBase.SIMPLE_DATE_FORMAT.format(new Date()));
        ticketOrder.setGoodsCode("PST20160315008871");
        ticketOrder.setGoodsName("product");
        //ticketOrder.setGoodsName("商品");
        ticketOrder.setRemark("this is a test product.");
        order.setTicketOrder(ticketOrder);

        TicketOrder ticketOrder2 = new TicketOrder();
        ticketOrder2.setOrderCode("t20153111331196");
        ticketOrder2.setPrice("100.00");
        ticketOrder2.setQuantity(1);
        ticketOrder2.setTotalPrice("200.00");
        ticketOrder2.setOccDate(JaxbBase.SIMPLE_DATE_FORMAT.format(new Date()));
        ticketOrder2.setGoodsCode("PST20160315008871");
        ticketOrder2.setGoodsName("上品1");
        //ticketOrder.setGoodsName("商品");
        ticketOrder.setRemark("this is a test product.");
        order.setTicketOrder(ticketOrder2);


        orderRequest.setOrder(order);

        OrderResponse orderResponse = client.execute(orderRequest);
        assert orderResponse != null;
        System.out.print(orderResponse.getCode());
        System.out.print(orderResponse.toString());
    }

    @Test
    public void testCancelOrder() throws Exception {
        Client client = new PwbClientImpl();
        OrderRequest cancelRequest = new OrderRequest();
        Order order = new Order();
        cancelRequest.setTransactionName("SEND_CODE_CANCEL_NEW_REQ");
        order.setOrderCode("t20153111331196");
        cancelRequest.setOrder(order);

        OrderResponse cancelResponse = client.execute(cancelRequest);
        assert cancelResponse != null;
        System.out.print(cancelResponse.toString());
    }

    @Test
    public void testImg() throws Exception {
        Client client = new PwbClientImpl();
        ImgRequest imgRequest = new ImgRequest();
        Order order = new Order();
        order.setOrderCode("912944341133_118");
        imgRequest.setOrder(order);

        ImgResponse imgResponse = client.execute(imgRequest);
        assert imgResponse != null;
        System.out.print(imgResponse.getCode());
        System.out.print(imgResponse.toXml());

    }
}
