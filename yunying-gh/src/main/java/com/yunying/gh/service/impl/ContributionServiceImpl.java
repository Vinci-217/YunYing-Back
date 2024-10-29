package com.yunying.gh.service.impl;

import com.yunying.gh.domain.Contribution;
import com.yunying.gh.domain.Developer;
import com.yunying.gh.domain.Repository;
import com.yunying.gh.mapper.ContributionMapper;
import com.yunying.gh.service.IContributionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunying.gh.util.NormalizeUtil;
import com.yunying.gh.util.ScoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
@Service
public class ContributionServiceImpl extends ServiceImpl<ContributionMapper, Contribution> implements IContributionService {


    @Autowired
    private ContributionMapper contributionMapper;


    private static final double WEIGHT_COMMIT = 0.2;
    private static final double WEIGHT_PR = 0.6;
    private static final double WEIGHT_ISSUE = 0.2;


    // 计算贡献得分
    @Override
    public void calculateContributionScore(Developer developer, Repository repository) throws NoSuchFieldException, IllegalAccessException {

        int devId = developer.getDevId();
        int repoId = repository.getRepoId();


        Contribution contribution = contributionMapper.selectByDevIdAndRepoId(devId, repoId);

        // 原始贡献得分
        Integer commitCount = contribution.getCommitCount();
        Integer prCount = contribution.getPrCount();
        Integer issueCount = contribution.getIssueCount();

        double normalizedCommitCount = NormalizeUtil.normalizeValue(commitCount,contributionMapper,"commit_count");
        double normalizedPrCount = NormalizeUtil.normalizeValue(prCount,contributionMapper,"pr_count");
        double normalizedIssueCount = NormalizeUtil.normalizeValue(issueCount,contributionMapper,"issue_count");

        double originalScore = normalizedCommitCount * WEIGHT_COMMIT
                + normalizedPrCount * WEIGHT_PR
                + normalizedIssueCount * WEIGHT_ISSUE;

        double finalScore = ScoreUtil.scaleScore(originalScore);

        // 更新数据库
        contribution.setWeight(finalScore);
        updateById(contribution);

    }

}
