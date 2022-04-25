package com.boot.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.constant.CacheKey;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.system.mapper.DictDetailMapper;
import com.boot.admin.system.model.DictDetail;
import com.boot.admin.system.service.DictDetailService;
import com.boot.admin.system.service.dto.DictDetailDTO;
import com.boot.admin.system.service.dto.DictDetailQuery;
import com.boot.admin.util.Assert;
import com.boot.admin.util.ConvertUtils;
import com.boot.admin.util.QueryHelp;
import com.boot.admin.util.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 字典详情 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
public class DictDetailServiceImpl extends ServiceImpl<DictDetailMapper, DictDetail> implements DictDetailService {

    private final RedisUtils redisUtils;

    public DictDetailServiceImpl(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public void saveDictDetail(DictDetail resource) {
        baseMapper.insert(resource);
        // 清理缓存
        delCaches(resource.getDictId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeDictDetailByIds(Collection<Long> ids) {
        List<DictDetail> dictDetails = baseMapper.selectBatchIds(ids);
        dictDetails.forEach(dictDetail -> {
            // 清理缓存
            delCaches(dictDetail.getDictId());
        });
        baseMapper.deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeDictDetailByDictIds(Collection<Long> dictIds) {
        // 清理缓存
        dictIds.forEach(this::delCaches);
        lambdaUpdate().in(DictDetail::getDictId, dictIds).remove();
    }

    @Override
    public void updateDictDetailById(DictDetail resource) {
        Long detailId = resource.getDetailId();
        Assert.notNull(baseMapper.selectById(detailId));
        baseMapper.updateById(resource);
        // 清理缓存
        delCaches(resource.getDictId());
    }

    @Override
    public List<DictDetailDTO> listDictDetails(DictDetailQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), DictDetailDTO.class);
    }

    @Override
    public Page<DictDetailDTO> listDictDetails(DictDetailQuery query, Page<DictDetail> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), DictDetailDTO.class);
    }

    /**
     * 清理缓存
     *
     * @param dictId 字典ID
     */
    private void delCaches(Long dictId) {
        redisUtils.del(CacheKey.DICT_ID + dictId);
    }
}
