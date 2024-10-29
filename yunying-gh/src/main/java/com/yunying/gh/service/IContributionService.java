package com.yunying.gh.service;

import com.yunying.gh.domain.Contribution;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunying.gh.domain.Developer;
import com.yunying.gh.domain.Repository;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
public interface IContributionService extends IService<Contribution> {


    /**
     * 计算贡献度分数
     * @param
     */
    void calculateContributionScore(Developer developer, Repository repository) throws NoSuchFieldException, IllegalAccessException;

}
