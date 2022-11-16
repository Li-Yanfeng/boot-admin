package com.boot.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 */
@Schema(description = "代码生成配置")
@TableName(value = "code_gen_config")
public class GenConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = ValidGroup.Update.class)
    private Long configId;

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "接口名称")
    private String apiAlias;

    @Schema(description = "模块名称")
    private String moduleName;

    @Schema(description = "包路径")
    private String pack;

    @Schema(description = "后端代码生成的路径")
    private String adminPath;

    @Schema(description = "前端代码生成的路径")
    private String frontPath;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "表前缀")
    private String prefix;

    @Schema(description = "是否覆盖")
    @TableField(value = "is_cover")
    private Integer cover;


    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getApiAlias() {
        return apiAlias;
    }

    public void setApiAlias(String apiAlias) {
        this.apiAlias = apiAlias;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getAdminPath() {
        return adminPath;
    }

    public void setAdminPath(String adminPath) {
        this.adminPath = adminPath;
    }

    public String getFrontPath() {
        return frontPath;
    }

    public void setFrontPath(String frontPath) {
        this.frontPath = frontPath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getCover() {
        return cover;
    }

    public void setCover(Integer cover) {
        this.cover = cover;
    }

    public GenConfig() {
    }

    public GenConfig(String tableName) {
        this.tableName = tableName;
    }
}
