package com.sunesoft.seera.yc.core.findbackpassword.application.factory;

import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.coupon.application.factory.CouponFactory;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;

/**
 * Created by temp on 2016/10/13.
 */
public class FindBackPasswordFactory extends Factory {

    public static TouristDto conertToTouristDto(Tourist tourist) {
        TouristDto dto= Factory.convert(tourist, TouristDto.class);
        if(tourist.getBindCoupons()!=null&&tourist.getBindCoupons().size()>0){
            dto.setBindCouponDtos(CouponFactory.convert(tourist.getBindCoupons()));
        }
        return dto;
    }
}
