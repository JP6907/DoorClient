package com.example.communityfunction.notice;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.example.communityfunction.adapter.NoticeAdapter;
import com.example.communityfunction.informationclass.Notice;
import com.example.communityfunction.informationclass.NoticeReply;
import com.example.communityfunction.pulltorefreshlistview.view.OnRefreshListener;
import com.example.communityfunction.pulltorefreshlistview.view.RefreshListView;
import com.example.communityfunction.tool.ToolGetNotice;
import com.example.communityfunction.tool.ToolGetNotice.getNoticeSucceedListener;
import com.neo.huikaimen.R;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import neo.door.cache.NoticeCache;
import neo.door.main.MyBaseFragment;
import neo.door.usermanager.UserManager;
/**
 * 小区公告
 * @author TangZH
 *
 */
public class NoticeFragment extends MyBaseFragment implements OnRefreshListener {

	ImageView ivDeleteText;
	EditText etSearch;
	Button btnSearch;
	List<Notice> noticeList;
	List<NoticeReply> replyList;
	RefreshListView noticeListview;
	NoticeAdapter adapter;
	InputMethodManager imm;
	 
	private LinearLayout linearView;
	private View searchBar;
	private boolean isPrepared=false;

	/**
	 * 排除刷新会发生点击 item事件
	 */
	private Boolean isRefreshListview = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_notice, container, false);

		noticeListview = (RefreshListView) view.findViewById(R.id.notice_listview);
		linearView = (LinearLayout) getActivity().findViewById(R.id.liner);
		searchBar = (View) getActivity().findViewById(R.id.search_bar);

		noticeListview.setOnRefreshListener(this);

		showOrGone(linearView, searchBar, noticeListview);
		initListView();
		isPrepared=true;
		loadData();//点击进去刷新
		return view;
	}

	@SuppressWarnings("unchecked")
	private void initListView() 
	{
		
		try 
		{
			noticeList = NoticeCache.readCache();
		} 
		catch (JSONException | IOException e) 
		{
			e.printStackTrace();
		}
		adapter = new NoticeAdapter(getActivity(), noticeList);
		noticeListview.setAdapter(adapter);
		
		noticeListview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{

				//防止点中头布局和脚布局
				if (!isRefreshListview && position > 0 && position!=noticeList.size()+1) 
				{

					Notice notice = noticeList.get(position - 1);
					Intent intent = new Intent(getActivity(), NoticeItemActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("noticeData", notice);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}

		});
	}
	@Override
	
	protected void loadData() 
	{
		if(!isPrepared||!isVisible||!UserManager.isNeedRefreashOnNotice())
		{
			return;
		}
		else
		{
			startRefresh();
			UserManager.setNeedRefreashOnNotice(false);//只刷新一次
		}
	}

	
	@Override
	public void onDownPullRefresh() 
	{
		isRefreshListview = true;
		loadNoticeDatas();
	}

	@Override
	public void onLoadingMore() 
	{
		isRefreshListview = true;

		ToolGetNotice getNotice=new ToolGetNotice(getActivity(),0,noticeList);
		getNotice.execute();
		getNotice.setOnNoticeSucceedListener(new getNoticeSucceedListener() 
		{
			@Override
			public void showNotice(List<Notice> NoticeList) 
			{
				if (NoticeList != null && !NoticeList.isEmpty())
				{
					noticeList=NoticeList;
					adapter.notifyDataSetChanged();//为何这里不行
				}
				
				// 控制脚布局隐藏
				noticeListview.hideFooterView();
				isRefreshListview = false;
			}
		});
	}

	private void startRefresh()
	{
		isRefreshListview = true;
		noticeListview.startRefreshHeaderView();
		loadNoticeDatas();

	}

	private void loadNoticeDatas()
	{
		ToolGetNotice getNotice=new ToolGetNotice(getActivity(),1,noticeList);
		getNotice.execute();
		getNotice.setOnNoticeSucceedListener(new getNoticeSucceedListener() 
		{
			@Override
			public void showNotice(List<Notice> NoticeList) 
			{
				if (NoticeList != null && !NoticeList.isEmpty()) 
				{
					noticeList=NoticeList;
					adapter.notifyDataSetChanged();//为何这里不行
				}
				
				noticeListview.hideHeaderView();
				isRefreshListview = false;
			}
		});
	}


}
