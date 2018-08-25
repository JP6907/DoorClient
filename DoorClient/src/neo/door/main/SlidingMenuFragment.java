package neo.door.main;

import java.io.File;

import neo.door.inputpass.LoadingDialog;
import neo.door.usermanager.UserLogin;
import neo.door.usermanager.UserManager;
import neo.door.usermanager.UserSettings;
import neo.door.utils.CircleImageView;
import neo.door.utils.CleanUtils;
import neo.door.utils.MyToast;
import neo.door.webutils.FileUtil;

import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SlidingMenuFragment extends Fragment implements OnClickListener{
	
	CircleImageView headImage;
	private TextView tvUsername;
	private TextView exit;
	private TextView userSet;
	LinearLayout smMsg, smSettings;
	private Dialog mDialog;
	private MyToast mToast;
	private String TAG="SlidingMenuFragment";
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.slidingmenu_user_info, null);
		init(view);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		FileUtil.setHeadImage(getActivity(), headImage, UserManager.getUsername());
		if(UserManager.getLoginStatu()){
			// 登录相关设置
			if(!UserManager.getUsername().equals("")){
				tvUsername.setText(UserManager.getUsername());	
			}else{
				tvUsername.setText(UserManager.getPhone());	
			}
		}else{
			startActivity(new Intent(getActivity(),UserLogin.class));
			getActivity().finish();
		}
	}
	
	private void init(View view){
		headImage = (CircleImageView)view.findViewById(R.id.head_image);
		tvUsername = (TextView)view.findViewById(R.id.tv_username);
		smMsg = (LinearLayout)view.findViewById(R.id.sm_msg);
		smSettings = (LinearLayout)view.findViewById(R.id.sm_clear);
		exit=(TextView)view.findViewById(R.id.quit);
		userSet=(TextView)view.findViewById(R.id.set);
		
		smMsg.setOnClickListener(this);
		smSettings.setOnClickListener(this);
		headImage.setOnClickListener(this);
		exit.setOnClickListener(this);
		userSet.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.sm_msg:        //没有break，那么它会执行下面的代码
		case R.id.head_image:
			if(UserManager.getLoginStatu()){
				startActivity(new Intent(getActivity(), UserSettings.class));
			}else{
				Intent intent = new Intent(getActivity(), UserLogin.class);
				startActivityForResult(intent, 101);
			}
			break;
				
		case R.id.sm_clear:
			new AlertDialog.Builder(getActivity())
				.setTitle("注意!")
				.setMessage("确定要清空缓存吗? 头像与聊天记录都将被清除!")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						File file = new File(getActivity().getExternalCacheDir().getPath());
						if(file.exists()) {
							mDialog = LoadingDialog.createLoadDialog(getActivity(), "正在清理...", false);
							mDialog.show();
							if(CleanUtils.cleanExternalCache(getActivity())) {
								Toast.makeText(getActivity().getApplicationContext(), "清除完成", Toast.LENGTH_SHORT).show();
							}
							mDialog.dismiss();
							
						}
					}
				})
				.setNegativeButton("取消", null)
				.create()
				.show();
			break;
			
		case R.id.quit:
			getActivity().finish();
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if(requestCode == 101){
			if(resultCode == 1){
				String name = data.getStringExtra("Name");
				tvUsername.setText(name);
				Log.e(TAG, "hhhhhh");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
