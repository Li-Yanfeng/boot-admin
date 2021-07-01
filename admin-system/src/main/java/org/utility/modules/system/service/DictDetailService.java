package org.utility.modules.system.service;

import org.utility.base.Service;
import org.utility.modules.system.model.DictDetail;
import org.utility.modules.system.service.dto.DictDetailDTO;
import org.utility.modules.system.service.dto.DictDetailQuery;

import java.util.Collection;
import java.util.List;

/**
 * 数据字典详情 服务类
 *
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public interface DictDetailService extends Service<DictDetailDTO, DictDetailQuery, DictDetail> {

    /**
     * 根据 dictId 删除
     *
     * @param dictIds 数据字典ID集合
     */
    void removeByDictIds(Collection<Long> dictIds);


    /**
     * 根根据 dictName 查询
     *
     * @param dictName 字典名称
     * @return /
     */
    List<DictDetailDTO> listByDictName(String dictName);
}
