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
@Schema(description = "服务器 数据查询对象")
public class ServerQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
    private List<LocalDateTime> createTime;

    @Schema(description = "多字段模糊")
    @Query(q = "name,ip")
    private String q;


    public List<LocalDateTime> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<LocalDateTime> createTime) {
        this.createTime = createTime;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
