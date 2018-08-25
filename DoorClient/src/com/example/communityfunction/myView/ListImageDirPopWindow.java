package com.example.communityfunction.myView;

import java.lang.reflect.Method;
import java.util.List;

import com.example.communityfunction.tool.FolderBean;
import com.example.communityfunction.tool.PostBBSImageLoader;
import com.example.communityfunction.tool.PostBBSImageLoader.Type;
import com.neo.huikaimen.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ListImageDirPopWindow extends PopupWindow
{
	private int mWith;
	private int mHeight;
	private View mConvertView;
	private ListView mListView;
	private List<FolderBean>mDatas;
	
	public interface OnDirSelectedListener
	{
		void onSelected(FolderBean folderBean);//��folderBean����ȥ������֪�������ĸ��ļ��б�����ˡ�
	}
	
	public OnDirSelectedListener mListener;
	

	public void setOnDirSelectedListener(OnDirSelectedListener mListener) 
	{
		this.mListener = mListener;
	}

	public ListImageDirPopWindow(Context context,List<FolderBean>datas)
	{
		calWidthAndHeight(context);
		mConvertView=LayoutInflater.from(context).inflate(R.layout.image_loader_popwindow, null);
		mDatas=datas;
		setContentView(mConvertView);
		setWidth(mWith);
		setHeight(mHeight);
		
		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
		
		//�������������ʹPopupWindow�� �˳���ť�¼� �Ͷ� ��Ļ��Touch�¼� ��Ч
		//Ҫ�õ��PopupWindow֮��ĵط�PopupWindow��ʧ
		setBackgroundDrawable(new BitmapDrawable());
		
		setTouchInterceptor(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				if(event.getAction()==MotionEvent.ACTION_OUTSIDE)
				{
					dismiss();
					return true;
				}
				return false;
			}
		});
		initView(context);
		initEvent();
	}

	private void initEvent() 
	{
		mListView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{
				if(mListener!=null)
				{
					mListener.onSelected(mDatas.get(position));//����ȥ
				}
			}
		});
	}

	private void initView(Context context) 
	{
		mListView=(ListView)mConvertView.findViewById(R.id.id_list_dir);
		mListView.setAdapter(new ListDirAdapter(context,mDatas));
	}

	/**
	 * ����popWindow�Ŀ�Ⱥ͸߶�
	 * @param context
	 */
	private void calWidthAndHeight(Context context)
	{
		WindowManager wm=(WindowManager)context.getSystemService(context.WINDOW_SERVICE);
		DisplayMetrics outMetrics=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mWith=outMetrics.widthPixels;
		mHeight=(int)(outMetrics.heightPixels*0.7);
	}
	
	private class ListDirAdapter extends ArrayAdapter<FolderBean>
	{
		
		private LayoutInflater mInflater;
		
		private List<FolderBean>mDatas;
		
		public ListDirAdapter(Context context,List<FolderBean> objects)
		{
			super(context,0, objects);
			mInflater=LayoutInflater.from(context);
		}
		public View getView(int position,View convertView,ViewGroup parent)
		{
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.pop_item, parent,false);
				
				holder.mImg=(ImageView)convertView.findViewById(R.id.id_dir_item_image);
				holder.mDirName=(TextView)convertView.findViewById(R.id.id_dir_item_name);
				holder.mDirCount=(TextView)convertView.findViewById(R.id.id_dir_item_count);
				
				convertView.setTag(holder);
			}
			else
			{
				holder=(ViewHolder)convertView.getTag();
			}
			
			FolderBean bean=getItem(position);
			//����
			holder.mImg.setImageResource(R.drawable.default_image);//����ڶ������õ�һ����ʱ��û������ʱ��ʾ���ǵ�һ����ͼƬ
			
			PostBBSImageLoader.getInstance(3,Type.LIFO).loadImage(bean.getFirstImgPath(),holder.mImg);
			holder.mDirCount.setText(bean.getCount()+"");
			holder.mDirName.setText(bean.getName());
			
			return convertView;
		}
		
		private class ViewHolder
		{
			ImageView mImg;
			TextView mDirName;
			TextView mDirCount;
			
		}
	}
}
