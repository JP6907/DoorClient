package com.example.chat.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.example.chat.Activity.FriendsReqMsg.MsgState;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseContactListFragment.EaseContactListItemClickListener;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseContactList;
import com.hyphenate.exceptions.HyphenateException;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import neo.door.cache.FriendCache;
import neo.door.cache.FriendsReqMsgCache;
import neo.door.usermanager.User;
import neo.door.usermanager.UserConfig;
import neo.door.utils.ChangeStatusBarColor;

public class ContactsActivity extends FragmentActivity implements OnClickListener 
{
	private Resources resources;
	private TextView tvTop;
	private ImageButton topButtonRight;
	private ImageButton returnBack;
	
	private FragmentManager manager;
	private EaseContactListFragment contactFragment;
	private FragmentTransaction ft;
	private Map<String, EaseUser> contacts;
	
	@SuppressLint("ResourceAsColor")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		resources = getResources();
		// 设置状态栏
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		init();
		initView();
	}
	
	private void init()
	{
		manager = ContactsActivity.this.getSupportFragmentManager();
	    contactFragment = new EaseContactListFragment();
		ft = manager.beginTransaction();
		contacts = new HashMap<String, EaseUser>();
		ft.add(R.id.contacts_list, contactFragment);
		ft.commit();
		
		contactFragment.setContactListItemClickListener(new EaseContactListItemClickListener() 
		{
			@Override
			public void onListItemClicked(EaseUser user) 
			{
				startActivity(new Intent(ContactsActivity.this, ChatActivity.class)
						.putExtra(EaseConstant.EXTRA_USER_ID,
						user.getUsername()));
			}
		});
		
		/**
		 * 从缓存中获取好友列表
		 */
		contacts.clear();
		List<User> contactList=null;
		try {
			contactList=FriendCache.readCache();
		} catch (JSONException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		for(int i=0;i<contactList.size();i++)
		{
			User user=new User("");
			user=contactList.get(i);
			EaseUser user1 = new EaseUser(user.getUsername());
			user1.setAvatar("/sdcard/Pictures/Screenshots/S61109-121622.jpg");
			contacts.put(user1.getUsername(), user1);
		}
		
		contactFragment.setContactsMap(contacts);
		
	}
	private void initView() 
	{
	    tvTop = (TextView) findViewById(R.id.chat_text_view);  
	    tvTop.setText(resources.getString(R.string.contact));
	    
	    returnBack = (ImageButton) findViewById(R.id.head_chat_return_back);
	    returnBack.setOnClickListener(this);
	    
    	topButtonRight = (ImageButton) findViewById(R.id.topButtonRight);
    	topButtonRight.setOnClickListener(this);
        topButtonRight.setVisibility(View.VISIBLE);
        
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
	switch (view.getId()){
	case R.id.topButtonRight:
		Intent intent=new Intent(this,SearchFriendsActivity.class);
		startActivity(intent);
		break;
	case R.id.head_chat_return_back:
		this.finish();
		break;
	}
	}
}
