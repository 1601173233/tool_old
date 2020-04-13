package hyj.tool.io;

import java.io.*;

/**
 * 文件读写io
 * @since 2018年1月4日
 * @author hyj
 * @version 1.0
 */
public class TextIo {
	final String  INPUTFILENAME  = "d://his/input.xml";
	final String  OUTPUTFILENAME = "d://his/output.xml";
	
	public String input() throws Exception{
		return this.input(INPUTFILENAME);
	}
	
	/**
	 * 读取文件
	 * @param filename 文件名
	 * @return
	 * @throws Exception
	 */
	public String input(String filename) throws Exception{
		return input(filename, "UTF-8");
	}
	
	/**
	 * 读取文件
	 * @param filename 文件名
	 * @param charaSet 文件类型
	 * @return
	 * @throws Exception
	 */
	public String input(String filename, String charaSet) throws Exception{
		String result;
		StringBuffer strBuffer = new StringBuffer();
		File file = new File(filename);
		BufferedReader fileio = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charaSet));
		String tempString = null;
		
		while((tempString = fileio.readLine()) != null){
			strBuffer.append(tempString).append("\r\n");
		}
		
		result = strBuffer.toString();

		strBuffer = null;
		
		fileio.close();
		return result;
	}

	/**
	 * 输出数据到文件
	 * @param outFilePath
	 * @param data
	 */
	public static void outPut(String outFilePath, String data){
		FileWriter out = null;
		try {
			// 通过一个文件输出流，就可以写到相应的文件中
			out = new FileWriter(new File(outFilePath));
			out.append(data);
			out.flush();
		} catch (IOException e) {
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

