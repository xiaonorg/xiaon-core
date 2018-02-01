package studio.xiaoyun.core.query.criterion;

import java.util.LinkedList;
import java.util.List;

import studio.xiaoyun.core.query.AbstractQuery;

/**
 * 逻辑与
 */
public class CriterionAnd extends AbstractCriterion {
	private AbstractCriterion[] criterions;

	CriterionAnd(AbstractCriterion... criterions) {
		this.criterions = criterions;
	}

	@Override
	public List<String> getPropertyName() {
		List<String> list = new LinkedList<>();
		for(AbstractCriterion c:criterions){
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
		for(AbstractCriterion c:criterions){
			if(c instanceof CriterionOr || c instanceof CriterionAnd){
				result.append("(");
			}
			result.append(c.getQuery());
			if(c instanceof CriterionOr || c instanceof CriterionAnd){
				result.append(")");
			}
			result.append(" and ");
		}
		result.delete(result.length()-5,result.length());
		return result.toString();
	}

}
