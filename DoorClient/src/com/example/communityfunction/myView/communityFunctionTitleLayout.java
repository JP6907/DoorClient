package com.example.communityfunction.myView;

import com.neo.huikaimen.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class communityFunctionTitleLayout extends LinearLayout{
      
	
	public communityFunctionTitleLayout(final Context context, AttributeSet attrs) {
		
		super(context, attrs);

	
		LayoutInflater.from(context).inflate(R.layout.community_function_title, this);
	 
	
		ImageButton back = (ImageButton) findViewById(R.id.back);
	 
		back.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View view) {
		
//			FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
//    		fragmentManager.popBackStack();
    		((Activity) context).finish();
		}
	});
	 
	  
	}

}
