package com.yunying.gh.service;

import com.yunying.gh.domain.Contribution;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
public interface IContributionService extends IService<Contribution> {


    /**
     * 计算贡献度分数
     *
     * @param
     * @param contribution
     */
    void calculateContributionScore(Contribution contribution) throws NoSuchFieldException, IllegalAccessException;


    /**
     * 预测字段
     *
     * @param contribution
     */
    void predictField(@Param("contribution") Contribution contribution);
}
