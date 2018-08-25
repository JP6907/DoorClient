package neo.door.utils;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class ChangeStatusBarColor {
	
	/**
	 * 
	 * @param color
	 */
	public static void setStatusBarColor(Activity context, int color){
		
		// 版本大于19
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			setTranslucentStatus(context, true);
		}
		
		// 设置状态栏颜色
		SystemBarTintManager tintManager = new SystemBarTintManager(context);    // 创建状态栏管理实例
		tintManager.setStatusBarTintEnabled(true);                               // 激活状态栏设置
		tintManager.setStatusBarTintResource(color);               				 // 设置状态栏资源

	}
	
	@TargetApi(19) 
	private static void setTranslucentStatus(Activity context, boolean on) {
		Window win = context.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
}
