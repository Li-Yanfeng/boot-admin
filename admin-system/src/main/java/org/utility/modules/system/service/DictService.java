package org.utility.modules.system.service;

import org.utility.base.Service;
import org.utility.modules.system.model.Dict;
import org.utility.modules.system.service.dto.DictDTO;
import org.utility.modules.system.service.dto.DictQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 数据字典 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface DictService extends Service<DictDTO, DictQuery, Dict> {

    /**
     * 导出数据
     *
     * @param response 响应对象
     * @param queryAll 待导出的数据
     * @throws IOException /
     */
    void download(HttpServletResponse response, List<DictDTO> queryAll) throws IOException;
}
