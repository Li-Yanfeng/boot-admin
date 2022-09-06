package com.boot.admin.quartz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.quartz.mapper.QuartzLogMapper;
import com.boot.admin.quartz.model.QuartzLog;
import com.boot.admin.quartz.service.QuartzLogService;
import com.boot.admin.quartz.service.dto.QuartzJobQuery;
import com.boot.admin.util.Assert;
import com.boot.admin.util.ConvertUtils;
import com.boot.admin.util.FileUtils;
import com.boot.admin.util.QueryHelp;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 定时任务日志 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
public class QuartzLogServiceImpl extends ServiceImpl<QuartzLogMapper, QuartzLog> implements QuartzLogService {

    @Override
    public void saveQuartzLog(QuartzLog resource) {
        baseMapper.insert(resource);
    }

    @Override
    public void removeQuartzLogByIds(Collection<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateQuartzLogById(QuartzLog resource) {
        Assert.notNull(resource.getLogId());
        baseMapper.updateById(resource);
    }

    @Override
    public List<QuartzLog> listQuartzLogs(QuartzJobQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), QuartzLog.class);
    }

    @Override
    public Page<QuartzLog> listQuartzLogs(QuartzJobQuery query, Page<QuartzLog> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), QuartzLog.class);
    }

    @Override
    public QuartzLog getQuartzLogById(Long id) {
        QuartzLog quartzLog = baseMapper.selectById(id);
        Assert.notNull(quartzLog);
        return ConvertUtils.convert(quartzLog, QuartzLog.class);
    }

    @Override
    public void exportQuartzLog(List<QuartzLog> exportData, HttpServletResponse response) {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(quartzLog -> {
            Map<String, Object> map = MapUtil.newHashMap(9, true);
            map.put("任务名称", quartzLog.getJobName());
            map.put("Bean名称", quartzLog.getBeanName());
            map.put("执行方法", quartzLog.getMethodName());
            map.put("参数", quartzLog.getParams());
            map.put("表达式", quartzLog.getCronExpression());
            map.put("异常详情", quartzLog.getExceptionDetail());
            map.put("耗时/毫秒", quartzLog.getTime());
            map.put("状态", CommonConstant.YES.equals(quartzLog.getSuccess()) ? "成功" : "失败");
            map.put("创建日期", quartzLog.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel("定时任务日志", list, response);
    }
}
