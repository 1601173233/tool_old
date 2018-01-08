package hyj.tool.excel;

import hyj.tool.excel.ExcelUtil.Type;
import hyj.tool.util.ObjectUtil;
import hyj.tool.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 
 * 根据模版语言读取excel数据 
 * @author hyj     
 * @date 2018年1月6日 下午10:55:40  
 * @version V1.0
 */
public class ExcelTemplateUtil {
	/**
	 * 通过excel模板获取数据
	 * @param filePath     excel文件地址
	 * @param templatePath excel模板地址
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, ExcelMsg>readByTemplate(String filePath, String templatePath) throws Exception {
		Map<String, ExcelMsg> resultMap = new HashMap<String, ExcelMsg>();
		//解析数据文件和模板文件
		Workbook dataWorkbook = ExcelUtil.getWorkbook(filePath);
		Workbook templateWorkbook = ExcelUtil.getWorkbook(templatePath);
		int sheetSize = templateWorkbook.getNumberOfSheets();
		
		//循环模板文件每一页
		for(int i = 0; i < sheetSize;i++) {
			Sheet templateSheet = templateWorkbook.getSheetAt(i);
			String sheetName    = templateSheet.getSheetName();
			Sheet dataSheet     = dataWorkbook.getSheet(sheetName);
			
			//根据模板sheet获取sheet的数据 
			ExcelMsg excelMsg = getExcelMsgByTemplateSheet(dataSheet, templateSheet);
			
			if(!ObjectUtil.isNull(excelMsg)) {
				resultMap.put(sheetName, excelMsg);
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 根据模板sheet获取sheet的数据 
	 * @param dataSheet     数据sheet
	 * @param templateSheet 模板sheet
	 * @return
	 * @throws Exception 
	 */
	public static ExcelMsg getExcelMsgByTemplateSheet(Sheet dataSheet, Sheet templateSheet) throws Exception {
		//解析excel模板对象
		ExcelTemplate excelTemplate = readExcelTemplate(templateSheet);
		
		//如果模板是空的，那么就返回空值
		if(excelTemplate.isEmpty()) {
			return null;
		}
		
		ExcelMsg excelMsg = new ExcelMsg();
		//读取离散数据
		excelMsg.setDiscreteDataMap(readDiscreteData(dataSheet, excelTemplate.getDiscreteCellList()));
		//读取列表数据
		excelMsg.setListDataMap(readLineData(dataSheet, excelTemplate.getLineCellList()));
		return excelMsg;
	}

	/**
	 * 读取离散数据
	 * @param sheet            数据页
	 * @param discreteCellList 散列模版数据映射
	 * @return
	 */
	public static Map<String, Object> readDiscreteData(Sheet sheet, List<ExcelCell> discreteCellList){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int rowSize = sheet.getLastRowNum() + 1;
		
		//循环模板获取数据
		for(ExcelCell excelCell : discreteCellList){
			String paramsName = excelCell.getParamsName(); //字段名称
			Type type = excelCell.getType();               //字段类型
			int rows = excelCell.getRows();                //行数
			int line = excelCell.getLine();                //列数
			
			//如果行数没有越界，那么继续，否则设置为空值
			if(rowSize > rows){
				//获取对应的行数据
				Row row = sheet.getRow(rows);
				
				//如果这行不是空的，那么继续，否则也设置为空值
				if(row != null){
					// 行中有多少个单元格，也就是有多少列
					int cellSize = row.getLastCellNum();
					
					//如果列数没有越界，那么就设置当前的值，否则也设置为空值
					if(cellSize > line){
						Cell cell = row.getCell(line);
						
						//设置值
						resultMap.put(paramsName, ExcelUtil.getCellValue(cell, type));
						continue;
					}
				}
			}
			
			//如果上面校验不通过，那么都设为空值
			resultMap.put(paramsName, null);
		}
		
		return resultMap;
	}
	
	/**
	 * 读取列表数据
	 * @param sheet            数据页
	 * @param discreteCellList 列表模版数据映射
	 * @return
	 * @throws Exception 
	 */
	public static List<Map<String, Object>> readLineData(Sheet sheet, List<ExcelCell> lineCellList) throws Exception{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		int size = lineCellList.size();
		
		//缓存一次性获取的数据
		List<String> paramsNameList = new ArrayList<String>();
		List<Type>   typeList       = new ArrayList<Type>();
		
		//循环每一个字段
		for(int i = 0; i < size;i++){
			//当前行的信息
			ExcelCell excelCell = lineCellList.get(i);
			
			//设置需要获取的字段名字和类型
			paramsNameList.add(excelCell.getParamsName());
			typeList.add(excelCell.getType());
			
		}

		//如果有结果
		if(size > 0){
			//第一行的信息
			ExcelCell excelCell = lineCellList.get(0);
			
			String[] paramsNameArray = (String [])paramsNameList.toArray(new String[paramsNameList.size()]);
			Type[]   typeArray       = (Type [])  typeList.toArray(new Type[paramsNameList.size()]);
				
			//获取列表数据
			resultList = ExcelUtil.readExcelMsg(sheet, paramsNameArray, typeArray, excelCell.getLine(), excelCell.getRows());
		}
		
		return resultList;
	}
	
	/**
	 * 读取excel模板
	 * @param sheet
	 * @return
	 */
	public static ExcelTemplate readExcelTemplate(Sheet sheet) {
		ExcelTemplate excelTemplate = new ExcelTemplate();
		
		int rowSize = sheet.getLastRowNum() + 1;
		
		// 遍历行
		for (int i = 0; i < rowSize; i++) {
			Row row = sheet.getRow(i);
			// 略过空行
			if (row == null) {
				continue;
			}

			// 行中有多少个单元格，也就是有多少列
			int cellSize = row.getLastCellNum();
			for (int j = 0; j < cellSize; j++) {
				Cell cell = row.getCell(j);
				
				//根据节点数据添加模板样式
				if(cell != null) {
					addCellMsg(cell.toString(), excelTemplate, i, j);
				}
			}
		}
		
		return excelTemplate;
	}
	
	/**
	 * 根据节点数据添加模板样式
	 * @param cellValue
	 * @param excelTemplate
	 */
	public static void addCellMsg(String cellValue, ExcelTemplate excelTemplate, int rows, int line) {
		ExcelCell excelCell = null;
		String patternStr = null;
		
		//校验是不是离散数据
		patternStr = "#\\{([a-zA-Z]*)(,type=)?([a-zA-Z]*)}";
		excelCell = validExcelCell(cellValue, patternStr, rows, line);
		
		if(excelCell != null) {
			excelTemplate.addDiscreteCell(excelCell);
		}
		
		//校验是不是列表数据
		patternStr = "\\$\\{([a-zA-Z]*)(,type=)?([a-zA-Z]*)}";
		excelCell = validExcelCell(cellValue, patternStr, rows, line);
		if(excelCell != null) {
			excelTemplate.addLineCell(excelCell);
		}
	}
	
	/**
	 * 根据节点数据构造excel单元格对象
	 * @param cellValue
	 * @return
	 */
	public static ExcelCell validExcelCell(String cellValue, String patternStr, int rows, int line) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(cellValue);

        //如果匹配了字符串
        if(matcher.find()) {
        	ExcelCell excelCell = new ExcelCell();
        	excelCell.setParamsName(matcher.group(1)); 	//设置参数名称
        	excelCell.setType(getCellType(matcher.group(3)));//设置参数的类型
        	excelCell.setRows(rows); 					//设置当前行数
        	excelCell.setLine(line); 					//设置当前列数
        	return excelCell;
        }else {
        	return null;
        }
        
	}
	
	/**
	 * 获取字段类型 
	 * @param type
	 * @return
	 */
	public static ExcelUtil.Type getCellType(String type){
		//如果是空的话
		if(StringUtil.isEmpty(type)) {
			return ExcelUtil.Type.NULL;
		}
		
		//如果是字符串
		if(type.equals("S")) {
			return ExcelUtil.Type.S;
		}
		//如果是浮点数
		if(type.equals("F")) {
			return ExcelUtil.Type.F;
		}
		//如果是整型
		if(type.equals("I")) {
			return ExcelUtil.Type.I;
		}
		//如果是日期
		if(type.equals("D")) {
			return ExcelUtil.Type.D;
		}
		//如果是布尔型
		if(type.equals("B")) {
			return ExcelUtil.Type.B;
		}
		
		return ExcelUtil.Type.NULL;
	}
}
