package com.sunesoft.seera.yc.core.picture.domain.infrastructure.hibernate;


import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.picture.domain.IPictureRepository;
import com.sunesoft.seera.yc.core.picture.domain.Picture;
import com.sunesoft.seera.yc.core.picture.domain.PictureType;
import com.sunesoft.seera.yc.core.picture.domain.criteria.PictureCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiazl on 2016/9/2.
 */
@Service("iPictureRepository")
public class PictureRepositoryImpl extends GenericHibernateRepository<Picture, Long> implements IPictureRepository {


    @Override
    public Picture getPicture(String loaction, Integer position) {
        Criteria criteria = getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.eq("location",loaction));
        criteria.add(Restrictions.eq("position", position));
        List<Picture> list = criteria.list();
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public List<Picture> getByName(String name) {
        Criteria criteria = getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("name", name));
        criteria.add(Restrictions.eq("isActive", true));
        List<Picture> list = criteria.list();
        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    /**
     * 通过名称获得
     * @param location
     * @return
     */
    @Override
    public List<Picture> getByLocation(String location){
        Criteria criteria = getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("location", location));
        criteria.add(Restrictions.eq("isActive", true));
        List<Picture> list = criteria.list();
        if (list != null && list.size() > 0)
            return list;
        return null;
    }


    /**
     * 通过名称获得
     * @param location
     * @return
     */
    @Override
    public List<Picture> getByLocationMobile(String location){
        Criteria criteria = getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("location", location));
        criteria.add(Restrictions.eq("pictureStatus", true));
        criteria.add(Restrictions.eq("isActive", true));
        List<Picture> list = criteria.list();
        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    @Override
    public CommonResult check(String name, String path) {
        Criteria criteria = getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("name", name));
        criteria.add(Restrictions.eq("path", path));
        criteria.add(Restrictions.eq("isActive", true));
        List<Picture> list = criteria.list();
        if (list != null && list.size() > 0)
            return ResultFactory.commonError("已经存在");
        return ResultFactory.commonSuccess();
    }

    @Override
    public List<Picture> getByPath(String path) {
        Criteria criteria = getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("path", path));
        criteria.add(Restrictions.eq("isActive", true));
        List<Picture> list = criteria.list();
        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    @Override
    public List<Picture> getBySrc(String src) {
        Criteria criteria = getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("src", src));
        criteria.add(Restrictions.eq("isActive", true));
        List<Picture> list = criteria.list();
        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    @Override
    public List<Picture> getByType(PictureType type) {
        Criteria criteria = getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("isActive", true));
        List<Picture> list = criteria.list();
        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    @Override
    public PagedResult<Picture> findPage(PictureCriteria criteria) {
        Criteria criterion = getSession().createCriteria(Picture.class);
        if(criteria.getType()!=null)
            criterion.add(Restrictions.eq("type", criteria.getType()));
        if(!StringUtils.isNullOrWhiteSpace(criteria.getPath()))
            criterion.add(Restrictions.eq("path", criteria.getPath()));
        if(!StringUtils.isNullOrWhiteSpace(criteria.getLocation()))
            criterion.add(Restrictions.eq("location",criteria.getLocation()));
        criterion.add(Restrictions.eq("isActive", true));
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.addOrder(
                criteria.isAscOrDesc() ?
                        Order.desc(criteria.getOrderByProperty() == null ? "id" : criteria.getOrderByProperty()) :
                        Order.asc(criteria.getOrderByProperty() == null ? "id" : criteria.getOrderByProperty())
        );
        criterion.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());

        return new PagedResult<Picture>(criterion.list(), criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }
    /**
     * 获取所有图片
     * @return
     */
    public List<Picture> getAll(){
        Criteria criteria=getSession().createCriteria(Picture.class);
        criteria.add(Restrictions.eq("isActive",true));
        criteria.add(Restrictions.or(
                Restrictions.eq("pictureStatus", true),
                Restrictions.isNull("pictureStatus")));
        List<Picture> list=criteria.list();
        if(list==null||list.isEmpty())return null;
        return list;
    }
}
