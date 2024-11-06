package com.yunying.gh.util;

public class FollowerScoreUtil {

    // 最大粉丝数阈值：超过此值时评分为10分
    private static final int FAN_THRESHOLD = 100000;

    /**
     * 根据粉丝数返回评估评分，评分范围为1到10
     *
     * @param fansCount 开发者的粉丝数量
     * @return 量化后的评分，范围为1到10
     */
    public static double evaluateFans(int fansCount) {
        if (fansCount == 0) {
            return 1; // 如果粉丝数为0，评分为1
        }

        if (fansCount >= FAN_THRESHOLD) {
            return 10; // 如果粉丝数超过10万，评分为10
        }

        // 对于粉丝数小于10万的情况，线性量化为1到9分
        double normalizedScore = (double) fansCount / FAN_THRESHOLD; // 将粉丝数映射到0到1之间
        double score = (normalizedScore * 9) + 1; // 映射到1到9之间

        return score;
    }

    public static void main(String[] args) {
        int fansCount = 100000;
        double score = evaluateFans(fansCount);
        System.out.println(score);
    }

}
