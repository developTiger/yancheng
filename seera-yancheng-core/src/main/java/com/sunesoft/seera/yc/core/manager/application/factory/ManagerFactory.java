package com.sunesoft.seera.yc.core.manager.application.factory;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.manager.application.dtos.InnerManagerDto;
import com.sunesoft.seera.yc.core.manager.domain.InnerManager;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiazl on 2016/7/28.
 */
public class ManagerFactory extends Factory {

    public static InnerManagerDto convert(InnerManager manager) {
        InnerManagerDto dto = Factory.convert(manager, InnerManagerDto.class);
        if(!manager.getUserRoleList().isEmpty())
            dto.setUserRoleListDto(Factory.convert(manager.getUserRoleList(),RoleDto.class));
        return dto;
    }

    public static List<InnerManagerDto> convert(List<InnerManager> managers) {
        List<InnerManagerDto> list = new ArrayList<>();

        if (managers != null && managers.size() > 0) {
            managers.stream().forEach(i -> list.add(convert(i)));
        }
        return list;
    }

    public static PagedResult<InnerManagerDto> convert(PagedResult<InnerManager> managers) {
        List<InnerManagerDto> list;

        if (managers != null && managers.getTotalItemsCount() > 0) {

            list = convert(managers.getItems());

            return new PagedResult<>(list, managers.getPageNumber(), managers.getPageSize(), managers.getTotalItemsCount());
        }
        return null;
    }
}
