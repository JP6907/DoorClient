package com.example.communityfunction.adapter;

import java.util.List;

import com.neo.huikaimen.R;
import com.example.communityfunction.informationclass.Notice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoticeAdapter extends BaseAdapter 
{
	private List<Notice> noticesList;
	private Context mContext;

	public NoticeAdapter(Context context,List<Notice> objects) 
	{
		this.mContext=context;
		noticesList=objects;
	}
	@Override
	public int getCount() 
	{
		return noticesList.size();
	}


	@Override
	public Object getItem(int position) 
	{
		if(noticesList==null)
			return null;
		return noticesList.get(position);
	}


	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.notice_listview_item, parent, false);
			
			holder.title = (TextView) convertView.findViewById(R.id.notice_content_title);
			holder.date = (TextView) convertView.findViewById(R.id.notice_content_date);
			
			convertView.setTag(holder);
		}
		else
			holder=(ViewHolder)convertView.getTag();
			
		Notice notice = noticesList.get(position);

		holder.title.setText(notice.getTitle());
		holder.date.setText(notice.getDate());

		return convertView;

	}
	private class ViewHolder 
	{
		TextView title;
		TextView date;
	}

}