package hyj.tool.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
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
 * @Description: excel模板对象类 
 * @author hyj     
 * @date 2018年1月5日 下午10:30:08  
 * @version V1.0
 */
public class ExcelTemplate {
	/** 散列模版数据映射 **/
	public List<ExcelCell> discreteCellList;
	
	/** 列表模板数据映射 **/
	public List<ExcelCell> lineCellList;
	
	/**
	 * <p>Title:ExcelTemplate.java </p>  
	 * <p>Description: 构造函数</p>
	 */
	public ExcelTemplate() {
		discreteCellList = new ArrayList<ExcelCell>();
		lineCellList = new ArrayList<ExcelCell>();
	}

	/**
	 * 获取散列模板数据映射
	 * @return
	 */
	public List<ExcelCell> getDiscreteCellList() {
		return discreteCellList;
	}

	/**
	 * 获取列表模板数据映射
	 * @return
	 */
	public List<ExcelCell> getLineCellList() {
		return lineCellList;
	};
	
	/**
	 * 添加散列映射关系数据
	 * @param excelCell
	 */
	public void addDiscreteCell(ExcelCell excelCell) {
		discreteCellList.add(excelCell);
	}
	
	/**
	 * 添加列表映射关系数据
	 * @param excelCell
	 */
	public void addLineCell(ExcelCell excelCell) {
		lineCellList.add(excelCell);
	}
	
	/**
	 * 是否为空
	 * @return
	 */
	public boolean isEmpty() {
		return discreteCellList.isEmpty() && lineCellList.isEmpty();
	}
}
