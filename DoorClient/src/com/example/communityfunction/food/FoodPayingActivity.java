package com.example.communityfunction.food;



import neo.door.utils.ChangeStatusBarColor;

import com.neo.huikaimen.R;
import com.example.communityfunction.informationclass.Food;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FoodPayingActivity extends Activity{
	
     private Food food ;
   
	 @SuppressLint("ResourceAsColor")
	protected void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);		
			setContentView(R.layout.food_paying);
			ChangeStatusBarColor.setStatusBarColor(this, R.color.theme_color);
			setFoodInformation();			
            initButtonResponse();
	 }

	private void initButtonResponse() {
		
		Button minusButton = (Button) findViewById(R.id.minus);
		
		Button plusButton = (Button) findViewById(R.id.plus);
		
		Button commitButton = (Button) findViewById(R.id.commit_order);
		 
		final EditText numberEditText = (EditText) findViewById(R.id.number_edittext);
	
		final TextView  payMoney = (TextView) findViewById(R.id.pay_sum_money_right);
		
		payMoney.setText( (food.getPrice() * Integer.parseInt(numberEditText.getText().toString()))+"元 ");
		
		numberEditText.setSelection(numberEditText.getText().toString().length());
		numberEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
                    s="";
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
				if(!TextUtils.isEmpty(numberEditText.getText().toString())){
				
				Integer	number =Integer.parseInt(numberEditText.getText().toString());
				payMoney.setText( (food.getPrice() * number)+"元 ");
				
				}else{
					numberEditText.setText("0");
				}
			}
		});
		
		minusButton.setOnClickListener(new OnClickListener() {
			 
			public void onClick(View view) {
			 
				int value = Integer.parseInt(numberEditText.getText().toString());
				double money = 0;
				if( value > 0 )
					value=value-1;
					
					
				 numberEditText.setText(""+value);
				 
				 numberEditText.setSelection(numberEditText.getText().toString().length());
				 
				 money = food.getPrice() * value;
				 
				 payMoney.setText( money + "元 ");
				 
				 
				
			}
		});
		 
		
		plusButton.setOnClickListener(new OnClickListener() {
			 
			public void onClick(View view) {
				
				int value = Integer.parseInt(numberEditText.getText().toString());
				double money = 0;
			
				if( value < 99 )
					value = value+1;
					
				 numberEditText.setText(""+value);
				 
				 numberEditText.setSelection(numberEditText.getText().toString().length());
				 
				 money = food.getPrice() * value;
				 
				 payMoney.setText(money + "元 ");
							
			}
		});
		
		
		commitButton.setOnClickListener(new OnClickListener() {
			
			 
			public void onClick(View view) {
			
				Toast.makeText(getApplicationContext(), "提交订单", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		
	}

	private void setFoodInformation() {
		
		Intent intent = getIntent();
			
	    food = (Food) intent.getSerializableExtra("food");
	    
	    //设置食物信息
	    TextView foodName = (TextView) findViewById(R.id.pay_food_name);
	    
	    TextView foodPrice = (TextView) findViewById(R.id.pay_food_price);
	    
	    foodName.setText(food.getInformation());
	    
	    foodPrice.setText(food.getPrice()+"元");
	    
	    
		
	}
}
