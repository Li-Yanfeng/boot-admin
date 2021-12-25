package com.boot.admin.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.boot.admin.annotation.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.boot.admin.util.StringUtils.toUnderScoreCase;

/**
 * @author Li Yanfeng
 */
public class QueryHelp {

    private static final Logger logger = LoggerFactory.getLogger(QueryHelp.class);

    /**
     * 获取 LambdaQueryWrapper
     *
     * @param <T> 实体
     * @param <Q> 查询对象
     * @return LambdaQueryWrapper
     */
    public static <T, Q> LambdaQueryWrapper<T> lambdaQueryWrapper(Q query) {
        QueryWrapper<T> wrapper = queryWrapper(query);
        return wrapper.lambda();
    }

    /**
     * 获取 QueryWrapper
     *
     * @param <T> 实体
     * @param <Q> 查询对象
     * @return QueryWrapper
     */
    public static <T, Q> QueryWrapper<T> queryWrapper(Q query) {
        QueryWrapper<T> queryWrapper = Wrappers.query();
        if (query == null) {
            return queryWrapper;
        }

        try {
            // 获取查询类Query的所有字段,包括父类字段
            List<Field> fields = ReflectionKit.getFieldList(query.getClass());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                // 获取字段上的 @Query 注解
                Query annotation = field.getAnnotation(Query.class);
                if (annotation != null) {
                    String propName = annotation.propName();
                    String q = annotation.q();

                    // 获取属性名
                    String attributeName = StrUtil.isBlank(propName) ? field.getName() : propName;
                    attributeName = toUnderScoreCase(attributeName).toLowerCase();
                    Object value = field.get(query);
                    if (ObjectUtil.isNull(value) || "".equals(value)) {
                        continue;
                    }

                    // 查询方式
                    setQueryWrapper(queryWrapper, annotation.type(), attributeName, value);

                    // 模糊多字段
                    if (StrUtil.isNotBlank(q)) {
                        Stream.of(q.split(",")).forEach(column ->
                            queryWrapper.or().like(toUnderScoreCase(column).toLowerCase(), value)
                        );
                        continue;
                    }
                }
                // 自定义排序
                setOrder(queryWrapper, query, field);
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return queryWrapper;
    }

    /**
     * 设置查询方式
     *
     * @param queryWrapper 查询条件构造器
     * @param query        查询类
     * @param field        查询实体属性
     * @throws IllegalAccessException /
     */
    private static <T, Q> void setOrder(QueryWrapper<T> queryWrapper, Q query, Field field) throws IllegalAccessException {
        // 获取排序字段
        if ("sort".equals(field.getName())) {
            String value = (String) field.get(query);
            if (StrUtil.isNotBlank(value)) {
                String[] elements = value.split("[,，]");
                // 待排序的字段
                String[] attributeNames = new String[elements.length - 1];
                // 排序方式
                String sort = null;
                for (int i = 0, size = elements.length; i < size; i++) {
                    if (i == elements.length - 1) {
                        sort = elements[i];
                    } else {
                        attributeNames[i] = toUnderScoreCase(elements[i]).toLowerCase();
                    }
                }
                // 是否排序
                boolean isSort = StrUtil.endWithIgnoreCase(sort, SqlKeyword.ASC.name()) || StrUtil.endWithIgnoreCase(sort, SqlKeyword.DESC.name());
                // 是否升序
                boolean isAsc = StrUtil.equalsIgnoreCase(SqlKeyword.ASC.name(), sort);
                queryWrapper.orderBy(isSort, isAsc, attributeNames);
            }
        }
    }

    /**
     * 设置查询对象
     *
     * @param queryWrapper  查询条件构造器
     * @param type          查询类型
     * @param attributeName 属性名
     * @param value         属性值
     */
    private static <T, Q> void setQueryWrapper(QueryWrapper<T> queryWrapper, SqlKeyword type, String attributeName, Object value) {
        switch (type) {
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
            default:
                break;
        }
    }
}
