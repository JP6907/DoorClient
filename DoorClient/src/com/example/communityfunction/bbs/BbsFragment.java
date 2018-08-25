package com.example.communityfunction.bbs;

import java.util.ArrayList;
import java.util.List;

import com.example.communityfunction.adapter.BBSAdapter;
import com.example.communityfunction.informationclass.BBS;
import com.example.communityfunction.myView.ActionItem;
import com.example.communityfunction.myView.TitlePopup;
import com.example.communityfunction.myView.TitlePopup.OnItemOnClickListener;
import com.example.communityfunction.pulltorefreshlistview.view.OnRefreshListener;
import com.example.communityfunction.pulltorefreshlistview.view.RefreshListView;
import com.example.communityfunction.tool.ToolGetBBS;
import com.example.communityfunction.tool.ToolGetImage;
import com.example.communityfunction.tool.ToolPostBBS;
import com.example.communityfunction.tool.ToolPostBBS.OnPostBbsListener;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import neo.door.cache.BBSCache;
import neo.door.main.MyBaseFragment;
import neo.door.usermanager.UserManager;

//论坛
public class BbsFragment extends MyBaseFragment implements OnRefreshListener {

	private final int START_POSTBBS=11;
	
	private boolean isRefreshListview = false;
	private boolean isPrepared=false;
	private LinearLayout linearView;
	private View view;
	private View searchBar;
	private RefreshListView bbsListView;
	private BBSAdapter adapter;
	private List<BBS> bbsList;
	private TitlePopup titPopup;       //弹窗菜单
	private ImageButton aTopButtonRight;
	private ViewPager viewPager;
	private ToolGetImage imageLoad;
	private boolean isScroll=false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.fragment_bbs_newlist_layout, container, false);

		initView();
		isPrepared=true;
		loadData();//一打开app刷新
		return view;
	}
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
	}
	/**
	 * 视图可见，并且已经实例化，并且最外层的viewPager已经滑动到第三个页面那么就可以刷新了
	 * 如果不判断最外层的viewPager是否已经滑动到第三个页面，那么会出现
	 * 一打开app就加载的情况
	 */
	@Override
	protected void loadData() 
	{
		if(!isPrepared||!isVisible||!UserManager.isNeedRefreashOnBbs())
		{
			return;
		}
		else
		{
			startRefresh();
			UserManager.setNeedRefreashOnBbs(false);//只刷新一次
		}
	}
	private void initView() {


		initData();

		viewPager=(ViewPager) getActivity().findViewById(R.id.vPager);
		aTopButtonRight = (ImageButton) getActivity().findViewById(R.id.topButtonRight);

		// 实例化标题栏弹窗
//		titPopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		titPopup.addAction(new ActionItem(getActivity(), "发贴", R.drawable.title_menulistbtu_compose));
//
//		titPopup.setItemOnClickListener(new OnItemOnClickListener() {
//
//			@Override
//			public void onItemClick(ActionItem item, int position) 
//			{
//				Intent intent = new Intent(getActivity(), PostBbsActivity.class);
//				getActivity().startActivityForResult(intent, START_POSTBBS);
//				
//			}
////		});
//		aTopButtonRight.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				titPopup.show(v);
//			}
//		});

		linearView = (LinearLayout) getActivity().findViewById(R.id.liner);
		searchBar = (View) getActivity().findViewById(R.id.search_bar);

		bbsListView = (RefreshListView) view.findViewById(R.id.bbs_listview);
		bbsList = BBSCache.read();

	    imageLoad=ToolGetImage.build();
	    adapter = new BBSAdapter(getActivity(), bbsList,imageLoad,isScroll);

		bbsListView.setAdapter(adapter);
		bbsListView.setOnRefreshListener(this);


		bbsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 将数据传给下一个fragment
				//防止点中头布局和脚布局
				if (!isRefreshListview && position > 0 && position!=bbsList.size()+1) {
					Intent intent = new Intent(getActivity().getApplicationContext(), BBSItemActivity.class);
					Bundle bundle = new Bundle();
					// 减掉2是因为bbsListView前面有两个头布局，一个是用来刷新用的，一个是用来避免第一个item被遮住用的。
					bundle.putSerializable("bbsData", bbsList.get(position - 1));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}

		});
//		showOrGone(linearView, searchBar,bbsListView);
		
	}
	
	@SuppressWarnings("unchecked")
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==11)
		{
//			BBS bbs=new BBS();
//			bbs.setBbsPhone(data.getStringExtra("userPhone"));
//			if (!data.getStringArrayListExtra("imageSelectedPath").isEmpty())
//				bbs.setBbsPictureList(data.getStringArrayListExtra("imageSelectedPath"));
//			bbs.setBbsPublisher(data.getStringExtra("userName"));
//			bbs.setBbsText(data.getStringExtra("bbsContent"));
//			bbs.setBbsTitle(data.getStringExtra("bbsTitle"));
//			
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			
//			bbs.setBbsPublishdt(sdf.format(System.currentTimeMillis()));//设置时间，刷新的时候才会正常，因为要发送时间过去
//			
//			bbs.setPostJustNow(true);
//			adapter.add(0,bbs);
			ToolPostBBS postBBSTask=new ToolPostBBS(getActivity());
			postBBSTask.execute(data.getStringExtra("userName") ,
					data.getStringExtra("userPhone"), data.getStringExtra("bbsTitle"),
					data.getStringExtra("bbsContent"),data.getStringArrayListExtra("imageSelectedPath"));
			
			postBBSTask.setOnPostBbsListener(new OnPostBbsListener() 
			{
				@Override
				public void getPostBbs() 
				{
					startRefresh();
				}
			});
		}
	}

	/**
	 * 下拉刷新
	 */
	public void onDownPullRefresh() 
	{
		isRefreshListview = true;

		/**
		 * 
		 * 修正: time 的格式不对 应该为 2012-12-12 12:12:00 格式 此处只有 2012-12-12
		 * 
		 * 
		 */
		loadBbsData();
	}

	/**
	 * 上拉加载数据
	 */
	public void onLoadingMore()
	{

		isRefreshListview = true;
		
		ToolGetBBS bbsNewPostDataTask=new ToolGetBBS(getActivity(),0,bbsList);
		bbsNewPostDataTask.execute();
		
		bbsNewPostDataTask
		.setOnBbsSucceedListener(new ToolGetBBS.getBbsSucceedListener()
		{
			@Override
			public void refreshBbs(List<BBS> bbsListData) 
			{
				if(bbsListData!=null && !bbsListData.isEmpty())
				{
					//此时的bbsList若是有内容，必须使用addAll方法，否则若是直接使用bbsList=bbsListData的话
					//指向新地址，调用notifyDataSetChanged并不会更新
					List<BBS> list=new ArrayList<BBS>();
					list.addAll(bbsListData);
					bbsList.clear();
					bbsList.addAll(list);
					adapter.notifyDataSetChanged();//用这种方式刷新完不会回到第一项
				}
				// 控制脚布局隐藏
				bbsListView.hideFooterView();
				isRefreshListview = false;
			}
		});
	}

	/**
	 * 进入界面时刷新列表数据
	 */
	public void startRefresh() 
	{
		isRefreshListview = true;
		bbsListView.startRefreshHeaderView();

		loadBbsData();
	}

	/**
	 * 从服务器下载帖子
	 * @param time
	 */
	@SuppressWarnings("unchecked")
	private void loadBbsData()
	{
		ToolGetBBS bbsNewPostDataTask=new ToolGetBBS(getActivity(),1,bbsList);
		bbsNewPostDataTask.execute();
		
		bbsNewPostDataTask//注册接口
		.setOnBbsSucceedListener(new ToolGetBBS.getBbsSucceedListener()
		{
			@Override
			public void refreshBbs(List<BBS> bbsListData) 
			{
				if(bbsListData!=null && !bbsListData.isEmpty())
				{
//					adapter = new BBSAdapter(getActivity(), bbsList,imageLoad,isScroll);
//					bbsListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();//用这种方式刷新完不会回到第一项
				}
				bbsListView.hideHeaderView();
				isRefreshListview = false;
			}
		});
	}
	
	private void initData() 
	{
		// 给标题栏弹窗添加子类
//		titlePopup.addAction(new ActionItem(getActivity(), "发贴", R.drawable.title_menulistbtu_compose));
//		titlePopup.addAction(new ActionItem(getActivity(), "。。。", R.drawable.title_menulistbtn_receiver_normal));

	}
}
