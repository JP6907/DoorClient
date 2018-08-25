package neo.door.main;

import java.util.Calendar;

import com.neo.huikaimen.R;
import com.zbar.lib.CaptureActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import neo.bluetooth.BluetoothOpenDoor;
import neo.door.inputpass.DialogWidget;
import neo.door.inputpass.InputPassPopupWindow;
import neo.door.inputpass.InputPassPopupWindow.OnInputListener;
import neo.door.qrcode.QrCodeActivity;
import neo.door.inputpass.LoadingDialog;
import neo.door.usermanager.UserManager;
import neo.door.utils.MyToast;
import neo.door.webutils.Config;
import neo.door.webutils.OpenDoorByGprsTask;
import neo.sms.SMSOpenDoor;

public class DoorFragment extends Fragment implements OnClickListener{
	
	private static final String RECIEVER_ACTION = "neo.intent.action.TimingOpenDoorReceiver";
	private static TimingOpenDoorReceiver mReciever;
	
	RelativeLayout  btnBluetoothOpenDoor;
	RelativeLayout btnSmsOpenDoor;
	RelativeLayout btnQR_scanOpenDoor;
	RelativeLayout btnGPRSOpenDoor;

	AlarmManager aManager;                               // 闹钟管理器
	PendingIntent pIntent;                               // 创建PendingIntent对象
	private Calendar mTimeingCalendar;
	
	private Context mContext;
	private static MyToast mToast;
	private DialogWidget mDialogWidget;
	public static Dialog mDialog;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){			
		return inflater.inflate(R.layout.fragment_open_door, container, false);
	}
	
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);	
		init();
		initReciever();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		aManager.cancel(pIntent);            // 取消定时任务
		unregisterReceiver(mReciever);
	}
	
	private void init(){
		getActivity().setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		
		mContext = getActivity().getApplication();     // 获得main.class的context
		mToast = new MyToast(mContext);
		aManager = (AlarmManager)getActivity().getSystemService(Service.ALARM_SERVICE);	

		btnBluetoothOpenDoor = (RelativeLayout)getActivity().findViewById(R.id.bluetooth_open_door);
		btnSmsOpenDoor = (RelativeLayout)getActivity().findViewById(R.id.sms_open_door);
		btnQR_scanOpenDoor = (RelativeLayout)getActivity().findViewById(R.id.qr_scan_open_door);
		btnGPRSOpenDoor = (RelativeLayout)getActivity().findViewById(R.id.gprs_open_door);
		
		btnBluetoothOpenDoor.setOnClickListener(this);
		btnSmsOpenDoor.setOnClickListener(this);
		btnQR_scanOpenDoor.setOnClickListener(this);
		btnGPRSOpenDoor.setOnClickListener(this);
	}
	
	private void initReciever() {
		mReciever = new TimingOpenDoorReceiver();
		IntentFilter filter = new IntentFilter(RECIEVER_ACTION);
		getActivity().registerReceiver(mReciever, filter);
		System.out.println("registerReceiver");
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		
			case R.id.gprs_open_door:
				if(UserManager.getLoginStatu()) {
					mDialogWidget = new DialogWidget(getActivity(), getDialogView(gprsListener));
					mDialogWidget.show();
				}else {
					mToast.show("请先登录", Toast.LENGTH_SHORT);
				}
				break;
		
			case R.id.bluetooth_open_door:
				if(UserManager.getLoginStatu()) {
					startActivity(new Intent(getActivity(), BluetoothOpenDoor.class));
				}else{
					mToast.show("请先登录", Toast.LENGTH_SHORT);
				}
				break;
	
			case R.id.sms_open_door:
				if(UserManager.getLoginStatu()) {
					startActivity(new Intent(getActivity(), SMSOpenDoor.class));
				}else{
					mToast.show("请先登录", Toast.LENGTH_SHORT);
				}
				break;
				
			case R.id.qr_scan_open_door:
				if(UserManager.getLoginStatu()) {
					startActivity(new Intent(getActivity(), QrCodeActivity.class));
				}else{
					mToast.show("请先登录", Toast.LENGTH_SHORT);
				}
				break;
				
//			case R.id.timing_open_door:		
//				if(!UserManager.getIsTiming()) {
//					timingOpenDoor();
//				}else {
//					// 取消定时闹钟
//					new AlertDialog.Builder(getActivity())
//						.setTitle("注意")
//						.setMessage("定时任务已设置, 是否取消?")
//						.setPositiveButton("确定", new DialogInterface.OnClickListener() {					
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								aManager.cancel(pIntent);
//								UserManager.setIsTiming(false);
//								mToast.show("定时任务已取消!", Toast.LENGTH_SHORT);
//							}
//						})
//						.setNegativeButton("取消", null)
//						.create()
//						.show();
//				}				
//				break;
//				
			default:
				break;
		}
	}
	
	
	private void timingOpenDoor() {
		final Calendar currrenTime = Calendar.getInstance();
		// 选择时间
		new TimePickerDialog(getActivity(), 0,	
			new TimePickerDialog.OnTimeSetListener() {			
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {		
					Calendar setTime = Calendar.getInstance();
					System.out.println("setTime:"+setTime.getTimeInMillis());
					int addHour = hourOfDay - setTime.get(Calendar.HOUR_OF_DAY);
					int addMinute = minute - setTime.get(Calendar.MINUTE);
					System.out.println("hour:" + addHour);
					System.out.println("minute:" + addMinute);
					int sum = addHour * 60 * 60 * 1000 + addMinute * 60 * 1000;
					if(addHour == 0 && addMinute < 0) {
						mHandler.sendEmptyMessage(0x004); 
						return;
					}
					if(addHour >= 0 && sum < 30 * 60 * 1000) {      // 定时不可超过半个钟
						setTime.add(Calendar.HOUR_OF_DAY, addHour);
						setTime.add(Calendar.MINUTE, addMinute);
						mTimeingCalendar = setTime;
						mDialogWidget = new DialogWidget(getActivity(), getDialogView(timingListener));
						mDialogWidget.show();
					}else {
						mHandler.sendEmptyMessage(0x004); 
					}
					System.out.println("setTime:"+setTime.getTimeInMillis());
				}
			}
		// 设置初始时间
		,currrenTime.get(Calendar.HOUR_OF_DAY)
		, currrenTime.get(Calendar.MINUTE)
		, true).show();
	}
	
	private View getDialogView(OnInputListener listener) {
		return InputPassPopupWindow.getInstance(mContext, listener).getView();
	}
	
	/**
	 * Gprs开门的监听器
	 */
	private OnInputListener gprsListener = new OnInputListener() {		
		@Override
		public void commit(String password) {
				new OpenDoorByGprsTask(mHandler).execute(UserManager.getPhone(), password);
				mDialogWidget.dismiss();
				mDialog = LoadingDialog.createLoadDialog(getActivity(), "正在开门...", false);
				mDialog.show();
		}
	};
	
	/**
	 * 定时开门监听器
	 */
	private OnInputListener timingListener = new OnInputListener() {		
		@Override
		public void commit(String password) {
			mDialogWidget.dismiss();
			Intent intent = new Intent(RECIEVER_ACTION);
			intent.putExtra("OD_PASS", password);
			pIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);    
			// 设置在对应的时间里启动服务
			// RTC_WAKEUP: 当系统的时间达到第二参数的数值时启动, 小米会延迟...
			aManager.set(AlarmManager.RTC, mTimeingCalendar.getTimeInMillis(), pIntent);
			mToast.show("定时任务设置成功!", Toast.LENGTH_SHORT);
			UserManager.setIsTiming(true); 
		}
	};
	
	
	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if(mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			UserManager.setIsTiming(false);
			switch (msg.what) {
			case Config.LOGIN_SUCCEED:
				mToast.show("开门成功!", Toast.LENGTH_SHORT);
				break;
				
			case Config.LOGIN_FAILED:
				mToast.show("开门口令错误!", Toast.LENGTH_SHORT);
				break;
				
			case Config.LOGIN_NO_NETWORK:
				mToast.show("网络开小差了?!", Toast.LENGTH_SHORT);
				break;
				
			case 0x004:
				mToast.show("为安全考虑, 定时不可超过30分钟哦!", Toast.LENGTH_SHORT);
				break;

			default:
				break;
			}
		};
	};
	
	
	private class TimingOpenDoorReceiver extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("MyReceiver", "*************onReceive**********");		
			String pass = intent.getStringExtra("OD_PASS");
			System.out.println("****pass"+pass);
			new OpenDoorByGprsTask(mHandler).execute(UserManager.getUsername(), pass);
		}	
	}

	private void unregisterReceiver(TimingOpenDoorReceiver mReciever) {
		getActivity().unregisterReceiver(mReciever);
		System.out.println("unregisterReceiver");
	}
}
