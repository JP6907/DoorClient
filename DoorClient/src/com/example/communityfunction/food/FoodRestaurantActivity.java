package com.example.communityfunction.food;

import java.util.ArrayList;
import java.util.List;

import neo.door.utils.ChangeStatusBarColor;

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
import com.neo.huikaimen.R;
import com.example.communityfunction.adapter.FoodRestaurantAdapter;
import com.example.communityfunction.informationclass.FoodRestaurant;
import com.example.communityfunction.informationclass.FoodSpinnerList;
import com.example.communityfunction.tool.GetRestaurantInformations;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class FoodRestaurantActivity extends Activity {

	private String address = "";
	private GeoCoder geoCoder;
	// 定位相关
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		MapView mMapView;
		BaiduMap mBaiduMap;
		public int count=0;
		// 保持数据
		private double mCurrentLantitude=0;
     	private double mCurrentLongitude=0;
	
		Location location;

	private List<FoodRestaurant> restaurantList = new ArrayList<FoodRestaurant>();
	private Button addressButon;

	LocationListener locationListener;
	protected boolean isRefresh = true;

	@SuppressLint("ResourceAsColor")
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.food_restaurant_information);
		
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);

		initButtonResponse();

		restaurantList = new GetRestaurantInformations().initRestaurants();

		initListView();
	
		// 定位相关
		initLocation();	
		initGeoCode();
	
		// initSpinner();

	}

	private void initLocation() {

		SDKInitializer.initialize(getApplicationContext());
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
	}

	private void getLocationInformation() {

		final LatLng latLng = new LatLng(mCurrentLantitude,mCurrentLongitude);

		addressButon.setText(" 正在刷新...");

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				getAddress(latLng);

				SystemClock.sleep(700);
				return null;
			}

			protected void onPostExecute(Void result) {				
	            if(mCurrentLantitude<0.001&&mCurrentLongitude<0.001)
	            	address = " 刷新失败";
	            if(TextUtils.isEmpty(address.trim()))
	            	address = " 刷新失败";
				
	            addressButon.setText(address);

			}
		}.execute(new Void[] {});
	}


	private void initSpinner() {

		final Spinner spinner1 = (Spinner) findViewById(R.id.select_restaurant);

		List<String> list = FoodSpinnerList.getData();

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.food_spinner_item, list);

		spinner1.setAdapter(adapter1);

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				String s = spinner1.getItemAtPosition(position).toString();

				Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT)
						.show();
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private void initGeoCode() {
		// 实例化一个地理编码查询对象
		geoCoder = GeoCoder.newInstance();

		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				address = result.getAddress();
			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult result1) {

			}
		});

	}

	private void getAddress(LatLng latLng) {

		// 设置反地理编码位置坐标
		ReverseGeoCodeOption op = new ReverseGeoCodeOption();
		op.location(latLng);
		// 发起反地理编码请求(经纬度->地址信息)
		geoCoder.reverseGeoCode(op);

	}

	private void initButtonResponse() {

		//addressButon = (Button) findViewById(R.id.address_button);

		addressButon.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				isRefresh  = true;

			}
		});

	}

	@SuppressLint("ResourceAsColor")
	private void initListView() {
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		FoodRestaurantAdapter adapter = new FoodRestaurantAdapter(
				FoodRestaurantActivity.this,
				R.layout.food_restaurant_item_information, restaurantList);
		ListView restaurantListview = (ListView) findViewById(R.id.restaurant_information_listview);

		restaurantListview.setAdapter(adapter);

		restaurantListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FoodRestaurant restaurant = restaurantList.get(position);
				Intent intent = new Intent(FoodRestaurantActivity.this,
						FoodActivity.class);
				intent.putExtra("restaurant_data", restaurant);
				startActivity(intent);

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!mLocClient.isStarted()) {
			mLocClient.start();
		}
	}

	@Override
	protected void onPause() {
		// 退出时销毁定位
		mLocClient.stop();		
		super.onPause();
	}

	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			if (location == null ) {
				return;
			}		
			mCurrentLantitude = location.getLatitude();
			mCurrentLongitude = location.getLongitude();  
			
			if(isRefresh)
			{
				getLocationInformation();
				isRefresh = false;
			}
		}
		
		
  }
}
