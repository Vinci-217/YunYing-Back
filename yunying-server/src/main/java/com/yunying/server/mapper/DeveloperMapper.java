package com.yunying.server.mapper;

import com.yunying.server.domain.Developer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author vinci
 * @since 2024-10-31
 */
public interface DeveloperMapper extends BaseMapper<Developer> {

    @MapKey("dev_id")
    List<Map<String, Object>> selectByField(String field, Integer limit, Integer offset);

    @MapKey("dev_id")
    List<Map<String, Object>> selectByNation(String nation, Integer limit, Integer offset);

    @MapKey("dev_id")
    List<Map<String, Object>> selectByFieldAndNation(String field, String nation, Integer limit, Integer offset);

    @MapKey("dev_id")
    List<Map<String, Object>> selectByPage(Integer limit, Integer offset);

    List<String> selectNation();

    List<String> selectField();

    @MapKey("repo_id")
    List<Map<String, Object>> selectContribution(Integer devId);

    List<String> selectLanguageByDevId(Integer devId);
}
