package com.boot.admin.config;

import cn.hutool.log.StaticLog;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis 配置
 *
 * @author Li Yanfeng
 */
@Configuration
@EnableCaching
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisCacheConfig extends CachingConfigurerSupport {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);

    private final LettuceConnectionFactory lettuceConnectionFactory;

    public RedisCacheConfig(LettuceConnectionFactory lettuceConnectionFactory) {
        this.lettuceConnectionFactory = lettuceConnectionFactory;
    }

    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 配置 redisTemplate
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        initRedisTemplate(redisTemplate, lettuceConnectionFactory, true);
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(
        RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(true);
        return template;
    }

    /**
     * 自定义缓存key生成策略，默认将使用该策略
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        // 设置自动key的生成规则，配置spring boot的注解，进行方法级别的缓存，
        // 使用：进行分割，可以很多显示出层级关系
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(":");
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(":" + obj);
            }
            String rsToUse = String.valueOf(sb);
            StaticLog.info("auto generate Redis Key -> [{}]", rsToUse);
            return rsToUse;
        };
    }

    /**
     * 缓存管理
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        // 初始化缓存管理器
        logger.info("init -> [{}]", "CacheManager RedisCacheManager Start");
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(lettuceConnectionFactory);

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            // 设置缓存过期时间
            .entryTtl(Duration.ofHours(2))
            // 不缓存空值
            .disableCachingNullValues();
        return builder.cacheDefaults(cacheConfiguration).build();
    }

    /**
     * 错误处理器
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        // 异常处理，当Redis发生异常时，打印日志，但是程序正常走
        logger.info("init -> [{}]", "Redis CacheErrorHandler");
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                logger.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                logger.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                logger.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                logger.error("Redis occur handleCacheClearError：", e);
            }
        };
    }

    private void initRedisTemplate(RedisTemplate<Object, Object> redisTemplate,
                                   LettuceConnectionFactory lettuceConnectionFactory,
                                   boolean isEnableTransactionSupport) {
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
            new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 将类名称序列化到json串中，去掉会导致得出来的的是LinkedHashMap对象，直接转换实体对象会失败
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // value序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.setEnableTransactionSupport(isEnableTransactionSupport);
        redisTemplate.afterPropertiesSet();
    }
}
