package neo.door.inputpass;


import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class LoadingDialog {
	
	/**
	 * �����Զ���Dialog
	 * @param context ������
	 * @param msg �Ի������ʾ
	 * @param cancelable �Ƿ����ȡ���Ի���
	 * @return �����Զ���Ի���
	 */
	@SuppressWarnings("deprecation")
	public static Dialog createLoadDialog(Context context, String msg, boolean cancelable){
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View loadView = inflater.inflate(R.layout.dialog_loading, null);     
		
		LinearLayout layout = (LinearLayout)loadView.findViewById(R.id.dialog_view);
		ImageView image = (ImageView)loadView.findViewById(R.id.loading_image);
		TextView tip = (TextView)loadView.findViewById(R.id.tipTextView);
		// ���ض���
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_animation); 
		// ʹ��imageView��ʾ����
		image.startAnimation(anim);
		// ������ʾ����
		tip.setText(msg);
		
		// �����Զ���Dialog
		Dialog dialog = new Dialog(context, R.style.loading_dialog);
		dialog.setCancelable(cancelable);         						   // �Ƿ��ܰ����ؼ�ȡ��
		dialog.setContentView(layout, new LinearLayout.LayoutParams(   // ���ò���
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		
		return dialog;
	}
	
	/**
	 * ����ʱ��,�Զ��ر�dialog
	 * @param closetime �ر�ʱ��
	 */
	public static void closeDialog(long closetime, final Dialog dialog){
		
		new Handler(){
			@SuppressLint("HandlerLeak")
			@Override
			public void handleMessage(Message msg) {
				if(dialog.isShowing()){
					dialog.dismiss();
				}
			};
		}.sendEmptyMessageDelayed(0, closetime);
	}
	
}
