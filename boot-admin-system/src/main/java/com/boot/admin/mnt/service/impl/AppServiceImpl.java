package com.boot.admin.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.mnt.mapper.AppMapper;
import com.boot.admin.mnt.model.App;
import com.boot.admin.mnt.service.AppService;
import com.boot.admin.mnt.service.dto.AppDTO;
import com.boot.admin.mnt.service.dto.AppQuery;
import com.boot.admin.util.ConvertUtils;
import com.boot.admin.util.FileUtils;
import com.boot.admin.util.QueryHelp;
import com.boot.admin.util.ValidationUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 应用 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Override
    public void saveApp(App resource) {
        verification(resource);
        baseMapper.insert(resource);
    }

    @Override
    public void removeAppByIds(Collection<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateAppById(App resource) {
        Long appId = resource.getAppId();
        ValidationUtils.notNull(baseMapper.selectById(appId), "App", "appId", appId);
        verification(resource);
        baseMapper.updateById(resource);
    }

    @Override
    public List<AppDTO> listApps(AppQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), AppDTO.class);
    }

    @Override
    public Page<AppDTO> listApps(AppQuery query, Page<App> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), AppDTO.class);
    }

    @Override
    public AppDTO getAppById(Long id) {
        App app = baseMapper.selectById(id);
        ValidationUtils.notNull(app, "App", "appId", id);
        return ConvertUtils.convert(app, AppDTO.class);
    }

    @Override
    public void exportApp(List<AppDTO> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(app -> {
            Map<String, Object> map = MapUtil.newHashMap(8, true);
            map.put("应用名称", app.getName());
            map.put("端口", app.getPort());
            map.put("上传目录", app.getUploadPath());
            map.put("部署目录", app.getDeployPath());
            map.put("备份目录", app.getBackupPath());
            map.put("启动脚本", app.getStartScript());
            map.put("部署脚本", app.getDeployScript());
            map.put("创建日期", app.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }

    /**
     * 验证
     *
     * @param resource /
     */
    private void verification(App resource) {
        String opt = "/opt";
        String home = "/home";
        if (!(resource.getUploadPath().startsWith(opt) || resource.getUploadPath().startsWith(home))) {
            throw new BadRequestException("文件只能上传在opt目录或者home目录 ");
        }
        if (!(resource.getDeployPath().startsWith(opt) || resource.getDeployPath().startsWith(home))) {
            throw new BadRequestException("文件只能部署在opt目录或者home目录 ");
        }
        if (!(resource.getBackupPath().startsWith(opt) || resource.getBackupPath().startsWith(home))) {
            throw new BadRequestException("文件只能备份在opt目录或者home目录 ");
        }
    }
}
