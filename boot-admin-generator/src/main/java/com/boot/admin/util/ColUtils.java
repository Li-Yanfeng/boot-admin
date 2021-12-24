package com.boot.admin.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sql 字段转 Java
 *
 * @author Li Yanfeng
 */
public class ColUtils {

    private static final Logger logger = LoggerFactory.getLogger(ColUtils.class);

    /**
     * 转换 mysql 数据类型为 java 数据类型
     *
     * @param type 数据库字段类型
     * @return String
     */
    static String cloToJava(String type) {
        Configuration config = getConfig();
        assert config != null;
        return config.getString(type, "unknowType");
    }

    /**
     * 获取配置信息
     */
    public static PropertiesConfiguration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
