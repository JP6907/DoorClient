package com.example.communityfunction.adapter;

import java.util.List;

import com.neo.huikaimen.R;
import com.example.communityfunction.informationclass.FoodRestaurant;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class FoodRestaurantAdapter extends ArrayAdapter<FoodRestaurant>{
	
	private int resourceId;
	
	 public FoodRestaurantAdapter(Context context, int resource,  List<FoodRestaurant> objects) {
		
		 super(context, resource, objects);
	    		
	 		resourceId = resource;
	 		 
	 	}
	 	
	 	public View getView(int position, View convertView, ViewGroup parent) {
	 		
	 		FoodRestaurant restaurant = (FoodRestaurant) getItem(position);
	 		
	 		View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
	 		
	 		TextView restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
	 		
	 		TextView   address= (TextView) view.findViewById(R.id.address);
	 		
	 		TextView   distance= (TextView) view.findViewById(R.id.distance);
	 	
	 		TextView   evaluateGrade= (TextView) view.findViewById(R.id.evaluate_grade);	 		 		
	 		
	 		RatingBar  evaluate_star = (RatingBar) view .findViewById(R.id.evaluate_star);
	 		
	 		restaurantName.setText(restaurant.getRestaurantName());
	 		
	 		address.setText(restaurant.getAddress());
	 		
	 		distance.setText(restaurant.getDistance());
	 		
	 		evaluateGrade.setText(restaurant.getEvaluateGrade()+"·Ö");
	 		
	 		evaluate_star.setRating(restaurant.getEvaluateStar());
	 		
	 		 		
	 		return view;
	 	}
	 	
	}


