package neo.door.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast消息处理
 */
public class MyToast{

	Toast mToast;
	Context mContext;
	
	/**
	 * 构造器
	 * @param context 上下文
	 */
	public MyToast(Context context){
		mContext = context;
	}
	
	/**
	 * Toast消息提示
	 * @param context 上下文
	 * @param text 文本
	 * @param duration 时间
	 */
	public void show(CharSequence text, int duration){
		if(mToast != null){
			mToast.setText(text);
			mToast.setDuration(duration);
			mToast.show();
		}else{
			mToast = Toast.makeText(mContext, text, duration);
			mToast.show();
		}
	}
}
