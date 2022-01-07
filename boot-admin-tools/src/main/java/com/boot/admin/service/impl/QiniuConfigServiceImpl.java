package com.boot.admin.service.impl;

import com.boot.admin.constant.CacheKey;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.core.service.impl.ServiceImpl;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.mapper.QiniuConfigMapper;
import com.boot.admin.mapper.QiniuContentMapper;
import com.boot.admin.model.QiniuConfig;
import com.boot.admin.model.QiniuContent;
import com.boot.admin.service.QiniuConfigService;
import com.boot.admin.util.FileUtils;
import com.boot.admin.util.QiNiuUtils;
import com.boot.admin.util.RedisUtils;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final RedisUtils redisUtils;

    public QiniuConfigServiceImpl(QiniuContentMapper qiniuContentMapper, RedisUtils redisUtils) {
        this.qiniuContentMapper = qiniuContentMapper;
        this.redisUtils = redisUtils;
    }

    @Override
    public void updateQiniuConfig(QiniuConfig resource) {
        resource.setConfigId(1L);
        if (!(resource.getHost().toLowerCase().startsWith(CommonConstant.HTTP) || resource.getHost().toLowerCase().startsWith(CommonConstant.HTTPS))) {
            throw new BadRequestException("外链域名必须以http://或者https://开头");
        }
        baseMapper.updateById(resource);
        // 更新缓存
        redisUtils.set(CacheKey.QINIU_CONFIG, resource);
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
