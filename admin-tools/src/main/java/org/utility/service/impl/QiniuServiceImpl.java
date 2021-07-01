package org.utility.service.impl;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.utility.base.impl.BaseServiceImpl;
import org.utility.exception.BadRequestException;
import org.utility.mapper.QiniuContentMapper;
import org.utility.mapper.QiniuMapper;
import org.utility.model.QiniuConfig;
import org.utility.model.QiniuContent;
import org.utility.service.QiniuService;
import org.utility.service.dto.QiniuConfigQuery;
import org.utility.util.FileUtils;
import org.utility.util.QiNiuUtils;
import org.utility.util.QueryHelp;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 七牛云存储 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-29
 */
@Service
@CacheConfig(cacheNames = {"qiniu"})
public class QiniuServiceImpl extends BaseServiceImpl<QiniuMapper, QiniuConfigQuery, QiniuConfig> implements QiniuService {

    @Value("${qiniu.max-size}")
    private Long maxSize;

    private final QiniuMapper qiniuMapper;
    private final QiniuContentMapper qiniuContentMapper;

    public QiniuServiceImpl(QiniuMapper qiniuMapper, QiniuContentMapper qiniuContentMapper) {
        this.qiniuMapper = qiniuMapper;
        this.qiniuContentMapper = qiniuContentMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "'config'")
    @Override
    public QiniuConfig config(QiniuConfig qiniuConfig) {
        qiniuConfig.setConfigId(1L);
        String http = "http://", https = "https://";
        if (!(qiniuConfig.getHost().toLowerCase().startsWith(http) || qiniuConfig.getHost().toLowerCase().startsWith(https))) {
            throw new BadRequestException("外链域名必须以http://或者https://开头");
        }
        qiniuMapper.updateById(qiniuConfig);
        return qiniuConfig;
    }

    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "'config'")
    @Override
    public void updateByType(String type) {
        qiniuMapper.updateType(type);
    }

    @Cacheable(key = "'config'")
    @Override
    public QiniuConfig getConfig() {
        return qiniuMapper.selectById(1L);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public QiniuContent upload(MultipartFile file, QiniuConfig qiniuConfig) {
        FileUtils.checkSize(maxSize, file.getSize());
        if (qiniuConfig.getConfigId() == null) {
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtils.getRegion(qiniuConfig.getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        try {
            String key = file.getOriginalFilename();
            if (qiniuContentMapper.selectByKey(key) != null) {
                key = QiNiuUtils.getKey(key);
            }
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //解析上传成功的结果

            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            QiniuContent content = qiniuContentMapper.selectByKey(FileUtils.getFileNameNoEx(putRet.key));
            if (content == null) {
                //存入数据库
                QiniuContent qiniuContent = new QiniuContent();
                qiniuContent.setSuffix(FileUtils.getExtensionName(putRet.key));
                qiniuContent.setBucket(qiniuConfig.getBucket());
                qiniuContent.setType(qiniuConfig.getType());
                qiniuContent.setKey(FileUtils.getFileNameNoEx(putRet.key));
                qiniuContent.setUrl(qiniuConfig.getHost() + "/" + putRet.key);
                qiniuContent.setSize(FileUtils.getSize(Integer.parseInt(file.getSize() + "")));
                qiniuContentMapper.insert(qiniuContent);
                return qiniuContent;
            }
            return content;
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void removeById(QiniuContent content, QiniuConfig config) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtils.getRegion(config.getZone()));
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(content.getBucket(), content.getKey() + "." + content.getSuffix());
            qiniuContentMapper.deleteById(content);
        } catch (QiniuException ex) {
            qiniuContentMapper.deleteById(content);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByIds(Long[] ids, QiniuConfig config) {
        for (Long id : ids) {
            this.removeById(this.getByContentId(id), config);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void synchronize(QiniuConfig config) {
        if (config.getConfigId() == null) {
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtils.getRegion(config.getZone()));
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(config.getBucket(),
                prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            QiniuContent qiniuContent;
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                if (qiniuContentMapper.selectByKey(FileUtils.getFileNameNoEx(item.key)) == null) {
                    qiniuContent = new QiniuContent();
                    qiniuContent.setSize(FileUtils.getSize(Integer.parseInt(item.fsize + "")));
                    qiniuContent.setSuffix(FileUtils.getExtensionName(item.key));
                    qiniuContent.setKey(FileUtils.getFileNameNoEx(item.key));
                    qiniuContent.setType(config.getType());
                    qiniuContent.setBucket(config.getBucket());
                    qiniuContent.setUrl(config.getHost() + "/" + item.key);
                    qiniuContentMapper.insert(qiniuContent);
                }
            }
        }
    }

    @Override
    public List<QiniuContent> listContent(QiniuConfigQuery query) {
        return qiniuContentMapper.selectList(QueryHelp.queryWrapper(query));
    }

    @Override
    public QiniuContent getByContentId(Long contentId) {
        return qiniuContentMapper.selectById(contentId);
    }

    @Override
    public String download(QiniuContent content, QiniuConfig config) {
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
    public void download(HttpServletResponse response, List<QiniuContent> queryAll) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QiniuContent content : queryAll) {
            Map<String, Object> map = MapUtil.newHashMap(6, true);
            map.put("文件名", content.getKey());
            map.put("文件类型", content.getSuffix());
            map.put("空间名称", content.getBucket());
            map.put("文件大小", content.getSize());
            map.put("空间类型", content.getType());
            map.put("创建日期", content.getUpdateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
