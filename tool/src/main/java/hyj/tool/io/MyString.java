package hyj.tool.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 快速格式化文件内容的数据
 * @since 2018年1月4日
 * @author hyj
 * @version 1.0
 */
public class MyString {
	/**
	 * 格式化字符串，并输出到外部文件
	 * @param format			格式化字符串，用${1},${2}进行字符串替换操作
	 * @param inFileNameList	读入的文件数据
	 * @param outFileName		输出文件的名字
	 * @throws Exception
	 */
	public static void formatStringToFile(String format,List<String> inFileNameList,String outFileName) throws Exception{
		formatStringToFile(format,inFileNameList,outFileName,1);
	}
	
	/**
	 * 格式化字符串，并输出到外部文件
	 * @param format			格式化字符串，用${1},${2}进行字符串替换操作
	 * @param inFileNameList	读入的文件数据
	 * @param outFileName		输出文件的名字
	 * @param everyLine			多少行输出换行符
	 * @throws Exception
	 */
	public static void formatStringToFile(String format,List<String> inFileNameList,String outFileName,int everyLine) throws Exception{
		//如果输出文件夹不存在，那么就建立
		String outDirName = outFileName.substring(0,outFileName.lastIndexOf('/'));
		File dirFile = new File(outDirName);
		
		if(!dirFile.isDirectory()){
			dirFile.mkdirs();
		}
		
		PrintWriter  out = new PrintWriter(new FileOutputStream(outFileName));
		MyString.formatString(format, inFileNameList, out, everyLine);
		out.close();
	}
	
	/**
	 * 格式化字符串，并输出到外部文件
	 * @param format			格式化字符串，用${1},${2}进行字符串替换操作
	 * @param inFileNameList	读入的文件数据
	 * @param out				文件输出流
	 * @throws Exception
	 */
	public static void formatString(String format,List<String> inFileNameList,PrintWriter out) throws Exception{
		MyString.formatString(format, inFileNameList, out, 1);
	}
	
	/**
	 * 格式化字符串，并输出到外部文件
	 * @param format			格式化字符串，用${1},${2}进行字符串替换操作
	 * @param inFileNameList	读入的文件数据
	 * @param out				文件输出流
	 * @param everyLine			多少行输出换行符
	 * @throws Exception
	 */
	public static void formatString(String format,List<String> inFileNameList,PrintWriter out,int everyLine) throws Exception{
		List<List<String>> inDataList = new ArrayList<List<String>>();
		
		int inLength = inFileNameList.size(),dataLength;
		TextIo textIo = new TextIo();
		
		//格式化读取的数据
		for(int i = 0;i < inLength;i++){
			String inString = textIo.input(inFileNameList.get(i));
			
			inString = inString.replaceAll("\r", "###").replaceAll("\n", "");

			inDataList.add(Arrays.asList(inString.split("###")));
		}
		
		dataLength = inDataList.get(0).size();
		
		for(int i = 0;i < dataLength; i++){
			String s = format;
			
			for(int j = 0;j < inLength;j++){
				s = s.replace(("${" + (j + 1) + "u}"), inDataList.get(j).get(i).toUpperCase());
				s = s.replace(("${" + (j + 1) + "}"), inDataList.get(j).get(i));
			}
			
			out.write(s);
			if(everyLine % everyLine == everyLine - 1)
				out.write("\n");
			out.flush();
		}
	}
}


