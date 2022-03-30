/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50710
 Source Host           : 127.0.0.1:3306
 Source Schema         : admin-master

 Target Server Type    : MySQL
 Target Server Version : 50710
 File Encoding         : 65001

 Date: 01/06/2021 00:00:14
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for code_column_config
-- ----------------------------
DROP TABLE IF EXISTS `code_column_config`;
CREATE TABLE `code_column_config`
(
    `column_id`    bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `table_name`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
    `column_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库字段名称',
    `column_type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库字段类型',
    `key_type`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库字段键类型',
    `extra`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段额外的参数',
    `remark`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库字段描述',
    `is_not_null`  tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否必填',
    `is_list_show` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否在列表显示',
    `is_form_show` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否表单显示',
    `form_type`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单类型',
    `query_type`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '查询类型',
    `dict_name`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典名称',
    PRIMARY KEY (`column_id`) USING BTREE,
    INDEX          `idx_table_name`(`table_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成字段信息存储' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of code_column_config
-- ----------------------------

-- ----------------------------
-- Table structure for code_gen_config
-- ----------------------------
DROP TABLE IF EXISTS `code_gen_config`;
CREATE TABLE `code_gen_config`
(
    `config_id`   bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `table_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
    `api_alias`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
    `module_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块名称',
    `pack`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '包路径',
    `admin_path`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后端代码生成的路径',
    `front_path`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端代码生成的路径',
    `author`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者',
    `prefix`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表前缀',
    `is_cover`    tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否覆盖',
    PRIMARY KEY (`config_id`) USING BTREE,
    INDEX         `idx_table_name`(`table_name`(100)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成配置' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of code_gen_config
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_app
-- ----------------------------
DROP TABLE IF EXISTS `mnt_app`;
CREATE TABLE `mnt_app`
(
    `app_id`         bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用名称',
    `upload_path`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传目录',
    `deploy_path`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部署路径',
    `backup_path`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备份路径',
    `port`           smallint(6) NULL DEFAULT NULL COMMENT '应用端口',
    `start_script`   varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '启动脚本',
    `deploy_script`  varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部署脚本',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`app_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用管理' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of mnt_app
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_database
-- ----------------------------
DROP TABLE IF EXISTS `mnt_database`;
CREATE TABLE `mnt_database`
(
    `db_id`          bigint(20) NOT NULL COMMENT 'ID',
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
    `jdbc_url`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'jdbc连接',
    `user_name`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
    `pwd`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`db_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据库管理' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of mnt_database
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_deploy
-- ----------------------------
DROP TABLE IF EXISTS `mnt_deploy`;
CREATE TABLE `mnt_deploy`
(
    `deploy_id`      bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `app_id`         bigint(20) NULL DEFAULT NULL COMMENT '应用编号',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`deploy_id`) USING BTREE,
    INDEX         `fk_6sy157pseoxx4fmcqr1vnvvhy`(`app_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部署管理' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of mnt_deploy
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_deploy_history
-- ----------------------------
DROP TABLE IF EXISTS `mnt_deploy_history`;
CREATE TABLE `mnt_deploy_history`
(
    `history_id`     bigint(20) NOT NULL COMMENT 'ID',
    `deploy_id`      bigint(20) NULL DEFAULT NULL COMMENT '部署编号',
    `ip`             varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '服务器IP',
    `app_name`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用名称',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`history_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部署历史管理' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of mnt_deploy_history
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_deploy_server
-- ----------------------------
DROP TABLE IF EXISTS `mnt_deploy_server`;
CREATE TABLE `mnt_deploy_server`
(
    `deploy_id` bigint(20) NOT NULL COMMENT '部署ID',
    `server_id` bigint(20) NOT NULL COMMENT '服务ID',
    PRIMARY KEY (`deploy_id`, `server_id`) USING BTREE,
    INDEX       `fk_eaaha7jew9a02b3bk9ghols53`(`server_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '应用与服务器关联' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of mnt_deploy_server
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_server
-- ----------------------------
DROP TABLE IF EXISTS `mnt_server`;
CREATE TABLE `mnt_server`
(
    `server_id`      bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`           varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
    `ip`             varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
    `port`           smallint(6) NULL DEFAULT NULL COMMENT '端口',
    `account`        varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
    `password`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`server_id`) USING BTREE,
    INDEX         `idx_ip`(`ip`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务器管理' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of mnt_server
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`
(
    `dept_id`        bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`            bigint(20) NULL DEFAULT NULL COMMENT '上级部门',
    `ancestors`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '祖级列表',
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
    `is_enabled`     tinyint(1) UNSIGNED NOT NULL COMMENT '是否启用',
    `dept_sort`      smallint(5) UNSIGNED NULL DEFAULT 999 COMMENT '排序',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`dept_id`) USING BTREE,
    INDEX         `idx_pid`(`pid`) USING BTREE,
    INDEX         `idx_enabled`(`is_enabled`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept`
VALUES (1, 0, '0', '华南分部', 0, 999, 1, 1, 'admin', 'admin', '2019-03-25 11:04:50', '2020-06-08 12:08:56');
INSERT INTO `sys_dept`
VALUES (2, 0, '0', '华北分部', 0, 999, 1, 1, 'admin', 'admin', '2019-03-25 11:04:53', '2020-05-14 12:54:00');
INSERT INTO `sys_dept`
VALUES (3, 1, '0,1', '研发部', 0, 999, 1, 1, 'admin', 'admin', '2019-03-25 09:15:32', '2020-08-02 14:48:47');
INSERT INTO `sys_dept`
VALUES (4, 1, '0,1', '运维部', 0, 999, 1, 1, 'admin', 'admin', '2019-03-25 09:20:44', '2020-05-17 14:27:27');
INSERT INTO `sys_dept`
VALUES (5, 2, '0,2', '测试部', 0, 999, 1, 1, 'admin', 'admin', '2019-03-25 09:52:18', '2020-06-08 11:59:21');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`
(
    `dict_id`        bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
    `description`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict`
VALUES (1, 'user_status', '用户状态', 1, 1, 'admin', 'admin', '2019-03-25 11:04:50', '2020-06-08 12:08:56');
INSERT INTO `sys_dict`
VALUES (2, 'dept_status', '部门状态', 1, 1, 'admin', 'admin', '2019-03-25 11:04:51', '2020-06-08 12:08:57');
INSERT INTO `sys_dict`
VALUES (3, 'job_status', '岗位状态', 1, 1, 'admin', 'admin', '2019-03-25 11:04:52', '2020-06-08 12:08:58');

-- ----------------------------
-- Table structure for sys_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_detail`;
CREATE TABLE `sys_dict_detail`
(
    `detail_id`      bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dict_id`        bigint(20) NULL DEFAULT NULL COMMENT '字典id',
    `label`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典标签',
    `value`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典值',
    `dict_sort`      smallint(5) UNSIGNED NULL DEFAULT NULL COMMENT '排序',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`detail_id`) USING BTREE,
    INDEX         `FK5tpkputc6d9nboxojdbgnpmyb`(`dict_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典详情' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dict_detail
-- ----------------------------
INSERT INTO `sys_dict_detail`
VALUES (1, 1, '激活', 'true', 1, 1, 1, 'admin', 'admin', '2019-03-25 11:04:50', '2020-06-08 12:08:56');
INSERT INTO `sys_dict_detail`
VALUES (2, 1, '禁用', 'false', 2, 1, 1, 'admin', 'admin', '2019-03-25 11:04:51', '2020-06-08 12:08:57');
INSERT INTO `sys_dict_detail`
VALUES (3, 2, '启用', 'true', 1, 1, 1, 'admin', 'admin', '2019-03-25 11:04:52', '2020-06-08 12:08:58');
INSERT INTO `sys_dict_detail`
VALUES (4, 2, '停用', 'false', 2, 1, 1, 'admin', 'admin', '2019-03-25 11:04:53', '2020-06-08 12:08:59');
INSERT INTO `sys_dict_detail`
VALUES (5, 3, '启用', 'true', 1, 1, 1, 'admin', 'admin', '2019-03-25 11:04:54', '2020-06-08 12:09:00');
INSERT INTO `sys_dict_detail`
VALUES (6, 3, '停用', 'false', 2, 1, 1, 'admin', 'admin', '2019-03-25 11:04:55', '2020-06-08 12:09:01');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`
(
    `job_id`         bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
    `is_enabled`     tinyint(1) UNSIGNED NOT NULL COMMENT '是否启用',
    `job_sort`       smallint(5) UNSIGNED NULL DEFAULT NULL COMMENT '排序号',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`job_id`) USING BTREE,
    UNIQUE INDEX `uk_name`(`name`) USING BTREE,
    INDEX         `idx_enabled`(`is_enabled`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '岗位' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job`
VALUES (1, '全栈开发', 0, NULL, 1, 1, 'admin', 'admin', '2019-03-25 11:04:50', '2020-06-08 12:08:50');
INSERT INTO `sys_job`
VALUES (2, '产品经理', 0, NULL, 1, 1, 'admin', 'admin', '2019-03-25 11:04:51', '2020-06-08 12:08:51');
INSERT INTO `sys_job`
VALUES (3, '软件测试', 0, NULL, 1, 1, 'admin', 'admin', '2019-03-25 11:04:52', '2020-06-08 12:08:52');
INSERT INTO `sys_job`
VALUES (4, '人事专员', 0, NULL, 1, 1, 'admin', 'admin', '2019-03-25 11:04:53', '2020-06-08 12:08:53');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`
(
    `log_id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `method`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名',
    `params`           text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '参数',
    `log_type`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志类型',
    `request_ip`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求Ip',
    `time`             bigint(20) NULL DEFAULT NULL COMMENT '请求耗时',
    `address`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
    `browser`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '浏览器',
    `description`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
    `exception_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常详细',
    `create_by`        bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `create_by_name`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `create_time`      datetime NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`log_id`) USING BTREE,
    INDEX              `idx_create_time`(`create_time`) USING BTREE,
    INDEX              `idx_log_type`(`log_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `menu_id`        bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`            bigint(20) NULL DEFAULT NULL COMMENT '上级菜单ID',
    `type`           tinyint(4) NULL DEFAULT NULL COMMENT '菜单类型',
    `title`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
    `component`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
    `component_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名称',
    `icon`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
    `permission`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限',
    `is_i_frame`     tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否外链',
    `path`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接地址',
    `is_cache`       tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '是否缓存',
    `is_hidden`      tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '是否隐藏',
    `menu_sort`      smallint(5) UNSIGNED NULL DEFAULT NULL COMMENT '排序',
    `sub_count`      tinyint(4) UNSIGNED NULL DEFAULT 0 COMMENT '子菜单数目',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`menu_id`) USING BTREE,
    UNIQUE INDEX `uk_title`(`title`) USING BTREE,
    UNIQUE INDEX `uk_name`(`title`) USING BTREE,
    INDEX            `idx_pid`(`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统菜单' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`
VALUES (1, NULL, 0, '系统管理', NULL, NULL, 'system', NULL, NULL, 'system', 0, 0, 1, 7, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (2, 1, 1, '用户管理', 'system/user/index', 'User', 'peoples', 'users:list', NULL, 'user', 0, 0, 1, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (3, 2, 2, '用户新增', '', NULL, '', 'users:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (4, 2, 2, '用户删除', '', NULL, '', 'users:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (5, 2, 2, '用户编辑', '', NULL, '', 'users:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (6, 1, 1, '角色管理', 'system/role/index', 'Role', 'role', 'roles:list', NULL, 'role', 0, 0, 2, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (7, 6, 2, '角色新增', '', NULL, '', 'roles:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (8, 6, 2, '角色删除', '', NULL, '', 'roles:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (9, 6, 2, '角色修改', '', NULL, '', 'roles:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (10, 1, 1, '菜单管理', 'system/menu/index', 'Menu', 'menu', 'menus:list', NULL, 'menu', 0, 0, 3, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (11, 10, 2, '菜单新增', '', NULL, '', 'menus:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (12, 10, 2, '菜单删除', '', NULL, '', 'menus:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (13, 10, 2, '菜单编辑', '', NULL, '', 'menus:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (14, 1, 1, '部门管理', 'system/dept/index', 'Dept', 'dept', 'depts:list', NULL, 'dept', 0, 0, 4, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (15, 14, 2, '部门新增', '', NULL, '', 'depts:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (16, 14, 2, '部门删除', '', NULL, '', 'depts:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (17, 14, 2, '部门编辑', '', NULL, '', 'depts:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (18, 1, 1, '岗位管理', 'system/job/index', 'Job', 'Steve-Jobs', 'jobs:list', NULL, 'job', 0, 0, 5, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (19, 18, 2, '岗位新增', '', NULL, '', 'jobs:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (20, 18, 2, '岗位删除', '', NULL, '', 'jobs:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (21, 18, 2, '岗位编辑', '', NULL, '', 'jobs:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (22, 1, 1, '字典管理', 'system/dict/index', 'Dict', 'dictionary', 'dicts:list', NULL, 'dict', 0, 0, 6, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (23, 22, 2, '字典新增', '', NULL, '', 'dicts:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (24, 22, 2, '字典删除', '', NULL, '', 'dicts:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (25, 22, 2, '字典编辑', '', NULL, '', 'dicts:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (26, 1, 1, '任务调度', 'system/timing/index', 'Timing', 'timing', 'timings:list', NULL, 'timing', 0, 0, 7, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (27, 26, 2, '任务新增', '', NULL, '', 'timings:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (28, 26, 2, '任务删除', '', NULL, '', 'timings:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (29, 26, 2, '任务编辑', '', NULL, '', 'timings:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (30, NULL, 0, '系统监控', NULL, NULL, 'monitor', NULL, NULL, 'monitor', 0, 0, 2, 5, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (31, 30, 1, '在线用户', 'monitor/online/index', 'OnlineUser', 'Steve-Jobs', NULL, NULL, 'online', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (32, 30, 1, '操作日志', 'monitor/log/index', 'Log', 'log', NULL, NULL, 'logs', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (33, 30, 1, '异常日志', 'monitor/log/errorLog', 'ErrorLog', 'error', NULL, NULL, 'errorLog', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (34, 30, 1, '服务监控', 'monitor/server/index', 'ServerMonitor', 'codeConsole', 'monitors:list', NULL, 'server', 0, 0, 4, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (35, NULL, 1, '运维管理', '', 'Mnt', 'mnt', NULL, NULL, 'mnt', 0, 0, 3, 5, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (36, 35, 1, '服务器', 'mnt/server/index', 'Server', 'server', 'servers:list', NULL, 'mnt/server', 0, 0, 1, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (37, 36, 2, '服务器新增', '', NULL, '', 'servers:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (38, 36, 2, '服务器删除', '', NULL, '', 'servers:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (39, 36, 2, '服务器编辑', '', NULL, '', 'servers:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (40, 35, 1, '应用管理', 'mnt/app/index', 'App', 'app', 'apps:list', NULL, 'mnt/app', 0, 0, 2, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (41, 40, 2, '应用新增', '', NULL, '', 'apps:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (42, 40, 2, '应用删除', '', NULL, '', 'apps:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (43, 40, 2, '应用编辑', '', NULL, '', 'apps:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (44, 35, 1, '部署管理', 'mnt/deploy/index', 'Deploy', 'deploy', 'deploys:list', NULL, 'mnt/deploy', 0, 0, 3, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (45, 44, 2, '部署新增', '', NULL, '', 'deploys:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (46, 44, 2, '部署删除', '', NULL, '', 'deploys:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (47, 44, 2, '部署编辑', '', NULL, '', 'deploys:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (48, 35, 1, '部署备份', 'mnt/deployHistory/index', 'DeployHistory', 'backup', 'deployHistories:list', NULL, 'mnt/deployHistory', 0, 0, 4, 1, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (49, 35, 1, '数据库管理', 'm nt/database/index', 'Database', 'database', 'databases:list', NULL, 'mnt/database', 0, 0, 5, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (50, 49, 2, '数据库新增', '', NULL, '', 'databases:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (51, 49, 2, '数据库删除', '', NULL, '', 'databases:del', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (52, 49, 2, '数据库编辑', '', NULL, '', 'databases:edit', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (53, NULL, 0, '系统工具', '', NULL, 'sys-tools', NULL, NULL, 'sys-tools', 0, 0, 4, 7, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (54, 53, 1, '接口文档', 'tools/swagger/index', 'Swagger', 'swagger', NULL, NULL, 'swagger2', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (55, 54, 1, '代码生成', 'generator/index', 'GeneratorIndex', 'dev', NULL, NULL, 'generator', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (56, 55, 2, '生成配置', 'generator/config', 'GeneratorConfig', 'dev', '', NULL, 'generator/config/s:tableName', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (57, 55, 2, '生成预览', 'generator/preview', 'Preview', 'java', NULL, NULL, 'generator/preview/s:tableName', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (58, 54, 1, '存储管理', 'tools/storage/index', 'Storage', 'qiniu', 'storages:list', NULL, 'storage', 0, 0, 3, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (59, 58, 2, '上传文件', '', NULL, '', 'storages:add', NULL, '', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (60, 58, 2, '文件编辑', '', NULL, '', 'storages:edit', NULL, '', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (61, 58, 2, '文件删除', '', NULL, '', 'storages:del', NULL, '', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (62, 54, 1, '邮件工具', 'tools/email/index', 'Email', 'email', NULL, NULL, 'email', 0, 0, 4, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (63, 62, 1, '支付宝工具', 'tools/aliPay/index', 'AliPay', 'alipay', NULL, NULL, 'aliPay', 0, 0, 5, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (64, NULL, 0, '组件管理', NULL, NULL, 'zujian', NULL, NULL, 'components', 0, 0, 5, 5, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (65, 64, 1, '图表库', 'components/Echarts', 'Echarts', 'chart', '', NULL, 'echarts', 0, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (66, 64, 1, '图标库', 'components/icons/index', 'Icons', 'icon', NULL, NULL, 'icon', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (67, 64, 1, '富文本', 'components/Editor', 'Editor', 'fwb', NULL, NULL, 'tinymce', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (68, 64, 1, 'Markdown', 'components/MarkDown', 'Markdown', 'markdown', NULL, NULL, 'markdown', 0, 0, 4, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (69, 64, 1, 'Yaml编辑器', 'components/YamlEdit', 'YamlEdit', 'dev', NULL, NULL, 'yaml', 0, 0, 5, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (70, NULL, 0, '多级菜单', '', NULL, 'menu', NULL, NULL, 'nested', 0, 0, 6, 2, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (71, 70, 1, '二级菜单1', 'nested/menu1/index', NULL, 'menu', NULL, NULL, 'menu1', 0, 0, 1, 2, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (72, 70, 1, '三级菜单1', 'nested/menu1/menu1-1', NULL, 'menu', NULL, NULL, 'menu1-1', 0, 0, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (73, 70, 1, '三级菜单2', 'nested/menu1/menu1-2', NULL, 'menu', NULL, NULL, 'menu1-2', 0, 0, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu`
VALUES (74, 70, 1, '二级菜单2', 'nested/menu2/index', NULL, 'menu', NULL, NULL, 'menu2', 0, 0, 4, 0, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_job`;
CREATE TABLE `sys_quartz_job`
(
    `job_id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `job_name`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
    `bean_name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Bean名称',
    `method_name`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名称',
    `params`              varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数',
    `cron_expression`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron 表达式',
    `is_pause`            tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否暂停',
    `sub_task`            varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子任务ID',
    `person_in_charge`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
    `email`               varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警邮箱',
    `pause_after_failure` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '任务失败后是否暂停',
    `description`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `create_by`           bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`           bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`job_id`) USING BTREE,
    INDEX                 `idx_is_pause`(`is_pause`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_quartz_job
-- ----------------------------

-- ----------------------------
-- Table structure for sys_quartz_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_log`;
CREATE TABLE `sys_quartz_log`
(
    `log_id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `job_name`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
    `bean_name`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Bean名称',
    `method_name`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名称',
    `params`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数',
    `cron_expression`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
    `is_success`       tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否成功',
    `exception_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常详情',
    `time`             bigint(20) NULL DEFAULT NULL COMMENT '执行耗时',
    `create_time`      datetime NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务日志' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_quartz_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `role_id`        bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
    `level`          tinyint(4) NULL DEFAULT NULL COMMENT '角色级别',
    `description`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
    `data_scope`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据权限',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`role_id`) USING BTREE,
    UNIQUE INDEX `uk_name`(`name`) USING BTREE,
    INDEX         `idx_role_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, '超级管理员', 1, '-', '全部', 1, 1, 'admin', 'admin', '2018-11-23 11:04:37', '2020-08-06 16:10:24');
INSERT INTO `sys_role`
VALUES (2, '普通用户', 2, '-', '自定义', 1, 1, 'admin', 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`
(
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`role_id`, `dept_id`) USING BTREE,
    INDEX     `fk_7qg6itn5ajdoa9h9o78v9ksur`(`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色部门关联' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`menu_id`, `role_id`) USING BTREE,
    INDEX     `fk_cngg2qadojhi3a651a5adkvbq`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单关联' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `user_id`        bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dept_id`        bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
    `username`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
    `nick_name`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
    `gender`         varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
    `phone`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
    `email`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
    `avatar_name`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像名称',
    `avatar_path`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像真实路径',
    `password`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
    `is_admin`       tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '是否为admin账号',
    `is_enabled`     tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否启用',
    `pwd_reset_time` datetime NULL DEFAULT NULL COMMENT '修改密码时间',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE INDEX `uk_username`(`username`) USING BTREE,
    UNIQUE INDEX `uk_email`(`email`) USING BTREE,
    INDEX            `fk_5rwmryny6jthaaxkogownknqp`(`dept_id`) USING BTREE,
    INDEX            `fk_pq2dhypk2qgt68nauh2by22jb`(`avatar_name`) USING BTREE,
    INDEX            `idx_enabled`(`is_enabled`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES (1, 2, 'admin', '管理员', '男', '18888888888', '201507802@qq.com', 'avatar-20200806032259161.png', '/Users/jie/Documents/work/me/admin/eladmin/~/avatar/avatar-20200806032259161.png', '$2a$10$tbRn5S0HPF0vz3PubX4j1.cjo2nsZ/tvEWGu85g14Ur90v8MWGlnG', 1, 1, '2020-05-03 16:38:31', 1, 1, 'admin', 'admin', '2018-08-23 09:11:56', '2020-05-03 16:38:31');
INSERT INTO `sys_user`
VALUES (2, 2, 'test', '测试', '男', '19999999999', '231@qq.com', NULL, NULL, '$2a$10$tbRn5S0HPF0vz3PubX4j1.cjo2nsZ/tvEWGu85g14Ur90v8MWGlnG', 0, 1, NULL, 1, 1, 'admin', 'admin', '2020-05-05 11:15:49', '2020-05-05 11:15:49');

-- ----------------------------
-- Table structure for sys_user_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_job`;
CREATE TABLE `sys_user_job`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `job_id`  bigint(20) NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (`user_id`, `job_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_job
-- ----------------------------
INSERT INTO `sys_user_job`
VALUES (1, 1);
INSERT INTO `sys_user_job`
VALUES (2, 3);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
    INDEX     `fk_q4eq273l04bpu4efj0jd0jb98`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role`
VALUES (2, 2);

-- ----------------------------
-- Table structure for tool_alipay_config
-- ----------------------------
DROP TABLE IF EXISTS `tool_alipay_config`;
CREATE TABLE `tool_alipay_config`
(
    `config_id`               bigint(20) NOT NULL COMMENT 'ID',
    `app_id`                  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用ID',
    `sys_service_provider_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
    `private_key`             text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商户私钥',
    `public_key`              text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '支付宝公钥',
    `sign_type`               varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名方式',
    `charset`                 varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码',
    `format`                  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型 固定格式json',
    `gateway_url`             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网关地址',
    `notify_url`              varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '异步回调',
    `return_url`              varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调地址',
    PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付宝配置类' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of tool_alipay_config
-- ----------------------------

-- ----------------------------
-- Table structure for tool_email_config
-- ----------------------------
DROP TABLE IF EXISTS `tool_email_config`;
CREATE TABLE `tool_email_config`
(
    `config_id` bigint(20) NOT NULL COMMENT 'ID',
    `host`      varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件服务器 SMTP 地址',
    `port`      varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件服务器 SMTP 端口',
    `user`      varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
    `pass`      varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权密码',
    `from_user` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发件人昵称（遵循RFC-822标准）',
    PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '邮箱配置' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of tool_email_config
-- ----------------------------
INSERT INTO `tool_email_config`
VALUES (1, 'smtp.163.com', '465', 'yanfeng413@163.com', '25106154114899F3F895FA9A05F6E324D9FD7FDBC949FC61', 'Boot-Admin<yanfeng413@163.com>');

-- ----------------------------
-- Table structure for tool_local_storage
-- ----------------------------
DROP TABLE IF EXISTS `tool_local_storage`;
CREATE TABLE `tool_local_storage`
(
    `storage_id`     bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `real_name`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件真实名',
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
    `suffix`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件后缀',
    `type`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
    `size`           varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件大小',
    `path`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问路径',
    `compress_path`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '压缩后访问路径',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `update_by`      bigint(20) NULL DEFAULT NULL COMMENT '更新人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `update_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    `is_deleted`     tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`storage_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '本地存储' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of tool_local_storage
-- ----------------------------

-- ----------------------------
-- Table structure for tool_qiniu_config
-- ----------------------------
DROP TABLE IF EXISTS `tool_qiniu_config`;
CREATE TABLE `tool_qiniu_config`
(
    `config_id`  bigint(20) NOT NULL COMMENT 'ID',
    `access_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'accessKey',
    `secret_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'secretKey',
    `bucket`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储空间名称作为唯一的 Bucket 识别符',
    `zone`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机房',
    `domain`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '域名',
    `space_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '空间类型：私有或公开',
    PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '七牛云配置' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of tool_qiniu_config
-- ----------------------------
INSERT INTO `tool_qiniu_config`
VALUES (1, '4gr0AzyZozKJfh-qMtrkfql-iZ_gOTcW1awCPwc9', 'UH_w_Yri58MqkX1HkQntzYgjBmNuColNRCdke-_y', 'boot-admin', '华南', 'http://r8xxnkj0f.hn-bkt.clouddn.com', '公开');

-- ----------------------------
-- Table structure for tool_qiniu_content
-- ----------------------------
DROP TABLE IF EXISTS `tool_qiniu_content`;
CREATE TABLE `tool_qiniu_content`
(
    `content_id`     bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `bucket`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Bucket 识别符',
    `space_type`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '空间类型：私有或公开',
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
    `suffix`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件后缀',
    `type`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
    `size`           varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件大小',
    `url`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问地址',
    `compress_url`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '压缩后访问地址',
    `create_by`      bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `create_by_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `is_deleted`     tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`content_id`) USING BTREE,
    UNIQUE INDEX `uk_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '七牛云文件存储' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of tool_qiniu_content
-- ----------------------------

SET
FOREIGN_KEY_CHECKS = 1;
