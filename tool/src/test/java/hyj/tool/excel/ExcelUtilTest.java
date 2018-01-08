package hyj.tool.excel;

import hyj.tool.excel.ExcelUtil.Type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
/**
 * 
 * @Description: excel工具类 
 * @author hyj     
 * @date 2018年1月5日 下午10:30:08  
 * @version V1.0
 */
public class ExcelUtilTest {
	public static void main(String arg[]) throws Exception {
	
	}
	
	public static void testRead() throws Exception{
		Workbook workbook = ExcelUtil.getWorkbook("C:\\Users\\admin\\Desktop\\test.xls");
		Sheet sheet = workbook.getSheetAt(0);
		
		String[] paramsArray = {"姓名","性别","年龄","出生","数字","浮点数","布尔类型"};
		Type[] typeArray = {Type.S,Type.S,Type.S,Type.S,Type.I,Type.F,Type.B};
		
		List<Map<String,Object>> resultList = ExcelUtil.readExcelMsg(sheet, paramsArray, typeArray, 0, 10, 100);
		
		ExcelUtil.closeWookbook(workbook);
	}
	
	public static void testWrite() throws InvalidFormatException, ParsePropertyException, IOException{
		Map<String, Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("name", "姓名1");
		paramsMap.put("sex",  "性别1");
		paramsMap.put("age",  "年龄1");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("school", "学校1");
		map.put("classes",  "班级1");
		map.put("time",   "时间1");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("school", "学校2");
		map.put("classes",  "班级2");
		map.put("time",   "时间2");
		list.add(map);
		
		paramsMap.put("list", list);
		ExcelUtil.createExcel(paramsMap, "src\\main\\resources\\excel\\export.xls", "C:\\Users\\ken\\Desktop\\1.xls");
		
	}
}
