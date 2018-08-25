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
	 * 创建自定义Dialog
	 * @param context 上下文
	 * @param msg 对话框的提示
	 * @param cancelable 是否可以取消对话框
	 * @return 返回自定义对话框
	 */
	@SuppressWarnings("deprecation")
	public static Dialog createLoadDialog(Context context, String msg, boolean cancelable){
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View loadView = inflater.inflate(R.layout.dialog_loading, null);     
		
		LinearLayout layout = (LinearLayout)loadView.findViewById(R.id.dialog_view);
		ImageView image = (ImageView)loadView.findViewById(R.id.loading_image);
		TextView tip = (TextView)loadView.findViewById(R.id.tipTextView);
		// 加载动画
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_animation); 
		// 使用imageView显示动画
		image.startAnimation(anim);
		// 设置提示内容
		tip.setText(msg);
		
		// 创建自定义Dialog
		Dialog dialog = new Dialog(context, R.style.loading_dialog);
		dialog.setCancelable(cancelable);         						   // 是否能按返回键取消
		dialog.setContentView(layout, new LinearLayout.LayoutParams(   // 设置布局
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		
		return dialog;
	}
	
	/**
	 * 设置时间,自动关闭dialog
	 * @param closetime 关闭时间
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
