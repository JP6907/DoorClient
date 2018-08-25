package neo.door.usermanager;


public class UserManager {
	
	private static int ID;
	private static String username;
	private static String phone;
	private static boolean isLogin = false;
	private static String building;
	private static String doorID;
	private static String userkey ="huikaimen";
	private static boolean isTiming = false;
	private static boolean isConnected = false;
	private static boolean isNeedRefreashOnBbs=true;//帖子是否需要刷新
	private static boolean isNeedRefreashOnNotice=true;//公告是否需要刷新
	private static boolean isReadMsg=true;//是否已经读取好友通知等信息
	
	public static int getID(){
		return ID;
	}
	
	public static void setID(int newID){
		ID = newID;
	}
	
	public static String getUsername(){
		return username;
	}
	
	public static void setUsername(String newUsername){
		username = newUsername;
	}
	
	public static String getPhone(){
		return phone;
	}
	
	public static void setPhone(String newPhone){
		phone = newPhone;
	}
	
	public static boolean getLoginStatu(){
		return isLogin;
	}
	
	public static void setLoginStatu(boolean newIsLogin){
		isLogin = newIsLogin;
	}
	
	public static String getBuilding(){
		return building;
	}
	
	public static void setBuilding(String newBuilding){
		building = newBuilding;
	}
	
	public static String getDoorID(){
		return doorID;
	}
	
	public static void setDoorID(String newDoorID){
		doorID = newDoorID;
	}
	
	public static boolean getIsTiming(){
		return isTiming;
	}
	
	public static void setIsTiming(boolean newIsTiming){
		isTiming = newIsTiming;
	}
	
	public static boolean getIsConnected(){
		return isConnected;
	}
	
	public static void setIsConnected(boolean newIsConnected){
		isConnected = newIsConnected;
	}

	public static boolean isNeedRefreashOnBbs() {
		return isNeedRefreashOnBbs;
	}

	public static void setNeedRefreashOnBbs(boolean isNeedRefreashOnBbs) {
		UserManager.isNeedRefreashOnBbs = isNeedRefreashOnBbs;
	}

	public static boolean isNeedRefreashOnNotice() {
		return isNeedRefreashOnNotice;
	}

	public static void setNeedRefreashOnNotice(boolean isNeedRefreashOnNotice) {
		UserManager.isNeedRefreashOnNotice = isNeedRefreashOnNotice;
	}

	public static void setIsReadMsg(boolean readState)
	{
		UserManager.isReadMsg=readState;
	}
	
	public static boolean getIsReadMsg()
	{
		return UserManager.isReadMsg;
	}

	public static String getkey(){
		return userkey;
	}
	
}
