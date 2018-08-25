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
	// ���
	private ListView mListView;
	private ArrayList<DeviceState> list;
	private Button btOPenDoor;
	private ImageButton btnReturnBack;
	private TextView tvTitle;
	private EditText ETpassWord;
	
	private String bluetoothAddress = null;
	private BluetoothConnect btConnect;
	
	// �����ϴ����ӵĵ�ַ
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	private MyBluetoothBaseAdapter mAdapter;   // �Ż���BaseAdapter
	
	private Context mContext;
	private MyHandler mHandler;         // ��Ϣ����
	private Dialog mDialog;             // �Ի���
	private MyToast mToast;
	
	private boolean isSearching = false;
	
	private final static int DISCONNECT = 0x999;
	private final static int SEARCH = 0x998;
	
	/* ȡ��Ĭ�ϵ����������� */
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
  
     // ������û������������
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
    			System.out.println("�����Ѵ�");
    			autoConnect();
    		}else if(resultCode == RESULT_CANCELED){
    			System.out.println("ȡ����");
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
     * ��ʼ��
     */
	private void init() {	
		
		mContext = this;
		
		mHandler = new MyHandler();
		mToast = new MyToast(this);
		
		btnReturnBack = (ImageButton)findViewById(R.id.head_other_return_back);
		btOPenDoor = (Button)findViewById(R.id.bt_bluetooth_open_door);
		tvTitle = (TextView)findViewById(R.id.head_other_title);
		ETpassWord = (EditText)findViewById(R.id.bluetooth_password);
		tvTitle.setText("��������");
		
		btnReturnBack.setOnClickListener(this);
		btOPenDoor.setOnClickListener(this);
		
		list = new ArrayList<DeviceState>();
		mAdapter = new MyBluetoothBaseAdapter(this, list);
		mListView = (ListView) findViewById(R.id.list);              
		mListView.setAdapter(mAdapter);                               // ����������
		mListView.setFastScrollEnabled(true);                   
		mListView.setOnItemClickListener(mDeviceClickListener);	      // ����	
        
		// ������������״̬
		IntentFilter stateFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		this.registerReceiver(mReceiver, stateFilter);
		
		// �����������ӳɹ�
		IntentFilter connectFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
		this.registerReceiver(mReceiver, connectFilter);
		
		// ������������ʧ��
		IntentFilter disconnectFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		this.registerReceiver(mReceiver, disconnectFilter);
		
		 // ��������
        IntentFilter discoveryFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);    // ����������
        this.registerReceiver(mReceiver, discoveryFilter);                                // ע��㲥

        // �������
        IntentFilter foundFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);   // �������
        this.registerReceiver(mReceiver, foundFilter);                                             // ע��㲥
        
        // ��ȡ��ƥ����豸
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        // �г���ƥ�������豸
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
            	list.add(new DeviceState(device.getName() + "\n" + device.getAddress(), true));
            	mAdapter.notifyDataSetChanged();
        		mListView.setSelection(list.size() - 1);
            }
        } else {
        	mToast.show("û�������豸��ƥ��!", Toast.LENGTH_SHORT);
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
				//��ȡ����
				passWord = ETpassWord.getText().toString().trim();
				if(passWord==null||passWord.equals("")||passWord.length()!=6){
					mToast.show("������6λ����!", Toast.LENGTH_SHORT);
				} else{
					//��ȡ�û��ֻ�����
					phone = UserManager.getPhone();
					if(btConnect.sendOrder("+" + phone + passWord + "-")){                // ��������
						mHandler.sendEmptyMessage(0x005);         // ���ųɹ�
					}else{
						mHandler.sendEmptyMessage(0x006);         // ����ʧ��
					}
				}
			}else{
				mHandler.sendEmptyMessage(0x004);             // δ����
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
        	 
	        DeviceState item = list.get(arg2);          // ��ȡ�����list
	        String info = item.getMsg();
	        final String address = info.substring(info.length() - 17); 
   
	        AlertDialog.Builder StopDialog =new AlertDialog.Builder(mContext);//����һ�����������
	        StopDialog.setTitle("����");//����          
	        StopDialog.setMessage(item.getMsg());
	        StopDialog.setPositiveButton("����", new DialogInterface.OnClickListener() {  
		        public void onClick(DialogInterface dialog, int which) { 
		        	
		        	// ������û������������
		            if (!mBtAdapter.isEnabled()) {
		                mToast.show("���ȿ�������", Toast.LENGTH_SHORT);
		            }else{
		            	 if(btConnect != null && btConnect.getConnect()){
				            	mToast.show("���ȶϿ�����", Toast.LENGTH_SHORT);
				         }else{			        	
				        	bluetoothAddress = address;    // ��������������ַ
				        	editor.putString("BLUETOOTHADDRESS", bluetoothAddress);
				        	editor.commit();
				        	// ��������
					        btConnect = new BluetoothConnect(bluetoothAddress);     
					        btConnect.connect(); 	
					        	
					        mDialog = LoadingDialog.createLoadDialog(mContext, "����Ŭ������...", false);
					        mDialog.show();
					        LoadingDialog.closeDialog(7000, mDialog);
				         }
		            }
      
		         }  
	        });
	        StopDialog.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					
				}
			});        	
	        StopDialog.show();     
        }
    };	
    
    
    // ���������Ĺ㲥
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // ���������豸����
            if (BluetoothDevice.ACTION_FOUND.equals(action)) 
            {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                System.out.println("*****found: " + device);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)   // ��������ûƥ��
                {
                	list.add(new DeviceState(device.getName() + "\n" + device.getAddress(), false));
                	mAdapter.notifyDataSetChanged();
            		mListView.setSelection(list.size() - 1);
                }
            } 
            // ������ɴ���
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) 
            {
            	System.out.println("*****finish ");
                setProgressBarIndeterminateVisibility(false);
                if (mListView.getCount() == 0){
                	mToast.show("�������������豸", Toast.LENGTH_SHORT);
                }
                mHandler.sendEmptyMessage(0x001);        // �������
            }
            // ����״̬�ı䴥��
            else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
            	System.out.println("*****change ");
            	if(!mBtAdapter.isEnabled()){            // ���ر�������	
            		bluetoothAddress = null;
            		if(btConnect != null){
	            		btConnect.shutdownConnection();
            		}
            	}
            }
            // �������ӳɹ�����
            else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
            	System.out.println("*****connect ");
            	mHandler.sendEmptyMessageDelayed(0x002, 4000);     // ��ʱ����, ��֤�ź�
            }
            // �����Ͽ����Ӵ���
            else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
            	System.out.println("*****disconnect ");
            	mHandler.sendEmptyMessage(0x003);
            }
        }
    };	
	
	
	
	// ��Ϣ����
	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			
			case 0x000:
				mToast.show("�뿪������!", Toast.LENGTH_SHORT);
				break;
				
			case 0x001:
				if(mDialog != null){
					mDialog.dismiss();
				}
				mToast.show("�������!", Toast.LENGTH_SHORT);
				isSearching = false;
				break;
				
			case 0x002:
				if(mDialog != null){
					mDialog.dismiss();
				}
				if(btConnect.comfirmDoorID()){
					mToast.show("�������ź�:"+ btConnect.getDoorID(), Toast.LENGTH_SHORT);
				}else{
					mToast.show("�źŲ���ȷ! ���Ź����ѹر�!", Toast.LENGTH_SHORT);
				}
				
				break;
				
			case 0x003:
				mToast.show("�Ͽ�����!", Toast.LENGTH_SHORT);
				break;
				
			case 0x004:
				mToast.show("��������!", Toast.LENGTH_SHORT);
				break;
				
			case 0x005:
				mToast.show("���ųɹ�!", Toast.LENGTH_SHORT);
				mToast.show(phone + "," + passWord, Toast.LENGTH_SHORT);
//				Intent result = new Intent();
//				result.putExtra("a", "b");
//                setResult(DSRESULT_CODE, result);
				finish();
				break;
				
			case 0x006:
				mToast.show("����ʧ��!", Toast.LENGTH_SHORT);
				break;

			default:
				break;
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,DISCONNECT, 0, "�Ͽ�����");
		menu.add(0, SEARCH, 0, "���������豸");
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
				if(mBtAdapter.isEnabled()){                 // �����ѿ�
					list.clear();                           // ����б�
					mAdapter.notifyDataSetChanged();
						
					// �г���ƥ���
					Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
					if (pairedDevices.size() > 0){
					    for (BluetoothDevice device : pairedDevices) {
					        list.add(new DeviceState(device.getName() + "\n" + device.getAddress(), true));
					        mAdapter.notifyDataSetChanged();
					        mListView.setSelection(list.size() - 1);
					    }
					}else{
						mToast.show("û�������豸��ƥ��!", Toast.LENGTH_SHORT);
					 }					
				     /* ��ʼ���� */
				     mBtAdapter.startDiscovery();
					 mDialog = LoadingDialog.createLoadDialog(mContext, "����������...", true);
					 mDialog.show();	
					 isSearching = true;
				}else{
					mHandler.sendEmptyMessage(0x000);
				}
			}else{
				mToast.show("��������...", Toast.LENGTH_SHORT);
			}
			
			break;

		default:
			break;
		}
		
		return true;
	}
	
}