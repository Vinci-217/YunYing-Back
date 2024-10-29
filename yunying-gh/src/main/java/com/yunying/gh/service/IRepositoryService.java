package com.yunying.gh.service;

import com.yunying.gh.domain.Repository;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vinci
 * @since 2024-10-29
 */
public interface IRepositoryService extends IService<Repository> {


    // 计算和缩放得分并更新数据库中的重要性得分
    void calculateImportanceScore(Repository repository);



//    // 更新所有项目的重要性得分
//    public void updateAllrepositoryScores() {
//        List<Repository> repositorys = repositoryRepository.findAll();
//        for (repository repository : repositorys) {
//            BigDecimal scaledScore = calculateImportanceScore(repository);
//            repository.setImportanceScore(scaledScore);
//            repositoryRepository.save(repository); // 更新数据库中的重要性得分
//        }
//    }

}
