package com.zbar.lib;

import neo.door.utils.ChangeStatusBarColor;

import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ScanResult extends Activity {

	TextView resultType;
	EditText result;
	Button btnButton;
	ImageButton returnBack;
	TextView tvTitle;
	
	Context mContext;

	@SuppressLint("ResourceAsColor")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 改变状态栏
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		
		init();

	}

	String resString;

	private void init() {
		
		mContext = this.getApplicationContext();
		
		// 初始化组件
		setContentView(R.layout.activity_qr_scan_result);
		resultType = (TextView) findViewById(R.id.scan_result_hint);
		result = (EditText) findViewById(R.id.scan_result);
		btnButton = (Button) findViewById(R.id.scan_btn);
		returnBack = (ImageButton) findViewById(R.id.head_other_return_back);
		tvTitle = (TextView)findViewById(R.id.head_other_title);
		tvTitle.setText(R.string.qr_scan_open_door);
		
		Intent intent = getIntent();
		resString = intent.getStringExtra("result");        // 得到二维码信息
		System.out.println("resString:" + resString);
		if(!Utils.isDoor_id(resString)){
			Toast.makeText(mContext, "无法连接门号或门号不正确!", Toast.LENGTH_SHORT).show();
			btnButton.setEnabled(false);
			result.setText("");
		}else{
			result.setText(resString);
		}
		
		btnButton.setOnClickListener(new open_door_onClick());
		returnBack.setOnClickListener(new return_onClick());
		

	}

	// 返回
	private class return_onClick implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			ScanResult.this.finish();
		}
	}
	
	// 开门
	private class open_door_onClick implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			// 放入开门代码
			Toast.makeText(mContext, "开门成功!", Toast.LENGTH_SHORT).show();
		}
		
	}

}
