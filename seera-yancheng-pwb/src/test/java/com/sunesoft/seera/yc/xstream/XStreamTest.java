package com.sunesoft.seera.yc.xstream;

import com.sunesoft.seera.yc.obj.Customer;
import com.sunesoft.seera.yc.pwb.OrderRequest;
import com.sunesoft.seera.yc.pwb.OrderResponse;
import com.sunesoft.seera.yc.pwb.common.Header;
import com.sunesoft.seera.yc.pwb.common.IdentityInfo;
import com.sunesoft.seera.yc.pwb.order.Order;
import com.sunesoft.seera.yc.pwb.order.TicketOrder;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

/**
 * Created by bing on 16/8/1.
 */
public class XStreamTest {

    @Test
    public void test() {
        Order order = new Order();
        XStream xStream = new XStream();
        System.out.println(xStream.toXML(order));
    }

    @Test
    public void testJaxb() throws JAXBException {
        Customer customer = new Customer();
        customer.setName("test");
        customer.setAge("123");

        com.sunesoft.seera.yc.obj.Order order1 = new com.sunesoft.seera.yc.obj.Order();
        order1.setLinkName("ewrerqe");

        customer.addOrder(order1);

        JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        marshaller.marshal(customer, byteArrayOutputStream);
        String result = new String(byteArrayOutputStream.toByteArray());
        System.out.println(result);
    }

    @Test
    public void testOrderRequest() throws JAXBException {
        OrderRequest orderRequest = new OrderRequest();
        Header header = new Header();
        header.setApplication("SendCode");
        header.setRequestTime("2011-21-20");
        orderRequest.setHeader(header);
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setCorpCode("BCFXS");
        identityInfo.setUserName("ADMIN");
        orderRequest.setIdentityInfo(identityInfo);
        Order order = new Order();
        order.setLinkName("21231231");
        orderRequest.setOrder(order);

        TicketOrder ticketOrder = new TicketOrder();
        ticketOrder.setGoodsCode("12312312312");
        order.setTicketOrder(ticketOrder);

        String xml = orderRequest.toString();

        System.out.println(xml);

        orderRequest = OrderRequest.toObject(xml, OrderRequest.class);
        assert orderRequest != null;
    }

    @Test
    public void testOrderResponse() {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setDescription("成功");
        Order order = new Order();
        order.setLinkName("21231231");
        orderResponse.addOrder(order);

        TicketOrder ticketOrder = new TicketOrder();
        ticketOrder.setGoodsCode("12312312312");
        order.setTicketOrder(ticketOrder);

        String xml = orderResponse.toString();
        System.out.println(xml);
    }
}
