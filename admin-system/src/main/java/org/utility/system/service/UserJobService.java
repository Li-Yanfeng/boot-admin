package org.utility.system.service;

import org.utility.core.service.Service;
import org.utility.system.model.UserJob;

import java.util.Collection;
import java.util.List;

/**
 * 用户岗位关联 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
public interface UserJobService extends Service<UserJob> {

    /**
     * 根据 userIds 删除
     *
     * @param userIds 用户ID列表
     */
    void removeUserJobByUserIds(Collection<Long> userIds);

    /**
     * 根据 userId 和 jobIds 删除
     *
     * @param userId 用户ID
     * @param jobIds 岗位ID列表
     */
    void removeUserJobByUserIdEqAndJobIdsIn(Long userId, Collection<Long> jobIds);

    /**
     * 根据 jobIds 删除
     *
     * @param jobIds 岗位ID列表
     */
    void removeUserJobByJobIds(Collection<Long> jobIds);

    /**
     * 根据 jobId 和 userIds 删除
     *
     * @param jobId   岗位ID
     * @param userIds 用户ID列表
     */
    void removeUserJobByJobIdEqAndUserIdsIn(Long jobId, Collection<Long> userIds);

    /**
     * 根据 jobId 查询
     *
     * @param jobId 岗位ID
     * @return 列表查询结果
     */
    List<Long> listUserIdsByJobId(Long jobId);

    /**
     * 根据 userId 查询
     *
     * @param userId 用户ID
     * @return 列表查询结果
     */
    List<Long> listJobIdsByUserId(Long userId);

    /**
     * 根据 jobIds 统计
     *
     * @param jobIds 岗位ID列表
     * @return 列表查询结果
     */
    int countUserIdByJobIds(Collection<Long> jobIds);
}
