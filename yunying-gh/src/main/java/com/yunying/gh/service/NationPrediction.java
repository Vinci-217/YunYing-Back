package com.yunying.gh.service;

import org.jgrapht.Graph;
import org.jgrapht.alg.clustering.LabelPropagationClustering;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class NationPrediction {

    private static final String GITHUB_API_URL = "https://api.github.com/users/";

    public static void main(String[] args) {
        // 设定起始用户和最大深度
        String startUser = "your_github_username"; // 替换为目标 GitHub 用户名
        Graph<Long, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // 构建关系图
        buildGraph(graph, startUser, new HashSet<>(), 0);

        // 初始化开发者节点的标签（nation）
        Map<Long, String> developerNations = new HashMap<>();
        // 这里可以根据需要初始化国家标签

        // 使用标签传播算法，创建LabelPropagationClustering实例
        LabelPropagationClustering<Long, DefaultEdge> clusterer = new LabelPropagationClustering<>(graph, new Random());

        // 计算聚类结果
        List<Set<Long>> clusters = clusterer.getClustering().getClusters();

        // 预测标签并计算置信度
        Map<Long, NationConfidence> results = new HashMap<>();
        for (long vertex : graph.vertexSet()) {
            String predictedNation = developerNations.getOrDefault(vertex, "N/A");
            double confidence = calculateConfidence(vertex, graph, developerNations);

            // 如果置信度低于阈值，将标签设置为 N/A
            results.put(vertex, new NationConfidence(confidence < 0.1 ? "N/A" : predictedNation, confidence));
        }

        // 输出结果
        results.forEach((id, nationConfidence) -> {
            System.out.println("Developer ID: " + id + ", " + nationConfidence);
        });
    }

    // 递归构建关系图
    private static void buildGraph(Graph<Long, DefaultEdge> graph, String username, Set<String> visited, int depth) {
        if (depth >= 7 || visited.contains(username)) {
            return;
        }

        visited.add(username);
        List<Long> followers = getFollowers(username);
        List<Long> following = getFollowing(username);

        // 添加用户到图中
        long userId = username.hashCode(); // 假设用户ID可以用用户名的哈希值表示
        graph.addVertex(userId);

        for (Long follower : followers) {
            graph.addVertex(follower);
            graph.addEdge(userId, follower);
            buildGraph(graph, follower.toString(), visited, depth + 1);
        }

        for (Long followee : following) {
            graph.addVertex(followee);
            graph.addEdge(userId, followee);
            buildGraph(graph, followee.toString(), visited, depth + 1);
        }
    }

    // 获取用户的粉丝
    private static List<Long> getFollowers(String username) {
        return fetchUserList(username + "/followers");
    }

    // 获取用户关注的人
    private static List<Long> getFollowing(String username) {
        return fetchUserList(username + "/following");
    }

    // 从 GitHub API 获取用户列表
    private static List<Long> fetchUserList(String endpoint) {
        List<Long> userIds = new ArrayList<>();
        try {
            URL url = new URL(GITHUB_API_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // 解析 JSON 数据，假设有一个 id 字段
                    // 这里需要根据 GitHub API 返回的实际格式解析用户 ID
                    long userId = parseUserId(inputLine);
                    userIds.add(userId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userIds;
    }

    // 解析用户 ID（假设输入是一行 JSON 数据）
    private static long parseUserId(String json) {
        // 简单示例，实际应用中建议使用 JSON 解析库如 Jackson 或 Gson
        // 从 JSON 中提取用户 ID，这里是个简单示例
        String[] parts = json.split(",");
        for (String part : parts) {
            if (part.contains("id")) {
                return Long.parseLong(part.split(":")[1].trim());
            }
        }
        return -1; // 找不到 ID
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
