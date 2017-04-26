package com.sunesoft.seera.yc.core.picture.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.yc.core.picture.application.dto.PictureDto;
import com.sunesoft.seera.yc.core.picture.application.pictureFactory.PictureFactory;
import com.sunesoft.seera.yc.core.picture.domain.IPictureRepository;
import com.sunesoft.seera.yc.core.picture.domain.Picture;
import com.sunesoft.seera.yc.core.picture.domain.PictureType;
import com.sunesoft.seera.yc.core.picture.domain.criteria.PictureCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiazl on 2016/9/2.
 */
@Service("iPictureService")
public class PictureServiceImpl extends GenericHibernateFinder implements IPictureService {

    @Autowired
    IPictureRepository repository;

    @Override
    public CommonResult create(PictureDto dto) {
        if (dto == null || dto.getPath() == null) return ResultFactory.commonError("请填写图片路径");
        CommonResult c = exist(dto.getName(), dto.getPath());
        if (c != null && !c.getIsSuccess()) return ResultFactory.commonError(c.getMsg());
        if (dto.getLocation() == null || dto.getPosition() == null) return ResultFactory.commonError("请填写图片的使用位置");
        Picture picture1 = repository.getPicture(dto.getLocation(), dto.getPosition());
        if (picture1 != null) return ResultFactory.commonError("该网页位置已经存在图片可以选择修改");
        Picture picture = PictureFactory.convert(dto);
        return ResultFactory.commonSuccess(repository.save(picture));
    }

    @Override
    public CommonResult edit(PictureDto dto) {
        Picture picture = repository.get(dto.getId());
        if (picture == null) return ResultFactory.commonError("该图片不存在");
        picture.setLink(dto.getLink());
        picture.setPath(dto.getPath());
        picture.setDescription(dto.getDescription());
        if (!dto.getPath().equals(picture.getPath()) && !(exist(dto.getName(), dto.getPath()).getIsSuccess()))
            return ResultFactory.commonError("修改后的同名同路径图片已存在");
//        Picture picture1 = repository.getPicture(dto.getLocation(), dto.getPosition());
//        if ((!dto.getPosition().equals(picture.getPosition()) || !dto.getLocation().equals(picture.getLocation())) && picture1 != null)
//            repository.delete(picture1.getId());
//            return ResultFactory.commonError("修改后的同位置图片已存在");
        picture = PictureFactory.convert(dto, picture);
        return ResultFactory.commonSuccess(repository.save(picture));
    }

    /**
     * 定位查找图片
     *
     * @param location
     * @param position
     * @return
     */
    @Override
    public PictureDto getPic(String location, Integer position) {
        Picture picture = repository.getPicture(location, position);
        if (picture != null) return PictureFactory.convert(picture);
        return null;
    }

    /**
     * 获取页面图片信息
     *
     * @param location
     * @return
     */
    @Override
    public List<PictureDto> getByLocation(String location) {
        List<Picture> pictures = repository.getByLocation(location);
        if (pictures == null || pictures.isEmpty()) return null;
        return PictureFactory.convert(pictures);
    }

    /**
     * 获取页面图片信息
     *
     * @param location
     * @return
     */
    @Override
    public List<PictureDto> getByLocationMobile(String location) {
        List<Picture> pictures = repository.getByLocationMobile(location);
        if (pictures == null || pictures.isEmpty()) return null;
        return PictureFactory.convert(pictures);
    }


    @Override
    public List<PictureDto> getBySrc(String src) {
        List<Picture> pictures = repository.getBySrc(src);
        if (pictures == null) return null;
        return PictureFactory.convert(pictures);
    }

    @Override
    public PictureDto getById(Long id) {
        Picture picture = repository.get(id);
        if (picture == null) return null;
        return PictureFactory.convert(picture);
    }

    @Override
    public List<PictureDto> getByIds(List<Long> ids) {
        Criteria criteria = getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.in("id", ids));
        List<Picture> pictures = criteria.list();
        if (pictures == null || pictures.size() == 0) return null;
        return PictureFactory.convert(pictures);
    }

    @Override
    public CommonResult remove(Long id) {
        try {
            repository.delete(id);
            return ResultFactory.commonSuccess(id);
        } catch (Exception e) {
            return ResultFactory.commonError(e.getMessage());
        }

    }

    @Override
    public CommonResult remove(List<Long> ids) {
        Criteria criteria = getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.in("id", ids));
        List<Picture> pictures = criteria.list();
        if (pictures == null || pictures.size() == 0) return ResultFactory.commonError("请选择删除的照片");
        pictures.stream().forEach(i -> repository.delete(i.getId()));
        return ResultFactory.commonSuccess();
    }

    @Override
    public List<PictureDto> getByName(String name) {
        List<Picture> pictures = repository.getByName(name);
        if (pictures == null || pictures.size() == 0) return null;
        return PictureFactory.convert(pictures);
    }

    @Override
    public List<PictureDto> getByPath(String path) {
        List<Picture> pictures = repository.getByPath(path);
        if (pictures == null || pictures.size() == 0) return null;
        return PictureFactory.convert(pictures);
    }

    @Override
    public List<PictureDto> getByType(PictureType type) {
        List<Picture> pictures = repository.getByType(type);
        if (pictures == null || pictures.size() == 0) return null;
        return PictureFactory.convert(pictures);
    }

    @Override
    public CommonResult exist(String name, String path) {
        return repository.check(name, path);
    }

    @Override
    public PagedResult<PictureDto> page(PictureCriteria criteria) {
        PagedResult<Picture> pg = repository.findPage(criteria);
        if (pg == null || pg.getItems() == null || pg.getItems().size() == 0) return new PagedResult<PictureDto>(1, 10);
        return PictureFactory.convert(pg);
    }
    /**
     * 获取所有图片
     *
     * @return
     */
    public List<PictureDto> getAll(){
        List<Picture> list=repository.getAll();
        if(list==null||list.isEmpty()) return null;
        return PictureFactory.convert(list);
    }

    @Override
    public CommonResult update(PictureDto pictureDto) {
        Picture picture=repository.get(pictureDto.getId());
        picture.setPictureStatus(pictureDto.getPictureStatus());
        return ResultFactory.commonSuccess(repository.save(picture));
    }
}
