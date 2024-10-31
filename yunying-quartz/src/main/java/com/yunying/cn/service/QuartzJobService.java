package com.yunying.cn.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunying.cn.constant.JobState;
import com.yunying.cn.entity.QuartzJob;
import com.yunying.cn.mapper.QuartzJobMapper;
import com.yunying.cn.scheduler.QuartzManage;
import jakarta.annotation.PostConstruct;

import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class QuartzJobService {

    @Autowired
    private QuartzJobMapper quartzJobMapper ;

    @Autowired
    private QuartzManage quartzManage;

    /**
     * 初始化加载定时任务
     */
    @PostConstruct
    public void init () {
        LambdaQueryWrapper<QuartzJob> queryWrapper = new LambdaQueryWrapper<>() ;
        queryWrapper.in(QuartzJob::getState, JobState.JOB_RUN.getStatus(),JobState.JOB_STOP.getStatus());
        List<QuartzJob> jobList = quartzJobMapper.selectList(queryWrapper);
        jobList.forEach(quartzJob -> {
            CronTrigger cronTrigger = quartzManage.getCronTrigger(quartzJob.getId()) ;
            if (Objects.isNull(cronTrigger)){
                quartzManage.createJob(quartzJob);
            } else {
                quartzManage.updateJob(quartzJob);
            }
        });
    }

    /**
     * 任务主键查询
     */
    public QuartzJob getById(Integer id) {
        return quartzJobMapper.selectById(id) ;
    }

    /**
     * 新增任务
     */
    public int insert(QuartzJob quartzJob) {
        int flag = quartzJobMapper.insert(quartzJob) ;
        if (flag > 0){
            quartzManage.createJob(quartzJob) ;
        }
        return flag;
    }

    /**
     * 更新任务
     */
    public int update(QuartzJob quartzJob) {
        int flag = quartzJobMapper.updateById(quartzJob);
        if (flag > 0){
            quartzManage.updateJob(quartzJob);
        }
        return flag ;
    }

    /**
     * 暂停任务
     */
    public void pause(Integer id) {
        QuartzJob quartzJob = quartzJobMapper.selectById(id) ;
        if (!Objects.isNull(quartzJob)){
            quartzJob.setState(JobState.JOB_STOP.getStatus());
            if (quartzJobMapper.updateById(quartzJob)>0){
                quartzManage.checkStop(quartzJob);
            }
        }
    }

    /**
     * 恢复任务
     */
    public void resume(Integer id) {
        QuartzJob quartzJob = quartzJobMapper.selectById(id) ;
        if (!Objects.isNull(quartzJob)){
            quartzJob.setState(JobState.JOB_RUN.getStatus());
            if (quartzJobMapper.updateById(quartzJob)>0){
                quartzManage.resumeJob(id);
            }
        }
    }

    /**
     * 执行任务一次
     */
    public void runOnce(Integer id) {
        QuartzJob quartzJob = quartzJobMapper.selectById(id) ;
        if (!Objects.isNull(quartzJob) && quartzJob.getState() != JobState.JOB_DEL.getStatus()){
            quartzManage.run(quartzJob);
        }
    }
}