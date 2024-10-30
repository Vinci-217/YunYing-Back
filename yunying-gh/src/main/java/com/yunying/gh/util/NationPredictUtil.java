package com.yunying.gh.util;

import com.yunying.gh.domain.Developer;
import org.jgrapht.Graph;
import org.jgrapht.alg.clustering.LabelPropagationClustering;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

public class NationPredictUtil {

    // 创建无向图并添加顶点和边
    public static Graph<Long, DefaultEdge> createGraph(List<Developer> developers,
                                                       List<Integer> followers,
                                                       List<Integer> following) {
        Graph<Long, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // 添加顶点（开发者节点）
        for (Developer dev : developers) {
            graph.addVertex((long) dev.getDevId());
        }

        // 添加边（开发者之间的关系）
        for (Developer dev : developers) {
            for (Integer followerId : followers) {
                graph.addEdge((long) dev.getDevId(), (long) followerId);
            }
            for (Integer followingId : following) {
                graph.addEdge((long) dev.getDevId(), (long) followingId);
            }
        }

        return graph;
    }

    // 进行标签传播算法
    public static Map<Long, NationConfidence> predictNations(Graph<Long, DefaultEdge> graph,
                                                             Map<Long, String> developerNations) {
        LabelPropagationClustering<Long, DefaultEdge> clusterer = new LabelPropagationClustering<>(graph, new Random());
        List<Set<Long>> clusters = clusterer.getClustering().getClusters();

        // 预测标签并计算置信度
        Map<Long, NationConfidence> results = new HashMap<>();
        for (long vertex : graph.vertexSet()) {
            String predictedNation = developerNations.getOrDefault(vertex, "N/A");
            double confidence = calculateConfidence(vertex, graph, developerNations);

            // 如果置信度低于阈值，将标签设置为 N/A
            String displayNation = confidence < 0.1 ? "N/A" : predictedNation;
            results.put(vertex, new NationConfidence(displayNation, confidence));
        }

        return results;
    }

    // 计算置信度
    private static double calculateConfidence(Long vertex, Graph<Long, DefaultEdge> graph, Map<Long, String> developerNations) {
        List<Long> neighbors = graph.outgoingEdgesOf(vertex).stream()
                .map(edge -> graph.getEdgeTarget(edge) == vertex ? graph.getEdgeSource(edge) : graph.getEdgeTarget(edge))
                .toList();
        long sameLabelCount = neighbors.stream()
                .filter(neighbor -> developerNations.get(neighbor).equals(developerNations.get(vertex)))
                .count();
        return neighbors.isEmpty() ? 0.0 : (double) sameLabelCount / neighbors.size();
    }

    // 自定义类存储 Nation 预测和置信度信息
    public static class NationConfidence {
        String nation;
        double confidence;

        public NationConfidence(String nation, double confidence) {
            this.nation = nation;
            this.confidence = confidence;
        }

        @Override
        public String toString() {
            return "Nation: " + nation + ", Confidence: " + confidence;
        }
    }
}
