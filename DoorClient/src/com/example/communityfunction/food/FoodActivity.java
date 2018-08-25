package com.example.communityfunction.food;

//店内菜单
import java.util.ArrayList;
import java.util.List;

import neo.door.utils.ChangeStatusBarColor;

import com.neo.huikaimen.R;
import com.example.communityfunction.adapter.FoodAdapter;
import com.example.communityfunction.informationclass.Food;
import com.example.communityfunction.informationclass.FoodRestaurant;
import com.example.communityfunction.myView.NoScrollListView;






import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RatingBar;
import android.widget.TextView;

public class FoodActivity extends Activity{
	
	private List<Food> foodList = new ArrayList<Food>();
	
	
	FoodRestaurant restaurant = null;
	
	 @SuppressLint("ResourceAsColor")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.food_restaurant_food_infomation);
		
		ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
		setRestaurantInformation();

		initListView();
				
	}
  


	private void initListView() {

		FoodAdapter adapter = new FoodAdapter(FoodActivity.this, R.layout.food_information, foodList);
		
		NoScrollListView foodInformationList = (NoScrollListView) findViewById(R.id.food_information_listview);
		
		foodInformationList.setAdapter(adapter);
		
		foodInformationList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				 Food food = foodList.get(position);
				 
				 Intent intent = new Intent(FoodActivity.this,FoodPurchaseActivity.class);
				 intent.putExtra("restaurant", restaurant);
				 intent.putExtra("food", food);
				 startActivity(intent);
				
				
			}
		});
		
	}

	private void setRestaurantInformation() {

		Intent intent = getIntent();
		
	    restaurant = (FoodRestaurant) intent.getSerializableExtra("restaurant_data");
		
		foodList = restaurant.foodList; //饭店里面的菜单？？？？？？为何是一点
		
		
		TextView restaurantName = (TextView) findViewById(R.id.restaurant_name);
 		
 		TextView   address= (TextView) findViewById(R.id.address);
 		
 		TextView   distance= (TextView) findViewById(R.id.distance);
 	
 		TextView   evaluateGrade= (TextView) findViewById(R.id.evaluate_grade);
 		
 		RatingBar  evaluate_star = (RatingBar) findViewById(R.id.evaluate_star);
 		
 		restaurantName.setText(restaurant.getRestaurantName());
 		
 		address.setText(restaurant.getAddress());
 		
 		distance.setText(restaurant.getDistance());
 		
 		evaluateGrade.setText(restaurant.getEvaluateGrade()+"分");
 		
 		evaluate_star.setRating(restaurant.getEvaluateStar());
		
	}


     
}
