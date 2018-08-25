package neo.bluetooth;

public class DeviceState {
	private String msg;
	private boolean isParied;

	public DeviceState(String msg, boolean isParied) {
		this.msg = msg;
		this.isParied = isParied;
	}
	
	/**
	 * 设置信息
	 * @param msg 信息
	 */
	public void setMsg(String msg){
		this.msg = msg; 
	}
	
	/**
	 * @return 返回信息
	 */
	public String getMsg(){
		return msg;
	}
	
	/**
	 * 设置匹配状态
	 * @param isParied 是否匹配
	 */
	public void setParied(boolean isParied){
		this.isParied = isParied;
	}
	
	/**
	 * @return 返回匹配状态
	 */
	public boolean getParied(){
		return isParied;
	}
}
