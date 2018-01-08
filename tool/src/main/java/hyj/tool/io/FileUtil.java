package hyj.tool.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
/**
 * 文件操作类
 * @since 2018年1月4日
 * @author hyj
 * @version 1.0
 */
public class FileUtil {
	/**
	 * 根据文件名获取文件
	 */
	public static File[] getByFileName(File file, final String fileName){
		File[] fileList = file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().equals(fileName)) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		return fileList;
	}
	/**
	 * 根据文件前缀获取文件
	 */
	public static File[] getByFileNameStarts(File file, final String fileName){
		File[] fileList = file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().startsWith(fileName)) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		return fileList;
	}

	
	/**
	 * 根据文件后缀获取文件
	 */
	public static File[] getByFileNameEnd(File file, final String fileName){
		File[] fileList = file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(fileName)) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		return fileList;
	}
	
	/**
	 * 复制文件
	 * @param f1
	 * @param f2
	 * @return
	 * @throws Exception
	 */
	public static void copyFile(File f1, File f2) throws Exception {
		int length=2097152;
        FileInputStream in=new FileInputStream(f1);
        FileOutputStream out=new FileOutputStream(f2);
        FileChannel inC=in.getChannel();
        FileChannel outC=out.getChannel();
        while(true){
            if(inC.position()==inC.size()){
                inC.close();
                outC.close();
                return ;
            }
            if((inC.size() - inC.position())<20971520)
                length=(int)(inC.size()-inC.position());
            else
                length=20971520;
            inC.transferTo(inC.position(),length,outC);
            inC.position(inC.position()+length);
        }
	}
}
