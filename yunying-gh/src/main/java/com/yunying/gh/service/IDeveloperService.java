package com.yunying.gh.service;

import com.yunying.gh.domain.Developer;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

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
     * @param devLogin
     * @return
     */
    boolean calculateFollowersScore(String devLogin) throws NoSuchFieldException, IllegalAccessException;

    /**
     * 计算开发者的技能排名
     *
     * @param
     * @param devId
     */
    void calculateTalentRank(String devLogin);


    /**
     * 预测开发者的国家
     *
     * @param devLogin
     * @return
     */
    boolean propagateNation(String devLogin);


    /**
     * 设置开发者的报告状态
     *
     * @param devLogin
     * @return
     */
    boolean setReport(@Param("devLogin") String devLogin);


}
