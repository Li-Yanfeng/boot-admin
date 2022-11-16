package com.boot.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 */
@Schema(description = "表的数据信息")
public class TableInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "表名称")
    private Object tableName;

    @Schema(description = "创建日期")
    private Object createTime;

    @Schema(description = "数据库引擎")
    private Object engine;

    @Schema(description = "编码集")
    private Object coding;

    @Schema(description = "备注")
    private Object remark;


    public Object getTableName() {
        return tableName;
    }

    public void setTableName(Object tableName) {
        this.tableName = tableName;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getEngine() {
        return engine;
    }

    public void setEngine(Object engine) {
        this.engine = engine;
    }

    public Object getCoding() {
        return coding;
    }

    public void setCoding(Object coding) {
        this.coding = coding;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public TableInfo() {
    }

    public TableInfo(Object tableName, Object createTime, Object engine, Object coding, Object remark) {
        this.tableName = tableName;
        this.createTime = createTime;
        this.engine = engine;
        this.coding = coding;
        this.remark = remark;
    }
}
