package org.utility.mnt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.mnt.mapper.DeployHistoryMapper;
import org.utility.mnt.model.DeployHistory;
import org.utility.mnt.service.DeployHistoryService;
import org.utility.mnt.service.dto.DeployHistoryDTO;
import org.utility.mnt.service.dto.DeployHistoryQuery;
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
 * 部署历史 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
public class DeployHistoryServiceImpl extends ServiceImpl<DeployHistoryMapper, DeployHistory> implements DeployHistoryService {

    @Override
    public void saveDeployHistory(DeployHistory resource) {
        baseMapper.insert(resource);
    }

    @Override
    public void removeDeployHistoryByIds(Collection<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void updateDeployHistoryById(DeployHistory resource) {
        Long historyId = resource.getHistoryId();
        ValidationUtils.notNull(baseMapper.selectById(historyId), "DeployHistory", "historyId", historyId);
        baseMapper.updateById(resource);
    }

    @Override
    public List<DeployHistoryDTO> listDeployHistorys(DeployHistoryQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), DeployHistoryDTO.class);
    }

    @Override
    public Page<DeployHistoryDTO> listDeployHistorys(DeployHistoryQuery query, Page<DeployHistory> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), DeployHistoryDTO.class);
    }

    @Override
    public DeployHistoryDTO getDeployHistoryById(Long id) {
        DeployHistory deployHistory = baseMapper.selectById(id);
        ValidationUtils.notNull(deployHistory, "DeployHistory", "historyId", id);
        return ConvertUtils.convert(deployHistory, DeployHistoryDTO.class);
    }

    @Override
    public void exportDeployHistory(List<DeployHistoryDTO> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(deployHistory -> {
            Map<String, Object> map = MapUtil.newHashMap(5, true);
            map.put("部署编号", deployHistory.getDeployId());
            map.put("应用名称", deployHistory.getAppName());
            map.put("部署IP", deployHistory.getIp());
            map.put("部署时间", deployHistory.getDeployDate());
            map.put("部署人员", deployHistory.getDeployUser());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }
}
