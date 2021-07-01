package org.utility.modules.quartz.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.utility.modules.quartz.mapper.QuartzJobMapper;
import org.utility.modules.quartz.model.QuartzJob;
import org.utility.modules.quartz.util.QuartzManage;

import java.util.List;

/**
 * @author Li Yanfeng
 */
@Component
public class JobRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(JobRunner.class);

    private final QuartzJobMapper quartzJobMapper;
    private final QuartzManage quartzManage;

    public JobRunner(QuartzJobMapper quartzJobMapper, QuartzManage quartzManage) {
        this.quartzJobMapper = quartzJobMapper;
        this.quartzManage = quartzManage;
    }


    /**
     * 项目启动时重新激活启用的定时任务
     *
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        logger.info("--------------------注入定时任务---------------------");
        List<QuartzJob> quartzJobs = quartzJobMapper.selectByPauseIsFalse();
        quartzJobs.forEach(quartzManage::addJob);
        logger.info("--------------------定时任务注入完成---------------------");
    }
}
