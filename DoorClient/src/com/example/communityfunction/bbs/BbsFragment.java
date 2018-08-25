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

//��̳
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
	private TitlePopup titPopup;       //�����˵�
	private ImageButton aTopButtonRight;
	private ViewPager viewPager;
	private ToolGetImage imageLoad;
	private boolean isScroll=false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.fragment_bbs_newlist_layout, container, false);

		initView();
		isPrepared=true;
		loadData();//һ��appˢ��
		return view;
	}
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
	}
	/**
	 * ��ͼ�ɼ��������Ѿ�ʵ����������������viewPager�Ѿ�������������ҳ����ô�Ϳ���ˢ����
	 * ������ж�������viewPager�Ƿ��Ѿ�������������ҳ�棬��ô�����
	 * һ��app�ͼ��ص����
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
			UserManager.setNeedRefreashOnBbs(false);//ֻˢ��һ��
		}
	}
	private void initView() {


		initData();

		viewPager=(ViewPager) getActivity().findViewById(R.id.vPager);
		aTopButtonRight = (ImageButton) getActivity().findViewById(R.id.topButtonRight);

		// ʵ��������������
//		titPopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		titPopup.addAction(new ActionItem(getActivity(), "����", R.drawable.title_menulistbtu_compose));
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
				// �����ݴ�����һ��fragment
				//��ֹ����ͷ���ֺͽŲ���
				if (!isRefreshListview && position > 0 && position!=bbsList.size()+1) {
					Intent intent = new Intent(getActivity().getApplicationContext(), BBSItemActivity.class);
					Bundle bundle = new Bundle();
					// ����2����ΪbbsListViewǰ��������ͷ���֣�һ��������ˢ���õģ�һ�������������һ��item����ס�õġ�
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
//			bbs.setBbsPublishdt(sdf.format(System.currentTimeMillis()));//����ʱ�䣬ˢ�µ�ʱ��Ż���������ΪҪ����ʱ���ȥ
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
	 * ����ˢ��
	 */
	public void onDownPullRefresh() 
	{
		isRefreshListview = true;

		/**
		 * 
		 * ����: time �ĸ�ʽ���� Ӧ��Ϊ 2012-12-12 12:12:00 ��ʽ �˴�ֻ�� 2012-12-12
		 * 
		 * 
		 */
		loadBbsData();
	}

	/**
	 * ������������
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
					//��ʱ��bbsList���������ݣ�����ʹ��addAll��������������ֱ��ʹ��bbsList=bbsListData�Ļ�
					//ָ���µ�ַ������notifyDataSetChanged���������
					List<BBS> list=new ArrayList<BBS>();
					list.addAll(bbsListData);
					bbsList.clear();
					bbsList.addAll(list);
					adapter.notifyDataSetChanged();//�����ַ�ʽˢ���겻��ص���һ��
				}
				// ���ƽŲ�������
				bbsListView.hideFooterView();
				isRefreshListview = false;
			}
		});
	}

	/**
	 * �������ʱˢ���б�����
	 */
	public void startRefresh() 
	{
		isRefreshListview = true;
		bbsListView.startRefreshHeaderView();

		loadBbsData();
	}

	/**
	 * �ӷ�������������
	 * @param time
	 */
	@SuppressWarnings("unchecked")
	private void loadBbsData()
	{
		ToolGetBBS bbsNewPostDataTask=new ToolGetBBS(getActivity(),1,bbsList);
		bbsNewPostDataTask.execute();
		
		bbsNewPostDataTask//ע��ӿ�
		.setOnBbsSucceedListener(new ToolGetBBS.getBbsSucceedListener()
		{
			@Override
			public void refreshBbs(List<BBS> bbsListData) 
			{
				if(bbsListData!=null && !bbsListData.isEmpty())
				{
//					adapter = new BBSAdapter(getActivity(), bbsList,imageLoad,isScroll);
//					bbsListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();//�����ַ�ʽˢ���겻��ص���һ��
				}
				bbsListView.hideHeaderView();
				isRefreshListview = false;
			}
		});
	}
	
	private void initData() 
	{
		// �������������������
//		titlePopup.addAction(new ActionItem(getActivity(), "����", R.drawable.title_menulistbtu_compose));
//		titlePopup.addAction(new ActionItem(getActivity(), "������", R.drawable.title_menulistbtn_receiver_normal));

	}
}
