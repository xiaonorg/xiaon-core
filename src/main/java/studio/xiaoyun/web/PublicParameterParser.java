package studio.xiaoyun.web;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlSelectParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.core.query.AbstractQuery;
import studio.xiaoyun.core.query.criterion.AbstractCriterion;
import studio.xiaoyun.core.query.criterion.Query;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 解析HTTP请求中的通用参数
 * @see PublicParameter
 */
public class PublicParameterParser {
    private static Logger logger = LoggerFactory.getLogger(PublicParameterParser.class);
    private PublicParameterParser(){}

    /**
     * 从HTTP请求中取得{@linkplain PublicParameter#FIELD field}的值。
     * <p>多个值之间以逗号分隔
     * @param request HTTP请求
     * @return 返回结果中应该包括的字段。如果没有数据，则返回空列表
     */
    public static List<String> getIncludeFields(HttpServletRequest request){
        List<String> result;
        String value = request.getParameter(PublicParameter.FIELD.value());
        if(value!=null){
            if(value.matches("[0-9a-zA-Z_]+(,[0-9a-zA-Z_]+)*")){
                result = Arrays.asList(value.split(","));
            }else{
                throw new InvalidParameterException("参数"+PublicParameter.FIELD.value()+"的格式错误:"+value);
            }
        }else{
            result = new LinkedList<>();
        }
        return result;
    }

    /**
     * 从HTTP请求中取得{@linkplain PublicParameter#ROWS rows}的值
     * @param request HTTP请求
     * @return 最多返回的记录的数量
     */
    public static int getRows(HttpServletRequest request){
        int rows = ParameterUtil.getInt(request,PublicParameter.ROWS.value(),20);
        rows = rows<1||rows>1000?20:rows;
        return rows;
    }

    /**
     * 从HTTP请求中取得{@linkplain PublicParameter#SORT sort}的值。
     * <p>输入格式为&lt;fieldName&gt; &lt;asc|desc&gt;[,&lt;fieldName&gt; &lt;asc|desc&gt;]...，
     * 例如：“name desc,text asc”表示先以name降序排序，再以text升序排序
     * @param request HTTP请求
     * @return 排序参数, Object数组的第一个表示排序字段，String类型; 第二个表示是否升序，Boolean类型。如果没有数据，则返回空列表
     */
    public static List<Object[]> getSortFields(HttpServletRequest request){
        List<Object[]> result = new LinkedList<>();
        String value = request.getParameter(PublicParameter.SORT.value());
        if(value!=null){
            if(value.matches("[0-9a-zA-Z_]+ (asc|desc)(,[0-9a-zA-Z_]+ (asc|desc))*")){
                String[] sorts = value.split(",");
                for(String sort:sorts){
                    String[] s = sort.split(" ");
                    Object[] o = new Object[2];
                    o[0] = s[0];
                    o[1] = "asc".equals(s[1]);
                    result.add(o);
                }
            }else{
                throw new InvalidParameterException("参数"+PublicParameter.SORT.value()+"的格式错误:"+value);
            }
        }
        return result;
    }

    /**
     * 从HTTP请求中取得{@linkplain PublicParameter#START start}的值。
     * <p>参数{@linkplain PublicParameter#PAGE page}会转换为{@linkplain PublicParameter#START start}
     * @param request HTTP请求
     * @return 开始记录
     */
    public static int getStart(HttpServletRequest request){
        int start = ParameterUtil.getInt(request,PublicParameter.START.value(),0);
        Integer page = ParameterUtil.getInt(request,PublicParameter.PAGE.value(),null);
        int rows = getRows(request);
        if(page!=null){
            start = (page-1)*rows;
        }
        start = start<0?0:start;
        return start;
    }

    /**
     * 将HTTP请求中的通用查询参数封装为参数类
     * @param request HTTP请求
     * @param parameter 参数类
     * @return 参数类的实例
     * @see PublicParameter PublicParameter
     */
    public static <T extends AbstractQuery> T getParameter(HttpServletRequest request, Class<T> parameter){
        AbstractQuery result;
        try{
            result = parameter.newInstance();
        }catch(Exception e){
            throw new InvalidParameterException(parameter.getName()+"实例化失败,"+e.getMessage());
        }
        int start = getStart(request);
        int rows = getRows(request);
        List<String> includeFields = getIncludeFields(request);
        List<Object[]> sortFields = getSortFields(request);
        List<AbstractCriterion> criterions = getCriterions(request);
        for(Object[] objs:sortFields){
            result.addSort((String)objs[0], (Boolean)objs[1]);
        }
        for(AbstractCriterion c:criterions){
            result.addQuery(c);
        }
        result.addIncludeField(includeFields.toArray(new String[includeFields.size()]));
        result.setFirstResult(start);
        result.setMaxResults(rows);
        if(!result.validate()){
            throw new InvalidParameterException("查询条件无法解析!");
        }
        @SuppressWarnings("unchecked")
        T t = (T)result;
        return t;
    }

    private static List<AbstractCriterion> getCriterions(HttpServletRequest request){
        List<AbstractCriterion> result = null;
        String value = request.getParameter(PublicParameter.SEARCH.value());
        if(value!=null){
            try{
                boolean isEncode = ParameterUtil.getBoolean(request,PublicParameter.SEARCH_ENCODE.value(),false);
                if(isEncode){  //如果为true，说明使用了urlencode
                    value = URLDecoder.decode(value, "utf-8");
                }
                if(value.length()>500){
                    throw new InvalidParameterException("参数太长");
                }
                MySqlSelectParser parser = new MySqlSelectParser("select 1 from dual where "+value);
                MySqlSelectQueryBlock query = (MySqlSelectQueryBlock)parser.query();
                result = parserExpression(query.getWhere());
            }catch(XyException e){
                throw e;
            }catch(Exception e){
                logger.debug("解析通用参数出错！"+value,e);
                throw new InvalidParameterException("参数"+PublicParameter.SEARCH.value()+"的值无法解析:"+value);
            }
        }
        return result==null?Collections.emptyList():result;
    }

    private static List<AbstractCriterion> parserOrExpression(SQLExpr sqlExpr){
        List<AbstractCriterion> list = new ArrayList<>();
        if(sqlExpr instanceof SQLBinaryOpExpr){
            SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr)sqlExpr;
            if(SQLBinaryOperator.BooleanOr.equals(sqlBinaryOpExpr.getOperator())){
                list.addAll(parserOrExpression(sqlBinaryOpExpr.getLeft()));
                list.addAll(parserOrExpression(sqlBinaryOpExpr.getRight()));
            }else if(SQLBinaryOperator.BooleanAnd.equals(sqlBinaryOpExpr.getOperator())){
                List<AbstractCriterion> list2 = parserExpression(sqlBinaryOpExpr);
                list.add(Query.and(list2.toArray(new AbstractCriterion[list2.size()])));
            }else{
                list.addAll(parserExpression(sqlBinaryOpExpr));
            }
        }else{
            list.addAll(parserExpression(sqlExpr));
        }
        return list;
    }

    private static List<AbstractCriterion> parserExpression(SQLExpr sqlExpr){
        List<AbstractCriterion> list = new ArrayList<>();
        if(sqlExpr instanceof SQLBinaryOpExpr){
            SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr)sqlExpr;
            SQLExpr left = sqlBinaryOpExpr.getLeft();
            SQLExpr right = sqlBinaryOpExpr.getRight();
            SQLBinaryOperator operator = sqlBinaryOpExpr.getOperator();
            switch (operator){
                case BooleanAnd:  // and
                    list.addAll(parserExpression(left));
                    list.addAll(parserExpression(right));
                    break;
                case BooleanOr:   // or
                    List<AbstractCriterion> cList = new ArrayList<>();
                    cList.addAll(parserOrExpression(left));
                    cList.addAll(parserOrExpression(right));
                    list.add(Query.or(cList.toArray(new AbstractCriterion[cList.size()])));
                    break;
                case GreaterThan:  // >
                    list.add(Query.gt(left.toString(),formatStr(right.toString())));
                    break;
                case GreaterThanOrEqual: // >=
                    list.add(Query.ge(left.toString(),formatStr(right.toString())));
                    break;
                case LessThan: // <
                    list.add(Query.lt(left.toString(),formatStr(right.toString())));
                    break;
                case LessThanOrEqual: // <=
                    list.add(Query.le(left.toString(),formatStr(right.toString())));
                    break;
                case Equality:  // =
                    list.add(Query.equals(left.toString(),formatStr(right.toString())));
                    break;
                case NotEqual: // !=
                    list.add(Query.notEquals(left.toString(),formatStr(right.toString())));
                    break;
                case Like: // like
                    String value = right.toString().replaceAll("[*]", "%").replaceAll("[?]", "_");
                    list.add(Query.like(left.toString(),formatStr(value)));
                    break;
                default:
                    throw new InvalidParameterException("无效参数:"+operator.getName());
            }
        } else if(sqlExpr instanceof SQLInListExpr){   // in
            SQLInListExpr sqlInListExpr = (SQLInListExpr)sqlExpr;
            String name = sqlInListExpr.getExpr().toString();
            List<SQLExpr> sqlExprList = sqlInListExpr.getTargetList();
            List<String> propertyValue = sqlExprList.stream().map(s -> formatStr(s.toString())).collect(Collectors.toList());
            list.add(Query.in(name,propertyValue));
        } else {
            throw new InvalidParameterException("表达式无法解析");
        }
        return list;
    }

    private static String formatStr(String str){
        if(str.startsWith("'") && str.endsWith("'")){
            return str.substring(1,str.length()-1);
        }else{
            return str;
        }
    }

}
