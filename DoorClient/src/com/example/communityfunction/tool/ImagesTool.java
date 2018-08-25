package com.example.communityfunction.tool;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ImagesTool 
{
	/**
	 * 根据imagView的大小压缩图片
	 * @param path
	 * @param imageView
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromPath(String path,ImageView imageView)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(path,options);
		
		ImageSize imageSize=getImageViewSize(imageView);
		options.inSampleSize=caculateInSampleSize(options,imageSize.width,imageSize.height);
		
		options.inJustDecodeBounds=false;
	    Bitmap bitmap=BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
	
	public static int caculateInSampleSize(Options options,ImageView imageView)
	{
		ImageSize imageSize=getImageViewSize(imageView);
		int inSampleSize=caculateInSampleSize(options, imageSize.width,imageSize.height);
		return inSampleSize;
	}

	/**
	 * 根据已知的大小需求压缩图片
	 * @param path
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static Bitmap decodeSampledBitmapFromPath(String path,int reqWidth, int reqHeight)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(path,options);
		
		options.inSampleSize=caculateInSampleSize(options,reqWidth,reqHeight);
		
		options.inJustDecodeBounds=false;
	    Bitmap bitmap=BitmapFactory.decodeFile(path, options);
		return bitmap;
		
	}

	private static int caculateInSampleSize(Options options, int reqWidth, int reqHeight) 
	{
		
		int width=options.outWidth;
		int height=options.outHeight;
		
		int inSampleSize=1;
		if(width>reqWidth || height>reqHeight)
		{
			int widthRadio=Math.round(width *1.0f/reqWidth);
			int heightRadio = Math.round(height * 1.0f / reqHeight);
			inSampleSize = Math.max(widthRadio, heightRadio);
		}
		return inSampleSize;
	}
	
	protected static ImageSize getImageViewSize(ImageView imageView) 
	{
		ImageSize imageSize = new ImageSize();

		// 屏幕的宽度
		DisplayMetrics metrics = imageView.getContext().getResources().getDisplayMetrics();
		LayoutParams lp = imageView.getLayoutParams();

		int width = imageView.getWidth();
		if (width <= 0) {
			width = lp.width;
		}
		if (width <= 0) {
			width = imageView.getMaxWidth();
		}
		if (width <= 0) {
			width = metrics.widthPixels;
		}

		int height = imageView.getHeight();
		if (height <= 0) {
			height = lp.height;
		}
		if (height <= 0) {
			height = imageView.getMaxHeight();
		}
		if (height <= 0) {
			height = metrics.heightPixels;
		}

		imageSize.width = width;
		imageSize.height = height;
		return imageSize;
	}
	
	private static class ImageSize
	{
		int width;
		int height;
	}

}
