package com.sunesoft.seera.yc.core.lottery.application.lotteryFactory;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.Coupon;
import com.sunesoft.seera.yc.core.lottery.application.dtos.SubjectDto;
import com.sunesoft.seera.yc.core.lottery.domain.Subject;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SubjectFactory extends Factory {


    public static SubjectDto convert(Subject coupon) {
        SubjectDto dto = convert(coupon,SubjectDto.class);
        return dto;
    }

    public static Subject convert(SubjectDto dto){
        Subject activity=convert(dto,Subject.class);
        return activity;
    }

    public static Subject convertFromDto(SubjectDto dto,Subject coupon) {
         coupon =DtoFactory.convert(dto,coupon);
         return coupon;

    }
    //该方法在数据持久化的时候容易产生id不唯一的冲突
    public static Subject convertFromDto(SubjectDto dto) {
        Subject coupon = Factory.convert(dto, Subject.class);

        return coupon;

    }

    public static PagedResult<SubjectDto> convert(PagedResult<Subject> pg) {
        List<SubjectDto> list = new ArrayList<>();
        if (pg.getItems()!=null&&pg.getTotalItemsCount() > 0)
            list = convert(pg.getItems());
        return new PagedResult<>(
                list, pg.getPageNumber(), pg.getPageSize(), pg.getTotalItemsCount());
    }


    public static List<SubjectDto> convert(List<Subject> source) {
        List<SubjectDto> list = new ArrayList<>();
        if (null != source)
            source.stream().forEach(s -> list.add(convert(s)));
        return list;
    }

}
