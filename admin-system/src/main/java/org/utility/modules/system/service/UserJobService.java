package org.utility.modules.system.service;

import java.util.Collection;

/**
 * 用户岗位关联 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface UserJobService {

    /**
     * 根据 userId 删除
     *
     * @param userIds 用户Id集合
     */
    void removeByUserIds(Collection<Long> userIds);

    /**
     * 根据 jobId 删除
     *
     * @param jobIds 岗位Id集合
     */
    void removeByJobIds(Collection<Long> jobIds);

    /**
     * 根据 jobId 查询总记录数
     *
     * @param jobIds 岗位Id集合
     * @return /
     */
    Integer countByJobIds(Collection<Long> jobIds);
}
