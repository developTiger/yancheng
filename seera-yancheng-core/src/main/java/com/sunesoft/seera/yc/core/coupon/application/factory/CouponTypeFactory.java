package com.sunesoft.seera.yc.core.coupon.application.factory;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponTypeDto;
import com.sunesoft.seera.yc.core.coupon.domain.CouponType;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiazl on 2016/8/31.
 */
public class CouponTypeFactory extends Factory {

    /**
     * 将 CouponType -》 CouponTypeDto
     */
    public static CouponTypeDto convert(CouponType couponType){
      return convert(couponType,CouponTypeDto.class);
    }

    /**
     * 将 CouponTypes -》 CouponTypeDto
     */
    public static List<CouponTypeDto> convertListdto(List<CouponType> couponTypes){
        List<CouponTypeDto> couponTypeDtos=new ArrayList<>();
        if(couponTypes==null||couponTypes.size()==0)return couponTypeDtos;
        couponTypes.stream().forEach(i->couponTypeDtos.add(convert(i)));
        return couponTypeDtos;
    }

    /**
     * 将PagedResult<CouponType> -》 PagedResult<CouponTypeDto>
     */
    public static PagedResult<CouponTypeDto> convertpg(PagedResult<CouponType> pg){
       if(pg==null) return null;
        return new PagedResult<CouponTypeDto>(convertListdto(pg.getItems()),pg.getPageNumber(),pg.getPageSize(),pg.getTotalItemsCount());

    }

    /**
     * 将 CouponTypeDto -》 CouponType 新增的时候用
     */
    public static CouponType convert(CouponTypeDto dto){
        return convert(dto,CouponType.class);
    }

    /**
     * 将 CouponTypeDto -》 CouponType 修改的时候用
     */
    public static CouponType convert(CouponTypeDto dto,CouponType couponType){
        return DtoFactory.convert(dto, couponType);
    }
    /**
     * 将 CouponTypes -》 CouponTypeDto
     */
    public static List<CouponType> convertList(List<CouponTypeDto> couponTypeDtos){
        List<CouponType> couponTypes=new ArrayList<>();
        if(couponTypeDtos==null||couponTypeDtos.size()==0)return couponTypes;
        couponTypes.stream().forEach(i->couponTypeDtos.add(convert(i)));
        return couponTypes;
    }
}
