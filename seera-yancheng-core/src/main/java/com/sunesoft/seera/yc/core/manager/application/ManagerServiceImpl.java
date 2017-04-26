package com.sunesoft.seera.yc.core.manager.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.MD5;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.manager.application.criteria.ManagerCriteria;
import com.sunesoft.seera.yc.core.manager.application.dtos.InnerManagerDto;
import com.sunesoft.seera.yc.core.manager.application.dtos.ManagerSessionDto;
import com.sunesoft.seera.yc.core.manager.application.factory.ManagerFactory;
import com.sunesoft.seera.yc.core.manager.domain.IManagerRepository;
import com.sunesoft.seera.yc.core.manager.domain.InnerManager;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysPermissionGroup;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResource;
import com.sunesoft.seera.yc.core.uAuth.domain.SysRole;
import com.sunesoft.seera.yc.core.uAuth.domain.SysRoleRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhaowy on 2016/7/11.
 */
@Service("IManagerService")
public class ManagerServiceImpl extends GenericHibernateFinder implements IManagerService {

    @Autowired
    IManagerRepository imanagerRepository;

    @Autowired
    SysRoleRepository roleRepository;

    /**
     * add or update用户
     *
     * @param dto 用户信息
     * @return Boolean
     */
    @Override
    public CommonResult addUser(InnerManagerDto dto) throws Exception {
        checkDate(dto);
        List<InnerManagerDto> dtos = getAllUser(dto.getUserName());
        if (dtos != null && dtos.size() > 0) {
            return ResultFactory.commonError("This manager already exists");
        }
        InnerManager manager = new InnerManager();
        manager = DtoFactory.convert(dto, manager);
        //密码加密
        manager.setPassword(dto.getPassword());
        //设置leader
        return ResultFactory.commonSuccess(imanagerRepository.save(manager));
    }


    /**
     * update用户
     *
     * @param dto 用户信息
     * @return Boolean
     */
    @Override
    public CommonResult updateUser(InnerManagerDto dto) {
        InnerManager manager = imanagerRepository.get(dto.getId());
        if (manager != null && manager.getIsActive() && manager.isStatus()) {
            if (!dto.getUserName().equals(manager.getUserName()))
                dto.setUserName(manager.getUserName());
            manager = DtoFactory.convert(dto, manager);

            return ResultFactory.commonSuccess(imanagerRepository.save(manager));
        }
        return ResultFactory.commonError("This manager has been forbidden");
    }

    /**
     * wxoauth
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public ManagerSessionDto login(String userName, String password) {
        Criteria criterion = getSession().createCriteria(InnerManager.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("userName", userName));
        criterion.add(Restrictions.eq("password", MD5.GetMD5Code(password)));
        criterion.add(Restrictions.eq("status", true));
        List<InnerManager> list = criterion.list();
        if (list != null && list.size() > 0) {
            list.get(0).setLastLoginTime(new Date());
            int i = list.get(0).getLoginCount();
            list.get(0).setLoginCount(++i);
            list.get(0).setLastLoginTime(new Date());
            return ManagerFactory.convert(list.get(0), ManagerSessionDto.class);
        }
        return null;
    }

    @Override
    public CommonResult checkPwd(Long id, String oldPwd) {
        Criteria criterion = getSession().createCriteria(InnerManager.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("id", id));
        criterion.add(Restrictions.eq("password", MD5.GetMD5Code(oldPwd)));
        criterion.add(Restrictions.eq("status", true));
        List<InnerManager> list = criterion.list();

        if(list!=null&&list.size()>0)
            return ResultFactory.commonSuccess();
        else return new CommonResult(false,"原始密码不正确");
    }

    /**
     * change password
     *
     * @param id
     * @param Password
     * @return
     */
    @Override
    public CommonResult changePassword(Long id, String Password) {
        InnerManager user = imanagerRepository.get(id);
        if (user == null || !user.getIsActive() || !user.isStatus()) {
            return ResultFactory.commonError("该账户不存在，或已经被禁用");
        }
        user.setPassword(Password);
        user.setLastUpdateTime(new Date());
        return ResultFactory.commonSuccess(imanagerRepository.save(user));
    }

    /**
     * 设置用户状态
     * 禁用或者为禁用
     *
     * @param id
     * @param status
     * @return
     */
    public CommonResult setStatus(Long id, boolean status) {
        InnerManager user = imanagerRepository.get(id);
        if (user == null || !user.getIsActive() || !user.isStatus()) {
            return ResultFactory.commonError("该账户不存在，或已经被禁用");
        }
        user.setStatus(status);
        user.setLastUpdateTime(new Date());
        return ResultFactory.commonSuccess(imanagerRepository.save(user));
    }


    /**
     * 设置用户状态
     * 禁用或者为禁用
     *
     * @param ids
     * @param status
     * @return
     */
    @Override
    public CommonResult setUserStatus(List<Long> ids, boolean status) {
        if (ids == null || ids.isEmpty()) {
            return ResultFactory.commonError("请选择设置对象");
        }
        Criteria criterion = getSession().createCriteria(InnerManager.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.in("id", ids));
        List<InnerManager> beans = criterion.list();
        if (beans != null && beans.size() > 0) {
            beans.stream().forEach(i -> {
                i.setStatus(status);
                i.setLastUpdateTime(new Date());
                imanagerRepository.save(i);
            });
        }
        return ResultFactory.commonSuccess();
    }

    /**
     * 查询实例
     *
     * @param criteria 查询条件
     * @return
     */
    @Override
    public PagedResult<InnerManagerDto> findUser(ManagerCriteria criteria) {
        return ManagerFactory.convert(imanagerRepository.findManagers(criteria));
    }

    /**
     * 多项选中删除
     *
     * @param ids
     * @return
     */
    @Override
    public CommonResult delete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResultFactory.commonError("请选中删除对象");
        }
        Criteria criterion = getSession().createCriteria(InnerManager.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.in("id", ids));
        List<InnerManager> list = criterion.list();
        if (list != null && list.size() > 0) {
            list.stream().forEach(i -> {
                i.setIsActive(false);
                i.setLastUpdateTime(new Date());
                imanagerRepository.save(i);
            });
        }
        return ResultFactory.commonSuccess();
    }

    /**
     * 获取所用用户
     *
     * @return
     */
    @Override
    public List<InnerManagerDto> getAllUser() {
        Criteria criterion = getSession().createCriteria(InnerManager.class);
        criterion.add(Restrictions.eq("isActive", true));
        List<InnerManager> managers = criterion.list();
        return ManagerFactory.convert(managers);
    }

    /**
     * 根据id获取用户
     *
     * @param id
     * @return
     */
    @Override
    public InnerManagerDto getById(Long id) {
        InnerManager user = imanagerRepository.get(id);
        if (user != null && user.getIsActive()) {
            return ManagerFactory.convert(user);
        }
        return null;
    }

    /**
     * 获取所用用户
     *
     * @return
     * @ param userName
     */
    @Override
    public List<InnerManagerDto> getAllUser(String userName) {
        Criteria criterion = getSession().createCriteria(InnerManager.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("userName", userName));
        List<InnerManager> managers = criterion.list();
        return ManagerFactory.convert(managers);
    }


    /**
     * 获取可以作为leader的所用用户
     *
     * @return
     * @ param staffId
     */
    @Override
    public List<InnerManagerDto> getLeaders(Long staffId) {
        Criteria criterion = getSession().createCriteria(InnerManager.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("status", true));
        List<InnerManager> managers = criterion.list();
        managers.removeIf(i -> i.getId().equals(staffId));
        return ManagerFactory.convert(managers);
    }

    /**
     * 获取登录信息
     *
     * @param uid
     * @return
     */
    @Override
    public ManagerSessionDto GetUserSessionDtoById(long uid) {
        InnerManager user = imanagerRepository.get(uid);
        if (user != null && user.getIsActive()) {
            return ManagerFactory.convert(user, ManagerSessionDto.class);
        }
        return null;
    }

    /**
     * 设置管理员角色
     *
     * @param managerId 管理员标识
     * @param roleId    角色标识
     * @return
     */
    @Override
    public CommonResult SetManagerRole(Long managerId, Long roleId) {
        InnerManager manager = imanagerRepository.get(managerId);
        InnerManager user = imanagerRepository.get(managerId);
        if (user != null && user.getIsActive()) {
            SysRole role = roleRepository.get(roleId);
            List<SysRole> roles = new ArrayList<>();
            roles.add(role);

            manager.setUserRoleList(roles);
            imanagerRepository.save(manager);
            return ResultFactory.commonSuccess();
        } else return ResultFactory.commonError("管理员不存在或已禁用");
    }

    private void checkDate(InnerManagerDto dto) throws Exception {
        if (dto == null) throw new Exception("请填写内容再添加");
        if (StringUtils.isNullOrWhiteSpace(dto.getUserName()) || StringUtils.isNullOrWhiteSpace(dto.getPassword()))
            throw new Exception("用户名，密码，用户类型必填");

    }

    @Override
    public Map<Long, List<ResourceDto>> getAllAuthInfoByRole() {

        Map<Long, List<ResourceDto>> resourceInfo = new HashMap<>();   Criteria criterion = getSession().createCriteria(InnerManager.class);
        criterion.add(Restrictions.eq("isActive", true));
        List<InnerManager> managers = criterion.list();
        for (InnerManager manager : managers) {

            List<ResourceDto> resourceDtos  = new ArrayList<>();
            if (manager.getUserRoleList() != null && manager.getUserRoleList().size() > 0) {
                for (SysRole role : manager.getUserRoleList()) {
                    if (role.getPrivilegeGroupList() != null && role.getPrivilegeGroupList().size() > 0) {
                        for (SysPermissionGroup group : role.getPrivilegeGroupList()) {
                            if (group.getResources() != null && group.getResources().size() > 0) {
                                for (SysResource resource : group.getResources())
                                    resourceDtos.add(convertFromResource(resource));
                            }
                        }
                    }
                }
                resourceDtos= resourceDtos.stream().distinct().collect(Collectors.toList());
                List<ResourceDto> resourcesTemp = resourceDtos.stream().filter(x->x.getParentId()==null).collect(Collectors.toList());
                for(ResourceDto resource:resourcesTemp){
                    List<ResourceDto>  res=   resourceDtos.stream().filter(x ->x.getParentId()!=null&& x.getParentId().equals(resource.getId())).collect(Collectors.toList());
                    if(res!=null&&res.size()>0)
                     resource.setChild(res);
                }
                resourceInfo.put(manager.getId(),resourcesTemp);
              }
        }
        return resourceInfo;
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
}
