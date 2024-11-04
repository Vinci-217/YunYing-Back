package com.yunying.gh.util;

import com.yunying.gh.domain.Developer;
import com.yunying.gh.mapper.DeveloperMapper;
import org.jgrapht.Graph;
import org.jgrapht.alg.clustering.LabelPropagationClustering;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NationPredictUtil {

    public static Graph<Developer, DefaultEdge> buildGraph(Developer centerDeveloper, DeveloperMapper developerMapper, int depth) {
        Graph<Developer, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        buildGraphRecursive(graph, centerDeveloper, developerMapper, depth, 0);
        return graph;
    }

    private static void buildGraphRecursive(Graph<Developer, DefaultEdge> graph, Developer developer,
                                            DeveloperMapper developerMapper, int depth, int currentDepth) {
        if (currentDepth >= depth) return;
        graph.addVertex(developer);

        List<Developer> relatedDevs = developerMapper.findMutualFollow(developer.getDevId());
        for (Developer relatedDev : relatedDevs) {
            graph.addVertex(relatedDev);
            graph.addEdge(developer, relatedDev);
            buildGraphRecursive(graph, relatedDev, developerMapper, depth, currentDepth + 1);
        }
    }

    public static Map<Developer, Double> inferNationsWithConfidence(Graph<Developer, DefaultEdge> graph, Map<Developer, String> nationMap, int maxIterations) {
        Map<Developer, Double> confidenceMap = new HashMap<>();

        // 使用新的 LabelPropagationClustering 实现
        LabelPropagationClustering<Developer, DefaultEdge> clustering = new LabelPropagationClustering<>(graph, maxIterations);
        List<Set<Developer>> clusters = clustering.getClustering().getClusters();

        for (Set<Developer> cluster : clusters) {
            String inferredNation = inferNation(cluster, nationMap);
            if (inferredNation != null) {
                double confidence = calculateConfidence(cluster, inferredNation, nationMap);
                for (Developer dev : cluster) {
                    if (dev.getNation() == null) {  // 只对未标记的开发者推断
                        confidenceMap.put(dev, confidence);
                    }
                }
            }
        }
        return confidenceMap;
    }

    private static String inferNation(Set<Developer> cluster, Map<Developer, String> nationMap) {
        Map<String, Integer> nationCount = new HashMap<>();
        for (Developer dev : cluster) {
            String nation = nationMap.get(dev);
            if (nation != null) {
                nationCount.put(nation, nationCount.getOrDefault(nation, 0) + 1);
            }
        }
        return nationCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static double calculateConfidence(Set<Developer> cluster, String inferredNation, Map<Developer, String> nationMap) {
        long totalKnown = cluster.stream().filter(dev -> nationMap.get(dev) != null).count();
        long inferredCount = cluster.stream().filter(dev -> inferredNation.equals(nationMap.get(dev))).count();
        return totalKnown > 0 ? (double) inferredCount / totalKnown : 0.0;
    }
}
