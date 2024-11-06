package com.yunying.gh.mapper;

import com.yunying.gh.domain.Developer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunying.gh.domain.Follower;
import com.yunying.gh.domain.Following;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
public interface DeveloperMapper extends BaseMapper<Developer> {


    /**
     * 根据开发者ID查找相互关注的人
     *
     * @param devId
     * @return
     */
    List<Developer> findMutualFollow(Integer devId);

    /**
     * 根据开发者ID查找粉丝的人
     *
     * @param devId
     */
    List<Follower> selectFollowers(@Param("devId") Integer devId);

    /**
     * 根据开发者ID查找关注的人
     *
     * @param devId
     * @return
     */
    List<Following> selectFollowing(Integer devId);
}
