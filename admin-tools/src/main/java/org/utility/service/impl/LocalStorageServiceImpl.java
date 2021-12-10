package org.utility.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.utility.config.FileProperties;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.exception.BadRequestException;
import org.utility.exception.enums.UserErrorCode;
import org.utility.mapper.LocalStorageMapper;
import org.utility.model.LocalStorage;
import org.utility.service.LocalStorageService;
import org.utility.service.dto.LocalStorageQuery;
import org.utility.util.ConvertUtils;
import org.utility.util.FileUtils;
import org.utility.util.QueryHelp;
import org.utility.util.ValidationUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 本地存储 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
public class LocalStorageServiceImpl extends ServiceImpl<LocalStorageMapper, LocalStorage> implements LocalStorageService {

    private final FileProperties fileProperties;

    public LocalStorageServiceImpl(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    @Override
    public void uploadLocalStorage(String filename, MultipartFile file) {
        FileUtils.checkSize(fileProperties.getMaxSize(), file.getSize());
        String suffix = FileUtils.getExtensionName(file.getOriginalFilename());
        String type = FileUtils.getFileType(suffix);
        File uploadFile = FileUtils.upload(file, fileProperties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(uploadFile)) {
            throw new BadRequestException(UserErrorCode.USER_UPLOAD_FILE_IS_ABNORMAL);
        }
        try {
            filename = StrUtil.isBlank(filename) ? FileUtils.getFileNameNoEx(file.getOriginalFilename()) : filename;
            LocalStorage localStorage = new LocalStorage(
                uploadFile.getName(),
                filename,
                suffix,
                uploadFile.getPath(),
                type,
                FileUtils.getSize(file.getSize())
            );
            baseMapper.insert(localStorage);
        } catch (Exception e) {
            FileUtils.del(uploadFile);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeLocalStorageByIds(Collection<Long> ids) {
        List<LocalStorage> localStorages = baseMapper.selectBatchIds(ids);
        baseMapper.deleteBatchIds(ids);
        for (LocalStorage storage : localStorages) {
            FileUtils.del(storage.getPath());
        }
    }

    @Override
    public void updateLocalStorageById(LocalStorage resource) {
        Long storageId = resource.getStorageId();
        ValidationUtils.notNull(baseMapper.selectById(storageId), "LocalStorage", "storageId", storageId);
        baseMapper.updateById(resource);
    }

    @Override
    public List<LocalStorage> listLocalStorages(LocalStorageQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), LocalStorage.class);
    }

    @Override
    public Page<LocalStorage> listLocalStorages(LocalStorageQuery query, Page<LocalStorage> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), LocalStorage.class);
    }

    @Override
    public LocalStorage getLocalStorageById(Long id) {
        LocalStorage localStorage = baseMapper.selectById(id);
        ValidationUtils.notNull(localStorage, "LocalStorage", "storageId", id);
        return localStorage;
    }

    @Override
    public void exportLocalStorage(List<LocalStorage> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(localStorage -> {
            Map<String, Object> map = MapUtil.newHashMap(6, true);
            map.put("文件名", localStorage.getRealName());
            map.put("备注名", localStorage.getName());
            map.put("文件类型", localStorage.getType());
            map.put("文件大小", localStorage.getSize());
            map.put("创建者", localStorage.getCreateBy());
            map.put("创建日期", localStorage.getCreateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }
}
