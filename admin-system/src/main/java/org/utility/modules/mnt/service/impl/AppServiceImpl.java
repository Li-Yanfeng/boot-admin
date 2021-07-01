package org.utility.modules.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.stereotype.Service;
import org.utility.base.impl.ServiceImpl;
import org.utility.exception.BadRequestException;
import org.utility.modules.mnt.mapper.AppMapper;
import org.utility.modules.mnt.model.App;
import org.utility.modules.mnt.service.AppService;
import org.utility.modules.mnt.service.dto.AppDTO;
import org.utility.modules.mnt.service.dto.AppQuery;
import org.utility.util.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 应用管理 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, AppDTO, AppQuery, App> implements AppService {

    @Override
    public void save(App resource) {
        this.verification(resource);
        super.save(resource);
    }

    @Override
    public void updateById(App resource) {
        this.verification(resource);
        super.updateById(resource);
    }

    @Override
    public void download(HttpServletResponse response, List<AppDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (AppDTO app : queryAll) {
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
        }
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
