package com.example.chat.Activity;

import com.hyphenate.easeui.EaseConstant;
import com.neo.huikaimen.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import neo.door.usermanager.User;

public class FriendCardPopWindow extends PopupWindow
{
	private View view;
	private TextView tvFriendPhone;
	private Button friendBtn;
	
	public FriendCardPopWindow(final Context context,final User user)
	{
		super(context);
		view=LayoutInflater.from(context).inflate(R.layout.friend_information,null);
		tvFriendPhone=(TextView)view.findViewById(R.id.tv_friend_phone);
		friendBtn=(Button)view.findViewById(R.id.friend_button);
		
		tvFriendPhone.setText(user.getUsername());
		if(user.getJudge()==-1 || user.getJudge()==1)
			friendBtn.setText("发消息");
		
		friendBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(user.getJudge()==0)
					new FriendsManage(context)
					.addContact(user.getUsername(), "哈哈");
				else
				{
					context.startActivity(new Intent(context,ChatActivity.class)
							.putExtra(EaseConstant.EXTRA_USER_ID,
							user.getUsername()));
					((Activity)context).finish();
				}
			}
		});
		
		// 设置弹窗的view
		this.setContentView(view);
		// 设置弹窗宽度
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置弹窗高度
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置弹窗可点�?
		this.setFocusable(true);
		// 设置弹窗动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置弹窗背景
		this.setBackgroundDrawable(dw);
	}
		
}
