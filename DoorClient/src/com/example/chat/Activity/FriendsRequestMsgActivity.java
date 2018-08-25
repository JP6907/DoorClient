package com.example.chat.Activity;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.neo.huikaimen.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import neo.door.cache.FriendsReqMsgCache;

public class FriendsRequestMsgActivity extends Activity
{
	private TextView title;
	private ListView friendsReqListView;
	private ImageView back;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_request_listview);
		
		initView();
		initData();
	}
	private void initData() 
	{
		try {
			List<FriendsReqMsg> reqMsgList=FriendsReqMsgCache.readCache();
			FriendsReqMsgAdapter reqMsgAdapter=new FriendsReqMsgAdapter(reqMsgList, this);
			friendsReqListView.setAdapter(reqMsgAdapter);
			
		} catch (JSONException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	private void initView()
	{
		title=(TextView)findViewById(R.id.chat_text_view);
		
		title.setText("…Í«Î”ÎÕ®÷™");
		
		friendsReqListView=(ListView)findViewById(R.id.friends_request_listview);
		back=(ImageView)findViewById(R.id.head_chat_return_back);
		
		back.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
	}

}
