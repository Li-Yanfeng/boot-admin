package com.boot.admin.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.boot.admin.annotation.DataScope;
import com.boot.admin.util.SecurityUtils;
import com.boot.admin.util.enums.DataScopeEnum;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据权限过滤处理
 *
 * @author Li Yanfeng
 */
@Aspect
@Component
public class DataScopePermissionHandler implements DataPermissionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataScopePermissionHandler.class);

    /**
     * 通过ThreadLocal记录权限相关的属性值
     */
    static final ThreadLocal<DataScopeProperty> threadLocal = new ThreadLocal<>();

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.boot.admin.annotation.DataScope)")
    public void pointcut() {
    }

    /**
     * 【前置通知】 用于拦截指定方法，判断用户当前操作是否需要限流处理
     *
     * @param joinPoint 切入点对象
     */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 获得注解中的属性
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        DataScope dataScope = signatureMethod.getAnnotation(DataScope.class);
        // 设置当前的用户权限范围及相关属性
        DataScopeProperty dataScopeProperty = new DataScopeProperty(dataScope.alias(), dataScope.field().getValue());
        threadLocal.set(dataScopeProperty);
    }

    /**
     * 【最终通知】 清空当前线程上次保存的权限信息
     */
    @After("pointcut()")
    public void doAfter() {
        threadLocal.remove();
    }


    /**
     * 获取数据权限 SQL 片段
     *
     * @param where             待执行 SQL Where 条件表达式
     * @param mappedStatementId Mybatis MappedStatement Id 根据该参数可以判断具体执行方法
     * @return JSqlParser 条件表达式
     */
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        DataScopeProperty dataScopeProperty = threadLocal.get();
        // 如果注解不存在,不过滤数据
        if (ObjectUtils.isNull(dataScopeProperty)) {
            return where;
        }
        // 处理数据权限
        return handlerDataPermission(where, dataScopeProperty);
    }


    /**
     * 处理数据权限
     *
     * @param where             待执行 SQL Where 条件表达式
     * @param dataScopeProperty 数据范围属性
     */
    private Expression handlerDataPermission(Expression where, DataScopeProperty dataScopeProperty) {
        List<String> dataScopeTypes = SecurityUtils.getDataScopeType();
        Column column = new Column(dataScopeProperty.getAliasDotField());

        // 如果包含‘全部数据权限’则跳过
        if (dataScopeTypes.contains(DataScopeEnum.ALL.getValue())) {
            return where;
        }

        // 拼接自定义表达式
        Expression customExpression = null;
        // 寻找所对应的角色权限类型
        for (String dataScopeType : dataScopeTypes) {
            // 本级数据权限
            if (DataScopeEnum.ORG.getValue().equals(dataScopeType)) {
                List<Long> currentUserDataScope = SecurityUtils.getCurrentUserDataScope();
                if (CollUtil.isNotEmpty(currentUserDataScope)) {
                    customExpression = buildEqExpression(customExpression, column, currentUserDataScope.get(0));
                }
            } else if (DataScopeEnum.ORG_AND_CHILD.getValue().equals(dataScopeType)) {
                // 本级及以下数据权限
                customExpression = buildInExpression(customExpression, column, SecurityUtils.getCurrentUserDataScope());
            } else if (DataScopeEnum.SELF.getValue().equals(dataScopeType)) {
                // 仅自己数据权限
                customExpression = buildEqExpression(customExpression, column, SecurityUtils.getCurrentUserId());
            } else if (DataScopeEnum.CUSTOM.getValue().equals(dataScopeType)) {
                // 自定义数据权限
                customExpression = buildInExpression(customExpression, column, SecurityUtils.getCurrentUserDataScope());
            }
        }
        // 合并表达式,例如 AND (dept_id = 2 OR dept_id IN (2, 5))
        if (customExpression != null) {
            return where != null ? new AndExpression(where, new Parenthesis(customExpression)) : new Parenthesis(customExpression);
        }
        return where;
    }

    /**
     * 构建 In 表达式
     *
     * @param expression 自定义条件表达式
     * @param column     列名
     * @param values     值
     */
    private Expression buildInExpression(Expression expression, Column column, List<Long> values) {
        if (CollUtil.isEmpty(values)) {
            return expression;
        }

        ItemsList itemsList = new ExpressionList(values.stream().map(LongValue::new).collect(Collectors.toList()));
        // 构建 In 表达式
        InExpression inExpression = new InExpression(column, itemsList);
        // 添加新的Where语句
        if (null == expression) {
            return inExpression;
        } else {
            return new OrExpression(expression, inExpression);
        }
    }

    /**
     * 构建 Eq 表达式
     *
     * @param expression 自定义条件表达式
     * @param column     列名
     * @param value      值
     */
    private Expression buildEqExpression(Expression expression, Column column, Long value) {
        if (ObjectUtil.isNull(value)) {
            return expression;
        }

        // 构建 Eq 表达式
        EqualsTo eqExpression = new EqualsTo();
        eqExpression.setLeftExpression(column);
        eqExpression.setRightExpression(new LongValue(value));
        // 添加新的Where语句
        if (null == expression) {
            return eqExpression;
        } else {
            return new OrExpression(expression, eqExpression);
        }
    }


    /**
     * 数据权限过滤属性
     * <p>
     * 由 ThreadLocal 传递注解中的值
     * </p>
     */
    static class DataScopeProperty {

        /**
         * 关联表别名
         */
        private String alias;
        /**
         * 关联字段
         */
        private String field;


        public String getAliasDotField() {
            return StrUtil.isBlank(alias) ? field : alias + StrUtil.DOT + field;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public DataScopeProperty(String alias, String field) {
            this.alias = alias;
            this.field = field;
        }
    }
}
