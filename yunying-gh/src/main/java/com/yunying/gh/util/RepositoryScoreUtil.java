package com.yunying.gh.util;

public class RepositoryScoreUtil {

    // 最大值（可以根据实际数据设置）
    private static final int MAX_STAR = 10000;
    private static final int MAX_FORK = 500;
    private static final int MAX_WATCH = 1000;
    private static final int MAX_COMMITS = 1000;
    private static final int MAX_ISSUE = 1000;
    private static final int MAX_PR = 100;
    private static final int MAX_CONTRIBUTORS = 100;

    // 权重常量
    private static final double WEIGHT_STAR = 0.4;
    private static final double WEIGHT_FORK = 0.2;
    private static final double WEIGHT_WATCH = 0.1;
    private static final double WEIGHT_COMMITS = 0.05;
    private static final double WEIGHT_ISSUE = 0.05;
    private static final double WEIGHT_PR = 0.1;
    private static final double WEIGHT_CONTRIBUTORS = 0.1;

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
     * 计算仓库的最终得分
     *
     * @param starCount    星标数
     * @param forkCount    fork数
     * @param watchCount   观察数
     * @param commits      提交数
     * @param issueCount   问题数
     * @param prCount      拉取请求数
     * @param contributors 贡献者数
     * @return 最终得分
     */
    public static double calculateFinalScore(int starCount, int forkCount, int watchCount,
                                             int commits, int issueCount, int prCount, int contributors) {
        // 分别计算每个指标的量化得分
        double starScore = mapToScore(starCount, MAX_STAR);
        double forkScore = mapToScore(forkCount, MAX_FORK);
        double watchScore = mapToScore(watchCount, MAX_WATCH);
        double commitScore = mapToScore(commits, MAX_COMMITS);
        double issueScore = mapToScore(issueCount, MAX_ISSUE);
        double prScore = mapToScore(prCount, MAX_PR);
        double contributorScore = mapToScore(contributors, MAX_CONTRIBUTORS);

        // 根据权重计算最终得分
        double finalScore = starScore * WEIGHT_STAR
                + forkScore * WEIGHT_FORK
                + watchScore * WEIGHT_WATCH
                + commitScore * WEIGHT_COMMITS
                + issueScore * WEIGHT_ISSUE
                + prScore * WEIGHT_PR
                + contributorScore * WEIGHT_CONTRIBUTORS;

        return finalScore;
    }

    // 测试方法
    public static void main(String[] args) {
        // 假设这些是从数据库或其他地方获取到的数据
        int starCount = 5000;       // 星标数
        int forkCount = 3000;       // Fork数
        int watchCount = 4000;      // Watch数
        int commits = 8000;         // 提交数
        int issueCount = 1000;      // 问题数
        int prCount = 500;          // PR数
        int contributors = 200;     // 贡献者数

        // 计算最终得分
        double finalScore = calculateFinalScore(starCount, forkCount, watchCount, commits,
                issueCount, prCount, contributors);
        System.out.println("最终得分: " + finalScore);  // 输出最终得分
    }
}
