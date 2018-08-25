package com.example.communityfunction.tool;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView; 

public class ToolGetImage 
{
	private static final String TAG="ToolGetImage";
	
	private static final int CPU_COUNT=Runtime.getRuntime().availableProcessors();//CPU������
	private static final int CORE_POOL_SIZE=CPU_COUNT+1;//�����߳���
	private static final int MAXIMUM_POOL_SIZE=CPU_COUNT*2+1;//����߳���
	private static final long KEEP_ALIVE=10L;
	
	
	private static final ThreadFactory mThreadFactory=new ThreadFactory() {
		private final AtomicInteger mCount=new AtomicInteger();
		
		@Override
		public Thread newThread(Runnable r) {
			// TODO Auto-generated method stub
			return new Thread(r, "ToolGetImage#"+mCount.getAndIncrement());
		}
	};
	
	//�̳߳�
	public static final Executor THREAD_POOL_EXECUTOR=new ThreadPoolExecutor
			(CORE_POOL_SIZE,
					MAXIMUM_POOL_SIZE,
					KEEP_ALIVE,
					TimeUnit.SECONDS,
					new LinkedBlockingDeque<Runnable>(),
					mThreadFactory); 
	/**
	 * ����ͼƬ��ʾ
	 */
	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			ImageResult result=(ImageResult) msg.obj;
			ImageView imageView=result.imageView;
			String uri=(String)imageView.getTag();
			if(uri.equals(result.uri) && imageView!=null)
			{
				imageView.setImageBitmap(result.bm);
			}
			else
			{
				Log.e(TAG, "set image bitmap,but url has change,ignored or the imageView is null!");
			}
		};
	};
	
	private LruCache<String, Bitmap> mMemoryChche;//�ڴ滺��
	
	private ToolGetImage()
	{
		int maxMemory=(int)(Runtime.getRuntime().maxMemory()/1024);
		int cacheSize=(int)maxMemory/8;
		mMemoryChche=new LruCache<String,Bitmap>(cacheSize)
		{
			protected int sizeOf(String key,Bitmap bm)
			{
				return bm.getRowBytes()*bm.getHeight()/1024;
			}
		};
	}
	
	public static ToolGetImage build()
	{
		return new ToolGetImage();
	}
	/**
	 * ��ӵ��ӻ���
	 * @param key
	 * @param bm
	 */
	private  void addBitmapToMemoryCache(String key,Bitmap bm)
	{
		if(getBitmapFromMemoryCache(key)==null)
			mMemoryChche.put(key, bm);
	}
	/**
	 * �ӻ����л�ȡ
	 * @param key
	 * @return
	 */
	private Bitmap getBitmapFromMemoryCache(String key) 
	{
		return mMemoryChche.get(key);
	}
	/**
	 * �ӱ����ļ���ȡ
	 * @param path
	 * @return
	 */
	private Bitmap loadBitmapFromPath(String path,ImageView imageView)
	{
		Bitmap bm=ImagesTool.decodeSampledBitmapFromPath(path, imageView);//��ȡ����ѹ��
		return bm;
		
	}
	/**
	 * ˢ��imagView
	 * @param uri
	 * @param imageView
	 * @param bm
	 */
	private void refreshBitmap(final String uri,final ImageView imageView,final Bitmap bm)
	{
		ImageResult result=new ImageResult(imageView,uri,bm);
		Message msg=Message.obtain();
		msg.obj=result;
		mHandler.sendMessage(msg);
	}
	/**
	 * 
	 * @param uri   ���ر���ͼƬ�ĵ�ַ
	 * @param downLoadPath ���ص�ַ
	 * @param imageView  
	 * @param userPhone  �û��绰
	 * @param fileName   ͼƬ��
	 * @param postDate   ��ͼƬ����
	 * @param isShow    �Ƿ�չʾ����
	 */
	public void bindBitmap(final String uri,final String downLoadPath,final ImageView imageView,
			final String userPhone,final String fileName,final String postDate,final boolean isShow)
	{
		if (isShow) 
		{
			imageView.setTag(uri);
			Bitmap bm = getBitmapFromMemoryCache(uri);// �ӻ����л�ȡ
			
			if (bm != null) 
			{
				refreshBitmap(uri, imageView, bm);
				return;
			}
		}
		
		Runnable loadBitmapTask=new Runnable() 
		{
			@Override
			public void run() 
			{
				if(!isShow)
				{
					if(searchPic(uri))//�ҵõ����������Ҳ�����ȥ���ء�
						return;
					else
					{
						ToolDownLoadPic.downLoad(downLoadPath, userPhone, fileName, postDate,imageView);//����ͼƬ
						return;
					}
				}
				
				Bitmap bm=loadBitmap(uri,downLoadPath,imageView,userPhone,fileName,postDate);
				if(bm!=null)
				{
					addBitmapToMemoryCache(uri, bm);
					refreshBitmap(uri, imageView, bm);
				}
			}
		};
		THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
	}
	
	/**
	 * ����û�У��򱾵ػ��߷�������ȡ
	 * @param uri
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	protected Bitmap loadBitmap(String uri,String downLoadPath,ImageView imageView,String userPhone,String fileName,String postDate) 
	{
		Bitmap bm;
		bm=loadBitmapFromPath(uri, imageView);//�����ļ�
		if(bm!=null)
			return bm;
		
		bm=ToolDownLoadPic.downLoad(downLoadPath, userPhone, fileName, postDate,imageView);//����ͼƬ

		if(bm!=null)
			return bm;
		
		return null;
	}
	
	/**
	 * Ѱ���ֻ������ļ��п�����û�и��ļ�
	 * @return
	 */
	public boolean searchPic(String uri)
	{
		File userPah = new File(uri);
		if(userPah.exists())
			return true;
		return false;
		
	}

	private static class ImageResult
	{
		public ImageView imageView;
		public String uri;
		public Bitmap bm;
		
		public ImageResult(ImageView imageView,String uri,Bitmap bm)
		{
			this.imageView=imageView;
			this.uri=uri;
			this.bm=bm;
		}
	}
	

}
