package com.yunying.cn.service;


import com.yunying.cn.entity.QuartzLog;
import com.yunying.cn.mapper.QuartzLogMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("quartzLogService")
public class QuartzLogService {

    @Autowired
    private QuartzLogMapper quartzLogMapper ;

    public Integer insert(QuartzLog quartzLog) {
        return quartzLogMapper.insert(quartzLog);
    }
}
