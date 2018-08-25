package neo.door.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.example.chat.Activity.FriendsReqMsg;
import com.example.chat.Activity.FriendsReqMsg.MsgState;
import com.example.chat.Activity.SearchFriendsActivity;
import com.example.communityfunction.bbs.PostBbsActivity;
import com.example.communityfunction.myView.ActionItem;
import com.example.communityfunction.myView.TitlePopup;
import com.example.communityfunction.myView.TitlePopup.OnItemOnClickListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseSettingsProvider;
import com.hyphenate.util.NetUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.neo.huikaimen.R;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import neo.adapter.MyFragmentPagerAdapter;
import neo.door.cache.FriendCache;
import neo.door.cache.FriendsReqMsgCache;
import neo.door.usermanager.User;
import neo.door.usermanager.UserManager;
import neo.door.utils.ChangeStatusBarColor;
import neo.door.utils.MyToast;


@SuppressLint("ResourceAsColor")
public class MainActivity extends MyBaseFragmentActivity implements OnClickListener {

	// 组件
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView tvTabDoor, tvTabCommunity, tvTabChat;
    private TextView tvOpen, tvChat, tvCommunity;
    private LinearLayout linerOpenDoor, linerChat, linerCommunity;
    private TextView tvTop;			     // 标题
    private ImageView topButton;         // 标题菜单按钮
    private ImageButton topButtonRight;
    private TextView unreadDot;
	private TitlePopup titlePopup;       //弹窗菜单
    private int currIndex = 0;           // 当前界面
    private int offset = 0;              // 动画偏移  
    private int position_one;
    private int position_two;
    
    private final int START_POSTBBS=11;

    private EditText searchFriend;    //在线好友界面的搜索框
    
    private Context mContext;
    
    private Resources resources;
    
    private SlidingMenu slidingMenu;      // 菜单
     
    private MyToast mToast; 
    private ChatFragment chatFragment;
    
    // 保存简单用户信息
    
    private EMMessageListener msgListener;
    private SharedPreferences.Editor editor;

	@SuppressLint("ResourceAsColor")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 设置状态栏
        ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
        
        resources = getResources();
        initEMContactListener();
        initEMMessageListener();
        initSlidingMenu();
        Init();
        InitComponents();
        InitViewPager();
    }
    
    /**
     * 初始化菜单
     */
    private void initSlidingMenu(){
    	slidingMenu = new SlidingMenu(this);
    	slidingMenu.setMode(SlidingMenu.LEFT);       					  // 设置菜单滑出模式
    	slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);       // 设置菜单偏移
    	slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);      // 设置触发模式
    	slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);  // 使SlidingMenu附加在Activity上  
    	slidingMenu.setShadowDrawable(R.color.background_color1);         // 设置边界颜色
    	slidingMenu.setShadowWidthRes(R.dimen.shadow_width);              // 设置边界阴影
    	slidingMenu.setFadeDegree(0.35f);                                 // 设置渐出渐入效果
    	slidingMenu.setMenu(R.layout.slidingmenu_container);              //设置menu的布局文件
    	getSupportFragmentManager().beginTransaction()
			.replace(R.id.slidingmenu_container, new SlidingMenuFragment()).commit();//添加fragment
    	slidingMenu.setOnOpenListener(new OnOpenListener() {               //监听slidingmenu打开
			@Override
			public void onOpen() {
				ObjectAnimator.ofFloat(topButton, "alpha", 1.0f,0.0f)
					.setDuration(400).start();
			}
		});
    	slidingMenu.setOnCloseListener(new OnCloseListener() {              //监听slidingmenu关闭时事件
			@Override
			public void onClose() {
				ObjectAnimator.ofFloat(topButton, "alpha", 0.0f, 1.0f)
				.setDuration(400).start();
			}
		});
    }

    /**
     * 初始化组件
     */
    private void InitComponents() {
    	tvTabDoor = (ImageView) findViewById(R.id.tv_tab_door);
    	//tvTabDoor.setBackgroundColor(resources.getColor(R.color.focus_color));
    	tvTabDoor.setImageResource(R.drawable.open_door_select);
    	tvTabCommunity = (ImageView) findViewById(R.id.tv_tab_community);
    	tvTabChat = (ImageView) findViewById(R.id.tv_tab_chat);
    	
    	unreadDot=(TextView)findViewById(R.id.unread_msg_number);
    	
    	tvOpen=(TextView)findViewById(R.id.tv_open_door);
    	tvOpen.setTextColor(getResources().getColor(R.color.theme_color));
    	tvChat=(TextView)findViewById(R.id.tv_chat);
    	tvCommunity=(TextView)findViewById(R.id.tv_community);

    	linerOpenDoor=(LinearLayout)findViewById(R.id.liner_open_door);
    	linerChat=(LinearLayout)findViewById(R.id.liner_chat);
    	linerCommunity=(LinearLayout)findViewById(R.id.liner_community);
    	
    	linerOpenDoor.setOnClickListener(this);
        linerChat.setOnClickListener(this);
        linerCommunity.setOnClickListener(this);
        
        tvTop = (TextView) findViewById(R.id.topTextView);  
        tvTop.setOnClickListener(this);
        
        topButton = (ImageView)findViewById(R.id.topButton);
        topButton.setOnClickListener(this);
        
    	topButtonRight = (ImageButton) findViewById(R.id.topButtonRight);
    	topButtonRight.setOnClickListener(this);
    	
    	
    	titlePopup = new TitlePopup(getApplicationContext(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titlePopup.addAction(new ActionItem(this, "发贴", R.drawable.title_menulistbtu_compose));  
        
        titlePopup.setItemOnClickListener(new OnItemOnClickListener() 
        {
			@Override
			public void onItemClick(ActionItem item, int position) 
			{
				Intent intent = new Intent(mContext, PostBbsActivity.class);
				startActivityForResult(intent, START_POSTBBS);
			}
		});
        
    }
    
    /**
     * 初始化ViewPager
     */
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();
        Fragment doorFragment = new DoorFragment();
        Fragment homeFragment = new CommunityFragment();
        chatFragment = new ChatFragment();
        
        fragmentsList.add(doorFragment);
        fragmentsList.add(chatFragment);
        fragmentsList.add(homeFragment);
        
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setOffscreenPageLimit(3);           // 同时加载3个fragment 
        
    }

    /** 
     * 初始化
     */
    private void Init() {

    	mContext = getApplicationContext();
    	mToast = new MyToast(mContext);
    	
    	// 初始化动画偏移量, 未使用
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) ((screenW / 3.0) / 2);
       // Log.i("MainActivity", "offset=" + offset);

        position_one = (int) (screenW / 3.0);
        position_two = position_one * 2;
        
      //注册一个监听连接状态的listener(环信)
    	EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }


    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;     // **没使用
//            Log.e("aaa", "d");
//            if(currIndex==1&&searchFriend.hasFocus())
//            	searchFriend.clearFocus();
//            try {
//				if(imm.isActive() && currentFocus==null){
//					Log.e("rrrrr", "d");
//					imm.hideSoftInputFromInputMethod(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//					Log.e("sss111", "d");
//
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            if(currentFocus!=null)
//            {
//            	currentFocus.clearFocus();
//            }
            switch (arg0) {
	            case 0:
	                if (currIndex == 1) { //判断当前处于哪一个，滑动后使这个变成原来的主题色
	                    animation = new TranslateAnimation(position_one, 0, 0, 0);
	                    //tvTabCommunity.setBackgroundColor(resources.getColor(R.color.theme_color));
	                    tvTabChat.setImageResource(R.drawable.chat);
	                    tvChat.setTextColor(R.color.black);
	                } else if (currIndex == 2) {
	                    animation = new TranslateAnimation(position_two, 0, 0, 0);
	                    //tvTabChat.setBackgroundColor(resources.getColor(R.color.theme_color));
	                    tvTabCommunity.setImageResource(R.drawable.community);
	                    tvCommunity.setTextColor(R.color.black);
	                } 
	                tvTop.setText(resources.getString(R.string.open_door));
	                //tvTabDoor.setBackgroundColor(resources.getColor(R.color.focus_color));     
	                tvTabDoor.setImageResource(R.drawable.open_door_select);
	                tvOpen.setTextColor(getResources().getColor(R.color.theme_color));
	                topButtonRight.setVisibility(View.GONE);
	        		// 销毁悬浮窗
	//        		Intent intent0 = new Intent(MainActivity.this, FloatViewService.class);  
	                //终止FloatViewService  
	//                stopService(intent0); 
	                break;
	                       		 
	            case 1:
	                if (currIndex == 0) {
	                    animation = new TranslateAnimation(offset, position_one, 0, 0);
	                    tvTabDoor.setImageResource(R.drawable.open_door);
		                tvOpen.setTextColor(R.color.black);
	                } else if (currIndex == 2) {
	                    animation = new TranslateAnimation(position_two, position_one, 0, 0);
	                    tvTabCommunity.setImageResource(R.drawable.community);
	                    tvCommunity.setTextColor(R.color.black);
	                } 
	                tvTop.setText(resources.getString(R.string.online_friend));
                    tvTabChat.setImageResource(R.drawable.chat_select);
                    tvChat.setTextColor(getResources().getColor(R.color.theme_color));
                    topButtonRight.setVisibility(View.VISIBLE);
                    
//				    Intent intent1 = new Intent(MainActivity.this, FloatViewService.class);  
		               //启动FloatViewService  
//		                startService(intent1);  
	                break;
	            case 2:
	                if (currIndex == 0) {
	                    animation = new TranslateAnimation(offset, position_two, 0, 0);
	                    tvTabDoor.setImageResource(R.drawable.open_door);
		                tvOpen.setTextColor(R.color.black);
		                
	                } else if (currIndex == 1) {
	                    animation = new TranslateAnimation(position_one, position_two, 0, 0);
	                    
	                    tvTabChat.setImageResource(R.drawable.chat);
	                    tvChat.setTextColor(R.color.black);
	                } 
                    tvTabCommunity.setImageResource(R.drawable.community_select);
                    tvCommunity.setTextColor(getResources().getColor(R.color.theme_color));
	                tvTop.setText(R.string.community);
	                topButtonRight.setVisibility(View.VISIBLE);
	        		// 销毁悬浮窗
//	        		Intent intent2 = new Intent(MainActivity.this, FloatViewService.class);  
	                //终止FloatViewService  
//	                stopService(intent2); 
	                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);       // 保留动画结束的状态 
            animation.setDuration(300);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    protected void onResume()
    {
    	refreshUIWithMessage();
    	super.onResume();
    }
    @Override
	protected void onPause() {
		super.onPause();
	}
    
    
    @Override
	protected void onDestroy() {
		// 保存用户信息
		editor = getSharedPreferences("DOOR", MODE_PRIVATE).edit();
		editor.putBoolean("LOGIN", UserManager.getLoginStatu());
		editor.putInt("ID", UserManager.getID());
		editor.putString("NAME", UserManager.getUsername());
		editor.putString("PHONE", UserManager.getPhone());
		editor.putString("BUILDING", UserManager.getBuilding());
		editor.putString("DOORID", UserManager.getDoorID());
		editor.putBoolean("readState", UserManager.getIsReadMsg());
		editor.commit();
		
		EMClient.getInstance().chatManager().removeMessageListener(msgListener);
		super.onDestroy();
	}

    private void initEMContactListener() {
		EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

			@Override
			public void onContactAgreed(String username) {
				// 好友请求被同意
				saveMsg(username, MsgState.BEAGREED, null);
				chatFragment.refreshUnreadDot(true);
				UserManager.setIsReadMsg(false);
				//收到消息声音提示（不设置provider，默认使用环信提供的）
				EaseUI.getInstance().getNotifier().vibrateAndPlayTone(null);
			}

			@Override
			public void onContactRefused(String username) {
				// 好友请求被拒绝
			}

			@Override
			public void onContactInvited(final String username, final String reason) 
			{
				// 收到好友邀请
				runOnUiThread(new Runnable() {
	                public void run() {
	                	saveMsg(username, MsgState.BEINVITEED, reason);
	                	chatFragment.refreshUnreadDot(true);
	    				UserManager.setIsReadMsg(false);
	    				//收到消息声音提示
	    				EaseUI.getInstance().getNotifier().vibrateAndPlayTone(null);
	                }
	            });
			}

			@Override
			public void onContactDeleted(String username) {
				// 被删除时回调此方法
			}

			@Override
			public void onContactAdded(String username) {
				// 增加了联系人时回调此方法
			}
		});
	}
    
    private void initEMMessageListener()
    {
    	msgListener = new EMMessageListener() {
    	     
    	    @Override
    	    public void onMessageReceived(List<EMMessage> messages) {
    	        //收到消息
    	    	 for (EMMessage message : messages) 
    	    	 {
    	    		 EaseUI.getInstance().getNotifier().onNewMsg(message);
    			 }
    	    	 refreshUIWithMessage();
    	    	 
    	    }
    	     
    	    @Override
    	    public void onCmdMessageReceived(List<EMMessage> messages) {
    	        //收到透传消息
    	    }
    	     
    	    @Override
    	    public void onMessageReadAckReceived(List<EMMessage> messages) {
    	        //收到已读回执
    	    }
    	     
    	    @Override
    	    public void onMessageDeliveryAckReceived(List<EMMessage> message) {
    	        //收到已送达回执
    	    }
    	     
    	    @Override
    	    public void onMessageChanged(EMMessage message, Object change) {
    	        //消息状态变动
    	    }
    	};
    	
    	EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }
    
    /**
	 * update unread message count
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadDot.setText(String.valueOf(count));
			unreadDot.setVisibility(View.VISIBLE);
		} else {
			unreadDot.setVisibility(View.INVISIBLE);
		}
	}
	
	public void refreshUIWithMessage() {
		runOnUiThread(new Runnable() {
			public void run() {
				// refresh unread count
				updateUnreadLabel();
				if (currIndex == 1) {
					// refresh conversation list
					if (chatFragment!= null) 
					{
						chatFragment.refresh();
					}
				}
			}
		});
	}
	/**
	 * get unread message count
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
		for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
			if(conversation.getType() == EMConversationType.ChatRoom)
			chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal-chatroomUnreadMsgCount;
	}
	
//	/**
//	 * update the total unread count 
//	 */
//	public void updateUnreadAddressLable() {
//		runOnUiThread(new Runnable() {
//			public void run() {
//				int count = getUnreadAddressCountTotal();
//				if (count > 0) {
//					unreadAddressLable.setVisibility(View.VISIBLE);
//				} else {
//					unreadAddressLable.setVisibility(View.INVISIBLE);
//				}
//			}
//		});
//	}
    
    public void saveMsg(String userName,MsgState msgState,String reason) 
	{
    	UserManager.setIsReadMsg(false);
		try 
		{
			/**
			 * 更新好友通知消息
			 */
			List<FriendsReqMsg>reqMsgList=FriendsReqMsgCache.readCache();
			FriendsReqMsg reqMsg=new FriendsReqMsg();
			if(reason==null)
				reason="加个好友呗";
			reqMsg.setName(userName);
			reqMsg.setReason(reason);
			reqMsg.setState(msgState);
			reqMsgList.add(reqMsg);
			FriendsReqMsgCache.writeCache(reqMsgList);
			/**
			 * 更新好友列表
			 */
			List<User> contactList=FriendCache.readCache();
			User user=new User(userName);
			contactList.add(user);
			user.setImgPath("");
			user.setNickname("");
			FriendCache.writeCache(contactList);
			
		} catch (JSONException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

    
	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		
			case R.id.topButton:
				slidingMenu.toggle();         // 打开menu
				break;
				
			case R.id.liner_open_door:
				mPager.setCurrentItem(0);
				break;
				
			case R.id.liner_chat:
				mPager.setCurrentItem(1);
				break;
				
			case R.id.liner_community:
				mPager.setCurrentItem(2);
				break;		
			case R.id.topButtonRight:
				if(mPager.getCurrentItem()==1)
				{
					Intent intent=new Intent(this,SearchFriendsActivity.class);
					startActivity(intent);
				}
				else if(mPager.getCurrentItem()==2)
					titlePopup.show(view); 
		default:
			break;
		}
	}
	
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:                 // 监听返回键
		 
			if((System.currentTimeMillis() - exitTime) > 2000){
				mToast.show("再按一次退出慧开门", Toast.LENGTH_SHORT);
				exitTime = System.currentTimeMillis();
			}else{
				MainActivity.this.finish();
			}
			return false;                           // 未完全处理事件
	
		default:
			break;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	 
	//实现ConnectionListener接口
	//当掉线时，Android SDK 会自动重连，无需进行任何操作，通过注册连接监听来知道连接状态。
	private class MyConnectionListener implements EMConnectionListener {
	    @Override
	    public void onConnected() {
	    }
	    @Override
	    public void onDisconnected(final int error) {
	        runOnUiThread(new Runnable() {
	 
	            @Override
	            public void run() {
	                if(error == EMError.USER_REMOVED){
	                	mToast.show("账号已被移除", Toast.LENGTH_SHORT);
	                    // 显示帐号已经被移除
	                }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
	                	mToast.show("帐号在其他设备登录", Toast.LENGTH_SHORT);
	                    // 显示帐号在其他设备登录
	                } else {
	                if (NetUtils.hasNetwork(MainActivity.this))
	                	mToast.show("连接不到聊天服务器", Toast.LENGTH_SHORT);
	                    //连接不到聊天服务器
	                else
	                	mToast.show("当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT);
	                    //当前网络不可用，请检查网络设置
	                }
	            }
	        });
	    }
	}

}