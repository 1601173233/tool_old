package hyj.tool.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.beans.BeanMap;

import com.fasterxml.jackson.core.JsonGenerationException;

/**
 * 
 * @Description: 对象操作类
 * @author hyj
 * @date 2018年1月7日 上午12:33:59
 * @version V1.0
 */
public class ObjectUtil {
	/**
	 * 判断参数是否为空
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNull(Object o) {
		if (o == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断参数是否为非空
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNotNull(Object o) {
		return !isNull(o);
	}

	/**
	 * 将对象装换为map
	 * 
	 * @param bean
	 * @return
	 */
	public static <T> Map<String, Object> beanToMap(T bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (bean != null) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				map.put(key + "", beanMap.get(key));
			}
		}
		return map;
	}

	/**
	 * 将map装换为javabean对象
	 * 
	 * @param map
	 * @param bean
	 * @return
	 */
	public static <T> T mapToBean(Map<String, Object> map, T bean) {
		BeanMap beanMap = BeanMap.create(bean);
		beanMap.putAll(map);
		return bean; 
	}

	/**
	 * 将List<T>转换为List<Map<String, Object>>
	 * 
	 * @param objList
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		if (objList != null && objList.size() > 0) {
			Map<String, Object> map = null;
			T bean = null;
			for (int i = 0, size = objList.size(); i < size; i++) {
				bean = objList.get(i);
				map = beanToMap(bean);
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 将List<Map<String,Object>>转换为List<T>
	 * 
	 * @param maps
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps,
			Class<T> clazz) throws InstantiationException,
			IllegalAccessException {
		List<T> list = new ArrayList<T>();
		if (maps != null && maps.size() > 0) {
			Map<String, Object> map = null;
			T bean = null;
			for (int i = 0, size = maps.size(); i < size; i++) {
				map = maps.get(i);
				bean = clazz.newInstance();
				mapToBean(map, bean);
				list.add(bean);
			}
		}
		return list;
	}

}
