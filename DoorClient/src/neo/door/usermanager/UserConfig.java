package neo.door.usermanager;

import java.io.File;

import android.util.Log;

public class UserConfig {
	private static String RootPath = "/sdcard/Huikaimen/";
	private static String UserRootPath = "/sdcard/Huikaimen/User/";
	public static String DefaultUserPath ;
	public static String CachePath ;
	public static String BBSCachePath ;
	public static String BBSPicCachePath ;
	public static String NoticeCachePath ;
	public static String TimeCachePath ;
	public static String FriendCachePath;
	public static String MyHeadPath ;
	public static String MessageCachePath;
	
	public static String TAG="UserConfig";
	/**
	 * 创建根文件夹
	 * 
	 * @return
	 */
	private static String getRootPath() {
		File f = new File(RootPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		File f1 = new File(UserRootPath);
		if (!f1.exists()) {
			f1.mkdir();
		}
		return UserRootPath;
	}

	private static String getDefaultUserPath() {
		getRootPath();
		File f = new File(DefaultUserPath);
		if (!f.exists()) {
			f.mkdir();
		}
		return DefaultUserPath;
	}

	public static String getCachePath() {
		getDefaultUserPath();
		File f = new File(CachePath);
		if (!f.exists()) {
			f.mkdir();
		}
		return CachePath;
	}

	public static String getBBSCachePath(){
		getCachePath();
		File f = new File(BBSCachePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		return BBSCachePath;
	}
	/**
	 * 獲取對應時間下的BBS目録
	 * 传入时间格式为 去除所有 '-' ' ' ':'
	 * 转化为 20001010121212
	 * @param postDate
	 * @return
	 */
	public static String getBBSPicCachePath(String postDate){
		getCachePath();
		File f = new File(BBSPicCachePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		File f1 = new File(BBSPicCachePath + "/" + postDate);
		if(!f1.exists()){
			f1.mkdir();
		}
		return BBSPicCachePath + "/" + postDate;
	}
	
	public static String getNoticeCachePath() {
		getCachePath();
		File f = new File(NoticeCachePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		return NoticeCachePath;
	}

	public static String getTimeCachePath() {
		getCachePath();
		File f = new File(TimeCachePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		return TimeCachePath;
	}

	public static String getFriendCachePath() {
		getCachePath();
		File f = new File(FriendCachePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		return FriendCachePath;
	}

	public static String getMyHeadPath() {
		Log.e(TAG+"  "+"getMyHEadPath", ""+UserManager.getPhone());
		Log.e(TAG+"  "+"getMyHEadPath", ""+DefaultUserPath);
		getCachePath();
		File f = new File(MyHeadPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		return MyHeadPath;
	}

	public static String getMessageCachePath() {
		getCachePath();
		File f = new File(MessageCachePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		return MessageCachePath;
	}
}