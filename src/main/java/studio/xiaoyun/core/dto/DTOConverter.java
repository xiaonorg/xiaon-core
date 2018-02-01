package studio.xiaoyun.core.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.core.query.AbstractQuery;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 辅助类，用于DO(数据对象)和DTO(数据传输对象)之间的转换
 * @author 岳正灵
 */
@Service
public class DTOConverter {
    private Logger logger = LoggerFactory.getLogger(DTOConverter.class);

    /**
     * 将DO(数据对象)转换为DTO(数据传输对象)。
     * <p>如果DO中的属性名和DTO中的相同，DO中属性的值将被复制到DTO中</p>
     * @param entity DO
     * @param query 查询参数
     * @param clazz DTO类型
     * @return DTO
     */
    public <T extends AbstractDTO> T toDto(Object entity, AbstractQuery query, Class<T> clazz) {
        List<T> list = toDto(Collections.singletonList(entity),query,clazz);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    /**
     * 将DO(数据对象)转换为DTO(数据传输对象)。
     * <p>如果DO中的属性名和DTO中的相同，DO中属性的值将被复制到DTO中</p>
     * @param entitys  do列表
     * @param query 查询参数
     * @param clazz  DTO类型
     * @return DTO列表
     */
    public <T extends AbstractDTO> List<T> toDto(List<?> entitys, AbstractQuery query, Class<T> clazz) {
        if (entitys.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> resources = new LinkedList<>();
        copy(entitys,resources,clazz);
        setNull(resources, query);  //将多余的属性设置为null
        return resources;
    }

    /**
     * 将DTO装换为DO
     * @param resource DTO
     * @param clazz DO
     * @return DO 类
     */
    public <K extends AbstractDTO,V> V toDo(K resource, Class<V> clazz) {
        List<V> list = toDo(Collections.singletonList(resource),clazz);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    /**
     * 将DTO装换为DO
     * @param resources DTO列表
     * @param clazz DO类型
     * @return DO列表
     */
    public <K extends AbstractDTO,V> List<V> toDo(List<K> resources, Class<V> clazz) {
        if (resources.isEmpty()) {
            return Collections.emptyList();
        }
        List<V> entitys = new LinkedList<>();
        copy(resources,entitys,clazz);
        return entitys;
    }

    /**
     * 将一个列表的数据复制到另一个列表中，列表的类型不同。
     * <p>只有属性名称相同时才能被复制</p>
     * @param sourceList 源列表
     * @param newList 新的列表
     * @param newListType 新列表的类型
     */
    private void copy(List sourceList,List newList,Class<?> newListType) {
        Map<String, Method> getMethodMap = new HashMap<>();
        Map<String, Method> setMethodMap = new HashMap<>();
        //获得所有get开头的所有方法
        for (Method m : sourceList.get(0).getClass().getMethods()) {
            if (m.getName().startsWith("get") && m.getParameterCount() == 0) {
                getMethodMap.put(m.getName().substring(3), m);
            }
        }
        //获得所有set开头的方法
        for (Method m : newListType.getMethods()) {
            if (m.getName().startsWith("set") && m.getParameterCount() == 1) {
                setMethodMap.put(m.getName().substring(3), m);
            }
        }
        for (Object entity : sourceList) {
            try {
                Object resource = newListType.newInstance();
                for (Map.Entry<String,Method> entry: setMethodMap.entrySet()) {
                    Method getMethod = getMethodMap.get(entry.getKey());
                    if (getMethod != null){
                        Object value = getMethod.invoke(entity);
                        entry.getValue().invoke(resource, value);
                    }
                }
                newList.add(resource);
            } catch (Exception e) {
                throw new XyException("复制数据失败",e);
            }
        }
    }

    /**
     * 根据参数设置将多余的属性设置为null
     * @param resources DTO类
     * @param parameter 搜索参数
     */
    public void setNull(List<?> resources, AbstractQuery parameter) {
        if (parameter == null || parameter.getIncludeField().isEmpty() || resources.isEmpty()) {
            return;
        }
        List<String> fields = parameter.getIncludeField();
        List<Method> methodList = new ArrayList<>();
        for (Method method : resources.get(0).getClass().getMethods()) {
            if (method.getName().startsWith("set") && method.getParameterCount() == 1 && method.getReturnType() == Void.TYPE) {
                String name = method.getName().substring(3);
                if (fields.stream().noneMatch(item -> item.equalsIgnoreCase(name))) {
                    methodList.add(method);
                }
            }
        }
        for (Method method : methodList) {
            for (Object resource : resources) {
                try {
                    method.invoke(resource, (Object) null);
                } catch (Exception e) {
                    logger.warn("执行方法" + method.getName() + "时出错", e);
                }
            }
        }
    }

}
