package hyj.tool.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import hyj.tool.excel.ExcelUtil.Type;
import hyj.tool.util.ObjectUtil;
import hyj.tool.util.StringUtil;

/**
 * 
 * 根据模版语言读取excel数据 
 * @author hyj     
 * @date 2018年1月6日 下午10:55:40  
 * @version V1.0
 */
public class ExcelTemplateUtil {
	public static void main(String arg[]) throws Exception {
		Workbook workbook = ExcelUtil.getWorkbook("C:\\Users\\admin\\Desktop\\testTemplate.xls");
		Sheet sheet = workbook.getSheetAt(0);
		
		ExcelTemplate excelTemplate = readExcelTemplate(sheet);
		
		ExcelUtil.closeWookbook(workbook);
	}
	
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
		Workbook templateWorkbook = ExcelUtil.getWorkbook(filePath);
		int sheetSize = templateWorkbook.getNumberOfSheets();
		
		//循环模板文件每一页
		for(int i = 0; i < sheetSize;i++) {
			Sheet templateSheet = templateWorkbook.getSheetAt(i);
			String sheetName    = templateSheet.getSheetName();
			Sheet dataSheet     = dataWorkbook.getSheet(sheetName);
			
			//根据模板sheet获取sheet的数据 
			ExcelMsg excelMsg = getExcelMsgByTemplateSheet(dataSheet, templateSheet);
			
			if(ObjectUtil.isNull(excelMsg)) {
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
	 */
	public static ExcelMsg getExcelMsgByTemplateSheet(Sheet dataSheet, Sheet templateSheet) {
		//解析excel模板对象
		ExcelTemplate excelTemplate = readExcelTemplate(templateSheet);
		
		//如果模板是空的，那么就返回空值
		if(excelTemplate.isEmpty()) {
			return null;
		}
		
		ExcelMsg excelMsg = new ExcelMsg();
		
		return excelMsg;
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
