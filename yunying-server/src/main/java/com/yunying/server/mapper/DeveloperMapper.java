package com.yunying.server.mapper;

import com.yunying.server.domain.Developer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vinci
 * @since 2024-10-31
 */
public interface DeveloperMapper extends BaseMapper<Developer> {

    List<Developer> selectByField(String field, Integer page, Integer pageSize);

    List<Developer> selectByNation(String nation, Integer page, Integer pageSize);

    List<Developer> selectByFieldAndNation(String field, String nation, Integer page, Integer pageSize);

    List<Developer> selectByPage(Integer limit, Integer offset);
}
