package hyj.tool.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import sun.misc.BASE64Encoder;
/**
 * 图片工具类
 * @since 2018年1月8日
 * @author hyj
 * @version 1.0
 */
public class ImageUtil {
	/** 
	 * 该方法用来将指定的文件转换成base64编码 
	 * @param path:图片路径 
	 * **/  
	public static String getImageStr(String path){  
	    //1、校验是否为空  
	    if(path==null || path.trim().length()<=0){return "";}  
	      
	    //2、校验文件是否为目录或者是否存在  
	    File picFile = new File(path);  
	    if(picFile.isDirectory() || (!picFile.exists())) return "";  
	      
	    //3、校验是否为图片  
	    try {    
	        BufferedImage image =ImageIO.read(picFile);    
	        if (image == null) {    
	            return "";  
	        }    
	    } catch(IOException ex) {   
	        ex.printStackTrace();  
	        return "";  
	    }  
	      
	    //4、转换成base64编码  
	    String imageStr = "";  
	    try {  
	        byte[] data = null;  
	        InputStream in = new FileInputStream(path);  
	        data = new byte[in.available()];  
	        in.read(data);  
	        BASE64Encoder encoder = new BASE64Encoder();  
	        imageStr = encoder.encode(data);  
	    } catch (Exception e) {  
	        imageStr="";  
	        e.printStackTrace();  
	    }  
	      
	    return imageStr;  
	}  
}
