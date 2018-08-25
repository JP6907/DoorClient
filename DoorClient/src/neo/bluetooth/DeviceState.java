package neo.bluetooth;

public class DeviceState {
	private String msg;
	private boolean isParied;

	public DeviceState(String msg, boolean isParied) {
		this.msg = msg;
		this.isParied = isParied;
	}
	
	/**
	 * ������Ϣ
	 * @param msg ��Ϣ
	 */
	public void setMsg(String msg){
		this.msg = msg; 
	}
	
	/**
	 * @return ������Ϣ
	 */
	public String getMsg(){
		return msg;
	}
	
	/**
	 * ����ƥ��״̬
	 * @param isParied �Ƿ�ƥ��
	 */
	public void setParied(boolean isParied){
		this.isParied = isParied;
	}
	
	/**
	 * @return ����ƥ��״̬
	 */
	public boolean getParied(){
		return isParied;
	}
}
