package com.yunying.gh.mapper;

import com.yunying.gh.domain.Developer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
public interface DeveloperMapper extends BaseMapper<Developer> {


    /**
     * 根据开发者ID查找相互关注的人
     *
     * @param devId
     * @return
     */
    List<Developer> findMutualFollow(Integer devId);
}
