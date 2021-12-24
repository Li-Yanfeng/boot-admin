-- 最新排序号
SELECT @menuSort := ( SELECT sort + 1 AS sort FROM sys_menu WHERE type = 1 AND pid = 1 ORDER BY sort DESC LIMIT 1 );

-- ${apiAlias!} SQL
INSERT INTO `sys_menu` (pid, sub_count, type, title, name, component, sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time)
VALUES (1, 3, 1, '${apiAlias!}管理', '${className}', '${moduleName}/${changeClassName}/index', @menuSort, NULL, NULL, b'0', b'0', b'0', '${changeClassName}:list', NULL, NULL, NULL, NULL);

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO `sys_menu` (pid, sub_count, type, title, name, component, sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time)
VALUES (@parentId, 0, 2, '${apiAlias!}新增', NULL, NULL, 1, NULL, NULL, b'0', b'0', b'0', '${changeClassName}:add', NULL, NULL, NULL, NULL);

INSERT INTO `sys_menu` (pid, sub_count, type, title, name, component, sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time)
VALUES (@parentId, 0, 2, '${apiAlias!}删除', NULL, NULL, 2, NULL, NULL, b'0', b'0', b'0', '${changeClassName}:del', NULL, NULL, NULL, NULL);

INSERT INTO `sys_menu` (pid, sub_count, type, title, name, component, sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time)
VALUES (@parentId, 0, 2, '${apiAlias!}编辑', NULL, NULL, 3, NULL, NULL, b'0', b'0', b'0', '${changeClassName}:edit', NULL, NULL, NULL, NULL);
