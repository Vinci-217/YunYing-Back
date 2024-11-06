package com.yunying.gh.util;

public class ContributionScoreUtil {

    // 定义最大值
    private static final int MAX_COMMIT = 1000;
    private static final int MAX_PR = 100;
    private static final int MAX_ISSUE = 1000;

    // 权重
    private static final double WEIGHT_COMMIT = 0.1;
    private static final double WEIGHT_PR = 0.7;
    private static final double WEIGHT_ISSUE = 0.2;

    /**
     * 将原始值量化到1-10范围内，并进行指数对数映射。
     *
     * @param count    原始数量值
     * @param maxCount 最大数量值
     * @return 映射后的分数
     */
    private static double mapToScore(int count, int maxCount) {
        if (count >= maxCount) {
            return 10.0; // 如果超过最大值，直接给10分
        }

        // 将count映射到1到10之间
        double normalizedScore = (double) count / maxCount * 9 + 1;  // 映射到1到10区间

        // 使用指数对数映射，增强评分差异
        double logScore = Math.log1p(normalizedScore - 1) + 1;  // log1p是log(1+x)，避免出现log(0)

        return Math.min(logScore, 10.0); // 确保最终评分不会超过10
    }

    /**
     * 计算开发者最终的贡献得分
     *
     * @param commitCount 提交数
     * @param prCount     拉取请求数
     * @param issueCount  问题数
     * @return 最终得分
     */
    public static double calculateFinalScore(int commitCount, int prCount, int issueCount) {
        // 分别计算每个指标的量化得分
        double commitScore = mapToScore(commitCount, MAX_COMMIT);
        double prScore = mapToScore(prCount, MAX_PR);
        double issueScore = mapToScore(issueCount, MAX_ISSUE);

        // 根据权重计算最终得分
        double finalScore = commitScore * WEIGHT_COMMIT
                + prScore * WEIGHT_PR
                + issueScore * WEIGHT_ISSUE;

        return finalScore;
    }

    // 测试方法
    public static void main(String[] args) {
        // 假设这些是获取到的贡献数据
        int commitCount = 300;   // 提交次数
        int prCount = 150;        // PR次数
        int issueCount = 500;    // Issue数

        // 计算最终得分
        double finalScore = calculateFinalScore(commitCount, prCount, issueCount);
        System.out.println("最终得分: " + finalScore);  // 输出最终得分
    }
}
