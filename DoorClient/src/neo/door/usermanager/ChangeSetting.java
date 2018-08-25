package neo.door.usermanager;

import neo.door.utils.MyToast;

import com.neo.huikaimen.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeSetting extends Activity implements OnClickListener{
	
	
	TextView title;
	EditText etChange;
	Button btnCannel, btnOk;
	
	boolean isDigits;//是否设置Editext的digits
	
	MyToast mToast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_setting);
		
		init();
		Intent intent = getIntent();
		title.setText(intent.getStringExtra("title"));
		isDigits=intent.getBooleanExtra("isDigits", false);
		
		if(isDigits)//设置Editext能输入的数字
			etChange.setKeyListener(DigitsKeyListener.
					getInstance("0123456789qwertyuiopasdfghjklxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"));
	}
	
	private void init(){
		mToast = new MyToast(getApplicationContext());
		
		title = (TextView)findViewById(R.id.tv_title);
		etChange = (EditText)findViewById(R.id.et_change);
		btnCannel = (Button)findViewById(R.id.btn_cannel);
		btnOk = (Button)findViewById(R.id.btn_ok);
		btnCannel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.btn_cannel:
			this.finish();
			break;
			
		case R.id.btn_ok:
			Intent intent = new Intent();
			if(!etChange.getText().toString().equals("")){
				intent.putExtra("change", etChange.getText().toString());
				setResult(1, intent);
				this.finish();
			}else{
				mToast.show("没有更改", Toast.LENGTH_SHORT);
			}

		default:
			break;
		}
	}
}
