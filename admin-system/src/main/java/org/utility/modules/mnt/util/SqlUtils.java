package org.utility.modules.mnt.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Sql 工具类
 *
 * @author Li Yanfeng
 */
public class SqlUtils {

    private static final Logger logger = LoggerFactory.getLogger(SqlUtils.class);

    /**
     * 获取数据源
     *
     * @param jdbcUrl  /
     * @param userName /
     * @param password /
     * @return DataSource
     */
    private static DataSource getDataSource(String jdbcUrl, String userName, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        String className;
        try {
            className = DriverManager.getDriver(jdbcUrl.trim()).getClass().getName();
        } catch (SQLException e) {
            throw new RuntimeException("Get class name error: =" + jdbcUrl);
        }
        if (StrUtil.isEmpty(className)) {
            DatabaseTypeEnum dataTypeEnum = DatabaseTypeEnum.urlOf(jdbcUrl);
            if (null == dataTypeEnum) {
                throw new RuntimeException("Not supported data type: jdbcUrl=" + jdbcUrl);
            }
            dataSource.setDriverClassName(dataTypeEnum.getDriver());
        } else {
            dataSource.setDriverClassName(className);
        }

        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        // 最小空闲连接数
        dataSource.setMinimumIdle(10);
        // 最大连接数
        dataSource.setMaximumPoolSize(20);
        // 连接在池中闲置的最长时间
        dataSource.setIdleTimeout(30000);
        // 连接池中连接的最大生存期
        dataSource.setMaxLifetime(1800000);
        // 连接超时时间
        dataSource.setConnectionTimeout(30000);
        return dataSource;
    }

    /**
     * 获取连接
     *
     * @param jdbcUrl  连接地址
     * @param userName 用户名
     * @param password 密码
     * @return /
     */
    private static Connection getConnection(String jdbcUrl, String userName, String password) {
        DataSource dataSource = getDataSource(jdbcUrl, userName, password);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (Exception ignored) {
        }
        try {
            int timeOut = 5;
            if (null == connection || connection.isClosed() || !connection.isValid(timeOut)) {
                logger.info("connection is closed or invalid, retry get connection!");
                connection = dataSource.getConnection();
            }
        } catch (Exception e) {
            logger.error("create connection error, jdbcUrl: {}", jdbcUrl);
            throw new RuntimeException("create connection error, jdbcUrl: " + jdbcUrl);
        } finally {
            IoUtil.close(connection);
        }
        return connection;
    }

    /**
     * 发布连接
     */
    private static void releaseConnection(Connection connection) {
        if (null != connection) {
            try {
                connection.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                logger.error("connection close error：" + e.getMessage());
            }
        }
    }

    /**
     * 测试连接
     *
     * @param jdbcUrl  连接地址
     * @param userName 用户名
     * @param password 密码
     * @return /
     */
    public static boolean testConnection(String jdbcUrl, String userName, String password) {
        Connection connection = null;
        try {
            connection = getConnection(jdbcUrl, userName, password);
            if (null != connection) {
                return true;
            }
        } catch (Exception e) {
            logger.info("Get connection failed:" + e.getMessage());
        } finally {
            releaseConnection(connection);
        }
        return false;
    }

    /**
     * 执行文件
     *
     * @param jdbcUrl  连接地址
     * @param userName 用户名
     * @param password 密码
     * @param sqlFile  sql 文件
     * @return /
     */
    public static String executeFile(String jdbcUrl, String userName, String password, File sqlFile) {
        Connection connection = getConnection(jdbcUrl, userName, password);
        try {
            batchExecute(connection, readSqlList(sqlFile));
        } catch (Exception e) {
            logger.error("sql脚本执行发生异常:{}", e.getMessage());
            return e.getMessage();
        } finally {
            releaseConnection(connection);
        }
        return "success";
    }


    /**
     * 批量执行sql
     *
     * @param connection /
     * @param sqlList    /
     */
    public static void batchExecute(Connection connection, List<String> sqlList) {
        Statement st = null;
        try {
            st = connection.createStatement();
            for (String sql : sqlList) {
                if (sql.endsWith(";")) {
                    sql = sql.substring(0, sql.length() - 1);
                }
                st.addBatch(sql);
            }
            st.executeBatch();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            IoUtil.close(st);
        }
    }

    /**
     * 将文件中的sql语句以；为单位读取到列表中
     *
     * @param sqlFile sql文件
     * @return /
     * @throws Exception e
     */
    private static List<String> readSqlList(File sqlFile) throws Exception {
        List<String> sqlList = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(sqlFile), StandardCharsets.UTF_8))) {
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                logger.info("line:{}", tmp);
                if (tmp.endsWith(";")) {
                    sb.append(tmp);
                    sqlList.add(sb.toString());
                    sb.delete(0, sb.length());
                } else {
                    sb.append(tmp);
                }
            }
            if (!"".endsWith(sb.toString().trim())) {
                sqlList.add(sb.toString());
            }
        }

        return sqlList;
    }
}
