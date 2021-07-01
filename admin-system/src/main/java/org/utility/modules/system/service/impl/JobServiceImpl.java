package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.base.impl.ServiceImpl;
import org.utility.constant.CacheConsts;
import org.utility.exception.BadRequestException;
import org.utility.exception.EntityExistException;
import org.utility.modules.system.mapper.JobMapper;
import org.utility.modules.system.model.Job;
import org.utility.modules.system.service.JobService;
import org.utility.modules.system.service.UserJobService;
import org.utility.modules.system.service.dto.JobDTO;
import org.utility.modules.system.service.dto.JobQuery;
import org.utility.util.FileUtils;
import org.utility.util.RedisUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 岗位 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-28
 */
@Service
@CacheConfig(cacheNames = {"job"})
public class JobServiceImpl extends ServiceImpl<JobMapper, JobDTO, JobQuery, Job> implements JobService {

    private final JobMapper jobMapper;
    private final UserJobService userJobService;

    private final RedisUtils redisUtils;

    public JobServiceImpl(JobMapper jobMapper, UserJobService userJobService, RedisUtils redisUtils) {
        this.jobMapper = jobMapper;
        this.userJobService = userJobService;
        this.redisUtils = redisUtils;
    }

    @Override
    public void save(Job resource) {
        Job job = this.getByName(resource.getName());
        if (job != null && ObjectUtil.notEqual(resource.getJobId(), job.getJobId())) {
            throw new EntityExistException(Job.class, "name", resource.getName());
        }
        jobMapper.insert(resource);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByIds(Collection<Long> ids) {
        // 删除缓存
        redisUtils.delByKeys(CacheConsts.JOB_ID, ids);
        jobMapper.deleteBatchIds(ids);
        userJobService.removeByJobIds(ids);
    }

    @Override
    public void updateById(Job resource) {
        Job job = this.getByName(resource.getName());
        if (job != null && ObjectUtil.notEqual(resource.getJobId(), job.getJobId())) {
            throw new EntityExistException(Job.class, "name", resource.getName());
        }
        // 清理缓存
        redisUtils.del(CacheConsts.JOB_ID + resource.getJobId());
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public JobDTO getById(Long id) {
        return super.getById(id);
    }

    @Override
    public void verification(Set<Long> ids) {
        if (userJobService.countByJobIds(ids) > 0) {
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public void download(HttpServletResponse response, List<JobDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (JobDTO job : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(3, true);
            map.put("岗位名称", job.getName());
            map.put("岗位状态", job.getEnabled() ? "启用" : "停用");
            map.put("创建日期", job.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 根据 name 查询
     *
     * @param name 岗位名称
     */
    private Job getByName(String name) {
        LambdaQueryWrapper<Job> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Job::getName, name);
        return jobMapper.selectOne(wrapper);
    }
}
