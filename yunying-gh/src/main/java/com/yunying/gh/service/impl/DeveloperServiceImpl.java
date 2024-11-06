package com.yunying.gh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunying.gh.domain.Developer;
import com.yunying.gh.domain.Follower;
import com.yunying.gh.domain.Following;
import com.yunying.gh.mapper.ContributionMapper;
import com.yunying.gh.mapper.DeveloperMapper;
import com.yunying.gh.mapper.RepositoryMapper;
import com.yunying.gh.service.IDeveloperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunying.gh.util.DeveloperNationPredictionUtil;
import com.yunying.gh.util.FollowerScoreUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
@Service
public class DeveloperServiceImpl extends ServiceImpl<DeveloperMapper, Developer> implements IDeveloperService {


    @Autowired
    private DeveloperMapper developerMapper;

    @Autowired
    private ContributionMapper contributionMapper;

    @Autowired
    private RepositoryMapper repositoryMapper;

    private static final double WEIGHT_REPOSITORY = 0.4;
    private static final double WEIGHT_CONTRIBUTION = 0.4; // 权重示例
    private static final double WEIGHT_FOLLOWERS = 0.2; // 权重示例

    /**
     * 计算开发者的粉丝分数
     *
     * @param devLogin
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public boolean calculateFollowersScore(String devLogin) throws NoSuchFieldException, IllegalAccessException {

        QueryWrapper<Developer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dev_login", devLogin);

        Developer developer = developerMapper.selectOne(queryWrapper);

        // 原始贡献得分
        Integer followers = developer.getFollowers();

        // 计算得分
        double finalScore = FollowerScoreUtil.evaluateFans(followers);

        // 更新数据库
        developer.setFollowersWeight(finalScore);
        System.out.println("更新粉丝分数成功");
        return updateById(developer);


    }

    /**
     * 计算开发者的TalentRank
     */
    @Override
    public void calculateTalentRank(int devId) {
//        Developer developer = developerMapper.selectById(devId);
//        // 1. 计算仓库贡献得分
//        List<Contribution> contributions = contributionMapper.selectByDevId(devId);
//
//        List<Repository> repositories = contributionMapper.selectRepoByDevId(devId);
//

    }

    /**
     * 预测开发者的nation，并填入数据库
     *
     * @param devLogin
     * @return
     */
    @Override
    public boolean propagateNation(String devLogin) {

        QueryWrapper<Developer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dev_login", devLogin);
//
        Developer centerDeveloper = developerMapper.selectOne(queryWrapper);
        if (centerDeveloper == null) return false;

        List<Follower> followers = developerMapper.selectFollowers(centerDeveloper.getDevId());
        List<Following> followings = developerMapper.selectFollowing(centerDeveloper.getDevId());

        List<Developer> followerDevelopers = new ArrayList<>();
        for (Follower follower : followers) {
            Developer developer = new Developer();
            developer.setDevId(follower.getDevId());
            developer.setDevLogin(follower.getDevLogin());
            developer.setNation(follower.getLocation());
            followerDevelopers.add(developer);
        }

        List<Developer> followingDevelopers = new ArrayList<>();
        for (Following following : followings) {
            Developer developer = new Developer();
            developer.setDevId(following.getDevId());
            developer.setDevLogin(following.getDevLogin());
            developer.setNation(following.getLocation());
            followingDevelopers.add(developer);
        }

        String nation = DeveloperNationPredictionUtil.predictNation(centerDeveloper, followerDevelopers, followingDevelopers);
        centerDeveloper.setNation(nation);
        updateById(centerDeveloper);
        return true;
    }


}
