package com.sunesoft.seera.yc.core.picture.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.picture.application.dto.PictureDto;
import com.sunesoft.seera.yc.core.picture.domain.PictureType;
import com.sunesoft.seera.yc.core.picture.domain.criteria.PictureCriteria;

import java.util.List;

/**
 * 对图片操作的接口
 * Created by xiazl on 2016/9/2.
 */
public interface IPictureService {

    /**
     * 增加图片
     *
     * @param dto
     * @return
     */
    public CommonResult create(PictureDto dto);

    /**
     * 修改图片信息.只能修改图片名称与超链接
     *
     * @param dto
     * @return
     */
    public CommonResult edit(PictureDto dto);

    /**
     * 定位查找图片
     *
     * @param location
     * @param position
     * @return
     */
    public PictureDto getPic(String location, Integer position);

    /**
     * 获取页面图片信息
     *
     * @param location
     * @return
     */
    public List<PictureDto> getByLocation(String location);

    /**
     * 获取页面图片信息
     *
     * @param location
     * @return
     */
    public List<PictureDto> getByLocationMobile(String location);
    /**
     * 获取图片信息
     *
     * @param src
     * @return
     */
    public List<PictureDto> getBySrc(String src);

    /**
     * 获取图片信息
     *
     * @param id
     * @return
     */
    public PictureDto getById(Long id);

    /**
     * 获取图片信息
     *
     * @param ids
     * @return
     */
    public List<PictureDto> getByIds(List<Long> ids);


    /**
     * 删除一个
     *
     * @param id
     * @return
     */
    public CommonResult remove(Long id);

    /**
     * 删除多个
     *
     * @param ids
     * @return
     */
    public CommonResult remove(List<Long> ids);

    /**
     * 获取图片信息
     *
     * @param name
     * @return
     */
    public List<PictureDto> getByName(String name);

    /**
     * 获取图片信息
     *
     * @param path
     * @return
     */
    public List<PictureDto> getByPath(String path);


    /**
     * 获取图片信息
     *
     * @param type
     * @return
     */
    public List<PictureDto> getByType(PictureType type);

    /**
     * 检查同名，通路径的文件是否存在
     *
     * @param name
     * @param path
     * @return 返回true标识不存在
     */
    public CommonResult exist(String name, String path);

    /**
     * 图片的分页查询
     *
     * @param criteria
     * @return
     */
    public PagedResult<PictureDto> page(PictureCriteria criteria);

    /**
     * 获取所有图片
     *
     * @return
     */
    public List<PictureDto> getAll();

    /**
     * 更新图片状态
     * @param pictureDto
     * @return
     */
    CommonResult update(PictureDto pictureDto);
}
