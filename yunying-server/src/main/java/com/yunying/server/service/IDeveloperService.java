package com.yunying.server.service;

import com.yunying.server.domain.Developer;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author vinci
 * @since 2024-10-31
 */
public interface IDeveloperService extends IService<Developer> {

    /**
     * 根据领域查询开发者信息
     *
     * @param field
     * @param page
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> selectByField(@Param("field") String field, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 根据领域查询开发者信息
     *
     * @param nation
     * @param page
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> selectByNation(@Param("nation") String nation, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 根据领域和民族查询开发者信息
     *
     * @param field
     * @param nation
     * @param page
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> selectByFieldAndNation(@Param("field") String field, @Param("nation") String nation, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 查询所有开发者信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> selectByPage(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 查询所有国家
     *
     * @return
     */
    List<String> selectNation();

    /**
     * 查询所有领域
     *
     * @return
     */
    List<String> selectField();

    /**
     * 根据开发者id查询开发者信息
     *
     * @param devId
     * @return
     */
    Developer selectByDevId(@Param("devId") Integer devId);

    /**
     * 根据开发者id查询开发者贡献信息
     *
     * @param devId
     * @return
     */
    List<Map<String, Object>> selectContribution(@Param("devId") Integer devId);

    /**
     * 根据开发者id查询开发者语言信息
     *
     * @param devId
     * @return
     */
    Map<String, Integer> selectLanguageByDevId(@Param("devId") Integer devId);
}
