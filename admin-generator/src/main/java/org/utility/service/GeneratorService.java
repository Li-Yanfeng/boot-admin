package org.utility.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.scheduling.annotation.Async;
import org.utility.api.Result;
import org.utility.model.ColumnInfo;
import org.utility.model.GenConfig;
import org.utility.model.vo.TableInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 代码生成 服务类
 *
 * @author Li Yanfeng
 */
public interface GeneratorService {

    /**
     * 保存数据
     *
     * @param columnInfos /
     */
    void save(List<ColumnInfo> columnInfos);


    /**
     * 同步表数据
     *
     * @param columnInfos    /
     * @param columnInfoList /
     */
    @Async
    void sync(List<ColumnInfo> columnInfos, List<ColumnInfo> columnInfoList);

    /**
     * 查询 table
     *
     * @return /
     */
    List<TableInfo> listTables();

    /**
     * 翻页查询 table
     *
     * @param page      分页参数
     * @param tableName 表名
     * @return /
     */
    IPage<TableInfo> pageTables(IPage page, String tableName);

    /**
     * 根据表名查询 表字段数据
     *
     * @param tableName 表名
     * @return /
     */
    List<ColumnInfo> listColumnInfos(String tableName);

    /**
     * 得到数据表的元数据
     *
     * @param tableName 表名
     * @return /
     */
    List<ColumnInfo> listColumns(String tableName);

    /**
     * 预览
     *
     * @param genConfig 配置信息
     * @param columns   字段信息
     * @return /
     */
    Result preview(GenConfig genConfig, List<ColumnInfo> columns);

    /**
     * 代码生成
     *
     * @param genConfig 配置信息
     * @param columns   字段信息
     */
    void generator(GenConfig genConfig, List<ColumnInfo> columns);

    /**
     * 下载
     *
     * @param genConfig 配置信息
     * @param columns   字段信息
     * @param request   /
     * @param response  /
     */
    void download(GenConfig genConfig, List<ColumnInfo> columns, HttpServletRequest request, HttpServletResponse response);
}

