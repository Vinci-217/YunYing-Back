package com.yunying.gh.service.impl;

import com.yunying.gh.domain.Repository;
import com.yunying.gh.mapper.ContributionMapper;
import com.yunying.gh.mapper.RepositoryMapper;
import com.yunying.gh.service.IRepositoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yunying.gh.util.RepositoryScoreUtil;
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
public class RepositoryServiceImpl extends ServiceImpl<RepositoryMapper, Repository> implements IRepositoryService {

    @Autowired
    private ContributionMapper contributionMapper;

    @Autowired
    private RepositoryMapper repositoryMapper;


    // 权重常量
    private static final double WEIGHT_STAR = 0.4;
    private static final double WEIGHT_FORK = 0.2;
    private static final double WEIGHT_WATCH = 0.1;
    private static final double WEIGHT_COMMITS = 0.1;
    private static final double WEIGHT_ISSUE = 0.1;
    private static final double WEIGHT_PR = 0.05;
    private static final double WEIGHT_CONTRIBUTORS = 0.05;

    /**
     * 计算仓库的重要性得分
     *
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


        double finalScore = RepositoryScoreUtil.calculateFinalScore(starCount, forkCount, watchCount, commits, issueCount, prCount, contributors);
        // 更新数据库
        repository.setImportance((float) finalScore);
        updateById(repository);

    }
}
