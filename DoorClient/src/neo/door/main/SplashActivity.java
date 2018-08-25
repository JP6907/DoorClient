package neo.door.main;

import com.neo.huikaimen.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import neo.door.usermanager.UserLogin;
import neo.door.utils.ScreenUtil;

public class SplashActivity extends Activity
{
	public static final int SPLASH_DISPLAY_TIME=2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_splash);
		
		/**
		 * …Ë÷√»´∆¡
		 */
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = ScreenUtil.getScreenSize(this).widthPixels;
		lp.height = ScreenUtil.getScreenSize(this).heightPixels;
		window.setGravity(Gravity.CENTER);
		window.setAttributes(lp);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this,UserLogin.class));
				finish();
			}
		}, SPLASH_DISPLAY_TIME);
	}

}
