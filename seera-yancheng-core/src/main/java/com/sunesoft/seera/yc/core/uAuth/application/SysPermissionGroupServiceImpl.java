package com.sunesoft.seera.yc.core.uAuth.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.PermissionGroupCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.PermissionGroupDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysPermissionGroup;
import com.sunesoft.seera.yc.core.uAuth.domain.SysPermissionGroupRepository;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResource;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResourceRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jiangkefan on 2016/5/26.
 */

@Service("sysPermissionGroupService")
public class SysPermissionGroupServiceImpl extends GenericHibernateFinder implements SysPermissionGroupService{
    @Autowired
    SysPermissionGroupRepository sysPermissionGroupRepository;

    @Autowired
    SysResourceRepository sysResourceRepository;

    public Long add(SysPermissionGroup sysPermissionGroup) {
          return sysPermissionGroupRepository.save(sysPermissionGroup);
    }

    public SysPermissionGroup getById(Long id) {
        return sysPermissionGroupRepository.get(id);
    }

    @Override
    public PermissionGroupDto getDtoById(Long id) {
        SysPermissionGroup group = sysPermissionGroupRepository.get(id);
        return convertfromDto(group);
    }

    public boolean delete(Long[] ids) {
        Criteria criterion = getSession().createCriteria(SysPermissionGroup.class);
        if (ids!=null&&ids.length>0) {
            criterion.add(Restrictions.in("id", ids));
        }
        List<SysPermissionGroup> beans = criterion.list();
        for (SysPermissionGroup bean : beans) {
            bean.setIsActive(false);
            sysPermissionGroupRepository.save(bean);
        }
        return true;
    }
    private SysPermissionGroup convertFromDto(PermissionGroupDto dto){
        SysPermissionGroup permissionGroup = new SysPermissionGroup();
        if(dto.getId()!=null&& dto.getId()>0) {
            permissionGroup= sysPermissionGroupRepository.get(dto.getId());
        }
        permissionGroup.setName(dto.getName());
        permissionGroup.setSort(dto.getSort());
        //设置菜单
        List<Long> ids = dto.getMenuIds();
        Criteria criterion = getSession().createCriteria(SysResource.class);
        try{
            if(!ids.isEmpty()){
                criterion.add(Restrictions.in("id",ids));
                permissionGroup.setResources(criterion.list());//权限组拥有的菜单对象
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        for(Long id : ids){
//            SysResource s = sysResourceRepository.get(id);
//            sysResources.add(s);
//        }
//        for(long id :ids)
//        {
//             SysResource s  = sysResourceRepository.get(id);
//
//             sysResources.add(s);
//}
        return  permissionGroup;
    }
    @Override
    public Long addOrUpdate(PermissionGroupDto dto) {
        return   sysPermissionGroupRepository.save(convertFromDto(dto));
    }

    @Override
    public boolean update(PermissionGroupDto permissionGroupDto) {
        SysPermissionGroup sysPermissionGroup = sysPermissionGroupRepository.get(permissionGroupDto.getId());
        sysPermissionGroup.setSort(permissionGroupDto.getSort());
        sysPermissionGroup.setIsActive(permissionGroupDto.isActive());
        sysPermissionGroup.setName(permissionGroupDto.getName());
        sysPermissionGroupRepository.save(sysPermissionGroup);
        return true;
    }

    @Override
    public List<PermissionGroupDto> getAllPermissionGroupNames() {
        Criteria criterion = getSession().createCriteria(SysPermissionGroup.class);
        criterion.add(Restrictions.eq("isActive", true));
        List<SysPermissionGroup> list1 = criterion.list();
        List<PermissionGroupDto> list = new ArrayList<PermissionGroupDto>();
        for (SysPermissionGroup sysPermissionGroup : list1) {
            list.add(convertfromDto(sysPermissionGroup));
        }
        return list;

    }

    @Override
    public List<PermissionGroupDto> getPermissionGroup(String groupName) {
        Criteria criterion = getSession().createCriteria(SysPermissionGroup.class);
        criterion.add(Restrictions.eq("isActive",true));
        if (!StringUtils.isNullOrWhiteSpace(groupName)) {
            criterion.add(Restrictions.like("name", "%" + groupName + "%"));
        }
        List<SysPermissionGroup> beans = criterion.list();
        List<PermissionGroupDto> dtos= ConvertListDto(beans);
        return dtos;
    }


    @Override
    public PagedResult<PermissionGroupDto> getPermissionGroupPaged(PermissionGroupCriteria criteria) {
        Criteria criterion = getSession().createCriteria(SysPermissionGroup.class);
        criterion.add(Restrictions.eq("isActive",true));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getPermissionGroupName())) {
            criterion.add(Restrictions.like("name", "%" + criteria.getPermissionGroupName() + "%"));
        }
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());
        List<SysPermissionGroup> beans = criterion.list();
        PagedResult<PermissionGroupDto> permissionGroupDtoPagedResult = new PagedResult<PermissionGroupDto>(ConvertListDto(beans), criteria.getPageNumber(), criteria.getPageSize(), totalCount);
        return permissionGroupDtoPagedResult;
    }


//    public PagedResult<PermissionGroupDto> getResourcePaged(ResourceCriteria criteria) {
//        Criteria criterion = getSession().createCriteria(SysPermissionGroup.class);
//        List<SysPermissionGroup> bean = criterion.list();
//        criterion.add(Restrictions.eq("isActive",true));
//        criterion.add(Restrictions.eq("isRoot",true)); //isRoot:1 根级，0 非根级.
////        criterion.add(Restrictions.eq("hasChild",false));
//        if (!StringUtils.isNullOrWhiteSpace(criteria.getMenuName())) {
//            criterion.add(Restrictions.like("name", "%" + criteria.getMenuName() + "%"));
//        }
//
//        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
//        criterion.setProjection(null);
//        criterion.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());
//        List<SysPermissionGroup> beans = criterion.list();
//        List<PermissionGroupDto> dtos= ConvertListDto(beans);
//        return new PagedResult<PermissionGroupDto>(dtos, criteria.getPageNumber(), criteria.getPageSize(), totalCount);
//
//    }
//
    private  List<PermissionGroupDto> ConvertListDto(Collection<SysPermissionGroup> resources){
        List<PermissionGroupDto> listDto = new ArrayList<PermissionGroupDto>();
        for(SysPermissionGroup res:resources){
            PermissionGroupDto dto = new PermissionGroupDto();

            dto = this.convertfromDto(res);
            if(res.getChildPermissionGroup()!=null&& res.getChildPermissionGroup().size()>0){
                dto.setChild(ConvertListDto(res.getChildPermissionGroup()));
            }
            listDto.add(dto);
        }
        return listDto;
    }
//
    private PermissionGroupDto convertfromDto(SysPermissionGroup sysPermissionGroup){
        PermissionGroupDto permissionGroupDto = new PermissionGroupDto();
        permissionGroupDto.setId(sysPermissionGroup.getId());
        permissionGroupDto.setName(sysPermissionGroup.getName());
        permissionGroupDto.setActive(sysPermissionGroup.getIsActive());
        permissionGroupDto.setSort(sysPermissionGroup.getSort());
        permissionGroupDto.setCreateDatetime(sysPermissionGroup.getCreateDateTime());
        permissionGroupDto.setLastUpdateTimes(sysPermissionGroup.getLastUpdateTime());

        List<Long> ids = new ArrayList<>();
        if(sysPermissionGroup.getResources()!=null&&sysPermissionGroup.getResources().size()>0){
            for(SysResource resource :sysPermissionGroup.getResources()){
                ids.add(resource.getId());
            }
        }

        permissionGroupDto.setMenuIds(ids);

        return permissionGroupDto;
    }

}
