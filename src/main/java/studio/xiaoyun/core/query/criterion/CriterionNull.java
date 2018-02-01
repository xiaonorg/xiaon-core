package studio.xiaoyun.core.query.criterion;

import studio.xiaoyun.core.query.AbstractQuery;

import java.util.Collections;
import java.util.List;

/**
 *  为null
 */
public class CriterionNull extends AbstractCriterion{
    private String propertyName;

    CriterionNull(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public List<String> getPropertyName() {
        return Collections.singletonList(propertyName);
    }

    @Override
    public List<Object> getPropertyValue(Class<? extends AbstractQuery> clazz) {
        return Collections.emptyList();
    }

    @Override
    public String getQuery() {
        return "%s is null";
    }
}
