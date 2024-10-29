package com.yunying.gh.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunying.common.exception.CommonException;

public class NormalizeUtil {


    // 归一化方法
    private static <T> double normalizeValue(double value, BaseMapper<T> mapper, Class<T> entityClass, String columnName) throws NoSuchFieldException, IllegalAccessException {

        int max = selectMaxValue(mapper, entityClass, columnName);
        int min = selectMinValue(mapper, entityClass, columnName);
        if (max == min){
            throw new CommonException("最大值和最小值不能相同");
        }
        return (value - min) / (max - min); // 将值归一化到0-1范围
    }


    // 泛型方法，T 表示实体类型，E 表示主键类型
    public static <T> int selectMaxValue(BaseMapper<T> mapper, Class<T> entityClass, String columnName) throws NoSuchFieldException, IllegalAccessException {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 构建查询条件
        queryWrapper.select("MAX(" + columnName + ") AS max_value");

        // 通过反射获取到字段的最大值
        T result = mapper.selectOne(queryWrapper);

        if(result == null){
            throw new CommonException("查询最大值失败");
        }
        // 通过反射获取到字段的最大值
        return (int) result.getClass().getDeclaredField(columnName).get(result);
    }

    // 泛型方法，T 表示实体类型，E 表示主键类型
    public static <T> int selectMinValue(BaseMapper<T> mapper, Class<T> entityClass, String columnName) throws NoSuchFieldException, IllegalAccessException {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 构建查询条件
        queryWrapper.select("MIN(" + columnName + ") AS min_value");

        // 通过反射获取到字段的最大值
        T result = mapper.selectOne(queryWrapper);

        if(result == null){
            throw new CommonException("查询最大值失败");
        }
        // 通过反射获取到字段的最大值
        return (int) result.getClass().getDeclaredField(columnName).get(result);
    }



}
