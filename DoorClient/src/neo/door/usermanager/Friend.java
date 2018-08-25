package neo.door.usermanager;

import android.widget.ImageView;


public class Friend {
	private ImageView headImage;
	private String username;
	private boolean hasMag;
	private String url;             // 图片的加载的地址并作为tag
	
	public Friend(String username, boolean hasMsg, String url) {
		this.username = username;
		this.hasMag = hasMsg;
		this.url = url;
	}
	
	public Friend(String username, String url){
		this.username = username;
		hasMag = false;
		this.url = url;
	}
	
	public ImageView getHeadImage(){
		return headImage;
	}
	
	public void setHeadImage(ImageView headImage){
		this.headImage = headImage;
	}

	public String getName(){
		return username;
	}
	
	public void setName(String username){
		this.username = username;
	}
	
	public boolean isHedMag(){
		return hasMag;
	}
	
	public void setMsg(boolean hasMsg){
		this.hasMag = hasMsg;
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
}
