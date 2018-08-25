package neo.door.inputpass;

import com.neo.huikaimen.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class DialogWidget extends Dialog  {
	Activity activity;
	private View view;
	private boolean isOutSideTouch=true;

	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public boolean isOutSideTouch() {
		return isOutSideTouch;
	}
	public void setOutSideTouch(boolean isOutSideTouch) {
		this.isOutSideTouch = isOutSideTouch;
	}
	public DialogWidget(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}
	public DialogWidget(Context context) {
		this(context,0);
		// TODO Auto-generated constructor stub
	}
	public DialogWidget(Activity activity, View view) {
		super(activity,R.style.MyDialog);
		this.activity = activity;
		this.view=view;
	}
	public DialogWidget(Activity activity, View view,int theme) {
		super(activity,theme);
		this.activity = activity;
		this.view=view;
	}
	public DialogWidget(Activity activity, View view,int theme,boolean isOutSide) {
		super(activity,theme);
		this.activity = activity;
		this.view=view;
		this.isOutSideTouch=isOutSide;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(view);
		setCanceledOnTouchOutside(isOutSideTouch);
		
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeight=dm.heightPixels;
		WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
		layoutParams.width = screenWidth;
		layoutParams.height = screenHeight-120;
		this.getWindow().setAttributes(layoutParams);
		
		view.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				int height=view.findViewById(R.id.gprs_input).getHeight();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					if(y<height)
						cancel();
				}
				return true;
			}
		});
	}

}
