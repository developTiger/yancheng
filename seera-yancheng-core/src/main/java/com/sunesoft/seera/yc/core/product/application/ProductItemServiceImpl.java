package com.sunesoft.seera.yc.core.product.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemSimpleDto;
import com.sunesoft.seera.yc.core.product.application.factory.ProductFactory;
import com.sunesoft.seera.yc.core.product.domain.IProductItemRepository;
import com.sunesoft.seera.yc.core.product.domain.ProductItem;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductItemCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品项服务实现类
 * Created by zhaowy on 2016/7/11.
 */
@Service("iProductItemService")
public class ProductItemServiceImpl
        extends GenericHibernateFinder implements IProductItemService {

    @Autowired
    private IProductItemRepository repository;

    /**
     * @param id 商品项标识
     * @return 商品项详细信息
     */
    @Override
    public ProductItemDto get(Long id) {
        ProductItem item = repository.get(id);
        if (null == item) return null;
        return Factory.convert(item, ProductItemDto.class);
    }

    /**
     * @param num 商品项编码
     * @return 商品项详细信息
     */
    @Override
    public ProductItemDto get(String num) {
        ProductItem item = repository.GetByNum(num);
        return item == null ? null : ProductFactory.convert(item, ProductItemDto.class);
    }

    /**
     * @param id 商品项标识
     * @return 商品项简要信息
     */
    @Override
    public ProductItemSimpleDto getSimple(Long id) {
        ProductItem item = repository.get(id);
        return null == item ? null : ProductFactory.convert(repository.get(id), ProductItemSimpleDto.class);
    }

    /**
     * @param num 商品项编码
     * @return 商品项简要信息
     */
    @Override
    public ProductItemSimpleDto getSimple(String num) {
        ProductItem item = repository.GetByNum(num);
        return null == item ? null : ProductFactory.convert(item, ProductItemSimpleDto.class);
    }

    /**
     * 新增商品项
     *
     * @param productItem 商品项信息
     */
    @Override
    public CommonResult create(ProductItemDto productItem) {
        if (get(productItem.getNum()) != null) return new CommonResult(false, "该商品项已经存在，只要增加库存即可");
        try {
            if (productItem.getProductItemType() == null) return ResultFactory.commonError("请选择类型");
            ProductItem item = ProductFactory.convert(productItem, ProductItem.class);
            //TODO Exist item check
            if (null != repository.GetByNum(productItem.getNum()))
                return ResultFactory.commonError(productItem.getNum() + "is exist!");
            return ResultFactory.commonSuccess(repository.save(item));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 编辑商品项
     *
     * @param productItem 确保ProductItemDto中id正确
     */
    @Override
    public CommonResult edit(ProductItemSimpleDto productItem) {
        try {
            ProductItem item = ProductFactory.convert(productItem, ProductItem.class);
            return ResultFactory.commonSuccess(repository.save(item));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    @Override
    public CommonResult edit(ProductItemDto productItem) {
        try {
            ProductItem item = ProductFactory.convert(productItem, ProductItem.class);
            return ResultFactory.commonSuccess(repository.save(item));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 根据标识移除商品项
     *
     * @param id
     */
    @Override
    public CommonResult remove(Long id) {
        try {
            ProductItem item = repository.get(id);
            item.setIsActive(false);
            repository.save(item);
            return ResultFactory.commonSuccess();
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 根据商品项标识批量移除商品项
     *
     * @param ids
     */
    @Override
    public CommonResult remove(List<Long> ids) {
        try {
            ids.stream().forEach(x -> remove(x));
            return ResultFactory.commonSuccess();
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 查询商品项
     *
     * @param criteria
     * @return 商品项简要信息集合
     */
    @Override
    public PagedResult<ProductItemSimpleDto> findProductItemsSimple(ProductItemCriteria criteria) {
        PagedResult<ProductItem> pagedResult = repository.findProductsItems(criteria);
        return ProductFactory.convert(pagedResult, ProductItemSimpleDto.class);
    }

    /**
     * 查询商品项
     *
     * @param criteria
     * @return 商品项详细信息集合
     */
    @Override
    public PagedResult<ProductItemDto> findProductItems(ProductItemCriteria criteria) {
        PagedResult<ProductItem> pagedResult = repository.findProductsItems(criteria);
        return ProductFactory.convert(pagedResult, ProductItemDto.class);
    }

    /**
     * 获取所有商品项
     *
     * @return
     */
    @Override
    public List<ProductItemSimpleDto> getProductItemSimpleDtos() {
        Criteria criterion = getSession().createCriteria(ProductItem.class);
        criterion.add(Restrictions.eq("isActive", true));
        List<ProductItem> list = criterion.list();
        if (list != null && list.size() > 0) return ProductFactory.convert(list, ProductItemSimpleDto.class);
        return null;
    }

    /**
     * 获取所有商品项
     *
     * @return
     */
    @Override
    public List<ProductItemDto> getProductItemDtos() {
        Criteria criterion = getSession().createCriteria(ProductItem.class);
        criterion.add(Restrictions.eq("isActive", true));
        List<ProductItem> list = criterion.list();
        if (list != null && list.size() > 0) return ProductFactory.convert(list, ProductItemDto.class);
        return null;
    }
}
