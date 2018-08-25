package com.example.communityfunction.tool;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import neo.door.usermanager.UserConfig;
import neo.door.utils.ScreenUtil;

public class ToolDownLoadPic {
	/**
	 * 默认保存目录
	 */

	public static Bitmap downLoad(String uri,String userPhone, String fileName, String postDate,ImageView imageView) 
	{
		Bitmap result = null;
		Log.e("从服务器上下载", "从服务器上下载");

		try 
		{
			result = downImage(uri,imageView);//下载图片
			if(result!=null)
				saveBitmap(result, userPhone, fileName, postDate);//保存到本地文件夹

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	/**
	 * 下载图片
	 * @param uriPic
	 * @return
	 */
	private static Bitmap downImage(String uriPic,ImageView imageView) 
	{
		URL imageUrl = null;
		Bitmap bitmap = null;
		try 
		{
			imageUrl = new URL(uriPic);
		}
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		try 
		{
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			
			
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds=true;
			bitmap=BitmapFactory.decodeStream(is, null, options);
//			
			int inSampleSize=ImagesTool.caculateInSampleSize(options, imageView);
////			
//	     	is.close();
//			conn.disconnect();
////			
			conn = (HttpURLConnection) imageUrl.openConnection();
//			conn.setDoInput(true);
//			conn.connect();
			is = conn.getInputStream();
////			
			options.inJustDecodeBounds=false;
			options.inSampleSize=inSampleSize;
			bitmap=BitmapFactory.decodeStream(is, null, options);
////			
//			BitmapFactory 
//			bitmap = BitmapFactory.decodeStream(is);
			is.close();
//			conn.disconnect();
			return bitmap;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存 Bitmap 到文件
	 * @param bitmap 
	 * @param userPhone 
	 * @param postDate
	 * @param fileName	保存的文件名
	 * @return
	 */
	private static boolean saveBitmap(Bitmap bitmap, String userPhone, String fileName, String postDate)
	{
		boolean result = false;
		/*
		 * 原时间格式为  2000-10-10 12:12:12
		 * 去除所有 '-' ' ' ':'
		 * 转化为 20001010121212
		 */
		postDate = postDate.replaceAll("[:,\\s+,-]", "");
		String picPath = UserConfig.getBBSPicCachePath(postDate);
		OutputStream outputStream;
		BufferedOutputStream bos = null;
		try 
		{
			outputStream = new FileOutputStream(picPath + "/" + fileName);
			bos = new BufferedOutputStream(outputStream);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			
			result = true;
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		try 
		{
			bos.flush();
			bos.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
