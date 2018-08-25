package com.example.communityfunction.fix;

import com.neo.huikaimen.R;

import neo.company.data.CompanyData;
import neo.door.utils.ChangeStatusBarColor;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CompanyDetail extends Activity implements android.view.View.OnClickListener{
	
	TextView tvCompanyName;
	TextView tvScore;
	TextView tvAddress;
	TextView tvPhone;	
	RelativeLayout relative2, relative3;
	LinearLayout bottom_relative1, bottom_relative2;
	
	CompanyData data;
	
	ImageButton returnBack;
	TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_detail_info);
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		initHead();
		init();
		
	}
	
	@SuppressLint("ResourceAsColor")
	private void initHead(){
		
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		
		returnBack = (ImageButton)findViewById(R.id.head_other_return_back);
		returnBack.setOnClickListener(new android.view.View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				CompanyDetail.this.finish();
			}
		});
		
		title = (TextView)findViewById(R.id.head_other_title);
		title.setText("公司信息");
	}
	
	private void init(){
		tvCompanyName = (TextView)findViewById(R.id.detail_name);
		tvScore = (TextView)findViewById(R.id.detail_score);
		tvAddress = (TextView)findViewById(R.id.detail_address);
		tvPhone = (TextView)findViewById(R.id.detail_phone);
		
		relative2 = (RelativeLayout)findViewById(R.id.relative2);
		relative3 = (RelativeLayout)findViewById(R.id.relative3);
		relative2.setOnClickListener(this);
		relative3.setOnClickListener(this);
		
		bottom_relative1 = (LinearLayout)findViewById(R.id.bottom_linear1);
		bottom_relative2 = (LinearLayout)findViewById(R.id.bottom_linear2);
		bottom_relative1.setOnClickListener(this);
		bottom_relative2.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();
		data = (CompanyData) bundle.getSerializable("DATE");
		
		tvCompanyName.setText(data.getName());
		tvScore.setText(Double.toString(data.getScore()));
		tvAddress.setText(data.getAddress());
		tvPhone.setText(data.getPhone());
	}


	@SuppressLint("InflateParams")
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.relative2:
			Toast.makeText(CompanyDetail.this, "请期待!", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.relative3:
			Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.getPhone()));
			startActivity(intent1);
			break;
			
		case R.id.bottom_linear1:
			
			final LinearLayout view = 
				(LinearLayout)getLayoutInflater().inflate(R.layout.dialog_set_score_view, null);
			
			new AlertDialog.Builder(CompanyDetail.this)
				.setTitle("评分")
				.setView(view)
				.setPositiveButton("确定", new OnClickListener() {			
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						EditText etScore = (EditText)view.findViewById(R.id.et_set_score);
						tvScore.setText(etScore.getText().toString());
						Toast.makeText(CompanyDetail.this, "评分成功!", Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("取消", null)
				.create()
				.show();
			
			break;
			
		case R.id.bottom_linear2:
			Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.getPhone()));
			startActivity(intent2);
			break;
			
		default:
			break;
		}
	}
}
