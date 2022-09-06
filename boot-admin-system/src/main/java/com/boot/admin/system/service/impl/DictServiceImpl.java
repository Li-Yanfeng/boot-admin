package com.boot.admin.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.constant.CacheKey;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.system.mapper.DictMapper;
import com.boot.admin.system.model.Dict;
import com.boot.admin.system.service.DictDetailService;
import com.boot.admin.system.service.DictService;
import com.boot.admin.system.service.dto.DictDTO;
import com.boot.admin.system.service.dto.DictDetailDTO;
import com.boot.admin.system.service.dto.DictDetailQuery;
import com.boot.admin.system.service.dto.DictQuery;
import com.boot.admin.util.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 字典 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"dict"})
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private final DictDetailService dictDetailService;
    private final RedisUtils redisUtils;

    public DictServiceImpl(DictDetailService dictDetailService, RedisUtils redisUtils) {
        this.dictDetailService = dictDetailService;
        this.redisUtils = redisUtils;
    }

    @Override
    public void saveDict(Dict resource) {
        baseMapper.insert(resource);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeDictByIds(Collection<Long> ids) {
        List<Dict> dicts = baseMapper.selectBatchIds(ids);
        for (Dict dict : dicts) {
            // 清理缓存
            delCaches(dict);
        }
        baseMapper.deleteBatchIds(ids);
        dictDetailService.removeDictDetailByDictIds(ids);
    }

    @Override
    public void updateDictById(Dict resource) {
        Long dictId = resource.getDictId();
        Assert.notNull(baseMapper.selectById(dictId));
        baseMapper.updateById(resource);
        // 清理缓存
        delCaches(resource);
    }

    @Cacheable(key = "'pid:' + #p0.name", condition = "#p0.name != null")
    @Override
    public List<DictDTO> listDicts(DictQuery query) {
        List<DictDTO> dicts = ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), DictDTO.class);
        // 获取关联数据
        dicts.forEach(this::getRelevantData);
        return dicts;
    }

    @Override
    public Page<DictDTO> listDicts(DictQuery query, Page<Dict> page) {
        Page<DictDTO> dicts = ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)),
            DictDTO.class);
        List<DictDTO> records = dicts.getRecords();
        // 获取关联数据
        records.forEach(this::getRelevantData);
        return dicts;
    }

    @Cacheable(key = "'id:' + #p0")
    @Override
    public DictDTO getDictById(Long id) {
        Dict dict = baseMapper.selectById(id);
        Assert.notNull(dict);
        DictDTO dictDTO = ConvertUtils.convert(dict, DictDTO.class);
        // 获取关联数据
        getRelevantData(dictDTO);
        return dictDTO;
    }

    @Override
    public void exportDict(List<DictDTO> exportData, HttpServletResponse response) {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(dict -> {
            List<DictDetailDTO> dictDetails = dict.getDictDetails();
            if (CollectionUtil.isNotEmpty(dictDetails)) {
                dictDetails.forEach(dictDetail -> {
                    Map<String, Object> map = MapUtil.newHashMap(5, true);
                    map.put("字典名称", dict.getName());
                    map.put("字典描述", dict.getDescription());
                    map.put("字典标签", dictDetail.getLabel());
                    map.put("字典值", dictDetail.getValue());
                    map.put("创建日期", dictDetail.getCreateTime());
                    list.add(map);
                });
            } else {
                Map<String, Object> map = MapUtil.newHashMap(5, true);
                map.put("字典名称", dict.getName());
                map.put("字典描述", dict.getDescription());
                map.put("字典标签", null);
                map.put("字典值", null);
                map.put("创建日期", dict.getCreateTime());
                list.add(map);
            }
        });
        FileUtils.downloadExcel("字典", list, response);
    }

    /**
     * 获取关联数据
     */
    private void getRelevantData(DictDTO dictDTO) {
        DictDetailQuery dictDetailQuery = new DictDetailQuery();
        dictDetailQuery.setDictId(dictDTO.getDictId());
        dictDTO.setDictDetails(dictDetailService.listDictDetails(dictDetailQuery));
    }

    /**
     * 清理缓存
     *
     * @param dict 字典
     */
    private void delCaches(Dict dict) {
        redisUtils.del(CacheKey.DICT_ID + dict.getDictId());
        redisUtils.del(CacheKey.DICT_NAME + dict.getName());
        redisUtils.del(CacheKey.DICT_DETAIL_DICT_ID + dict.getDictId());
    }
}
