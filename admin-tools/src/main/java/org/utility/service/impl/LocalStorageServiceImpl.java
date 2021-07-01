package org.utility.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.utility.base.impl.BaseServiceImpl;
import org.utility.config.FileProperties;
import org.utility.exception.BadRequestException;
import org.utility.exception.enums.UserErrorCode;
import org.utility.mapper.LocalStorageMapper;
import org.utility.model.LocalStorage;
import org.utility.service.LocalStorageService;
import org.utility.service.dto.LocalStorageQuery;
import org.utility.util.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 本地存储 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Service
public class LocalStorageServiceImpl extends BaseServiceImpl<LocalStorageMapper, LocalStorageQuery, LocalStorage> implements LocalStorageService {

    private final LocalStorageMapper localStorageMapper;

    private final FileProperties fileProperties;

    public LocalStorageServiceImpl(LocalStorageMapper localStorageMapper, FileProperties fileProperties) {
        this.localStorageMapper = localStorageMapper;
        this.fileProperties = fileProperties;
    }


    @Override
    public LocalStorage save(String name, MultipartFile file) {
        FileUtils.checkSize(fileProperties.getMaxSize(), file.getSize());
        String suffix = FileUtils.getExtensionName(file.getOriginalFilename());
        String type = FileUtils.getFileType(suffix);
        File uploadFile = FileUtils.upload(file, fileProperties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(uploadFile)) {
            throw new BadRequestException(UserErrorCode.USER_UPLOAD_FILE_IS_ABNORMAL);
        }
        try {
            name = StrUtil.isBlank(name) ? FileUtils.getFileNameNoEx(file.getOriginalFilename()) : name;
            LocalStorage localStorage = new LocalStorage(
                    uploadFile.getName(),
                    name,
                    suffix,
                    uploadFile.getPath(),
                    type,
                    FileUtils.getSize(file.getSize())
            );
            localStorageMapper.insert(localStorage);
            return localStorage;
        } catch (Exception e) {
            FileUtils.del(uploadFile);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByIds(Long[] ids) {
        for (Long id : ids) {
            LocalStorage storage = localStorageMapper.selectById(id);
            FileUtils.del(storage.getPath());
            localStorageMapper.deleteById(id);
        }
    }

    @Override
    public void download(HttpServletResponse response, List<LocalStorage> queryAll) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (LocalStorage localStorage : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(6, true);
            map.put("文件名", localStorage.getRealName());
            map.put("备注名", localStorage.getName());
            map.put("文件类型", localStorage.getType());
            map.put("文件大小", localStorage.getSize());
            map.put("创建者", localStorage.getCreateBy());
            map.put("创建日期", localStorage.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
