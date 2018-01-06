package hyj.tool.excel;

import java.util.List;
import java.util.Map;

/**
 * Excel数据 
 * @author hyj     
 * @date 2018年1月6日 下午10:54:08  
 * @version V1.0
 */
public class ExcelMsg {
	/** 离散数据 **/
	private Map<String, Object> discreteDataMap;
	/** 列表数据 **/
	private List<Map<String, Object>> listDataMap;
	
	/**
	 * 获取离散数据
	 * @return
	 */
	public Map<String, Object> getDiscreteDataMap() {
		return discreteDataMap;
	}
	/**
	 * 设置离散数据
	 * @param discreteDataMap
	 */
	public void setDiscreteDataMap(Map<String, Object> discreteDataMap) {
		this.discreteDataMap = discreteDataMap;
	}
	/**
	 * 获取列表数据
	 * @return
	 */
	public List<Map<String, Object>> getListDataMap() {
		return listDataMap;
	}
	/**
	 * 设置列表数据
	 * @param listDataMap
	 */
	public void setListDataMap(List<Map<String, Object>> listDataMap) {
		this.listDataMap = listDataMap;
	}
	
	
}
