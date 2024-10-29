package com.yunying.gh.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ScoreUtil {

    /**
     * 将得分缩放到指定范围并保留两位小数
     *
     * @param originalScore 原始得分，范围在 0-1 之间
     * @return 缩放后的得分，范围在 1-10 之间
     */
    public static float scaleScore(double originalScore) {
        if (originalScore < 0 || originalScore > 1) {
            throw new IllegalArgumentException("originalScore 必须在 0 和 1 之间");
        }
        BigDecimal scaledScore = BigDecimal.valueOf(1 + originalScore * 9);
        return scaledScore.setScale(2, RoundingMode.HALF_UP).floatValue(); // 保留2位小数
    }
}
