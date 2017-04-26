package com.sunesoft.seera.yc.core.uAuth.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.UserCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysRole;
import com.sunesoft.seera.yc.core.uAuth.domain.SysRoleRepository;
import com.sunesoft.seera.yc.core.uAuth.domain.SysUser;
import com.sunesoft.seera.yc.core.uAuth.domain.SysUserRepository;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.UserCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserDto;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouz on 2016/5/19.
 */
@Service("sysUserService")
public class SysUserServiceImpl extends GenericHibernateFinder implements SysUserService {

    @Autowired
    SysUserRepository userRepository;

    @Autowired
    SysRoleRepository roleRepository;

    @Autowired
    SysRoleService roleService;

    /**
     * @param dto 用户信息
     * @return
     */
    @Override
    public CommonResult addorUpdateUser(UserDto dto) {
        List<UserDto> list=null;
        SysUser user=null;
        boolean addFlag=false;
        if (dto.getLoginName() != null) {
            list = getAllUser(dto.getLoginName());
        }
        if (dto.getId() != null && dto.getId() > 0) {
             user = userRepository.get(dto.getId());
            if (user != null && user.getIsActive()) {
                addFlag=true;
                if (!user.getLoginName().equals(dto.getLoginName())) {
                    if(list!=null&&list.size()>0) {
                        return ResultFactory.commonError("修改失败，该用户名已经存在");
                    }
                }
                user=DtoFactory.convert(dto,user);
                user.setLastUpdateTime(new Date());
                return  ResultFactory.commonSuccess(userRepository.save(user));
            }
        }
        if(!addFlag){
            if(list!=null&&list.size()>0) {
                return ResultFactory.commonError("新增失败，该用户名已经存在");
            }
            user=DtoFactory.convert(dto,new SysUser());
            return ResultFactory.commonSuccess(userRepository.save(user));
        }
        return ResultFactory.commonError("程序有误");
    }

    @Override
    public UserSessionDto login(String userName, String password) {
        Criteria criterion = getSession().createCriteria(SysUser.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("loginName", userName));
        criterion.add(Restrictions.eq("password", password));
        criterion.add(Restrictions.eq("status", 1));
        List<SysUser> list = criterion.list();
        if (list != null && list.size() > 0) {
            ((SysUser) list.get(0)).setLastLoginTime(new Date());
            int i = ((SysUser) list.get(0)).getLoginCount();
            ((SysUser) list.get(0)).setLoginCount(++i);
            return DtoFactory.convert(list.get(0), new UserSessionDto());
        }
        return null;
    }


    /**
     * 修改用户的password
     *
     * @param id
     * @param password
     * @return
     */
    @Override
    public CommonResult changePassword(Long id, String password) {
        SysUser user = userRepository.get(id);
        if (user == null || !user.getIsActive() || user.getStatus() != 1) {
            return ResultFactory.commonError("该账户不存在，或已经被禁用");
        }
        user.setPassword(password);
        Date time = new Date();
        user.setLastUpdateTime(time);
        userRepository.save(user);
        return ResultFactory.commonSuccess();
    }


    @Override
    public CommonResult setUserStatus(List<Long> ids, int status) {

        Criteria criterion = getSession().createCriteria(SysUser.class);
        criterion.add(Restrictions.eq("isActive", true));
        if (ids != null && ids.size() > 0) {
            criterion.add(Restrictions.in("id", ids));
        }
        List<SysUser> beans = criterion.list();
        if (beans != null && beans.size() > 0) {
            for (SysUser bean : beans) {
                bean.setStatus(status);
                Date time = new Date();
                bean.setLastUpdateTime(time);
                userRepository.save(bean);
            }
        }
        return ResultFactory.commonSuccess();
    }


    @Override
    public PagedResult<UserDto> FindUser(UserCriteria searchCriteria) {
        Criteria criterion = getSession().createCriteria(SysUser.class);
        criterion.add(Restrictions.eq("isActive", true));
        if (!StringUtils.isNullOrWhiteSpace(searchCriteria.getUserName())) {
            criterion.add(Restrictions.like("name", "%" + searchCriteria.getUserName() + "%"));
        }
        if (!StringUtils.isNullOrWhiteSpace(searchCriteria.getPhone())) {
            criterion.add(Restrictions.like("phone", "%" + searchCriteria.getPhone() + "%"));
        }
        if (!StringUtils.isNullOrWhiteSpace(searchCriteria.getLoginName())) {
            criterion.add(Restrictions.like("loginName", "%" + searchCriteria.getLoginName() + "%"));
        }
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setFirstResult((searchCriteria.getPageNumber() - 1) * searchCriteria.getPageSize()).setMaxResults(searchCriteria.getPageSize());
        List<SysUser> beans = criterion.list();
        List<UserDto> list = new ArrayList<UserDto>();
        if (beans != null && beans.size() > 0) {
            for (SysUser user : beans) {
                list.add(DtoFactory.convert(user, new UserDto()));
            }
        }
        //  System.out.println(JsonHelper.toJson(beans));
        return new PagedResult<UserDto>(list, searchCriteria.getPageNumber(), searchCriteria.getPageSize(), totalCount);
    }


    @Override
    public CommonResult delete(List<Long> ids) {
        Criteria criterion = getSession().createCriteria(SysUser.class);
        criterion.add(Restrictions.eq("isActive", true));
        if (ids != null && ids.size() > 0) {
            criterion.add(Restrictions.in("id", ids));
        }
        List<SysUser> list = criterion.list();
        if (list != null && list.size() > 0) {
            for (SysUser user : list) {
                user.setIsActive(false);
                user.setLastUpdateTime(new Date());
                userRepository.save(user);
            }
        }
        return ResultFactory.commonSuccess();
    }


    @Override
    public List<UserDto> getAllUser() {
        Criteria criterion = getSession().createCriteria(SysUser.class);
        criterion.add(Restrictions.eq("isActive", true));
        List<SysUser> list1 = criterion.list();
        List<UserDto> list = new ArrayList<UserDto>();
        if (list1 != null && list.size() > 0) {
            for (SysUser user : list1) {
                list.add(DtoFactory.convert(user, new UserDto()));
            }
        }
        return list;
    }


    @Override
    public UserDto getById(Long id) {
        SysUser user = userRepository.get(id);
        if (user != null && user.getIsActive()) {
            return DtoFactory.convert(user, new UserDto());
        }
        return null;
    }


    @Override
    public List<UserDto> getAllUser(String loginName) {
        Criteria criterion = getSession().createCriteria(SysUser.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("loginName", loginName));
        List<SysUser> list1 = criterion.list();
        List<UserDto> list = new ArrayList<UserDto>();
        if (list1 != null && list.size() > 0) {
            for (SysUser user : list1) {
                list.add(DtoFactory.convert(user, new UserDto()));
            }
        }
        return list;
    }

    @Override
    public UserSessionDto GetUserSessionDtoById(long uid) {
        SysUser user = userRepository.get(uid);
        return DtoFactory.convert(user, new UserSessionDto());
    }

}
