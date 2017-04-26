package com.sunesoft.seera.yc.core.product.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.fr.utils.NumGenerator;
import com.sunesoft.seera.fr.utils.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商品实体
 * Created by zhaowy on 2016/7/11.
 */
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    //region Construct

    public Product() {
        this.num = NumGenerator.generate("sp");
        this.status = ProductStatus.Stoped;
        this.price = BigDecimal.valueOf(0);
        this.saleCount = 0;
        this.setIsActive(true);
        this.setCreateDateTime(new Date());
        this.setLastUpdateTime(new Date());
        this.productItemList = new ArrayList<>();

    }

    public Product(String name, ProductType type, BigDecimal discountPrice) {
        this.setIsActive(true);
        this.setCreateDateTime(new Date());
        this.setLastUpdateTime(new Date());
        this.num = NumGenerator.generate("sp");
        this.status = ProductStatus.Stoped;
        this.name = name;
        this.type = type;
        this.discountPrice = discountPrice;
        this.productItemList = new ArrayList<>();
    }

    //endregion

    //region Filed
    /**
     * 商品项清单
     */
    @ManyToMany
    @JoinTable(name = "product_to_item", inverseJoinColumns = @JoinColumn(name = "item_id"),
            joinColumns = @JoinColumn(name = "product_id"))
    private List<ProductItem> productItemList;

    /**
     * 商品项数量结构
     * <p>eg: productItemA.id:count;productItemB.id:count</p>
     */
    private String productItemWithCount;

    /**
     * 商品状态
     */
    @Column(name = "product_status")
    private ProductStatus status;

    /**
     * 商品类型
     */
    private ProductType type;
    /**
     * 商品限购区域
     */
    private String rejectAreas;

    /**
     * 商品编码
     */
    private String num;

    /**
     * 商品销售类型
     */
    private ProductKind kind = ProductKind.Nomal;

    /**
     * 商品名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 特殊规格说明
     * 如适用时间，有效期限等特殊说明
     */
    private String specDescription;

    /**
     * 商品标价
     * 默认为组合商品项价格总和，可编辑
     */
    private BigDecimal price;

    /**
     * 折扣价
     */
    @Column(name = "discount_price")
    private BigDecimal discountPrice;

    /**
     * 商品介绍
     */
    @Column(columnDefinition = "text")
    private String profile;

    /**
     * 购买须知
     */
    @Column(columnDefinition = "text")
    private String notice;

    /**
     * 交通指南
     */
    @Column(columnDefinition = "text")
    private String trafficGuide;

    /**
     * 商品主图片相对路径
     */
    @Column(name = "main_picture_path")
    private String mainPicturePath;

    /**
     * 商品轮播图路径集合
     */
    @Column(name = "detail_pictures_path")
    private String detailPicturesPaths;

    /**
     * 可改签
     */
    @Column(name = "can_meal")
    private Boolean canMeal;

    /**
     * 可退
     */
    @Column(name = "can_return")
    private Boolean canReturn;

    /**
     * 商品库存量，定义时取决于商品项的最小库存量
     */
    private int stock;


    /**
     * 已售数量
     */
    private Integer saleCount;

    /**
     * 上架时间
     */
    @Column(name = "onSale_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date onSaleTime;


    /**
     * 票种 当天票 /区间票/永久票
     */
    private ProductCT productCt;


    /**
     * 区间票的时间区间
     */
    private Date ctBeginDate;


    /**
     *
     */
    private Date ctEndDate;

//endregion

    //region Property Get&Set


    public ProductCT getProductCt() {
        return productCt;
    }

    public void setProductCt(ProductCT productCt) {
        this.productCt = productCt;
    }

    public Date getCtBeginDate() {
        return ctBeginDate;
    }

    public void setCtBeginDate(Date ctBeginDate) {
        this.ctBeginDate = ctBeginDate;
    }

    public Date getCtEndDate() {
        return ctEndDate;
    }

    public void setCtEndDate(Date ctEndDate) {
        this.ctEndDate = ctEndDate;
    }

    public ProductKind getKind() {
        return kind;
    }


    public void setKind(ProductKind kind) {
        this.kind = kind;
    }

    public Date getOnSaleTime() {
        return onSaleTime;
    }

    public void setOnSaleTime(Date onSaleTime) {
        this.onSaleTime = onSaleTime;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
        //TODO 商品下架消除商品项库存占用
//        if (status.equals(ProductStatus.Stoped))
//            setStock(0);
        if (status.equals(ProductStatus.OnSale))
            setOnSaleTime(new Date());
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getTrafficGuide() {
        return trafficGuide;
    }

    public void setTrafficGuide(String trafficGuide) {
        this.trafficGuide = trafficGuide;
    }

    public String getMainPicturePath() {
        return mainPicturePath;
    }

    public void setMainPicturePath(String mainPicturePath) {
        this.mainPicturePath = mainPicturePath;
    }

    public Boolean getCanMeal() {
        return canMeal;
    }

    public void setCanMeal(Boolean canMeal) {
        this.canMeal = canMeal;
    }

    public Boolean getCanReturn() {
        return canReturn;
    }

    public void setCanReturn(Boolean canReturn) {
        this.canReturn = canReturn;
    }

    public List<ProductItem> getProductItemList() {
        return productItemList;
    }

    public String getRejectAreas() {
        return rejectAreas;
    }

    public void setRejectAreas(String rejectAreas) {
        this.rejectAreas = rejectAreas;
    }

    public void setNum(String num) {
        this.num = num;
    }

    private void setProductItemList(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
    }

    private void setProductItemWithCount(String productItemWithCount) {
        this.productItemWithCount = productItemWithCount;
    }


    private String getProductItemWithCount() {
        return productItemWithCount;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    //endregion

    //region Domain Action

    /**
     * 设置商品项
     *
     * @param map          商品项集合
     * @param productItems
     * @param map
     * @param map
     */
    public void setProductItemLists(List<ProductItem> productItems, Map<Long, Integer> map) {
        //初始化
        if (map == null || map.isEmpty()) {
            this.productItemList = new ArrayList<>();
            return;
        }
        BigDecimal price = BigDecimal.ZERO;
        for (ProductItem p : productItems) {
            if (map.get(p.getId()) <= 0) {
                productItems.remove(p);
                map.remove(p.getId());
            } else {
                price = price.add(p.getPrice().multiply(BigDecimal.valueOf(map.get(p.getId()))));
            }
        }
        setProductItemWithCountMap(map);
        setProductItemList(productItems);
        setPrice(price);
    }

    /**
     * 增加商品项
     *
     * @param productItem 增加的商品项
     * @param count       增加的商品数量
     */
    public void addProductItemLists(ProductItem productItem, Integer count) {
        Map<Long, Integer> map = getProductItemWithCountMap();
        this.price = this.price.add(productItem.getPrice().multiply(BigDecimal.valueOf(count)));
        this.discountPrice.add(productItem.getPrice().multiply(BigDecimal.valueOf(count)));
        if (this.productItemList.isEmpty() || !this.productItemList.contains(productItem)) {
            this.productItemList.add(productItem);
            String s = String.valueOf(productItem.getId()) + ":" + String.valueOf(count);
            setProductItemWithCount(this.productItemWithCount + ";" + s);
        } else if (this.productItemList.contains(productItem)) {
            String s1 = String.valueOf(productItem.getId()) + ":" + String.valueOf(map.get(productItem.getId()));
            String s2 = String.valueOf(productItem.getId()) + ":" + String.valueOf(map.get(productItem.getId()) + count);
            setProductItemWithCount(this.productItemWithCount.replace(s1, s2));
            return;
        }
    }


    /**
     * 移除商品项
     *
     * @param productItemId
     */
    public void removeProductItem(Long productItemId) {
        if (null != this.productItemList) {
            ProductItem item = productItemList.stream().filter(i -> i.getId() == productItemId).findFirst().get();
            productItemList.removeIf(i -> i.getId() == productItemId);
            Map<Long, Integer> map = getProductItemWithCountMap();
            if (map.containsKey(productItemId)) {
                map.remove(productItemId);
//                setProductItemWithCount(setProductItemWithCountMap(map));
                setProductItemWithCountMap(map);
                this.price.subtract(item.getPrice().multiply(BigDecimal.valueOf(map.get(productItemId))));
                this.discountPrice.subtract(item.getPrice().multiply(BigDecimal.valueOf(map.get(productItemId))));
            }

        }
    }

    /**
     * 消耗库存
     *
     * @param stock
     * @return
     */
    public Boolean reduceProductStock(int stock) {
        if (this.stock < stock || stock <= 0)
            return false;
        this.stock -= stock;
        if(this.saleCount== null)
            this.saleCount=0;
        this.saleCount += stock;
        return true;
    }

    /**
     * 增加库存
     *
     * @param stock
     * @return
     */
    public Boolean increaseProductStock(int stock) {
        if (stock <= 0)
            return false;
        this.stock += stock;
        if (this.saleCount != null && this.saleCount > stock)
            this.saleCount -= stock;
        else
            this.saleCount = 0;

        return true;
    }

    /**
     * 获取商品轮播图片地址集合
     *
     * @return
     */
    public List<String> getDetailPicturesPaths() {
        if (StringUtils.isNullOrWhiteSpace(this.detailPicturesPaths)) return null;
        return Arrays.asList(detailPicturesPaths.split(";"));
    }

    /**
     * 设置轮播图片地址
     *
     * @param detailPicturesPaths
     */
    public void setDetailPicturesPaths(List<String> detailPicturesPaths) {
        if (detailPicturesPaths != null && detailPicturesPaths.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < detailPicturesPaths.size(); i++) {
                sb.append(detailPicturesPaths.get(i)).append(';');
            }
            this.detailPicturesPaths = sb.toString().substring(0, sb.toString().length() - 1);
        }
    }


    /**
     * 获取商品下各商品项Id及数量的map
     *
     * @return
     */
    public Map<Long, Integer> getProductItemWithCountMap() {
        Map<Long, Integer> map = new HashMap<>();
        if (this.productItemList == null || this.productItemList.isEmpty()) {
            return map;
        }
        List<String> list = Arrays.asList(this.productItemWithCount.split(";"));
        list.stream().forEach(i ->
        {
            Long k = Long.parseLong(i.split(":")[0]);
            Integer v = Integer.parseInt(i.split(":")[1]);
            map.put(k, v);
        });
        return map;
    }

    /**
     * 根据传入的map 设定productItemWithCount字符串
     *
     * @param map
     */
    public String setProductItemWithCountMap(Map<Long, Integer> map) {
        String productItemWithCount = "";
        if (map == null || map.isEmpty()) {
            setProductItemWithCount(productItemWithCount);
            return productItemWithCount;
        } else {
            Set<Long> set = map.keySet();
            for (Iterator<Long> it = set.iterator(); it.hasNext(); ) {
                Long itemId = it.next();
                Integer itemCount = map.get(itemId);
                if (itemCount <= 0) continue;
                productItemWithCount += String.valueOf(itemId) + ":" + String.valueOf(itemCount) + ";";
            }
            productItemWithCount = productItemWithCount.substring(0, productItemWithCount.length() - 1);
            setProductItemWithCount(productItemWithCount);
            return productItemWithCount;
        }
    }

    /**
     * 根据给定的的itemId获取指定的ProductItem的数量
     *
     * @param itemId
     * @return
     */
    public Integer getProductItemCountById(Long itemId) {
        Map<Long, Integer> map = getProductItemWithCountMap();
        if (map == null || map.isEmpty() || !map.containsKey(itemId)) {
            return 0;
        } else {
            return map.get(itemId);
        }
    }

    /**
     * 重新设置ProductItem
     * 这里面不能传输itemId,否则新增时，原本不存在的情况下无法操作
     *
     * @param item
     * @param count
     */
    public void updateProductItemWithCount(ProductItem item, Integer count) {
        Map<Long, Integer> map = getProductItemWithCountMap();
        if (count < 0 || (count == 0 && !map.containsKey(item.getId()))) {
            return;
        }
        String s0 = String.valueOf(item.getId()) + ":" + String.valueOf(count);
        //增加的情况
        if (count == 0 && map.containsKey(item.getId())) {
            removeProductItem(item.getId());
            return;
        }
        if (count > 0) {
            if (map.containsKey(item.getId())) {
                String s = String.valueOf(item.getId()) + ":" + String.valueOf(map.get(item.getId()));
                setProductItemWithCount(this.productItemWithCount.replace(s, s0));
                setPrice(this.price.subtract(BigDecimal.valueOf(map.get(item.getId())).multiply(item.getPrice())).add(BigDecimal.valueOf(count)).multiply(item.getPrice()));
                setDiscountPrice(this.discountPrice.subtract(BigDecimal.valueOf(map.get(item.getId())).multiply(item.getPrice())).add(BigDecimal.valueOf(count)).multiply(item.getPrice()));
                return;
            } else {
                setPrice(this.price.add(BigDecimal.valueOf(count)).multiply(item.getPrice()));
                setDiscountPrice(this.discountPrice.add(BigDecimal.valueOf(count)).multiply(item.getPrice()));
                this.productItemList.add(item);

                setProductItemWithCount(this.productItemWithCount + ";" + s0);
            }
        }
    }

    public void setDetailPicturesPaths(String detailPicturesPaths) {
        this.detailPicturesPaths = detailPicturesPaths;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public String getSpecDescription() {
        return specDescription;
    }

    public void setSpecDescription(String specDescription) {
        this.specDescription = specDescription;
    }

    //endregion
}
