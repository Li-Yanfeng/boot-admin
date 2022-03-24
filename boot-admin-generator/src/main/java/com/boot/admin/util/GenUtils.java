package com.boot.admin.util;

import cn.hutool.extra.template.*;
import com.boot.admin.constant.CommonConstant;
import com.boot.admin.model.ColumnConfig;
import com.boot.admin.model.GenConfig;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成工具类
 *
 * @author Li Yanfeng
 */
public class GenUtils {


    private static final String TIMESTAMP = "Timestamp";
    private static final String BIGDECIMAL = "BigDecimal";
    public static final String PK = "PRI";
    public static final String EXTRA = "auto_increment";


    /**
     * 模板所在路径
     */
    private static final String TEMPLATE_PATH = "templates";
    /**
     * 后端模板路径
     */
    private static final String ADMIN_TEMPLATE_PATH = "generator/admin/";
    /**
     * 前端模板路径
     */
    private static final String FRONT_TEMPLATE_PATH = "generator/front/";
    /**
     * 模板后缀
     */
    private static final String TEMPLATE_SUFFIX = ".ftl";

    /**
     * 获取后端代码模板名称
     *
     * @return List
     */
    private static List<String> getAdminTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("entity");
        templateNames.add("dto");
        templateNames.add("query");
        templateNames.add("mapper");
        templateNames.add("mapper.xml");
        templateNames.add("service");
        templateNames.add("serviceImpl");
        templateNames.add("controller");
        templateNames.add("sql");
        return templateNames;
    }

    /**
     * 获取前端代码模板名称
     *
     * @return List
     */
    private static List<String> getFrontTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("api");
        templateNames.add("index");
        return templateNames;
    }

    /**
     * 预览
     *
     * @param columns   列数据信息
     * @param genConfig 代码生成配置
     * @return /
     */
    public static List<Map<String, Object>> preview(List<ColumnConfig> columns, GenConfig genConfig) {
        Map<String, Object> genMap = getGenMap(columns, genConfig);
        List<Map<String, Object>> genList = new ArrayList<>();
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(TEMPLATE_PATH,
            TemplateConfig.ResourceMode.CLASSPATH));
        // 获取后端模版
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {
            Map<String, Object> map = new HashMap<>(1);
            Template template = engine.getTemplate(ADMIN_TEMPLATE_PATH + templateName + TEMPLATE_SUFFIX);
            map.put("content", template.render(genMap));
            map.put("name", templateName);
            genList.add(map);
        }
        // 获取前端模版
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Map<String, Object> map = new HashMap<>(1);
            Template template = engine.getTemplate(FRONT_TEMPLATE_PATH + templateName + TEMPLATE_SUFFIX);
            map.put(templateName, template.render(genMap));
            map.put("content", template.render(genMap));
            map.put("name", templateName);
            genList.add(map);
        }
        return genList;
    }

    /**
     * 下载
     *
     * @param columns   列数据信息
     * @param genConfig 代码生成配置
     * @return /
     * @throws IOException /
     */
    public static String download(List<ColumnConfig> columns, GenConfig genConfig) throws IOException {
        // 拼接的路径：/tmpadmin-gen-temp/，这个路径在Linux下需要root用户才有权限创建,非root用户会权限错误而失败，更改为： /tmp/admin-gen-temp/
        String tempPath =
            FileUtils.SYS_TEM_DIR + "admin-gen-temp" + File.separator + genConfig.getTableName() + File.separator;
        Map<String, Object> genMap = getGenMap(columns, genConfig);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(TEMPLATE_PATH,
            TemplateConfig.ResourceMode.CLASSPATH));
        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate(ADMIN_TEMPLATE_PATH + templateName + TEMPLATE_SUFFIX);
            String filePath = getAdminFilePath(templateName, genConfig, genMap.get("className").toString());

            assert filePath != null;
            File file = new File(filePath);
            // 如果非覆盖生成
            if (CommonConstant.NO.equals(genConfig.getCover()) && FileUtils.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
        // 生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate(FRONT_TEMPLATE_PATH + templateName + ADMIN_TEMPLATE_PATH);
            String filePath = getFrontFilePath(templateName, genConfig, genMap.get("changeClassName").toString());

            assert filePath != null;
            File file = new File(filePath);
            // 如果非覆盖生成
            if (CommonConstant.YES.equals(genConfig.getCover()) && FileUtils.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
        return tempPath;
    }

    /**
     * 生成代码
     *
     * @param columns   列数据信息
     * @param genConfig 代码生成配置
     * @throws IOException /
     */
    public static void generatorCode(List<ColumnConfig> columns, GenConfig genConfig) throws IOException {
        Map<String, Object> genMap = getGenMap(columns, genConfig);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(TEMPLATE_PATH,
            TemplateConfig.ResourceMode.CLASSPATH));
        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate(ADMIN_TEMPLATE_PATH + templateName + TEMPLATE_SUFFIX);
            String filePath = getAdminFilePath(templateName, genConfig, genMap.get("className").toString());

            assert filePath != null;
            File file = new File(filePath);
            // 如果非覆盖生成
            if (CommonConstant.NO.equals(genConfig.getCover()) && FileUtils.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
        // 生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate(FRONT_TEMPLATE_PATH + templateName + TEMPLATE_SUFFIX);
            String filePath = getFrontFilePath(templateName, genConfig, genMap.get("changeClassName").toString());

            assert filePath != null;
            File file = new File(filePath);
            // 如果非覆盖生成
            if (CommonConstant.NO.equals(genConfig.getCover()) && FileUtils.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
    }

    /**
     * 获取模版数据
     *
     * @param columnInfos 列数据信息
     * @param genConfig   代码生成配置
     * @return /
     */
    private static Map<String, Object> getGenMap(List<ColumnConfig> columnInfos, GenConfig genConfig) {
        // 存储模版字段数据
        Map<String, Object> genMap = new HashMap<>(16);
        // 接口别名
        genMap.put("apiAlias", genConfig.getApiAlias());
        // 包名称
        genMap.put("package", genConfig.getPack());
        // 模块名称
        genMap.put("moduleName", genConfig.getModuleName());
        // 作者
        genMap.put("author", genConfig.getAuthor());
        // 创建日期
        genMap.put("date", LocalDate.now().toString());
        // 表名
        genMap.put("tableName", genConfig.getTableName());
        // 大写开头的类名
        String className = StringUtils.toCapitalizeCamelCase(genConfig.getTableName());
        // 小写开头的类名
        String changeClassName = StringUtils.toCamelCase(genConfig.getTableName());
        // 判断是否去除表前缀
        if (StringUtils.isNotEmpty(genConfig.getPrefix())) {
            className = StringUtils.toCapitalizeCamelCase(StringUtils.removePrefix(genConfig.getTableName(),
                genConfig.getPrefix()));
            changeClassName = StringUtils.toCamelCase(StringUtils.removePrefix(genConfig.getTableName(),
                genConfig.getPrefix()));
        }
        // 保存类名
        genMap.put("className", className);
        // 保存小写开头的类名
        genMap.put("changeClassName", changeClassName);
        // 保存缓存Key
        genMap.put("cacheKey", StringUtils.toUnderlineCase(className).toUpperCase());
        // 继承实体父类
        genMap.put("extendSuperEntity", false);
        // 继承逻辑删除实体父类
        genMap.put("extendLogicDeleteSuperEntity", false);
        // 存在 Timestamp 字段
        genMap.put("hasTimestamp", false);
        // 查询类中存在 Timestamp 字段
        genMap.put("queryHasTimestamp", false);
        // 存在 BigDecimal 字段
        genMap.put("hasBigDecimal", false);
        // 查询类中存在 BigDecimal 字段
        genMap.put("queryHasBigDecimal", false);
        // 是否需要创建查询
        genMap.put("hasQuery", true);
        // 自增主键
        genMap.put("auto", false);
        // 存在字典
        genMap.put("hasDict", false);
        // 保存字段信息
        List<Map<String, Object>> columns = new ArrayList<>();
        // 保存公共字段信息
        List<Map<String, Object>> commonColumns = new ArrayList<>();
        // 保存逻辑删除信息
        List<Map<String, Object>> logicDeleteColumns = new ArrayList<>();
        // 保存查询字段的信息
        List<Map<String, Object>> queryColumns = new ArrayList<>();
        // 存储字典信息
        List<String> dicts = new ArrayList<>();
        // 存储 between 信息
        List<Map<String, Object>> betweens = new ArrayList<>();
        // 存储不为空的字段信息
        List<Map<String, Object>> isNotNullColumns = new ArrayList<>();

        for (ColumnConfig column : columnInfos) {
            Map<String, Object> listMap = new HashMap<>(16);
            // 字段描述
            listMap.put("remark", column.getRemark());
            // 字段类型
            listMap.put("columnKey", column.getKeyType());
            // 主键类型
            String colType = ColUtils.cloToJava(column.getColumnType());
            // 小写开头的字段名
            String changeColumnName = StringUtils.toCamelCase(removeIsPrefix(column.getColumnName()));
            // 大写开头的字段名
            String capitalColumnName = StringUtils.toCapitalizeCamelCase(removeIsPrefix(column.getColumnName()));
            if (PK.equals(column.getKeyType())) {
                // 存储主键类型
                genMap.put("pkColumnType", colType);
                // 存储小写开头的字段名
                genMap.put("pkChangeColName", changeColumnName);
                // 存储大写开头的字段名
                genMap.put("pkCapitalColName", capitalColumnName);
            }
            // 是否存在 Timestamp 类型的字段
            if (TIMESTAMP.equals(colType)) {
                genMap.put("hasTimestamp", true);
            }
            // 是否存在 BigDecimal 类型的字段
            if (BIGDECIMAL.equals(colType)) {
                genMap.put("hasBigDecimal", true);
            }
            // 主键是否自增
            if (EXTRA.equals(column.getExtra())) {
                genMap.put("auto", true);
            }
            // 主键存在字典
            if (StringUtils.isNotBlank(column.getDictName())) {
                genMap.put("hasDict", true);
                dicts.add(column.getDictName());
            }

            // 存储字段类型
            listMap.put("columnType", colType);
            // 存储字原始段名称
            listMap.put("columnName", column.getColumnName());
            // 不为空
            listMap.put("isNotNull", CommonConstant.YES.equals(column.getNotNull()));
            // 字段列表显示
            listMap.put("columnShow", column.getListShow());
            // 表单显示
            listMap.put("formShow", column.getFormShow());
            // 表单组件类型
            listMap.put("formType", StringUtils.isNotBlank(column.getFormType()) ? column.getFormType() : "Input");
            // 小写开头的字段名称
            listMap.put("changeColumnName", changeColumnName);
            //大写开头的字段名称
            listMap.put("capitalColumnName", capitalColumnName);
            // 字典名称
            listMap.put("dictName", column.getDictName());
            // 添加非空字段信息
            if (CommonConstant.YES.equals(column.getNotNull())) {
                isNotNullColumns.add(listMap);
            }
            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if (!StringUtils.isBlank(column.getQueryType())) {
                // 查询类型
                listMap.put("queryType", column.getQueryType());
                // 是否存在查询
                genMap.put("hasQuery", true);
                if (TIMESTAMP.equals(colType)) {
                    // 查询中存储 Timestamp 类型
                    genMap.put("queryHasTimestamp", true);
                }
                if (BIGDECIMAL.equals(colType)) {
                    // 查询中存储 BigDecimal 类型
                    genMap.put("queryHasBigDecimal", true);
                }
                if ("between".equalsIgnoreCase(column.getQueryType())) {
                    betweens.add(listMap);
                } else {
                    // 添加到查询列表中
                    queryColumns.add(listMap);
                }
            }
            // 添加到通用字段列表中
            if (changeColumnName.startsWith("create") || changeColumnName.startsWith("update")) {
                commonColumns.add(listMap);
            } else if ("deleted".equals(changeColumnName)) {
                // 添加到逻辑删除列表中
                logicDeleteColumns.add(listMap);
            } else {
                columns.add(listMap);
            }
        }
        // 公共字段少于4个，不继承实体父类
        if (commonColumns.size() < 6) {
            columns.addAll(commonColumns);
        } else {
            genMap.put("extendSuperEntity", true);
            // 如果存在逻辑删除注解
            if (logicDeleteColumns.size() > 0) {
                genMap.put("extendLogicDeleteSuperEntity", true);
            } else {
                columns.addAll(logicDeleteColumns);
            }
        }
        // 保存字段列表
        genMap.put("columns", columns);
        // 保存公共字段列表
        genMap.put("commonColumns", commonColumns);
        // 保存查询列表
        genMap.put("queryColumns", queryColumns);
        // 保存字段列表
        genMap.put("dicts", dicts);
        // 保存查询列表
        genMap.put("betweens", betweens);
        // 保存非空字段信息
        genMap.put("isNotNullColumns", isNotNullColumns);
        return genMap;
    }

    /**
     * 定义后端文件路径以及名称
     */
    private static String getAdminFilePath(String templateName, GenConfig genConfig, String className) {
        String projectPath =
            new File(genConfig.getAdminPath()).getParent() + File.separator + genConfig.getModuleName();
        String packagePath =
            projectPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String resourcePath = projectPath + File.separator + "src" + File.separator + "main" + File.separator +
            "resources" + File.separator;
        if (!ObjectUtils.isEmpty(genConfig.getPack())) {
            packagePath += genConfig.getPack().replace(".", File.separator) + File.separator;
        }

        switch (templateName) {
            case "entity":
                return packagePath + "model" + File.separator + className + ".java";

            case "dto":
                return packagePath + "service" + File.separator + "dto" + File.separator + className + "DTO.java";

            case "query":
                return packagePath + "service" + File.separator + "dto" + File.separator + className + "Query.java";

            case "mapper":
                return packagePath + "mapper" + File.separator + className + "Mapper.java";

            case "mapper.xml":
                return resourcePath + "mapper" + File.separator + className + "Mapper.xml";

            case "service":
                return packagePath + "service" + File.separator + className + "Service.java";

            case "serviceImpl":
                return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl" + ".java";

            case "controller":
                return packagePath + "rest" + File.separator + className + "Controller.java";

            case "sql":
                return resourcePath + "sql" + File.separator + className + "Menu.sql";

            default:
                return null;
        }
    }

    /**
     * 定义前端文件路径以及名称
     */
    private static String getFrontFilePath(String templateName, GenConfig genConfig, String apiName) {
        String projectPath =
            new File(genConfig.getFrontPath()).getParent() + File.separator + genConfig.getModuleName() + "-web" + File.separator;

        switch (templateName) {
            case "api":
                return projectPath + "src" + File.separator + "api" + File.separator + apiName + ".js";

            case "index":
                return projectPath + "src" + File.separator + "views" + File.separator + apiName + File.separator +
                    "index.vue";

            default:
                return null;
        }
    }

    /**
     * 生成文件
     *
     * @param file     文件
     * @param template 模板
     * @param map      列信息
     * @throws IOException /
     */
    private static void genFile(File file, Template template, Map<String, Object> map) throws IOException {
        // 生成目标文件
        Writer writer = null;
        try {
            FileUtils.touch(file);
            writer = new FileWriter(file);
            template.render(map, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            assert writer != null;
            writer.close();
        }
    }

    /**
     * 删除 is 前缀
     *
     * @param columnName 列名
     */
    private static String removeIsPrefix(String columnName) {
        String isPrefix = "is_";
        return columnName.startsWith(isPrefix) ? columnName.replaceFirst(isPrefix, "") : columnName;
    }
}
