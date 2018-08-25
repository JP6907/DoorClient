package com.example.communityfunction.food;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.communityfunction.adapter.FoodRestaurantAdapter;
import com.example.communityfunction.informationclass.FoodRestaurant;
import com.example.communityfunction.informationclass.FoodSpinnerList;
import com.example.communityfunction.pulltorefreshlistview.view.OnRefreshListener;
import com.example.communityfunction.pulltorefreshlistview.view.RefreshListView;
import com.example.communityfunction.tool.GetRestaurantInformations;
import com.neo.huikaimen.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import neo.door.cache.NoticeCache;
import neo.door.main.MyBaseFragment;
import neo.door.utils.ChangeStatusBarColor;

public class FoodFragment extends MyBaseFragment implements OnRefreshListener {
	private String address = "";
	private GeoCoder geoCoder;
	private View view;
	// ��λ���
		LocationClient mLocClient;
	//	public MyLocationListenner myListener = new MyLocationListenner();
		MapView mMapView;
		BaiduMap mBaiduMap;
		public int count=0;
		// ��������
		private double mCurrentLantitude=0;
     	private double mCurrentLongitude=0;
     	private Button addressButon;
     	
		Location location;
		
		private LinearLayout linearView;
		private View searchBar;
		private RefreshListView restaurantListview;  
		private FoodRestaurantAdapter adapter;

	private List<FoodRestaurant> restaurantList = new ArrayList<FoodRestaurant>();
	//private Button addressButon;

	LocationListener locationListener;
	private boolean isRefreshListview = false;

	@SuppressLint("ResourceAsColor")
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater
				.inflate(R.layout.food_fragment, container, false);
		linearView=(LinearLayout)getActivity().findViewById(R.id.liner);
		searchBar=(View)getActivity().findViewById(R.id.search_bar);
		restaurantList = new GetRestaurantInformations().initRestaurants();  //�õ������������Ϣ
	//	initButtonResponse();
		initListView();
		
		// ��λ���
		//initLocation();	
		//initGeoCode();
		showOrGone(linearView,searchBar, restaurantListview);
		return view;
	}
	
	
//	private void initButtonResponse() {
//		addressButon = (Button) view.findViewById(R.id.tem_button);
//		addressButon.setText("123");
//	}
	
	
//	private void initLocation() {
//
//		SDKInitializer.initialize(getApplicationContext());
//		mLocClient = new LocationClient(this);
//		mLocClient.registerLocationListener(myListener);
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);// ��gps
//		option.setCoorType("bd09ll"); // ������������
//		option.setScanSpan(1000);
//		mLocClient.setLocOption(option);
//	}

//	private void getLocationInformation() {
//
//		final LatLng latLng = new LatLng(mCurrentLantitude,mCurrentLongitude);
//
//		//addressButon.setText(" ����ˢ��...");
//
//		new AsyncTask<Void, Void, Void>() {
//
//			@Override
//			protected Void doInBackground(Void... params) {
//
//				getAddress(latLng);
//
//				SystemClock.sleep(700);
//				return null;
//			}
//
//			protected void onPostExecute(Void result) {				
////	            if(mCurrentLantitude<0.001&&mCurrentLongitude<0.001)
////	            	address = " ˢ��ʧ��";
////	            if(TextUtils.isEmpty(address.trim()))
////	            	address = " ˢ��ʧ��";
////				
////	            addressButon.setText(address);
//
//			}
//		}.execute(new Void[] {});
//	}


//	private void initSpinner() {
//
//		final Spinner spinner1 = (Spinner) findViewById(R.id.select_restaurant);
//
//		List<String> list = FoodSpinnerList.getData();
//
//		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
//				R.layout.food_spinner_item, list);
//
//		spinner1.setAdapter(adapter1);
//
//		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				String s = spinner1.getItemAtPosition(position).toString();
//
//				Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT)
//						.show();
//			}
//
//			public void onNothingSelected(AdapterView<?> arg0) {
//
//			}
//		});
//
//	}

//	private void initGeoCode() {
//		// ʵ����һ����������ѯ����
//		geoCoder = GeoCoder.newInstance();
//
//		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
//			@Override
//			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//				address = result.getAddress();
//			}
//
//			@Override
//			public void onGetGeoCodeResult(GeoCodeResult result1) {
//
//			}
//		});
//
//	}

	private void getAddress(LatLng latLng) {

		// ���÷��������λ������
		ReverseGeoCodeOption op = new ReverseGeoCodeOption();
		op.location(latLng);
		// ���𷴵����������(��γ��->��ַ��Ϣ)
		geoCoder.reverseGeoCode(op);

	}

//	private void initButtonResponse() {
//
//		addressButon = (Button) findViewById(R.id.address_button);
//
//		addressButon.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View view) {
//
//				isRefresh  = true;
//
//			}
//		});
//
//	}

	@SuppressLint("ResourceAsColor")
	private void initListView() {
	
		adapter = new FoodRestaurantAdapter(
				getActivity(),
				R.layout.food_restaurant_item_information, restaurantList);
		restaurantListview = (RefreshListView)view.findViewById(R.id.tem_List);

		restaurantListview.setAdapter(adapter);

		restaurantListview.setOnRefreshListener(this);
		
		restaurantListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(!isRefreshListview && position!=restaurantList.size()+1)  //û��ˢ�µ�ʱ��Ϳ��Ե��������һ������
				{
				FoodRestaurant restaurant = restaurantList.get(position-2);
				Intent intent = new Intent(getActivity(),
						FoodActivity.class);
				intent.putExtra("restaurant_data", restaurant);
				startActivity(intent);
				}
			}
		});

	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		if (!mLocClient.isStarted()) {
//			mLocClient.start();
//		}
//	}
//
//	@Override
//	protected void onPause() {
//		// �˳�ʱ���ٶ�λ
//		mLocClient.stop();		
//		super.onPause();
//	}

	
	/**
	 * ��λSDK��������
	 */
//	public class MyLocationListenner implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			
//			if (location == null ) {
//				return;
//			}		
//			mCurrentLantitude = location.getLatitude();
//			mCurrentLongitude = location.getLongitude();  
//			
//			if(isRefreshListview)
//			{
//				getLocationInformation();
//				isRefreshListview = false;
//			}
//		}
//		
//		
//  }


	@Override
	public void onDownPullRefresh() {
		// TODO Auto-generated method stub
		isRefreshListview = true;
		
		 new AsyncTask<Void, Void, Void>() {

	      @Override
	      protected Void doInBackground(Void... params) {
	        SystemClock.sleep(2000);
	        

	   //     noticeList.addAll(new Tool().getNotices());
	        
	        return null;
	      }

	  
	      protected void onPostExecute(Void result) {
	        adapter.notifyDataSetChanged();
	        restaurantListview.hideHeaderView();
	        isRefreshListview = false;
	      }
	    }.execute(new Void[]{});
	}

	@Override
	public void onLoadingMore() {
		// TODO Auto-generated method stub
		isRefreshListview = true;
		  
		    new AsyncTask<Void, Void, Void>() {

		      @Override
		      protected Void doInBackground(Void... params) {
		        SystemClock.sleep(2000);
		        return null;
		      }

		      @Override
		      protected void onPostExecute(Void result) {
		        adapter.notifyDataSetChanged();

		        // ���ƽŲ�������
		       restaurantListview.hideFooterView();
		      
		       isRefreshListview = false;
		      }
		    }.execute(new Void[]{});
	}

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		
	}

}
