package org.utility.modules.quartz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.stereotype.Service;
import org.utility.core.service.impl.BaseServiceImpl;
import org.utility.modules.quartz.mapper.QuartzLogMapper;
import org.utility.modules.quartz.model.QuartzLog;
import org.utility.modules.quartz.service.QuartzLogService;
import org.utility.modules.quartz.service.dto.QuartzJobQuery;
import org.utility.util.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 定时任务日志 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Service
public class QuartzLogServiceImpl extends BaseServiceImpl<QuartzLogMapper, QuartzJobQuery, QuartzLog> implements QuartzLogService {

    @Override
    public void download(HttpServletResponse response, List<QuartzLog> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (QuartzLog quartzLog : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(9, true);
            map.put("任务名称", quartzLog.getJobName());
            map.put("Bean名称", quartzLog.getBeanName());
            map.put("执行方法", quartzLog.getMethodName());
            map.put("参数", quartzLog.getParams());
            map.put("表达式", quartzLog.getCronExpression());
            map.put("异常详情", quartzLog.getExceptionDetail());
            map.put("耗时/毫秒", quartzLog.getTime());
            map.put("状态", quartzLog.getSuccess() ? "成功" : "失败");
            map.put("创建日期", quartzLog.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
