package com.yunying.gh.service.impl;

import com.yunying.gh.domain.Repository;
import com.yunying.gh.mapper.ContributionMapper;
import com.yunying.gh.mapper.RepositoryMapper;
import com.yunying.gh.service.IRepositoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunying.gh.util.NormalizeUtil;
import com.yunying.gh.util.ScoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
@Service
public class RepositoryServiceImpl extends ServiceImpl<RepositoryMapper, Repository> implements IRepositoryService {

    @Autowired
    private ContributionMapper contributionMapper;

    @Autowired
    private RepositoryMapper repositoryMapper;

    /**
     * 计算仓库的重要性得分
     * @param repository
     */
    @Override
    public void calculateImportanceScore(Repository repository) throws NoSuchFieldException, IllegalAccessException {
        // 提取字段
        Integer repoId = repository.getRepoId();
        Integer forkCount = repository.getForkCount();
        Integer watchCount = repository.getWatchCount();
        int commits = contributionMapper.selectCommitCountByRepoId(repoId);
        Integer issueCount = repository.getIssueCount();
        Integer prCount = repository.getPrCount();
        int contributors = contributionMapper.selectCountByRepoId(repoId);
        Integer starCount = repository.getStarCount();

        double normalizedStarCount = NormalizeUtil.normalizeValue(starCount, repositoryMapper, "star_count");
        double normalizedForkCount = NormalizeUtil.normalizeValue(forkCount, repositoryMapper, "fork_count");
        double normalizedWatchCount = NormalizeUtil.normalizeValue(watchCount, repositoryMapper, "watch_count");
        double normalizedCommits = NormalizeUtil.normalizeValue(commits, contributionMapper, "commits");
        double normalizedIssueCount = NormalizeUtil.normalizeValue(issueCount, repositoryMapper, "issue_count");
        double normalizedPrCount = NormalizeUtil.normalizeValue(prCount, repositoryMapper, "pr_count");
        double normalizedContributors = NormalizeUtil.normalizeValue(contributors, contributionMapper, "contributors");

        double originalScore = normalizedStarCount * 0.4
                + normalizedForkCount * 0.2
                + normalizedWatchCount * 0.1
                + normalizedCommits * 0.1
                + normalizedIssueCount * 0.1
                + normalizedPrCount * 0.05
                + normalizedContributors * 0.05;

        float finalScore = ScoreUtil.scaleScore(originalScore);
        // 更新数据库
        Repository updateRepository = new Repository();
        updateRepository.setRepoId(repoId);
        updateRepository.setImportance(finalScore);
        updateById(repository);

    }
}
