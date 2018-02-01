package studio.xiaoyun.web;

import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.query.AbstractQuery;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 辅助类，方便从HTTP请求中获得参数
 * @author 岳正灵
 * @since 1.0.0
 */
public class ParameterUtil {
	private ParameterUtil(){}

	/**
	 * 从请求中获得Boolean类型的参数
	 * @param request HTTP请求
	 * @param parameterName 参数名
	 * @param defaultValue 默认值
	 * @return 参数的值,如果指定的参数不存在，则返回默认值，如果参数不是一个Boolean类型的值，则返回false
     */
	public static Boolean getBoolean(HttpServletRequest request,String parameterName,Boolean defaultValue){
		String value = request.getParameter(parameterName);
		if(value!=null){
			return Boolean.valueOf(value);
		}else{
			return defaultValue;
		}
	}

	/**
	 * 从请求中获得Date类型的参数。
	 * <p>参数的格式应该是yyyy-MM-dd
	 * @param request HTTP请求
	 * @param parameterName 参数名
	 * @return 参数的值，如果参数不存在，则返回null
	 * @throws InvalidParameterException 如果参数的值不是Date类型，则抛出异常
	 */
	public static Date getDate(HttpServletRequest request,String parameterName)throws InvalidParameterException {
		return getDate(request,parameterName,"yyyy-MM-dd");
	}
	
	/**
	 * 从请求中获得Date类型的参数
	 * @param request HTTP请求
	 * @param parameterName 参数名
	 * @param pattern 日期格式, 例如:yyyy-MM-dd
	 * @return 参数的值，如果参数不存在，则返回null
	 * @throws InvalidParameterException 如果参数的值不是Date类型，则抛出异常
	 */
	public static Date getDate(HttpServletRequest request,String parameterName,String pattern)throws InvalidParameterException {
		Date date = null;
		String value = request.getParameter(parameterName);
		if(value!=null){
			try{
				date = new SimpleDateFormat(pattern).parse(value);
			}catch(ParseException e){
				throw new InvalidParameterException("参数"+parameterName+"的格式错误:"+value);
			}
		}
		return date;
	}
	
	/**
	 * 从请求中获得Double类型的参数
	 * @param request HTTP请求
	 * @param parameterName 参数名
	 * @param defaultValue 默认值
	 * @return 参数的值，如果参数不存在，则返回默认值
	 * @throws InvalidParameterException 如果参数的值不是Double类型，则抛出异常
	 */
	public static Double getDouble(HttpServletRequest request,String parameterName,Double defaultValue)throws InvalidParameterException {
		Double result = defaultValue;
		String value = request.getParameter(parameterName);
		if(value!=null){
			try{
				result = Double.valueOf(value.trim());
			}catch(NumberFormatException e){
				throw new InvalidParameterException(parameterName+"参数应该是数字:"+value);
			}
		}
		return result;
	}
	
	/**
	 * 从请求中获得整数类型的参数
	 * @param request HTTP请求
	 * @param parameterName 参数名
	 * @param defaultValue 默认值
	 * @return 参数的值，如果参数不存在，则返回默认值
	 * @throws InvalidParameterException 如果参数的值不是整数类型，则抛出异常
	 */
	public static Integer getInt(HttpServletRequest request,String parameterName,Integer defaultValue)throws InvalidParameterException {
		Integer result = defaultValue;
		String value = request.getParameter(parameterName);
		if(value!=null){
			try{
				result = Integer.valueOf(value.trim());
			}catch(NumberFormatException e){
				throw new InvalidParameterException(parameterName+"参数应该是整数:"+value);
			}
		}
		return result;
	}
	
	/**
	 * 从请求中获得整数类型的参数
	 * @param request HTTP请求
	 * @param parameterName 参数名
	 * @param defaultValue 默认值
	 * @return 参数的值，如果参数不存在，则返回默认值
	 * @throws InvalidParameterException 如果参数的值不是整数类型，则抛出异常
	 */
	public static Long getLong(HttpServletRequest request,String parameterName,Long defaultValue)throws InvalidParameterException {
		Long result = defaultValue;
		String value = request.getParameter(parameterName);
		if(value!=null){
			try{
				result = Long.valueOf(value.trim());
			}catch(NumberFormatException e){
				throw new InvalidParameterException(parameterName+"参数应该是整数:"+value);
			}
		}
		return result;
	}
	
	/**
	 * 将HTTP请求中的通用查询参数封装为参数类
	 * @param request HTTP请求
	 * @param parameter 参数类
	 * @return 参数类的实例
	 * @see studio.xiaoyun.web.PublicParameter PublicParameter
	 */
	public static <T extends AbstractQuery> T getParameter(HttpServletRequest request, Class<T> parameter){
		return PublicParameterParser.getParameter(request,parameter);
	}
	
	/**
	 * 从请求中获得String类型的参数
	 * @param request HTTP请求
	 * @param parameterName 参数名
	 * @param defaultValue 默认值
	 * @return 参数的值，如果参数不存在，则返回默认值
	 */
	public static String getString(HttpServletRequest request,String parameterName,String defaultValue){
		String value = request.getParameter(parameterName);
		if(value==null){
			return defaultValue;
		}else{
			return value;
		}
	}

}
