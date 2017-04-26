package com.sunesoft.seera.yc.core.uAuth.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.RoleCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.PermissionGroupDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysPermissionGroup;
import com.sunesoft.seera.yc.core.uAuth.domain.SysPermissionGroupRepository;
import com.sunesoft.seera.yc.core.uAuth.domain.SysRole;
import com.sunesoft.seera.yc.core.uAuth.domain.SysRoleRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xiazl on 2016/5/26.
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends GenericHibernateFinder implements SysRoleService {

    @Autowired
    SysRoleRepository roleRepository;

    @Autowired
    SysPermissionGroupRepository sysPermissionGroupRepository;

    @Autowired
    SysPermissionGroupService permissionGroupService;

    @Override
    public Long addRole(RoleDto dto) {
        List<RoleDto> list=getAllRole(dto.getName());
        for(RoleDto r:list){
            if(r.getName().equals(dto.getName())){
                return -1l;
            }
        }
        return roleRepository.save(convertFromDto(dto));
    }

    @Override
    public Boolean deleteRole(Long[] ids) {
        Criteria criterion = getSession().createCriteria(SysRole.class);
        if (ids != null && ids.length > 0) {
            criterion.add(Restrictions.in("id", ids));
        }
        List<SysRole> list = criterion.list();
        for (SysRole role : list) {
            role.setIsActive(false);
            Date time=new Date();
            role.setLastUpdateTime(time);
            roleRepository.save(role);
        }
        return true;
    }

    @Override
    public Long updateRole(RoleDto dto){
        Date time=new Date();
        SysRole role = roleRepository.get(dto.getId());
        if (role != null) {
            role.setName(dto.getName());
            role.setDescription(dto.getDescription());
            role.setIdCode(dto.getIdCode());
            List<PermissionGroupDto> listp=dto.getPrivilegeGroupListDtos();
            List<SysPermissionGroup> lists=new ArrayList<SysPermissionGroup>();
            if(listp!=null&&listp.size()>0){
                for(PermissionGroupDto pd:listp) {
                    SysPermissionGroup sp = permissionGroupService.getById(pd.getId());
                    lists.add(sp);
                }
                role.setPrivilegeGroupList(lists);
            }
            return roleRepository.save(role);
        }
        return -1L;
    }

    @Override
    public RoleDto getRoleById(Long id) {
        RoleDto roleDto = new RoleDto();
        if (roleRepository.get(id).getIsActive() == null) {
            roleRepository.get(id).setIsActive(false);
            System.out.println("当前角色的isActive值为null,已经帮你设置为false");
        }
        if (roleRepository.get(id).getIsActive()) {
            try {
                roleDto = convertToDto(roleRepository.get(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return roleDto;
    }


    @Override
    public Long save(RoleDto dto) {
        return roleRepository.save(convertFromDto(dto));
    }

    @Override
    public Long addOrUpdate(RoleDto dto) {
        if(dto.getId()!=null&&dto.getId()>0) {
            return roleRepository.save(convertFromDto(dto));
        }else{
          List<RoleDto> list=getAllRole(dto.getName());
           if(list!=null&&list.size()>0){
               dto.setId(list.get(0).getId());
               addOrUpdate(dto);
           }
            return roleRepository.save(convertFromDto(dto));
        }
    }

    private SysRole convertFromDto(RoleDto dto) {
        SysRole role =null;
        if (dto.getId() != null && dto.getId() > 0) {
            role = roleRepository.get(dto.getId());
        }else {
            role = new SysRole();
        }
            role.setName(dto.getName());
            role.setSort(dto.getSort());
            role.setIdCode(dto.getIdCode());
            role.setDescription(dto.getDescription());
            role.setLastUpdateTime(new Date());

        List<PermissionGroupDto> permissionGroupDtos =dto.getPrivilegeGroupListDtos();
        List<SysPermissionGroup> listsp=new ArrayList<SysPermissionGroup>();
        if (permissionGroupDtos != null && permissionGroupDtos.size()> 0) {
            for(PermissionGroupDto pd:permissionGroupDtos){
                SysPermissionGroup sp=permissionGroupService.getById(pd.getId());
                listsp.add(sp);
            }
            role.setPrivilegeGroupList(listsp);
        }
        return role;
    }

    private RoleDto convertToDto(SysRole role) {
        RoleDto dto = new RoleDto();
            dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setSort(role.getSort());
        dto.setIdCode(role.getIdCode());
        dto.setDescription(role.getDescription());
        List<SysPermissionGroup> sysPermissionGroups = role.getPrivilegeGroupList();
        List<PermissionGroupDto> permissionGroupDtos = new ArrayList<PermissionGroupDto>();

        if (sysPermissionGroups != null && sysPermissionGroups.size() > 0) {
            for (SysPermissionGroup s : sysPermissionGroups) {
                PermissionGroupDto pgd = new PermissionGroupDto();
                pgd.setId(s.getId());
                pgd.setName(s.getName());
                pgd.setSort(s.getSort());
                pgd.setActive(s.getIsActive());
                permissionGroupDtos.add(pgd);
            }
            dto.setPrivilegeGroupListDtos(permissionGroupDtos);
        }
        return dto;
    }

    @Override
    public List<RoleDto> getAllRole() {
        Criteria criterion = getSession().createCriteria(SysRole.class);
        criterion.add(Restrictions.eq("isActive", true));
        List<SysRole> list1 = criterion.list();
        List<RoleDto> list = new ArrayList<RoleDto>();
        for (SysRole role : list1) {
                list.add(convertToDto(role));
        }
        return list;
    }

    @Override
    public PagedResult<RoleDto> getRolePage(RoleCriteria criteria) {
        Criteria criterion = getSession().createCriteria(SysRole.class);
        criterion.add(Restrictions.eq("isActive", true));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getRoleName())) {
            criterion.add(Restrictions.like("name", "%" + criteria.getRoleName() + "%"));
        }
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());
        List<SysRole> list = criterion.list();
        List<RoleDto> list1 = new ArrayList<RoleDto>();
        for (SysRole role : list) {
                list1.add(convertToDto(role));
        }
//        for(RoleDto d:list1){
//            System.out.println(d.getId()+"\t"+d.getDescription()+"\t"+d.getIdCode());
//        }
        return new PagedResult<RoleDto>(list1, criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }

    @Override
    public List<RoleDto> getAllRole(String roleName) {
        Criteria criterion = getSession().createCriteria(SysRole.class);
        criterion.add(Restrictions.eq("isActive", true));
        if (!StringUtils.isNullOrWhiteSpace(roleName)) {
            criterion.add(Restrictions.like("name", "%" + roleName + "%"));
        }
        List<SysRole> list1 = criterion.list();
        List<RoleDto> list = new ArrayList<RoleDto>();
        for (SysRole role : list1) {
                list.add(convertToDto(role));

        }
        return list;
    }




}
