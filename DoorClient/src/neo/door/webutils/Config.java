package neo.door.webutils;

public class Config {
	
	public static final String IP = "123.207.117.75";
	//public static final String IP = "192.168.1.104";
	public static final String WEB = "http://"+IP+":8092";
	public static final int PORT = 30011;
	public static final String URL = WEB + "/DoorServer/servlet/";
	public static final String DownLoadBBSPath = WEB + "/DoorServer/Upload/BBS/";
	public static final String DownLoadHeadImagePath = WEB + "/DoorServer/Upload/HeadImage/";
	public static final String METHOD_UPLOAD = "UploadPicServlet";
	public static final String METHOD_LOGIN = "LoginServlet";
	public static final String METHOD_REGISTER = "RegisterServlet";
	public static final String METHOD_UPDATE = "UpdateServlet";
	public static final String METHOD_DOWNLOAD = "GetPicServlet";
	public static final String METHOD_OPEN_BY_GPRS = "OpenDoorByGprsServlet";
	//发贴
	public static final String METHOD_POSTBBS = "PostBbsServlet";
	//帖子回复
	public static final String METHOD_BBSREPLY = "BbsReplyServlet";
	//获取帖子数据
	public static final String GetNoticeMethod = "GetNoticeServlet";
	public static final String GetBBSPostDateMethod = "GetPostDataServlet";
	//获取帖子回复数据
	public static final String GetBBSReplyDateMethod = "GetReplyDataServlet";
	public static final String GetNoticeReplyMethod = "GetNoticeReplyServlet";

	public static final String FindPasswordMethod = "FindPasswordServlet";
	//获取用户数据
	public static final String GetMyFriendMethod = "QueryUserListServlet";

	public static final String QueryUser = "QueryUserServlet";
	public static final int LOGIN_SUCCEED = 1;
	public static final int LOGIN_FAILED = 0;
	public static final int LOGIN_NO_NETWORK = -1;
}
