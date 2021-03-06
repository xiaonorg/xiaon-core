package studio.xiaoyun.core.query.criterion;

import studio.xiaoyun.core.query.AbstractQuery;

import java.util.Collections;
import java.util.List;

/**
 * 小于等于
 */
public class CriterionLe extends AbstractCriterion {
    private String propertyName;
    private Object value;

    CriterionLe(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    @Override
    public List<String> getPropertyName() {
        return Collections.singletonList(propertyName);
    }

    @Override
    public List<Object> getPropertyValue(Class<? extends AbstractQuery> clazz) {
        Object result = getPropertyValue(clazz, propertyName, value);
        return Collections.singletonList(result);
    }

    @Override
    public String getQuery() {
        return "%s <= ?";
    }


}
