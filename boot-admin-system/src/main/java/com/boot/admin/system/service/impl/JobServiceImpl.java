package com.boot.admin.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.constant.CacheKey;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.EntityExistException;
import com.boot.admin.system.mapper.JobMapper;
import com.boot.admin.system.model.Job;
import com.boot.admin.system.service.JobService;
import com.boot.admin.system.service.UserJobService;
import com.boot.admin.system.service.dto.JobDTO;
import com.boot.admin.system.service.dto.JobQuery;
import com.boot.admin.system.service.dto.JobSmallDTO;
import com.boot.admin.util.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 岗位 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"job"})
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    private final UserJobService userJobService;

    private final RedisUtils redisUtils;

    public JobServiceImpl(UserJobService userJobService, RedisUtils redisUtils) {
        this.userJobService = userJobService;
        this.redisUtils = redisUtils;
    }

    @Override
    public void saveJob(Job resource) {
        if (ObjectUtil.isNotNull(getJobByName(resource.getName()))) {
            throw new EntityExistException(Job.class, "name", resource.getName());
        }
        baseMapper.insert(resource);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeJobByIds(Collection<Long> ids) {
        // 清理缓存
        redisUtils.delByKeys(CacheKey.JOB_ID, ids);
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateJobById(Job resource) {
        Long jobId = resource.getJobId();
        ValidationUtils.notNull(baseMapper.selectById(jobId), "Job", "jobId", jobId);
        Job job1 = getJobByName(resource.getName());
        if (ObjectUtil.isNotNull(job1) && ObjectUtil.notEqual(jobId, job1.getJobId())) {
            throw new EntityExistException(Job.class, "name", resource.getName());
        }
        baseMapper.updateById(resource);
        // 清空缓存
        redisUtils.del(CacheKey.JOB_ID + jobId);
    }

    @Override
    public List<JobDTO> listJobs(JobQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), JobDTO.class);
    }

    @Override
    public Page<JobDTO> listJobs(JobQuery query, Page<Job> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), JobDTO.class);
    }

    @Override
    public List<JobSmallDTO> listJobsByUserId(Long userId) {
        return ConvertUtils.convert(baseMapper.selectJobListByUserId(userId), JobSmallDTO.class);
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public JobDTO getJobById(Long id) {
        Job job = baseMapper.selectById(id);
        ValidationUtils.notNull(job, "Job", "jobId", id);
        return ConvertUtils.convert(job, JobDTO.class);
    }

    @Override
    public void verification(Collection<Long> ids) {
        if (userJobService.countUserIdByJobIds(ids) > 0) {
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public void exportJob(List<JobDTO> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(job -> {
            Map<String, Object> map = MapUtil.newHashMap(3, true);
            map.put("岗位名称", job.getName());
            map.put("岗位状态", CommonConstant.ENABLE.equals(job.getEnabled()) ? "启用" : "停用");
            map.put("创建日期", job.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 根据 name 查询
     *
     * @param name 岗位名称
     */
    private Job getJobByName(String name) {
        return lambdaQuery().eq(Job::getName, name).one();
    }
}
