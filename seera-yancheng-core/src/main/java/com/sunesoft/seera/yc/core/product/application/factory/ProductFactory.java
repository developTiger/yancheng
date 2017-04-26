package com.sunesoft.seera.yc.core.product.application.factory;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.Product;
import com.sunesoft.seera.yc.core.product.domain.ProductItem;
import com.sunesoft.seera.yc.core.product.domain.ProductItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/12.
 */
public class ProductFactory extends Factory {

    public static PagedResult<ProductDto> convert(PagedResult<Product> pagedResult) {
        List<ProductDto> dtoList = new ArrayList<>();
        if (pagedResult.getTotalItemsCount() > 0) {
            pagedResult.getItems().stream().forEach(i -> {
                ProductDto dto = convert(i);
                setTypeString(dto);
                dtoList.add(dto);
            });
        }
        return new PagedResult<>(
                dtoList, pagedResult.getPageNumber(), pagedResult.getPageSize(), pagedResult.getTotalItemsCount());

    }

    public static PagedResult<ProductSimpleDto> convertToSimple(PagedResult<Product> pagedResult) {
        List<ProductSimpleDto> dtoList = new ArrayList<>();
        if (pagedResult.getTotalItemsCount() > 0) {
            pagedResult.getItems().stream().forEach(i -> {
                ProductSimpleDto dto = convert(i, ProductSimpleDto.class);
                dtoList.add(dto);
            });
        }
        return new PagedResult<>(
                dtoList, pagedResult.getPageNumber(), pagedResult.getPageSize(), pagedResult.getTotalItemsCount());

    }

    public static List<ProductSimpleDto> convertToSimple(List<Product> products) {
        List<ProductSimpleDto> dtoList = new ArrayList<>();
        if (null != products)
            products.stream().forEach(i -> {
                ProductSimpleDto dto = convert(i, ProductSimpleDto.class);
                dtoList.add(dto);
            });
        return dtoList;
    }

    /**
     * dto to entity
     * <p>用于转化的Product不支持ProductItem的转化</p>
     *
     * @param dto
     * @return
     */
    public static Product convert(ProductDto dto) {
        Product product = convert(dto, Product.class);
        if (dto.getDetailPicturesPaths() != null && dto.getDetailPicturesPaths().size() > 0)
            product.setDetailPicturesPaths(dto.getDetailPicturesPaths());
        if (dto.getIdAndCount() != null && dto.getIdAndCount().size() > 0)
            product.setProductItemWithCountMap(dto.getIdAndCount());
        product.setProductItemLists(convert(dto.getProductItemDtoList(), ProductItem.class), dto.getIdAndCount());
        product.setDiscountPrice(dto.getDiscountPrice());
        return product;
    }

    /**
     * 将ProductDto convert Product
     *
     * @param dto
     * @param product
     * @return
     */
    public static Product convert(ProductDto dto, Product product) {
        product = DtoFactory.convert(dto, product);
        if (dto.getDetailPicturesPaths() != null && dto.getDetailPicturesPaths().size() > 0)
            product.setDetailPicturesPaths(dto.getDetailPicturesPaths());
        if (dto.getIdAndCount() != null && dto.getIdAndCount().size() > 0)
            product.setProductItemWithCountMap(dto.getIdAndCount());
        return product;
    }


    public static ProductDto convert(Product product) {
        ProductDto dto = convert(product, ProductDto.class);
        List<ProductItemDto> itemDtos = convert(product.getProductItemList(), ProductItemDto.class);
        dto.setProductItemDtoList(itemDtos);
        dto.setDetailPicturesPaths(product.getDetailPicturesPaths());
        dto.setIdAndCount(product.getProductItemWithCountMap());
        dto.setHasHotel(product.getProductItemList().stream().anyMatch(j -> (j.getProductItemType() != null && j.getProductItemType().equals(ProductItemType.Hotel))));
        setTypeString(dto);
        return dto;
    }

    /**
     * 设置ProductDto里的TypeString
     *
     * @param dto
     */
    private static void setTypeString(ProductDto dto) {
        String s = "";
        List<ProductItemDto> list = dto.getProductItemDtoList();
        if (dto != null && list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getProductItemType() != null)
                    if (!s.contains(list.get(i).getProductItemType().toString()))
                        s += "+" + getChinese(list.get(i).getProductItemType().toString());
            }
            if (!s.equals(""))
                dto.setTypeString(s.substring(1));
        }
    }

    /**
     * 将类型转化为中文
     *
     * @param s
     * @return
     */
    private static String getChinese(String s) {
        String s1 = "";
        switch (s) {
            case "Ticket":
                s1 = "门票";
                break;
            case "Catering":
                s1 = "餐饮";
                break;
            case "Souvenirs":
                s1 = "纪念品";
                break;
            case "Hotel":
                s1 = "酒店";
                break;
            case "Other":
                s1 = "其他";
                break;
            default:
                s1 = "";
                break;
        }
        return s1;
    }
}
