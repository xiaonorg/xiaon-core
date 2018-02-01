package studio.xiaoyun.core.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.core.query.criterion.AbstractCriterion;
import studio.xiaoyun.core.query.criterion.CriterionAnd;
import studio.xiaoyun.core.query.criterion.CriterionOr;
import studio.xiaoyun.core.query.criterion.Query;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 所有查询类的超类
 * 
 */
public abstract class AbstractQuery implements Serializable {

	private Logger logger = LoggerFactory.getLogger(AbstractQuery.class);
	private static final long serialVersionUID = 8599993944205393403L;
	/**
	 * 添加的查询条件
	 */
	private List<AbstractCriterion> criterions;
	/**
	 * 第一条记录的位置
	 */
	private int firstResult = 0;
	/**
	 * 返回值中包括的字段，不同字段之间使用','分隔
	 */
	private String includeField = "";
	/**
	 * 是否升序，由0和1组成的字符串,0 降序 ,1 升序
	 */
	private String isAsc = "";

	/**
	 * 最多返回的记录数
	 */
	private int maxResults = 20;
	
	/**
	 * 排序字段,不同字段之间用','分隔
	 */
	private String sortField = "";
	
	/**
	 * 添加返回值中包括的字段。
	 * <p>默认返回所有数据，使用该方法可以设置只返回某些字段的数据，没有设置的数据将不会返回
	 * @param fields 返回值中包括的字段
	 * @return Parameter的实例
	 */
	public AbstractQuery addIncludeField(String... fields){
		if(fields==null || fields.length==0){
			return this;
		}
		for(String field:fields){
			if (field != null && field.matches("[0-9a-zA-Z_-]+")) {
				if (this.includeField.length()==0) {
					this.includeField = field;
				} else {
					this.includeField += "," + field;
				}
			} else {
				throw new InvalidParameterException("参数错误:"+field);
			}
		}
		return this;
	}

	/**
	 * 添加查询条件。
	 * <p>示例：parameter.addQuery(Query.equals("name","test"));
	 * @param query 查询条件
	 * @return Parameter的实例
	 * @see Query
	 */
	public AbstractQuery addQuery(AbstractCriterion query) {
		if(criterions==null){
			this.criterions = new LinkedList<>();
		}
		this.criterions.add(query);
		return this;
	}

	/**
	 * 添加排序参数。
	 * <p>在子类中有定义的属性都可以排序，没有定义的属性则不能排序
	 * @param sortField 排序字段
	 * @param isAsc 是否升序，true 升序，false 降序
	 * @return Parameter的实例
	 */
	public AbstractQuery addSort(String sortField, boolean isAsc){
		if (sortField != null && sortField.matches("[0-9a-zA-Z_-]+")) {
			if (isAsc) {
				this.isAsc += "1";
			} else {
				this.isAsc += "0";
			}
			if (this.sortField.length()==0) {
				this.sortField += sortField;
			} else {
				this.sortField += "," + sortField;
			}
		} else {
			throw new InvalidParameterException("参数错误:"+sortField);
		}
		return this;
	}

	/**
	 * 
	 * @return 最多返回的记录数
	 * @since 1.0.0
	 */
	public int getFirstResult() {
		return firstResult;
	}

	/**
	 * 取得返回值中要包含的字段
	 * @return 返回值中要包含的字段
	 * @since 1.0.0
	 */
	public List<String> getIncludeField(){
		String str = includeField.trim();
		if(str.length()==0){
			return Collections.emptyList();
		}else{
			return Arrays.asList(str.split(","));
		}
	}

	/**
	 * 获得是否升序的数组。
	 * <p>该数组和排序字段的数组一一对应
	 * @return 是否升序
	 * @since 1.0.0
	 */
	public List<Boolean> getIsAsc() {
		if(isAsc.length()==0){
			return Collections.emptyList();
		}else{
			byte[] bytes = isAsc.getBytes();
			List<Boolean> result = new LinkedList<>();
			for (byte aByte : bytes) {
				if (aByte == '0') {
					result.add(false);
				} else {
					result.add(true);
				}
			}
			return result;
		}
	}

	/**
	 * 
	 * @return 第一条记录的位置
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * 获得Criterion中的所有属性
	 */
	private List<String> getPropertiesByCriterions(List<AbstractCriterion> criterions){
		if(criterions==null || criterions.isEmpty()){
			return Collections.emptyList();
		}
		List<String> result = new LinkedList<>();
		for(AbstractCriterion criterion:criterions){
			result.addAll(criterion.getPropertyName());
		}
		return result;
	}

	/**
	 * 获得当前类实例中的所有属性名，不包括Parameter类的属性
	 * @return 类中的所有属性名
     */
	public Set<String> getPropertyName(){
		List<Field> fieldList = new LinkedList<>();
		Field[] fields = this.getClass().getDeclaredFields();
		fieldList.addAll(Arrays.asList(fields));
		Class<?> superClass = this.getClass().getSuperclass();
		while (superClass != Object.class && superClass != AbstractQuery.class) {
			fields = superClass.getDeclaredFields();
			fieldList.addAll(Arrays.asList(fields));
			superClass = superClass.getSuperclass();
		}
		return fieldList.stream().map(Field::getName).collect(Collectors.toSet());
	}

	/**
	 * 根据属性名获得属性的值
	 * @param name 属性名
	 * @return 属性的值,如果没有get方法，或者没有值，则返回空的Optional实例
     */
	private Optional<Object> getPropertyValue(String name){
		Optional<Object> result = Optional.empty();
		Method[] methods = getClass().getMethods();
		for(Method method:methods){
			if(method.getName().equalsIgnoreCase("get"+name) && method.getParameterCount() == 0){
				try {
					result = Optional.ofNullable(method.invoke(this));
				} catch (Exception e) {
					logger.error("使用反射获得方法的值时出现错误",e);
				}
				break;
			}
		}
		return result;
	}

	/**
	 * 获得字符串格式的查询条件
	 * @return 字符串格式的查询条件
	 */
	public String getQuery() {
		return this.getQuery(null);
	}
	
	/**
	 * 获得字符串格式的查询条件
	 * @param mapping 设置生成的字符串中类成员变量和数据库字段的映射关系，Map的key是类的成员变量名称，value是数据库字段名称,可以为null
	 * @return 字符串格式的查询条件
	 */
	public String getQuery(Map<String, String> mapping) {
		if(!validate()){
			throw new InvalidParameterException("查询条件存在错误!");
		}
		List<AbstractCriterion> cList = getAllCriterion();
		if(cList.isEmpty()){
			return "";
		}
		//将查询条件转换为字符串
		String where = this.getStringByCriterions(cList);
		List<String> propertiesList = this.getPropertiesByCriterions(cList);
		Object[] params;
		if(mapping==null || mapping.isEmpty()){
			params = propertiesList.toArray(new String[propertiesList.size()]);
		}else{
			params = new Object[propertiesList.size()];
			for (int i = 0; i < propertiesList.size(); i++) {
				String param = mapping.get(propertiesList.get(i));
				if (param != null) {
					params[i] = param;
				} else {
					throw new XyException("没有找到属性"+propertiesList.get(i)+"的映射关系");
				}
			}
		}
		return String.format(where, params);
	}

	/**
	 *
	 * @return 查询条件的值
     */
	public List<Object> getQueryValue(){
		List<Object> result = new ArrayList<>();
		List<AbstractCriterion> list = getAllCriterion();
		for(AbstractCriterion c:list){
			result.addAll(c.getPropertyValue(this.getClass()));
		}
		return  result;
	}

	private List<AbstractCriterion> getAllCriterion(){
		List<AbstractCriterion> cList = new LinkedList<>();
		//将类中非null的属性添加到查询条件中
		Set<String> names = getPropertyName();
		for (String name : names) {
			Optional<Object> value = getPropertyValue(name);
			if(value.isPresent()){
				cList.add(Query.equals(name, value.get()));
			}
		}
		//添加使用addQuery()方法设置的查询条件
		if(criterions!=null){
			cList.addAll(criterions);
		}
		return cList;
	}

	/**
	 * 获得排序的字段。
	 * <p>该数组和是否升序的数组一一对应</p>
	 * @return 排序的字段
	 */
	public List<String> getSortField() {
		if(sortField.length()==0){
			return Collections.emptyList();
		}else{
			List<String> result = new LinkedList<>();
			Collections.addAll(result, sortField.split(","));
			return result;
		}
	}
	
	/**
	 * 将Criterion列表转换为字符串
	 */
	private String getStringByCriterions(List<AbstractCriterion> criterions){
		StringBuilder result = new StringBuilder();
		String defaultMode = " and ";
		for(AbstractCriterion criterion:criterions){
			if(criterion instanceof CriterionOr || criterion instanceof CriterionAnd){
				result.append("(");
			}
			result.append(criterion.getQuery());
			if(criterion instanceof CriterionOr || criterion instanceof CriterionAnd){
				result.append(")");
			}
			result.append(defaultMode);
		}
		result.delete(result.length()- defaultMode.length(), result.length());
		if(criterions.size()==1 && (criterions.get(0) instanceof CriterionAnd || criterions.get(0) instanceof CriterionOr)){
			result.deleteCharAt(0);
			result.deleteCharAt(result.length()-1);
		}
		return result.toString();
	}

	/**
	 * 设置第一条记录的位置
	 * @param firstResult 第一条记录的位置，从0开始
	 */
	public void setFirstResult(int firstResult) {
		if (firstResult < 0) {
			throw new InvalidParameterException("参数不能小于0");
		} else {
			this.firstResult = firstResult;
		}
	}

	/**
	 * 设置最多返回的记录数
	 * @param maxResults 最多返回的记录数,取值范围为：大于0，并且小于1001
	 */
	public void setMaxResults(int maxResults) {
		if (maxResults < 0 || maxResults > 1000) {
			throw new InvalidParameterException("参数应大于0,并且小于1001!");
		} else {
			this.maxResults = maxResults;
		}
	}

	/**
	 * 数据是否有效
	 * @return true 有效，false 有效
	 */
	public boolean validate() {
		//获得类实例中的所有属性
		Set<String> name = getPropertyName();
		//验证添加的查询条件中的属性名在类实例中是否有定义
		List<String> propertyList = getPropertiesByCriterions(criterions);
		boolean flag = propertyList.stream().allMatch(name::contains);
		if(!flag){
			if(logger.isDebugEnabled()){
				logger.debug("查询条件中的属性名在类实例中没有有定义,"+propertyList.toString());
			}
			return false;
		}
		//验证排序字段是否在类实例中是否有定义
		List<String> sorts = getSortField();
		flag = sorts.stream().allMatch(name::contains);
		if(!flag){
			if(logger.isDebugEnabled()){
				logger.debug("排序字段在类实例中没有有定义,"+sorts.toString());
			}
			return false;
		}
		return flag;
	}
	
}
