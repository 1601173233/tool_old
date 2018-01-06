package hyj.tool.util;
/**
 * 
 * @Description: 字符串操作类
 * @author hyj     
 * @date 2018年1月7日 上午12:34:26  
 * @version V1.0
 */
public class StringUtil {
	/**
	 * 判断字符串是否为空
	 * @param o
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if(s == null || s.equals("")) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 判断字符串是否为非空
	 * @param o
	 * @return
	 */
	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}
	
	
}
