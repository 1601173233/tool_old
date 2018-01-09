package hyj.tool.json;

import com.google.gson.Gson;
/**
 * json工具类
 * @since 2018年1月9日
 * @author hyj
 * @version 1.0
 */
public class JsonUtil {
	/**
	 * json转String
	 * @param obj
	 * @return
	 */
	public static String jsonToString(Object obj){
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		return json;
	}
	
	
}
