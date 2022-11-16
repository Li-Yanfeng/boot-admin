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
 * @date 2021-06-01
 */
@Schema(description = "应用 数据查询对象")
public class AppQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "应用名称")
    @Query(type = SqlKeyword.LIKE)
    private String name;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
    private List<LocalDateTime> createTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LocalDateTime> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<LocalDateTime> createTime) {
        this.createTime = createTime;
    }
}
