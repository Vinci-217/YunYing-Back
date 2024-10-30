package com.yunying.gh.service.impl;

import com.yunying.gh.domain.Contribution;
import com.yunying.gh.domain.Developer;
import com.yunying.gh.domain.Repository;
import com.yunying.gh.mapper.ContributionMapper;
import com.yunying.gh.mapper.DeveloperMapper;
import com.yunying.gh.mapper.RepositoryMapper;
import com.yunying.gh.service.IDeveloperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunying.gh.util.NormalizeUtil;
import com.yunying.gh.util.ScoreUtil;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    @Autowired
    private ContributionMapper contributionMapper;
    
    @Autowired
    private RepositoryMapper repositoryMapper;

    private Graph<Developer, DefaultWeightedEdge> developerGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);


    /**
     * 计算开发者的粉丝分数
     * @param developer
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
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

    /**
     * 计算开发者的TalentRank
     */
    @Override
    public void calculateTalentRank() {
        // 获取所有开发者
        List<Developer> developers = list(); // 使用MyBatis-Plus的方法获取所有开发者

        // 获取该开发者的项目贡献
        Map<Integer, List<Contribution>> contributions = getContributions(developers);

        // 初始化图
        initializeGraph(developers, contributions);

        // 计算 TalentRank
        double dampingFactor = 0.85; // 设定阻尼系数
        Map<Developer, Double> talentRanks = calculateTalentRanks(dampingFactor);

        for(Developer dev : talentRanks.keySet()){

            // 获取该开发者的 TalentRank
            Double rank = talentRanks.get(dev);
            if (rank != null) {
                dev.setTalentRank(rank.floatValue());
                // 更新数据库中的 TalentRank
                updateById(dev);
            }

        }


    }

    // 初始化图结构
    private void initializeGraph(List<Developer> developers, Map<Integer, List<Contribution>> contributions) {
        // 将开发者加入到节点中
        developers.forEach(developerGraph::addVertex);
        for (Map.Entry<Integer, List<Contribution>> entry : contributions.entrySet()) {
            
            Developer developer = findDeveloperById(entry.getKey(), developers);
            for (Contribution contribution : entry.getValue()) {
                Developer targetDeveloper = findDeveloperById(contribution.getDevId(), developers);
                if (developer != null && targetDeveloper != null) {
                    DefaultWeightedEdge edge = developerGraph.addEdge(developer, targetDeveloper);
                    double weight = calculateEdgeWeight(contribution);
                    developerGraph.setEdgeWeight(edge, weight);
                }
            }
        }
    }

    // 计算边的权重
    private double calculateEdgeWeight(Contribution contribution) {
        double repositoryImportanceWeight = 0.4;
        double contributionWeight = 0.6;

        Repository repository = repositoryMapper.selectById(contribution.getRepoId());

        return repositoryImportanceWeight * repository.getImportance() +
                contributionWeight * contribution.getWeight();
    }

    // 通过开发者ID查找开发者
    private Developer findDeveloperById(Integer id, List<Developer> developers) {
        return developers.stream().filter(dev -> dev.getDevId().equals(id)).findFirst().orElse(null);
    }

    // 计算 TalentRank
    private Map<Developer, Double> calculateTalentRanks(double dampingFactor) {
        PageRank<Developer, DefaultWeightedEdge> pageRank = new PageRank<>(developerGraph, dampingFactor);
        Map<Developer, Double> talentRanks = new HashMap<>();

        for (Developer developer : developerGraph.vertexSet()) {
            double rankScore = pageRank.getVertexScore(developer);
            double finalRank = rankScore * 0.7 + developer.getFollowers() * 0.3;
            developer.setTalentRank((float) finalRank);
            talentRanks.put(developer, finalRank);
        }
        return talentRanks;
    }

    // 获取项目贡献的方法
    private Map<Integer, List<Contribution>> getContributions(List<Developer> developers) {
        Map<Integer, List<Contribution>> contributions = new HashMap<>();
        for (Developer developer : developers) {
            List<Contribution> contribution = contributionMapper.selectByDevId(developer.getDevId());
            contributions.put(developer.getDevId(), contribution);
        }
        return contributions;
    }
}
