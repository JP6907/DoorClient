package com.example.communityfunction.adapter;

import java.util.ArrayList;
import java.util.List;
import com.example.communityfunction.tool.PostBBSImageLoader;
import com.example.communityfunction.tool.PostBBSImageLoader.Type;
import com.neo.huikaimen.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class PostBBSImageLoaderAdapter extends BaseAdapter
{
	/**
	 * 保存选中的图片的路径
	 */
	public static ArrayList<String>mSelectedImg=new ArrayList<String>();//用来判断图片是否被选中
//	public static ArrayList<String> imageSelec=new ArrayList<String>();//以防选中的图片超过九张
	public static int imageSelectedCount=0;//此次选中图片的数目
	
	private Context context;
	
	private String mDirPath;
	private List<String>mImgPaths;
	private LayoutInflater mInflater;
	
	public PostBBSImageLoaderAdapter(Context context,List<String>mDatas,String dirPath)
	{
		this.mDirPath=dirPath;
		this.mImgPaths=mDatas;
		mInflater=LayoutInflater.from(context);
		this.context=context;
	}

	@Override
	public int getCount() 
	{
		return mImgPaths.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mImgPaths.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	public void clear()
	{
		if(mSelectedImg!=null)
			mSelectedImg.clear();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		
		final ViewHolder viewHolder;
		if(convertView==null)
		{
			convertView=mInflater.inflate(R.layout.item_grid_view,parent,false);
			viewHolder=new ViewHolder();
			viewHolder.mImg=(ImageView)convertView.findViewById(R.id.item_image);
			viewHolder.mSelect=(ImageButton)convertView.findViewById(R.id.item_select);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		
		//重置状态
		viewHolder.mImg.setImageResource(R.drawable.default_image);
		viewHolder.mSelect.setImageResource(R.drawable.image_unselect);
		viewHolder.mImg.setColorFilter(null);
		
			
			PostBBSImageLoader.getInstance(3,Type.LIFO).loadImage(
					mDirPath+"/"+mImgPaths.get(position),viewHolder.mImg);
			final String filePath=mDirPath+"/"+mImgPaths.get(position);
			viewHolder.mImg.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					//已经被选中
					if(mSelectedImg.contains(filePath))//同完整路径，防止在不同文件夹下的图片同名
					{
//						imageSelec.remove(filePath);
						mSelectedImg.remove(filePath);
						imageSelectedCount--;
						viewHolder.mImg.setColorFilter(null);
						viewHolder.mSelect.setImageResource(R.drawable.image_unselect);
					}
					else//未被选择
					{
						if(imageSelectedCount==9)
						{
							Toast.makeText(context, "最多只能选择九张图片", Toast.LENGTH_SHORT).show();//一次性选得比九张多则提示
							return;
						}
						mSelectedImg.add(filePath);
//						imageSelec.add(filePath);
						imageSelectedCount++;
						viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
						viewHolder.mSelect.setImageResource(R.drawable.image_selected);
						
					}
					//notifyDataSetChanged();
				}
			});
			
			if(mSelectedImg.contains(filePath))
			{
				viewHolder.mImg.setColorFilter(Color
	                    .parseColor("#77000000"));
	            viewHolder.mSelect
	                    .setImageResource(R.drawable.image_selected);
			}
		return convertView;
	}
	private class ViewHolder
	{
		ImageView mImg;
		ImageButton mSelect;
	}
	
}
