package hyj.tool.dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库处理类
 * @author yyy
 *
 */
public class BaseDao {
	/**
	 * 获取数据库连接
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnect() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
	
        // XE是精简版Oracle的默认数
        String url = "jdbc:oracle:thin:@" + Contants.OralceUrl;
        
        // 用户名,系统默认的账户名
        String user = Contants.OralceUserName;
        
        // 密码
        String password = Contants.OralcePassword;
        
        // 获取连接
        Connection con = DriverManager.getConnection(url, user, password);
        
        return con;
	}
	
	/**
	 * 查询一条记录
	 * @param conn
	 * @param sql
	 * @param paramsList
	 * @return
	 * @throws SQLException 
	 */
	public Map<String,Object> selectOneRow(Connection conn, String sql, List<Object> paramsList) throws SQLException{
		List<Map<String,Object>> resultList = select(conn, sql, paramsList);
		
		//获取第一条记录
		return resultList != null ? resultList.get(0) : null;
	}
	
	/**
	 * 查询某个对象
	 * @param conn
	 * @param sql
	 * @param paramsList
	 * @return
	 * @throws SQLException 
	 */
	public Object selectObject(Connection conn, String sql, List<Object> paramsList) throws SQLException{
		Object result = null;
		
		//添加入参
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
		addMsgToPstmt(pstmt, paramsList);
		
		//进行查询操作
		ResultSet rs = pstmt.executeQuery();
		ResultSetMetaData metaData = rs.getMetaData();  
		
		int col = metaData.getColumnCount();
		
		//循环每个查询记录
		while (rs.next()) {
			result = rs.getObject(1);
		}
		
		//关闭相关对象
		rs.close();
		pstmt.close();
		return result;
	}
	
	/**
	 * 查询多条记录
	 */
	public List<Map<String,Object>> select(Connection conn, String sql, List<Object> paramsList) throws SQLException{
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//添加入参
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
		addMsgToPstmt(pstmt, paramsList);
		
		//进行查询操作
		ResultSet rs = pstmt.executeQuery();
		ResultSetMetaData metaData = rs.getMetaData();  
		
		int col = metaData.getColumnCount();
		
		//循环每个查询记录
		while (rs.next()) {
			Map<String,Object> resultMap = new HashMap<String,Object>();
			for (int i = 1; i <= col; i++) {
				resultMap.put(metaData.getColumnName(i), rs.getObject(i));
			}
			resultList.add(resultMap);
		}
		
		//关闭相关对象
		rs.close();
		pstmt.close();
		return resultList;
	}
	
	/**
	 * 更新数据（插入、更新、删除）
	 * @throws SQLException 
	 */
	public int update(Connection conn, String sql, List<Object> paramsList) throws SQLException {
		//进行更新操作
		//添加入参
	    PreparedStatement pstmt;
	    pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    addMsgToPstmt(pstmt, paramsList);
		
	    int i = pstmt.executeUpdate();
	    
		//关闭相关对象
	    pstmt.close();
	    return i;
	}
	
	/**
	 * 批量插入
	 * @throws SQLException 
	 */
	public int[] batchInsert(Connection conn, String sql, List<List<Object>> paramsListArray) throws SQLException {
		//进行更新操作
		//添加入参
	    PreparedStatement pstmt;
	    pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    
	    if(paramsListArray != null){
		    for(List paramsList : paramsListArray){
		    	addMsgToPstmt(pstmt, paramsList);
				pstmt.addBatch();
		    }
	    }
		
	    int[] i = pstmt.executeBatch();
	    
		//关闭相关对象
	    pstmt.close();
	    return i;
	}
	
	/**
	 * 把数据放到PreparedStatement
	 * @throws SQLException 
	 */
	public void addMsgToPstmt(PreparedStatement pstmt, List<Object> paramsList) throws SQLException{
		if(paramsList != null){
			for(int i = 0; i < paramsList.size();i++){
				pstmt.setObject(i + 1, paramsList.get(i));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		BaseDao baseDao = new BaseDao();
		baseDao.getConnect();
	}
	
}

