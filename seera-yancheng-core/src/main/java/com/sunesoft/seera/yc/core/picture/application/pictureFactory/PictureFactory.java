package com.sunesoft.seera.yc.core.picture.application.pictureFactory;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import com.sunesoft.seera.yc.core.picture.application.dto.PictureDto;
import com.sunesoft.seera.yc.core.picture.domain.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiazl on 2016/9/2.
 */
public class PictureFactory  extends Factory{

    /**
     * 将PictureDto-》Picture
     * @param dto
     * @return
     */
    public static Picture convert(PictureDto dto){
        return convert(dto,Picture.class);
    }
    /**
     * 用于更新的时候
     * 将PictureDto-》Picture
     * @param dto
     * @return
     */
    public static Picture convert(PictureDto dto,Picture picture){
        return DtoFactory.convert(dto,picture);
    }

    /**
     * 将Picture-》PictureDto
     * @param picture
     * @return
     */
    public static PictureDto convert(Picture picture){
        return convert(picture,PictureDto.class);
    }
    /**
     * 将List<Picture>-》List<PictureDto>
     * @param pictures
     * @return
     */
    public static List<PictureDto> convert(List<Picture> pictures){
        List<PictureDto> dtos=new ArrayList<>();
        if(pictures==null||pictures.size()==0)return null;
        pictures.stream().forEach(i->dtos.add(convert(i)));
        return dtos;
    }
    /**
     * 将PagedResult<Picture>-》PagedResult<PictureDto>
     * @param pg
     * @return
     */
    public static PagedResult<PictureDto> convert(PagedResult<Picture> pg){
       if(pg==null||pg.getItems()==null||pg.getItems().size()==0) return new PagedResult<PictureDto>(1,10);
        return new PagedResult<PictureDto>(convert(pg.getItems()),pg.getPageNumber(),pg.getPageSize(),pg.getTotalItemsCount());
    }
}
