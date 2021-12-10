package org.utility.system.service.impl;

import org.springframework.stereotype.Service;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.system.mapper.UserJobMapper;
import org.utility.system.model.UserJob;
import org.utility.system.service.UserJobService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户岗位关联 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
public class UserJobServiceImpl extends ServiceImpl<UserJobMapper, UserJob> implements UserJobService {

    @Override
    public void removeUserJobByUserIds(Collection<Long> userIds) {
        lambdaUpdate().in(UserJob::getUserId, userIds).remove();
    }

    @Override
    public void removeUserJobByUserIdEqAndJobIdsIn(Long userId, Collection<Long> jobIds) {
        lambdaUpdate().eq(UserJob::getUserId, userId).in(UserJob::getJobId, jobIds).remove();
    }

    @Override
    public void removeUserJobByJobIds(Collection<Long> jobIds) {
        lambdaUpdate().in(UserJob::getJobId, jobIds).remove();
    }

    @Override
    public void removeUserJobByJobIdEqAndUserIdsIn(Long jobId, Collection<Long> userIds) {
        lambdaUpdate().eq(UserJob::getJobId, jobId).in(UserJob::getUserId, userIds).remove();
    }

    @Override
    public List<Long> listUserIdsByJobId(Long jobId) {
        return lambdaQuery().eq(UserJob::getJobId, jobId).list().stream().map(UserJob::getUserId).collect(Collectors.toList());
    }

    @Override
    public List<Long> listJobIdsByUserId(Long userId) {
        return lambdaQuery().eq(UserJob::getUserId, userId).list().stream().map(UserJob::getJobId).collect(Collectors.toList());
    }

    @Override
    public int countUserIdByJobIds(Collection<Long> jobIds) {
        return lambdaQuery().in(UserJob::getJobId, jobIds).count();
    }
}
