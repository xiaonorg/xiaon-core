package studio.xiaoyun.core.query.criterion;

import java.util.Collection;

/**
 * 封装查询条件中的各种运算符。
 * <p>例如：大于、小于、等于</p>
 */
public final class Query {

    /**
     * 值为null
     *
     * @param propertyName 属性名
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion isNull(String propertyName) {
        return new CriterionNull(propertyName);
    }

    /**
     * 逻辑与
     *
     * @param criterions 搜索条件
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion and(AbstractCriterion... criterions) {
        return new CriterionAnd(criterions);
    }

    /**
     * 等于
     *
     * @param propertyName 属性名
     * @param value        值
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion equals(String propertyName, Object value) {
        return new CriterionEquals(propertyName, value);
    }

    /**
     * 大于等于
     *
     * @param propertyName 属性名
     * @param value        值
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion ge(String propertyName, Object value) {
        return new CriterionGe(propertyName, value);
    }

    /**
     * 大于
     *
     * @param propertyName 属性名
     * @param value        值
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion gt(String propertyName, Object value) {
        return new CriterionGt(propertyName, value);
    }

    /**
     * 范围查询
     *
     * @param propertyName 属性名
     * @param values       值
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion in(String propertyName, Collection<?> values) {
        return new CriterionIn(propertyName, values);
    }

    /**
     * 小于等于
     *
     * @param propertyName 属性名
     * @param value        值
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion le(String propertyName, Object value) {
        return new CriterionLe(propertyName, value);
    }

    /**
     * 模糊查询
     * <br/>支持占位符‘%’、'_'
     *
     * @param propertyName 属性名
     * @param value        值
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion like(String propertyName, Object value) {
        return new CriterionLike(propertyName, value);
    }

    /**
     * 小于
     *
     * @param propertyName 属性名
     * @param value        值
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion lt(String propertyName, Object value) {
        return new CriterionLt(propertyName, value);
    }

    /**
     * 不等于
     *
     * @param propertyName 属性名
     * @param value        值
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion notEquals(String propertyName, Object value) {
        return new CriterionNotEquals(propertyName, value);
    }

    /**
     * 逻辑或
     *
     * @param criterions Criterion实例
     * @return Criterion实例
     * @since 1.0.0
     */
    public static AbstractCriterion or(AbstractCriterion... criterions) {
        return new CriterionOr(criterions);
    }

    private Query() {
    }

}
