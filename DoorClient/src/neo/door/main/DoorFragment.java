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

	AlarmManager aManager;                               // ���ӹ�����
	PendingIntent pIntent;                               // ����PendingIntent����
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
		aManager.cancel(pIntent);            // ȡ����ʱ����
		unregisterReceiver(mReciever);
	}
	
	private void init(){
		getActivity().setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		
		mContext = getActivity().getApplication();     // ���main.class��context
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
					mToast.show("���ȵ�¼", Toast.LENGTH_SHORT);
				}
				break;
		
			case R.id.bluetooth_open_door:
				if(UserManager.getLoginStatu()) {
					startActivity(new Intent(getActivity(), BluetoothOpenDoor.class));
				}else{
					mToast.show("���ȵ�¼", Toast.LENGTH_SHORT);
				}
				break;
	
			case R.id.sms_open_door:
				if(UserManager.getLoginStatu()) {
					startActivity(new Intent(getActivity(), SMSOpenDoor.class));
				}else{
					mToast.show("���ȵ�¼", Toast.LENGTH_SHORT);
				}
				break;
				
			case R.id.qr_scan_open_door:
				if(UserManager.getLoginStatu()) {
					startActivity(new Intent(getActivity(), QrCodeActivity.class));
				}else{
					mToast.show("���ȵ�¼", Toast.LENGTH_SHORT);
				}
				break;
				
//			case R.id.timing_open_door:		
//				if(!UserManager.getIsTiming()) {
//					timingOpenDoor();
//				}else {
//					// ȡ����ʱ����
//					new AlertDialog.Builder(getActivity())
//						.setTitle("ע��")
//						.setMessage("��ʱ����������, �Ƿ�ȡ��?")
//						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {					
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								aManager.cancel(pIntent);
//								UserManager.setIsTiming(false);
//								mToast.show("��ʱ������ȡ��!", Toast.LENGTH_SHORT);
//							}
//						})
//						.setNegativeButton("ȡ��", null)
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
		// ѡ��ʱ��
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
					if(addHour >= 0 && sum < 30 * 60 * 1000) {      // ��ʱ���ɳ��������
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
		// ���ó�ʼʱ��
		,currrenTime.get(Calendar.HOUR_OF_DAY)
		, currrenTime.get(Calendar.MINUTE)
		, true).show();
	}
	
	private View getDialogView(OnInputListener listener) {
		return InputPassPopupWindow.getInstance(mContext, listener).getView();
	}
	
	/**
	 * Gprs���ŵļ�����
	 */
	private OnInputListener gprsListener = new OnInputListener() {		
		@Override
		public void commit(String password) {
				new OpenDoorByGprsTask(mHandler).execute(UserManager.getPhone(), password);
				mDialogWidget.dismiss();
				mDialog = LoadingDialog.createLoadDialog(getActivity(), "���ڿ���...", false);
				mDialog.show();
		}
	};
	
	/**
	 * ��ʱ���ż�����
	 */
	private OnInputListener timingListener = new OnInputListener() {		
		@Override
		public void commit(String password) {
			mDialogWidget.dismiss();
			Intent intent = new Intent(RECIEVER_ACTION);
			intent.putExtra("OD_PASS", password);
			pIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);    
			// �����ڶ�Ӧ��ʱ������������
			// RTC_WAKEUP: ��ϵͳ��ʱ��ﵽ�ڶ���������ֵʱ����, С�׻��ӳ�...
			aManager.set(AlarmManager.RTC, mTimeingCalendar.getTimeInMillis(), pIntent);
			mToast.show("��ʱ�������óɹ�!", Toast.LENGTH_SHORT);
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
				mToast.show("���ųɹ�!", Toast.LENGTH_SHORT);
				break;
				
			case Config.LOGIN_FAILED:
				mToast.show("���ſ������!", Toast.LENGTH_SHORT);
				break;
				
			case Config.LOGIN_NO_NETWORK:
				mToast.show("���翪С����?!", Toast.LENGTH_SHORT);
				break;
				
			case 0x004:
				mToast.show("Ϊ��ȫ����, ��ʱ���ɳ���30����Ŷ!", Toast.LENGTH_SHORT);
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
