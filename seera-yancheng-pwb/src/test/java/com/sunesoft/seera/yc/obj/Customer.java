package com.sunesoft.seera.yc.obj;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bing on 16/8/4.
 */

@XmlRootElement
public class Customer {

    private String name;

    private String age;

    private List<Order> orders = new ArrayList<Order>();

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @XmlElementWrapper(name = "orderRequest")
    @XmlElement(name = "order")
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        if (this.orders == null)
            this.orders = new ArrayList<Order>();
        this.orders.add(order);
    }
}
