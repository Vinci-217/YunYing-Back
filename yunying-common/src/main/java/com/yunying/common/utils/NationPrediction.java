package com.yunying.common.utils;

import org.apache.flink.shaded.guava30.com.google.common.graph.Graph;
import org.apache.flink.types.NullValue;
import java.util.List;

public class NationPrediction {

    public static void main(String[] args) throws Exception {
        // 初始化 Flink 执行环境
        final StreamExecutionEnvironment  env = ExecutionEnvironment.getExecutionEnvironment();

        // 加载开发者节点和关系边数据
        DataSet<Vertex<Long, String>> developers = getDevelopersWithNationLabels(env); // 有或无 nation 标签
        DataSet<Edge<Long, NullValue>> relationships = getDeveloperRelationships(env);

        // 构建图结构
        Graph<Long, String, NullValue> devGraph = Graph.fromDataSet(developers, relationships, env);

        // 使用 Gelly 标签传播算法
        Graph<Long, String, NullValue> resultGraph = devGraph.run(new LabelPropagation<>(10)); // 迭代 10 次

        // 计算每个预测标签的置信度
        DataSet<Vertex<Long, NationConfidence>> confidenceGraph = resultGraph.getVertices()
                .map(vertex -> {
                    String predictedNation = vertex.getValue();
                    double confidence = calculateConfidence(vertex, devGraph);
                    // 如果置信度低于阈值，将 nation 标签设置为 N/A
                    String displayNation = confidence < 0.5 ? "N/A" : predictedNation;
                    return new Vertex<>(vertex.getId(), new NationConfidence(displayNation, confidence));
                });

        // 输出或展示带有 Nation 预测和置信度的节点
        confidenceGraph.print();
    }

    // 模拟获取开发者节点数据的方法
    private static DataSet<Vertex<Long, String>> getDevelopersWithNationLabels(ExecutionEnvironment env) {
        // 返回开发者节点数据
        return env.fromElements(
                new Vertex<>(1L, "US"),
                new Vertex<>(2L, ""),
                new Vertex<>(3L, "CN"),
                new Vertex<>(4L, "")
        );
    }

    // 模拟获取开发者关系边数据的方法
    private static DataSet<Edge<Long, NullValue>> getDeveloperRelationships(ExecutionEnvironment env) {
        return env.fromElements(
                new Edge<>(1L, 2L, NullValue.getInstance()),
                new Edge<>(2L, 3L, NullValue.getInstance()),
                new Edge<>(3L, 4L, NullValue.getInstance())
        );
    }

    // 计算置信度的自定义方法
    private static double calculateConfidence(Vertex<Long, String> vertex, Graph<Long, String, NullValue> graph) {
        // 计算邻居中相同标签比例作为置信度（伪代码示例）
        List<Vertex<Long, String>> neighbors = graph.getNeighbors(vertex.getId()).collect();
        long sameLabelCount = neighbors.stream().filter(neighbor -> neighbor.getValue().equals(vertex.getValue())).count();
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
