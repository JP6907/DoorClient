package com.example.chat.Activity;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import neo.door.utils.ChangeStatusBarColor;

public class ChatActivity extends FragmentActivity{
    private EaseChatFragment chatFragment;
	String toChatUsername;
	
	@SuppressLint("ResourceAsColor")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		// …Ë÷√◊¥Ã¨¿∏
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		init();
	}
	
	private void init(){
		
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new EaseChatFragment();
        //set arguments
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_chat, chatFragment).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.ec_layou, chatFragment).commit();
	}
	


}
