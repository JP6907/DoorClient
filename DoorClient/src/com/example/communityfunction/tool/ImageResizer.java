package com.example.communityfunction.tool;

import java.io.FileDescriptor;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * ��ͼƬ���в���, ѹ��
 */
public class ImageResizer {

	/**
	 * ����Դ���������ͼƬ
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public Bitmap decodeSampleBitmapFromResource(Resources res, 
			int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// ֻ����ͼƬ���
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// ���������
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// ���ݲ����ʼ���ͼƬ
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	
	/**
	 * ���ļ����������ͼƬ
	 * @param fd
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, 
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// ֻ����ͼƬ���
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fd, null, options);
		// ���������
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// ���ݲ����ʼ���ͼƬ
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFileDescriptor(fd, null, options);
	}
	
	/**
	 * ���������
	 * @param options
	 * @param reqWidth �����ͼƬ���
	 * @param reqHeight �����ͼƬ�߶�
	 * @return
	 */
	private int calculateInSampleSize(BitmapFactory.Options options, 
			int reqWidth, int reqHeight) {
		if(reqWidth == 0 || reqHeight == 0) {
			return 1;
		}	
		// ���ͼƬԭʼ��߶�
		final int width = options.outWidth;
		final int height = options.outHeight;
		int inSampleSize = 1;      // ������
		if(height > reqHeight || width > reqWidth) {
			final int halfWidth = width / 2;
			final int halfHeight = height / 2;
			while((halfWidth/inSampleSize) >= reqWidth
					&& (halfHeight/inSampleSize) >= reqHeight) {
				// ���������
				inSampleSize *= 2;
			}
		}
		
		return inSampleSize;
	}
}
