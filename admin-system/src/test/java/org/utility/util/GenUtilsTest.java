package org.utility.util;

import cn.hutool.core.map.MapUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.utility.model.ColumnInfo;
import org.utility.model.GenConfig;
import org.utility.service.GeneratorService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author Li Yanfeng
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GenUtilsTest {

    @Resource
    private GeneratorService generatorService;

    @Test
    public void testGeneratorCode() throws IOException {
        Map<String, String> tableInfo = MapUtil.newHashMap(16, false);
//        tableInfo.put("sys_dept", "部门");
//        tableInfo.put("sys_dict", "数据字典");
//        tableInfo.put("sys_dict_detail", "数据字典详情");
//        tableInfo.put("sys_job", "岗位");
//        tableInfo.put("sys_menu", "菜单");
//        tableInfo.put("sys_role", "角色");
//        tableInfo.put("sys_role_dept", "角色部门关联");
//        tableInfo.put("sys_role_menu", "角色菜单关联");
//        tableInfo.put("sys_user", "用户");
//        tableInfo.put("sys_user_job", "用户岗位关联");
//        tableInfo.put("sys_user_role", "用户角色关联");

//        tableInfo.put("sys_quartz_job", "定时任务");
//        tableInfo.put("sys_quartz_log", "定时任务日志");

//        tableInfo.put("tool_alipay_config", "支付宝配置");
//        tableInfo.put("tool_email_config", "邮件配置");
//        tableInfo.put("tool_local_storage", "本地存储");
//        tableInfo.put("tool_qiniu_config", "七牛云存储");
//        tableInfo.put("tool_qiniu_content", "七牛云文件");

//        tableInfo.put("mnt_app", "应用管理");
//        tableInfo.put("mnt_database", "数据库管理");
//        tableInfo.put("mnt_deploy", "部署管理");
//        tableInfo.put("mnt_deploy_history", "部署历史管理");
//        tableInfo.put("mnt_deploy_server", "应用与服务器关联");
//        tableInfo.put("mnt_server", "服务器管理");


        for (Map.Entry<String, String> entry : tableInfo.entrySet()) {
            GenConfig genConfig = new GenConfig();
            // 表名
            genConfig.setTableName(entry.getKey());
            // 接口名称
            genConfig.setApiAlias(entry.getValue());
            // 包路径
            genConfig.setPack("org.utility.modules");
            // 模块名
            genConfig.setModuleName("mnt");
            // 前端代码生成的路径
            genConfig.setPath("D:\\admin\\");
            // 前端Api文件路径
            genConfig.setApiPath("D:\\front\\");
            // 作者
            genConfig.setAuthor("Li Yanfeng");
            // 表前缀
            genConfig.setPrefix("mnt_");
            // 是否覆盖
            genConfig.setCover(true);

            List<ColumnInfo> columnInfos = generatorService.listColumns(entry.getKey());
            GenUtils.generatorCode(columnInfos, genConfig);
        }
    }
}
