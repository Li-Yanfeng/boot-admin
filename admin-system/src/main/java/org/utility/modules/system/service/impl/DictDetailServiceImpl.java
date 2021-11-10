package org.utility.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.constant.CacheConsts;
import org.utility.modules.system.mapper.DictDetailMapper;
import org.utility.modules.system.mapper.DictMapper;
import org.utility.modules.system.model.Dict;
import org.utility.modules.system.model.DictDetail;
import org.utility.modules.system.service.DictDetailService;
import org.utility.modules.system.service.dto.DictDetailDTO;
import org.utility.modules.system.service.dto.DictDetailQuery;
import org.utility.util.ConvertUtils;
import org.utility.util.RedisUtils;

import java.util.Collection;
import java.util.List;

/**
 * 数据字典详情 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-28
 */
@Service
@CacheConfig(cacheNames = {"dictDetail"})
public class DictDetailServiceImpl extends ServiceImpl<DictDetailMapper, DictDetailDTO, DictDetailQuery, DictDetail> implements DictDetailService {

    private final DictDetailMapper dictDetailMapper;
    private final DictMapper dictMapper;

    private final RedisUtils redisUtils;

    public DictDetailServiceImpl(DictDetailMapper dictDetailMapper, DictMapper dictMapper, RedisUtils redisUtils) {
        this.dictDetailMapper = dictDetailMapper;
        this.dictMapper = dictMapper;
        this.redisUtils = redisUtils;
    }

    @Override
    public void save(DictDetail resource) {
        dictDetailMapper.insert(resource);
        // 清理缓存
        delCaches(resource.getDictId());
    }

    @Override
    public void removeByDictIds(Collection<Long> dictIds) {
        for (Long dictId : dictIds) {
            // 清理缓存
            delCaches(dictId);
        }
        LambdaUpdateWrapper<DictDetail> wrapper = Wrappers.lambdaUpdate();
        wrapper.in(DictDetail::getDictId, dictIds);
        dictDetailMapper.delete(wrapper);
    }

    @Override
    public void updateById(DictDetail resource) {
        dictDetailMapper.updateById(resource);
        // 清理缓存
        delCaches(resource.getDictId());
    }

    @Cacheable(key = "'name:' + #p0")
    @Override
    public List<DictDetailDTO> listByDictName(String dictName) {
        return ConvertUtils.convertList(dictDetailMapper.selectListByDictName(dictName), DictDetailDTO.class);
    }

    private void delCaches(Long dictId) {
        Dict dict = dictMapper.selectById(dictId);
        redisUtils.del(CacheConsts.DICT_NAME + dict.getName());
    }
}
