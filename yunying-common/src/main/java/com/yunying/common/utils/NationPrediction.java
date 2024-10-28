package com.yunying.common.utils;

import org.jgrapht.Graph;
import org.jgrapht.alg.clustering.LabelPropagationClustering;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

public class NationPrediction {

    public static void main(String[] args) {
        // 创建无向图
        Graph<Long, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // 添加顶点（开发者节点）
        for (long i = 1; i <= 4; i++) {
            graph.addVertex(i);
        }

        // 添加边（开发者之间的关系）
        graph.addEdge(1L, 2L);
        graph.addEdge(2L, 3L);
        graph.addEdge(3L, 4L);

        // 初始化开发者节点的标签（nation）
        Map<Long, String> developerNations = new HashMap<>();
        developerNations.put(1L, "US");
        developerNations.put(2L, "CN");  // 无标签
        developerNations.put(3L, "CN");
        developerNations.put(4L, "CN");  // 无标签

        // 使用标签传播算法，创建LabelPropagationClustering实例
        LabelPropagationClustering<Long, DefaultEdge> clusterer = new LabelPropagationClustering<>(graph, new Random());

        // 计算聚类结果
        List<Set<Long>> clusters = clusterer.getClustering().getClusters();

        // 预测标签并计算置信度
        Map<Long, NationConfidence> results = new HashMap<>();

        for (long vertex : graph.vertexSet()) {
            String predictedNation = developerNations.getOrDefault(vertex, "");
            double confidence = calculateConfidence(vertex, graph, developerNations);

            // 如果置信度低于阈值，将标签设置为 N/A
            String displayNation = confidence < 0.1 ? "N/A" : predictedNation;
            results.put(vertex, new NationConfidence(displayNation, confidence));
        }

        // 输出结果
        results.forEach((id, nationConfidence) -> {
            System.out.println("Developer ID: " + id + ", " + nationConfidence);
        });
    }

    // 计算置信度
    private static double calculateConfidence(Long vertex, Graph<Long, DefaultEdge> graph, Map<Long, String> developerNations) {
        List<Long> neighbors = graph.outgoingEdgesOf(vertex).stream()
                .map(edge -> graph.getEdgeTarget(edge) == vertex ? graph.getEdgeSource(edge) : graph.getEdgeTarget(edge))
                .toList();
        long sameLabelCount = neighbors.stream()
                .filter(neighbor -> developerNations.get(neighbor).equals(developerNations.get(vertex)))
                .count();
        return (double) sameLabelCount / neighbors.size();
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
