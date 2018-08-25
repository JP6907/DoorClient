package neo.door.webutils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.widget.ImageView;
import neo.door.usermanager.UserConfig;

public class FileUtil {

	/**
	 * 保存文件
	 * @param username 已用户名作为文件子路�?
	 * @param filePath 文件路径
	 * @param fileName 文件�?
	 * @param bytes 数据
	 * @return
	 */
	public static String saveFile(Context context, String phone, String fileName, byte[] bytes){
		String fileFullName = "";
		FileOutputStream fos = null;
//		String filePath = context.getExternalCacheDir() + "/Images/" + username + "/";
		String filePath = UserConfig.getMyHeadPath();
		File file = new File(filePath);     // 创建文件夹
		if(!file.exists()) {
			file.mkdirs();                  // 创建路径目录
		}
		File fullFile = new File(filePath, fileName);
		fileFullName = fullFile.getPath();
		try {
			fos = new FileOutputStream(fullFile);
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			fileFullName = "";
			e.printStackTrace();
		} 
		
		return fileFullName;
	}
	
	public static String saveFile(Context context, String phone, String fileName, Bitmap bitmap) {
		byte[] bytes = bitmapToBytes(bitmap, 80);
		return saveFile(context, phone,fileName, bytes);
	}
	
	/**
	 * 压缩图片
	 * @param bitmap 图片
	 * @param quality 压缩0~100,100表示不压缩
	 * @return
	 */
	private static byte[] bitmapToBytes(Bitmap bitmap, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, quality, baos);
		return baos.toByteArray();
	}
	
	/**
	 * 更新头像
	 * @param imageView
	 */
	public static void setHeadImage(Context context, ImageView imageView, String username){
		// 更换照片保存的照片路�?
//		String filePath = context.getExternalCacheDir() + "/Images/" + username + "/" + "headImage.jpg";
		String filePath = UserConfig.getMyHeadPath()+"/"+"headImage.jpg";
		File file = new File(filePath);
		if(file.exists()){
			Bitmap bm = BitmapFactory.decodeFile(filePath);
			imageView.setImageBitmap(bm);
		}else{
			Resources res = context.getResources();
			Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.image_head);
			imageView.setImageBitmap(bm);
		}
	}
	
	public static void getAndSavePic(Context context, String name, String path) {
		Bitmap bitmap = null;
		bitmap = WebUtil.getPic(Config.WEB + "/DoorServer/" + path);
		System.out.println(name);
		System.out.println(path);
		// 保存图片在本地
		if(bitmap != null) {
			saveFile(context, name, "headImage.jpg", bitmap);
			System.out.println("****savafile");		
		}
	}
	
	/**
	 * 将消息保存在文件
	 * @param context 上下文
	 * @param content 内容
	 * @param sender 对话的好友
	 * @throws IOException
	 */
	@SuppressLint("SimpleDateFormat")
	public static void writeMsgToFile(Context context, String content, String friend) throws IOException {
		String filePath = context.getExternalCacheDir().getPath() + "/Msgs/" + friend + "/";
		System.out.println("**##filePath: " + filePath);
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		File txtFile = new File(file, "msg.txt");		
		if(!txtFile.exists()){
			txtFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(txtFile, true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
		bw.write(content+"\n");
		bw.close();
	}
}
