package com.sunesoft.seera.yc.core.picture.domain;


import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.picture.domain.criteria.PictureCriteria;

import java.util.List;

/**
 * 对图片操作的仓储
 * Created by xiazl on 2016/9/2.
 */
public interface IPictureRepository extends IRepository<Picture,Long> {

    /**
     * 定位查找图片
     * @param location
     * @param position
     * @return
     */
    public Picture getPicture(String location,Integer position);

    /**
     * 通过名称获得
     * @param name
     * @return
     */
    public List<Picture> getByName(String name);

    /**
     * 通过名称获得
     * @param location
     * @return
     */
    public List<Picture> getByLocation(String location);


    public List<Picture> getByLocationMobile(String location);

    /**
     * 检查该名称是否已经被使用,名称相同，路径相同。就是覆盖了，容易造成冲突
     * @param name
     * @return
     */
    public CommonResult check(String name,String path);

    /**
     * 通过路径获得
     * @param path
     * @return
     */
    public List<Picture> getByPath(String path);

    /**
     * 通过链接获得
     * @param src
     * @return
     */
    public List<Picture> getBySrc(String src);

    /**
     * 根据用途类型查询
     * @param type
     * @return
     */
    public List<Picture> getByType(PictureType type);

    /**
     * 图片的分页查询
     * @param criteria
     * @return
     */
    public PagedResult<Picture> findPage(PictureCriteria criteria);

    /**
     * 获取所有图片
     * @return
     */
    public List<Picture> getAll();

}
