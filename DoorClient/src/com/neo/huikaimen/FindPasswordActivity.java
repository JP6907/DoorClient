package com.neo.huikaimen;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import neo.door.inputpass.LoadingDialog;
import neo.door.utils.ChangeStatusBarColor;
import neo.door.utils.JudgeInput;
import neo.door.utils.MyToast;
import neo.door.webutils.Config;
import neo.door.webutils.WebUtil;
/**
 * 找回密码
 * @author Administrator
 *
 */
public class FindPasswordActivity extends Activity implements OnClickListener{
	private static final String appKey = "13890d6ae99ef";
	private static final String appSecret = "572d8f99d3abe28b7a027c620bd0a0fd";
	
	String passwordType;
	
	TextView headTitle;
	ImageButton headReturnBack;
	EditText etPhone, etPassword, etPasswordAgain,etVerifiCode;
	Button btnVerifiCode, btnFix;
	Context mContext;
	MyToast mToast;
	
	int count = 60;       // 计时
	
	String phone;
	String password;
	String passwordAgain;
	String verifiCode;
	
	Dialog mDialog;
	
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);	
		//获取修改密码的类型
		passwordType = getIntent().getStringExtra("type");
		// 设置状态栏
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
           
        init();
	}
	
	
	private void init(){
		
		// 启动短信验证sdk
    	SMSSDK.initSDK(this, appKey, appSecret);
		
		mContext = getApplicationContext();
		mToast = new MyToast(mContext);
		
		headTitle = (TextView)findViewById(R.id.head_other_title);
		headReturnBack = (ImageButton)findViewById(R.id.head_other_return_back);
		etPhone = (EditText)findViewById(R.id.et_user_phone);
		etPassword = (EditText)findViewById(R.id.et_new_password);
		etPasswordAgain=(EditText)findViewById(R.id.et_new_password_again);
		etVerifiCode = (EditText)findViewById(R.id.et_new_verifi_code);
		btnVerifiCode = (Button)findViewById(R.id.btn_new_get_verifi_code);
		btnFix = (Button)findViewById(R.id.btn_fix_password);
		
		headReturnBack.setOnClickListener(this);
		btnVerifiCode.setOnClickListener(this);
		btnFix.setOnClickListener(this);
		
		if(passwordType.equals("userPassword")){
			headTitle.setText("找回登陆密码");
		} else if(passwordType.equals("doorPassword")){
			headTitle.setText("找回开门密码");
		}

		EventHandler eventHandler = new EventHandler(){
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		
	}

	@SuppressLint("UseValueOf")
	@Override
	public void onClick(View v) {
		
		phone = etPhone.getText().toString();
		password = etPassword.getText().toString();
		passwordAgain=etPasswordAgain.getText().toString();
		verifiCode = etVerifiCode.getText().toString();
		
		switch (v.getId()) {
		case R.id.head_other_return_back:
			this.finish();
			break;		
		case R.id.btn_new_get_verifi_code:
			
			// 验证手机是否正确
			if(!JudgeInput.judgePhoneNums(phone)){
				mToast.show("手机号码输入有误！",Toast.LENGTH_SHORT);
				return;
			}
			
			// sdk请求短信验证
			SMSSDK.getVerificationCode("86", phone);
			btnVerifiCode.setClickable(false);
			btnVerifiCode.setText("重新发送("+count+")");
			countDown();
			break;
			
		case R.id.btn_fix_password:
			if(!JudgeInput.judgePhoneNums(phone)){
				mToast.show("请输入正确号码", Toast.LENGTH_SHORT);
				return;
			}
			if(!JudgeInput.judgePassword(password)){
				mToast.show("请输入至少6位的密码", Toast.LENGTH_SHORT);
				return;
			}
			if(!password.equals(passwordAgain))
			{
				etPasswordAgain.setText("");
				mToast.show("两次输入的密码不一致", Toast.LENGTH_SHORT);
				return;
			}
			if(!verifiCode.equals("")){
				// 提交验证码
				SMSSDK.submitVerificationCode("86", phone, verifiCode);
			}else{
				mToast.show("请输入验证码", Toast.LENGTH_SHORT);
			}
			break;
			
		default:
			break;
		}
	}
	
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){ 
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0x001){             // 计时
				btnVerifiCode.setText("重新发送("+count+")");
			}else if(msg.what == 0x002){       // 时间到
				btnVerifiCode.setText("获取验证码");
				btnVerifiCode.setClickable(true);
				count = 60;
			}else{
				int event = msg.arg1;
				int result = msg.arg2;
				Object data = msg.obj;
				Log.v("SMS", "result="+ result);
				Log.v("SMS", "event="+ event);
				if(result == SMSSDK.RESULT_COMPLETE){      // 操作成功
					if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){       		// 验证成功
						mToast.show("验证成功", Toast.LENGTH_SHORT);
						// 完成注册				
						new FindPasswordTask().execute(phone, password, passwordType);
						
					}else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
						mToast.show("请求验证码中...", Toast.LENGTH_SHORT);
					}
				}else if(result == SMSSDK.RESULT_ERROR){
					try {
						mToast.show("验证码错误", Toast.LENGTH_SHORT);
						etVerifiCode.setText("");
					    Throwable throwable = (Throwable) data;
					    throwable.printStackTrace();
					    JSONObject object = new JSONObject(throwable.getMessage());
					    String des = object.optString("detail");//错误描述
					    int status = object.optInt("status");//错误代码
					    if (status > 0 && !TextUtils.isEmpty(des)) {
						    Log.e("SMS", des);
						    return;
					    }
					} catch (Exception e) {
						  e.printStackTrace();
					}
				}
			}
		};
	};
	
	
	@Override
	protected void onDestroy() {
		SMSSDK.unregisterAllEventHandler();      // 注销
		super.onDestroy();
	}
	
	private class FindPasswordTask extends AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			mDialog = LoadingDialog.createLoadDialog(FindPasswordActivity.this, "请稍等...", false);
			mDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			JSONArray reqArray;
			JSONArray respArray;
			try {
				reqArray = new JSONArray();
				reqArray.put(new JSONObject().put("PHONE", params[0])
											 .put("PASSWORD", params[1])
											 .put("TYPE", params[2]));
				respArray = WebUtil.getJsonByWeb(Config.FindPasswordMethod, reqArray);
				if(respArray != null){
					result = respArray.getJSONObject(0).getString("RESULT");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}  catch (IOException e) {
				result = "timeout";
				e.printStackTrace();
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if(mDialog.isShowing()){
				mDialog.dismiss();
			}
			
			if(result == null){
				mToast.show("修改密码失败", Toast.LENGTH_SHORT);
				return;
			}
			
			if(result.equals("timeout")){
				mToast.show("连接超时", Toast.LENGTH_SHORT);
			}
			if(result.equals("true")){
				mToast.show("重新设置密码成功", Toast.LENGTH_SHORT);
				Intent intent = new Intent();
				intent.putExtra("Name", phone);
				setResult(2, intent);
				FindPasswordActivity.this.finish();
			}else if(result.equals("false")){
				mToast.show("该电话已注册, 请直接登录", Toast.LENGTH_SHORT);
			}
		}
	}
	
	/**
	 * 倒计时
	 */
	private void countDown() {
		new Thread(){
			@Override
			public void run() {
				for(; count>0; count--){
					// 更新ui
					handler.sendEmptyMessage(0x001);
					if(count<=0){
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(0x002);
			};
		}.start();
	}
}
