package neo.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothConnect {
										 
	private static final String mUUID = "00001101-0000-1000-8000-00805F9B34FB";         // UUID: ȫ��Ψһ��ʶ��
	private String bluetoothAddress;                                                    // ������ַ
	private boolean isConnected;                                                        // ����״̬
	private BluetoothDevice device = null;                                              // �����豸
	private BluetoothSocket socket = null;                                              // �����ͷ���Socket
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  // �������������
	private ConnectThread connectThread;                 		                        // ���������߳�
	private ReadDataThread readDataThread;                                              // ��ȡ�����߳�
	
	private String doorId = "";
	
	
	/**
	 * ������  ������������
	 * @param bluetoothAddress ������ַ
	 */
	public BluetoothConnect(String bluetoothAddress)
	{
		
		this.bluetoothAddress = bluetoothAddress;
		isConnected = false;
	}
	
	/**
	 * ��������
	 * @param address
	 */
	public void connect(){
	
		if(!bluetoothAddress.equals("")){
			// ��ȡ�����ӵ������豸
			device = mBluetoothAdapter.getRemoteDevice(bluetoothAddress);
			// ���������豸
			connectThread = new ConnectThread();
			connectThread.start();
		}
	}

	
	// �����߳�
	private class ConnectThread extends Thread{
		@Override
		public void run() {
			
			try {    
				socket = device.createRfcommSocketToServiceRecord(UUID.fromString(mUUID));
//				Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
//				socket = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
				
				// ���ӽ���֮ǰ�������    
			    if (device.getBondState() == BluetoothDevice.BOND_NONE) {    
			         Method creMethod = BluetoothDevice.class.getMethod("createBond");    
			         Log.e("TAG", "��ʼ���");    
			         creMethod.invoke(device);    
			    }   
			}catch (Exception e) {    
			     System.out.println("�޷�ƥ��"); 
			     e.printStackTrace();    
			 }    
			if(mBluetoothAdapter.isDiscovering()){
				mBluetoothAdapter.cancelDiscovery();
			} 
			
			try {    		
			    socket.connect();    
			    
			    setConnect(true);                                 // ���ӳɹ�
				System.out.println("******connectture");
				
				if(getConnect()){
					// ��ȡ����      
					readDataThread = new ReadDataThread();
			        readDataThread.start();
			        // ���ͱ�ʶ��
			        sendOrder("0102");
				}
						
			}catch(IOException e){    
			      try {    
			          socket.close();  
			          socket = null;  
			          setConnect(false);                      	 // ����ʧ��
			          System.out.println("******connectfalse");
			          
			          if(readDataThread != null){
				          readDataThread.interrupt();
				          readDataThread = null;
			          }
	          
			      }catch (IOException e2) {    
			    	  e.printStackTrace();
			      }    
			 } 
		}
	}
	
	/**
	 * @return �����ӳɹ�����true, ���򷵻�false
	 */
	public boolean getConnect(){
		return isConnected;
	}
	
	/**
	 * ��������״̬
	 * @param isConnected
	 */
	public void setConnect(boolean isConnected){
		this.isConnected = isConnected;
	}
	
	
	public String getDoorID(){
		return doorId;
	}
	
	/**
	 * ��������
	 * @param order ����
	 */
	public boolean sendOrder(String order){
		
		if(socket == null){
			return false;
		}
		try {
			// ��ȡ�����
			OutputStream out = socket.getOutputStream();
			out.write(order.getBytes());
			//out.flush();
			return true;
		} catch (IOException e) {
			Log.v("Control", "�쳣");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean comfirmDoorID(){
		
		boolean result = false;
		if(getConnect()){         // ��������		
	        if(doorId.startsWith("DOOR_")){
	        	result = true;
	        	System.out.println("��������"+ doorId);
	        }
		}    
        return result;
	}
	
	/**
	 * ��ȡ��Ƭ�����ص���Ϣ
	 */
	public class ReadDataThread extends Thread{
		
		@Override
		public void run() {
			
			byte[] buffer = new byte[1024];
			int bytes;
			InputStream in = null;
			
			try{
				in = socket.getInputStream();    // ��ȡ������
			}catch (IOException e) {
				System.out.println("#########1");
				e.printStackTrace();
			}
			
			// ��ȡ����
			boolean flag = false;
			while(!flag){
				try {
					if((bytes = in.read(buffer)) > 0){
						byte[] data = new byte[bytes];
						for(int i=0; i<bytes; i++){
							data[i] = buffer[i];
						}
						String dataString = new String(data);
						Log.i("BlueTooth", "���յ����ص����ݣ�" + dataString);
						doorId += dataString;
					}else {
						flag = true;
					}
				} catch (IOException e) {
					System.out.println("#########2");
					try {
						in.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					break;
				}
			}
		}
	}
	
	
	/**
	 * �ر�����
	 */
	public void shutdownConnection(){
		new Thread(){
			public void run(){
				
				setConnect(false);
				
				if(connectThread != null){
					connectThread.interrupt();
					connectThread = null;
				}
				
				if(readDataThread != null){
					readDataThread.interrupt();
					readDataThread = null;
				}
				
				if(socket != null){
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					socket = null;
				}
			}
		}.start();
	}
	
	
}
