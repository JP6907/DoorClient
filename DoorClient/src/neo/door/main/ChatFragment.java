package neo.door.main;

import com.example.chat.Activity.ChatActivity;
import com.example.chat.Activity.ContactsActivity;
import com.example.chat.Activity.FriendsRequestMsgActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment.EaseConversationListItemClickListener;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import neo.adapter.BaseFragment;
import neo.door.usermanager.UserManager;
import neo.door.utils.MyToast;
import neo.door.webutils.ReadAndWriteThread;

public class ChatFragment extends BaseFragment implements OnClickListener{
	
//	private ListView mFriendListView;
	private LinearLayout contacts,information,group,smallCommunity;
	private View view;
	private TextView unreadDot;
//	private static BaseAdapter mFriendAdapter;
//	private static List<Friend> mList;
	public static ReadAndWriteThread rawThread = null;
	private MyToast mToast;
	private boolean isConnected = true;
	private String username;
	public static boolean mIsListViewIdle = true;
	private EaseConversationListFragment  conversationListFragment;
	
	private final static String TAG = "CHATFRAGMENT";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_chat, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
		initView();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		disconnect(username);
	}
	
	@Override
	protected void lazyLoad() {
		Log.i(TAG, "****LazyLoad");
		if(UserManager.getLoginStatu()) {
			username = UserManager.getUsername();
			// 若连接服务器没成功, 重新连接
			if((!isConnected) && !UserManager.getIsConnected()) { 
				rawThread = new ReadAndWriteThread(getActivity(), updateHandler);
				rawThread.start();
				Log.i(TAG, "****restart Thread");
			}
		}else {
			disconnect(username);
//			mList.clear();
//			mList.add(new Friend("群聊", null));
		}
//		mFriendAdapter.notifyDataSetChanged();
	}

    /**
     * 初始化
     */
	protected void init() {	
		
		/***
		 * 环信部分
		 */
	    FragmentManager manager = this.getFragmentManager();
	    FragmentTransaction ft = manager.beginTransaction();
	    conversationListFragment = new EaseConversationListFragment();
	    conversationListFragment.setConversationListItemClickListener(new EaseConversationListItemClickListener() {   
	          @Override
	          public void onListItemClicked(EMConversation conversation) {
	                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
	            }
	        });
		ft.add(R.id.conversation_list, conversationListFragment);
		ft.commit();
		
		
		
		/***
		 * 
		 */
		username = UserManager.getUsername();
//		mList = new ArrayList<Friend>();
//		mList.add(new Friend("群聊", null));
		if(UserManager.getLoginStatu()) {     // 登录后才能连接
			rawThread = new ReadAndWriteThread(getActivity(), updateHandler);
			rawThread.start();
			System.out.println("****startThread");
		}else {
			isConnected = false;
		}
		mToast = new MyToast(getActivity());
//		mFriendListView = (ListView)getActivity().findViewById(R.id.lv_chat);	
//		mFriendAdapter = new FriendBaseAdapter(mList, getActivity());
//		mFriendListView.setAdapter(mFriendAdapter);
//		
//		mFriendListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long arg3) {
//				mList.get(position).setMsg(false);
//				mFriendAdapter.notifyDataSetChanged();
//				Intent intent = new Intent(getActivity(), ChatList.class);
//				intent.putExtra("FRIEND", mList.get(position).getName());
//				getActivity().startActivityForResult(intent, 0);
//			}
//		});
//		mFriendListView.setOnScrollListener(new OnScrollListener() {		
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				// 设置listview滚动时不加载图片, 实现优化
//				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE) {        
//					mIsListViewIdle = true;
//					mFriendAdapter.notifyDataSetChanged();
//				}else {
//					mIsListViewIdle = false;
//				}
//			}	
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				// ingore
//			}
//		});
		
	}
	
	public void initView()
	{
		contacts = (LinearLayout)view.findViewById(R.id.contacts);
		information = (LinearLayout)view.findViewById(R.id.information);
		group = (LinearLayout)view.findViewById(R.id.group);
		smallCommunity = (LinearLayout)view.findViewById(R.id.small_community);
		unreadDot=(TextView)view.findViewById(R.id.friends_request_unread_dot);
		
		contacts.setOnClickListener(this);
		information.setOnClickListener(this);
		group.setOnClickListener(this);
		smallCommunity.setOnClickListener(this);
		
		if(!UserManager.getIsReadMsg())
			unreadDot.setVisibility(View.VISIBLE);
	}
	/**
	 * 刷新聊天列表
	 */
	public void refresh()
	{
		conversationListFragment.refresh();
	}
	
	public void refreshUnreadDot(boolean isRead)
	{
		unreadDot.setVisibility(isRead ? View.VISIBLE:View.INVISIBLE);
	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.contacts:
			Intent intent=new Intent(getActivity(),ContactsActivity.class);
			startActivity(intent);
			break;
		case R.id.information:
			Intent intent2=new Intent(getActivity(),FriendsRequestMsgActivity.class);
			startActivity(intent2);
			UserManager.setIsReadMsg(true);
			unreadDot.setVisibility(View.INVISIBLE);
			break;
		case R.id.group:
			Toast.makeText(getActivity(), "请期待", Toast.LENGTH_LONG).show();
			break;
		case R.id.small_community:
			Toast.makeText(getActivity(), "请期待", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
		Log.e("###", ""+requestCode);
		if(data != null) {
			// 把消息提醒取消
			String friendName = data.getStringExtra("FRIEND");
			System.out.println("****3"+friendName);
//			for(int i=0; i<mList.size(); i++) {
//				if(mList.get(i).getName().equals(friendName)) {
//					mList.get(i).setMsg(false);
//					mFriendAdapter.notifyDataSetChanged();
//				}
//			}
		}
	}
	
	
	@SuppressLint("HandlerLeak")
	public Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
//			case 0x001:
//				for(int i=0; i<mList.size(); i++) {
//					if(mList.get(i).getName().equals(msg.obj.toString())) {
//						mList.get(i).setMsg(true);
//					}
//				}
//				mFriendAdapter.notifyDataSetChanged();
//				break;
//			
//			case 0x002:	
//				Friend friend = (Friend)msg.obj;
//				mList.add(friend);
//				mFriendAdapter.notifyDataSetChanged();
//				break;
//				
//			case 0x003:
//				String qName = msg.obj.toString();
//				for(int i=0; i < mList.size(); i ++){
//					if(mList.get(i).getName().equals(qName)){
//						mList.remove(i);         // 移除用户						
//						break;
//					}
//				}
//				mFriendAdapter.notifyDataSetChanged();
//				break;
				
			case 0x004:
				mToast.show("咦?网络开小差了?!", Toast.LENGTH_SHORT);
				// 不要加break;
			case 0x014:
				isConnected = false;
				break;

			default:
				break;
			}
		};
	};
	
	
	private void disconnect(String username) {
		if(UserManager.getIsConnected()) {
			System.out.println("****logout");
			Message msg = new Message();
			msg.what = 0x002;
			msg.obj = username;
			rawThread.sendHandler.sendMessage(msg);
			rawThread.interrupt();
			rawThread = null;
			UserManager.setIsConnected(false);
			isConnected = false;
		}
	}

}
