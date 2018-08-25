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
										 
	private static final String mUUID = "00001101-0000-1000-8000-00805F9B34FB";         // UUID: 全球唯一标识符
	private String bluetoothAddress;                                                    // 蓝牙地址
	private boolean isConnected;                                                        // 连接状态
	private BluetoothDevice device = null;                                              // 蓝牙设备
	private BluetoothSocket socket = null;                                              // 蓝牙客服端Socket
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  // 获得蓝牙适配器
	private ConnectThread connectThread;                 		                        // 连接蓝牙线程
	private ReadDataThread readDataThread;                                              // 读取数据线程
	
	private String doorId = "";
	
	
	/**
	 * 构造器  尝试连接蓝牙
	 * @param bluetoothAddress 蓝牙地址
	 */
	public BluetoothConnect(String bluetoothAddress)
	{
		
		this.bluetoothAddress = bluetoothAddress;
		isConnected = false;
	}
	
	/**
	 * 连接蓝牙
	 * @param address
	 */
	public void connect(){
	
		if(!bluetoothAddress.equals("")){
			// 获取需连接的蓝牙设备
			device = mBluetoothAdapter.getRemoteDevice(bluetoothAddress);
			// 连接蓝牙设备
			connectThread = new ConnectThread();
			connectThread.start();
		}
	}

	
	// 连接线程
	private class ConnectThread extends Thread{
		@Override
		public void run() {
			
			try {    
				socket = device.createRfcommSocketToServiceRecord(UUID.fromString(mUUID));
//				Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
//				socket = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
				
				// 连接建立之前的先配对    
			    if (device.getBondState() == BluetoothDevice.BOND_NONE) {    
			         Method creMethod = BluetoothDevice.class.getMethod("createBond");    
			         Log.e("TAG", "开始配对");    
			         creMethod.invoke(device);    
			    }   
			}catch (Exception e) {    
			     System.out.println("无法匹配"); 
			     e.printStackTrace();    
			 }    
			if(mBluetoothAdapter.isDiscovering()){
				mBluetoothAdapter.cancelDiscovery();
			} 
			
			try {    		
			    socket.connect();    
			    
			    setConnect(true);                                 // 连接成功
				System.out.println("******connectture");
				
				if(getConnect()){
					// 读取数据      
					readDataThread = new ReadDataThread();
			        readDataThread.start();
			        // 发送标识码
			        sendOrder("0102");
				}
						
			}catch(IOException e){    
			      try {    
			          socket.close();  
			          socket = null;  
			          setConnect(false);                      	 // 连接失败
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
	 * @return 若连接成功返回true, 否则返回false
	 */
	public boolean getConnect(){
		return isConnected;
	}
	
	/**
	 * 设置连接状态
	 * @param isConnected
	 */
	public void setConnect(boolean isConnected){
		this.isConnected = isConnected;
	}
	
	
	public String getDoorID(){
		return doorId;
	}
	
	/**
	 * 发送命令
	 * @param order 命令
	 */
	public boolean sendOrder(String order){
		
		if(socket == null){
			return false;
		}
		try {
			// 获取输出流
			OutputStream out = socket.getOutputStream();
			out.write(order.getBytes());
			//out.flush();
			return true;
		} catch (IOException e) {
			Log.v("Control", "异常");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean comfirmDoorID(){
		
		boolean result = false;
		if(getConnect()){         // 若已连接		
	        if(doorId.startsWith("DOOR_")){
	        	result = true;
	        	System.out.println("已连接上"+ doorId);
	        }
		}    
        return result;
	}
	
	/**
	 * 读取单片机返回的信息
	 */
	public class ReadDataThread extends Thread{
		
		@Override
		public void run() {
			
			byte[] buffer = new byte[1024];
			int bytes;
			InputStream in = null;
			
			try{
				in = socket.getInputStream();    // 获取输入流
			}catch (IOException e) {
				System.out.println("#########1");
				e.printStackTrace();
			}
			
			// 读取数据
			boolean flag = false;
			while(!flag){
				try {
					if((bytes = in.read(buffer)) > 0){
						byte[] data = new byte[bytes];
						for(int i=0; i<bytes; i++){
							data[i] = buffer[i];
						}
						String dataString = new String(data);
						Log.i("BlueTooth", "接收到返回的数据：" + dataString);
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
	 * 关闭连接
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
