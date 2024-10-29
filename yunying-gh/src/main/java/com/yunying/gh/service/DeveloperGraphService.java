package com.yunying.gh.service;

import com.yunying.gh.domain.Contribution;
import com.yunying.gh.domain.Developer;
import com.yunying.gh.domain.Repository;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.alg.scoring.PageRank;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeveloperGraphService {

    private static final double REPOSITORY_IMPORTANCE_WEIGHT = 0.4;
    private static final double CONTRIBUTION_WEIGHT = 0.6;
    private static final double RANK_SCORE_WEIGHT = 0.7;
    private static final double FOLLOWERS_COUNT_WEIGHT = 0.3;

    private final Graph<Developer, DefaultWeightedEdge> developerGraph;

    public DeveloperGraphService() {
        this.developerGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }

    // 初始化图结构，将开发者和仓库贡献关系作为边加入图中
    public void initializeGraph(List<Developer> developers,
                                Map<Integer, List<Contribution>> contributions,
                                Map<Integer, Repository> repositories) {
        // 添加开发者节点
        developers.forEach(developerGraph::addVertex);

        // 根据贡献，添加边并设置权重
        contributions.forEach((developerId, contributionList) -> {
            Developer developer = findDeveloperById(developerId, developers);
            if (developer != null) {
                contributionList.forEach(contribution -> {
                    Developer targetDeveloper = findDeveloperById(contribution.getDevId(), developers);
                    if (targetDeveloper != null) {
                        Repository repository = repositories.get(contribution.getRepoId());
                        if (repository != null) {
                            DefaultWeightedEdge edge = developerGraph.addEdge(developer, targetDeveloper);
                            double weight = calculateEdgeWeight(contribution, repository);
                            developerGraph.setEdgeWeight(edge, weight);
                        }
                    }
                });
            }
        });
    }

    // 计算边的权重，基于贡献权重和仓库重要性评分
    private double calculateEdgeWeight(Contribution contribution, Repository repository) {
        double contributionWeight = contribution.getWeight() != null ? contribution.getWeight() : 0.0;
        double repositoryImportance = repository.getImportance() != null ? repository.getImportance() : 0.0;
        return (contributionWeight * CONTRIBUTION_WEIGHT) + (repositoryImportance * REPOSITORY_IMPORTANCE_WEIGHT);
    }

    // 通过开发者ID查找开发者
    private Developer findDeveloperById(Integer id, List<Developer> developers) {
        return developers.stream()
                .filter(dev -> dev.getDevId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // 计算并返回开发者的 TalentRank
    public Map<Developer, Double> calculateTalentRanks(double dampingFactor) {
        PageRank<Developer, DefaultWeightedEdge> pageRank = new PageRank<>(developerGraph, dampingFactor);
        Map<Developer, Double> talentRanks = new HashMap<>();

        for (Developer developer : developerGraph.vertexSet()) {
            // 获取 PageRank 分数并结合粉丝数计算最终的 TalentRank
            double rankScore = pageRank.getVertexScore(developer);
            double finalRank = rankScore * RANK_SCORE_WEIGHT + developer.getFollowers() * FOLLOWERS_COUNT_WEIGHT;
            developer.setTalentRank((float) finalRank); // 假设需要转换为 Float
            talentRanks.put(developer, finalRank);
        }
        return talentRanks;
    }
}
