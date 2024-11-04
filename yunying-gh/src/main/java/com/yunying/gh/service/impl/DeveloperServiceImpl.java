package com.yunying.gh.service.impl;

import com.yunying.gh.domain.Contribution;
import com.yunying.gh.domain.Developer;
import com.yunying.gh.domain.Repository;
import com.yunying.gh.mapper.ContributionMapper;
import com.yunying.gh.mapper.DeveloperMapper;
import com.yunying.gh.mapper.RepositoryMapper;
import com.yunying.gh.service.IDeveloperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunying.gh.util.NationPredictUtil;
import com.yunying.gh.util.NormalizeUtil;
import com.yunying.gh.util.ScoreUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param devId
     * @throws IllegalAccessException
     */
    @Override
    public void calculateFollowersScore(int devId) throws NoSuchFieldException, IllegalAccessException {


        Developer developer = developerMapper.selectById(devId);

        // 原始贡献得分
        Integer followers = developer.getFollowers();

        // 计算得分
        double normalizedScore = NormalizeUtil.normalizeValue(followers, developerMapper, "followers");

        normalizedScore = ScoreUtil.scaleScore(normalizedScore);

        // 更新数据库
        developer.setFollowersWeight(normalizedScore);
        updateById(developer);


    }

    /**
     * 计算开发者的TalentRank
     */
    @Override
    public void calculateTalentRank(int devId) {
        Developer developer = developerMapper.selectById(devId);
        // 1. 计算仓库贡献得分
        List<Contribution> contributions = contributionMapper.selectByDevId(devId);

        List<Repository> repositories = contributionMapper.selectRepoByDevId(devId);


    }

    /**
     * 预测开发者的nation，并填入数据库
     *
     * @param devId
     */
    @Override
    public void propagateNation(int devId) {
        Developer centerDeveloper = developerMapper.selectById(devId);
        if (centerDeveloper == null) return;

        // 1. 创建图
        Graph<Developer, DefaultEdge> graph = NationPredictUtil.buildGraph(centerDeveloper, developerMapper, 7);

        // 2. 标记国家
        Map<Developer, String> nationMap = new HashMap<>();
        for (Developer dev : graph.vertexSet()) {
            if (dev.getNation() != null) {
                nationMap.put(dev, dev.getNation());
            }
        }

        // 3. 使用标签传播算法推测国家和置信度
        Map<Developer, Double> confidenceMap = NationPredictUtil.inferNationsWithConfidence(graph, nationMap, 100); // 可以调整最大迭代次数

        // 4. 更新开发者的国家和置信度
        for (Map.Entry<Developer, Double> entry : confidenceMap.entrySet()) {
            Developer dev = entry.getKey();
            String inferredNation = nationMap.get(dev);
            dev.setNation(inferredNation);
            dev.setNationConf(entry.getValue()); // 假设Developer类有confidence字段
            developerMapper.updateById(dev);
        }
    }


}
