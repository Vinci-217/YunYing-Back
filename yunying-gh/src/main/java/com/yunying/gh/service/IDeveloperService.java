package com.yunying.gh.service;

import com.yunying.gh.domain.Developer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
public interface IDeveloperService extends IService<Developer> {

    /**
     * 计算开发者的粉丝分数
     * @param developer
     */
    void calculateFollowersScore(Developer developer) throws NoSuchFieldException, IllegalAccessException;

    /**
     * 计算开发者的技能排名
     * @param
     */
    void calculateTalentRank();
}
