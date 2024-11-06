package com.yunying.gh.service.impl;

import com.yunying.gh.domain.Contribution;
import com.yunying.gh.domain.Developer;
import com.yunying.gh.domain.Repository;
import com.yunying.gh.mapper.ContributionMapper;
import com.yunying.gh.service.IContributionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yunying.gh.util.ContributionScoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
@Service
public class ContributionServiceImpl extends ServiceImpl<ContributionMapper, Contribution> implements IContributionService {


    @Autowired
    private ContributionMapper contributionMapper;


    private static final double WEIGHT_COMMIT = 0.1;
    private static final double WEIGHT_PR = 0.7;
    private static final double WEIGHT_ISSUE = 0.2;


    /**
     * 计算贡献度得分
     *
     * @param developer
     * @param repository
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Override
    public void calculateContributionScore(Developer developer, Repository repository) throws NoSuchFieldException, IllegalAccessException {

        int devId = developer.getDevId();
        int repoId = repository.getRepoId();


        Contribution contribution = contributionMapper.selectByDevIdAndRepoId(devId, repoId);

        // 原始贡献得分
        Integer commitCount = contribution.getCommitCount();
        Integer prCount = contribution.getPrCount();
        Integer issueCount = contribution.getIssueCount();

        double finalScore = ContributionScoreUtil.calculateFinalScore(commitCount, prCount, issueCount);

        // 更新数据库
        contribution.setWeight(finalScore);
        updateById(contribution);

    }

}
