package com.example.communityfunction.adapter;

import java.util.ArrayList;

import com.example.communityfunction.tool.ImagesTool;
import com.example.communityfunction.tool.PostBBSImageLoader;
import com.example.communityfunction.tool.ToolGetImage;
import com.example.communityfunction.tool.PostBBSImageLoader.Type;
import com.neo.huikaimen.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class PostBBSImageAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<String> imgSelectedPath;

    public PostBBSImageAdapter(Context context,ArrayList<String>imgPath) 
	{
		this.context=context;
		this.imgSelectedPath=imgPath;
	}
	@Override
	public int getCount() 
	{
		if(imgSelectedPath.size()==9)//如果图片数已经九张，那么最后那个添加图片的图标就不要了
			return imgSelectedPath.size();
//		else if(imgSelectedPath.size()==0)
//			return 0;
		return imgSelectedPath.size()+1;//为了在最后能够加上一个添加图片的图标
	}

	@Override
	public Object getItem(int arg0) 
	{
		return imgSelectedPath.get(arg0);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public void add(String imgPath)
	{
		if(imgSelectedPath==null)
			imgSelectedPath=new ArrayList<String>();
		this.imgSelectedPath.add(imgPath);
		notifyDataSetChanged();
	}
	public void remove(int position)
	{
		if(imgSelectedPath!=null)
		{
			imgSelectedPath.remove(position);
			notifyDataSetChanged();
		}
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		final Holder holder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(context).inflate(R.layout.bbs_post_image_item, parent, false);
			holder=new Holder();
			holder.img=(ImageView)convertView.findViewById(R.id.bbs_post_image);
			convertView.setTag(holder);
		}
		else
		{
			holder=(Holder)convertView.getTag();
		}
		//之所以加上这个判断是因为getview会多次执行position=0，原因未知，所以加上这个让他正常的时候才执行下面代码。
		if(parent.getChildCount()==position)
		{
			/**
			 * 把那张添加图片的图标放在最后
			 */
			if(imgSelectedPath!=null && position<imgSelectedPath.size())
			{
				PostBBSImageLoader.getInstance(3,Type.LIFO).loadImage(
						imgSelectedPath.get(position),holder.img);
				
			}
			else
			{
				if(imgSelectedPath.size()<9)//添加图片没有九张时，把那张添加图片的图标放在最后
					{
					Log.e("PostBBSImage:oaderadapter", ""+position);
					holder.img.setImageResource(R.drawable.add_pic);
					holder.img.setScaleType(ScaleType.FIT_XY);
					}
				
			}
		}
		return convertView;
	}

	private class Holder
	{
		ImageView img;
	}
	

}
