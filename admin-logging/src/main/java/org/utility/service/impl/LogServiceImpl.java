package org.utility.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.mapper.LogMapper;
import org.utility.model.Log;
import org.utility.service.LogService;
import org.utility.service.dto.LogDTO;
import org.utility.service.dto.LogQuery;
import org.utility.service.dto.LogSmallDTO;
import org.utility.util.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 日志 服务实现类
 *
 * @author Li Yanfeng
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLog(String username, String browser, String ip, ProceedingJoinPoint joinPoint, Log resource) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        org.utility.annotation.Log aopLog = method.getAnnotation(org.utility.annotation.Log.class);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        resource.setDescription(aopLog.value());
        resource.setRequestIp(ip);
        resource.setAddress(IpUtils.getCityInfo(resource.getRequestIp()));
        resource.setMethod(methodName);
        resource.setUsername(username);
        resource.setParams(getParameter(method, joinPoint.getArgs()));
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
    public void updateLogById(Log resource) {
        Long logId = resource.getLogId();
        ValidationUtils.notNull(baseMapper.selectById(logId), "Log", "logId", logId);
        baseMapper.updateById(resource);
    }

    @Override
    public List<LogDTO> listLogs(LogQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), LogDTO.class);
    }

    @Override
    public Page<LogDTO> listLogs(LogQuery query, Page<Log> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), LogDTO.class);
    }

    @Override
    public Page<LogSmallDTO> listLogsByUser(LogQuery query, Page<Log> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), LogSmallDTO.class);
    }

    @Override
    public LogDTO getLogById(Long id) {
        Log log = baseMapper.selectById(id);
        ValidationUtils.notNull(log, "Log", "logId", id);
        return ConvertUtils.convert(log, LogDTO.class);
    }

    @Override
    public void exportLog(List<LogDTO> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(log -> {
            Map<String, Object> map = MapUtil.newHashMap(8, true);
            map.put("用户名", log.getUsername());
            map.put("IP", log.getRequestIp());
            map.put("IP来源", log.getAddress());
            map.put("描述", log.getDescription());
            map.put("浏览器", log.getBrowser());
            map.put("请求耗时/毫秒", log.getTime());
            map.put("异常详情", new String(Optional.of(log.getExceptionDetail()).orElseGet(""::getBytes)));
            map.put("创建日期", log.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private String getParameter(Method method, Object[] args) {
        List<Object> argList = CollUtil.newArrayList();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StrUtil.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return "";
        }
        return argList.size() == 1 ? JSONUtil.toJsonStr(argList.get(0)) : JSONUtil.toJsonStr(argList);
    }
}
