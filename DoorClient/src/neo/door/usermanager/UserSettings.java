package neo.door.usermanager;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import neo.door.inputpass.LoadingDialog;
import neo.door.inputpass.SelectPicPopupWindow;
import neo.door.main.SlidingMenuFragment;
import neo.door.utils.ChangeStatusBarColor;
import neo.door.utils.CircleImageView;
import neo.door.utils.JudgeInput;
import neo.door.utils.MyToast;
import neo.door.webutils.Config;
import neo.door.webutils.FileUtil;
import neo.door.webutils.WebUtil;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.neo.huikaimen.ChangSettingPasswordActivity;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserSettings extends Activity implements OnClickListener{
	
	private static final int REQUESTCODE_TAKE = 0;
	private static final int REQUESTCODE_PICK = 1;
	private static final int REQUESTCODE_CUTTING = 2;
	private static final String IMAGE_FILE_NAME = "headImage.jpg";
	
	private RelativeLayout item1, item2, item3, item4, item5, itemPassword;
	private ImageButton headReturnBack;
	private CircleImageView headImage;
	private TextView headTitle;
	private TextView datail1,datail2,datail3,datail4,datail5,detailPassword;
	private Button btnLoginOut;
	
	private Context mContext;
	private MyToast mToast;
	private Dialog mDialog;
	private SelectPicPopupWindow popupWindow;
	private String changeContent = "";
	
	private String fileFullPath;
	private Bitmap bitmap;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_user_settings);
		
		init();
	};
	
	@SuppressLint("ResourceAsColor")
	private void init(){
		
		// 设置状态栏
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
        mContext = getApplicationContext();
        mToast = new MyToast(mContext);
		headImage = (CircleImageView)findViewById(R.id.head_image);
		headImage.setOnClickListener(this);
		FileUtil.setHeadImage(this, headImage, UserManager.getUsername());
		item1 = (RelativeLayout)findViewById(R.id.item1);
		item1.setOnClickListener(this);
		item2 = (RelativeLayout)findViewById(R.id.item2);
		item2.setOnClickListener(this);
		item3 = (RelativeLayout)findViewById(R.id.item3);
		item3.setOnClickListener(this);
		item4 = (RelativeLayout)findViewById(R.id.item4);
		item4.setOnClickListener(this);
		item5 = (RelativeLayout)findViewById(R.id.item5);
		item5.setOnClickListener(this);
		itemPassword = (RelativeLayout)findViewById(R.id.item_doorpassword);
		itemPassword.setOnClickListener(this);
		
		datail1 = (TextView)findViewById(R.id.info_detail1);
		datail2 = (TextView)findViewById(R.id.info_detail2);
		datail3 = (TextView)findViewById(R.id.info_detail3);
		datail4 = (TextView)findViewById(R.id.info_detail4);
		datail5 = (TextView)findViewById(R.id.info_detail5);
		detailPassword = (TextView)findViewById(R.id.info_detail_password);
		datail1.setText(UserManager.getUsername());
//		datail2.setText(UserManager.get);
		datail3.setText(UserManager.getPhone());
		datail4.setText(UserManager.getBuilding());
		datail5.setText(UserManager.getDoorID());
		detailPassword.setText("******");
		
		headReturnBack = (ImageButton)findViewById(R.id.head_other_return_back);
		headReturnBack.setOnClickListener(this);
		headTitle = (TextView)findViewById(R.id.head_other_title);
		headTitle.setText("设置");
		
		btnLoginOut = (Button)findViewById(R.id.btn_loginout);
		btnLoginOut.setOnClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(this, ""+resultCode+"  "+requestCode, Toast.LENGTH_SHORT).show();
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 1){
			if(data != null){
				changeContent = data.getStringExtra("change");
				if(changeContent.equals(UserManager.getUsername())                  // 无需修改
						|| changeContent.equals(UserManager.getPhone())){
					mToast.show("无需更改", Toast.LENGTH_SHORT);
					return;
				}
			}
			
			switch (requestCode) {		
			case 101:
				new UpdateTask().execute(String.valueOf(UserManager.getID()), "Name", changeContent);
				break;
					
			case 102:
				String[] password1 = changeContent.split("-");
				if(JudgeInput.judgePassword(password1[1])) {
					new UpdateTask().execute(String.valueOf(UserManager.getID()), "Password", changeContent);
				} else {
					mToast.show("密码至少为6位", Toast.LENGTH_SHORT);
				}
				break;
					
			case 103:
				if(JudgeInput.judgePhoneNums(changeContent)) {
					new UpdateTask().execute(String.valueOf(UserManager.getID()), "Phone", changeContent);
				}else {
					mToast.show("手机号码错误", Toast.LENGTH_SHORT);
				}
				
				break;
					
			case 104:
				new UpdateTask().execute(String.valueOf(UserManager.getID()), "Building", changeContent);
				break;
					
			case 105:
				new UpdateTask().execute(String.valueOf(UserManager.getID()), "DoorID", changeContent);
				break;
				
			case 106:
				String[] password2 = changeContent.split("-");
				if(JudgeInput.judgeNewDoorPassword(password2[1])) {
				new UpdateTask().execute(String.valueOf(UserManager.getID()), "dODPass", changeContent);
				break;
			}

		}
	}
	if(resultCode == RESULT_OK){
		switch (requestCode) {	
		
		case REQUESTCODE_TAKE:
			// 获取拍照后照片的存储路径
			File temp = new File(UserConfig.getMyHeadPath()+ "/" + IMAGE_FILE_NAME);
			startPhotoZoom(Uri.fromFile(temp));
			break;
			
		case REQUESTCODE_PICK:
			try{
				startPhotoZoom(data.getData());
			} catch (NullPointerException e) {         // data为null, 点击取消
				e.printStackTrace();
			}
			break;
				
		case REQUESTCODE_CUTTING:
			if(data != null){
				setPicToView(data);
			}
			break;

		default:
			break;
		
		}
	}
		else
			Toast.makeText(this, ""+resultCode, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onClick(View v) {
		
		Intent intent = new Intent(this, ChangeSetting.class);
		Intent intentPassword = new Intent(this, ChangSettingPasswordActivity.class);
		
		switch (v.getId()) {
		
		case R.id.head_other_return_back:
			UserSettings.this.finish();
			break;
			
		case R.id.head_image:
			popupWindow = new SelectPicPopupWindow(mContext, mOnClickListener);
			popupWindow.showAtLocation(findViewById(R.id.settings_layout), 
					Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
			
		case R.id.item1:
			intent.putExtra("title", "设置昵称");
			startActivityForResult(intent, 101);
			break;
	
		case R.id.item2:
			intentPassword.putExtra("title", "设置登陆密码");
			intentPassword.putExtra("isDigits", true);//设置Editext的digits
			intentPassword.putExtra("type", "userPassword");
			startActivityForResult(intentPassword, 102);
			break;
			
		case R.id.item3:
			intent.putExtra("title", "设置电话");
			startActivityForResult(intent, 103);
			break;
			
		case R.id.item4:
			intent.putExtra("title", "设置楼栋");
			startActivityForResult(intent, 104);
			break;
			
		case R.id.item5:
			intent.putExtra("title", "设置门号");
			startActivityForResult(intent, 105);
			break;
			
		case R.id.item_doorpassword:
			intentPassword.putExtra("title", "设置开门密码");
			intentPassword.putExtra("isDigits", true);//设置Editext的digits
			intentPassword.putExtra("type", "doorPassword");
			startActivityForResult(intentPassword, 106);
			break;
			
		case R.id.btn_loginout:
			loginOut();
			break;

		default:
			break;
		}
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			popupWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				// 调用相机
				Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 指定拍照后照片的存储路径
				takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, 
						Uri.fromFile(new File(UserConfig.getMyHeadPath()+ "/", IMAGE_FILE_NAME)));
				//IMAGE_FILE_NAME 为一个临时文件，每次拍照后这个图 片都会被替换
				startActivityForResult(takeIntent, REQUESTCODE_TAKE);
				break;
				
			case R.id.btn_pick_photo:
				// 从相册选择照片, 数据保存在data
				Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
				pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(pickIntent, REQUESTCODE_PICK);
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 裁剪图片
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置开启的Intent中设置的view可裁剪
		intent.putExtra("crop", "true");
		// 设置宽高比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		// return-data:true不返回uri,false返回uri
		intent.putExtra("return-data", true);
		// 裁剪图片
		startActivityForResult(intent, REQUESTCODE_CUTTING);
		
	}
	
	
	/**
	 * 保存图片
	 * @param intent
	 */
	private void setPicToView(Intent intent){
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			// 取得SDCard图片路径做显示
			bitmap = bundle.getParcelable("data");
			// 保存图片
			fileFullPath = FileUtil.saveFile(mContext, UserManager.getPhone(), IMAGE_FILE_NAME, bitmap);
			
			mDialog = LoadingDialog.createLoadDialog(UserSettings.this, "更换头像...", false);
			mDialog.show();
			// 上传图片至服务器
			new Thread() {
				@Override
				public void run() {
					if(!fileFullPath.equals("")){
						String fileName = fileFullPath.substring(fileFullPath.lastIndexOf("/") + 1);   // 得到文件名
						if(WebUtil.uploadBySocket(mContext, UserManager.getPhone(), "headImage.jpg")){
							mHandler.sendEmptyMessage(0x001);
						}else{
							mHandler.sendEmptyMessage(0x002);
						}
					}
				};
			}.start();
		}
	}

	
	private void loginOut(){
		UserManager.setLoginStatu(false);
		UserManager.setID(-1);
		UserManager.setUsername("");
		UserManager.setPhone("");
		UserManager.setBuilding("");
		UserManager.setDoorID("");
		EMClient.getInstance().logout(true);//环信注销账号
		
		super.onDestroy();
		finish();
	}
	
	
	private class UpdateTask extends AsyncTask<String, Void, Integer>{

		JSONArray respArray = null;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			mDialog = LoadingDialog.createLoadDialog(UserSettings.this, "请稍等...", false);
			mDialog.show();
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			
			Integer result = null;
			JSONArray reqArray;
			try {
				reqArray = new JSONArray();
				reqArray.put(new JSONObject().put("ID", Integer.parseInt(params[0]))
										.put("FIELD", params[1])
										.put("CONTENT", params[2]));
				// 发送请求, 并接收服务器响应
				respArray = WebUtil.getJsonByWeb(Config.METHOD_UPDATE, reqArray);
				if(respArray != null){                            
					result = respArray.getJSONObject(0).getInt("RESULT");  // 获取返回结果
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}  catch (IOException e) {
				result = -1;
				e.printStackTrace();
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
			if(mDialog.isShowing()){
				mDialog.dismiss();
			}
			
			if(result == null){
				mToast.show("修改失败", Toast.LENGTH_SHORT);
				return;
			}
			
			switch (result) {
			
			case -1:
				mToast.show("连接超时", Toast.LENGTH_SHORT);
				break;
			
			case 1:
				datail1.setText(changeContent);
				UserManager.setUsername(changeContent);
				break;
				
			case 2:
				mToast.show("修改登陆密码成功!", Toast.LENGTH_SHORT);
				break;
				
			case 3:
				datail3.setText(changeContent);
				UserManager.setPhone(changeContent);
				break;
				
			case 4:
				datail4.setText(changeContent);
				UserManager.setBuilding(changeContent);
				break;
				
			case 5:
				datail5.setText(changeContent);
				UserManager.setDoorID(changeContent);
				break;
				
			case 6:
				mToast.show("修改开门密码成功!", Toast.LENGTH_SHORT);
				break;
				
			case 7:
				mToast.show("旧密码不正确!", Toast.LENGTH_SHORT);
				break;
				

			default:
				break;
			}
			
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			mDialog.dismiss();
			switch (msg.what) {
			case 0x001:
				mToast.show("更换头像成功", Toast.LENGTH_SHORT);
				// 设置头像
				Drawable drawable = new BitmapDrawable(null, bitmap);
				headImage.setImageDrawable(drawable);	
				break;
				
			case 0x002:
				mToast.show("更换头像失败", Toast.LENGTH_SHORT);
				break;

			default:
				break;
			}
		};
	};

}
