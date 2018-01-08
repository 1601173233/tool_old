package hyj.tool.windows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类用于删除指定端口的服务
 * @since 2017年12月27日
 * @author hyj
 * @version 1.0
 */
public class KillServer {
    private Set<Integer> ports;
    
    public void start(int port){
        Runtime runtime = Runtime.getRuntime();
        try {
            //查找进程号
            Process p = runtime.exec("cmd /c netstat -ano | findstr \""+port+"\"");
            InputStream inputStream = p.getInputStream();
            
            List<String> read = read(inputStream, "UTF-8");
            
            //如果找到就干掉进程
            if(read.size() != 0){
                kill(read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    /**
     * 获取所有的父进程pid
     * @param pids 当前进程pid
     * @return
     */
    public Set<Integer> getAllParentPid(Set<Integer> pids){
    	Set<Integer> parentPids = new HashSet<>();
    	
    	Runtime runtime = Runtime.getRuntime();
        
        for(Integer pid : pids){
	        try {
	            //查找进程号
	            Process p = runtime.exec("cmd /k wmic process where ProcessId=" + pid + " get ParentProcessId && exit");
	            InputStream inputStream = p.getInputStream();
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	            String line;
	            Pattern pattern = Pattern.compile("^[0-9]+");
	            
	            while((line = reader.readLine()) != null){
	                Matcher matcher = pattern.matcher(line);

	                if(matcher.find()){
	                	String find = matcher.group();
	                	Integer parentPid = Integer.parseInt(find);
	                	parentPids.add(parentPid);
	                }
	            }
	            
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } 
        }
        
        return parentPids;
    }
    
    /**
     * 验证此行是否为指定的端口，因为 findstr命令会是把包含的找出来，例如查找80端口，但是会把8099查找出来
     * @param str
     * @return
     */
    private boolean validPort(String str){
        Pattern pattern = Pattern.compile("^ *[a-zA-Z]+ +\\S+");
        Matcher matcher = pattern.matcher(str);

        matcher.find();
        String find = matcher.group();
        int spstart = find.lastIndexOf(":");
        find = find.substring(spstart + 1);
        
        int port = 0;
        try {
            port = Integer.parseInt(find);
        } catch (NumberFormatException e) {
            System.out.println("查找到错误的端口:" + find);
            return false;
        }
        
        if(this.ports.contains(port)){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * 更换为一个Set，去掉重复的pid值
     * @param data
     */
    public void kill(List<String> data){
        Set<Integer> pids = new HashSet<>();
        for (String line : data) {
            int offset = line.lastIndexOf(" ");
            String spid = line.substring(offset);
            spid = spid.replaceAll(" ", "");
            int pid = 0;
            try {
                pid = Integer.parseInt(spid);
            } catch (NumberFormatException e) {
                System.out.println("获取的进程号错误:" + spid);
            }
            pids.add(pid);
        }
        
        //获取所有父节点pid
        Set<Integer> parentPids = getAllParentPid(pids);
        
        
        System.out.println("开始杀死子进程!!");
        //杀掉所有进程
        killWithPid(pids);

        System.out.println("开始杀死父进程!!");
        //杀掉所有父进程
        killWithPid(parentPids);
    }
    
    /**
     * 一次性杀除所有的端口
     * @param pids
     */
    public void killWithPid(Set<Integer> pids){
        for (Integer pid : pids) {
            try {
                Process process = Runtime.getRuntime().exec("taskkill /F /pid "+pid+"");
                InputStream inputStream = process.getInputStream();
                String txt = readTxt(inputStream, "GBK");
                System.out.println(txt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 获取进程返回信息
     * @param in
     * @param charset
     * @return
     * @throws IOException
     */
    private List<String> read(InputStream in,String charset) throws IOException{
        List<String> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        String line;
        while((line = reader.readLine()) != null){
            boolean validPort = validPort(line);
            if(validPort){
                data.add(line);
            }
        }
        reader.close();
        return data;
    }
    
    /**
     * 获取进程处理结果
     * @param in
     * @param charset
     * @return
     * @throws IOException
     */
    public String readTxt(InputStream in,String charset) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        StringBuffer sb = new StringBuffer();
        String line;
        while((line = reader.readLine()) != null){
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

	public void setPorts(Set<Integer> ports) {
		this.ports = ports;
	}
}