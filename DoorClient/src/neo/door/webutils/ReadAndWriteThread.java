package neo.door.webutils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import neo.door.usermanager.Friend;
import neo.door.usermanager.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ReadAndWriteThread extends Thread{
	
	private Selector selector = null;
	private Charset charset = Charset.forName("utf-8");
	public SocketChannel clientChannel = null; 
	public Handler sendHandler;                                  // 负责发送信息
	private Handler updateHandler;                               // 负责更新UI
	boolean flag = true;
	private Context mContext;
	
	public ReadAndWriteThread(Context context, Handler updateHandler){
		mContext = context;
		this.updateHandler = updateHandler;
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	public void run() {
		
		// 连接
		connect();
		
		if(UserManager.getIsConnected()) {
			// 在开线程来读取服务器信息...
			new Thread(){
				public void run() {
					try {				
						while(flag && selector.select() > 0){
							handleRead();
						}
						System.out.println("******读取线程停止");
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
			
			// 创建Handler与主线程交互,用来发信信
			Looper.prepare();
			sendHandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					if(clientChannel != null){
						switch (msg.what) {
						case 0x001:
							try {
								String content = Protocal.CHAT_FLAG + msg.obj.toString();
								clientChannel.write(charset.encode(content));
							} catch (IOException e) {
								UserManager.setIsConnected(false);
								e.printStackTrace();
							}
						
						break;
						
						case 0x002:
							try {
								String content = Protocal.QUIT_FLAG + msg.obj.toString();
								clientChannel.write(charset.encode(content));
								clientChannel.close();
								flag = false;
								this.getLooper().quit();
							} catch (IOException e) {
								UserManager.setIsConnected(false);
								e.printStackTrace();
							}
							break;
						default:
							break;
						}
					}
				}
			};
			Looper.loop();
			System.out.println("******发送线程停止");
		}
	}
	
	private void connect(){
		try {
			selector = Selector.open();
			InetSocketAddress isa = new InetSocketAddress(Config.IP, Config.PORT);
			clientChannel = SocketChannel.open(isa);
			// 非阻塞工作
			clientChannel.configureBlocking(false);
			// 注册
			clientChannel.register(selector, SelectionKey.OP_READ);
			// 把用户名发送给服务器,通知其他用户该帐号已登录
			clientChannel.write(charset.encode(Protocal.LOGIN_FLAG + UserManager.getUsername()));
			UserManager.setIsConnected(true);
			System.out.println("连接成功");
		} catch (Exception e) {
			System.out.println("连接服务器失败");
			UserManager.setIsConnected(false);
			updateHandler.sendEmptyMessage(0x004);
			e.printStackTrace();
		}
	}
	
	
	private void handleRead() throws Exception {
		for(SelectionKey sk : selector.selectedKeys()){           // 得到可以进行IO操作的SelectionKsy
			// 删除
			selector.selectedKeys().remove(sk);
			if(sk.isReadable()){
				SocketChannel sc = (SocketChannel)sk.channel();           // 得到可读的Channel
				ByteBuffer buff = ByteBuffer.allocate(1024);
				String content = "";
				// 读取数据
				while(sc.read(buff) > 0){
					sc.read(buff);
					buff.flip();
					content += charset.decode(buff);
					buff.clear();
				}
				System.out.println("***content:" + content);
				if(content.equals(""))
					break;
					
				String flag = content.substring(0, 4);
				String msgs = content.substring(4);
				System.out.println("***flag:" + flag);
				System.out.println("***msgs:" + msgs);
				if(flag.equals(Protocal.CHAT_FLAG)) {
					chatMsg(msgs);
				}else if(flag.equals(Protocal.LOGIN_FLAG)) {
					loginMsg(msgs);
				}else if(flag.equals(Protocal.UPDATE_FLAG)) {
					updateMsg(msgs);
				}else if(flag.equals(Protocal.QUIT_FLAG)) {
					quitMsg(msgs);
				}
				// 为下次读取做准备
				sk.interestOps(SelectionKey.OP_READ);
			}
		}
	}
	
	private void chatMsg(String msgs) throws IOException, JSONException {
		JSONObject jObject = new JSONObject(msgs);
		String sender = jObject.getString("SENDER");
		String reciever = jObject.getString("RECIEVER");
		String friend = null;
		if(reciever.equals("群聊") || sender.equals(UserManager.getUsername())) {
			friend = reciever;
		}else {
			friend = sender;
		}
		FileUtil.writeMsgToFile(mContext, msgs, friend);
		Message msg = new Message();
		msg.what = 0x001;
		msg.obj = friend;
		updateHandler.sendMessage(msg);
	}
	
	private void loginMsg(String msgs) throws JSONException {
		// 新用户登录
		System.out.println("***新用户登录");
		JSONArray json = new JSONArray(msgs);
		String name = json.getJSONObject(0).getString("USERNAME");
		String path = json.getJSONObject(0).getString("PATH");
		System.out.println("name="+name + ", path=" + path);
		if(!UserManager.getUsername().equals(name)) {
			FileUtil.getAndSavePic(mContext, name, path);
			Message msg = new Message();
			msg.what = 0x002;
			msg.obj = new Friend(name, Config.WEB + "/DoorServer/" + path);
			updateHandler.sendMessage(msg);
		}					
	}
	
	private void updateMsg(String msgs) throws JSONException {
		// 更新好友列表
		System.out.println("***更新好友列表");
		JSONArray json = new JSONArray(msgs);
		int num = json.getJSONObject(0).getInt("NUM");       // 得到在线人数
		for(int i=1; i<num + 1; i++) {
			String name = json.getJSONObject(i).getString("USERNAME");
			String path = json.getJSONObject(i).getString("PATH");
			System.out.println("name="+name + ", path=" + path);
			FileUtil.getAndSavePic(mContext, name, path);
			Message msg = new Message();
			msg.what = 0x002;
			msg.obj = new Friend(name, Config.WEB + "/DoorServer/" + path);
			updateHandler.sendMessage(msg);
		}
	}
	
	private void quitMsg(String msgs) {
		Message msg = new Message();
		msg.what = 0x003;
		msg.obj = msgs;
		updateHandler.sendMessage(msg);
	}

}
