package neo.door.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	public static String getTime(){
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
	String time = df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��
	return time;
	}
}
