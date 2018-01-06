package hyj.tool.util;
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
	 * @param o
	 * @return
	 */
	public static boolean isNull(Object o) {
		if(o == null) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 判断参数是否为非空
	 * @param o
	 * @return
	 */
	public static boolean isNotNull(Object o) {
		return !isNull(o);
	}
	
	
}
