package neo.door.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
public class ScreenUtil 
{
	/**
	 * ��ȡ��Ļ����
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getScreenSize(Context context)
	{
		DisplayMetrics metrics=new DisplayMetrics();
		WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display=wm.getDefaultDisplay();
		display.getMetrics(metrics);
		return metrics;
	}

}
