package com.neo.huikaimen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import neo.door.utils.MyToast;

public class ChangSettingPasswordActivity extends Activity implements OnClickListener{

	TextView title;
	EditText etOldPassword;
	EditText etNewPassword1;
	EditText etNewPassword2;
	Button btnCannel, btnOk;
	TextView tvForgetPassword;
	
	boolean isDigits;//是否设置Editext的digits
	String type;
	
	MyToast mToast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chang_setting_password);
		
		init();
		Intent intent = getIntent();
		title.setText(intent.getStringExtra("title"));
		isDigits=intent.getBooleanExtra("isDigits", false);
		type = intent.getStringExtra("type");//获取密码类型
		
		if(isDigits)//设置Editext能输入的数字
		{
			etOldPassword.setKeyListener(DigitsKeyListener.
					getInstance("0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"));
			etNewPassword1.setKeyListener(DigitsKeyListener.
					getInstance("0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"));
			etNewPassword2.setKeyListener(DigitsKeyListener.
					getInstance("0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"));
		}
	}
	
	private void init(){
		mToast = new MyToast(getApplicationContext());
		
		tvForgetPassword = (TextView)this.findViewById(R.id.tv_froget);
		tvForgetPassword.setClickable(true);
		tvForgetPassword.setOnClickListener(this);
		title = (TextView)findViewById(R.id.tv_title_password);
		etOldPassword = (EditText)findViewById(R.id.et_change_oldpassword);
		etNewPassword1 = (EditText)findViewById(R.id.et_change_newpassword1);
		etNewPassword2 = (EditText)findViewById(R.id.et_change_newpassword2);
		btnCannel = (Button)findViewById(R.id.btn_cannel_password);
		btnOk = (Button)findViewById(R.id.btn_ok_password);
		btnCannel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.btn_cannel_password:
			this.finish();
			break;
			
		case R.id.btn_ok_password:
			String oldPassword = etOldPassword.getText().toString();
			String newPassword1 = etNewPassword1.getText().toString();
			String newPassword2 = etNewPassword2.getText().toString();
			if(!newPassword1.equals(newPassword2)){
				mToast.show("两次输入的新密码不一样!", Toast.LENGTH_SHORT);
			} else{
				Intent intent = new Intent();
				intent.putExtra("change", oldPassword + "-" + newPassword1);
				setResult(1, intent);
				this.finish();
			}
			break;
		case R.id.tv_froget:
			Intent intent1 = new Intent(); 
			intent1.putExtra("type", type);
			intent1.setClass(ChangSettingPasswordActivity.this, FindPasswordActivity.class);
			startActivityForResult(intent1, 1);
			this.finish();
			//mToast.show("忘记密码", Toast.LENGTH_SHORT);
			break;
		default:
			break;
		}
	}
}
