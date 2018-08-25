package com.example.chat.Activity;

import java.util.List;

import com.example.chat.Activity.FriendsReqMsg.MsgState;
import com.neo.huikaimen.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class FriendsReqMsgAdapter extends BaseAdapter
{
	private List<FriendsReqMsg> reqList;
	private Context mContext;
	
	public FriendsReqMsgAdapter(List<FriendsReqMsg> reqList,Context context) 
	{
		this.reqList=reqList;
		this.mContext=context;
	}
	@Override
	public int getCount() 
	{
		return reqList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		if(reqList.size()==0)
			return null;
		return reqList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.friends_request_msg, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.reason = (TextView) convertView.findViewById(R.id.message);
            holder.agree = (Button) convertView.findViewById(R.id.agree);
			holder.status = (Button) convertView.findViewById(R.id.user_state);
			
			convertView.setTag(holder);
		}
		else
			holder=(ViewHolder) convertView.getTag();
		
		String str1 = mContext.getResources().getString(R.string.Has_agreed_to_your_friend_request);
		String str2 = mContext.getResources().getString(R.string.agree);
		String str3 = mContext.getResources().getString(R.string.Request_to_add_you_as_a_friend);
		String str5 = mContext.getResources().getString(R.string.Has_agreed_to);
		String str6 = mContext.getResources().getString(R.string.Has_refused_to);
		String str7 = mContext.getResources().getString(R.string.refuse);
		
		final FriendsReqMsg reqMsg=reqList.get(position);
		holder.name.setText(reqMsg.getName());
//		if(reqMsg.getReason()!=null)
//		holder.reason.setText(reqMsg.getReason());
		holder.agree.setVisibility(View.INVISIBLE);
		
		if (reqMsg.getState() == MsgState.BEAGREED) 
		{
			holder.status.setVisibility(View.INVISIBLE);
			holder.reason.setText(str1);
		}
		else if (reqMsg.getState() == MsgState.BEINVITEED)
		{
			holder.agree.setVisibility(View.VISIBLE);
            holder.agree.setEnabled(true);
            holder.agree.setBackgroundResource(android.R.drawable.btn_default);
            holder.agree.setText(str2);
            
            holder.status.setVisibility(View.VISIBLE);
			holder.status.setEnabled(true);
			holder.status.setBackgroundResource(android.R.drawable.btn_default);
			holder.status.setText(str7);
			if (reqMsg.getReason() == null) {
				// use default text
				holder.reason.setText(str3);
			}
			
			// set click listener
	        holder.agree.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // accept invitation
	                new FriendsManage(mContext).acceptInvitation(holder.agree, holder.status, reqMsg,reqList);
	            }
	        });
			holder.status.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// decline invitation
					new FriendsManage(mContext).refuseInvitation(holder.agree, holder.status, reqMsg,reqList);
				}
			});
		}
		else if (reqMsg.getState() == MsgState.AGREED) 
		{
			holder.status.setText(str5);
			holder.status.setBackgroundDrawable(null);
			holder.status.setEnabled(false);
			holder.reason.setText("加个好友呗！");
			}
		else if(reqMsg.getState() == MsgState.REFUSED)
		{
			holder.status.setText(str6);
			holder.status.setBackgroundDrawable(null);
			holder.status.setEnabled(false);
			holder.reason.setText("加个好友呗！");
		}
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView name;
		TextView reason;
		Button agree;
		Button status;
	}
}
