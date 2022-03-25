package com.boot.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.config.bean.FileProperties;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.UserErrorCode;
import com.boot.admin.mapper.LocalStorageMapper;
import com.boot.admin.model.LocalStorage;
import com.boot.admin.service.LocalStorageService;
import com.boot.admin.service.dto.LocalStorageQuery;
import com.boot.admin.util.FileUtils;
import com.boot.admin.util.ImageUtils;
import com.boot.admin.util.QueryHelp;
import com.boot.admin.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Value(value = "${domain}")
    private String domain;

    private final FileProperties fileProperties;

    public LocalStorageServiceImpl(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    @Override
    public LocalStorage uploadLocalStorage(MultipartFile file) {
        FileUtils.checkSize(fileProperties.getMaxSize(), file.getSize());
        String suffix = FileUtils.getExtensionName(file.getOriginalFilename());
        String type = FileUtils.getFileType(suffix);
        String rootPath = fileProperties.getPath().getPath();
        String filePath = rootPath + type;
        // 上传文件
        File uploadFile = FileUtils.upload(file, filePath);
        if (ObjectUtil.isNull(uploadFile)) {
            throw new BadRequestException(UserErrorCode.USER_UPLOAD_FILE_IS_ABNORMAL);
        }
        // 压缩文件
        File compressFile = null;
        if (FileUtils.IMAGE.equals(type) && fileProperties.isCompressImage()) {
            compressFile = ImageUtils.compress(uploadFile);
        }
        try {
            LocalStorage localStorage = new LocalStorage(
                uploadFile.getName(),
                FileUtils.getFileNameNoEx(file.getOriginalFilename()),
                suffix,
                type,
                FileUtils.getSize(file.getSize()),
                FileUtils.replaceAccessPath(uploadFile.getPath(), rootPath, domain)
            );
            if (compressFile != null) {
                localStorage.setCompressPath(FileUtils.replaceAccessPath(compressFile.getPath(), rootPath, domain));
            }
            baseMapper.insert(localStorage);
            return localStorage;
        } catch (Exception e) {
            FileUtils.del(uploadFile);
            if (compressFile != null) {
                FileUtils.del(compressFile);
            }
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
        return baseMapper.selectList(QueryHelp.queryWrapper(query));
    }

    @Override
    public Page<LocalStorage> listLocalStorages(LocalStorageQuery query, Page<LocalStorage> page) {
        return baseMapper.selectPage(page, QueryHelp.queryWrapper(query));
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
