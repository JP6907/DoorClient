package com.example.chat.Activity;

import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import neo.door.utils.ChangeStatusBarColor;

public class InformationActivity extends Activity implements OnClickListener
{
	private Resources resources;
	private TextView tvTop;
	private ImageButton topButtonRight;
	private ImageButton returnBack;
	
	@SuppressLint("ResourceAsColor")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);

		// …Ë÷√◊¥Ã¨¿∏
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		initView();
	}
	
	private void initView() 
	{
	    tvTop = (TextView) findViewById(R.id.chat_text_view);  
	    tvTop.setText(resources.getString(R.string.information));
	    
	    returnBack = (ImageButton) findViewById(R.id.head_chat_return_back);
	    returnBack.setOnClickListener(this);
	    
    	topButtonRight = (ImageButton) findViewById(R.id.topButtonRight);
    	topButtonRight.setOnClickListener(this);
        topButtonRight.setVisibility(View.INVISIBLE);
        
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
			switch (view.getId()){
			case R.id.head_chat_return_back:
				this.finish();
				break;
			}
	}

}
