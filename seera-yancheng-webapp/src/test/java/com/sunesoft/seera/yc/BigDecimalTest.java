package com.sunesoft.seera.yc;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by bing on 16/8/16.
 */
public class BigDecimalTest {

    @Test
    public void test() {
        BigDecimal one = new BigDecimal(1.0);
        BigDecimal two = new BigDecimal(2);
        BigDecimal three = new BigDecimal(0);
        System.out.println(three.add(one.multiply(two)).add(new BigDecimal(4)));
    }

}
