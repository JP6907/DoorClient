package com.example.chat.Activity;

import java.util.List;

import com.example.communityfunction.myView.CircleImageView;
import com.neo.huikaimen.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import neo.door.usermanager.User;

public class SearchFriendsAdapter extends BaseAdapter
{
	private Context mContext;
	private List<User> friendList;
	
    public SearchFriendsAdapter(Context context,List<User> list) 
    {
    	this.mContext=context;
    	this.friendList=list;
	}

	@Override
	public int getCount() 
	{
		return friendList.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		if(friendList.size()==0)
			return null;
		return friendList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) 
	{
		User user=friendList.get(arg0);
		ViewHolder holder=null;
		if(view==null)
		{
			holder=new ViewHolder();
			view=LayoutInflater.from(mContext).inflate(R.layout.friends_search_item, arg2,false);
			holder.headImage=(CircleImageView)view.findViewById(R.id.friend_image);
			holder.friendName=(TextView)view.findViewById(R.id.friend_name);
			view.setTag(holder);
		}
		else
			holder=(ViewHolder) view.getTag();
			
		holder.friendName.setText(user.getUsername());
//		ToolDownLoadPic downLoadPic=new ToolDownLoadPic();
//		downLoadPic.
//		holder.headImage.setImageBitmap(BitmapFactory.dec);
		return view;
	}
	class ViewHolder
	{
		CircleImageView headImage;
		TextView friendName;
	}

}
