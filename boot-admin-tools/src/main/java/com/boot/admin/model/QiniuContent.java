package com.boot.admin.model;

import com.baomidou.mybatisplus.annotation.*;
import com.boot.admin.annotation.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Li Yanfeng
 * @date 2021-06-01
 */
@Schema(description = "七牛云文件")
@TableName(value = "tool_qiniu_content")
public class QiniuContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    @NotNull(groups = ValidGroup.Update.class)
    private Long contentId;

    @Schema(description = "Bucket 识别符")
    private String bucket;

    @Schema(description = "空间类型：私有或公开")
    private String spaceType;

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件后缀")
    private String suffix;

    @Schema(description = "文件类型")
    private String type;

    @Schema(description = "文件大小")
    private String size;

    @Schema(description = "访问地址")
    private String url;

    @Schema(description = "压缩后访问地址")
    private String compressUrl;

    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    protected Long createBy;

    @Schema(description = "创建人名称")
    @TableField(fill = FieldFill.INSERT)
    protected String createByName;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @Schema(description = "是否删除")
    @TableField(value = "is_deleted")
    @TableLogic
    private Integer deleted;


    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(String spaceType) {
        this.spaceType = spaceType;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCompressUrl() {
        return compressUrl;
    }

    public void setCompressUrl(String compressUrl) {
        this.compressUrl = compressUrl;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
