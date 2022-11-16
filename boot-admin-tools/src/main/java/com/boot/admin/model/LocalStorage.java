package com.boot.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.core.model.BaseEntityLogicDelete;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "本地存储")
@TableName(value = "tool_local_storage")
public class LocalStorage extends BaseEntityLogicDelete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = ValidGroup.Update.class)
    private Long storageId;

    @Schema(description = "文件真实名称")
    private String realName;

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件后缀")
    private String suffix;

    @Schema(description = "文件类型")
    private String type;

    @Schema(description = "文件大小")
    private String size;

    @Schema(description = "访问路径")
    private String path;

    @Schema(description = "压缩后访问路径")
    private String compressPath;


    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public LocalStorage() {
    }

    public LocalStorage(String realName, String name, String suffix, String type, String size, String path) {
        this.realName = realName;
        this.name = name;
        this.suffix = suffix;
        this.type = type;
        this.size = size;
        this.path = path;
    }
}
