package org.utility.quartz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.constant.SystemConstant;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.exception.BadRequestException;
import org.utility.quartz.mapper.QuartzJobMapper;
import org.utility.quartz.model.QuartzJob;
import org.utility.quartz.service.QuartzJobService;
import org.utility.quartz.service.dto.QuartzJobQuery;
import org.utility.quartz.util.QuartzManage;
import org.utility.util.*;

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
 * @since 2021-06-01
 */
@Service
public class QuartzJobServiceImpl extends ServiceImpl<QuartzJobMapper, QuartzJob> implements QuartzJobService {

    private final QuartzManage quartzManage;

    private final RedisUtils redisUtils;

    public QuartzJobServiceImpl(QuartzManage quartzManage, RedisUtils redisUtils) {
        this.quartzManage = quartzManage;
        this.redisUtils = redisUtils;
    }

    @Override
    public void saveQuartzJob(QuartzJob resource) {
        if (!CronExpression.isValidExpression(resource.getCronExpression())) {
            throw new BadRequestException("cron表达式格式错误");
        }
        baseMapper.insert(resource);
        quartzManage.addJob(resource);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeQuartzJobByIds(Collection<Long> ids) {
        for (Long id : ids) {
            QuartzJob quartzJob = getQuartzJobById(id);
            quartzManage.deleteJob(quartzJob);
            baseMapper.deleteById(quartzJob);
        }
    }

    @Override
    public void updateQuartzJobById(QuartzJob resource) {
        Long jobId = resource.getJobId();
        ValidationUtils.notNull(baseMapper.selectById(jobId), "QuartzJob", "jobId", jobId);

        if (!CronExpression.isValidExpression(resource.getCronExpression())) {
            throw new BadRequestException("cron表达式格式错误");
        }

        if (StrUtil.isNotBlank(resource.getSubTask())) {
            List<String> tasks = Arrays.asList(resource.getSubTask().split("[,，]"));
            if (tasks.contains(resource.getJobId().toString())) {
                throw new BadRequestException("子任务中不能添加当前任务ID");
            }
        }
        baseMapper.updateById(resource);
        quartzManage.updateJobCron(resource);
    }

    @Override
    public void updateQuartzJobByIsPause(QuartzJob resource) {
        if (SystemConstant.YES.equals(resource.getPause())) {
            quartzManage.resumeJob(resource);
            resource.setPause(SystemConstant.NO);
        } else {
            quartzManage.pauseJob(resource);
            resource.setPause(SystemConstant.YES);
        }
        updateQuartzJobById(resource);
    }

    @Override
    public List<QuartzJob> listQuartzJobs(QuartzJobQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), QuartzJob.class);
    }

    @Override
    public Page<QuartzJob> listQuartzJobs(QuartzJobQuery query, Page<QuartzJob> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), QuartzJob.class);
    }

    @Override
    public QuartzJob getQuartzJobById(Long id) {
        QuartzJob quartzJob = baseMapper.selectById(id);
        ValidationUtils.notNull(quartzJob, "QuartzJob", "jobId", id);
        return ConvertUtils.convert(quartzJob, QuartzJob.class);
    }

    @Override
    public void executionJob(QuartzJob resource) {
        quartzManage.runJobNow(resource);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void executionSubJob(String[] tasks) throws InterruptedException {
        for (String id : tasks) {
            QuartzJob quartzJob = getQuartzJobById(Long.parseLong(id));
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
    public void exportQuartzJob(List<QuartzJob> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(quartzJob -> {
            Map<String, Object> map = MapUtil.newHashMap(8, true);
            map.put("任务名称", quartzJob.getJobName());
            map.put("Bean名称", quartzJob.getBeanName());
            map.put("执行方法", quartzJob.getMethodName());
            map.put("参数", quartzJob.getParams());
            map.put("表达式", quartzJob.getCronExpression());
            map.put("状态", SystemConstant.YES.equals(quartzJob.getPause()) ? "暂停中" : "运行中");
            map.put("描述", quartzJob.getDescription());
            map.put("创建日期", quartzJob.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }
}
