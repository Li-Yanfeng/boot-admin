package org.utility.quartz.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.utility.config.thread.ThreadPoolExecutorUtils;
import org.utility.constant.SystemConstant;
import org.utility.model.vo.EmailVO;
import org.utility.quartz.mapper.QuartzLogMapper;
import org.utility.quartz.model.QuartzJob;
import org.utility.quartz.model.QuartzLog;
import org.utility.quartz.service.QuartzJobService;
import org.utility.service.EmailService;
import org.utility.util.RedisUtils;
import org.utility.util.SpringContextHolder;
import org.utility.util.ThrowableUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 执行定时任务
 *
 * @author Li Yanfeng
 * @link [人人开源] https://gitee.com/renrenio/renren-security
 */
@Async
public class ExecutionJob extends QuartzJobBean {

    /**
     * 该处仅供参考
     */
    private final static ThreadPoolExecutor EXECUTOR = ThreadPoolExecutorUtils.getPool();

    @Override
    public void executeInternal(JobExecutionContext context) {
        QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(QuartzJob.JOB_KEY);
        // 获取spring bean
        QuartzLogMapper quartzLogRepository = SpringContextHolder.getBean(QuartzLogMapper.class);
        QuartzJobService quartzJobService = SpringContextHolder.getBean(QuartzJobService.class);
        RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);

        String uuid = quartzJob.getUuid();

        QuartzLog log = new QuartzLog();
        log.setJobName(quartzJob.getJobName());
        log.setBeanName(quartzJob.getBeanName());
        log.setMethodName(quartzJob.getMethodName());
        log.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        log.setCronExpression(quartzJob.getCronExpression());
        try {
            // 执行任务
            System.out.println("--------------------------------------------------------------");
            System.out.println("任务开始执行，任务名称：" + quartzJob.getJobName());
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(),
                quartzJob.getParams());
            Future<?> future = EXECUTOR.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            if (StrUtil.isNotBlank(uuid)) {
                redisUtils.set(uuid, true);
            }
            // 任务状态
            log.setSuccess(SystemConstant.YES);
            System.out.println("任务执行完毕，任务名称：" + quartzJob.getJobName() + ", 执行时间：" + times + "毫秒");
            System.out.println("--------------------------------------------------------------");
            // 判断是否存在子任务
            if (StrUtil.isNotBlank(quartzJob.getSubTask())) {
                String[] tasks = quartzJob.getSubTask().split("[,，]");
                // 执行子任务
                quartzJobService.executionSubJob(tasks);
            }
        } catch (Exception e) {
            if (StrUtil.isNotBlank(uuid)) {
                redisUtils.set(uuid, false);
            }
            System.out.println("任务执行失败，任务名称：" + quartzJob.getJobName());
            System.out.println("--------------------------------------------------------------");
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态 0：成功 1：失败
            log.setSuccess(SystemConstant.NO);
            log.setExceptionDetail(ThrowableUtils.getStackTrace(e));
            // 任务如果失败了则暂停
            if (quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()) {
                quartzJob.setPause(SystemConstant.NO);
                //更新状态
                quartzJobService.updateQuartzJobByIsPause(quartzJob);
            }
            if (quartzJob.getEmail() != null) {
                EmailService emailService = SpringContextHolder.getBean(EmailService.class);
                // 邮箱报警
                if (StrUtil.isNotBlank(quartzJob.getEmail())) {
                    EmailVO emailVo = taskAlarm(quartzJob, ThrowableUtils.getStackTrace(e));
                    emailService.send(emailVo, emailService.getEmailConfig());
                }
            }
        } finally {
            quartzLogRepository.insert(log);
        }
    }

    private EmailVO taskAlarm(QuartzJob quartzJob, String msg) {
        EmailVO emailVo = new EmailVO();
        emailVo.setSubject("定时任务【" + quartzJob.getJobName() + "】执行失败，请尽快处理！");
        Map<String, Object> data = new HashMap<>(16);
        data.put("task", quartzJob);
        data.put("msg", msg);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template",
            TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/taskAlarm.ftl");
        emailVo.setContent(template.render(data));
        List<String> emails = Arrays.asList(quartzJob.getEmail().split("[,，]"));
        emailVo.setTos(emails);
        return emailVo;
    }
}
