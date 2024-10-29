package com.yunying.gh.service.impl;

import com.yunying.gh.domain.Contribution;
import com.yunying.gh.domain.Developer;
import com.yunying.gh.domain.Repository;
import com.yunying.gh.mapper.ContributionMapper;
import com.yunying.gh.mapper.DeveloperMapper;
import com.yunying.gh.service.IDeveloperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunying.gh.util.NormalizeUtil;
import com.yunying.gh.util.ScoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private DeveloperMapper developerMapper;

    @Override
    public void calculateFollowersScore(Developer developer) throws NoSuchFieldException, IllegalAccessException {

        int devId = developer.getDevId();

        // 原始贡献得分
        Integer followers = developer.getFollowers();

        // 计算得分
        double normalizedScore = NormalizeUtil.normalizeValue(followers, developerMapper, "followers");

        normalizedScore = ScoreUtil.scaleScore(normalizedScore);

        // 更新数据库
        developer.setFollowersWeight(normalizedScore);
        updateById(developer);



    }
}
