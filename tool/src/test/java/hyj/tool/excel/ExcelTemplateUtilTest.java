package hyj.tool.excel;

import java.util.Map;

/**
 * 
 * 根据模版语言读取excel数据 
 * @author hyj     
 * @date 2018年1月6日 下午10:55:40  
 * @version V1.0
 */
public class ExcelTemplateUtilTest {
	public static void main(String arg[]) throws Exception {
		Map<String, ExcelMsg> excelTemplate = ExcelTemplateUtil.readByTemplate("src\\main\\resources\\excel\\test.xls", "src\\main\\resources\\excel\\testTemplate.xls");
		
		System.out.println();
	}
}
