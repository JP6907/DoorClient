package neo.door.usermanager;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.communityfunction.pulltorefreshlistview.view.RefreshListView;
import com.example.communityfunction.tool.ToolGetFriend;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.neo.huikaimen.FindPasswordActivity;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import neo.door.inputpass.DialogWidget;
import neo.door.inputpass.LoadingDialog;
import neo.door.main.MainActivity;
import neo.door.utils.ChangeStatusBarColor;
import neo.door.utils.MyToast;
import neo.door.webutils.Config;
import neo.door.webutils.FileUtil;
import neo.door.webutils.WebUtil;

public class UserLogin extends Activity implements OnClickListener{

	private TextView headTitle, toRegister;
	private ImageButton headReturnBack;
	private EditText etUsername, etPassword;
	private Button btnLogin;
	private Button btnForget;
	private Context mContext;
	private MyToast mToast;

	private String user;
	private String password;
	
	private Dialog mDialog;
	private boolean autoLogin = false;
	private final String TAG="UserLogin";
//	// 保存简单用户信息
    private SharedPreferences preferences;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		preferences = getSharedPreferences("DOOR", MODE_PRIVATE);
		boolean isLoggedBefore=preferences.getBoolean("LOGIN", false);
		
		//判断环信还有自己的服务器之前是否登陆过没注销账号
		if (EMClient.getInstance().isLoggedInBefore()&& isLoggedBefore==true) 
		{
			// enter to main activity directly if you logged in before.
			initData();
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
//		initData();
		setContentView(R.layout.activity_login);
		
		// 设置状态栏
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		init();
		
	}
	
	private void initData()
	{
		preferences = getSharedPreferences("DOOR", MODE_PRIVATE);
        UserManager.setID(preferences.getInt("ID", -1));
        UserManager.setUsername(preferences.getString("NAME", ""));
        UserManager.setPhone(preferences.getString("PHONE", ""));
        UserManager.setLoginStatu(preferences.getBoolean("LOGIN", false));
        UserManager.setBuilding(preferences.getString("BUILDING", ""));
        UserManager.setDoorID(preferences.getString("DOORID", ""));
        UserManager.setIsReadMsg(preferences.getBoolean("readState", true));
        
        refreshCachePath();
	}

	private void init(){
		
		mContext = getApplicationContext();
		mToast = new MyToast(mContext);
		
		headTitle = (TextView)findViewById(R.id.head_other_title);
		toRegister = (TextView)findViewById(R.id.tv_to_register);
		headReturnBack = (ImageButton)findViewById(R.id.head_other_return_back);
		etUsername = (EditText)findViewById(R.id.et_login_username);
		etPassword = (EditText)findViewById(R.id.et_login_password);
		btnLogin = (Button)findViewById(R.id.btn_login);
		btnForget = (Button)this.findViewById(R.id.btn_forgetPassword);
		
		toRegister.setOnClickListener(this);
		headReturnBack.setVisibility(View.GONE);
		btnLogin.setOnClickListener(this);
		btnForget.setOnClickListener(this);
		
		headTitle.setText("登录");
	
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_login:
			if (!EaseCommonUtils.isNetWorkConnected(this)) {
				Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
				return;
			}
			user = etUsername.getText().toString();
			password = etPassword.getText().toString();
			
			if(user.equals("") || password.equals("")){
				mToast.show("用户名或密码不能为空!", Toast.LENGTH_SHORT);
			}else{
				login(user, password);
			}
			break;
			
		case R.id.tv_to_register:
			startActivityForResult(new Intent(UserLogin.this, UserRegister.class), 102);
			break;
			
		case R.id.btn_forgetPassword:
			Intent intent = new Intent();
			intent.putExtra("type", "userPassword");
			intent.setClass(UserLogin.this, FindPasswordActivity.class);
			startActivityForResult(intent,103);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 102){
			if(resultCode == 2){
				String phone = data.getStringExtra("Name");
				etUsername.setText(phone);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 登录环信
	 * @param currentUsername
	 * @param currentPassword
	 */
	private void login(String currentUsername,String currentPassword)
	{
		mDialog = LoadingDialog.createLoadDialog(UserLogin.this, "正在登录...", false);
		mDialog.show();
		
        final long start = System.currentTimeMillis();
		// call login method
		Log.d(TAG, "EMClient.getInstance().login");
		EMClient.getInstance().login(currentUsername, UserManager.getkey(), new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "login: onSuccess");

				// ** manually load all local groups and conversation
			    EMClient.getInstance().groupManager().loadAllGroups();
			    EMClient.getInstance().chatManager().loadAllConversations();

			    new LoginTask().execute(user, password);
			}

			@Override
			public void onProgress(int progress, String status) {
				Log.d(TAG, "login: onProgress");
			}

			@Override
			public void onError(final int code, final String message) {
				Log.d(TAG, "login: onError: " + code);
				if (!mDialog.isShowing()) {
					return;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						mDialog.dismiss();
						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	
	private class LoginTask extends AsyncTask<String, Void, Integer>{

		JSONArray respArray = null;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			
			Integer result = null;
			JSONArray reqArray;
			try {
				reqArray = new JSONArray();
				reqArray.put(new JSONObject().put("PHONE", params[0])
											 .put("PASSWORD", params[1]) );
				// 发送请求, 并接收服务器响应
				respArray = WebUtil.getJsonByWeb(Config.METHOD_LOGIN, reqArray);
				if(respArray != null){                            
					result = respArray.getJSONObject(0).getInt("ID");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}catch (IOException e) {
				result = -1;
				e.printStackTrace();
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
			if(!UserLogin.this.isFinishing() && mDialog.isShowing()){
				mDialog.dismiss();
			}
			
			loginComplete(result);
		}
		
		@SuppressWarnings("unchecked")
		private void loginComplete(Integer ID) {
			
			if(ID == null || ID == 0){                   // 登录失败
				mToast.show("用户名或密码错误", Toast.LENGTH_SHORT);
				EMClient.getInstance().logout(true);//环信注销账号
				return;
			}
			
			if(ID == -1){
				mToast.show("连接超时", Toast.LENGTH_SHORT);
				EMClient.getInstance().logout(true);//环信注销账号
			}else{
				try{
					// 登录成功, 保存用户信息
					UserManager.setLoginStatu(true);
					UserManager.setID(respArray.getJSONObject(0).getInt("ID"));
					UserManager.setUsername(respArray.getJSONObject(0).getString("USERNAME"));
					UserManager.setPhone(respArray.getJSONObject(0).getString("PHONE"));
					
					UserManager.setNeedRefreashOnBbs(true);//重新登录进去需要再刷新帖子和论坛
					UserManager.setNeedRefreashOnNotice(true);
					
					refreshCachePath();
					
					Thread downloadPic = new Thread() {
						public void run() {
							try {
								FileUtil.getAndSavePic(mContext, UserManager.getUsername(), respArray.getJSONObject(0).getString("PATH"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						};
					};
					downloadPic.start();    
					downloadPic.join();        // 先加载完图片

//					UserManager.setBuilding(respArray.getJSONObject(0).getString("BUILDING"));
//					UserManager.setDoorID(respArray.getJSONObject(0).getString("DOORID"));
					
					Intent intent=new Intent(UserLogin.this,MainActivity.class);
					startActivity(intent);
					
					UserConfig.getCachePath();
					
					finish();
					
					new ToolGetFriend(UserLogin.this).execute();//加载好友列表
					
					mToast.show("登录成功", Toast.LENGTH_SHORT);
					
				}catch (JSONException e) {
					// 若Building为空就异常, 在此处理
					UserManager.setBuilding("");
					UserManager.setDoorID("");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
//					Intent intent = new Intent();
//					intent.putExtra("Name", UserManager.getUsername());
//					setResult(1, intent);
//					UserLogin.this.finish();	
//					Intent intent=new Intent(UserLogin.this,MainActivity.class);
//					intent.putExtra("Name", UserManager.getUsername());
//					startActivity(intent);
//					finish();
				}
			}
		}
	}
	/**
	 * 用户登录之后更新目录名称（必须更新，不然由于它是静态变量会出现它的值没改变，还是上次的值的情况）
	 */
	private void refreshCachePath()
	{
		UserConfig.DefaultUserPath="/sdcard/Huikaimen/User/" + UserManager.getPhone();
		UserConfig.CachePath = UserConfig.DefaultUserPath + "/Cache";
		UserConfig.BBSCachePath = UserConfig.CachePath + "/BBSCache";
		UserConfig.BBSPicCachePath = UserConfig.BBSCachePath + "/Pic";
		UserConfig.NoticeCachePath = UserConfig.CachePath + "/NoticeCache";
		UserConfig.TimeCachePath = UserConfig.CachePath + "/TimeCache";
		UserConfig.FriendCachePath = UserConfig.CachePath + "/FriendCache";
		UserConfig.MyHeadPath = UserConfig.CachePath + "/MyHeadCache";
		UserConfig.MessageCachePath = UserConfig.CachePath + "/MessageCache";
	}
}
