package neo.door.usermanager;

import com.hyphenate.easeui.domain.EaseUser;

public class User extends EaseUser{
	private String imgPath;
	private int judge;	// 1代表是好友 ,0代表不是好友，-1代表自己
	
	public User(String username) {
		super(username);
		// TODO Auto-generated constructor stub
	}
	public int getJudge(){
		return judge;
	}
	public String getImgPath(){
		return imgPath;
	}
	public void setUserName(String tem){
		this.username = tem;
	}
	public void setImgPath(String tem){
		imgPath = tem;
	}
	public void setJudge(int tem){
		judge = tem;
	}
	public void setAvatar(String avatar)
	{
		this.avatar=avatar;
	}
	public String getAvatar()
	{
		return this.avatar;
	}
	public String hash(String tem){
	String hashString = "";
		for(int i = 0 ; i < tem.length() ; i++){
			if(i%2 == 1)
			hashString += tem.charAt(i)+1;
			else hashString += tem.charAt(i)-1;	
		}
		return hashString;
	}
	
}
