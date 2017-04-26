package com.sunesoft.seera.yc.core.lottery.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.yc.core.lottery.application.dtos.SubjectDto;

import java.util.List;

/**
 * Created by kkk on 2017/1/9.
 */
public interface SubjectService {

    /**
     * 增加题目
     * @param dto
     * @return
     */
    public CommonResult create(SubjectDto dto);

    /**
     * 移除题目
     * @param ids
     * @return
     */
    public CommonResult remove(List<Long> ids);

    /**
     * 修改题目
     * @param dto
     * @return
     */
    public CommonResult edit(SubjectDto dto);

    /**
     * 查询某个题目
     * @param id
     * @return
     */
    public SubjectDto getById(Long id);

    /**
     * 查询所有题目
     * @param
     * @return
     */
    public List<SubjectDto> getAll();

    /**
     * 查询所有题目 0 status=true 1 所有
     * @param
     * @return
     */
    public List<SubjectDto> getByType(Integer type);

    /**
     * 更新原创题状态
     * @return
     */
    CommonResult updateStatus(Long typeId,Long userId);

    /**
     * 检查原创题状态
     * true 可答题 false 不可答题
     * @param typeId
     * @return
     */
    Boolean checkStatus(long typeId);

    /**
     * 更新领取奖项状态
     * @return
     */
    void updateReceive(long typeId);

}
