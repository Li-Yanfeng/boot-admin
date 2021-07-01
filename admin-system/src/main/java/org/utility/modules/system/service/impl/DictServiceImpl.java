package org.utility.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.base.impl.ServiceImpl;
import org.utility.constant.CacheConsts;
import org.utility.modules.system.mapper.DictMapper;
import org.utility.modules.system.model.Dict;
import org.utility.modules.system.service.DictDetailService;
import org.utility.modules.system.service.DictService;
import org.utility.modules.system.service.dto.DictDTO;
import org.utility.modules.system.service.dto.DictDetailDTO;
import org.utility.modules.system.service.dto.DictQuery;
import org.utility.util.FileUtils;
import org.utility.util.RedisUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据字典 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-28
 */
@Service
@CacheConfig(cacheNames = {"dict"})
public class DictServiceImpl extends ServiceImpl<DictMapper, DictDTO, DictQuery, Dict> implements DictService {

    private final DictMapper dictMapper;
    private final DictDetailService dictDetailService;

    private final RedisUtils redisUtils;

    public DictServiceImpl(DictMapper dictMapper, DictDetailService dictDetailService, RedisUtils redisUtils) {
        this.dictMapper = dictMapper;
        this.dictDetailService = dictDetailService;
        this.redisUtils = redisUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByIds(Collection<Long> ids) {
        List<Dict> dicts = dictMapper.selectBatchIds(ids);
        for (Dict dict : dicts) {
            this.delCaches(dict);
        }
        dictMapper.deleteBatchIds(ids);
        dictDetailService.removeByDictIds(ids);
    }

    @Override
    public void updateById(Dict resource) {
        super.updateById(resource);
        redisUtils.del(CacheConsts.DICT_ID + resource.getDictId());
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public DictDTO getById(Long id) {
        return super.getById(id);
    }

    @Override
    public void download(HttpServletResponse response, List<DictDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (DictDTO dict : queryAll) {
            if (CollectionUtil.isNotEmpty(dict.getDictDetails())) {
                for (DictDetailDTO dictDetail : dict.getDictDetails()) {
                    Map<String, Object> map = MapUtil.newHashMap(5, true);
                    map.put("字典名称", dict.getName());
                    map.put("字典描述", dict.getDescription());
                    map.put("字典标签", dictDetail.getLabel());
                    map.put("字典值", dictDetail.getValue());
                    map.put("创建日期", dictDetail.getCreateTime());
                    list.add(map);
                }
            } else {
                Map<String, Object> map = MapUtil.newHashMap(5, true);
                map.put("字典名称", dict.getName());
                map.put("字典描述", dict.getDescription());
                map.put("字典标签", null);
                map.put("字典值", null);
                map.put("创建日期", dict.getCreateTime());
                list.add(map);
            }
        }
        FileUtils.downloadExcel(list, response);
    }

    private void delCaches(Dict dict) {
        redisUtils.del(CacheConsts.DICTDEAIL_DICTNAME + dict.getName());
        redisUtils.del(CacheConsts.DICTDEAIL_DICTID + dict.getDictId());
        redisUtils.del(CacheConsts.DICT_ID + dict.getDictId());
    }
}
