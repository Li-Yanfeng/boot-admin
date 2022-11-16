package com.boot.admin.mnt.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.boot.admin.annotation.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Li Yanfeng
 * @since 2021-06-01
 */
@Schema(description = "部署 数据查询对象")
public class DeployQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "应用程序名称")
    @Query(type = SqlKeyword.EQ)
    private String appName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
    private List<LocalDateTime> createTime;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<LocalDateTime> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<LocalDateTime> createTime) {
        this.createTime = createTime;
    }
}
