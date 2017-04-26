package com.sunesoft.seera.yc.core.activity.application.Activityfactory;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivityDto;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivitySimpleDto;
import com.sunesoft.seera.yc.core.activity.domain.Activity;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import com.sunesoft.seera.yc.core.product.application.factory.ProductFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiazl on 2016/8/6.
 */
public class Activityfactory extends Factory {

    /**
     * 将ActivityDto->Activity
     * @param dto
     * @return
     */
    public static Activity convert(ActivityDto dto){
        Activity activity=convert(dto,Activity.class);
        if(dto.getProductDto()!=null){
            activity.setProduct(ProductFactory.convert(dto.getProductDto()));
        }

        return activity;
    }

    /**
     * 将ActivityDto->Activity
     * @param dto
     * @param activity
     * @return
     */
    public static Activity convert(ActivityDto dto,Activity activity){
        activity= DtoFactory.convert(dto, activity);
        if(dto.getProductDto()!=null){
            activity.setProduct(ProductFactory.convert(dto.getProductDto()));
        }

        return activity;
    }

    /**
     * Activity->ActivityDto
     * @param activity
     * @return
     */
    public static ActivityDto convert(Activity activity){
        ActivityDto dto=convert(activity,ActivityDto.class);
        if(activity.getProduct()!=null){
            dto.setProductDto(ProductFactory.convert(activity.getProduct()));
        }

        return dto;
    }

    /**
     * Activity->ActivitySimpleDto
     * @param activity
     * @return
     */
    public static ActivitySimpleDto convertSimple(Activity activity){
        ActivitySimpleDto dto=convert(activity,ActivitySimpleDto.class);
        if(activity.getProduct()!=null){
            dto.setProductDto(ProductFactory.convert(activity.getProduct()));
        }

        return dto;
    }

    /**
     * List<ActivityDto>-> List<ActivityDto>
     * @param list
     * @return
     */
    public static List<ActivityDto> convertList(List<Activity> list){
        List<ActivityDto> dtos=new ArrayList<>();
        list.stream().forEach(i->
            dtos.add(convert(i))
        );
        return dtos;
    }

    /**
     * List<ActivityDto>-> List<ActivitySimpleDto>
     * @param list
     * @return
     */
    public static List<ActivitySimpleDto> convertSimpleList(List<Activity> list){
        List<ActivitySimpleDto> dtos=new ArrayList<>();
        list.stream().forEach(i->
            dtos.add(convertSimple(i))
        );
        return dtos;
    }

    /**
     * PagedResult<Activity>-->PagedResult<ActivityDto>
     * @param pg
     * @return
     */
    public static PagedResult<ActivityDto> convertpg(PagedResult<Activity> pg){
      List<ActivityDto> dtos=new ArrayList<>();
        if(pg.getItems()!=null&&pg.getItems().size()>0){
            dtos=convertList(pg.getItems());
        }
        return new PagedResult<ActivityDto>(dtos,pg.getPageNumber(),pg.getPageSize(),pg.getTotalItemsCount());
    }

    /**
     * PagedResult<Activity>-->PagedResult<ActivitySimpleDto>
     * @param pg
     * @return
     */
    public static PagedResult<ActivitySimpleDto> convertSimplepg(PagedResult<Activity> pg){
        List<ActivitySimpleDto> dtos=new ArrayList<>();
        if(pg.getItems()!=null&&pg.getItems().size()>0){
            dtos=convertSimpleList(pg.getItems());
        }
        return new PagedResult<ActivitySimpleDto>(dtos,pg.getPageNumber(),pg.getPageSize(),pg.getTotalItemsCount());
    }
}
