package com.boot.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.annotation.Log;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.mapper.LogMapper;
import com.boot.admin.service.LogService;
import com.boot.admin.service.dto.LogDTO;
import com.boot.admin.service.dto.LogQuery;
import com.boot.admin.service.dto.LogSmallDTO;
import com.boot.admin.util.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 日志 服务实现类
 *
 * @author Li Yanfeng
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, com.boot.admin.model.Log> implements LogService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLog(String browser, String ip, ProceedingJoinPoint joinPoint, com.boot.admin.model.Log resource) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        resource.setDescription(aopLog.value());
        resource.setRequestIp(ip);
        resource.setAddress(IpUtils.getCityInfo(resource.getRequestIp()));
        resource.setMethod(methodName);
        resource.setParams(JSONUtil.toJsonStr(joinPoint.getArgs()));
        resource.setBrowser(browser);
        if (resource.getLogId() != null) {
            baseMapper.updateById(resource);
        } else {
            baseMapper.insert(resource);
        }
    }

    @Override
    public void removeLogByIds(Collection<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateLogById(com.boot.admin.model.Log resource) {
        Long logId = resource.getLogId();
        Assert.notNull(baseMapper.selectById(logId));
        baseMapper.updateById(resource);
    }

    @Override
    public List<LogDTO> listLogs(LogQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), LogDTO.class);
    }

    @Override
    public Page<LogDTO> listLogs(LogQuery query, Page<com.boot.admin.model.Log> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), LogDTO.class);
    }

    @Override
    public Page<LogSmallDTO> listLogsByUser(LogQuery query, Page<com.boot.admin.model.Log> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), LogSmallDTO.class);
    }

    @Override
    public LogDTO getLogById(Long id) {
        com.boot.admin.model.Log log = baseMapper.selectById(id);
        Assert.notNull(log);
        return ConvertUtils.convert(log, LogDTO.class);
    }

    @Override
    public void exportLog(List<LogDTO> exportData, HttpServletResponse response) {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(log -> {
            Map<String, Object> map = MapUtil.newHashMap(8, true);
            map.put("用户名", log.getCreateByName());
            map.put("IP", log.getRequestIp());
            map.put("IP来源", log.getAddress());
            map.put("描述", log.getDescription());
            map.put("浏览器", log.getBrowser());
            map.put("请求耗时/毫秒", log.getTime());
            map.put("异常详情", new String(Optional.of(log.getExceptionDetail()).orElseGet(""::getBytes)));
            map.put("创建日期", log.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel("操作日志", list, response);
    }
}
