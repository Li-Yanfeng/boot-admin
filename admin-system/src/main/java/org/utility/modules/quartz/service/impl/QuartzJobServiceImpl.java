package org.utility.modules.quartz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.base.impl.BaseServiceImpl;
import org.utility.exception.BadRequestException;
import org.utility.modules.quartz.mapper.QuartzJobMapper;
import org.utility.modules.quartz.model.QuartzJob;
import org.utility.modules.quartz.service.QuartzJobService;
import org.utility.modules.quartz.service.dto.QuartzJobQuery;
import org.utility.modules.quartz.util.QuartzManage;
import org.utility.util.FileUtils;
import org.utility.util.RedisUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 定时任务 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Service
public class QuartzJobServiceImpl extends BaseServiceImpl<QuartzJobMapper, QuartzJobQuery, QuartzJob> implements QuartzJobService {

    private final QuartzJobMapper quartzJobMapper;
    private final QuartzManage quartzManage;

    private final RedisUtils redisUtils;

    public QuartzJobServiceImpl(QuartzJobMapper quartzJobMapper, QuartzManage quartzManage, RedisUtils redisUtils) {
        this.quartzJobMapper = quartzJobMapper;
        this.quartzManage = quartzManage;
        this.redisUtils = redisUtils;
    }


    @Override
    public void save(QuartzJob resource) {
        if (!CronExpression.isValidExpression(resource.getCronExpression())) {
            throw new BadRequestException("cron表达式格式错误");
        }
        quartzJobMapper.insert(resource);
        quartzManage.addJob(resource);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByIds(Collection<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                QuartzJob quartzJob = this.getById(id);
                quartzManage.deleteJob(quartzJob);
                quartzJobMapper.deleteById(quartzJob);
            }
        }
    }

    @Override
    public void updateById(QuartzJob resource) {
        if (!CronExpression.isValidExpression(resource.getCronExpression())) {
            throw new BadRequestException("cron表达式格式错误");
        }
        if (StrUtil.isNotBlank(resource.getSubTask())) {
            List<String> tasks = Arrays.asList(resource.getSubTask().split("[,，]"));
            if (tasks.contains(resource.getJobId().toString())) {
                throw new BadRequestException("子任务中不能添加当前任务ID");
            }
        }
        quartzJobMapper.updateById(resource);
        quartzManage.updateJobCron(resource);
    }

    @Override
    public void updateIsPause(QuartzJob resource) {
        if (resource.getPause()) {
            quartzManage.resumeJob(resource);
            resource.setPause(false);
        } else {
            quartzManage.pauseJob(resource);
            resource.setPause(true);
        }
        this.updateById(resource);
    }

    @Override
    public void executionJob(QuartzJob resource) {
        quartzManage.runJobNow(resource);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void executionSubJob(String[] tasks) throws InterruptedException {
        for (String id : tasks) {
            QuartzJob quartzJob = this.getById(Long.parseLong(id));
            // 执行任务
            String uuid = IdUtil.simpleUUID();
            quartzJob.setUuid(uuid);
            // 执行任务
            this.executionJob(quartzJob);
            // 获取执行状态，如果执行失败则停止后面的子任务执行
            Boolean result = (Boolean) redisUtils.get(uuid);
            while (result == null) {
                // 休眠5秒，再次获取子任务执行情况
                Thread.sleep(5000);
                result = (Boolean) redisUtils.get(uuid);
            }
            if (!result) {
                redisUtils.del(uuid);
                break;
            }
        }
    }

    @Override
    public void download(HttpServletResponse response, List<QuartzJob> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (QuartzJob quartzJob : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(8, true);
            map.put("任务名称", quartzJob.getJobName());
            map.put("Bean名称", quartzJob.getBeanName());
            map.put("执行方法", quartzJob.getMethodName());
            map.put("参数", quartzJob.getParams());
            map.put("表达式", quartzJob.getCronExpression());
            map.put("状态", quartzJob.getPause() ? "暂停中" : "运行中");
            map.put("描述", quartzJob.getDescription());
            map.put("创建日期", quartzJob.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
