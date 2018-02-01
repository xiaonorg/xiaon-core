package studio.xiaoyun.core.query.criterion;

import studio.xiaoyun.core.query.AbstractQuery;

import java.util.LinkedList;
import java.util.List;

/**
 * 逻辑或
 */
public class CriterionOr extends AbstractCriterion {
    private AbstractCriterion[] criterions;

    CriterionOr(AbstractCriterion[] criterions) {
        this.criterions = criterions;
    }

    @Override
    public List<String> getPropertyName() {
        List<String> list = new LinkedList<>();
        for (AbstractCriterion c : criterions) {
            list.addAll(c.getPropertyName());
        }
        return list;
    }

    @Override
    public List<Object> getPropertyValue(Class<? extends AbstractQuery> clazz) {
        List<Object> list = new LinkedList<>();
        for(AbstractCriterion c:criterions){
            list.addAll(c.getPropertyValue(clazz));
        }
        return list;
    }

    @Override
    public String getQuery() {
        StringBuilder result = new StringBuilder();
        for (AbstractCriterion c : criterions) {
            if (c instanceof CriterionAnd || c instanceof CriterionOr) {
                result.append("(");
            }
            result.append(c.getQuery());
            if (c instanceof CriterionAnd || c instanceof CriterionOr) {
                result.append(")");
            }
            result.append(" or ");
        }
        result.delete(result.length() - 4, result.length());
        return result.toString();
    }

}
