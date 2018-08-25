package neo.door.inputpass;


import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.PopupWindow;


/**
 * 自定义popupWindow
 * @author leo
 *
 */
public class SelectPicPopupWindow extends PopupWindow
{

	private Button takePhotoBtn, pickPhotoBtn, cancelBtn;
	private View popupView;
	
	/**
	 * 
	 * @param context
	 * @param itemsOnClickListener 传入监听�?,在其他地方处理点击事�?,get!
	 */
	@SuppressLint("InflateParams")
	public SelectPicPopupWindow(Context context, OnClickListener onClickListener){
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		popupView = inflater.inflate(R.layout.popup_window, null);
		takePhotoBtn = (Button)popupView.findViewById(R.id.btn_take_photo);
		pickPhotoBtn = (Button)popupView.findViewById(R.id.btn_pick_photo);
		cancelBtn = (Button)popupView.findViewById(R.id.btn_cannel);
		takePhotoBtn.setOnClickListener(onClickListener);
		pickPhotoBtn.setOnClickListener(onClickListener);
		cancelBtn.setOnClickListener(onClickListener);
		
		// 设置弹窗的view
		this.setContentView(popupView);
		// 设置弹窗宽度
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置弹窗高度
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置弹窗可点�?
		this.setFocusable(true);
		// 设置弹窗动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置弹窗背景
		this.setBackgroundDrawable(dw);
		// 点击弹窗外�??�?
		popupView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = popupView.findViewById(R.id.popup_layout).getHeight();
				int y = (int) event.getY();
				if(event.getAction() == MotionEvent.ACTION_UP){
					if(y<height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}
}
