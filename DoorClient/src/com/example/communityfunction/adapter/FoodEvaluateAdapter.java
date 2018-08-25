package com.example.communityfunction.adapter;

import java.util.List;

import com.neo.huikaimen.R;
import com.example.communityfunction.informationclass.FoodEvaluate;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class FoodEvaluateAdapter extends ArrayAdapter<FoodEvaluate>{
	 private int resourceId;
	
	 public FoodEvaluateAdapter(Context context, int resource, List<FoodEvaluate> objects) {
			super(context, resource, objects);
			
			resourceId = resource;
			 
		}
	
		public View getView(int position, View convertView, ViewGroup parent) {
			
			 FoodEvaluate evaluate = getItem(position);
				
			View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
		
			TextView   personName = (TextView) view.findViewById(R.id.name);
			
			TextView   evaluateDate = (TextView) view.findViewById(R.id.date);
			
			TextView   evaluateContent = (TextView) view.findViewById(R.id.evaluate_content);
			
			RatingBar  evaluateGrade =(RatingBar) view.findViewById(R.id.star);
			
			personName.setText(evaluate.getPersonName());
			
			evaluateDate.setText(evaluate.getEvaluateDate());
			
			evaluateContent.setText(evaluate.getEvaluateContent());
			
			evaluateGrade.setRating(evaluate.getEvaluateGrade());
			
			return view;
			
		
		}
			
}
