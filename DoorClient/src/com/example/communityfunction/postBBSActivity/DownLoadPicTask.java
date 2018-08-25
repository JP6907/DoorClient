//package com.example.communityfunction.postBBSActivity;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.List;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.Toast;
//import neo.door.usermanager.UserConfig;
//
///**
// * 根据URL地址下载图片
// * @author Administrator
// *
// */
//
//public class DownLoadPicTask extends AsyncTask {
//
//	private Activity mContext;
//	private ProgressDialog progressDialog;
//	private ImageView imageView;
//	private String userPhone;
//	private String fileName;
//	private String postDate;
//	private boolean isShowImage;
//	private boolean isSaveImage;
//	
//	private GetImageView getImageView;
//	
//	/**
//	 * 
//	 * @param context     Activity
//	 * @param isShowImage	是否显示图像
//	 * @param imageView		显示图像的imageview
//	 * @param isSaveImage	是否保存图片
//	 * @param userPhone		用户手机号码
//	 * @param postDate		发贴日期
//	 * @param fileName		保存的文件名
//	 */
//	public DownLoadPicTask(Activity context, boolean isShowImage, ImageView imageView, boolean isSaveImage,
//			String userPhone,String fileName,String postDate) {
//		this.mContext = context;
//		this.isShowImage = isShowImage;
//		this.imageView = imageView;
//		this.isSaveImage = isSaveImage;
//		this.userPhone = userPhone;
//		this.fileName = fileName;
//		this.postDate = postDate;
//		
//	}
//
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//		progressDialog = new ProgressDialog(mContext);
//		progressDialog.show();
//		
//	}
//
//	@Override
//	protected Object doInBackground(Object... params) {
//		Bitmap result = null;
//
//		try {
//			Log.e("aaaa", "rr");
//			result = downImage((String) params[0]);
//			Log.e("aaaa", "tt");
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			result = null;
//		}
//		return result;
//	}
//
//	@Override
//	protected void onPostExecute(Object result) {
//		super.onPostExecute(result);
//		progressDialog.cancel();
//		if (result != null) {
//			Toast.makeText(mContext, "下载成功!", Toast.LENGTH_SHORT).show();
//			if (isShowImage) {
//				try {
//					imageView.setImageBitmap((Bitmap) result);
//				} catch (Exception e) {
//					// TODO: handle exception
//					Log.e("qqqqqq", e.toString());
//				}
//			}
//			if(isSaveImage){
//				boolean saveResult = saveBitmap((Bitmap)result,userPhone,postDate,fileName);
//				if(saveResult){
//					Toast.makeText(mContext, "保存成功!", Toast.LENGTH_SHORT).show();
////					getImageView.getImage();
//				}else{
//					Toast.makeText(mContext, "保存失败!", Toast.LENGTH_SHORT).show();
//				}
//			}
//		} else {
//			Toast.makeText(mContext, "下载失败!", Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	private Bitmap downImage(String uriPic) {
//		URL imageUrl = null;
//		Bitmap bitmap = null;
//		try {
//			imageUrl = new URL(uriPic);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		try {
//			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
//			conn.setDoInput(true);
//			conn.connect();
//			Log.e("e", "dasdfghjk");
//			InputStream is = conn.getInputStream();
//			Log.e("4", "dasdfghjk");
//			bitmap = BitmapFactory.decodeStream(is);
//			is.close();
//			Log.e("1", "dasdfghjk");
//			return bitmap;
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			Log.e("f", e.getMessage());
//		}
//		return null;
//	}
//	
//	/**
//	 * 保存 Bitmap 到文件
//	 * @param bitmap 
//	 * @param userPhone 
//	 * @param postDate
//	 * @param fileName	保存的文件名
//	 * @return
//	 */
//	private boolean saveBitmap(Bitmap bitmap, String userPhone, String postDate, String fileName){
//		boolean result = false;
//		/*
//		 * 原时间格式为  2000-10-10 12:12:12
//		 * 去除所有 '-' ' ' ':'
//		 * 转化为 20001010121212
//		 */
//		postDate = postDate.replaceAll("[:,\\s+,-]", "");
//		String bbsPath = UserConfig.getBBSPicCachePath(postDate);
//		OutputStream outputStream;
//		BufferedOutputStream bos = null;
//		try {
//			outputStream = new FileOutputStream(bbsPath + "/" + fileName);
//			bos = new BufferedOutputStream(outputStream);
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//			
//			result = true;
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			bos.flush();
//			bos.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return result;
//	}
//	public void setGetImageViewListener(GetImageView getImageView)
//	{
//		this.getImageView=getImageView;
//	}
//	
//	public interface GetImageView
//	{
//		void getImage();
//	}
//
//}
