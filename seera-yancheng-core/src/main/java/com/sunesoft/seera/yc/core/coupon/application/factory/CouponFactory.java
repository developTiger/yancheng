package com.sunesoft.seera.yc.core.coupon.application.factory;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.BeanUtils;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.Coupon;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xiazl on 2016/7/28.
 */
public class CouponFactory extends Factory {


    public static CouponDto convert(Coupon coupon) {
        CouponDto dto = convert(coupon,CouponDto.class);
        dto.setGqDate(DateHelper.formatDate(coupon.getExpireDate(),"yyyy-MM-dd"));
        if (coupon.getRefStaff() != null) {
            dto.setStaffId(coupon.getRefStaff().getId());
            dto.setRealName(coupon.getRefStaff().getRealName());
        }

        return dto;
    }

    public static Coupon convertFromDto(CouponDto dto,Coupon coupon) {
         coupon =DtoFactory.convert(dto,coupon);
        if (dto.getGqDate() == null) dto.setGqDate(DateHelper.formatDate(DateHelper.addDay(new Date(), 1)));
        coupon.setExpireDate(DateHelper.parse(dto.getGqDate(),"yyyy-MM-dd"));
        return coupon;

    }
    //该方法在数据持久化的时候容易产生id不唯一的冲突
    public static Coupon convertFromDto(CouponDto dto) {
        Coupon coupon = Factory.convert(dto, Coupon.class);
        if (dto.getGqDate() == null) dto.setGqDate(DateHelper.formatDate(DateHelper.addDay(new Date(), 1)));
        coupon.setExpireDate(DateHelper.parse(dto.getGqDate(), "yyyy-MM-dd"));
        return coupon;

    }

    public static PagedResult<CouponDto> convert(PagedResult<Coupon> pg) {
        List<CouponDto> list = new ArrayList<>();
        if (pg.getItems()!=null&&pg.getTotalItemsCount() > 0)
            list = convert(pg.getItems());
        return new PagedResult<>(
                list, pg.getPageNumber(), pg.getPageSize(), pg.getTotalItemsCount());
    }


    public static List<CouponDto> convert(List<Coupon> source) {
        List<CouponDto> list = new ArrayList<>();
        if (null != source)
            source.stream().forEach(s -> list.add(convert(s)));
        return list;
    }

}
