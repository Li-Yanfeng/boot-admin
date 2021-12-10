package org.utility.service.impl;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utility.core.service.impl.ServiceImpl;
import org.utility.exception.BadRequestException;
import org.utility.mapper.QiniuConfigMapper;
import org.utility.mapper.QiniuContentMapper;
import org.utility.model.QiniuConfig;
import org.utility.model.QiniuContent;
import org.utility.service.QiniuConfigService;
import org.utility.util.FileUtils;
import org.utility.util.QiNiuUtils;

/**
 * 七牛云存储 服务实现类
 *
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Service
@CacheConfig(cacheNames = {"qiniu"})
public class QiniuConfigServiceImpl extends ServiceImpl<QiniuConfigMapper, QiniuConfig> implements QiniuConfigService {

    private final QiniuContentMapper qiniuContentMapper;

    public QiniuConfigServiceImpl(QiniuContentMapper qiniuContentMapper) {
        this.qiniuContentMapper = qiniuContentMapper;
    }

    @CachePut(key = "'config'")
    @Override
    public void updateQiniuConfig(QiniuConfig resource) {
        resource.setConfigId(1L);
        String http = "http://", https = "https://";
        if (!(resource.getHost().toLowerCase().startsWith(http) || resource.getHost().toLowerCase().startsWith(https))) {
            throw new BadRequestException("外链域名必须以http://或者https://开头");
        }
        baseMapper.updateById(resource);
    }

    @CachePut(key = "'config'")
    @Override
    public void updateQiniuConfigByType(String type) {
        baseMapper.updateType(type);
    }

    @Cacheable(key = "'config'")
    @Override
    public QiniuConfig getQiniuConfig() {
        return baseMapper.selectById(1L);
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
}
