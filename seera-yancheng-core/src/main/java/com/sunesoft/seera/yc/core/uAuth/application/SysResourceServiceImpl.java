package com.sunesoft.seera.yc.core.uAuth.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.ResourceCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResource;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResourceRepository;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhouz on 2016/5/25.
 */
@Service("sysResourceService")
public class SysResourceServiceImpl extends GenericHibernateFinder implements SysResourceService {
    @Autowired
    SysResourceRepository sysResourceRepository;

    @Override
    public Long addMenu(ResourceDto menudto) {
        return sysResourceRepository.save(convertFromDto(menudto));
    }

    @Override
    public Boolean save(ResourceDto menudto) {
        sysResourceRepository.save(convertFromDto(menudto));
        return true;
    }

    @Override
    public SysResource getByKey(Long id) {
        return sysResourceRepository.get(id);
    }

    @Override
    public Boolean deleteMenu(Long[] ids) {

        Criteria criterion = getSession().createCriteria(SysResource.class);
        if (ids!=null&&ids.length>0) {
            criterion.add(Restrictions.in("id", ids));
        }
        List<SysResource> beans = criterion.list();
        for (SysResource bean : beans) {
            bean.setIsActive(false);
            bean.setParentResource(null);
            sysResourceRepository.save(bean);
        }
        return true;
    }

    @Override
    public Long addOrUodate(ResourceDto dto) {
      return   sysResourceRepository.save(convertFromDto(dto));
    }


    private SysResource convertFromDto(ResourceDto dto){
        SysResource menu = new SysResource();
        if(dto.getId()!=null&& dto.getId()>0) {
            menu= sysResourceRepository.get(dto.getId());
        }
        menu.setUrl(dto.getUrl());
        if(dto.getParentId()!=null &&dto.getParentId()>0){
            SysResource parent=sysResourceRepository.get(dto.getParentId());
           // parent.setId(dto.getParentId());
            menu.setParentResource(parent);
            menu.setIsRoot(false);
            menu.setUrl(menu.getUrl());

        }else{
            menu.setIsRoot(true);
        }

        menu.setName(dto.getName());

        menu.setSort(dto.getSort());
        menu.setResourceType(dto.getResType());
        menu.setIdCode(dto.getIdCode());
        menu.setIconName(dto.getIconName());
        return  menu;
    }

    private ResourceDto convertFromResource(SysResource res){
        ResourceDto dto = new ResourceDto();
        if(!res.getIsRoot()){
            dto.setParentId(res.getParentResource().getId());
        }
        dto.setId(res.getId());
        dto.setName(res.getName());
        dto.setUrl(res.getUrl());
        dto.setSort(res.getSort());
        dto.setResType(res.getResourceType());
        dto.setIdCode(res.getIdCode());
        dto.setIconName(res.getIconName());

        return  dto;
    }

    @Override
    public Boolean updateResource(Long id, String menuUrl, String menuName) {
        SysResource menu = sysResourceRepository.get(id);
        menu.setUrl(menuUrl);
        menu.setName(menuName);
        sysResourceRepository.save(menu);
        return true;
    }

    @Override
    public Boolean updateResource(Long id, String menuUrl, String menuName, int sort) {
        SysResource menu = sysResourceRepository.get(id);
        menu.setUrl(menuUrl);
        menu.setName(menuName);
        menu.setSort(sort);
        sysResourceRepository.save(menu);
        return true;
    }

    @Override
    public List<ResourceDto> getResources(String menuName) {
        List<ResourceDto> dtos= ConvertListDto(getResourceFromName(menuName));
        return dtos;
    }


    public List<SysResource> getResourceFromName(String name) {
        Criteria criterion = getSession().createCriteria(SysResource.class);
        criterion.add(Restrictions.eq("isActive",true));
        criterion.add(Restrictions.eq("isRoot",true)); //isRoot:1 根级，0 非根级.
        if (!StringUtils.isNullOrWhiteSpace(name)) {
            criterion.add(Restrictions.like("name", "%" + name + "%"));
        }
        List<SysResource> beans = criterion.list();
        return beans;
    }


    @Override
    public PagedResult<ResourceDto> getResourcePaged(ResourceCriteria criteria) {
        Criteria criterion = getSession().createCriteria(SysResource.class);
        criterion.add(Restrictions.eq("isActive",true));
        criterion.add(Restrictions.eq("isRoot",true)); //isRoot:1 根级，0 非根级.
//        criterion.add(Restrictions.eq("hasChild",false));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getMenuName())) {
            criterion.add(Restrictions.like("name", "%" + criteria.getMenuName() + "%"));
        }
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());
        List<SysResource> beans = criterion.list();
        List<ResourceDto> dtos= ConvertTreeListDto(beans);
        return new PagedResult<ResourceDto>(dtos, criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }



    @Override
    public List<ResourceDto> getResourceList(ResourceCriteria criteria) {
        Criteria criterion = getSession().createCriteria(SysResource.class);
        criterion.add(Restrictions.eq("isActive",true));
        criterion.add(Restrictions.eq("isRoot",true)); //isRoot:1 根级，0 非根级.
//        criterion.add(Restrictions.eq("hasChild",false));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getMenuName())) {
            criterion.add(Restrictions.like("name", "%" + criteria.getMenuName() + "%"));
        }
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());

        List<SysResource> beans = criterion.list();
        return ConvertTreeListDto(beans);
        // return new PagedResult<ResourceDto>(dtos, criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }


   private  List<ResourceDto> ConvertTreeListDto(Collection<SysResource> resources){
       List<ResourceDto> listDto = new ArrayList<ResourceDto>();
       for(SysResource res:resources){
           ResourceDto dto = new ResourceDto();
           dto = this.convertFromResource(res);
           if(res.getChildResource()!=null&& res.getChildResource().size()>0){
               dto.setChild(ConvertTreeListDto(res.getChildResource()));
           }
            listDto.add(dto);
       }
        return listDto;
   }

    private  List<ResourceDto> ConvertListDto(Collection<SysResource> resources){
        List<ResourceDto> listDto = new ArrayList<ResourceDto>();
        for(SysResource res:resources){
            ResourceDto dto = this.convertFromResource(res);
            listDto.add(dto);
            if(res.getChildResource()!=null&& res.getChildResource().size()>0){
                listDto.addAll(ConvertListDto(res.getChildResource()));//合并（返回父对象和下级对象）
            }
        }
        return listDto;
    }

}
