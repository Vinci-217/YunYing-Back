package com.yunying.gh.service.impl;

import com.yunying.gh.domain.Developer;
import com.yunying.gh.mapper.DeveloperMapper;
import com.yunying.gh.service.IDeveloperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
@Service
public class DeveloperServiceImpl extends ServiceImpl<DeveloperMapper, Developer> implements IDeveloperService {


    // 计算贡献得分
    public BigDecimal calculateContributionScore(Developer developer) {
        // 定义权重
        double weightCommit = 0.2;
        double weightPR = 0.6;
        double weightIssue = 0.2;

        // 原始贡献得分
        double originalScore = developer.getCommitCount() * weightCommit
                + developer.getPullRequestCount() * weightPR
                + developer.getIssueCount() * weightIssue;


    }
}
