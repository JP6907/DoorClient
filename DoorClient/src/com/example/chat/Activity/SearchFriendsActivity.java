package com.example.chat.Activity;


import java.util.ArrayList;
import java.util.List;

import com.example.communityfunction.tool.ToolQueryUser;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import neo.door.usermanager.User;
import neo.door.utils.ChangeStatusBarColor;

public class SearchFriendsActivity extends Activity 
{
	private Button btnSearch;
	private ImageView ivDeleteText;
	private EditText etSearch;
	private ListView friendListView;
	private InputMethodManager imm;
	private SearchFriendsAdapter mAdapter;
	private List<User> friendList;
	
	@SuppressLint("ResourceAsColor")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_search);

		// 设置状态栏
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		initView();
		initSearchBar();
	}

	private void initView() 
	{
		friendListView=(ListView) findViewById(R.id.friend_search_list);
		friendList=new ArrayList<User>();
		
		friendListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				FriendCardPopWindow popWindow=new FriendCardPopWindow(SearchFriendsActivity.this,
						friendList.get(arg2));
				popWindow.showAtLocation(findViewById(R.id.friend_search_activity), 
						Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
			
		});
	}

	private void initSearchBar() 
	{
		btnSearch = (Button) findViewById(R.id.btnSearch);
		ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
		etSearch = (EditText) findViewById(R.id.etSearch);
		btnSearch.setText("取消");

		// 删除
		ivDeleteText.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				etSearch.setText("");
			}
		});
		
		imm = (InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		btnSearch.setOnClickListener(new OnClickListener() 
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View view)
			{
				if(btnSearch.getText().toString().equals("取消"))
				{
					finish();
					return;
				}
				
				ToolQueryUser queryUser = new ToolQueryUser(SearchFriendsActivity.this);
				queryUser.execute(etSearch.getText().toString());
				queryUser
				.setOnUserSucceedListener(new ToolQueryUser.getUserSucceedListener()
				{
					@Override
					public void showUser(User queriedUser) 
					{
						if(queriedUser.getUsername()!=null)
						{
							friendList.add(queriedUser);
							mAdapter=new SearchFriendsAdapter(SearchFriendsActivity.this,friendList);
							friendListView.setAdapter(mAdapter);
						}
					}
				});
				
				(new Handler()).postDelayed(new Runnable() 
				{
					public void run() 
					{
						imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
					}
				}, 100);
			}
		});

		etSearch.addTextChangedListener(new TextWatcher()
		{
			public void afterTextChanged(Editable s) 
			{
				if (s.length() == 0) 
				{
					ivDeleteText.setVisibility(View.GONE);
					btnSearch.setText("取消");
				}
				else 
				{
					btnSearch.setText("搜索");
					ivDeleteText.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
			{
			}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
			{
			}
		});

	}

}
