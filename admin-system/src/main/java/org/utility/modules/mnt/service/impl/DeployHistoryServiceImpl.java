package org.utility.modules.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.stereotype.Service;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.modules.mnt.mapper.DeployHistoryMapper;
import org.utility.modules.mnt.model.DeployHistory;
import org.utility.modules.mnt.service.DeployHistoryService;
import org.utility.modules.mnt.service.dto.DeployHistoryDTO;
import org.utility.modules.mnt.service.dto.DeployHistoryQuery;
import org.utility.util.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 部署历史管理 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-07-01
 */
@Service
public class DeployHistoryServiceImpl extends ServiceImpl<DeployHistoryMapper, DeployHistoryDTO, DeployHistoryQuery, DeployHistory> implements DeployHistoryService {

    @Override
    public void download(HttpServletResponse response, List<DeployHistoryDTO> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (DeployHistoryDTO deployHistory : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(5, true);
            map.put("部署编号", deployHistory.getDeployId());
            map.put("应用名称", deployHistory.getAppName());
            map.put("部署IP", deployHistory.getIp());
            map.put("部署时间", deployHistory.getDeployDate());
            map.put("部署人员", deployHistory.getDeployUser());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
