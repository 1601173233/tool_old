package hyj.tool.timer;

import java.util.Date;
/**
 * 测试性能工具
 * @since 2018年1月4日
 * @author hyj
 * @version 1.0
 */
public class StaTextTime {
	static private long start;
	static private long end;
	static private long last = 0;
	static private int time = 0 ;
	
	static public void start(){
		start = (new Date()).getTime();
		time ++;
	}

	static public void end(){
		end = (new Date()).getTime();
		last = last + end - start;
		System.out.println("\n第" + time + "次执行结束，用时为:" + (end - start));
	}
	
	static public void getLast(){
		System.out.println("\n一共"+time+"次执行结束，一共用时为:" + last);
	}
}
