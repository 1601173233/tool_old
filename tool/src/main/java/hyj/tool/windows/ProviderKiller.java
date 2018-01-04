package hyj.tool.windows;

import hyj.tool.io.TextIo;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
/**
 * 杀死dubbo指定服务
 * @since 2018年1月4日
 * @author hyj
 * @version 1.0
 */
public class ProviderKiller {
	//dubbo端口号的定义语句
	private String portStr;
	
	//dubbo配置文件的名称
	private String propertiesName;
	
	/**
	 * 获取文件夹内的所有dubbo服务的端口号
	 * @throws Exception 
	 */
	public Set<Integer> getDubboPorts(File file) throws Exception{
		Set<Integer> portList = new HashSet<Integer>();
		
		TextIo textIo = new TextIo();
		
		//循环里面的所有文件
		for(File mFile : file.listFiles()){
			if(mFile.isDirectory()){
				//添加子文件夹里面的所有端口号
				portList.addAll(getDubboPorts(mFile));
			} else{
				String fileName = mFile.getName();
				
				//如果是dubbo的配置文件，那么
				if(fileName.equals(propertiesName)){
					String fileMsg = textIo.input(mFile.getAbsolutePath(), "UTF-8");
					
					try{
						portList.add(getDubboPort(fileMsg));
					}catch(Exception e){
						
					}
				}
			}
		}
		
		return portList;
	}

	/**
	 * 获取文件夹内的所有dubbo服务的地址
	 * @param filePath
	 * @return
	 */
	private Set<Integer> getDubboPorts(String filePath) throws Exception{
		File file = new File(filePath);
		return getDubboPorts(file);
	}
	
	/**
	 * 获取文件夹内的dubbo服务的地址
	 * @param filePath
	 * @return
	 */
	private Integer getDubboPort(String fileMsg){
		//寻找dubbo端口配置信息
		int start = fileMsg.indexOf(portStr);
		
		//如果存在端口配置
		if(start >= 0){
			start += portStr.length();
			
			int end = fileMsg.indexOf("\r\n", start);
			String find = "";
			
			//如果后面有回车
			if(end >= 0){
				find = fileMsg.substring(start, end);
			}else{
				find = fileMsg.substring(start);
			}
			
			Integer port = Integer.parseInt(find);
            return port;
		}
		
		return null;
	}
	
	/**
	 * 杀掉所有占用端口的进程
	 * @param filePath
	 * @throws Exception 
	 */
	public void kill(String filePath) throws Exception{
		KillServer killServer = new KillServer();
		Set<Integer> portList = getDubboPorts(filePath);
		
		killServer.setPorts(portList);
		for(Integer port : portList){
			if(port != null )
				killServer.start(port);
		}
	}
}
