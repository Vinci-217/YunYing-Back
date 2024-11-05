package com.yunying.cn.job;

import com.yunying.cn.service.GhClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateJob implements JobService {

    @Autowired
    private GhClient ghClient;

    private static final Logger log = LoggerFactory.getLogger(TimerJob.class);


    @Override
    public void run(String params) {
        log.info("\n ======== \n update-job-params:{} \n ========", params);
        ghClient.updateDeveloperInfo();

    }
}
