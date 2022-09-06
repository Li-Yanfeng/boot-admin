package com.boot.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.admin.config.bean.QiniuProperties;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.mapper.QiniuContentMapper;
import com.boot.admin.model.QiniuConfig;
import com.boot.admin.model.QiniuContent;
import com.boot.admin.service.QiniuContentService;
import com.boot.admin.service.dto.QiniuContentQuery;
import com.boot.admin.util.*;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 七牛云文件 服务实现类
 *
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Service
public class QiniuContentServiceImpl extends ServiceImpl<QiniuContentMapper, QiniuContent> implements QiniuContentService {

    private final QiniuProperties qiniuProperties;

    public QiniuContentServiceImpl(QiniuProperties qiniuProperties) {
        this.qiniuProperties = qiniuProperties;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeQiniuContentByIds(Collection<Long> ids, QiniuConfig config) {
        List<QiniuContent> qiniuContents = baseMapper.selectBatchIds(ids);
        for (QiniuContent content : qiniuContents) {
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(QiNiuUtils.getRegion(config.getZone()));
            Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
            BucketManager bucketManager = new BucketManager(auth, cfg);
            try {
                bucketManager.delete(content.getBucket(), content.getName() + "." + content.getSuffix());
                baseMapper.deleteById(content);
            } catch (QiniuException ex) {
                baseMapper.deleteById(content);
            }
        }
    }

    @Override
    public List<QiniuContent> listQiniuContents(QiniuContentQuery query) {
        return ConvertUtils.convert(baseMapper.selectList(QueryHelp.queryWrapper(query)), QiniuContent.class);
    }

    @Override
    public Page<QiniuContent> listQiniuContents(QiniuContentQuery query, Page<QiniuContent> page) {
        return ConvertUtils.convert(baseMapper.selectPage(page, QueryHelp.queryWrapper(query)), QiniuContent.class);
    }

    @Override
    public QiniuContent getQiniuContentById(Long id) {
        QiniuContent qiniuContent = baseMapper.selectById(id);
        Assert.notNull(qiniuContent);
        return qiniuContent;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public QiniuContent uploadContent(MultipartFile file, QiniuConfig config) {
        FileUtils.checkSize(qiniuProperties.getMaxSize(), file.getSize());
        String suffix = FileUtils.getExtensionName(file.getOriginalFilename());
        String type = FileUtils.getFileType(suffix);
        String fileDir = qiniuProperties.getPath() + type + StringUtils.SLASH;
        if (config.getConfigId() == null) {
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtils.getRegion(config.getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        String upToken = auth.uploadToken(config.getBucket());
        try {
            String filename = QiNiuUtils.getKey(file.getOriginalFilename());
            // 原始文件
            Response uploadFileResponse = uploadManager.put(file.getBytes(), fileDir + filename, upToken);
            // 解析上传成功的结果
            DefaultPutRet uploadFilePutRet = JSON.parseObject(uploadFileResponse.bodyString(), DefaultPutRet.class);
            DefaultPutRet compressFilePutRet = null;
            // 如果需要压缩图片
            if (FileUtils.IMAGE.equals(type) && qiniuProperties.isCompressImage()) {
                String compressFilePath = fileDir + ImageUtils.COMPRESS_DIR + StringUtils.SLASH + filename;
                File compressFile = ImageUtils.compress(file, compressFilePath);
                if (compressFile != null) {
                    // 压缩文件
                    Response compressFileResponse = uploadManager.put(compressFile, compressFilePath, upToken);
                    compressFilePutRet = JSON.parseObject(compressFileResponse.bodyString(), DefaultPutRet.class);
                }
            }

            // 存入数据库
            QiniuContent qiniuContent = new QiniuContent();
            qiniuContent.setBucket(config.getBucket());
            qiniuContent.setSpaceType(config.getSpaceType());
            qiniuContent.setName(FileUtils.getFileNameNoEx(uploadFilePutRet.key));
            qiniuContent.setSuffix(suffix);
            qiniuContent.setType(FileUtils.getFileType(suffix));
            qiniuContent.setSize(FileUtils.getSize(file.getSize()));
            qiniuContent.setUrl(config.getDomain() + StringUtils.SLASH + uploadFilePutRet.key);
            if (compressFilePutRet != null) {
                qiniuContent.setCompressUrl(config.getDomain() + StringUtils.SLASH + compressFilePutRet.key);
            }

            baseMapper.insert(qiniuContent);
            return qiniuContent;
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public String downloadContent(QiniuContent content, QiniuConfig config) {
        String finalUrl;
        String type = "公开";
        if (type.equals(content.getType())) {
            finalUrl = content.getUrl();
        } else {
            Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
            // 1小时，可以自定义链接过期时间
            long expireInSeconds = 3600;
            finalUrl = auth.privateDownloadUrl(content.getUrl(), expireInSeconds);
        }
        return finalUrl;
    }

    @Override
    public void exportQiniuContent(List<QiniuContent> exportData, HttpServletResponse response) {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(content -> {
            Map<String, Object> map = MapUtil.newHashMap(6, true);
            map.put("空间名称", content.getBucket());
            map.put("文件名称", content.getName());
            map.put("文件后缀", content.getSuffix());
            map.put("文件类型", content.getType());
            map.put("文件大小", content.getSize());
            map.put("创建日期", content.getCreateBy());
            list.add(map);
        });
        FileUtils.downloadExcel("文件", list, response);
    }
}
