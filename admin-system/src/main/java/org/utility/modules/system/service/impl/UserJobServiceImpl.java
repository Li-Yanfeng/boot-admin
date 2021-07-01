package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.utility.constant.SystemConfigConsts;
import org.utility.modules.system.mapper.UserJobMapper;
import org.utility.modules.system.model.UserJob;
import org.utility.modules.system.service.UserJobService;

import java.util.Collection;

/**
 * 用户岗位关联 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-28
 */
@Service
public class UserJobServiceImpl implements UserJobService {

    private final UserJobMapper userJobMapper;

    public UserJobServiceImpl(UserJobMapper userJobMapper) {
        this.userJobMapper = userJobMapper;
    }

    @Override
    public void removeByUserIds(Collection<Long> userIds) {
        if (CollUtil.isNotEmpty(userIds)) {
            LambdaUpdateWrapper<UserJob> wrapper = Wrappers.lambdaUpdate();
            wrapper.in(UserJob::getUserId, userIds);
            userJobMapper.delete(wrapper);
        }
    }

    @Override
    public void removeByJobIds(Collection<Long> jobIds) {
        if (CollUtil.isNotEmpty(jobIds)) {
            LambdaUpdateWrapper<UserJob> wrapper = Wrappers.lambdaUpdate();
            wrapper.in(UserJob::getJobId, jobIds);
            userJobMapper.delete(wrapper);
        }
    }

    @Override
    public Integer countByJobIds(Collection<Long> jobIds) {
        if (CollUtil.isNotEmpty(jobIds)) {
            LambdaQueryWrapper<UserJob> wrapper = Wrappers.lambdaQuery();
            wrapper.in(UserJob::getJobId, jobIds);
            return userJobMapper.selectCount(wrapper);
        } else {
            return SystemConfigConsts.NO_RECORD;
        }
    }
}
