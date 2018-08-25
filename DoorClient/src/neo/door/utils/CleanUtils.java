package neo.door.utils;

import java.io.File;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

/**
 * �����湤����
 * @author leo
 *
 */
public class CleanUtils {
	
	/**
	 * ������û���
	 * @param context
	 */
	public static boolean cleanInternalCache(Context context) {
		return deleteFilesByDir(context.getCacheDir());
	}
	
	/**
	 * ��������ļ�����
	 * @param context
	 */
	public static boolean cleanInternalFile(Context context) {
		return deleteFilesByDir(context.getFilesDir());
	}
	
	/**
	 * ������ݿ�
	 * @param context
	 */
	@SuppressLint("SdCardPath")
	public static boolean cleanDatabases(Context context) {
		return deleteFilesByDir(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}
	
	/**
	 * ���SharedPreference
	 * @param context
	 */
	@SuppressLint("SdCardPath")
	public static boolean cleanSheredPreference(Context context) {
		return deleteFilesByDir(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}
	
	/**
	 * ���SD������
	 * @param context
	 */
	public static boolean cleanExternalCache(Context context) {
		if(Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return deleteFilesByDir(context.getExternalCacheDir());
		}
		return false;
	}
	
	/**
	 * ���SD���ļ�����
	 * @param context
	 */
	public static boolean cleanExternalFile(Context context) {
		if(Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return deleteFilesByDir(context.getExternalFilesDir(null));
		}
		return false;
	}
	
	public static void cleanAppData(Context context) {
		cleanInternalCache(context);
		cleanInternalFile(context);
		cleanDatabases(context);
		cleanSheredPreference(context);
		cleanExternalCache(context);
		cleanExternalFile(context);
	}
	
	private static boolean deleteFilesByDir(File dir) {
		if(dir != null && dir.exists() && dir.isDirectory()) {
			for(File file : dir.listFiles()) {
				if(file.isDirectory()) {
					deleteFilesByDir(file);
				}
				file.delete();
			}
			return true;
		}
		return false;
	}
}
