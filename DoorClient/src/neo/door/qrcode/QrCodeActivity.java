package neo.door.qrcode;

import java.io.IOException;

import com.google.zxing.WriterException;
import com.neo.huikaimen.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QrCodeActivity extends Activity implements OnClickListener {
	
	private ImageButton btnReturnBack;
	private Button btnCreateQrCode;
	private EditText etPassword;
	private ImageView  qrCodeView;
	private TextView tvTip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code);
		
		init();
	}
	
	private void init(){
		btnReturnBack = (ImageButton)findViewById(R.id.head_other_return_back);
		btnReturnBack.setOnClickListener(this);
		btnCreateQrCode = (Button) this.findViewById(R.id.bt_qr_open_door);
		btnCreateQrCode.setOnClickListener(this);
		etPassword = (EditText) this.findViewById(R.id.qr_password);
		qrCodeView = (ImageView) this.findViewById(R.id.qr_code_imageview);
		tvTip = (TextView) this.findViewById(R.id.tv_tip);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.head_other_return_back:
				QrCodeActivity.this.finish();
				break;
			case R.id.bt_qr_open_door:
				//获取密码
				String password = etPassword.getText().toString().trim();
				if(password.length()!=6){
					Toast.makeText(QrCodeActivity.this, "请输入6位密码!", Toast.LENGTH_LONG).show();
				} else{
					new CreateQrCodeTask(QrCodeActivity.this, qrCodeView, tvTip).execute(password);
				}
				break;
			default:
				break;
		}
	}
}
