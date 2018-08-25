package com.example.baidumap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.baidumap.MyOrientationListener.OnOrientationListener;
import com.neo.huikaimen.R;

public class MapMainActivity extends Activity implements
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private TextView editCity;
	private EditText editSearchKey;
	private LinearLayout popupMap;
	private ImageButton routeButton;

	/**
	 * �����ؼ������봰��
	 */
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index = 0;

	// ��λ���
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	private GeoCoder geoCoder;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	MapView mMapView;
	BaiduMap mBaiduMap;
	public int count=0;
	// ��������
	private double mCurrentLantitude;
	private double mCurrentLongitude;

	// ��ַ
	private String address = "";
	// UI���
	private ImageButton requestLocButton;
	
	private TextView tv;
	InfoWindow mInfoWindow;
	/**
	 * ���򴫸����ļ�����
	 */
	private MyOrientationListener myOrientationListener;
	/**
	 * ���򴫸���X�����ֵ
	 */
	private int mXDirection;
	protected String cityName;

	// ����·�߹滮
	private String myCityName = "����";
	private String myPlaceName;
	private String goalCityName = "����";
	private String goalPlaceName;
	protected boolean isLongClickMap = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_location);

		initPoiSearch();

		initView();

		initGeoCode();

		initMap();

		initLocation();

		mCurrentMode = LocationMode.NORMAL;
		// �޸�Ϊ�Զ���marker
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));

		initOritationListener();

	}

	private void initView() {

		popupMap = (LinearLayout) LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.map_popup, null);
		tv = (TextView) popupMap.findViewById(R.id.map_popup_tv);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mBaiduMap.hideInfoWindow();
			}
		});
		routeButton = (ImageButton) popupMap.findViewById(R.id.route_plan);
		routeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				getRoutePlan();

			}
		});

		requestLocButton = (ImageButton) findViewById(R.id.imagebutton);
		editCity = (TextView) findViewById(R.id.city);
		editSearchKey = (EditText) findViewById(R.id.searchkey);

		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				// ��λ������λ��
				locateSelf();
			}
		};

		requestLocButton.setOnClickListener(btnClickListener);
	}

	private void initPoiSearch() {
		// ��ʼ������ģ�飬ע�������¼�����
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		keyWorldsView.setAdapter(sugAdapter);

		/**
		 * ������ؼ��ֱ仯ʱ����̬���½����б�
		 */
		keyWorldsView.setSelection(keyWorldsView.getText().toString().length());
		keyWorldsView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				String city = ((TextView) findViewById(R.id.city)).getText()
						.toString();
				/**
				 * ʹ�ý������������ȡ�����б������onSuggestionResult()�и���
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				String city = ((TextView) findViewById(R.id.city)).getText()
						.toString();
				/**
				 * ʹ�ý������������ȡ�����б������onSuggestionResult()�и���
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});

	}

	private void initMap() {
		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);

		// ɾ���ٶȵ�ͼ��logo
		mMapView.removeViewAt(1);

		mBaiduMap = mMapView.getMap();

		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(final LatLng latLng) {
				// ��ȡ��γ��
				double latitude = latLng.latitude;
				double longitude = latLng.longitude;
				isLongClickMap = true;
				// �����ͼ��
				mBaiduMap.clear();
				// ����Maker�����
				LatLng point = new LatLng(latitude, longitude);

				// ����Markerͼ��
				BitmapDescriptor bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.click_locate);
				// ����MarkerOption�������ڵ�ͼ�����Marker
				MarkerOptions options = new MarkerOptions().position(point)
						.icon(bitmap);
				// �ڵ�ͼ�����Marker������ʾ
				mBaiduMap.addOverlay(options);

				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						getAddress(latLng);
						SystemClock.sleep(700);
						return null;
					}

					protected void onPostExecute(Void result) {

						tv.setText(address);

						mInfoWindow = new InfoWindow(popupMap, latLng, -95);
						// ��ʾInfoWindow
						mBaiduMap.showInfoWindow(mInfoWindow);

					}
				}.execute(new Void[] {});

			}
		});

	}

	// ��λ��ʼ��
	private void initLocation() {

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);

	}

	private void locateSelf() {

		LatLng point = new LatLng(mCurrentLantitude, mCurrentLongitude);

		// �����ͼ��
		mBaiduMap.clear();

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
		mBaiduMap.animateMapStatus(u);

	}

	/**
	 * ��ʼ�����򴫸���
	 */
	private void initOritationListener() {
		myOrientationListener = new MyOrientationListener(
				getApplicationContext());
		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {
					@Override
					public void onOrientationChanged(float x) {
						mXDirection = (int) x;

					}
				});
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(mXDirection).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			mCurrentLantitude = location.getLatitude();
			mCurrentLongitude = location.getLongitude();
            
			if(count%15==0)
			{
				count=1;
				getAddress(new LatLng(location.getLatitude(),location.getLongitude()));
			}
			count++;
			mBaiduMap.setMyLocationData(locData);

			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
			//	getAddress(latLng);

				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(
						latLng, mBaiduMap.getMaxZoomLevel() - 3);

				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onStart() {
		// ����ͼ�㶨λ
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocClient.isStarted()) {
			mLocClient.start();
		}
		// �������򴫸���
		myOrientationListener.start();

		super.onStart();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);

		// �رշ��򴫸���
		myOrientationListener.stop();

		mMapView.onDestroy();
		mMapView = null;

		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		super.onDestroy();
	}

	/**
	 * lating ��������
	 */

	private void initGeoCode() {
		// ʵ����һ����������ѯ����
		geoCoder = GeoCoder.newInstance();

		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

				if (isLongClickMap) { 
					// ��ȡ����������ַ
					isLongClickMap = false;
					address = result.getAddress();
					cityName = result.getAddressDetail().city;
					goalCityName = cityName.replace( "��","").trim();
					goalPlaceName = address;
		
				} else {
					myPlaceName = result.getAddress();
					myCityName = result.getAddressDetail().city.replace( "��","").trim();
					editCity.setText(myCityName);
				
				}
			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
			}
		});

	}

	private void getAddress(LatLng latLng) {

		// ���÷��������λ������
		ReverseGeoCodeOption op = new ReverseGeoCodeOption();
		op.location(latLng);
		// ���𷴵����������(��γ��->��ַ��Ϣ)
		geoCoder.reverseGeoCode(op);

	}

	/**
	 * Ӱ��������ť����¼�
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {

		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(editCity.getText().toString())
				.keyword(editSearchKey.getText().toString())
				.pageNum(load_Index));

		final InputMethodManager imm1 = (InputMethodManager) editSearchKey
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		(new Handler()).postDelayed(new Runnable() {
			public void run() {

				imm1.hideSoftInputFromWindow(editSearchKey.getWindowToken(), 0);
			}
		}, 100);

	}

	public void goToNextPage(View v) {
		load_Index++;
		searchButtonProcess(null);
	}

	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(getApplicationContext(), "δ�ҵ����", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			String strInfo = "δ�ҵ����";
			Toast.makeText(getApplicationContext(), strInfo, Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getApplicationContext(), "��Ǹ��δ�ҵ����",
					Toast.LENGTH_SHORT).show();
		} else {

			goalCityName = result.getName();
			goalPlaceName = result.getAddress();
			showAddress(result.getLocation(), goalCityName + ": "
					+ goalPlaceName);

		}
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);

			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
					.poiUid(poi.uid));

			return true;
		}
	}

	/**
	 * 
	 * @param latLng
	 *            ��ַ��Ϣ
	 * @param address
	 *            ����
	 */
	private void showAddress(LatLng latLng, String address) {

		tv.setText(address);

		mInfoWindow = new InfoWindow(popupMap, latLng, -70);
		// ��ʾInfoWindow
		mBaiduMap.showInfoWindow(mInfoWindow);

	}

	public void getRoutePlan() {

		mBaiduMap.hideInfoWindow();

		Intent intent = new Intent(MapMainActivity.this,
				RoutePlanActivity.class);
		intent.putExtra("myCityName", myCityName);
		intent.putExtra("myPlaceName", myPlaceName);
		intent.putExtra("goalCityName", goalCityName);
		intent.putExtra("goalPlaceName", goalPlaceName);
		startActivity(intent);
	}

	@Override
	public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
		// TODO Auto-generated method stub
		
	}

}
