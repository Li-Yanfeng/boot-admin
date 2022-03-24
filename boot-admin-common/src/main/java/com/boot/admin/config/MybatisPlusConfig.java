package com.boot.admin.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.boot.admin.aspect.DataScopePermissionHandler;
import com.boot.admin.constant.PackagePattern;
import com.boot.admin.constant.SystemConstant;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MybatisPlus 配置
 *
 * @author Li Yanfeng
 */
@Configuration
@MapperScan(basePackages = {PackagePattern.MAPPER_PATH_STAR})
public class MybatisPlusConfig {

    private final Environment environment;
    private final DataSource dataSource;

    @Value(value = "${mybatis-plus.illegal-sql-inner-interceptor.enabled}")
    private Boolean enableIllegalSqlInnerInterceptor;

    public MybatisPlusConfig(Environment environment, DataSource dataSource) {
        this.environment = environment;
        this.dataSource = dataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        // 配置多数据源
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*Mapper.xml"));

        MybatisConfiguration configuration = new MybatisConfiguration();
        // 是否开启自动驼峰命名规则映射,从数据库列名 A_COLUMN 到 Java 属性名 aColumn 的类似映射
        configuration.setMapUnderscoreToCamelCase(true);
        // 二级缓存,默认：true
        configuration.setCacheEnabled(false);
        // 当查询结果中包含空值的列，则 MyBatis映射时候不会映射这个字段,默认：false
        configuration.setCallSettersOnNulls(true);
        // 配置JdbcTypeForNull, oracle数据库必须配置
        configuration.setJdbcTypeForNull(JdbcType.NULL);

        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        // 主键类型  AUTO:"数据库ID自增", NONE:"该类型为未设置主键类型", INPUT:"用户输入ID", ASSIGN_ID:"全局唯一ID (数字类型唯一ID)",
        /// dbConfig.setIdType(IdType.AUTO);
        // 字段策略 IGNORED:"忽略判断",NOT_NULL:"非 NULL 判断"),NOT_EMPTY:"非空判断"
        dbConfig.setInsertStrategy(FieldStrategy.NOT_NULL);
        dbConfig.setUpdateStrategy(FieldStrategy.NOT_NULL);
        // 数据库大写下划线转换
        dbConfig.setCapitalMode(false);
        // 逻辑删除配置
        dbConfig.setLogicDeleteValue(SystemConstant.LOGIC_DELETE);
        dbConfig.setLogicNotDeleteValue(SystemConstant.LOGIC_NOT_DELETE);

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setDbConfig(dbConfig);
        // 公共字段填充
        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
        // 关掉控制台 banner
        globalConfig.setBanner(false);


        sqlSessionFactory.setGlobalConfig(globalConfig);
        sqlSessionFactory.setConfiguration(configuration);

        // 添加插件
        sqlSessionFactory.setPlugins(mybatisPlusInterceptor());
        return sqlSessionFactory.getObject();
    }

    /**
     * MybatisPlus拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加数据权限插件
        DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor();
        dataPermissionInterceptor.setDataPermissionHandler(new DataScopePermissionHandler());
        interceptor.addInnerInterceptor(dataPermissionInterceptor);
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // Sql性能规范插件
        if (isDevEnv() && Boolean.TRUE.equals(enableIllegalSqlInnerInterceptor)) {
            interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        }
        // 防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     * 是否为开发环境
     */
    private boolean isDevEnv() {
        String[] activeProfiles = environment.getActiveProfiles();
        return CollUtil.contains(CollUtil.newArrayList(activeProfiles), com.boot.admin.constant.Environment.DEV);
    }
}
