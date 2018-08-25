package neo.door.usermanager;

public class Chat {
	private String username;
	private String msg;
	private int type;
	
	public Chat(String username, String msg, int type){
		this.username = username;
		this.msg = msg;
		this.type = type;
	}
	
	public String getName(){
		return username;
	}
	
	public void setName(String username){
		this.username = username;
	}
	
	public int getType() {
		return type;
	}
	
	public String getMsg() {
		return msg;
	}
}
