package com.yunying.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.math3.special.Erf;

import java.util.Arrays;

public class DeveloperRankCalculator {

    // 计算累积分布函数的指数（CDF）值
    private double exponentialCdf(double x) {
        // 使用累积分布函数的公式计算值
        return 1 - Math.exp(-x);
    }

    // 计算对数正态分布的累积分布函数的值
    private double logNormalCdf(double x) {
        // 计算对数正态分布 CDF 的值
        return 0.5 * (1 + Erf.erf((Math.log(x) - 0) / (Math.sqrt(2))));
    }

    // 计算开发者的评级
    public RankResult calculateRank(
            int allCommits, // 所有提交的总数（是否为1000的标志）
            int commits, // 开发者的提交数量
            int prs, // 开发者的拉取请求数量
            int issues, // 开发者解决的问题数量
            int reviews, // 开发者的代码评审数量
            int stars, // 项目的星标数量
            int followers // 开发者的关注者数量
    ) {
        // 中位数和权重设置
        final int COMMITS_MEDIAN = (allCommits > 0) ? 1000 : 250;
        final double COMMITS_WEIGHT = 2.0;

        final int PRS_MEDIAN = 50;
        final double PRS_WEIGHT = 3.0;

        final int ISSUES_MEDIAN = 25;
        final double ISSUES_WEIGHT = 1.0;

        final int REVIEWS_MEDIAN = 2;
        final double REVIEWS_WEIGHT = 1.0;

        final int STARS_MEDIAN = 50;
        final double STARS_WEIGHT = 4.0;

        final int FOLLOWERS_MEDIAN = 10;
        final double FOLLOWERS_WEIGHT = 1.0;

        // 计算总权重
        double TOTAL_WEIGHT = COMMITS_WEIGHT + PRS_WEIGHT + ISSUES_WEIGHT +
                REVIEWS_WEIGHT + STARS_WEIGHT + FOLLOWERS_WEIGHT;

        // 设定阈值和等级
        double[] THRESHOLDS = {1, 12.5, 25, 37.5, 50, 62.5, 75, 87.5, 100};
        String[] LEVELS = {"S", "A+", "A", "A-", "B+", "B", "B-", "C+", "C"};

        // 计算排名
        double rank = 1 - (
                COMMITS_WEIGHT * exponentialCdf((double) commits / COMMITS_MEDIAN) +
                        PRS_WEIGHT * exponentialCdf((double) prs / PRS_MEDIAN) +
                        ISSUES_WEIGHT * exponentialCdf((double) issues / ISSUES_MEDIAN) +
                        REVIEWS_WEIGHT * exponentialCdf((double) reviews / REVIEWS_MEDIAN) +
                        STARS_WEIGHT * logNormalCdf((double) stars / STARS_MEDIAN) +
                        FOLLOWERS_WEIGHT * logNormalCdf((double) followers / FOLLOWERS_MEDIAN)
        ) / TOTAL_WEIGHT;

        // 确定评级级别
        String level = LEVELS[Arrays.binarySearch(THRESHOLDS, rank * 100) >= 0
                ? Arrays.binarySearch(THRESHOLDS, rank * 100)
                : -Arrays.binarySearch(THRESHOLDS, rank * 100) - 2];

        // 返回结果
        return new RankResult(level, rank * 100);
    }

    // 内部类用于返回计算结果
    @Data
    @AllArgsConstructor
    public static class RankResult {
        String level; // 评级
        double percentile; // 百分比
    }

//    // 主方法用于测试
//    public static void main(String[] args) {
//        DeveloperRankCalculator calculator = new DeveloperRankCalculator();
//        RankResult result = calculator.calculateRank(1000, 500, 30, 15, 1, 60, 8);
//        System.out.println("Level: " + result.getLevel() + ", Percentile: " + result.getPercentile()+"%");
//    }
}
