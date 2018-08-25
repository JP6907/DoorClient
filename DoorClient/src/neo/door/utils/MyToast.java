package neo.door.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast��Ϣ����
 */
public class MyToast{

	Toast mToast;
	Context mContext;
	
	/**
	 * ������
	 * @param context ������
	 */
	public MyToast(Context context){
		mContext = context;
	}
	
	/**
	 * Toast��Ϣ��ʾ
	 * @param context ������
	 * @param text �ı�
	 * @param duration ʱ��
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
