package com.example.communityfunction.housekeeping;

import java.util.ArrayList;
import java.util.List;

import com.example.communityfunction.fix.CompanyDetail;
import com.example.communityfunction.pulltorefreshlistview.view.OnRefreshListener;
import com.neo.huikaimen.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import neo.adapter.MyCompanyBaseAdapter;
import neo.company.data.CompanyData;
import neo.company.data.CompanyDataManager;
import neo.door.main.MyBaseFragment;
import neo.tools.pulltorefresh.PullableListView;

public class HouseFragment extends MyBaseFragment implements OnRefreshListener{
	
	private PullableListView pListView;          // �Զ���listview
	private MyCompanyBaseAdapter adapter;        // ������
	private View view;
	private boolean isRefreshListview = false;
	private LinearLayout linearView;
	private View searchBar;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		linearView=(LinearLayout)getActivity().findViewById(R.id.liner);
		searchBar=(View)getActivity().findViewById(R.id.search_bar);
		view = inflater.inflate(R.layout.activity_refresh_company_listview, container, false);
		pListView = (PullableListView)view.findViewById(R.id.listview_content);
		pListView.setCanPullDown(false);                    // ���ò�������ˢ��
		pListView.enableAutoLoad(false);                    // �������Զ�����
		
		initListView();      								// ��ʼ��listView
		pListView.setOnLoadListener(this);   
		
		final View headView=new View(getActivity());
		headView.post(new Runnable() {
			
			@Override
			public void run() 
			{
				headView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
						linearView.getHeight()+searchBar.getHeight()));
				pListView.addHeaderView(headView);
			}
		});
		
		showOrGone(linearView, searchBar, pListView);
		 return view;
	}


	private void initListView() {
		// TODO Auto-generated method stub
		// ��������
		
		List<CompanyData> companyDatas = new ArrayList<CompanyData>();
		companyDatas = CompanyDataManager.createHousekeepingCompanyData();
		adapter = new MyCompanyBaseAdapter(getActivity(), companyDatas);           // ����������
		pListView.setAdapter(adapter);                             // ����������
		
		
		
		// ����ListView
		pListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) {
				if(!isRefreshListview && position > 0){
					// ������ת
					Bundle bundle = new Bundle();
					bundle.putSerializable("DATE", adapter.getItem(position - 1));
					Intent intent = new Intent(getActivity(),CompanyDetail.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}

			}
		});
		
	}

	@Override
	public void onDownPullRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingMore() {
		// TODO Auto-generated method stub
		new Handler(){
			@Override
			public void handleMessage(Message msg) {
				
				if(adapter.getCount() > 20){           // �������
					pListView.setHasMoreData(false);
					Toast.makeText(getActivity(), "�Ѿ�������", Toast.LENGTH_SHORT).show();
					return;
				}
				
				List<CompanyData> newItems = new ArrayList<CompanyData>();
				newItems = CompanyDataManager.createHousekeepingCompanyData();
				
				for(int i=0; i<newItems.size(); i++){
					adapter.addItem(newItems.get(i));
				}
				adapter.notifyDataSetChanged();
			//	pullableListView.finishLoading();       // �������
				pListView.setHasMoreData(true);
			}
		}.sendEmptyMessageDelayed(0, 2000);
	}
	

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
	}


}
