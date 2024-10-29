package com.yunying.gh.mapper;

import com.yunying.gh.domain.Contribution;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
public interface ContributionMapper extends BaseMapper<Contribution> {

    /**
     * 根据repoId查询贡献数量
     * @param repoId
     * @return
     */
    int selectCountByRepoId(int repoId);

    /**
     * 根据repoId查询提交数量
     * @param repoId
     * @return
     */
    int selectCommitCountByRepoId(int repoId);

}
