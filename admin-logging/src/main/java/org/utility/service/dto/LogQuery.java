package org.utility.service.dto;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.utility.annotation.Query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Li Yanfeng
 */
@ApiModel(description = "日志 数据查询对象")
public class LogQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志类型")
    @Query(type = SqlKeyword.EQ)
    private String logType;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = SqlKeyword.BETWEEN)
    private List<Date> createTime;

    @ApiModelProperty(value = "多字段模糊")
    @Query(blurry = "username,description,address,requestIp,method,params")
    private String blurry;


    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public List<Date> getCreateTime() {
        return createTime;
    }

    public void setCreateTime(List<Date> createTime) {
        this.createTime = createTime;
    }

    public String getBlurry() {
        return blurry;
    }

    public void setBlurry(String blurry) {
        this.blurry = blurry;
    }
}
