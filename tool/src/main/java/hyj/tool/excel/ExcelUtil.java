package hyj.tool.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 
 * @Description: excel工具类 
 * @author hyj     
 * @date 2018年1月5日 下午10:30:08  
 * @version V1.0
 */
public class ExcelUtil {
	public static void main(String arg[]) throws Exception {
		Workbook workbook = getWorkbook("C:\\Users\\admin\\Desktop\\test.xls");
		Sheet sheet = workbook.getSheetAt(0);
		
		String[] paramsArray = {"姓名","性别","年龄","出生","数字","浮点数","布尔类型"};
		Type[] typeArray = {Type.S,Type.S,Type.S,Type.S,Type.I,Type.F,Type.B};
		
		List<Map<String,Object>> resultList = readExcelMsg(sheet, paramsArray, typeArray, 0, 10, 100);
		
		closeWookbook(workbook);
	}
	
	/**
	 * Excel 导入类型  
	 * @author hyj     
	 * @date 2018年1月6日 上午12:00:18  
	 * @version V1.0
	 */
	public enum Type{
		S,//字符串
		F,//浮点数
		I,//整型
		D,//日期
		B,//布尔型
		NULL//空类型，默认获取字符串
	};
	
	/**
	 * 获取excel解析后的
	 * @param filePath 文件的绝对路径 
	 * @return
	 * @throws Exception
	 */
	public static Workbook getWorkbook(String filePath) throws Exception {
		//获取文件的后缀名
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());

		InputStream is = null;
		Workbook workbook = null;
		
		try {
			is = new FileInputStream(filePath);
		
			//如果是excel2003
			if (fileType.equals("xls")) {
				workbook = new HSSFWorkbook(is);
			//如果是excel2007
			} else if (fileType.equals("xlsx")) {
				workbook = new XSSFWorkbook(is);
			} else {
				throw new Exception("读取的不是excel文件");
			}
		}catch (Exception e) {
			throw e;
		}finally {
			//最后关闭数据流
			if (is != null) {
				is.close();
			}
		}
		
		return workbook;
	}
	
	/**
	 * 关闭excel文件
	 * @param workbook
	 * @throws IOException
	 */
	public static void closeWookbook(Workbook workbook) throws IOException {
		if (workbook != null) {
			workbook.close();
		}
	}
	
	/**
	 * 获取excel的数据
	 * 只能获取列表数据
	 * @param sheet excel某页文件
	 * @param paramsArray 参数的字段名
	 * @param typeArray   参数的类型 S:字符串，F:浮点数，I:整型,D:日期,B:布尔型  ,null 默认获取字符串
	 * @param startLine   开始的列数
	 * @param startRow    开始的行数
	 * @param endRow      结束的行数
	 * @return
	 * @throws Exception 
	 */
	public static List<Map<String, Object>> readExcelMsg(Sheet sheet, 
			                                             String[] paramsArray,
			                                             Type[]   typeArray,
			                                             int startLine,
			                                             int startRow,
			                                             int endRow) throws Exception{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		//最大的行数
		int rowSize = sheet.getLastRowNum();
		int endLine = paramsArray.length + startLine;
		
		//如果需要的行数比当前行数要大，那么就只取到最后一行数据
		if(rowSize < endRow ) {
			endRow = rowSize;
		}
		
		
		// 遍历行
		for (int i = startRow; i < endRow + 1; i++) {
			Map<String ,Object> map = new HashMap<String, Object>();
			resultList.add(map);
			
			Row row = sheet.getRow(i);
			//如果是空行，那么直接初始化这行数据为空行
			if (row == null) {// 略过空行
				initMapNull(map, paramsArray, 0);
				continue;
			}
			
			// 行中有多少个单元格，也就是有多少列
			int cellSize = row.getLastCellNum();
			for (int j = startLine; j < endLine; j++) {
				int index = j - startLine;
				Cell cell = row.getCell(j);
				//获取当前字段的字段名
				String params = paramsArray[index]; 
				//获取当前字段的类型
				Type type = getCellType(cell, typeArray, index);
				
				map.put(params, getCellValue(cell, type));
			}
			
			//初始化剩余的数据
			initMapNull(map, paramsArray, cellSize);
		}
		
		return resultList;
	}
	
	/**
	 * 获取格子的数据
	 * @param cell
	 * @param type 类型 S:字符串，F:浮点数，I:整型,D:日期,B:布尔型  ,null 默认获取字符串
	 * @return
	 */
	public static Object getCellValue(Cell cell, Type type) {
		 Object o = null;
		 
		//根据类型获取结果
		switch(type) {
			case S:o = getStringValue(cell);break;	//字符串
			case F:o = getDoubleValue(cell);break;  //浮点数
			case I:o = getIntValue(cell);break;     //整型
			case D:o = getDateValue(cell);break;    //日期
			case B:o = getBooleanValue(cell);break; //布尔型
			case NULL:o = getNoTypeValue(cell);break;//默认类型
		}
		
		return o;
	}
	
	/**
	 * 获取没有设置类型的cell结果
	 * @param cell
	 * @return
	 */
	public static String getNoTypeValue(Cell cell) {
		String result = null;
		
		if(cell != null) {
			try {
				result = cell.toString();
			}catch (Exception e) {
				
			}
		}
		
		return result;
	}
	
	/**
	 * 获取String类型的cell结果
	 * @param cell
	 * @return
	 */
	public static String getStringValue(Cell cell) {
		String result = null;
		
		if(cell != null) {
			try {
				result = cell.getStringCellValue();
				
				//如果结果是空的，那么尝试获取toString转为字符串
				if(result == null) {
					result = cell.toString();
				}
			}catch (Exception e) {
				
			}
		}
		
		return result;
	}
	
	/**
	 * 获取double类型的cell结果
	 * @param cell
	 * @return
	 */
	public static Double getDoubleValue(Cell cell) {
		Double result = null;

		if(cell != null) {
			try {
				result = cell.getNumericCellValue();
				
				//如果结果是空的，那么尝试获取toString转为字符串
				if(result == null) {
					result = Double.parseDouble(cell.toString());
				}
			}catch (Exception e) {
				
			}
		}
		
		return result;
	}
	
	/**
	 * 获取integer类型的cell结果
	 * @param cell
	 * @return
	 */
	public static Integer getIntValue(Cell cell) {
		Integer result = null;

		if(cell != null) {
			try {
				result = ((Double) cell.getNumericCellValue()).intValue();
				
				//如果结果是空的，那么尝试获取toString转为字符串
				if(result == null) {
					result = Integer.parseInt(cell.toString());
				}
			}catch (Exception e) {
				
			}
		}
		
		return result;
	}
	
	/**
	 * 获取Date类型的cell结果
	 * @param cell
	 * @return
	 */
	public static Date getDateValue(Cell cell) {
		Date result = null;

		if(cell != null) {
			try {
				result = cell.getDateCellValue();
			}catch (Exception e) {
				
			}
		}
		
		return result;
	}
	
	/**
	 * 获取Boolean类型的cell结果
	 * @param cell
	 * @return
	 */
	public static Boolean getBooleanValue(Cell cell) {
		Boolean result = null;

		if(cell != null) {
			try {
				result = cell.getBooleanCellValue();
				
				//如果结果是空的，那么尝试获取toString转为字符串
				if(result == null) {
					result = Boolean.parseBoolean(cell.toString());
				}
			}catch (Exception e) {
				
			}
		}
		
		return result;
	}
	
	/**
	 * 获取节点类型
	 * @param cell      节点类型
	 * @param typeArray 参数的类型 S:字符串，F:浮点数，I:整型,D:日期,B:布尔型  ,null 默认获取字符串
	 * @param index
	 * @return
	 */
	public static Type getCellType(Cell cell, Type[] typeArray, int index) {
		Type type = null;
		
		//如果存在类型，那么就设置类型的值
		if(typeArray != null && typeArray.length > index ) {
			type = typeArray[index];
		}

		//如果没有设置类型，默认为空值
		if(type == null) {
			type = Type.NULL;
		}
		
		return type;
		
	}
	
	/**
	 * 初始化map的对应参数为空
	 * @param map         参数map
	 * @param paramsArray 参数的字段名
	 * @param startIndex  开始设置为空的下标
	 * @throws Exception 
	 */
	private static void initMapNull(Map<String, Object> map, String[] paramsArray, int startIndex) throws Exception {
		//如果下标不合法，那么就报错
		if(startIndex < 0 || paramsArray.length < startIndex) {
			throw new Exception("初始化导入数据的下标越界，index：" + startIndex + ",size:" + paramsArray.length);
		}
		
		//循环初始化map对象
		for(int index = startIndex; index < paramsArray.length; index++) {
			map.put(paramsArray[index], null);
		}
		
	}
}
