package org.utility.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.utility.annotation.DataPermission;
import org.utility.annotation.Query;
import org.utility.annotation.Scene;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.utility.util.StringUtils.toUnderScoreCase;

/**
 * @author Li Yanfeng
 */
public class QueryHelp {

    private static final Logger logger = LoggerFactory.getLogger(QueryHelp.class);

    /**
     * 获取 Page
     *
     * @param query 查询对象
     * @param <T>   实体
     * @return Page
     */
    public static <T, Q> Page<T> page(Q query) {
        // 获取 current 属性值
        Integer current = (Integer) ReflectionKit.getFieldValue(query, "current");
        // 获取 size 属性值
        Integer size = (Integer) ReflectionKit.getFieldValue(query, "size");
        return new Page<T>(current, size);
    }

    /**
     * 获取 QueryWrapper
     *
     * @param query 查询对象
     * @param <T>   实体
     * @param <Q>   查询对象
     * @return QueryWrapper
     */
    // TODO DataPermission
    public static <T, Q> QueryWrapper<T> queryWrapper(Q query) {
        QueryWrapper<T> queryWrapper = Wrappers.query();
        if (query == null) {
            return queryWrapper;
        }

        // 数据权限验证
        DataPermission permission = query.getClass().getAnnotation(DataPermission.class);
        if (permission != null) {
            // 获取数据权限
            List<Long> dataScopes = SecurityUtils.getCurrentUserDataScope();
            if (CollectionUtil.isNotEmpty(dataScopes)) {
                // 过滤场景
                boolean ignoreScene = false;
                if(ArrayUtil.isNotEmpty(permission.ignoreScene())) {
                    boolean exits = query.getClass().isAnnotationPresent(Scene.class);
                    if(exits) {
                        for(Field field : query.getClass().getDeclaredFields()) {
                            Scene scene = field.getAnnotation(Scene.class);
                            if (scene != null) {
                                Object sceneValue = ReflectionUtils.getField(field, query);
                                if(ObjectUtil.isNotNull(sceneValue)) {
                                    if(StringUtils.equalsAny(String.valueOf(sceneValue), permission.ignoreScene())) {
                                        ignoreScene = true;
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        throw new RuntimeException("需要用 @Scene 注解标识场景字段值, 根据字段对应的值判断是否忽略");
                    }
                }
                if(!ignoreScene) {
                    if(StringUtils.isNotBlank(permission.joinName()) && StringUtils.isNotBlank(permission.fieldName())) {
                        //Join join = root.join(permission.joinName(), JoinType.LEFT);
                        //list.add(getExpression(permission.fieldName(),join, root).in(dataScopes));

                        throw new RuntimeException("未实现");
                    } else if (StringUtils.isBlank(permission.joinName()) && StringUtils.isNotBlank(permission.fieldName())) {
                        //list.add(getExpression(permission.fieldName(),null, root).in(dataScopes));
                        queryWrapper.in(permission.fieldName(), dataScopes);
                    }
                }
            }
        }

        try {
            // 获取查询类Query的所有字段,包括父类字段
            List<Field> fields = ReflectionKit.getFieldList(query.getClass());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                // 获取字段上的 @Query 注解
                Query q = field.getAnnotation(Query.class);
                if (q != null) {
                    String propName = q.propName();
                    String blurry = q.blurry();

                    // 属性名
                    String attributeName = StrUtil.isBlank(propName) ? field.getName() : propName;
                    attributeName = toUnderScoreCase(attributeName);
                    Object value = field.get(query);
                    if (ObjectUtil.isNull(value) || "".equals(value)) {
                        continue;
                    }

                    // 模糊多字段
                    if (ObjectUtil.isNotEmpty(blurry)) {
                        Stream.of(blurry.split(",")).forEach(column ->
                                queryWrapper.or().like(toUnderScoreCase(column), value)
                        );
                        continue;
                    }

                    // 查询方式
                    switch (q.type()) {
                        case EQ:
                            queryWrapper.eq(attributeName, value);
                            break;
                        case NE:
                            queryWrapper.ne(attributeName, value);
                            break;
                        case GT:
                            queryWrapper.gt(attributeName, value);
                            break;
                        case GE:
                            queryWrapper.ge(attributeName, value);
                            break;
                        case LT:
                            queryWrapper.lt(attributeName, value);
                            break;
                        case LE:
                            queryWrapper.le(attributeName, value);
                            break;
                        case IN:
                            if (CollUtil.isNotEmpty((Collection<?>) value)) {
                                queryWrapper.in(attributeName, value);
                            }
                            break;
                        case NOT_IN:
                            if (CollUtil.isNotEmpty((Collection<?>) value)) {
                                queryWrapper.notIn(attributeName, value);
                            }
                            break;
                        case LIKE:
                            queryWrapper.like(attributeName, value);
                            break;
                        case LEFT_LIKE:
                            queryWrapper.likeLeft(attributeName, value);
                            break;
                        case RIGHT_LIKE:
                            queryWrapper.likeRight(attributeName, value);
                            break;
                        case IS_NULL:
                            queryWrapper.isNull(attributeName);
                            break;
                        case IS_NOT_NULL:
                            queryWrapper.isNotNull(attributeName);
                            break;
                        case BETWEEN:
                            List<Object> between = CollUtil.newArrayList((Collection<Object>) value);
                            if (CollUtil.isNotEmpty(between)) {
                                queryWrapper.between(attributeName, between.get(0), between.get(1));
                            }
                            break;
                        case ASC:
                            queryWrapper.orderByAsc(attributeName);
                            break;
                        case DESC:
                            queryWrapper.orderByDesc(attributeName);
                            break;
                        default:
                            break;
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return queryWrapper;
    }
}
