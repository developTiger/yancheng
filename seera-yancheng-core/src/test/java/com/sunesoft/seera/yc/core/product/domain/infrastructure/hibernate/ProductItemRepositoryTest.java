package com.sunesoft.seera.yc.core.product.domain.infrastructure.hibernate;

import com.sunesoft.seera.yc.core.product.domain.IProductItemRepository;
import com.sunesoft.seera.yc.core.product.domain.ProductItem;
import com.sunesoft.seera.yc.core.product.domain.ProductItemType;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductItemRepositoryTest extends TestCase {

    @Autowired
    private IProductItemRepository repository;

    @Test
    public void testGetByNum() throws Exception {
        ProductItem item = new ProductItem();
        item.setNum("No-" + UUID.randomUUID());
        item.setName("仓储票" + new Date().getTime());
        item.setPrice(BigDecimal.valueOf(190.00 + new Date().getTime() % 30));
        item.setProductItemType(ProductItemType.Ticket);
        item.setSeller("自营");
        repository.save(item);


    }

    public void testFindProductsItems() throws Exception {

    }
}