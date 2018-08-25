package com.example.communityfunction.food;

import java.util.List;

import neo.door.utils.ChangeStatusBarColor;
import com.neo.huikaimen.R;
import com.example.communityfunction.adapter.FoodEvaluateAdapter;
import com.example.communityfunction.informationclass.Food;
import com.example.communityfunction.informationclass.FoodEvaluate;
import com.example.communityfunction.informationclass.FoodRestaurant;






import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FoodPurchaseActivity extends Activity{
  
	private List<FoodEvaluate> evaluateList ;
	
	private Food food ;
	
	@SuppressLint("ResourceAsColor")
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//分享
//		 ShareSDK.initSDK(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.food_purchase);
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		setRestaurantInformation();
		initListView();
		
		initButtonResponse();
		
  }
	
	 private void initButtonResponse() {
	      //购买按钮响应
	      Button purchaseButton = (Button) findViewById(R.id.purchase_button);
	      
	      purchaseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 
				 Intent intent = new Intent(FoodPurchaseActivity.this,FoodPayingActivity.class);
				 intent.putExtra("food",food);
				 startActivity(intent);		
			}
		});
		
	      
	}

	private void initListView() {

		//设置食客评价区域 
      FoodEvaluateAdapter adapter = new FoodEvaluateAdapter(FoodPurchaseActivity.this, R.layout.food_purchase_evaluate, evaluateList);
		
      ListView evaluateListView = (ListView) findViewById(R.id.evaluate_listview);
      
      evaluateListView.setAdapter(adapter);
      
      //setListViewHeightBasedOnChildren( evaluateListView);
      
	}
	 /*** 
	     * 动态设置listview的高度 
	     *  
	     * @param listView 
	     */  
	    public void setListViewHeightBasedOnChildren(ListView listView) {  
	        ListAdapter listAdapter = listView.getAdapter();  
	        if (listAdapter == null) {  
	            return;  
	        }  
	        int totalHeight = 0;  
	        for (int i = 0; i < listAdapter.getCount(); i++) {  
	            View listItem = listAdapter.getView(i, null, listView);  
	            listItem.measure(0, 0);  //在还没有构建View 之前无法取得View的度宽。 在此之前我们必须选 measure 一下. 

	            totalHeight += listItem.getMeasuredHeight();  
	        }  
	        ViewGroup.LayoutParams params = listView.getLayoutParams();  
	        params.height = totalHeight  
	                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
	        // params.height += 5;// if without this statement,the listview will be  
	        // a  
	        // little short  
	        // listView.getDividerHeight()获取子项间分隔符占用的高度  
	        // params.height最后得到整个ListView完整显示需要的高度  
	        listView.setLayoutParams(params);  
	    }
	private void setRestaurantInformation() {
		
		 Intent intent = getIntent();
			
	     food = (Food) intent.getSerializableExtra("food");
	     
	     final FoodRestaurant restaurant = (FoodRestaurant) intent.getSerializableExtra("restaurant");
	     
	     evaluateList = restaurant.evaluateList;
	     
	     //购买详情布局上部分
	     
	     ImageView foodImage = (ImageView) findViewById(R.id.food_image);
	     
	     TextView restaurantName = (TextView) findViewById(R.id.brief_information_above);
	    
	     TextView foodText = (TextView) findViewById(R.id.brief_information_below);
	     
	     foodImage.setImageResource(food.getImageId());
	     
	     foodText.setText(food.getInformation());
	     
	     restaurantName.setText(restaurant.getRestaurantName());
	     
	     //食物价格和销售量
	     
	     TextView foodPrice = (TextView) findViewById(R.id.food_price);
		    
	     TextView foodPrePrice = (TextView) findViewById(R.id.food_pre_price);
	     
	     TextView purchasedNumber = (TextView) findViewById(R.id.purchased_number);
	     
	     foodPrice.setText(food.getPrice()+"元");
	     
	     //加删除线
	     foodPrePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);	     
	     foodPrePrice.setText(food.getPrePrice()+"元");
	     
		  purchasedNumber.setText("已售"+food.getCount());      
	     
	     //商家信息
	     TextView phoneNumber = (TextView) findViewById(R.id.phonenumber);
	     
	     TextView detailAddress = (TextView) findViewById(R.id.detail_address);
	     
	     ImageButton dailButton = (ImageButton) findViewById(R.id.dial_button);
	     
	     dailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 Intent intent = new Intent(Intent.ACTION_DIAL);
				 intent.setData(Uri.parse("tel:"+restaurant.getPhoneNumber()));
				 startActivity(intent);
				
			}
		});
	     
	     phoneNumber.setText("联系电话： "+restaurant.getPhoneNumber());
	     
	     detailAddress.setText("地址： "+restaurant.getAddress());
	     
	   
	     //购买须知
	     TextView  purchaseDetail = (TextView) findViewById(R.id.description);
	           
	     purchaseDetail.setText(restaurant.getPurchaseDetail());
	     

	}
      

}
