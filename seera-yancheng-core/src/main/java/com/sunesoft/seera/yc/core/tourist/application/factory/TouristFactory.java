package com.sunesoft.seera.yc.core.tourist.application.factory;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.coupon.application.factory.CouponFactory;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import com.sunesoft.seera.yc.core.tourist.application.dtos.FetcherDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.domain.Fetcher;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhaowy on 2016/7/12.
 */
public class TouristFactory extends Factory {


    //region Description Fetcher

    /**
     * 将FetcherDto convert Fetcher
     *
     * @param dto
     * @return
     */
    public static Fetcher convertFromFetcherDto(FetcherDto dto) {
        return Factory.convert(dto, Fetcher.class);
    }

    /**
     * 将Fetcher convert FetcherDto
     *
     * @param fetcher
     * @return
     */
    public static FetcherDto convertToFetcherDto(Fetcher fetcher) {

        return Factory.convert(fetcher, FetcherDto.class);
    }

    /**
     * 将List<Fetcher> convert List<FetcherDto>
     *
     * @param fetchers
     * @return
     */
    public static List<FetcherDto> conertToFetcherDto(List<Fetcher> fetchers) {
        List<FetcherDto> dtos = new ArrayList<>();
        fetchers.stream().forEach(i -> dtos.add(convertToFetcherDto(i)));
        return dtos;
    }
    //endregion


    /**
     * 将TouristDto convert Tourist
     *
     * @param dto
     * @return
     */
    public static Tourist conertFromTouristDto(TouristDto dto) {
        Tourist tourist =Factory.convert(dto, Tourist.class);
//        if (dto.getBindCouponDtos() != null && dto.getBindCouponDtos().size() > 0) {
//            for (CouponDto d : dto.getBindCouponDtos()) {
//                tourist.bindCoupons(CouponFactory.convertFromDto());
//            }
//        }
        if (dto.getFetcherDtos() != null && dto.getFetcherDtos().size() > 0) {
            for (FetcherDto f : dto.getFetcherDtos()) {
                tourist.addFetchers(Factory.convert(f, Fetcher.class));
            }
        }

        return tourist;
    }

    /**
     * 将TouristDto convert Tourist
     *
     * @param dto
     * @return
     */
    public static Tourist conertFromTouristDto(TouristDto dto,Tourist tourist) {
        tourist=DtoFactory.convert(dto,tourist);
        if (dto.getFetcherDtos() != null && dto.getFetcherDtos().size() > 0) {
            for (FetcherDto f : dto.getFetcherDtos()) {
                tourist.addFetchers(Factory.convert(f, Fetcher.class));
            }
        }

        return tourist;
    }



    /**
     * 将 to TouristDto
     *
     * @param tourist
     * @return
     */
    public static TouristDto conertToTouristDto(Tourist tourist) {
        TouristDto dto=Factory.convert(tourist,TouristDto.class);
        if(tourist.getBindCoupons()!=null&&tourist.getBindCoupons().size()>0){
            dto.setBindCouponDtos(CouponFactory.convert(tourist.getBindCoupons()));
        }
        if(tourist.getFetchers()!=null&&tourist.getFetchers().size()>0){
            dto.setFetcherDtos(convert(tourist.getFetchers(),FetcherDto.class));
        }
        return dto;
    }

    public static PagedResult<TouristDto> convertpg(PagedResult<Tourist> pg) {
        List<TouristDto> list = new ArrayList<>();
        if (pg.getTotalItemsCount() > 0)
            pg.getItems().stream().forEach(i->list.add(conertToTouristDto(i)));
        return new PagedResult<>(
                list, pg.getPageNumber(), pg.getPageSize(), pg.getTotalItemsCount());
    }

}
