package org.utility.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 代码生成配置
 *
 * @author Li Yanfeng
 */
@TableName(value = "code_gen_config")
public class GenConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID", hidden = true)
    @TableId(value = "config_id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "表名")
    @NotBlank
    private String tableName;

    @ApiModelProperty(value = "接口名称")
    private String apiAlias;

    @ApiModelProperty(value = "模块名")
    @NotBlank
    private String moduleName;

    @ApiModelProperty(value = "包路径")
    @NotBlank
    private String pack;

    @ApiModelProperty(value = "后端代码生成的路径")
    @NotBlank
    private String adminPath;

    @ApiModelProperty(value = "前端代码生成的路径")
    @NotBlank
    private String frontPath;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "表前缀")
    private String prefix;

    @ApiModelProperty(value = "是否覆盖")
    private Boolean cover = false;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getCover() {
        return cover;
    }

    public void setCover(Boolean cover) {
        this.cover = cover;
    }

    public GenConfig() {
    }

    public GenConfig(String tableName) {
        this.tableName = tableName;
    }
}

