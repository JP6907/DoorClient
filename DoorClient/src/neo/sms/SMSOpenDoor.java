package neo.sms;

import neo.door.inputpass.LoadingDialog;
import neo.door.usermanager.UserManager;
import neo.door.utils.ChangeStatusBarColor;

import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
@SuppressWarnings("deprecation")
public class SMSOpenDoor extends Activity implements OnClickListener{

	private SmsManager smsManager;
	private Button send;
	private ImageButton returnBack;
	private TextView tvTitle; 
	private EditText phoneNum, content;
	private Context mContext;
	
	Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_open_door);
		
		

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		// ����״̬��
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		
		init();
		
	}
	
	/**
	 * ��ʼ��
	 */
	private void init(){
		smsManager = SmsManager.getDefault();
		mContext = this.getApplicationContext();
		
		send = (Button)findViewById(R.id.send);
		phoneNum = (EditText)findViewById(R.id.phone);
		content = (EditText)findViewById(R.id.content);
		
		returnBack = (ImageButton)findViewById(R.id.head_other_return_back);
		tvTitle = (TextView)findViewById(R.id.head_other_title);
		tvTitle.setText("���ſ���");
		
		send.setOnClickListener(this);
		returnBack.setOnClickListener(this);
	}
	
	
	@SuppressLint("HandlerLeak")
	private Handler MyHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			
			case 0x000:	
				dialog.dismiss();
				send.setEnabled(true);
				content.setText("");
				Toast.makeText(mContext, "����ͳɹ�!", Toast.LENGTH_SHORT).show();
				break;
				
			case 0x001:
				Toast.makeText(mContext, "������4-6λ���������ĵ����ֻ��ַ�!", Toast.LENGTH_SHORT).show();
				break;

			case 0x002:
				Toast.makeText(mContext, "���벻��Ϊ��!", Toast.LENGTH_SHORT).show();
				break;
				
			case 0x003:
				Toast.makeText(mContext, "���벻����!", Toast.LENGTH_SHORT).show();
				break;			
			
			default:
				break;
			}
		}
	};

	@SuppressLint("UnlocalizedSms")
	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()) {
		
			case R.id.send:
				// *****************�ȰѺ���̶�, ������************************************************
//				String phone = phoneNum.getText().toString();
				String input = content.getText().toString();
				
//				if(Check.isPhoneNumber(phone)){        // �жϺ����Ƿ����	
					
					if(!(input.isEmpty()))
					{            // �����Ƿ�Ϊ��

						if(Check.isLegal(input))
						{      // �����Ƿ�Ϸ�
							
							// �����Ի���
							dialog = LoadingDialog.createLoadDialog(SMSOpenDoor.this, "���ŷ�����...", false);
							dialog.show();
							
							// ���Ͷ���
							String message = UserManager.getPhone() + input;
							PendingIntent piIntent = PendingIntent.getActivity(mContext, 0, new Intent(), 0);
							smsManager.sendTextMessage("13553409503",                      // �̶�
									null, message, piIntent, null);
							
							send.setEnabled(false);
			
							// ��ʱ4��
							MyHandler.sendEmptyMessageDelayed(0x000, 4000);
							
						}
						else
						{
							MyHandler.sendEmptyMessage(0x001);
						}
					}
					else
					{
						MyHandler.sendEmptyMessage(0x002);
					}
//				}else {
//					MyHandler.sendEmptyMessage(0x003);
//				}
			
				break;
				
			case R.id.head_other_return_back:
				SMSOpenDoor.this.finish();     // ����
	
			default:
				break;
		}
	}
	
}
