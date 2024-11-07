package com.yunying.gh.util;

import com.yunying.gh.domain.Developer;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.alg.clustering.LabelPropagationClustering;
import org.jgrapht.alg.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class DeveloperNationPredictionUtil {

    /**
     * 根据开发者的实体，粉丝列表，关注者列表，使用标签传播算法预测nation，并计算置信度。
     *
     * @param developer 开发者实体
     * @param followers 开发者的粉丝列表
     * @param following 开发者的关注者列表
     * @return 包含预测的nation和置信度的字符串数组
     */
    public static String[] predictNationWithConfidence(Developer developer, List<Developer> followers, List<Developer> following) {
        // 创建图
        Graph<Developer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // 将开发者和他们的粉丝及关注者加入图中
        graph.addVertex(developer);
        followers.forEach(graph::addVertex);
        following.forEach(graph::addVertex);

        // 为开发者添加与粉丝及关注者之间的边
        followers.forEach(follower -> graph.addEdge(developer, follower));
        following.forEach(followingDev -> graph.addEdge(developer, followingDev));

        // 使用LabelPropagationClustering进行标签传播
        LabelPropagationClustering<Developer, DefaultEdge> clustering = new LabelPropagationClustering<>(graph);
        List<Set<Developer>> clusters = clustering.getClustering().getClusters();

        // 查找开发者所在的社区
        Set<Developer> devCluster = null;
        for (Set<Developer> cluster : clusters) {
            if (cluster.contains(developer)) {
                devCluster = cluster;
                break;
            }
        }

        if (devCluster == null) {
            // 如果找不到开发者所在的社区，返回默认的nation和置信度0
            return new String[]{"CN", "0"};
        }

        // 从开发者所在的社区中，统计每个国家出现的次数，跳过nation为空的开发者
        Map<String, Long> nationCounts = devCluster.stream()
                .filter(dev -> dev.getNation() != null && !dev.getNation().isEmpty())  // 跳过nation为空的开发者
                .collect(Collectors.groupingBy(Developer::getNation, Collectors.counting()));

        // 如果社区内没有有效的国家数据，返回默认的结果
        if (nationCounts.isEmpty()) {
            return new String[]{"CN", "0"};
        }

        // 选择出现次数最多的国家作为预测结果
        Optional<Map.Entry<String, Long>> predictedNation = nationCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        // 计算置信度：出现次数最多的国家的比例
        long totalCount = devCluster.size();
        long predictedNationCount = predictedNation.map(Map.Entry::getValue).orElse(0L);
        double confidence = (double) predictedNationCount / totalCount;

        // 如果无法预测（所有节点国家数量相同），返回默认的国家"CN"和置信度0
        String nation = predictedNation.map(Map.Entry::getKey).orElse("CN");
        String confidenceStr = nation.equals("CN") ? "0" : String.format("%.2f", confidence);

        return new String[]{nation, confidenceStr};
    }

    // 测试方法
//    public static void main(String[] args) {
//        // 创建一个开发者和一些粉丝与关注者的示例
//        Developer developer = new Developer(1, "dev1", "Dev One", "USA");
//        Developer follower1 = new Developer(2, "dev2", "Dev Two", "USA");
//        Developer follower2 = new Developer(3, "dev3", "Dev Three", "Canada");
//        Developer follower3 = new Developer(4, "dev4", "Dev Four", "Canada");
//        Developer following1 = new Developer(5, "dev5", "Dev Five", "USA");
//        Developer following2 = new Developer(6, "dev6", "Dev Six", "Canada");
//
//        List<Developer> followers = Arrays.asList(follower1, follower2, follower3);
//        List<Developer> following = Arrays.asList(following1, following2);
//
//        // 预测开发者的nation和置信度
//        String[] result = predictNationWithConfidence(developer, followers, following);
//        System.out.println("预测的Nation: " + result[0]);
//        System.out.println("置信度: " + result[1]);
//    }
}
