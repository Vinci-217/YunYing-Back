package com.yunying.cn.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 公众号:知了一笑
 * @since 2023-07-26 14:51
 */
@Component("timerJob")
public class TimerJob implements JobService {

    private static final Logger log = LoggerFactory.getLogger(TimerJob.class);
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;

    @Override
    public void run(String params) {
        log.info("\n ======== \n timer-job-params:{} \n ========",params);
        log.info("time-now:{}",format.format(new Date()));
    }
}
