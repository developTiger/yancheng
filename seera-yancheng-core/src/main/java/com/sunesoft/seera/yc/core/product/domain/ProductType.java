package com.sunesoft.seera.yc.core.product.domain;

/**
 * 商品类型枚举
 * Created by zhaowy on 2016/7/11.
 */
public enum ProductType {

    /**
     * 门票
     */
    Ticket,

    /**
     * 餐饮
     */
    Catering,

    /**
     * 纪念品
     */
    Souvenirs,

    /**
     * 酒店
     */
    Hotel,

    /**
     * 组合商品
     */
    GroupProduct,

    /**
     * 其他
     */
    Other;
//
//    /*
//     * 门票
//      */
//    Ticket("门票", 0),
//
//    /*
//    * * 餐饮
//     */
//    Catering("餐饮", 1),
//
//    /*
//     * 纪念品
//     */
//    Souvenirs("纪念品", 2),
//
//    /*
//     ** 酒店
//      */
//    Hotel("酒店", 3),
//
//    /*
//      *组合商品
//       */
//    GroupProduct("组合商品", 4),
//
//    /*
//    * 其他
//     */
//    Other("其他", 5);
//
//    /**
//     * 状态
//     */
//    private String typeName;
//
//    /**
//     * 值
//     */
//    private Integer typeValue;
//
//    private ProductType(String typeName, Integer typeValue) {
//        this.typeName = typeName;
//        this.typeValue = typeValue;
//    }
//
//    @Override
//    public String toString() {
//        return String.valueOf(this.typeName);
//    }
//
//    public Integer toValue() {
//        return this.typeValue;
//    }
}
