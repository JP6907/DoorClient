package neo.bluetooth;

import java.util.ArrayList;
import java.util.Set;

import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import neo.adapter.MyBluetoothBaseAdapter;
import neo.door.inputpass.LoadingDialog;
import neo.door.usermanager.UserManager;
import neo.door.utils.ChangeStatusBarColor;
import neo.door.utils.MyToast;

public class BluetoothOpenDoor extends Activity implements OnClickListener{
	
	//private BlueTooth bluetooth;
	
	String phone;
	String passWord;
	// 组件
	private ListView mListView;
	private ArrayList<DeviceState> list;
	private Button btOPenDoor;
	private ImageButton btnReturnBack;
	private TextView tvTitle;
	private EditText ETpassWord;
	
	private String bluetoothAddress = null;
	private BluetoothConnect btConnect;
	
	// 保存上次连接的地址
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	private MyBluetoothBaseAdapter mAdapter;   // 优化的BaseAdapter
	
	private Context mContext;
	private MyHandler mHandler;         // 消息处理
	private Dialog mDialog;             // 对话框
	private MyToast mToast;
	
	private boolean isSearching = false;
	
	private final static int DISCONNECT = 0x999;
	private final static int SEARCH = 0x998;
	
	/* 取得默认的蓝牙适配器 */
	private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

	@SuppressWarnings("deprecation")
	@SuppressLint("ResourceAsColor")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_open_door);
        
        //bluetooth = new BlueTooth(BluetoothAdapter.getDefaultAdapter(), this);
        
        ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
        
        init();
        
        preferences = getSharedPreferences("DOOR", MODE_PRIVATE);
        editor = preferences.edit();
  
     // 若蓝牙没开，开启蓝牙
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 1);     	
        }else{  	
        	autoConnect();
        }
    } 
    
   
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	if(requestCode == 1){
    		if(resultCode == RESULT_OK){
    			System.out.println("蓝牙已打开");
    			autoConnect();
    		}else if(resultCode == RESULT_CANCELED){
    			System.out.println("取消打开");
    		}
    	}
    }
    
    
    public void autoConnect(){
    	bluetoothAddress = preferences.getString("BLUETOOTHADDRESS", null);	
    	System.out.println("*******address:" + bluetoothAddress);
    	if(bluetoothAddress != null){
    				btConnect = new BluetoothConnect(bluetoothAddress);     
        	        btConnect.connect();   	
    	}
    }
    
   
    /**
     * 初始化
     */
	private void init() {	
		
		mContext = this;
		
		mHandler = new MyHandler();
		mToast = new MyToast(this);
		
		btnReturnBack = (ImageButton)findViewById(R.id.head_other_return_back);
		btOPenDoor = (Button)findViewById(R.id.bt_bluetooth_open_door);
		tvTitle = (TextView)findViewById(R.id.head_other_title);
		ETpassWord = (EditText)findViewById(R.id.bluetooth_password);
		tvTitle.setText("蓝牙开门");
		
		btnReturnBack.setOnClickListener(this);
		btOPenDoor.setOnClickListener(this);
		
		list = new ArrayList<DeviceState>();
		mAdapter = new MyBluetoothBaseAdapter(this, list);
		mListView = (ListView) findViewById(R.id.list);              
		mListView.setAdapter(mAdapter);                               // 设置适配器
		mListView.setFastScrollEnabled(true);                   
		mListView.setOnItemClickListener(mDeviceClickListener);	      // 监听	
        
		// 监听蓝牙开关状态
		IntentFilter stateFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		this.registerReceiver(mReceiver, stateFilter);
		
		// 监听蓝牙连接成功
		IntentFilter connectFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
		this.registerReceiver(mReceiver, connectFilter);
		
		// 监听蓝牙连接失败
		IntentFilter disconnectFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		this.registerReceiver(mReceiver, disconnectFilter);
		
		 // 发现蓝牙
        IntentFilter discoveryFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);    // 搜索到蓝牙
        this.registerReceiver(mReceiver, discoveryFilter);                                // 注册广播

        // 搜索完成
        IntentFilter foundFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);   // 搜索完成
        this.registerReceiver(mReceiver, foundFilter);                                             // 注册广播
        
        // 获取已匹配的设备
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        // 列出已匹配蓝牙设备
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
            	list.add(new DeviceState(device.getName() + "\n" + device.getAddress(), true));
            	mAdapter.notifyDataSetChanged();
        		mListView.setSelection(list.size() - 1);
            }
        } else {
        	mToast.show("没有蓝牙设备已匹配!", Toast.LENGTH_SHORT);
        }
		
	}    
	
	
	@Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        
        this.unregisterReceiver(mReceiver);
        
        if(btConnect != null){
        	btConnect.shutdownConnection();
        }
    }


	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()) {
			
		case R.id.bt_bluetooth_open_door:
			if(btConnect != null && btConnect.getConnect()){	
				//获取密码
				passWord = ETpassWord.getText().toString().trim();
				if(passWord==null||passWord.equals("")||passWord.length()!=6){
					mToast.show("请输入6位密码!", Toast.LENGTH_SHORT);
				} else{
					//获取用户手机号码
					phone = UserManager.getPhone();
					if(btConnect.sendOrder("+" + phone + passWord + "-")){                // 发送命令
						mHandler.sendEmptyMessage(0x005);         // 开门成功
					}else{
						mHandler.sendEmptyMessage(0x006);         // 开门失败
					}
				}
			}else{
				mHandler.sendEmptyMessage(0x004);             // 未连接
			}
			break;
			
		case R.id.head_other_return_back:
			BluetoothOpenDoor.this.finish();

		default:
			break;
		}
	}
		
		
    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
        	 
	        DeviceState item = list.get(arg2);          // 获取点击的list
	        String info = item.getMsg();
	        final String address = info.substring(info.length() - 17); 
   
	        AlertDialog.Builder StopDialog =new AlertDialog.Builder(mContext);//定义一个弹出框对象
	        StopDialog.setTitle("连接");//标题          
	        StopDialog.setMessage(item.getMsg());
	        StopDialog.setPositiveButton("连接", new DialogInterface.OnClickListener() {  
		        public void onClick(DialogInterface dialog, int which) { 
		        	
		        	// 若蓝牙没开，开启蓝牙
		            if (!mBtAdapter.isEnabled()) {
		                mToast.show("请先开启蓝牙", Toast.LENGTH_SHORT);
		            }else{
		            	 if(btConnect != null && btConnect.getConnect()){
				            	mToast.show("请先断开连接", Toast.LENGTH_SHORT);
				         }else{			        	
				        	bluetoothAddress = address;    // 保存点击的蓝牙地址
				        	editor.putString("BLUETOOTHADDRESS", bluetoothAddress);
				        	editor.commit();
				        	// 连接蓝牙
					        btConnect = new BluetoothConnect(bluetoothAddress);     
					        btConnect.connect(); 	
					        	
					        mDialog = LoadingDialog.createLoadDialog(mContext, "正在努力连接...", false);
					        mDialog.show();
					        LoadingDialog.closeDialog(7000, mDialog);
				         }
		            }
      
		         }  
	        });
	        StopDialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					
				}
			});        	
	        StopDialog.show();     
        }
    };	
    
    
    // 监听蓝牙的广播
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // 发现蓝牙设备触发
            if (BluetoothDevice.ACTION_FOUND.equals(action)) 
            {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                System.out.println("*****found: " + device);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)   // 若该蓝牙没匹配
                {
                	list.add(new DeviceState(device.getName() + "\n" + device.getAddress(), false));
                	mAdapter.notifyDataSetChanged();
            		mListView.setSelection(list.size() - 1);
                }
            } 
            // 搜索完成触发
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) 
            {
            	System.out.println("*****finish ");
                setProgressBarIndeterminateVisibility(false);
                if (mListView.getCount() == 0){
                	mToast.show("搜索不到蓝牙设备", Toast.LENGTH_SHORT);
                }
                mHandler.sendEmptyMessage(0x001);        // 搜索完成
            }
            // 蓝牙状态改变触发
            else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
            	System.out.println("*****change ");
            	if(!mBtAdapter.isEnabled()){            // 若关闭了蓝牙	
            		bluetoothAddress = null;
            		if(btConnect != null){
	            		btConnect.shutdownConnection();
            		}
            	}
            }
            // 蓝牙连接成功触发
            else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
            	System.out.println("*****connect ");
            	mHandler.sendEmptyMessageDelayed(0x002, 4000);     // 延时发送, 验证门号
            }
            // 蓝牙断开连接触发
            else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
            	System.out.println("*****disconnect ");
            	mHandler.sendEmptyMessage(0x003);
            }
        }
    };	
	
	
	
	// 消息处理
	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			
			case 0x000:
				mToast.show("请开启蓝牙!", Toast.LENGTH_SHORT);
				break;
				
			case 0x001:
				if(mDialog != null){
					mDialog.dismiss();
				}
				mToast.show("搜索完成!", Toast.LENGTH_SHORT);
				isSearching = false;
				break;
				
			case 0x002:
				if(mDialog != null){
					mDialog.dismiss();
				}
				if(btConnect.comfirmDoorID()){
					mToast.show("已连接门号:"+ btConnect.getDoorID(), Toast.LENGTH_SHORT);
				}else{
					mToast.show("门号不正确! 开门功能已关闭!", Toast.LENGTH_SHORT);
				}
				
				break;
				
			case 0x003:
				mToast.show("断开连接!", Toast.LENGTH_SHORT);
				break;
				
			case 0x004:
				mToast.show("请先连接!", Toast.LENGTH_SHORT);
				break;
				
			case 0x005:
				mToast.show("开门成功!", Toast.LENGTH_SHORT);
				mToast.show(phone + "," + passWord, Toast.LENGTH_SHORT);
//				Intent result = new Intent();
//				result.putExtra("a", "b");
//                setResult(DSRESULT_CODE, result);
				finish();
				break;
				
			case 0x006:
				mToast.show("开门失败!", Toast.LENGTH_SHORT);
				break;

			default:
				break;
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,DISCONNECT, 0, "断开连接");
		menu.add(0, SEARCH, 0, "搜索蓝牙设备");
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case DISCONNECT:
			
			if(btConnect != null){
				btConnect.shutdownConnection();
			}	
			break;
			
		case SEARCH:
			if(!isSearching){
				if(mBtAdapter.isEnabled()){                 // 蓝牙已开
					list.clear();                           // 清空列表
					mAdapter.notifyDataSetChanged();
						
					// 列出已匹配的
					Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
					if (pairedDevices.size() > 0){
					    for (BluetoothDevice device : pairedDevices) {
					        list.add(new DeviceState(device.getName() + "\n" + device.getAddress(), true));
					        mAdapter.notifyDataSetChanged();
					        mListView.setSelection(list.size() - 1);
					    }
					}else{
						mToast.show("没有蓝牙设备已匹配!", Toast.LENGTH_SHORT);
					 }					
				     /* 开始搜索 */
				     mBtAdapter.startDiscovery();
					 mDialog = LoadingDialog.createLoadDialog(mContext, "蓝牙搜索中...", true);
					 mDialog.show();	
					 isSearching = true;
				}else{
					mHandler.sendEmptyMessage(0x000);
				}
			}else{
				mToast.show("正在搜索...", Toast.LENGTH_SHORT);
			}
			
			break;

		default:
			break;
		}
		
		return true;
	}
	
}