package com.example.communityfunction.bbs;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.communityfunction.adapter.PostBBSImageLoaderAdapter;
import com.example.communityfunction.myView.ListImageDirPopWindow;
import com.example.communityfunction.myView.ListImageDirPopWindow.OnDirSelectedListener;
import com.example.communityfunction.tool.FolderBean;
import com.neo.huikaimen.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PostBbsImageLoaderActivity extends Activity 
{
	private GridView mGridView;
	private List<String> mImgs;
	/**
	 * 已经选中的图片
	 */
	private ArrayList<String> imageSelectedPath=new ArrayList<String>();
	
	private RelativeLayout mBottomLy;
	private TextView mDirName;
	private TextView mDirCount;
	
	private ImageView back;
	private Button checkFinish;
	
	private File mCurrentDir;
	private int mMaxCount;
	private static int DATA_LOADED=0x110;
	
	private Cursor cursor;
	private ProgressDialog mProgressDialog;
	private PostBBSImageLoaderAdapter mImgAdapter;
	

	private List<FolderBean> mFolderBeans=new ArrayList<FolderBean>();
	
	private ListImageDirPopWindow mDirPopupWindow;
	
	private Handler mHandler=new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg)
		{
			if(msg.what==0)
			{
				mProgressDialog.dismiss();
				//绑定数据到view中
				data2View();
				initDirPopupwinddow();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_post_image_loader);
		initView();
		initDatas();
		initEvent();
	}

	protected void data2View() 
	{
		if(mCurrentDir==null)
		{
			Toast.makeText(this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
			return;
		}
		mImgs=Arrays.asList(mCurrentDir.list());
//		 mImgs=Arrays.asList(mCurrentDir.list(new FilenameFilter() {
//
//	            @Override
//	            public boolean accept(File dir, String filename) {
//	                if(filename.endsWith(".jpg")||filename.endsWith(".jpeg")||filename.endsWith(".png"))
//	                    return true;
//	                return false;
//	            }
//	        }));
//		mImgs.add(0, "file:///android_asset/new_friend.png");
        mImgAdapter = new PostBBSImageLoaderAdapter(this, mImgs, mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mImgAdapter);
        mDirCount.setText(mMaxCount+"");
        mDirName.setText(mCurrentDir.getName());
		
	}

	private void initView() 
	{
		mGridView=(GridView)findViewById(R.id.id_gridview);
		mBottomLy=(RelativeLayout)findViewById(R.id.id_bottom_ly);
		mDirName=(TextView)findViewById(R.id.id_dir_name);
		mDirCount=(TextView)findViewById(R.id.id_dir_count);
		
		back=(ImageView)findViewById(R.id.id_imageloader_back);
		checkFinish=(Button)findViewById(R.id.id_check_finish);
		
		Intent intent=getIntent();
		PostBBSImageLoaderAdapter.imageSelectedCount=intent.getExtras().getInt("imageCount");
	}
	

	/**
	 * 利用ContentProvider扫描手机中所有图片
	 */
	private void initDatas() 
	{
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "当前存储卡不可用！", Toast.LENGTH_SHORT).show();
			return;
		}
		mProgressDialog=ProgressDialog.show(this, null, "正在加载...");
		
		new Thread()
		{
			@Override
			public void run() 
			{
				Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver cr = PostBbsImageLoaderActivity.this.getContentResolver();
					 
					//cursor=cr.query(mImgUri, null,
						//	MediaStore.Images.Media.MIME_TYPE + "=? or" 
					// + MediaStore.Images.Media.MIME_TYPE + "=?",
					//		new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);
				
					 
					  cursor = cr.query(mImgUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
							    + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]
							{ "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

			    //Cursor cursor=cr.query(mImgUri, null,
				//		MediaStore.Images.Media.MIME_TYPE + "= ? or" + MediaStore.Images.Media.MIME_TYPE + "= ?",
				//		new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);
				Set<String>mDirPaths=new HashSet<String>();
				while(cursor.moveToNext())
				{
					String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					File parentFile=new File(path).getParentFile();
					if(parentFile==null)
						continue;
					String dirPath=parentFile.getAbsolutePath();
					FolderBean folderBean=null;
					if(mDirPaths.contains(dirPath))
						continue;
					else
					{
						mDirPaths.add(dirPath);
						folderBean=new FolderBean();
						folderBean.setDir(dirPath);
						folderBean.setFirstImgPath(path);
					}
					if(parentFile.list()==null)
						continue;
					int picSize=parentFile.list(new FilenameFilter() 
					{
						
						@Override
						public boolean accept(File dir, String filename) 
						{
							if(filename.endsWith(".jpeg")||filename.endsWith(".jpg")||filename.endsWith(".png"))
								return true;
							return false;
						}
					}).length;
					
					folderBean.setCount(picSize);
					mFolderBeans.add(folderBean);
					
					if(picSize>mMaxCount)
					{
						mMaxCount=picSize;
						mCurrentDir=parentFile;
					}
						
				}
				
				cursor.close();
				//通知handle扫描图片完成
				mHandler.sendEmptyMessage(0);
			};
		}.start();
		
	}

	private void initEvent()
	{
		
		mBottomLy.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
				lightOff();
			}
		});
		
		back.setOnClickListener(new OnClickListener() //返回
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent();
				PostBbsImageLoaderActivity.this.setResult(1, intent);
				PostBBSImageLoaderAdapter.mSelectedImg.clear();//取消已经选中的标记
//				PostBBSImageLoaderAdapter.imageSelec.clear();
				finish();
			}
		});
		checkFinish.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				imageSelectedPath=PostBBSImageLoaderAdapter.mSelectedImg;
				Intent intent=getIntent();
				intent.putStringArrayListExtra("imagePath", imageSelectedPath);
//				PostBBSImageLoaderAdapter.mSelectedImg.clear();//取消已经选中的标记?为何加上这个setAdapter失效，难道清空后imageSelectedPath也没了，难道他们引用的是同个地址？
				PostBbsImageLoaderActivity.this.setResult(0, intent);
				finish();
			}
		});
		
	}
	/**
	 * 重写返回键
	 */
	@Override
	public void onBackPressed()
	{
		Intent intent=new Intent();
		PostBbsImageLoaderActivity.this.setResult(1, intent);
		PostBBSImageLoaderAdapter.mSelectedImg.clear(); //取消已经选中的标记
		finish();
	}
	
	/**
	 * 内容区域变暗
	 */
	protected void lightOff()
	{
		WindowManager.LayoutParams lp=getWindow().getAttributes();
		lp.alpha=0.3f;
		getWindow().setAttributes(lp);
	}
	
	protected void initDirPopupwinddow() 
	{
		mDirPopupWindow=new ListImageDirPopWindow(this,mFolderBeans);
		
		mDirPopupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() 
			{
				lightOn();
			}
		});
		
		mDirPopupWindow.setOnDirSelectedListener(new OnDirSelectedListener() 
		{

			@Override
			public void onSelected(FolderBean folderBean) 
			{
				mCurrentDir=new File(folderBean.getDir()); 
				
				mImgs=Arrays.asList(mCurrentDir.list(new FilenameFilter()
				{
					public boolean accept(File dir,String filename)
					{
						if(filename.endsWith(".jpg")||filename.endsWith(".jpeg")||filename.endsWith(".png"))
							return true;
						return false;
					}
				}));
				
				mImgAdapter=new PostBBSImageLoaderAdapter(PostBbsImageLoaderActivity.this, mImgs, mCurrentDir.getAbsolutePath());
				mGridView.setAdapter(mImgAdapter);
				
				mDirCount.setText(mImgs.size()+"");
				mDirName.setText(folderBean.getName());
				mDirPopupWindow.dismiss();
			}
			
		});
	}
	/**
	 * 内容区域变亮
	 */
	protected void lightOn() 
	{
		WindowManager.LayoutParams lp=getWindow().getAttributes();
		lp.alpha=1.0f;
		getWindow().setAttributes(lp);
		
	}
	
}
