package com.sunesoft.seera.yc.core.product.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.order.domain.IOrderRepository;
import com.sunesoft.seera.yc.core.order.domain.OrderInfo;
import com.sunesoft.seera.yc.core.order.domain.OrderStatus;
import com.sunesoft.seera.yc.core.product.application.dtos.FeedBackDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.application.factory.FeedBackFactory;
import com.sunesoft.seera.yc.core.product.application.factory.ProductFactory;
import com.sunesoft.seera.yc.core.product.domain.*;
import com.sunesoft.seera.yc.core.product.domain.criteria.FeedBackCriteria;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import com.sunesoft.seera.yc.core.shoppingCar.application.IShoppingCarService;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.domain.TouristStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 商品服务实现类
 * Created by zhaowy on 2016/7/11.
 */
@Service("iProductService")
public class ProductServiceImpl
        extends GenericHibernateFinder implements IProductService {

    @Autowired
    private IProductRepository repository;

    @Autowired
    private IProductItemRepository itemRepository;

    @Autowired
    private IOrderRepository iOrderRepository;


    @Autowired
    private IFeedBackRepository feedBackRepository;
    @Autowired
    private IProductItemService itemService;
    @Autowired
    private ITouristService touristService;
    @Autowired
    IShoppingCarService shoppingCarService;

    @Autowired
    IOrderService iOrderService;


    /**
     * 新增商品
     *
     * @param dto 商品dto
     */
    @Override
    public CommonResult create(ProductDto dto) {
        if (dto.getIdAndCount() == null || dto.getIdAndCount().size() == 0) return ResultFactory.commonError("添加商品项");
        if (repository.check(dto.getName())) return ResultFactory.commonError("该商品名称已经存在");
        List<ProductItemDto> list = getListByProduct(dto.getIdAndCount());
        if (list == null || list.size() == 0) return ResultFactory.commonError("添加的商品项个数存在异议");
        dto.setProductItemDtoList(list);
        Map<Long, Integer> map = dto.getIdAndCount();
        Set<Long> set = map.keySet();
        for (Iterator<Long> it = set.iterator(); it.hasNext(); ) {
            Long itemId = it.next();
            ProductItem item = itemRepository.get(itemId);
            if (item == null || !item.getIsActive()) return ResultFactory.commonError(item.getName() + "不存在");
        }

        try {
            Product product = ProductFactory.convert(dto);

            //TODO Obj Check
            return ResultFactory.commonSuccess(repository.save(product));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * //xzl overwrite
     * 新增商品项
     *
     * @param productId 商品标识
     * @param itemId    新增商品项标识
     * @param count     新增商品项数量
     * @return
     */
    @Override
    public CommonResult addProductItem(Long productId, Long itemId, int count) {

        Product product = repository.get(productId);
        ProductItem item = itemRepository.get(itemId);
        if (count < 1 || item == null) {
            return ResultFactory.commonError("请添加合理的数量,或者该产品项不存在");
        }
        product.addProductItemLists(item, count);
        product.setLastUpdateTime(new Date());
        repository.save(product);
        return ResultFactory.commonSuccess();
    }

    /**
     * 重置商品项集合
     *
     * @param productId 商品标识
     * @param map       重置商品项标识集合
     * @return
     */
    @Override
    public CommonResult resetProductItem(Long productId, Map<Long, Integer> map, BigDecimal dicprice) {
        try {
            Product product = repository.get(productId);
            if (product == null || !product.getIsActive()) {
                return ResultFactory.commonError("该商品不存在");
            }
            List<ProductItem> items = new ArrayList<>();
            Set<Long> set = map.keySet();
            for (Iterator<Long> it = set.iterator(); it.hasNext(); ) {
                Long id = it.next();
                ProductItem item = itemRepository.get(id);
                if (item == null || !item.getIsActive()) {
                    return ResultFactory.commonError(item.getName() + "商品项不存在");
                } else {
                    items.add(item);
                }
            }
            product.setProductItemLists(items, map);
            product.setLastUpdateTime(new Date());
            product.setDiscountPrice(dicprice);
            return ResultFactory.commonSuccess(repository.save(product));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }


    /**
     * 设置商品销售类型
     *
     * @param id   商品标识
     * @param kind 商品状态
     */
    public CommonResult setProductKind(Long id, ProductKind kind) {
        try {
            Product t = repository.get(id);
            if (null == t || !t.getIsActive()) return ResultFactory.commonError(id + " Product don't exist");
            t.setKind(kind);
            t.setLastUpdateTime(new Date());
            return ResultFactory.commonSuccess(repository.save(t));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 批量设置商品销售类型
     *
     * @param ids  商品标识
     * @param kind 商品状态
     */
    public CommonResult setProductKind(List<Long> ids, ProductKind kind) {
        Criteria criteria = getSession().createCriteria(Product.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.in("id", ids));
        List<Product> products = criteria.list();
        if (products == null || products.size() == 0) return ResultFactory.commonError("请选中商品");
        try {
            products.stream().forEach(i -> {
                i.setKind(kind);
                i.setLastUpdateTime(new Date());
                repository.save(i);
            });
            return ResultFactory.commonSuccess();
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 设置商品状态
     *
     * @param id     商品标识
     * @param status 商品状态
     */
    @Override
    public CommonResult setProductStatus(Long id, ProductStatus status) {
        try {
            Product t = repository.get(id);
            if (null == t || !t.getIsActive()) return ResultFactory.commonError(id + " Product don't exist");
            t.setStatus(status);
            if (status.equals(ProductStatus.OnSale))
                t.setOnSaleTime(new Date());

            if (status.equals(ProductStatus.Stoped)) {
                //该商品下架，购物车的该商品也将都失效
                shoppingCarService.setShoppingItemInvalid(t.getId());
                //所有包含该商品的订单都将失效。。
                iOrderService.setOrderStatusByProductStatus(t.getId());

            }
            t.setLastUpdateTime(new Date());

            return ResultFactory.commonSuccess(repository.save(t));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 批量设置商品状态
     *
     * @param ids    商品标识集合
     * @param status 商品状态
     */
    @Override
    public CommonResult setProductStatus(List<Long> ids, ProductStatus status) {
        Criteria criteria = getSession().createCriteria(Product.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.in("id", ids));
        List<Product> products = criteria.list();
        if (products == null || products.size() == 0) return ResultFactory.commonError("请选中商品");
        try {
            products.stream().forEach(i -> {
                i.setStatus(status);
                //设置上架时间
                if (status.equals(ProductStatus.OnSale))
                    i.setOnSaleTime(new Date());
                //该商品下架，购物车的该商品也将都失效
                if (status.equals(ProductStatus.Stoped))
                    shoppingCarService.setShoppingItemInvalid(i.getId());
                i.setLastUpdateTime(new Date());
                repository.save(i);
            });
            return ResultFactory.commonSuccess();
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }


    /**
     * 移除商品
     *
     * @param id
     */
    @Override
    public CommonResult remove(Long id) {
        try {
            Product product = repository.get(id);
            if (null == product || !product.getIsActive()) return ResultFactory.commonError(id + " don't exist!");
            product.setIsActive(false);
            repository.save(product);
            shoppingCarService.setShoppingItemInvalid(product.getId());
            return ResultFactory.commonSuccess();
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 批量移除商品
     *
     * @param ids
     */
    @Override
    public CommonResult remove(List<Long> ids) {
        Criteria criterion = getSession().createCriteria(Product.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.in("id", ids));
        List<Product> list = criterion.list();
        try {
            list.stream().forEach(i -> {
                i.setIsActive(false);
                repository.save(i);
                shoppingCarService.setShoppingItemInvalid(i.getId());
            });
            return ResultFactory.commonSuccess();
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 更新商品
     *
     * @param dto 商品信息
     *            测试一下
     */
    @Override
    public CommonResult edit(ProductDto dto) {

        if (dto.getIdAndCount() == null || dto.getIdAndCount().size() == 0)
            return ResultFactory.commonError("没有商品项的商品无意义");
        Product p = repository.get(dto.getId());
        if (!dto.getName().equals(p.getName()) && repository.check(dto.getName()))
            return ResultFactory.commonError("修改后的商品名称已经存在");
        List<ProductItem> newItems = new ArrayList<>();
        dto.getIdAndCount().keySet().forEach(i ->
        {
            ProductItem item = itemRepository.get(i);
            newItems.add(item);
        });
        try {
            Product product = repository.get(dto.getId());
            product = ProductFactory.convert(dto, product);
            product.setProductItemLists(newItems, dto.getIdAndCount());
            product.setDiscountPrice(dto.getDiscountPrice());//重写销售价

            product.setLastUpdateTime(new Date());
            if (dto.getProductCt() == ProductCT.Nomal) {
                product.setCtEndDate(null);
                product.setCtBeginDate(null);
            }
            return ResultFactory.commonSuccess(repository.save(product));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }

//        if (dto.getIdAndCount() == null || dto.getIdAndCount().size() == 0)
//            return ResultFactory.commonError("没有商品项的商品无意义");
//        dto.setProductItemDtoList(getListByProduct(dto.getIdAndCount()));
//        Product p = repository.get(dto.getId());
//        if (p == null || !p.getIsActive()) return ResultFactory.commonError(p.getName() + " not exist!");
//        try {
//            p = ProductFactory.convert(dto, p);
//            //TODO Obj check
//            p.setLastUpdateTime(new Date());
//            repository.save(p);
//            return ResultFactory.commonSuccess();
//        } catch (Exception ex) {
//            return ResultFactory.commonError(ex.getMessage());
//        }
    }

    /**
     * 获取商品信息
     *
     * @param productId 商品标识
     * @return dto or NULL
     */
    @Override
    public ProductDto get(long productId) {
        Product p = repository.get(productId);
        if (p == null || !p.getIsActive()) {
            return null;
        }
        ProductDto dto=ProductFactory.convert(p);
        dto.setRejectAreasNames(editRejectAreasNames(p.getRejectAreas()));
        return dto;
    }

    /**
     * 获取商品信息
     *
     * @param num 商品编码
     * @return
     */
    @Override
    public ProductDto get(String num) {
        Product product = repository.getByNum(num);
        if (product == null || !product.getIsActive()) {
            return null;
        }
        ProductDto dto=ProductFactory.convert(product);
        dto.setRejectAreasNames(editRejectAreasNames(product.getRejectAreas()));
        return dto;
    }

    /**
     * 获取商品信息
     *
     * @param productId 商品标识
     * @return dto or NULL
     */
    @Override
    public ProductSimpleDto getSimple(Long productId) {
        Product p = repository.get(productId);
        if (p == null || !p.getIsActive()) {
            return null;
        }
        ProductSimpleDto dto=ProductFactory.convert(p);
        dto.setRejectAreasNames(editRejectAreasNames(p.getRejectAreas()));
        return dto;
    }


    /**
     * 获取商品信息
     *
     * @param num 商品编码
     * @return dto or NULL
     */
    @Override
    public ProductSimpleDto getSimple(String num) {
        Product item = repository.getByNum(num);
        if (item == null || !item.getIsActive()) {
            return null;
        }
        ProductSimpleDto dto= ProductFactory.convert(item, ProductSimpleDto.class);
        dto.setRejectAreasNames(editRejectAreasNames(item.getRejectAreas()));
        return dto
                ;
    }

    /**
     * 增加商品库存量
     *
     * @param productId
     * @param count
     * @return
     */
    @Override
    public CommonResult increaseStock(Long productId, int count) {
        if (productId == null || productId < 1)
            return ResultFactory.commonError("参数异常");
        Product product = repository.get(productId);
        if (product != null && product.increaseProductStock(count))
            return ResultFactory.commonSuccess(repository.save(product));
        return ResultFactory.commonError("指定标识的商品不存在");
    }

    /**
     * 减少商品库存量
     *
     * @param productId
     * @param count
     * @return
     */
    @Override
    public CommonResult reduceStock(Long productId, int count) {
        if (productId == null || productId < 1)
            return ResultFactory.commonError("参数异常");
        Product product = repository.get(productId);
        if (product != null && product.reduceProductStock(count))
            return ResultFactory.commonSuccess(repository.save(product));
        return ResultFactory.commonError("指定标识的商品不存在或库存已不足");
    }

    /**
     * 查询商品
     *
     * @param criteria
     * @return 商品简要信息集合
     */
    @Override
    public PagedResult<ProductSimpleDto> findProductsSimple(ProductCriteria criteria) {
        PagedResult<Product> pagedResult = repository.findProducts(criteria);
        //检查该商品的商品项是否存在，否则设置为下架
        List<Product> list = pagedResult.getItems();
        //切记，不要通过查询对数据做任何写操作，这个是职责分离，
        //        List<Product> delist = new ArrayList<>();
        //        for (Product p : list) {
        //            if (p.getProductItemList() == null || p.getProductItemList().size() == 0) {
        //                setProductStatus(p.getId(), ProductStatus.Stoped);
        //                p.setProductItemWithCountMap(Collections.EMPTY_MAP);
        //                delist.add(p);
        //
        //            }
        //        }

        //        list.removeAll(delist);
        PagedResult<Product> pagedResult1 = new PagedResult<>(
                list, pagedResult.getPageNumber(), pagedResult.getPageSize(), pagedResult.getTotalItemsCount());
        return ProductFactory.convertToSimple(pagedResult1);
    }

    /**
     * 查询商品
     *
     * @param criteria
     * @return 商品详细信息集合
     */
    @Override
    public PagedResult<ProductDto> findProducts(ProductCriteria criteria) {
        PagedResult<Product> pagedResult = repository.findProducts(criteria);
        PagedResult<ProductDto> pg=ProductFactory.convert(pagedResult);
        if(pg.getItems().size()>0){
            for(ProductDto dto:pg.getItems()){
                dto.setRejectAreasNames(editRejectAreasNames(dto.getRejectAreas()));
            }
        }
        return new PagedResult<ProductDto>(pg.getItems(),pg.getPageNumber(),pg.getPageSize(),pg.getTotalItemsCount());
    }

    /**
     * 查询包含指定商品项标识的所有商品
     *
     * @param productItemId
     * @return
     */
    public List<ProductSimpleDto> getProductWithItemId(Long productItemId) {
        List<Product> list = repository.getProductWithItemId(productItemId);
        if (null != list && !list.isEmpty())
            return ProductFactory.convertToSimple(list);
        return null;
    }

    /**
     * 从map中获取产品里的产品项
     */
    private List<ProductItemDto> getListByProduct(Map<Long, Integer> map) {
        List<ProductItemDto> list = new ArrayList<>();
        if (map == null || map.size() == 0) {
            return null;
        }
        Set<Long> set = map.keySet();
        for (Iterator<Long> it = set.iterator(); it.hasNext(); ) {
            Long id = it.next();
            Integer count = map.get(id);
            //存在<=的数量的商品项，添加无意义
            if (count <= 0) return null;
            ProductItem item = itemRepository.get(id);
            ProductItemDto itemDto = ProductFactory.convert(item, ProductItemDto.class);
            //不存在的商品项，添加无意义
            if (itemDto == null) return null;
            list.add(itemDto);
        }
        return list;
    }

    /**
     * 新增商品评论
     *
     * @param dto 评论信息
     * @return
     */
    @Override
    public CommonResult addProductFeedBack(FeedBackDto dto) {

        //检测游客是否存在，是否禁用
        TouristDto touristDto = touristService.getTourist(dto.getTouristId()).getT();
        if (touristDto == null) return ResultFactory.commonError("该游客不存在");
        dto.setTouristRealName(touristDto.getRealName());
        if (touristDto.getStatus().equals(TouristStatus.Forbidden)) return ResultFactory.commonError("该游客被禁止");

        ProductDto productDto = get(dto.getNum());
        //检测该商品是否存在
        if (productDto == null) return ResultFactory.commonError("该商品不存在");
//        if(productDto.getStatus().equals(ProductStatus.Stoped)) return ResultFactory.commonError("该商品已经停售");
        dto.setProductId(productDto.getId());
        FeedBack feedBack = FeedBackFactory.convert(dto, FeedBack.class);
        return ResultFactory.commonSuccess(feedBackRepository.save(feedBack));
    }

    /**
     * 批量新增商品评论
     *
     * @param dtos 评论信息
     * @return
     */
    @Override
    public CommonResult addProductFeedBack(List<FeedBackDto> dtos, String orderNum) {

        //检测游客是否存在，是否禁用
        for (FeedBackDto dto : dtos) {
            TouristDto touristDto = touristService.getTourist(dto.getTouristId()).getT();
            if (touristDto == null) return ResultFactory.commonError("该游客不存在");
            dto.setTouristRealName(touristDto.getRealName());
            if (touristDto.getStatus().equals(TouristStatus.Forbidden)) return ResultFactory.commonError("该游客被禁止");

            ProductDto productDto = get(dto.getNum());
            //检测该商品是否存在
            if (productDto == null) return ResultFactory.commonError("该商品不存在");
//        if(productDto.getStatus().equals(ProductStatus.Stoped)) return ResultFactory.commonError("该商品已经停售");
            dto.setProductId(productDto.getId());
            FeedBack feedBack = FeedBackFactory.convert(dto, FeedBack.class);
            feedBackRepository.save(feedBack);
        }
        /**
         * 更改订单状态
         */
        OrderInfo orderInfo = iOrderRepository.get(orderNum);
        orderInfo.setStatus(OrderStatus.end);
        iOrderRepository.save(orderInfo);
        return ResultFactory.commonSuccess();
    }

    /**
     * 删除商品评论
     *
     * @param feedBackId 评论标识
     * @return
     */
    @Override
    public CommonResult removeProductFeedBack(Long feedBackId) {
        feedBackRepository.delete(feedBackId);
        return ResultFactory.commonSuccess();
    }

    /**
     * 获取商品评论信息
     *
     * @param criteria 评论查询条件
     * @return
     */
    @Override
    public PagedResult<FeedBackDto> getProductFeeBacks(FeedBackCriteria criteria) {

        PagedResult<FeedBack> feedBacks = feedBackRepository.findFeedBacks(criteria);

        return FeedBackFactory.convert(feedBacks, FeedBackDto.class);
    }

    @Override
    public Map<Integer, String> getAllProvices() {
        Map<Integer, String> map = new HashMap<>();
        map.put(11,"北京市");
        map.put(12,"天津市");
        map.put(13,"河北省");
        map.put(14,"山西省");
        map.put(15,"内蒙古自治区");
        map.put(21,"辽宁省");
        map.put(22,"吉林省");
        map.put(23,"黑龙江省");
        map.put(31,"上海市");
        map.put(32,"江苏省");
        map.put(33,"浙江省");
        map.put(34,"安徽省");
        map.put(35,"福建省");
        map.put(36,"江西省");
        map.put(37,"山东省");
        map.put(41,"河南省");
        map.put(42,"湖北省");
        map.put(43,"湖南省");
        map.put(44,"广东省");
        map.put(45,"广西壮族自治区");
        map.put(46,"海南省");
        map.put(50,"重庆市");
        map.put(51,"四川省");
        map.put(52,"贵州省");
        map.put(53,"云南省");
        map.put(54,"西藏自治区");
        map.put(61,"陕西省");
        map.put(62,"甘肃省");
        map.put(63,"青海省");
        map.put(64,"宁夏回族自治区");
        map.put(65,"新疆维吾尔族自治区省");
        map.put(71,"台湾省");
        map.put(81,"香港特别行政区");
        map.put(91,"澳门特别行政区");
        return map;
    }
    /**
     * 获取省份名称
     * @param keys
     * @return
     */
    @Override
    public String editRejectAreasNames(String keys){
        StringBuffer result=new StringBuffer();
        Map<Integer,String> map=getAllProvices();
        if(!StringUtils.isNullOrWhiteSpace(keys)){
            String[] strings=keys.split(",");
            for(String str:strings){
                String name=map.get(Integer.parseInt(str));
                result.append(",").append(name);
            }
        }
        if(result.length()>0){
            return result.substring(1);
        }
        return "";
    }

}
