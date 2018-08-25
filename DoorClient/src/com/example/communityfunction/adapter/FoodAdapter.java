package com.example.communityfunction.adapter;

import java.util.List;

import com.neo.huikaimen.R;
import com.example.communityfunction.informationclass.Food;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodAdapter extends ArrayAdapter<Food>{
     private int resourceId;
	public FoodAdapter(Context context, int resource, List<Food> objects) {
		super(context, resource, objects);
		
		resourceId = resource;
		 
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Food food = getItem(position);
		
		View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
		
		ImageView foodImage = (ImageView) view.findViewById(R.id.food_image);
		
		TextView   foodInformation= (TextView) view.findViewById(R.id.text_information);
		
		TextView   foodCount= (TextView) view.findViewById(R.id.text_count);
	
		TextView   foodPrice= (TextView) view.findViewById(R.id.text_price);
		
		TextView   foodPrePrice= (TextView) view.findViewById(R.id.text_pre_price);
		
		foodPrePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		
		foodPrePrice.setText("£§"+food.getPrePrice());
		
		foodImage.setImageResource(food.getImageId());
		
		foodInformation.setText(food.getInformation());
		
		foodCount.setText("“— €"+food.getCount());
		
		foodPrice.setText("£§"+food.getPrice());
		
		return view;
	}
	
	

}
