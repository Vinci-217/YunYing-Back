package com.yunying.gh.service;

import com.yunying.gh.domain.Developer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
public interface IDeveloperService extends IService<Developer> {

    /**
     * 计算开发者的粉丝分数
     *
     * @param dev_id
     */
    void calculateFollowersScore(int dev_id) throws NoSuchFieldException, IllegalAccessException;

    /**
     * 计算开发者的技能排名
     *
     * @param
     * @param devId
     */
    void calculateTalentRank(int devId);


    /**
     * 预测开发者的国家
     *
     * @param devId
     */
    void propagateNation(int devId);
}
