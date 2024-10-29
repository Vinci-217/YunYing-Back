package com.yunying.gh.service.impl;

import com.yunying.gh.domain.Repository;
import com.yunying.gh.mapper.ContributionMapper;
import com.yunying.gh.mapper.RepositoryMapper;
import com.yunying.gh.service.IRepositoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunying.gh.util.NormalizeUtil;
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
    public void calculateImportanceScore(Repository repository) {
        // 提取字段
        Integer repoId = repository.getRepoId();
        Integer forkCount = repository.getForkCount();
        Integer watchCount = repository.getWatchCount();
        int commits = contributionMapper.selectCommitCountByRepoId(repoId);
        Integer issueCount = repository.getIssueCount();
        Integer prCount = repository.getPrCount();
        int contributors = contributionMapper.selectCountByRepoId(repoId);
        Integer starCount = repository.getStarCount();

        double originalScore = starCount * 0.4
                + forkCount * 0.2
                + watchCount * 0.1
                + commits * 0.1
                + issueCount * 0.1
                + prCount * 0.05
                + contributors * 0.05;

        // 假设 originalScore 在 0-1 区间内，将得分缩放到 1-10
        BigDecimal scaledScore = BigDecimal.valueOf(1 + originalScore * 9);
        scaledScore = scaledScore.setScale(2, RoundingMode.HALF_UP); // 保留2位小数

        // 更新数据库
        Repository updateRepository = new Repository();
        updateRepository.setRepoId(repoId);
        updateRepository.setImportance(scaledScore.floatValue());
        updateById(repository);

    }
}
