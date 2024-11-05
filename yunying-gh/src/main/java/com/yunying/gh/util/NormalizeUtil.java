package com.yunying.gh.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunying.common.exception.CommonException;

import java.util.Map;

public class NormalizeUtil {


    // 归一化方法
    public static <T> double normalizeValue(double value, BaseMapper<T> mapper, String columnName) throws NoSuchFieldException, IllegalAccessException {

        int max = 100000;
        int min = 1;
        if (max == min) {
            throw new CommonException("最大值和最小值不能相同");
        }
        return (value - min) / (max - min); // 将值归一化到0-1范围
    }


    // 查询最大值
    private static <T> int selectMaxValue(BaseMapper<T> mapper, String columnName) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("MAX(" + columnName + ") AS max_value");

        // 执行查询，结果返回一个Map
        Map<String, Object> result = mapper.selectMaps(queryWrapper).stream().findFirst().orElse(null);

        if (result == null || result.get("max_value") == null) {
            throw new CommonException("查询最大值失败");
        }
        return ((Number) result.get("max_value")).intValue();
    }

    // 查询最小值
    private static <T> int selectMinValue(BaseMapper<T> mapper, String columnName) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("MIN(" + columnName + ") AS min_value");

        // 执行查询，结果返回一个Map
        Map<String, Object> result = mapper.selectMaps(queryWrapper).stream().findFirst().orElse(null);

        if (result == null || result.get("min_value") == null) {
            throw new CommonException("查询最小值失败");
        }
        return ((Number) result.get("min_value")).intValue();
    }


}
