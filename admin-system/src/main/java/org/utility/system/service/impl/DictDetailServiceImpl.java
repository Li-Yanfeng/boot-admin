package org.utility.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.constant.CacheKey;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.system.mapper.DictDetailMapper;
import org.utility.system.model.DictDetail;
import org.utility.system.service.DictDetailService;
import org.utility.system.service.dto.DictDetailDTO;
import org.utility.system.service.dto.DictDetailQuery;
import org.utility.util.ConvertUtils;
import org.utility.util.QueryHelp;
import org.utility.util.RedisUtils;
import org.utility.util.ValidationUtils;

import java.util.Collection;
import java.util.List;

/**
 * 数据字典详情 服务实现类
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
        ValidationUtils.notNull(baseMapper.selectById(detailId), "DictDetail", "detailId", detailId);
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
     * @param dictId 数据字典ID
     */
    private void delCaches(Long dictId) {
        redisUtils.del(CacheKey.DICT_ID + dictId);
    }
}
