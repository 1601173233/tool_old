package hyj.tool.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库处理类
 * @author yyy
 *
 */
public class CommDao {
	BaseDao baseDao = new BaseDao();
	
	/**
	 * 查询一条记录
	 */
	public Map<String,Object> selectOneRow(Connection conn, String sql, String order, Map<String,Object> dataMap) throws SQLException{
		//构造入参列表
		List<Object> paramsList = putMsgToList(order, dataMap);
		
		//获取第一条记录
		return baseDao.selectOneRow(conn, sql, paramsList);
	}	
	
	/**
	 * 查询某个对象
	 */
	public Object selectObject(Connection conn, String sql, String order, Map<String,Object> dataMap) throws SQLException{
		//构造入参列表
		List<Object> paramsList = putMsgToList(order, dataMap);
		
		//获取第一条记录
		return baseDao.selectObject(conn, sql, paramsList);
	}	
	
	/**
	 * 更新数据（插入、更新、删除）
	 */
	public int update(Connection conn, String sql, String order, Map<String,Object> dataMap) throws SQLException{
		//构造入参列表
		List<Object> paramsList = putMsgToList(order, dataMap);
		
		//获取多条记录
		return baseDao.update(conn, sql, paramsList);
	}
	
	/**
	 * 查询多条记录
	 */
	public List<Map<String,Object>> select(Connection conn, String sql, String order, Map<String,Object> dataMap) throws SQLException{
		//构造入参列表
		List<Object> paramsList = putMsgToList(order, dataMap);
		
		//获取多条记录
		return baseDao.select(conn, sql, paramsList);
	}
	
	/**
	 * 批量插入
	 */
	public int[] batchInsert(Connection conn, String sql, String order, List<Map<String,Object>> dataMapList) throws SQLException{
		if(dataMapList != null){
			List<List<Object>> paramsListArray = new ArrayList<List<Object>>();

			//循环构造入参列表
			for(Map<String,Object> dataMap : dataMapList){
				List<Object> paramsList = putMsgToList(order, dataMap);
				paramsListArray.add(paramsList);
			}
			
			//调用公共过程查询结果 
			return baseDao.batchInsert(conn, sql, paramsListArray);
		}else{
			return null;
		}
	}
	
	/**
	 * 把数据按照制定顺序从Map构造为List
	 * @param order    传入参数顺序，用逗号分割
	 * @param dataMap
	 * @return
	 */
	public List<Object> putMsgToList(String order,Map<String,Object> dataMap){
		List<Object> resultList = new ArrayList<Object>();
		String[] orderArray = order.replaceAll(" ", "").split(",");
		
		for(String s : orderArray){
			resultList.add(dataMap.get(s));
		}
		
		return resultList;
	}
	
}

