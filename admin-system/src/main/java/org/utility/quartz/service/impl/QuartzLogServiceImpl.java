package org.utility.quartz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.utility.constant.SystemConstant;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.quartz.mapper.QuartzLogMapper;
import org.utility.quartz.model.QuartzLog;
import org.utility.quartz.service.QuartzLogService;
import org.utility.quartz.service.dto.QuartzJobQuery;
import org.utility.util.ConvertUtils;
import org.utility.util.FileUtils;
import org.utility.util.QueryHelp;
import org.utility.util.ValidationUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 定时任务日志 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
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
        Long logId = resource.getLogId();
        ValidationUtils.notNull(baseMapper.selectById(logId), "QuartzLog", "logId", logId);
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
        ValidationUtils.notNull(quartzLog, "QuartzLog", "logId", id);
        return ConvertUtils.convert(quartzLog, QuartzLog.class);
    }

    @Override
    public void exportQuartzLog(List<QuartzLog> exportData, HttpServletResponse response) throws IOException {
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
            map.put("状态", SystemConstant.YES.equals(quartzLog.getSuccess()) ? "成功" : "失败");
            map.put("创建日期", quartzLog.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }
}
