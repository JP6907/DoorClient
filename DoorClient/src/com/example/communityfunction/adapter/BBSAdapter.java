package com.example.communityfunction.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.communityfunction.informationclass.BBS;
import com.example.communityfunction.tool.ToolGetImage;
import com.neo.huikaimen.R;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import neo.door.usermanager.UserConfig;
import neo.door.utils.ScreenUtil;
import neo.door.webutils.Config;

public class BBSAdapter extends BaseAdapter
{
	private List<BBS> bbsList=new ArrayList<BBS>();
	private Context context;
	private ToolGetImage imageLoad;
	private boolean isScroll=false;

	public BBSAdapter(Context context,List<BBS> bbsList,ToolGetImage getImage,boolean isScroll) 
	{
		this.context=context;
		this.bbsList=bbsList;
		this.imageLoad=getImage;
		this.isScroll=isScroll;
	}
	@Override
	public int getCount() 
	{
		return bbsList.size();
	}

	@Override
	public BBS getItem(int position) 
	{
		if(bbsList.get(position)!=null)
			return bbsList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	public void add(int location, BBS bbs) 
	{
		bbsList.add(location, bbs);
		notifyDataSetChanged();
	}

	public void add(BBS bbs) 
	{
		bbsList.add(bbs);
		notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		final ViewHolder holder ;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.bbs_listview_item, parent,false);
			holder.title=(TextView)convertView.findViewById(R.id.bbs_content_title);
			holder.content=(TextView) convertView.findViewById(R.id.bbs_content);
			holder.authorName = (TextView)convertView.findViewById(R.id.bbs_content_authorname);
			holder.imageGroup = (ViewGroup)convertView.findViewById(R.id.imageGroup);
			holder.mImageView = new ImageView[3];

			for(int i=0;i<holder.mImageView.length;i++)
			{
				int imageViewSize=(
						ScreenUtil.getScreenSize(context).widthPixels-200)/3;
				ImageView imageView=new ImageView(context);
				imageView.setLayoutParams(new LayoutParams(imageViewSize,imageViewSize));
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setPadding(8, 8, 8, 8);
				holder.mImageView[i]=imageView;
			}
			convertView.setTag(holder);
		}
		else
			holder=(ViewHolder)convertView.getTag();
		
		BBS bbs=bbsList.get(position);
		holder.title.setText(bbs.getBbsTitle());
		holder.content.setText("\u3000\u3000"+(Html.fromHtml(bbs.getBbsText())).toString());
		holder.authorName.setText(bbs.getBbsPublisher());
		
		TextPaint tp1 = holder.title.getPaint();
		TextPaint tp2 = holder.authorName.getPaint();
		TextPaint tp3 = holder.content.getPaint();
		tp1.setFakeBoldText(true);
		tp2.setFakeBoldText(true);
		tp3.setFakeBoldText(true);
		
//		int typeface = Typeface.BOLD_ITALIC;
		if(holder.imageGroup.getChildCount()!=0)//重置状态
			holder.imageGroup.removeAllViews();
		
		List<String> bbsPictureList=bbs.getBbsPictureList();
		
		if(bbsPictureList!=null)
			for(int i=0;i<bbsPictureList.size();i++)
			{
				if(!bbsPictureList.get(i).equals("null") && !bbs.isPostJustNow())
				{
					String time1 = bbs.getBbsPublishdt().replaceAll("[:,\\s+,-]", "");
					String urlPath = Config.DownLoadBBSPath + bbs.getBbsPhone() + "/" + time1 + "/"
							+ bbsPictureList.get(i);
					String path = UserConfig.getBBSPicCachePath(time1) + "/" + bbsPictureList.get(i);
					/**
					 * 显示3张图片
					 */
					if(holder.imageGroup.getChildCount()<3)
					{
						holder.mImageView[i].setImageResource(R.drawable.bbs_image_not_load);
						holder.imageGroup.addView(holder.mImageView[i]);
						holder.mImageView[i].setTag(path);
//						if (!isScroll)
						imageLoad.bindBitmap(path, urlPath, holder.mImageView[i], bbs.getBbsPhone(),
								bbsPictureList.get(i), bbs.getBbsPublishdt(),true);
					}
					/**
					 * 找到图片，没有的话就去下载，但是不显示出来
					 */
					else
					{
						imageLoad.bindBitmap(path,urlPath,holder.mImageView[0], bbs.getBbsPhone(), 
								bbsPictureList.get(i), bbs.getBbsPublishdt(),false);
					}
				}
				
				if(!bbsPictureList.get(i).equals("null") && bbs.isPostJustNow())
				{
					if(holder.imageGroup.getChildCount()<3)
					{
						holder.mImageView[i].setImageResource(R.drawable.bbs_image_not_load);
						holder.imageGroup.addView(holder.mImageView[i]);
						holder.mImageView[i].setTag(bbsPictureList.get(i));
//						if (!isScroll)
						imageLoad.bindBitmap(bbsPictureList.get(i), null, holder.mImageView[i], null,
								null, null,true);
					}
				}
				
			}
		return convertView;
	}
	private class ViewHolder
	{
		TextView title;
		TextView content;
		TextView authorName;
		ViewGroup imageGroup;
		ImageView [] mImageView;
	}

}
