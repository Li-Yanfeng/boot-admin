-- ${apiAlias!} SQL
INSERT INTO `sys_menu` (pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time)
VALUES (1, 3, 1, '${apiAlias!}管理', '${className}', '${moduleName}/${changeClassName}/index', NULL, '${changeClassName}', '${changeClassName}', b'0', b'0', b'0', '${changeClassName}:list', NULL, NULL, NULL, NULL);

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO `sys_menu` (pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time)
VALUES (@parentId, 0, 2, '${apiAlias!}新增', NULL, '', 1, '', '', b'0', b'0', b'0', '${changeClassName}:add', NULL, NULL, NULL, NULL);

INSERT INTO `sys_menu` (pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time)
VALUES (@parentId, 0, 2, '${apiAlias!}编辑', NULL, '', 2, '', '', b'0', b'0', b'0', '${changeClassName}:edit', NULL, NULL, NULL, NULL);

INSERT INTO `sys_menu` (pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time)
VALUES (@parentId, 0, 2, '${apiAlias!}删除', NULL, '', 3, '', '', b'0', b'0', b'0', '${changeClassName}:del', NULL, NULL, NULL, NULL);

INSERT INTO `sys_menu` (pid, sub_count, type, title, name, component, menu_sort, icon, path, i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time)
VALUES (@parentId, 0, 2, '${apiAlias!}导出', NULL, '', 4, '', '', b'0', b'0', b'0', '${changeClassName}:export', NULL, NULL, NULL, NULL);
