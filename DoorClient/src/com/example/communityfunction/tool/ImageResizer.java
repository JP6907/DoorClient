package com.example.communityfunction.tool;

import java.io.FileDescriptor;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 对图片进行采样, 压缩
 */
public class ImageResizer {

	/**
	 * 从资源里采样加载图片
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public Bitmap decodeSampleBitmapFromResource(Resources res, 
			int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 只测量图片宽度
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 计算采样率
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 根据采样率加载图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	
	/**
	 * 从文件里采样加载图片
	 * @param fd
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, 
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 只测量图片宽度
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fd, null, options);
		// 计算采样率
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 根据采样率加载图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFileDescriptor(fd, null, options);
	}
	
	/**
	 * 计算采样率
	 * @param options
	 * @param reqWidth 需求的图片宽度
	 * @param reqHeight 需求的图片高度
	 * @return
	 */
	private int calculateInSampleSize(BitmapFactory.Options options, 
			int reqWidth, int reqHeight) {
		if(reqWidth == 0 || reqHeight == 0) {
			return 1;
		}	
		// 获得图片原始宽高度
		final int width = options.outWidth;
		final int height = options.outHeight;
		int inSampleSize = 1;      // 采样率
		if(height > reqHeight || width > reqWidth) {
			final int halfWidth = width / 2;
			final int halfHeight = height / 2;
			while((halfWidth/inSampleSize) >= reqWidth
					&& (halfHeight/inSampleSize) >= reqHeight) {
				// 增大采样率
				inSampleSize *= 2;
			}
		}
		
		return inSampleSize;
	}
}
