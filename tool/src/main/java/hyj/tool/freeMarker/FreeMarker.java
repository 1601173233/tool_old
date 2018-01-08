package hyj.tool.freeMarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import hyj.tool.image.ImageUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FreeMarker工具类
 * 
 * @since 2018年1月8日
 * @author hyj
 * @version 1.0
 */
public class FreeMarker {
	public static void main(String arg[]){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("name", "姓名");
		dataMap.put("sex",  "年龄");
		dataMap.put("image1", ImageUtil.getImageStr("C:\\Users\\ken\\Desktop\\22.jpg"));
		dataMap.put("image2", ImageUtil.getImageStr("C:\\Users\\ken\\Desktop\\11.jpg"));
		
		List<String> list = new ArrayList<String>();
		list.add("班级1");
		list.add("班级2");
		list.add("班级3");
		
		fprint("doc2.xml", dataMap, "C:\\Users\\ken\\Desktop\\111.doc");
	}
	
	/**
	 * 获取模板
	 * 
	 * @param name
	 * @return
	 */
	public static Template getTemplate(String name) {
		try {
			// 通过Freemaker的Configuration读取相应的ftl
			Configuration cfg = new Configuration();
			// 设定去哪里读取相应的ftl模板文件
			cfg.setDirectoryForTemplateLoading(new File("src/main/resources/freeMarkerTemplate"));
			
			// 在模板文件目录中找到名称为name的文件
			Template temp = cfg.getTemplate(name);
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 输出到文件
	 * 
	 * @param name
	 * @param dataMap
	 * @param outFile
	 */
	public static void fprint(String name, Map<String, Object> dataMap, String outFilePath) {
		FileWriter out = null;
		try {
			// 通过一个文件输出流，就可以写到相应的文件中
			out = new FileWriter(new File(outFilePath));
			Template temp = getTemplate(name);
			temp.process(dataMap, out);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
