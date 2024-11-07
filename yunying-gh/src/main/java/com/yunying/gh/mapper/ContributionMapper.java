package com.yunying.gh.mapper;

import com.yunying.gh.domain.Contribution;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunying.gh.domain.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
public interface ContributionMapper extends BaseMapper<Contribution> {

    /**
     * 根据repoId查询贡献数量
     *
     * @param repoId
     * @return
     */
    int selectCountByRepoId(int repoId);

    /**
     * 根据repoId查询提交数量
     *
     * @param repoId
     * @return
     */
    int selectCommitCountByRepoId(int repoId);

    /**
     * 根据devId和repoId查询贡献
     *
     * @param devId
     * @param repoId
     * @return
     */
    Contribution selectByDevIdAndRepoId(int devId, int repoId);


    /**
     * 根据devId查询所有的Contribution
     *
     * @param devId
     * @return
     */
    List<Contribution> selectByDevId(Integer devId);

    /**
     * 根据devId查询所有的Repository
     *
     * @param devId
     * @return
     */
    List<Repository> selectRepoByDevId(int devId);
}
