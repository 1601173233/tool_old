package hyj.tool.windows;

import hyj.tool.io.TextIo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 执行bat脚本的程序
 * 
 * @since 2017年12月28日
 * @author hyj
 * @version 1.0
 */
public class BatUtil {
	/**
	 * 删除打包文件的pause语句
	 * @param filePath 项目所在地址
	 * @param fileName 项目打包的.bat文件
	 * @throws Exception 
	 */
	private static void removePause(String filePath, String fileName) throws Exception{
		TextIo textIo = new TextIo();
		String fileMsg = textIo.input(filePath + "\\" + fileName, "GBK");
		
		PrintWriter printWriter = new PrintWriter(new File(filePath + "\\" + fileName), "GBK");
		printWriter.print(fileMsg.replaceAll("pause",""));
		printWriter.flush();
		printWriter.close();
	}

	/**
	 * 执行指定的Bat文件
	 * @param filePath 项目所在地址
	 * @param fileName 项目打包的.bat文件
	 * @throws Exception 
	 */
	public static void doBat(String filePath, String fileName) throws Exception {
		Runtime runtime = Runtime.getRuntime();
		try {
			//先删除pause的字段防止堵塞
			removePause(filePath, fileName);
			
			String dir = "cd /d" + filePath + " && "
					+ filePath + "\\" + fileName + " && exit ";
			
			// 查找进程号
			Process p = runtime.exec("cmd /k " + dir);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream(), "UTF-8"));
			
			while ((reader.readLine()) != null) {}
			
			p.waitFor();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
