package com.boot.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 七牛云文件 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
public class QiniuContentServiceImpl extends ServiceImpl<QiniuContentMapper, QiniuContent> implements QiniuContentService {

    @Value("${qiniu.max-size}")
    private Long maxSize;

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
                bucketManager.delete(content.getBucket(), content.getKey() + "." + content.getSuffix());
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
        ValidationUtils.notNull(qiniuContent, "QiniuContent", "contentId", id);
        return qiniuContent;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public QiniuContent uploadContent(MultipartFile file, QiniuConfig config) {
        FileUtils.checkSize(maxSize, file.getSize());
        if (config.getConfigId() == null) {
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtils.getRegion(config.getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        String upToken = auth.uploadToken(config.getBucket());
        try {
            String key = file.getOriginalFilename();
            if (baseMapper.selectByKey(key) != null) {
                key = QiNiuUtils.getKey(key);
            }
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            QiniuContent content = baseMapper.selectByKey(FileUtils.getFileNameNoEx(putRet.key));
            if (content == null) {
                //存入数据库
                QiniuContent qiniuContent = new QiniuContent();
                qiniuContent.setSuffix(FileUtils.getExtensionName(putRet.key));
                qiniuContent.setBucket(config.getBucket());
                qiniuContent.setType(config.getType());
                qiniuContent.setKey(FileUtils.getFileNameNoEx(putRet.key));
                qiniuContent.setUrl(config.getHost() + "/" + putRet.key);
                qiniuContent.setSize(FileUtils.getSize(Integer.parseInt(file.getSize() + "")));
                baseMapper.insert(qiniuContent);
                return qiniuContent;
            }
            return content;
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
    public void exportQiniuContent(List<QiniuContent> exportData, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();
        exportData.forEach(content -> {
            Map<String, Object> map = MapUtil.newHashMap(6, true);
            map.put("文件名", content.getKey());
            map.put("文件类型", content.getSuffix());
            map.put("空间名称", content.getBucket());
            map.put("文件大小", content.getSize());
            map.put("空间类型", content.getType());
            map.put("创建日期", content.getUpdateTime());
            list.add(map);
        });
        FileUtils.downloadExcel(list, response);
    }
}
