package com.yunying.gh.service;

import com.yunying.gh.domain.Repository;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
public interface IRepositoryService extends IService<Repository> {


    /**
     * 计算项目的重要性得分
     * @param repository
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    void calculateImportanceScore(Repository repository) throws NoSuchFieldException, IllegalAccessException;




}
